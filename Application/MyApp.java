package com.fancoff.coffeemaker.Application;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.multidex.MultiDexApplication;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.ui.commomView.MyImageView;
import com.fancoff.coffeemaker.ui.commomView.MyVideoView;
import com.fancoff.coffeemaker.utils.ScreenShot;
import com.fancoff.coffeemaker.utils.TimeUtil;
import com.fancoff.coffeemaker.utils.floatview.FloatPermissionManager;
import com.fancoff.coffeemaker.utils.log4j.ConfigureLog4J;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.rx.RxNetTool;
import com.kingsoft.media.httpcache.KSYProxyService;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by apple on 2017/9/10.
 * Application
 */
public class MyApp extends MultiDexApplication implements Thread.UncaughtExceptionHandler {
    /*************测试开关**********/
    //测试开关，正式打包用 打包时一定要改为true
    public final static boolean ALL_TEST_OFF = true;
    //设备唯一号取mac地址或者Android ID
    public final static boolean MAC = false;

    //当ALL_TEST_OFF为false时，以下设置有效，否则默认false
    public final static boolean addTextFile = getType(false);//本地导入测试工艺文件 正式打包置为false
    public final static boolean IOTEST = getType(true);//模拟vmc通讯数据
    public final static boolean testMac = getType(true);//使用测试mac地址
    public final static boolean DEBUG_LOG = getType(true);//打开debug测试日志
    public final static boolean TEST_HOST = getType(true);//服务器地址 false使用正式地址，true使用测试地址
    public final static boolean IS_DEVELEPMENT_DEVICE = getType(false);//是否为开发测试设备
    public final static boolean OPEN_FLOAT = getType(false);//是否打开悬浮按钮
    public final static boolean BUGLY_ENABLE_DEBUG = getType(true);//<!-- 配置Bugly调试模式（true或者false）官方建议正式打包时置为false-->
    public final static boolean TEST_CTASH = getType(false);//<!-- 通过按键模拟制造一个Crash-->
    public final static boolean NONET = getType(true);//<!-- 无需服务器模拟测试-->
    public final static boolean oPenLeakCanary = getType(false);//<!-- 内存泄漏测试-->

    //ALL_TEST_OFF
    static boolean getType(boolean type) {
        return ALL_TEST_OFF ? false : type;
    }


    public boolean getPayDebug() {
        return ALL_TEST_OFF ? false : true;
    }//点击二维码模拟支付成功

    public final static String BUGLY_APPID = "02737a97be";//bugly app id
    boolean isRunBackGround = true;//app是否运行在后台
    static MyApp ins;

    private KSYProxyService proxy;
    //金山云Android HTTPCache SDK可以方便地和播放器进行集成，提供对HTTP视频边播放缓存的功能，缓存完成的内容可以离线工作。
    // 对于点播，播放器通过getProxyUrl接口获得播放地址，进行播放。
    // 对于直播，则可通过getProxyUrl(url, newCache)接口获得播放地址，并通过参数newCache控制播放和缓存的行为。
    private FloatPermissionManager mFloatPermissionManager;
    ActivityManager manager;

    /**返回：isRunBackGround; app是否运行在后台*/
    public boolean isRunBackGround() {
        return isRunBackGround;
    }

    public void setRunBackGround(boolean runBackGround) {
        isRunBackGround = runBackGround;
    }

