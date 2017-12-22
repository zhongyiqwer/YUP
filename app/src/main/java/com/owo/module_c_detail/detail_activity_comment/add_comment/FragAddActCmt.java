package com.owo.module_c_detail.detail_activity_comment.add_comment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.base.FragBase;
import com.owo.model.User;
import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_c_comment_user.widgets.AtyCommUser;
import com.owo.module_c_detail.AtyDetail;

import com.owo.utils.Common;
import com.owo.utils.DateTimeHelper;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;

import com.wao.dogcat.widget.CircleImageView;
import com.wao.dogcat.widget.MyRatingBar;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 【活动已结束】进行评价界面
 * @author XQF
 * @created 2017/5/23
 */
public class FragAddActCmt extends FragBase implements ViewAddActCmt{


    public static FragAddActCmt newInstance(BeanTask beanTask) {
        FragAddActCmt fragViewActCmt = new FragAddActCmt();
        mBeanTask = beanTask;
        return fragViewActCmt;
    }

    public static BeanTask mBeanTask;

    @BindView(R.id.offline_add_cmnt_avatar)
    protected CircleImageView head;

    @BindView(R.id.offline_add_cmnt_name)
    protected TextView username;

    @BindView(R.id.offline_add_cmnt_sex)
    protected ImageView sex;

    @BindView(R.id.offline_add_cmnt_age)
    protected TextView age;

    @BindView(R.id.offline_add_cmnt_height)
    protected TextView height;

    @BindView(R.id.offline_add_cmnt_weight)
    protected TextView weight;

    @BindView(R.id.offline_ratingbar)
    MyRatingBar mRatingBar;

    @BindView(R.id.offline_total_score)
    TextView score;

    @BindView(R.id.offline_add_cmnt_content)
    EditText content;

    float totalScore=1;
    int count;


    @OnClick(R.id.comment_user)
    public void comment(){
        Common.half = new User();
        Common.half.setAvatar(mBeanTask.getAvatar());
        Common.half.setSex(mBeanTask.getSex()+"");
        Common.half.setUserName(mBeanTask.getUserName());
        Common.halfId=mBeanTask.getTaskUserID();
        start(getContext(), AtyCommUser.class,1); //我是去评价发布任务的人
    }

    @OnClick(R.id.btnBack)
    public void back(){
        getActivity().finish();
    }


    @OnClick(R.id.offline_add_cmnt)
    public void btn(){
        count++;

        if (count==1) {
            String taskComment = content.getText().toString();

            int code = 0;
            int code2 = 0;

            try {
                code = HttpHelper.getCode(insertTaskComment(userID, mBeanTask.
                        getTaskId(), totalScore, taskComment));
                HashMap<String, String> map = new HashMap<>();
                map.put("userIDs ", userID + "");
                map.put("taskID", mBeanTask.
                        getTaskId() + "");
                map.put("applyStatus", "3");
                String json = HttpHelper.postData(MyURL.SET_APPLY_USERS_TASK_STATE, map, null);
                code2 = HttpHelper.getCode(json);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (code == 200 && code2 == 200) {
                //////////评价成功，跳转至活动结束页面
                AtyDetail.startAtyDetail(getContext(), AtyDetail.class, mBeanTask, AtyDetail.VALUE_TYPE_VIEWATYCMT);
                getActivity().finish();

            } else Common.display(getContext(), "操作失败 CODE：" + code + " " + code2);
        }
    }


    private DisplayImageOptions options1;
    private String path1;
    private int userID;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail_offline_add_cmnt, container, false);
        ButterKnife.bind(this, view);
        Common.userSP = getContext().getSharedPreferences("userSP", 0);
        userID = Common.userSP.getInt("ID",0);

        options1 = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my) //
                .showImageOnFail(R.drawable.my) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//


        path1 = MyURL.ROOT + mBeanTask.getAvatar();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                displayImage(path1, head, options1);
        username.setText(  mBeanTask.getUserName());

        mRatingBar.setClickable(true);//设置可否点击
        mRatingBar.setStepSize(MyRatingBar.StepSize.Half);//设置每次点击增加一颗星还是半颗星
                    mRatingBar.setOnRatingChangeListener(new MyRatingBar.OnRatingChangeListener() {
                        @Override
                        public void onRatingChange(float ratingCount) {//点击星星变化后选中的个数
                            totalScore = ratingCount*2;
                            score.setText(totalScore+"");
                        }
                    });


        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
        return view;
    }


    public String insertTaskComment(int userID,int taskID,float taskScore,String taskComment) throws Exception{
        HashMap<String,String> map = new HashMap<>();
        map.put("userID",userID+"");
        map.put("taskID",taskID+"");
        map.put("taskScore",((int)taskScore)+"");
        map.put("taskComment",taskComment);
        System.out.println("PPPPPPPPARAM="+map);
        String result = HttpHelper.postData(MyURL.INSERT_TASK_COMMENT,map,null);
        return result;
    }


//    @Override
//    public void getUserInfo(BeanUser user) {
//
//    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (mBeanTask.getTaskUserID() == userID) {
                        //活动发起者
                        AtyDetail.startAtyDetail(getContext(), AtyDetail.class, mBeanTask, AtyDetail.VALUE_TYPE_VIEWATYCMT);
                        getActivity().finish();

                    }else{
                        int code = getApplyStatus();
                        if (code!=200 && code!=201){
                            //已评论
                            AtyDetail.startAtyDetail(getContext(), AtyDetail.class, mBeanTask, AtyDetail.VALUE_TYPE_VIEWATYCMT);
                            getActivity().finish();

                        }
                    }
                    break;


            }
        }
    };


    public int getApplyStatus() {
        int code = 0;
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userID", userID + "");
            map.put("taskID", mBeanTask.getTaskId() + "");
            String json = HttpHelper.postData(MyURL.GET_APPLY_STATUS_BY_UID_TID, map, null);
            code = HttpHelper.getCode(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return code;

    }


}
