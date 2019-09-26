package com.oneinlet.common.file.monitor

import com.oneinlet.common.bean.FileAction
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor
import org.apache.commons.io.monitor.FileAlterationObserver
import org.slf4j.LoggerFactory
import java.io.File

/**
 * Created by WangZiHe on 19-8-12
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

class FileListener : FileAlterationListenerAdaptor() {

    private val log = LoggerFactory.getLogger(FileListener::class.java)


    /**
     * 文件创建执行
     */
    override fun onFileCreate(file: File) {
        log.info("[新建文件]:" + file.absolutePath)
        pushEvent(file, FileAction.MAKE_FILE)
    }

    /**
     * 文件创建修改
     */
    override fun onFileChange(file: File) {
        log.info("[修改文件]:" + file.absolutePath)
        pushEvent(file, FileAction.UPDATE_FILE)
    }

    /**
     * 文件删除
     */
    override fun onFileDelete(file: File) {
        log.info("[删除文件]:" + file.absolutePath)
        pushEvent(file, FileAction.DELETE_FILE)
    }

    /**
     * 目录创建
     */
    override fun onDirectoryCreate(directory: File) {
        log.info("[新建目录]:" + directory.absolutePath)
        pushEvent(directory, FileAction.MAKE_DIRECTORY)
    }

    /**
     * 目录修改
     */
    override fun onDirectoryChange(directory: File) {
        log.info("[修改目录]:" + directory.absolutePath)
        pushEvent(directory, FileAction.UPDATE_DIRECTORY)
    }

    /**
     * 目录删除
     */
    override fun onDirectoryDelete(directory: File) {
        log.info("[删除目录]:" + directory.absolutePath)
        pushEvent(directory, FileAction.DELETE_DIRECTORY)
    }

    override fun onStart(observer: FileAlterationObserver) {
        // TODO Auto-generated method stub
        super.onStart(observer)
    }

    override fun onStop(observer: FileAlterationObserver) {
        // TODO Auto-generated method stub
        super.onStop(observer)
    }

    fun pushEvent(file: File, fileAction: FileAction) {
        //publisher.onData(file, fileAction)
    }
}