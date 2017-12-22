package com.owo.module_c_detail.detail_activity.pick_member;

import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_c_detail.detail_activity.*;
import com.owo.module_c_detail.detail_activity_comment.view_comment.BeanActCmt;
import com.owo.module_c_detail.detail_activity_comment.view_comment.ModelActCmt;
import com.owo.utils.util_http.HttpHelper;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ppssyyy on 2017-06-13.
 */
public class PresenterPickImpl implements PresenterPick {
    private ModelPick modelPick;
    private ViewPick viewPick;

    public PresenterPickImpl(ViewPick viewPick){
        modelPick  = new ModelPickImpl();
        this.viewPick = viewPick;

    }


    @Override
    public void getApplyUsersByTID(int taskID) {

        HashMap<String,String> map = new HashMap<>();
        map.put("taskID",taskID+"");

        modelPick.loadApplyUsers(map, new OnLoadListener() {
            @Override
            public void onSucess(String string) {
                try {
                    List<BeanUser> list = HttpHelper.AJResultGetUsers(string);
                    viewPick.getApplyUsers(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
