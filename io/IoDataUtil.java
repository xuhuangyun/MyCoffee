package com.fancoff.coffeemaker.io;

import android.util.Log;

import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.io.data.CMDTask;
import com.fancoff.coffeemaker.io.data.SendByteBean;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by apple on 2017/12/3.
 * vmc通讯工具类
 */

/**
 * CMDTask nowTask
 * ArrayList<CMDTask> writeList = new ArrayList<CMDTask>();
 * 1、CMDTask任务列表writeList，需要发送的CMDTask任务都添加到这个列表；
 * 2、取出任务列表writeList第0号任务，并调用CMDTask的write方法（输出流到串口）；
 * 3、write的时候调用10ms间隔读输入流方法，最终会调用CMDTask的read方法（读串口的输入流）；
 * 4、一些释放读写任务Disposable方法；
 */
public class IoDataUtil {
    private static final IoDataUtil ourInstance = new IoDataUtil();

    private Disposable dispWrite;
    private Disposable dispRead;

    public static IoDataUtil getInstance() {
        return ourInstance;
    }

    private IoDataUtil() {
    }

    /**
     * 释放读写Disposable
     */
    public void onDestroy() {
        releseWritre();
        releseRead();
    }

    /**
     * startWrite():发送数据
     */
    public void onStart() {
        startWrite();

    }

    ArrayList<CMDTask> writeList = new ArrayList<CMDTask>();
    CMDTask nowTask;

    /**
     * 将icallback回调接口对象赋予CMDTask对象task的ioCallback
     * 将发送的字节数组bw赋予CMDTask对象task的sendBytes
     * 将task对象加入到writeList列表
     */
    public CMDTask sendCMDWithResult(final IoCallBack icallback, final byte[] bw) {
        synchronized ("CMDTask") {
            CMDTask task = new CMDTask();
            task.setIoCallBack(icallback);
            task.setSendBytes(bw);
            writeList.add(task);
            return task;
        }
    }

    /**
     * 获得CMDTask任务的第0号成员，没有则发送握手指令；
     * writeList列表有成员，将第0个成员赋予CMDTask对象ck，同时删除列表中的第0个成员；
     * writeList列表为空，则CMDTask对象ck，属性ioCallBack和sendBytes赋值；使用握手指令数据
     */
    public CMDTask getNowTask() {
        synchronized ("CMDTask") {
            CMDTask ck = null;
            if (writeList.size() > 0) {
                ck = writeList.get(0);
                writeList.remove(ck);
            } else {//握手查询
                SendByteBean sendByteBean = new SendByteBean(IOConstans.OPTION.OPTION_QUE, null);
                ck = new CMDTask();
                ck.setIoCallBack(CMDUtil.getInstance().getDefultcmdCallBack());
                ck.setSendBytes(sendByteBean.getSendData());
            }
            return ck;
        }
    }

    boolean isstartRead;
    boolean isstartWrite;

    /**
     * 清空CMDTask nowTask任务的数据（属性）缓存
     */
    public void relese() {
        if (nowTask != null) {
            nowTask.release();
            nowTask = null;
        }

    }

    /**
     *  isstartRead = true正在读，则返回false
     *  间隔10ms去读输入流中的数据（即读串口有没有收到数据）
     *  只有在发送数据后，才会去读数据；（发送数据后会设置CMDTask nowTask的isStartRead=true）
     *  本类的startWrite会调用该方法
     */
    public boolean startRead() {
        if (isstartRead) {
            return false;
        }
        isstartRead = true;
        if (SeriaPortUtil.getInstance().isInit() && (dispRead == null )) {//串口初始化成功，dispRead不为空
            dispRead = Observable.interval(0, 10, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
                //每隔10ms执行一次，执行无限次,默认在新线程上
                @Override
                public void accept(Long aLong) {
                    try {
                        //Log.e("Thread:",Thread.currentThread().getName());
                        if (nowTask != null && nowTask.isStartRead()) {//CMDTask有实例对象，并且isStartRead标志置位
                            //本类的startWrite方法会调nowTask.write()，write方法会调用CMDTask类中的setStartRead(true);
                            nowTask.read();//有执行命令时获取读取数据
                        } else {//没有发送任务的时候nowTask=null
                            Thread.sleep(300);
                        }
                    } catch (Exception e) {
                        LogUtil.error("startRead:" + e.toString());
                    }
                }
            });
        }


        isstartRead = false;
        return true;
    }

    /**
     *  isstartWrite = true，正在写则返回false
     *  开启10ms读输入流任务，然后进行写入输出流任务，并设置读取初始时间，10秒未收到输入流数据代表VMC通讯超时。
     *  //onStart和reStart会调用该方法
     *  //IOService的start会调用onStart方法
     *  //IOService的reStart会调用reStart方法
     *  间隔1s从任务列表writeList取出最前面任务，从串口发送数据到vmc
     */
    public boolean startWrite() {

        if (isstartWrite) {
            return false;
        }
        isstartWrite = true;
        if ((dispWrite == null )) {
            dispWrite = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                    .compose(MyApp.getIns().getNowActivity().<Long>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new Consumer<Long>() {
             //1000ms执行一次任务，执行无限次，默认在新线程上
             //使用ActivityEvent类，其中的CREATE、START、 RESUME、PAUSE、STOP、 DESTROY分别对应生命周期内的方法。
             //使用bindUntilEvent指定在哪个生命周期方法调用时取消订阅。
             //获得CMDTask任务的第0号
                @Override
                public void accept(Long aLong) {
                    try {
                        //Log.e("Thread:",Thread.currentThread().getName());
                        startRead();//10ms读输入流的任务开启
                        nowTask = getNowTask();
                        if (nowTask != null) {
                            if (nowTask.write()) {//串口输出数据
                                nowTask.checkTimeOut(CMDUtil.VMC_TIMEOUT);//10秒输入流中没收到数据，代表VMC通讯超时
                                relese();//CMDTask nowTask中的数据清空
                            }
                        }

                    } catch (Exception e) {
                        LogUtil.error("startWrite:" + e.toString());
                    }


                }
            });
        }
        isstartWrite = false;
        return true;
    }

    boolean restart;

    private void releseWritre() {
        if (dispWrite != null) {
            dispWrite.dispose();
            dispWrite = null;
        }
    }

    private void releseRead() {
        if (dispRead != null) {
            dispRead.dispose();
            dispRead = null;
        }
    }

    /**
     * restart = true;
     * 释放读写Disposable
     * 发送数据到输出流 IoDataUtil的startWrite()
     */
    public void reStart() {
        if (restart) {
            return;
        }
        restart = true;
        releseWritre();
        releseRead();
        startWrite();
        restart = false;
    }
}
