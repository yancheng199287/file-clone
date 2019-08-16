package com.oneinlet

import org.junit.Test
import java.io.File
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
}