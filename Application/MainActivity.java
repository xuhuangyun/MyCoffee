package com.fancoff.coffeemaker.Application;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.MakingStatebean;
import com.fancoff.coffeemaker.bean.MessageBean;
import com.fancoff.coffeemaker.bean.MsgEvent;
import com.fancoff.coffeemaker.bean.PayBean;
import com.fancoff.coffeemaker.bean.VMCState;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.bean.coffe.ShowImageBean;
import com.fancoff.coffeemaker.io.CMDUtil;
import com.fancoff.coffeemaker.io.IOService;
import com.fancoff.coffeemaker.io.MainViewCallBack;
import com.fancoff.coffeemaker.service.AppService;
import com.fancoff.coffeemaker.service.DurationUtil;
import com.fancoff.coffeemaker.service.TaskUtil;
import com.fancoff.coffeemaker.ui.Imageshow.ImageShowFragment;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.buyPage.BuyPageFragment;
import com.fancoff.coffeemaker.ui.commomView.MyRelativeLayout;
import com.fancoff.coffeemaker.ui.commomView.MyTextView;
import com.fancoff.coffeemaker.ui.dialog.DialogMakeErrorFragment;
import com.fancoff.coffeemaker.ui.dialog.DialogPageFragment;
import com.fancoff.coffeemaker.ui.dialog.DialogSorryFragment;
import com.fancoff.coffeemaker.ui.dialog.ProgressPageFragment;
import com.fancoff.coffeemaker.ui.goods.GoodsFragment;
import com.fancoff.coffeemaker.ui.logo.LogoPageFragment;
import com.fancoff.coffeemaker.ui.main.MainPageFragment;
import com.fancoff.coffeemaker.ui.making.MakingPageFragment;
import com.fancoff.coffeemaker.ui.making.MakingPageMode;
import com.fancoff.coffeemaker.ui.pay.PayPageFragment;
import com.fancoff.coffeemaker.ui.pickup.PickPageFragment;
import com.fancoff.coffeemaker.ui.screen.ScreenPageFragment;
import com.fancoff.coffeemaker.ui.setting.SettingPageFragment;
import com.fancoff.coffeemaker.utils.AppInfoUtil;
import com.fancoff.coffeemaker.utils.FileUtil;
import com.fancoff.coffeemaker.utils.RxBus;
import com.fancoff.coffeemaker.utils.SdCardUtil;
import com.fancoff.coffeemaker.utils.TimeUtil;
import com.fancoff.coffeemaker.utils.glide.ImageLoadUtils;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.huxq17.floatball.libarary.FloatBallManager;
import com.huxq17.floatball.libarary.floatball.FloatBallCfg;
import com.huxq17.floatball.libarary.utils.BackGroudSeletor;
import com.huxq17.floatball.libarary.utils.DensityUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.HashMap;

import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * 入口MainActivity
 */
public class MainActivity extends RxAppCompatActivity implements BaseFragment.OnFragmentInteractionListener, AppService.ViewStateCallBack, MainViewCallBack {


    NumberProgressBar number_progress_bar;
    MyTextView t_show;

    AppService appService;

