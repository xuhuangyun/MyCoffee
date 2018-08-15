package com.fancoff.coffeemaker.net.RequstBean;

/**
 * Created by apple on 2017/11/17.
 */

public class ParamBean extends  BaseBean{
    String coffee_config;
    String machine_config;

    public String getCoffee_config() {
        return coffee_config;
    }

    public void setCoffee_config(String coffee_config) {
        this.coffee_config = coffee_config;
    }

    public String getMachine_config() {
        return machine_config;
    }

    public void setMachine_config(String machine_config) {
        this.machine_config = machine_config;
    }

    @Override
    public String toString() {
        return "ParamBean{" +
                "coffee_config='" + coffee_config + '\'' +
                ", machine_config='" + machine_config + '\'' +
                '}';
    }
}
