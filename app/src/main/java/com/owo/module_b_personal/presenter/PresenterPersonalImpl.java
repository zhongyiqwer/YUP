package com.owo.module_b_personal.presenter;



import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_personal.bean.BeanMoment;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.bean.BeanUserCommentLabel;
import com.owo.module_b_personal.model.ModelPersonal;
import com.owo.module_b_personal.model.ModelPersonalImpl;
import com.owo.module_b_personal.model.OnLoadListener;
import com.owo.module_b_personal.model.OnLoadListener2;
import com.owo.module_b_personal.view.ViewPersonal;
import com.owo.module_b_personal.view.ViewPersonalDownViewpagerActivity;
import com.owo.module_b_personal.view.ViewPersonalDownViewpagerMoment;
import com.owo.module_b_personal.view.ViewPersonalViewpagerLeft;
import com.owo.module_b_personal.view.ViewPersonalViewpagerLeftHolder;
import com.owo.module_b_personal.view.ViewPersonalViewpagerRight;
import com.owo.utils.Constants;
import com.owo.utils.util_http.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author XQF
 * @created 2017/5/21
 */
public class PresenterPersonalImpl implements PresenterPersonal {


    private ModelPersonal mModelPersonal;
    private ViewPersonal mViewPersonal;
    private ViewPersonalDownViewpagerMoment mViewPersonalDownViewpagerMoment; //个人中心--> 动态
    private ViewPersonalDownViewpagerActivity mViewPersonalDownViewpagerActivity; //个人中心--> 活动
    private ViewPersonalViewpagerLeft mViewPersonalViewpagerLeft;
    private ViewPersonalViewpagerLeftHolder mViewPersonalViewpagerLeftHolder;
    private ViewPersonalViewpagerRight mViewPersonalViewpagerRight;

    public PresenterPersonalImpl(ViewPersonal viewPersonal) {
        mModelPersonal = new ModelPersonalImpl();
        mViewPersonal = viewPersonal;
    }

    public PresenterPersonalImpl(ViewPersonalDownViewpagerMoment viewPersonalDownViewpagerMoment) {
        mModelPersonal = new ModelPersonalImpl();
        mViewPersonalDownViewpagerMoment = viewPersonalDownViewpagerMoment;
    }

    public PresenterPersonalImpl(ViewPersonalDownViewpagerActivity viewPersonalDownViewpagerActivity) {
        mModelPersonal = new ModelPersonalImpl();
        mViewPersonalDownViewpagerActivity = viewPersonalDownViewpagerActivity;
    }

    public PresenterPersonalImpl(ViewPersonalViewpagerLeft viewPersonalViewpagerLeft) {
        mModelPersonal = new ModelPersonalImpl();
        mViewPersonalViewpagerLeft = viewPersonalViewpagerLeft;
    }

    public PresenterPersonalImpl(ViewPersonalViewpagerLeftHolder viewPersonalViewpagerLeftHolder) {
        mModelPersonal = new ModelPersonalImpl();
        mViewPersonalViewpagerLeftHolder = viewPersonalViewpagerLeftHolder;
    }

    public PresenterPersonalImpl(ViewPersonalViewpagerRight viewPersonalViewpagerRight) {
        mModelPersonal = new ModelPersonalImpl();
        mViewPersonalViewpagerRight = viewPersonalViewpagerRight;
    }


