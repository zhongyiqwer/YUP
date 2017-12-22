package com.owo.module_a_login.presenter;

import android.content.Context;

import com.owo.module_a_login.model.ModelLogin;
import com.owo.module_a_login.model.ModelLoginImpl;
import com.owo.module_a_login.model.OnLoadListener;
import com.owo.module_a_login.model.OnLoadListener2;
import com.owo.module_a_login.view.ViewLogin;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.utils.Common;
import com.owo.utils.Constants;
import com.owo.utils.util_http.HttpHelper;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author XQF
 * @created 2017/5/22
 */
public class PresenterLoginImpl implements PresenterLogin {

    private ModelLogin mModelLogin;
    private ViewLogin mViewLogin;

    public PresenterLoginImpl(ViewLogin viewLogin) {
        mModelLogin = new ModelLoginImpl();
        mViewLogin = viewLogin;
    }


    @Override
    public void loginByPhoneAndPwd(String phone, String pwd) {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("phoneNumber", phone);
        paramHM.put("passWord", pwd);
        mModelLogin.login(paramHM, new OnLoadListener2() {
            @Override
            public void onSucess(String string,int code) {
                mViewLogin.getResultLogin(string,code);
            }
        });

    }

    @Override
    public void loadUserById(int userId,final Context context) {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("id", userId + "");
        mModelLogin.loadUser(paramHM, new OnLoadListener() {
            @Override
            public void onSucess(String string) {
                try {
                    HashMap<String, Object> map = HttpHelper.AnalysisUserInfo(string);
                    BeanUser beanUser = Constants.getBeanUserFromMap(map);
                    mViewLogin.getResultGetUserById(beanUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Common.display(context,"登录失败：用户名或密码错误");
                }
            }
        });
    }
}
