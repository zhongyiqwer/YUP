package com.owo.module_b_main;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TimeUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.clusterutil.MarkerClusterDemo;
import com.baidu.mapapi.clusterutil.MyItem;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.nightonke.boommenu.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.owo.module_a_login.AtyLoginOrRegister;
import com.owo.module_a_selectlabel.widgets.AtySelectLabel;
import com.owo.module_b_home.widgets.FragHomeNormal;
import com.owo.utils.DateTimeHelper;
import com.owo.utils.UtilTime;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.owo.widget.DateTimePickDialogUtil;
import com.wao.dogcat.R;
import com.owo.module_b_addactivity.presenter.PresenterAddAty;
import com.owo.module_b_addactivity.presenter.PresenterAddAtyImpl;
import com.owo.module_b_addactivity.view.ViewAddAty;
import com.owo.module_b_explore.widgets.FragExplore;
import com.owo.module_b_home.widgets.FragHome;
import com.owo.module_b_message.widgets.FragMessage;
import com.owo.module_b_personal.widgets.FragPersonal;
import com.owo.utils.Common;
import com.owo.utils.UtilLog;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.jiguang.api.JCoreInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import io.jchat.android.chatting.utils.HandleResponseCode;
import io.jchat.android.chatting.utils.SharePreferenceManager;
import io.jchat.android.chatting.utils.TimeFormat;
import zhouyou.flexbox.adapter.TagAdapter;
import zhouyou.flexbox.interfaces.OnFlexboxSubscribeListener;
import zhouyou.flexbox.widget.BaseTagView;
import zhouyou.flexbox.widget.TagFlowLayout;


public class AtyMain extends AppCompatActivity implements ViewAddAty {

    private static final int REQUEST_CODE_GALLERY = 111;
    public static final int FRAGMENT_N = 4;


    //底部按钮的正常背景资源ID数组
    public static final int[] tabsNormalBackIds = new int[]{
            R.drawable.home_gray,
            R.drawable.message_gray,
            R.drawable.find_gray,
            R.drawable.person_gray};
    //底部按钮的点击后的背景资源ID数组
    public static final int[] tabsActiveBackIds = new int[]{
            R.drawable.home_yellow,
            R.drawable.message_yellow,
            R.drawable.find_yellow,
            R.drawable.person_yellow};


    //fragment的TAG标识，，。表示之前没有这么用过
    private static final String FRAGMENT_TAG_HOME = "home";
    private static final String FRAGMENT_TAG_MESSAGE = "message";
    private static final String FRAGMENT_TAG_DISCOVER = "discover";
    private static final String FRAGMENT_TAG_PRESONAL = "personal";


    //放进一个数组中方便随机访问
    private static final String[] fragmentTags = new String[]{
            FRAGMENT_TAG_HOME,
            FRAGMENT_TAG_MESSAGE,
            FRAGMENT_TAG_DISCOVER,
            FRAGMENT_TAG_PRESONAL
    };


//    @BindView(R.id.btn_chat)
//    protected RelativeLayout mBtnChat;
//
//    @BindView(R.id.btn_contact)
//    protected RelativeLayout mBtnMessage;
//
//    @BindView(R.id.btn_discover)
//    protected RelativeLayout mBtnExplore;
//
//    @BindView(R.id.btn_personal_space)
//    protected RelativeLayout mBtnPersonalSapce;

    @BindView(R.id.home_img)
    protected Button homeImg;

    @BindView(R.id.message_img)
    protected Button messageImg;

    @BindView(R.id.discover_img)
    protected Button discoverImg;

    @BindView(R.id.person_img)
    protected Button personImg;

    @BindView(R.id.btn_add_task)
    protected ImageView mBtnAddTask;


    @BindView(R.id.iv_recent_tips)
    protected ImageView mRecentTips;


    protected Toolbar mToolbar;


    protected Button[] tabs;
    private FragHome mFragHome;
    private FragMessage mFragMessage;
    private FragExplore mFragExplore;

    private FragPersonal mFragPersonal;

    private PresenterAddAty mPresenterAddAty;
    private String picPath = "";
    private Bitmap bitmap;

    private int mUserId;
    private boolean mIsOpenDialog = false;

    private ImageView imageButton;
    private String currentDateTime;

    private int label = 0;

    private long time = 0;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public static void start(Context context, Class<?> cls, boolean isOpenDialog) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("flag", isOpenDialog);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ButterKnife.bind(this);
        mIsOpenDialog = getIntent().getBooleanExtra("flag", false);
        mPresenterAddAty = new PresenterAddAtyImpl(this);
        tabs = new Button[]{homeImg, messageImg, discoverImg, personImg};
        Common.userSP = getSharedPreferences("userSP", 0);
        sp = getSharedPreferences("certi",0);
        mUserId = Common.userSP.getInt("ID", 0);

        setSupportActionBar(mToolbar);
        homeImg.performClick();

