package com.wao.dogcat.controller.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ppssyyy on 2017-03-29.
 */
public class MyAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //当广播执行onReceive方法时，启动TimeService服务以完成无限循环
        Intent i = new Intent(context, TimeService.class);
        context.startService(i);
    }

}