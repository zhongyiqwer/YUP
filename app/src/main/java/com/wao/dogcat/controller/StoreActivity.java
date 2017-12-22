package com.wao.dogcat.controller;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;


import com.owo.model.Item;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * 道具相关
 */
public class StoreActivity extends Activity implements View.OnClickListener {
    private ImageView imgBack, itemSelect;
    private LinearLayout loading, navBar;
    private LinearLayout mainContent;
    private ArrayList<HashMap<String, Object>> msgLists;
    private GridView gv;
    private TextView title,nodata;

    private ArrayList<Item> itemArrayList;
    private Item item;

    private int userID;
    private int status;
    private int count = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    loadFail(msg.obj.toString());
                    break;
                case 1:
                    //我的道具
                    if (nodata.getVisibility()==View.VISIBLE)
                        nodata.setVisibility(View.GONE);
                    ArrayList<HashMap<String, Object>> msgLists =
                            (ArrayList<HashMap<String, Object>>) msg.obj;
                    loadData(msgLists,false);
                    break;
                case 11:
                    //道具商城
                    if (nodata.getVisibility()==View.VISIBLE)
                        nodata.setVisibility(View.GONE);
                    ArrayList<HashMap<String, Object>> itemLists =
                            (ArrayList<HashMap<String, Object>>) msg.obj;
                    loadData(itemLists,true);
                    break;
                case 2:
                    itemSelect.setImageResource(R.drawable.item_selectframe_left_yellow);
                    initData(false);
                    break;
                case 3:
                    itemSelect.setImageResource(R.drawable.item_selectframe_right_yellow);
                    initData(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.grid);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        imgBack = (ImageView) findViewById(R.id.btnBack);
        imgBack.setOnClickListener(this);
        itemSelect = (ImageView) findViewById(R.id.item_select);
        nodata = (TextView) findViewById(R.id.nodata);

        loading = (LinearLayout) findViewById(R.id.loading);
        mainContent = (LinearLayout) findViewById(R.id.mainContent);

