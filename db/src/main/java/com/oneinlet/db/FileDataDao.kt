package com.oneinlet.db

import com.j256.ormlite.dao.BaseDaoImpl
import java.sql.Timestamp

/**
 * Created by WangZiHe on 19-8-19
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
object FileDataDao : BaseDaoImpl<FileData, Int>(SqliteDataSource.getConnectionSource(), FileData::class.java) {

    fun saveFileData(fileData: FileData) {
        fileData.createTime = Timestamp(System.currentTimeMillis())
        fileData.updateTime = Timestamp(System.currentTimeMillis())
        this.create(fileData)
    }

    fun batchSave(version: String, fileDataList: List<FileData>) {
//        val sql = "insert or ignore into file_data  (id,md5,path,endStatus,version,createTime,updateTime) values\n" +
//                "(null,'md555556891883','/home/aa/a1.txt',false,66,'1569577914734','1569577914734'),\n" +
//                "(null,'md555556891224','/home/aa/a1.txt',false,66,'1569577914734','1569577914734');"
        val sql = "insert or ignore into file_data  (id,md5,path,endStatus,version,createTime,updateTime) values "
        val sb = StringBuilder(sql)
        for (fileData in fileDataList) {
            val time = System.currentTimeMillis().toString()
            val value = " (null,'%s','%s',false,'%s','%s','%s'),".format(fileData.md5, fileData.path, version, time, time)
            sb.append(value)
        }
        this.callBatchTasks {
            this.executeRawNoArgs(sb.toString().dropLast(1))
        }
    }

    fun saveIgnore(version: String, fileData: FileData) {
        val sql = "insert or ignore into file_data  (id,md5,path,endStatus,version,createTime,updateTime) values "
        val sb = StringBuilder(sql)
        val time = System.currentTimeMillis().toString()
        val value = " (null,'%s','%s',false,'%s','%s','%s'),".format(fileData.md5, fileData.path, version, time, time)
        sb.append(value)
        this.executeRawNoArgs(sb.toString().dropLast(1))
    }

    fun batchSave1(version: String, fileDataList: List<FileData>) {
        val connection = SqliteDataSource.getConnectionSource().getReadWriteConnection(SqliteDataSource.tableName)
        connection.isAutoCommit = false
        for (fileData in fileDataList) {
            val sql = "insert or ignore into file_data  (id,md5,path,endStatus,version,createTime,updateTime) values "
            val sb = StringBuilder(sql)
            val time = System.currentTimeMillis().toString()
            val value = " (null,'%s','%s',false,'%s','%s','%s') ".format(fileData.md5, fileData.path, version, time, time)
            sb.append(value)
            connection.executeStatement(sb.toString(), -1)
            println(sb.toString())
        }
        //null 表示全部提交
        connection.commit(null)
        connection.closeQuietly()
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
     *  运行批处理操作
     *  如果初次创建数据库，并且数据库没有数据，我们一次性批量导入
     * */
    fun batchSaveFileData(list: List<FileData>) {
        this.callBatchTasks {
            for (fileData in list) {
                this.create(fileData)
            }
        }
    }

    fun countFileData(): Long {
        return this.countOf()
    }
}