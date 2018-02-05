package com.amap.map3d.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.clusterutil.MarkerClusterDemo;
import com.baidu.mapapi.clusterutil.MyItem;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_home.widgets.FragHomeNormal;
import com.owo.module_c_detail.AtyDetail;
import com.owo.utils.Common;
import com.owo.utils.DateTimeHelper;
import com.owo.utils.UtilLog;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;

import com.wao.dogcat.widget.CircleImageView;


import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeDemo;
import cn.sharesdk.system.text.ShortMessage;

/**
 * Created by ppssyyy on 2017-03-22.
 */
public class MapUtil {

//    public static BitmapDescriptor circleGray = BitmapDescriptorFactory.fromAsset("circle_gray.png");
//    public static BitmapDescriptor circleRed = BitmapDescriptorFactory.fromAsset("circle_red.png");
//    public static BitmapDescriptor circleBlue = BitmapDescriptorFactory.fromAsset("circle_blue.png");
//
//    public static BitmapDescriptor circleMan1 = BitmapDescriptorFactory.fromAsset("male1.png");
//    public static BitmapDescriptor circleMan2 = BitmapDescriptorFactory.fromAsset("male2.png");
//    public static BitmapDescriptor circleMan3 = BitmapDescriptorFactory.fromAsset("male3.png");
//    public static BitmapDescriptor circleMan4 = BitmapDescriptorFactory.fromAsset("male4.png");
//    public static BitmapDescriptor circleMan5 = BitmapDescriptorFactory.fromAsset("male5.png");
//    public static BitmapDescriptor circleMan6 = BitmapDescriptorFactory.fromAsset("male6.png");
//    public static BitmapDescriptor circleMan7 = BitmapDescriptorFactory.fromAsset("male7.png");
//
//    public static BitmapDescriptor circleFemale1 = BitmapDescriptorFactory.fromAsset("femal1.png");
//    public static BitmapDescriptor circleFemale2 = BitmapDescriptorFactory.fromAsset("femal2.png");
//    public static BitmapDescriptor circleFemale3 = BitmapDescriptorFactory.fromAsset("femal3.png");
//    public static BitmapDescriptor circleFemale4 = BitmapDescriptorFactory.fromAsset("femal4.png");
//    public static BitmapDescriptor circleFemale5 = BitmapDescriptorFactory.fromAsset("femal5.png");
//    public static BitmapDescriptor circleFemale6 = BitmapDescriptorFactory.fromAsset("femal6.png");
//    public static BitmapDescriptor circleFemale7 = BitmapDescriptorFactory.fromAsset("femal7.png");

    public static String inviteCode;
    public static HashMap<String, Object> locHM = new HashMap<>();
    public static HashMap<String, Object> halfHM = new HashMap<>();

    public static Object data;

