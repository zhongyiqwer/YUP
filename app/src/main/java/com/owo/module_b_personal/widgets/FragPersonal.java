package com.owo.module_b_personal.widgets;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.owo.base.FragBase;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.presenter.PresenterPersonal;
import com.owo.module_b_personal.presenter.PresenterPersonalImpl;
import com.owo.module_b_personal.view.ViewPersonal;
import com.owo.utils.Common;
import com.wao.dogcat.R;
import com.wao.dogcat.controller.StoreActivity;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author XQF
 * @created 2017/5/5
 */
public class FragPersonal extends FragBase implements ViewPersonal {


    @BindView(R.id.frag_personal_viewpager_up)
    protected ViewPager mViewPagerUp;

//    @BindView(R.id.frag_personal_viewpager_down)
//    protected ViewPager mViewPagerDown;
//
//    @BindView(R.id.tablayout)
//    protected TabLayout mTabLayout;


    private FragPersonalViewpagerRight mFragPersonalViewpagerRight;
    private FragPersonalViewpagerLeft mFragPersonalViewpagerLeft;
    private List<Fragment> mlist;

    private BeanUser mBeanUser;
    private PresenterPersonal mPresenterPersonal;

    private FragmentManager mFragmentManager;
    private FragPersonalDownViewpagerActivity fragPersonalDownViewpagerActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_personal_layout, container, false);
        ButterKnife.bind(this, view);
        mPresenterPersonal = new PresenterPersonalImpl(this);
        Common.userSP = getActivity().getSharedPreferences("userSP", 0);
        int userId = Common.userSP.getInt("ID", 0);
        mPresenterPersonal.loadUserPersaonlInfoById(userId);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    /**
     * 个人主页的上半部分
     */
    private void initUp() {
        mFragPersonalViewpagerLeft = FragPersonalViewpagerLeft.newInstance(mBeanUser);
        mFragPersonalViewpagerRight = FragPersonalViewpagerRight.newInstance(mBeanUser);
        mlist = new ArrayList<>();
        mlist.add(mFragPersonalViewpagerLeft);
        mlist.add(mFragPersonalViewpagerRight);
        mViewPagerUp.setAdapter(new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mlist.get(position);
            }

            @Override
            public int getCount() {
                return mlist.size();
            }
        });
        initDown();
    }


    /**
     * 个人主页的下半部分
     */
    private void initDown() {
//        AdapterDownViewPager adapterDownViewPager = new AdapterDownViewPager(getActivity().getSupportFragmentManager());
//        adapterDownViewPager.add(FragPersonalDownViewpagerMoment.newInstance(mBeanUser), "动态");
//        adapterDownViewPager.add(FragPersonalDownViewpagerActivity.newInstance(mBeanUser), "活动");
//        mViewPagerDown.setAdapter(adapterDownViewPager);
//        mTabLayout.setupWithViewPager(mViewPagerDown);
        mFragmentManager = getActivity().getSupportFragmentManager();
        if (fragPersonalDownViewpagerActivity == null){
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragPersonalDownViewpagerActivity = FragPersonalDownViewpagerActivity.newInstance(mBeanUser);
            fragmentTransaction.replace(R.id.frag_person_act,fragPersonalDownViewpagerActivity,"活动");
            fragmentTransaction.show(fragPersonalDownViewpagerActivity);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void getPersonalInfo(BeanUser beanUser) {
        mBeanUser = beanUser;
        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();

    }


//    class AdapterDownViewPager extends FragmentPagerAdapter {
//
//        List<String> titleList;
//        List<Fragment> frags;
//
//        public AdapterDownViewPager(FragmentManager fm) {
//            super(fm);
//            titleList = new ArrayList<>();
//            frags = new ArrayList<>();
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return frags.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return frags.size();
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titleList.get(position);
//        }
//
//        public void add(Fragment fragment, String title) {
//            frags.add(fragment);
//            titleList.add(title);
//        }
//    }


//    @OnClick(R.id.item)
//    public void onClickItem() {
//        Intent i = new Intent();
//        i.setClass(getContext(), StoreActivity.class);
//        getContext().startActivity(i);
//    }
//
//    @OnClick(R.id.egg)
//    public void onClickEgg() {
//
//    }
//
//    @OnClick(R.id.medal)
//    public void onClickMedal() {
//
//    }
//
//    @OnClick(R.id.pose)
//    public void onClickPose() {
////        Intent i = new Intent();
////        i.setClass(getContext(), UserActivity.class);
////        getContext().startActivity(i);
//    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                    case 1:
                    initUp();
                    break;
            }
        }
    };


}
