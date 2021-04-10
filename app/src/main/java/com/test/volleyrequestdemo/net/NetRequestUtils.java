package com.test.volleyrequestdemo.net;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.test.volleyrequestdemo.VolleyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lyq
 * on 2021-04-08
 *
 * <p>
 * 约定后端返回的正常格式：
 * {
 * "code": 200,
 * "msg": "Berhasil",
 * "data": [
 * {
 * "province_id": 34,
 * "province_name": "PAPUA BARAT"
 * }
 * ]
 * }
 * <p>
 * 非正常格式：
 * {
 * "code": 200,
 * "msg": "Berhasil",
 * "data": ""
 * }
 */
public class NetRequestUtils {
    //跟后端约定的请求成功码
    private final int SUCCESS_CODE = 200;
    //后端数据data里异常
    private final int BACK_DATA_ERROR = 10011;
    //后端数据结构异常
    private final int BACK_JSON_ERROR = 10012;
    //接口请求不通
    private final int BACK_REQUEST_ERROR = 10013;
    //切换域名时，次数
    private int connection = 0;


    private static NetRequestUtils mNetRequestUtils;
    private final RequestQueue requestQueue;

    public NetRequestUtils(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static NetRequestUtils getInstance(Context context) {
        if (mNetRequestUtils == null) {
            synchronized (NetRequestUtils.class) {
                if (mNetRequestUtils == null) {
                    mNetRequestUtils = new NetRequestUtils(context);
                }
            }
        }
        return mNetRequestUtils;
    }

    /**
     * Post请求的回调
     *
     * @param <T>
     */
    public interface OnPostResponse<T> {
        //传递参数
        void onDataMap(Map<String, String> map);

        //请求成功
        void onDataSuccess(NetWorkBean<T> response);

        //请求失败
        void onDataError(int code, String msg);
    }

    /**
     * get请求的回调
     *
     * @param <T>
     */
    public interface OnGetResponse<T> {
        //请求成功
        void onDataSuccess(NetWorkBean<T> response);

        //请求失败
        void onDataError(int code, String msg);
    }

    /**
     * StringRequest Get请求方式
     *
     * @param url
     * @param clazz
     * @param listener
     * @param <T>
     */
    public <T> void StringRequest_Get(String url, final Class<T> clazz, final OnGetResponse<T> listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                数据解析同：StringRequest_Post
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onDataError(BACK_REQUEST_ERROR, "onErrorResponse");
            }
        });
        //超时时间、重新尝试连接次数、曲线增长因子
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0, 1.0f));
        requestQueue.add(stringRequest);

    }

    /**
     * StringRequest Post请求方式
     *
     * @param url
     * @param clazz
     * @param listener
     * @param <T>
     */
    public <T> void StringRequest_Post(String url, final Class<T> clazz, final OnPostResponse<T> listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        NetWorkBean<T> netWorkBean = new NetWorkBean<>();
                        if (jsonObject.has("code") && jsonObject.has("msg")) {
                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");
                            netWorkBean.setCode(code);
                            netWorkBean.setMsg(msg);
                            if (code == SUCCESS_CODE) {
                                if (jsonObject.has("data")) {
                                    //此处是防止后端返回 非正常格式(上方注释所示。。。) 意外情况之一（苦笑。。。。）
                                    if (clazz == String.class) {
                                        String data = jsonObject.getString("data");
                                        netWorkBean.setStr(data);
                                        listener.onDataSuccess(netWorkBean);
                                    } else {
                                        String data = jsonObject.getString("data");
                                        //使用fastJson转为对象
                                        T t = JSON.parseObject(data, clazz);
                                        netWorkBean.setDate(t);
                                        listener.onDataSuccess(netWorkBean);
                                    }
                                } else {
                                    listener.onDataError(BACK_DATA_ERROR, msg);
                                }
                            } else {
                                listener.onDataError(code, msg);
                            }
                        } else {
                            listener.onDataError(BACK_JSON_ERROR, "code == null || msg == null");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    listener.onDataError(BACK_REQUEST_ERROR, "response error || response == null");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //如需切换域名，注释此句
                listener.onDataError(BACK_REQUEST_ERROR, "onErrorResponse");

                //如果不需切换域名忽略下方
                //此处需求要求：如第一个域名请求失败，则自动切换下一个域名，直到所有保存域名全部试完
//                if (EnvironmentUtils.INSTANCE.getSubscript() != 1) {
//                    if (connection < 3) {
//                        connection++;
//                        int index = (int) new SpUtils(VolleyApplication.Companion.getMContext()).getSpParam("hostIndex", 0);
//                        if (index >= RequestUrls.INSTANCE.getHostList().size() - 1) {
//                            index = 0;
//                        } else {
//                            index++;
//                        }
//                        RequestUrls.INSTANCE.setROOT_HOST(RequestUrls.INSTANCE.getHostList().get(index));
//                        new SpUtils(VolleyApplication.Companion.getMContext()).saveSpParam("hostIndex", index);
//                        post(url, clazz, listener);
//                    } else {
//                        listener.onDataError(BACK_REQUEST_ERROR, "onErrorResponse");
//                    }
//                } else {
//                    listener.onDataError(BACK_REQUEST_ERROR, "onErrorResponse");
//                }
            }
        }) {
            //重新getParams()方法，此处可添加公共参数
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                addParams(map);
                listener.onDataMap(map);
                return map;
            }
        };
        //超时时间、重新尝试连接次数、曲线增长因子
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0, 1.0f));
        requestQueue.add(stringRequest);
    }


    private void addParams(Map<String, String> paramsMap) {
        //其他公共参数
        paramsMap.put("token", "");
        paramsMap.put("customer_id", "");
        //等等。。。
    }


    /**
     * JsonObjectRequest Get请求方式
     *
     * @param url
     * @param clazz
     * @param listener
     * @param <T>
     */
    public <T> void JsonObjectRequest_Get(String url, final Class<T> clazz, final OnGetResponse<T> listener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //                数据解析同：StringRequest_Post
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0, 1.0f));
        requestQueue.add(jsonObjectRequest);

    }

    /**
     * JsonObjectRequest Post请求方式
     * 这个类可以用来发送和接收JSON对象
     *
     * @param url
     * @param clazz
     * @param listener
     * @param <T>
     */
    public <T> void JsonObjectRequest_Post(String url, final Class<T> clazz, final OnPostResponse<T> listener) {
        Map<String, String> paramsMap = new HashMap<>();
        addParamsJsonObjectRequest(paramsMap);
        listener.onDataMap(paramsMap);
        JSONObject object = new JSONObject(paramsMap);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //                数据解析同：StringRequest_Post
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0, 1.0f));
        requestQueue.add(jsonObjectRequest);

    }


    /**
     * 给JsonObjectRequest 使用添加公共参数
     *
     * @param paramsMap
     */
    private void addParamsJsonObjectRequest(Map<String, String> paramsMap) {
        //其他公共参数
        paramsMap.put("token", "");
        paramsMap.put("customer_id", "");
        //等等。。。
    }


    /**
     * 正常接口接口请求，接口接收非json格式的请求
     *
     * @param url
     * @param clazz
     * @param listener
     * @param <T>
     *           如果服务端并不支持json的请求方式,比如常见的spring mvc服务端,就很难支持json的请求方式,
     *                 * 那么就需要客户端以普通的post方式进行提交,服务端返回json串
     *                 * 首先在Activity类里,继承Request实现一个NormalPostRequest类
     */
    public <T> void JsonObjectRequest_Post2(String url, final Class<T> clazz, final OnPostResponse<T> listener) {
        Map<String, String> paramsMap = new HashMap<>();
        addParamsJsonObjectRequest(paramsMap);
        listener.onDataMap(paramsMap);

        NormalPostRequest jsonObjectRequest = new NormalPostRequest(url, paramsMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //                数据解析同：StringRequest_Post
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0, 1.0f));
        requestQueue.add(jsonObjectRequest);

    }


}
