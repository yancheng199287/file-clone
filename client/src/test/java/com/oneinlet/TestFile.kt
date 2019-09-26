package com.oneinlet

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.time.StopWatch
import org.junit.Test
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by WangZiHe on 19-8-16
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class TestFile {

    @Test
    fun testTraverseFile() {
        val fileDirectoryPath = "/home/yancheng/temp"

        val rootFileDirectory: File = File(fileDirectoryPath)
        traverseFile(rootFileDirectory)
    }

    val sb = StringBuilder("")
    fun traverseFile(rootFileDirectory: File) {
        val rootFiles = rootFileDirectory.listFiles()
        val len = rootFiles.size - 1
        for (i in 0..len) {
            val file = rootFiles[i]
            if (file.isDirectory) {
                sb.append("--------目录" + file.name + "\n")
                traverseFile(file)
            } else {
                println(sb.toString() + "===${sb.hashCode()}====" + file.path)
            }
        }

    }

    @Test
    fun md5FileTest() {
        val file = File("/media/yancheng/文档/ISO-Image/5566.mp3")
        // 不要使用这个方法file.readBytes() ，会导致内存用尽
        val md5 = DigestUtils.md5Hex(FileInputStream(file))
        println(md5)
    }

    @Test
    fun seekFileTest() {
        val file = File("/media/yancheng/文档/ISO-Image/5566.mp3")
        val raFile = RandomAccessFile(file, "r")
        println("文件大小：" + raFile.channel.size())
        raFile.seek(1024)
        println("获取指定节点：" + raFile.filePointer)
    }

    @Test
    fun formatSize() {
        println(YCFormat.formatSpaceSize(5555))
    }

    @Test
    fun scanFile() {
        val watch = StopWatch.createStarted()
        //val path = "/home/yancheng/html"
        val path ="/home/yancheng/Documents/JavaGuide-master"

        //val listFile = FileScan.traverseFileTreeByCompletableFuture(path)

        val listFile = traverseFileTreeByQueue1(path)
        watch.stop()
        println("文件数量：${listFile.size}")
        println("扫描时间：${watch.time}")
    }







    /***
     *  使用队列形式遍历文件，即每次将循环得到的文件目录push到队列尾部中去，然后再从队列首部取出，遍历目录的所有文件夹
     *  循环使用了break tag 类似 goto语句，性能更强，循环更有效，节约时间，快速
     *  优点：不会出现堆栈溢出异常，一种非递归形式的遍历,速度非常快
     *  缺点：速度比较快，无限循环，占用CPU高
     *  场景：适合少量层级目录
     *
     *  @param path 根目录的文件夹路径
     *  @return 返回 fileList，装满所有的文件
     * */
    fun traverseFileTreeByQueue1(path: String): ArrayList<File> {
        val fileList = ArrayList<File>()

        //创建根目录文件对象
        val rootFileDirectory = File(path)
        // 如果不是根目录 那么自动退出
        if (!rootFileDirectory.isDirectory) {
            return fileList
        }
        // 如果 listFiles等于null  直接返回  fileList
        val rootFiles = rootFileDirectory.listFiles() ?: return fileList

        // 新建一个队列
        val filesInDirectory = LinkedList<Array<File>>()
        while (true) {
            var i = 0

            // 文件数量，包括文件+文件夹
            val len = rootFiles.size

            // 如果当前 i 小于 文件
            retry@ while (i < len) {
                val file = rootFiles[i]
                println("path:"+file.path)

                if (file.isDirectory) {
                    val files = file.listFiles()
                    if (files != null) {
                        filesInDirectory.push(files)
                    }
                } else {
                    fileList.add(file)
                }
                if (i == len - 1) {
                    val len1 = filesInDirectory.size
                    return if (len1 > 0) {
                        for (j in 0 until len1) {
                            val dirFiles = filesInDirectory.pop()
                            if (dirFiles != null && dirFiles.isNotEmpty()) {
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
}