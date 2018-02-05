package com.owo.module_b_home.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.amap.map3d.tools.ItemUtil;
import com.amap.map3d.tools.MapUtil;
import com.amap.map3d.tools.SigninUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.clusterutil.MarkerClusterDemo;
import com.baidu.mapapi.clusterutil.MyItem;
import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.module_a_selectlabel.widgets.AtySelectLabel;
import com.owo.module_b_home.recyclerview.changed.adapter.AdapterHomeChangedRecyclerview;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_c_detail.AtyDetail;
import com.owo.utils.DateTimeHelper;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.owo.base.BeanBase;
import com.owo.base.FragBase;
import com.owo.module_b_home.bean.BeanProp;
import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_home.presenter.PresenterHomeImpl;
import com.owo.module_b_home.presenter.PresneterHome;
import com.owo.module_b_home.recyclerview.normal.adapter.AdapterNormal;
import com.owo.module_b_home.recyclerview.normal.bean.BeanActivityLabel;
import com.owo.module_b_home.view.ViewHome;
import com.owo.utils.Common;
import com.owo.utils.Constants;
import com.owo.utils.UtilLog;
import com.wao.dogcat.controller.server.Pedometer;

import com.wao.dogcat.controller.server.TimeService;
import com.wao.dogcat.widget.CircleImageView;
import com.wao.dogcat.widget.CompleteDialog;
import com.wao.dogcat.widget.ItemMapDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import io.jchat.android.activity.LoginActivity;
import io.jchat.android.chatting.utils.HandleResponseCode;


/**
 * @author XQF
 * @created 2017/5/10
 */
public class FragHomeNormal extends FragBase implements ViewHome {


//    //中间的点我按钮
//    @BindView(R.id.btn_test)
//    protected Button mBtnTest;

    //定位按钮
    @BindView(R.id.btn_location)
    protected LinearLayout mBtnLocation;

    //道具按钮
    @BindView(R.id.btn_prop)
    protected LinearLayout mBtnProp;

    //查看自己活动按钮
    @BindView(R.id.btn_activities)
    protected LinearLayout mBtnActivities;

    private PresneterHome mPresneterHome;
    private List<BeanTask> mTasks;

    private List<List<BeanTask>> mBeanTasksResult;

    //正在接受报名
    private List<BeanTask> mTaskOnReceiving;
    //正在进行中的
    private List<BeanTask> mTaskOnWorking;
    private List<BeanBase> mBeanBases;


    private String[] mlabels = new String[]{
            "接收报名中",
            "正在进行中",
    };


    private List<BeanProp> mBeanProps;


    private boolean flag1,flag2,flag3;

    int code = 0;

    public static FragHomeNormal newInsstance() {
        return new FragHomeNormal();
    }




    @BindView(R.id.mapView)
    protected MapView myMapView;

    public static BaiduMap mBaiduMap;
    //是否是第一次定位
    private boolean isFirstLatLng = true;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private CheckBox animationBox = null;

    private Pedometer pedometer;
    private double myLongtitude, myLatitude;
    //UiSettings:百度地图 UI 控制器
    private UiSettings mUiSettings;

    private List<HashMap<String, Object>> camLists = new ArrayList<>();
    private List<HashMap<String, Object>> txtLists = new ArrayList<>();
    private List<BeanTask> nearbyTask = new ArrayList<>();


    private boolean mIsOpenDialog = false;
    private static final int REQUEST_CODE_GALLERY = 111;
    private String picPath;
    private Bitmap bitmap;
    private int mUserId;
    private CircleImageView circleImageView;
    private int chooseSex = 0;

    @BindView(R.id.marker_info)
    CardView markerInfo;

    private DisplayImageOptions options;
    private String path;

    private TimeCount time;
    private long restTime;

    ViewHolder viewHolder = null;

    LatLng mLoction = null;

    MapStatus ms;

