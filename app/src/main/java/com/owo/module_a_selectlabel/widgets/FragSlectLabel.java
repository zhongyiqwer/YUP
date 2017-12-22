package com.owo.module_a_selectlabel.widgets;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.owo.module_b_explore.recyclerview.AdapterRecyclerView;
import com.wao.dogcat.R;
import com.owo.base.FragBase;
import com.owo.module_a_selectlabel.FragSelectLabelFindFriend;
import com.owo.module_a_selectlabel.FragSelectLabelSelf;
import com.owo.module_a_selectlabel.bean.BeanTag;
import com.owo.module_a_selectlabel.presenter.PresenterSelectLabel;
import com.owo.module_a_selectlabel.presenter.PresenterSelectLabelImpl;
import com.owo.module_a_selectlabel.view.ViewSelectLabel;
import com.owo.module_b_main.AtyMain;
import com.owo.utils.Common;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author XQF
 * @created 2017/5/24
 */
public class FragSlectLabel extends FragBase implements ViewSelectLabel {


    @BindView(R.id.btn_next_step)
    protected Button mBtnNextStep;

    @BindView(R.id.title)
    protected TextView title;

    private FragSelectLabelFindFriend mFragSelectLabelFindFriend;
    private FragSelectLabelSelf mFragSelectLabelSelf;

    private FragmentManager mFragmentManager;

    private PresenterSelectLabel mPresenterSelectLabel;

    private int mUserId;

    private int count;

    @OnClick(R.id.btnBack)
    public void back(){
        if (Common.selectLabelState == Common.SELECT_SELF_LABEL) {
            getActivity().finish();
        }else if (Common.selectLabelState == Common.SELECT_FIND_LABEL) {
            Common.selectLabelState = Common.SELECT_SELF_LABEL;
            mFragSelectLabelSelf = FragSelectLabelSelf.newInstance(Common.user.getSex());
            mFragmentManager.beginTransaction().replace(R.id.frag_container_aty_select_label, mFragSelectLabelSelf).commit();
            mFragSelectLabelFindFriend = null;
            new Thread() {
                @Override
                public void run() {
                    Message msg = handler.obtainMessage();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }.start();
        }
    }

    public static FragSlectLabel newInstance() {

        FragSlectLabel fragment = new FragSlectLabel();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.aty_select_label, container, false);
        ButterKnife.bind(this, view);

        Common.selectLabelState = Common.SELECT_SELF_LABEL;
        mUserId =Common.userSP.getInt("ID", 0);
        mPresenterSelectLabel = new PresenterSelectLabelImpl(this);
        mFragmentManager = getActivity().getSupportFragmentManager();
        if (mFragSelectLabelSelf == null) {
            mFragSelectLabelSelf = FragSelectLabelSelf.newInstance(Common.user.getSex());
        }
        mFragmentManager.beginTransaction().add(R.id.frag_container_aty_select_label, mFragSelectLabelSelf).commit();
        return view;
    }

    /**
     * 本情景中的两次点击按钮
     */

    @OnClick(R.id.btn_next_step)
    public void onClickBtnNext() {
        //count++;
        if (Common.selectLabelState == Common.SELECT_SELF_LABEL) {
            Common.selectLabelState = Common.SELECT_FIND_LABEL;
            //切换到选择交友标签
            String selfLabels = mFragSelectLabelSelf.getSelfLabels();
            if (selfLabels.trim().length()>0) {
                mFragSelectLabelFindFriend = FragSelectLabelFindFriend.newInstance(mFragSelectLabelSelf.getSelfLabels());
                mFragmentManager.beginTransaction().replace(R.id.frag_container_aty_select_label, mFragSelectLabelFindFriend).commit();
                mFragSelectLabelSelf = null;
                new Thread() {
                    @Override
                    public void run() {
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }.start();
            }else Common.display(getContext(),"至少要选择4个标签哦~");

        } else if (Common.selectLabelState == Common.SELECT_FIND_LABEL) {

            String hobby = mFragSelectLabelFindFriend.getHobby();
            if (hobby.trim().length()>0) {
                mPresenterSelectLabel.updateHobbyById(mUserId, hobby);
                System.out.println("HHHHHHHHHOBY===" + hobby);
                getActivity().finish();
            }else Common.display(getContext(),"至少要选择4个标签哦~");

        }
    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    title.setText("选择交友标签");
                    break;
                case 2:
                    title.setText("选择自我标签");
                    break;
            }
        }
    };

    @Override
    public void getLabelSelfFormNet(List<List<BeanTag>> resultList) {

    }
}
