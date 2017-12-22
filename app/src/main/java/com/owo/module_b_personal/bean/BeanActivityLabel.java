package com.owo.module_b_personal.bean;


import com.owo.base.BeanBase;
import com.owo.utils.Constants;

/**
 * @author XQF
 * @created 2017/5/21
 */
public class BeanActivityLabel extends BeanBase {


    private String mContent;

    public BeanActivityLabel(String str) {
        mContent = str;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    @Override
    public int getBeanType() {
        return Constants.BEAN_TYPE_LABEL;
    }
}
