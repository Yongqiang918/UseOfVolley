package com.test.volleyrequestdemo.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.test.volleyrequestdemo.net.NetRequestUtils;

/**
 * Created by Lyq
 * on 2021-04-10
 */
public class NetImgUtils {
    private int maxWidth = 0;
    private int maxHeight = 0;
    private static NetImgUtils mNetImgUtils;
    private final RequestQueue requestQueue;
    private final ImageLoader imageLoader;

    public NetImgUtils(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new BitmapCache());
    }

    public static NetImgUtils getInstance(Context context) {
        if (mNetImgUtils == null) {
            synchronized (NetRequestUtils.class) {
                if (mNetImgUtils == null) {
                    mNetImgUtils = new NetImgUtils(context);
                }
            }
        }
        return mNetImgUtils;
    }

    public interface OnResponse{
        void onResponse(Bitmap response);
        void onErrorResponse(String errorMsg);
    }

    /**
     * 使用imageRequest 进行网络图片加载
     * @param url
     * @param listener
     *
     *ImageRequest大致可以分为以下三步：
     * 1. 创建一个RequestQueue对象。
     * 2. 创建一个ImageRequest对象。
     * 3. 将ImageRequest对象添加到RequestQueue里面。
     */
    public void imageRequest_method(String url, final OnResponse listener) {
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        listener.onResponse(response);
                    }
                }, maxWidth, maxHeight, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse(error.getMessage());
            }
        });
        requestQueue.add(imageRequest);
    }


    /**
     * 未经优化\未经优化\未经优化
     * 使用imageLoader 进行网络图片加载(未经优化的 imageLoader 的加载方式)
     * @param url 要加载的图片地址
     * @param imageView 要展示的布局上的imageview
     * @param loadingImg 正在加载展示的图片资源（不需要传0）
     * @param errorImg 加载失败展示的图片资源（不需要传0）
     *
     *
     * ImageLoader大致可以分为以下四步：
     * 1. 创建一个RequestQueue对象。（构造方法已经实现）
     * 2. 创建一个ImageLoader对象。
     * 3. 获取一个ImageListener对象。
     * 4. 调用ImageLoader的get()方法加载网络上的图片。
     *
     */
    public void imageLoader_method(String url, ImageView imageView,int loadingImg,int errorImg) {
        //创建一个ImageLoader对象
        ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });
        //获取一个ImageListener对象
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, loadingImg, errorImg);
        //调用ImageLoader的get()方法加载网络上的图片
        imageLoader.get(url, imageListener,maxWidth,maxHeight);
    }

    /**
     * 优化后的
     * 使用imageLoader 进行网络图片加载
     * @param url 图片路径
     * @param imageListener
     *
     * 把imageLoader 也放在构造方法中，在单例中初始化，只执行一次
     * ImageCache 使用自定义的BitmapCache（LruCache）
     * 在使用时把ImageListener当做参数传递进来，只执行ImageLoader的get()方法
     */
    public void imageLoader_method(String url, ImageLoader.ImageListener imageListener) {
        imageLoader.get(url, imageListener,maxWidth,maxHeight);
    }

    /**
     * 使用networkImageView 进行网络图片加载
     * @param url  图片路径
     * @param imageView  展示图片的networkImageView
     * @param loadingImg 加载中展示的图片
     * @param errorImg  加载失败展示的图片
     */
    public void networkImageView_method(String url, NetworkImageView imageView, int loadingImg, int errorImg) {
        if(loadingImg !=0){
            imageView.setDefaultImageResId(loadingImg);
        }
        if(errorImg !=0){
            imageView.setErrorImageResId(errorImg);
        }
        imageView.setImageUrl(url, imageLoader);
    }

















}
