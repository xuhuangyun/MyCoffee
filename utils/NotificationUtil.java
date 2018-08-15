package com.fancoff.coffeemaker.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.fancoff.coffeemaker.Application.MainActivity;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.R;

/**
 * Created by apple on 2018/5/8.
 * 通知栏操作类
 */

public class NotificationUtil {
    private static final NotificationUtil ourInstance = new NotificationUtil();

    public static NotificationUtil getInstance() {
        return ourInstance;
    }

    private NotificationUtil() {//获取系统的notificationManager服务
        notificationManager = (NotificationManager) MyApp.getIns()
                .getSystemService(android.content.Context.NOTIFICATION_SERVICE);
    }

    Notification notification;
    NotificationManager notificationManager;
//    int id = 121212;
    /**
     * 发送点击跳转到MainActivity的通知；
     */
    public synchronized Notification show(int id) {
        if(notification!=null){
            return notification;
        }
        CharSequence contentTitle = MyApp.getIns().getString(R.string.app_name);
        Intent notificationIntent = new Intent(MyApp.getIns(), MainActivity.class);
        PendingIntent contentItent = PendingIntent.getActivity(MyApp.getIns(), 0,
                notificationIntent, 0);
        notification = new Notification.Builder(MyApp.getIns())
                .setSmallIcon(R.mipmap.ic_launcher)//设置小图标
                .setContentTitle(contentTitle)//设置通知的内容标题:CoffeeMaker
                .setContentText("运行中...")//设置通知的内容
                .setContentIntent(contentItent)//设置通知栏点击跳转,跳转到MainActivity
                .build();

        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_NO_CLEAR;
//        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
//		notification.defaults = Notification.DEFAULT_LIGHTS;
//        notification.defaults = Notification;
//        notification.ledARGB = Color.BLUE;
//        notification.ledOnMS = 5000;


        notificationManager.notify(id, notification);//发送通知
        return notification;
    }

//    public void dismiss() {
//        if (notificationManager != null) {
//            notificationManager.cancel(id);
//            notification = null;
//        }
//
//    }
}
