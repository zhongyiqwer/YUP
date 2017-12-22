package com.owo.module_b_explore.bean;

import com.owo.module_b_personal.bean.BeanUser;

/**
 * @author XQF
 * @created 2017/5/15
 */
public class BeanViewPagerItem {

    BeanUser mBeanUser;
    int mMatchDegree;

    public BeanUser getBeanUser() {
        return mBeanUser;
    }

    public void setBeanUser(BeanUser beanUser) {
        mBeanUser = beanUser;
    }

    public int getMatchDegree() {
        return mMatchDegree;
    }

    public void setMatchDegree(int matchDegree) {
        mMatchDegree = matchDegree;
    }
}
