package com.oneinlet.common.disruptor

import com.lmax.disruptor.*
import com.oneinlet.common.bean.FileAction
import com.oneinlet.common.bean.Message

import java.io.File

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

//使用translator来去发布生产消息
class MessageEventProducerWithTranslator(private val ringBuffer: RingBuffer<MessageEvent>) {

    fun onData(message: Message, rw: Byte) {
        ringBuffer.publishEvent(TRANSLATOR, message, rw)
    }

    companion object {

        private val TRANSLATOR = EventTranslatorTwoArg<MessageEvent, Message, Byte> { event, sequence, message, rw ->
            event.sequence = sequence
            event.message = message
            event.rw = rw
        }
    }
}
