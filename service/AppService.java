package com.fancoff.coffeemaker.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.bean.ErrorBean;
import com.fancoff.coffeemaker.bean.MsgEvent;
import com.fancoff.coffeemaker.bean.TaskBean;
import com.fancoff.coffeemaker.bean.VMCState;
import com.fancoff.coffeemaker.bean.heatbit.HeatParams;
import com.fancoff.coffeemaker.bean.machine.CleanTime;
import com.fancoff.coffeemaker.io.CMDUtil;
import com.fancoff.coffeemaker.net.BaseObserver;
import com.fancoff.coffeemaker.net.NetUtil;
import com.fancoff.coffeemaker.net.RequstBean.HeadBeatBean;
import com.fancoff.coffeemaker.net.RequstBean.Order;
import com.fancoff.coffeemaker.net.RequstBean.UploadDatasBean;
import com.fancoff.coffeemaker.utils.DbUtil;
import com.fancoff.coffeemaker.utils.NotificationUtil;
import com.fancoff.coffeemaker.utils.RxBus;
import com.fancoff.coffeemaker.utils.TimeUtil;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.rx.RxNetTool;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * app后台服务
 */
public class AppService extends Service {
    NetUtil netUtil = NetUtil.getInstance();


    public class MyBinder extends Binder {
        public AppService getService() {
            return AppService.this;
        }
    }

    public void action() {

    }

    public AppService() {

    }

    /**
     * 开启service后的调用第一步：
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1, NotificationUtil.getInstance().show(1));//使此服务在前台运行，并在此状态下向用户提供正在进行的通知。
//        BaseObserver<TestBean> baseObserver=new BaseObserver<TestBean>() {
//            @Override
//            public void onHandleSuccess(TestBean t) {
//                Logger.e(t.toString());
//            }
//        };
//        NetUtil.getInstance().actioTest(baseObserver);

    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }


    @Override
    public boolean onUnbind(Intent intent) {

        disp.dispose();
        return super.onUnbind(intent);
    }


    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder();
    }

    /**
     * MainActivity会实现这些接口方法
     */
    public interface ViewStateCallBack {
        boolean isSleepFragment();

        void refView();

        void showDialog();

        void hideDialog();

        void showProgress(String msg);

        void hideProgress();

    }

    public int checkNet(Context context) {
        int netType = RxNetTool.getNetWorkType(context);
        if (netType == RxNetTool.NETWORK_NO) {
            viewStateCallBack.showDialog();
        } else {
            viewStateCallBack.hideDialog();
        }
        return netType;
    }

