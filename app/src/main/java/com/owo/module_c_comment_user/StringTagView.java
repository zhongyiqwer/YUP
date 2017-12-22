package com.owo.module_c_comment_user;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.owo.module_a_selectlabel.bean.BeanTag;

import zhouyou.flexbox.widget.BaseTagView;


/**
 * @author XQF
 * @created 2017/5/28
 */
class StringTagView extends BaseTagView<BeanTag> {

    public StringTagView(Context context) {
        this(context, null);
    }

    public StringTagView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public StringTagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setItem(BeanTag item) {
        super.setItem(item);
        textView.setText(item.getTag());
    }
}