package com.owo.module_b_personal.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.owo.module_b_personal.bean.BeanMoment;
import com.owo.module_b_personal.recyclerview.holder.HolderMoment;
import com.wao.dogcat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XQF
 * @created 2017/5/17
 */
public class AdapterFragPersonalDownViewpagerMoment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context mContext;
    List<BeanMoment> mBeanMoments;
    public static int thisPosition;


    public AdapterFragPersonalDownViewpagerMoment(Context context) {
        mContext = context;
        mBeanMoments = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(mContext).inflate(R.layout.frag_personal_camera_item, parent, false);
        return new HolderMoment(mContext,itemview);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mBeanMoments!=null) {
            thisPosition = position;
            BeanMoment beanMoment = mBeanMoments.get(position);
            if (holder instanceof HolderMoment) {
                ((HolderMoment) holder).bind(beanMoment);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mBeanMoments!=null)
        return mBeanMoments.size();
        else return 0;
    }

    public void addItems(List<BeanMoment> list) {
        mBeanMoments = list;
    }

}