package com.owo.module_b_home.widgets;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.map3d.tools.MapUtil;
import com.amap.map3d.tools.SigninUtil;
import com.baidu.mapapi.map.MapView;
import com.owo.module_a_selectlabel.widgets.AtySelectLabel;
import com.owo.module_b_main.AtyMain;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.owo.base.FragBase;
import com.wao.dogcat.controller.server.TimeService;
import com.wao.dogcat.widget.CircleImageView;
import com.wao.dogcat.widget.CompleteDialog;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import io.jchat.android.chatting.utils.HandleResponseCode;

/**
 * @author XQF
 * @created 2017/5/10
 */
public class FragHomeChanged extends FragBase {

    public static FragHomeChanged newInstance() {
        return new FragHomeChanged();
    }


    @BindView(R.id.toolber_common_header_search_and_recommend)
    protected Toolbar searchView;

    @BindView(R.id.btn_recommend_common_header_search_and_recommend)
    protected Button mBtnRecommend;

    @BindView(R.id.btn_nearby_common_header_search_and_recommend)
    protected Button mBtnNearby;

    private FragmentManager mFragmentManager;
    private FragHomeChangedNearby mFragHomeChangedNearby;
    private FragHomeChangedRecommend mFragHomeChangedRecommend;

    private static String FRAG_RECOMMEND = "recommend";
    private static String FRAG_NEARBY = "bearby";


    private int mUserId;
    private SharedPreferences resumeSP;
    private SharedPreferences.Editor resumeEdit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home_changed_layout, container, false);
        ButterKnife.bind(this, view);

        mFragmentManager = getActivity().getSupportFragmentManager();
        mBtnRecommend.performClick();
//        resumeSP = getContext().getSharedPreferences("resumeSP", 0);
//        resumeEdit = resumeSP.edit();
//        resumeEdit.putInt("resume", 0);
//        resumeEdit.commit();
        searchView.setVisibility(View.INVISIBLE);

        Common.userSP = getActivity().getSharedPreferences("userSP", 0);
        mUserId = Common.userSP.getInt("ID", 0);


        return view;
    }



    @OnClick(R.id.btn_recommend_common_header_search_and_recommend)
    public void onBtnRecommend() {
        mBtnRecommend.setTextColor(getResources().getColor(R.color.word_color_white));
        mBtnRecommend.setBackgroundColor(getResources().getColor(R.color.toolbar_bg_color));

        if (mFragHomeChangedRecommend == null) {
            mFragHomeChangedRecommend = new FragHomeChangedRecommend();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_content, mFragHomeChangedRecommend, FRAG_RECOMMEND);
            fragmentTransaction.show(mFragHomeChangedRecommend);
            fragmentTransaction.commit();
            mFragHomeChangedNearby = null;

            mBtnNearby.setTextColor(getResources().getColor(R.color.word_color_black));
            mBtnNearby.setBackgroundColor(getResources().getColor(R.color.tab_color_normal));
        }

    }

    @OnClick(R.id.btn_nearby_common_header_search_and_recommend)
    public void onBtnNearby() {
        mBtnNearby.setTextColor(getResources().getColor(R.color.word_color_white));
        mBtnNearby.setBackgroundColor(getResources().getColor(R.color.toolbar_bg_color));

        if (mFragHomeChangedNearby == null) {
            mFragHomeChangedNearby = new FragHomeChangedNearby();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_content, mFragHomeChangedNearby, FRAG_NEARBY);
            fragmentTransaction.show(mFragHomeChangedNearby);
            fragmentTransaction.commit();
            mFragHomeChangedRecommend = null;

            mBtnRecommend.setTextColor(getResources().getColor(R.color.word_color_black));
            mBtnRecommend.setBackgroundColor(getResources().getColor(R.color.tab_color_normal));
        }

    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
//        System.out.println("SINGLE____________________destory");
//        resumeEdit = resumeSP.edit();
//        resumeEdit.putInt("resume", 0);
//        resumeEdit.commit();

    }

}
