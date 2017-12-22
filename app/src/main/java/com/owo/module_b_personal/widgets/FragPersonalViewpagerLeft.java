package com.owo.module_b_personal.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.map3d.tools.MapUtil;
import com.baidu.mapapi.map.BaiduMap;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.base.FragBase;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.presenter.PresenterPersonal;
import com.owo.module_b_personal.presenter.PresenterPersonalImpl;
import com.owo.module_b_personal.view.ViewPersonalViewpagerLeft;
import com.owo.module_c_detail.AtyDetail;
import com.owo.utils.Common;
import com.owo.utils.Constants;
import com.owo.utils.FileManager;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.wao.dogcat.controller.CompleteActivity;
import com.wao.dogcat.controller.SettingActivity;

import com.wao.dogcat.controller.server.TimeService;
import com.wao.dogcat.controller.single.FriendActivity;
;
import com.wao.dogcat.widget.CircleImageView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * @author XQF
 * @created 2017/5/17
 */
public class FragPersonalViewpagerLeft extends FragBase implements ViewPersonalViewpagerLeft {

    public static FragPersonalViewpagerLeft newInstance(BeanUser beanUser) {
        FragPersonalViewpagerLeft fragPersonalViewpagerLeft = new FragPersonalViewpagerLeft();
        mBeanUser = beanUser;
        return fragPersonalViewpagerLeft;
    }


    public static BeanUser mBeanUser;
    private static final int REQUEST_CODE_GALLERY = 111;
    private String picPath;
    public static Bitmap bitmap;

    @BindView(R.id.left_avatar)
    protected CircleImageView mCircleImageViewUserAvatar;


    @OnClick(R.id.left_avatar)
    public void fun1(){
        start(getContext(), CompleteActivity.class);
    }

    @BindView(R.id.person_bg)
    protected ImageView backImage;

    @OnClick(R.id.person_bg)
    public void fun2(){


        Activity activity = getActivity();
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        final Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.alert_dialog);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        TextView content = (TextView) dialog.findViewById(R.id.dialog_content);
        content.setText("更换背景图片?");

