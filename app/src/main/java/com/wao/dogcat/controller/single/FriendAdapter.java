package com.wao.dogcat.controller.single;

/**
 * Created by ppssyyy on 2017-02-18.
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.owo.model.User;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_c_detail.AtyDetail;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;

import com.wao.dogcat.widget.CircleImageView;


import java.util.ArrayList;
import java.util.HashMap;

public class FriendAdapter extends BaseAdapter {
    private int resource;
    private ArrayList<User> data;
    private Context context;
    private ViewHolder holder = null;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private String formatDate = "";


    public FriendAdapter(Context context, ArrayList<User> data,
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
        return 4;
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
            holder.info = (TextView) convertView.findViewById(R.id.info);
            holder.sex = (ImageView) convertView.findViewById(R.id.sex);
            holder.activeTime = (TextView) convertView.findViewById(R.id.activeTime);
            holder.item = (LinearLayout) convertView.findViewById(R.id.item);
           // holder.followBtn = (ImageView) convertView.findViewById(R.id.followBtn);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        final String path =
                MyURL.ROOT + data.get(position).getAvatar();
        String age = data.get(position).getAge()>0?data.get(position).getAge()+"岁 ":" ";
        String height = data.get(position).getHeight()>0?data.get(position).getHeight()+"cm ":" ";
        String weight = data.get(position).getWeight()>0?data.get(position).getWeight()+"kg ":" ";
        holder.info.setText(age+height+weight);
        holder.name.setText(data.get(position).getUserName());
        String sex = "";
        sex = data.get(position).getSex();
        if (sex.trim().length() != 0) {
            if (sex.equals(2))
                holder.sex.setImageResource(R.drawable.sex_woman_nearpeople);
        } else holder.sex.setVisibility(View.GONE);


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


        holder.item.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               //好像跳转有点问题 暂时没做这个
                                               BeanUser mBeanUser = new BeanUser();
                                               mBeanUser.setUserName(data.get(position).getUserName());
                                               mBeanUser.setAvatar(data.get(position).getAvatar());
                                               mBeanUser.setId(data.get(position).getId());
                                               mBeanUser.setHeight(data.get(position).getHeight());
                                               mBeanUser.setWeight(data.get(position).getWeight());
                                               mBeanUser.setSex(data.get(position).getSex());
                                               mBeanUser.setBackImage(data.get(position).getBackImage());
                                               mBeanUser.setSignature(data.get(position).getSignature());
                                               mBeanUser.setLevel(data.get(position).getLevel());
                                               mBeanUser.setAge(data.get(position).getAge());
                                               mBeanUser.setHobby(data.get(position).getHobby());

                                               AtyDetail.startAtyDetail(context, AtyDetail.class, mBeanUser);

                                           }
                                       }

        );

        return convertView;

    }


    public String canFollow(String uid, String fid) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("userID", uid);
        paramHM.put("friendID", fid);
        return HttpHelper.postData(MyURL.CHECK_IS_FOLLOWED, paramHM, null);
    }

    public String insertFriend(String uid, String fid) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("userID", uid);
        paramHM.put("friendID", fid);
        String result = HttpHelper.postData(MyURL.INSERT_FRIEND, paramHM, null);
        return result;
    }

    public String deleteFriend(String uid, String fid) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("userID", uid);
        paramHM.put("friendID", fid);
        String result = HttpHelper.postData(MyURL.DELETE_FRIEND, paramHM, null);
        return result;
    }


    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        return super.isEnabled(position);
    }

    private final class ViewHolder {
        public CircleImageView head;
        public TextView name, info, activeTime;
        public ImageView sex;
        public ImageView followBtn;
        public LinearLayout item;
    }


}