    ClusterManager mClusterManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home_normal_layout, container, false);
        ButterKnife.bind(this, view);
        mIsOpenDialog = getActivity().getIntent().getBooleanExtra("flag", false);
        mPresneterHome = new PresenterHomeImpl(this);
        mBeanProps = new ArrayList<>();
        //从前面放进去的数据里面取出ID

        Common.userSP = getActivity().getSharedPreferences("userSP", 0);
        mUserId = Common.userSP.getInt("ID", 0);
        UtilLog.d("test", "本地取出来的userID: " + mUserId);

        if (!Common.isKitkatWithStepSensor(getContext())) {
            Common.display(getContext(), "你的手机没有计步器，将无法获取金币...");
        }
        Common.locationPermission(getActivity());

        if (!Common.isServiceRun(getContext(), "com.wao.dogcat.controller.server.TimeService")) {
            //开启时间服务
            Intent intent = new Intent(getContext(), TimeService.class);
            getContext().startService(intent);
        }


        /////////////////////////////////////////////////////
        //start(getActivity(), AtyCommUser.class);


        if (mIsOpenDialog) {

            Activity activity = getActivity();
            while (activity.getParent() != null) {
                activity = activity.getParent();
            }
            final CompleteDialog completeDialog = new CompleteDialog(activity, R.style.MyDialogStyle);
            completeDialog.show();
            final EditText editText = (EditText) completeDialog.findViewById(R.id.myName);
            final EditText editAge = (EditText) completeDialog.findViewById(R.id.myAge);
            final TextView msgUser = (TextView) completeDialog.findViewById(R.id.msgUser);
            final ImageView manBtn, womanBtn;
            manBtn = (ImageView) completeDialog.findViewById(R.id.manBtn);
            womanBtn = (ImageView) completeDialog.findViewById(R.id.womanBtn);
            circleImageView = (CircleImageView) completeDialog.findViewById(R.id.myHead);
            manBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chooseSex = 1;
                    manBtn.setImageResource(R.drawable.sex_man_selected_single_choose_sex);
                    womanBtn.setImageResource(R.drawable.sex_woman_unselected_single_choose_sex);
                }
            });
            womanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chooseSex = 2;
                    manBtn.setImageResource(R.drawable.sex_man_unselected_single_choose_sex);
                    womanBtn.setImageResource(R.drawable.sex_woman_selected_single_choose_sex);


                }
            });

            completeDialog.editAvatar(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //配置功能
                            FunctionConfig functionConfig = new FunctionConfig.Builder()
                                    .setEnableCrop(true)
                                    .setEnableRotate(true)
                                    .setCropSquare(true)
                                    .setEnablePreview(true)
                                    .setEnableEdit(true)//编辑功能
                                    .setEnableCrop(true)//裁剪功能
                                    .setEnableCamera(true)//相机功能
                                    .setForceCropEdit(true)
                                    .setCropHeight(200)
                                    .setCropWidth(200)
                                    .setForceCrop(true)
                                    .build();
                            GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);


                        }
                    }
            );
            completeDialog.ok(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String editName = editText.getText().toString().trim();
                    String editAgeStr = editAge.getText().toString().trim();
                    if (chooseSex != 0) {
                        if (editName.length() != 0) {
                            if (editName.length() >= 2 && editName.length() <= 12) {
                                if (editName.matches("^[A-Za-z0-9\u4E00-\u9FA5]{2,12}$")) {
                                    if (editAgeStr.length() > 0) {
                                        try {
                                            String json =
                                                    MapUtil.postData(MyURL.UPDATE_USERNAME_BY_ID, "userName", editName.trim());
                                            int code = HttpHelper.getCode(json);
                                            if (code == 200) {

                                                Common.user.setUserName(editName.trim());
                                                Common.user.setSex(chooseSex + "");

                                                HashMap<String, String> param = new HashMap<String, String>();
                                                param.put("sex", chooseSex + "");
                                                param.put("id", mUserId + "");
                                                HttpHelper.postData(MyURL.UPDATE_SEX_BY_ID, param, null);

                                                HashMap<String, String> param1 = new HashMap<String, String>();
                                                param1.put("age", editAgeStr + "");
                                                param1.put("id", mUserId + "");
                                                HttpHelper.postData(MyURL.UPDAET_AGE_BY_ID, param1, null);


//                                                HashMap<String, String> p = new HashMap<String, String>();
//                                                p.put("status", "1");
//                                                p.put("id", mUserId + "");
//                                                HttpHelper.postData(MyURL.UPDATE_STATUS_BY_ID, p, null);
//                                                Common.user.setStatus(1);
                                                UserInfo myInfo = JMessageClient.getMyInfo();
                                                if (myInfo != null) {
                                                    String avatar = Common.user.getAvatar();
                                                    String signature = Common.user.getSignature();
                                                    String sex = Common.user.getSex();
                                                    myInfo.setNickname(Common.user.getUserName());
                                                    myInfo.setSignature(signature);
                                                    if (sex.equals("1")) {
                                                        myInfo.setGender(UserInfo.Gender.male);
                                                    } else if (sex.equals("2")) {
                                                        myInfo.setGender(UserInfo.Gender.female);
                                                    } else
                                                        myInfo.setGender(UserInfo.Gender.unknown);

                                                    JMessageClient.updateMyInfo(UserInfo.Field.nickname, myInfo, new BasicCallback() {
                                                        @Override
                                                        public void gotResult(final int status, final String desc) {

                                                            if (status == 0) {
                                                                //OK
                                                            } else {
                                                                HandleResponseCode.onHandle(getContext(), status, false);
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

                                                        }
                                                    });

                                                    File file = new File(avatar);

                                                    JMessageClient.updateUserAvatar(file, new BasicCallback() {
                                                        @Override
                                                        public void gotResult(final int status, final String desc) {

                                                            if (status == 0) {

                                                            }

                                                        }
                                                    });

                                                }

                                                start(getContext(), AtySelectLabel.class);
                                                //getActivity().finish();
                                                completeDialog.dismiss();

//                                                SharedPreferences sp = getSharedPreferences("guideSingleSP", 0);
//                                                if (sp.getInt("flag", 0) == 0) {
//                                                    SharedPreferences.Editor ed = sp.edit();
//                                                    ed.putInt("flag", 1);
//                                                    ed.commit();
//                                                    final RelativeLayout firstll = (RelativeLayout) findViewById(R.id.firstll);
//                                                    firstll.setVisibility(View.VISIBLE);
//                                                    ImageView delMap = (ImageView) findViewById(R.id.delMap);
//                                                    delMap.setOnClickListener(
//                                                            new View.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(View view) {
//                                                                    firstll.setVisibility(View.GONE);
//                                                                }
//                                                            }
//                                                    );
//                                                }


                                            } else {
                                                if (code == 0) {
                                                    Common.display(getContext(), "服务器错误，请稍后再试");

                                                } else {
                                                    Common.display(getContext(), code + ":" + HttpHelper.getMsg(json));
                                                }
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Common.display(getContext(), "服务器错误，请稍后再试");
                                        }
                                    } else msgUser.setText("你输入的年龄不对哦~");
                                } else msgUser.setText("昵称不能包含特殊字符哦~");
                            } else msgUser.setText("昵称只能为2~12位字符哦~");
                        } else msgUser.setText("昵称不能为空哦~");
                    } else msgUser.setText("请选择性别");


                }
            });


        }else{
            checkIn();
        }


        //加载申请任务
        mPresneterHome.loadTasksAppliedByUserId(mUserId);
        //加载发布的任务
        mPresneterHome.loadTasksPublishedByUserId(mUserId);
        //加载我的道具
        //mPresneterHome.loadUserPropNyUserId(mUserId);
        //加载附近任务
        mPresneterHome.loadTasksNearbyByLatitudeAndLongtitude(myLatitude + "",
                myLongtitude + "",mUserId);

