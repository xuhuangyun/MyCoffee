package com.fancoff.coffeemaker.service;

import android.view.MotionEvent;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.bean.MsgEvent;
import com.fancoff.coffeemaker.bean.RemoveEvent;
import com.fancoff.coffeemaker.bean.coffe.CoffeeConfigBean;
import com.fancoff.coffeemaker.utils.RxBus;

import java.util.ArrayList;

/**
 * Created by apple on 2018/4/4.
 * 屏保等页面休眠关闭时间控制
 */

/**
 * 1、有默认的界面超时时间：取杯、卡杯、制作、设置等界面的超时时间
 * 2、
 */
public class DurationUtil {

    static DurationUtil sleepUtil;

    public static DurationUtil getIns() {
        if (sleepUtil == null) {
            sleepUtil = new DurationUtil();
        }
        return sleepUtil;

    }

    /**增加RemoveEvent到ArrayList<RemoveEvent> fragmentRemoveTask列表
     * BaseFragment会调用*/
    public void startRemoveTime(RemoveEvent event) {
        fragmentRemoveTask.add(event);
    }
    /**从ArrayList<RemoveEvent> fragmentRemoveTask列表移除RemoveEvent
     * BaseFragment会调用*/
    public void cancleRemoveTime(RemoveEvent event) {
        fragmentRemoveTask.remove(event);
    }

    public final static int bannerDefultDuration = 10000;
    public final static int MINHEATDefultDuration = 3;//最低心跳秒
    public final static int HEATDefultDuration = MINHEATDefultDuration;//默认心跳时间秒
    private final static int Defult_SLEEPTIME_MAKEERROR_DIALOG = 10;//秒 制作错误界面超时时间
    private final static int Defult_SLEEPTIME_STUCK_CUP_DIALOG = 60;//秒移除卡杯界面超时时间
    private final static int Defult_SLEEPTIME_SORRY_DIALOG = 10;//秒 sorry界面超时时间
    private final static int Defult_SCREEN_SLEEPTIME = 60;//秒
    private final static int Defult_SLEEPTIME_MAIN = 60;//秒
    private final static int Defult_SLEEPTIME_PAY = 90;//秒  默认支付超时90s
    private final static int Defult_SLEEPTIME_SETTING = 300;//秒  设置界面超时时间

    private static final int Defult_PICK_CUP = 40;//取杯时间默认40秒
    private static final int MACKCF_TIMEOUT = 6;//秒制作超时时间

    int NoActionTime = 0;

    /** 从机器工艺表中获得取杯时间 默认40s*/
    public int getPickCupTime() {
        if (DataCenter.getInstance().getMachineConfig() != null) {
            int del = DataCenter.getInstance().getMachineConfig().getCupStuck_delay();
            if (del > 0) {
                return del;
            }
        }
        return Defult_PICK_CUP;
    }

    /**获得卡杯时间：默认的 60s */
    public int getStuckCupTime() {
        return Defult_SLEEPTIME_STUCK_CUP_DIALOG;
    }

    /**获得制作超时时间：默认的 6s */
    public int getMakeOutTime() {
        return MACKCF_TIMEOUT;
    }

    /**获得设置时间：默认的 300s */
    public int getSettingTime() {
        return Defult_SLEEPTIME_SETTING;
    }

