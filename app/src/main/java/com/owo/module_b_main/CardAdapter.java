package com.owo.module_b_main;

import android.support.v7.widget.CardView;

/**
 * Created by XQF on 2017/5/12.
 */
public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 5;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}