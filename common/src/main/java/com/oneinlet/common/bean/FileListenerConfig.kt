package com.oneinlet.common.bean


/**
 * Created by WangZiHe on 19-8-12
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
data class FileListenerConfig(
        val ignoreFileFormat: Array<String>, //文件格式，txt jpg zip
        val maxSize:Int   //文件大小
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FileListenerConfig) return false

        if (!ignoreFileFormat.contentEquals(other.ignoreFileFormat)) return false
        if (maxSize != other.maxSize) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ignoreFileFormat.contentHashCode()
        result = 31 * result + maxSize
        return result
    }
}