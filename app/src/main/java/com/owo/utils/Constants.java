package com.owo.utils;

import android.content.SharedPreferences;

import com.owo.module_b_personal.bean.BeanUser;

import java.util.HashMap;

/**
 * @author XQF
 * @created 2017/5/15
 */
public class Constants {

    public static int FRAG_HOME_ITEM_VIEWTYPE_BLUE = 0;
    public static int FRAG_HOME_ITEM_VIEWTYPE_RED = 1;
    public static int FRAG_HOME_ITEM_VIEWTYPE_YELLOW = 2;

    public static int FRAG_PERSONAL_ITEM_VIEWTYPE_CAMEAR = 0;
    public static int FRAG_PERSONAL_ITEM_VIEWTYPE_NOTE = 1;


    //bean的类型
    public static int BEAN_TYPE_TASK = 0;
    public static int BEAN_TYPE_LABEL = 1;
    public static int BEAN_TYPE_COMMENT = 2;
    public static int BEAN_TYPE_LIKE = 3;


    //任务状态
    //报名中
    public static int TASK_STATUS_ENROOLING = 1;
    public static int TASK_STATUS_WORKING = 2;
    public static int TASK_STATUS_FINISHED = 3;

    //
    public static int status = 0;
    public static int FOLLOWED = 1;
    public static int FOLLOW = 2;



    public static BeanUser getBeanUserFromMap(HashMap<String, Object> mapResult) {
        BeanUser beanUser = new BeanUser();

        beanUser.setId((Integer) mapResult.get("id"));
        beanUser.setUserName((String) mapResult.get("username"));
        beanUser.setPassWord((String) mapResult.get("password"));
        beanUser.setAvatar((String) mapResult.get("avatar"));
        beanUser.setWexID((String) mapResult.get("wexid"));


        beanUser.setQq((String) mapResult.get("qq"));
        beanUser.setPhoneNumber((String) mapResult.get("phonenumber"));
        beanUser.setLevel((Integer) mapResult.get("level"));
        beanUser.setSignature((String) mapResult.get("signature"));
        beanUser.setInviteCode((String) mapResult.get("invitecode"));


        beanUser.setSex((String) mapResult.get("sex"));
        beanUser.setStatus((Integer) mapResult.get("status"));
        beanUser.setLongtitude((String) mapResult.get("longtitude"));
        beanUser.setLatitude((String) mapResult.get("latitude"));
        beanUser.setStepsToday((Integer) mapResult.get("stepstoday"));


        beanUser.setCheckinDays((Integer) mapResult.get("checkindays"));
        beanUser.setAge((Integer) mapResult.get("age"));
        beanUser.setHeight((Integer) mapResult.get("height"));
        beanUser.setWeight((Integer) mapResult.get("weight"));
        beanUser.setHobby((String) mapResult.get("hobby"));


        beanUser.setExp((Integer) mapResult.get("exp"));
        beanUser.setBackImage((String) mapResult.get("backimage"));
        beanUser.setMoney((Integer) mapResult.get("money"));
        beanUser.setRegDate((String) mapResult.get("regdate"));
        return beanUser;
    }

}