    //读取地图样式文件
    public static void setMapCustomStyleFile(Context context) {
        String styleName = "style_json.json";
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        String filePath = null;
        try {
            inputStream = context.getAssets().open(styleName);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            filePath = context.getFilesDir().getAbsolutePath();
            File file = new File(filePath + "/" + styleName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            outputStream.write(b);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();

                if (outputStream != null)
                    outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MapView.setCustomMapStylePath(filePath + "/" + styleName);

    }


//    public static BitmapDescriptor getCircleBitmapByLevel(int level) {
//        switch (level) {
//            case 1:
//            case 2:
//            case 3:
//                return circleMan1;
//            case 4:
//            case 5:
//            case 6:
//                return circleMan2;
//            case 7:
//            case 8:
//            case 9:
//                return circleMan3;
//            case 10:
//            case 11:
//            case 12:
//                return circleMan4;
//            case 13:
//            case 14:
//            case 15:
//                return circleMan5;
//            case 16:
//            case 17:
//            case 18:
//                return circleFemale6;
//            case 19:
//            case 20:
//                return circleFemale7;
//            default:
//                return circleMan1;
//        }
//    }


//    public static BitmapDescriptor getCircleBitmapBySex(int sex, int level) {
//        if (sex == 2) {
//            switch (level) {
//                case 1:
//                case 2:
//                case 3:
//                    return circleFemale1;
//                case 4:
//                case 5:
//                case 6:
//                    return circleFemale2;
//                case 7:
//                case 8:
//                case 9:
//                    return circleFemale3;
//                case 10:
//                case 11:
//                case 12:
//                    return circleFemale4;
//                case 13:
//                case 14:
//                case 15:
//                    return circleFemale5;
//                case 16:
//                case 17:
//                case 18:
//                    return circleFemale6;
//                case 19:
//                case 20:
//                    return circleFemale7;
//                default:
//                    return circleFemale1;
//            }
//        } else {
//            switch (level) {
//                case 1:
//                case 2:
//                case 3:
//                    return circleMan1;
//                case 4:
//                case 5:
//                case 6:
//                    return circleMan2;
//                case 7:
//                case 8:
//                case 9:
//                    return circleMan3;
//                case 10:
//                case 11:
//                case 12:
//                    return circleMan4;
//                case 13:
//                case 14:
//                case 15:
//                    return circleMan5;
//                case 16:
//                case 17:
//                case 18:
//                    return circleMan6;
//                case 19:
//                case 20:
//                    return circleMan7;
//                default:
//                    return circleMan1;
//            }
//        }
//    }

    /**
     * 获取loverID
     *
     * @param context
     */
    public static void getLoverID(Context context) {
        try {
            //获取loverID
            HashMap<String, String> hm = new HashMap<>();
            hm.put("loverAID", Common.userSP.getInt("ID", 0) + "");
            String json =
                    post(MyURL.GET_LID_BY_ID, hm);
            System.out.println("GET_LID_BY_ID:" + json);
            int code = HttpHelper.getCode(json);
            if (code == 200) {
                Common.loverID = HttpHelper.AnalysisData(json).toString();
                HashMap<String, String> loveID = new HashMap<>();
                loveID.put("loverID", Common.loverID);
                HashMap<String, Object> ls = new HashMap<>();
                ls = HttpHelper.AJLS(HttpHelper.postData(MyURL.GET_LS_BY_ID, loveID, null));
                if (ls != null)
                    Common.setLoveShipInfo(ls);

            } else {
                Log.v("MapUtil", "异常:GET_LID_BY_ID " + HttpHelper.getMsg(json));
                //Common.display(context, "异常:GET_LID_BY_ID " + HttpHelper.getMsg(json));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("MapUtil", "服务器异常:GET_LID_BY_ID");
            // Common.display(context, "服务器异常:GET_LID_BY_ID");
        }

    }

    /**
     * 获取圈子信息
     *
     * @param context
     */
    public static void getRecord(Context context, BaiduMap baidumap, int sex) {

        if (Common.loverID.trim().length() != 0) {
            //获取圈子ID
            try {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("loverID", Common.loverID);
                String json =
                        post(MyURL.GET_ID_BY_LID, hm);
                int code = HttpHelper.getCode(json);
                if (code == 200) {
                    Common.circleID = (int) HttpHelper.AnalysisData(json);
                    if (Common.circleID != -1) {
                        HashMap<String, String> hm1 = new HashMap<>();
                        hm1.put("id", Common.circleID + "");
                        String json1 =
                                post(MyURL.GET_RECORD_BY_ID, hm1);
                        int code1 = HttpHelper.getCode(json1);
                        if (code1 == 200) {
                            String jsonInfo = getHalfByID();
                            Common.setHalfInfo(HttpHelper.AJgetHalfByID(jsonInfo)); //设置另一半信息
                            Common.setActivityRecordInfo(HttpHelper.AJgetRecordByID(json1));
                            //显示我的圈子
                            // addMyCircle(Common.activityRecord, baidumap, sex);

                        } else {
                            Log.v("MapUtil", "异常:GET_RECORD_BY_ID " + HttpHelper.getMsg(json));
                            //Common.display(context, "异常:GET_RECORD_BY_ID " + HttpHelper.getMsg(json));
                        }
                    }
                } else {
                    Log.v("MapUtil", "异常:GET_ID_BY_LID " + HttpHelper.getMsg(json));
                    //Common.display(context, "异常:GET_ID_BY_LID " + HttpHelper.getMsg(json));
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.v("MapUtil", "服务器异常,无法获取情侣圈信息");
                // Common.display(context, "服务器异常,无法获取情侣圈信息");
            }

        }
    }



//    public static void addMyCircle(BaiduMap baiduMap, int sex, double latitude, double longtitude) {
//        if (latitude != 0 && longtitude != 0) {
//            LatLng latLng = new LatLng(latitude, longtitude);
//            OverlayOptions overlayOptions = new GroundOverlayOptions().position(latLng).image(getCircleBitmapByLevel(0))
//                    .dimensions(300).zIndex(4);
//            baiduMap.addOverlay(overlayOptions);
//        }
//
//    }







    /**
     * 定位到指定位置
     */
    public static void toLocation(double longtitude, double latitude, BaiduMap baidumap, float zoomLevel) {
        if (longtitude != 0 && latitude != 0) {
            LatLng ll = new LatLng(latitude,
                    longtitude);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(zoomLevel);
            //设置我的位置为地图的中心点
            baidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }

    }

    //跳转相机动态页面
    public static void viewMomentCamInfo(BaiduMap baiduMap, Marker marker, final HashMap<String, Object> camHM, final Context context) {
        InfoWindow mInfoWindow;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pic_infowindow, null);
        CircleImageView head;
        LinearLayout infowindowll;
        ImageView image = (ImageView) view.findViewById(R.id.momentImage);
        head = (CircleImageView) view.findViewById(R.id.head);
        TextView momentContent = (TextView) view.findViewById(R.id.momentContent);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView date = (TextView) view.findViewById(R.id.momentDate);

        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my)
                .showImageOnFail(R.drawable.my)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                MyURL.ROOT + camHM.get("avatar").toString(), head, options);



        if ( camHM.get("photo")!=null) {
            DisplayImageOptions opt = new DisplayImageOptions.Builder()//
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                    .showImageForEmptyUri(R.drawable.head)
                    .showImageOnFail(R.drawable.head)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();


            com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                    displayImage(
                            camHM.get("photo").toString().charAt(0) == 'P' ?
                                    MyURL.ROOT + camHM.get("photo").toString() : camHM.get("photo").toString(), image, opt);

        }
        String formatDate = "";

        if (camHM.get("publishDate").toString().length() != 0) {
            formatDate = DateTimeHelper.timeLogic(DateTimeHelper
                            .timeMillis2FormatTime(camHM.get("publishDate").toString()
                                    , DateTimeHelper.DATE_FORMAT_TILL_SECOND),
                    DateTimeHelper.DATE_FORMAT_TILL_SECOND);
        }
        momentContent.setText(camHM.get("content").toString());
        name.setText(camHM.get("userName").toString());
        date.setText(formatDate);

        infowindowll = (LinearLayout) view.findViewById(R.id.infowindowll);
        infowindowll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Activity activity = (Activity) context;
//                Intent intent = new Intent();
//                intent.putExtra("extra", camHM.get("loveAID").toString());
//                intent.setClass(context, CoupleUserActivity.class);
//                activity.startActivity(intent);
//                activity.finish();
            }
        });


        //将marker所在的经纬度的信息转化成屏幕上的坐标
        final LatLng ll = marker.getPosition();
        Point p = baiduMap.getProjection().toScreenLocation(ll);
        Log.e("Marker", "--!" + p.x + " , " + p.y);
        p.y -= 47;
        LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);

        mInfoWindow = new InfoWindow(view, llInfo, 0);
        //显示InfoWindow
        baiduMap.showInfoWindow(mInfoWindow);
    }


    //跳转纸条动态页面
    public static void viewMomentTxtInfo(BaiduMap baiduMap, Marker marker, final HashMap<String, Object> txtHM, final Context context) {
        InfoWindow mInfoWindow;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.txt_infowindow, null);
        CircleImageView head;
        LinearLayout infowindowll;
        head = (CircleImageView) view.findViewById(R.id.head);
        TextView momentContent = (TextView) view.findViewById(R.id.momentContent);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView date = (TextView) view.findViewById(R.id.momentDate);

        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my)
                .showImageOnFail(R.drawable.my)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                MyURL.ROOT + txtHM.get("avatar").toString(), head, options);

        String formatDate = "";

        if (txtHM.get("publishDate").toString().length() != 0) {
            formatDate = DateTimeHelper.timeLogic(DateTimeHelper
                            .timeMillis2FormatTime(txtHM.get("publishDate").toString()
                                    , DateTimeHelper.DATE_FORMAT_TILL_SECOND),
                    DateTimeHelper.DATE_FORMAT_TILL_SECOND);
        }
        momentContent.setText(txtHM.get("text").toString());
        name.setText(txtHM.get("userName").toString());
        date.setText(formatDate);

        infowindowll = (LinearLayout) view.findViewById(R.id.infowindowll);
        infowindowll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Activity activity = (Activity) context;
