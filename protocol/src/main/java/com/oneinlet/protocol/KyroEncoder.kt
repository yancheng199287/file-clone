package com.oneinlet.protocol

import com.oneinlet.common.bean.Message
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import com.esotericsoftware.kryo.io.Output
import java.io.ByteArrayOutputStream
import org.slf4j.LoggerFactory


/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class KyroEncoder : MessageToByteEncoder<Message>() {

    private val logger = LoggerFactory.getLogger("KyroEncoder")

    private val kryoFactory = KryoFactory()
    override fun encode(ctx: ChannelHandlerContext?, msg: Message, out: ByteBuf?) {
        val body = convertToBytes(msg)  //将对象转换为byte
        val dataLength = body!!.size  //读取消息的长度
        out!!.writeInt(dataLength)  //先将消息长度写入，也就是消息头
        out.writeBytes(body)  //消息体中包含我们要发送的数据
    }

    private fun convertToBytes(message: Message): ByteArray? {
        var bos: ByteArrayOutputStream? = null
        var output: Output? = null
        try {
            bos = ByteArrayOutputStream()
            output = Output(bos)
            kryoFactory.getKryo().writeObject(output, message)
            output.flush()
            return bos.toByteArray()
        } catch (e: Exception) {
            logger.error("编码失败,message:$message", e)
        } finally {
            output?.close()
            bos?.close()
        }
        return null
    }

}