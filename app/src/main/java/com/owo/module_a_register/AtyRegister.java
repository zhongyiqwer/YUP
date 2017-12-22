package com.owo.module_a_register;

import android.support.v4.app.Fragment;

import com.owo.base.AtyBase;
import com.owo.module_a_register.widgets.FragRegister;

/**
 * @author XQF
 * @created 2017/5/18
 */
public class AtyRegister extends AtyBase {

    @Override
    public Fragment createFragment() {
        return FragRegister.newInstance();
    }

}
