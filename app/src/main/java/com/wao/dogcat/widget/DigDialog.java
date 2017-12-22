package com.wao.dogcat.widget;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.Marker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;

import java.util.HashMap;


public class DigDialog extends Dialog {
    private Context context;
    private ImageView imageView;
    private Button digBtn, cancelBtn;
    private int status = -1;
    private HashMap<String, Object> treasureHM;
    private Marker marker;
    private TextView goldNum;


    public DigDialog(Context context, int style, HashMap<String, Object> treasureHM, Marker marker) {
        super(context, style);
        this.context = context;
        this.treasureHM = treasureHM;
        this.marker = marker;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_dig_dialog);
        digBtn = (Button) findViewById(R.id.digBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        imageView = (ImageView) findViewById(R.id.image);
        goldNum = (TextView) findViewById(R.id.goldNum);

        Common.userSP = context.getSharedPreferences("userSP", 0);

        status = Common.userSP.getInt("status", 0);

        setCancelable(true);
        setCanceledOnTouchOutside(true);


                if ((int)treasureHM.get("money")!=0){
                    imageView.setImageResource(R.drawable.gold);
                    goldNum.setVisibility(View.VISIBLE);
                    goldNum.setText("X "+treasureHM.get("money").toString());
                }
                else {
                    String url="";
                    switch (Integer.parseInt(treasureHM.get("treasureCode").toString())){
                        case Common.CAM:
                            url="http://ww1.sinaimg.cn/large/0060lm7Tgy1fec8avf8pyj305006oq2w";
                            break;
                        case Common.CAPSULE:
                            url="http://ww1.sinaimg.cn/large/0060lm7Tgy1fec8cak0wmj305006o0t2";
                            break;
                        case Common.DOODLE:
                            url="http://wx1.sinaimg.cn/mw690/8429b3bdly1fec8lb5sctj205006o3yh";
                            break;
                        case Common.GO_DIE:
                            url="http://wx4.sinaimg.cn/mw690/8429b3bdly1fec8lalyztj205006owes";
                            break;
                        case Common.TXT:
                            url="http://ww4.sinaimg.cn/large/0060lm7Tgy1fec8bt6225j305006ot8o";
                            break;
                        case Common.MOVE:
                            url="http://ww4.sinaimg.cn/large/0060lm7Tgy1fec8iek4aoj305006omxg";
                            break;
                        case Common.LETS_GO:
                            url="http://wx4.sinaimg.cn/mw690/8429b3bdly1fec8lbsdldj205006o3yl";
                            break;
                        case Common.POS_CAM:
                            url="http://ww3.sinaimg.cn/large/006HJ39wgy1fey1m2liibj305006odfv";
                            break;
                    }
                    DisplayImageOptions options = new DisplayImageOptions.Builder()//
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                            .showImageForEmptyUri(R.drawable.item_fail)
                            .showImageOnFail(R.drawable.item_fail)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .build();
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                           url, imageView, options);
                }




        digBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("digUserID", Common.userID + "");
                param.put("treasureCode", treasureHM.get("treasureCode").toString());
                param.put("treasureID", treasureHM.get("id").toString());
                try {
                    int code = HttpHelper.getCode(HttpHelper.postData(MyURL.INSERT_DH, param, null));
                    if (code == 200) {
                        if ((int)treasureHM.get("money")!=0)
                        Common.display(context, "金币 +"+(int)treasureHM.get("money"));
                        else   Common.display(context, "宝物已收入囊中~");
                        marker.setVisible(false);
                        dismiss();
                    } else Common.display(context, "挖宝失败 code:" + code);
                } catch (Exception e) {
                    e.printStackTrace();
                    Common.display(context, "挖宝失败:Exception");
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
