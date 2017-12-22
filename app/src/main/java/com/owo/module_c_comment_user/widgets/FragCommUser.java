package com.owo.module_c_comment_user.widgets;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.owo.base.FragBase;
import com.owo.module_a_selectlabel.FragSelectLabelFindFriend;
import com.owo.module_a_selectlabel.FragSelectLabelSelf;
import com.owo.module_a_selectlabel.bean.BeanTag;
import com.owo.module_a_selectlabel.presenter.PresenterSelectLabel;
import com.owo.module_a_selectlabel.presenter.PresenterSelectLabelImpl;
import com.owo.module_a_selectlabel.view.ViewSelectLabel;
import com.owo.module_b_main.AtyMain;
import com.owo.module_c_comment_user.FragCommUserLabbel;
import com.owo.module_c_comment_user.presenter.PresenterCommUserImpl;
import com.owo.module_c_comment_user.view.ViewCommUser;
import com.owo.module_c_detail.AtyDetail;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;

/**
 * @author XQF
 * @created 2017/5/24
 */
public class FragCommUser extends FragBase implements ViewCommUser {


    @BindView(R.id.btn_next_step)
    protected Button mBtnNextStep;

    @BindView(R.id.title)
    protected TextView title;

    private FragCommUserLabbel mFragSelectLabelSelf;

    private FragmentManager mFragmentManager;


    private int mUserId;

    private int statusParam;

    @OnClick(R.id.btnBack)
    public void back() {
        getActivity().finish();
    }

    public static FragCommUser newInstance() {

        FragCommUser fragment = new FragCommUser();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.aty_comm_user, container, false);
        ButterKnife.bind(this, view);
        // mUserId = Common.userSP.getInt("ID", 0);
        mFragmentManager = getActivity().getSupportFragmentManager();
        if (mFragSelectLabelSelf == null) {
            mFragSelectLabelSelf = FragCommUserLabbel.newInstance(Common.half.getSex());
        }
        mFragmentManager.beginTransaction().add(R.id.frag_container_aty_select_label, mFragSelectLabelSelf).commit();

        Intent i = getActivity().getIntent();
        statusParam = i.getIntExtra("param",0);

        return view;
    }


    @OnClick(R.id.btn_next_step)
    public void onClickBtnNext() {

        Common.showProgressDialog("提交评价...", getContext());

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ////////////////////////

                //提交评价
                ArrayList<HashMap<String, String>> allParams = mFragSelectLabelSelf.getParams();

                System.out.println("评价："+allParams);
                if (allParams.size() > 0) {

                    try {
                        String json = "";

                        for (int i = 0; i < allParams.size(); i++) {
                          json  = HttpHelper.postData(MyURL.INSERT_USER_COMMENT,
                                            allParams.get(i), null);

                            if (i==allParams.size()-1){
                                if (HttpHelper.getCode(json) == 200) {
                                    Looper.prepare();
                                    Common.dismissProgressDialog(getContext());
                                    Common.display(getContext(), "评价成功");
                                    JMessageClient.createSingleTextMessage("yup_"+Common.halfId, "4d454221295c35af705cc26e", "我对你进行了评价，快去查看吧!");
//                            if (statusParam == 2) //跳转至用户主页
//                            {
//
//                                //AtyDetail.startAtyDetail(getContext(), AtyDetail.class, mBeanUser);
//                            }
                                    getActivity().finish();
                                    Looper.loop();
                                } else {
                                    Looper.prepare();
                                    Common.dismissProgressDialog(getContext());
                                    Common.display(getContext(), "提交失败：CODE " + HttpHelper.getCode(json));
                                    Looper.loop();
                                }

                            }

                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                        Looper.prepare();
                        Common.dismissProgressDialog(getContext());
                        Common.display(getContext(), "提交失败：Exception ");
                        Looper.loop();
                    }


                } else {
                    Looper.prepare();
                    Common.dismissProgressDialog(getContext());
                    Common.display(getContext(), "没有选择任何标签哦");
                    Looper.loop();
                }


                ////////////////////////
            }

        };
        timer.schedule(task, 100);


    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    };

    @Override
    public void getLabelSelfFormNet(List<List<BeanTag>> resultList) {

    }
}
