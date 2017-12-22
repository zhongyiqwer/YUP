package com.owo.module_c_comment_user.presenter;

import com.owo.module_a_selectlabel.bean.BeanTag;
import com.owo.module_c_comment_user.model.ModelCommUser;
import com.owo.module_c_comment_user.model.ModelCommUserImpl;
import com.owo.module_c_comment_user.model.OnLoadListener;
import com.owo.module_c_comment_user.view.ViewCommUser;
import com.owo.utils.util_http.HttpHelper;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author XQF
 * @created 2017/5/27
 */
public class PresenterCommUserImpl implements PresenterCommUser {

    private ModelCommUser mModelSelectLabel;
    private ViewCommUser mViewSelectLabel;


    public PresenterCommUserImpl(ViewCommUser viewSelectLabel) {
        mModelSelectLabel = new ModelCommUserImpl();
        mViewSelectLabel = viewSelectLabel;
    }

    @Override
    public void loadLabelSelfBySex(String sex) {


        HashMap<String, String> map = new HashMap<>();
        map.put("sex", sex);
        mModelSelectLabel.loadLabeSelf(map, new OnLoadListener() {
            @Override
            public void onSucess(String string) {

                try {
                    List<BeanTag> result = HttpHelper.AJResultGetInterestTagBySex(string);

                    List<List<BeanTag>> tagList = new ArrayList<>();

                    for (int i = 0; i < 5; i++) {
                        List<BeanTag> tags = new ArrayList<>();
                        tagList.add(tags);
                    }

                    for (int i = 0; i < result.size(); i++) {
                        BeanTag tag = result.get(i);
                        int type = tag.getType();

                        if (type == 0) {
                            tagList.get(0).add(tag);
                        } else if (type == 1) {
                            tagList.get(1).add(tag);
                        } else if (type == 2) {
                            tagList.get(2).add(tag);
                        } else if (type == 3) {
                            tagList.get(3).add(tag);
                        } else if (type == 4) {
                            tagList.get(4).add(tag);
                        }
                    }
                    mViewSelectLabel.getLabelSelfFormNet(tagList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }



    @Override
    public void inuserUserComm( HashMap<String, String> map ) {
        mModelSelectLabel.insertUserComment(map);
    }
}
