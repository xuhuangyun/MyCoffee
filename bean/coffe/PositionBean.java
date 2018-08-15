package com.fancoff.coffeemaker.bean.coffe;

import java.io.Serializable;

/**
 * Created by apple on 2018/2/1.
 */

/**
 *
 */
public class PositionBean implements Serializable {
    int top;
    int left;

    public PositionBean(int top, int left) {
        this.top = top;
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    @Override
    public String toString() {
        return "PositionBean{" +
                "top=" + top +
                ", left=" + left +
                '}';
    }
}
