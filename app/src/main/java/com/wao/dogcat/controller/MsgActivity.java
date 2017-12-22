package com.wao.dogcat.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.owo.model.Msg;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;


import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * 消息页面
 */
public class MsgActivity extends Activity implements View.OnClickListener{
    private ImageView imgBack;
    private LinearLayout loading,navBar;
    private LinearLayout mainContent;
    private ArrayList<HashMap<String, Object>> msgLists;
    private ListView lv;

    private ArrayList<Msg> msgArrayList;
    private Msg msg;

    private int userID;
    private int status;
    private TextView nodata,title;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.msg);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        imgBack = (ImageView) findViewById(R.id.btnBack);
        imgBack.setOnClickListener(this);

        loading = (LinearLayout) findViewById(R.id.loading);
        mainContent = (LinearLayout) findViewById(R.id.mainContent);
        nodata = (TextView) findViewById(R.id.noData);
        navBar = (LinearLayout) findViewById(R.id.navBar);
        title = (TextView) findViewById(R.id.title);

        Common.userSP = getSharedPreferences("userSP", 0);
        userID = Common.userSP.getInt("ID", 0);
        status = Common.userSP.getInt("status", 0);


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
            initData();}
        else Common.display(getApplicationContext(),"请开启手机网络");
    }

    private void loadFail(String str) {
        nodata.setVisibility(View.VISIBLE);
        nodata.setText(str);
        loading.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);
    }



    private void initData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (userID != 0)
                        msgLists = HttpHelper.AJgetMsgByRID(postData());
                    if (msgLists != null && msgLists.size()!=0) {
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.obj = msgLists;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = handler.obtainMessage();
                        msg.what = -1;
                        msg.obj = "暂时没有消息哦";
                        handler.sendMessage(msg);
                    }
                }

//                catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }

                catch (Exception e) {
                    e.printStackTrace();
                    Message msg = handler.obtainMessage();
                    msg.what = -1;
                    msg.obj = "暂时没有消息哦";
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }



    private void loadData(final ArrayList<HashMap<String, Object>> msgLists) {
        loading.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);

        lv = (ListView) findViewById(R.id.list);
        lv.setCacheColorHint(0);
        getData(msgLists);
        Collections.sort(msgArrayList);
        System.out.println(msgArrayList);
        MsgListAdapter adapter = new MsgListAdapter(MsgActivity.this, msgArrayList,
                R.layout.msg_item, new String[]{"head", "name", "content","date"},
                new int[]{R.id.head, R.id.name, R.id.content,R.id.date});
        adapter.notifyDataSetChanged();
        lv.setAdapter(adapter);
    }


    public String postData() throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("id", userID+"");
        String result = HttpHelper.postData(MyURL.GET_MSG_BY_RID, paramHM, null);
        return result;
    }


    private void getData(ArrayList<HashMap<String, Object>> msgLists) {

        msgArrayList = new ArrayList<>();

        for (int i = 0; i < msgLists.size(); i++) {// list
            msg = new Msg();
            msg.setMsgContent(msgLists.get(i).get("msgContent").toString());
            msg.setMsgDate(msgLists.get(i).get("msgDate").toString());
            msg.setUserName(msgLists.get(i).get("userName").toString());
            msg.setUserAvatar(msgLists.get(i).get("avatar").toString());
            msg.setUserID((int)msgLists.get(i).get("userID"));
            msg.setMsgType(msgLists.get(i).get("msgType").toString());
            msg.setMsgId((int)msgLists.get(i).get("id"));
            msgArrayList.add(msg);
        }

    }

    @Override
    public void onClick(View arg0) {
        if (arg0 == imgBack) {
            finish();
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
