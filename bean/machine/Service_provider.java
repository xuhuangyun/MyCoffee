package com.fancoff.coffeemaker.bean.machine;

/**
 * Created by apple on 2018/4/11.
 */

public class Service_provider {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Service_provider{" +
                "name='" + name + '\'' +
                '}';
    }
}
