package com.owo.module_c_detail.detail_activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mob.tools.utils.ResHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.base.FragBase;
import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_personal.bean.BeanMoment;
import com.owo.utils.Common;
import com.owo.utils.DateTimeHelper;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.wao.dogcat.widget.CircleImageView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.socialization.Comment;
import cn.sharesdk.socialization.CommentFilter;
import cn.sharesdk.socialization.CommentListener;
import cn.sharesdk.socialization.LikeListener;
import cn.sharesdk.socialization.QuickCommentBar;
import cn.sharesdk.socialization.Socialization;
import cn.sharesdk.socialization.component.ReplyTooFrequentlyException;
import cn.sharesdk.socilization.MyPlatform;


/**
 * @author XQF
 * @created 2017/5/23
 */
public class FragDetailOnlineActivity extends FragBase {


    public static FragDetailOnlineActivity newInstance(BeanTask beanTask) {
        FragDetailOnlineActivity fragDetailActivity = new FragDetailOnlineActivity();
        mBeanTask = beanTask;
//        Bundle args = new Bundle();
//        args.putSerializable("beantask", beanTask);
//        fragDetailActivity.setArguments(args);
        return fragDetailActivity;
    }

    public static BeanTask mBeanTask;

    @BindView(R.id.imageV_item_activity_avatar)
    protected CircleImageView head;

    @BindView(R.id.textV_item_activity_username)
    protected TextView username;

    @BindView(R.id.textV_item_activity_time)
    protected TextView time;

    @BindView(R.id.textV_item_activity_title)
    protected TextView title;

    @BindView(R.id.textV_item_activity_content)
    protected TextView actContent;

    @BindView(R.id.textV_item_activity_image)
    protected ImageView img;

    @BindView(R.id.textV_item_activity_deadline)
    protected TextView deadline;


    @BindView(R.id.textV_item_activity_status)
    protected TextView userNum2;

    @OnClick(R.id.btnBack)
    public void back(){
        getActivity().finish();
    }



