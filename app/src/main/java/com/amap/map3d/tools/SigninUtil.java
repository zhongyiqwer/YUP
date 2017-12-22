package com.amap.map3d.tools;

import android.content.Context;

import com.wao.dogcat.R;
import com.wao.dogcat.widget.CoupleSignDialog;
import com.wao.dogcat.widget.SingleSignDialog;

/**
 * Created by ppssyyy on 2017-03-28.
 */
public class SigninUtil {
    public static void showSingleSignDialog(Context context){
        SingleSignDialog singleSignDialog = new SingleSignDialog(context, R.style.MyDialogStyle);
        singleSignDialog.show();
    }
    public static void showCoupleSignDialog(Context context,int code){
        CoupleSignDialog coupleSignDialog = new CoupleSignDialog(context, R.style.MyDialogStyle,code);
        coupleSignDialog.show();
    }
}
