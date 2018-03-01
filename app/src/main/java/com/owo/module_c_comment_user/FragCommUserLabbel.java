package com.owo.module_c_comment_user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.base.FragBase;
import com.owo.module_a_selectlabel.bean.BeanShowTags;
import com.owo.module_a_selectlabel.bean.BeanTag;
import com.owo.module_c_comment_user.presenter.PresenterCommUser;
import com.owo.module_c_comment_user.presenter.PresenterCommUserImpl;
import com.owo.module_c_comment_user.view.ViewCommUser;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.wao.dogcat.controller.CompleteActivity;
import com.wao.dogcat.widget.CircleImageView;
import com.wao.dogcat.widget.MyRatingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.lujun.androidtagview.TagContainerLayout;
import zhouyou.flexbox.interfaces.OnFlexboxSubscribeListener;
import zhouyou.flexbox.widget.TagFlowLayout;

/**
 * @author XQF
 * @created 2017/5/24
 */
public class FragCommUserLabbel extends FragBase implements ViewCommUser {

    public static String sex;
    private LinearLayout input;
    private EditText edit;

    public static FragCommUserLabbel newInstance(String sex1) {
        sex = sex1;
        return new FragCommUserLabbel();
    }


    private String mSelfResult;
    private List<List<BeanTag>> mSelfTags;

    private List<BeanShowTags> mBeanShowTagses;
    @BindView(R.id.recyclerview_tag_container)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.tagview)
    protected TagContainerLayout mTagContainerLayout;

//    @BindView(R.id.tags_own)
//    protected EditText mEditText;
//
//    @BindView(R.id.btn_ok)
//    protected Button mButton;

    private MyAdapter mAdapter;

    private PresenterCommUser mPresenterSelectLabel;

    private List<BeanTag> selectedItem;
    private Map<Integer,  List<Integer>> tagMap =new HashMap();
    private ArrayList<String> tags = new ArrayList<>();

    @BindView(R.id.comm_user_avatar)
    CircleImageView avatar;

    @BindView(R.id.comm_user_username)
    TextView username;

    @BindView(R.id.comm_user_score)
    TextView score;

    @BindView(R.id.comm_user_ratingbar)
    MyRatingBar ratingBar;

    private DisplayImageOptions options;
    private String path;

    float totalScore;

    @OnClick(R.id.add_new_tag)
    public void addTag(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        input = (LinearLayout) inflater.inflate(R.layout.input_value, null); //parent
        edit = (EditText) input.findViewById(R.id.edit); //child
        edit.setHint("多个标签间用空格隔开");
        new AlertDialog.Builder(getContext())
                .setTitle("添加新标签")
                .setView(input)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {

                                if (edit.getText().toString().length() != 0) {
                                    if (edit.getText().toString().length() > 120) {
                                        Common.display(getContext(),"不能超过120个字符");
                                    } else {
                                        String str[] = edit.getText().toString().split(" ");
                                        for (int i=0;i<str.length;i++){
                                            if (str[i].trim().length()!=0){
                                                tags.add(str[i]);
                                            }
                                        }


                                    }
                                }

                            }
                        }

                )
                .setNegativeButton("取消", null)
                .show();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_comm_user_label, container, false);
        ButterKnife.bind(this, view);
        mPresenterSelectLabel = new PresenterCommUserImpl(this);
        mPresenterSelectLabel.loadLabelSelfBySex(sex);
        mBeanShowTagses = new ArrayList<>();
        selectedItem = new ArrayList<>();

        options = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my) //
                .showImageOnFail(R.drawable.my) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//

        path = MyURL.ROOT + Common.half.getAvatar();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().
                displayImage(path, avatar, options);
        username.setText(  Common.half.getUserName());

        ratingBar.setClickable(true);//设置可否点击
        ratingBar.setStepSize(MyRatingBar.StepSize.Half);//设置每次点击增加一颗星还是半颗星
        ratingBar.setOnRatingChangeListener(new MyRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {//点击星星变化后选中的个数
                totalScore = ratingCount*2;
                score.setText(totalScore+"");
            }
        });

        return view;
    }


    @Override
    public void getLabelSelfFormNet(List<List<BeanTag>> resultList) {
        mSelfTags = resultList;

        for (int i = 0; i < mSelfTags.size(); i++) {
            BeanShowTags beanTags = new BeanShowTags();
            //考虑到假如是女生那么男生部分的内容就是size()==0
            if (mSelfTags.size() != 0) {
                if (i == 0) {
                    beanTags.setTitles("通用");
                    beanTags.setTags(mSelfTags.get(i));
                } else if (i == 1) {
                    beanTags.setTitles("BOY");
                    beanTags.setTags(mSelfTags.get(i));
                } else if (i == 2) {
                    beanTags.setTitles("GIRL");
                    beanTags.setTags(mSelfTags.get(i));
                } else if (i == 3) {
                    beanTags.setTitles("兴趣");
                    beanTags.setTags(mSelfTags.get(i));
                } else if (i == 4) {
                    beanTags.setTitles("运动");
                    beanTags.setTags(mSelfTags.get(i));
                }
                mBeanShowTagses.add(beanTags);
            }
        }

        mSelfTags.clear();

        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tags_title)
        protected TextView mTextViewTitle;

        @BindView(R.id.tags_card)
        protected CardView cardView;

        @BindView(R.id.flow_layout)
        protected TagFlowLayout mTagFlowLayout;
        private StringTagAdapter adapter;

        private List<String> dataList;
        private boolean load = false;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            dataList = new ArrayList<>();
        }

        public void bind(BeanShowTags beanShowTags) {
            List<BeanTag> listTags = beanShowTags.getTags();
            if (listTags.size() > 0) {
                mTextViewTitle.setText(beanShowTags.getTitles());
                adapter = new StringTagAdapter(getActivity(), listTags, null);
                adapter.setOnSubscribeListener(new OnFlexboxSubscribeListener<BeanTag>() {
                    @Override
                    public void onSubscribe(List<BeanTag> selectedItem1) {
                        //获取到选择的除了自定义以外的数据
                        System.out.println("one"+selectedItem1.size());
                        selectedItem.addAll(selectedItem1);
                    }
                });
                mTagFlowLayout.setAdapter(adapter);
            } else cardView.setVisibility(View.GONE);
        }

    }

