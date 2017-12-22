package com.owo.module_c_comment_user.widgets;

import android.support.v4.app.Fragment;

import com.owo.base.AtyBase;
import com.owo.module_c_comment_user.FragCommUserLabbel;
import com.owo.utils.Common;

/**
 * @author XQF
 * @created 2017/5/23
 */
public class AtyCommUser extends AtyBase {
    @Override
    public Fragment createFragment() {
        return FragCommUser.newInstance();
    }

}
