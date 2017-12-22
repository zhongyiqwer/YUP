package com.owo.module_a_selectlabel.widgets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.owo.base.AtyBase;
import com.owo.module_b_explore.widgets.FragExploreRecommend;
import com.wao.dogcat.R;
import com.owo.module_a_selectlabel.FragSelectLabelFindFriend;
import com.owo.module_a_selectlabel.FragSelectLabelSelf;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author XQF
 * @created 2017/5/23
 */
public class AtySelectLabel extends AtyBase {


    @Override
    public Fragment createFragment() {
        return FragSlectLabel.newInstance();
    }


//    @BindView(R.id.btn_next_step)
//    protected Button mBtnNextStep;
//
//
//    private FragSelectLabelFindFriend mFragSelectLabelFindFriend;
//    private FragCommUserLabbel mFragSelectLabelSelf;
//
//    private FragmentManager mFragmentManager;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.aty_select_label);
//        ButterKnife.bind(this);
//
//
//    }
//
//    @OnClick(R.id.btn_next_step)
//    public void onClickNextStep() {
//
//    }
}
