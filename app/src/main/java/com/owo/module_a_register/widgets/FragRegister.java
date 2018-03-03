package com.owo.module_a_register.widgets;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.owo.module_a_login.AtyLoginOrRegister;
import com.owo.module_a_selectlabel.widgets.AtySelectLabel;
import com.owo.utils.util_http.MyURL;
import com.owo.utils.util_http.URL;
import com.wao.dogcat.R;
import com.owo.base.FragBase;
import com.owo.module_b_main.AtyMain;
import com.owo.module_a_register.presenter.PresenterRegister;
import com.owo.module_a_register.presenter.PresenterRgisterImpl;
import com.owo.module_a_register.view.ViewRegister;
import com.owo.utils.Common;
import com.owo.utils.EncodeAndDecode;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.UtilLog;
import com.wao.dogcat.controller.server.TimeService;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import io.jchat.android.activity.MainActivity;
import io.jchat.android.chatting.utils.HandleResponseCode;
import io.jchat.android.database.UserEntry;

/**
 * @author XQF
 * @created 2017/5/18
 */
public class FragRegister extends FragBase implements ViewRegister {


    public static FragRegister newInstance() {
        return new FragRegister();
    }


    //交互中介
    private PresenterRegister mPresenterRegister;
    //用户ID
    private int mUserId;

    //保存用户ID
    private SharedPreferences.Editor mEditor;


    private String mUserName;
    private String mUserPhone;
    private String mUserPassword;
    private String mUserPasswordAgain;
    private String mUserMd5password;

    private String mResultRegister;


    @BindView(R.id.editV_frag_login_or_register_phone)
    protected EditText mEditTextPhone;
    @BindView(R.id.editV_frag_login_or_register_password)
    protected EditText mEditTextPwd;
    @BindView(R.id.editV_frag_login_or_register_password1)
    protected EditText mEditTextPwd1;
    @BindView(R.id.btn_register)
    protected Button mButtonRegister;


    public ProgressDialog mDialog;

    private int regCode;
            //posRegCode, posLogCode;
    private boolean flag = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_register_layout, container, false);
        ButterKnife.bind(this, view);
        mPresenterRegister = new PresenterRgisterImpl(this);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Common.dismissProgressDialog(getContext());
    }

    @OnClick(R.id.backBtn)
    public void backBtn(){
        start(getActivity(), AtyLoginOrRegister.class);
        getActivity().finish();
    }


    /**
     * 注册时的逻辑
     */

    @OnClick(R.id.btn_register)
    public void onBtnRegisterClick() {

        mUserPhone = mEditTextPhone.getText().toString();
        mUserPassword = mEditTextPwd.getText().toString();
        mUserName = "u_" + Common.createRandom(true, 6);


        boolean isConnectWS = true;
        if (!mEditTextPwd.getText().toString().matches("[a-zA-Z\\d+]{6,16}")) {
            Common.display(getContext(), "密码应该为6-16位字母或数字组合");
            isConnectWS = false;
        }
        if (!mEditTextPwd.getText().toString().equals(mEditTextPwd1.getText().toString())) {
            Common.display(getContext(), "与上次输入密码不同");
            isConnectWS = false;
        }

        if (!mEditTextPhone.getText().toString().matches("1[1-9]{1}[0-9]{9}")) {
            Common.display(getContext(), "手机号码格式错误");
            isConnectWS = false;
        }
        boolean flag = Common.isNetworkAvailable(getContext());
        if (!flag) {
            Common.display(getContext(), "请开启手机网络");
            isConnectWS = false;
        }

        if (!isConnectWS)
            return;

//        mDialog = showSpinnerDialog();
//        mDialog.show();
        //注册应用

        mPresenterRegister.registerByPhoneAndPsw(mUserName, mUserPhone, mUserPassword);

    }

    /**
     * 注册成功返回数据
     *
     * @param string 返回的数据
     */

    @Override
    public void getRegisterResult(String string, int code) {

        mResultRegister = string;
        regCode = code;

        UtilLog.d("test", "注册成功返回的数据  mResultRegister" + mResultRegister);
        UtilLog.d("test", "进入注册pose相机之前  mResultRegister" + mResultRegister);

        registerPoseCameraAndSoOn(mResultRegister);
    }

