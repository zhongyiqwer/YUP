package com.owo.module_c_detail.detail_otheruser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.model.User;
import com.owo.module_a_selectlabel.bean.BeanTag;
import com.owo.module_b_personal.bean.BeanUserCommentLabel;
import com.owo.module_c_comment_user.widgets.AtyCommUser;
import com.owo.module_c_detail.detail_activity.ViewFragDetail;
import com.owo.utils.Common;
import com.owo.utils.Constants;
import com.owo.utils.TagMap;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.owo.base.FragBase;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.widgets.FragPersonalDownViewpagerActivity;
import com.owo.module_b_personal.widgets.FragPersonalDownViewpagerMoment;

import com.wao.dogcat.widget.CircleImageView;
import com.wao.dogcat.widget.MyRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.jchat.android.application.JChatDemoApplication;
import io.jchat.android.chatting.ChatActivity;


/**
 * @author XQF
 * @created 2017/5/23
 */
public class FragDetailOtherUser extends FragBase implements ViewFragOtherUser{

    public static FragDetailOtherUser newInstance(BeanUser beanUser) {
        FragDetailOtherUser fragDetailOtherUser = new FragDetailOtherUser();
        mBeanUser = beanUser;

        return fragDetailOtherUser;
    }


    public static BeanUser mBeanUser;

//    @BindView(R.id.viewpager_frag_detail_other_user)
//    protected ViewPager mViewPager;
//
//    @BindView(R.id.tablayout_frag_detail_other_user)
//    protected TabLayout mTabLayout;

    @BindView(R.id.fab_btn_frag_detail_other_user)
    protected FloatingActionButton mFloatingActionButton;


    @BindView(R.id.detail_other_user_avatar)
    protected CircleImageView mCircleImageViewUserAvatar;

    @BindView(R.id.levelNum)
    protected TextView mUserLevel;
    @BindView(R.id.detail_other_user_name)
    protected TextView mTextViewUserName;


    @BindView(R.id.detail_other_user_height)
    protected TextView mTextViewHeight;

    @BindView(R.id.detail_other_user_age)
    protected TextView mTextViewAge;

    @BindView(R.id.detail_other_user_weight)
    protected TextView mTextViewWeight;

    @BindView(R.id.detail_other_user_signature)
    protected TextView mTextViewSignNature;

    @BindView(R.id.detail_other_user_sex)
    protected ImageView mSex;

    @BindView(R.id.detail_other_user_followed)
    protected TextView mTextViewUserFollowedSum;


    @BindView(R.id.detail_other_user_followme)
    protected TextView mTextViewUserFollowMeSum;



    @BindView(R.id.detail_other_user_ratingbar)
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
    @BindView(R.id.detail_other_user_ratingnum)
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

    private float mUserScore;
    private List<BeanUserCommentLabel> mUserLabels;
    private List<BeanTag> mUserSelfTag;

    private int userID;
    private int follow,followed;
    private PresenterOtherUser presenterOtherUser;



    private FragmentManager mFragmentManager;
    private FragPersonalDownViewpagerActivity fragPersonalDownViewpagerActivity;

    @BindView(R.id.person_bg)
    protected ImageView backImage;
    private DisplayImageOptions options1, options2;
    private String path1, path2;

    @OnClick(R.id.detail_other_user_addlabel)

    public void addLabel(){
        Common.half = new User();
        Common.half.setAvatar(mBeanUser.getAvatar());
        Common.half.setSex(mBeanUser.getSex()+"");
        Common.half.setUserName(mBeanUser.getUserName());
        Common.halfId=mBeanUser.getId();
        start(getContext(), AtyCommUser.class,2);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail_other_user, container, false);
        ButterKnife.bind(this, view);
       Common.userSP = getContext().getSharedPreferences("userSP", 0);
        userID = Common.userSP.getInt("ID",0);
        presenterOtherUser = new PresenterOtherUserImpl(this);



