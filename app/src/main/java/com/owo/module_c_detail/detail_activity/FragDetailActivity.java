package com.owo.module_c_detail.detail_activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.presenter.PresenterPersonalImpl;
import com.owo.module_c_detail.AtyDetail;
import com.owo.module_c_detail.detail_activity_comment.view_comment.AdapterRecyclerView;
import com.owo.utils.BitmapUtil;
import com.owo.utils.Common;
import com.owo.utils.Constants;
import com.owo.utils.DateTimeHelper;
import com.owo.utils.UtilLog;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.owo.base.FragBase;
import com.owo.module_b_home.bean.BeanTask;
import com.wao.dogcat.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;

/**
 * @author XQF
 * @created 2017/5/23
 */
public class FragDetailActivity extends FragBase implements ViewFragDetail{


    public static FragDetailActivity newInstance(BeanTask beanTask) {
        FragDetailActivity fragDetailActivity = new FragDetailActivity();
        mBeanTask = beanTask;
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

    @BindView(R.id.textV_item_activity_taken_users)
    protected TextView userNum;

    @BindView(R.id.textV_item_activity_deadline)
    protected TextView deadline;

    @BindView(R.id.textV_item_activity_where)
    protected TextView where;

    @BindView(R.id.textV_item_activity_status)
    protected TextView userNum2;

    @BindView(R.id.textV_item_activity_money)
    protected TextView money;

    @BindView(R.id.btn_offline_act)
    protected Button btn;

    private DisplayImageOptions options1, options2;
    private String path1, path2;

    private int userID;
    private List<BeanUser> userLists;
    private PresenterDetail presenterDetail;

    private int btnStatus;
    private boolean isPressed = false;
    private int state;

    @OnClick(R.id.btnBack)
    public void back(){
        getActivity().finish();
    }

    @BindView(R.id.jike_time_card)
    CardView time_card;


    @BindView(R.id.jike_time_text)
    TextView time_text;


    @BindView(R.id.clock_img)
    ImageView clock_img;

    private TimeCount timeCount;
    private long restTime;


    @BindView(R.id.recyclerview_taken_users)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.noData)
    TextView noData;

    /**
     * 活动详情界面的分享按钮在这里
     * */
    @OnClick(R.id.btnShare)
    public void btnShareClick(){
        ShareSDK.initSDK(getContext());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        String taskImageURL = MyURL.ROOT + mBeanTask.getTaskIamge();
        String time = "截止时间：" + DateTimeHelper.timeMillis2FormatTime(
                        mBeanTask.getTaskDeadLine(), "yyyy.MM.dd");
        String where = "地址:"+getWhere(mBeanTask.getTaskLatitude(), mBeanTask.getTaskLongitude());
        System.out.println(time +""+ where);
        System.out.println(taskImageURL);
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("yup! "+mBeanTask.getTaskName());
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://yup.sxl.cn/");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("yup!活动分享,"+mBeanTask.getTaskContent()+" "+time+" "+where);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(taskImageURL);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://yup.sxl.cn/");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("yup！是一款很好用的软件");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://yup.sxl.cn/");
        // 启动分享GUI
        oks.show(getContext());

    }

    @OnClick(R.id.imageV_item_activity_avatar)
    public void viewPublishUser(){
        try {

            HashMap<String,String> hm = new HashMap<>();
            hm.put("id",mBeanTask.getTaskUserID()+"");
            String str = HttpHelper.postData(MyURL.GET_USER_BY_ID,hm,null);
            HashMap<String, Object> map = HttpHelper.AnalysisUserInfo(str);
            BeanUser beanUser = Constants.getBeanUserFromMap(map);
            if (beanUser!=null && beanUser.getId()!=Common.userID)
            AtyDetail.startAtyDetail(getContext(), AtyDetail.class, beanUser);
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail_activity, container, false);
        ButterKnife.bind(this, view);


        return view;
    }




    @Override
    public void onResume() {
        super.onResume();

        presenterDetail = new PresenterDetailImpl(this);
        presenterDetail.getApplyUsersByTID(mBeanTask.getTaskId());

        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            time_text.setText("已过期");
            time_text.setTextColor(getContext().getResources().getColor(R.color.textGray));
            timeCount.cancel();
            // SmsCompleteManager.Instance.removeListener(RegActivity.this);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            time_text.setText(DateTimeHelper.countdownMillis2FormatTime(millisUntilFinished));
        }
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