        if (JMessageClient.getAllUnReadMsgCount() > 0) {
            mRecentTips.setVisibility(View.VISIBLE);
        } else {
            if (mRecentTips.getVisibility() == View.VISIBLE)
                mRecentTips.setVisibility(View.GONE);
        }

    }

    /**
     * 初始化隐藏所有的fragment
     */
    private void hideFragments(FragmentManager mFragmentManager, FragmentTransaction mFragmentTransaction) {
        for (int i = 0; i < fragmentTags.length; i++) {
            Fragment fragment = mFragmentManager.findFragmentByTag(fragmentTags[i]);
            if (fragment != null && fragment.isVisible()) {
                mFragmentTransaction.hide(fragment);
            }
        }

    }

    /**
     * 将底部按钮的背景都设为正常
     */
    private void setBottomBtnNormalBgs() {
        for (int i = 0; i < tabs.length; i++) {
            Button btn = tabs[i];
            setDrawable(btn, tabsNormalBackIds[i]);
        }
    }

    /**
     * 更新该btn的背景值为
     *
     * @param btn   btn控件
     * @param resId 资源,根据不同的资源代表不同的状态
     */
    private void setDrawable(Button btn, int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth()
                , drawable.getMinimumHeight());
        btn.setCompoundDrawables(null, drawable, null, null);
    }


    /**
     * 底部按钮的按钮事件
     *
     * @param v
     */
    public void onTabSelect(View v) {

        //每次都获取一个manager和transaction，否则会炸
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        int id = v.getId();
        hideFragments(mFragmentManager, mFragmentTransaction);
        //所有按钮正常
        //为什么不把所有的fragment一股脑添加进去？然后根据用户点击来显示。那样做的代码量应该更少
        setBottomBtnNormalBgs();
        if (id == R.id.home_img) {
            mFragMessage = null;
            if (mFragHome == null) {
                mFragHome = new FragHome();
                mFragmentTransaction.add(R.id.fragment_container, mFragHome, FRAGMENT_TAG_HOME);
            }
            mFragmentTransaction.show(mFragHome);
        } else if (id == R.id.message_img) {
            mFragMessage = null;
            if (mFragMessage == null) {
                mFragMessage = new FragMessage();
                mFragmentTransaction.add(R.id.fragment_container, mFragMessage, FRAGMENT_TAG_MESSAGE);
            }
            mFragmentTransaction.show(mFragMessage);
        } else if (id == R.id.discover_img) {
            mFragMessage = null;
            if (mFragExplore == null) {
                mFragExplore = new FragExplore();
                mFragmentTransaction.add(R.id.fragment_container, mFragExplore, FRAGMENT_TAG_DISCOVER);
            }
            mFragmentTransaction.show(mFragExplore);
        } else if (id == R.id.person_img) {
            mFragMessage = null;
            if (mFragPersonal == null) {
                mFragPersonal = new FragPersonal();
                mFragmentTransaction.add(R.id.fragment_container, mFragPersonal, FRAGMENT_TAG_PRESONAL);
            }
            mFragmentTransaction.show(mFragPersonal);
        }

        //通过遍历来确定点击的button在button实例数组中的索引pos
        int pos;
        for (pos = 0; pos < FRAGMENT_N; pos++) {
            if (tabs[pos] == v) {
                break;
            }
        }
        mFragmentTransaction.commit();
        //根据pos来更新按钮的背景状态
        setDrawable(tabs[pos], tabsActiveBackIds[pos]);
    }


    @OnClick(R.id.btn_add_task)
    public void onBtnAddTaskClick() {
        UtilLog.d("test", "从本地取出的userid " + mUserId);
        final SimpleTooltip tooltip = new SimpleTooltip.Builder(this)
                .anchorView(mBtnAddTask)
                //.text("发布活动")

                .gravity(Gravity.TOP)
                .dismissOnOutsideTouch(false)
                .dismissOnInsideTouch(false)
                .transparentOverlay(false)
                .modal(true)
                .animated(true)
                .showArrow(false)
                .backgroundColor(Color.WHITE)
                .contentView(R.layout.aty_main_tooltip_addtask, R.id.tv_text)
                .focusable(true)
                .build();

        ImageView online = tooltip.findViewById(R.id.imageV_aty_main_tooltip_online);
        ImageView offline = tooltip.findViewById(R.id.imageV_aty_main_tooltip_offline);
        ImageView jike = tooltip.findViewById(R.id.imageV_aty_main_tooltip_jike);
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = AtyMain.this;
                while (activity.getParent() != null) {
                    activity = activity.getParent();
                }
                final Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);
                dialog.setContentView(R.layout.aty_main_tooltip_addtask_online);
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);

                //               WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                //               lp.copyFrom(dialog.getWindow().getAttributes());
