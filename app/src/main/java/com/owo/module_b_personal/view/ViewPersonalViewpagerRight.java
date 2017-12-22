package com.owo.module_b_personal.view;


import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.bean.BeanUserCommentLabel;

import java.util.List;

/**
 * Created by XQF on 2017/5/28.
 */
public interface ViewPersonalViewpagerRight {
    void getUserComment(float score, List<BeanUserCommentLabel> list);
    //void getPersonalInfo(BeanUser beanUser);

}
