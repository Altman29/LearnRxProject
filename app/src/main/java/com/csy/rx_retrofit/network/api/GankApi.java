package com.csy.rx_retrofit.network.api;

import com.csy.rx_retrofit.entity.GankBeautyResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * author:Csy on 2016/9/8 17:02
 * e-mail：s1yuan_chen@163.com
 * desc: gank api    Retrofit_Rx ?
 */
public interface GankApi {
    @GET("data/福利/{number}/{page}")
    Observable<GankBeautyResult> getBeauties(@Path("number") int number, @Path("page") int page);
}
