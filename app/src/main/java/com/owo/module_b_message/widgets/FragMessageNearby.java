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
public class FragMessageNearby extends FragBase {

//    @BindView(R.id.recyclerview)
//    protected RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_message_nearby_layout, container, false);
        ButterKnife.bind(this, view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        List<MessageItem> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(new MessageItem(i));
//        }
//        AdapterRecyclerView adapter = new AdapterRecyclerView(getActivity(), list);
//        mRecyclerView.setAdapter(adapter);
        return view;
    }
}
