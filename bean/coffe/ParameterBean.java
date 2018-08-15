package com.fancoff.coffeemaker.bean.coffe;

import java.io.Serializable;

/**
 * Created by apple on 2018/2/1.
 */

public class ParameterBean implements Serializable {
    int heartbeat_time;
    WaitingTimeBean waiting_time;
    DurationBean duration;
    int pay_type;

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public DurationBean getDuration() {
        return duration;
    }

    public void setDuration(DurationBean duration) {
        this.duration = duration;
    }

    public int getHeartbeat_time() {
        return heartbeat_time;
    }

    public void setHeartbeat_time(int heartbeat_time) {
        this.heartbeat_time = heartbeat_time;
    }

    public WaitingTimeBean getWaiting_time() {
        return waiting_time;
    }

    public void setWaiting_time(WaitingTimeBean waiting_time) {
        this.waiting_time = waiting_time;
    }

    @Override
    public String toString() {
        return "ParameterBean{" +
                "heartbeat_time=" + heartbeat_time +
                ", waiting_time=" + waiting_time +
                ", duration=" + duration +
                ", pay_type=" + pay_type +
                '}';
    }
}
