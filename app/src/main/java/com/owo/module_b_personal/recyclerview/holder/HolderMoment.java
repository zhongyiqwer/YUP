package com.owo.module_b_personal.recyclerview.holder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mob.tools.utils.ResHelper;
import com.owo.base.HolderBase;
import com.owo.module_b_personal.bean.BeanComment;
import com.owo.module_b_personal.bean.BeanLike;
import com.owo.module_b_personal.bean.BeanMoment;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.presenter.PresenterPersonal;
import com.owo.module_b_personal.presenter.PresenterPersonalImpl;
import com.owo.module_b_personal.recyclerview.adapter.AdapterFragPersonalDownViewpagerMoment;
import com.owo.module_b_personal.view.ViewPersonalViewpagerLeftHolder;
import com.owo.module_b_personal.widgets.FragPersonalDownViewpagerMoment;
import com.owo.module_c_detail.AtyDetail;
import com.owo.utils.Common;
import com.owo.utils.DateTimeHelper;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.wao.dogcat.widget.CircleImageView;


import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
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
 * @created 2017/5/17
 */
public class HolderMoment extends HolderBase<BeanMoment> implements ViewPersonalViewpagerLeftHolder {

    @BindView(R.id.moment_avatar)
    protected CircleImageView mCircleImageView;


    @BindView(R.id.moment_username)
    protected TextView mTextViewUsername;

    @BindView(R.id.moment_content_text)
    protected TextView mTextViewContentText;

    @BindView(R.id.moment_content_image)
    protected ImageView mImageViewContentImage;


//    @BindView(R.id.moment_cmnt)
//    protected ImageView mImageViewMomentCmnt;
//
//    @BindView(R.id.moment_cmnt_num)
//    protected TextView mTextViewMomentCmntSum;
//
//
//    @BindView(R.id.moment_like)
//    protected ImageView mImageViewMomentLike;
//
//
//    @BindView(R.id.moment_like_num)
//    protected TextView mTextViewMomentLikeSum;

    @BindView(R.id.moment_time)
    protected TextView mTextViewMomentTime;

    @BindView(R.id.moment_delete)
    protected TextView mTextViewMomentDel;

    private Context mContext;
    private PresenterPersonal mPresenterPersonal;
    private List<BeanComment> mBeanCommentList;
    private List<BeanLike> mBeanLikes;
    private BeanUser mBeanUser;
    private int mCommentId;
    private BeanMoment mBeanMoment;

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

    public HolderMoment(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mPresenterPersonal = new PresenterPersonalImpl(this);
    }

