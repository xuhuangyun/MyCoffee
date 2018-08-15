package com.fancoff.coffeemaker.utils.rx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * Created by Vondear on 2017/3/15.
 */

public class RxBroadcastTool {

    /**
     * 注册监听网络状态的广播
     *
     * @param context
     * @return
     */
    public static BroadcastReceiver initRegisterReceiverNetWork(Context context, BroadcastReceiver mReceiverNetWork) {
        // 注册监听网络状态的服务
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(mReceiverNetWork, mFilter);
        return mReceiverNetWork;
    }

    public static void unRegisterReceiverNetWork(Context context, BroadcastReceiver mReceiverNetWork) {
        if(mReceiverNetWork!=null){
            context.unregisterReceiver(mReceiverNetWork);
        }

    }




}
