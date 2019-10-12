package com.oneinlet.common.event.listener

import com.google.common.eventbus.Subscribe
import com.oneinlet.common.event.source.SystemLogEvent

/**
 * Created by WangZiHe on 19-10-12
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class SystemLogListener {


    @Subscribe
    private fun log(systemLogEvent: SystemLogEvent) {
        println("SystemLogEvent-接收到系统日志事件：$systemLogEvent")
    }
}