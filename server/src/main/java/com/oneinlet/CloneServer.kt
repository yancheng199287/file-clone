package com.oneinlet

import com.oneinlet.common.bean.ServerConfig
import com.oneinlet.handler.CloneServerHandler
import com.oneinlet.protocol.KyroDecoder
import com.oneinlet.protocol.KyroEncoder
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.slf4j.LoggerFactory

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class CloneServer {

    private val logger = LoggerFactory.getLogger("CloneServer")


    fun startCloneServer(serverConfig: ServerConfig) {
        val bossGroup = NioEventLoopGroup()
        val workerGroup = NioEventLoopGroup()
        val serverBootstrap = ServerBootstrap()
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java)
                    .childHandler(
                            object : ChannelInitializer<SocketChannel>() {
                                override fun initChannel(ch: SocketChannel?) {
                                    ch!!.pipeline()
                                            .addLast(KyroDecoder())
                                            .addLast(KyroEncoder())
                                            .addLast(CloneServerHandler())
                                }
                            })
            val f: ChannelFuture = serverBootstrap.bind(serverConfig.host, serverConfig.port).sync()
            val address = f.channel().localAddress()
            logger.info("CloneSServer has been started，address:$address ,  please enjoy it！")
            f.channel().closeFuture().sync()
        } finally {
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
            logger.warn("Clone server has been shutdown， EventLoopGroup  has been released all resources！")
        }

    }
}