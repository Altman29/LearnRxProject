package com.csy.rx_retrofit.network.api;

import android.support.annotation.NonNull;

import com.csy.rx_retrofit.entity.FakeThing;
import com.csy.rx_retrofit.entity.FakeToken;

import java.util.Random;

import rx.Observable;
import rx.functions.Func1;

/**
 * author:Csy on 2016/9/8 17:08
 * e-mail：s1yuan_chen@163.com
 * desc: 网络访问模拟  138 3715 1215
 */
public class FakeApi {
    Random mRandom = new Random();

    /**被观察者，map 转换*/
    public Observable<FakeToken> getFakeToken(@NonNull String fakeAuth){
        return Observable.just(fakeAuth)
                .map(new Func1<String, FakeToken>() {
                    @Override
                    public FakeToken call(String fakeAuth) {
                        //Add some random delay to mock the network delay
                        int fakeNetworkTimeCost = mRandom.nextInt(500)+500;
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        FakeToken fakeToken = new FakeToken();
                        fakeToken.mToken = createToken();
                        return fakeToken;
                    }
                });
    }

    private static String createToken(){
        return "fake_token_" + System.currentTimeMillis() % 10000;
    }

    //被观察者，map 转换
    public Observable<FakeThing> getFakeData(FakeToken fakeToken){
        return Observable.just(fakeToken)
                .map(new Func1<FakeToken, FakeThing>() {
                    @Override
                    public FakeThing call(FakeToken fakeToken) {
                        //Add some random delay mock the network delay
                        int fakeNetworkTimeCost = mRandom.nextInt(500)+500;
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (fakeToken.mExpired){
                            throw new IllegalArgumentException("Token expired!");
                        }

                        FakeThing fakeData = new FakeThing();
                        fakeData.mId = (int) (System.currentTimeMillis()%1000);
                        fakeData.mName = "FAKE_USER_"+fakeData.mId;
                        return fakeData;
                    }
                });
    }

}
