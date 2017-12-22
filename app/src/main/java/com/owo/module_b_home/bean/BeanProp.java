package com.owo.module_b_home.bean;

/**
 * @author XQF
 * @created 2017/5/20
 */
//道具
public class BeanProp {
    private int mPropId;
    private String mPropName;
    private String mPropFunction;
    private int mPropPrice;
    private String mPropImage;

    private int mPropType;//类型
    private int mPropLevel;//等级限制
    private int mPropPrivilege;//权限
    private int mCount;

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public int getPropId() {
        return mPropId;
    }

    public void setPropId(int propId) {
        mPropId = propId;
    }

    public String getPropName() {
        return mPropName;
    }

    public void setPropName(String propName) {
        mPropName = propName;
    }

    public String getPropFunction() {
        return mPropFunction;
    }

    public void setPropFunction(String propFunction) {
        mPropFunction = propFunction;
    }

    public int getPropPrice() {
        return mPropPrice;
    }

    public void setPropPrice(int propPrice) {
        mPropPrice = propPrice;
    }

    public String getPropImage() {
        return mPropImage;
    }

    public void setPropImage(String propImage) {
        mPropImage = propImage;
    }

    public int getPropType() {
        return mPropType;
    }

    public void setPropType(int propType) {
        mPropType = propType;
    }

    public int getPropLevel() {
        return mPropLevel;
    }

    public void setPropLevel(int propLevel) {
        mPropLevel = propLevel;
    }

    public int getPropPrivilege() {
        return mPropPrivilege;
    }

    public void setPropPrivilege(int propPrivilege) {
        mPropPrivilege = propPrivilege;
    }


}
