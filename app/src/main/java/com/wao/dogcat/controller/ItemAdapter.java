package com.wao.dogcat.controller;

/**
 * 我的道具
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
import com.owo.module_b_main.AtyMain;
import com.owo.utils.Common;
import com.wao.dogcat.R;


import com.wao.dogcat.widget.UseItemDialog;

import java.util.ArrayList;


public class ItemAdapter extends BaseAdapter {
    private int resource;
    private ArrayList<Item> data;
    private Context context;
    private ViewHolder holder = null;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private String formatDate = "";

    private TextView price, name, typeView, level, privilege, function, buyNum;
    private ImageView image;
    private int currentNum = 0;
    private Button ok;
    private int status=-1;

    public ItemAdapter(Context context, ArrayList<Item> data,
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

        holder.num.setText(data.get(position).getItemCount()+"");

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
                                               final UseItemDialog storeDialog = new UseItemDialog(activity, R.style.MyDialogStyle);
                                               storeDialog.show();

                                               name = (TextView) storeDialog.findViewById(R.id.name);
                                               typeView = (TextView) storeDialog.findViewById(R.id.type);
                                               level = (TextView) storeDialog.findViewById(R.id.level);
                                               privilege = (TextView) storeDialog.findViewById(R.id.privilege);
                                               function = (TextView) storeDialog.findViewById(R.id.function);

                                               image = (ImageView) storeDialog.findViewById(R.id.image);

                                               name.setText("名称：" + data.get(position).getItemName());
                                               typeView.setText("类型：" + Common.ItemType2Str(data.get(position).getItemType()));
                                               level.setText("解锁：" + "Lv."+data.get(position).getItemLevel());
                                               privilege.setText("权限：" + Common.ItemPrivilege2Str(data.get(position).getItemPrivilege()));
                                               function.setText("介绍：" +
                                                       ItemUtil.getItemFunction(data.get(position).getItemFunction(),status,
                                                               data.get(position).getItemPrivilege()));

                                               ok = (Button) storeDialog.findViewById(R.id.btnOk);


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




                                               if (Common.user != null) {
                                                   //判断权限
                                                   boolean flag = true;
                                                   if (status == 1) {
                                                       if (data.get(position).getItemPrivilege() == 1) {
                                                           flag = true;
                                                       }
                                                       else if (data.get(position).getItemPrivilege() == 2) {
                                                           flag = false;
                                                       }
                                                       else {
                                                           flag = true;
                                                       }
                                                   } else {
                                                       if (data.get(position).getItemPrivilege() == 1) {
                                                           flag = true;
                                                       }
                                                       else if (data.get(position).getItemPrivilege() == 2) {
                                                           flag = true;
                                                       }
                                                       else {
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

                                                       } else {
                                                           ok.setBackgroundResource(R.drawable.btn_shape_gray);
                                                           ok.setClickable(false);
                                                           ok.setText("等级不够");
                                                       }

                                                   } else {
                                                       ok.setBackgroundResource(R.drawable.btn_shape_gray);
                                                       ok.setClickable(false);
                                                       ok.setText("无法使用");
                                                   }
                                               }


                                               storeDialog.ok(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {

                                                       holder.num.setText((data.get(position).getItemCount()-1)+"");
                                                       notifyDataSetChanged();

                                                       Intent intent = new Intent();
                                                       Activity activity = (Activity) context;
                                                       switch (data.get(position).getId()) {

                                                           case Common.CAM:
                                                               //照相机
                                                               intent.putExtra("item", Common.CAM);
                                                               break;
                                                           case Common.TXT:
                                                               //小纸条
                                                               intent.putExtra("item", Common.TXT);

                                                               break;
//                                                           case Common.CAPSULE:
//                                                               //时间沙漏
//                                                               intent.putExtra("item", Common.CAPSULE);
//
//                                                               break;
//                                                           case Common.MOVE:
//                                                               //搬家卡
//                                                               intent.putExtra("item", Common.MOVE);
//
//                                                               break;
//                                                           case Common.GO_DIE:
//                                                               //狗die
//                                                               intent.putExtra("item", Common.GO_DIE);
//
//                                                               break;
//                                                           case Common.LETS_GO:
//                                                               //lets狗
//                                                               intent.putExtra("item",Common.LETS_GO);
//
//                                                               break;
//                                                           case Common.DOODLE:
//                                                               //涂鸦笔
//                                                               intent.putExtra("item", Common.DOODLE);
//                                                               break;
//                                                           case Common.POS_CAM:
//                                                               //pose相机
//                                                               intent.putExtra("item", Common.POS_CAM);
//                                                               break;
                                                           default:
                                                               Common.display(context,"暂时不能使用该道具");
                                                               break;

                                                       }

//                                                       if (Common.userSP.getInt("status", 0) == 1)
//                                                           intent.setClass(activity, SingleBasicMapActivity.class);
//                                                       else intent.setClass(activity, CoupleBasicMapActivity.class);
                                                       intent.setClass(activity, AtyMain.class);
                                                       activity.finish();
                                                       activity.startActivity(intent);
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
        public TextView itemPrice, num;
        public LinearLayout item, numberll;
    }


}
