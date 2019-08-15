package com.oneinlet.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

// 事件处理的消费者
public class MessageHandler implements EventHandler<MessageEvent> {

    // 事件触发，会回调，开发者在这里获取数据对象，消费
    public void onEvent(MessageEvent messageEvent, long l, boolean b) throws Exception {
        System.out.println("消费者获取事件消息 Event: " + messageEvent);
    }
}
