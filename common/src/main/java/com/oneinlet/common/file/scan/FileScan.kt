package com.oneinlet.common.file.scan

import com.oneinlet.common.AppConf
import com.oneinlet.common.jni.JNILoader
import com.oneinlet.db.FileDataService
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.CompletableFuture


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
            traverseFileTree(AppConf.parseClientConf().sourcePath)
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
        val filePath = AppConf.parseTempFilePath()
        val file = File(filePath)

        val lineIterator = FileUtils.lineIterator(file, "UTF-8")
        lineIterator.use {
            val list = ArrayList<File>(50)
            while (lineIterator.hasNext()) {
                val line = lineIterator.nextLine()
                var file = File(line)
                if (file.isFile) {
                    list.add(file)
                }
                FileDataService.afterQuerySave(list)
            }
        }
    }
}


