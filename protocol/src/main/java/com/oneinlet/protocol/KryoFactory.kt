package com.oneinlet.protocol

import com.esotericsoftware.kryo.Kryo
import com.oneinlet.common.bean.FileAction
import com.oneinlet.common.bean.Message


/**
 * Created by WangZiHe on 19-8-15
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class KryoFactory {

/*
    创建一个ThreadLocalKryoFactory继承KryoFactory，用来为每个线程创建一个Kryo对象，原因是由于Kryo 不是线程安全的。
    每个线程都应该有自己的 Kryo，Input 和 Output 实例。此外， bytes[] Input 可能被修改，然后在反序列化期间回到初始状态，
    因此不应该在多线程中并发使用相同的 bytes[]。
    Kryo 实例的创建/初始化是相当昂贵的，所以在多线程的情况下，您应该线程池化 Kryo 实例。简单的解决方案是使用 ThreadLocal 将 Kryo实例绑定到 Threads。
*/
    private val holder = object : ThreadLocal<Kryo>() {
        override fun initialValue(): Kryo {
            return createKryo()
        }
    }

    fun createKryo(): Kryo {
        val kryo = Kryo()
        kryo.register(Message::class.java)
        kryo.register(ByteArray::class.java)
        kryo.register(FileAction::class.java)
        return kryo
    }

    fun getKryo(): Kryo {
        return holder.get()
    }
}