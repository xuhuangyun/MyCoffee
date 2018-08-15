package com.fancoff.coffeemaker.bean.machine;

/**
 * Created by apple on 2018/4/11.
 */

public class Assist_temper {
    int goal;
    int backlash;

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

    @Override
    public String toString() {
        return "Assist_temper{" +
                "goal=" + goal +
                ", backlash=" + backlash +
                '}';
    }
}
