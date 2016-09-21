package com.csy.rx_retrofit.module.map_2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.csy.rx_retrofit.BaseFragment;
import com.csy.rx_retrofit.R;
import com.csy.rx_retrofit.adapter.ItemListAdapter;
import com.csy.rx_retrofit.entity.Item;
import com.csy.rx_retrofit.network.Network;
import com.csy.rx_retrofit.utils.GankBeautyResultToItemsMapper;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * author:Csy on 2016/9/9 14:46
 * e-mail：s1yuan_chen@163.com
 * desc: map页
 *      转换(map)
 *      有些服务器接口设计，会在返回的数据外包裹一些额外的信息。这些信息对于调试很有用，但是本地显示用不到那么多。
 *      使用map()可以把最外层的剥下来，只留下核心数据，代码形式大致如下：
 *          api.getData()
 *              .map(response -> response.data)
 *              .subscribeOn(Schedulers.io())
 *              .observeOn(AndroidSchedulers.mainThread())
 *              .subscribe(observer);
 */
public class MapFragment extends BaseFragment {

    private static final String TAG = "MapFragment";

    private int page = 0;//页码

    @Bind(R.id.pageTv)    TextView pageTv;
    @Bind(R.id.previousPageBt)    Button previousPageBt;//上一页
    @Bind(R.id.swipeRefreshLayout)    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.gridRv)    RecyclerView gridRv;

    ItemListAdapter mAdapter = new ItemListAdapter();

    /**
     * 创建Obsever：它决定事件触发的时候将有怎样的行为。
     */
    Observer<List<Item>> observer = new Observer<List<Item>>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            /** 失败 设置swiperefreshlayout不可刷新*/
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loadng_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<Item> items) {
            swipeRefreshLayout.setRefreshing(false);//加载后禁止用户刷新
            pageTv.setText(getString(R.string.page_with_number, page));/** string中使用%s 占位，这里page赋值*/
            mAdapter.setItems(items);
        }
    };

    /**
     * 上一页
     */
    @OnClick(R.id.previousPageBt)
    void previousPage() {
        loadPage(--page);
        if (1 == page)
            previousPageBt.setEnabled(false);
    }

    /**
     * 下一页
     */
    @OnClick(R.id.nextPageBt)
    void nextPag() {
        loadPage(++page);
        if (2 == page)
            previousPageBt.setEnabled(true);
    }

    /**
     * 加载页面的数据
     *
     * @param page
     */
    private void loadPage(int page) {
        swipeRefreshLayout.setRefreshing(true);
        unSubscribe();//加载前，先释放                 //BaseFragmen定义
        mSubscription = Network.getGankApi()         //BaseFragment定义
                .getBeauties(10, page)                //获取数据源
                .map(GankBeautyResultToItemsMapper.getInstance())    //map转换数据源以供显示。
                .subscribeOn(Schedulers.io())                    //事件开始在io线程 因为是请求网络的。
                .observeOn(AndroidSchedulers.mainThread())      //最后显示在页面UI 所以设置成主线程。
                .subscribe(observer);/** 订阅*/
    }

    /**
     * Fragment的onCreateView()是创建该Fragment对应的视图，必须在这里创建自己的视图并返回给调用者，
     * 其中super.onCreateView()无所谓，因为它是直接返回的null。
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        ButterKnife.bind(this,view);

        /** RecycleView设置  2列  Vertical 纵向    StaggeredGridLayoutManager 是瀑布流的布局管理器*/
        gridRv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        gridRv.setAdapter(mAdapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);//设置进度条的大小
        swipeRefreshLayout.setEnabled(false);//?
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_map;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_map;
    }
}
