package com.oneinlet.common

import com.oneinlet.common.bean.ClientConfig
import com.oneinlet.common.bean.ClientFileConfig
import com.oneinlet.common.bean.ServerConfig
import com.oneinlet.common.bean.ServerFileConfig
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

    fun parserClientFileConfig(): ClientFileConfig {
        return config.extract("clientFileConfig")
    }

    fun parserServerFileConfig(): ServerFileConfig {
        return config.extract("serverFileConfig")
    }
    fun parserClientConf(): ClientConfig {
        return config.extract("clientConfig")
    }

    fun parserServerConf(): ServerConfig {
        return config.extract("serverConfig")
    }

}