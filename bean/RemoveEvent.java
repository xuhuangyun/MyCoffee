package com.fancoff.coffeemaker.bean;

/**
 * 猜估计是退出对话框事件：(多少时间到了后退出对话框)
 * 1、showTime、controlType、tag、timeToRemov这些属性的读取和设置；
 * 2、RemoveEvent(String tag, int controlType,int timeToRemove)：
 *    tat、controlType、timeToRemove属性的设置
 */
public class RemoveEvent {
    public static final int TYPE_TOUCH_RESET_TIME =0;//有触摸时重新计算退出时间
    public static final int TYPE_TOUCH_NO_RESET_TIME=1;//有触摸时不重新计算退出时间
    private int showTime;
    private int controlType;  //这个估计是上两个静态属性TYPE_TOUCH_RESET_TIME或TYPE_TOUCH_NO_RESET_TIME
    private String tag;  //估计是对话框的标记
    private int timeToRemove;

    /**showTime*/
    public int getShowTime() {
        return showTime;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    public int getControlType() {
        return controlType;
    }

    public void setControlType(int controlType) {
        this.controlType = controlType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /**timeToRemove*/
    public int getTimeToRemove() {
        return timeToRemove;
    }

    public void setTimeToRemove(int timeToRemove) {
        this.timeToRemove = timeToRemove;
    }

    public RemoveEvent(String tag, int controlType,int timeToRemove) {
        this.tag = tag;
        this.timeToRemove = timeToRemove;
        this.controlType=controlType;
    }

}