//问题描述为：第一个点击标签开始为1，当点击第二个标签后第一个就变为2，以此类推
    public ArrayList<HashMap<String,String>>getParams(){
        ArrayList<HashMap<String,String>> allParams = new ArrayList<>();
        if (selectedItem.size()>0) {

            System.out.println("two selectedItem"+selectedItem.size());
            List<BeanTag> selectedItem2 = new ArrayList<>();
            for (int i=0;i<selectedItem.size();i++){
                if (!selectedItem2.contains(selectedItem.get(i))){
                    selectedItem2.add(selectedItem.get(i));
                }
            }
            System.out.println("three selectedItem2"+selectedItem2.size());

            for (int i = 0; i < selectedItem2.size(); i++) {
                HashMap<String, String> param = new HashMap<>();
                param.put("userID", Common.halfId + "");
                param.put("userScore",((int)totalScore)+"" );
                param.put("userLabels", selectedItem2.get(i).getTag());
                allParams.add(param);
            }
        }

        if (tags.size()>0){
            System.out.println("four tags"+tags.size());
            for (int i = 0; i < tags.size(); i++) {
                HashMap<String, String> param = new HashMap<>();
                param.put("userID", Common.halfId + "");
                param.put("userScore", ((int)totalScore)+"" );
                param.put("userLabels", tags.get(i));
                allParams.add(param);
            }
        }

        return allParams;

    }

    class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        private Context mContext;
        private List<BeanShowTags> mBeanShowTagses;

        public MyAdapter(Context context) {
            mContext = context;
            mBeanShowTagses = new ArrayList<>();
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.frag_selectlabel_recycleritem, parent, false);
            return new MyHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            BeanShowTags beanShowTags = mBeanShowTagses.get(position);
            holder.bind(beanShowTags);
        }

        @Override
        public int getItemCount() {
            return mBeanShowTagses.size();
        }

        public void addItems(List<BeanShowTags> list) {
            mBeanShowTagses = list;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    System.out.println("TTTTTTTTTAG=" + mBeanShowTagses);

                    mRecyclerView.setLayoutManager(new MyLayoutManager(getActivity()));
                    mAdapter = new MyAdapter(getActivity());
                    mAdapter.addItems(mBeanShowTagses);
                    mRecyclerView.setAdapter(mAdapter);


                    break;


            }
        }
    };
}
