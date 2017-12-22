package com.owo.module_b_home.recyclerview.changed.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_home.recyclerview.changed.bean.BeanColorTask;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_c_detail.AtyDetail;
import com.owo.utils.Common;
import com.owo.utils.Constants;
import com.owo.utils.DateTimeHelper;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.wao.dogcat.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 玩的【线下】
 * @author XQF
 * @created 2017/5/14
 */
public class HolderRed extends HolderTest<BeanColorTask> {

    //头像
    @BindView(R.id.item_red_portrait)
    protected CircleImageView mCircleImageViewAvatar;

    //性别
    @BindView(R.id.item_red_sex)
    protected ImageView mImageViewSex;

    @BindView(R.id.item_red_official)
    protected ImageView official;

    //标题
    @BindView(R.id.item_red_title)
    protected TextView mTextViewTitle;

    //线上活动剩余时间
    @BindView(R.id.item_red_resttime)
    protected TextView mTextViewRestTime;

    //活动参与人数
    @BindView(R.id.item_red_sum)
    protected TextView mTextViewSum;

    @BindView(R.id.item_red_dis)
    protected TextView mTextViewDis;

    @BindView(R.id.item_red_time)
    protected TextView mTextViewTime;

    @BindView(R.id.item_red_name)
    protected TextView mTextViewName;


//    @BindView(R.id.item_red_where)
//    protected TextView mTextViewWhere;
    private Context context;

    private BeanColorTask beanBlue;
    private DisplayImageOptions options;
    private String path;

    @OnClick(R.id.item_red_portrait)
    public void seeUser(){
        try {

            HashMap<String,String> hm = new HashMap<>();
            hm.put("id",beanBlue.getUserID()+"");
            String str = HttpHelper.postData(MyURL.GET_USER_BY_ID,hm,null);
            HashMap<String, Object> map = HttpHelper.AnalysisUserInfo(str);
            BeanUser beanUser = Constants.getBeanUserFromMap(map);
            if (beanUser!=null && beanUser.getId()!= Common.userID)
                AtyDetail.startAtyDetail(context, AtyDetail.class, beanUser);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public HolderRed(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;

        // 使用ImageLoader加载网络图片
        options = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my) //
                .showImageOnFail(R.drawable.my) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
    }

    @Override
    public void bind(BeanColorTask beanRed) {
        beanBlue = beanRed;
//        Glide.with(context).load(MyURL.ROOT+beanRed.getAvatatrUrl()).
//                placeholder(R.drawable.my).into(mCircleImageViewAvatar); path = MyURL.ROOT+beanBlue.getAvatatrUrl();

        if (beanRed.getId().equals("78")){
            official.setVisibility(View.VISIBLE);
        }

        path = MyURL.ROOT+beanRed.getAvatatrUrl();
        //防止图片闪烁
        final String tag = (String)mCircleImageViewAvatar.getTag();

        if (tag == null || !tag.equals(path)) {
            mCircleImageViewAvatar.setTag(path);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(path, mCircleImageViewAvatar, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            mCircleImageViewAvatar.setTag(path);//确保下载完成再打tag.
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
        }

        double distance = (beanRed.getDis() / 1000);
        String disStr = distance >= 1 ? (  distance>100?">100km": (int) distance + "km") : (((int)(distance*1000))+"m");
        mTextViewDis.setText(disStr);
        if (beanRed.getSex().trim().length()>0 &&
                beanRed.getSex().trim().equals("2")){
            mImageViewSex.setImageResource(R.drawable.map_girl);
        }
        mTextViewName.setText(beanRed.getName());
        mTextViewTitle.setText(beanRed.getTitle());
        String s = beanRed.getSum()+"/"+beanRed.getmMax();
        if (Integer.parseInt(beanRed.getSum())<Integer.parseInt(beanRed.getmMax())){
            s=s+"(尚未满人)";
        }

        mTextViewSum.setText(s);
        mTextViewRestTime.setText(
                DateTimeHelper.timeMillis2FormatTime(
                        beanRed.getTime(),"yyyy.MM.dd"));
        String formatDate  = DateTimeHelper.timeLogic(DateTimeHelper
                        .timeMillis2FormatTime(beanRed.getmPubTime(),
                                DateTimeHelper.DATE_FORMAT_TILL_SECOND),
                DateTimeHelper.DATE_FORMAT_TILL_SECOND);
        mTextViewTime.setText(formatDate);
        //mTextViewWhere.setText(getWhere(beanRed.getmTaskLatitude(),beanRed.getmTaskLongitude()));
    }

//    public String getWhere(String latitude, String longitude) {
//        String locStr = "";
//        try {
//            locStr =
//                    HttpHelper.getLocation(
//                            HttpHelper.sendGet(com.owo.utils.util_http.MyURL.BAIDU_MAP_URL+
//                                    "&location=" +latitude+","+longitude,null));
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//        return locStr;
//    }

    public BeanTask colorTaskToTask(BeanColorTask task) {
        BeanTask beanBlue = new BeanTask();
        beanBlue.setAvatar(task.getAvatatrUrl());
        beanBlue.setTaskLatitude(task.getmTaskLatitude());
        beanBlue.setTaskLongitude(task.getmTaskLongitude());
        beanBlue.setTaskId(Integer.parseInt(task.getId()));
        beanBlue.setTaskPublishTime(task.getmPubTime());
        beanBlue.setUserName(task.getName());
        int sex = 0;
        if (task.getSex().trim().length()>=0){
            sex = Integer.parseInt(task.getSex());
        }
        beanBlue.setSex(sex);
        int max = 0;
        if (task.getmMax().trim().length()>=0){
            max = Integer.parseInt(task.getmMax());
        }
        beanBlue.setTaskMaxNum(max);
        beanBlue.setTaskDeadLine(task.getTime());
        beanBlue.setTaskName(task.getTitle());
        beanBlue.setTaskUserID(Integer.parseInt(task.getUserID()));
        beanBlue.setTaskTakenNum(Integer.parseInt(task.getSum()));
        beanBlue.setTaskIamge(task.getImage());
        beanBlue.setTaskStatus(task.getStatus());
        beanBlue.setTaskMoney(task.getMoney());
        beanBlue.setTaskScore(task.getTaskScore());
        beanBlue.setTaskContent(task.getTaskContent());

        return beanBlue;

    }

    @OnClick(R.id.cardView_red)
    public void onClickCardView() {
        //线下 --> 活动结束，查看评论
        if (beanBlue.getStatus() == 3) {
            AtyDetail.startAtyDetail(context, AtyDetail.class, colorTaskToTask(beanBlue), AtyDetail.VALUE_TYPE_ADDATYCMT);
        }
            else {
            AtyDetail.startAtyDetail(context, AtyDetail.class, colorTaskToTask(beanBlue), AtyDetail.VALUE_TYPE_ATYDETAIL);
        }
    }
}
