package com.oneinlet.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import java.nio.ByteBuffer;

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

//使用translator来去发布生产消息
public class MessageEventProducerWithTranslator {
    private final RingBuffer<MessageEvent> ringBuffer;

    public MessageEventProducerWithTranslator(RingBuffer<MessageEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<MessageEvent, ByteBuffer> TRANSLATOR =
            new EventTranslatorOneArg<MessageEvent, ByteBuffer>() {
                public void translateTo(MessageEvent event, long sequence, ByteBuffer bb) {
                    event.setNumber(bb.getLong(0));
                }
            };

    public void onData(ByteBuffer bb) {
        ringBuffer.publishEvent(TRANSLATOR, bb);
    }
}
