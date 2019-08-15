package com.oneinlet.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

//为了让Disruptor为我们预先分配这些事件，我们需要一个将执行构造的EventFactory
public class MessageEventFactory implements EventFactory<MessageEvent> {
    public MessageEvent newInstance() {
        return new MessageEvent();
    }
}
