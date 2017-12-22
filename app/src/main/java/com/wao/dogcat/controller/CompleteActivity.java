package com.wao.dogcat.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.utils.Common;
import com.owo.utils.FileManager;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.wao.dogcat.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 个人资料
 */
public class CompleteActivity extends Activity implements View.OnClickListener {
    private TextView signatureTxt, myName,  myAge, MyHeight, MyWeight, title;
    private LinearLayout input, inputArea, singlell, agell, height11, weight11, hobbyll;
    private EditText edit, editArea;

    private RadioButton male, female;
    private CircleImageView myHead;

    private String picPath;
    public static Bitmap bitmap;
    private ImageView back;
    private LinearLayout navBar;


    private boolean isSexSetted, isSucceed;

    private HashMap<String, Object> userHM;

    private static final int REQUEST_CODE_GALLERY = 111;
    private String str;

    private SharedPreferences.Editor editor;

    private int userID, status;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -2:
                case -1:
                case 0:
                    loadFail(msg.obj.toString());
                    break;
                case 1:
                    str = msg.obj.toString();
                    myName.setText(str);
                    Common.user.setUserName(str);
                    break;
                case 2:
                    str = msg.obj.toString();
                    Common.user.setSignature(str);
                    signatureTxt.setText(str);
                    break;
                case 3:
                    str = msg.obj.toString();
                    myAge.setText(str+"岁");
                    Common.user.setAge(Integer.parseInt(str));
                    break;
                case 4:
                    str = msg.obj.toString();
                    MyHeight.setText(str + "cm");
                    Common.user.setHeight(Integer.parseInt(str));
                    break;
                case 5:
                    str = msg.obj.toString();
                    MyWeight.setText(str + "kg");
                    Common.user.setWeight(Integer.parseInt(str));
                    break;
//                case 6:
//                    str = msg.obj.toString();
//                    Common.user.setHobby(str);
//                    myHobby.setText(str);
//                    break;

            }
        }
    };


    void loadFail(String msg) {
        Common.display(CompleteActivity.this, msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager mam = MyActivityManager.getInstance();
        mam.pushOneActivity(CompleteActivity.this);
        this.setContentView(R.layout.complete);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        signatureTxt = (TextView) findViewById(R.id.signatureTxt);
        myName = (TextView) findViewById(R.id.myName);
        myHead = (CircleImageView) findViewById(R.id.myHead);
        singlell = (LinearLayout) findViewById(R.id.singlell);

        navBar = (LinearLayout) findViewById(R.id.navBar);
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.btnBack);
        back.setOnClickListener(this);

        Common.userSP = getSharedPreferences("userSP", 0);
        status = Common.userSP.getInt("status", 0);
        userID = Common.userSP.getInt("ID", 0);

        myAge = (TextView) findViewById(R.id.myAge);
        MyHeight = (TextView) findViewById(R.id.myHeight);
        MyWeight = (TextView) findViewById(R.id.myWeight);
        //myHobby = (TextView) findViewById(R.id.myHobby);



        if (Common.isNetworkAvailable(this)) {
            if (Common.user != null) {
                ////我的信息///
                DisplayImageOptions options1 = new DisplayImageOptions.Builder()//
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                        .showImageForEmptyUri(R.drawable.my)
                        .showImageOnFail(R.drawable.my)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                        MyURL.ROOT + Common.user.getAvatar(), myHead, options1);
                myName.setText(Common.user.getUserName());
                signatureTxt.setText(Common.user.getSignature());
                if (status == 1) {
                    myAge.setText(Common.user.getAge() + " 岁");
                    MyHeight.setText(Common.user.getHeight() + " cm");
                    MyWeight.setText(Common.user.getWeight() + " kg");
                    //myHobby.setText(Common.user.getHobby());

                }
            }

        }


    }


    public String postData() throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("id", userID + "");
        String result = HttpHelper.postData(MyURL.GET_USER_BY_ID, paramHM, null);
        return result;
    }

    public String postData(String path) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("id", userID + "");
        HashMap<String, String> fileHM = new HashMap<>();
        fileHM.put("avatar", path);
        System.out.println(paramHM + " " + fileHM);
        return HttpHelper.postData(MyURL.UPDATE_AVATAR_BY_ID, paramHM, fileHM);
    }

    public String postData(String url, String key, String value) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("id", userID + "");
        paramHM.put(key, value);
        return HttpHelper.postData(url, paramHM, null);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }


    @Override
    public void onClick(final View view) {
        new Thread() {
            @Override
            public void run() {
                final Message msg = handler.obtainMessage();
                if (view.getId() == R.id.btnBack){
                    finish();
                }
                if (view.getId() == R.id.userName) {

                    Looper.prepare();
                    LayoutInflater inflater = LayoutInflater.from(CompleteActivity.this);
                    input = (LinearLayout) inflater.inflate(R.layout.input_value, null); //parent
                    edit = (EditText) input.findViewById(R.id.edit); //child
                    edit.setText(Common.user.getUserName());
                    new AlertDialog.Builder(CompleteActivity.this)
                            .setTitle("编辑昵称")
                            .setView(input)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {


                                            if (edit.getText().toString().trim().length() != 0) {
                                                if (!Common.user.getUserName().trim().equals(edit.getText().toString().trim())) {
                                                    if (!edit.getText().toString().trim().matches("^[A-Za-z0-9\u4E00-\u9FA5]{4,6}$")) {
                                                        msg.what = -2;
                                                        msg.obj = "昵称只能为4~6位字符，不能有特殊字符哦";
                                                        handler.sendMessage(msg);

                                                    } else {

                                                        try {
                                                            String json =
                                                                    postData(MyURL.UPDATE_USERNAME_BY_ID, "userName", edit.getText().toString().trim());
                                                            int code = HttpHelper.getCode(json);
                                                            if (code == 200) {
                                                                msg.what = 1;
                                                                msg.obj = edit.getText().toString().trim();
                                                                handler.sendMessage(msg);
                                                                /////////


                                                            } else {

                                                                if (code == 0) {
                                                                    msg.what = -1;
                                                                    msg.obj = "服务器错误，请稍后再试";
                                                                    handler.sendMessage(msg);

                                                                } else {
                                                                    msg.what = 0;
                                                                    msg.obj = code + ":" + HttpHelper.getMsg(json);
                                                                    handler.sendMessage(msg);
                                                                }
                                                            }

                                                        } catch (Exception e) {
                                                            msg.what = -1;
                                                            msg.obj = "服务器错误，请稍后再试";
                                                            handler.sendMessage(msg);
                                                        }

                                                    }

                                                }


                                            }

                                        }
                                    }

                            )
                            .setNegativeButton("取消", null)
                            .show();
                    Looper.loop();


                }
                if (view.getId() == R.id.signature) {
                    Looper.prepare();
                    LayoutInflater inflater = LayoutInflater.from(CompleteActivity.this);
                    input = (LinearLayout) inflater.inflate(R.layout.input_value, null); //parent
                    edit = (EditText) input.findViewById(R.id.edit); //child
                    edit.setHint("签名");
                    new AlertDialog.Builder(CompleteActivity.this)
                            .setTitle("编辑签名")
                            .setView(input)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {

                                            if (edit.getText().toString().length() != 0) {
                                                if (edit.getText().toString().length() > 120) {
                                                    msg.what = -2;
                                                    msg.obj = "签名不能超过120个字符";
                                                    handler.sendMessage(msg);
                                                } else {

                                                    try {
                                                        String json =
                                                                postData(MyURL.UPDATE_SIGNATURE_BY_ID, "signature", edit.getText().toString());
                                                        int code = HttpHelper.getCode(json);
                                                        if (code == 200) {
                                                            msg.what = 2;
                                                            msg.obj = edit.getText().toString();
                                                            handler.sendMessage(msg);
                                                            /////////

                                                        } else {

                                                            if (code == 0) {
                                                                msg.what = -1;
                                                                msg.obj = "服务器错误，请稍后再试";
                                                                handler.sendMessage(msg);

                                                            } else {
                                                                msg.what = 0;
                                                                msg.obj = code + ":" + HttpHelper.getMsg(json);
                                                                handler.sendMessage(msg);
                                                            }
                                                        }

                                                    } catch (Exception e) {
                                                        msg.what = -1;
                                                        msg.obj = "服务器错误，请稍后再试";
                                                        handler.sendMessage(msg);

                                                    }

                                                }
                                            }

                                        }
                                    }

                            )
                            .setNegativeButton("取消", null)
                            .show();
                    Looper.loop();

                }

                if (view.getId() == R.id.headFrame) {
                    //配置功能
                    FunctionConfig functionConfig = new FunctionConfig.Builder()
                            .setEnableCrop(true)
                            .setEnableRotate(true)
                            .setCropSquare(true)
                            .setEnablePreview(true)
                            .setEnableEdit(true)//编辑功能
                            .setEnableCrop(true)//裁剪功能
                            .setEnableCamera(true)//相机功能
                            .setForceCropEdit(true)
                            .setCropHeight(200)
                            .setCropWidth(200)
                            .setForceCrop(true)
                            .build();
                    GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                }

                if (view.getId() == R.id.agell) {
                    Looper.prepare();

                    LayoutInflater inflater = LayoutInflater.from(CompleteActivity.this);
                    input = (LinearLayout) inflater.inflate(R.layout.input_value, null); //parent
                    edit = (EditText) input.findViewById(R.id.edit); //child
                    edit.setHint("年龄");
                    edit.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
                    new AlertDialog.Builder(CompleteActivity.this)
                            .setTitle("编辑年龄")
                            .setView(input)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {

                                            if (edit.getText().toString().trim().length() != 0) {
                                                if (!edit.getText().toString().trim().matches("^(?:[1-9][0-9]?|1[01][0-9]|120)$")) {
                                                    msg.what = -2;
                                                    msg.obj = "年龄只能为1~120岁";
                                                } else {

                                                    try {
                                                        String json =
                                                                postData(MyURL.UPDAET_AGE_BY_ID, "age", edit.getText().toString().trim());
                                                        int code = HttpHelper.getCode(json);
                                                        if (code == 200) {
                                                            msg.what = 3;
                                                            msg.obj = edit.getText().toString().trim();
                                                            handler.sendMessage(msg);
                                                            ///////////////////////////////////

                                                        } else {

                                                            if (code == 0) {
                                                                msg.what = -1;
                                                                msg.obj = "服务器错误，请稍后再试";
                                                                handler.sendMessage(msg);

                                                            } else {
                                                                msg.what = 0;
                                                                msg.obj = code + ":" + HttpHelper.getMsg(json);
                                                                handler.sendMessage(msg);
                                                            }
                                                        }

                                                    } catch (Exception e) {
                                                        msg.what = -1;
                                                        msg.obj = "服务器错误，请稍后再试";
                                                        handler.sendMessage(msg);

                                                    }
                                                }

                                            }


                                        }
                                    }

                            )
                            .setNegativeButton("取消", null)
                            .show();
                    Looper.loop();


                }

                if (view.getId() == R.id.heightll) {
                    Looper.prepare();

                    LayoutInflater inflater = LayoutInflater.from(CompleteActivity.this);
                    input = (LinearLayout) inflater.inflate(R.layout.input_value, null); //parent
                    edit = (EditText) input.findViewById(R.id.edit); //child
                    edit.setHint("身高");
                    edit.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
                    new AlertDialog.Builder(CompleteActivity.this)
                            .setTitle("编辑身高")
                            .setView(input)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            {
                                                Pattern pattern = Pattern.compile("[0-9]*");//验证是否为数字
                                                if (edit.getText().toString().trim().length() != 0) {
                                                    if (!pattern.matcher(edit.getText().toString().trim()).matches()) {
                                                        msg.what = -2;
                                                        msg.obj = "身高只能为1~250cm";
                                                        handler.sendMessage(msg);
                                                    } else {
                                                        int heightInt = Integer.parseInt(edit.getText().toString().trim());
                                                        if (heightInt > 250 || heightInt < 1)
                                                        {
                                                            msg.what = -2;
                                                            msg.obj = "身高只能为1~250cm";
                                                            handler.sendMessage(msg);
                                                        }else {
                                                            try {
                                                                String json =
                                                                        postData(MyURL.UPDATE_HEIGHT_BY_ID, "height", edit.getText().toString().trim());
                                                                int code = HttpHelper.getCode(json);
                                                                if (code == 200) {
                                                                    msg.what = 4;
                                                                    msg.obj = edit.getText().toString().trim();
                                                                    handler.sendMessage(msg);
                                                                    /////////////////////////////////

                                                                } else {

                                                                    if (code == 0) {
                                                                        msg.what = -1;
                                                                        msg.obj = "服务器错误，请稍后再试";
                                                                        handler.sendMessage(msg);

                                                                    } else {
                                                                        msg.what = 0;
                                                                        msg.obj = code + ":" + HttpHelper.getMsg(json);
                                                                        handler.sendMessage(msg);
                                                                    }
                                                                }

                                                            } catch (Exception e) {
                                                                msg.what = -1;
                                                                msg.obj = "服务器错误，请稍后再试";
                                                                handler.sendMessage(msg);

                                                            }
                                                        }
                                                    }
                                                }

                                            }

                                        }
                                    }

                            )
                            .setNegativeButton("取消", null)
                            .show();

                    Looper.loop();


                }

                if (view.getId() == R.id.weightll) {
                    Looper.prepare();

                    LayoutInflater inflater = LayoutInflater.from(CompleteActivity.this);
                    input = (LinearLayout) inflater.inflate(R.layout.input_value, null); //parent
                    edit = (EditText) input.findViewById(R.id.edit); //child
                    edit.setHint("体重");
                    edit.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
                    new AlertDialog.Builder(CompleteActivity.this)
                            .setTitle("编辑体重")
                            .setView(input)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            {

                                                if (edit.getText().toString().trim().length() != 0) {
                                                    if (!edit.getText().toString().trim().matches("^(?:[1-9][0-9]?|1[01][0-9]|150)$")) {
                                                        msg.what = -2;
                                                        msg.obj = "体重只能为1~150kg";
                                                        handler.sendMessage(msg);
                                                    } else {

                                                        try {
                                                            String json =
                                                                    postData(MyURL.UPDATE_WEIGHT_BY_ID, "weight", edit.getText().toString().trim());
                                                            int code = HttpHelper.getCode(json);
                                                            if (code == 200) {
                                                                msg.what = 5;
                                                                msg.obj = edit.getText().toString().trim();
                                                                handler.sendMessage(msg);
                                                                /////////////////////////////////

                                                            } else {

                                                                if (code == 0) {
                                                                    msg.what = -1;
                                                                    msg.obj = "服务器错误，请稍后再试";
                                                                    handler.sendMessage(msg);

                                                                } else {
                                                                    msg.what = 0;
                                                                    msg.obj = code + ":" + HttpHelper.getMsg(json);
                                                                    handler.sendMessage(msg);
                                                                }
                                                            }

                                                        } catch (Exception e) {
                                                            msg.what = -1;
                                                            msg.obj = "服务器错误，请稍后再试";
                                                            handler.sendMessage(msg);

                                                        }
                                                    }
                                                }

                                            }

                                        }
                                    }

                            )
                            .setNegativeButton("取消", null)
                            .show();
                    Looper.loop();


                }
