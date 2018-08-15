package com.fancoff.coffeemaker.bean.coffe;

import com.fancoff.coffeemaker.bean.heatbit.BasePushBean;

/**
 * Created by apple on 2018/3/18.
 */

public class TechConfigPushBean extends BasePushBean {
    String coffee;
    String machine;

    public String getCoffee() {
        return coffee;
    }

    public void setCoffee(String coffee) {
        this.coffee = coffee;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }
}
