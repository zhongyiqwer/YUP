package com.owo.module_b_explore;

import android.support.v4.app.Fragment;

import com.owo.base.AtyBase;
import com.owo.module_b_explore.widgets.FragSearchUser;

/**
 * Created by ppssyyy on 2017-09-01.
 */
public class SearchUserAty extends AtyBase {
    @Override
    public Fragment createFragment() {
        return FragSearchUser.newInstance();
    }
}
