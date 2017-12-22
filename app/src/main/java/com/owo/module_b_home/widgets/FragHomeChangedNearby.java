package com.owo.module_b_home.widgets;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.owo.module_b_home.recyclerview.changed.bean.BeanItemFragHomeChanged;
import com.owo.module_b_home.recyclerview.changed.bean.BeanColorTask;
import com.owo.module_b_personal.recyclerview.adapter.AdapterFragPersonalDownViewpagerMoment;
import com.owo.utils.Common;
import com.owo.utils.Constants;
import com.owo.utils.MapCalculator;
import com.wao.dogcat.R;
import com.owo.base.FragBase;
import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_home.presenter.PresenterHomeImpl;
import com.owo.module_b_home.presenter.PresneterHome;
import com.owo.module_b_home.recyclerview.changed.adapter.AdapterHomeChangedRecyclerview;
import com.owo.module_b_home.view.ViewHomeChangedNearby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author XQF
 * @created 2017/5/11
 */
public class FragHomeChangedNearby extends FragBase implements ViewHomeChangedNearby {


    @BindView(R.id.recyclerview)
    protected RecyclerView mRecyclerView;

    private AdapterHomeChangedRecyclerview mAdapterHomeChangedRecyclerview;

    private PresneterHome mPresneterHome;
    //private List<List<BeanColorTask>> mBeanTasksResult;
    private List<BeanTask> nearByTask;
    private List<BeanColorTask> taskBlue;
    private List<BeanColorTask> taskRed;
    private List<BeanColorTask> taskYellow;
    private List<BeanItemFragHomeChanged> beanItemFragHomeChanged = new ArrayList<>();

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.noData)
    TextView noData;

    private int userID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home_changed_nearby_layout, container, false);
        ButterKnife.bind(this, view);
        mPresneterHome = new PresenterHomeImpl(this);
        Common.userSP = getActivity().getSharedPreferences("userSP", 0);
        userID = Common.userSP.getInt("ID", 0);
        mPresneterHome.loadTasksNearbyByLatitudeAndLongtitude(Common.myLatitude + "", Common.myLongtitude + "",userID);

        nearByTask = new ArrayList<>();
        taskBlue = new ArrayList<>();
        taskRed = new ArrayList<>();
        taskYellow = new ArrayList<>();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public BeanColorTask taskToColorTask(BeanTask task,int viewType,int locID) {
        BeanColorTask beanBlue = new BeanColorTask();
        beanBlue.setAvatatrUrl(task.getAvatar());
        double dis =
                MapCalculator.getDistance(Common.myLatitude + "", Common.myLongtitude + "",
                        task.getTaskLatitude(), task.getTaskLongitude());
        beanBlue.setDis(dis);
        beanBlue.setId(task.getTaskId() + "");
        beanBlue.setmPubTime(task.getTaskPublishTime());
        beanBlue.setName(task.getUserName());
        beanBlue.setSex(task.getSex() + "");
        beanBlue.setSum(task.getTaskTakenNum() + "");
        beanBlue.setTime(task.getTaskDeadLine());
        beanBlue.setTitle(task.getTaskName());
        beanBlue.setUserID(task.getTaskUserID() + "");
        beanBlue.setmTaskLatitude(task.getTaskLatitude());
        beanBlue.setmTaskLongitude(task.getTaskLongitude());
        beanBlue.setmMax(task.getTaskMaxNum() + "");
        beanBlue.setViewType(viewType);
        beanBlue.setLocID(locID);
        beanBlue.setImage(task.getTaskIamge());
        beanBlue.setStatus(task.getTaskStatus());
        beanBlue.setMoney(task.getTaskMoney());
        beanBlue.setTaskScore(task.getTaskScore());
        beanBlue.setTaskType(task.getTaskType());
        beanBlue.setTaskContent(task.getTaskContent());
        return beanBlue;

    }


    @Override
    public void getAllTaskOrderByDistance(List<BeanTask> tasks) {
        nearByTask = tasks;
        for (int i = 0; i < nearByTask.size(); i++) {
            BeanTask task = nearByTask.get(i);

            if (task.getTaskType()==1) {
                taskBlue.add(taskToColorTask(task, Constants.FRAG_HOME_ITEM_VIEWTYPE_BLUE,i));
            } else if (task.getTaskType()==3) {
                taskYellow.add(taskToColorTask(task,Constants.FRAG_HOME_ITEM_VIEWTYPE_YELLOW,i));

            } else {
                taskRed.add(taskToColorTask(task,Constants.FRAG_HOME_ITEM_VIEWTYPE_RED,i));
            }
        }
        List<BeanColorTask> taskMerge = new ArrayList<>();
        taskMerge.addAll(taskBlue);
        taskMerge.addAll(taskYellow);
        taskMerge.addAll(taskRed);
        Collections.sort(taskMerge);

        beanItemFragHomeChanged.addAll(taskMerge);


        nearByTask.clear();
        taskMerge.clear();
        taskBlue.clear();
        taskYellow.clear();
        taskRed.clear();

        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (beanItemFragHomeChanged.size()>0) {
                        loading.setVisibility(View.GONE);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mAdapterHomeChangedRecyclerview = new AdapterHomeChangedRecyclerview(getActivity());
                        mAdapterHomeChangedRecyclerview.addItems(beanItemFragHomeChanged);
                        mRecyclerView.setAdapter(mAdapterHomeChangedRecyclerview);
                    }
                    break;


            }
        }
    };
}
