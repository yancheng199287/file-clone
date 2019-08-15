package com.oneinlet

import com.oneinlet.common.bean.ClientConfig
import com.oneinlet.handler.CloneClientHandler
import com.oneinlet.protocol.KyroDecoder
import com.oneinlet.protocol.KyroEncoder
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import org.slf4j.LoggerFactory

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class CloneClient {

    private val logger = LoggerFactory.getLogger("CloneClient")


    fun startCloneClient(clientConfig: ClientConfig) {
        val group = NioEventLoopGroup()
        val bootstrap = Bootstrap()
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel::class.java)
                    .remoteAddress(clientConfig.host, clientConfig.port)
                    .handler(object : ChannelInitializer<SocketChannel>() {
                        override fun initChannel(ch: SocketChannel?) {
                            ch!!.pipeline()
                                    .addLast(KyroDecoder())
                                    .addLast(KyroEncoder())
                                    .addLast(CloneClientHandler())
                        }
                    })
            val f = bootstrap.connect().sync().addListener {
                logger.info("connect server ok，you can sync file！")
            }
            f.channel().closeFuture().sync()

        } finally {
            group.shutdownGracefully()
        }

    }
}