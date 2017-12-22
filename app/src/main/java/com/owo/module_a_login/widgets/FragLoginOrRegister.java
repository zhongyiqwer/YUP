package com.owo.module_a_login.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.platform.comjni.map.commonmemcache.JNICommonMemCache;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.owo.base.FragBase;
import com.owo.module_a_register.AtyRegister;
import com.owo.module_b_main.AtyMain;
import com.owo.module_a_login.presenter.PresenterLogin;
import com.owo.module_a_login.presenter.PresenterLoginImpl;
import com.owo.module_a_login.view.ViewLogin;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import io.jchat.android.activity.MainActivity;
import io.jchat.android.chatting.utils.HandleResponseCode;
import io.jchat.android.database.UserEntry;

/**
 * @author XQF
 * @created 2017/5/18
 */
public class FragLoginOrRegister extends FragBase implements ViewLogin {

    public static FragLoginOrRegister newInstance() {
        return new FragLoginOrRegister();
    }


    //登陆按钮
    @BindView(R.id.btn_login)
    protected Button mBtn;

    //电话信息栏
    @BindView(R.id.editV_frag_login_or_register_phone)
    protected EditText mEditTextPhone;

    //密码信息栏
    @BindView(R.id.editV_frag_login_or_register_password)
    protected EditText mEditTextPassword;


    //忘记密码
    @BindView(R.id.editV_frag_login_or_register_forget_password)
    protected TextView mTextViewForgetPwd;

    //注册
    @BindView(R.id.editV_frag_login_or_register_toregister)
    protected TextView mTextViewRegister;
    //交互者
    private PresenterLogin mPresenterLogin;
    //网络获取的登陆结果
    private String mResultLogin;

    //用户信息的bean
    public static BeanUser mBeanUser;

    //根据id拿到用户信息后
    private HashMap<String, Object> mMap;
    //这是保存数据的sharedp,主要是用来保存ID的
    private SharedPreferences.Editor mEditor;
    // 
    private int mUserId;

