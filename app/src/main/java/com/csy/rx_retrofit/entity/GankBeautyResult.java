package com.csy.rx_retrofit.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author:Csy on 2016/9/8 17:03
 * e-mail：s1yuan_chen@163.com
 * desc:  ?
 * @SerializedName 作用是把与entity不一致的字段名连接
 */
public class GankBeautyResult {
    public boolean error;
    public @SerializedName("results") List<GankBeauty> beauties;
}
