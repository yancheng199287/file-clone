package com.oneinlet.common

import com.oneinlet.common.bean.ClientConfig
import com.oneinlet.common.bean.IgnoreFile
import com.oneinlet.common.bean.ServerConfig
import com.typesafe.config.ConfigFactory
import io.github.config4k.extract


/**
 *  Created by WangZiHe on 2017/8/29.
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 */

object AppConf {

    val config = ConfigFactory.load()

    fun parserSecret(): String {
        return config.getString("secret")
    }

    fun parserIgnoreFile(): IgnoreFile {
        return config.extract("ignoreFile")
    }

    fun parserClientConf(): ClientConfig {
        return config.extract("clientConfig")
    }

    fun parserServerConf(): ServerConfig {
        return config.extract("serverConfig")
    }

}