    MyRelativeLayout dialgo_layout;
    Button btn_test;
    //使用自定义的CalligraphyContextWrapper类
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 当与service的连接建立后被调用
            appService = ((AppService.MyBinder) service).getService();
            appService.startTime(MainActivity.this);//开启服务后，调用该方法可以间隔1s执行任务：
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 当与service的连接意外断开时被调用
            appService = null;
        }
    };
    IOService ioService;
    PowerManager.WakeLock wakeLock;
    boolean init;
    boolean isruning;
    Snackbar mSnackbar2;
    String dialogShow = "";
    /************************fragment基础操作类************************************/
    MainPageFragment mainFragment;
    BuyPageFragment buyPageFragment;
    PickPageFragment pickPageFragment;
    ImageShowFragment imageShowFragment;
    ScreenPageFragment screenPageFragment;
    LogoPageFragment logoPageFragment;
    SettingPageFragment settingPageFragment;
    ProgressPageFragment progressPageFragment;

    ServiceConnection connIo = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.test("onServiceConnected");
            CMDUtil.getInstance().setMainViewCallBack(MainActivity.this);
            ioService = ((IOService.MyBinder) service).getService();
            CMDUtil.getInstance().setIoService(ioService);
            CMDUtil.getInstance().start();
            initDate();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            CMDUtil.getInstance().setIoService(null);
        }
    };
    PayPageFragment payPageFragment;
    MakingPageFragment makingPageFragment;
    DialogMakeErrorFragment dialogMakeErrorFragment;//制作失败提示框
    DialogPageFragment dialogPageFragment;//警告框，不能手动取消
    DialogSorryFragment dialogSorryFragment;
    boolean isrunPermmision;
    /*********************悬浮球代码，app暂时获取不到系统权限不可用*********************************/
    private FloatBallManager mFloatballManager;
    private ActivityLifeCycleListener mActivityLifeCycleListener = new ActivityLifeCycleListener();

    //vmc返回数据回调

    /**
     * 回调接口方法实现； MainViewCallBack类中的接口MainViewCallBack中的抽象方法：void showViewByVmc(VMCState vmcStateBean);
     * 有MainPageFragment实例，进行主线程界面处理：
     *   1、有视频缓存则显示提示和进度，有VMC故障则提示VMC故障；MakingPageMode.makingStatebean = null
     *   2、无VMC故障：
     *      2.1、显示工程菜单并更新工程菜单内容；
     *      2.2、正在清洗，显示故障界面对话框---清洗；MakingPageMode.makingStatebean = null
     *      2.3、不在清洗：
     *           2.3.1、网络没有断开，已注册，有配置表：
     *                  移除故障对话框；
     *
     */
    @Override
    public void showViewByVmc(final VMCState vmcStateBean) {
        if (hasFragmentAdd(MainPageFragment.class.getName())) {//有MainPageFragment
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!shoWarnErrorDialg(vmcStateBean)) {//vmc错误dialog 没有vmc通讯错误返回false
                        showViewSetting(vmcStateBean);//工程模式界面dialog
                        if (!showCleanDialog(vmcStateBean)) {//清洗中dialog
                            if (!showDEVerrorDialg()) {//网络断开或者初始化失败
                                removeFragment(DialogPageFragment.class.getName());
                            }
                            showMaking(vmcStateBean);//制作咖啡界面刷新
                        } else {
                            stopMaking();  //MakingPageMode.makingStatebean = null
                        }
                    } else {
                        stopMaking(); //MakingPageMode.makingStatebean = null
                    }
                }
            });
        }
    }

    /**MakingPageMode.makingStatebean = null*/
    private void stopMaking() {
        MakingPageMode.getIns().setMakingStatebean(null);
    }

    @Override
    protected void onStop() {
        LogUtil.action("MainACtivity onStop");
        MyApp.getIns().setRunBackGround(true);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.action("MainACtivity onDestroy");
        TimeUtil.getInstance().stopTimerschedule();
        releaseAllFragment();
        RxBus rxbux = RxBus.getInstance();
        rxbux.unregisterAll();
        rxbux = null;
        if (conn != null && appService != null) {
            unbindService(conn);

        }
        if (connIo != null && ioService != null) {
            unbindService(connIo);
        }
        ImageLoadUtils.getInstance().release();
        if (MyApp.OPEN_FLOAT) {
            getApplication().unregisterActivityLifecycleCallbacks(mActivityLifeCycleListener);
        }
        MyApp.getIns().relese();
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        LogUtil.action("MainACtivity onResume");
        MyApp.getIns().setRunBackGround(false);
        super.onResume();
        if (MyApp.OPEN_FLOAT) {
            mFloatballManager.reqPermise();
        }

//        if (appService != null) {
//            appService.checkNet(this);
//        }

        wakeLock = ((PowerManager) getSystemService(POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        | PowerManager.ON_AFTER_RELEASE, this.getClass().getName());
        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        LogUtil.action("MainACtivity onPause");
        super.onPause();
        if (wakeLock != null) {
            wakeLock.release();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LogUtil.action("MainACtivity onCreate");
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.
                FLAG_KEEP_SCREEN_ON);//窗口标志:只要这个窗口对用户可见，保持设备的屏幕打开和明亮
        if (MyApp.OPEN_FLOAT) {
            init();
        }
        MyApp.getIns().thisActivity(this);
        TimeUtil.getInstance().startTimerschedule();//间隔1s执行HashMap<String, RunCallBack> mapRuns任务
        //Event 类型的 Observable 订阅
        RxBus.getInstance().register(MsgEvent.class).subscribe(new Consumer<MsgEvent>() {
            //线程1发送过来，RxBus在线程1；线程2发送过来，RxBus在线程2；处理界面需要进入UI线程：runOnUiThread
            @Override
            public void accept(final MsgEvent msg) throws Exception {
                runOnUiThread(new Runnable() {//切换到主线程执行
                    @Override
                    public void run() {
                        if (msg.getType() == MyConstant.ACTION.INTENT_TO_REMOVE) {
                            //INTENT_TO_REMOVE=0；用来隐藏工程菜单（需要发送命令给VMC）或者其他的fragment
                            if (msg.getmMsg().equals(SettingPageFragment.class.getName())
                                    && hasFragmentAdd(SettingPageFragment.class.getName())) {
                                //tag是SettingPageFragment，并且工程菜单界面纯在，退出工程菜单；
                                settingPageFragment.cancle();//发送隐藏工程菜单命令
                            }
                            //tag是其他的fragment，则退出其他的fragment
                            removeFragment(msg.getmMsg());//移除fragment
                        } else if (msg.getType() == MyConstant.ACTION.INTENT_TO_SCREEN_FRAGMENT) {
                            //INTENT_TO_SCREEN_FRAGMENT=6,没有其他的fragment事件，
                            // 且屏保时间到了DurationUtil类的timeToIntent方法会发消息到这，
                            releaseNomalFragment(MakingPageFragment.class.getName());//移除普通的二级界面
                            showScreenFragment();//显示屏保界面

                        } else if (msg.getType() == MyConstant.ACTION.REF_ALL) {//REF_ALL=15
                                  //TaskUtil类中的getConfig方法发送REF_ALL消息；
                            refView();//更新主界面、屏保图片
                        } else if (msg.getType() == MyConstant.ACTION.REF_ALL_NOGOODS) {//REF_ALL_NOGOODS=16
                            refViewNoGoods(); //更新主界面、更新屏保
                        } else if (msg.getType() == MyConstant.ACTION.RESUME_APP) {//RESUME_APP=20
                            //AppService类的checAppRun()方法会发送该消息，app在后台运行2分钟到后需进入前台；
                            AppInfoUtil.startMainLauncher(MainActivity.this);  //进入ACTION_MAIN
                        } else if (msg.getType() == MyConstant.ACTION.RESUME_FLOAT) {//RESUME_FLOAT=21
                            setFloatballVisible(true);//暂时不用
                        } else if (msg.getType() == MyConstant.ACTION.HIDE_FLOAT) {//HIDE_FLOAT=22
                            setFloatballVisible(false);//暂时不用
                        }
                    }
                });
            }
        });
        btn_test = findViewById(R.id.btn_test);
        t_show = findViewById(R.id.t_show);
        dialgo_layout = findViewById(R.id.dialgo_layout);
        number_progress_bar = findViewById(R.id.number_progress_bar);
        number_progress_bar.setVisibility(View.GONE);
        t_show.setText("");
        if (MyApp.TEST_CTASH) {
            btn_test.setVisibility(View.VISIBLE);//AA 20 00 02 01 00 89
            btn_test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    CrashReport.testJavaCrash();
                }
            });
        }
        if (MyApp.addTextFile) {
            showProgress("正在导入测试数据...");
            MyApp.getIns().runTask("addTextFile", new MyApp.TaskRun() {
                @Override
                public void run() {
                    FileUtil.deleteFolderFile(FilesManage.ROOT_PATH, true);
                    SdCardUtil.copyAssets(MainActivity.this, FilesManage.ROOT_DRI, FilesManage.ROOT_PATH);
                }

                @Override
                protected void onOk() {
                    hideProgress();
                    startIoService();
                }

                @Override
                protected void onError() {
                    super.onError();
                    hideProgress();
                    startIoService();
                }
            });
        } else {
            startIoService();
        }

    }

    void startIoService() {
        Intent intentIo = new Intent(MainActivity.this, IOService.class);
        bindService(intentIo, connIo, BIND_AUTO_CREATE);
    }

