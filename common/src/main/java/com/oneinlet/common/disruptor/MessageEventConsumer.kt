package com.oneinlet.common.disruptor

import com.lmax.disruptor.EventHandler
import com.oneinlet.common.file.FileSpiltPart
import org.slf4j.LoggerFactory

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class MessageEventConsumer : EventHandler<MessageEvent> {

    private val logger = LoggerFactory.getLogger("MessageEventConsumer")

    override fun onEvent(event: MessageEvent, sequence: Long, endOfBatch: Boolean) {
        logger.info("当前的消费者对象:" + this.hashCode() + ", " + event.hashCode() + ",消费事件" + event.toString() + ", sequence:" + sequence + ", endOfBatch:" + endOfBatch)
        Thread.sleep(8000)
    }

    fun readFile(messageEvent: MessageEvent) {
        FileSpiltPart.getRemainPartStreamMessage(messageEvent.message!!)
    }

    fun writeFile(messageEvent: MessageEvent) {
        FileSpiltPart.getRemainPartStreamMessage(messageEvent.message!!)
    }
}