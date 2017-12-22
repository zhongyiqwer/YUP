package com.owo.module_b_home.widgets;

import android.content.Intent;
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
import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_home.recyclerview.changed.adapter.AdapterHomeChangedRecyclerview;
import com.owo.module_b_home.recyclerview.changed.bean.BeanColorTask;
import com.owo.module_b_home.recyclerview.changed.bean.BeanItemFragHomeChanged;
import com.owo.utils.Common;
import com.owo.utils.Constants;
import com.owo.utils.MapCalculator;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author XQF
 * @created 2017/5/5
 */
public class FragSearchTask extends FragBase {

    @BindView(R.id.recyclerview_frag_explore_nearby)
    protected RecyclerView mRecyclerView;
    private   List<BeanTask>  taskList ;
    private List<BeanColorTask> taskBlue;
    private List<BeanColorTask> taskRed;
    private List<BeanColorTask> taskYellow;
    private List<BeanItemFragHomeChanged> beanItemFragHomeChanged = new ArrayList<>();

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.navBar)
    LinearLayout navBar;

    @BindView(R.id.noData)
    TextView noData;

    public static FragSearchTask newInstance() {
        FragSearchTask fragment = new FragSearchTask();
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
        taskList = new ArrayList<>();
        taskBlue = new ArrayList<>();
        taskRed = new ArrayList<>();
        taskYellow = new ArrayList<>();

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
                      taskList = searchTaskByName(param);
                    }


                    if (taskList.size()>0) {
                        loading.setVisibility(View.GONE);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        AdapterHomeChangedRecyclerview adapter = new AdapterHomeChangedRecyclerview(getActivity());
                        for (int i = 0; i < taskList.size(); i++) {
                            BeanTask task = taskList.get(i);

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

                        taskList.clear();
                        taskMerge.clear();
                        taskBlue.clear();
                        taskYellow.clear();
                        taskRed.clear();
                        adapter.addItems(beanItemFragHomeChanged);
                        mRecyclerView.setAdapter(adapter);
                    }else {
                        loading.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        noData.setText("没有找到相关任务，请尝试其他关键字");
                    }
                    break;
            }
        }
    };
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
        return beanBlue;

    }


    public  List<BeanTask> searchTaskByName(String taskname){
        List<BeanTask> result = new ArrayList<>();
        try {
            HashMap<String,String> map = new HashMap<>();
            map.put("taskName",taskname);
            String json = HttpHelper.postData(MyURL.SEARCH_TASK_BY_TASKNAME,map,null);
            result = HttpHelper.getBeanTasksFromJson2(json);
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }




}
