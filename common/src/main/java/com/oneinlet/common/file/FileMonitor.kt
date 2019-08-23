package com.oneinlet.common.file

import org.apache.commons.io.filefilter.FileFilterUtils
import org.apache.commons.io.filefilter.HiddenFileFilter
import org.apache.commons.io.monitor.FileAlterationMonitor
import org.apache.commons.io.monitor.FileAlterationObserver
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class FileMonitor() {

    fun startFileStatus() {
        // 监控目录
        val rootDir = "/home/yancheng/temp/file"
        // 轮询间隔 5 秒
        val interval = TimeUnit.SECONDS.toMillis(1)
        // 创建过滤器
        val directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE)
        val files = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".txt"))
        val filter = FileFilterUtils.or(directories, files)
        // 使用过滤器
        val observer = FileAlterationObserver(File(rootDir), filter)
        //不使用过滤器
        //FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir));
        observer.addListener(FileListener())
        //创建文件变化监听器
        val monitor = FileAlterationMonitor(interval, observer)
        // 开始监控
        monitor.start()
    }


    fun scanDirectory(rootDirectory: File, fileList: ArrayList<File>) {
        val files = rootDirectory.listFiles()
        for (file in files) {
            if (file.isDirectory) {
                this.scanDirectory(file, fileList)
            } else {
                fileList.add(file)
            }
        }
    }
}