//        mPresneterHome.loadCamByUserId(mUserId);
//        mPresneterHome.loadTxtByUserId(mUserId);

        mTasks = new ArrayList<>();
        mBeanTasksResult = new ArrayList<>();
        mTaskOnReceiving = new ArrayList<>();
        mTaskOnWorking = new ArrayList<>();
        mBeanBases = new ArrayList<>();

//        if (mIsOpenDialog) {
//
//            mBtnTest.performClick();
//        } else {
//            mBtnTest.setVisibility(View.GONE);
//
//        }

        //////////////////////////////////////////////
        //MapView.setMapCustomEnable(true);
        //MapUtil.setMapCustomStyleFile(getContext());
        pedometer = new Pedometer(getContext());
        initMap();

        return view;
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
                        circleImageView.setImageBitmap(bitmap);
                        try {

                            if (picPath != null) {
                                String json =
                                        MapUtil.postData(picPath);
                                int code = HttpHelper.getCode(json);
                                if (code == 200) {
                                    String avatarPath = HttpHelper.AnalysisData(json).toString();
                                    Common.user.setAvatar(avatarPath);

                                } else {
                                    if (code == 0) {
                                        Common.display(getContext(), "服务器错误，请稍后再试");
                                    } else
                                        Common.display(getContext(), code + ":" + HttpHelper.getMsg(json));
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Common.display(getContext(), "服务器错误，请稍后再试");
                        }

                        break;
                }
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(getContext(), "requestCode: " + requestCode + "  " + errorMsg, Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Intent in = getActivity().getIntent();
        int itemID = -1;
        itemID = in.getIntExtra("item", 0);
        if (itemID != -1)
            ItemUtil.itemManage(itemID, getContext(), mBaiduMap);

        if (itemID == 111) {
            ItemUtil.showPublishPicDialog(getContext(), Common.picPath, mBaiduMap, false);
        }

        if (itemID == 112) {
            ItemUtil.showPublishPicDialog(getContext(), Common.picPath, mBaiduMap, false);
        }

      /*  MapUtil.addCam(mBaiduMap, 1, 1, camLists);
        MapUtil.addTxt(mBaiduMap, 1, 1, txtLists);*/


    }


    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (myMapView != null)
            mBaiduMap = myMapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();

        // 隐藏百度的LOGO
        View child = myMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        // 显示地图上比例尺
        myMapView.showScaleControl(true);

        // 不显示地图缩放控件（按钮控制栏）
        myMapView.showZoomControls(false);

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        mCurrentMarker = BitmapDescriptorFactory// 构建mark图标
                .fromResource(R.drawable.map_user);

        mBaiduMap.setMyLocationEnabled(true);


        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));

        // 定位初始化
        mLocClient = new LocationClient(getContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();


        //mBaiduMap.showMapPoi(false);
        mBaiduMap.setMaxAndMinZoomLevel(20, 1);
        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setOverlookingGesturesEnabled(false);

        /////////////////////////////////////

        /**
         * 聚合功能后的点击事件
         */
        UtilLog.e("FrageHomeNormal","开始进入聚合点击"+getContext());
      /*  MarkerClusterDemo markerClusterDemo = MarkerClusterDemo.getSingleton();
        markerClusterDemo.updataMap();
        markerClusterDemo.init(getContext(),mBaiduMap);
        ClusterManager mClusterManager = markerClusterDemo.getClusterManager();
        if (mClusterManager!=null){
            UtilLog.e("FrageHomeNormal","mClusterManager不为空");
        }else {
            UtilLog.e("FrageHomeNormal","mClusterManager为空");
        }
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        if (mClusterManager!=null){
            UtilLog.e("FrageHomeNormal","mClusterManager不为空");
        }else {
            UtilLog.e("FrageHomeNormal","mClusterManager为空");
        }
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
        UtilLog.e("FrageHomeNormal","聚合点击");*/



         /*对Marker的点击*/
 /*        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                UtilLog.e("FrageHome","点击事件");
                if (marker.getTitle().equals("camera")) {
                    HashMap<String, Object> camHM =
                            (HashMap<String, Object>) marker.getExtraInfo().get("camInfo");
                    MapUtil.viewMomentCamInfo(mBaiduMap, marker, camHM, getContext());
                } else if (marker.getTitle().equals("text")) {
                    HashMap<String, Object> txtHM =
                            (HashMap<String, Object>) marker.getExtraInfo().get("txtInfo");
                    MapUtil.viewMomentTxtInfo(mBaiduMap, marker, txtHM, getContext());
                } else if (marker.getTitle().equals("myCamera")) {
                    HashMap<String, Object> camHM =
                            (HashMap<String, Object>) marker.getExtraInfo().get("myCamInfo");
                    MapUtil.viewMomentCamInfo(mBaiduMap, marker, camHM, getContext());
                } else if (marker.getTitle().equals("myText")) {
                    HashMap<String, Object> camHM =
                            (HashMap<String, Object>) marker.getExtraInfo().get("myTextInfo");
                    MapUtil.viewMomentTxtInfo(mBaiduMap, marker, camHM, getContext());
                } else if (marker.getTitle().equals("act")) {

                    BeanTask camHM =
                            (BeanTask) marker.getExtraInfo().get("actInfo");
//                    MarkerOptions ooD = new MarkerOptions().position(llD).icons(giflist)
//                            .zIndex(0).period(10);
//                    if (animationBox.isChecked()) {
//                        // 生长动画
//                        ooD.animateType(MarkerOptions.MarkerAnimateType.grow);
//                    }
//                    Marker  mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));
                    //MapUtil.viewActInfo(mBaiduMap, marker, camHM, getContext());
//                    if (camHM.getTaskType()==1){
//                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_online_select_pin_big));
//                    }else if (camHM.getTaskType()==2){
//                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_reality_select_pin_big));
//                    }else {
//                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_immediately_select_pin_big));
//                    }
                    markerInfo.setVisibility(View.VISIBLE);
                    popInfo(camHM);

                } else if (marker.getTitle().equals("myAct")){
                    HashMap<String, String> hm =
                            (HashMap<String, String>) marker.getExtraInfo().get("actMapInfo");
                    BeanTask task = new BeanTask();
                    task.setSex(Common.user.getSex().trim().length()==0 ? 1:
                            Integer.parseInt(Common.user.getSex()));
                    task.setTaskType(Integer.parseInt(hm.get("taskType")));
                    task.setTaskLabel(hm.get("taskLabel").toString());
                    task.setTaskDeadLine(DateTimeHelper.FormatTime2timeMillis(hm.get("taskDeadline").toString(),
                            "yyyy-MM-dd HH:mm:ss")+"");
                    task.setTaskContent(hm.get("taskContent").toString());
                    task.setTaskName(hm.get("taskName").toString());
                    task.setTaskIamge(hm.get("taskImage").toString());
                    task.setTaskMoney(Integer.parseInt(hm.get("taskMoney")));
                    task.setTaskMaxNum(Integer.parseInt(hm.get("taskMaxNum")));
                    task.setTaskLatitude(hm.get("taskLatitude").toString());
                    task.setTaskLongitude(hm.get("taskLongitude").toString());
                    task.setTaskUserID(Integer.parseInt(hm.get("taskUserID")));
                    task.setAvatar(Common.user.getAvatar());
                    task.setUserName(Common.user.getUserName());

                    markerInfo.setVisibility(View.VISIBLE);
                    popInfo(task);
                }
                return true;
            }
        });*/


        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0) {
                if (code!=200)
                markerInfo.setVisibility(View.GONE);
                //mBaiduMap.hideInfoWindow();

            }
        });


    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onConnectHotSpotMessage(java.lang.String connectWifiMac,
                                            int hotSpotState) {

        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || myMapView == null) {
                return;
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(1f)
                    //定位精度（圆圈半径）
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            myLatitude = location.getLatitude();
            myLongtitude = location.getLongitude();
            //自己位置
            mLoction = new LatLng(myLatitude,myLongtitude);

            if (isFirstLatLng) {
                isFirstLatLng = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(16);
                //设置我的位置为地图的中心点
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                //放置一个marker
                // overlay(ll, mCurrentMarker, mBaiduMap);
                try {
                    //更新自己的位置信息
                    HashMap<String, String> param = new HashMap<>();
                    param.put("id", mUserId + "");
                    param.put("latitude", myLatitude + "");
                    param.put("longtitude ", myLongtitude + "");
                    HttpHelper.postData(MyURL.UPDATE_LOCATION_BY_ID, param, null);
                    Common.myLatitude = myLatitude;
                    Common.myLongtitude = myLongtitude;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v("BDLocation", "UPDATE_LOCATION_BY_ID");
                }
            }
            ///////////////////////////////////////////////
            //updateUI();
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        pedometer.register();
    }

    @Override
    public void onResume() {
        super.onResume();
        myMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        myMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        myMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        pedometer.unRegister();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        if (mLocClient != null)
            mLocClient.stop();
        // 关闭定位图层
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);
            MapView.setMapCustomEnable(false);
            mBaiduMap.clear();
        }
        if (myMapView != null) {
            myMapView.onDestroy();
            myMapView = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 点击定位按钮弹出内容
     */
    @OnClick(R.id.btn_location)
    public void onBtnLocationClick() {
//        new SimpleTooltip.Builder(getActivity())
//                .anchorView(mBtnLocation)
//                .text(R.string.btn_simple)
//                .gravity(Gravity.START)
//                .build()
//                .show();
        MapUtil.toLocation(myLongtitude, myLatitude, mBaiduMap, 16);
    }


//    /**
//     * 点击道具按钮弹出的内容
//     */
//    @OnClick(R.id.btn_prop)
//    public void onBtnPropClick() {
////        new SimpleTooltip.Builder(getActivity())
////                .anchorView(mBtnProp)
////                .text("使用道具")
////                .gravity(Gravity.TOP)
////                .contentView(R.layout.tooltip_custom_xqf_prop, R.id.textV_useProp)
////                .build()
////                .show();
//        Activity activity = getActivity();
//        while (activity.getParent() != null) {
//            activity = activity.getParent();
//        }
//        final ItemMapDialog itemMapDialog =
//                new ItemMapDialog(activity, R.style.MyDialogStyle, mBaiduMap);
//        itemMapDialog.show();
//    }


    //点击活动按钮查看活动

    @OnClick(R.id.btn_activities)
    public void onBtnActivitiesClick() {
        Activity activity = getActivity();
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        final Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.frag_home_activities_dialog);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout loading = (LinearLayout) dialog.findViewById(R.id.loading);
        TextView noData = (TextView) dialog.findViewById(R.id.noData);

        if (mBeanBases.size()>0) {
            loading.setVisibility(View.GONE);
            RecyclerView mRecyclerview = (RecyclerView) dialog.findViewById(R.id.dialog_recyclerview);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            AdapterNormal adapterNormal = new AdapterNormal(getActivity());
            adapterNormal.addItems(mBeanBases);
            mRecyclerview.setAdapter(adapterNormal);
        }else {
            loading.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
            noData.setText("暂时没有我的活动");
        }
    }

    @Override
    public void getTasksIApplied(List<BeanTask> list) {
        if (list!=null)
        mTasks.addAll(list);
        flag3 = true;
    }

    @Override
    public void getTasksIPublished(List<BeanTask> taskList) {
       if (taskList!=null)
        mTasks.addAll(taskList);
        if (flag3 && mTasks.size()>0){
            for (int i = 0; i < mTasks.size(); i++) {
                BeanTask task = mTasks.get(i);
                if (task.getTaskStatus() == Constants.TASK_STATUS_ENROOLING) {
                    mTaskOnReceiving.add(task);
                } else if (task.getTaskStatus() == Constants.TASK_STATUS_WORKING) {
                    mTaskOnWorking.add(task);
                }
            }
            mTasks.clear();

            mBeanTasksResult.add(mTaskOnReceiving);
            mBeanTasksResult.add(mTaskOnWorking);
            System.out.println("mBeanTasksResult="+mBeanTasksResult);
            for (int i = 0; i < mBeanTasksResult.size(); i++) {
                mBeanBases.add(new BeanActivityLabel(mlabels[i]));
                mBeanBases.addAll(mBeanTasksResult.get(i));
            }
            System.out.println("mBeanBases="+mBeanBases);

//            new Thread() {
//                @Override
//                public void run() {
//                    Message msg = handler.obtainMessage();
//                    msg.what = 2;
//                    handler.sendMessage(msg);
//                }
//            }.start();
        }
    }