    private int code;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_login_or_register_layout, container, false);
        ButterKnife.bind(this, view);
        mPresenterLogin = new PresenterLoginImpl(this);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Common.userSP = getActivity().getSharedPreferences("userSP", 0);
        //从splash过来不用传数据，但是会有退出登录的地方到这里来
        Intent intent = getActivity().getIntent();
        String intentStr = intent.getStringExtra("extra");
        if (intentStr != null) {
            if (intentStr.equals("logout")) {
                mEditTextPhone.setText("");
                mEditTextPassword.setText("");
            }

        } else {
            //检查一下网络设置
            if (!Common.isNetworkAvailable(getActivity())) {
                Common.display(getActivity(), "检查手机网络设置！");
            } else {
                if (intentStr == null) {//不是由其他界面跳转过来的，检查是不是有本地信息的id，要是有则直接进行跳转，要是没有，等待用户按下登陆键
                    mUserId = Common.userSP.getInt("ID", 0);
                    mPresenterLogin.loadUserById(mUserId,getContext());
                    if (mUserId != 0 && mBeanUser != null) {
                        Common.showProgressDialog("自动登录...",getContext());

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                jpushUpdate();
                                Looper.prepare();
                                loginIM("yup_"+mUserId, mBeanUser.getPassWord(), getActivity());
                                Looper.loop();
                            }
                        };
                        timer.schedule(task, 100);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Common.dismissProgressDialog(getContext());
    }

    //如果有记录就自动登陆

    /**
     * 点击登陆
     */
    @OnClick(R.id.btn_login)
    public void onBtnClick() {
        String phone = mEditTextPhone.getText().toString().trim();
        String password = mEditTextPassword.getText().toString().trim();
        loginByPhoneAndPassword(phone, password);
    }

    /*
    忘记密码
     */
    @OnClick(R.id.editV_frag_login_or_register_forget_password)
    public void onClickForgetPwd() {
        Common.display(getContext(), "敬请期待");
    }

    /*
    前往注册
     */
    @OnClick(R.id.editV_frag_login_or_register_toregister)
    public void onClickRegiter() {
        start(getActivity(), AtyRegister.class);
        getActivity().finish();
    }


    /**
     * 登陆
     *
     * @param phone    电话
     * @param password 密码
     */
    private void loginByPhoneAndPassword(String phone, String password) {
        if (phone.trim().length() == 0)
            Common.display(getContext(), "手机号不能为空");
        else if (password.trim().length() == 0)
            Common.display(getContext(), "密码不能为空");

        else {
            mPresenterLogin.loginByPhoneAndPwd(phone, password);
        }
    }


    @Override
    public void getResultLogin(String string, int code) {
        mResultLogin = string;
        this.code = code;
        afterGetResultLogin();
    }


    public void afterGetResultLogin() {
        try {
            Object data = HttpHelper.AnalysisData(mResultLogin);
            if (data != null) {
                mUserId = (int) data;
                mPresenterLogin.loadUserById(mUserId,getContext());

            } else {
                Looper.prepare();
                Common.display(getContext(), "登录失败：请检查用户名和密码");
                Looper.loop();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Looper.prepare();
            Common.display(getContext(), "登录失败：JSONException");
            Looper.loop();
        }
    }


    @Override
    public void getResultGetUserById(BeanUser beanUser) {
        mBeanUser = beanUser;
        afterGetUser();
    }

    public void afterGetUser() {
        if (mBeanUser != null) {
            jpushUpdate();
            handleUserInfo();
            loginIM("yup_"+mUserId, mBeanUser.getPassWord(), getActivity());

        } else {
            Looper.prepare();
            Common.display(getContext(), "登录失败：未能获取用户数据");
            Looper.loop();
        }
    }


    private void jpushUpdate() {
        Set<String> tags = new HashSet<>();
        int a = mBeanUser.getStatus();
        tags.add(a + "");
        if (mUserId != 0 && mBeanUser.getStatus() != 0) {
            JPushInterface.setAliasAndTags(getActivity().getApplicationContext(), mUserId + "", tags, new TagAliasCallback() {
                @Override
                public void gotResult(int responseCode, String alias, Set<String> tags) {
                    // TODO
                    if (responseCode == 0) {
                        //加载成功
                        Log.i("AliasAndTags", alias + " " + tags.toString());
                    }
                }
            });
        }
    }


    public void loginIM(final String username, final String password, final Context mContext) {
        JMessageClient.login(username, password, new BasicCallback() {
            @Override
            public void gotResult(final int status, final String desc) {
                if (status == 0) {
                    String username = JMessageClient.getMyInfo().getUserName();
                    String appKey = JMessageClient.getMyInfo().getAppKey();
                    UserEntry user = UserEntry.getUser(username, appKey);
                    if (null == user) {
                        user = new UserEntry(username, appKey);
                        user.save();
                    }
                    handleUserInfo();
                    start(getActivity(), AtyMain.class);
                    getActivity().finish();
                    Common.dismissProgressDialog(getContext());
                } else {
                    Log.i("LoginController", "status = " + status);
                    HandleResponseCode.onHandle(mContext, status, false);
                    Common.dismissProgressDialog(getContext());
                }
            }
        });

    }


    //////////////处理遗留问题//////////////////
    public void handleUserInfo() {

        Common.userSP = getContext().getSharedPreferences("userSP", 0);

        SharedPreferences.Editor editor = Common.userSP.edit();
        editor.putInt("ID", mUserId);
        editor.commit();

        mUserId = Common.userSP.getInt("ID", 0);


        try {
            String jsonInfo = postData();
            HashMap<String, Object>
                    userHM = new HashMap<>();
            userHM = HttpHelper.AnalysisUserInfo(jsonInfo);
            Common.setUserInfo(userHM);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String postData() throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("id", mUserId + "");
        String result = HttpHelper.postData(MyURL.GET_USER_BY_ID, paramHM, null);
        return result;
    }

}
