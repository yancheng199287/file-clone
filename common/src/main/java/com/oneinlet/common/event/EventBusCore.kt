package com.oneinlet.common.event

import com.google.common.eventbus.EventBus
import com.oneinlet.common.event.listener.FileLogListener

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

    fun batchRegisterLisener(vararg objs: Any) {
        for (obj in objs) {
            this.register(obj)
        }
    }

    fun initRegisterLisener() {
        val fileLogListener = FileLogListener()
        this.batchRegisterLisener(fileLogListener)
    }
}