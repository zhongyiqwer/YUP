package com.owo.module_b_personal.widgets;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.base.FragBase;
import com.owo.module_a_register.AtyRegister;
import com.owo.module_a_selectlabel.bean.BeanTag;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.bean.BeanUserCommentLabel;
import com.owo.module_b_personal.presenter.PresenterPersonal;
import com.owo.module_b_personal.presenter.PresenterPersonalImpl;
import com.owo.module_b_personal.view.ViewPersonalViewpagerRight;
import com.owo.module_c_mylabel.AtyMyLabel;
import com.owo.utils.Common;
import com.owo.utils.TagMap;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;

import com.wao.dogcat.widget.MyRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author XQF
 * @created 2017/5/17
 */
public class FragPersonalViewpagerRight extends FragBase implements ViewPersonalViewpagerRight {


    public static FragPersonalViewpagerRight newInstance(BeanUser beanUser) {
        FragPersonalViewpagerRight fragPersonalViewpagerRight = new FragPersonalViewpagerRight();
//        Bundle args = new Bundle();
//        args.putSerializable("beanUser", beanUser);
//        fragPersonalViewpagerRight.setArguments(args);
        mBeanUser = beanUser;
        return fragPersonalViewpagerRight;
    }

    @BindView(R.id.right_ratingbar)
    protected MyRatingBar mRatingBar;

    @BindView(R.id.right_btn_selfcomment1)
    protected TextView mTextViewSelfTags1;

    @BindView(R.id.right_btn_selfcomment2)
    protected TextView mTextViewSelfTags2;

    @BindView(R.id.right_btn_selfcomment3)
    protected TextView mTextViewSelfTags3;

    @BindView(R.id.right_btn_selfcomment4)
    protected TextView mTextViewSelfTags4;


    @BindView(R.id.right_btn_othercomment1)
    protected TextView mTextViewOtherComment1;

    @BindView(R.id.right_btn_othercomment2)
    protected TextView mTextViewOtherComment2;
    @BindView(R.id.right_btn_othercomment3)
    protected TextView mTextViewOtherComment3;
    @BindView(R.id.right_btn_othercomment4)
    protected TextView mTextViewOtherComment4;



    @BindView(R.id.right_btn_overalltags)
    protected TextView mBtnLookAllTags;

    @BindView(R.id.right_mycoins)
    protected TextView mTextViewMyCoins;

    @BindView(R.id.right_mysteps)
    TextView mTextViewMySteps;

    @BindView(R.id.right_ratingnum)
    protected TextView mTextViewScore;

    @BindView(R.id.tagNum1)
    protected TextView mTextViewTagNum1;

    @BindView(R.id.tagNum2)
    protected TextView mTextViewTagNum2;

    @BindView(R.id.tagNum3)
    protected TextView mTextViewTagNum3;

    @BindView(R.id.tagNum4)
    protected TextView mTextViewTagNum4;

    @BindView(R.id.other_cmt1)
    protected FrameLayout otherCmt1;

    @BindView(R.id.other_cmt2)
    protected FrameLayout otherCmt2;

    @BindView(R.id.other_cmt3)
    protected FrameLayout otherCmt3;

    @BindView(R.id.other_cmt4)
    protected FrameLayout otherCmt4;



    public static BeanUser mBeanUser;
    private float mUserScore;
    public static List<BeanUserCommentLabel> mUserLabels;
    public static List<BeanTag> mUserSelfTag;

    private PresenterPersonal mPresenterPersonal;

    @BindView(R.id.person_bg)
    protected ImageView backImage;