    ViewStateCallBack viewStateCallBack;
    Disposable disp;
    /**
     * 间隔1s执行一次accept();
     *    1、无操作，屏保计时到进入屏保；
     *    2、心跳包；
     *    3、上传销售记录；
     *    4、执行服务器推送过来的任务；
     *    5、今日秒杀商品信息更新；
     *    6、日期变化清除日志？
     *    7、定时清洗；
     *    8、获得视频下载进度：downLoadProgress；
     *    9、判断app是否在后台运行，是则120s后进入ACTION_MAIN；
     *    10、打印内存日志；
     */
    public void startTime(final ViewStateCallBack viewStateCallBack) {
        this.viewStateCallBack = viewStateCallBack;
        DurationUtil.getIns().seTimeToAwake();//NoActionTime = 0;
        if (disp == null) {//上电后为空，可以进来
            disp = Observable.interval(0, 1, TimeUnit.SECONDS)
                    .compose(MyApp.getIns().getNowActivity().<Long>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            runTimeToSleep();//fragment超时退出、工程菜单超时隐藏、屏保时间进入屏保
                            runTimeToHeadBeat();//心跳包
                            runTimeToActionTask();//上传销售记录
                            TaskUtil.getInstance().runtimeToPushTask();//执行推送的任务
                            DataCenter.getInstance().checkSkill();//今日秒杀商品信息更新
                            LogUtil.getIns().clearLogs();//清理
                            checkTimeToClean();//定时清洗
                            DataCenter.getInstance().checkUnDownLoadVedios();
                            if (!MyApp.OPEN_FLOAT) {
                                checAppRun();//检测app是否在后台运行
                            }
                            logDetail();//打印内存日志
                        }


                    });
        }
    }

    private void logDetail() {
        MyApp.getIns().displayBriefMemory();
    }

    int count;
    /**
     * app在后台运行，2分钟到后，发送ACTION.RESUME_APP到消息队列，MainActivity会处理该消息：进入ACTION_MAIN
     */
    private void checAppRun() {
        if (MyApp.getIns().isRunBackGround()) {
            count++;
            if (count >= 120) {
                LogUtil.test("checAppRun:RESUME_APP");
                RxBus.getInstance().post(new MsgEvent(MyConstant.ACTION.RESUME_APP));
                count = 0;
            }
        } else {
            count = 0;
        }

    }


    /**
     * 监测清洗任务：
     *    会调用TimeUtil类的changeMM()方法，传入一个接口；
     *    changeMM方法，在1分钟变化后会回调本方法实例化接口方法onChange()，传入现在的时、分；
     *        遍历清洗时间列表，与现在的时、分相同，并判断能清洗，则清洗指令添加到任务等待执行；
     */
    private void checkTimeToClean() {
        //每分钟执行一次回调接口
        TimeUtil.getInstance().changeMM(new TimeUtil.TimeMMChangeCallBack() {
            @Override
            public void onChange(String notime) {
                for (CleanTime cle : DataCenter.getInstance().getCleanTimeList()) {//遍历清洗时间列表
                    LogUtil.test("CleanTime:" + cle.getTime());
                    if (cle.getTime().equals(notime)) {
                        if (VMCState.getIns().canClean()) {
                            CMDUtil.getInstance().clean(null);
                            return;
                        }
                    }
                }
            }
        });


    }


    /**
     * fragment超时则退出、工程菜单超时隐藏、屏保时间到了进入屏保
     *    没有制作和进度fragment：
     *       是：  进入屏保的事件，以及一些需要退出对话框的事件；
     *       不是：NoActionTime = 0
     */
    private void runTimeToSleep() {
        DurationUtil.getIns().runTime();//NoActionTime++
        if (!viewStateCallBack.isSleepFragment()) {//无制作和进度fragment
            DurationUtil.getIns().timeToIntent();
        } else {//有制作或进度fragment
            DurationUtil.getIns().seTimeToAwake();//NoActionTime = 0;
        }
    }

    boolean isTask = false;

    /**
     * 上传销售记录
     *   1、isTask=true：return；
     *   2、调用NetUtil的actionUploadDatas方法，并实现了BaseObserver<UploadDatasBean>接口的方法：
     *      onHandleSuccess：
     *      onHandleError：打印日志：上传销售数据失败+msg
     *        NetUtil类的actionUploadDatas方法会回调该方法：obser.onHandleError(-1, noRegishtError);
     *        BaseObserver类的onNext方法会回调上面两个方法；
     *   3、return_code=1：打印日志：DEBUG_OKHTTP:上传销售数据成功BaseBean{return_code=1, return_msg='null'}
     */
    private synchronized void runTimeToActionTask() {
        if (isTask) {
            return;
        }
        isTask = true;

        List<Order> list = DbUtil.getInstance().selectOrders();
        if (list != null && list.size() > 0) {
            final Order order = list.get(list.size() - 1);
            NetUtil.getInstance().actionUploadDatas(order, new BaseObserver<UploadDatasBean>() {
                @Override
                public void onHandleSuccess(UploadDatasBean uploadDatasBean) {

                }

                @Override
                public void onHandleError(int code, String msg) {
                    super.onHandleError(code, msg);
                    LogUtil.debugNet("上传销售数据失败：" + msg);
                    isTask = false;
                }
            }, new Consumer<UploadDatasBean>() {
                @Override
                public void accept(UploadDatasBean uploadDatasBean) throws Exception {
                    if (uploadDatasBean.getIsSuccess()) {
                        DbUtil.getInstance().deleteOrders(order);
                        LogUtil.debugNet("上传销售数据成功" + uploadDatasBean.toString());
                        isTask = false;
                    }
                }
            });

        } else {
            isTask = false;
        }


    }

    boolean runHeat = false;
    HeatParams h;


    /**
     * HeatParams h为空则新建HeatParams实例；否则清空h；
     */
    public HeatParams newHeatParams() {
        if (h == null) {
            h = new HeatParams();
        }
        h.clear();
        return h;

    }

    public boolean compare(ArrayList<ErrorBean> a, ArrayList<ErrorBean> b) {
        String aa = "error=" + a;
        String bb = "error=" + b;
        return aa.equals(bb);
    }

    ArrayList<ErrorBean> errorListPre = new ArrayList<>();

    /**
     * 本方法间隔1s会被调用：正在心跳则runHeat=true；
     * 1、runHeat=true;返回；
     * 2、得到需要push的任务列表ArrayList<TaskBean> taskBeens；
     *    将push任务列表taskBeens传入到HeatParams h的push，HeatUtil类设置心跳间隔时间count；
     * 3、获得错误列表ArrayList<ErrorBean> errorList；不为空，添加到HeatParams h的ArrayList  error
     * 4、现在的错误列表errorList和先前的错误列表errorListPre不相等：
     *         打印日志操作日志：has new  content -runNow- +errorList
     *         HeatUtil类设置心跳间隔时间count；
     *    先前的错误列表清零，现在的错误列表赋值到先前的错误列表；
     * 5、心跳时间到了：actionHeadBeat
     *                       1、实现BaseObserver<HeadBeatBean>接口方法：
     *                       onHandleSuccess：发送ACTION.REF_ALL到mainActivity；
     *                       onHandleError：设置5s后到心跳时间；
     *                       2、实现Consumer<HeadBeatBean>接口方法：
     *                       accept：
     *                           移除taskBeens、errorList；
     *
     *    心跳时间没到，runHeat=false；
     */
    private synchronized void runTimeToHeadBeat() {
        if (runHeat) {
            return;
        }
        runHeat = true;
        HeatParams h = newHeatParams();
        final ArrayList<TaskBean> taskBeens = TaskUtil.getInstance().getPushs();
        if (taskBeens.size() > 0) {
            h.setPush(taskBeens);
            HeatUtil.getIns().runNow();
        }
        final ArrayList<ErrorBean> errorList = TaskUtil.getInstance().getErrorList();
        if (errorList.size() > 0) {
            h.setError(errorList);
        }
        if (!compare(errorList, errorListPre)) {
            LogUtil.action("has new  content -runNow-" + errorList);
            HeatUtil.getIns().runNow();
        }
        errorListPre.clear();
        errorListPre.addAll(errorList);
        if (HeatUtil.getIns().isTimeToHeat()) {


            NetUtil.getInstance().actionHeadBeat(h, new BaseObserver<HeadBeatBean>() {
                @Override
                public void onHandleSuccess(HeadBeatBean headBeatBean) {
                    if (headBeatBean.isChangeview()) {
                        RxBus.getInstance().post(new MsgEvent(MyConstant.ACTION.REF_ALL));
                    }
                    runHeat = false;
                }

                @Override
                public void onHandleError(int code, String msg) {
                    super.onHandleError(code, msg);
                    int timeToRetry = 5;//5秒后重试
                    LogUtil.debugNet("心跳包发送失败：" + msg + "，" +
                            timeToRetry + "秒后重试");
                    HeatUtil.getIns().runHeatNextM(timeToRetry);
                    runHeat = false;
                }
            }, new Consumer<HeadBeatBean>() {
                @Override
                public void accept(HeadBeatBean headBeatBean) throws Exception {
                    TaskUtil.getInstance().removeTask(taskBeens);//移除已执行任务
                    TaskUtil.getInstance().removeErrorTask(errorList);//移除已执行任务
                    boolean refView = TaskUtil.getInstance().checkTask(headBeatBean);//监测新任务
                    headBeatBean.setChangeview(refView);
                }
            });
        } else {
            runHeat = false;
        }

    }


}
