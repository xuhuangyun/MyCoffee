package com.fancoff.coffeemaker.bean.heatbit;

import com.fancoff.coffeemaker.bean.ErrorBean;
import com.fancoff.coffeemaker.bean.TaskBean;

import java.util.ArrayList;

/**
 * Created by apple on 2018/2/9.
 */

/**
 * ArrayList<TaskBean> push;
 * ArrayList<ErrorBean> error;
 */
public class HeatParams {
    ArrayList<TaskBean> push;
    ArrayList<ErrorBean> error;

    public ArrayList<ErrorBean> getError() {
        return error;
    }

    public void setError(ArrayList<ErrorBean> error) {
        this.error = error;
    }

    public ArrayList<TaskBean> getPush() {
        return push;
    }

    public void setPush(ArrayList<TaskBean> push) {
        this.push = push;
    }

    public void clear() {
        push = null;
        error = null;
    }

    @Override
    public String toString() {
        return "HeatParams{" +
                "push=" + push +
                ", error=" + error +
                '}';
    }
}
