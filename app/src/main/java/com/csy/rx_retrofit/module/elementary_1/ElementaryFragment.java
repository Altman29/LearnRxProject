package com.csy.rx_retrofit.module.elementary_1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.csy.rx_retrofit.BaseFragment;
import com.csy.rx_retrofit.R;
import com.csy.rx_retrofit.adapter.CommonAdapter;
import com.csy.rx_retrofit.entity.CommonImage;
import com.csy.rx_retrofit.network.Network;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:Csy on 2016/9/8 14:15
 * e-mail：s1yuan_chen@163.com
 * desc:  基本页
 */
public class ElementaryFragment extends BaseFragment{

    private static final String TAG = "ElementaryFragment";

    @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;//SwipeFreshLayout
    @Bind(R.id.gridRv) RecyclerView mGridRv;//RecycleZView

    CommonAdapter mAdapter = new CommonAdapter();

    //Rx!
    Observer<List<CommonImage>> observer = new Observer<List<CommonImage>>() {//观察者
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            mSwipeRefreshLayout.setRefreshing(false);//错误时禁止刷新
            Toast.makeText(getActivity(), R.string.loadng_failed,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<CommonImage> commonImages) {
            mSwipeRefreshLayout.setRefreshing(false);//?为什么禁
            mAdapter.setImages(commonImages);
        }
    };

    @OnCheckedChanged({R.id.searchRb1,R.id.searchRb2,R.id.searchRb3,R.id.searchRb4})
    void onTagChecked(RadioButton searchRb,boolean checked){
        if (checked){
            unSubscribe();
            mAdapter.setImages(null);
            mSwipeRefreshLayout.setRefreshing(true);
            search(searchRb.getText().toString());
        }
    }

    private void search(String key){
        mSubscription = Network.getCommonApi()
                .search(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_elementary,container,false);
        ButterKnife.bind(this,view);

        mGridRv.setLayoutManager(new GridLayoutManager(getActivity(),2));//2?
        mGridRv.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        mSwipeRefreshLayout.setEnabled(false);//不可见?;

        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_elementary;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_elementary;
    }
}
