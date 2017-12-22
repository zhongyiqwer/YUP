package com.wao.dogcat.controller;

/**
 * 商店
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.amap.map3d.tools.ItemUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.owo.model.Item;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;

import com.wao.dogcat.widget.StoreDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreAdapter extends BaseAdapter {
    private int resource;
    private ArrayList<Item> data;
    private Context context;
    private ViewHolder holder = null;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private String formatDate = "";

    private TextView price, name, typeView, level, privilege, function, buyNum;
    private ImageView image, minus, add;
    private int buyCount = 1;
    private int userMoney = 0;
    private int spendMoney = 0;
    private Button ok;
    private int status = -1;

    public StoreAdapter(Context context, ArrayList<Item> data,
                        int resource, String[] from, int[] to) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.data = data;
        this.resource = resource;
        this.context = context;
        // 使用ImageLoader加载网络图片
        options = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.item_fail) //
                .showImageOnFail(R.drawable.item_fail) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//

        Common.userSP = context.getSharedPreferences("userSP", 0);
        status = Common.userSP.getInt("status", 0);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else return 1;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        final int type = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(resource, null);

            holder.itemImage = (ImageView) convertView.findViewById(R.id.gvImg);
            holder.itemPrice = (TextView) convertView.findViewById(R.id.price);
            holder.item = (LinearLayout) convertView.findViewById(R.id.item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.itemPrice.setText(data.get(position).getItemPrice() + "");


        final String path =
                data.get(position).getItemImage();


        //防止图片闪烁
        final String tag = (String) holder.itemImage.getTag();

        if (tag == null || !tag.equals(path)) {
            holder.itemImage.setTag(path);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(path, holder.itemImage, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            holder.itemImage.setTag(path);//确保下载完成再打tag.
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
        }

        ///////网络图片缓存到本地的路径
        // final String localPath = Common.local_pic_path + path.hashCode();


        holder.item.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Activity activity = (Activity) context;
                                               while (activity.getParent() != null) {
                                                   activity = activity.getParent();
                                               }
                                               final StoreDialog storeDialog = new StoreDialog(activity, R.style.MyDialogStyle);
                                               storeDialog.show();

                                               price = (TextView) storeDialog.findViewById(R.id.price);
                                               name = (TextView) storeDialog.findViewById(R.id.name);
                                               typeView = (TextView) storeDialog.findViewById(R.id.type);
                                               level = (TextView) storeDialog.findViewById(R.id.level);
                                               privilege = (TextView) storeDialog.findViewById(R.id.privilege);
                                               function = (TextView) storeDialog.findViewById(R.id.function);
                                               buyNum = (TextView) storeDialog.findViewById(R.id.buyNum);
                                               image = (ImageView) storeDialog.findViewById(R.id.image);
                                               minus = (ImageView) storeDialog.findViewById(R.id.minus);
                                               add = (ImageView) storeDialog.findViewById(R.id.add);

                                               buyCount = 1;
                                               buyNum.setText(buyCount + "");
                                               price.setText(data.get(position).getItemPrice() + "");
                                               name.setText("名称：" + data.get(position).getItemName());
                                               typeView.setText("类型：" + Common.ItemType2Str(data.get(position).getItemType()));
                                               level.setText("解锁：" + "Lv." + data.get(position).getItemLevel());
                                               privilege.setText("权限：" + Common.ItemPrivilege2Str(data.get(position).getItemPrivilege()));
                                               function.setText("介绍：" +
                                                       ItemUtil.getItemFunction(data.get(position).getItemFunction(), Common.userSP.getInt("status", 0),
                                                               data.get(position).getItemPrivilege()));

                                               ok = (Button) storeDialog.findViewById(R.id.btnOk);

                                               int id = data.get(position).getId();
                                               if (id==12 ||id==13 || id==14 || id==15 || id==16 || id==17 || id ==18 )
                                               {
                                                   ok.setBackgroundResource(R.drawable.btn_shape_gray);
                                                   ok.setClickable(false);
                                                   ok.setText("暂未开放");
                                               }else {
                                                   if (Common.user != null) {
                                                       //判断权限
                                                       boolean flag = true;
                                                       if (status == 1) {
                                                           if (data.get(position).getItemPrivilege() == 1) {
                                                               flag = true;
                                                           } else if (data.get(position).getItemPrivilege() == 2) {
                                                               flag = false;
                                                           } else {
                                                               flag = true;
                                                           }
                                                       } else {
                                                           if (data.get(position).getItemPrivilege() == 1) {
                                                               flag = true;
                                                           } else if (data.get(position).getItemPrivilege() == 2) {
                                                               flag = true;
                                                           } else {
                                                               flag = false;
                                                           }
                                                       }
                                                       if (flag) {
                                                           ok.setClickable(true);
                                                           boolean f = true;
                                                           if (data.get(position).getItemLevel() > Common.user.getLevel()) {
                                                               f = false;

                                                           }
                                                           //判断等级
                                                           if (f) {
                                                               ok.setClickable(true);

                                                               //判断是否有足够金币
                                                               userMoney = Common.user.getMoney();
                                                               spendMoney = Integer.parseInt(buyNum.getText().toString()) * data.get(position).getItemPrice();
                                                               if (userMoney - spendMoney < 0) {
                                                                   ok.setBackgroundResource(R.drawable.btn_shape_gray);
                                                                   ok.setClickable(false);
                                                                   ok.setText("金币不足");
                                                               } else {
                                                                   ok.setClickable(true);
                                                                   ok.setBackgroundResource(R.drawable.bg_btn_next_step);


                                                               }

                                                           } else {
                                                               ok.setBackgroundResource(R.drawable.btn_shape_gray);
                                                               ok.setClickable(false);
                                                               ok.setText("等级不够");
                                                           }

                                                       } else {
                                                           ok.setBackgroundResource(R.drawable.btn_shape_gray);
                                                           ok.setClickable(false);
                                                           ok.setText("无使用权限");
                                                       }


                                                   }
                                               }


                                               final String path =
                                                       data.get(position).getItemImage();


                                               //防止图片闪烁
                                               final String tag = (String) image.getTag();

                                               if (tag == null || !tag.equals(path)) {
                                                   image.setTag(path);
                                                   com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                                                           .displayImage(path, image, options, new ImageLoadingListener() {
                                                               @Override
                                                               public void onLoadingStarted(String s, View view) {

                                                               }

                                                               @Override
                                                               public void onLoadingFailed(String s, View view, FailReason failReason) {

                                                               }

                                                               @Override
                                                               public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                                                   image.setTag(path);//确保下载完成再打tag.
                                                               }

                                                               @Override
                                                               public void onLoadingCancelled(String s, View view) {

                                                               }
                                                           });
                                               }


                                               storeDialog.ok(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       int id = data.get(position).getId();
                                                       if (id==12 ||id==13 || id==14 || id==15 || id==16 || id==17 || id ==18 )
                                                       {
                                                           ok.setBackgroundResource(R.drawable.btn_shape_gray);
                                                           ok.setClickable(false);
                                                           ok.setText("暂未开放");
                                                       }else {
                                                           if (Common.user != null) {
                                                               //判断权限
                                                               boolean flag = true;
                                                               if (status == 1) {
                                                                   if (data.get(position).getItemPrivilege() == 1) {
                                                                       flag = true;
                                                                   } else if (data.get(position).getItemPrivilege() == 2) {
                                                                       flag = false;
                                                                   } else {
                                                                       flag = true;
                                                                   }
                                                               } else {
                                                                   if (data.get(position).getItemPrivilege() == 1) {
                                                                       flag = true;
                                                                   } else if (data.get(position).getItemPrivilege() == 2) {
                                                                       flag = true;
                                                                   } else {
                                                                       flag = false;
                                                                   }
                                                               }
                                                               if (flag) {
                                                                   ok.setClickable(true);
                                                                   ok.setBackgroundResource(R.drawable.btn_shape_yellow);

                                                                   boolean f = true;
                                                                   if (data.get(position).getItemLevel() > Common.user.getLevel()) {
                                                                       f = false;

                                                                   }
                                                                   //判断等级
                                                                   if (f) {
                                                                       ok.setClickable(true);
                                                                       ok.setBackgroundResource(R.drawable.btn_shape_yellow);


                                                                       //判断是否有足够金币
                                                                       userMoney = Common.user.getMoney();
                                                                       spendMoney = Integer.parseInt(buyNum.getText().toString()) * data.get(position).getItemPrice();
                                                                       if (userMoney - spendMoney < 0) {
                                                                           ok.setBackgroundResource(R.drawable.btn_shape_gray);
                                                                           ok.setClickable(false);
                                                                           ok.setText("金币不足");
                                                                       } else {
                                                                           ok.setClickable(true);
                                                                           ok.setBackgroundResource(R.drawable.btn_shape_yellow);

                                                                           try {
                                                                               HashMap<String, String> param = new HashMap<String, String>();
                                                                               param.put("id", Common.userID + "");
                                                                               param.put("money", (userMoney - spendMoney + ""));
                                                                               HttpHelper.getCode(HttpHelper
                                                                                       .postData(MyURL.UPDATE_MONEY_BY_ID, param, null));
                                                                               //一次把钱更新完

                                                                               int code = 0;
                                                                               for (int i = 0; i < Integer.parseInt(buyNum.getText().toString()); i++) {
                                                                                   HashMap<String, String> qq = new HashMap<>();
                                                                                   qq.put("userID", Common.userID + "");
                                                                                   qq.put("itemID", data.get(position).getId() + "");
                                                                                   code = HttpHelper.getCode(HttpHelper.postData(MyURL.INSERT_USER_ITEM, qq, null));
                                                                               }
                                                                               if (code == 200) {
                                                                                   Common.user.setMoney(userMoney - spendMoney);
                                                                                   Common.display(context, "购买成功");
                                                                                   storeDialog.dismiss();
                                                                               } else {
                                                                                   Common.display(context, "购买失败：code " + code);
                                                                                   storeDialog.dismiss();
                                                                               }


                                                                           } catch (Exception e) {
                                                                               e.printStackTrace();
                                                                               Common.display(context, "购买失败：Exception");
                                                                               storeDialog.dismiss();
                                                                           }


                                                                       }

                                                                   } else {
                                                                       ok.setBackgroundResource(R.drawable.btn_shape_gray);
                                                                       ok.setClickable(false);
                                                                       ok.setText("等级不够");
                                                                   }

                                                               } else {
                                                                   ok.setBackgroundResource(R.drawable.btn_shape_gray);
                                                                   ok.setClickable(false);
                                                                   ok.setText("无使用权限");
                                                               }


                                                           }
                                                           else Common.display(context, "购买失败：user is null");
                                                       }

                                                   }
                                               });
                                               storeDialog.minus(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       int id = data.get(position).getId();
                                                       if (id==10 ||id==11 ) {

                                                           new Thread() {
                                                               @Override
                                                               public void run() {
                                                                   Message msg = handler.obtainMessage();
                                                                   msg.what = 1;
                                                                   msg.obj = position;
                                                                   handler.sendMessage(msg);
                                                               }
                                                           }.start();
                                                       }

                                                   }
                                               });
                                               storeDialog.add(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       int id = data.get(position).getId();
                                                       if (id==10 ||id==11 ) {
                                                           new Thread() {
                                                               @Override
                                                               public void run() {
                                                                   Message msg = handler.obtainMessage();
                                                                   msg.what = 2;
                                                                   msg.obj = position;
                                                                   handler.sendMessage(msg);
                                                               }
                                                           }.start();
                                                       }

                                                   }
                                               });

                                           }
                                       }

        );

        return convertView;

    }


    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        return super.isEnabled(position);
    }

    private final class ViewHolder {
        public ImageView itemImage;
        public TextView itemPrice;
        public LinearLayout item;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (buyCount > 0)
                        buyCount--;
                    buyNum.setText(buyCount + "");
                    int position = (int) msg.obj;
                    if (Common.user != null) {
                        userMoney = Common.user.getMoney();
                        spendMoney = Integer.parseInt(buyNum.getText().toString()) * data.get(position).getItemPrice();
                        if (userMoney - spendMoney < 0) {
                            ok.setBackgroundResource(R.drawable.btn_shape_gray);
                            ok.setClickable(false);
                            ok.setText("金币不足");
                        } else {
                            ok.setClickable(true);
                            ok.setBackgroundResource(R.drawable.btn_shape_yellow);

                        }
                    }
                    break;
                case 2:
                    buyCount++;
                    buyNum.setText(buyCount + "");
                    int p = (int) msg.obj;
                    if (Common.user != null) {
                        userMoney = Common.user.getMoney();
                        spendMoney = Integer.parseInt(buyNum.getText().toString()) * data.get(p).getItemPrice();
                        if (userMoney - spendMoney < 0) {
                            ok.setBackgroundResource(R.drawable.btn_shape_gray);
                            ok.setClickable(false);
                            ok.setText("金币不足");
                        } else {
                            ok.setClickable(true);
                            ok.setBackgroundResource(R.drawable.btn_shape_yellow);
                        }
                    }
                    break;
            }
        }
    };

}
