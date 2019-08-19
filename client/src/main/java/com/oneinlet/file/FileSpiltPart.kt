package com.oneinlet.file

import com.github.benmanes.caffeine.cache.Caffeine
import com.oneinlet.common.bean.FileAction
import com.oneinlet.common.bean.Message
import org.apache.commons.codec.digest.DigestUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

/**
 *  处理文件分割服务，采用多线程，采用队列
 *
 *  每次从队列取出最多三个文件，去执行，这三个文件对象放在缓存里
 * */
class FileSpiltPart {


    private val logger = LoggerFactory.getLogger("FileSpiltPart")


    private val fileCache = Caffeine.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .maximumSize(3)
            .build<String, RandomAccessFile>()

    private val singlePackage = 1048576 // 1MB

    init {
    }

    /***
     *   从队列中获取任务对象，添加到缓存
     * */
    fun takeQueue() {
    }

    /***
     *   根据当前的传输进度，将文件拆分组装成Message对象
     * */
    fun getRemainPartStreamMessage(message: Message): Message {
        if (message.indexPackage!! > message.totalPackage!!) {
            throw RuntimeException("当前的索引包数大于总包数，任务已经执行完毕，message：$message")
        }

        val randomAccessFile = fileCache.getIfPresent(message.filePath!!)
                ?: throw RuntimeException("本地缓存的randomAccessFile文件丢失，message：$message")
        var readIndex = 0L
        if (message.indexPackage == 0) {
            randomAccessFile.seek(0)
        } else {
            readIndex = singlePackage.toLong().times(message.indexPackage!!)
            //标记起点位置
            randomAccessFile.seek(readIndex)
        }

        // 剩余的文件长度,readIndex的长度还有一种方式是调用randomAccessFile.getFilePointer()，调用底层方法更加耗损性能，我们在这里直接缓存计算
        val remainReadLength = randomAccessFile.length() - readIndex

        // 如果剩余长度小于，说明文件已经传输完成，那么则停止
        if (remainReadLength <= 0) {
            throw RuntimeException("剩余读取的长度为小于等于0，本次文件已经传输完毕，message：$message")
        }

        var sendByteLength = singlePackage
        if (remainReadLength < singlePackage) {
            sendByteLength = remainReadLength.toInt()
        }

        // nio读取文件
        val buffer = ByteBuffer.allocate(sendByteLength)
        if (randomAccessFile.channel.read(buffer) > 0) {
            message.fileData = buffer.array()
            return message
        }
        fileCache.invalidate(message.filePath!!)
        randomAccessFile.close()
        logger.info("文件已经读完--------$message")
        return message
    }

    fun initMessage(path1: String, fileAction: FileAction): Message {
        val path = "/home/yancheng/Downloads/Shadowsocks-4.1.7.1.zip"
        val file = File(path)
        val message = Message()
        message.filePath = path
        message.fileName = file.name
        message.fileAction = fileAction

        if (fileAction == FileAction.MAKE_FILE || fileAction == FileAction.UPDATE_FILE) {

            message.md5 = DigestUtils.md5Hex(FileInputStream(file))
            message.indexPackage = 0 //初始化为0，从1开始计数 直到最后一个包
            var totalPackage = file.totalSpace.div(singlePackage).toInt()
            if (file.totalSpace.rem(singlePackage) != 0L) {
                totalPackage++
            }
            message.totalPackage = totalPackage
            //初始化的文件传输任务，需要放进缓存，下次不需要再次去new RandomAccessFile对象，直接复用
            fileCache.put(message.filePath!!, RandomAccessFile(file, "r"))
        }
        return message
    }


    fun receivePartStream(message: Message): Message {
        message.indexPackage = message.indexPackage?.plus(1)
        return message
    }

}