//                lp.width = (int) SimpleTooltipUtils.pxFromDp(400);
//                lp.height = (int) SimpleTooltipUtils.pxFromDp(400);
//                dialog.getWindow().setAttributes(lp);
                final EditText textViewName = (EditText) dialog.findViewById(R.id.activity_online_name);
                final EditText textViewDescription = (EditText) dialog.findViewById(R.id.activity_online_description);
                final TextView textViewLastTime = (TextView) dialog.findViewById(R.id.activity_online_lasttime);
                textViewLastTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentDateTime = DateTimeHelper.getCurrentDateTime("yyyy年MM月dd日 HH:mm");
                        DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(AtyMain.this, currentDateTime);
                        dateTimePicKDialog.dateTimePicKDialog(textViewLastTime);

                    }
                });
                imageButton = (ImageView) dialog.findViewById(R.id.activity_online_image);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FunctionConfig functionConfig = new FunctionConfig.Builder()
                                .setEnableCrop(true)
                                .setEnableRotate(true)
                                .setCropSquare(false)
                                .setEnablePreview(true)
                                .setEnableEdit(true)//编辑功能
                                .setEnableCrop(true)//裁剪功能
                                .setEnableCamera(true)//相机功能
                                .setForceCropEdit(true)
                                .setCropHeight(520)
                                .setCropWidth(720)
                                .setForceCrop(true)
                                .build();
                        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);

                    }
                });

                final Button btnPublish = (Button) dialog.findViewById(R.id.activity_online_publish);
                btnPublish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isOK = false;
                        if (textViewDescription.getText().toString().trim().length() == 0) {
                            Common.display(AtyMain.this, "活动描述不能为空");
                        } else if (textViewLastTime.getText().toString().trim().length() == 0) {
                            Common.display(AtyMain.this, "截止日期不能为空");
                        } else if (textViewName.getText().toString().trim().length() == 0) {
                            Common.display(AtyMain.this, "活动名称不能为空");
                        } else isOK = true;
                        if (isOK) {


                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("taskUserID", mUserId + "");
                            map.put("taskType", 1 + "");//线上任务
                            map.put("taskLabel", "线上");
                            map.put("taskDeadline",
                                    DateTimeHelper.timeMillis2FormatTime( DateTimeHelper.FormatTime2timeMillis(textViewLastTime.getText().toString().trim(),
                                            "yyyy年MM月dd日 HH:mm") + "","yyyy-MM-dd HH:mm:ss"));
                            map.put("taskContent", textViewDescription.getText().toString().trim());
                            map.put("taskMoney", "0");
                            map.put("taskMaxNum", "9999");
                            map.put("taskLatitude", Common.myLatitude + "");
                            map.put("taskLongitude", Common.myLongtitude + "");
                            //map.put("taskPublishTime", System.currentTimeMillis() + "");
                            map.put("taskName", textViewName.getText().toString().trim());

                            HashMap<String, String> mapFile = new HashMap<>();
                            mapFile.put("taskImage", picPath);
                            //上传
                            int code = 0;
                            ///////////////////////发布任务的时候需要预留加载时间，否则code一来就为0了！！！！
                            try {
                                String str = "";
                                if (picPath.trim().length() != 0) {
                                    str = HttpHelper.postData(MyURL.INSERT_TASK, map, mapFile);
                                    code = HttpHelper.getCode(str);
                                    //mPresenterAddAty.sendTaskByParamMap(map, mapFile);
                                } else {
                                    str = HttpHelper.postData(MyURL.INSERT_TASK, map, null);
                                    code = HttpHelper.getCode(str);
                                }
                                if (code == 200) {

                                    Common.display(AtyMain.this, "发布成功！");
                                    if (FragHomeNormal.mBaiduMap!=null) {
                                        Bundle bundle = new Bundle();
                                        map.put("taskImage",picPath);
                                        bundle.putSerializable("actMapInfo", map);
                                        picPath = "";
                                        insertTaskToMap(FragHomeNormal.mBaiduMap,1,bundle);
                                    }

                                    dialog.dismiss();
//                                    if (!dialog.isShowing()) {
//                                        finish();
//                                        start(getApplicationContext(), AtyMain.class, false);
//                                    }

                                } else if (code == 201) {
                                    if (HttpHelper.getMsg(str).equals("your task number is more than five")){
                                        Common.display(AtyMain.this, "你当前进行的任务不能超过5个！");
                                    }else

                                    Common.display(AtyMain.this, "发布失败 " + HttpHelper.getMsg(str));

                                } else if (code == 202) {

                                    Common.display(AtyMain.this, "没有足够金币,发布失败");

                                   } else if (code == 204) {

                                Common.display(AtyMain.this, "你之前的即刻任务还没完成" + code);

                            }else   Common.display(AtyMain.this, "发布失败 ERROR CODE:" + code);
                            } catch (Exception e) {
                                e.printStackTrace();

                                Common.display(AtyMain.this, "发布失败 EXCEPTION");

                            }


                        }

                    }
                });
            }
        });

        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Activity activity = AtyMain.this;
                while (activity.getParent() != null) {
                    activity = activity.getParent();
                }


                if (sp.getInt("is_ver", 0) == 0) {
                    final Dialog dialogCerti = new Dialog(activity, R.style.MyDialogStyle);
                    dialogCerti.setContentView(R.layout.alert_dialog_certi);
                    dialogCerti.show();
                    dialogCerti.setCanceledOnTouchOutside(false);

                    final Button yesBtn = (Button) dialogCerti.findViewById(R.id.yesBtn);
                    final Button noBtn = (Button) dialogCerti.findViewById(R.id.noBtn);

                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Common.display(getApplicationContext(), "请等待审核");
                            dialogCerti.dismiss();
                            editor = sp.edit();
                            editor.putInt("is_ver", 1);
                            editor.commit();
                        }
                    });


                    noBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Common.display(getApplicationContext(), "未实名认证发布不了线下活动哦");
                            dialogCerti.dismiss();
