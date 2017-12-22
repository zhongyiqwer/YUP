package com.wao.dogcat.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.owo.module_a_login.AtyLoginOrRegister;
import com.owo.module_b_main.AtyMain;
import com.wao.dogcat.R;


public class GuideActivity extends Activity {

    private View view1, view2, view3,view4;
    private ViewPager viewPager;  //对应的viewPager

    private List<View> viewList;//view数组


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        LayoutInflater inflater=getLayoutInflater();
        view1 = inflater.inflate(R.layout.layout_1, null);
        view2 = inflater.inflate(R.layout.layout_2,null);
        view3 = inflater.inflate(R.layout.layout_3, null);
        view4 = inflater.inflate(R.layout.layout_4, null);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);


        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container,final int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));

                final View view = viewList.get(position);
                view.setOnClickListener (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position==viewList.size()-1){
                            Intent intent1 = new Intent();
                            finish();
                            intent1.setClass(GuideActivity.this, AtyLoginOrRegister.class);
                            startActivity(intent1);
                        }


                    }
                });

                return view;
            }


        };


        viewPager.setAdapter(pagerAdapter);

    }


}