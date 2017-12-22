package com.owo.module_b_explore.recyclerview;

/**
 * @author XQF
 * @created 2017/5/11
 */
public class MessageItem {
    private String mStr;


    public MessageItem(int i) {
        mStr = i + " ";
    }

    public String getStr() {
        return mStr;
    }

    public void setStr(String str) {
        mStr = str;
    }
}