//                Intent intent = new Intent();
//                intent.putExtra("extra", camHM.get("loveAID").toString());
//                intent.setClass(context, CoupleUserActivity.class);
//                activity.startActivity(intent);
//                activity.finish();
            }
        });


        //将marker所在的经纬度的信息转化成屏幕上的坐标
        final LatLng ll = marker.getPosition();
        Point p = baiduMap.getProjection().toScreenLocation(ll);
        Log.e("Marker", "--!" + p.x + " , " + p.y);
        p.y -= 47;
        LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);

        mInfoWindow = new InfoWindow(view, llInfo, 0);
        //显示InfoWindow
        baiduMap.showInfoWindow(mInfoWindow);
    }

    //跳转活动页面
    public static void viewActInfo(BaiduMap baiduMap, Marker marker, final BeanTask task, final Context context) {
        InfoWindow mInfoWindow;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act_infowindow, null);
        CircleImageView head;
        RelativeLayout infowindowll;
        head = (CircleImageView) view.findViewById(R.id.head);
        TextView actTitle = (TextView) view.findViewById(R.id.actTitle);
//        TextView date = (TextView) view.findViewById(R.id.date);
//        TextView location = (TextView) view.findViewById(R.id.location);
//        TextView people = (TextView) view.findViewById(R.id.people);

        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my)
                .showImageOnFail(R.drawable.my)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                MyURL.ROOT + task.getAvatar(), head, options);