//    @Override
//    public void getMyProps(List<BeanProp> propList) {
//        mBeanProps = propList;
//    }
//
//    @Override
//    public void getText(List<HashMap<String, Object>> data){
//        txtLists = data;
//        flag1 = true;
//
//
//    }
//    @Override
//    public void getCamera(List<HashMap<String, Object>> data){
//        camLists = data;
//        flag2 = true;
//
//    }

    @Override
    public void getTasksNearby(List<BeanTask> taskList){
        nearbyTask = taskList;
       // if (flag1 && flag2){
            new Thread() {
                @Override
                public void run() {
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }.start();
       // }
    }


    public  void checkIn(){
        try {
            if (mUserId != -1) {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("userID", mUserId + "");
                int code = HttpHelper.getCode(HttpHelper.postData(MyURL.INSERT_SINGLE_SIGN, param, null));
                if (code == 200) {
                    SigninUtil.showSingleSignDialog(getContext());
                    if (Common.user != null) {
                        HashMap<String, String> data = new HashMap<>();
                        data.put("id", mUserId + "");
                        data.put("checkinDays", (Common.user.getCheckinDays() + 1) + "");
                        if (HttpHelper.getCode(HttpHelper.postData(MyURL.UPDATE_CHECKINDAYS_BY_ID, data, null)) == 200) {
                            Common.user.setCheckinDays(Common.user.getCheckinDays() + 1);
                            HashMap<String, String> dd = new HashMap<>();
                            dd.put("id", mUserId + "");
                            dd.put("money", (Common.user.getMoney() + (Common.user.getCheckinDays() % 5 == 0 ? 500 : 100)) + "");
                            if (HttpHelper.getCode(HttpHelper.postData(MyURL.UPDATE_MONEY_BY_ID, dd, null)) == 200) {
                                Common.display(getContext(), "金币 +" + (Common.user.getCheckinDays() % 5 == 0 ? 500 : 100));

                            }

                        }

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*
 * 默认的点击按钮会弹出最后注册对话框，选择完成后可以上传个人信息
 */
//    @OnClick(R.id.btn_test)
//    public void onBtnTestClick() {
//
//
//        int age = -1;
//        String username = null;
//        final int[] sex = {1};
//
//
//        final PresneterHome presenterHome = mPresneterHome;
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.setContentView(R.layout.dialog);
//        dialog.show();
//
////        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
////        lp.copyFrom(dialog.getWindow().getAttributes());
////        lp.width = (int) SimpleTooltipUtils.pxFromDp(400);
////        lp.height = (int) SimpleTooltipUtils.pxFromDp(500);
////        dialog.getWindow().setAttributes(lp);
//
//        final ImageButton imageButton = (ImageButton) dialog.findViewById(R.id.avatar);
//
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FunctionConfig functionConfig = new FunctionConfig.Builder()
//                        .setEnableCrop(true)
//                        .setEnableRotate(true)
//                        .setCropSquare(false)
//                        .setEnablePreview(true)
//                        .setEnableEdit(true)//编辑功能
//                        .setEnableCrop(true)//裁剪功能
//                        .setEnableCamera(true)//相机功能
//                        .setForceCropEdit(true)
//                        .setCropHeight(500)
//                        .setCropWidth(500)
//                        .setForceCrop(true)
//                        .build();
//                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
//                imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                imageButton.setImageBitmap(bitmap);
//            }
//        });
//        final Button btnClose = (Button) dialog.findViewById(R.id.btn_close);
//        final EditText editTextUserName = (EditText) dialog.findViewById(R.id.dialog_editT_username);
//        final EditText editTextAge = (EditText) dialog.findViewById(R.id.dialog_editT_age);
//
//
//        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.dialog_radiogroup);
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if (i == R.id.male) {
//                    sex[0] = 1;
//                } else {
//                    sex[0] = 2;
//                }
//            }
//        });
//
//
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String username = editTextUserName.getText().toString().trim();
//                String age = editTextAge.getText().toString().trim();
//                //把对话框中获取的数据全部上传
//                presenterHome.sendUserOtherInfoById(mUserId, picPath, username, sex[0], age);
//                mBtnTest.setVisibility(View.GONE);
//                dialog.dismiss();
//            }
//        });
//
//
//    }
//
//    GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
//        @Override
//        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
//            Log.v("onHanlderSuccess", "reqeustCode: " + reqeustCode + "  resultList.size" + resultList.size());
//            for (PhotoInfo info : resultList) {
//                switch (reqeustCode) {
//                    case REQUEST_CODE_GALLERY:
//                        picPath = info.getPhotoPath();
//                        bitmap = BitmapFactory.decodeFile(info.getPhotoPath());
//                }
//            }
//
//
//        }
//
//        @Override
//        public void onHanlderFailure(int requestCode, String errorMsg) {
//
//        }
//
//    };
    public void mClusterManagerClick(ClusterManager mClusterManager){
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        if (mClusterManager!=null){
            UtilLog.e("FrageHomeNormal","mClusterManager不为空");
        }else {
            UtilLog.e("FrageHomeNormal","mClusterManager为空");
        }

        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);

        UtilLog.e("FrageHomeNormal","聚合点击");
        //点击聚合后的点
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                //点的个数
                int count = cluster.getSize();
                Toast.makeText(getContext(),"有"+count+"个act",Toast.LENGTH_SHORT).show();
                // TODO: 2018/2/1
                // 可以对聚合点点击事件做进一步修改
                return true;
            }

        });

        //点击点单个Item

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                Toast.makeText(getContext(),"点击单个act",Toast.LENGTH_SHORT).show();
                Bundle bundle = item.getBundle();
                String act = bundle.getString("actOwn");

                //随意弄的坐标和图片,为了不报错
                LatLng latLng = new LatLng(38,121);
                OverlayOptions op = new MarkerOptions().position(latLng).
                        icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.map_reality)).zIndex(5).alpha(0);


                Marker marker  = (Marker) (mBaiduMap.addOverlay(op));
                Bundle bundle1 = new Bundle();
                //判断属于哪一种活动
                if (act.equals("act")){
                    BeanTask beanTask = (BeanTask) bundle.getSerializable("actInfo");
                    bundle1.putSerializable("actInfo",beanTask);
                    marker.setExtraInfo(bundle1);
                    marker.setTitle("act");
                }else if (act.equals("myAct")){
                    bundle1 = bundle.getBundle("myActBundle");
                    marker.setExtraInfo(bundle1);
                    marker.setTitle("myAct");
                }


                if (marker.getTitle().equals("camera")) {
                    HashMap<String, Object> camHM =
                            (HashMap<String, Object>) marker.getExtraInfo().get("camInfo");
                    MapUtil.viewMomentCamInfo(mBaiduMap, marker, camHM, getContext());
                } else if (marker.getTitle().equals("text")) {
                    HashMap<String, Object> txtHM =
                            (HashMap<String, Object>) marker.getExtraInfo().get("txtInfo");
                    MapUtil.viewMomentTxtInfo(mBaiduMap, marker, txtHM, getContext());
                } else if (marker.getTitle().equals("myCamera")) {
                    HashMap<String, Object> camHM =
                            (HashMap<String, Object>) marker.getExtraInfo().get("myCamInfo");
                    MapUtil.viewMomentCamInfo(mBaiduMap, marker, camHM, getContext());
                } else if (marker.getTitle().equals("myText")) {
                    HashMap<String, Object> camHM =
                            (HashMap<String, Object>) marker.getExtraInfo().get("myTextInfo");
                    MapUtil.viewMomentTxtInfo(mBaiduMap, marker, camHM, getContext());
                } else if (marker.getTitle().equals("act")) {


                    BeanTask camHM =
                            (BeanTask) marker.getExtraInfo().get("actInfo");
                    //如果点击的是上一次点击的点，则取消视图，否则显示
                    //实际效果是点击非marker取消视图
                    int taskId =-1;
                    if (taskId == camHM.getTaskId()){
                        markerInfo.setVisibility(View.GONE);
                    }else {
                        markerInfo.setVisibility(View.VISIBLE);
                    }
                    popInfo(camHM);
                } else if (marker.getTitle().equals("myAct")){
                    HashMap<String, String> hm =
                            (HashMap<String, String>) marker.getExtraInfo().get("actMapInfo");
                    BeanTask task = new BeanTask();
                    task.setSex(Common.user.getSex().trim().length()==0 ? 1:
                            Integer.parseInt(Common.user.getSex()));
                    task.setTaskType(Integer.parseInt(hm.get("taskType")));
                    task.setTaskLabel(hm.get("taskLabel").toString());
                    task.setTaskDeadLine(DateTimeHelper.FormatTime2timeMillis(hm.get("taskDeadline").toString(),
                            "yyyy-MM-dd HH:mm:ss")+"");
                    task.setTaskContent(hm.get("taskContent").toString());
                    task.setTaskName(hm.get("taskName").toString());
                    task.setTaskIamge(hm.get("taskImage").toString());
                    task.setTaskMoney(Integer.parseInt(hm.get("taskMoney")));
                    task.setTaskMaxNum(Integer.parseInt(hm.get("taskMaxNum")));
                    task.setTaskLatitude(hm.get("taskLatitude").toString());
                    task.setTaskLongitude(hm.get("taskLongitude").toString());
                    task.setTaskUserID(Integer.parseInt(hm.get("taskUserID")));
                    task.setAvatar(Common.user.getAvatar());
                    task.setUserName(Common.user.getUserName());

                    int taskId =-1;
                    if (taskId == task.getTaskId()){
                        markerInfo.setVisibility(View.GONE);
                    }else {
                        markerInfo.setVisibility(View.VISIBLE);
                    }
                    popInfo(task);
                }
                return true;
            }
        });
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //多传了一个上线下文
                    mClusterManager = MapUtil.addAct(getContext(),mBaiduMap, nearbyTask);
                    mClusterManagerClick(mClusterManager);
                    try {
                        HashMap<String,String> map = new HashMap<>();
                        map.put("userID",Common.userID+"");
                        code =Integer.parseInt(HttpHelper.getMsg( HttpHelper.postData(MyURL.HAS_TASK_3,map,null)));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

//                    if (code == 200){
//                        markerInfo.setVisibility(View.VISIBLE);
//                    }

                    break;


            }
        }
    };

    public class ViewHolder{
        CircleImageView avatar;
        TextView userName;
        TextView actName;
        LinearLayout deadlinell;
        TextView deadline;
        TextView where;
        TextView people;
        TextView timetxt;
        TextView jikeTime;
        Button markerBtn;
    }

    public String getWhere(String latitude, String longitude) {
        String locStr = "";
        try {
            locStr =
                    HttpHelper.getLocation(
                            HttpHelper.sendGet(MyURL.BAIDU_MAP_URL + "&location="
                                    + latitude + "," + longitude, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locStr;
    }

//    TimerTask timerTask = new TimerTask() {
//        @Override
//        public void run() {
//            getActivity().runOnUiThread(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            restTime -= 1000;
//                            viewHolder.jikeTime.setText(DateTimeHelper.timeMillis2FormatTime(restTime+"","hh:mm:ss"));
//                            if (restTime < 0) {
//                                timerTask.cancel();
//                            }
//                        }
//                    }
//            );
//        }
//    };

    public void popInfo(final BeanTask info){

        if (markerInfo.getTag() == null){
            viewHolder = new ViewHolder();
            viewHolder.avatar = (CircleImageView) markerInfo.findViewById(R.id.avatar);
            viewHolder.userName = (TextView) markerInfo.findViewById(R.id.username);
            viewHolder.actName = (TextView) markerInfo.findViewById(R.id.activity_name);
            viewHolder.deadlinell = (LinearLayout) markerInfo.findViewById(R.id.deadlinell);
            viewHolder.deadline = (TextView) markerInfo.findViewById(R.id.activity_deadline);
            viewHolder.where = (TextView) markerInfo.findViewById(R.id.activity_where);
            viewHolder.people = (TextView) markerInfo.findViewById(R.id.activity_people);
            viewHolder.timetxt = (TextView) markerInfo.findViewById(R.id.timetxt);
            viewHolder.jikeTime = (TextView) markerInfo.findViewById(R.id.item_jike_time);
            viewHolder.markerBtn = (Button) markerInfo.findViewById(R.id.marker_btn);

            markerInfo.setTag(viewHolder);

        }



        options = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my) //
                .showImageOnFail(R.drawable.my) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
        path = MyURL.ROOT + info.getAvatar();

        viewHolder = (ViewHolder) markerInfo.getTag();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                displayImage(path, viewHolder.avatar, options);
        viewHolder.userName.setText(info.getUserName());
        viewHolder.actName.setText(info.getTaskName());
        viewHolder.where.setText(getWhere(info.getTaskLatitude(),info.getTaskLongitude()));
        String s = info.getTaskTakenNum() + "/" + info.getTaskMaxNum();
        if (info.getTaskTakenNum() < info.getTaskMaxNum()) {
            if (info.getTaskType()==1)
            s = "参与人数："+info.getTaskTakenNum();
            else
            s += "(尚未满人)";
        }
        viewHolder.people.setText(s);
        viewHolder.deadline.setText("截止时间：" +
                DateTimeHelper.timeMillis2FormatTime(
                        info.getTaskDeadLine(), "yyyy.MM.dd"));
        if (info.getTaskType()==3) { //即刻活动
            viewHolder.deadlinell.setVisibility(View.GONE);
            viewHolder.timetxt.setVisibility(View.VISIBLE);
            viewHolder.jikeTime.setVisibility(View.VISIBLE);
            restTime =  Long.parseLong(info.getTaskDeadLine()) - System.currentTimeMillis();
           System.out.println("TTTTTTTTTTTIME==="+restTime+" "+info.getTaskDeadLine()+" "+System.currentTimeMillis());
            int count = 0;
            if (restTime > 0 && count == 0) {
                count++;
                time = new TimeCount(restTime, 1000);// 构造CountDownTimer对象
                time.start();
            }else {
                //time.cancel();
                viewHolder.jikeTime.setText("已过期");
            }
        }



        viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        HashMap<String,String> hm = new HashMap<>();
                        hm.put("id",info.getTaskUserID()+"");
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
        });

        //处理button
        viewHolder.markerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.getTaskType()==1)
                    //线上
                    AtyDetail.startAtyDetail(getContext(), AtyDetail.class, info,AtyDetail.VALUE_TYPE_ONLINEATYDETAIL);
                else{
                    if (info.getTaskStatus() == 3)
                        AtyDetail.startAtyDetail(getContext(), AtyDetail.class, info,AtyDetail.VALUE_TYPE_VIEWATYCMT);
                    else {
                        AtyDetail.startAtyDetail(getContext(), AtyDetail.class, info, AtyDetail.VALUE_TYPE_ATYDETAIL);
                    }
                }
            }
        });

    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            viewHolder.jikeTime.setText("已过期");
            time.cancel();
            // SmsCompleteManager.Instance.removeListener(RegActivity.this);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            viewHolder.jikeTime.setText(DateTimeHelper.countdownMillis2FormatTime(millisUntilFinished));
        }
    }



}