    private DisplayImageOptions  options2;
    private String path2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_personal_viewpager_right, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Common.user!=null) {
            mPresenterPersonal = new PresenterPersonalImpl(this);
            mPresenterPersonal.loadUserCommentBy(Common.userID);
        }

        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }.start();
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void getUserComment(float score, List<BeanUserCommentLabel> list) {
        //TODO 重新登录这儿没进去啊啊啊啊

        mUserScore = score;
        mUserLabels = list;
        System.out.println("mUserLabels===" + mUserLabels);
        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();

    }




    public List<BeanTag> getUserSelfTag() {
        String hobby = Common.user.getHobby();
        List<BeanTag> result = new ArrayList<>();
        try {

        JSONObject jsonObject = new JSONObject(hobby);
        String objStr = jsonObject.getString("self");
        JSONObject selfObject = new JSONObject(objStr);

            List<JSONArray> jsonArrays = new ArrayList<>();
            for (int i=0;i<6;i++){
                if (selfObject.has(i+"")) {
                    JSONArray jsonArray = selfObject.getJSONArray(i + "");
                    jsonArrays.add(jsonArray);
                }
            }


            if (jsonArrays.size()>0) {
                for (int i = 0; i < jsonArrays.size(); i++) { //遍历每个jsonArray
                    for (int j = 0; j < jsonArrays.get(i).length(); j++) { //遍历jsonArray的每个元素
                        BeanTag beanTag = new BeanTag();
                        JSONObject idObj = jsonArrays.get(i).getJSONObject(j);
                        beanTag.setId(idObj.getInt("id"));
                        beanTag.setType(i);
                        result.add(beanTag);
                    }
                }
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return result;
        }


    }

    @OnClick(R.id.right_btn_overalltags)
    public void onClickOverAllTags() {
        start(getActivity(), AtyMyLabel.class);
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    Glide.with(getActivity()).load((MyURL.ROOT+mBeanUser.getBackImage())).placeholder(R.drawable.single_bg).
//                            into(backImage);


                    mTextViewScore.setText(mUserScore + "");

                    if (mUserLabels!=null) {
                        if (mUserLabels.size() >= 1) {
                            otherCmt1.setVisibility(View.VISIBLE);
                            mTextViewOtherComment1.setText(mUserLabels.get(0).getLabel());
                            mTextViewTagNum1.setText(mUserLabels.get(0).getLabelNum());
                        }

                        if (mUserLabels.size() >= 2) {
                            otherCmt2.setVisibility(View.VISIBLE);
                            mTextViewOtherComment2.setText(mUserLabels.get(1).getLabel());
                            mTextViewTagNum2.setText(mUserLabels.get(1).getLabelNum());
                        }

                        if (mUserLabels.size() >= 3) {
                            otherCmt3.setVisibility(View.VISIBLE);
                            mTextViewOtherComment3.setText(mUserLabels.get(2).getLabel());
                            mTextViewTagNum3.setText(mUserLabels.get(2).getLabelNum());
                        }

                        if (mUserLabels.size() >= 4) {
                            otherCmt4.setVisibility(View.VISIBLE);
                            mTextViewOtherComment4.setText(mUserLabels.get(3).getLabel());
                            mTextViewTagNum4.setText(mUserLabels.get(3).getLabelNum());
                        }
                    }

                    mRatingBar.setClickable(false);//设置可否点击
                    mRatingBar.setStar(mUserScore / 2);//设置显示的星星个数
                    mRatingBar.setStepSize(MyRatingBar.StepSize.Half);//设置每次点击增加一颗星还是半颗星
//                    mRatingBar.setOnRatingChangeListener(new MyRatingBar.OnRatingChangeListener() {
//                        @Override
//                        public void onRatingChange(float ratingCount) {//点击星星变化后选中的个数
//
//                        }
//                    });

                    break;
                case 2:
                    options2 = new DisplayImageOptions.Builder()//
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                            .showImageForEmptyUri(R.drawable.single_bg) //
                            .showImageOnFail(R.drawable.single_bg) //
                            .cacheInMemory(true) //
                            .cacheOnDisk(true) //
                            .build();//
                    path2 = MyURL.ROOT+Common.user.getBackImage();
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                            displayImage(path2, backImage, options2);

                    if (FragPersonalViewpagerLeft.bitmap!=null){
                        backImage.setImageBitmap(FragPersonalViewpagerLeft.bitmap);
                    }


                    Map<Integer, String> map = new HashMap<>();
                    map = TagMap.readFile(getContext(), R.raw.tag_map);

                        mUserSelfTag = getUserSelfTag();
                        System.out.println("mUserSelfTag===" + mUserSelfTag);


                    if (mUserSelfTag!=null) {
                        if (mUserSelfTag.size() >= 1) {
                            mTextViewSelfTags1.setVisibility(View.VISIBLE);
                            mTextViewSelfTags1.setText(map.get(mUserSelfTag.get(0).getId()));
                        }
                        if (mUserSelfTag.size() >= 2) {
                            mTextViewSelfTags2.setVisibility(View.VISIBLE);
                            mTextViewSelfTags2.setText(map.get(mUserSelfTag.get(1).getId()));
                        }
                        if (mUserSelfTag.size() >= 3) {
                            mTextViewSelfTags3.setVisibility(View.VISIBLE);
                            mTextViewSelfTags3.setText(map.get(mUserSelfTag.get(2).getId()));
                        }
                        if (mUserSelfTag.size() >= 4) {
                            mTextViewSelfTags4.setVisibility(View.VISIBLE);
                            mTextViewSelfTags4.setText(map.get(mUserSelfTag.get(3).getId()));
                        }
                    }


                    double money = 0;
                    if (Common.user.getMoney() > 10000) {
                        money = Common.user.getMoney() / 10000.0;
                        mTextViewMyCoins.setText(round(money,1) + "万");
                    }
                    else {
                        mTextViewMyCoins.setText(Common.user.getMoney()  + "");
                    }


                    try {
                        HashMap<String, Object> stepHM = new HashMap<>();
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("userID", Common.userID + "");
                        String str = HttpHelper.postData(MyURL.GET_STEPS, hm, null);
                        int code = HttpHelper.getCode(str);
                        if (code == 200) {
                            stepHM = HttpHelper.AJSteps(str);
                        }
                        int steps = -1;
                        if (stepHM != null) {
                            steps = (int) stepHM.get("stepsToday");
                        }
                        mTextViewMySteps.setText(steps+"");
                    } catch (Exception e) {
                        e.printStackTrace();
                        mTextViewMySteps.setText("0");
                    }



                    break;
            }
        }
    };


    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