//        String formatDate = "";
//
//        if (task.getTaskPublishTime().toString().length() != 0) {
//            formatDate = DateTimeHelper.timeLogic(DateTimeHelper
//                            .timeMillis2FormatTime(task.getTaskPublishTime().toString()
//                                    , DateTimeHelper.DATE_FORMAT_TILL_SECOND),
//                    DateTimeHelper.DATE_FORMAT_TILL_SECOND);
//        }
//
        actTitle.setText(task.getTaskName());
//        String locStr = "";
//        try {
//            locStr =
//            HttpHelper.getLocation(
//                    HttpHelper.sendGet(com.owo.utils.util_http.MyURL.BAIDU_MAP_URL+"&location="
//                            +task.getTaskLatitude()+","+task.getTaskLongitude(),null));
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//        location.setText(locStr);
//
//        people.setText(task.getTaskTakenNum()+"/"+task.getTaskMaxNum()+"(尚未满人)");
//        date.setText(formatDate);
//
        infowindowll = (RelativeLayout) view.findViewById(R.id.infowindowll);
        infowindowll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (task.getTaskType()==1)
                    //线上
                    AtyDetail.startAtyDetail(context, AtyDetail.class, task,AtyDetail.VALUE_TYPE_ONLINEATYDETAIL);
                    else{
                    if (task.getTaskStatus() == 3)
                        AtyDetail.startAtyDetail(context, AtyDetail.class, task,AtyDetail.VALUE_TYPE_VIEWATYCMT);
                    else {
                        AtyDetail.startAtyDetail(context, AtyDetail.class, task, AtyDetail.VALUE_TYPE_ATYDETAIL);
                    }
                }

            }
        });


        //将marker所在的经纬度的信息转化成屏幕上的坐标
        final LatLng ll = marker.getPosition();
        Point p = baiduMap.getProjection().toScreenLocation(ll);
        Log.e("Marker", "--!" + p.x + " , " + p.y);
        p.y -= 47;
        LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);

        mInfoWindow = new InfoWindow(view, llInfo, 0);
        //显示InfoWindow
        baiduMap.showInfoWindow(mInfoWindow);
    }

    //跳转至用户个人信息页面
    public static void viewUserInfo(BaiduMap baiduMap, Marker marker, final HashMap<String, Object> circleHM, final Context context) {
        InfoWindow mInfoWindow;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.couple_infowindow, null);
        CircleImageView headA, headB;
        LinearLayout infowindowll;
        ImageView backImage;
        headA = (CircleImageView) view.findViewById(R.id.headA);
        headB = (CircleImageView) view.findViewById(R.id.headB);
        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my)
                .showImageOnFail(R.drawable.my)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                MyURL.ROOT + circleHM.get("avatarA").toString(), headA, options);

        DisplayImageOptions opt = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my)
                .showImageOnFail(R.drawable.my)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                MyURL.ROOT + circleHM.get("avatarB").toString(), headB, opt);

        backImage = (ImageView) view.findViewById(R.id.bgImage);
        ////背景图///
        DisplayImageOptions o = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.couple_bg)
                .showImageOnFail(R.drawable.couple_bg)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                MyURL.ROOT + circleHM.get("backImageA").toString(), backImage, o);


        infowindowll = (LinearLayout) view.findViewById(R.id.infowindowll);
        infowindowll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Activity activity = (Activity) context;
