package com.owo.module_b_explore.widgets;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.owo.base.FragBase;
import com.owo.module_a_selectlabel.widgets.FragSlectLabel;
import com.owo.module_b_explore.bean.BeanRecyclerViewItem;
import com.owo.module_b_explore.presenter.PresenterExplore;
import com.owo.module_b_explore.recyclerview.AdapterRecyclerView;
import com.owo.utils.Common;
import com.owo.utils.MapCalculator;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author XQF
 * @created 2017/5/5
 */
public class FragSearchUser extends FragBase {

    @BindView(R.id.recyclerview_frag_explore_nearby)
    protected RecyclerView mRecyclerView;

    private List<BeanRecyclerViewItem> mBeanRecyclerViewItemList;

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.navBar)
    LinearLayout navBar;

    @BindView(R.id.noData)
    TextView noData;

    public static FragSearchUser newInstance() {
        FragSearchUser fragment = new FragSearchUser();
        return fragment;
    }

    @OnClick(R.id.btnBack)
    public void back(){
        getActivity().finish();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_explore_nearby_layout, container, false);
        ButterKnife.bind(this, view);

        navBar.setVisibility(View.VISIBLE);
        mBeanRecyclerViewItemList = new ArrayList<>();

        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();

        return view;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = getActivity().getIntent();
                    String param = intent.getStringExtra("param");
                    if (param!=null && param.trim().length()>0){
                        ArrayList<HashMap<String, Object>> userList = searchUserByName(param);
                        if (userList.size()>0){
                            converter(userList);
                        }
                    }


                    if (mBeanRecyclerViewItemList.size()>0) {
                        loading.setVisibility(View.GONE);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        AdapterRecyclerView adapter = new AdapterRecyclerView(getActivity());
                        adapter.addItems(mBeanRecyclerViewItemList);
                        mRecyclerView.setAdapter(adapter);
                    }else {
                        loading.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        noData.setText("没有找到合适的用户，请尝试其他关键字");
                    }
                    break;
            }
        }
    };

    public  ArrayList<HashMap<String, Object>> searchUserByName(String username){
        ArrayList<HashMap<String, Object>> userList = new ArrayList<>();
        try {
            HashMap<String,String> map = new HashMap<>();
            map.put("username",username);
            String json = HttpHelper.postData(MyURL.SEARCH_USER_BY_NAME,map,null);
            userList = HttpHelper.AJUsers(json);
        }catch (Exception e){
            e.printStackTrace();
        }

        return userList;
    }

    public void converter(ArrayList<HashMap<String, Object>> userList){
        for (HashMap<String, Object> userMap:userList){
            BeanRecyclerViewItem beanItem = new BeanRecyclerViewItem();
            beanItem.setUsername(userMap.get("username").toString());
            beanItem.setSignature(userMap.get("signature").toString());
            beanItem.setHeight((int)userMap.get("height"));
            beanItem.setAge((int)userMap.get("age"));
            beanItem.setWeight((int)userMap.get("weight"));
            beanItem.setUrlAvatar(userMap.get("avatar").toString());
            beanItem.setUserId((int)userMap.get("id"));
            beanItem.setSex(userMap.get("sex").toString().trim().length()>0?Integer.parseInt(userMap.get("sex").toString().trim()):1);
            String longtitude = userMap.get("longtitude").toString();
            String latitude = userMap.get("latitude").toString();
            double distance = MapCalculator.getDistance(latitude,longtitude, Common.myLatitude+"",Common.myLongtitude+"");
            beanItem.setDis(distance);
            mBeanRecyclerViewItemList.add(beanItem);
        }

    }



}
