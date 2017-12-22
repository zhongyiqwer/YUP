package com.owo.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.wao.dogcat.R;

/**
 * @author XQF
 * @created 2017/5/4
 */
public abstract class AtyBase extends AppCompatActivity {
    public abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_base_layout);
        FragmentManager fragManager = getSupportFragmentManager();
        Fragment fragment = fragManager.findFragmentById(R.id.frag_container);
        if (fragment == null) {
            fragment = createFragment();
            fragManager.beginTransaction().add(R.id.frag_container, fragment).commit();
        }
    }

    public static void start(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public static void toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context,int resId){
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
