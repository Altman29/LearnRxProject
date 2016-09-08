package com.csy.rx_retrofit.network;

import com.csy.rx_retrofit.network.api.CommonApi;
import com.csy.rx_retrofit.network.api.FakeApi;
import com.csy.rx_retrofit.network.api.GankApi;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author:Csy on 2016/9/8 16:49
 * e-mail：s1yuan_chen@163.com
 * desc: 网络访问的假实现。
 */
public class Network {
    private static CommonApi commonApi;
    private static GankApi gankApi;
    private static FakeApi fakeApi;
    private static OkHttpClient okHttpClient = new OkHttpClient();//okhttp
    private static Converter.Factory gsonConvertFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static CommonApi getCommonApi(){
        if (null == commonApi){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://zhuangbi.info/")
                    .addConverterFactory(gsonConvertFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            commonApi = retrofit.create(CommonApi.class);
        }
        return commonApi;
    }

    public static GankApi getGankApi(){
        if (null == gankApi){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(gsonConvertFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            gankApi = retrofit.create(GankApi.class);
        }
        return gankApi;
    }

    public static FakeApi getFakeApi(){
        if (null == fakeApi){
            fakeApi = new FakeApi();
        }
        return fakeApi;
    }
}