        navBar = (LinearLayout) findViewById(R.id.navBar);
        Common.userSP = getSharedPreferences("userSP", 0);
        userID = Common.userSP.getInt("ID", 0);
        //status = Common.userSP.getInt("status", 0);



    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        loading.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.GONE);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (Common.isNetworkAvailable(getApplicationContext())) {
            Intent intent = getIntent();
            if (intent.getIntExtra("extra",0)==1)
            initData(true);
            else initData(false);
        } else Common.display(getApplicationContext(), "请开启手机网络");
    }

    private void loadFail(String str) {
        nodata.setVisibility(View.VISIBLE);
        nodata.setText(str);
        loading.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);
        if (gv!=null){
            if (gv.getVisibility()==View.VISIBLE){
                gv.setVisibility(View.GONE);
            }
        }
    }


    private void initData(final boolean isRight) {
        new Thread() {
            @Override
            public void run() {

                if (!isRight) {
                    //我的道具
                    try {
                        msgLists = HttpHelper.AJItems(postData(),true);
                        if (msgLists != null && msgLists.size()!=0) {
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

                } else {
                    try {
                        //道具商城
//                        ArrayList<HashMap<String, Object>> temp1 = new ArrayList<HashMap<String, Object>>();
//                        ArrayList<HashMap<String, Object>> temp2 = new ArrayList<HashMap<String, Object>>();
                        msgLists = HttpHelper.AJItems(getAll(),false);
//                        temp1 = HttpHelper.AJItems(postData("2"),false);
//                        temp2 = HttpHelper.AJItems(postData("3"),false);
//                        if (temp1.size()!=0)
//                        msgLists.addAll(temp1);
//                        if (temp2.size()!=0)
//                        msgLists.addAll(temp2);
                        if (msgLists != null && msgLists.size()!=0) {
                            Message msg = handler.obtainMessage();
                            msg.what = 11;
                            msg.obj = msgLists;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = handler.obtainMessage();
                            msg.what = -1;
                            msg.obj = "暂时没有任何道具出售";
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Message msg = handler.obtainMessage();
                        msg.what = -1;
                        msg.obj = "暂时没有任何道具出售";
                        handler.sendMessage(msg);
                    }
                }


            }
        }.start();
    }


    private void loadData(final ArrayList<HashMap<String, Object>> msgLists,boolean isRight) {
        loading.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);

        gv = (GridView) findViewById(R.id.gview);
        gv.setCacheColorHint(0);
        gv.setVisibility(View.VISIBLE);
        getData(msgLists,!isRight);

        if (isRight) {
            //道具商城
            StoreAdapter adapter = new StoreAdapter(StoreActivity.this, itemArrayList,
                    R.layout.store_item, new String[]{"image", "price"},
                    new int[]{R.id.gvImg, R.id.price});

            adapter.notifyDataSetChanged();
            gv.setAdapter(adapter);
        }else {
            //我的道具
            ItemAdapter adapter = new ItemAdapter(StoreActivity.this, itemArrayList,
                    R.layout.my_item, new String[]{"image","number"},
                    new int[]{R.id.gvImg,R.id.itemNumIcon});

            adapter.notifyDataSetChanged();
            gv.setAdapter(adapter);
        }

    }


//    public String postData(String itemType) throws Exception {
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("itemType", itemType);
//        String result = HttpHelper.postData(MyURL.GET_ITEMS_BY_TYPE, hm, null);
//        return result;
//    }

    public String getAll() throws Exception {
        String result = HttpHelper.postData(MyURL.GET_ALL_ITEMS, null, null);
        return result;
    }


    public String postData() throws Exception {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("userID", userID+"");
        String result = HttpHelper.postData(MyURL.GET_ITEMS_BY_UID, hm, null);
        return result;
    }

    private void getData(ArrayList<HashMap<String, Object>> msgLists,boolean isCount) {

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
            if (isCount)
            item.setItemCount((int)msgLists.get(i).get("count"));
            item.setId((int)msgLists.get(i).get("id"));
            itemArrayList.add(item);
        }

    }

    @Override
    public void onClick(View arg0) {

        if (arg0 == imgBack) {
//            Intent intent = new Intent();

//            Intent i = getIntent();
//            String arg = "";
//            arg = i.getStringExtra("extra");
//            if (arg!=null && arg.equals("map_item")){
//                if (status == 1)
//                    intent.setClass(StoreActivity.this, SingleBasicMapActivity.class);
//                else intent.setClass(StoreActivity.this, CoupleBasicMapActivity.class);
//
//            }else {
//
//                if (status == 1)
//                    intent.setClass(StoreActivity.this, SingleMyActivity.class);
//                else intent.setClass(StoreActivity.this, CoupleMyActivity.class);
//            }
//            startActivity(intent);

            finish();
        }
        if (arg0 == itemSelect) {
            new Thread() {
                @Override
                public void run() {
                    count++;
                    if (count % 2 == 0) {
                        Message msg = handler.obtainMessage();
                        msg.what = 2;
                        handler.sendMessage(msg);

                    } else {
                        Message msg = handler.obtainMessage();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }

                }
            }.start();

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent intent = new Intent();
//            Intent i = getIntent();
//            String arg = "";
//            arg = i.getStringExtra("extra");
//            if (arg!=null && arg.equals("map_item")){
//                if (status == 1)
//                    intent.setClass(StoreActivity.this, SingleBasicMapActivity.class);
//                else intent.setClass(StoreActivity.this, CoupleBasicMapActivity.class);
//
//            }else {
//
//                if (status == 1)
//                    intent.setClass(StoreActivity.this, SingleMyActivity.class);
//                else intent.setClass(StoreActivity.this, CoupleMyActivity.class);
//            }
//            startActivity(intent);


            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
