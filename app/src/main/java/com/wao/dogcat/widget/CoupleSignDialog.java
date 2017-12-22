package com.wao.dogcat.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.owo.utils.Common;
import com.wao.dogcat.R;

import java.util.Timer;
import java.util.TimerTask;


public class CoupleSignDialog extends Dialog {
    private Context context;
    private ImageView heart, signBtn;
    private int code = 0;


    public CoupleSignDialog(Context context, int style, int code) {
        super(context, style);
        this.context = context;
        this.code = code;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_couple_sign_dialog);
        signBtn = (ImageView) findViewById(R.id.signBtn);
        heart = (ImageView) findViewById(R.id.heart);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        Common.userSP = context.getSharedPreferences("userSP", 0);

        if (code == 202) {
            //成功一半
            if (Common.userSP.getInt("sex", 0) == 2)
                signBtn.setImageResource(R.drawable.sign_empty_red);
            else signBtn.setImageResource(R.drawable.sign_empty_blue);
            heart.setImageResource(R.drawable.heart_empty);
        }
        if (code == 203) {
            //完成签到
            signBtn.setImageResource(R.drawable.sign_half);
            if (Common.userSP.getInt("sex", 0) == 2)
                heart.setImageResource(R.drawable.heart_half_left);
            else heart.setImageResource(R.drawable.heart_half_right);

        }


        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (code == 202) {
                    //成功一半
                    signBtn.setImageResource(R.drawable.sign_half);
                    if (Common.userSP.getInt("sex", 0) == 2)
                        heart.setImageResource(R.drawable.heart_half_left);
                    else heart.setImageResource(R.drawable.heart_half_right);
                }
                if (code == 203) {
                    //完成签到
                    signBtn.setImageResource(R.drawable.sign_full_couple);
                    heart.setImageResource(R.drawable.heart_full);

                }

                //获取签到天数，执行奖励

                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        dismiss();
                        Looper.loop();
                    }
                };
                timer.schedule(task, 600);


            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
