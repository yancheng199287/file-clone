package com.oneinlet.disruptor;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

//具体而言，在多生产者的情况下，这将导致消费者停滞并且在没有重启的情况下无法恢复。 因此，建议使用EventTranslator API。
// 这种方法是遗留下的问题，3.0以后改变了这个问题，请使用 EventTranslator APIs
public class MessageEventProducer {
    private final RingBuffer<MessageEvent> ringBuffer;

    public MessageEventProducer(RingBuffer<MessageEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer bb) {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try {
            MessageEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
            // for the sequence
            event.setNumber(bb.getLong(0));  // Fill with data
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
