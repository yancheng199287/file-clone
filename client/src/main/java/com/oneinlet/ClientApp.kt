package com.oneinlet

import com.oneinlet.common.AppConf
import com.oneinlet.common.disruptor.DisruptorServer
import com.oneinlet.common.file.monitor.FileMonitor
import com.oneinlet.common.file.scan.FileScan


/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

fun main() {
    val clientClient = AppConf.parseClientConf()
    println("client config：$clientClient")
    //启动客户端
    CloneClient().startCloneClient(clientClient, object : CloneClient.ClientConnectSuccessListener {
        override fun onSuccess() {
            // 开启文件扫描
            FileScan.startFileScan()
            // 开启文件监控
            FileMonitor.startClientFileMonitor()
            // 开启发布订阅
            DisruptorServer.startDisruptorServer()
        }
    })
}


