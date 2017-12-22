package com.owo.module_b_home;

import android.support.v4.app.Fragment;

import com.owo.base.AtyBase;
import com.owo.module_b_explore.widgets.FragSearchUser;
import com.owo.module_b_home.widgets.FragSearchTask;

/**
 * Created by ppssyyy on 2017-09-01.
 */
public class SearchTaskAty extends AtyBase {
    @Override
    public Fragment createFragment() {
        return FragSearchTask.newInstance();
    }
}
