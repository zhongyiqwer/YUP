package com.owo.module_b_explore.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.owo.module_a_selectlabel.bean.BeanTag;
import com.owo.module_b_explore.recyclerview.AdapterRecyclerView;
import com.owo.module_b_main.CardPagerAdapter;
import com.owo.module_b_main.ShadowTransformer;
import com.owo.module_c_detail.AtyDetail;
import com.owo.utils.Common;
import com.owo.utils.TagMap;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.owo.base.FragBase;
import com.owo.module_b_explore.bean.BeanViewPagerItem;
import com.owo.module_b_explore.presenter.PresenterExplore;
import com.owo.module_b_explore.presenter.PresenterExploreImpl;
import com.owo.module_b_explore.view.ViewExploreRecommend;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.presenter.PresenterPersonal;
import com.owo.module_b_personal.presenter.PresenterPersonalImpl;
import com.owo.module_b_personal.view.ViewPersonal;


import com.wao.dogcat.widget.CircleImageView;

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

/**
 * @author XQF
 * @created 2017/5/11
 */
public class FragExploreRecommend extends FragBase implements ViewExploreRecommend, ViewPersonal {
    public static FragExploreRecommend newInstance() {

        return new FragExploreRecommend();
    }

    @BindView(R.id.viewpager_frag_expolre_recomend)
    protected ViewPager mViewPager;

    private List<BeanViewPagerItem> list;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    private PresenterExplore mPresenterExplore;
    private PresenterPersonal mPresenterPersonal;
    private BeanUser mBeanUser;

    int mUserId;

    private int count = 0;

    public static boolean isLike = false;


