package com.oneinlet.common.jni

import jnr.ffi.LibraryLoader
import java.io.File
import java.lang.System.mapLibraryName

/**
 * Created by WangZiHe on 19-9-26
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */

object JNILoader {

    fun getRustLib(): RustLib {
        val dylib = "recursive_path"
        System.setProperty("jnr.ffi.library.path", getLibraryPath(dylib))
        return LibraryLoader.create(RustLib::class.java).load(dylib)
    }


    private fun getLibraryPath(dylib: String): String {
        val a = mapLibraryName(dylib)
        val f = File(JNILoader::class.java.classLoader.getResource(a)!!.file)
        return f.parent
    }
}