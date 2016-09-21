package com.csy.rx_retrofit.module.cache_6;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.csy.rx_retrofit.BaseFragment;
import com.csy.rx_retrofit.R;
import com.csy.rx_retrofit.adapter.ItemListAdapter;
import com.csy.rx_retrofit.entity.Item;
import com.csy.rx_retrofit.module.cache_6.data.Data;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * author:Csy on 2016/9/12 15:22
 * e-mail：s1yuan_chen@163.com
 * desc:        缓存(BehaviorSubject)
 * RxJava中有一个较少被使用的类叫做Subject它是一种"既是Observable，又是Observer"的东西。
 * 因此可以被用作中间件来做数据传递。例如：可以用它的子类BehaviorSubject来制作缓存。
 * 代码大致如下：
 * <p/>
 * api.getData()
 * .subscribe(behaviorSubject); //网络数据会被缓存
 * <p/>
 * behaviorSubject.subscribe(observer); //之前的缓存将直接送达observer
 */
public class CacheFragment extends BaseFragment {

    @Bind(R.id.loadingTimeTv)
    TextView loadingTimeTv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.cacheRv)
    RecyclerView cacheRc;
    ItemListAdapter mAdapter = new ItemListAdapter();
    private long startingTime;


    @OnClick(R.id.clearMemoryCacheBt)
    //清空内存缓存
    void clearMemortCache() {
        Data.getInstance().clearMemoryCache();
        mAdapter.setItems(null);
        Toast.makeText(getActivity(), R.string.memory_cache_cleared, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.clearMemoryAndDiskCacheBt)
    //清空内存和磁盘缓存
    void clearMemoryAndDiskCache() {
        Data.getInstance().clearMemoryAndDiskCache();
        mAdapter.setItems(null);
        Toast.makeText(getActivity(), R.string.memory_and_disk_cache_cleared, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.loadBt)//加载
    void load() {
        swipeRefreshLayout.setRefreshing(true);
        startingTime = System.currentTimeMillis();
        unSubscribe();
        mSubscription = Data.getInstance()
                .subscribeData(new Observer<List<Item>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.loadng_failed, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<Item> items) {
                        swipeRefreshLayout.setRefreshing(false);
                        int loadingTime = (int) (System.currentTimeMillis() - startingTime);
                        loadingTimeTv.setText(getString(R.string.loading_time_and_source, loadingTime, Data.getInstance().getDataSourceText()));
                        mAdapter.setItems(items);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cache, container, false);
        ButterKnife.bind(this, view);

        cacheRc.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        cacheRc.setAdapter(mAdapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_cache;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_cache;
    }

}
