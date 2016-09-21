package com.csy.rx_retrofit.module.Zip_3;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.csy.rx_retrofit.BaseFragment;
import com.csy.rx_retrofit.R;
import com.csy.rx_retrofit.adapter.ItemListAdapter;
import com.csy.rx_retrofit.entity.ZhuangbiImage;
import com.csy.rx_retrofit.entity.Item;
import com.csy.rx_retrofit.network.Network;
import com.csy.rx_retrofit.utils.GankBeautyResultToItemsMapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * author:Csy on 2016/9/12 11:01
 * e-mail：s1yuan_chen@163.com
 * desc:    压合(zip)
 * 有点时候，app中会需要同时访问不同的接口，然后将结果糅合后转为统一的格式输出(例如：将第三方
 * 广告API夹杂进自家平台返回的数据List中)。这种并行的异步处理比较麻烦，不过用了zip()之后，
 * 就会简单很多。代码形式大致如下：
 * Observable.zip(api.getData(),adApi.getAds(),zipFunc())
 * .subscribeOn(Schedulers.io())
 * .observeOn(AndroidSchedulers.mainThread())
 * .subscribe(observer);
 */
public class ZipFragment extends BaseFragment {

    private static final String TAG = "ZipFragmeng";

    @Bind(R.id.gridRv)
    RecyclerView gridRv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    ItemListAdapter mAdapter = new ItemListAdapter();

    /**
     * 创建观察者  它决定事件触发的时候将有怎样的行为
     */
    Observer<List<Item>> observer = new Observer<List<Item>>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loadng_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<Item> items) {
            swipeRefreshLayout.setRefreshing(false);
            mAdapter.setItems(items);
        }
    };

    /**
     * 加载按钮
     */
    @OnClick(R.id.zipLoadBt)
    void load() {
        swipeRefreshLayout.setRefreshing(true);
        unSubscribe();
        mSubscription = Observable.zip(Network.getGankApi().getBeauties(200, 1).map(GankBeautyResultToItemsMapper.getInstance()),
                Network.getCommonApi().search("装逼"), new Func2<List<Item>, List<ZhuangbiImage>, List<Item>>() {
                    @Override
                    public List<Item> call(List<Item> gankItems, List<ZhuangbiImage> zhuangbiImages) {
                        List<Item> items = new ArrayList<Item>();
                        for (int i = 0; i < gankItems.size() / 2 && i < zhuangbiImages.size(); i++) {// /2干么
                            items.add(gankItems.get(i * 2));
                            items.add(gankItems.get(i * 2 + 1));
                            Item zhuangbiItem = new Item();
                            ZhuangbiImage zhuangbiImage = zhuangbiImages.get(i);
                            zhuangbiItem.description = zhuangbiImage.description;
                            zhuangbiItem.imageUrl = zhuangbiImage.image_url;
                            items.add(zhuangbiItem);
                        }
                        return items;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zip, container, false);
        ButterKnife.bind(this, view);

        gridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        gridRv.setAdapter(mAdapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_zip;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_zip;
    }
}