        final Button yesBtn = (Button) dialog.findViewById(R.id.yesBtn);
        final Button noBtn = (Button) dialog.findViewById(R.id.noBtn);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                dialog.dismiss();


            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });




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
                        backImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        backImage.setImageBitmap(bitmap);
                        try {
                            if (picPath != null) {
                                String json =
                                        postData(picPath);
                                int code = HttpHelper.getCode(json);
                                if (code == 200) {
                                    String imgPath = HttpHelper.AnalysisData(json).toString();
                                    Common.user.setBackImage(imgPath);
                                    FileManager.delAllFile(Common.FULL_IMG_CACHE_PATH);

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


    public String postData(String path) throws Exception {
        if (Common.user!=null) {
            HashMap<String, String> paramHM = new HashMap<>();
            paramHM.put("id", Common.userID+ "");
            HashMap<String, String> fileHM = new HashMap<>();
            fileHM.put("backImage", path);
            return HttpHelper.postData(MyURL.UPDATE_BIMG_BY_ID, paramHM, fileHM);
        }else return "";
    }

    @BindView(R.id.levelNum)
    protected TextView mUserLevel;
    @BindView(R.id.left_name)
    protected TextView mTextViewUserName;


    @BindView(R.id.left_height)
    protected TextView mTextViewHeight;

    @BindView(R.id.left_age)
    protected TextView mTextViewAge;

    @BindView(R.id.left_weight)
    protected TextView mTextViewWeight;

    @BindView(R.id.left_signature)
    protected TextView mTextViewSignNature;

    @BindView(R.id.left_sex)
    protected ImageView mSex;

    @BindView(R.id.left_followed)
    protected TextView mTextViewUserFollowedSum;


    @BindView(R.id.left_followme)
    protected TextView mTextViewUserFollowMeSum;

    @BindView(R.id.left_followedTxt)
    protected TextView mTextViewUserFollowedSumTxt;


    @BindView(R.id.left_followmeTxt)
    protected TextView mTextViewUserFollowMeSumTxt;

    private PresenterPersonal mPresenterPersonal;
    private int mSumIFollowed;
    private int mSumFollowMe;

    private DisplayImageOptions options1, options2;
    private String path1, path2;

    @OnClick(R.id.settingBtn)
    public void setting(){
        Intent i = new Intent();
        i.setClass(getContext(),SettingActivity.class);
        getContext().startActivity(i);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_personal_viewpager_left, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Common.user!=null) {
            mPresenterPersonal = new PresenterPersonalImpl(this);
            mPresenterPersonal.loadSumUserFollowedByUserId(Common.userID);
            mPresenterPersonal.loadSumUserFollowMeByUserId(Common.userID);
        }

        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }



    @Override
    public void getSumIFoloowed(int sum) {
        mSumIFollowed = sum;
        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 3;
                handler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void getSumFollowMe(int sum) {
        mSumFollowMe = sum;
        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }.start();
    }


    public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    if (Common.user!=null) {
                        final String avatarUrl = Common.user.getAvatar();
//                            Glide.with(getActivity()).load((MyURL.ROOT+avatarUrl)).placeholder(R.drawable.my).
//                                    into(mCircleImageViewUserAvatar);
//
//                        Glide.with(getActivity()).load((MyURL.ROOT+mBeanUser.getBackImage())).placeholder(R.drawable.single_bg).
//                                into(backImage);

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
                                .showImageForEmptyUri(R.drawable.single_bg) //
                                .showImageOnFail(R.drawable.single_bg) //
                                .cacheInMemory(true) //
                                .cacheOnDisk(true) //
                                .build();//

                        path1 = MyURL.ROOT+avatarUrl;
                        path2 = MyURL.ROOT+Common.user.getBackImage();
                        com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                                displayImage(path1, mCircleImageViewUserAvatar, options1);
                        com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                                displayImage(path2, backImage, options2);

                        if (CompleteActivity.bitmap!=null)
                            mCircleImageViewUserAvatar.setImageBitmap(CompleteActivity.bitmap);
                        if (bitmap!=null)
                            backImage.setImageBitmap(bitmap);


                        int level = TimeService.level;
                        mUserLevel.setText("Lv." + level);

                        mTextViewUserName.setText(Common.user.getUserName());
                        if (Common.user.getHeight()>0) {
                            mTextViewHeight.setVisibility(View.VISIBLE);
                            mTextViewHeight.setText(Common.user.getHeight() + "cm");
                        }
                        if (Common.user.getAge()>0) {
                            mTextViewAge.setVisibility(View.VISIBLE);
                            mTextViewAge.setText(Common.user.getAge() + "岁");
                        }
                        if (Common.user.getWeight()>0) {
                            mTextViewWeight.setVisibility(View.VISIBLE);
                            mTextViewWeight.setText(Common.user.getWeight() + "kg");
                        }
                        if (Common.user.getSex().equals("2"))
                            mSex.setImageResource(R.drawable.map_girl);
                        mTextViewSignNature.setText(Common.user.getSignature());

                    }

                    break;
                case 2:
                    mTextViewUserFollowMeSum.setText(mSumFollowMe + "");
                    mTextViewUserFollowMeSum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Constants.status = Constants.FOLLOWED;
                            Intent intent = new Intent();
                            intent.setClass(getContext(), FriendActivity.class);
                            getContext().startActivity(intent);

                        }
                    });
                    mTextViewUserFollowMeSumTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Constants.status = Constants.FOLLOWED;
                            Intent intent = new Intent();
                            intent.setClass(getContext(), FriendActivity.class);
                            getContext().startActivity(intent);

                        }
                    });
                    break;
                case 3:
                    mTextViewUserFollowedSum.setText(mSumIFollowed + "");
                    mTextViewUserFollowedSum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Constants.status = Constants.FOLLOW;
                            Intent intent = new Intent();
                            intent.setClass(getContext(), FriendActivity.class);
                            getContext().startActivity(intent);

                        }
                    });
                    mTextViewUserFollowedSumTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Constants.status = Constants.FOLLOW;
                            Intent intent = new Intent();
                            intent.setClass(getContext(), FriendActivity.class);
                            getContext().startActivity(intent);

                        }
                    });
                    break;



            }
        }
    };
}
