package com.test.volleyrequestdemo

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.test.volleyrequestdemo.img.NetImgUtils
import com.test.volleyrequestdemo.net.NetRequestUtils
import com.test.volleyrequestdemo.net.NetWorkBean
import com.test.volleyrequestdemo.net.RequestUrls
import com.test.volleyrequestdemo.net.TestGetTokenBean


/**
 * StringRequest可以用来从服务器获取String，如果想自己解析请求响应可以使用这个类，例如返回xml数据、json格式数据
 * JsonObjectRequest{
 * 1,可以用来发送和接收JSON对象
 * 2,如果服务端并不支持json的请求方式,比如常见的spring mvc服务端,就很难支持json的请求方式,
 * 那么就需要客户端以普通的post方式进行提交,服务端返回json串
 * }
 *
 * 正常开发，使用StringRequest即可
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //测试执行网络请求
//        executeNetRequest()

        //测试执行加载网络图片
        executeNetImg()
        

    }

    /**
     * volley加载网络图片
     * 多Demo
     */
    private fun executeNetImg() {
        val pathUrl = "http://xxxx/wubanner.png"
        val loadImgIv = findViewById<ImageView>(R.id.loadImg_iv)
        val loadImgIv2 = findViewById<NetworkImageView>(R.id.loadImg_iv2)
        val loadImgBt = findViewById<Button>(R.id.loadImg_bt)
        loadImgBt.setOnClickListener {
            //Volley--imageRequest请求Demo
            NetImgUtils.getInstance(this).imageRequest_method(pathUrl, object : NetImgUtils.OnResponse {
                override fun onResponse(response: Bitmap?) {
                    loadImgIv.setImageBitmap(response)
                }

                override fun onErrorResponse(errorMsg: String?) {
                    Log.e("测试", "加载失败" + errorMsg)
                }

            })


            //Volley--imageLoader请求Demo
            val listener = ImageLoader.getImageListener(loadImgIv, 0, 0)
            NetImgUtils.getInstance(this).imageLoader_method(pathUrl, listener)

            //Volley--networkImageView加载Demo
            NetImgUtils.getInstance(this).networkImageView_method(pathUrl, loadImgIv2, 0, 0)


        }
    }

    /**
     * volley执行网络请求
     * 多Demo
     */
    private fun executeNetRequest() {
        //Volley--StringRequest_Get请求Demo
        var urlget = "http://xxxxxx?customer_id=74&language_type=1&app_channel=1&amount=0&type=1"
        NetRequestUtils.getInstance(this).StringRequest_Get(urlget, TestGetTokenBean::class.java, object : NetRequestUtils.OnGetResponse<TestGetTokenBean> {
            //请求成功
            override fun onDataSuccess(response: NetWorkBean<TestGetTokenBean>?) {
                if (response != null) {
                    Log.e("测试", "请求成功")
                }
            }

            //请求失败
            override fun onDataError(code: Int, msg: String?) {
                Log.e("测试", "请求失败")
            }
        })


        //Volley--StringRequest_Post请求Demo
        //1,post(请求地址，未接口返回的数据bean,回调（添加参数，执行成功与失败）)
        //2,TestGetTokenBean 要根据不同接口后端返回的数据进行
        NetRequestUtils.getInstance(this).StringRequest_Post(RequestUrls.ROOT_HOST + RequestUrls.DEMO_URL, TestGetTokenBean::class.java, object : NetRequestUtils.OnPostResponse<TestGetTokenBean> {
            override fun onDataMap(map: MutableMap<String, String>) {
                //添加请求参数
                //map.put("参数1","")
                //map.put("参数2","")
            }

            //请求成功
            override fun onDataSuccess(response: NetWorkBean<TestGetTokenBean>?) {
                if (response != null) {
                    Log.e("测试", "请求成功")
                }
            }

            //请求失败
            override fun onDataError(code: Int, msg: String?) {
                Log.e("测试", "请求失败")
            }

        })


        //Volley--JsonObjectRequest_Post请求Demo
        NetRequestUtils.getInstance(this).JsonObjectRequest_Post(RequestUrls.ROOT_HOST + RequestUrls.DEMO_URL, TestGetTokenBean::class.java, object : NetRequestUtils.OnPostResponse<TestGetTokenBean> {
            override fun onDataMap(map: MutableMap<String, String>) {
                //添加请求参数
                map.put("number", "2")
            }

            //请求成功
            override fun onDataSuccess(response: NetWorkBean<TestGetTokenBean>?) {
                if (response != null) {
                    Log.e("测试", "请求成功")
                }
            }

            //请求失败
            override fun onDataError(code: Int, msg: String?) {
                Log.e("测试", "请求失败")
            }


        })

        //Volley--JsonObjectRequest_Post请求Demo
        NetRequestUtils.getInstance(this).JsonObjectRequest_Post2(RequestUrls.ROOT_HOST + RequestUrls.DEMO_URL, TestGetTokenBean::class.java, object : NetRequestUtils.OnPostResponse<TestGetTokenBean> {
            override fun onDataMap(map: MutableMap<String, String>) {
                //添加请求参数
                map.put("number", "2")
            }

            //请求成功
            override fun onDataSuccess(response: NetWorkBean<TestGetTokenBean>?) {
                if (response != null) {
                    Log.e("测试", "请求成功")
                }
            }

            //请求失败
            override fun onDataError(code: Int, msg: String?) {
                Log.e("测试", "请求失败")
            }


        })
    }

}