    public ActivityManager getManager(Context context) {
        if (manager == null) {
            manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return manager;
    }

    ActivityManager activityManager;
    int loginCount = 60;
    ActivityManager.MemoryInfo info;

    /**打印内存信息 60秒打印一次*/
    public void displayBriefMemory() {
        if (loginCount >= 60) {
            if (nowActivity != null) {
                if (activityManager == null) {
                    activityManager = (ActivityManager) nowActivity.getSystemService(nowActivity.ACTIVITY_SERVICE);
                }
                if (info == null) {
                    info = new ActivityManager.MemoryInfo();
                }
                activityManager.getMemoryInfo(info);
                LogUtil.action("系统剩余内存:" + (info.availMem >> 10) + "k");
                LogUtil.action("系统是否处于低内存运行：" + info.lowMemory);
                LogUtil.action("当系统剩余内存低于" + (info.threshold >> 10) + "k" + "时就看成低内存运行");
            }
            loginCount = 0;
        } else {
            loginCount++;
        }


    }
//    MyVideoView myVideoView;
//    MyVideoView myVideoViewTop;
//    public MyVideoView getMyVideoViewTop() {
//        if (myVideoViewTop == null) {
//            myVideoViewTop = new MyVideoView(MyApp.getIns().getNowActivity());
//        }
//        return myVideoViewTop;
//    }
//    public MyVideoView getMyVideoView() {
//        if (myVideoView == null) {
//            myVideoView = new MyVideoView(MyApp.getIns().getNowActivity());
//        }
//        return myVideoView;
//    }

    /***为了保证正常工作，推荐一个app只使用一个KSYProxyService实例，例如:*/

    /**获得KSYProxyService proxy的实例*/
    public static KSYProxyService getProxy(Context context) {
        MyApp app = (MyApp) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }
    /**新建一个KSYProxyService对象，并进行缓存位置和大小的配置，并启动server*/
    private KSYProxyService newProxy() {
        KSYProxyService mproxy = new KSYProxyService(this);
        mproxy.setCacheRoot(new File(FilesManage.dri.video));//设置缓存存储位置
        mproxy.setMaxCacheSize(300 * 1024 * 1024);//使用限制文件总大小的策略，默认使用的是该策略，且缓存大小为500M
        mproxy.startServer();//启动server
        return mproxy;

    }

    /**
     * 新建Intent示例(显示系统的设置),在上下文co中启动Intent对象的Activity
     * MainActivity的onBackPressed()的MyApp.getIns().intentToSetting(MainActivity.this);
     * SettingPageFragment工程菜单界面的“设置”键：MyApp.getIns().intentToSetting(getActivity());*/
    public void intentToSetting(Context co) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        co.startActivity(intent);
    }

    public void runOnUiThread(Runnable runnable) {
        if (nowActivity != null) {
            nowActivity.runOnUiThread(runnable);
        }
    }

    org.apache.log4j.Logger gLogger;

    /**
     * log()打印日志会调用getgLogger()方法;
     * getgLogger()方法会调用TimeUtil的changerHH()方法，并对接口进行实例化;
     * TimeUtil类的changerHH()方法比较年月日时，与先前保存的小时不一样则会调用onChange()方法，参数为现在的年月日时;
     *       getgLogger()方法返回记录器给log()方法;log()可以调用记录器gLogger的error()等方法;
     */
    public org.apache.log4j.Logger getgLogger() {
        TimeUtil.getInstance().changeHH(new TimeUtil.TimeMMChangeCallBack() {
            @Override
            public void onChange(String notime) {
                gLogger = org.apache.log4j.Logger.getLogger(ConfigureLog4J.configure().getClass());
                //得到记录器
            }
        });
        return gLogger;
    }

    public void setgLogger(org.apache.log4j.Logger gLogger) {
        this.gLogger = gLogger;
    }

    /**判断是否有网络；false：无网络；true：有网络；*/
    public boolean checkNet(Context context) {
        int netType = RxNetTool.getNetWorkType(context);
        if (netType == RxNetTool.NETWORK_NO) {
            return false;
        } else {
            return true;
        }
    }

    public FloatPermissionManager getmFloatPermissionManager() {
        return mFloatPermissionManager;
    }

