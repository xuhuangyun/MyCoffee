package com.fancoff.coffeemaker.bean.heatbit;

import java.io.Serializable;

/**
 * Created by apple on 2018/3/18.
 */

public class BasePushBean implements Serializable {
    int push_id;


    public int getPush_id() {
        return push_id;
    }

    public void setPush_id(int push_id) {
        this.push_id = push_id;
    }

    @Override
    public String toString() {
        return "BasePushBean{" +
                "push_id=" + push_id +
                '}';
    }
}
