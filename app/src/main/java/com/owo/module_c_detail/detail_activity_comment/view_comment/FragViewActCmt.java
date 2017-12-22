package com.owo.module_c_detail.detail_activity_comment.view_comment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.base.FragBase;
import com.owo.module_b_home.bean.BeanTask;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 【活动已结束】查看评论界面
 * @author XQF
 * @created 2017/5/23
 */
public class FragViewActCmt extends FragBase implements ViewActCmt{


    public static FragViewActCmt newInstance(BeanTask beanTask) {
        FragViewActCmt fragViewActCmt = new FragViewActCmt();
        mBeanTask = beanTask;
        return fragViewActCmt;
    }

    public static BeanTask mBeanTask;
    @BindView(R.id.offline_ratingbar)
    protected MyRatingBar stars;

    @BindView(R.id.offline_total_score)
    protected TextView scoreTxt;

    @BindView(R.id.textV_item_activity_status)
    protected TextView userNum;

    @BindView(R.id.textV_item_activity_deadline)
    protected TextView deadline;

    @BindView(R.id.textV_item_activity_where)
    protected TextView where;


    @BindView(R.id.textV_item_activity_money)
    protected TextView money;


    @OnClick(R.id.btnBack)
    public void back(){
        getActivity().finish();
    }


    private DisplayImageOptions options1;
    private String path1;
    private int userID;

    private List<BeanActCmt> cmtList;

    @BindView(R.id.recyclerview_offline_comment)
    RecyclerView mRecyclerView;

    private PresenterActCmt presenterActCmt;

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.noData)
    TextView noData;

    @BindView(R.id.write_cmt_btn)
    Button button;

    private int code;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail_offline_activity_end, container, false);
        ButterKnife.bind(this, view);
        Common.userSP = getContext().getSharedPreferences("userSP", 0);
        userID = Common.userSP.getInt("ID",0);
        presenterActCmt = new PresenterActCmtImpl(this);
        presenterActCmt.getTaskCmtByTID(mBeanTask.getTaskId());

        cmtList = new ArrayList<>();


        userNum.setText("已参加用户："+mBeanTask.getTaskTakenNum()+"/"+mBeanTask.getTaskMaxNum());
        //显示已参加用户列表



        deadline.setText("截止时间：" +
                DateTimeHelper.timeMillis2FormatTime(
                        mBeanTask.getTaskDeadLine(), "yyyy.MM.dd"));
        String latitude = mBeanTask.getTaskLatitude();
        String longitude = mBeanTask.getTaskLongitude();
        where.setText(getWhere(latitude, longitude));

        money.setText("参与者支付："+mBeanTask.getTaskMoney()+"金币");


        return view;
    }

    public String getWhere(String latitude, String longitude) {
        String locStr = "";
        try {
            locStr =
                    HttpHelper.getLocation(
                            HttpHelper.sendGet(MyURL.BAIDU_MAP_URL + "&location="
                                    + latitude + "," + longitude, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locStr;
    }



    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    scoreTxt.setText(mBeanTask.getTaskScore() + "");
                    stars.setClickable(false);//设置可否点击
                    stars.setStar(mBeanTask.getTaskScore()/ 2);//设置显示的星星个数
                    stars.setStepSize(MyRatingBar.StepSize.Half);//设置每次点击增加一颗星还是半颗星

                    if(Long.parseLong(mBeanTask.getTaskDeadLine())< System.currentTimeMillis())
                    {
                        deadline.setTextColor(getContext().getResources().getColor(R.color.textGray));
                    }

                    if (cmtList.size()>0) {
                        loading.setVisibility(View.GONE);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        AdapterRecyclerView adapter = new AdapterRecyclerView(getActivity());
                        adapter.addItems(cmtList);
                        mRecyclerView.setAdapter(adapter);
                    }else{
                        loading.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        noData.setText("暂时没有任何评价");
                    }

                    code = getApplyStatus();

                    if (code!=201){
                        button.setText("该活动已结束");
                        button.setTextColor(getContext().getResources().getColor(R.color.textGray));
                        button.setClickable(false);
                    }

                    break;
            }
        }
    };

    @Override
    public void getActCmt(List<BeanActCmt> list) {
        cmtList = list;
        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();

    }

    public int getApplyStatus(){
        int code = 0;
        try {
            HashMap<String,String> map = new HashMap<>();
            map.put("userID",userID+"");
            map.put("taskID",mBeanTask.getTaskId()+"");
            String json = HttpHelper.postData(MyURL.GET_APPLY_STATUS_BY_UID_TID,map,null);
            code = HttpHelper.getCode(json);
        }catch (Exception e){
            e.printStackTrace();
        }

        return code;

    }

    @OnClick(R.id.write_cmt_btn)
    public void click(){
        if (code == 201){
            AtyDetail.startAtyDetail(getContext(), AtyDetail.class, mBeanTask,AtyDetail.VALUE_TYPE_ADDATYCMT);
            getActivity().finish();
        }
    }


}
