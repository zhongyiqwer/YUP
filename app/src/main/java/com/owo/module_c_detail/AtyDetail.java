package com.owo.module_c_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.owo.base.AtyBase;
import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_personal.bean.BeanComment;
import com.owo.module_b_personal.bean.BeanLike;
import com.owo.module_b_personal.bean.BeanMoment;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_c_detail.detail_activity.FragDetailActivity;
import com.owo.module_c_detail.detail_activity.FragDetailOnlineActivity;
import com.owo.module_c_detail.detail_activity.pick_member.FragPick;
import com.owo.module_c_detail.detail_activity_comment.add_comment.FragAddActCmt;
import com.owo.module_c_detail.detail_activity_comment.view_comment.FragViewActCmt;
import com.owo.module_c_detail.detail_comment.FragDetailComment;
import com.owo.module_c_detail.detail_otheruser.FragDetailOtherUser;

import java.io.Serializable;
import java.util.List;

/**
 * @author XQF
 * @created 2017/5/23
 */
public class AtyDetail extends AtyBase {

    public static final String KEY_TYPE = "type";
    public static final String KEY_BEANUSER = "beanuser";
    public static final String KEY_COMMENTS = "comments";
    public static final String KEY_MOMENT = "moment";
    public static final String KEY_LIKES = "likes";
    public static final String VALUE_TYPE_COMMENTDETAIL = "commentdetail";
    public static final String KEY_BEANTASK = "beantask";
    public static final String VALUE_TYPE_ATYDETAIL = "activitydetail";
    public static final String VALUE_TYPE_VIEWATYCMT = "viewatycmt";
    public static final String VALUE_TYPE_ADDATYCMT = "addatycmt";
    public static final String VALUE_TYPE_ONLINEATYDETAIL = "onlineactivitydetail";
    public static final String VALUE_TYPE_OTHERUSERDETAIL = "otheruserdetail";
    public static final String VALUE_TYPE_PICK = "pick";


    /**
     * 在评论点击的时候启动【评论动态】
     * @param context
     * @param cls
     * @param beanuser
     * @param comments
     * @param likes
     */
    public static void startAtyDetail(Context context, Class<?> cls, BeanUser beanuser, List<BeanComment> comments, List<BeanLike> likes
                                      ,BeanMoment beanMoment ) {
        Bundle args = new Bundle();
        args.putString(KEY_TYPE, VALUE_TYPE_COMMENTDETAIL);
        args.putSerializable(KEY_BEANUSER, beanuser);
        args.putSerializable(KEY_COMMENTS, (Serializable) comments);
        args.putSerializable(KEY_LIKES, (Serializable) likes);
        args.putSerializable(KEY_MOMENT, (Serializable) beanMoment);
        Intent intent = new Intent(context, cls);
        intent.putExtras(args);
        context.startActivity(intent);
    }


    /**
     * 活动细节【区分线上/线下/线下(已完成)】
     *
     * @param context
     * @param cls
     * @param beanTask
     */
    public static void startAtyDetail(Context context, Class<?> cls, BeanTask beanTask,String type) {
        Bundle args = new Bundle();
        if (type.equals(VALUE_TYPE_ATYDETAIL))
        args.putString(KEY_TYPE, VALUE_TYPE_ATYDETAIL); //线下
        else if (type.equals(VALUE_TYPE_ONLINEATYDETAIL))
            args.putString(KEY_TYPE, VALUE_TYPE_ONLINEATYDETAIL); //线上
        else if (type.equals(VALUE_TYPE_VIEWATYCMT))
            args.putString(KEY_TYPE,VALUE_TYPE_VIEWATYCMT);
        else if (type.equals(VALUE_TYPE_ADDATYCMT))
            args.putString(KEY_TYPE,VALUE_TYPE_ADDATYCMT);
        args.putSerializable(KEY_BEANTASK, beanTask);
        Intent intent = new Intent(context, cls);
        intent.putExtras(args);
        context.startActivity(intent);
    }

    public static void startPickDetail(Context context, Class<?> cls,String taskName, int taskID,int maxNum,int takenNum) {
        Bundle args = new Bundle();
        args.putString(KEY_TYPE, VALUE_TYPE_PICK);
        args.putString("tname",taskName);
        args.putInt("tid", taskID);
        args.putInt("max", maxNum);
        args.putInt("taken",takenNum);
        Intent intent = new Intent(context, cls);
        intent.putExtras(args);
        context.startActivity(intent);
    }


    /**
     * 查看其他人的个人主页
     *
     * @param context
     * @param cls
     * @param beanuser
     */
    public static void startAtyDetail(Context context, Class<?> cls, BeanUser beanuser) {
        Bundle args = new Bundle();
        args.putString(KEY_TYPE, VALUE_TYPE_OTHERUSERDETAIL);
        args.putSerializable(KEY_BEANUSER, beanuser);
        Intent intent = new Intent(context, cls);
        intent.putExtras(args);
        context.startActivity(intent);
    }


    @Override
    public Fragment createFragment() {
        Intent intent = getIntent();
        String string = intent.getStringExtra(KEY_TYPE);
        Fragment fragment = null;
        if (string.equals(VALUE_TYPE_COMMENTDETAIL)) {
            BeanUser beanuser = (BeanUser) intent.getSerializableExtra(KEY_BEANUSER);
            List<BeanComment> comments = (List<BeanComment>) intent.getSerializableExtra(KEY_COMMENTS);
            List<BeanLike> likes = (List<BeanLike>) intent.getSerializableExtra(KEY_LIKES);
            BeanMoment beanMoment = (BeanMoment) intent.getSerializableExtra(KEY_MOMENT);
            fragment = FragDetailComment.newInstance(beanuser, comments, likes,beanMoment);
        } else if (string.equals(VALUE_TYPE_ATYDETAIL)) {
            BeanTask beantask = (BeanTask) intent.getSerializableExtra(KEY_BEANTASK);
            fragment = FragDetailActivity.newInstance(beantask);
        } else if (string.equals(VALUE_TYPE_OTHERUSERDETAIL)) {
            BeanUser beanuser = (BeanUser) intent.getSerializableExtra(KEY_BEANUSER);
            fragment = FragDetailOtherUser.newInstance(beanuser);
        }else if (string.equals(VALUE_TYPE_ONLINEATYDETAIL)) {
            BeanTask beantask = (BeanTask) intent.getSerializableExtra(KEY_BEANTASK);
            fragment = FragDetailOnlineActivity.newInstance(beantask);
        } else if (string.equals(VALUE_TYPE_VIEWATYCMT)){
            BeanTask beanTask = (BeanTask) intent.getSerializableExtra(KEY_BEANTASK);
            fragment = FragViewActCmt.newInstance(beanTask);
        } else if (string.equals(VALUE_TYPE_ADDATYCMT)){
            BeanTask beanTask = (BeanTask) intent.getSerializableExtra(KEY_BEANTASK);
            fragment = FragAddActCmt.newInstance(beanTask);
        }else if (string.equals(VALUE_TYPE_PICK)){
            String taskName = intent.getStringExtra("tname");
            int taskID = intent.getIntExtra("tid",0);
            int maxNum = intent.getIntExtra("max",0);
            int takenNUm = intent.getIntExtra("taken",0);
            fragment = FragPick.newInstance(taskName,taskID,maxNum,takenNUm);
        }
        return fragment;
    }
}
