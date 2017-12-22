package com.owo.module_b_home.view;

import com.owo.module_b_home.bean.BeanProp;
import com.owo.module_b_home.bean.BeanTask;

import java.util.HashMap;
import java.util.List;

/**
 * Created by XQF on 2017/5/20.
 */
public interface ViewHome {

    void getTasksIApplied(List<BeanTask> taskList);

    void getTasksIPublished(List<BeanTask> taskList);

    void getTasksNearby(List<BeanTask> taskList);

//    void getCamera(List<HashMap<String, Object>> data);
//
//    void getText(List<HashMap<String, Object>> data);
//
//    void getMyProps(List<BeanProp> propList);
}