    /** 获得咖啡工艺中top的轮询时间 s，默认10s*/
    public int getTopBannerTime() {
        CoffeeConfigBean nowCoffeeConfigBean = DataCenter.getInstance().getNowCoffeeConfigBean();
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getParameter() != null
                    && nowCoffeeConfigBean.getParameter().getDuration() != null) {
                int du = nowCoffeeConfigBean.getParameter().getDuration().getTop();
                if (du > 0) {
                    return du * 1000;
                }

            }
        }
        return bannerDefultDuration;

    }


    /**
     * 从数据中心获得咖啡工艺；
     * 从咖啡工艺中取出心跳时间，最低心跳时间为3秒
     */
    public int getHeatTime() {
        CoffeeConfigBean nowCoffeeConfigBean = DataCenter.getInstance().getNowCoffeeConfigBean();
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getParameter() != null) {//咖啡工艺中有Parameter
                int time = nowCoffeeConfigBean.getParameter().getHeartbeat_time();//获得心跳时间
                if (time < MINHEATDefultDuration) {//心跳最低3秒
                    return MINHEATDefultDuration;
                } else {
                    return time;
                }
            }
        }
        return HEATDefultDuration;

    }

    /** 制作错误超时时间 10s */
    public int getMakeErrorTime() {

        return Defult_SLEEPTIME_MAKEERROR_DIALOG;

    }

    /** sorry超时时间 10s */
    public int getSorryTime() {

        return Defult_SLEEPTIME_SORRY_DIALOG;

    }

    /**获得咖啡工艺中左下角图片的轮询时间 s，默认10s*/
    public int getBottomBannerTime() {
        CoffeeConfigBean nowCoffeeConfigBean = DataCenter.getInstance().getNowCoffeeConfigBean();
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getParameter() != null
                    && nowCoffeeConfigBean.getParameter().getDuration() != null) {
                int du = nowCoffeeConfigBean.getParameter().getDuration().getLeft_bottoms();
                if (du > 0) {
                    return du * 1000;
                }
            }
        }
        return bannerDefultDuration;

    }

    /**获得咖啡工艺中进入屏保时间s，默认60s*/
    public int getScreensaverTime() {
        CoffeeConfigBean nowCoffeeConfigBean = DataCenter.getInstance().getNowCoffeeConfigBean();
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getParameter() != null
                    && nowCoffeeConfigBean.getParameter().getWaiting_time() != null) {
                int du = nowCoffeeConfigBean.getParameter().getWaiting_time().getScreensaver();
                if (du > 0) {
                    return du;
                }
            }
        }
        return Defult_SCREEN_SLEEPTIME;

    }

    /**获得咖啡工艺中退回到主菜单时间 默认60s*/
    public int getMainTime() {
        CoffeeConfigBean nowCoffeeConfigBean = DataCenter.getInstance().getNowCoffeeConfigBean();
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getParameter() != null
                    && nowCoffeeConfigBean.getParameter().getWaiting_time() != null) {
                int du = nowCoffeeConfigBean.getParameter().getWaiting_time().getMain();
                if (du > 0) {
                    return du;
                }
            }
        }
        return Defult_SLEEPTIME_MAIN;

    }

    /**获得咖啡工艺中支付超时时间 默认90s*/
    public int getPayTime() {
        CoffeeConfigBean nowCoffeeConfigBean = DataCenter.getInstance().getNowCoffeeConfigBean();
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getParameter() != null
                    && nowCoffeeConfigBean.getParameter().getWaiting_time() != null) {
                int du = nowCoffeeConfigBean.getParameter().getWaiting_time().getPay();
                if (du > 0) {
                    return du;
                }
            }
        }
        return DurationUtil.Defult_SLEEPTIME_PAY;

    }

    /**获得咖啡工艺中屏保轮询时间*/
    public int getScreenBannerTime() {
        CoffeeConfigBean nowCoffeeConfigBean = DataCenter.getInstance().getNowCoffeeConfigBean();
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getParameter() != null
                    && nowCoffeeConfigBean.getParameter().getDuration() != null) {
                int du = nowCoffeeConfigBean.getParameter().getDuration().getScreensaver();
                if (du > 0) {
                    return du * 1000;
                }
            }
        }
        return bannerDefultDuration;

    }

    /** 属性NoActionTime++ */
    public void runTime() {
        NoActionTime++;
//        if (NoActionTime > getScreensaverTime()) {
//            NoActionTime = getScreensaverTime();
//        }

    }

    /** 有触摸事件的时候，设置NoActionTime = 0 */
    public void onTouchScreen(MotionEvent event) {
        seTimeToAwake();

    }

    /**NoActionTime = 0;*/
    public void seTimeToAwake() {
        NoActionTime = 0;
    }

    public void seTimeToSleep() {
        NoActionTime = getScreensaverTime();
    }

    ArrayList<RemoveEvent> fragmentRemoveTask = new ArrayList<RemoveEvent>();

    /**
     * 有fragment对话框的事件，超时退出fragment；没有事件则时间到后进入屏保（AppService会1秒钟调用runTimeToSleep->timeToIntent）；
     * RemoveEvent实例列表：（退出对话框事件）
     *    有事件列表：
     *        循环实例列表：（每秒每个RemoveEvent事件的showTime+1，并进行时间判断）
     *            1、读取RemoveEvent.showTime，然后showTime = showTime+1；（每1s进来一次）
     *            2、读取RemoveEvent.controlType：(用来退出工程菜单或者其他的fragment)
     *                   controlType=TYPE_TOUCH_RESET_TIME:触摸后重新计算时间（触摸事件会使NoActionTime置0，所以用NoActionTime计算）
     *                        NoActionTime>=RemoveEvent.timeToRemove: 需要退出的时间到了，NoActionTime设置为0，
     *                        post(MsgEvent实例)， 发送msg消息到MAinActivity，主线程处理该消息更新ui；
     *                   controlType!=TYPE_TOUCH_RESET_TIME:触摸后不重新计算时间（用showTime计时）
     *                        showTime>=timeToRemove:showtime累加的时间到达需要退出的时间了 NoActionTime设置为0，
     *                        post(MsgEvent实例)， 发送msg消息到MainActivity，主线程处理该消息更新ui；
     *    没有事件列表：
     *            有屏保图片，NoActionTime>=进入屏保时间：
     *               NoActionTime=进入屏保时间，发送进入屏保的消息到MainActivity主线程了
     *            无屏保图片：
     *               NoActionTime=0；
     */
    public void timeToIntent() {
        if (fragmentRemoveTask != null && fragmentRemoveTask.size() > 0) {
            for (RemoveEvent event : fragmentRemoveTask) {
                event.setShowTime(event.getShowTime() + 1);//1s钟进一次，所以进一次加1；

                if (event.getControlType() == RemoveEvent.TYPE_TOUCH_RESET_TIME) {
                    //触摸屏幕后，复位时间NoActionTime=0，所以用NoActionTime来计时
//                    LogUtil.test("timeToIntent:NoActionTime:" + NoActionTime + "-RemoveTime" + event.getTimeToRemove() + "tag:" + event.getTag());
                    if (NoActionTime >= event.getTimeToRemove()) {
                        seTimeToAwake();//NoActionTime = 0;
                        RxBus.getInstance().post(new MsgEvent(MyConstant.ACTION.INTENT_TO_REMOVE, event.getTag()));
                        //MsgEvent.mMsg=RemoveEvent.tag; MsgEvent.type=INTENT_TO_REMOVE = 0
                        //post MsgEvent实例
                        //发现消息msg到MainActiviyt中，主线程根据解析msg并更新ui
                    }
                } else {//触摸了，不复位时间，用showTime来计时
//                    LogUtil.test("timeToIntent:showTime" + event.getShowTime() + "-RemoveTime" + event.getTimeToRemove() + "tag:" + event.getTag());
                    if (event.getShowTime() >= event.getTimeToRemove()) {
                        seTimeToAwake();//NoActionTime = 0;
                        RxBus.getInstance().post(new MsgEvent(MyConstant.ACTION.INTENT_TO_REMOVE, event.getTag()));
                    }
                }
            }
        } else {
//            LogUtil.test("timeToIntent:NoActionTime:" + NoActionTime + "ScreensaverTime:" + getScreensaverTime());
            if (DataCenter.getInstance().getScreenBannerlist() != null
                    && DataCenter.getInstance().getScreenBannerlist().size() > 0) {
                if (NoActionTime >= getScreensaverTime()) {//默认NoActionTime>60s
                    NoActionTime = getScreensaverTime();
                    RxBus.getInstance().post(new MsgEvent(MyConstant.ACTION.INTENT_TO_SCREEN_FRAGMENT));
                }
            } else {
                seTimeToAwake();//NoActionTime = 0;
            }
        }
    }
}
