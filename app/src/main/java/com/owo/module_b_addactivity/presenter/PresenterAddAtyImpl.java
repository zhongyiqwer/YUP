package com.owo.module_b_addactivity.presenter;

import com.owo.module_b_addactivity.model.ModelAddAty;
import com.owo.module_b_addactivity.model.ModelAddAtyImpl;
import com.owo.module_b_addactivity.view.ViewAddAty;

import java.util.HashMap;

/**
 * @author XQF
 * @created 2017/5/26
 */
public class PresenterAddAtyImpl implements PresenterAddAty {

    ModelAddAty mModelAddAty;
    ViewAddAty mViewAddAty;

    public PresenterAddAtyImpl(ViewAddAty viewAddAty) {
        mModelAddAty = new ModelAddAtyImpl();
        mViewAddAty = viewAddAty;
    }


    @Override
    public int sendTaskByParamMap(HashMap<String, String> map, HashMap<String, String> map1) {
        return mModelAddAty.sendTask(map, map1);
    }
}
