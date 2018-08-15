package com.fancoff.coffeemaker.io;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.fancoff.coffeemaker.utils.NotificationUtil;

/**
 * @author ChenPingMin
 * vmc通讯服务
 */

/**
 * IoDataUtil ioDataUtil;
 * 定义进程中通讯Binder：返回IOService
 * 在onCreate中，开启了通知，点击后进入MainActivity
 */
public class IOService extends Service {
    private final static String TAG = "IOService";

    private IoDataUtil ioDataUtil;


    public void test() {

    }

    /**
     * 调用IoDataUtil的reStart方法；
     * 主要是用来释放读写任务Disposable，并重新发送数据到输出流startWrite
     */
    public void reStart() {
        ioDataUtil.reStart();
    }

    /**
     * 定义进程间通讯的Binder，并返回IOService
     */
    public class MyBinder extends Binder {
        //Binder:远程对象的基类，是由IBinder定义的轻量级远程过程调用机制的核心部分
        public IOService getService() {
            return IOService.this;  //内部类使用外部类需要加this
        }
        //Binder是用于进程间通讯,一个进程A要调用进程B的一个方法，
        //这个进程A就需要获取进程B的Binder代理对象，这个Binder代理对象就是进程B的Binder本地对象。
        //当然这个Binder本地对象需要我们在进程B去实现在Service里面，实现的同时还要实现进程A想要调用方法。
        //而在进程A里也要实现Binder代理对象。
        //进程A调用Binder代理对象时会通知Binder驱动，Binder驱动又会去调用进程B实现的Binder本地对象到实现的方法。
    }

    /**
     * 发送数据：发送数据bytes，回调接口IoCallBack cllBack
     */
    public void sendCMDWithResult(byte[] bytes, IoCallBack cllBack) {
        ioDataUtil.sendCMDWithResult(cllBack, bytes);

    }


    /**
     * ioDataUtil.onStart():发送数据startWrite
     */
    public void start() {
        ioDataUtil.onStart();
    }

    /**
     * 绑定到service
     * 获得IoDataUtil实例 ioDataUtil
     * 返回IOService
     */
    @Override
    public IBinder onBind(Intent intent) {
        ioDataUtil = IoDataUtil.getInstance();
        return new IOService.MyBinder();
    }

    /**
     * 解除绑定
     * 释放实例ioDataUtil的读写Disposable
     */
    @Override
    public boolean onUnbind(Intent intent) {
        ioDataUtil.onDestroy();
        return super.onUnbind(intent);
    }

    /**
     * 启动service后第一步
     * 服务运行在前台，提示用户点击通知，然后进入MainActivity
     */
    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1, NotificationUtil.getInstance().show(1));
        //使此服务在前台运行，提供在此状态下显示给用户的持续通知；点击通知进入MainActivity
    }


    /**
     * 此service被关闭：
     * 将此服务从前台状态中删除，如果需要更多内存，则允许将其删除。
     * 释放实例ioDataUtil的读写Disposable
     */
    @Override
    public void onDestroy() {
        stopForeground(true);
        ioDataUtil.onDestroy();
        super.onDestroy();

    }

}
