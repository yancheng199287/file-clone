package com.oneinlet.common.disruptor

import com.lmax.disruptor.BlockingWaitStrategy
import com.lmax.disruptor.dsl.Disruptor
import com.lmax.disruptor.dsl.ProducerType
import com.lmax.disruptor.util.DaemonThreadFactory

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class DisruptorServer {

    private val RING_SIZE: Int = 1024


    fun startDisruptorServer(): MessageEventProducerWithTranslator {
        val disruptor = Disruptor<MessageEvent>(
                MessageEventFactory(), RING_SIZE, DaemonThreadFactory.INSTANCE, ProducerType.MULTI,
                BlockingWaitStrategy())
        disruptor.handleEventsWith(MessageEventConsumer(),MessageEventConsumer(),MessageEventConsumer())
        val publishProducer = MessageEventProducerWithTranslator(disruptor.ringBuffer)
        disruptor.start()
        return publishProducer
    }


}