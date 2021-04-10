package com.test.volleyrequestdemo.net

import com.test.volleyrequestdemo.VolleyApplication

object EnvironmentUtils {
    //默认是测试
    var subscript  = 1
    //application里面设置环境（1为测试，2为线上）
    fun setStatus(status: Boolean,application: VolleyApplication) {
        subscript  = if (status) {
            1
        } else {
            2
        }
        when (subscript) {
            1 -> {
                //测试
                RequestUrls.ROOT_HOST = RequestUrls.TEST_HOST
//                LogUtils.Init("VolleyProject", GsonFormatter())
            }
            2 -> {
                //线上
                RequestUrls.ROOT_HOST = RequestUrls.hostList[0]
            }
        }

    }
}