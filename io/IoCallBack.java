package com.fancoff.coffeemaker.io;

/**
 * Created by apple on 2018/3/30.
 * vmc通讯会调接口
 */


public interface IoCallBack {
    public final static int ERROR_TIME_OUT = 1;//vmc超时
    public final static int ERROR_IO = 2;//串口读写失败
    public final static int ERROR_NO_SERVICE = 3;//app服务未启动

    void onFailed(byte[] send,int type);
    void onSuccess(byte[] send,byte[] rb);


}
