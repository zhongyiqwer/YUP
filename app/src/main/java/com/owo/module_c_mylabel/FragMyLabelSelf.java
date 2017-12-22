package com.owo.module_c_mylabel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.owo.base.FragBase;
import com.owo.module_a_selectlabel.bean.BeanTag;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_b_personal.widgets.FragPersonalViewpagerRight;
import com.owo.module_c_detail.detail_activity.pick_member.PresenterPick;
import com.owo.module_c_detail.detail_activity.pick_member.PresenterPickImpl;
import com.owo.module_c_detail.detail_activity.pick_member.ViewPick;
import com.owo.utils.Common;
import com.owo.utils.MapCalculator;
import com.owo.utils.TagMap;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.wao.dogcat.widget.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author XQF
 * @created 2017/5/23
 */
public class FragMyLabelSelf extends FragBase{


    public static FragMyLabelSelf newInstance() {
        FragMyLabelSelf fragPick = new FragMyLabelSelf();

        return fragPick;
    }

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.noData)
    TextView noData;


    @BindView(R.id.recyclerview_my_label_self)
    RecyclerView recyclerView;


    private List<BeanTag> userList = new ArrayList<>();

    AdapterRecyclerView adapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_my_label_self, container, false);
        ButterKnife.bind(this, view);

        if (FragPersonalViewpagerRight.mUserSelfTag!=null)
        userList.addAll(FragPersonalViewpagerRight.mUserSelfTag);


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

        List<BeanTag> list;
        Context mContext;


        public AdapterRecyclerView(Context context) {
            mContext = context;
            list = new ArrayList<>();
        }




        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.self_label_item, parent, false);
            return new Holder(mContext, view);
        }

        @Override
        public void onBindViewHolder(final Holder holder, final int position) {
            final BeanTag item = list.get(position);
            holder.bind(item);


        }



        @Override
        public int getItemCount() {
            return list.size();
        }

        public void addItems(List<BeanTag> list) {
            this.list = list;

        }
    }


    public class Holder extends RecyclerView.ViewHolder {



        private Context mContext;

        @BindView(R.id.item)
        LinearLayout itemll;

        @BindView(R.id.self_tag_item)
        TextView selftag;



        public Holder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mContext = context;

        }



        public void bind(final BeanTag item) {
            Map<Integer, String> map = new HashMap<>();
            map = TagMap.readFile(getContext(), R.raw.tag_map);

            selftag.setText(map.get(item.getId()));

        }




    }

}