//                if (view.getId() == R.id.hobbyll) {
//                    Looper.prepare();
//                    LayoutInflater inflater = LayoutInflater.from(CompleteActivity.this);
//                    inputArea = (LinearLayout) inflater.inflate(R.layout.input_area, null); //parent
//                    editArea = (EditText) inputArea.findViewById(R.id.editArea); //child
//                    new AlertDialog.Builder(CompleteActivity.this)
//                            .setTitle("兴趣")
//                            .setView(inputArea)
//                            .setPositiveButton("确定",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface arg0,
//                                                            int arg1) {
//
//                                            if (editArea.getText().toString().length() != 0) {
//                                                if (editArea.getText().toString().length() > 120) {
//                                                    msg.what = -2;
//                                                    msg.obj = "兴趣不能超过120个字符";
//                                                    handler.sendMessage(msg);
//                                                } else {
//
//                                                    try {
//                                                        String json =
//                                                                postData(MyURL.UPDATE_HOBBY_BY_ID, "hobby", editArea.getText().toString());
//                                                        int code = HttpHelper.getCode(json);
//                                                        if (code == 200) {
//                                                            msg.what = 6;
//                                                            msg.obj = editArea.getText().toString();
//                                                            handler.sendMessage(msg);
//                                                            ///////////////////
//
//
//                                                        } else {
//
//                                                            if (code == 0) {
//                                                                msg.what = -1;
//                                                                msg.obj = "服务器错误，请稍后再试";
//                                                                handler.sendMessage(msg);
//
//                                                            } else {
//                                                                msg.what = 0;
//                                                                msg.obj = code + ":" + HttpHelper.getMsg(json);
//                                                                handler.sendMessage(msg);
//                                                            }
//                                                        }
//
//                                                    } catch (Exception e) {
//                                                        msg.what = -1;
//                                                        msg.obj = "服务器错误，请稍后再试";
//                                                        handler.sendMessage(msg);
//
//                                                    }
//
//                                                }
//                                            }
//
//                                        }
//                                    }
//
//                            )
//                            .setNegativeButton("取消", null)
//                            .show();
//                    Looper.loop();
//                }


            }

        }.start();


    }


    GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            Log.v("onHanlderSuccess", "reqeustCode: " + reqeustCode + "  resultList.size" + resultList.size());
            for (PhotoInfo info : resultList) {
                switch (reqeustCode) {
                    case REQUEST_CODE_GALLERY:
                        picPath = info.getPhotoPath();
                        bitmap = BitmapFactory.decodeFile(info.getPhotoPath());
                        myHead.setImageBitmap(bitmap);
                        try {

                            if (picPath != null) {
                                String json =
                                        postData(picPath);
                                int code = HttpHelper.getCode(json);
                                if (code != 200) {
                                    if (code == 0) {
                                        Common.display(CompleteActivity.this, "服务器错误，请稍后再试");
                                    } else
                                        Common.display(CompleteActivity.this, code + ":" + HttpHelper.getMsg(json));
                                }else{
                                    FileManager.delAllFile(Common.FULL_IMG_CACHE_PATH);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Common.display(CompleteActivity.this, "服务器错误，请稍后再试");
                        }

                        break;
                }
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(CompleteActivity.this, "requestCode: " + requestCode + "  " + errorMsg, Toast.LENGTH_LONG).show();
        }
    };


}

