package com.oneinlet.db

import com.oneinlet.YCString
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.time.StopWatch
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.sql.Timestamp
import java.util.concurrent.TimeUnit

/**
 * Created by WangZiHe on 19-8-26
 * QQ/WeChat:648830605
 *
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
object FileDataService {

    private val logger = LoggerFactory.getLogger("FileDataService")

    private val version: String = YCString.generateRandomChar(5)

    fun addFileData(fileData: FileData) {
        val fileData = FileDataDao.queryFileDataByMd5(fileData.md5!!)
        if (fileData != null) {
            logger.info("该文件已经传输完毕过， 不再传输，fileData：{}", fileData)
            return
        }
        FileDataDao.saveFileData(fileData!!)
    }

    fun batchSaveFileData(fileList: ArrayList<File>?) {
        if (fileList == null) {
            throw RuntimeException("文件列表不能为空")
        }
        //      Constant.scanVersion = YCLocalDateTime.format(LocalDateTime.now(), "yyyyMMddHHmmss").plus(YCString.generateRandomChar(5))
        if (FileDataDao.countFileData() > 0) {
            afterQuerySave(fileList)
        } else {
            //批量插入性能更好，如果对象太多，考虑分批插入
            var listFileData: ArrayList<FileData>? = null
            val count = 1000
            val size = fileList.size
            if (size > 1000) {
                val batchCount = fileList.size.div(count)
                for (i in 0 until batchCount) {
                    val firstIndex = i * count
                    var lastIndex = size.minus(firstIndex)
                    if (lastIndex >= 999) {
                        lastIndex = firstIndex + 999
                    }
                    listFileData = transformListFileData(fileList.subList(firstIndex, lastIndex))
                    FileDataDao.batchSaveFileData(listFileData)
                }
            } else {
                listFileData = transformListFileData(fileList)
                FileDataDao.batchSaveFileData(listFileData)
            }
            // 释放内存 加速回收
            listFileData!!.clear()
            listFileData = null
        }
        // 释放内存 加速回收
        fileList.clear()
    }

    private fun transformListFileData(fileList: List<File>): ArrayList<FileData> {
        val list = ArrayList<FileData>(fileList.size)
        for (file in fileList) {
            val fileData = transformFileData(file)
            list.add(fileData)
        }
        return list
    }

    private fun transformFileData(file: File): FileData {
        val fileData = FileData()
        fileData.createTime = Timestamp(System.currentTimeMillis())
        fileData.updateTime = Timestamp(System.currentTimeMillis())
        fileData.path = file.path
        fileData.endStatus = false
        fileData.md5 = DigestUtils.md5Hex(FileInputStream(file))
        fileData.version = "55"
        return fileData
    }


    fun afterQuerySave(fileList: List<File>): Int {
        val watch = StopWatch.createStarted()
        val fileDataList = transformListFileData(fileList)
        watch.stop()
        logger.info("获取50个文件对象数据消耗时间：${watch.getTime(TimeUnit.MILLISECONDS)}")

        watch.reset()
        watch.start()
        val c = this.saveIfNotExist(fileDataList)
        logger.info("插入insert 50个文件对象数据消耗时间：${watch.getTime(TimeUnit.MILLISECONDS)}")
        return c
    }


    /***
     *  多线程执行插入操作，分批
     */


    private fun saveIfNotExist(fileDataList: List<FileData>): Int {
        val watch = StopWatch.createStarted()
        watch.stop()
        var count = 0
        for (fileData in fileDataList) {
            watch.reset()
            watch.start()
            var fileDataDB: FileData? = fileData
            if (fileDataDB?.md5 != null) {
                fileDataDB = FileDataDao.queryFileDataByMd5(fileDataDB.md5!!)
            }
            if (fileDataDB == null) {
                FileDataDao.create(fileData)
                count++
            } else {
                logger.info("发现已存在相同的文件，当前只更新版本号，当前扫描的文件： {}， 已经存在的文件：{}", fileData.path, fileDataDB.path)
                FileDataDao.updateVersionForFileDataByMd5(fileDataDB.version!!, fileDataDB.md5!!)
            }
            watch.stop()

            logger.info("插入单个文件对象数据消耗时间：${watch.getTime(TimeUnit.MILLISECONDS)}")
        }
        logger.info("文件数据插入完毕，总共数量：{}", count)
        return count
    }
}

