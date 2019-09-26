package com.oneinlet.common.channel

import com.oneinlet.common.bean.Message
import io.netty.channel.Channel

/**
 * Created by WangZiHe on 19-9-26
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

object ClientChannelManage {

    /***
     *  缓存与Server建立的通道
     * */
    lateinit var CloneServerChannel: Channel

    fun writeAndFlush(message: Message) {
        CloneServerChannel.writeAndFlush(message)
    }

}