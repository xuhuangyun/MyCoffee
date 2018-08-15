package com.fancoff.coffeemaker.bean.machine;

import java.util.ArrayList;

/**
 * Created by apple on 2018/4/11.
 */

public class CleanBean {
    int cnt;
    int main_water;
    int assist_water;
    ArrayList<CleanTime> times = new ArrayList<CleanTime>();

    public ArrayList<CleanTime> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<CleanTime> times) {
        this.times = times;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getMain_water() {
        return main_water;
    }

    public void setMain_water(int main_water) {
        this.main_water = main_water;
    }

    public int getAssist_water() {
        return assist_water;
    }

    public void setAssist_water(int assist_water) {
        this.assist_water = assist_water;
    }

    @Override
    public String toString() {
        return "CleanBean{" +
                "cnt=" + cnt +
                ", main_water=" + main_water +
                ", assist_water=" + assist_water +
                ", times=" + times +
                '}';
    }
}
