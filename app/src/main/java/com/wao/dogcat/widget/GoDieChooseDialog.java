package com.wao.dogcat.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ppssyyy on 2017-02-18.
 */
public class GoDieChooseDialog extends Dialog {
    private LinearLayout loading;
    private LinearLayout mainContent;
    private ArrayList<HashMap<String, Object>> msgLists;
    private GridView gv;
    private TextView nodata;

    private ArrayList<HashMap<String, Object>> uArrayList;
    private HashMap<String, Object> userHM;
    private Button okBtn,cancelBtn;

    public static int userID;
    private int status;

    private Context context;

    private GoDieChooseAdapter adapter;

    private ImageView searchBtn;
    private EditText searchEdt;

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

    public GoDieChooseDialog(Context context, int style) {
        super(context, style);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_godie_choose_dialog);


        nodata = (TextView) findViewById(R.id.noData);

        loading = (LinearLayout) findViewById(R.id.loading);
        mainContent = (LinearLayout) findViewById(R.id.mainContent);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        searchBtn = (ImageView) findViewById(R.id.searchBtn);
        searchEdt = (EditText) findViewById(R.id.searchEdt);

        Common.userSP = context.getSharedPreferences("userSP", 0);
        userID = Common.userSP.getInt("ID", 0);
        status = Common.userSP.getInt("status", 0);

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (GoDieChooseAdapter.userID==-1){
                        Common.display(context,"请选择用户");
                    }else {
                        try {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("userItemID", "14");
                            hm.put("reciveID",GoDieChooseAdapter.userID + "");
                            hm.put("userID",Common.userID+"");
                            int code = HttpHelper.getCode(HttpHelper.postData(MyURL.INSERT_GO_DIE, hm, null));
                            if (code==200){
                                Common.display(context,"施放成功！");
                                dismiss();
                                //成功
                            }else {
                                Common.display(context,"ERROR_CODE:"+code);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Common.display(context,"ERROR:Exception");
                        }

                    }
                }
            });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });

    }


    private void loadFail(String str) {
        nodata.setVisibility(View.VISIBLE);
        nodata.setText(str);
    }



    private void initData() {
        new Thread() {
            @Override
            public void run() {

                try {

                    if (searchEdt.getText().toString().trim().length() > 0) {
                        msgLists = HttpHelper.AJUsers(searchUser(searchEdt.getText().toString().trim()));
                        if (msgLists != null && msgLists.size()!=0) {
                            Message msg = handler.obtainMessage();
                            msg.what = 1;
                            msg.obj = msgLists;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = handler.obtainMessage();
                            msg.what = -1;
                            msg.obj = "未搜到相关用户";
                            handler.sendMessage(msg);
                        }
                    }else {
                        Message msg = handler.obtainMessage();
                        msg.what = -1;
                        msg.obj = "输入不能为空";
                        handler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = handler.obtainMessage();
                    msg.what = -1;
                    msg.obj = "服务器错误，未搜到相关用户";
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }


    private void loadData(final ArrayList<HashMap<String, Object>> msgLists) {


        gv = (GridView) findViewById(R.id.gview);
        gv.setCacheColorHint(0);
        gv.setVisibility(View.VISIBLE);
        getData(msgLists);
         adapter = new GoDieChooseAdapter(context, uArrayList,
                R.layout.go_die_user_item);

        adapter.notifyDataSetChanged();
        gv.setAdapter(adapter);


    }


    public String searchUser(String name) throws Exception {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("username", name);
        String result = HttpHelper.postData(MyURL.SEARCH_USER_BY_NAME, hm, null);
        return result;
    }

    private void getData(ArrayList<HashMap<String, Object>> msgLists) {

        uArrayList = new ArrayList<>();

        for (int i = 0; i < msgLists.size(); i++) {// list
            userHM = new HashMap<>();
            userHM.put("name", msgLists.get(i).get("username").toString());
            userHM.put("head", msgLists.get(i).get("avatar").toString());
            userHM.put("id", msgLists.get(i).get("id").toString());
            uArrayList.add(userHM);
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
