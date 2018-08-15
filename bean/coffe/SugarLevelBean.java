package com.fancoff.coffeemaker.bean.coffe;

import java.io.Serializable;

/**
 * Created by apple on 2018/2/1.
 */

/**
 *
 */
public class SugarLevelBean implements Serializable {

    int none;
    int low;

    int middle;
    int high;

    public int getNone() {
        return none;
    }

    public void setNone(int none) {
        this.none = none;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getMiddle() {
        return middle;
    }

    public void setMiddle(int middle) {
        this.middle = middle;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    @Override
    public String toString() {
        return "SugarLevelBean{" +
                "none=" + none +
                ", low=" + low +
                ", middle=" + middle +
                ", high=" + high +
                '}';
    }
}