    @OnClick(R.id.btn_offline_act)
    public void act() {

        switch (btnStatus) {
            case 1:
                if (!isPressed) {

                    Activity activity = getActivity();
                    while (activity.getParent() != null) {
                        activity = activity.getParent();
                    }
                    final Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);
                    dialog.setContentView(R.layout.alert_dialog_enroll);
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(true);
                    TextView content = (TextView) dialog.findViewById(R.id.dialog_content);
                    content.setText("确定报名该活动?");

                    final Button yesBtn = (Button) dialog.findViewById(R.id.yesBtn);
                    TextView money = (TextView) dialog.findViewById(R.id.dialog_money);
                    money.setText(mBeanTask.getTaskMoney() + "");

                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isPressed = true;

                            if (mBeanTask.getTaskMoney()<=Common.user.getMoney()) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("taskID", mBeanTask.getTaskId() + "");
                                map.put("userID", userID + "");
                                int code = 0;
                                try {
                                    code = HttpHelper.getCode(HttpHelper.postData(MyURL.INSERT_APPLY, map, null));
                                    if (code == 200) {
                                        Common.display(getContext(), "报名成功，请耐心等待");
                                        dialog.dismiss();
                                    } else {
                                        Common.display(getContext(), "报名失败：CODE " + code);
                                        dialog.dismiss();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Common.display(getContext(), "报名失败：EXCEPTION ");
                                    dialog.dismiss();
                                }
                            }else{
                                Common.display(getContext(), "报名失败：没有足够金币！");
                                dialog.dismiss();
                            }

                        }
                    });

                } else Common.display(getContext(), "你已报名，请耐心等待");
                break;
            case 4:
                AtyDetail.startAtyDetail(getContext(), AtyDetail.class, mBeanTask, AtyDetail.VALUE_TYPE_ADDATYCMT);
                //getActivity().finish();
                break;
            case 8:
                //跳转活动评价页面
                AtyDetail.startAtyDetail(getContext(), AtyDetail.class, mBeanTask, AtyDetail.VALUE_TYPE_VIEWATYCMT);
                //getActivity().finish();
                break;
            case 6:
                //跳转筛选页面
                AtyDetail.startPickDetail(getContext(), AtyDetail.class,mBeanTask.getTaskName(), mBeanTask.getTaskId(),
                        mBeanTask.getTaskMaxNum(),mBeanTask.getTaskTakenNum());
                //getActivity().finish();
                break;
            case 7:
                //结束活动
                Activity activity = getActivity();
                while (activity.getParent() != null) {
                    activity = activity.getParent();
                }
                final Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);
                dialog.setContentView(R.layout.alert_dialog);
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);
                TextView content = (TextView) dialog.findViewById(R.id.dialog_content);
                content.setText("确定结束活动吗?");

                final Button yesBtn = (Button) dialog.findViewById(R.id.yesBtn);
                final Button noBtn = (Button) dialog.findViewById(R.id.noBtn);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("taskID", mBeanTask.getTaskId() + "");
                        map.put("taskStatus", "3");
                        int code = 0;
                        try {
                            code = HttpHelper.getCode(HttpHelper.postData(MyURL.UPDATE_TASK_STATUS_BY_ID, map, null));
                            if (code == 200) {
                                Common.display(getContext(), "活动已结束");
                                btn.setText("该活动已结束");
                                btn.setTextColor(getContext().getResources().getColor(R.color.textGray));
                                btnStatus = 8;
                                /////////////跳转至活动结束页面
                                AtyDetail.startAtyDetail(getContext(), AtyDetail.class, mBeanTask, AtyDetail.VALUE_TYPE_VIEWATYCMT);
                                getActivity().finish();

                            }else if (code==201){
                                Common.display(getContext(),"该活动参加人数已满，到期后方可自动结束");
                            }
                            else Common.display(getContext(), "操作失败：CODE " + code);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Common.display(getContext(), "操作失败：EXCEPTION ");
                        }

                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                break;
        }
    }

    @Override
    public void getApplyUsers(List<BeanUser> userLists, int code) {
//        this.userLists = userLists;
//        state = code;
//
//        new Thread() {
//            @Override
//            public void run() {
//                Message msg = handler.obtainMessage();
//                msg.what = 1;
//                handler.sendMessage(msg);
//            }
//        }.start();

    }



    public ArrayList<HashMap<String, Object>> getTakenUserByTaskID(){
        ArrayList<HashMap<String, Object>> userList = new ArrayList<>();
        try {
            HashMap<String,String> map = new HashMap<>();
            map.put("taskID",mBeanTask.getTaskId() + "");
            String json = HttpHelper.postData(MyURL.GET_TAKEN_USERS_BY_TASKID,map,null);
            userList = HttpHelper.AJUsers2(json);
        }catch (Exception e){
            e.printStackTrace();
        }

        return userList;
    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Common.userSP = getContext().getSharedPreferences("userSP", 0);
                    userID = Common.userSP.getInt("ID", 0);
                    userLists = new ArrayList<>();


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
                    if (mBeanTask.getTaskIamge().trim().length() != 0) {
                        path2 = MyURL.ROOT + mBeanTask.getTaskIamge();
                        com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                                displayImage(path2, img, options2);
                    } else img.setVisibility(View.GONE);

                    //TODO 显示已参加用户列表
                    ArrayList<HashMap<String, Object>> userList = getTakenUserByTaskID();
                    if (userList.size()>0) {
                        loading.setVisibility(View.GONE);
                        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),8));
                        AdapterRecyclerViewDetail adapter = new AdapterRecyclerViewDetail(getActivity());
                        adapter.addItems(userList);
                        mRecyclerView.setAdapter(adapter);
                        userNum.setText("已参加用户：" + (userList.size()) + "/" + mBeanTask.getTaskMaxNum());
                    }else{
                        loading.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        noData.setText("暂时没有人参加此活动");
                    }


                    String s = userList.size() + "/" + mBeanTask.getTaskMaxNum();
                    if (userList.size() < mBeanTask.getTaskMaxNum())
                        s += "(尚未满人)";
                    userNum2.setText(s);

                    actContent.setText(mBeanTask.getTaskContent());

                    username.setText(mBeanTask.getUserName());
                    title.setText(mBeanTask.getTaskName());

                    String latitude = mBeanTask.getTaskLatitude();
                    String longitude = mBeanTask.getTaskLongitude();
                    where.setText(getWhere(latitude, longitude));
                    String formatDate = DateTimeHelper.timeLogic(DateTimeHelper
                                    .timeMillis2FormatTime(mBeanTask.getTaskDeadLine(),
                                            DateTimeHelper.DATE_FORMAT_TILL_SECOND),
                            DateTimeHelper.DATE_FORMAT_TILL_SECOND);
                    time.setText(formatDate);
                    money.setText("参与者支付：" + mBeanTask.getTaskMoney() + "金币");

                    if (mBeanTask.getTaskType()==3){
                        clock_img.setVisibility(View.GONE);
                        deadline.setVisibility(View.GONE);
                        time_card.setVisibility(View.VISIBLE);

                        restTime =  Long.parseLong(mBeanTask.getTaskDeadLine()) - System.currentTimeMillis();
                        int count = 0;
                        if (restTime > 0 && count == 0) {
                            count++;
                            timeCount = new TimeCount(restTime, 1000);// 构造CountDownTimer对象
                            timeCount.start();
                        }else {
                            //timeCount.cancel();
                            time_text.setText("已过期");
                            time_text.setTextColor(getContext().getResources().getColor(R.color.textGray));
                        }
                    }else {
                        deadline.setText("截止时间：" +
                                DateTimeHelper.timeMillis2FormatTime(
                                        mBeanTask.getTaskDeadLine(), "yyyy.MM.dd"));
                    }

                        int status = mBeanTask.getTaskStatus();
                        //活动参与者
                        if (mBeanTask.getTaskUserID() != userID) {
                            if (status == 1) {
                                int code = getApplyStatus();
                                if (code == 201) {
                                    btn.setText("已报名，等待通过...");
                                    btn.setTextColor(getContext().getResources().getColor(R.color.textGray));
                                    btnStatus = 2;
                                } else if (code == 200) {
                                    btn.setText("活动进行中...");
                                    btn.setTextColor(getContext().getResources().getColor(R.color.textGray));
                                    btnStatus = 3;
                                } else {
                                    //没人报名
                                    btn.setText("立即报名");
                                    btnStatus = 1;
                                }


                            } else if (status == 2) {
                                btn.setText("活动进行中...");
                                btn.setTextColor(getContext().getResources().getColor(R.color.textGray));
                                btnStatus = 3;

                            } else if (status == 3) {
                                btn.setText("活动已结束，去写评价吧");
                                btnStatus = 4;
                            }


                        } else {
                            state = getApplyUsers();
                            //活动发起者
                            if (status == 1) {
                                if (state == 200) {
                                    btn.setText("报名筛选");
                                    btnStatus = 6;
                                } else {
                                    btn.setText("等待参与者报名...");
                                    btn.setTextColor(getContext().getResources().getColor(R.color.textGray));
                                    btnStatus = 5;
                                }
                            } else if (status == 2) {
                                btn.setText("结束此活动");
                                btnStatus = 7;

                            } else if (status == 3) {
                                btn.setText("该活动已结束");
                                btn.setTextColor(getContext().getResources().getColor(R.color.textGray));
                                btnStatus = 8;
                            }

                        }
                  //  }


                    break;
            }
        }
    };


    public int getApplyStatus() {
        int code = 0;
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userID", userID + "");
            map.put("taskID", mBeanTask.getTaskId() + "");
            String json = HttpHelper.postData(MyURL.GET_APPLY_STATUS_BY_UID_TID, map, null);
            code = HttpHelper.getCode(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return code;

    }


    public int getApplyUsers() {
        int code = 0;
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("taskID", mBeanTask.getTaskId() + "");
            String json = HttpHelper.postData(MyURL.GET_APPLY_USERS_BY_TID, map, null);
            code = HttpHelper.getCode(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return code;

    }

    public ArrayList<HashMap<String, Object>> getTakenUsers() {
        int code = 0;
        ArrayList<HashMap<String, Object>> users = new ArrayList<>();
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("taskID", mBeanTask.getTaskId() + "");
            String json = HttpHelper.postData(MyURL.GET_TAKEN_USERS_BY_TASKID, map, null);
            users = HttpHelper.AJUsers(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;

    }


}
