package com.owo.module_b_personal.presenter;

/**
 * Created by XQF on 2017/5/20.
 */
public interface PresenterPersonal {

    void loadUserPersaonlInfoById(int userId);

    void loadUserFriendMomentById(int userId);

    //void loadTasksAppliedByUserId(int userId);

    void loadTasksPublishedByUserId(int userId);


    void loadSumUserFollowedByUserId(int userId);

    void loadSumUserFollowMeByUserId(int userId);

    void loadResultCommentByUserId(int userId);

    void loadResultLikesByCommentId(int commentId);

    void sendUpdateMomentLikeByMomentIdAndLikeNum(int momentId, int likeNum);


    void loadUserCommentBy(int userId);
}