//
//                Intent intent = new Intent();
//                intent.putExtra("extra", circleHM.get("loveAID").toString());
//                intent.setClass(context, CoupleUserActivity.class);
//                activity.startActivity(intent);
//                activity.finish();
            }
        });


        //将marker所在的经纬度的信息转化成屏幕上的坐标
        final LatLng ll = marker.getPosition();
        Point p = baiduMap.getProjection().toScreenLocation(ll);
        Log.e("Marker", "--!" + p.x + " , " + p.y);
        p.y -= 47;
        LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);

        mInfoWindow = new InfoWindow(view, llInfo, 0);
        //显示InfoWindow
        baiduMap.showInfoWindow(mInfoWindow);
    }


    //////////////////////////post数据///////////////////////
    public static String getTexts(boolean isGetMy) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("userID", Common.userID + "");
        String result = "";
        if (isGetMy)
            result = HttpHelper.postData(MyURL.GET_TEXTS_BY_UID, paramHM, null);
        else result = HttpHelper.postData(MyURL.GET_TEXTS_EXCEPT_ONE, paramHM, null);
        return result;
    }

    public static String getPhotos(boolean isGetMy) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("userID", Common.userID + "");
        String result = "";
        if (isGetMy)
            result = HttpHelper.postData(MyURL.GET_PHOTOS_BY_UID, paramHM, null);
        else result = HttpHelper.postData(MyURL.GET_PHOTOS_EXCEPT_ONE, paramHM, null);
        return result;
    }

    public static String post(String url, HashMap<String, String> paramHM) throws Exception {
        return HttpHelper.postData(url, paramHM, null);
    }

    public static String getAllRecords() throws Exception {
        String result = HttpHelper.postData(MyURL.GET_ALL_RECORDS, null, null);
        return result;
    }

    public static String getAllTreasures() throws Exception {
        String result = HttpHelper.postData(MyURL.GET_ALL_TREASURES, null, null);
        return result;
    }

    public static String getUserByID(String id) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("id", id);
        String result = HttpHelper.postData(MyURL.GET_USER_BY_ID, paramHM, null);
        return result;
    }

    public static String postData(String path) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("id", Common.userID + "");
        HashMap<String, String> fileHM = new HashMap<>();
        fileHM.put("avatar", path);
        System.out.println(paramHM + " " + fileHM);
        return HttpHelper.postData(MyURL.UPDATE_AVATAR_BY_ID, paramHM, fileHM);
    }

    public static String postData(String url, String key, String value) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("id", Common.userID + "");
        paramHM.put(key, value);
        return HttpHelper.postData(url, paramHM, null);
    }

    public static String getUserByInviteCode(String inviteCode) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("inviteCode", inviteCode);
        paramHM.put("id", Common.userSP.getInt("ID", 0) + "");
        String result = HttpHelper.postData(MyURL.GET_USER_BY_INVITE_CODE, paramHM, null);
        return result;
    }

    public static String getHalfByID() throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("loverAID", Common.userSP.getInt("ID", 0) + "");
        String result = HttpHelper.postData(MyURL.GET_HALF_BY_ID, paramHM, null);
        return result;
    }

    //搬家
    public static String postLocation(String longtitude, String latitude) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("id", Common.userID + "");
        paramHM.put("longtitude", longtitude);
        paramHM.put("latitude", latitude);
        return HttpHelper.postData(MyURL.UPDATE_LOCATION_BY_ID, paramHM, null);
    }
    //////////////////////////END post数据///////////////////////

    //添加地图上的所有circle
