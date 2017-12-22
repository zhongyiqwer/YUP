package com.owo.module_a_selectlabel;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wao.dogcat.R;
import com.owo.base.FragBase;
import com.owo.module_a_selectlabel.bean.BeanShowTags;
import com.owo.module_a_selectlabel.bean.BeanTag;
import com.owo.module_a_selectlabel.presenter.PresenterSelectLabel;
import com.owo.module_a_selectlabel.presenter.PresenterSelectLabelImpl;
import com.owo.module_a_selectlabel.view.ViewSelectLabel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import zhouyou.flexbox.interfaces.OnFlexboxSubscribeListener;
import zhouyou.flexbox.widget.TagFlowLayout;

/**
 * @author XQF
 * @created 2017/5/24
 */
public class FragSelectLabelFindFriend extends FragBase implements ViewSelectLabel {


    public static FragSelectLabelFindFriend newInstance(String string) {
        FragSelectLabelFindFriend fragSelectLabelFindFriend = new FragSelectLabelFindFriend();
        Bundle args = new Bundle();
        args.putString("self", string);
        fragSelectLabelFindFriend.setArguments(args);
        return fragSelectLabelFindFriend;
    }

    private List<List<BeanTag>> mFindFriendTags;

    private List<BeanShowTags> mBeanShowTagses;
    @BindView(R.id.recyclerview_tag_container1)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.tagview1)
    protected TagContainerLayout mTagContainerLayout;

//    @BindView(R.id.tags_own1)
//    protected EditText mEditText;
//
//    @BindView(R.id.btn_ok1)
//    protected Button mButton;

    private MyAdapter mAdapter;

    private PresenterSelectLabel mPresenterSelectLabel;

    private List<BeanTag> selectedItem;
    private Map<Integer,  List<Integer>> tagMap =new HashMap();

    private String mSelf;


    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_select_label_find_friend, container, false);
        ButterKnife.bind(this, view);
        mPresenterSelectLabel = new PresenterSelectLabelImpl(this);
        mPresenterSelectLabel.loadLabelFindFriend();
        mSelf = (String) getArguments().get("self");
        mBeanShowTagses = new ArrayList<>();
        selectedItem = new ArrayList<>();


        return view;
    }

    @Override
    public void getLabelSelfFormNet(List<List<BeanTag>> resultList) {
        mFindFriendTags = resultList;
        for (int i = 0; i < mFindFriendTags.size(); i++) {
            BeanShowTags beanTags = new BeanShowTags();
            //考虑到假如是女生那么男生部分的内容就是size()==0
            if (mFindFriendTags.size() != 0) {
                if (i == 0) {
                    beanTags.setTitles("通用");
                    beanTags.setTags(mFindFriendTags.get(i));
                } else if (i == 1) {
                    beanTags.setTitles("BOY");
                    beanTags.setTags(mFindFriendTags.get(i));
                } else if (i == 2) {
                    beanTags.setTitles("GIRL");
                    beanTags.setTags(mFindFriendTags.get(i));
                } else if (i == 3) {
                    beanTags.setTitles("兴趣");
                    beanTags.setTags(mFindFriendTags.get(i));
                } else if (i == 4){
                    beanTags.setTitles("运动");
                    beanTags.setTags(mFindFriendTags.get(i));
                }
                mBeanShowTagses.add(beanTags);
            }

        }
        mFindFriendTags.clear();
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
        private boolean load;

        @BindView(R.id.flow_layout)
        protected TagFlowLayout mTagFlowLayout;
        private StringTagAdapter adapter;
        private List<String> dataList;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            dataList = new ArrayList<>();
        }

        public void bind(BeanShowTags beanShowTags) {
            List<BeanTag> listTags = beanShowTags.getTags();
            if (listTags.size()>0) {
                mTextViewTitle.setText(beanShowTags.getTitles());
                adapter = new StringTagAdapter(getActivity(), listTags, null);
                adapter.setOnSubscribeListener(new OnFlexboxSubscribeListener<BeanTag>() {
                    @Override
                    public void onSubscribe(List<BeanTag> selectedItem1) {
                        //获取到选择的除了自定义以外的数据
                        selectedItem.addAll(selectedItem1);
                    }
                });
                mTagFlowLayout.setAdapter(adapter);
            }else cardView.setVisibility(View.GONE);
        }

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


    /////////////////////////////////////
    public String getHobby() {

        if (selectedItem.size()>3) {

            for (int i = 0; i < 5; i++) {
                List<Integer> list = new ArrayList<>();//存id的
                for (int j = 0; j < selectedItem.size(); j++) {
                    if (i == selectedItem.get(j).getType()) {
                        if (!list.contains(selectedItem.get(j).getId())) {
                            list.add(selectedItem.get(j).getId());
                            tagMap.put(selectedItem.get(j).getType(), list);
                        }
                    }
                }
            }


            StringBuffer sb0 = new StringBuffer(mSelf);
            StringBuffer sb = new StringBuffer("\"findFriend\":{");
            int count = 0;
            Iterator it = tagMap.entrySet().iterator();
            while (it.hasNext()) {
                count++;
                Map.Entry entry = (Map.Entry) it.next();
                Integer type = (Integer) entry.getKey();
                List<Integer> tagID = (List<Integer>) entry.getValue();

                sb.append("\"" + (type) + "\":[");

                for (int i = 0; i < tagID.size(); i++) {
                    if (i != tagID.size() - 1) {
                        sb.append("{\"id\":" + tagID.get(i) + "},");
                    } else {
                        sb.append("{\"id\":" + tagID.get(i) + "}");
                    }
                }


                if (count != tagMap.size()) {
                    sb.append("],");
                } else {
                    sb.append("]");
                }

            }

            sb.append("}}");

            return sb0.append("," + sb).toString();
        }else return "";
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    mRecyclerView.setLayoutManager(new MyLayoutManager(getActivity()));
                    mAdapter = new MyAdapter(getActivity());
                    mAdapter.addItems(mBeanShowTagses);
                    mRecyclerView.setAdapter(mAdapter);

                    break;


            }
        }
    };


}
