package com.oneinlet.common.file

import java.io.File
import java.util.*
import java.util.concurrent.*
import java.util.function.Supplier
import kotlin.collections.ArrayList
import kotlin.streams.toList

/**
 * Created by WangZiHe on 19-8-23
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class FileScan02 {


    fun call02(path: String): List<File> {
        val task = FileScanTask(File(path))
        val pool = ForkJoinPool.commonPool()
        val future = pool.submit(task) //提交分解的SumTask 任务
        val fileList = future.get()
        pool.shutdown() //关闭线程池
        return fileList
    }

    class FileScanTask(private val dirFile: File) : RecursiveTask<List<File>>() {

        override fun compute(): List<File> {
            val listFile = ArrayList<File>()
            val files = dirFile.listFiles()
            for (file in files) {
                if (file.isDirectory) {
                    val fileScanTask = FileScanTask(file)
                    fileScanTask.fork()
                    println("55555555555555555")
                    return fileScanTask.join()
                } else {
                    println("file: ${file}")
                    listFile.add(file)
                }
            }
            return listFile
        }

    }


    fun call01(path: String): ArrayList<File> {
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