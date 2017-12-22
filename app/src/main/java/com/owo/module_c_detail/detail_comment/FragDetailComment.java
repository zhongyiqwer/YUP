package com.owo.module_c_detail.detail_comment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mob.tools.utils.ResHelper;
import com.owo.module_b_personal.bean.BeanMoment;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.owo.base.FragBase;
import com.owo.module_b_personal.bean.BeanComment;
import com.owo.module_b_personal.bean.BeanLike;
import com.owo.module_b_personal.bean.BeanUser;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

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
public class FragDetailComment extends FragBase {

    public static FragDetailComment newInstance(BeanUser beanUser, List<BeanComment> comments, List<BeanLike> likes,BeanMoment beanMoment) {
        FragDetailComment fragDetailComment = new FragDetailComment();
        Bundle args = new Bundle();
        args.putSerializable("beanuser", beanUser);
        args.putSerializable("comments", (Serializable) comments);
        args.putSerializable("likes", (Serializable) likes);
        fragDetailComment.setArguments(args);
        return fragDetailComment;
    }


    private BeanUser mBeanUser;
    private List<BeanComment> mBeanComments;
    private List<BeanLike> mBeanLikes;

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
    private BeanMoment mBeanMoment;
    @OnClick(R.id.btnBack)
    public void back(){
        getActivity().finish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail_comment, container, false);
        ButterKnife.bind(this, view);
        mBeanUser = (BeanUser) getArguments().get("beanuser");
        mBeanComments = (List<BeanComment>) getArguments().get("comments");
        mBeanLikes = (List<BeanLike>) getArguments().get("likes");

        ////////////////第三方评论组件
        //评论详情上面的
        topicId =  mBeanMoment.getMomentId()+"";
        topicTitle = mBeanMoment.getMomentUserName()+"的动态";
        //topicPublishTime = formatDate;
        topicAuthor = mBeanMoment.getMomentUserName();

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
                    if(mBeanMoment.getMomentUserId()!= Common.userID)
                        //评论成功
                        if (mBeanUser!=null)
                            insertMsg(mBeanMoment.getMomentUserId()+"","2", mBeanUser.getUserName()+"评论了你的动态");
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
                if(mBeanMoment.getMomentUserId()!= Common.userID)
                    //点赞成功
                    if (mBeanUser!=null)
                        insertMsg(mBeanMoment.getMomentUserId()+"","1", Common.user.getUserName()+"赞了你的动态");
            }

            @Override
            public void onFail(String topicId, String topicTitle, String commentId, String error) {
            }

        });


        return view;
    }

    //Socialization服务依赖OnekeyShare组件，此方法初始化一个OnekeyShare对象
    private void initOnekeyShare() {
        oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("OWO -- You Will Never Walk Alone");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://dwz.cn/OWO_yyb");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我们在玩OWO，快来邀请TA和你一起玩吧~");
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
