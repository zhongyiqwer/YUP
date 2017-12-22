package com.wao.dogcat.controller.single;

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

import com.owo.model.User;
import com.owo.utils.Common;
import com.owo.utils.Constants;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;



import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ppssyyy on 2017-02-18.
 */
public class FriendActivity extends Activity implements View.OnClickListener {
    private ImageView imgBack, itemSelect;
    private LinearLayout loading;
    private LinearLayout mainContent;
    private ArrayList<HashMap<String, Object>> msgLists;
    private ListView lv;
    private TextView nodata;

    private ArrayList<User> itemArrayList;
    private User u;

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
                    if (nodata.getVisibility()==View.VISIBLE)
                        nodata.setVisibility(View.GONE);
                    ArrayList<HashMap<String, Object>> msgLists =
                            (ArrayList<HashMap<String, Object>>) msg.obj;
                    loadData(msgLists);
                    break;
                case 2:
                    //我关注的
                    if (nodata.getVisibility()==View.VISIBLE)
                        nodata.setVisibility(View.GONE);
                    itemSelect.setImageResource(R.drawable.my_focus);
                    initData(false);
                    break;
                case 3:
                    //关注我的
                    if (nodata.getVisibility()==View.VISIBLE)
                        nodata.setVisibility(View.GONE);
                    itemSelect.setImageResource(R.drawable.focus_me);
                    initData(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.friend);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        imgBack = (ImageView) findViewById(R.id.btnBack);
        imgBack.setOnClickListener(this);
        itemSelect = (ImageView) findViewById(R.id.item_select);

        loading = (LinearLayout) findViewById(R.id.loading);
        mainContent = (LinearLayout) findViewById(R.id.mainContent);
        nodata = (TextView) findViewById(R.id.noData);

        Common.userSP = getSharedPreferences("userSP", 0);
        userID = Common.userSP.getInt("ID", 0);


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
            boolean flag = false;
            if (Constants.status == Constants.FOLLOWED) //关注我的
            {
                flag = true;
                if (nodata.getVisibility()==View.VISIBLE)
                    nodata.setVisibility(View.GONE);
                itemSelect.setImageResource(R.drawable.focus_me);
                count++;
            }

            initData(flag);
        } else Common.display(getApplicationContext(), "请开启手机网络");
    }

    private void loadFail(String str) {
        nodata.setVisibility(View.VISIBLE);
        nodata.setText(str);
        loading.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);
        if (lv!=null){
            if (lv.getVisibility()==View.VISIBLE){
                lv.setVisibility(View.GONE);
            }
        }
    }


    private void initData(final boolean isRight) {
        new Thread() {
            @Override
            public void run() {

                if (!isRight) {
                    //我关注
                    try {

                        msgLists = HttpHelper.AJFriends(getFriendsByUID());
                        if (msgLists != null && msgLists.size()!=0) {
                            Message msg = handler.obtainMessage();
                            msg.what = 1;
                            msg.obj = msgLists;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = handler.obtainMessage();
                            msg.what = -1;
                            msg.obj = "您未关注任何人";
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Message msg = handler.obtainMessage();
                        msg.what = -1;
                        msg.obj = "您未关注任何人";
                        handler.sendMessage(msg);
                    }

                } else {
                    //关注我的--> isRight
                    try {
                        msgLists = HttpHelper.AJFriends(getFriendsByFID());
                        if (msgLists != null && msgLists.size()!=0) {
                            Message msg = handler.obtainMessage();
                            msg.what = 1;
                            msg.obj = msgLists;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = handler.obtainMessage();
                            msg.what = -1;
                            msg.obj = "还没人关注你哦";
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Message msg = handler.obtainMessage();
                        msg.what = -1;
                        msg.obj = "还没人关注你哦";
                        handler.sendMessage(msg);
                    }
                }


            }
        }.start();
    }


    private void loadData(final ArrayList<HashMap<String, Object>> msgLists) {
        loading.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);

        lv = (ListView) findViewById(R.id.list);
        lv.setCacheColorHint(0);
        lv.setVisibility(View.VISIBLE);
        getData(msgLists);
        FriendAdapter adapter = new FriendAdapter(FriendActivity.this, itemArrayList,
                R.layout.friend_item, new String[]{"image", "name","sex","info","activeTime"},
                new int[]{R.id.head, R.id.name,R.id.sex,R.id.info,R.id.activeTime});
        adapter.notifyDataSetChanged();
        lv.setAdapter(adapter);
    }


    public String getFriendsByUID() throws Exception {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("userID", userID+"");
        String result = HttpHelper.postData(MyURL.GET_FRIENDS_BY_UID, hm, null);
        return result;
    }

    public String getFriendsByFID() throws Exception {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("friendID", userID+"");
        String result = HttpHelper.postData(MyURL.GET_FRIENDS_BY_FID, hm, null);
        return result;
    }

    private void getData(ArrayList<HashMap<String, Object>> msgLists) {

        itemArrayList = new ArrayList<>();

        for (int i = 0; i < msgLists.size(); i++) {// list
            u = new User();
            u.setId((int)msgLists.get(i).get("userID"));
            u.setAvatar(msgLists.get(i).get("avatar").toString());
            u.setUserName(msgLists.get(i).get("userName").toString());
            u.setHeight((int)msgLists.get(i).get("height"));
            u.setWeight((int)msgLists.get(i).get("weight"));
            u.setSex(msgLists.get(i).get("sex").toString());
            u.setBackImage(msgLists.get(i).get("backImage").toString());
            u.setSignature(msgLists.get(i).get("signature").toString());
            u.setLevel((int)msgLists.get(i).get("level"));
            u.setAge((int)msgLists.get(i).get("age"));
            u.setHobby(msgLists.get(i).get("hobby").toString());
            itemArrayList.add(u);
        }

    }

    @Override
    public void onClick(View arg0) {

        if (arg0 == imgBack) {
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
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
