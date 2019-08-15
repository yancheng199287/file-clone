package com.oneinlet.common.cache

import org.mapdb.DB
import org.mapdb.DBMaker
import org.mapdb.Serializer
import java.util.ArrayList

/**
 * Created by WangZiHe on 19-7-31
 * QQ/WeChat:648830605
 * QQ-Group:368512253
 * Blog:www.520code.net
 * Github:https://github.com/yancheng199287
 */
class LocalCache {
    private var db: DB? = null

    private val cacheMap: MutableMap<String, String>
        get() = db!!.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen()


    private fun makeYourDB(fileName: String): LocalCache {
        if (db == null) {
            db = DBMaker.fileDB(fileName).fileMmapEnable().make()
        }
        return this
    }

    private fun makeDefaultDB(): LocalCache {
        if (db == null) {
            db = DBMaker.fileDB("local-cache.db").fileMmapEnable().make()
        }
        return this
    }

    fun putValue(key: String, value: String) {
        val map = this.makeDefaultDB().cacheMap
        map[key] = value
        db!!.commit()
        db!!.close()
    }

    fun putListValue(mapData: Map<String, String>) {
        val map = this.makeDefaultDB().cacheMap
        map.putAll(mapData)
        db!!.commit()
        db!!.close()
    }

    fun getValue(key: String): String? {
        val map = this.makeDefaultDB().cacheMap
        val value = map[key]
        db!!.close()
        return value
    }

    fun getListValue(vararg keys: String): List<String>{
        val map = this.makeDefaultDB().cacheMap
        val valueList = ArrayList<String>(keys.size)
        for (k in keys) {
            valueList.add(map[k]!!)
        }
        db!!.close()
        return valueList
    }

    companion object {

        private val localCache: LocalCache? = null

        val instance: LocalCache
            get() = localCache ?: LocalCache()
    }

}
