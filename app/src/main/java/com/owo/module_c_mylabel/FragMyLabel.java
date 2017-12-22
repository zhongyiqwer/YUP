package com.owo.module_c_mylabel;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.base.FragBase;
import com.owo.module_b_personal.widgets.FragPersonalViewpagerLeft;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.wao.dogcat.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author XQF
 * @created 2017/5/24
 */
public class FragMyLabel extends FragBase {

    public static FragMyLabel newInstance(){
        return  new FragMyLabel();
    }


    private static String FRAG_SELF = "self";
    private static String FRAG_HIM = "him";


    @BindView(R.id.btn_self)
    protected Button mBtnSelf;

    @BindView(R.id.btn_him)
    protected Button mBtnHim;

    private FragmentManager mFragmentManager;

    private FragMyLabelSelf myLabelSelf;
    private FragMyLabelHim myLabelHim;

    private DisplayImageOptions options;
    @BindView(R.id.my_label_avatar)
    protected CircleImageView mCircleImageViewUserAvatar;

    @OnClick(R.id.btnBack)
    public void back(){
        getActivity().finish();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aty_my_label, container, false);

        ButterKnife.bind(this, view);
        mFragmentManager = getActivity().getSupportFragmentManager();
        mBtnSelf.performClick();


        options = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my) //
                .showImageOnFail(R.drawable.my) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                displayImage(MyURL.ROOT+ FragPersonalViewpagerLeft.mBeanUser.getAvatar(), mCircleImageViewUserAvatar, options);

        return view;
    }

    @OnClick(R.id.btn_self)
    public void onBtnSelfClick() {
        mBtnSelf.setTextColor(getResources().getColor(R.color.word_color_white));
        mBtnSelf.setBackgroundColor(getResources().getColor(R.color.toolbar_bg_color));
        if (myLabelSelf == null) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            myLabelSelf = FragMyLabelSelf.newInstance();
            fragmentTransaction.replace(R.id.frag_container_aty_my_label, myLabelSelf, FRAG_SELF);
            fragmentTransaction.show(myLabelSelf);
            fragmentTransaction.commit();
            myLabelHim = null;
            mBtnHim.setTextColor(getResources().getColor(R.color.word_color_black));
            mBtnHim.setBackgroundColor(getResources().getColor(R.color.tab_color_normal));
        }
    }

    @OnClick(R.id.btn_him)
    public void onBtnHimClick() {
        mBtnHim.setTextColor(getResources().getColor(R.color.word_color_white));
        mBtnHim.setBackgroundColor(getResources().getColor(R.color.toolbar_bg_color));

        if (myLabelHim == null) {
            myLabelHim = FragMyLabelHim.newInstance();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frag_container_aty_my_label, myLabelHim, FRAG_HIM);
            fragmentTransaction.show(myLabelHim);
            fragmentTransaction.commit();
            myLabelSelf = null;
            mBtnSelf.setTextColor(getResources().getColor(R.color.word_color_black));
            mBtnSelf.setBackgroundColor(getResources().getColor(R.color.tab_color_normal));
        }
    }


}


