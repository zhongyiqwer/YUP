package com.wao.dogcat.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.owo.utils.Common;
import com.wao.dogcat.R;


public class UseItemMapDialog extends Dialog {
    private Context context;
    private Button ok,mai;
    private ImageView image,minus,add;
    private TextView buyTxt;
    private int status = -1;
    private  int itemLevel,itemPrivilege;
    public static boolean canUse;

    public UseItemMapDialog(Context context, int style,int itemPrivilege,int itemLevel ) {
        super(context, style);
        this.context = context;
        this.itemLevel = itemLevel;
        this.itemPrivilege = itemPrivilege;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_useitem_map_dialog);
        ok = (Button) findViewById(R.id.useBtn);
        //mai = (Button) findViewById(R.id.maiBtn);

        setCancelable(true);
        setCanceledOnTouchOutside(true);

        Common.userSP = context.getSharedPreferences("userSP", 0);
        status = Common.userSP.getInt("status", 0);


        if (Common.user != null) {
            //判断权限
            boolean flag = true;
            if (status == 1) {
                if (itemPrivilege == 1) {
                    flag = true;
                }
                else if (itemPrivilege == 2) {
                    flag = false;
                }
                else {
                    flag = true;
                }
            } else {
                if (itemPrivilege == 1) {
                    flag = true;
                }
                else if (itemPrivilege == 2) {
                    flag = true;
                }
                else {
                    flag = false;
                }
            }
            if (flag) {
                canUse = true;
                ok.setClickable(true);
                ok.setBackgroundResource(R.drawable.btn_shape_yellow);

                boolean f = true;
                if (itemLevel > Common.user.getLevel()) {
                    f = false;

                }
                //判断等级
                if (f) {
                    canUse = true;
                    ok.setClickable(true);
                    ok.setBackgroundResource(R.drawable.btn_shape_yellow);



                } else {
                    canUse = false;
                    ok.setBackgroundResource(R.drawable.btn_shape_gray);
                    ok.setClickable(false);
                    ok.setText("等级不够");
                }

            } else {
                canUse = false;
                ok.setBackgroundResource(R.drawable.btn_shape_gray);
                ok.setClickable(false);
                ok.setText("无法使用");
            }
        }
    }




    public void ok(final View.OnClickListener listener) {
        ok.setOnClickListener(listener);
    }

//    public void mai(final View.OnClickListener listener) {
//        mai.setOnClickListener(listener);
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
