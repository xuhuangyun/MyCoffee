package com.fancoff.coffeemaker.service;

/**
 * Created by apple on 2018/4/4.
 * 心跳管理类
 */

public class HeatUtil {

    static HeatUtil sleepUtil;

    public static HeatUtil getIns() {
        if (sleepUtil == null) {
            sleepUtil = new HeatUtil();
        }
        return sleepUtil;

    }

    int count;

    /**设置count为心跳间隔时间-5*/
    public void runHeatNextM(int time) {
        int t = DurationUtil.getIns().getHeatTime() - time;
        if (t < DurationUtil.MINHEATDefultDuration) {
            t = DurationUtil.MINHEATDefultDuration;
        }
        count = t;

    }

    /**count为咖啡工艺中的心跳间隔时间*/
    public void runNow() {
        count = DurationUtil.getIns().getHeatTime();

    }

    /**
     * 间隔1s会被调用，心跳间隔时间到了返回true；
     * count++;
     * count>=咖啡工艺中的间隔时间
     *      count=0；返回true
     *  返回false；
     */
    public boolean isTimeToHeat() {
        count++;
        if (count >= DurationUtil.getIns().getHeatTime()) {
            count = 0;
            return true;
        }
        return false;
    }

    /**
     * 返回心跳时间，最低为3秒
     */
    public void initHeatTime() {
        count = DurationUtil.getIns().getHeatTime();
    }
}
