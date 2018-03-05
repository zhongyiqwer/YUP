package com.wao.dogcat.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.owo.utils.Common;
import com.owo.utils.UtilLog;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by ZJX on 2018/3/3.
 * 更改密码：输入旧密码，新密码。对于目前服务端接口来说，不用传入旧密码，只要新密码和id
 */

public class ChangePassWordActivity extends Activity implements View.OnClickListener{

    private ImageView backBtn;
    private Button commit;
    private EditText oldPasswordEdit;
    private EditText newPasswordEdit;
    private EditText newPasswordAgainEdit;

    private String oldPassword;
    private String newPassword;
    private String newPasswordAgain;

    private HashMap<String,String> map = new HashMap<>();

    public static final String TAG = "ChangePassWordActivity:";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        initView();
    }

    private void initView() {
        backBtn = (ImageView) findViewById(R.id.backBtn1);
        commit = (Button) findViewById(R.id.btn_change_password);
        oldPasswordEdit = (EditText) findViewById(R.id.editV_old_password);
        newPasswordEdit = (EditText) findViewById(R.id.editV_new_password);
        newPasswordAgainEdit = (EditText) findViewById(R.id.editV_new_password_again);

        backBtn.setOnClickListener(this);
        commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backBtn1:
                Intent i = new Intent();
                i.setClass(ChangePassWordActivity.this, SettingActivity.class);
                finish();
                ChangePassWordActivity.this.startActivity(i);
                break;
            case R.id.btn_change_password:
                changePassword();
                break;
        }
    }

    private void changePassword() {
        oldPassword = oldPasswordEdit.getText().toString();
        newPassword = newPasswordEdit.getText().toString();
        newPasswordAgain = newPasswordAgainEdit.getText().toString();
        boolean isConnectWS = true;

        if (!oldPassword.matches("[a-zA-Z\\d+]{6,16}")) {
            Common.display(this, "密码应该为6-16位字母或数字组合");
            isConnectWS = false;
        }
        if (!newPassword.matches("[a-zA-Z\\d+]{6,16}")) {
            Common.display(this, "密码应该为6-16位字母或数字组合");
            isConnectWS = false;
        }
        if (!newPasswordAgain.equals(newPassword)) {
            Common.display(this, "与上次输入密码不同");
            isConnectWS = false;
        }

        boolean flag = Common.isNetworkAvailable(this);
        if (!flag) {
            Common.display(this, "请开启手机网络");
            isConnectWS = false;
        }

        if (!isConnectWS)
            return;

        //Common.showProgressDialog("正在提交",this);

        int userID = Common.userID;
        map.put("passWord",newPassword);
        map.put("id",userID+"");
        UtilLog.i(TAG,"map "+map.toString());
        updatePassword();
    }

    private void updatePassword() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    String jsonData = HttpHelper.postData(MyURL.UPDATE_PW_BY_ID,map, null);
                    UtilLog.d(TAG, "修改密码返回的数据--- jsonData" + jsonData);
                    //Common.dismissProgressDialog(ChangePassWordActivity.this);
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("jsonDate",jsonData);
                    message.setData(bundle);
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                   /* if (Common.mAlertDialog != null){
                        Common.dismissProgressDialog(ChangePassWordActivity.this);
                    }*/
                }
            }
        };
        timer.schedule(timerTask,100);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String jsonDate = bundle.getString("jsonDate");
            try {
                int code = HttpHelper.getCode(jsonDate);
                if (code == 100){
                    Common.display(ChangePassWordActivity.this,"修改成功");
                    // TODO: 2018/3/5 有问题 
                    JMessageClient.updateUserPassword(oldPassword, newPassword, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            UtilLog.i(TAG,"i:"+i+" s:"+s);
                            if (i == 0){

                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    });
}
