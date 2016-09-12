package com.csy.rx_retrofit.network.api;

import com.csy.rx_retrofit.entity.ZhuangbiImage;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * author:Csy on 2016/9/8 16:52
 * e-mail：s1yuan_chen@163.com
 * desc: 网络 q ?
 */
public interface CommonApi {
    @GET("search")
    Observable<List<ZhuangbiImage>> search(@Query("q") String query);
}
