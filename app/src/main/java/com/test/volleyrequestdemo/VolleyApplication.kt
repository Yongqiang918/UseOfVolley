package com.test.volleyrequestdemo

import android.app.Application
import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.test.volleyrequestdemo.net.EnvironmentUtils
import com.test.volleyrequestdemo.net.NetRequestUtils
import com.test.volleyrequestdemo.net.RequestUrls
import kotlin.properties.Delegates

/**
 *Created by Lyq
 *on 2021-04-08
 */
class VolleyApplication : Application() {

    companion object {
        var mContext: Context by Delegates.notNull()
//        var requestQueue: RequestQueue? = null

    }

    override fun onCreate() {
        super.onCreate()
//        requestQueue = Volley.newRequestQueue(applicationContext)
        NetRequestUtils.getInstance(applicationContext)
        RequestUrls.initDomain()
        EnvironmentUtils.setStatus(true, this)//false为线上打包
    }
}