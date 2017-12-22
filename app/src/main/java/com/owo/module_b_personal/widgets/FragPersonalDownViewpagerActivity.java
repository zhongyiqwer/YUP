package com.owo.module_b_personal.widgets;

import android.os.Bundle;
import android.os.CountDownTimer;
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


import com.owo.base.BeanBase;
import com.owo.base.FragBase;
import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_personal.bean.BeanActivityLabel;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.presenter.PresenterPersonal;
import com.owo.module_b_personal.presenter.PresenterPersonalImpl;
import com.owo.module_b_personal.recyclerview.adapter.AdapterFragPersonalDownViewpagerActivity;
import com.owo.module_b_personal.recyclerview.adapter.AdapterFragPersonalDownViewpagerMoment;
import com.owo.module_b_personal.view.ViewPersonalDownViewpagerActivity;
import com.owo.utils.Common;
import com.owo.utils.Constants;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author XQF
 * @created 2017/5/17
 */
public class FragPersonalDownViewpagerActivity extends FragBase implements ViewPersonalDownViewpagerActivity {


    public static FragPersonalDownViewpagerActivity newInstance(BeanUser beanUser) {
        mBeanUser = beanUser;
//        Bundle args = new Bundle();
//        args.putSerializable("userbean", beanUser);
        FragPersonalDownViewpagerActivity fragPersonalDownViewpagerActivity = new FragPersonalDownViewpagerActivity();
 //       fragPersonalDownViewpagerActivity.setArguments(args);
        return fragPersonalDownViewpagerActivity;
    }


    private PresenterPersonal mPresenterPersonal;
    private List<BeanTask> mBeanTaskList;

    private List<List<BeanTask>> mBeanTasksResult;

    private List<BeanTask> mBeanTaskTop;
    private List<BeanTask> mBeanTaskMid;
    private List<BeanTask> mBeanTaskBottom;

    private List<BeanBase> mBeanBases;

    private String[] mlabels = new String[]{
            "接收报名中",
            "正在进行中",
            "已结束"
    };


    @BindView(R.id.recyclerview_my_activity)
    protected RecyclerView mRecyclerView;

    private AdapterFragPersonalDownViewpagerActivity mAdapterFragPersonalDownViewpagerActivity;
    public static BeanUser mBeanUser;
    private boolean flag = false;

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.noData)
    TextView noData;

    int code1,code2;
    int userId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_personal_down_viewpager_activity, container, false);
        ButterKnife.bind(this, view);
        mPresenterPersonal = new PresenterPersonalImpl(this);
//        mBeanUser = (BeanUser) getArguments().get("userbean");
        Common.userSP = getActivity().getSharedPreferences("userSP", 0);
         userId = Common.userSP.getInt("ID", 0);
        if (mBeanUser!=null)
            userId = mBeanUser.getId();
        //mPresenterPersonal.loadUserPersaonlInfoById(userId);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBeanTaskList = new ArrayList<>();
        mBeanTaskTop = new ArrayList<>();
        mBeanTaskMid = new ArrayList<>();
        mBeanTaskBottom = new ArrayList<>();
        mBeanBases = new ArrayList<>();
        mBeanTasksResult = new ArrayList<>();

      //  mPresenterPersonal.loadTasksAppliedByUserId(userId);
        mPresenterPersonal.loadTasksPublishedByUserId(userId);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

//    @Override
//    public void getTasksIApplied(List<BeanTask> taskList,int code) {
//        mBeanTaskList.addAll(taskList);
//        code1 = code;
//        flag = true;
//    }

    @Override
    public void getTasksIPublished(List<BeanTask> taskList,int code) {
        List<BeanTask> applytaskList = new ArrayList<>();
        HashMap<String,String> param = new HashMap<>();
        param.put("userID",userId+"");
        try {
            String jsonStr = HttpHelper.postData(MyURL.GET_TASK_I_APPLIED, param, null);
            applytaskList.addAll(HttpHelper.getBeanTasksFromJson(jsonStr));
        }catch (Exception e){
            e.printStackTrace();
        }

        mBeanTaskList.addAll(applytaskList);
        mBeanTaskList.addAll(taskList);
        code2 = code;
        //if (flag){

            for (int i = 0; i < mBeanTaskList.size(); i++) {
                BeanTask beanTask = mBeanTaskList.get(i);
                if (beanTask.getTaskStatus() == Constants.TASK_STATUS_ENROOLING) {
                    mBeanTaskTop.add(beanTask);
                } else if (beanTask.getTaskStatus() == Constants.TASK_STATUS_WORKING) {
                    mBeanTaskMid.add(beanTask);
                } else if (beanTask.getTaskStatus() == Constants.TASK_STATUS_FINISHED) {
                    mBeanTaskBottom.add(beanTask);
                }
            }

            mBeanTaskList.clear();

            mBeanTasksResult.add(mBeanTaskTop);
            mBeanTasksResult.add(mBeanTaskMid);
            mBeanTasksResult.add(mBeanTaskBottom);

            System.out.println("mBeanTasksResult="+mBeanTasksResult);

            for (int i = 0; i < mBeanTasksResult.size(); i++) {
                mBeanBases.add(new BeanActivityLabel(mlabels[i]));
                mBeanBases.addAll(mBeanTasksResult.get(i));
            }

            System.out.println("mBeanBases="+mBeanBases);

            new Thread() {
                @Override
                public void run() {
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }.start();

       // }
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
                    if (mBeanTaskTop.size()>0 || mBeanTaskMid.size()>0 || mBeanTaskBottom.size()>0)  {
                        loading.setVisibility(View.GONE);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mAdapterFragPersonalDownViewpagerActivity = new AdapterFragPersonalDownViewpagerActivity(getActivity());
                        mAdapterFragPersonalDownViewpagerActivity.addItems(mBeanBases);
                        mRecyclerView.setAdapter(mAdapterFragPersonalDownViewpagerActivity);
                    }else if (mBeanTaskTop.size()==0 && mBeanTaskMid.size()==0 && mBeanTaskBottom.size()==0){
                        //timer.start();
                        loading.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        noData.setText("暂时没有任何活动");
                    }
                    break;


            }
        }
    };




    private CountDownTimer timer = new CountDownTimer(3000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            loading.setVisibility(View.GONE);
            noData.setText("暂时没有任何活动");
        }
    };




}
