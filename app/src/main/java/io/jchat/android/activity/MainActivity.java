package io.jchat.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.owo.utils.Common;
import com.wao.dogcat.R;


import java.io.File;

import cn.jiguang.api.JCoreInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;

import cn.jpush.im.api.BasicCallback;
import io.jchat.android.application.JChatDemoApplication;
import io.jchat.android.chatting.utils.HandleResponseCode;
import io.jchat.android.controller.MainController;
import io.jchat.android.chatting.utils.FileHelper;
import io.jchat.android.chatting.utils.SharePreferenceManager;
import io.jchat.android.view.MainView;

public class MainActivity extends FragmentActivity {
    private MainController mMainController;
    private MainView mMainView;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_im);
        mMainView = (MainView) findViewById(R.id.main_view);
        mMainView.initModule();
        mMainController = new MainController(mMainView, this);

        mMainView.setOnClickListener(mMainController);
        mMainView.setOnPageChangeListener(mMainController);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        JCoreInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        JCoreInterface.onResume(this);
        //第一次登录需要设置昵称
        boolean flag = SharePreferenceManager.getCachedFixProfileFlag();
        UserInfo myInfo = JMessageClient.getMyInfo();
        if (myInfo == null) {
            Intent intent = new Intent();
            if (null != SharePreferenceManager.getCachedUsername()) {
                intent.putExtra("userName", SharePreferenceManager.getCachedUsername());
                intent.putExtra("avatarFilePath", SharePreferenceManager.getCachedAvatarPath());
                intent.setClass(this, ReloginActivity.class);
            } else {
                intent.setClass(this, LoginActivity.class);
            }
            startActivity(intent);
            finish();

        }

        else {
            String username = Common.user.getUserName();
            String avatar = Common.user.getAvatar();
            String signature = Common.user.getSignature();
            String sex = Common.user.getSex();
            int age = Common.user.getAge();
            int height = Common.user.getHeight();
            int weight = Common.user.getWeight();
            int id = Common.user.getId();
            myInfo.setNickname(username);
            myInfo.setSignature(signature);
            if (sex.equals("1")) {
                myInfo.setGender(UserInfo.Gender.male);
            }else if (sex.equals("2")){
                myInfo.setGender(UserInfo.Gender.female);
            }else myInfo.setGender(UserInfo.Gender.unknown);

            JMessageClient.updateMyInfo(UserInfo.Field.nickname, myInfo, new BasicCallback() {
                @Override
                public void gotResult(final int status, final String desc) {

                    if (status == 0) {
                        //OK

                    } else {
                        HandleResponseCode.onHandle(MainActivity.this, status, false);
                    }
                }
            });

            String oldpw = Common.user.getPassWord();
            String newpw = Common.user.getPassWord();

            JMessageClient.updateUserPassword(oldpw,newpw, new BasicCallback() {
                @Override
                public void gotResult(final int status, final String desc) {

                    if (status == 0) {
                        //OK

                    } else {
                        HandleResponseCode.onHandle(MainActivity.this, status, false);
                    }
                }
            });

            File file = new File(avatar);

            JMessageClient.updateUserAvatar(file, new BasicCallback() {
                @Override
                public void gotResult(final int status, final String desc) {

                    if (status == 0) {
                        //OK

                    } else {
                        HandleResponseCode.onHandle(MainActivity.this, status, false);
                    }
                }
            });
        }
        mMainController.sortConvList();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public FragmentManager getSupportFragmentManger() {
        // TODO Auto-generated method stub
        return getSupportFragmentManager();
    }



}