    @Override
    public void bind(BeanMoment beanMoment) {
        mBeanMoment = beanMoment;
        mCommentId = beanMoment.getMomentCommentId();


        //这个地方有问题，加载评论的时候
        //mPresenterPersonal.loadResultCommentByCommentId(mCommentId);
        mPresenterPersonal.loadResultCommentByUserId(mCommentId);
        mPresenterPersonal.loadResultLikesByCommentId(mCommentId);
        mPresenterPersonal.loadUserPersaonlInfoById(mBeanMoment.getMomentUserId());

        Glide.with(mContext).load(MyURL.ROOT+mBeanMoment.getMomentUserAvatar()).
                placeholder(R.drawable.my).into(mCircleImageView);
        mTextViewUsername.setText(mBeanMoment.getMomentUserName());
        mTextViewContentText.setText(mBeanMoment.getMomentContent());
        Glide.with(mContext).load(MyURL.ROOT+mBeanMoment.getMomentImage()).
                placeholder(R.drawable.head).into(mImageViewContentImage);
        String formatDate  = DateTimeHelper.timeLogic(DateTimeHelper
                        .timeMillis2FormatTime(mBeanMoment.getMomentDate(),
                                DateTimeHelper.DATE_FORMAT_TILL_SECOND),
                DateTimeHelper.DATE_FORMAT_TILL_SECOND);
        mTextViewMomentTime.setText(formatDate);

//        mTextViewMomentCmntSum.setText(mBeanCommentList.size());
//        mTextViewMomentLikeSum.setText(mBeanLikes.size());
//        mTextViewMomentCmntSum.setText("0");
//        mTextViewMomentLikeSum.setText("0");

        mTextViewMomentDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Common.display(mContext, "删除"+mBeanMoment.getMomentId());
                Activity activity = (Activity)mContext;
                while (activity.getParent() != null) {
                    activity = activity.getParent();
                }
                final Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);
                dialog.setContentView(R.layout.alert_dialog);
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);
                TextView content = (TextView) dialog.findViewById(R.id.dialog_content);
                content.setText("确定删除该动态?");

                final Button yesBtn = (Button) dialog.findViewById(R.id.yesBtn);

                final Button noBtn = (Button) dialog.findViewById(R.id.noBtn);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int code = deleteMoment(mBeanMoment.getMomentId());
                        if (code == 200){
                            FragPersonalDownViewpagerMoment.mBeanMoments.
                                    remove(AdapterFragPersonalDownViewpagerMoment.thisPosition);
                            FragPersonalDownViewpagerMoment.mAdapterFragPersonalDownViewpagerMoment.
                                    notifyItemRemoved(AdapterFragPersonalDownViewpagerMoment.thisPosition);
                            FragPersonalDownViewpagerMoment.mAdapterFragPersonalDownViewpagerMoment.
                                    notifyItemRangeChanged(0,FragPersonalDownViewpagerMoment.mBeanMoments.size()-
                                            AdapterFragPersonalDownViewpagerMoment.thisPosition);


                            dialog.dismiss();
                        }else {
                            Common.display(mContext,"删除失败 CODE："+code);
                            dialog.dismiss();
                        }


                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
            }
        });



        ////////////////第三方评论组件

        //评论详情上面的
        topicId =  mBeanMoment.getMomentId()+"";
        topicTitle = mBeanMoment.getMomentUserName()+"的动态";
        topicPublishTime = formatDate;
        topicAuthor = mBeanMoment.getMomentUserName();

        Socialization service = ShareSDK.getService(Socialization.class);
        service.setCustomPlatform(new MyPlatform(mContext));
        initOnekeyShare();
        initQuickCommentBar();

        //设置评论监听
        Socialization.setCommentListener(new CommentListener() {

            @Override
            public void onSuccess(Comment comment) {
                int resId = ResHelper.getStringRes(mContext, "ssdk_socialization_reply_succeeded");
                if (resId > 0) {
//                    if(mBeanMoment.getMomentUserId()!= com.wao.dogcat.utils.Common.userID)
//                        //评论成功
//                    if (mBeanUser!=null)
//                        insertMsg(mBeanMoment.getMomentUserId()+"","2", mBeanUser.getUserName()+"评论了你的动态");
                }
            }

            @Override
            public void onFail(Comment comment) {
                //Toast.makeText(context, comment.getFileCodeString(context), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable throwable) {
                if (throwable instanceof ReplyTooFrequentlyException) {
                    int resId = ResHelper.getStringRes(mContext, "ssdk_socialization_replay_too_frequently");
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
//                if(mBeanMoment.getMomentUserId()!= com.wao.dogcat.utils.Common.userID)
//                    //点赞成功
//                    if (mBeanUser!=null)
//                    insertMsg(mBeanMoment.getMomentUserId()+"","1", com.wao.dogcat.utils.Common.user.getUserName()+"赞了你的动态");
            }

            @Override
            public void onFail(String topicId, String topicTitle, String commentId, String error) {
            }

        });


    }


//    /**
//     * 点击查看详情或评论
//     */
//    @OnClick(R.id.moment_cmnt)
//    public void onClickCmnt() {
//        AtyDetail.startAtyDetail(mContext, AtyDetail.class, mBeanUser, mBeanCommentList, mBeanLikes);
//    }
//
//
//    /**
//     * 点赞
//     */
//    @OnClick(R.id.moment_like)
//    public void onClickLike() {
//        mPresenterPersonal.sendUpdateMomentLikeByMomentIdAndLikeNum(mBeanMoment.getMomentId(), mBeanLikes.size() + 1);
//    }


    @Override
    public void getResultComment(List<BeanComment> list) {
        mBeanCommentList = list;
    }

    @Override
    public void getResultLike(List<BeanLike> like) {
        mBeanLikes = like;
    }

    @Override
    public void getPersonalInfo(BeanUser beanUser) {
        mBeanUser = beanUser;
    }

    //Socialization服务依赖OnekeyShare组件，此方法初始化一个OnekeyShare对象
    private void initOnekeyShare() {
        oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("yup!");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://dwz.cn/OWO_yyb");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我们都在玩yup!，快来一起玩吧~");
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

    public int deleteMoment(int momentID ){
        int code = 0;
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", momentID + "");
            code = HttpHelper.getCode(HttpHelper.postData(MyURL.DELETE_MOMENT_BY_ID, hashMap, null));
        }catch (Exception e){
            e.printStackTrace();
        }

        return code;
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
