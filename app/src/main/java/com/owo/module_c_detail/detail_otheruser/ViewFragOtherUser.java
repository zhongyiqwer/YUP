package com.owo.module_c_detail.detail_otheruser;

import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.bean.BeanUserCommentLabel;

import java.util.List;

/**
 * Created by ppssyyy on 2017-06-12.
 */
public interface ViewFragOtherUser {
    void getSumIFoloowed(int sum);
    void getSumFollowMe(int sum);
    void getUserComment(float score, List<BeanUserCommentLabel> list);
}
