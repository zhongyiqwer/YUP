package com.owo.module_c_detail.detail_activity;

import com.owo.module_b_personal.bean.BeanUser;
import com.owo.utils.util_http.HttpHelper;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ppssyyy on 2017-06-12.
 */
public class PresenterDetailImpl implements  PresenterDetail{
    private ViewFragDetail viewFragDetail;
    private ModelDetail modelDetail;

    public PresenterDetailImpl(ViewFragDetail viewFragDetail){
        this.viewFragDetail = viewFragDetail;
        modelDetail = new ModelDetailImpl();
    }



    @Override
    public void getApplyUsersByTID(int taskID) {

        HashMap<String,String> map = new HashMap<>();
        map.put("taskID",taskID+"");

        modelDetail.loadApplyUsers(map, new OnLoadListener() {
            @Override
            public void onSucess(String string,int code) {
                try {
                    List<BeanUser> list = HttpHelper.AJResultGetUsers(string);
                    viewFragDetail.getApplyUsers(list,code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
