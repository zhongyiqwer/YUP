package com.owo.module_b_explore.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.owo.module_a_selectlabel.bean.BeanTag;
import com.owo.module_b_explore.bean.BeanViewPagerItem;
import com.owo.utils.Common;
import com.owo.utils.TagMap;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.owo.module_b_explore.bean.BeanRecyclerViewItem;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.presenter.PresenterPersonal;
import com.owo.module_b_personal.presenter.PresenterPersonalImpl;
import com.owo.module_b_personal.view.ViewPersonal;
import com.owo.module_c_detail.AtyDetail;

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
import cn.jpush.im.android.api.JMessageClient;

/**
 * @author XQF
 * @created 2017/5/11
 */
public class Holder extends RecyclerView.ViewHolder implements ViewPersonal {

    @BindView(R.id.frag_explore_nearby_item_name)
    protected TextView mTextViewName;

    @BindView(R.id.frag_explore_nearby_item_avatar)
    protected CircleImageView mCircleImageView;

    @BindView(R.id.frag_explore_nearby_item_dis)
    protected TextView mTextViewDis;

    @BindView(R.id.frag_explore_nearby_item_sex)
    protected ImageView mImageViewSex;

    @BindView(R.id.frag_explore_nearby_item_age)
    protected TextView mTextViewAge;

    @BindView(R.id.frag_explore_nearby_item_height)
    protected TextView mTextViewHeight;

    @BindView(R.id.frag_explore_nearby_item_weight)
    protected TextView mTextViewWeight;

    @BindView(R.id.frag_explore_nearby_item_autograph)
    protected TextView mTextViewAutograph;

    @BindView(R.id.frag_explore_nearby_item_status)
    protected Button mButtonStatus;

    @BindView(R.id.nearby_item_cardview)
    protected CardView mCardView;


    private PresenterPersonal mPresenterPersonal;
    private BeanUser mBeanUser;
    private Context mContext;


    private DisplayImageOptions options;
    private String path;
    private List<BeanTag> mUserSelfTag;

    private int userID,friendID;

    public Holder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mPresenterPersonal = new PresenterPersonalImpl(this);
        mContext = context;
        // 使用ImageLoader加载网络图片
        options = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my) //
                .showImageOnFail(R.drawable.my) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
        Common.userSP = mContext.getSharedPreferences("userSP", 0);
        userID = Common.userSP.getInt("ID",0);
    }


    public void bind(final BeanRecyclerViewItem item) {
        friendID = item.getUserId();
        mPresenterPersonal.loadUserPersaonlInfoById(friendID);
        mTextViewName.setText(item.getUsername());
        path = MyURL.ROOT+item.getUrlAvatar();


        //防止图片闪烁
        final String tag = (String)mCircleImageView.getTag();

        if (tag == null || !tag.equals(path)) {
            mCircleImageView.setTag(path);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(path, mCircleImageView, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            mCircleImageView.setTag(path);//确保下载完成再打tag.
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
        }




        double distance = (item.getDis() / 1000);
        String disStr = distance >= 1 ? (  distance>100?">100km": (int) distance + "km") : (((int)(distance*1000))+"m");
        mTextViewDis.setText(disStr);
        if (item.getSex() == 2) {
            mImageViewSex.setImageResource(R.drawable.frag_home_change_item_wemen);
        }
        if (item.getAge()>0)
        mTextViewAge.setText(item.getAge()+"岁");
        else mTextViewAge.setVisibility(View.GONE);
        if (item.getHeight()>0)
        mTextViewHeight.setText(item.getHeight()+"cm");
        else mTextViewHeight.setVisibility(View.GONE);
        if (item.getWeight()>0)
        mTextViewWeight.setText(item.getWeight()+"kg");
        else mTextViewWeight.setVisibility(View.GONE);
       // mTextViewAutograph.setText(item.getSignature());
        //解析hobby
        Map<Integer, String> map = TagMap.readFile(mContext, R.raw.tag_map);

        mUserSelfTag = getUserSelfTag(item);
        StringBuffer tagStrBr = new StringBuffer("");
        if (mUserSelfTag != null) {
            if (mUserSelfTag.size()<=4) {
                for (int i = 0; i < mUserSelfTag.size(); i++) {
                    tagStrBr.append(map.get(mUserSelfTag.get(i).getId())+" ");
                }
            }else {
                for (int i = 0; i < 4; i++) {
                    tagStrBr.append(map.get(mUserSelfTag.get(i).getId())+" ");
                }
            }
            mTextViewAutograph.setText(tagStrBr.toString());
        }





        mButtonStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int counter = item.getCounter();
                if (counter % 2 == 0) {
                    //mButtonStatus.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));

                    try {

                        int code2 =
                                HttpHelper.getCode(canFollow(friendID));
                        if (code2 == 200) {
                            //关注别人
                            if (HttpHelper.getCode(insertFriend(friendID)) == 200) {
                                mButtonStatus.setText("已关注");
                                JMessageClient.createSingleTextMessage("yup_"+item.getUserId(), "4d454221295c35af705cc26e", "Hello~ 我关注你啦！\n(点我头像加好友)");
                            }
                            else Common.display(mContext, "操作失败");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                } else {
                   // mButtonStatus.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
                    mButtonStatus.setText("添加关注");
                }
                counter++;
                item.setCounter(counter);
            }
        });
    }

    @Override
    public void getPersonalInfo(BeanUser beanUser) {
        mBeanUser = beanUser;
    }
    public List<BeanTag> getUserSelfTag (BeanRecyclerViewItem item){
        String hobby = item.getHobby();
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

    /**
     * 跳往主界面
     */
    @OnClick(R.id.nearby_item_cardview)
    public void onClickCardView() {
        AtyDetail.startAtyDetail(mContext, AtyDetail.class, mBeanUser);
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    };


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
}
