package com.amap.map3d.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;


import com.baidu.mapapi.map.BaiduMap;
import com.owo.utils.Common;
import com.wao.dogcat.R;
import com.wao.dogcat.widget.CamDialog;
import com.wao.dogcat.widget.GoDieChooseDialog;
import com.wao.dogcat.widget.PublishCapsuleDialog;
import com.wao.dogcat.widget.PublishPicDialog;


//import org.lasque.camerakit.TakePhoto;

import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by ppssyyy on 2017-03-25.
 */
public class ItemUtil {
    private static final int REQUEST_CODE_GALLERY = 111;

    public static String imagePath = "";

    public static Context cxt;

    public static String imagePath2 = "";

    public static BaiduMap bmap;

    public static void itemManage(int itemID, Context context, BaiduMap baiduMap) {
        cxt = context;
        bmap = baiduMap;
        switch (itemID) {
            case Common.CAM:
                //照相机
                showCamDialog(context);

                break;
            case Common.POS_CAM:
                //pose相机
                Intent intent = new Intent();
                //intent.putExtra("type", 1);
//                intent.setClass(cxt, TakePhoto.class);
//                ((Activity) cxt).finish();
//                cxt.startActivity(intent);

                break;
            case Common.TXT:
                //小纸条
                showPublishTxtDialog(context, baiduMap);

                break;
            case Common.CAPSULE:
                //时间沙漏
                showPublishCapsuleDialog(context);

                break;
            case Common.MOVE:
                //搬家卡
                Common.display(context, "敬请期待！");
                break;
            case Common.GO_DIE:
                //狗die
                showGoDieDialog(context);
                break;
            case Common.LETS_GO:
//                //lets狗
//                SingleBasicMapActivity.selectLetsGo.setVisibility(View.VISIBLE);
//                SingleBasicMapActivity.isDog = true;
                break;
            case Common.DOODLE:
                //涂鸦笔
                Common.display(context, "敬请期待！");
                break;

        }
    }


    public static void showCamDialog(final Context context) {
        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        final CamDialog cd = new CamDialog(activity, R.style.MyDialogStyle);
        cd.show();
        cd.takePhoto(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //金币+20
                Intent intent = new Intent();
                //intent.putExtra("type", 1);
                //intent.setClass(cxt, TakePhoto.class);
                ((Activity) cxt).finish();
                cxt.startActivity(intent);
                cd.dismiss();

            }
        });
        cd.chooseFromAlbum(new View.OnClickListener() {
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
                        .setForceCrop(true)
                        .build();
                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                cd.dismiss();

            }
        });
    }

    public static void showPublishCapsuleDialog(final Context context) {
        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        final PublishCapsuleDialog pd
                = new PublishCapsuleDialog(activity, R.style.MyDialogStyle);

        pd.show();


    }


    public static void showPublishPicDialog(final Context context, String picPath, BaiduMap baiduMap, boolean isPose) {
        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }

        final PublishPicDialog pd = new PublishPicDialog(activity, R.style.MyDialogStyle, picPath, false,isPose, baiduMap);
        pd.show();
        System.out.println("localpath=" + picPath);


    }


    public static void showGoDieDialog(final Context context) {
        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }

        final GoDieChooseDialog pd = new GoDieChooseDialog(activity, R.style.MyDialogStyle);
        pd.show();
    }


    public static void showPublishTxtDialog(final Context context, BaiduMap baiduMap) {
        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }

        final PublishPicDialog pd = new PublishPicDialog(activity, R.style.MyDialogStyle, "", true,false, baiduMap);
        pd.show();

    }

    public static GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            Log.v("onHanlderSuccess", "reqeustCode: " + reqeustCode + "  resultList.size" + resultList.size());
            for (PhotoInfo info : resultList) {
                switch (reqeustCode) {
                    case REQUEST_CODE_GALLERY:
                        imagePath = info.getPhotoPath();
                        if (imagePath.trim().length() != 0) ;
                        showPublishPicDialog(cxt, imagePath, bmap,false);
                        break;
                }
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
        }
    };

    public static String getItemFunction(String content, int status, int itemPrivilege) {
        String result = "";
        if (itemPrivilege == 1) {
            if (status == 1) //通用道具
            {
                result = content.split("_")[0];   //单身介绍

            }
            if (status == 2) {
                result =
                        content.split("_")[1]; //情侣介绍
            }
        } else result = content;

        return result;

    }
}
