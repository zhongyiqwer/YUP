package com.owo.module_a_selectlabel;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.owo.module_a_selectlabel.bean.BeanTag;

import java.util.List;

import zhouyou.flexbox.adapter.TagAdapter;

/**
 * @author XQF
 * @created 2017/5/28
 */
class StringTagAdapter extends TagAdapter<StringTagView, BeanTag> {

    StringTagAdapter(Context context, List<BeanTag> data) {
        this(context, data, null);
    }

    StringTagAdapter(Context context, List<BeanTag> data, List<BeanTag> selectItems) {
        super(context, data, selectItems);
    }

    /**
     * 检查item和所选item是否一样
     *
     * @param view
     * @param item
     * @return
     */
    @Override
    protected boolean checkIsItemSame(StringTagView view, BeanTag item) {
        return TextUtils.equals(view.getItem().getTag(), item.getTag());
    }

    /**
     * 检查item是否是空指针
     *
     * @return
     */
    @Override
    protected boolean checkIsItemNull(BeanTag item) {
        return TextUtils.isEmpty(item.getTag());
    }

    /**
     * 添加标签
     *
     * @param item
     * @return
     */
    @Override
    protected StringTagView addTag(BeanTag item) {
        StringTagView tagView = new StringTagView(getContext());
        tagView.setPadding(20, 20, 20, 20);

        TextView textView = tagView.getTextView();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView.setGravity(Gravity.CENTER);

        tagView.setItemDefaultDrawable(itemDefaultDrawable);
        tagView.setItemSelectDrawable(itemSelectDrawable);
        tagView.setItemDefaultTextColor(itemDefaultTextColor);
        tagView.setItemSelectTextColor(itemSelectTextColor);
        tagView.setItem(item);
        return tagView;
    }
}
