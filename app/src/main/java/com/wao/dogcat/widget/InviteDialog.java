package com.wao.dogcat.widget;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.owo.utils.Common;
import com.wao.dogcat.R;



public class InviteDialog extends Dialog {
    private Context context;
    private Button invite,single;


    public InviteDialog(Context context, int style) {
        super(context, style);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_invite_dialog);
        invite = (Button) findViewById(R.id.btnInvite);
        //single = (Button) findViewById(R.id.single);

        Common.userSP = context.getSharedPreferences("userSP", 0);

        if (Common.userSP.getInt("sex",0)==2){
            invite.setBackgroundResource(R.drawable.btn_shape_red);
        }

//        single.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//            }
//        });
    }

    public void ok(final View.OnClickListener listener) {
        invite.setOnClickListener(listener);
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
