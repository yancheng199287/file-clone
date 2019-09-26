package com.oneinlet

import org.junit.Test
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer


/**
 * Created by WangZiHe on 19-9-26
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class TestThread {


    @Test
    fun asyncThread() {
        val future = CompletableFuture.supplyAsync {
            println("File Scan have finished")
            Thread.sleep(3000)
            System.currentTimeMillis()
        }
        future.whenComplete { t, action -> println("执行完成！ 异步任务返回结果值：${t}  是否有异常： ${action}") }
        Thread.sleep(4000)
    }
}