package com.oneinlet.common.jni

/**
 * Created by WangZiHe on 19-9-26
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
interface RustLib {
     fun recursive_directory(tempFilePath: String, rootPath: String): String
}