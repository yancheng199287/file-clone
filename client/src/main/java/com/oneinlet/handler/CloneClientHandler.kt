package com.oneinlet.handler

import com.oneinlet.common.bean.FileAction
import com.oneinlet.common.bean.Message
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287*/

class CloneClientHandler : SimpleChannelInboundHandler<Message>() {

    private val logger = LoggerFactory.getLogger("CloneClientHandler")

    override fun channelActive(ctx: ChannelHandlerContext?) {
        logger.info("客户端连接成功，通道激活")
        var message = Message()
        message.fileName = "文件名称"
        message.filePath = "/data/user"
        message.md5 = "65asa56sa65sa6s56a"
        message.indexPackage = 1
        message.totalPackage = 10
        message.fileAction = FileAction.MAKE_FILE
        val data = "我是一个字符串文本内容"
        message.fileData = data.toByteArray()
        ctx!!.writeAndFlush(message)
    }


    override fun channelRead0(ctx: ChannelHandlerContext?, msg: Message?) {
        logger.info("获取服务端返回的数据： $msg")
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
    }


}