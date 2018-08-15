package com.fancoff.coffeemaker.bean.machine;

/**
 * Created by apple on 2018/4/11.
 */

public class Main_temper {
    int goal;
    int backlash;
    int min;

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getBacklash() {
        return backlash;
    }

    public void setBacklash(int backlash) {
        this.backlash = backlash;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    @Override
    public String toString() {
        return "Main_temper{" +
                "goal=" + goal +
                ", backlash=" + backlash +
                ", min=" + min +
                '}';
    }
}
