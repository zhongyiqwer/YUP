package com.wao.dogcat.widget;


import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.amap.map3d.tools.ItemUtil;
import com.owo.utils.Common;
import com.owo.utils.DateTimeHelper;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;


import java.util.HashMap;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;


public class PublishCapsuleDialog extends Dialog {
    private Context context;
    private Button publishBtn, cancelBtn, oneMonthBtn, threeMonthBtn, halYearBtn, oneYearBtn;
    private int status = -1;
    private int time = 0;
    private ImageView image;
    private EditText edit;
    private static final int REQUEST_CODE_GALLERY2 = 222;


    public PublishCapsuleDialog(Context context, int style) {
        super(context, style);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_pulish_capsule_dialog);
        publishBtn = (Button) findViewById(R.id.publishBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        oneMonthBtn = (Button) findViewById(R.id.oneMonthBtn);
        threeMonthBtn = (Button) findViewById(R.id.threeMonthBtn);
        halYearBtn = (Button) findViewById(R.id.halfYearBtn);
        oneYearBtn = (Button) findViewById(R.id.oneYearBtn);
        image = (ImageView) findViewById(R.id.cap_image);
        edit = (EditText) findViewById(R.id.editArea);

        setCancelable(true);
        setCanceledOnTouchOutside(true);

        Common.userSP = context.getSharedPreferences("userSP", 0);
        status = Common.userSP.getInt("status", 0);


        setButtonStyle(oneMonthBtn);
        setTime(1);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FunctionConfig functionConfig = new FunctionConfig.Builder()
                        .setEnableCrop(true)
                        .setEnableRotate(true)
                        .setCropSquare(false)
                        .setEnablePreview(true)
                        .setEnableEdit(true)//编辑功能
                        .setEnableCrop(true)//裁剪功能
                        .setEnableCamera(true)//相机功能
                        .setForceCropEdit(true)
                        .setForceCrop(true)
                        .build();
                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY2, functionConfig, mOnHanlderResultCallback);
            }
        });


        oneMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonStyle(oneMonthBtn);
                setTime(1);
            }
        });
        threeMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonStyle(threeMonthBtn);
                setTime(3);
            }
        });

        halYearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonStyle(halYearBtn);
                setTime(6);
            }
        });

        oneYearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonStyle(oneYearBtn);
                setTime(12);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if (Common.half != null) {
                    try {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("userID", Common.userID + "");
                        hm.put("receiverID", 174 + "");
                        hm.put("ItemID", "12");
                        hm.put("openDay", DateTimeHelper.calFutureTime(time));
                        hm.put("content", edit.getText().toString());
                        HashMap<String, String> file = new HashMap<String, String>();
                        file.put("photo", ItemUtil.imagePath2);
                        if (ItemUtil.imagePath2.length()==0){
                            file = null;
                        }
                        int code = HttpHelper.getCode(
                                HttpHelper.postData(MyURL.INSERT_CAPSULE, hm, file));
                        if (code == 200) {
                            Common.display(context, "你已把时间沙漏送给另一半");
                            dismiss();
                        } else if (code ==201){
                            Common.display(context,"要等他打开之前的沙漏才能继续送哦...");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Common.display(context, "ERROR:Exception");
                    }
               // } else Common.display(context, "获取对象信息失败");

            }
        });

    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }


    public void publishBtn(final View.OnClickListener listener) {
        publishBtn.setOnClickListener(listener);
    }

    public void image(final View.OnClickListener listener) {
        image.setOnClickListener(listener);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setButtonStyle(Button selectedBtn) {
        Button[] buttons = {oneMonthBtn, threeMonthBtn, halYearBtn, oneYearBtn};
        for (int i = 0; i < buttons.length; i++) {

                buttons[i].setBackgroundResource(R.drawable.btn_capsule_empty_yellow);
                buttons[i].setTextColor(context.getResources().getColor(R.color.themeColor));


            if (buttons[i].equals(selectedBtn)) {
                    buttons[i].setBackgroundResource(R.drawable.btn_capsule_yellow);
                    buttons[i].setTextColor(context.getResources().getColor(R.color.themeColor));
            }
        }
    }


    public GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            Log.v("onHanlderSuccess", "reqeustCode: " + reqeustCode + "  resultList.size" + resultList.size());
            for (PhotoInfo info : resultList) {
                switch (reqeustCode) {
                    case REQUEST_CODE_GALLERY2:
                        ItemUtil.imagePath2 = info.getPhotoPath();
                        if (ItemUtil.imagePath2.length() != 0)
                            image.setImageBitmap(BitmapFactory.decodeFile(ItemUtil.imagePath2));
                        break;
                }
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
        }
    };

}
