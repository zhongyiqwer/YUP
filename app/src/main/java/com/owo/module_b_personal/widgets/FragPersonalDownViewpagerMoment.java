package com.owo.module_b_personal.widgets;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.owo.base.FragBase;
import com.owo.module_b_personal.bean.BeanMoment;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.presenter.PresenterPersonal;
import com.owo.module_b_personal.presenter.PresenterPersonalImpl;
import com.owo.module_b_personal.recyclerview.adapter.AdapterFragPersonalDownViewpagerMoment;
import com.owo.module_b_personal.view.ViewPersonalDownViewpagerMoment;
import com.owo.utils.Common;
import com.wao.dogcat.R;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author XQF
 * @created 2017/5/17
 */
public class FragPersonalDownViewpagerMoment extends FragBase implements ViewPersonalDownViewpagerMoment {

    public static FragPersonalDownViewpagerMoment newInstance(BeanUser beanUser) {
        mBeanUser = beanUser;

//        Bundle args = new Bundle();
//        args.putSerializable("userbean", beanUser);
        FragPersonalDownViewpagerMoment fragPersonalDownViewpagerMoment = new FragPersonalDownViewpagerMoment();
//        fragPersonalDownViewpagerMoment.setArguments(args);
        return fragPersonalDownViewpagerMoment;
    }


    @BindView(R.id.recyclerview_my_moment)
    protected RecyclerView mRecyclerView;

    public static AdapterFragPersonalDownViewpagerMoment mAdapterFragPersonalDownViewpagerMoment;
    private PresenterPersonal mPresenterPersonal;
    public static List<BeanMoment> mBeanMoments;
    public static BeanUser mBeanUser;

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.noData)
    TextView noData;

    int code;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_personal_down_viewpager_moment, container, false);
        ButterKnife.bind(this, view);
        mPresenterPersonal = new PresenterPersonalImpl(this);
//        mBeanUser = (BeanUser) getArguments().get("userbean");
        Common.userSP = getActivity().getSharedPreferences("userSP", 0);
        int userId = Common.userSP.getInt("ID", 0);
        mBeanMoments = new ArrayList<>();

        if (mBeanUser!=null)
         userId = mBeanUser.getId();
        mPresenterPersonal.loadUserPersaonlInfoById(userId);
        mPresenterPersonal.loadUserFriendMomentById(userId);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void getResultMomentFromSever(List<BeanMoment> list,int code) {
        mBeanMoments = list;
        this.code = code;
        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();

    }

//    @Override
//    public void getPersonalInfo(BeanUser beanUser) {
//        mBeanUser = beanUser;
//    }
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (mBeanMoments.size()>0) {
                        loading.setVisibility(View.GONE);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mAdapterFragPersonalDownViewpagerMoment = new AdapterFragPersonalDownViewpagerMoment(getActivity());
                        mAdapterFragPersonalDownViewpagerMoment.addItems(mBeanMoments);
                        mRecyclerView.setAdapter(mAdapterFragPersonalDownViewpagerMoment);
                    }else {
                        loading.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        noData.setText("暂时没有任何动态");
                    }
                    break;

            }
        }
    };
}
