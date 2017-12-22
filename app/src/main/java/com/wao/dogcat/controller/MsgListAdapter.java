package com.wao.dogcat.controller;

/**
 * Created by ppssyyy on 2017-02-18.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.owo.model.Msg;
import com.owo.utils.Common;
import com.owo.utils.DateTimeHelper;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;


import com.wao.dogcat.widget.CircleImageView;


import java.util.ArrayList;
import java.util.HashMap;

public class MsgListAdapter extends BaseAdapter {
    private int resource;
    private ArrayList<Msg> data;
    private Context context;
    private ViewHolder holder = null;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private String formatDate = "";
    private String userID;
    private String avatar;

    private String userIDMsg;
    private String avatarMsg;
    private String nameMsg;

    private String path;
    public MsgListAdapter(Context context, ArrayList<Msg> data,
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
                .showImageForEmptyUri(R.drawable.my) //
                .showImageOnFail(R.drawable.my) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
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

            holder.head = (CircleImageView) convertView.findViewById(R.id.head);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.item = (LinearLayout) convertView.findViewById(R.id.item);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        if (type == 0)
//            holder.item.setBackground(
//                    context.getResources().getDrawable(R.drawable.two_side_bar));
//        if (type==1)
//            holder.item.setBackground(
//                    context.getResources().getDrawable(R.drawable.one_side_bar));


        if (data.get(position).getMsgDate().length() != 0) {
            formatDate = DateTimeHelper.timeLogic(DateTimeHelper
                            .timeMillis2FormatTime(data.get(position)
                                    .getMsgDate(), DateTimeHelper.DATE_FORMAT_TILL_SECOND),
                    DateTimeHelper.DATE_FORMAT_TILL_SECOND);
        }

        holder.date.setText(formatDate);


        if (data.get(position).getUserID() == -1){
            path =
                   Common.SYSTEM_MSG_URL;
            holder.name.setText("系统消息");
            if (data.get(position).getMsgType().equals("3")){
                holder.content.setText("现在可以打开TA送你的时间沙漏啦");
            }else {
                holder.content.setText(data.get(position).getMsgContent());
            }
        }else {
            if (data.get(position).getMsgType().equals("3")){
                holder.content.setText("现在可以打开TA送你的时间沙漏啦");
                path = MyURL.ROOT + data.get(position).getUserAvatar();
                holder.name.setText(data.get(position).getUserName());
            }
            else if (data.get(position).getMsgType().equals("21")){
                avatar =  data.get(position).getMsgContent().split("_")[1];
                userID =  data.get(position).getMsgContent().split("_")[2];
                holder.content.setText( data.get(position).getMsgContent().split("_")[0]);
                path =
                        Common.SYSTEM_MSG_URL;
                holder.name.setText("系统消息");
            }
            else if (data.get(position).getMsgType().equals("100")){
                userIDMsg =  data.get(position).getMsgContent().split("_")[1];
                avatarMsg =  data.get(position).getMsgContent().split("_")[2];
                nameMsg =  data.get(position).getMsgContent().split("_")[3];
                holder.content.setText( data.get(position).getMsgContent().split("_")[0]);
                path = MyURL.ROOT + avatarMsg;
                holder.name.setText(nameMsg);
            }
            else {
                holder.content.setText( data.get(position).getMsgContent());
                path = MyURL.ROOT + data.get(position).getUserAvatar();
                holder.name.setText(data.get(position).getUserName());
            }

        }



        //防止图片闪烁
        final String tag = (String) holder.head.getTag();

        if (tag == null || !tag.equals(path)) {
            holder.head.setTag(path);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(path, holder.head, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            holder.head.setTag(path);//确保下载完成再打tag.
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
        }


        ///////网络图片缓存到本地的路径
        // final String localPath = Common.local_pic_path + path.hashCode();


        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("确定删除此消息？")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    HashMap<String,String> hashMap = new HashMap<String, String>();
                                    hashMap.put("id",data.get(position).getMsgId()+"");
                                    int code = 0;
                                    code = HttpHelper.getCode(HttpHelper.postData(MyURL.DELETE_MSG_BY_ID,hashMap,null));
                                    if (code==200){
                                        Intent intent = new Intent();
                                        Activity activity = (Activity) context;
                                        activity.finish();
                                        intent.setClass(activity, MsgActivity.class);
                                        activity.startActivity(intent);
                                    }else Common.display(context,"删除失败");
                                }catch (Exception e){
                                    e.printStackTrace();
                                    Common.display(context,"删除失败：Exception");
                                }
                            }
                        })
                        .show();

                return true;
            }
        });


        holder.item.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               //查看消息详情//////////////////////////
                                               /**
                                                * 1.动态被点赞
                                                * 2.动态被评论
                                                3.时间到，提醒时间沙漏可打开 （接收者收到系统推送）你心爱的时间沙漏可以打开啦，快去看看吧！
                                                4.被人使用攻击道具（自己收到别人的推送） 【godie】你被别人限制行动了！ 【涂鸦】你的照片被人涂鸦了！ 【letsgo】你们的首页被人恶搞了！
                                                *5.--签到奖励
                                                6.挖到宝箱后所得道具／金币
                                                7.埋下的宝被挖后得到奖励金币
                                                8.--被关注（狗专属）（收到推送）
                                                9.被邀请配对（狗专属）（推送给别人）
                                                10.收到对方配对成功消息（情侣）（邀请者收到被邀请者消息）配对成功！
                                                11.发送时间胶囊成功：你已将时间沙漏送给另一半
                                                12 恭喜你已摆脱单身
                                                100 给对方发消息
                                                */

                                               switch (Integer.parseInt(data.get(position).getMsgType())) {
                                                   case 3:
                                                       //
                                                       Intent intent = new Intent();
                                                       Activity activity = (Activity) context;
                                                       activity.finish();
                                                       intent.putExtra("content", data.get(position).getMsgContent());
                                                       intent.setClass(activity, MsgDetailActivity.class);
                                                       activity.startActivity(intent);
                                                       break;
                                                   case 4:
                                                       //
                                                       break;
                                                   case 5:
                                                       //
//                                                       Intent intent1 = new Intent();
//                                                       Activity activity1 = (Activity) context;
//                                                       activity1.finish();
//                                                       intent1.putExtra("extra",5);
//                                                       intent1.setClass(activity1, MsgDetailActivity.class);
//                                                       activity1.startActivity(intent1);
                                                       break;
                                                   case 8:
                                                       //
                                                       break;
                                                   case 21:
                                                       //
//                                                       Intent i = new Intent();
//                                                       Activity a = (Activity) context;
//                                                       a.finish();
//                                                       Bundle bundle = new Bundle();
//                                                       HashMap<String,String> hashMap = new HashMap<String, String>();
//                                                       hashMap.put("username",data.get(position).getUserName());
//                                                       hashMap.put("username",data.get(position).getUserName());
//                                                       i.putExtra("content", data.get(position).getMsgContent());
//                                                       i.putExtra("match",Integer.parseInt(userID));
//                                                       i.putExtra("avatar",avatar);
//                                                       i.setClass(a, AMatchActivity.class);
//                                                       a.startActivity(i);
                                                       break;
                                                   case 100:
                                                       //
                                                       Intent ii = new Intent();
                                                       Activity aa = (Activity) context;
                                                       aa.finish();
                                                       ii.putExtra("id",Integer.parseInt(userIDMsg));
                                                       ii.putExtra("avatar",avatarMsg);
                                                       ii.putExtra("name",nameMsg);
                                                       ii.putExtra("date",data.get(position).getMsgDate());
                                                       ii.putExtra("content", data.get(position).getMsgContent().split("_")[0]);
                                                       ii.setClass(aa, UserMsgDetailActivity.class);
                                                       aa.startActivity(ii);
                                                       break;

                                               }

//                                               Intent intent = new Intent();
//                                               Activity activity = (Activity)context;
//                                               activity.finish();
//                                               intent.setClass(activity, MsgDetailActivity.class);
//                                               activity.startActivity(intent);
//                                               setMsg(data.get(position).getMsgContent(),
//                                                       formatDate,data.get(position).getUserName(),data.get(position).getUserAvatar());
                                           }
                                       }

        );

        return convertView;

    }

    public void setMsg(String content, String date, String username, String avatar) {
        Msg msg = new Msg();
        msg.setMsgDate(date);
        msg.setUserName(username);
        msg.setUserAvatar(avatar);
        msg.setMsgContent(content);
    }


    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        return super.isEnabled(position);
    }

    private final class ViewHolder {
        public CircleImageView head;
        public TextView name, content, date;
        public LinearLayout item;
    }

}
