package com.wao.dogcat.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wao.dogcat.R;

/**
 * Created by ZJX on 2018/3/3.
 */

public class ChangePassWordActivity extends Activity implements View.OnClickListener{

    private ImageView backBtn;
    private Button commit;
    private EditText oldPassword;
    private EditText newPassword;
    private EditText newPasswordAgain;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        initView();
    }

    private void initView() {
        backBtn = (ImageView) findViewById(R.id.backBtn1);
        commit = (Button) findViewById(R.id.btn_change_password);
        oldPassword = (EditText) findViewById(R.id.editV_old_password);
        newPassword = (EditText) findViewById(R.id.editV_new_password);
        newPasswordAgain = (EditText) findViewById(R.id.editV_new_password_again);

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
                // TODO: 2018/3/3

                Toast.makeText(ChangePassWordActivity.this,"确认修改",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
