package com.oneinlet

import com.oneinlet.common.AppConf

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

fun main() {
    val serverConfig = AppConf.parseServerConf()
    println("server config： $serverConfig")
    CloneServer().startCloneServer(serverConfig)
}