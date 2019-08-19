package com.oneinlet.db

import com.j256.ormlite.dao.BaseDaoImpl
import com.oneinlet.common.bean.FileData
import java.sql.Timestamp

/**
 * Created by WangZiHe on 19-8-19
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
object FileDataDao : BaseDaoImpl<FileData, Int>(SqliteDataSource.getConnection(), FileData::class.java) {

    fun saveFileData(fileData: FileData) {
        if (queryFileDataByMd5(fileData.md5!!) != null) {
            return
        }
        fileData.createTime = Timestamp(System.currentTimeMillis())
        fileData.updateTime = Timestamp(System.currentTimeMillis())
        this.create(fileData)
    }

    fun queryFileDataByMd5(md5: String): FileData? {
        val list = this.queryForEq("md5", md5)
        return if (list.size <= 0) {
            null
        } else {
            list[0]
        }
    }

    fun batchSaveFileData(list: List<FileData>) {
        this.batchSaveFileData(list)
    }

    fun countFileData(): Long {
        return this.countOf()
    }
}