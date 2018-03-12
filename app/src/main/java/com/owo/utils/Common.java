package com.owo.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import android.Manifest;

import com.owo.module_b_personal.bean.BeanUser;
import com.wao.dogcat.R;
import com.owo.model.ActivityRecord;
import com.owo.model.LoveShip;
import com.owo.model.PosLib;
import com.owo.model.User;
import com.owo.model.UserPose;
import com.wao.dogcat.widget.GenericProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 公共工具
 */
public class Common {

    //    AppID:10081596
//    SecretID:AKIDd5HEwNHjfLlvoSnyrd9PFrQjmtkMJpgH
//    SecretKey:SebI2VsymJkuoSr0sUt63lERO4UeYCZh
    public static final String APP_ID = "10081596";
    public static final String SECRET_ID = "AKIDd5HEwNHjfLlvoSnyrd9PFrQjmtkMJpgH";
    public static final String SECRET_KEY = "SebI2VsymJkuoSr0sUt63lERO4UeYCZh";

    public static final String[] SELF_POS_TYPE = {"女神自拍", "潮男自拍"};
    public static final String[] POS_TYPE = {"美女", "帅哥", "情侣", "集体", "小孩"};

    public static UserPose userPose;
    public static int userIdPose = -1;
    public static PosLib pos;
    public static boolean isVisible;

    public static User user;
    public static int userID = -1;
    public static LoveShip loveShip;
    public static String loverID = "";

    public static int halfId = -1;
    public static User half;

    public static int status = -1;
    public static int circleID = -1;
    public static ActivityRecord activityRecord;

    public static double myLatitude;
    public static double myLongtitude;


    public static double picLatitude;
    public static double picLongtitude;


    public static final String IMG_CACHE_PATH = "/YUP/";
    public static final String FULL_IMG_CACHE_PATH = "/mnt/sdcard"+IMG_CACHE_PATH;
    public static String local_pic_path = Environment.getExternalStorageDirectory() +
            "/Android/data/com.wao.dogcat/cache/imageCache/";
    public static AlertDialog mAlertDialog;
    public static SharedPreferences userSP;


    public static int finishStatus = -1;
    public static final int CHOOSE_SEX = 10;
    public static final int COMPLETE = 11;
    public static final int CHOOSE = 12;
    public static final int INVITE = 13;
    public static final int INVITED = 14;
    public static final int WAITING = 15;
    public static final int MATCHED = 16;
    public static final int SUCCESS = 17;

    public static final int CAM = 10;
    public static final int TXT = 11;
    public static final int CAPSULE = 12;
    public static final int MOVE = 13;
    public static final int GO_DIE = 14;
    public static final int LETS_GO = 15;
    public static final int DOODLE = 16;
    public static final int POS_CAM = 18;


    public static int screenWidth;
    public static int screenHeight;
    public static int visibleHeight;

    public static String fragParamName;
    public static String fragParam;
    public static Bitmap bitmap;
    public static String picPath;
    public static boolean isFirstGoDie = true;
    public static boolean isCamera = false;


    public static final String GO_DIE_URL = "http://wx2.sinaimg.cn/mw690/8429b3bdly1feaxq8cwjej205a03g0sq";
    public static final String BIG_GOLD = "http://wx3.sinaimg.cn/mw690/8429b3bdly1feaxq8hvj7j201w01w3yb";
    public static final String MOVE_HOUSE_URL = "http://wx2.sinaimg.cn/mw690/8429b3bdly1feaxq8oy7dj203e03oglh";
    public static final String LOVE_CAPSULE_URL = "http://wx3.sinaimg.cn/mw690/8429b3bdly1feaxq8uc8gj203e044dfs";
    public static final String SYSTEM_MSG_URL = "http://ww1.sinaimg.cn/large/0060lm7Tgy1feay01d93dj303c03c3ya";
    public static final String LETS_DOG_URL = "http://ww3.sinaimg.cn/large/0060lm7Tgy1feay6ke691j304g04g3yk";

    public static int selectLabelState;
    public static final int SELECT_SELF_LABEL = 1;
    public static final int SELECT_FIND_LABEL = 2;

