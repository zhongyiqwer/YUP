package com.owo.module_b_personal.recyclerview.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.owo.base.HolderBase;
import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.presenter.PresenterPersonal;
import com.owo.module_b_personal.presenter.PresenterPersonalImpl;
import com.owo.module_b_personal.view.ViewPersonal;
import com.owo.module_c_detail.AtyDetail;
import com.owo.utils.Common;
import com.owo.utils.Constants;
import com.owo.utils.DateTimeHelper;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.wao.dogcat.widget.CircleImageView;


import org.json.JSONException;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author XQF
 * @created 2017/5/21
 */
public class HolderActivity extends HolderBase<BeanTask> {


    @BindView(R.id.imageV_item_activity_avatar)
    protected CircleImageView mCircleImageViewAvatar;

    @BindView(R.id.textV_item_activity_username)
    protected TextView mTextViewUserName;

    @BindView(R.id.textV_item_activity_name)
    protected TextView mTextViewActivityName;

    @BindView(R.id.textV_item_activity_deadline)
    protected TextView mTextViewDeadLine;

    @BindView(R.id.textV_item_activity_where)
    protected TextView mTextViewWhere;

    @BindView(R.id.textV_item_activity_status)
    protected TextView mTextViewStatus;

    @BindView(R.id.textV_item_activity_publishtime)
    protected TextView mTextViewPublishtime;

    @BindView(R.id.cardView_item_activity)
    protected CardView mCardView;
    private Context mContext;

    @BindView(R.id.month)
    protected TextView month;

    @BindView(R.id.point)
    protected TextView point;


    private BeanTask mBeanTask;
    private DisplayImageOptions options;
    private String path;
    private int taskType;

    public HolderActivity(Context context, View itemView) {
        super(itemView);
        mContext = context;

        options = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my) //
                .showImageOnFail(R.drawable.my) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
    }

    public void bind(BeanTask beanTask) {
        mBeanTask = beanTask;
        int userId = beanTask.getTaskUserID();
        //头像
//        Glide.with(mContext).load((MyURL.ROOT +  beanTask.getAvatar())).
//                placeholder(R.drawable.my).into(mCircleImageViewAvatar);
        path = MyURL.ROOT+beanTask.getAvatar();

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

        taskType = beanTask.getTaskType();

        mTextViewUserName.setText(  beanTask.getUserName());
        mTextViewActivityName.setText(beanTask.getTaskName());
        mTextViewDeadLine.setText("截止时间：" +
                DateTimeHelper.timeMillis2FormatTime(
                        beanTask.getTaskDeadLine(), "yyyy.MM.dd"));
        String latitude = beanTask.getTaskLatitude();
        String longitude = beanTask.getTaskLongitude();
        mTextViewWhere.setText(getWhere(latitude, longitude));


        String formatDate = DateTimeHelper.timeLogic(DateTimeHelper
                        .timeMillis2FormatTime(beanTask.getTaskPublishTime(), DateTimeHelper.DATE_FORMAT_TILL_SECOND),
                DateTimeHelper.DATE_FORMAT_TILL_SECOND);
        mTextViewPublishtime.setText(formatDate);


        if (beanTask.getTaskStatus() == Constants.TASK_STATUS_ENROOLING) {
            if (beanTask.getTaskType()==1){
                mTextViewStatus.setText("线上活动");
            }else {
                int sum = beanTask.getTaskTakenNum();
                int max = beanTask.getTaskMaxNum();
                String s = sum + "/" + max;
                if (sum < max) {

                    s = s + "(尚未满人)";
                }
                mTextViewStatus.setText(s);
            }
        } else if (beanTask.getTaskStatus() == Constants.TASK_STATUS_WORKING) {
            mTextViewStatus.setText("等待评价");
        }else{
            month.setText( DateTimeHelper.timeMillis2FormatTime(
                    beanTask.getTaskPublishTime(), "M"));
            point.setTextColor(mContext.getResources().getColor(R.color.textGray));
        }

        mCircleImageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    HashMap<String,String> hm = new HashMap<>();
                    hm.put("id",mBeanTask.getTaskUserID()+"");
                    String str = HttpHelper.postData(MyURL.GET_USER_BY_ID,hm,null);
                    HashMap<String, Object> map = HttpHelper.AnalysisUserInfo(str);
                    BeanUser beanUser = Constants.getBeanUserFromMap(map);
                    if (beanUser!=null && beanUser.getId()!=Common.userID)
                        AtyDetail.startAtyDetail(mContext, AtyDetail.class, beanUser);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }


    public String getWhere(String latitude, String longitude) {
        String locStr = "";
        try {
            locStr =
                    HttpHelper.getLocation(
                            HttpHelper.sendGet(com.owo.utils.util_http.MyURL.BAIDU_MAP_URL + "&location="
                                    + latitude + "," + longitude, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locStr;
    }

    @OnClick(R.id.cardView_item_activity)
    public void onClickItem() {
        if (taskType == 1)
            AtyDetail.startAtyDetail(mContext, AtyDetail.class, mBeanTask,AtyDetail.VALUE_TYPE_ONLINEATYDETAIL);
        else AtyDetail.startAtyDetail(mContext, AtyDetail.class, mBeanTask,AtyDetail.VALUE_TYPE_ATYDETAIL);
    }
}