//                        editor.putInt("is_ver",0);
//                        editor.commit();
                        }
                    });
                }
                else{
                /////////////////////
                final Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);
                dialog.setContentView(R.layout.aty_main_tooltip_addtask_offline);
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);


                final Button eatBtn = (Button) dialog.findViewById(R.id.eatBtn);
                final Button playBtn = (Button) dialog.findViewById(R.id.playBtn);
                eatBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eatBtn.setBackgroundResource(R.drawable.btn_capsule_yellow);
                        eatBtn.setTextColor(getResources().getColor(R.color.white));
                        playBtn.setBackgroundResource(R.drawable.btn_capsule_empty_yellow);
                        playBtn.setTextColor(getResources().getColor(R.color.themeColor));
                        label = 1;
                    }
                });
                playBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playBtn.setBackgroundResource(R.drawable.btn_capsule_yellow);
                        playBtn.setTextColor(getResources().getColor(R.color.white));
                        eatBtn.setBackgroundResource(R.drawable.btn_capsule_empty_yellow);
                        eatBtn.setTextColor(getResources().getColor(R.color.themeColor));
                        label = 2;
                    }
                });

                final EditText textViewName = (EditText) dialog.findViewById(R.id.activity_offline_name);
                final TextView textViewActivityTime = (TextView) dialog.findViewById(R.id.activity_offline_time);
                final EditText textViewDescription = (EditText) dialog.findViewById(R.id.activity_offline_description);
                //final TextView textViewActivityWhere = (TextView) dialog.findViewById(R.id.activity_offline_where);
                final EditText texViewActivitySum = (EditText) dialog.findViewById(R.id.activity_offline_num);
                final EditText textViewActivityMoney = (EditText) dialog.findViewById(R.id.activity_offline_money);
                textViewActivityTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentDateTime = DateTimeHelper.getCurrentDateTime("yyyy年MM月dd日 HH:mm");
                        DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(AtyMain.this, currentDateTime);
                        dateTimePicKDialog.dateTimePicKDialog(textViewActivityTime);

                    }
                });
                imageButton = (ImageButton) dialog.findViewById(R.id.activity_offline_imagebtn_image);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FunctionConfig functionConfig = new FunctionConfig.Builder()
                                .setEnableCrop(true)
                                .setEnableRotate(true)
                                .setCropSquare(false)
                                .setEnablePreview(true)
                                .setEnableEdit(true)//编辑功能
                                .setEnableCrop(true)//裁剪功能
                                .setEnableCamera(true)//相机功能
                                .setForceCropEdit(true)
                                .setCropHeight(520)
                                .setCropWidth(720)
                                .setForceCrop(true)
                                .build();
                        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);

                    }
                });


                final Button btnClose = (Button) dialog.findViewById(R.id.acttivity_offline_publish);

                textViewActivityMoney.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                        if (texViewActivitySum.getText().toString().trim().length() != 0
                                && textViewActivityMoney.getText().toString().trim().length() != 0) {
                            int money = Integer.parseInt(texViewActivitySum.getText().toString().trim())
                                    * Integer.parseInt(textViewActivityMoney.getText().toString().trim())
                                    * 8 / 10;
                            btnClose.setText("确认支付" + money + "金币以发布活动");
                        }

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });


                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isOK = false;
                        if (textViewDescription.getText().toString().trim().length() == 0) {
                            Common.display(AtyMain.this, "活动描述不能为空");
                        } else if (textViewActivityTime.getText().toString().trim().length() == 0) {
                            Common.display(AtyMain.this, "截止日期不能为空");
                        } else if (textViewActivityMoney.getText().toString().trim().length() == 0) {
                            Common.display(AtyMain.this, "活动价格不能为空");
                        } else if (textViewName.getText().toString().trim().length() == 0) {
                            Common.display(AtyMain.this, "活动名称不能为空");
                        } else if (texViewActivitySum.getText().toString().trim().length() == 0) {
                            Common.display(AtyMain.this, "参与人数不能为空");
                        } else if (texViewActivitySum.getText().toString().trim().length() != 0
                                && Integer.parseInt(texViewActivitySum.getText().toString().trim()) == 0) {
                            Common.display(AtyMain.this, "参与人数不能为0");
                        } else if (label == 0) {
                            Common.display(AtyMain.this, "请选择标签");
                        } else isOK = true;


                        if (isOK) {


                            HashMap<String, String> map1 = new HashMap<>();
                            map1.put("taskContent", textViewDescription.getText().toString().trim());
                            map1.put("taskDeadline",
                                    DateTimeHelper.timeMillis2FormatTime( DateTimeHelper.FormatTime2timeMillis(textViewActivityTime.getText().toString().trim(),
                                            "yyyy年MM月dd日 HH:mm") + "","yyyy-MM-dd HH:mm:ss"));
                            map1.put("taskMoney", textViewActivityMoney.getText().toString().trim());
                            map1.put("taskMaxNum", texViewActivitySum.getText().toString().trim());
                            map1.put("taskType", 2 + "");//线下任务
                            map1.put("taskUserID", mUserId + "");
                            //map1.put("taskLabel", resultSelectedItem.get(0));
                            String labelStr = "";
                            if (label == 1) labelStr = "吃的";
                            else if (label == 2) labelStr = "玩的";
                            map1.put("taskLabel", labelStr);
                            map1.put("taskLatitude", Common.myLatitude + "");
                            map1.put("taskLongitude", Common.myLongtitude + "");
                            //map1.put("taskPublishTime", System.currentTimeMillis() + "");
                            map1.put("taskName", textViewName.getText().toString().trim());

                            HashMap<String, String> mapFile = new HashMap<>();
                            mapFile.put("taskImage", picPath);

                            //上传
                            int code = 0;
                            ///////////////////////发布任务的时候需要预留加载时间，否则code一来就为0了！！！！
                            try {
                                String str = "";
                                if (picPath.trim().length() != 0) {
                                    str = HttpHelper.postData(MyURL.INSERT_TASK, map1, mapFile);
                                    code = HttpHelper.getCode(str);
                                    //mPresenterAddAty.sendTaskByParamMap(map, mapFile);
                                } else {
                                    str = HttpHelper.postData(MyURL.INSERT_TASK, map1, null);
                                    code = HttpHelper.getCode(str);
                                }
                                if (code == 200) {
                                    Common.display(AtyMain.this, "发布成功！");

                                    if (FragHomeNormal.mBaiduMap!=null) {
                                        Bundle bundle = new Bundle();
                                        map1.put("taskImage",picPath);
                                        bundle.putSerializable("actMapInfo", map1);
                                        picPath = "";
                                        insertTaskToMap(FragHomeNormal.mBaiduMap,2,bundle);
                                    }

                                    dialog.dismiss();
//                                    if (!dialog.isShowing()) {
//                                        finish();
//                                        start(getApplicationContext(), AtyMain.class, false);
//                                    }

                                } else if (code == 201) {

                                    if (HttpHelper.getMsg(str).equals("your task number is more than five")){
                                        Common.display(AtyMain.this, "你当前进行的任务不能超过5个！");
                                    }else

                                        Common.display(AtyMain.this, "发布失败 " + HttpHelper.getMsg(str));

                                } else if (code == 202) {

                                    Common.display(AtyMain.this, "没有足够金币,发布失败");

                                } else if (code == 204) {

                                    Common.display(AtyMain.this, "你之前的即刻任务还没完成" + code);

                                }else   Common.display(AtyMain.this, "发布失败 ERROR CODE:" + code);
                            } catch (Exception e) {
                                e.printStackTrace();

                                Common.display(AtyMain.this, "发布失败 EXCEPTION");

                            }


                        }

                    }
                });

            }
                //
            }
        }
        );



        jike.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Activity activity = AtyMain.this;
                                           while (activity.getParent() != null) {
                                               activity = activity.getParent();
                                           }


                                           if (sp.getInt("is_ver", 0) == 0) {
                                               final Dialog dialogCerti = new Dialog(activity, R.style.MyDialogStyle);
                                               dialogCerti.setContentView(R.layout.alert_dialog_certi);
                                               dialogCerti.show();
                                               dialogCerti.setCanceledOnTouchOutside(false);

                                               final Button yesBtn = (Button) dialogCerti.findViewById(R.id.yesBtn);
                                               final Button noBtn = (Button) dialogCerti.findViewById(R.id.noBtn);

                                               yesBtn.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       Common.display(getApplicationContext(), "请等待审核");
                                                       dialogCerti.dismiss();
                                                       editor = sp.edit();
                                                       editor.putInt("is_ver", 1);
                                                       editor.commit();
                                                   }
                                               });


                                               noBtn.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       Common.display(getApplicationContext(), "未实名认证发布不了线下活动哦");
                                                       dialogCerti.dismiss();
