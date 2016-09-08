package com.csy.rx_retrofit.entity;

/**
 * author:Csy on 2016/9/8 17:10
 * e-mail：s1yuan_chen@163.com
 * desc: bean
 */
public class FakeToken {
    public String mToken;
    public boolean mExpired;//过期

    public FakeToken(){}

    public FakeToken(boolean expired){
        this.mExpired = expired;
    }

}
