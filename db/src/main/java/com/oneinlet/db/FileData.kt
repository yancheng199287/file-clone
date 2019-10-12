package com.oneinlet.db

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.oneinlet.YCString
import java.sql.Timestamp
import java.time.LocalDateTime

/**
 * Created by WangZiHe on 19-8-19
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
@DatabaseTable(tableName = "file_data")
data class FileData(
        @DatabaseField(generatedId = true)
        var id: Int? = null,  // id
        @DatabaseField(canBeNull = false, uniqueIndex = true)
        var md5: String? = null, //文件md5
        @DatabaseField(canBeNull = false)
        var path: String? = null,//文件绝对路径
        @DatabaseField(canBeNull = false)
        var endStatus: Boolean? = null, //文件结束状态
        @DatabaseField(canBeNull = false)
        var version: String? = null, //本次扫描的文件版本号
        @DatabaseField(canBeNull = false)
        var createTime: Timestamp? = null,
        @DatabaseField(canBeNull = false)
        var updateTime: Timestamp? = null
) {
    companion object {
        private var currentVersion = YCString.generateRandomChar(5)
        //每次生成新的版本号标识
        fun getCurrentVersion(): String {
            return currentVersion
        }
    }
}