//                        editor.putInt("is_ver",0);
//                        editor.commit();
                                                   }
                                               });
                                           }
                                           else {

                                               final Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);
                                               dialog.setContentView(R.layout.aty_main_tooltip_addtask_jike);
                                               dialog.show();
                                               dialog.setCanceledOnTouchOutside(true);

                                               final Button tenBtn = (Button) dialog.findViewById(R.id.ten_min);
                                               final Button twentyBtn = (Button) dialog.findViewById(R.id.twenty);
                                               final Button thirtyBtn = (Button) dialog.findViewById(R.id.thirty_min);
                                               final Button sixtyBtn = (Button) dialog.findViewById(R.id.sixty_min);

                                               final Button btns[] = {tenBtn, twentyBtn, thirtyBtn, sixtyBtn};
                                               time = 1800000;

                                               tenBtn.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       setButtonStyle(tenBtn, btns);
                                                       time = 600000;
                                                   }
                                               });

                                               twentyBtn.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       setButtonStyle(twentyBtn, btns);
                                                       time = 1200000;
                                                   }
                                               });

                                               thirtyBtn.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       setButtonStyle(thirtyBtn, btns);
                                                       time = 1800000;
                                                   }
                                               });

                                               sixtyBtn.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       setButtonStyle(sixtyBtn, btns);
                                                       time = 3600000;
                                                   }
                                               });


                                               final EditText textViewName = (EditText) dialog.findViewById(R.id.activity_jike_name);
                                               final EditText textViewDescription = (EditText) dialog.findViewById(R.id.activity_jike_description);
                                               final EditText texViewActivitySum = (EditText) dialog.findViewById(R.id.activity_jike_num);
                                               final EditText textViewActivityMoney = (EditText) dialog.findViewById(R.id.activity_jike_money);
