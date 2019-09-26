package com.oneinlet.handler

import com.oneinlet.common.bean.Message
import com.oneinlet.common.bean.TransferStatus
import com.oneinlet.common.file.rw.FileSpiltPart
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class CloneServerHandler : SimpleChannelInboundHandler<Message>() {

    private val logger = LoggerFactory.getLogger("CloneServer")


    override fun channelActive(ctx: ChannelHandlerContext?) {
        logger.info("server channel Active")
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        logger.info("服务端读取客户端的消息：$msg")
        val message = FileSpiltPart.receivePartStream(msg)
        if (message.transferStatus == TransferStatus.RECEIVE_FINISHED) {
            logger.info("已经完全接收对方发来的文件：${message}")
            ctx.close()
            return
        }
        ctx.writeAndFlush(message)

    }

}