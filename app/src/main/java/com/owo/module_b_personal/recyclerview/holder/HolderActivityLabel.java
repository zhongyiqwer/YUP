package com.owo.module_b_personal.recyclerview.holder;

import android.view.View;
import android.widget.TextView;

import com.owo.base.HolderBase;
import com.owo.module_b_personal.bean.BeanActivityLabel;
import com.wao.dogcat.R;

import butterknife.BindView;


/**
 * @author XQF
 * @created 2017/5/21
 */
public class HolderActivityLabel extends HolderBase<BeanActivityLabel> {
    @BindView(R.id.item_label)
    protected TextView mTextViewLabel;

    public HolderActivityLabel(View itemView) {
        super(itemView);
    }

    public void bind(BeanActivityLabel beanActivityLabel) {
        mTextViewLabel.setText(beanActivityLabel.getContent());
    }
}
