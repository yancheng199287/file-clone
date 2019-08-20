package com.oneinlet.common.file

import com.github.benmanes.caffeine.cache.Caffeine
import com.oneinlet.common.bean.FileAction
import com.oneinlet.common.bean.Message
import com.oneinlet.common.bean.TransferStatus
import org.apache.commons.codec.digest.DigestUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
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
object FileSpiltPart {

    private val logger = LoggerFactory.getLogger("FileSpiltPart")

    private val readFileCache = Caffeine.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .maximumSize(3)
            .build<String, FileChannel>()

    private val writeFileCache = Caffeine.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .maximumSize(3)
            .build<String, FileChannel>()

    private val singlePackage = 1048576 // 1MB

    /***
     *   根据当前的传输进度，将文件拆分组装成Message对象
     * */
    fun getRemainPartStreamMessage(message: Message): Message {
        //对方在获取索引包数，如果indexPackage=3 表示对方要第三个包，如果这个包等于总包，就是越界了
        if (message.indexPackage!! >= message.totalPackage!!) {
            throw RuntimeException("当前的索引包数大于总包数，任务已经执行完毕，message：$message")
        }
        val fileChannel = readFileCache.getIfPresent(message.md5!!)
                ?: throw RuntimeException("本地缓存的randomAccessFile文件丢失，message：$message")
        val readIndex = singlePackage.toLong().times(message.indexPackage!!)

        // 剩余的文件长度,readIndex的长度还有一种方式是调用randomAccessFile.getFilePointer()，调用底层方法更加耗损性能，我们在这里直接缓存计算

        val remainReadLength = fileChannel.size() - readIndex

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

        val result = fileChannel.read(buffer)

        // 如果当前读取的位置已经到达文件尾部，或者读取的容量小于0，result是读取的可用字节数，那么意味文件传输完毕
        if (fileChannel.position() == fileChannel.size() || result <= 0) {
            message.transferStatus = TransferStatus.SEND_FINISHED
            readFileCache.invalidate(message.md5!!)
            fileChannel.close()
            logger.info("文件已经读完-------")
        }
        message.fileData = buffer.array()
        logger.info("读取文件的状态.... $message")
        return message
    }

    fun initMessage(path: String, fileAction: FileAction): Message {
        val file = File(path)
        val message = Message()
        message.filePath = path
        message.fileName = file.name
        message.fileAction = fileAction

        if (fileAction == FileAction.MAKE_FILE || fileAction == FileAction.UPDATE_FILE) {
            message.md5 = DigestUtils.md5Hex(FileInputStream(file))
            message.indexPackage = 0 //初始化为0，从1开始计数 直到最后一个包
            var totalPackage = file.length().div(singlePackage).toInt()
            if (file.totalSpace.rem(singlePackage) != 0L) {
                totalPackage++
            }
            message.totalPackage = totalPackage
            message.transferStatus = TransferStatus.SENDING
            //初始化的文件传输任务，需要放进缓存，下次不需要再次去new RandomAccessFile对象，直接复用
            readFileCache.put(message.md5!!, FileInputStream(file).channel)
        }
        return message
    }


    fun receivePartStream(message: Message): Message {
        val serverPath = "/home/yancheng/temp/server-base"
        var fileChannel = writeFileCache.getIfPresent(message.md5!!)
        //如果缓存没有取到，只能是第一次文件传输过来
        if (fileChannel == null) {
            val file = File(serverPath + File.separator + message.fileName)
            val fileOutputStream = FileOutputStream(file)
            fileChannel = fileOutputStream.channel
            fileChannel.force(true)
            writeFileCache.put(message.md5!!, fileChannel)
        }
        val readIndex = singlePackage.toLong().times(message.indexPackage!!)

        //在这里可以打印监控接收文件进度
        logger.info("当前正在接收文件:{},已经接收数据:{},当前接收数据：{}", message.fileName, fileChannel!!.size(), message.fileData!!.size)

        val buffer = ByteBuffer.wrap(message.fileData)
        fileChannel!!.write(buffer, readIndex)

        //如果当前的第几包数大于了或者等于是最后的包数了，意味文件传输完毕
        if (message.transferStatus == TransferStatus.SEND_FINISHED || message.indexPackage!! >= message.totalPackage!! - 1) {
            //  如果文件流也会关闭 可通过 fileOutputStream.fd.valid()判断，false为关闭    不用再管fileOutputStream!!.close()
            fileChannel.close()
            writeFileCache.invalidate(message.md5!!)
            message.transferStatus = TransferStatus.RECEIVE_FINISHED
        }
        message.fileData = null//返回过去重置为空，节约开销
        message.indexPackage = message.indexPackage?.plus(1)
        return message
    }

}