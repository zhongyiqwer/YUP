package com.owo.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * @author XQF
 * @created 2017/5/17
 */
public abstract class HolderBase<T> extends RecyclerView.ViewHolder {

    public HolderBase(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void bind(T t);
}
