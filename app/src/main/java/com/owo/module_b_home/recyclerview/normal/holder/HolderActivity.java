package com.owo.module_b_home.recyclerview.normal.holder;

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
import com.owo.utils.Common;
import com.owo.utils.DateTimeHelper;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.owo.base.HolderBase;
import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.presenter.PresenterPersonal;
import com.owo.module_b_personal.presenter.PresenterPersonalImpl;
import com.owo.module_b_personal.view.ViewPersonal;
import com.owo.module_c_detail.AtyDetail;
import com.owo.utils.Constants;
import com.wao.dogcat.widget.CircleImageView;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author XQF
 * @created 2017/5/25
 */
public class HolderActivity extends HolderBase<BeanTask> implements ViewPersonal {


    @BindView(R.id.imageV_normal_item_activity_avatar)
    protected CircleImageView mCircleImageViewAvatar;
    @BindView(R.id.textV_normal_item_activity_username)
    protected TextView mTextViewUserName;
    @BindView(R.id.textV_normal_item_activity_name)
    protected TextView mTextViewActivityName;
    @BindView(R.id.textV_normal_item_activity_waitcomment)
    protected TextView mTextViewWaitComment;
    @BindView(R.id.textV_normal_item_activity_news)
    protected TextView mTextViewNews;
    @BindView(R.id.textV_normal_item_activity_deadline)
    protected TextView mTextViewTime;
    @BindView(R.id.textV_normal_item_activity_where)
    protected TextView mTextViewWhere;
    @BindView(R.id.cardView_normal_item_activity)
    protected CardView mCardView;

    private Context mContext;
    private PresenterPersonal mPresenterPersonal;
    private BeanTask mBeanTask;

    private BeanUser mBeanUser;
    private DisplayImageOptions options;
    private String path;
    private int taskType;


    public HolderActivity(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mPresenterPersonal = new PresenterPersonalImpl(this);

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
    public void bind(BeanTask beanTask) {
        mBeanTask = beanTask;
        int status = beanTask.getTaskStatus();
        int userId = beanTask.getTaskUserID();
        mPresenterPersonal.loadUserPersaonlInfoById(userId);
        taskType = mBeanTask.getTaskType();

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




        mTextViewUserName.setText(beanTask.getUserName());
        mTextViewActivityName.setText(beanTask.getTaskName());
        //正在报名中
        if (status == Constants.TASK_STATUS_ENROOLING) {
            mTextViewWaitComment.setVisibility(View.GONE);
            mTextViewNews.setVisibility(View.VISIBLE);
            if (beanTask.getTaskType()==1){
                mTextViewNews.setText("线上活动");
            }else {
                int sum = beanTask.getTaskTakenNum();
                int max = beanTask.getTaskMaxNum();
                String s = sum + "/" + max;
                if (sum < max) {
                    s = s + "(尚未满人)";
                }
                mTextViewNews.setText(s);
            }
        } else if (status == Constants.TASK_STATUS_WORKING) {
            mTextViewNews.setVisibility(View.GONE);
            mTextViewWaitComment.setVisibility(View.VISIBLE);
        }
        mTextViewTime.setText("截止时间："+
                DateTimeHelper.timeMillis2FormatTime(
                        beanTask.getTaskDeadLine(),"yyyy.MM.dd"));
        String latitude = beanTask.getTaskLatitude();
        String longitude = beanTask.getTaskLongitude();
        mTextViewWhere.setText(getWhere(latitude, longitude));
        mCircleImageViewAvatar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mBeanUser!=null && mBeanUser.getId()!= Common.userID)
                            AtyDetail.startAtyDetail(mContext, AtyDetail.class, mBeanUser);
                    }
                }
        );
    }

    @Override
    public void getPersonalInfo(BeanUser beanUser) {
        mBeanUser = beanUser;
    }

    public String getWhere(String latitude, String longitude) {
        String locStr = "";
        try {
            locStr =
                    HttpHelper.getLocation(
                            HttpHelper.sendGet(com.owo.utils.util_http.MyURL.BAIDU_MAP_URL+
                                    "&location=" +latitude+","+longitude,null));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return locStr;
    }

    /**
     * 活动的点击事件
     */
    @OnClick(R.id.cardView_normal_item_activity)
    public void onClickCardView() {
        if (taskType == 1)
        AtyDetail.startAtyDetail(mContext, AtyDetail.class, mBeanTask,AtyDetail.VALUE_TYPE_ONLINEATYDETAIL);
        else AtyDetail.startAtyDetail(mContext, AtyDetail.class, mBeanTask,AtyDetail.VALUE_TYPE_ATYDETAIL);
    }
}
