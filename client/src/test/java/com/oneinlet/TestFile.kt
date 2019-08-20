package com.oneinlet

import org.apache.commons.codec.digest.DigestUtils
import org.junit.Test
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.lang.StringBuilder

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
    fun formatSize(){
        println(YCFormat.formatSpaceSize(5555))
    }
}