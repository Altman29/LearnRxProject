package com.csy.rx_retrofit.module.token_retryWhen_5;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.csy.rx_retrofit.BaseFragment;
import com.csy.rx_retrofit.R;
import com.csy.rx_retrofit.entity.FakeThing;
import com.csy.rx_retrofit.entity.FakeToken;
import com.csy.rx_retrofit.network.api.FakeApi;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * author:Csy on 2016/9/12 14:06
 * e-mail：s1yuan_chen@163.com
 * desc:    有些token并非一次性的，而是可以多次使用，直到它超时或被销毁(多数token都是这样的)。这样的token
 *          处理比较麻烦：需要把它保存起来，并且在发现它失效的时候要能够自动重新获取新的token，并继续访问
 *          之前由于token失效而失败的请求。如果项目中有多处接口都需要这样的自动修复机制，使用传统的Callback
 *          形式需要写出非常复杂的代码。而使用RxJava，可以使用retryWhen()来轻松处理这样的问题，代码大致如下:
 *
 *          api.getData(token)
 *              .retryWhen(observable ->
 *                  observable.flatMap(->
 *                      api.getToken()
 *                          .doOnNext(-> updateToken())))
 *                  .subscribeOn(Schedulers.io())
 *                  .observerOn(AndroidSchedulers.mainThread())
 *                  .subscribe(observer);
 */
public class TokenAdvancedFragment extends BaseFragment {

    @Bind(R.id.tokenTv)    TextView tokenTv;
    @Bind(R.id.swipeRefreshLayout)    SwipeRefreshLayout swipeRefreshLayout;

    FakeToken cacheFakeToken = new FakeToken(true);
    boolean tokenUpdated;//更新

    @OnClick(R.id.invalidateTokenBt)
    void invalidatedToken(){
        cacheFakeToken.mExpired = true;//销毁后，设置过期
        Toast.makeText(getActivity(), R.string.token_destroyed, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.requestBt)//请求数据
    void upload(){
        tokenUpdated = false;
        swipeRefreshLayout.setRefreshing(true);
        unSubscribe();
        final FakeApi fakeApi = new FakeApi();
        mSubscription = Observable.just(null)
                .flatMap(new Func1<Object, Observable<FakeThing>>() {
                    @Override
                    public Observable<FakeThing> call(Object o) {
                        return cacheFakeToken.mToken == null
                                ? Observable.<FakeThing>error(new NullPointerException("Token is null"))
                                :fakeApi.getFakeData(cacheFakeToken);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                if (throwable instanceof IllegalArgumentException || throwable instanceof NullPointerException){
                                    return fakeApi.getFakeToken("fake_auth_code")
                                            .doOnNext(new Action1<FakeToken>() {
                                                @Override
                                                public void call(FakeToken fakeToken) {
                                                    tokenUpdated =true;
                                                    cacheFakeToken.mToken = fakeToken.mToken;
                                                    cacheFakeToken.mExpired = fakeToken.mExpired;
                                                }
                                            });
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FakeThing>() {
                    @Override
                    public void call(FakeThing fakeData) {
                        swipeRefreshLayout.setRefreshing(false);
                        String token = cacheFakeToken.mToken;
                        if (tokenUpdated) {
                            token += "(" + getString(R.string.updated) + ")";
                        }
                        tokenTv.setText(getString(R.string.got_token_and_data, token, fakeData.mId, fakeData.mName));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.loadng_failed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_advanced,container,false);
        ButterKnife.bind(this,view);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setRefreshing(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_token_advanced;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_token_advanced;
    }
}
