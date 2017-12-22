package com.wao.dogcat.widget;

/**
 * Created by ppssyyy on 2017-02-18.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.map3d.tools.ItemUtil;
import com.amap.map3d.tools.MapUtil;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.owo.model.Item;
import com.owo.utils.Common;
import com.wao.dogcat.R;



import java.util.ArrayList;
import java.util.HashMap;

public class ItemMapAdapter extends BaseAdapter {
    private int resource;
    private ArrayList<Item> data;
    private Context context;
    private ViewHolder holder = null;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private String formatDate = "";

    private TextView price, name, typeView, level, privilege, function, buyNum, buyTxt;
    private ImageView image;
    private int currentNum = 0;
    private Button ok, mai;
    private BaiduMap baiduMap;
    private LatLng treaLatlng;
    private ItemMapDialog itemMapDialog;
    private int status=-1;

    public ItemMapAdapter(Context context, ArrayList<Item> data,
                          int resource, BaiduMap baiduMap,ItemMapDialog itemMapDialog) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.data = data;
        this.resource = resource;
        this.context = context;
        this.baiduMap = baiduMap;
        this.itemMapDialog = itemMapDialog;
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
        status = Common.userSP.getInt("status",0);
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
            holder.item = (LinearLayout) convertView.findViewById(R.id.item);
            holder.numberll = (LinearLayout) convertView.findViewById(R.id.itemNumIcon);
            holder.num = (TextView) convertView.findViewById(R.id.itemNum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.num.setText(data.get(position).getItemCount() + "");

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


        holder.item.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Activity activity = (Activity) context;
                                               while (activity.getParent() != null) {
                                                   activity = activity.getParent();
                                               }

                                               final UseItemMapDialog storeDialog = new UseItemMapDialog(activity, R.style.MyDialogStyle,
                                                       data.get(position).getItemPrivilege(),data.get(position).getItemLevel());
                                               storeDialog.show();

                                               name = (TextView) storeDialog.findViewById(R.id.name);
                                               typeView = (TextView) storeDialog.findViewById(R.id.type);
                                               level = (TextView) storeDialog.findViewById(R.id.level);
                                               privilege = (TextView) storeDialog.findViewById(R.id.privilege);
                                               function = (TextView) storeDialog.findViewById(R.id.function);


                                               image = (ImageView) storeDialog.findViewById(R.id.image);

                                               name.setText("名称：" + data.get(position).getItemName());
                                               typeView.setText("类型：" + Common.ItemType2Str(data.get(position).getItemType()));
                                               level.setText("解锁：" + "Lv." + data.get(position).getItemLevel());
                                               privilege.setText("权限：" + Common.ItemPrivilege2Str(data.get(position).getItemPrivilege()));
                                               function.setText("介绍：" +
                                                       ItemUtil.getItemFunction(data.get(position).getItemFunction(), Common.userSP.getInt("status", 0),
                                                               data.get(position).getItemPrivilege()));

                                               ok = (Button) storeDialog.findViewById(R.id.useBtn);
                                               //mai = (Button) storeDialog.findViewById(R.id.maiBtn);

                                               final String path = data.get(position).getItemImage();


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

                                                       if (UseItemMapDialog.canUse) {
                                                           holder.num.setText((data.get(position).getItemCount() - 1) + "");
                                                           notifyDataSetChanged();
                                                           ItemUtil.itemManage(data.get(position).getId(), context, baiduMap);
                                                           storeDialog.dismiss();
                                                           itemMapDialog.dismiss();
                                                       }

                                                   }
                                               });


//                                               storeDialog.mai(new View.OnClickListener() {
//                                                   @Override
//                                                   public void onClick(View view) {
//                                                       Activity activity = (Activity) context;
//                                                       while (activity.getParent() != null) {
//                                                           activity = activity.getParent();
//                                                       }
//
//                                                       final UseItemReminderDialog uirb = new UseItemReminderDialog(activity, R.style.MyDialogStyle);
//                                                       uirb.show();
//
//
//                                                       uirb.ok(new View.OnClickListener() {
//                                                           @Override
//                                                           public void onClick(View view) {
//                                                               /////////////////// 埋宝
//                                                               //获取屏幕中心点坐标（圈子的屏幕坐标）
//                                                               Point point = new Point();
//                                                               point.x = Common.screenWidth / 2;
//                                                               point.y = Common.visibleHeight / 2;
//
//                                                               if (baiduMap.getProjection() != null) {
//                                                                   //屏幕坐标转latlng对象
//                                                                   treaLatlng = baiduMap.getProjection().fromScreenLocation(point);
//
//                                                                   new Thread() {
//                                                                       @Override
//                                                                       public void run() {
//                                                                           try {
//                                                                               HashMap<String, String> hm = new HashMap<String, String>();
//                                                                               hm.put("userID", Common.userID + "");
//                                                                               hm.put("longtitude", treaLatlng.longitude + "");
//                                                                               hm.put("latitude", treaLatlng.latitude + "");
//                                                                               hm.put("treasureCode", data.get(position).getId() + "");
//                                                                               int code = HttpHelper.getCode(HttpHelper.postData(MyURL.INSERT_TREASURE, hm, null));
//                                                                               if (code == 200) {
//                                                                                   Message msg = handler.obtainMessage();
//                                                                                   msg.what = 1;
//                                                                                   handler.sendMessage(msg);
//                                                                                   Looper.prepare();
//                                                                                   uirb.dismiss();
//                                                                                   Common.display(context, "埋宝成功！");
//                                                                                   itemMapDialog.dismiss();
//                                                                                   storeDialog.dismiss();
//                                                                                   Looper.loop();
//                                                                               }
//
//
//                                                                           } catch (Exception ee) {
//                                                                               ee.printStackTrace();
//                                                                           }
//
//                                                                       }
//                                                                   }.start();
//
//                                                               }
//                                                           }
//
//                                                       });
//
//
//                                                   }
//
//
//                                               });


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

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MapUtil.insertTreasure(baiduMap, treaLatlng);
                    break;
            }
        }
    };


    private final class ViewHolder {
        public ImageView itemImage;
        public TextView itemPrice, num;
        public LinearLayout item, numberll;
    }


}