//    public static void addCircle(BaiduMap baiduMap, int sex,List<HashMap<String, Object>>
//            circleLists) {
//        try {
//            // System.out.println("888888888888888888"+circleLists);
//            if (circleLists != null && circleLists.size() != 0) {
//                LatLng latLng = null;
//                OverlayOptions overlayOptions = null;
//                OverlayOptions op = null;
//                Marker marker = null;
//                for (int i = 0; i < circleLists.size(); i++) {
//                    if (circleLists.get(i).get("latitude").toString().length() != 0 &&
//                            circleLists.get(i).get("longtitude").toString().length() != 0 &&
//                            (int) circleLists.get(i).get("radius") != 0) {
//                        latLng = new LatLng(Double.parseDouble(circleLists.get(i).get("latitude").toString()),
//                                (Double.parseDouble(circleLists.get(i).get("longtitude").toString())));
//                        overlayOptions = new GroundOverlayOptions().position(latLng).image(getCircleBitmapByLevel(
//                                Common.getLevelByR((int) circleLists.get(i).get("radius"))))
//                                .dimensions((int) circleLists.get(i).get("radius")).zIndex(4);
//                        op = new MarkerOptions().position(latLng)
//                                .icon(BitmapDescriptorFactory// 构建mark图标
//                                        .fromResource(Common.getImageResourceByLevel(
//                                                Common.getLevelByR((int) circleLists.get(i).get("radius"))
//                                        ))).zIndex(5);
//                        marker = (Marker) (baiduMap.addOverlay(op));
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("circleInfo", circleLists.get(i));
//                        marker.setExtraInfo(bundle);
//                        marker.setTitle("circle");
//                        baiduMap.addOverlay(overlayOptions);
//                    }
//                }
//            }
//        } catch (Exception ee) {
//            ee.printStackTrace();
//        }
//
//
//    }

    //添加地图上的所有宝藏
    public static void addTreasure(BaiduMap baiduMap,List<HashMap<String, Object>> treasureLists) {
        try {
            if (treasureLists != null && treasureLists.size() != 0) {
                LatLng latLng = null;
                OverlayOptions op = null;
                Marker marker = null;

                for (int i = 0; i < treasureLists.size(); i++) {
                    if (treasureLists.get(i).get("latitude").toString().length() != 0 &&
                            treasureLists.get(i).get("longtitude").toString().length() != 0) {
                        latLng = new LatLng(Double.parseDouble(treasureLists.get(i).get("latitude").toString()),
                                (Double.parseDouble(treasureLists.get(i).get("longtitude").toString())));
                        op = new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory// 构建mark图标
                                        .fromResource(R.drawable.treasure_chest)).zIndex(5);
                        marker = (Marker) (baiduMap.addOverlay(op));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("treasureInfo", treasureLists.get(i));
                        marker.setExtraInfo(bundle);
                        marker.setTitle("treasure");
                    }
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }


    }

    //添加地图上的所有相机
    public static void addCam(BaiduMap baiduMap, int sex, int status, List<HashMap<String, Object>>
            camLists) {
        try {
            if (camLists != null && camLists.size() != 0) {
                LatLng latLng = null;
                OverlayOptions op = null;
                Marker marker = null;

                for (int i = 0; i < camLists.size(); i++) {
                    if (camLists.get(i).get("latitude").toString().length() != 0 &&
                            camLists.get(i).get("longtitude").toString().length() != 0) {
                        latLng = new LatLng(Double.parseDouble(camLists.get(i).get("latitude").toString()),
                                (Double.parseDouble(camLists.get(i).get("longtitude").toString())));
                            if (Integer.parseInt(camLists.get(i).get("isMe").toString()) == 1) {
                                op = new MarkerOptions().position(latLng)
                                        .icon(BitmapDescriptorFactory// 构建mark图标
                                                .fromResource(R.drawable.map_xiangji)).zIndex(5);
                            } else {
                                op = new MarkerOptions().position(latLng)
                                        .icon(BitmapDescriptorFactory// 构建mark图标
                                                .fromResource(R.drawable.map_xiangji)).zIndex(5);
                            }

                        marker = (Marker) (baiduMap.addOverlay(op));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("camInfo", camLists.get(i));
                        marker.setExtraInfo(bundle);
                        marker.setTitle("camera");
                    }
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }


    }

    //添加地图上的所有小纸条
    public static void addTxt(BaiduMap baiduMap, int sex, int status, List<HashMap<String, Object>>
            txtLists ) {
        try {
            if (txtLists != null && txtLists.size() != 0) {
                LatLng latLng = null;
                OverlayOptions op = null;
                Marker marker = null;

                for (int i = 0; i < txtLists.size(); i++) {
                    if (txtLists.get(i).get("latitude").toString().length() != 0 &&
                            txtLists.get(i).get("longtitude").toString().length() != 0) {
                        latLng = new LatLng(Double.parseDouble(txtLists.get(i).get("latitude").toString()),
                                (Double.parseDouble(txtLists.get(i).get("longtitude").toString())));
                            if (Integer.parseInt(txtLists.get(i).get("isMe").toString()) == 1) {
                                op = new MarkerOptions().position(latLng)
                                        .icon(BitmapDescriptorFactory// 构建mark图标
                                                .fromResource(R.drawable.map_xiangji)).zIndex(5);
                            } else op = new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory// 构建mark图标
                                            .fromResource(R.drawable.map_xiangji)).zIndex(5);

                        marker = (Marker) (baiduMap.addOverlay(op));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("txtInfo", txtLists.get(i));
                        marker.setExtraInfo(bundle);
                        marker.setTitle("text");
                    }
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }


    }


    //添加地图上的活动
    //Context 是自己后来加上的
    public static ClusterManager addAct(Context context,BaiduMap baiduMap, List<BeanTask>
            txtLists ) {
        MarkerClusterDemo markerClusterDemo = null;
        ClusterManager<MyItem> clusterManager = null;
        try {
            UtilLog.e("FrageHomeNormaladd","进入addAct"+context);
            UtilLog.e("FrageHomeNormaladd","进入addAct");
            markerClusterDemo = MarkerClusterDemo.getSingleton();
            markerClusterDemo.init(context,baiduMap);
            markerClusterDemo.updataMap();
            clusterManager = markerClusterDemo.getClusterManager();
            if (clusterManager==null){
                UtilLog.e("FrageHomeNormaladd","clusterManager为空");
            }else {
                UtilLog.e("FrageHomeNormaladd","clusterManager不为空");
            }
            UtilLog.e("FrageHomeNormaladd"," "+txtLists.size());
            if (txtLists != null && txtLists.size() != 0) {

                LatLng latLng = null;
                OverlayOptions op = null;
                Marker marker = null;
                List<MyItem> items = new ArrayList<MyItem>();

                for (int i = 0; i < txtLists.size(); i++) {
                    if (txtLists.get(i).getTaskLatitude().trim().length() != 0 &&
                            txtLists.get(i).getTaskLongitude().trim().length() != 0) {

                        BeanTask beanTask= txtLists.get(i);
                        latLng = new LatLng(Double.parseDouble(beanTask.getTaskLatitude()),
                                (Double.parseDouble(beanTask.getTaskLongitude())));
                        int taskType = beanTask.getTaskType();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("actInfo", txtLists.get(i));
                        bundle.putString("actOwn","act");

                       /* marker = (Marker) (mBaiduMap.addOverlay(op));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("actInfo", txtLists.get(i));
                        marker.setExtraInfo(bundle);
                        marker.setTitle("act");*/

                        items.add(new MyItem(latLng,taskType,bundle));
                    }
                }
                clusterManager.addItems(items);
                //clusterManager.onMapStatusChange(baiduMap.getMapStatus());
                //markerClusterDemo.updataMap();
                UtilLog.e("FrageHomeNormaladd","add完毕");
            }
           /* UtilLog.e("FrageHomeNormaladd"," "+txtLists.size());
            if (txtLists != null && txtLists.size() != 0) {

                //   这是本来没有聚合功能的实现
                LatLng latLng = null;
                OverlayOptions op = null;
                Marker marker = null;


                for (int i = 0; i < txtLists.size(); i++) {
                    if (txtLists.get(i).getTaskLatitude().trim().length() != 0 &&
                            txtLists.get(i).getTaskLongitude().trim().length() != 0) {
                        latLng = new LatLng(Double.parseDouble(txtLists.get(i).getTaskLatitude()),
                                (Double.parseDouble(txtLists.get(i).getTaskLongitude())));



                        //.perspective(true) 开启近大远小
                        if ( txtLists.get(i).getTaskType()==2) {
                            op = new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory// 构建mark图标
                                            .fromResource(R.drawable.map_reality)).zIndex(5);
                        } else if ( txtLists.get(i).getTaskType()==1) {
                            op = new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory// 构建mark图标
                                            .fromResource(R.drawable.map_online)).zIndex(5);
                        } else if ( txtLists.get(i).getTaskType()==3){
                            op = new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory// 构建mark图标
                                            .fromResource(R.drawable.map_immediately)).zIndex(5);
                        }

                        marker = (Marker) (baiduMap.addOverlay(op));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("actInfo", txtLists.get(i));
                        marker.setExtraInfo(bundle);
                        marker.setTitle("act");
                    }
                }
            }*/

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return clusterManager;
    }


    //埋宝
    public static void insertTreasure(BaiduMap baiduMap, LatLng latLng) {
        OverlayOptions op = null;
        Marker marker = null;
        if (latLng.longitude != 0 &&
                latLng.latitude != 0) {
            op = new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory// 构建mark图标
                            .fromResource(R.drawable.treasure_chest)).zIndex(5);
            marker = (Marker) (baiduMap.addOverlay(op));

            //marker.setExtraInfo(bundle);
            marker.setTitle("myTreasure");
        }

    }

    //添加我的相机
    public static void insertMyCam(BaiduMap baiduMap, LatLng latLng, int sex, int status,Bundle bundle) {
        OverlayOptions op = null;
        Marker marker = null;
        if (latLng.longitude != 0 &&
                latLng.latitude != 0) {

                op = new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory// 构建mark图标
                                .fromResource(R.drawable.map_xiangji)).zIndex(5);

            marker = (Marker) (baiduMap.addOverlay(op));
            marker.setExtraInfo(bundle);
            marker.setTitle("myCamera");
        }

    }

    //添加我的小纸条
    public static void insertMyTxt(BaiduMap baiduMap, LatLng latLng, int sex, int status,Bundle bundle) {
        OverlayOptions op = null;
        Marker marker = null;
        if (latLng.longitude != 0 &&
                latLng.latitude != 0) {
                op = new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory// 构建mark图标
                                .fromResource(R.drawable.map_xiangji)).zIndex(5);

            marker = (Marker) (baiduMap.addOverlay(op));
            marker.setExtraInfo(bundle);
            marker.setTitle("myText");
        }

    }


    //添加我的活动
    public static void insertMyAct(BaiduMap baiduMap, LatLng latLng,Bundle bundle) {
        OverlayOptions op = null;
        Marker marker = null;
        HashMap<String, Object> hm = (HashMap<String, Object>) bundle.getSerializable("myActInfo");
        if (latLng.longitude != 0 &&
                latLng.latitude != 0) {
            op = new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory// 构建mark图标
                            .fromResource(R.drawable.map_xiangji)).zIndex(5);
            marker = (Marker) (baiduMap.addOverlay(op));
            marker.setExtraInfo(bundle);
            marker.setTitle("act");
        }

    }


    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    MapUtil.addMyCircle((BaiduMap) msg.obj, msg.arg1, Double.parseDouble(locHM.get("latitude").toString()),
//                            Double.parseDouble(locHM.get("longtitude").toString()));
                    break;

            }
        }
    };

    public static void showShare(/*boolean silent, String platform, */Context context, String inviteCode) {
//        final OnekeyShare oks = new OnekeyShare();
//        //不同平台的分享参数，请看文档
//        String text = "http://www.mob.com";
//        oks.setTitle("share title");
//        oks.setText(text);
//        //oks.setSilent(silent);
//        oks.disableSSOWhenAuthorize();
//        if (platform != null) {
//            oks.setPlatform(platform);
//        }
//
//        oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo(inviteCode));
//        oks.show(context);
        Uri smsToUri = Uri.parse("smsto:");// 联系人地址
        Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO,
                smsToUri);
        mIntent.putExtra("sms_body",
                "我邀请你一起使用情侣应用OWO，快来下载和我一起玩吧~ http://dwz.cn/OWO_yyb 邀请码："+inviteCode);// 短信内容
        context.startActivity(mIntent);
    }


}




