package com.wao.dogcat.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.owo.utils.Common;
import com.wao.dogcat.R;


public class CompleteDialog extends Dialog {
    private Context context;
    private Button ok;
//    private EditText editText;
//    private TextView msgUser;
    private CircleImageView circleImageView;


    public CompleteDialog(Context context, int style) {
        super(context, style);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_complete_dialog);
        //editText = (EditText) findViewById(R.id.myName);
        circleImageView = (CircleImageView) findViewById(R.id.myHead);
        ok = (Button) findViewById(R.id.btnOk);
        Common.userSP = context.getSharedPreferences("userSP", 0);
    }

    public void ok(final View.OnClickListener listener) {
        ok.setOnClickListener(listener);
    }

    public void editAvatar(final View.OnClickListener listener) {
        circleImageView.setOnClickListener(listener);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
