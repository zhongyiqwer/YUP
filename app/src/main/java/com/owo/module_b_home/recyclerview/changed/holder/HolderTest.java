package com.owo.module_b_home.recyclerview.changed.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * @author XQF
 * @created 2017/5/14
 */
public abstract class HolderTest<T> extends RecyclerView.ViewHolder {

    public HolderTest(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void bind(T t);
}