    @BindView(R.id.like)
    protected Button like;

//    @BindView(R.id.unlike)
//    protected Button unlike;

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.noData)
    TextView noData;

    private String findFriend;


    @OnClick(R.id.like)
    public void like() {

        int friendID = mCardAdapter.getFriendID();

        try {

            int code2 =
                    HttpHelper.getCode(canFollow(friendID));

            if (code2 == 200) {
                //关注别人
                if (HttpHelper.getCode(insertFriend(friendID)) == 200) {
                    //成功-->翻页
                    Common.display(getContext(), "关注成功");
                } else Common.display(getContext(), "操作失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @OnClick(R.id.unlike)
//    public void unlike() {
//        //翻页
//
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_explore_recommend_layout, container, false);
        ButterKnife.bind(this, view);
        Common.userSP = getActivity().getSharedPreferences("userSP", 0);
        mUserId = Common.userSP.getInt("ID", 0);
        mPresenterExplore = new PresenterExploreImpl(this);
        mPresenterPersonal = new PresenterPersonalImpl(this);
        mPresenterPersonal.loadUserPersaonlInfoById(mUserId);


        return view;
    }

    private String getFindFriend() throws JSONException {
        String result = "";

        String hobby = mBeanUser.getHobby();
        if (hobby.length() > 0)

        {
            JSONObject jsonObject = new JSONObject(hobby);
            String objStr = jsonObject.getString("findFriend");
            result = "{\"findFriend\":" + objStr + "}";
        }

        return result;
    }

    @Override
    public void getResultUserByMatchDegree(List<BeanViewPagerItem> list) {
        this.list = list;
        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
//                if (findFriend.length()==0)
//                msg.what = 2;
//                else
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void getPersonalInfo(BeanUser beanUser) {
        mBeanUser = beanUser;
        try {
            //根据交友标签获取推荐
            findFriend = getFindFriend();
            System.out.println("getFindFriend=====" + findFriend);
            mPresenterExplore.getUserOrderByMatchIndex(mUserId + "", findFriend);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    interface CardAdapter {

        int MAX_ELEVATION_FACTOR = 5;

        float getBaseElevation();

        CardView getCardViewAt(int position);

        int getCount();
    }

    class CardPagerAdapter extends PagerAdapter implements CardAdapter {

        private List<CardView> mViews;
        private List<BeanViewPagerItem> mData;
        private float mBaseElevation;
        private DisplayImageOptions options;
        private String path;

        private int friendID;

        private List<BeanTag> mUserSelfTag;
        private TextView tag1, tag2, ta3, tag4;


        public CardPagerAdapter() {
            mData = new ArrayList<>();
            mViews = new ArrayList<>();

            options = new DisplayImageOptions.Builder()//
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                    .showImageForEmptyUri(R.drawable.my) //
                    .showImageOnFail(R.drawable.my) //
                    .cacheInMemory(true) //
                    .cacheOnDisk(true) //
                    .build();//
        }

        public void addItems(List<BeanViewPagerItem> list) {
            if (list != null) {
                if (mData.size() == 0) {
                    mData = list;
                }
                for (int i = 0; i < list.size(); i++) {
                    mViews.add(null);
                }
            }
        }


        public int getFriendID() {
            return friendID;
        }

        public void setFriendID(int friendID) {
            this.friendID = friendID;
        }


        public float getBaseElevation() {
            return mBaseElevation;
        }

        @Override
        public CardView getCardViewAt(int position) {
            return mViews.get(position);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.frag_explore_recommend_viewpager_item, container, false);
            container.addView(view);
            bind(mData.get(position), view);
            CardView cardView = (CardView) view.findViewById(R.id.cardView);


            if (mBaseElevation == 0) {
                mBaseElevation = cardView.getCardElevation();
            }

            cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
            mViews.set(position, cardView);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            mViews.set(position, null);
        }

        private void bind(final BeanViewPagerItem item, View view) {
            setFriendID(item.getBeanUser().getId());
            final CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.viewpager_frag_explore_avatar);
            TextView textViewMatchDegree = (TextView) view.findViewById(R.id.viewpager_frag_explore_match_degree);
            TextView username = (TextView) view.findViewById(R.id.viewpager_frag_explore_autograph);
            String avatar = item.getBeanUser().getAvatar();
            ImageView sexImg = (ImageView) view.findViewById(R.id.rec_vp_sex);
            TextView heightView = (TextView) view.findViewById(R.id.left_height);
            TextView weightView = (TextView) view.findViewById(R.id.left_weight);
            TextView ageView = (TextView) view.findViewById(R.id.left_age);
            TextView tag1 = (TextView) view.findViewById(R.id.tag1);
            TextView tag2 = (TextView) view.findViewById(R.id.tag2);
            TextView tag3 = (TextView) view.findViewById(R.id.tag3);
            TextView tag4 = (TextView) view.findViewById(R.id.tag4);

            TextView matchTxt = (TextView) view.findViewById(R.id.matchTxt);


            path = MyURL.ROOT + avatar;
            //防止图片闪烁
            final String tag = (String) circleImageView.getTag();

            if (tag == null || !tag.equals(path)) {
                circleImageView.setTag(path);
                com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                        .displayImage(path, circleImageView, options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                circleImageView.setTag(path);//确保下载完成再打tag.
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
            }


            if (item.getMatchDegree() > 0) {
                textViewMatchDegree.setText(item.getMatchDegree() + "");
            } else {
                textViewMatchDegree.setTextSize(18);
                textViewMatchDegree.setText("热门用户");
                matchTxt.setVisibility(View.GONE);
                }
                username.setText(item.getBeanUser().getUserName());
                if (item.getBeanUser().getAge() > 0) {
                    ageView.setVisibility(View.VISIBLE);
                    ageView.setText(item.getBeanUser().getAge() + "岁");
                }
                if (item.getBeanUser().getHeight() > 0) {
                    heightView.setVisibility(View.VISIBLE);
                    heightView.setText(item.getBeanUser().getHeight() + "cm");
                }
                if (item.getBeanUser().getWeight() > 0) {
                    weightView.setVisibility(View.VISIBLE);
                    weightView.setText(item.getBeanUser().getWeight() + "kg");
                }
                if (item.getBeanUser().getSex().trim().length() > 0) {
                    if (item.getBeanUser().getSex().equals("1")) {
                        sexImg.setImageResource(R.drawable.map_boy);
                    } else sexImg.setImageResource(R.drawable.map_girl);
                }

                //解析hobby
                Map<Integer, String> map = TagMap.readFile(getContext(), R.raw.tag_map);

                mUserSelfTag = getUserSelfTag(item);

                if (mUserSelfTag != null) {
                    if (mUserSelfTag.size() >= 1) {
                        tag1.setText(map.get(mUserSelfTag.get(0).getId()));
                    }
                    if (mUserSelfTag.size() >= 2) {
                        tag2.setText(map.get(mUserSelfTag.get(1).getId()));
                    }
                    if (mUserSelfTag.size() >= 3) {
                        tag3.setText(map.get(mUserSelfTag.get(2).getId()));
                    }
                    if (mUserSelfTag.size() >= 4) {
                        tag4.setText(map.get(mUserSelfTag.get(3).getId()));
                    }
                }


                CardView cardView = (CardView) view.findViewById(R.id.cardView);
                //跳转到个人项目细节
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AtyDetail.startAtyDetail(getActivity(), AtyDetail.class, item.getBeanUser());
                    }
                });
            }


            public List<BeanTag> getUserSelfTag (BeanViewPagerItem item){
                String hobby = item.getBeanUser().getHobby();
                List<BeanTag> result = new ArrayList<>();
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


            }


        }


        class ShadowTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {

            private ViewPager mViewPager;
            private CardAdapter mAdapter;
            private float mLastOffset;
            private boolean mScalingEnabled;

            public ShadowTransformer(ViewPager viewPager, CardAdapter adapter) {
                mViewPager = viewPager;
                viewPager.addOnPageChangeListener(this);
                mAdapter = adapter;
            }

            public void enableScaling(boolean enable) {
                if (mScalingEnabled && !enable) {
                    // shrink main card
                    CardView currentCard = mAdapter.getCardViewAt(mViewPager.getCurrentItem());
                    if (currentCard != null) {
                        currentCard.animate().scaleY(1);
                        currentCard.animate().scaleX(1);
                    }
                } else if (!mScalingEnabled && enable) {
                    // grow main card
                    CardView currentCard = mAdapter.getCardViewAt(mViewPager.getCurrentItem());
                    if (currentCard != null) {
                        currentCard.animate().scaleY(1.1f);
                        currentCard.animate().scaleX(1.1f);
                    }
                }

                mScalingEnabled = enable;
            }

            @Override
            public void transformPage(View page, float position) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int realCurrentPosition;
                int nextPosition;
                float baseElevation = mAdapter.getBaseElevation();
                float realOffset;
                boolean goingLeft = mLastOffset > positionOffset;

                // If we're going backwards, onPageScrolled receives the last position
                // instead of the current one
                if (goingLeft) {
                    realCurrentPosition = position + 1;
                    nextPosition = position;
                    realOffset = 1 - positionOffset;
                } else {
                    nextPosition = position + 1;
                    realCurrentPosition = position;
                    realOffset = positionOffset;
                }

                // Avoid crash on overscroll
                if (nextPosition > mAdapter.getCount() - 1
                        || realCurrentPosition > mAdapter.getCount() - 1) {
                    return;
                }

                CardView currentCard = mAdapter.getCardViewAt(realCurrentPosition);

                // This might be null if a fragment is being used
                // and the views weren't created yet
                if (currentCard != null) {
                    if (mScalingEnabled) {
                        currentCard.setScaleX((float) (1 + 0.1 * (1 - realOffset)));
                        currentCard.setScaleY((float) (1 + 0.1 * (1 - realOffset)));
                    }
                    currentCard.setCardElevation((baseElevation + baseElevation
                            * (com.owo.module_b_main.CardAdapter.MAX_ELEVATION_FACTOR - 1) * (1 - realOffset)));
                }

                CardView nextCard = mAdapter.getCardViewAt(nextPosition);

                // We might be scrolling fast enough so that the next (or previous) card
                // was already destroyed or a fragment might not have been created yet
                if (nextCard != null) {
                    if (mScalingEnabled) {
                        nextCard.setScaleX((float) (1 + 0.1 * (realOffset)));
                        nextCard.setScaleY((float) (1 + 0.1 * (realOffset)));
                    }
                    nextCard.setCardElevation((baseElevation + baseElevation
                            * (com.owo.module_b_main.CardAdapter.MAX_ELEVATION_FACTOR - 1) * (realOffset)));
                }

                mLastOffset = positionOffset;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        }

        public Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:

                        if (list.size() > 0) {
                            loading.setVisibility(View.GONE);
                            mCardAdapter = new CardPagerAdapter();
                            mCardAdapter.addItems(list);
                            mViewPager.setAdapter(mCardAdapter);
                            mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
                            mCardShadowTransformer.enableScaling(true);
                            mViewPager.setPageTransformer(false, mCardShadowTransformer);
                            mViewPager.setOffscreenPageLimit(3);

                            int friendID = mCardAdapter.getFriendID();

                            if (list.get(0).getMatchDegree() == 0)
                                Common.display(getContext(), "设置交友标签能为你找到最匹配的用户哦...");


                            try {

                                int code =
                                        HttpHelper.getCode(canFollow(friendID));
                                if (code == 201 || code == 203) {
                                    //只有我关注了别人
                                    isLike = true;
                                } else if (code == 202) {
                                    //相互关注
                                    isLike = true;
                                } else if (code == 200) { //可以关注
                                    isLike = false;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            loading.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                            noData.setText("暂时没匹配到合适的用户...");
                        }

                        break;
//                case 2:
//                    loading.setVisibility(View.GONE);
//                    noData.setText("需要有交友标签才能推荐哦，赶快去个人中心设置吧~");
//                    break;
                }
            }
        };


        public String canFollow(int friendID) throws Exception {
            HashMap<String, String> paramHM = new HashMap<>();
            paramHM.put("userID", mUserId + "");
            paramHM.put("friendID", friendID + "");
            return HttpHelper.postData(MyURL.CHECK_IS_FOLLOWED, paramHM, null);
        }

        public String insertFriend(int friendID) throws Exception {
            HashMap<String, String> paramHM = new HashMap<>();
            paramHM.put("userID", mUserId + "");
            paramHM.put("friendID", friendID + "");
            String result = HttpHelper.postData(MyURL.INSERT_FRIEND, paramHM, null);
            return result;
        }

        public String deleteFriend(int friendID) throws Exception {
            HashMap<String, String> paramHM = new HashMap<>();
            paramHM.put("userID", mUserId + "");
            paramHM.put("friendID", friendID + "");
            String result = HttpHelper.postData(MyURL.DELETE_FRIEND, paramHM, null);
            return result;
        }


    }