        if (mBeanUser!=null) {
            presenterOtherUser.loadSumUserFollowedByUserId(mBeanUser.getId());
            presenterOtherUser.loadSumUserFollowMeByUserId(mBeanUser.getId());
            presenterOtherUser.loadUserCommentBy(mBeanUser.getId());


            final String avatarUrl = mBeanUser.getAvatar();


            options1 = new DisplayImageOptions.Builder()//
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                    .showImageForEmptyUri(R.drawable.my) //
                    .showImageOnFail(R.drawable.my) //
                    .cacheInMemory(true) //
                    .cacheOnDisk(true) //
                    .build();//

            options2 = new DisplayImageOptions.Builder()//
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                    .showImageForEmptyUri(R.drawable.single_bg) //
                    .showImageOnFail(R.drawable.single_bg) //
                    .cacheInMemory(true) //
                    .cacheOnDisk(true) //
                    .build();//

            path1 = MyURL.ROOT+avatarUrl;
            path2 = MyURL.ROOT+mBeanUser.getBackImage();
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                    displayImage(path1, mCircleImageViewUserAvatar, options1);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                    displayImage(path2, backImage, options2);

//            Glide.with(getActivity()).load((MyURL.ROOT+avatarUrl)).placeholder(R.drawable.my)
//                    .into(mCircleImageViewUserAvatar);
//
//            Glide.with(getActivity()).load((MyURL.ROOT+mBeanUser.getBackImage())).placeholder(R.drawable.single_bg).
//                    into(backImage);


            int level = mBeanUser.getLevel();
            mUserLevel.setText("Lv." + level);
            mTextViewUserName.setText(mBeanUser.getUserName());
            if (mBeanUser.getHeight()>0) {
                mTextViewHeight.setVisibility(View.VISIBLE);
                mTextViewHeight.setText(mBeanUser.getHeight() + "cm");
            }
            if (mBeanUser.getAge()>0) {
                mTextViewAge.setVisibility(View.VISIBLE);
                mTextViewAge.setText(mBeanUser.getAge() + "岁");
            }
            if (mBeanUser.getWeight()>0) {
                mTextViewWeight.setVisibility(View.VISIBLE);
                mTextViewWeight.setText(mBeanUser.getWeight() + "kg");
            }
            if (mBeanUser.getSex().equals("2"))
                mSex.setImageResource(R.drawable.map_girl);
            mTextViewSignNature.setText(mBeanUser.getSignature());

        }



