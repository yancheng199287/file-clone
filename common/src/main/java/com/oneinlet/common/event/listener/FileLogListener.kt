package com.oneinlet.common.event.listener

import com.google.common.eventbus.Subscribe
import com.oneinlet.common.event.source.FileLogEvent

/**
 * Created by WangZiHe on 19-8-20
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class FileLogListener {


    @Subscribe
    private fun log(fileLogEvent: FileLogEvent) {
        println("接收到事件发布... $fileLogEvent")
    }
}