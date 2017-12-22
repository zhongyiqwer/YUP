package com.owo.module_c_detail.detail_otheruser;

/**
 * Created by ppssyyy on 2017-06-12.
 */
public interface PresenterOtherUser {
    void loadSumUserFollowedByUserId(int userId);
    void loadSumUserFollowMeByUserId(int userId);
    void loadUserCommentBy(int userId);

}
