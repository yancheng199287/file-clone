package com.oneinlet.common.disruptor

import com.lmax.disruptor.BlockingWaitStrategy
import com.lmax.disruptor.dsl.Disruptor
import com.lmax.disruptor.dsl.ProducerType
import com.lmax.disruptor.util.DaemonThreadFactory
import org.slf4j.LoggerFactory

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

object DisruptorServer {


    private val logger = LoggerFactory.getLogger("DisruptorServer")

    lateinit var publishProducer: MessageEventProducerWithTranslator;


    fun startDisruptorServer(): MessageEventProducerWithTranslator {
        val disruptor = Disruptor<MessageEvent>(
                MessageEventFactory(), 1024, DaemonThreadFactory.INSTANCE, ProducerType.MULTI,
                BlockingWaitStrategy())
        disruptor.handleEventsWith(MessageEventConsumer())
        publishProducer = MessageEventProducerWithTranslator(disruptor.ringBuffer)
        disruptor.start()
        logger.info("DisruptorServer has started!")
        return publishProducer
    }


}