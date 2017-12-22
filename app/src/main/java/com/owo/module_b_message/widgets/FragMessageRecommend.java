package com.owo.module_b_message.widgets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wao.dogcat.R;
import com.owo.base.FragBase;

import butterknife.ButterKnife;

/**
 * @author XQF
 * @created 2017/5/11
 */
public class FragMessageRecommend extends FragBase {
    @Nullable

//    @BindView(R.id.recyclerview1)
//    protected RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_message_recommend_layout, container, false);
        ButterKnife.bind(this, view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        List<BeanMessageRecommend> list = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            list.add(new BeanMessageRecommend());
//        }
//        AdapterFragMessageRecommend adapter = new AdapterFragMessageRecommend(getActivity(), list);
//        mRecyclerView.setAdapter(adapter);
        return view;
    }
}