//    //咖啡故障
//    private boolean showCoffeErrorDialog(VMCStateBean vmcStateBean) {
//
//        if (vmcStateBean.stuckCup()) {
//            releaseNomalFragment("");
//            showDialogCoffeErrorFragment();
//            return true;
//
//        } else {
//            return false;
//        }
//    }

    private void initDate() {
        if (isruning) {
            return;
        }
        if (!init) {
            isruning = true;
            showProgress("正在初始化...");

            MyApp.getIns().runTask("key_init", new MyApp.TaskRun() {
                @Override
                public void run() {
                    DataCenter.getInstance().initLocalData(MyApp.addTextFile);

                }

                @Override
                protected void onError() {
                    super.onError();
                    TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0003, MyConstant.APP_ERROR_CODE.ERROR0003_S);
                }

                @Override
                public void onOk() {
                    TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0003, null);
                    showMainFragmne();
                    Intent intent = new Intent(MainActivity.this, AppService.class);
                    bindService(intent, conn, BIND_AUTO_CREATE);
                    hideProgress();
                    init = true;
                    isruning = false;

                }
            });

        } else {
            Intent intent = new Intent(MainActivity.this, AppService.class);
            bindService(intent, conn, BIND_AUTO_CREATE);
            /**
             * 第一个bindService()的参数是一个明确指定了要绑定的service的Intent．
             * 第二个参数是ServiceConnection对象．
             * 第三个参数是一个标志，它表明绑定中的操作．它一般应是BIND_AUTO_CREATE，
             * 这样就会在service不存在时创建一个．其它可选的值是BIND_DEBUG_UNBIND和BIND_NOT_FOREGROUND,不想指定时设为0即可．
             */
        }

    }

    /**设置字体的第4步：在Activity中注入Context，重写一个方法*/
    //attachBaseContext()方法原本是由系统来调用的
    // 我们将自定义的ContextImpl对象作为参数传递到attachBaseContext()方法当中,从而赋值给mBase对象
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * BaseFragment类中定义了该接口方法，MAinActivity实现了该方法；
     * BaseFragment会发送消息，该方法接收消息并进行处理
     */
    @Override
    public void onFragmentInteraction(final MessageBean bean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bean.getAction() == MessageBean.ACTION_INTENT) {
                    intentAction(bean.getWhat(), bean);
                } else if (bean.getAction() == MessageBean.ACTION_BACK) {
                    removeFragment(bean.getMsg());
                } else if (bean.getAction() == MessageBean.ACTION_PROGRESS) {
                    showProgress(bean.getMsg());
                } else if (bean.getAction() == MessageBean.ACTION_HIDEPROGRESS) {
                    hideProgress();
                } else if (bean.getAction() == MessageBean.ACTION_CONTENT) {
                    if (bean.getWhat() == MyConstant.WHAT.MAIN_ONRESUME) {
                        if (mainFragment != null) {
                            mainFragment.onResume();
                        }

                    } else if (bean.getWhat() == MyConstant.WHAT.MAIN_ONPAUSE) {
                        if (mainFragment != null) {
                            mainFragment.onPause();
                        }
                    }
                }
            }
        });
    }

    /**
     * 处理action=ACTION_INTENT的个消息
     * 1、MyConstant.ACTION.INTENT_TO_BUY_FRAGMENT://购买ACTION；显示购买界面
     * 2、MyConstant.ACTION.INTENT_TO_PAY_FRAGMENT:支付ACTION；移除购买界面，调用支付界面
     * 3、
     * 4、MyConstant.ACTION.INTENT_TO_SHOWIMAGE：显示左下和右下图片的大图；
     * 5、
     * 6、
     * 7、
     * 8、MyConstant.ACTION.INTENT_TO_MAKING:制作ACTION，移除支付界面
     *
     */
    private void intentAction(int what, MessageBean bean) {
        switch (what) {
            case MyConstant.ACTION.INTENT_TO_BUY_FRAGMENT://购买ACTION
                showBuyFragment((CoffeeBean) bean.getObj());
                break;
            case MyConstant.ACTION.INTENT_TO_PAY_FRAGMENT://支付ACTION
                removeFragment(BuyPageFragment.class.getName());
                showPayFragment((PayBean) bean.getObj());
                break;
            case MyConstant.ACTION.INTENT_TO_PICK_FRAGMENT://取杯界面
                showPickFragment();
                break;
            case MyConstant.ACTION.INTENT_TO_SHOWIMAGE://显示大图

                showImageFragment((ShowImageBean) bean.getObj());
                break;
            case MyConstant.ACTION.INTENT_TO_LOGO_FRAGMENT:
                showLogoPageFragment();
                break;
            case MyConstant.ACTION.INTENT_TO_SETTING_FRAGMENT:
//                showSettingPageFragment();
                break;
            case MyConstant.ACTION.INTENT_TO_DIALOG_WR:

                break;
            case MyConstant.ACTION.INTENT_TO_MAKING://制作
                removeFragment(PayPageFragment.class.getName());
                break;
            case MyConstant.ACTION.INTENT_TO_PICK_MAKING:
                removeFragment(PickPageFragment.class.getName());
//                showMakingPageFragment((PayBean) bean.getObj());
                break;
            case MyConstant.ACTION.INTENT_TO_ADDCAR://添加购物车
                CoffeeBean g = (CoffeeBean) bean.getObj();
                break;


        }
    }

    /**
     * 实例化AppService接口中的方法:boolean isSleepFragment();
     * 有MakingPageFragment或者ProgressPageFragment返回True；
     */
    @Override
    public boolean isSleepFragment() {
        if (hasFragmentAdd(MakingPageFragment.class.getName())
                || hasFragmentAdd(ProgressPageFragment.class.getName())
                ) {
            return true;
        }
        return false;
    }

    /**
     * 实例化AppService接口中的方法:void refView();
     * 1、运行到主线程：
     *    1.1、MainPageFragment有实例：更新主界面；
     *    1.2、ScreenPageFragment有实例：更新屏保图片；
     */
    @Override
    public void refView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (hasFragmentAdd(MainPageFragment.class.getName())) {
                    mainFragment.refAllView();//更新主界面
                }
                if (hasFragmentAdd(ScreenPageFragment.class.getName())) {
                    screenPageFragment.refView();//更新屏保图片
                }
            }
        });


    }

    /**
     * 更新主界面、更新屏保
     */
    public void refViewNoGoods() {
        if (hasFragmentAdd(MainPageFragment.class.getName())) {
            mainFragment.refAllView();//更新主界面
        }
        if (hasFragmentAdd(ScreenPageFragment.class.getName())) {
            screenPageFragment.refView();//更新屏保
        }


    }

    /**
     * 实例化AppService接口中的方法：void showDialog();
     *   显示底部的Snackbar mSnackbar2;提示下载进度
     */
    @Override
    public void showDialog() {
        if (mSnackbar2 == null) {
            mSnackbar2 = Snackbar.make(dialgo_layout, dialogShow, Snackbar.LENGTH_INDEFINITE);
            mSnackbar2.getView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)); //colors.xml
            mSnackbar2.show();
        }
    }

    /**
     * 实例化AppService接口中的方法：void hideDialog();
     *   取消 Snackbar mSnackbar2
     */
    @Override
    public void hideDialog() {
        if (mSnackbar2 != null) {
            mSnackbar2.dismiss();
            mSnackbar2 = null;
        }


    }

    /**
     * 实例化接口AppService中的方法：void showProgress(String msg);
     *
     */
    @Override
    public void showProgress(String msg) {
        showProgressFragment(true, msg);
    }

    /**
     * 实例化接口AppService中的方法：void hideProgress();
     *
     */
    @Override
    public void hideProgress() {
        removeFragment(ProgressPageFragment.class.getName());
    }

    /**
     * VMC状态是busy，并且在清洗；显示故障提醒对话框：
     */
    private boolean showCleanDialog(VMCState vmcStateBean) {
        if (vmcStateBean.isBusy() && vmcStateBean.isCleanning()) {
            showDialogFragment(false, getString(R.string.warn), getString(R.string.cleaning));
            return true;
        }
        return false;
    }

    /**
     * 有视频下载缓存则显示、VMC通讯故障则显示；
     * 1、有视频缓存，则显示下载个数和下载进度；
     * 2、没有接收到vmc过来的数据、或者接收的数据错误，则调用showDialogFragment()进行故障对话框显示；
     */
    public boolean shoWarnErrorDialg(VMCState vmcStateBean) {
        showVedioDownLoadProgress();
        if (!vmcStateBean.isNull()) {//接收到vmc来的数据
            if (vmcStateBean.isRealData()) {
                return false;
            } else {
                showDialogFragment(false, getString(R.string.warn), getString(R.string.vmcerror));
                LogUtil.vmc(getString(R.string.vmcerror));
                return true;
            }
        } else {//没有接收到vmc来的数据，
            showDialogFragment(false, getString(R.string.warn), getString(R.string.vmcerror));
            LogUtil.vmc("isNull:" + getString(R.string.vmcerror));
            return true;
        }

    }

    /**
     * 有缓存列表，显示下载视频个数和下载进度；
     * 1、缓存的列表，url和进度值：
     * 2、有缓存列表：显示底部Snackbar，并显示下个视频个数和下载进度；
     *    无缓存列表：取消底部Snackbar；
     */
    private void showVedioDownLoadProgress() {
        HashMap<String, Integer> map = DataCenter.getInstance().getCachingPercentsMap();
        if (map.size() > 0) {
            showDialog();
            String sh = getString(R.string.downloadvedioshow);
            String mdialogShow = sh.replace("x", map.size() + "").replace("w", DataCenter.getInstance().getDownLoadProgress() + "");
            //下载个数和下载进度
            if (!mdialogShow.equals(dialogShow)) {
                LogUtil.action(mdialogShow);
            }
            dialogShow = mdialogShow;
            mSnackbar2.setText(dialogShow);

        } else {
            hideDialog();
        }
    }

    /**
     * 正在制作中：
     *     调用MakingPageMode的makingProgress方法，该方法会进行回调，本方法实现了回调方法；
     *         1、有清洗、脏杯、卡杯、其他意外则调用onError方法
     *               卡杯或者落杯器故障显示20分钟退款，其他显示制作失败对话框
     *         2、正常制作中的各步骤调用onSuccess方法：调用制作fragment，进行显示
     */
    private void showMaking(VMCState vmcStateBean) {
        if (MakingPageMode.getIns().isMakeing()) {//正在制作中
            MakingPageMode.getIns().makingProgress(vmcStateBean, new MakingPageMode.ErrorCallBack() {
                @Override
                public void onError(String failedS) {//正在清洗、脏杯、卡杯、其他意外情况、制作未开始会调用该方法
                    LogUtil.action("making failed：" + failedS);
                    removeFragment(MakingPageFragment.class.getName());
                    if (getString(R.string.canotremoveStukCup).equals(failedS)
                            || getString(R.string.canotremoveErrorCup).equals(failedS)) {//卡杯 或者落杯器故障 故障
                        showDialogSorryFragment(getString(R.string.sorrycontent));
                    } else {
                        showDialogCoffeErrorFragment(failedS);
                    }
                }

                @Override
                public void onSucess() {//正常制作中会调用该方法
                    showMakingPageFragment(MakingPageMode.getIns().getMakingStatebean());
                }
            });

        } else {
            removeFragment(MakingPageFragment.class.getName());
        }

    }

    /**
     * 1、无网络，显示网络已断开故障对话框；
     * 2、有网络：
     *     2.1、已注册：
     *          无机器工艺和咖啡工艺：提示故障对话框，本地无机器和咖啡工艺，请从服务端推送；
     *          无机器工艺：提示故障对话框，本地无机器工艺，请从服务端推送；
     *          无咖啡工艺：提示故障对话框，本地无咖啡工艺，请从服务端推送；
     *     2.2、无注册：
     *          提示故障对话框，初始化失败
     */
    private boolean showDEVerrorDialg() {
        if (!MyApp.getIns().checkNet(getApplicationContext())) {
            showDialogFragment(false, getString(R.string.warn), getString(R.string.nonet));
            return true;
        } else {
            if (DeviceInfo.getInstance().isRegist()) {
                if (!DataCenter.getInstance().hasMAtion() && !DataCenter.getInstance().hasCoffeeConfig()) {
                    showDialogFragment(false, getString(R.string.warn), getString(R.string.nocfandmach));
                    return true;
                } else if (!DataCenter.getInstance().hasMAtion()) {
                    showDialogFragment(false, getString(R.string.warn), getString(R.string.noma));
                    return true;
                } else if (!DataCenter.getInstance().hasCoffeeConfig()) {
                    showDialogFragment(false, getString(R.string.warn), getString(R.string.nocf));
                    return true;
                } else {
                    return false;
                }

            } else {
                if (MyApp.NONET) {
                    return false;
                }
                showDialogFragment(false, getString(R.string.initfailed), DeviceInfo.getInstance().getReturnMsd());
                return true;

            }
        }


    }

    //工程界面刷新
    /**
     *  1、工作模式在工程菜单：
     *     1.1、有工程菜单对话框实例：更新工程菜单内容
     *     1.2、无工程菜单对话框实例：移除屏保界面，显示工程菜单界面；
     *
     *  2、工作模式不在工程菜单：移除工程菜单；
     */
    private void showViewSetting(final VMCState vmcStateBean) {
        if (vmcStateBean.isShowDeBugMenu()) {
            if (hasFragmentAdd(SettingPageFragment.class.getName())) {
                settingPageFragment.refView(vmcStateBean);
            } else {
                releaseScreenFragment("");
                showSettingPageFragment();
            }
        } else {
            removeFragment(SettingPageFragment.class.getName());
        }


    }

    /**移除主页面fragment*/
    public void releaseMainFragment() {
        removeFragment(GoodsFragment.class.getName());
        removeFragment(MainPageFragment.class.getName());
    }

    /**ex与屏保对话框的Tag不相等，则删除屏保界面*/
    public void releaseScreenFragment(String ex) {
        removeFragmentWithExclude(ScreenPageFragment.class.getName(), ex);

    }

    public void releaseDialogFragment(String ex) {
        releaseScreenFragment(ex);
        removeFragmentWithExclude(ScreenPageFragment.class.getName(), ex);
        removeFragmentWithExclude(DialogPageFragment.class.getName(), ex);
        removeFragmentWithExclude(DialogMakeErrorFragment.class.getName(), ex);
    }

    /**移除普通二级页面fragment*/
    public void releaseNomalFragment(String ex) {

        removeFragmentWithExclude(ProgressPageFragment.class.getName(), ex);
        removeFragmentWithExclude(BuyPageFragment.class.getName(), ex);
        removeFragmentWithExclude(PickPageFragment.class.getName(), ex);
        removeFragmentWithExclude(LogoPageFragment.class.getName(), ex);
        removeFragmentWithExclude(PayPageFragment.class.getName(), ex);
        removeFragmentWithExclude(ImageShowFragment.class.getName(), ex);
        removeFragmentWithExclude(MakingPageFragment.class.getName(), ex);
        removeFragmentWithExclude(DialogSorryFragment.class.getName(), ex);
    }

    /**移除所有fragment*/
    public void releaseAllFragment() {
        releaseMainFragment();
        releaseDialogFragment("");
        releaseNomalFragment("");
    }

    @Override
    public void onBackPressed() {
        LogUtil.test("onBackPressed");
        DurationUtil.getIns().seTimeToAwake();
        if (!removeFragment(ProgressPageFragment.class.getName())
                && !removeFragment(BuyPageFragment.class.getName())
                && !removeFragment(PickPageFragment.class.getName())
                && !removeFragment(ScreenPageFragment.class.getName())
                && !removeFragment(PayPageFragment.class.getName())
                && !removeFragment(MakingPageFragment.class.getName())
                && !removeFragment(LogoPageFragment.class.getName())
                && !removeFragment(ImageShowFragment.class.getName())
                && !removeFragment(DialogMakeErrorFragment.class.getName())
                && !removeFragment(DialogSorryFragment.class.getName())) {
            MyApp.getIns().intentToSetting(MainActivity.this);

        }
    }

    /**tag标记的fragment是否存在，存在返回Fragment，不存在返回null*/
    public Fragment isHasFragment(String tag) {
        if (isNullByTag(tag)) {
            return null;
        } else {
            Fragment fra = getSupportFragmentManager()
                    .findFragmentByTag(tag);
            return fra;
        }

    }

    /**
     * 移除
     * tag标记的fragment存在，则该fragment=null；并移除该fragment；
     */
    public boolean removeFragment(String tag) {

        Fragment fra = isHasFragment(tag);
        if (fra != null) {
            releseFragmentByTag(tag);
            getSupportFragmentManager()
                    .beginTransaction().remove(fra).commitNowAllowingStateLoss();

            return true;
        }
        return false;

    }

    /**
     * tag标记的fragment=null
     */
    private void releseFragmentByTag(String tag) {
        if (BuyPageFragment.class.getName().equals(tag)) {
            buyPageFragment = null;
        } else if (PickPageFragment.class.getName().equals(tag)) {
            pickPageFragment = null;
        } else if (ScreenPageFragment.class.getName().equals(tag)) {
            screenPageFragment = null;
        } else if (PayPageFragment.class.getName().equals(tag)) {
            payPageFragment = null;
        } else if (MakingPageFragment.class.getName().equals(tag)) {
            makingPageFragment = null;
        } else if (LogoPageFragment.class.getName().equals(tag)) {
            logoPageFragment = null;
        } else if (SettingPageFragment.class.getName().equals(tag)) {
            settingPageFragment = null;
        } else if (ImageShowFragment.class.getName().equals(tag)) {
            imageShowFragment = null;
        } else if (DialogMakeErrorFragment.class.getName().equals(tag)) {
            dialogMakeErrorFragment = null;
        } else if (ProgressPageFragment.class.getName().equals(tag)) {
            progressPageFragment = null;
        } else if (DialogPageFragment.class.getName().equals(tag)) {
            dialogPageFragment = null;
        } else if (MainPageFragment.class.getName().equals(tag)) {
            mainFragment = null;
        } else if (DialogSorryFragment.class.getName().equals(tag)) {
            dialogSorryFragment = null;
        }
    }

    /**判断tag标记的fragment是否为空，是则返回true*/
    private boolean isNullByTag(String tag) {
        if (BuyPageFragment.class.getName().equals(tag)) {
            return buyPageFragment == null;
        } else if (PickPageFragment.class.getName().equals(tag)) {
            return pickPageFragment == null;
        } else if (ScreenPageFragment.class.getName().equals(tag)) {
            return screenPageFragment == null;
        } else if (PayPageFragment.class.getName().equals(tag)) {
            return payPageFragment == null;
        } else if (MakingPageFragment.class.getName().equals(tag)) {
            return makingPageFragment == null;
        } else if (LogoPageFragment.class.getName().equals(tag)) {
            return logoPageFragment == null;
        } else if (SettingPageFragment.class.getName().equals(tag)) {
            return settingPageFragment == null;
        } else if (ImageShowFragment.class.getName().equals(tag)) {
            return imageShowFragment == null;
        } else if (DialogMakeErrorFragment.class.getName().equals(tag)) {
            return dialogMakeErrorFragment == null;
        } else if (ProgressPageFragment.class.getName().equals(tag)) {
            return progressPageFragment == null;
        } else if (DialogPageFragment.class.getName().equals(tag)) {
            return dialogPageFragment == null;
        } else if (MainPageFragment.class.getName().equals(tag)) {
            return mainFragment == null;
        } else if (DialogSorryFragment.class.getName().equals(tag)) {
            return dialogSorryFragment == null;
        }
        return false;
    }

    /**tag标记的fragment存在且不为空，则返回true*/
    public boolean hasFragmentAdd(String tag) {
        return isHasFragment(tag) != null;

    }

    /**
     * 显示sorry对话框
     */
    private void showDialogSorryFragment(String content) {
        if (!hasFragmentAdd(DialogSorryFragment.class.getName())) {
            Bundle bundle = new Bundle();
            bundle.putString("content", content);
            dialogSorryFragment = DialogSorryFragment.newInstance(bundle);
            dialogSorryFragment.addControlOutTime(DurationUtil.getIns().getSorryTime(), false);//默认10s退出该对话框
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.parent, dialogSorryFragment, DialogSorryFragment.class.getName())
                    .commitNowAllowingStateLoss();
        }
    }

    /**
     * 主界面
     */
    public void showMainFragmne() {

        if (!hasFragmentAdd(MainPageFragment.class.getName())) {
            mainFragment = MainPageFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root, mainFragment, MainPageFragment.class.getName())
                    .commitNowAllowingStateLoss();
        }

    }

    /**
     * 屏保
     */
    public void showScreenFragment() {
        if (!hasFragmentAdd(ScreenPageFragment.class.getName())) {
            screenPageFragment = new ScreenPageFragment();
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.parent, screenPageFragment, ScreenPageFragment.class.getName())
                    .commitNowAllowingStateLoss();

        }
    }

    /**
     * 显示制作
     * 没有实例化的MakingPageFragment，则新建制作界面对象makingPageFragment；
     *
     */
    private void showMakingPageFragment(MakingStatebean makingStatebean) {
        if (!hasFragmentAdd(MakingPageFragment.class.getName())) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("obj", makingStatebean);
            makingPageFragment = MakingPageFragment.newInstance(bundle);
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.parent, makingPageFragment, MakingPageFragment.class.getName())
                    .commitNowAllowingStateLoss();

        }
    }

    /**
     * 支付对话框：购买对话框点击支付图片的时候会发送需要支付界面的消息到MainActivity，
     *             MainActivity进行消息识别处理，然后调用showPayFragment()方法
     * 1、传入的参数：payBean:二维码链接，支付类型；
     * 2、新建payPageFragment对象，传入payBean参数
     * 3、
     */
    private void showPayFragment(PayBean payBean) {
        if (!hasFragmentAdd(PayPageFragment.class.getName())) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("obj", payBean);
            payPageFragment = PayPageFragment.newInstance(bundle);
            payPageFragment.addControlOutTime(DurationUtil.getIns().getPayTime(), true);
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.parent, payPageFragment, PayPageFragment.class.getName())
                    .commitNowAllowingStateLoss();

        }

    }

    /**
     * 购买对话框：传入饮品
     * 1、buyPageFragment不存在：
     *    将键obj，值coffeeBean作为参数新建一个购买对话框对象buyPageFragment
     *    进行超时退出购买对话框事件的添加
     *    buyPageFragment添加到FragmentManager进行显示
     */
    public void showBuyFragment(CoffeeBean coffeeBean) {
        if (!hasFragmentAdd(BuyPageFragment.class.getName())) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("obj", coffeeBean);
            buyPageFragment = BuyPageFragment.newInstance(bundle);
            buyPageFragment.addControlOutTime(DurationUtil.getIns().getMainTime(), true);
            //获得咖啡工艺中退回到主菜单时间 默认60s
            //将buyPageFragment类名和超时时间添加到removeEvent事件中；
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.parent, buyPageFragment, BuyPageFragment.class.getName())
                    .commitNowAllowingStateLoss();
            //将buyPageFragment添加到FragmentManager进行显示
        }

    }

    /**
     * logo对话框
     */
    public void showLogoPageFragment() {
        if (!hasFragmentAdd(LogoPageFragment.class.getName())) {
            logoPageFragment = LogoPageFragment.newInstance();
            logoPageFragment.addControlOutTime(DurationUtil.getIns().getMainTime(), true);
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.parent, logoPageFragment, LogoPageFragment.class.getName())
                    .commitNowAllowingStateLoss();

        }
    }

    /**
     * 显示开发者菜单
     */
    public void showSettingPageFragment() {

        if (!hasFragmentAdd(SettingPageFragment.class.getName())) {
            settingPageFragment = SettingPageFragment.newInstance();
            settingPageFragment.addControlOutTime(DurationUtil.getIns().getSettingTime(), true);//默认300s超时退出对话框
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.top_layout, settingPageFragment, SettingPageFragment.class.getName())
                    .commitNowAllowingStateLoss();
        }

    }

    /**
     *
     * 新建ImageShowFragment实例对象imgeShowFragment，并传入参数：data、showImageBean
     * 将imageShowFragment添加到FragmentManager进行显示；
     */
    public void showImageFragment(ShowImageBean showImageBean) {
        if (!hasFragmentAdd(ImageShowFragment.class.getName())) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", showImageBean);
            imageShowFragment = ImageShowFragment.newInstance(bundle);
            imageShowFragment.addControlOutTime(DurationUtil.getIns().getMainTime(), true);//默认60s退出该fragment
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.parent, imageShowFragment, ImageShowFragment.class.getName())
                    .commitNowAllowingStateLoss();

        }
    }

    /**
     * 取货对话框
     */
    public void showPickFragment() {

        if (!hasFragmentAdd(PickPageFragment.class.getName())) {
            pickPageFragment = PickPageFragment.newInstance();
            pickPageFragment.addControlOutTime(DurationUtil.getIns().getMainTime(), true);
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.parent, pickPageFragment, PickPageFragment.class.getName())
                    .commitNowAllowingStateLoss();

        }

    }

    /**
     * 进度对话框：传入progerss，content内容
     * progress：
     *    true：
     *    false：
	      */
    public void showProgressFragment(boolean progress, String content) {
        if (!hasFragmentAdd(ProgressPageFragment.class.getName())) {//进度对话框实例不存在
            Bundle bundle = new Bundle();
            bundle.putString("content", content);
            bundle.putBoolean("progress", progress);
            progressPageFragment = ProgressPageFragment.newInstance(bundle);//新建进度对话框实例，并传入参数
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                     //为Fragment添加动画
                    .add(R.id.dialgo_layout, progressPageFragment, ProgressPageFragment.class.getName())
                    .commitNowAllowingStateLoss();
            DurationUtil.getIns().seTimeToAwake();//NoActionTime = 0
        } else {
            if (progressPageFragment != null) {
                progressPageFragment.ref(progress, content);
            }
        }

    }

    /**
     * 故障提醒对话框的显示DialogPageFragment：
     * 提醒VMC通讯故障：传入showBtnClear=false;title=提醒;content=VMC通讯故障;
     * 提醒正在清洗：传入showBtnClear=false;title=提醒；content=正在清洗中，请稍后购买....；
     * 无网络：传入false、提醒、网络已断开；
     * 无咖啡工艺、机器工艺，提醒；
     * 没有注册，初始化失败提醒；
     * 1、没有系统故障提醒页面:
     *       新建DialogPageFragment对话框实例dialogPageFragment:传入参数并进行立即显示；
     * 2、有系统故障提醒页面：
     *       更新故障提醒对话框的标题和内容；
     */
    public void showDialogFragment(boolean showBtnClear, final String title, final String content) {
//        MakingPageMode.getIns().setMakingStatebean(null);
        if (!hasFragmentAdd(DialogPageFragment.class.getName())) {
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("content", content);
            bundle.putBoolean("btn", showBtnClear);
            dialogPageFragment = DialogPageFragment.newInstance(bundle);
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.dialgo_layout, dialogPageFragment, DialogPageFragment.class.getName())
                    .commitNowAllowingStateLoss();
        } else {
            if (dialogPageFragment != null) {
                dialogPageFragment.refContent(title, content);
            }
        }
    }

    /**
     * 制作失败退款
     */
    public void showDialogCoffeErrorFragment(String content) {
        if (!hasFragmentAdd(DialogMakeErrorFragment.class.getName())) {
            Bundle bundle = new Bundle();
            bundle.putString("content", content);
            dialogMakeErrorFragment = DialogMakeErrorFragment.newInstance(bundle);
            dialogMakeErrorFragment.addControlOutTime(DurationUtil.getIns().getMakeErrorTime(), false);//默认10s退出该对话框
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.dialgo_layout, dialogMakeErrorFragment, DialogMakeErrorFragment.class.getName())
                    .commitNowAllowingStateLoss();
            DurationUtil.getIns().seTimeToAwake();

        }

    }

    /**excludeTag不等于removeTag;则移除removeTag*/
    public void removeFragmentWithExclude(String removeTag, String excludeTag) {
        if (!excludeTag.equals(removeTag)) {
            removeFragment(removeTag);
        }

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (MyApp.OPEN_FLOAT) {
            mFloatballManager.hide();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (MyApp.OPEN_FLOAT) {
            mFloatballManager.show();
        }
    }

    /**打开悬浮按钮，才会初始化*/
    private void init() {
        //1 初始化悬浮球配置，定义好悬浮球大小和icon的drawable
        int ballSize = DensityUtil.dip2px(this, 45);
        Drawable ballIcon = BackGroudSeletor.getdrawble("ic_floatball", this);
        //可以尝试使用以下几种不同的config。
//        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon);
//        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon, FloatBallCfg.Gravity.LEFT_CENTER,false);
//        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon, FloatBallCfg.Gravity.LEFT_BOTTOM, -100);
//        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon, FloatBallCfg.Gravity.RIGHT_TOP, 100);
        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon, FloatBallCfg.Gravity.RIGHT_CENTER);
        //设置悬浮球不半隐藏
        ballCfg.setHideHalfLater(false);
        mFloatballManager = new FloatBallManager(getApplicationContext(), ballCfg);
        mFloatballManager.setOnFloatBallClickListener(new FloatBallManager.OnFloatBallClickListener() {
            @Override
            public void onFloatBallClick() {
                AppInfoUtil.startMainLauncher(MainActivity.this);
            }
        });
        setFloatPermission();
        //6 如果想做成应用内悬浮球，可以添加以下代码。
        getApplication().registerActivityLifecycleCallbacks(mActivityLifeCycleListener);


    }

    private void setFloatPermission() {
        // 设置悬浮球权限，用于申请悬浮球权限的，这里用的是别人写好的库，可以自己选择
        //如果不设置permission，则不会弹出悬浮球
        mFloatballManager.setPermission(new FloatBallManager.IFloatBallPermission() {
            @Override
            public boolean onRequestFloatBallPermission() {
                requestFloatBallPermission(MainActivity.this);
                return true;
            }

            @Override
            public boolean hasFloatBallPermission(Context context) {
                return MyApp.getIns().getmFloatPermissionManager().checkPermission(context);
            }

            @Override
            public void requestFloatBallPermission(final Activity activity) {
                MyApp.getIns().getmFloatPermissionManager().applyPermission(activity);
                isrunPermmision = true;
                MyApp.getIns().runTask("getPermison", new MyApp.TaskRun() {
                    @Override
                    public void run() {
                        while (!MyApp.getIns().getmFloatPermissionManager().checkPermission(activity)) {
                            try {
                                LogUtil.test("requestFloatBallPermission");
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    protected void onOk() {
                        setFloatballVisible(true);
                        isrunPermmision = false;
                    }
                });
            }


        });
    }

    /**
     *  暂时不用
     */
    private void setFloatballVisible(boolean visible) {
        if (mFloatballManager != null) {
            if (visible) {
                if (mFloatballManager != null && mFloatballManager.hasFloatBallPermission()) {
                    mFloatballManager.show();
                }
            } else {
                mFloatballManager.hide();
            }
        }

    }

    public class ActivityLifeCycleListener implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            setFloatballVisible(false);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            setFloatballVisible(false);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            setFloatballVisible(false);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            setFloatballVisible(true);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            setFloatballVisible(true);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            setFloatballVisible(true);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            setFloatballVisible(false);
        }
    }


}




