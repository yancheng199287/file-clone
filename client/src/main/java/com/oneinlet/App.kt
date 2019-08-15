package com.oneinlet

import com.oneinlet.common.AppConf
import com.oneinlet.disruptor.DisruptorServer
import com.oneinlet.file.FileMonitor

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

fun main() {
    val clientClient = AppConf.parserClientConf()
    println("客户端配置参数：$clientClient")
    //启动客户端
    CloneClient().startCloneClient(clientClient, object : CloneClient.ClientConnectSuccessListener {
        override fun onSuccess() {
            val publishProducer = DisruptorServer().startDisruptorServer()
            println("Disruptor service  has started")
            //启动文件扫描监听器
            FileMonitor(publishProducer).startFileStatus()
            println("FileMonitor service  has started")
        }
    })
}


