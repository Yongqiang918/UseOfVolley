package com.test.volleyrequestdemo.net

import java.io.Serializable


class NetWorkBean<T>(var code:Int = 0,var msg:String = "",var str:String = "",var date:T ?= null) : Serializable