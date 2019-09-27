package com.oneinlet.common.file.scan

import com.oneinlet.common.AppConf
import com.oneinlet.common.jni.JNILoader
import com.oneinlet.db.FileDataService
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.time.StopWatch
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit


/**
 * Created by WangZiHe on 19-8-19
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
object FileScan {

    private val logger = LoggerFactory.getLogger("FileScan")


    fun startFileScan() {
        val future = CompletableFuture.supplyAsync {
            val path = AppConf.parseClientConf().sourcePath
            logger.info("当前扫描的文件路径：$path")
            traverseFileTree(path)
        }
        future.whenComplete { result, exception ->
            run {
                logger.info("FileScan Task has been completed，result:$result, exception:$exception")
                saveFileToDB()
            }
        }
    }

    private fun traverseFileTree(rootDirectoryPath: String): String {
        val path = AppConf.parseTempFilePath()
        println("traverseFileTree $path")
        return JNILoader.getRustLib().recursive_directory(path, rootDirectoryPath)
    }


    private fun saveFileToDB() {
        var totalInsert = 0
        val filePath = AppConf.parseTempFilePath()
        val file = File(filePath)
        val lineIterator = FileUtils.lineIterator(file, "UTF-8")
        lineIterator.use {
            val list = ArrayList<File>(50)
            while (lineIterator.hasNext()) {
                val line = lineIterator.nextLine()
                val f = File(line)
                if (!f.isDirectory) {
                    list.add(f)
                }
                try {
                    if (list.size >= 50) {
                        val watch = StopWatch.createStarted()
                        val count = FileDataService.afterQuerySave(list)
                        watch.stop()
                        logger.info("插入50个文件对象消耗时间：${watch.getTime(TimeUnit.MILLISECONDS)}")
                        totalInsert += count
                        list.clear()
                    }
                } catch (e: Exception) {
                    logger.error("保存文件数据到数据库出错", e)
                }
            }
            if (list.isNotEmpty()) {
                logger.info("最后一批执行插入操作：当前list容量：" + list.size)
                FileDataService.afterQuerySave(list)
            }
            logger.error("首次启动扫描文件完成，一共插入总数据：", totalInsert)
        }
    }
}


