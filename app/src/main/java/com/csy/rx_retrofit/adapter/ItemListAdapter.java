package com.csy.rx_retrofit.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csy.rx_retrofit.R;
import com.csy.rx_retrofit.entity.Item;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author:Csy on 2016/9/9 15:09
 * e-mail：s1yuan_chen@163.com
 * desc: MapFragment 填充器
 *         /** 如果需要实现瀑布流，仅需要在onBindViewHolder中这是item的高度随机即可。
 * 拓展：
 *      RecycleView：整体上看RecyclerView架构，提供了一种插拔式的体验，高度的解耦，异常的灵活。
 *      通过设置它提供的不同的LayoutManager，ItemDecoration，ItemAnimator实现个各种效果。
 *      若想要控制其显示方式，可以通过布局管理器LayoutManager
 *      若想要控制Item间的间隔(可绘制)，可以通过ItemDecoration
 *      若想要控制Item增删的动画，可以通过ItemAnimator
 *      若想要控制点击、长按事件、可以自己写。。。
 *
 *      详细见：
 *      http://blog.csdn.net/lmj623565791/article/details/45059587
 */
public class ItemListAdapter extends RecyclerView.Adapter {

    private static final String TAG = "ItemListAdapter";

    List<Item> images;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        return new DebounceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DebounceViewHolder debounceViewHolder = (DebounceViewHolder) holder;
        Item image = images.get(position);
        Glide.with(holder.itemView.getContext()).load(image.imageUrl).into(debounceViewHolder.imageIv);
        debounceViewHolder.descriptionTv.setText(image.description);
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    /** 设置图片*/
    public void setItems(List<Item> images){
        this.images = images;
        notifyDataSetChanged();/** 设置后更新*/
    }



    /** 创建ViewHolder  Debounce防抖动*/
    static class DebounceViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.imageIv) ImageView imageIv;
        @Bind(R.id.descriptionTv) TextView descriptionTv;

        public DebounceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
