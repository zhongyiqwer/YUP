package com.wao.dogcat.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wao.dogcat.R;


public class CamDialog extends Dialog {
    private Context context;
    private LinearLayout takePhoto;
    private TextView chooseFromAlbum;


    public CamDialog(Context context, int style) {
        super(context, style);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_cam_dialog);
        takePhoto = (LinearLayout) findViewById(R.id.takePhoto);
        chooseFromAlbum =  (TextView) findViewById(R.id.chooseFromAlbum);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

    }

    public void takePhoto(final View.OnClickListener listener) {
        takePhoto.setOnClickListener(listener);
    }
    public void chooseFromAlbum(final View.OnClickListener listener) {
        chooseFromAlbum.setOnClickListener(listener);
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