    public static int addNum = 0;
    public static void display(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    //////////////UI组件
    public static void showProgressDialog(final String msg, final Context context) {
        final Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!activity.isFinishing()) {
                    if (mAlertDialog == null) {
                        mAlertDialog = new GenericProgressDialog(context);
                    }
                    mAlertDialog.setMessage(msg);
                    ((GenericProgressDialog) mAlertDialog)
                            .setProgressVisiable(true);
                    mAlertDialog.setCancelable(false);
                    mAlertDialog.setOnCancelListener(null);
                    mAlertDialog.show();
                    mAlertDialog.setCanceledOnTouchOutside(false);
                }
            }
        });
    }

    public static void dismissProgressDialog(final Context context) {
        final Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAlertDialog != null && mAlertDialog.isShowing()
                        && !activity.isFinishing()) {
                    mAlertDialog.dismiss();
                    mAlertDialog = null;
                }
            }
        });
    }

    //////////////权限检查
    public static boolean isKitkatWithStepSensor(Context context) {
        // BEGIN_INCLUDE(iskitkatsensor)
        // Require at least Android KitKat
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        // Check that the device supports the step counter and detector sensors
        PackageManager packageManager = context.getPackageManager();
        return currentApiVersion >= android.os.Build.VERSION_CODES.KITKAT
                && packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
                && packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR);
        // END_INCLUDE(iskitkatsensor)
    }

    public static void locationPermission(Activity context) {

        //Android 6.0判断用户是否授予定位权限
        //if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.M)

        // {//如果 API level 是大于等于 23(Android 6.0) 时
        //判断是否具有权限
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //判断是否需要向用户解释为什么需要申请该权限
//            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
//                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                Toast.makeText(context, "自Android 6.0开始需要打开位置权限", Toast.LENGTH_SHORT).show();
//            }
            //请求权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        // }

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) {
            return false;
        } else {
            return true;
        }

    }

    //////////////实用工具

    /**
     * 创建指定数量的随机字符串
     *
     * @param numberFlag 是否是数字
     * @param length
     * @return
     */
    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

    public static int getImageResourceByNum(int num) {
        switch (num) {
            case 0:
                return R.drawable.num_0;
            case 1:
                return R.drawable.num_1;
            case 2:
                return R.drawable.num_2;
            case 3:
                return R.drawable.num_3;
            case 4:
                return R.drawable.num_4;
            case 5:
                return R.drawable.num_5;
            case 6:
                return R.drawable.num_6;
            case 7:
                return R.drawable.num_7;
            case 8:
                return R.drawable.num_8;
            case 9:
                return R.drawable.num_9;
            default:
                return R.drawable.num_0;
        }
    }

    public static int getRByLevel(int level) {
        int R = 300;
        return R + (level - 1) * 200;
    }

    public static int getLevelByR(int R) {
        return (R - 100) / 200;
    }

    public static int getCircleClassByR(int R) {
        if (300 <= R && R <= (300 + 2 * 200)) {
            return 1;
        } else if (R >= (300 + 3 * 200) && R <= (300 + 5 * 200)) {
            return 2;
        } else if (R >= (300 + 6 * 200) && R <= (300 + 8 * 200)) {
            return 3;
        } else if (R >= (300 + 9 * 200) && R <= (300 + 10 * 200)) {
            return 4;
        } else if (R >= (300 + 11 * 200) && R <= (300 + 14 * 200)) {
            return 5;
        } else if (R >= (300 + 15 * 200) && R <= (300 + 17 * 200)) {
            return 6;
        } else if (R >= (300 + 18 * 200) && R <= (300 + 19 * 200)) {
            return 7;
        } else return 0;

    }

    public static float getZoomLevelByR(int R) {
        return (float) (18 - getLevelByR(R) * 0.2);
    }

    public static int getImageResourceByLevel(int level) {
        switch (level) {
            case 1:
                return R.drawable.lv1;
            case 2:
                return R.drawable.lv2;
            case 3:
                return R.drawable.lv3;
            case 4:
                return R.drawable.lv4;
            case 5:
                return R.drawable.lv5;
            case 6:
                return R.drawable.lv6;
            case 7:
                return R.drawable.lv7;
            case 8:
                return R.drawable.lv8;
            case 9:
                return R.drawable.lv9;
            case 10:
                return R.drawable.lv10;
            case 11:
                return R.drawable.lv11;
            case 12:
                return R.drawable.lv12;
            case 13:
                return R.drawable.lv13;
            case 14:
                return R.drawable.lv14;
            case 15:
                return R.drawable.lv15;
            case 16:
                return R.drawable.lv16;
            case 17:
                return R.drawable.lv17;
            case 18:
                return R.drawable.lv18;
            case 19:
                return R.drawable.lv19;
            case 20:
                return R.drawable.lv20;
            default:
                return R.drawable.lv1;
        }
    }

    ////////////////////////////////////////
    public static String ItemType2Str(int type) {
        String typeStr = "";
        switch (type) {
            case 1:
                typeStr = "记录类";
                break;
            case 2:
                typeStr = "功能类";
                break;
            case 3:
                typeStr = "恶作剧";
                break;
        }
        return typeStr;
    }

    public static String ItemPrivilege2Str(int privilege) {
        String pStr = "";
        switch (privilege) {
            case 1:
                pStr = "通用";
                break;
            case 2:
                pStr = "情侣";
                break;
            case 3:
                pStr = "单身";
                break;
        }
        return pStr;
    }
    ///////////////////////////////////////

    /**
     * 判断服务是否后台运行
     *
     * @param mContext  Context
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRun(Context mContext, String className) {
        boolean isRun = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(40);
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }

    ///////////////数据赋值
    public static void setLoveShipInfo(HashMap<String, Object> lsHM) {
        loveShip = new LoveShip();

//        loveShip.setAvatarB(lsHM.get("girlavatar").toString());
//        loveShip.setNameB(lsHM.get("girlname").toString());
        loveShip.setLoveTime(lsHM.get("lovetime").toString());
//        loveShip.setAvatarA(lsHM.get("boyavatar").toString());
//        loveShip.setNameA(lsHM.get("boyname").toString());
        loveShip.setLoveIndex(lsHM.get("loveindex").toString());
        loveShip.setLoverAID((int) lsHM.get("loverboyid"));
        loveShip.setLoverBID((int) lsHM.get("lovergirlid"));
        loveShip.setState((int) lsHM.get("state"));
        loveShip.setLoverID(lsHM.get("loverid").toString());


    }

    public static void setActivityRecordInfo(HashMap<String, Object> arHM) {
        activityRecord = new ActivityRecord();
        activityRecord.setId((int) arHM.get("id"));
        activityRecord.setLatitude(arHM.get("latitude").toString());
        activityRecord.setLongtitude(arHM.get("longtitude").toString());
        activityRecord.setLoverID(arHM.get("loverid").toString());
        activityRecord.setTotalSteps((int) arHM.get("totalsteps"));
        activityRecord.setRadius((int) arHM.get("radius"));
    }

    public static void setHalfInfo(HashMap<String, Object> halfHM) {
        half = new User();
        half.setId((int) halfHM.get("id"));
        half.setAvatar(halfHM.get("avatar").toString());
        half.setUserName(halfHM.get("username").toString());
        half.setLatitude(halfHM.get("latitude").toString());
        half.setLongtitude(halfHM.get("longtitude").toString());
        half.setSex((halfHM.get("sex").toString()));
        half.setStepsToday((int) halfHM.get("stepstoday"));
        half.setCheckinDays((int) halfHM.get("checkindays"));
    }

    public static void setUserInfo(HashMap<String, Object> userHM) {
        user = new User();
        user.setPhoneNumber(userHM.get("phonenumber").toString());
        user.setUserName(userHM.get("username").toString());
        user.setId((int) userHM.get("id"));
        user.setPassWord(userHM.get("password").toString());
        user.setAge((int) userHM.get("age"));
        user.setAvatar(userHM.get("avatar").toString());
        user.setUserName(userHM.get("username").toString());
        user.setCheckinDays((int) userHM.get("checkindays"));
        user.setExp((int) userHM.get("exp"));
        user.setBackImage(userHM.get("backimage").toString());
        user.setWexID(userHM.get("wexid").toString());
        user.setQq(userHM.get("qq").toString());
        user.setWeight((int) userHM.get("weight"));
        user.setHeight((int) userHM.get("height"));
        user.setStepsToday((int) userHM.get("stepstoday"));
        user.setLatitude(userHM.get("latitude").toString());
        user.setLongtitude(userHM.get("longtitude").toString());
        user.setHobby(userHM.get("hobby").toString());
        user.setSignature(userHM.get("signature").toString());
        user.setSex((userHM.get("sex").toString()));
        user.setLevel((int) userHM.get("level"));
        user.setInviteCode(userHM.get("invitecode").toString());
        user.setStatus((int) userHM.get("status"));
        user.setMoney((int) userHM.get("money"));
        user.setRegDate(userHM.get("regdate").toString());

    }

    public static BeanUser getBeanUser(HashMap<String, Object> userHM) {
        BeanUser user = new BeanUser();
        user.setPhoneNumber(userHM.get("phonenumber").toString());
        user.setUserName(userHM.get("username").toString());
        user.setId((int) userHM.get("id"));
        user.setPassWord(userHM.get("password").toString());
        user.setAge((int) userHM.get("age"));
        user.setAvatar(userHM.get("avatar").toString());
        user.setUserName(userHM.get("username").toString());
        user.setCheckinDays((int) userHM.get("checkindays"));
        user.setExp((int) userHM.get("exp"));
        user.setBackImage(userHM.get("backimage").toString());
        user.setWexID(userHM.get("wexid").toString());
        user.setQq(userHM.get("qq").toString());
        user.setWeight((int) userHM.get("weight"));
        user.setHeight((int) userHM.get("height"));
        user.setStepsToday((int) userHM.get("stepstoday"));
        user.setLatitude(userHM.get("latitude").toString());
        user.setLongtitude(userHM.get("longtitude").toString());
        user.setHobby(userHM.get("hobby").toString());
        user.setSignature(userHM.get("signature").toString());
        user.setSex((userHM.get("sex").toString()));
        user.setLevel((int) userHM.get("level"));
        user.setInviteCode(userHM.get("invitecode").toString());
        user.setStatus((int) userHM.get("status"));
        user.setMoney((int) userHM.get("money"));
        user.setRegDate(userHM.get("regdate").toString());
        return user;

    }


    //清空所有静态变量
    //////////////////////////////////////

    public static void clearAll() {
        user = null;
        userIdPose = -1;
        pos = null;
        userPose = null;
        userID = -1;
        loveShip = null;
        loverID = "";
        halfId = -1;
        half = null;
        circleID = -1;
        activityRecord = null;
        myLatitude = 0;
        myLongtitude = 0;
        finishStatus = -1;
        fragParamName = "";
        fragParam = "";
        bitmap = null;
        picPath = null;
//        ItemUtil.imagePath2 = "";
//        ItemUtil.imagePath = "";

        user = new User();
        loveShip = new LoveShip();
        activityRecord = new ActivityRecord();


    }

    public static int type2int(String type) {
        int typeInt = 1;
        if (type.equals("女神自拍")) {
            typeInt = 1;
        } else if (type.equals("潮男自拍")) {
            typeInt = 2;
        } else if (type.equals("美女")) {
            typeInt = 3;
        } else if (type.equals("帅哥")) {
            typeInt = 4;
        } else if (type.equals("情侣")) {
            typeInt = 5;
        } else if (type.equals("集体")) {
            typeInt = 6;
        } else if (type.equals("小孩")) {
            typeInt = 7;
        }
        return typeInt;
    }




    public static void setSearchViewOnClickListener(View v, View.OnClickListener listener) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)v;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = group.getChildAt(i);
                if (child instanceof LinearLayout || child instanceof RelativeLayout) {
                    setSearchViewOnClickListener(child, listener);
                }

                if (child instanceof TextView) {
                    TextView text = (TextView)child;
                    text.setFocusable(false);
                }
                child.setOnClickListener(listener);
            }
        }
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static final int REQUEST_PERMISSION = 3;
    public static Boolean checkPermissions(Activity activity){
        List<String> permissionList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(activity,permissions,REQUEST_PERMISSION);
            return false;
        }
        return true;
    }

}
