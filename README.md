# UseOfVolley

包含Volley的网络请求、网络图片的加载
---网络请求包含：
   Volley--StringRequest的Get请求
   Volley--StringRequest的Post请求
   Volley--JsonObjectRequest的Post 传递json参数请求
   Volley--JsonObjectRequest的Post 传递普通参数请求
   以及请求失败时多域名切换
   
---网络图片加载包含：
   Volley--ImageRequest加载
   Volley--ImageLoader加载
   Volley--NetworkImageView加载
   使用LruCache缓存
   

网络请求：
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


网络图片加载：
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