//    @Override
//    public void getPoseRegisterResult(String string, int code) {
//        posRegCode = code;
//        flag = true;
//
//    }


    /**
     * 根据注册返回的信息数据注册pose相机等等一系列的操作
     *
     * @param string
     */
    private void registerPoseCameraAndSoOn(final String string) {
       // UtilLog.d("test", "准备解析的pose相机: " + string);

        Common.showProgressDialog("加载初始数据...", getContext());

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {


                try {

                    if (regCode == 200) {


                        //获取ID
                        Object objId = HttpHelper.AnalysisData(string);


                        if (objId != null) {
                            mUserMd5password =  EncodeAndDecode.getMD5Str(mUserPassword);

                            Common.userSP = getContext().getSharedPreferences("userSP", 0);
                            mUserId = Integer.parseInt(objId.toString().substring(0, objId.toString().length() - 6));
                            UtilLog.d("test", "mUserId: " + mUserId);
                            mEditor = Common.userSP.edit();
                            mEditor.putInt("ID", mUserId);
                            mEditor.putInt("status", 1);

                            mEditor.commit();

                            Common.userID = mUserId;


                            //用id登录
                            String jsonInfo = login();
                            HashMap<String, Object>
                                    userHM = HttpHelper.AnalysisUserInfo(jsonInfo);
                            Common.setUserInfo(userHM);


                            //注册pose相机
                            //mPresenterRegister.registerPoseCameraByPhoneAndMd5passsword(mUserName, mUserPhone, mUserMd5password);

//                            String str = regPose();
//                            posRegCode = HttpHelper.getCode(str);


 //                           if (posRegCode == 100) {
                                //登陆pose相机
                                //mPresenterRegister.loginPoseCameraByNameAndMd5password(mUserName, mUserMd5password, mUserId);
                                //String jsonResultData = loginPose(mUserName, mUserMd5password);
                               // posLogCode = HttpHelper.getCode(jsonResultData);
                               // if (posLogCode == 100) {
//                                    UtilLog.d("test", "登录pose相机返回的数据 " + jsonResultData);
//                                    HashMap<String, Object> poseID = HttpHelper.AnalysisUid(jsonResultData);
//                                    Common.userIdPose = (int) poseID.get("userid"); //获取poseUserID
//                                    HashMap<String, String> po = new HashMap<>();
//                                    po.put("id", mUserId + "");
//                                    po.put("wexID", Common.userIdPose + "");
//                                    HttpHelper.postData(MyURL.UPDATE_WEXID_BY_ID, po, null); //建立poseUserID与yup通道

//                                    HashMap<String, String> param = new HashMap<>();
//                                    param.put("userID", mUserId + "");
//                                    param.put("itemID", "10");
//                                    HttpHelper.postData(MyURL.INSERT_USER_ITEM, param, null); //注册成功奖励相机道具
//
//                                    HashMap<String, String> p = new HashMap<>();
//                                    p.put("userID", mUserId + "");
//                                    p.put("itemID", "11");
//                                    HttpHelper.postData(MyURL.INSERT_USER_ITEM, p, null); //注册成功奖励纸条道具

//                                    HashMap<String, String> pose = new HashMap<>();
//                                    pose.put("userID", mUserId + "");
//                                    pose.put("itemID", "18");
//                                    HttpHelper.postData(MyURL.INSERT_USER_ITEM, pose, null); //注册成功pose相机道具


                                    HashMap<String, String> data = new HashMap<>();
                                    data.put("userID", mUserId + "");
                                    data.put("status", "1");
                                    HttpHelper.postData(MyURL.INSERT_STEPS, data, null);

                                    Looper.prepare();
                                    registerIM("yup_"+mUserId, mUserMd5password, getContext());
                                    Looper.loop();

                               // }

//                            } else {
//                                Looper.prepare();
//                                Common.dismissProgressDialog(getContext());
//                                Common.display(getContext(), "注册失败：POSCODE " + posRegCode);
//                                Looper.loop();
//                            }


                        } else {
                            Looper.prepare();
                            Common.dismissProgressDialog(getContext());
                            Common.display(getContext(), "注册失败：NULL");
                            Looper.loop();
                        }


                    } else if (regCode == 101) {
                        Looper.prepare();
                        Common.dismissProgressDialog(getContext());
                        Common.display(getContext(), "初始化昵称失败，请联系管理员= =..");
                        Looper.loop();
                    } else if (regCode == 102) {
                        Looper.prepare();
                        Common.dismissProgressDialog(getContext());
                        Common.display(getContext(), "该手机号已被注册过了哦");
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Common.dismissProgressDialog(getContext());
                        Common.display(getContext(), "注册失败");
                        Looper.loop();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Common.dismissProgressDialog(getContext());
                    Common.display(getContext(), "注册失败:Exception");
                    Looper.loop();
                }


            }


        };
        timer.schedule(task, 100);


    }


    public void registerIM(final String username, final String password, final Context mContext) {
        JMessageClient.register(username, password, new BasicCallback() {

            @Override
            public void gotResult(final int status, final String desc) {
                if (status == 0) {
                    JMessageClient.login(username, password, new BasicCallback() {
                        @Override
                        public void gotResult(final int status, String desc) {
                            if (status == 0) {
                                String username = JMessageClient.getMyInfo().getUserName();
                                String appKey = JMessageClient.getMyInfo().getAppKey();
                                UserEntry user = UserEntry.getUser(username, appKey);
                                if (null == user) {
                                    user = new UserEntry(username, appKey);
                                    user.save();
                                }

                                AtyMain.start(getActivity(), AtyMain.class,true);
                                getActivity().finish();

                                if (Common.isServiceRun(mContext, "com.wao.dogcat.controller.server.TimeService")) {
                                    Intent i = new Intent(mContext, TimeService.class);
                                    getContext().stopService(i);
                                }


                            }

//                            else {
//                                HandleResponseCode.onHandle(mContext, status, false);
//
//                            }
                        }
                    });
                }
//                else {
//                    HandleResponseCode.onHandle(mContext, status, false);
//
//                }
            }
        });

    }


//    public String regPose() throws Exception {
//        HashMap<String, String> paramHM = new HashMap<>();
//        paramHM.put("username", mUserName);
//        paramHM.put("userphone", mUserPhone);
//        paramHM.put("password", mUserMd5password);
//        paramHM.put("userpb", "0");
//        paramHM.put("taskpic_url", "119.29.245.167:8089/head.png");
//        return HttpHelper.postData(URL.REG_URL, paramHM, null);
//    }
//
//    public String loginPose(String ln, String pw) throws Exception {
//        HashMap<String, String> paramHM = new HashMap<>();
//        paramHM.put("username", ln);
//        paramHM.put("password", pw);
//        String result = HttpHelper.postData(URL.LOGIN_URL, paramHM, null);
//        return result;
//    }

    public String login() throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("id", mUserId + "");
        String result = HttpHelper.postData(MyURL.GET_USER_BY_ID, paramHM, null);
        return result;
    }


}
