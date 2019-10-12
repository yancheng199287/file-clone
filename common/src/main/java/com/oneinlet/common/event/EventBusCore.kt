package com.oneinlet.common.event

import com.google.common.eventbus.EventBus
import com.oneinlet.common.event.listener.FileLogListener
import com.oneinlet.common.event.listener.SystemLogListener
import com.oneinlet.common.event.source.FileLogEvent
import com.oneinlet.common.event.source.SystemLogEvent

/**
 * Created by WangZiHe on 19-8-20
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
object EventBusCore {

    private val eventBus = EventBus()

    fun getInstance(): EventBus {
        return eventBus
    }

    fun register(obj: Any) {
        eventBus.register(obj)
    }

    fun unregister(obj: Any) {
        eventBus.unregister(obj)
    }

    fun post(obj: Any) {
        eventBus.post(obj)
    }

    fun postFileLogListener(info: String) {
        eventBus.post(FileLogEvent(info))
    }

    fun postSystemLogEvent(info: String) {
        eventBus.post(SystemLogEvent(info))
    }

    private fun batchRegisterListener(vararg objs: Any) {
        for (obj in objs) {
            this.register(obj)
        }
    }

    fun initRegisterListener() {
        this.batchRegisterListener(FileLogListener(), SystemLogListener())
    }
}