package com.fancoff.coffeemaker.bean.coffe;

import java.io.Serializable;

/**
 * Created by apple on 2018/2/1.
 */

public class WaitingTimeBean implements Serializable {
    int screensaver;
    int pay;
    int main;

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public int getScreensaver() {
        return screensaver;
    }

    public void setScreensaver(int screensaver) {
        this.screensaver = screensaver;
    }

    public int getMain() {
        return main;
    }

    public void setMain(int main) {
        this.main = main;
    }

    @Override
    public String toString() {
        return "WaitingTimeBean{" +
                "screensaver=" + screensaver +
                ", pay=" + pay +
                ", main=" + main +
                '}';
    }
}
