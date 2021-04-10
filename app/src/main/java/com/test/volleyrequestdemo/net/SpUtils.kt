package com.test.volleyrequestdemo.net

import android.content.Context
import android.content.SharedPreferences

/**
 * 本地存储类，封装的sp
 */
class SpUtils(context: Context) {

    private var SP_STRING = "sp_string"
    private var preferences: SharedPreferences? = null
    private var ed: SharedPreferences.Editor? = null

    init {
        preferences = context.getSharedPreferences(SP_STRING, Context.MODE_PRIVATE)
        ed = preferences!!.edit()
    }

    fun saveSpParam(bond: String, worth: String) {
        ed?.putString(bond, worth)
        ed?.apply()
    }

    fun saveSpParam(bond: String, worth: Int) {
        ed?.putInt(bond, worth)
        ed?.apply()
    }

    fun saveSpParam(bond: String, worth: Boolean) {
        ed?.putBoolean(bond, worth)
        ed?.apply()
    }

    fun saveSpParam(bond: String, worth: Long) {
        ed?.putLong(bond, worth)
        ed?.apply()
    }

    fun saveSpParam(bond: String, worth: Float) {
        ed?.putFloat(bond, worth)
        ed?.apply()
    }

    fun getSpParam(bond: String, worth: String): String? {
        return preferences?.getString(bond, worth)
    }

    fun getSpParam(bond: String, worth: Int): Int? {
        return preferences?.getInt(bond, worth)
    }

    fun getSpParam(bond: String, worth: Boolean): Boolean? {
        return preferences?.getBoolean(bond, worth)
    }

    fun getSpParam(bond: String, worth: Float): Float? {
        return preferences?.getFloat(bond, worth)
    }
    fun getSpParam(bond: String, worth: Long): Long? {
        return preferences?.getLong(bond, worth)
    }
}