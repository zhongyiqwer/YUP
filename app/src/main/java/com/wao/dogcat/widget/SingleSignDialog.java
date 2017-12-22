package com.wao.dogcat.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.wao.dogcat.R;


import java.util.Timer;
import java.util.TimerTask;


public class SingleSignDialog extends Dialog {
    private Context context;
    private ImageView heart,signBtn;


    public SingleSignDialog(Context context, int style) {
        super(context, style);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_single_sign_dialog);
        signBtn = (ImageView) findViewById(R.id.signBtn);
        heart =  (ImageView) findViewById(R.id.heart);
        setCancelable(true);
        setCanceledOnTouchOutside(true);


        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signBtn.setImageResource(R.drawable.sign_full_dog);
                heart.setImageResource(R.drawable.palm_full);

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
