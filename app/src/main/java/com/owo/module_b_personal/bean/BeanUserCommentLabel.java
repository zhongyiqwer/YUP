package com.owo.module_b_personal.bean;

/**
 * @author XQF
 * @created 2017/5/28
 */
public class BeanUserCommentLabel {
    private String mLabel;
    private String mLabelNum;

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getLabelNum() {
        return mLabelNum;
    }

    public void setLabelNum(String labelNum) {
        mLabelNum = labelNum;
    }

    @Override
    public String toString() {
        return "BeanUserCommentLabel{" +
                "mLabel='" + mLabel + '\'' +
                ", mLabelNum='" + mLabelNum + '\'' +
                '}';
    }
}
