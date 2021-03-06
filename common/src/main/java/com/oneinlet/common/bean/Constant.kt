package com.oneinlet.common.bean

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
object Constant {
    val fileSpiltPartLength = 1024 * 10
    val clientBasePath: String? = null //客户端基路径
    val serverBasePath: String? = null//服务端基路径
    var scanVersion: String? = null//本次扫描文件的版本标识，如果不是本次的都删除
}