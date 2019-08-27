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
        fileData.createTime = Timestamp(System.currentTimeMillis())
        fileData.updateTime = Timestamp(System.currentTimeMillis())
        this.create(fileData)
    }

    /**
     *  根据文件md5查询是否存在缓存数据，并判断该文件对象是否已经传输完成
     * */
    fun queryFileDataByMd5(md5: String): FileData? {
        val list = this.queryForEq("md5", md5)
        return if (list.size <= 0) {
            null
        } else {
            list[0]
        }
    }

    //传输完成更新状态
    fun updateEndStatusForFileDataByMd5(md5: String) {
        val sql = "update file_data set endStatus=true where md5=?"
        this.updateRaw(sql, md5)
    }

    //扫描文件更新存在的文件数据的版本号
    fun updateVersionForFileDataByMd5(version: String, md5: String) {
        val sql = "update file_data set version=? where md5=?"
        this.updateRaw(sql, version, md5)
    }

    /***
     *  如果初次创建数据库，并且数据库没有数据，我们一次性批量导入
     * */
    fun batchSaveFileData(list: List<FileData>) {
        this.batchSaveFileData(list)
    }

    fun countFileData(): Long {
        return this.countOf()
    }
}