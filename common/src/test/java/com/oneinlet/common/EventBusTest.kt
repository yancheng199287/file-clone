package com.oneinlet.common

import com.oneinlet.common.event.EventBusCore
import com.oneinlet.common.event.source.FileLogEvent
import org.junit.Test

/**
 * Created by WangZiHe on 19-8-20
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class EventBusTest {


    @Test
    fun postEvent() {
        EventBusCore.initRegisterListener()
        val fileLogEvent = FileLogEvent()
        fileLogEvent.remark = "hello,I am from event"
        EventBusCore.post(fileLogEvent)

        EventBusCore.postSystemLogEvent("正在扫描文件")
    }
}