package com.oneinlet.disruptor

import com.oneinlet.common.bean.FileAction
import java.io.File

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

data class MessageEvent(
        var sequence: Long? = null,//序号
        var file: File? = null,
        var fileAction: FileAction? = null
)