    @BindView(R.id.qcBar)
    protected QuickCommentBar qcBar;
    // 主题id
    private String topicId;
    // 主题标题
    private String topicTitle;
    // 主题发布时间
    private String topicPublishTime;
    // 主题作者
    private String topicAuthor;
    private OnekeyShare oks;
    private CommentFilter filter;
    private DisplayImageOptions options1,options2;
    private String path1,path2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail_online_activity, container, false);
        ButterKnife.bind(this, view);
        //mBeanTask = (BeanTask) getArguments().get("beantask");

        userNum2.setText("当前参与人数："+mBeanTask.getTaskTakenNum());
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
                .showImageForEmptyUri(R.drawable.head) //
                .showImageOnFail(R.drawable.head) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//



        path1 = MyURL.ROOT + mBeanTask.getAvatar();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                displayImage(path1, head, options1);
        if ( mBeanTask.getTaskIamge().trim().length()!=0) {
            path2 = MyURL.ROOT + mBeanTask.getTaskIamge();
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                    displayImage(path2, img, options2);
        }
        else img.setVisibility(View.GONE);

        actContent.setText(mBeanTask.getTaskContent());

        username.setText(  mBeanTask.getUserName());
        title.setText(mBeanTask.getTaskName());
        deadline.setText("活动截止时间：" +
                DateTimeHelper.timeMillis2FormatTime(
                        mBeanTask.getTaskDeadLine(), "yyyy.MM.dd"));

        if(Long.parseLong(mBeanTask.getTaskDeadLine())< System.currentTimeMillis())
        {
            String s = "该活动已结束，但你可以继续评论与点赞";
            deadline.setText(s);
            deadline.setTextColor(getContext().getResources().getColor(R.color.textGray));

        }


        String formatDate = DateTimeHelper.timeLogic(DateTimeHelper
                        .timeMillis2FormatTime(mBeanTask.getTaskDeadLine(), DateTimeHelper.DATE_FORMAT_TILL_SECOND),
                DateTimeHelper.DATE_FORMAT_TILL_SECOND);
        time.setText(formatDate);





        ////////////////第三方评论组件

        //评论详情上面的
        topicId =  mBeanTask.getTaskId()+"";
        topicTitle = mBeanTask.getUserName()+"的动态";
        topicPublishTime = formatDate;
        topicAuthor = mBeanTask.getUserName();

        Socialization service = ShareSDK.getService(Socialization.class);
        service.setCustomPlatform(new MyPlatform(getContext()));
        initOnekeyShare();
        initQuickCommentBar();

        //设置评论监听
        Socialization.setCommentListener(new CommentListener() {

            @Override
            public void onSuccess(Comment comment) {
                int resId = ResHelper.getStringRes(getContext(), "ssdk_socialization_reply_succeeded");
                if (resId > 0) {
                    if (addTaskTakenNum()==200){
                        mBeanTask.setTaskTakenNum(mBeanTask.getTaskTakenNum()+1);
                        userNum2.setText("当前参与人数："+mBeanTask.getTaskTakenNum());
                    }

                }
            }

            @Override
            public void onFail(Comment comment) {
                //Toast.makeText(context, comment.getFileCodeString(context), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable throwable) {
                if (throwable instanceof ReplyTooFrequentlyException) {
                    int resId = ResHelper.getStringRes(getContext(), "ssdk_socialization_replay_too_frequently");
                    if (resId > 0) {
                        //Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    throwable.printStackTrace();
                }
            }
        });
        //设置点赞监听
        Socialization.setLikeListener(new LikeListener() {

            @Override
            public void onSuccess(String topicId, String topicTitle, String commentId) {
                //if(mBeanMoment.getMomentUserId()!= com.wao.dogcat.utils.Common.userID)
//                    //点赞成功
//                    if (mBeanUser!=null)
//                        insertMsg(mBeanMoment.getMomentUserId()+"","1", com.wao.dogcat.utils.Common.user.getUserName()+"赞了你的动态");
            }

            @Override
            public void onFail(String topicId, String topicTitle, String commentId, String error) {
            }

        });
        return view;
    }

    public int  addTaskTakenNum(){
        int code = 0;
        try {
            HashMap<String,String> map = new HashMap<>();
            map.put("taskID",mBeanTask.getTaskId()+"");
            String json = HttpHelper.postData(MyURL.ADD_TASK_TAKEN_NUM,map,null);
            code = HttpHelper.getCode(json);
        }catch (Exception e){
            e.printStackTrace();
        }

        return code;
    }

    //Socialization服务依赖OnekeyShare组件，此方法初始化一个OnekeyShare对象
    private void initOnekeyShare() {
        oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("yup! -- You Will Never Walk Alone");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://dwz.cn/OWO_yyb");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我们在玩yup!，快来邀请TA和你一起玩吧~");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://pp.myapp.com/ma_icon/0/icon_52450069_1491548043/96");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://dwz.cn/OWO_yyb");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("应用下载");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://dwz.cn/OWO_yyb");

        // oks.show(context);


    }

    private void initQuickCommentBar() {
        qcBar.setTopic(topicId, topicTitle, topicPublishTime, topicAuthor);
        //qcBar.setTextToShare(getString(R.string.share_content));
        qcBar.getBackButton().setVisibility(View.GONE);
        //qcBar.getBackButton().setOnClickListener(this);
        qcBar.setAuthedAccountChangeable(false);

        CommentFilter.Builder builder = new CommentFilter.Builder();
        // 非空过滤器
        builder.append(new CommentFilter.FilterItem() {
            // 返回true表示是垃圾评论
            public boolean onFilter(String comment) {
                if (TextUtils.isEmpty(comment)) {
                    return true;
                } else if (comment.trim().length() <= 0) {
                    return true;
                } else if (comment.trim().toLowerCase().equals("null")) {
                    return true;
                }
                return false;
            }

            @Override
            public int getFilterCode() {
                return 0;
            }
        });
        // 字数上限过滤器
        builder.append(new CommentFilter.FilterItem() {
            // 返回true表示是垃圾评论
            public boolean onFilter(String comment) {
                if (comment != null) {
                    String pureComment = comment.trim();
                    String wordText = ResHelper.toWordText(pureComment, 140);
                    if (wordText.length() != pureComment.length()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public int getFilterCode() {
                return 0;
            }
        });
        filter = builder.build();
        qcBar.setCommentFilter(filter);
        qcBar.setOnekeyShare(oks);
    }

    private void insertMsg(String receiverID,String type,String content){
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("userID", Common.userID + "");
            hashMap.put("receiverID", receiverID);
            hashMap.put("msgType", type);
            hashMap.put("msgContent", content);
            HttpHelper.postData(MyURL.INSERT_MSG, hashMap, null);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
