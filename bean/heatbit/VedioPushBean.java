package com.fancoff.coffeemaker.bean.heatbit;

/**
 * Created by apple on 2018/3/18.
 */

public class VedioPushBean {
    ObjPushBean making;
    ObjPushBean top;

    public ObjPushBean getMaking() {
        return making;
    }

    public void setMaking(ObjPushBean making) {
        this.making = making;
    }

    public ObjPushBean getTop() {
        return top;
    }

    public void setTop(ObjPushBean top) {
        this.top = top;
    }

    @Override
    public String toString() {
        return "VedioPushBean{" +
                "making=" + making +
                ", top=" + top +
                '}';
    }
}
