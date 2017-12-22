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


public class UseItemReminderDialog extends Dialog {
    private Context context;
    private Button ok,cancel;
    private ImageView image,minus,add;
    private TextView price,name,type,level,privilege,function,buyNum;
    private int status = -1;

    public UseItemReminderDialog(Context context, int style) {
        super(context, style);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_useitem_reminder_dialog);
        ok = (Button) findViewById(R.id.okBtn);
        cancel = (Button) findViewById(R.id.cancelBtn);

        setCancelable(true);
        setCanceledOnTouchOutside(true);

        Common.userSP = context.getSharedPreferences("userSP", 0);
        status = Common.userSP.getInt("status", 0);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



    }


    public void ok(final View.OnClickListener listener) {
        ok.setOnClickListener(listener);
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
