package com.wao.dogcat.widget;

/**
 * Created by ppssyyy on 2017-02-18.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class GoDieChooseAdapter extends BaseAdapter {
    private int resource;
    private ArrayList<HashMap<String, Object>> data;
    private Context context;
    private ViewHolder holder = null;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private String formatDate = "";
    private int count = 0;
    private Vector<Boolean> isSelectList = new Vector<>();    // 选中与否
    private int lastPosition = -1;            //记录上一次选中的图片位置，-1表示未选中任何图片
    private boolean multiChoose = false;                //表示当前适配器是否允许多选
    public static int userID = -1; //被选中的用户


    public GoDieChooseAdapter(Context context, ArrayList<HashMap<String, Object>> data,
                              int resource) {
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
        for (int i = 0; i < data.size(); i++) {
            isSelectList.add(false);
        }
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
            holder.select = (ImageView) convertView.findViewById(R.id.select);
            holder.item = (LinearLayout) convertView.findViewById(R.id.item);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final String path =
                MyURL.ROOT + data.get(position).get("head").toString();
        holder.name.setText(data.get(position).get("name").toString());

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
        select(isSelectList.elementAt(position));

        holder.item.setOnClickListener(new View.OnClickListener() {

                                           @Override
                                           public void onClick(View view) {
                                               userID = changeState(position);
                                           }
                                       }

        );

        return convertView;

    }

    public void select(boolean isSelect) {
        if (isSelect == true) {
            holder.select.setVisibility(View.VISIBLE);
        } else {
            holder.select.setVisibility(View.GONE);
        }
    }

    public int changeState(int pos) {
        // 多选时
//        if (multiChoose == true) {
//            isSelectList.setElementAt(!isSelectList.elementAt(pos), pos);     //直接取反即可
//        }
//        // 单选时
//        else {
        if (lastPosition != -1)
            isSelectList.setElementAt(false, lastPosition);        //取消上一次的选中状态
        isSelectList.setElementAt(!isSelectList.elementAt(pos), pos);     //直接取反即可
        lastPosition = pos;                //记录本次选中的位置
        //}
        notifyDataSetChanged();         //通知适配器进行更新
        return Integer.parseInt(data.get(lastPosition).get("id").toString());
    }


    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        return super.isEnabled(position);
    }

    private final class ViewHolder {
        public CircleImageView head;
        public TextView name;
        public ImageView select;
        public LinearLayout item;
    }


}
