package com.owo.module_c_detail.detail_activity_comment.view_comment;

import android.view.View;

import com.owo.utils.util_http.HttpHelper;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ppssyyy on 2017-06-13.
 */
public class PresenterActCmtImpl implements PresenterActCmt{
    private ModelActCmt modelActCmt;
    private ViewActCmt viewActCmt;

    public PresenterActCmtImpl(ViewActCmt viewActCmt){
        modelActCmt  = new ModelActCmtImpl();
        this.viewActCmt = viewActCmt;

    }





    @Override
    public void getTaskCmtByTID(int taskID) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("taskID",taskID+"");
        modelActCmt.loadTaskCmt(hashMap, new OnLoadListener() {
            @Override
            public void onSucess(String str) {
                try {
                    List<BeanActCmt> list = HttpHelper.AJActCmt(str);
                    viewActCmt.getActCmt(list);
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });

    }
}
