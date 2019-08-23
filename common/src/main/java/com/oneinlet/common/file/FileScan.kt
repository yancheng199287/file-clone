package com.oneinlet.common.file

import java.io.File
import java.util.ArrayList
import java.util.LinkedList
import java.io.FileNotFoundException
import java.util.Arrays
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import java.util.function.Supplier
import kotlin.streams.toList


/**
 * Created by WangZiHe on 19-8-19
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class FileScan {


    fun call01(path: String): ArrayList<File>? {
        val fileList = ArrayList<File>()
        val rootDirFileList = ArrayList<File>()
        val files = File(path).listFiles()
        val size = files!!.size
        for (i in 0 until size) {
            val file = files[i]
            if (file.listFiles() != null && file.isDirectory) {
                rootDirFileList.add(file)
            } else {
                fileList.add(file)
            }
        }
        val executorService = Executors.newFixedThreadPool(rootDirFileList.size)
        val cfs = rootDirFileList.stream().map {
            CompletableFuture
                    .supplyAsync(Supplier { traverseFileTree(it, ArrayList()) }, executorService)
                    .thenAccept {
                        fileList.addAll(it)
                    }
        }.toList()
        CompletableFuture.allOf(*cfs.toTypedArray()).join()
        executorService.shutdown()
        return fileList
    }


    //非递归遍历文件树
    fun traverseFileTree(rootDirectory: File, fileList: ArrayList<File>): ArrayList<File> {
        val tempDirectoryFile = LinkedList<File>()
        var rootListFile = rootDirectory.listFiles()
        while (true) {
            var i = 0
            val len = rootListFile.size
            retry@ while (i < len) {
                val file = rootListFile[i]
                if (file.isDirectory) {
                    tempDirectoryFile.push(file)
                } else {
                    fileList.add(file)
                }
                if (i == len - 1) {
                    val len1 = tempDirectoryFile.size
                    return if (len1 > 0) {
                        for (j in 0 until len1) {
                            val dirFile = tempDirectoryFile.pop()
                            rootListFile = dirFile.listFiles()
                            if (rootListFile != null && rootListFile.isNotEmpty()) {
                                break@retry
                            }
                        }
                        fileList
                    } else {
                        fileList
                    }
                }
                i++
            }
        }
    }


    fun writeFile() {

    }

}