package com.fancoff.coffeemaker.net.RequstBean;

import com.fancoff.coffeemaker.bean.coffe.CoffeeConfigBean;

/**
 * Created by apple on 2018/2/4.
 */

public class CoffeeConfigResultBean extends BaseBean {
    CoffeeConfigBean coffee_config;
    String machine_config;

    public String getMachine_config() {
        return machine_config;
    }

    public void setMachine_config(String machine_config) {
        this.machine_config = machine_config;
    }

    public CoffeeConfigBean getCoffee_config() {
        return coffee_config;
    }

    public void setCoffee_config(CoffeeConfigBean coffee_config) {
        this.coffee_config = coffee_config;
    }
}
