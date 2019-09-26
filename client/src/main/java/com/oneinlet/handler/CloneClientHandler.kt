package com.oneinlet.handler

import com.oneinlet.common.bean.Message
import com.oneinlet.common.bean.TransferStatus
import com.oneinlet.common.channel.ClientChannelManage
import com.oneinlet.common.file.rw.FileSpiltPart
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
        logger.info("client channel Active")
        ClientChannelManage.CloneServerChannel = ctx!!.channel()

        /*  var message = Message()
          message.fileName = "文件名称"
          message.filePath = "/data/user"
          message.md5 = "65asa56sa65sa6s56a"
          message.indexPackage = 1
          message.totalPackage = 10
          message.fileAction = FileAction.MAKE_FILE
          val data = "我是一个字符串文本内容"
          message.fileData = data.toByteArray()
          ctx!!.writeAndFlush(message)*/
//马蜂窝香港.pdf  薄冰英语语法指南图文版802236.epub
        //  var message = FileSpiltPart.initMessage("/home/yancheng/Downloads/马蜂窝香港.pdf", FileAction.MAKE_FILE)
        //  message = FileSpiltPart.getRemainPartStreamMessage(message)
        //   ctx!!.writeAndFlush(message)
    }


    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        logger.info("获取服务端返回的数据： $msg")
        if (msg.transferStatus == TransferStatus.RECEIVE_FINISHED) {
            logger.info("文件已经发送完毕，对方已成功接收： $msg")
        } else {
            val message = FileSpiltPart.getRemainPartStreamMessage(msg)
            ctx.writeAndFlush(message)
        }
    }

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        super.channelRead(ctx, msg)
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
    }


}