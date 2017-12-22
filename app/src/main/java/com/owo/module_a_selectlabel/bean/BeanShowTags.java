package com.owo.module_a_selectlabel.bean;

import java.util.List;

/**
 * @author XQF
 * @created 2017/5/27
 */
public class BeanShowTags {

    private String mTitles;
    private List<BeanTag> mTags;

    public String getTitles() {
        return mTitles;
    }

    public void setTitles(String titles) {
        mTitles = titles;
    }

    public List<BeanTag> getTags() {
        return mTags;
    }

    public void setTags(List<BeanTag> tags) {
        mTags = tags;
    }

    @Override
    public String toString() {
        return "BeanShowTags{" +
                "mTitles='" + mTitles + '\'' +
                ", mTags=" + mTags +
                '}';
    }
}