        initDown();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }.start();

    }
    public List<BeanTag> getUserSelfTag() {
        String hobby = "";
        hobby = mBeanUser.getHobby();
        List<BeanTag> result = new ArrayList<>();

        if (hobby.trim().length()>0) {
            try {

                JSONObject jsonObject = new JSONObject(hobby);
                String objStr = jsonObject.getString("self");
                JSONObject selfObject = new JSONObject(objStr);

                List<JSONArray> jsonArrays = new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    if (selfObject.has(i + "")) {
                        JSONArray jsonArray = selfObject.getJSONArray(i + "");
                        jsonArrays.add(jsonArray);
                    }
                }


                if (jsonArrays.size() > 0) {
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
        }else  return result;


    }

    private void initDown() {
//        AdapterDownViewPager adapterDownViewPager = new AdapterDownViewPager(getActivity().getSupportFragmentManager());
//        adapterDownViewPager.add(FragPersonalDownViewpagerMoment.newInstance(mBeanUser), "动态");
//        adapterDownViewPager.add(FragPersonalDownViewpagerActivity.newInstance(mBeanUser), "活动");
//        mTabLayout.setupWithViewPager(mViewPager);
//        mViewPager.setAdapter(adapterDownViewPager);


        mFragmentManager = getActivity().getSupportFragmentManager();
        if (fragPersonalDownViewpagerActivity == null){
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragPersonalDownViewpagerActivity = FragPersonalDownViewpagerActivity.newInstance(mBeanUser);
            fragmentTransaction.replace(R.id.frag_person_act,fragPersonalDownViewpagerActivity,"活动");
            fragmentTransaction.show(fragPersonalDownViewpagerActivity);
            fragmentTransaction.commit();
        }


        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                BubbleActions.on(v)
//                        .addAction("Star", R.drawable.like_red, new Callback() {
//                            @Override
//                            public void doAction() {
//                                Toast.makeText(v.getContext(), "Star pressed!", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addAction("Share", R.drawable.like_red, new Callback() {
//                            @Override
//                            public void doAction() {
//                                Toast.makeText(v.getContext(), "Share pressed!", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addAction("Hide", R.drawable.like_red, new Callback() {
//                            @Override
//                            public void doAction() {
//                                Toast.makeText(v.getContext(), "Hide pressed!", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .show();

                final SimpleTooltip tooltip = new SimpleTooltip.Builder(getContext())
                        .anchorView(mFloatingActionButton)
                        .gravity(Gravity.START)
                        .dismissOnOutsideTouch(false)
                        .dismissOnInsideTouch(false)
                        .transparentOverlay(false)
                        .modal(true)
                        .animated(false)
                        .showArrow(false)
                        .contentView(R.layout.frag_detail_tooltip_float_btn, R.id.tv_text)
                        .focusable(true)
                        .build();
                Button add = (Button) tooltip.findViewById(R.id.add_btn);
                Button chat = (Button)tooltip.findViewById(R.id.chat_btn);
                Button special = (Button) tooltip.findViewById(R.id.special_btn);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int friendID = mBeanUser.getId();

                        try {

                            int code2 =
                                    HttpHelper.getCode(canFollow(friendID));

                            if (code2 == 200) {
                                //关注别人
                                if (HttpHelper.getCode(insertFriend(friendID)) == 200) {
                                    //成功-->翻页
                                    Common.display(getContext(), "关注成功");
                                    JMessageClient.createSingleTextMessage("yup_"+mBeanUser.getId(), "4d454221295c35af705cc26e", "Hello~ 我关注你啦！\n" +
                                            "(点我头像加好友)");
                                }
                                else Common.display(getContext(), "操作失败");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra(JChatDemoApplication.CONV_TITLE, mBeanUser.getUserName());
                        intent.putExtra(JChatDemoApplication.TARGET_ID, "yup_"+mBeanUser.getId());
                        intent.putExtra(JChatDemoApplication.TARGET_APP_KEY, "4d454221295c35af705cc26e");
                        startActivity(intent);
                    }
                });
                special.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.display(getContext(),"已申请特别关注");
                    }
                });
                tooltip.show();

            }
        });

    }

    @Override
    public void getSumIFoloowed(int sum) {
        followed = sum;
    }

    @Override
    public void getSumFollowMe(int sum) {
        follow = sum;

    }

    @Override
    public void getUserComment(float score, List<BeanUserCommentLabel> list) {
        mUserScore = score;
        mUserLabels = list;
        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();

    }

    /**
     * 内部类
     */
    class AdapterDownViewPager extends FragmentPagerAdapter {

        List<String> titleList;
        List<Fragment> frags;

        public AdapterDownViewPager(FragmentManager fm) {
            super(fm);
            titleList = new ArrayList<>();
            frags = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return frags.get(position);
        }

        @Override
        public int getCount() {
            return frags.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        public void add(Fragment fragment, String title) {
            frags.add(fragment);
            titleList.add(title);
        }
    }

    public String canFollow(int friendID) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("userID", userID + "");
        paramHM.put("friendID", friendID + "");
        return HttpHelper.postData(MyURL.CHECK_IS_FOLLOWED, paramHM, null);
    }
    public String insertFriend(int friendID) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("userID", userID + "");
        paramHM.put("friendID", friendID + "");
        String result = HttpHelper.postData(MyURL.INSERT_FRIEND, paramHM, null);
        return result;
    }



    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
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




                    mTextViewUserFollowedSum.setText(followed + "");
                    mTextViewUserFollowMeSum.setText(follow + "");


                    break;
            }
        }
    };


}
