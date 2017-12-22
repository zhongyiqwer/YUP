package com.owo.module_c_mylabel;

import android.support.v4.app.Fragment;

import com.owo.base.AtyBase;
import com.owo.module_a_selectlabel.widgets.FragSlectLabel;

/**
 * @author XQF
 * @created 2017/5/23
 */
public class AtyMyLabel extends AtyBase {
    @Override
    public Fragment createFragment() {
        return FragMyLabel.newInstance();
    }

}
