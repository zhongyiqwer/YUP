package io.jchat.android.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

import io.jchat.android.activity.ContactsFragment;
import io.jchat.android.activity.ConversationListFragment;
import io.jchat.android.activity.MainActivity;
import io.jchat.android.activity.MeFragment;
import io.jchat.android.adapter.ViewPagerAdapter;
import io.jchat.android.application.JChatDemoApplication;
import io.jchat.android.chatting.utils.HandleResponseCode;
import io.jchat.android.view.MainView;
import com.wao.dogcat.R;
public class MainController implements OnClickListener, OnPageChangeListener {

    private final static String TAG = "MainController";

    private ConversationListFragment mConvListFragment;

    private MeFragment mMeActivity;
    private MainView mMainView;
    private ContactsFragment mContactsActivity;

    private MainActivity mContext;
    private ProgressDialog mDialog;
    // 裁剪后图片的宽(X)和高(Y), 720 X 720的正方形。
    private static int OUTPUT_X = 720;
    private static int OUTPUT_Y = 720;

    public MainController(MainView mMainView, MainActivity context) {
        this.mMainView = mMainView;
        this.mContext = context;
        setViewPager();
    }

    private void setViewPager() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        // init Fragment
        mConvListFragment = new ConversationListFragment();
        mContactsActivity = new ContactsFragment();
        mMeActivity = new MeFragment();
        fragments.add(mConvListFragment);
        fragments.add(mContactsActivity);
        fragments.add(mMeActivity);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext.getSupportFragmentManger(),
                fragments);
        mMainView.setViewPagerAdapter(viewPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.actionbar_msg_btn:
                mMainView.setCurrentItem(0);
                break;
            case R.id.actionbar_contact_btn:
                mMainView.setCurrentItem(1);
                break;
            case R.id.actionbar_me_btn:
                mMainView.setCurrentItem(2);
                break;
        }
    }

    public String getPhotoPath() {
        return mMeActivity.getPhotoPath();
    }



    @Override
    public void onPageSelected(int index) {
        // TODO Auto-generated method stub
        mMainView.setButtonColor(index);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    public void sortConvList() {
        mConvListFragment.sortConvList();
    }

    public void refreshNickname(String newName) {
        mMeActivity.refreshNickname(newName);
    }
}
