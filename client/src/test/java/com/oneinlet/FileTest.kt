package com.oneinlet

import com.oneinlet.common.file.scan.FileScan
import org.apache.commons.lang3.time.StopWatch
import org.junit.Test
import java.util.concurrent.TimeUnit

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
        val path = System.getProperty("user.dir")
        println("消耗时间：${path}")
    }

    @Test
    fun traverseFileTree01() {
        val watch = StopWatch.createStarted()
        FileScan.startFileScan()
        watch.stop()
        println("消耗时间：${watch.getTime(TimeUnit.MILLISECONDS)}")
        Thread.sleep(8000)

    }

    @Test
    fun traverseFileTree1() {
       // FileScan.getFile()
    }


}