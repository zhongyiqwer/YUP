package com.owo.module_c_detail.detail_otheruser;

import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.bean.BeanUserCommentLabel;
import com.owo.module_c_detail.detail_otheruser.OnLoadListener;

import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ppssyyy on 2017-06-12.
 */
public class PresenterOtherUserImpl implements  PresenterOtherUser{
    private ViewFragOtherUser viewFragOtherUser;
    private ModelOtherUser modelOtherUser;

    public PresenterOtherUserImpl(ViewFragOtherUser viewFragOtherUser){
        this.viewFragOtherUser = viewFragOtherUser;
        modelOtherUser = new ModelOtherUserImpl();
    }

    @Override
    public void loadSumUserFollowedByUserId(int userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userID", userId + "");
        modelOtherUser.loadSumIFollowed(map, new OnLoadListener() {
            @Override
            public void onSucess(String string) {
                try {
                    int result = HttpHelper.AJSumFollow(string);
                    viewFragOtherUser.getSumIFoloowed(result);
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
        modelOtherUser.loadSumFollowMe(map, new OnLoadListener() {
            @Override
            public void onSucess(String string) {
                try {
                    int result = HttpHelper.AJSumFollow(string);
                    viewFragOtherUser.getSumFollowMe(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadUserCommentBy(int userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userID", userId + "");
        modelOtherUser.loadUserComment(map, new OnLoadListener() {
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
                    viewFragOtherUser.getUserComment(score, list);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
