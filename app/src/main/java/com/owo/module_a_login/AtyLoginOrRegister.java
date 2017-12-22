package com.owo.module_a_login;

import android.support.v4.app.Fragment;

import com.owo.base.AtyBase;
import com.owo.module_a_login.widgets.FragLoginOrRegister;

/**
 * 登陆主界面或者去注册
 *
 * @author XQF
 * @created 2017/5/18
 */
public class AtyLoginOrRegister extends AtyBase {
    @Override
    public Fragment createFragment() {
        return FragLoginOrRegister.newInstance();
    }
}
