package com.owo.module_b_explore.widgets;

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

import com.bumptech.glide.Glide;
import com.owo.module_b_explore.bean.BeanRecyclerViewItem;
import com.owo.module_b_explore.presenter.PresenterExplore;
import com.owo.module_b_explore.presenter.PresenterExploreImpl;
import com.owo.module_b_explore.recyclerview.AdapterRecyclerView;
import com.owo.module_b_explore.view.ViewExploreNearby;
import com.wao.dogcat.R;
import com.owo.base.FragBase;
import com.owo.utils.Common;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author XQF
 * @created 2017/5/11
 */
public class FragExploreNearby extends FragBase implements ViewExploreNearby {
    public static FragExploreNearby newInstance() {
        return new FragExploreNearby();
    }

    @BindView(R.id.recyclerview_frag_explore_nearby)
    protected RecyclerView mRecyclerView;

    private List<BeanRecyclerViewItem> mBeanRecyclerViewItemList;
    private PresenterExplore mPresenterExplore;
    private int mUserId;

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.noData)
    TextView noData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_explore_nearby_layout, container, false);
        ButterKnife.bind(this, view);
        mBeanRecyclerViewItemList = new ArrayList<>();
        Common.userSP = getActivity().getSharedPreferences("userSP", 0);
        mUserId = Common.userSP.getInt("ID", 0);
        mPresenterExplore = new PresenterExploreImpl(this);
        mPresenterExplore.getUserOrderByDistance(Common.myLatitude+"",Common.myLongtitude+"",mUserId);

        return view;
    }

    @Override
    public void getResultUserByDis(List<BeanRecyclerViewItem> list) {
        mBeanRecyclerViewItemList = list;
        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        for (int i = 0; i < mBeanRecyclerViewItemList.size(); i++) {
//            if (mBeanRecyclerViewItemList.get(i).getCounter() % 2 == 0) {
//                int friendId = mBeanRecyclerViewItemList.get(i).getUserId();
//                mPresenterExplore.sendAddFriendByIdAndFriendId(mUserId, friendId); //添加朋友
//            }
//        }
    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (mBeanRecyclerViewItemList.size()>0) {
                        loading.setVisibility(View.GONE);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        AdapterRecyclerView adapter = new AdapterRecyclerView(getActivity());
                        adapter.addItems(mBeanRecyclerViewItemList);
                        mRecyclerView.setAdapter(adapter);
                    }else {
                        loading.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        noData.setText("暂时没搜寻到任何用户");
                    }
                    break;
            }
        }
    };


}
