package com.owo.module_c_detail.detail_activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_c_detail.AtyDetail;
import com.owo.module_c_detail.detail_activity_comment.view_comment.BeanActCmt;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.wao.dogcat.widget.CircleImageView;
import com.wao.dogcat.widget.MyRatingBar;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ppssyyy on 2017-06-13.
 */
public class Holder extends RecyclerView.ViewHolder {

    @BindView(R.id.item)
    CircleImageView head;


    private Context mContext;


    private DisplayImageOptions options;
    private String path;


    public Holder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        mContext = context;
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



    public void bind(final HashMap<String, Object> item) {

        path = MyURL.ROOT+item.get("avatar").toString();

        //防止图片闪烁
        final String tag = (String)head.getTag();

        if (tag == null || !tag.equals(path)) {
            head.setTag(path);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(path, head, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            head.setTag(path);//确保下载完成再打tag.
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
        }

       head.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try {
                   BeanUser beanUser = Common.getBeanUser(item);
                   if (beanUser!=null && beanUser.getId()!=Common.userID)
                       AtyDetail.startAtyDetail(mContext, AtyDetail.class, beanUser);
               }
               catch (Exception e){
                   e.printStackTrace();
               }
           }
       });



    }




}
