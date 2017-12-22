package com.owo.module_b_home.recyclerview.changed.holder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 吃的【线下】
 * @author XQF
 * @created 2017/5/14
 */
public class HolderYellow extends HolderTest<BeanColorTask> {
    @BindView(R.id.item_yellow_official)
    protected ImageView official;

    //头像
    @BindView(R.id.item_yellow_portrait)
    protected CircleImageView mCircleImageViewAvatar;

    //性别
    @BindView(R.id.item_yellow_sex)
    protected ImageView mImageViewSex;

    //标题
    @BindView(R.id.item_yellow_title)
    protected TextView mTextViewTitle;

//    //线上活动剩余时间
//    @BindView(R.id.item_yellow_resttime)
//    protected TextView mTextViewRestTime;

    //活动参与人数
    @BindView(R.id.item_yellow_sum)
    protected TextView mTextViewSum;

    @BindView(R.id.item_yellow_dis)
    protected TextView mTextViewDis;

//    @BindView(R.id.item_yellow_time)
//    protected TextView mTextViewTime;

    @BindView(R.id.item_yellow_name)
    protected TextView mTextViewName;


    @BindView(R.id.item_jike_time)
    protected TextView jikeTime;

    private TimeCount time;
    private long restTime;

//    @BindView(R.id.item_yellow_where)
//    protected TextView mTextViewWhere;
    private Context context;

    private BeanColorTask beanBlue;
    private DisplayImageOptions options;
    private String path;

    @OnClick(R.id.item_yellow_portrait)
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

    public HolderYellow(Context context, View itemView) {
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
    public void bind(BeanColorTask beanYellow) {
        beanBlue = beanYellow;
//        Glide.with(context).load(MyURL.ROOT+beanYellow.getAvatatrUrl()).
//                placeholder(R.drawable.my).into(mCircleImageViewAvatar);
        path = MyURL.ROOT+beanYellow.getAvatatrUrl();

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
        double distance = (beanYellow.getDis() / 1000);
        String disStr = distance >= 1 ? (  distance>100?">100km": (int) distance + "km") : (((int)(distance*1000))+"m");
        mTextViewDis.setText(disStr);
        if (beanYellow.getSex().trim().length()>0 &&
                beanYellow.getSex().trim().equals("2")){
            mImageViewSex.setImageResource(R.drawable.map_girl);
        }
        mTextViewName.setText(beanYellow.getName());
        mTextViewTitle.setText(beanYellow.getTitle());
        String s = beanYellow.getSum()+"/"+beanYellow.getmMax();
        if (Integer.parseInt(beanYellow.getSum())<Integer.parseInt(beanYellow.getmMax())){
            s=s+"(尚未满人)";
        }
        mTextViewSum.setText(s);
//        mTextViewRestTime.setText(
//                DateTimeHelper.timeMillis2FormatTime(
//                        beanYellow.getTime(),"yyyy.MM.dd"));
//        String formatDate  = DateTimeHelper.timeLogic(DateTimeHelper
//                        .timeMillis2FormatTime(beanYellow.getmPubTime(),
//                                DateTimeHelper.DATE_FORMAT_TILL_SECOND),
//                DateTimeHelper.DATE_FORMAT_TILL_SECOND);
//        mTextViewTime.setText(formatDate);
        //mTextViewWhere.setText(getWhere(beanYellow.getmTaskLatitude(),beanYellow.getmTaskLongitude()));
        restTime =  Long.parseLong(beanYellow.getTime()) - System.currentTimeMillis();
        int count = 0;
        if (restTime > 0 && count == 0) {
            count++;
            time = new TimeCount(restTime, 1000);// 构造CountDownTimer对象
            time.start();
        }else {
            //time.cancel();
            jikeTime.setText("已过期");
            jikeTime.setTextColor(context.getResources().getColor(R.color.textGray));
        }
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
        beanBlue.setTaskType(task.getTaskType());
        beanBlue.setTaskContent(task.getTaskContent());

        return beanBlue;

    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            jikeTime.setText("已过期");
            jikeTime.setTextColor(context.getResources().getColor(R.color.textGray));
            time.cancel();
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            jikeTime.setText(DateTimeHelper.countdownMillis2FormatTime(millisUntilFinished));
        }
    }

    @OnClick(R.id.cardView_yellow)
    public void onClickCardView() {
        if (beanBlue.getStatus() == 3)
            AtyDetail.startAtyDetail(context, AtyDetail.class, colorTaskToTask(beanBlue),AtyDetail.VALUE_TYPE_ADDATYCMT);
        else
            AtyDetail.startAtyDetail(context, AtyDetail.class, colorTaskToTask(beanBlue),AtyDetail.VALUE_TYPE_ATYDETAIL);
    }
}
