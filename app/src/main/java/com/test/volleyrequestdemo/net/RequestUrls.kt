package com.test.volleyrequestdemo.net

import java.util.ArrayList

/**
 * 存储请求url
 */
object RequestUrls {
    //存储线上多域名使用
    val hostList = ArrayList<String>()

    //根域名(可测试，可线上，具体是由application 里的EnvironmentUtils.setStatus设定)
    var ROOT_HOST = "http://appapi-test.xxxx.top"
    //测试域名
    var TEST_HOST = "http://appapi-test.xxxx.top"

    //在application里初始化调用，设置ROOT_HOST
    fun initDomain() {
        hostList.add("https://......")
        hostList.add("https://......")
        hostList.add("https://......")
        hostList.add("https://......")
        ROOT_HOST = hostList[0]
    }

    //可存储其他接口路由，与ROOT_HOST拼接组成全路径（ROOT_HOST+DEMO_URL）
    var DEMO_URL = "/uploadWuToken"//测试路由1
    var DEMO_URL2 = "/xxxx"//测试路由2
    var DEMO_URL3 = "/xxxx"//测试路由3





}