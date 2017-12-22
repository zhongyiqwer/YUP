package com.owo.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.owo.module_b_main.AtyMain;

/**
 * @author XQF
 * @created 2017/5/4
 */
public class FragBase extends Fragment {
    protected AppCompatActivity mAty = (AppCompatActivity) getActivity();


    /**
     * 显示进度条
     *
     * @return
     */
    public ProgressDialog showSpinnerDialog() {
        //activity = modifyDialogContext(activity);
        Activity activity = getActivity();
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("加载数据...");
        if (!activity.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    public static void start(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public static void start(Context context, Class<?> cls,int param) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("param",param);
        context.startActivity(intent);
    }

    public static void start(Context context, Class<?> cls,String param) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("param",param);
        context.startActivity(intent);
    }


    public static void toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
