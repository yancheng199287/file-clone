package com.oneinlet.common.bean

import java.util.*

/**
 * Created by WangZiHe on 19-8-14
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
data class Message(
        var fileName: String? = null,//文件名称
        var fileAction: FileAction? = null,//执行文件动作
        var filePath: String? = null,//文件的路径，基于基路径的下层路径
        var md5: String? = null,//md5校验文件的完整性
        var indexPackage: Int? = null,//当前的第几个包数
        var totalPackage: Int? = null,//总共的分包数
        var fileData: ByteArray? = null, //当前承载的字节数组
        var transferStatus: TransferStatus? = null
) {
    override fun toString(): String {
        var temp: String? = null
        if (fileData == null) {
            temp = "空数组"
        } else {
            temp = "有数据内容"
        }
        return "Message(fileName=$fileName, fileAction=$fileAction, filePath=$filePath, md5=$md5, indexPackage=$indexPackage, totalPackage=$totalPackage, fileData=${temp}, transferStatus=$transferStatus)"
    }
}

enum class FileAction {
    MAKE_FILE,//传输文件
    UPDATE_FILE,//传输文件
    DELETE_FILE,//发送指令 删除文件

    MAKE_DIRECTORY,
    UPDATE_DIRECTORY,
    DELETE_DIRECTORY,
}

enum class TransferStatus {
    SENDING,//发送中，也代表接收中，表示文件克隆任务正在进行
    SEND_FINISHED,//发送完成
    RECEIVE_FINISHED//接收中
}