//
                                               imageButton = (ImageButton) dialog.findViewById(R.id.activity_jike_imagebtn_image);
                                               imageButton.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       FunctionConfig functionConfig = new FunctionConfig.Builder()
                                                               .setEnableCrop(true)
                                                               .setEnableRotate(true)
                                                               .setCropSquare(false)
                                                               .setEnablePreview(true)
                                                               .setEnableEdit(true)//编辑功能
                                                               .setEnableCrop(true)//裁剪功能
                                                               .setEnableCamera(true)//相机功能
                                                               .setForceCropEdit(true)
                                                               .setCropHeight(520)
                                                               .setCropWidth(720)
                                                               .setForceCrop(true)
                                                               .build();
                                                       GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);

                                                   }
                                               });


                                               final Button btnClose = (Button) dialog.findViewById(R.id.acttivity_jike_publish);

                                               textViewActivityMoney.addTextChangedListener(new TextWatcher() {
                                                   @Override
                                                   public void onTextChanged(CharSequence s, int start, int before,
                                                                             int count) {
                                                       if (texViewActivitySum.getText().toString().trim().length() != 0
                                                               && textViewActivityMoney.getText().toString().trim().length() != 0) {
                                                           int money = Integer.parseInt(texViewActivitySum.getText().toString().trim())
                                                                   * Integer.parseInt(textViewActivityMoney.getText().toString().trim())
                                                                   * 8 / 10;
                                                           btnClose.setText("确认支付" + money + "金币以发布活动");
                                                       }

                                                   }

                                                   @Override
                                                   public void beforeTextChanged(CharSequence s, int start, int count,
                                                                                 int after) {
                                                   }

                                                   @Override
                                                   public void afterTextChanged(Editable s) {
                                                   }
                                               });

                                               btnClose.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       boolean isOK = false;
                                                       if (textViewDescription.getText().toString().trim().length() == 0) {
                                                           Common.display(AtyMain.this, "活动描述不能为空");
                                                       } else if (time == 0) {
                                                           Common.display(AtyMain.this, "截止时间不能为空");
                                                       } else if (textViewActivityMoney.getText().toString().trim().length() == 0) {
                                                           Common.display(AtyMain.this, "活动价格不能为空");
                                                       } else if (textViewName.getText().toString().trim().length() == 0) {
                                                           Common.display(AtyMain.this, "活动名称不能为空");
                                                       } else if (texViewActivitySum.getText().toString().trim().length() == 0) {
                                                           Common.display(AtyMain.this, "参与人数不能为空");
                                                       } else if (texViewActivitySum.getText().toString().trim().length() != 0
                                                               && Integer.parseInt(texViewActivitySum.getText().toString().trim()) == 0) {
                                                           Common.display(AtyMain.this, "参与人数不能为0");
                                                       } else isOK = true;

                                                       if (isOK) {

                                                           HashMap<String, String> map1 = new HashMap<>();
                                                           map1.put("taskContent", textViewDescription.getText().toString().trim());
                                                           long deadline = (time + System.currentTimeMillis());
                                                           //System.out.println("TTTTTTTTTTIME===="+time+" "+System.currentTimeMillis()+" "+deadline);
                                                           map1.put("taskDeadline",   DateTimeHelper.timeMillis2FormatTime(deadline+"","yyyy-MM-dd HH:mm:ss"));
                                                           map1.put("taskMoney", textViewActivityMoney.getText().toString().trim());
                                                           map1.put("taskMaxNum", texViewActivitySum.getText().toString().trim());
                                                           map1.put("taskType", 3 + "");
                                                           map1.put("taskUserID", mUserId + "");

                                                           map1.put("taskLabel", "即刻");
                                                           map1.put("taskLatitude", Common.myLatitude + "");
                                                           map1.put("taskLongitude", Common.myLongtitude + "");
                                                           map1.put("taskName", textViewName.getText().toString().trim());

                                                           HashMap<String, String> mapFile = new HashMap<>();
                                                           mapFile.put("taskImage", picPath);

                                                           //上传
                                                           int code = 0;
                                                           ///////////////////////发布任务的时候需要预留加载时间，否则code一来就为0了！！！！
                                                           try {
                                                               String str = "";
                                                               if (picPath.trim().length() != 0) {
                                                                   str = HttpHelper.postData(MyURL.INSERT_TASK, map1, mapFile);
                                                                   code = HttpHelper.getCode(str);
                                                                   //mPresenterAddAty.sendTaskByParamMap(map, mapFile);
                                                               } else {
                                                                   str = HttpHelper.postData(MyURL.INSERT_TASK, map1, null);
                                                                   code = HttpHelper.getCode(str);
                                                               }
                                                               if (code == 200) {
                                                                   Common.display(AtyMain.this, "发布成功！");
                                                                   if (FragHomeNormal.mBaiduMap!=null) {
                                                                       Bundle bundle = new Bundle();
                                                                       map1.put("taskImage",picPath);
                                                                       bundle.putSerializable("actMapInfo", map1);
                                                                       picPath = "";
                                                                       insertTaskToMap(FragHomeNormal.mBaiduMap,3,bundle);
                                                                   }

                                                                   dialog.dismiss();
//                                                               if (!dialog.isShowing()) {
//                                                                   finish();
//                                                                   start(getApplicationContext(), AtyMain.class, false);
//                                                               }

                                                               } else if (code == 201) {

                                                                   if (HttpHelper.getMsg(str).equals("your task number is more than five")){
                                                                       Common.display(AtyMain.this, "你当前进行的任务不能超过5个！");
                                                                   }else

                                                                       Common.display(AtyMain.this, "发布失败 " + HttpHelper.getMsg(str));

                                                               } else if (code == 202) {

                                                                   Common.display(AtyMain.this, "没有足够金币,发布失败");

                                                               } else if (code == 204) {

                                                                   Common.display(AtyMain.this, "你之前的即刻任务还没完成" + code);

                                                               }else   Common.display(AtyMain.this, "发布失败 ERROR CODE:" + code);
                                                           } catch (Exception e) {
                                                               e.printStackTrace();

                                                               Common.display(AtyMain.this, "发布失败 EXCEPTION");
                                                           }

                                                       }

                                                   }
                                               });

                                           }

                                       }
                                   }
        );

        /////////////////////////////////



        tooltip.show();
    }



    public static void insertTaskToMap(BaiduMap baiduMap, int taskType, Bundle bundle) {
        Point point = new Point();
        point.x = Common.screenWidth / 2;
        point.y = Common.visibleHeight / 2;
        LatLng latLng = baiduMap.getProjection().fromScreenLocation(point);

        OverlayOptions op = null;
        Marker marker = null;
        if (latLng.longitude != 0 &&
                latLng.latitude != 0) {

 /*           if (taskType == 1) {
                op = new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory// 构建mark图标
                                .fromResource(R.drawable.map_online)).zIndex(5);
            }
            else if (taskType == 2) {
                op = new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory// 构建mark图标
                                .fromResource(R.drawable.map_reality)).zIndex(5);
            }
                else {
                op = new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory// 构建mark图标
                                .fromResource(R.drawable.map_immediately)).zIndex(5);
            }*/

            /*marker = (Marker) (baiduMap.addOverlay(op));
            //没有上一行，这里会报错，最后发布成功，但是地图上没有显示，因为地图数据是从marker上获取的
            marker.setExtraInfo(bundle);
            marker.setTitle("myAct");

            UtilLog.e("AtyMainLog","添加活动");
            UtilLog.e("AtyMainLog","添加活动"+latLng);
            UtilLog.e("AtyMainLog","添加活动"+taskType);
            UtilLog.e("AtyMainLog","添加活动"+marker.getExtraInfo());
            //聚合后发起活动
            MyItem myItem = new MyItem(latLng,taskType,marker);*/
            UtilLog.e("FrageHomeNormalAtyMainLog","添加活动");
            Bundle bundle1 = new Bundle();
            bundle1.putString("actOwn","myAct");
            bundle1.putBundle("myActBundle",bundle);
            MyItem myItem = new MyItem(latLng,taskType,bundle1);
            MarkerClusterDemo markerClusterDemo = MarkerClusterDemo.getSingleton();
            ClusterManager clusterManager = markerClusterDemo.getClusterManager();
            if (clusterManager==null){
                UtilLog.e("FrageHomeNormalAtyMainLog","clusterManager为空");
            }else {
                UtilLog.e("FrageHomeNormalAtyMainLog","clusterManager不为空");
            }
            clusterManager.addItem(myItem);
            //不知道有没有用，为了解觉添加后要对地图进行操作才显示的情况
            //markerClusterDemo.updataMap();
            //clusterManager.onMapStatusChange(baiduMap.getMapStatus());
            //markerClusterDemo.updataMap();
        }

    }


    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    }

    GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            Log.v("onHanlderSuccess", "reqeustCode: " + reqeustCode + "  resultList.size" + resultList.size());
            for (PhotoInfo info : resultList) {
                switch (reqeustCode) {
                    case REQUEST_CODE_GALLERY:
                        picPath = info.getPhotoPath();
                        bitmap = BitmapFactory.decodeFile(info.getPhotoPath());
                        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageButton.setImageBitmap(bitmap);
                }
            }


        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }

    };

    class StringTagAdapter extends TagAdapter<StringTagView, String> {

        StringTagAdapter(Context context, List<String> data) {
            this(context, data, null);
        }

        StringTagAdapter(Context context, List<String> data, List<String> selectItems) {
            super(context, data, selectItems);
        }

        /**
         * 检查item和所选item是否一样
         *
         * @param view
         * @param item
         * @return
         */
        @Override
        protected boolean checkIsItemSame(StringTagView view, String item) {
            return TextUtils.equals(view.getItem(), item);
        }

        /**
         * 检查item是否是空指针
         *
         * @return
         */
        @Override
        protected boolean checkIsItemNull(String item) {
            return TextUtils.isEmpty(item);
        }

        /**
         * 添加标签
         *
         * @param item
         * @return
         */
        @Override
        protected StringTagView addTag(String item) {
            StringTagView tagView = new StringTagView(getContext());
            tagView.setPadding(20, 20, 20, 20);

            TextView textView = tagView.getTextView();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            textView.setGravity(Gravity.CENTER);

            tagView.setItemDefaultDrawable(itemDefaultDrawable);
            tagView.setItemSelectDrawable(itemSelectDrawable);
            tagView.setItemDefaultTextColor(itemDefaultTextColor);
            tagView.setItemSelectTextColor(itemSelectTextColor);
            tagView.setItem(item);
            return tagView;
        }
    }

    class StringTagView extends BaseTagView<String> {

        public StringTagView(Context context) {
            this(context, null);
        }

        public StringTagView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs, 0);
        }

        public StringTagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public void setItem(String item) {
            super.setItem(item);
            textView.setText(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //程序启动，给服务其发送当前时间和UserID
        Long startTime = System.currentTimeMillis();
        String time = UtilTime.timeFormat(startTime);
        UtilLog.e("AtyMain",time);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String result = HttpHelper.postData(MyURL.UPDATE_BEHAVIOR,
                            UtilTime.getEnterTimeMap(mUserId), null);
                    int code = HttpHelper.getCode(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
    }

    @Override
    protected void onStop() {
        //程序结束，给服务其发送当前时间和UserID
        Long startTime = System.currentTimeMillis();
        String time = UtilTime.timeFormat(startTime);
        UtilLog.e("AtyMain",time);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String result = HttpHelper.postData(MyURL.UPDATE_BEHAVIOR,
                            UtilTime.getLeaveTimeMap(mUserId), null);
                    int code = HttpHelper.getCode(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
        super.onStop();
    }

    @Override
    protected void onPause() {
        JCoreInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        JCoreInterface.onResume(this);


        if (Common.user == null) {
            Common.userSP = getSharedPreferences("userSP", 0);
            SharedPreferences.Editor editor1 = Common.userSP.edit();
            editor1.putInt("ID", mUserId);
            editor1.putInt("status", 1);
            editor1.commit();
            Common.userID = mUserId;

            try {
                String jsonInfo = postData();
                HashMap<String, Object>
                        userHM = HttpHelper.AnalysisUserInfo(jsonInfo);
                Common.setUserInfo(userHM);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        UserInfo myInfo = JMessageClient.getMyInfo();

        if (myInfo == null){
            start(AtyMain.this, AtyLoginOrRegister.class,false);
            finish();
        }


//        if (myInfo == null) {
//            Intent intent = new Intent();
//            if (null != SharePreferenceManager.getCachedUsername()) {
//                intent.putExtra("userName", SharePreferenceManager.getCachedUsername());
//                intent.putExtra("avatarFilePath", SharePreferenceManager.getCachedAvatarPath());
//                intent.setClass(this, ReloginActivity.class);
//            } else {
//                intent.setClass(this, LoginActivity.class);
//            }
//            startActivity(intent);
//            finish();
//
//        }
//
//        else {

        if (Common.user != null && myInfo!=null) {
            String username = "yup_"+mUserId;
            String avatar =  MyURL.ROOT+Common.user.getAvatar();
            String localPath = Common.FULL_IMG_CACHE_PATH + avatar.hashCode();
            String signature = Common.user.getSignature();
            String sex = Common.user.getSex();
            int age = Common.user.getAge();
            int height = Common.user.getHeight();
            int weight = Common.user.getWeight();
            myInfo.setNickname(Common.user.getUserName());
            myInfo.setSignature(signature);
            if (sex.equals("1")) {
                myInfo.setGender(UserInfo.Gender.male);
            } else if (sex.equals("2")) {
                myInfo.setGender(UserInfo.Gender.female);
            } else myInfo.setGender(UserInfo.Gender.unknown);

            JMessageClient.updateMyInfo(UserInfo.Field.nickname, myInfo, new BasicCallback() {
                @Override
                public void gotResult(final int status, final String desc) {

                    if (status == 0) {
                        //OK
                    } else {
                        HandleResponseCode.onHandle(AtyMain.this, status, false);
                    }
                }
            });

            String oldpw = Common.user.getPassWord();
            String newpw = Common.user.getPassWord();

            JMessageClient.updateUserPassword(oldpw, newpw, new BasicCallback() {
                @Override
                public void gotResult(final int status, final String desc) {

                    if (status == 0) {
                        //OK

                    }

//                    else {
//                        HandleResponseCode.onHandle(AtyMain.this, status, false);
//                    }
                }
            });

            File file = new File(localPath);

            JMessageClient.updateUserAvatar(file, new BasicCallback() {
                @Override
                public void gotResult(final int status, final String desc) {

                    if (status == 0) {
                        //OK

                    }

//                    else {
//                        HandleResponseCode.onHandle(AtyMain.this, status, false);
//                    }
                }
            });
            // }
        }


        if (mFragMessage != null)
            mFragMessage.sortConvList();
        super.onResume();
    }


    public String postData() throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("id", mUserId + "");
        String result = HttpHelper.postData(MyURL.GET_USER_BY_ID, paramHM, null);
        return result;
    }


    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    Timer tExit = new Timer();

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(
                new Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_HOME), 0);
        // TODO Auto-generated method stub
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (isExit == false) {
                isExit = true;
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                        hasTask = true;
                    }
                };
                tExit.schedule(task, 2000);

            } else {
                ActivityInfo ai = homeInfo.activityInfo;
                Intent startIntent = new Intent(Intent.ACTION_MAIN);
                startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                startIntent.setComponent(new ComponentName(ai.packageName,
                        ai.name));
                startActivitySafely(startIntent);

            }
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(
                new Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_HOME), 0);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit == false) {
                isExit = true;
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                if (!hasTask) {
                }
            } else {

                ActivityInfo ai = homeInfo.activityInfo;
                Intent startIntent = new Intent(Intent.ACTION_MAIN);
                startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                startIntent.setComponent(new ComponentName(ai.packageName,
                        ai.name));
                startActivitySafely(startIntent);

            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    public void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            // Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }

    public void setButtonStyle(Button selectedBtn,  Button[] buttons) {
        for (int i = 0; i < buttons.length; i++) {

            buttons[i].setBackgroundResource(R.drawable.btn_capsule_empty_yellow);
            buttons[i].setTextColor(getResources().getColor(R.color.themeColor));


            if (buttons[i].equals(selectedBtn)) {
                buttons[i].setBackgroundResource(R.drawable.btn_capsule_yellow);
                buttons[i].setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

}
