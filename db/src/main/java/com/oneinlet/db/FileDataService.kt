package com.oneinlet.db

import org.apache.commons.codec.digest.DigestUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.sql.Timestamp

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
        if (file.isFile) {
            fileData.md5 = DigestUtils.md5Hex(FileInputStream(file))
        }
        fileData.version = "55"
        return fileData
    }


    fun afterQuerySave(fileList: List<File>) {
        this.saveIfNotExist(transformListFileData(fileList))
    }


    /***
     *  多线程执行插入操作，分批
     */
    private fun saveIfNotExist(fileDataList: List<FileData>) {
        for (fileData in fileDataList) {
            var fileDataDB: FileData? = fileData
            if (fileDataDB?.md5 != null) {
                fileDataDB = FileDataDao.queryFileDataByMd5(fileDataDB.md5!!)
            }
            if (fileDataDB == null) {
                FileDataDao.create(fileData)
            } else {
                FileDataDao.updateVersionForFileDataByMd5(fileData.version!!, fileData.md5!!)
            }
        }
    }
}

