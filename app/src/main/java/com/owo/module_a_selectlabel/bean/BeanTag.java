package com.owo.module_a_selectlabel.bean;

/**
 * @author XQF
 * @created 2017/5/27
 */
public class BeanTag {

    private int mType;
    private String mTag;
    private int id;


    private int counter;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BeanTag{" +
                "mType=" + mType +
                ", mTag='" + mTag + '\'' +
                ", id=" + id +
                ", counter=" + counter +
                '}';
    }
}

