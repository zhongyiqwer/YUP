package com.owo.module_b_message.recyclerview.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wao.dogcat.R;

import java.util.List;

/**
 * @author XQF
 * @created 2017/5/17
 */
public class AdapterFragMessageRecommend extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<BeanMessageRecommend> mBeanMessageRecommendList;

    Context mContext;

    public AdapterFragMessageRecommend(Context context, List<BeanMessageRecommend> list) {
        mContext = context;
        mBeanMessageRecommendList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(mContext).inflate(R.layout.frag_message_recommend_item, parent, false);
        return new HolderRecommend(itemview);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mBeanMessageRecommendList.size();
    }
}
