package com.owo.module_b_home.recyclerview.changed.holder;

import android.content.Context;
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
import com.owo.utils.MapCalculator;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;

import com.wao.dogcat.widget.CircleImageView;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 线上活动
 * @author XQF
 * @created 2017/5/14
 */
public class HolderBlue extends HolderTest<BeanColorTask> {

    @BindView(R.id.item_blue_official)
    protected ImageView official;

    //头像
    @BindView(R.id.item_blue_portrait)
    protected CircleImageView mCircleImageViewAvatar;

    //性别
    @BindView(R.id.item_blue_sex)
    protected ImageView mImageViewSex;

    //标题
    @BindView(R.id.item_blue_title)
    protected TextView mTextViewTitle;

    //线上活动剩余时间
    @BindView(R.id.item_blue_resttime)
    protected TextView mTextViewRestTime;

    //活动参与人数
    @BindView(R.id.item_blue_sum)
    protected TextView mTextViewSum;

    @BindView(R.id.item_blue_dis)
    protected TextView mTextViewDis;

    @BindView(R.id.item_blue_time)
    protected TextView mTextViewTime;

    @BindView(R.id.item_blue_name)
    protected TextView mTextViewName;

    private Context context;
    private BeanColorTask beanBlue;

    private DisplayImageOptions options;
    private String path;


    @OnClick(R.id.item_blue_portrait)
    public void seeUser(){
        try {

            HashMap<String,String> hm = new HashMap<>();
            hm.put("id",beanBlue.getUserID()+"");
            String str = HttpHelper.postData(MyURL.GET_USER_BY_ID,hm,null);
            HashMap<String, Object> map = HttpHelper.AnalysisUserInfo(str);
            BeanUser beanUser = Constants.getBeanUserFromMap(map);
            if (beanUser!=null && beanUser.getId()!=Common.userID)
                AtyDetail.startAtyDetail(context, AtyDetail.class, beanUser);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }




    public HolderBlue(Context context, View itemView) {
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
    public void bind(BeanColorTask beanBlue) {
        this.beanBlue = beanBlue;
        //Glide.with(context).load(MyURL.ROOT+beanBlue.getAvatatrUrl()).
                //placeholder(R.drawable.my).into(mCircleImageViewAvatar);

        path = MyURL.ROOT+beanBlue.getAvatatrUrl();

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





        double distance = (beanBlue.getDis() / 1000);
        String disStr = distance >= 1 ? (  distance>100?">100km": (int) distance + "km") : (((int)(distance*1000))+"m");
        mTextViewDis.setText(disStr);
        if (beanBlue.getSex().trim().length()>0 &&
                beanBlue.getSex().trim().equals("2")){
            mImageViewSex.setImageResource(R.drawable.map_girl);
        }
        mTextViewName.setText(beanBlue.getName());
        mTextViewTitle.setText(beanBlue.getTitle());
        mTextViewSum.setText(beanBlue.getSum());
        mTextViewRestTime.setText(
                DateTimeHelper.timeMillis2FormatTime(
                        beanBlue.getTime(),"yyyy.MM.dd"));
        String formatDate  = DateTimeHelper.timeLogic(DateTimeHelper
                        .timeMillis2FormatTime(beanBlue.getmPubTime(),
                                DateTimeHelper.DATE_FORMAT_TILL_SECOND),
                DateTimeHelper.DATE_FORMAT_TILL_SECOND);
        mTextViewTime.setText(formatDate);

    }


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
        beanBlue.setTaskIamge(task.getImage());
        beanBlue.setTaskContent(task.getTaskContent());

        return beanBlue;

    }



    /**
     * 活动的点击事件
     */
    @OnClick(R.id.cardView_blue)
    public void onClickCardView() {
        //线上
        AtyDetail.startAtyDetail(context, AtyDetail.class, colorTaskToTask(beanBlue),AtyDetail.VALUE_TYPE_ONLINEATYDETAIL);
    }
}
