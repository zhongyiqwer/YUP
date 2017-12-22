package com.owo.module_a_selectlabel.presenter;

import com.owo.module_a_selectlabel.bean.BeanTag;
import com.owo.module_a_selectlabel.model.ModelSelectLabel;
import com.owo.module_a_selectlabel.model.ModelSelectLabelImpl;
import com.owo.module_a_selectlabel.model.OnLoadListener;
import com.owo.module_a_selectlabel.view.ViewSelectLabel;
import com.owo.utils.util_http.HttpHelper;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author XQF
 * @created 2017/5/27
 */
public class PresenterSelectLabelImpl implements PresenterSelectLabel {

    private ModelSelectLabel mModelSelectLabel;
    private ViewSelectLabel mViewSelectLabel;


    public PresenterSelectLabelImpl(ViewSelectLabel viewSelectLabel) {
        mModelSelectLabel = new ModelSelectLabelImpl();
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
    public void loadLabelFindFriend() {
        mModelSelectLabel.loadLabelFindFriend(new OnLoadListener() {
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
    public void updateHobbyById(int mUserId, String hobby) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", mUserId + "");
        map.put("hobby", hobby);
        mModelSelectLabel.updateHobby(map);
    }
}
