package com.owo.module_b_home.view;

import com.owo.module_b_home.bean.BeanTask;

import java.util.List;

/**
 * Created by XQF on 2017/5/20.
 */
public interface ViewHomeChangedNearby {
    void getAllTaskOrderByDistance(List<BeanTask> tasks);
}
