package com.fancoff.coffeemaker.bean.coffe;

import java.io.Serializable;

/**
 * Created by apple on 2018/2/4.
 */

public class TextsBean implements Serializable {
    TextBean seckill;

    public TextBean getSeckill() {
        return seckill;
    }

    public void setSeckill(TextBean seckill) {
        this.seckill = seckill;
    }

    @Override
    public String toString() {
        return "TextsBean{" +
                "seckill=" + seckill +
                '}';
    }
}
