package com.fancoff.coffeemaker.utils;

import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by apple on 2017/12/1.
 * 时间格式化类
 */

public class TimeUtil {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
    SimpleDateFormat formatterDate = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
    SimpleDateFormat formatterDateH = new SimpleDateFormat("yyyyMMddHH");
    private static final TimeUtil ourInstance = new TimeUtil();

    public static TimeUtil getInstance() {
        return ourInstance;
    }

    private TimeUtil() {
    }

    public String getNowDate() {
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatterDate.format(curDate);
        return str;
    }
    /**返回年月天小时的字符串*/
    public String getNowTimeHH() {
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatterDateH.format(curDate);
        return str;
    }

    /**获取当前时间:HHmm*/
    public String getNowTimeHHMM() {
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatterTime.format(curDate);
        return str;
    }

    /**获取当前时间:yyyyMMddHHmmssSSS*/
    public String getNowTime() {
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**获取当前时间:yyyyMMddHHmmss*/
    public String getNowTimeShort() {
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter2.format(curDate);
        return str;
    }

    /**
     * 当前时间大于影响起始时间，当前时间小于影响失效时间；返回1
     * 现在的时刻在秒杀的时间范围内
     */
    public boolean isBettew(String timeStart, String timeEnd) {
        try {
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            Date dateStart = formatter2.parse(timeStart);//yyyyMMddHHmmss
            Date dateEnd = formatter2.parse(timeEnd);
            long startDay = dateStart.getTime();
            long endDay = dateEnd.getTime();
            long nowDay = curDate.getTime();
            return (nowDay >= startDay && nowDay <= endDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
//        String str = formatter2.format(curDate);
//        return str;
    }

    private final static long ss = 1000;// 1分钟
    private final static long minute = 60 * ss;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 返回文字描述的日期
     *
     * @return
     */
    public static String getTimeFormatText(long diff) {
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "月";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟";
        }
        if (diff > ss) {
            r = (diff / ss);
            return r + "秒";
        }
        return "0秒";
    }

    /**失效时间ms-当前时间ms，差值比对年、月、日、时、分、秒，属于哪一档就返回该比例；
    *比如差值为2000，则返回2秒*/
    public String showLast(String effect_time, String expiration_time) {

        try {
            Date curDate = new Date(System.currentTimeMillis());
            Date dateStart = formatter2.parse(effect_time);
            Date dateEnd = formatter2.parse(expiration_time);
            long startDay = dateStart.getTime();
            long endDay = dateEnd.getTime();
            long nowDay = curDate.getTime();
            long ee = endDay - nowDay;
            if (ee < 0) {
                ee = 0;
            }
            return getTimeFormatText(ee);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

    }

    /**返回当前Date的日期(时间都清零了)*/
    public Date getStartTimeOfDay(Date date) {
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        return day.getTime();
        //getTime返回的是Date类型；日历的时间
    }

    /**日期不变，时间设置为23:59:59:999；当天最后的时刻*/
    public Date getEndTimeOfDay(Date date) {
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.set(Calendar.HOUR_OF_DAY, 23);
        day.set(Calendar.MINUTE, 59);
        day.set(Calendar.SECOND, 59);
        day.set(Calendar.MILLISECOND, 999);
        return day.getTime();
    }

   //string类型的时间转换为Date类型，并返回ms
    public long StringToDataTime(String time) {
        try {
            Date date = formatter2.parse(time);//yyyyMMddHHmmss
            return date.getTime();//ms方式返回时间
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //促销活动是否处于今日
    public boolean isToday(String effect_time, String expiration_time) {
        try {
            Date curDate = new Date();
            Date dateStart = formatter2.parse(effect_time);
            Date dateEnd = formatter2.parse(expiration_time);
            long startDay = dateStart.getTime();
            long endDay = dateEnd.getTime();

            long nowDayStart = getStartTimeOfDay(curDate).getTime();//年月日的ms数，当天开始时刻
            long nowDayEnd = getEndTimeOfDay(curDate).getTime();//当天结束时刻
            if (startDay <= endDay && ((startDay <= nowDayStart && endDay >= nowDayStart)
                    || (endDay >= nowDayEnd && startDay <= nowDayEnd)
                    || (startDay >= nowDayStart && endDay <= nowDayEnd))) {

                return true;

            }
        } catch (ParseException e) {
            LogUtil.error(e.toString());
        }
        return false;
    }

    String preNoTime = "";

    /**接口回调函数*/
    public interface TimeMMChangeCallBack {
        void onChange(String notime);
    }

    /**
     * AppService类中的checkTimeToClean()方法会调用该方法，传入接口实例；
     *     changeMM()执行后会回调AppService类中的checkTimeToClean()方法的onChange()方法
     *     变了1分钟，则执行回调；
     *     现在的时分赋予preNoTime；
     */
    public void changeMM(TimeMMChangeCallBack callback) {
        String noTime = TimeUtil.getInstance().getNowTimeHHMM();//时、分
        if (!noTime.equals(preNoTime)) {//时、分跟之前的时、分不一样，即走了1分钟了
            callback.onChange(noTime);
        }
        preNoTime = noTime;

    }

    String preNoTimehh = "";

    /**
     * noTime=当前的年月日时，noTime与preNoTimehh不同则会回调onChange;
     * preNoTimehh=noTime;
     * MyApp类的getgLogger方法会调用：TimeUtil.getInstance().changeHH(new TimeUtil.TimeMMChangeCallBack()
     * */
    public void changeHH(TimeMMChangeCallBack callback) {
        String noTime = TimeUtil.getInstance().getNowTimeHH();
        if (!noTime.equals(preNoTimehh)) {
            callback.onChange(noTime);
        }
        preNoTimehh = noTime;

    }

    /**RunCallBack接口，有一个runHander()方法需要实现*/
    public interface RunCallBack {
        void runHander();
//        void runThread();
    }

    Disposable disp;

    /**
     * mapRuns清除；
     * dispose清除；
     */
    public void stopTimerschedule() {
        synchronized ("runtask") {
            mapRuns.clear();
            if (disp != null) {
                disp.dispose();
                disp=null;
            }
        }

    }

    /**在HashMap<String, RunCallBack> mapRuns移除tag键对应的mapRuns*/
    public void removeTimerschedule(String tag) {
        synchronized ("runtask") {
            mapRuns.remove(tag);
        }
    }

    /**
     * 清除mapRuns,disp;
     * 每隔1s执行一次，遍历mapRuns执行回调runHander()；
     * 1、MyTextViewSkill类的update()会实现runHander()方法；
     * 2、MakePageMode类的showMakingContent会实现runHander()方法;
     */
    public void startTimerschedule() {
        stopTimerschedule();
        if(disp==null){
            disp = Observable.interval(0, 1, TimeUnit.SECONDS)//每隔1s执行一次，执行无限次
                    .compose(MyApp.getIns().getNowActivity().<Long>bindUntilEvent(ActivityEvent.DESTROY))
                    //使用ActivityEvent类，其中的CREATE、START、 RESUME、PAUSE、STOP、 DESTROY分别对应生命周期内的方法。
                    //使用bindUntilEvent指定在哪个生命周期方法调用时取消订阅。
                    .doOnNext(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {

                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            synchronized ("runtask") {
                                for (Map.Entry<String, RunCallBack> entry : mapRuns.entrySet()) {
                                    String key = entry.getKey();
                                    RunCallBack value = entry.getValue();
                                    if (value != null) {
                                        value.runHander();
                                    }
                                }
                            }
                        }
                    });
        }
    }

    HashMap<String, RunCallBack> mapRuns = new HashMap<>();

    /**
     * 在HashMap<String, RunCallBack> mapRuns添加key和runcallback
     */
    public void addTimerschedule(String key, final RunCallBack runcallback) {
        synchronized ("runtask") {
            mapRuns.put(key, runcallback);
        }
    }
}