    @Override
    public void loadUserPersaonlInfoById(int userId) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("id", userId + " ");
        mModelPersonal.loadUserPersonalInfo(map, new OnLoadListener() {
            @Override
            public void onSucess(String string) {
                try {
                    HashMap<String, Object> mapResult = HttpHelper.AnalysisUserInfo(string);
                    BeanUser beanUser = Constants.getBeanUserFromMap(mapResult);
//                    if (mViewPersonalViewpagerLeft != null) {
//                        mViewPersonalViewpagerLeft.getPersonalInfo(beanUser);
//                    }
                    if (mViewPersonal != null) {

                        mViewPersonal.getPersonalInfo(beanUser);
                    }
                    if (mViewPersonalViewpagerLeftHolder != null) {

                        mViewPersonalViewpagerLeftHolder.getPersonalInfo(beanUser);
                    }
//                    if (mViewPersonalViewpagerRight != null) {
//                        mViewPersonalViewpagerRight.getPersonalInfo(beanUser);
//                    }
//                    if (mViewPersonalDownViewpagerActivity != null) {
//                        mViewPersonalDownViewpagerActivity.getPersonalInfo(beanUser);
//                    }
//                    if (mViewPersonalDownViewpagerMoment != null) {
//                        mViewPersonalDownViewpagerMoment.getPersonalInfo(beanUser);
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void loadUserFriendMomentById(int userId) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("userID", userId + " ");
        mModelPersonal.loadPersoalFriendMoment(map, new OnLoadListener2() {
            List<BeanMoment> resultListMoment = new ArrayList<>();

            @Override
            public void onSucess(String string,int code) {
                try {
                    ArrayList<HashMap<String, Object>> resultList = HttpHelper.AJgetMommentByUID(string);
                    for (int i = 0; i < resultList.size(); i++) {
                        BeanMoment beanMoment = new BeanMoment();
                        HashMap<String, Object> map = resultList.get(i);
                        beanMoment.setMomentUserId((Integer) map.get("userID"));
                        beanMoment.setMomentUserAvatar((String) map.get("avatar"));
                        beanMoment.setMomentUserName((String) map.get("username"));
                        beanMoment.setMomentCommentId((Integer) map.get("commentID"));
                        beanMoment.setMomentId((Integer) map.get("id"));
                        beanMoment.setMomentContent((String) map.get("momentContent"));
                        beanMoment.setMomentImage((String) map.get("momentImage"));
                        beanMoment.setMomentDate(map.get("momentDate").toString());
                        beanMoment.setMomentType((Integer) map.get("momentType"));
                        resultListMoment.add(beanMoment);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mViewPersonalDownViewpagerMoment.getResultMomentFromSever(resultListMoment,code);
            }
        });
    }

//    @Override
//    public void loadTasksAppliedByUserId(int userId) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("userID", userId + "");
//        mModelPersonal.loadActivityIApplied(map, new OnLoadListener2() {
//            @Override
//            public void onSucess(String string,int code) {
//                List<BeanTask> result = HttpHelper.getBeanTasksFromJson(string);
//                mViewPersonalDownViewpagerActivity.getTasksIApplied(result,code);
//            }
//        });
//    }

    @Override
    public void loadTasksPublishedByUserId(int userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userID", userId + "");
        mModelPersonal.loadActivityIPublishedAndReceiveApplicatoin(map, new OnLoadListener2() {
            @Override
            public void onSucess(String string,int code) {
                List<BeanTask> result = HttpHelper.getBeanTasksFromJson2(string);
                mViewPersonalDownViewpagerActivity.getTasksIPublished(result,code);
            }
        });
    }

    @Override
    public void loadSumUserFollowedByUserId(int userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userID", userId + "");
        mModelPersonal.loadSumIFollowed(map, new OnLoadListener() {
            @Override
            public void onSucess(String string) {
                try {
                    int result = HttpHelper.AJSumFollow(string);
                    mViewPersonalViewpagerLeft.getSumIFoloowed(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void loadSumUserFollowMeByUserId(int userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("friendID", userId + "");
        mModelPersonal.loadSumFollowMe(map, new OnLoadListener() {
            @Override
            public void onSucess(String string) {
                try {
                    int result = HttpHelper.AJSumFollow(string);
                    mViewPersonalViewpagerLeft.getSumFollowMe(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadResultCommentByUserId(int momentId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("momentID", momentId + "");
        mModelPersonal.loadResultComment(map, new OnLoadListener() {
            @Override
            public void onSucess(String string) {

//                mViewPersonalViewpagerLeftHolder.getResultComment();
            }
        });
    }

    @Override
    public void loadResultLikesByCommentId(int commentId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("commentID", commentId + "");
        mModelPersonal.loadResultLike(map, new OnLoadListener() {
            @Override
            public void onSucess(String string) {
//                mViewPersonalViewpagerLeftHolder.getResultLike();
            }
        });
    }

    @Override
    public void sendUpdateMomentLikeByMomentIdAndLikeNum(int momentId, int likeNum) {
        HashMap<String, String> map = new HashMap<>();
        map.put("momentID", momentId + "");
        mModelPersonal.sendUpdateLikeSum(map);
    }

    @Override
    public void loadUserCommentBy(int userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userID", userId + "");
        mModelPersonal.loadUserComment(map, new OnLoadListener() {
            @Override
            public void onSucess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    String objStr = jsonObject.getString("data");
                    JSONObject obj = new JSONObject(objStr);
                    int score = (int) obj.get("userScore");
                    JSONArray jsonArray = obj.getJSONArray("userLabelList");
                    List<BeanUserCommentLabel> list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                        BeanUserCommentLabel beanUserCommentLabel = new BeanUserCommentLabel();
                        beanUserCommentLabel.setLabel((String) jsonObject1.get("userlabel"));
                        beanUserCommentLabel.setLabelNum(jsonObject1.getInt("labelnum")+"");
                        list.add(beanUserCommentLabel);
                    }
                    mViewPersonalViewpagerRight.getUserComment(score, list);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
