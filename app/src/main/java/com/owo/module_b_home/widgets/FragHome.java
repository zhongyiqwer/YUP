package com.owo.module_b_home.widgets;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.owo.module_b_explore.SearchUserAty;
import com.owo.module_b_home.SearchTaskAty;
import com.wao.dogcat.R;
import com.owo.base.FragBase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author XQF
 * @created 2017/5/4
 */
public class FragHome extends FragBase {

    private static final String FRAG_NORMAL = "normal";
    private static final String FRAG_CHANGED = "changed";


    //状态切换按钮
    @BindView(R.id.toolbar_btn_frag_home_change_state)
    protected LinearLayout mBtnToolbarFragHomeChangeState;

//


    //    搜索栏
    @BindView(R.id.toolbar_searchview_frag_home)
    protected SearchView searchView;

    private FragmentManager mFragmentManager;
    private FragmentTransaction fragmentTransaction;


    private FragHomeChanged mFragHomeChanged;

    private FragHomeNormal mFragHomeNormal;

    private Fragment mContent;
//    @BindView(R.id.switch_txt)
//    TextView switchTxt;
    private int count = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home_layout, container, false);
        ButterKnife.bind(this, view);
        mFragHomeNormal = FragHomeNormal.newInsstance();
        mFragHomeChanged = FragHomeChanged.newInstance();

        //mSearchView.setVisibility(View.GONE);
        setDefaultFrag(mFragHomeNormal);


        // 设置搜索文本监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                start(getContext(), SearchTaskAty.class, query);
                return true;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });



        return view;
    }


    /**
     * 按钮成功完成切换
     */
    @OnClick(R.id.toolbar_btn_frag_home_change_state)
    public void test() {
        count++;

        if (count%2==0) {
            //地图模式（默认）
            switchContent(mFragHomeNormal);
            //searchView.setVisibility(View.VISIBLE);
           // switchTxt.setText("活动大厅");

        }else {
            //列表模式
            switchContent(mFragHomeChanged);
            //searchView.setVisibility(View.GONE);
           // switchTxt.setText("地图模式");
        }


//        if (mFragHomeChanged == null) {
//            //切换到列表模式
//            mSearchView.setVisibility(View.GONE);
//            mFragHomeChanged = FragHomeChanged.newInstance();
//            mFragmentManager.beginTransaction().replace(R.id.frag_home_main_content, mFragHomeChanged).commit();
//            mFragHomeNormal = null;
//            switchTxt.setText("活动大厅");
//
//        }
//    if (mFragHomeNormal == null) {
//        //切换到地图模式
//        mSearchView.setVisibility(View.VISIBLE);
//        mFragHomeNormal = FragHomeNormal.newInsstance();
//        mFragmentManager.beginTransaction().replace(R.id.frag_home_main_content, mFragHomeNormal).commit();
//        mFragHomeChanged = null;
//        switchTxt.setText("地图模式");
//    }

    }


    public void setDefaultFrag(Fragment fragment) {
        mFragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frag_home_main_content, fragment).commit();
        mContent = fragment;
    }

    public void switchContent(Fragment to){
        //如果当前frag跟要切换的frag不同-->进行切换
        if (mContent!=to){
            mFragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = mFragmentManager.beginTransaction();
            if (!to.isAdded()){
                fragmentTransaction.hide(mContent).add(R.id.frag_home_main_content, to).commit();
            }else {
                fragmentTransaction.hide(mContent).show(to).commit();
            }
        }
        mContent = to;
    }
}
