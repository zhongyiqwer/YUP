package com.owo.module_b_personal.view;


import com.owo.module_b_personal.bean.BeanMoment;
import com.owo.module_b_personal.bean.BeanUser;

import java.util.List;

/**
 * Created by XQF on 2017/5/21.
 */
public interface ViewPersonalDownViewpagerMoment {

    void getResultMomentFromSever(List<BeanMoment> list,int code);
    //void getPersonalInfo(BeanUser beanUser);

}