    /**
     * MakingPageFragment类的onDestory方法中会调用该方法，并进行观察
     */
    public static RefWatcher getRefWatcher(Context context) {
        MyApp application = (MyApp) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        ins = this;
        mFloatPermissionManager = new FloatPermissionManager();
        Thread.setDefaultUncaughtExceptionHandler(this);
        if (MyApp.oPenLeakCanary) {
            refWatcher = LeakCanary.install(this);
        }
        /**************bugly初始化**************/
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        if (IS_DEVELEPMENT_DEVICE) {
            strategy.setAppVersion(getString(R.string.debug_device));
        }
        CrashReport.initCrashReport(getApplicationContext(), BUGLY_APPID, BUGLY_ENABLE_DEBUG, strategy);
        CrashReport.setUserId(this, DeviceInfo.getInstance().getDeviceId());
        /**************vmc模拟测试数据初始化**************/
        if (IOTEST) {
            TestIoDatas.getInstance().intTestMakingList();
        }
        /**************字体初始化**************/
        /**CalligraphyConfig :
         * 1、加载依赖库：compile 'uk.co.chrisjenx:calligraphy:2.3.0'
         * 2、添加自定义字体文件到指定目录
         * 将自定义字体放置在assets/目录下,以后使用过程中都将以此路径作为相对路径。
         * 当然你也可以在此路径下创建子目录,例如"fonts/"作为存放字体文件的目录,在布局文件中可以直接使用
         *3.1、初始化字体配置,在Application的 onCreate 方法中初始化字体配置,如果不设置的话就不会生效
         *3.2、在MainActivity中：protected void attachBaseContext(Context newBase)
         */
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/YouYuan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }


    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        boolean isf = (ni != null && ni.isConnected());
        return isf;
    }


    MainActivity nowActivity;

    /**
     * MainActivity中的onCreate()方法会调用该方法：传入MainActivity；
     * MyApp.getIns().thisActivity(this);
     */
    public void thisActivity(MainActivity activity) {
        nowActivity = activity;

    }

    public MainActivity getNowActivity() {
        return nowActivity;
    }

    public void setNowActivity(MainActivity nowActivity) {
        this.nowActivity = nowActivity;
    }

    /**返回base64格式的字符串数组*/
    public String screenShot(File filePath) {
        if (nowActivity != null) {
            return ScreenShot.shoot(nowActivity, filePath);//返回base64格式的字符串数组
        }
        return "";
    }


    public static MyApp getIns() {
        return ins;
    }

    /**销毁activity，并且正常退出程序*/
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LogUtil.error("uncaughtException" + e.toString());
        if (nowActivity != null) {
            nowActivity.onDestroy();
        }
        System.exit(1);//是非正常退出，就是说无论程序正在执行与否，都退出

    }

    /**
     * 抽象类TaskRun：3个抽象方法
     *    run();onOk();onError();
     */
    public abstract static class TaskRun {
        public abstract void run();

        protected abstract void onOk();

        protected void onError() {
        }
    }

    long nowDday;

    /**日期变化，则重新赋当前的日期给nowDday*/
    public boolean isDayChange() {
        Date curDate = new Date();
        long day = TimeUtil.getInstance().getStartTimeOfDay(curDate).getTime();
        if (day != nowDday) {
            nowDday = day;
            return true;
        }
        return false;
    }


    HashMap<String, Disposable> task = new HashMap<>();

    /**
     *  DataCenter类的checkSkill()方法 MyApp.getIns().runTask("key_check_skill", new MyApp.TaskRun()；
     *  TaskBean类的runtimeToPushTask()方法：
     *                                 MyApp.getIns().runTask("key_upload_logs", new MyApp.TaskRun()；
     *                                 MyApp.getIns().runTask("key_cut_screen", new MyApp.TaskRun()
     */
    public void runTask(final String key, final TaskRun taskRun) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                taskRun.run();
                e.onComplete();
            }
        })
                .compose(MyApp.getIns().getNowActivity().<String>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            // 使用ActivityEvent类，其中的CREATE、START、 RESUME、PAUSE、STOP、 DESTROY分别对应生命周期内的方法。
            // 使用bindUntilEvent指定在哪个生命周期方法调用时取消订阅。
            //observable.subscribe(observer);
            //创建了 Observable 和 Observer 之后，再用 subscribe() 方法将它们联结起来，整条链子就可以工作了
            //Schedulers.newThread(): 总是启用新线程，并在新线程执行操作
            //AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                task.put(key, d);
            }

            @Override
            public void onNext(@NonNull String s) {//普通事件，类似于onClick，onEvent；
            }
            //在oError和onComplete后调用disposable.dispose();
            @Override
            public void onError(@NonNull Throwable e) {
                //事件队列异常。在事件处理过程中出异常时，onError()会被触发，同时队列自动终止，不允许再有事件发出
                taskRun.onError();
                disposeByKey(key);
            }

            @Override
            public void onComplete() {
                //事件队列完结。RxJava 不仅把每个事件单独处理，还会把它们看做一个队列。
                // RxJava 规定，当不会再有新的 onNext() 发出时，需要触发 onCompleted() 方法作为标志。
                taskRun.onOk();
                disposeByKey(key);

            }
        });
    }
    //dispose():切断Observable和Observer的联系；
    private void disposeByKey(String key) {
        Disposable d = task.get(key);
        if (d != null) {
            if (!d.isDisposed()) {
                d.dispose();

            }
            d = null;
        }
        task.put(key, null);
    }
    public void relese(){
//        if(myVideoView!=null){
//            myVideoView.suspend();
//            myVideoView=null;
//        }
//        if(myVideoViewTop!=null){
//            myVideoViewTop.suspend();
//            myVideoView=null;
//        }
    }

}
