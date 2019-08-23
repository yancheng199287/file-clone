package com.oneinlet.common

import com.oneinlet.common.file.FileScan
import com.oneinlet.common.file.FileScan01
import org.apache.commons.lang3.time.StopWatch
import org.junit.Test
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.streams.toList

/**
 * Created by WangZiHe on 19-8-22
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class FileTest {


    @Test
    fun traverseFileTree() {
        val path = "/home/yancheng"
        val fileScan = FileScan01()
        val watch = StopWatch.createStarted()
        fileScan.call01(path)
        watch.stop()
        println("消耗时间：${watch.time}")

        while (true){
            Thread.sleep(15000)
        }
    }

}