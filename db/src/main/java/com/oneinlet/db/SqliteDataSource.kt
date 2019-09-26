package com.oneinlet.db

import com.j256.ormlite.jdbc.DataSourceConnectionSource
import org.apache.commons.dbcp2.BasicDataSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils


/**
 * Created by WangZiHe on 19-8-19
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
object SqliteDataSource {

    private const val databaseUrl = "jdbc:sqlite:file-clone.db"
    private val basicDataSource: BasicDataSource = BasicDataSource()
    private lateinit var connectionSource: ConnectionSource

    init {
        initConfigDataSource()
        initConnectionSource()
        initCreateTable()
    }


    fun initConfigDataSource() {
        basicDataSource.url = databaseUrl
        basicDataSource.initialSize = 3
        basicDataSource.maxTotal = 10
        basicDataSource.maxIdle = 5
        basicDataSource.minIdle = 2
    }

    private fun initConnectionSource() {
        connectionSource = DataSourceConnectionSource(basicDataSource, databaseUrl)
    }

    private fun initCreateTable() {
        TableUtils.createTableIfNotExists(connectionSource, FileData::class.java)
    }

    fun getDataSource(): BasicDataSource {
        return basicDataSource
    }

    fun getConnection(): ConnectionSource {
        return connectionSource
    }
}