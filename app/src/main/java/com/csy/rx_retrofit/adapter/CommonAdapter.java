package com.csy.rx_retrofit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csy.rx_retrofit.R;
import com.csy.rx_retrofit.entity.CommonImage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author:Csy on 2016/9/8 14:50
 * e-mail：s1yuan_chen@163.com
 * desc: 通用Adapter
 */
public class CommonAdapter extends RecyclerView.Adapter{

    private static final String TAG = "CommonAdapter";

    List<CommonImage> mImages;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        return new DebouceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DebouceViewHolder viewHolder = (DebouceViewHolder) holder;
        CommonImage image = mImages.get(position);
        Glide.with(holder.itemView.getContext()).load(image.image_url).into(viewHolder.imageIv);//Glide
        viewHolder.descriptionTv.setText(image.description);
    }

    @Override
    public int getItemCount() {
        return mImages == null ? 0 : mImages.size();//空就是0，反之是size
    }

    /**
     * 设置图片？？
     * @param images
     */
    public void setImages(List<CommonImage> images){
        this.mImages = images;
        notifyDataSetChanged();
    }

    static class DebouceViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.imageIv) ImageView imageIv;//item的空间
        @Bind(R.id.descriptionTv) TextView descriptionTv;
        public DebouceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
