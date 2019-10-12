package com.oneinlet.db

import org.junit.Test
import java.sql.Timestamp

/**
 * Created by WangZiHe on 19-8-19
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class SQLiteTest {


    @Test
    fun batchSaveTest() {
       println( FileData.getCurrentVersion())

       /* // FileDataDao.batchSave()
        val fileData = FileData()
        fileData.path = "/home/aa/p.xls"
        fileData.md5 = "asda55ada5da999888"

        val fileData1 = FileData()
        fileData1.path = "/home/aa/asaas.mp3"
        fileData1.md5 = "asdadad555"

        val list = ArrayList<FileData>()
        list.add(fileData)
        list.add(fileData1)
        FileDataDao.batchSave("888888888", list)*/
    }

    @Test
    fun addFileDataTest() {
        var fileData = FileData()
        fileData.md5 = "ad656asd6a5da56a"
        fileData.path = "/home/yancheng/test/a.txt"
        fileData.endStatus = false
        fileData.version = "201908195523"
        fileData.createTime = Timestamp(System.currentTimeMillis())
        fileData.updateTime = Timestamp(System.currentTimeMillis())
        FileDataDao.create(fileData)
    }


    @Test
    fun queryFileDataTest() {
        val fileData = FileDataDao.queryForId(1)
        println(fileData)

        val fileData1 = FileDataDao.queryForEq("md5", "ad656asd6a5da56a")
        println(fileData1)

        val map = mapOf("md5" to "ad656asd6a5da56a", "endStatus" to false)
        val fileData2 = FileDataDao.queryForFieldValues(map)
        println(fileData2)

        val count = FileDataDao.countFileData()
        println(count)
    }


    @Test
    fun updateEndStatusForFileDataByMd5() {
        FileDataDao.updateEndStatusForFileDataByMd5("ad656asd6a5da56a")
        val fileData = FileDataDao.queryForId(1)
        println(fileData)
    }
}