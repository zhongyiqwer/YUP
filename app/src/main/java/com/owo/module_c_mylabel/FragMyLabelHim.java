package com.owo.module_c_mylabel;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.owo.base.FragBase;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.bean.BeanUserCommentLabel;
import com.owo.module_b_personal.widgets.FragPersonalViewpagerRight;
import com.wao.dogcat.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author XQF
 * @created 2017/5/23
 */
public class FragMyLabelHim extends FragBase{


    public static FragMyLabelHim newInstance() {
        FragMyLabelHim fragPick = new FragMyLabelHim();

        return fragPick;
    }

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.noData)
    TextView noData;


    @BindView(R.id.recyclerview_my_label_him)
    RecyclerView recyclerView;


    private List<BeanUserCommentLabel> userList = new ArrayList<>();

    AdapterRecyclerView adapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_my_label_him, container, false);
        ButterKnife.bind(this, view);

        if (FragPersonalViewpagerRight.mUserLabels!=null)
        userList.addAll(FragPersonalViewpagerRight.mUserLabels);



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



    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (userList.size()>0) {
                        loading.setVisibility(View.GONE);
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
                        adapter = new AdapterRecyclerView(getActivity());
                        adapter.addItems(userList);
                        recyclerView.setAdapter(adapter);
                    }else {
                        loading.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        noData.setText("暂时没有任何标签");
                    }

                    break;
            }
        }
    };



    public class AdapterRecyclerView extends RecyclerView.Adapter<Holder> {

        List<BeanUserCommentLabel> list;
        Context mContext;


        public AdapterRecyclerView(Context context) {
            mContext = context;
            list = new ArrayList<>();
        }




        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.him_label_item, parent, false);
            return new Holder(mContext, view);
        }

        @Override
        public void onBindViewHolder(final Holder holder, final int position) {
            final BeanUserCommentLabel item = list.get(position);
            holder.bind(item);


        }



        @Override
        public int getItemCount() {
            return list.size();
        }

        public void addItems(List<BeanUserCommentLabel> list) {
            this.list = list;

        }
    }


    public class Holder extends RecyclerView.ViewHolder {



        private Context mContext;

        @BindView(R.id.item)
        LinearLayout itemll;

        @BindView(R.id.him_tag_item)
        FrameLayout himTag;

        @BindView(R.id.him_tag_item_txt)
        TextView himTagTxt;

        @BindView(R.id.tagNum)
        TextView tagNum;



        public Holder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mContext = context;

        }



        public void bind(final BeanUserCommentLabel item) {
            himTagTxt.setText(item.getLabel());
            tagNum.setText(item.getLabelNum());

        }




    }

}
