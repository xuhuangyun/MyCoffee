package com.fancoff.coffeemaker.net.RequstBean;

/**
 * Created by apple on 2017/11/17.
 */

public class PayResultBean extends  BaseBean{
    int status;//1成功 2不成功

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PayResultBean{" +
                "status=" + status +
                '}';
    }

    public boolean getIsPaySuccess() {
        return status==1;
    }
}
