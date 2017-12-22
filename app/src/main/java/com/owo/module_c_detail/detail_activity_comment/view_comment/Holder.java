package com.owo.module_c_detail.detail_activity_comment.view_comment;

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

import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;

import com.wao.dogcat.widget.CircleImageView;
import com.wao.dogcat.widget.MyRatingBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ppssyyy on 2017-06-13.
 */
public class Holder extends RecyclerView.ViewHolder {

    @BindView(R.id.offline_cmnt_portrait)
    CircleImageView head;


    @BindView(R.id.offline_cmnt_username)
    TextView username;

    @BindView(R.id.offline_ratingbar)
    MyRatingBar ratingBar;

    @BindView(R.id.offline_total_score)
    TextView score;

    @BindView(R.id.offline_cmnt_content)
    TextView content;

    @BindView(R.id.cardView_offline_cmnt)
    protected CardView mCardView;

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



    public void bind(final BeanActCmt item) {

        score.setText(item.getTaskscore()+"");
        content.setText(item.getTaskcomment());
        ratingBar.setClickable(false);//设置可否点击
        ratingBar.setStar(item.getTaskscore() / 2);//设置显示的星星个数
        ratingBar.setStepSize(MyRatingBar.StepSize.Half);//设置每次点击增加一颗星还是半颗星



        username.setText(item.getUserName());
        path = MyURL.ROOT+item.getAvator();


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



    }




}
