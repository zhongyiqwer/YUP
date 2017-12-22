package com.wao.dogcat.widget;


import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.map3d.tools.MapUtil;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.owo.utils.Common;
import com.owo.utils.MapCalculator;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;


import java.util.HashMap;


public class PublishPicDialog extends Dialog {
    private Context context;
    private Button publishBtn, cancelBtn;
    private int status = 1;
    private String picPath;
    private ImageView imageView;
    private EditText edit;
    private boolean isTxt,isPose;
    private BaiduMap baiduMap;


    public PublishPicDialog(Context context, int style, String picPath, boolean isTxt,boolean isPose,
                            BaiduMap baiduMap) {
        super(context, style);
        this.context = context;
        this.picPath = picPath;
        this.isTxt = isTxt;
        this.isPose = isPose;
        this.baiduMap = baiduMap;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_pulish_pic_dialog);
        publishBtn = (Button) findViewById(R.id.publishBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        imageView = (ImageView) findViewById(R.id.image);
        edit = (EditText) findViewById(R.id.editArea);

        Common.userSP = context.getSharedPreferences("userSP", 0);
       // status = Common.userSP.getInt("status", 0);


        if (!isTxt) {
            if (picPath.length() != 0)
                imageView.setImageBitmap(BitmapFactory.decodeFile(picPath));
        } else imageView.setVisibility(View.GONE);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        publishBtn.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {

                                              Point point = new Point();
                                              point.x = Common.screenWidth / 2;
                                              point.y = Common.visibleHeight / 2;

                                              if (baiduMap.getProjection() != null) {
                                                  //屏幕坐标转latlng对象
                                                  final LatLng latLng
                                                          = baiduMap.getProjection().fromScreenLocation(point);
                                                  int code = 0;
                                                  if (status == 2 && Common.activityRecord != null) {
                                                      if (latLng != null) {
                                                          boolean isIn =
                                                                  MapCalculator.isInCircle(Common.activityRecord.getLatitude(), Common.activityRecord.getLongtitude(),
                                                                          Common.activityRecord.getRadius(), latLng.latitude + "", latLng.longitude + "");
                                                          if (isIn) {
                                                              code = 1;
                                                          } else code = -1;

                                                      } else code = -2;
                                                  }
                                                  if (code != -2) {
                                                      if (isTxt) {
                                                          try {

                                                             final String editStr = edit.getText().toString();

                                                              if (editStr.trim().length()!=0) {

                                                                  HashMap<String, String> param = new HashMap<String, String>();
                                                                  param.put("userID", Common.userID + "");
                                                                  param.put("itemID", Common.TXT + "");
                                                                  param.put("text", editStr);
                                                                  param.put("longtitude", latLng.longitude + "");
                                                                  param.put("latitude", latLng.latitude + "");
                                                                  if (code == 1 && status == 2 && Common.activityRecord != null)
                                                                      param.put("recordsID", Common.activityRecord.getId() + "");
                                                                  if (status == 1 ||(status==2 && code==-1)) {
                                                                      param.put("recordsID", "-1");
                                                                  }

                                                                  System.out.println("小纸条条条："+param);

                                                                  if (HttpHelper.getCode(HttpHelper.postData(MyURL.INSERT_TXT, param, null)) == 200) {
                                                                      if (code == -1 && status == 2) {
                                                                          Common.display(context, "在你圈子外面的小纸条将在一个月后消失哦");
                                                                      }
                                                                      dismiss();
                                                                      new Thread() {
                                                                          @Override
                                                                          public void run() {
                                                                              Message msg = handler.obtainMessage();
                                                                              HashMap<String,String> hashMap = new HashMap<String, String>();
                                                                              hashMap.put("text",editStr);
                                                                              hashMap.put("avatar",Common.user.getAvatar());
                                                                              hashMap.put("publishDate",System.currentTimeMillis()+"");
                                                                              hashMap.put("userName",Common.user.getUserName());
                                                                              Bundle bundle = new Bundle();
                                                                              bundle.putSerializable("myTextInfo",hashMap);
                                                                              msg.setData(bundle);
                                                                              msg.what = 2;
                                                                              msg.obj = latLng;
                                                                              handler.sendMessage(msg);
                                                                          }
                                                                      }.start();

                                                                  }
                                                              }else Common.display(context,"你还没写任何东西哦");

                                                          } catch (Exception e) {
                                                              e.printStackTrace();
                                                              Common.display(context, "ERROR:操作失败！");
                                                          }

                                                      } else {
                                                          try {

                                                              HashMap<String, String> param = new HashMap<String, String>();
                                                              param.put("userID", Common.userID + "");
                                                              if (isPose)
                                                                  param.put("userItemID", Common.POS_CAM + "");
                                                              else
                                                              param.put("userItemID", Common.CAM + "");
                                                              param.put("content", edit.getText().toString());
                                                              param.put("longtitude", latLng.longitude + "");
                                                              param.put("latitude", latLng.latitude + "");
                                                              if (code == 1 && status == 2 && Common.activityRecord != null)
                                                                  param.put("recordsID", Common.activityRecord.getId() + "");
                                                              if (status == 1 ||(status==2 && code==-1)) {
                                                                  code = -1;
                                                                  param.put("recordsID", "-1");
                                                              }
                                                              HashMap<String, String> file = new HashMap<String, String>();
                                                              file.put("photo", picPath);

                                                              System.out.println("照相机急急急："+param+ "file="+file);

                                                              if (HttpHelper.getCode(HttpHelper.postData(MyURL.INSERT_PHOTO, param, file)) == 200) {
                                                                  if (code == -1 && status == 2) {
                                                                      Common.display(context, "在你圈子外面的照片将在一个月后消失哦");
                                                                  }
                                                                  dismiss();

                                                                      new Thread() {
                                                                          @Override
                                                                          public void run() {
                                                                              Message msg = handler.obtainMessage();
                                                                              HashMap<String,String> hashMap = new HashMap<String, String>();
                                                                              hashMap.put("content",edit.getText().toString());
                                                                              hashMap.put("photo",picPath);
                                                                              hashMap.put("avatar",Common.user.getAvatar());
                                                                              hashMap.put("publishDate",System.currentTimeMillis()+"");
                                                                              hashMap.put("userName",Common.user.getUserName());
                                                                              Bundle bundle = new Bundle();
                                                                              bundle.putSerializable("myCamInfo",hashMap);
                                                                              msg.setData(bundle);
                                                                              msg.what = 1;
                                                                              msg.obj = latLng;
                                                                              handler.sendMessage(msg);
                                                                          }
                                                                      }.start();

                                                              }

                                                          } catch (Exception e) {
                                                              e.printStackTrace();
                                                              Common.display(context, "ERROR:操作失败！");
                                                          }

                                                      }
                                                  } else Common.display(context, "ERROR:获取坐标为空");


                                              }


                                          }
                                      }

        );
    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MapUtil.insertMyCam(baiduMap, (LatLng) msg.obj, Common.userSP.getInt("sex", 0), status,msg.getData()); //添加到地图
                    if (!isPose) {
                        try {
                            if (Common.isCamera) {
                                HashMap<String, String> dd = new HashMap<>();
                                dd.put("id", Common.userID + "");
                                dd.put("money", (Common.user.getMoney() + 20) + "");
                                if (HttpHelper.getCode(HttpHelper.postData(MyURL.UPDATE_MONEY_BY_ID, dd, null)) == 200) {
                                    Common.display(context, "金币 +20");
                                    Common.isCamera = false;
                                    Common.user.setMoney(Common.user.getMoney() + 20);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    MapUtil.insertMyTxt(baiduMap, (LatLng) msg.obj, Common.userSP.getInt("sex", 0), status,msg.getData()); //添加到地图
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
