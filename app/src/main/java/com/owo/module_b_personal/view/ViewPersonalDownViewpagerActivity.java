package com.owo.module_b_personal.view;


import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_personal.bean.BeanUser;

import java.util.List;

/**
 * Created by XQF on 2017/5/21.
 */
public interface ViewPersonalDownViewpagerActivity {
    //void getTasksIApplied(List<BeanTask> taskList,int code);
    void getTasksIPublished(List<BeanTask> taskList,int code);
    //void getPersonalInfo(BeanUser beanUser);

}
