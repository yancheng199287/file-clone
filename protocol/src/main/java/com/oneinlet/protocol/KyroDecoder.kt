package com.oneinlet.protocol

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import com.esotericsoftware.kryo.io.Input
import com.oneinlet.common.bean.Message
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream


/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class KyroDecoder : ByteToMessageDecoder() {

    private val logger = LoggerFactory.getLogger("KyroDecoder")

    private val HEAD_LENGTH = 4

    private val kryoFactory = KryoFactory()

    override fun decode(ctx: ChannelHandlerContext?, byteBuffer: ByteBuf?, out: MutableList<Any>?) {
        if (byteBuffer!!.readableBytes() < HEAD_LENGTH) {  //这个HEAD_LENGTH是我们用于表示头长度的字节数。 由于Encoder中我们传的是一个int类型的值，所以这里HEAD_LENGTH的值为4.
            return
        }
        byteBuffer.markReaderIndex()                  //我们标记一下当前的readIndex的位置
        val dataLength = byteBuffer.readInt()       // 读取传送过来的消息的长度。ByteBuf 的readInt()方法会让他的readIndex增加4
        if (dataLength < 0) { // 我们读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
            ctx!!.close()
        }
        if (byteBuffer.readableBytes() < dataLength) { //读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
            byteBuffer.resetReaderIndex()
            return
        }
        val body = ByteArray(dataLength)  //传输正常
        byteBuffer.readBytes(body)
        val message = convertToObject(body)  //将byte数据转化为我们需要的对象

        if (message != null) {
            out!!.add(message)
        } else {
            logger.error("反序列化对象失败：message is null")
        }
    }


    private fun convertToObject(body: ByteArray): Message? {
        var input: Input? = null
        var byteArrayStream: ByteArrayInputStream? = null
        try {
            byteArrayStream = ByteArrayInputStream(body)
            input = Input(byteArrayStream)
            return kryoFactory.getKryo().readObject(input, Message::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error("解码失败,body:$body", e)
        } finally {
            input?.close()
            byteArrayStream?.close()
        }
        return null
    }

}