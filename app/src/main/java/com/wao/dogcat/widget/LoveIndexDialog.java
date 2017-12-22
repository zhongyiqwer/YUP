package com.wao.dogcat.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.wao.dogcat.R;


public class LoveIndexDialog extends Dialog {
    Context context;

    public LoveIndexDialog(Context context, int style) {
        super(context, style);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_loveindex_dialog);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

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
