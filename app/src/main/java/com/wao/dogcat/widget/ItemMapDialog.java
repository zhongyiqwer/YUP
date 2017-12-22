package com.wao.dogcat.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.owo.model.Item;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.wao.dogcat.controller.ItemAdapter;
import com.wao.dogcat.controller.StoreActivity;



import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ppssyyy on 2017-02-18.
 */
public class ItemMapDialog extends Dialog {
    private LinearLayout loading;
    private LinearLayout mainContent;
    private ArrayList<HashMap<String, Object>> msgLists;
    private GridView gv;
    private TextView buyTxt, nodata;
    private TextView cancelX;

    private ArrayList<Item> itemArrayList;
    private Item item;

    private int userID;
    private int status;
    private BaiduMap baiduMap;

    private Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    loadFail(msg.obj.toString());
                    break;
                case 1:
                    ArrayList<HashMap<String, Object>> msgLists =
                            (ArrayList<HashMap<String, Object>>) msg.obj;
                    loadData(msgLists);
                    break;
            }
        }
    };

    public ItemMapDialog(Context context, int style,BaiduMap baiduMap) {
        super(context, style);
        this.context = context;
        this.baiduMap = baiduMap;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_item_map_dialog);


        nodata = (TextView) findViewById(R.id.noData);
        buyTxt = (TextView) findViewById(R.id.buyTxt);
        cancelX = (TextView) findViewById(R.id.cancelX);

        loading = (LinearLayout) findViewById(R.id.loading);
        mainContent = (LinearLayout) findViewById(R.id.mainContent);

        Common.userSP = context.getSharedPreferences("userSP", 0);
        userID = Common.userSP.getInt("ID", 0);
        status = Common.userSP.getInt("status", 0);

        if (Common.isNetworkAvailable(context.getApplicationContext())) {
            initData();
        } else Common.display(context.getApplicationContext(), "请开启手机网络");

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        buyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("extra","map_item");
                intent.setClass(context, StoreActivity.class);
                context.startActivity(intent);
            }
        });

        cancelX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    private void loadFail(String str) {
        nodata.setVisibility(View.VISIBLE);
        nodata.setText(str);
        loading.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);
        if (gv != null) {
            if (gv.getVisibility() == View.VISIBLE) {
                gv.setVisibility(View.GONE);
            }
        }
    }


    private void initData() {
        new Thread() {
            @Override
            public void run() {
                //我的道具
                try {
                    msgLists = HttpHelper.AJItems(postData(), true);
                    if (msgLists != null && msgLists.size() != 0) {
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.obj = msgLists;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = handler.obtainMessage();
                        msg.what = -1;
                        msg.obj = "您未获得任何道具哦";
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = handler.obtainMessage();
                    msg.what = -1;
                    msg.obj = "您未获得任何道具哦";
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }


    private void loadData(final ArrayList<HashMap<String, Object>> msgLists) {
        loading.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);

        gv = (GridView) findViewById(R.id.gview);
        gv.setCacheColorHint(0);
        gv.setVisibility(View.VISIBLE);
        getData(msgLists);

        //我的道具
        ItemMapAdapter adapter = new ItemMapAdapter(context, itemArrayList,
                R.layout.my_item,baiduMap,this);

        adapter.notifyDataSetChanged();
        gv.setAdapter(adapter);


    }





    public String postData() throws Exception {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("userID", userID + "");
        String result = HttpHelper.postData(MyURL.GET_ITEMS_BY_UID, hm, null);
        return result;
    }

    private void getData(ArrayList<HashMap<String, Object>> msgLists) {

        itemArrayList = new ArrayList<>();

        for (int i = 0; i < msgLists.size(); i++) {// list
            item = new Item();
            item.setItemFunction(msgLists.get(i).get("itemfunction").toString());
            item.setItemName(msgLists.get(i).get("itemname").toString());
            item.setItemPrice((int) msgLists.get(i).get("itemprice"));
            item.setItemType((int) msgLists.get(i).get("itemtype"));
            item.setItemImage(msgLists.get(i).get("itemimage").toString());
            item.setItemLevel((int) msgLists.get(i).get("itemLevel"));
            item.setItemPrivilege((int) msgLists.get(i).get("itemPrivilege"));
            item.setItemCount((int)msgLists.get(i).get("count"));
            item.setId((int)msgLists.get(i).get("id"));
            itemArrayList.add(item);
        }

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
           dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
