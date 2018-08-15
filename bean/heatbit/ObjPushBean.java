package com.fancoff.coffeemaker.bean.heatbit;

import com.fancoff.coffeemaker.bean.coffe.ImageBean;

import java.util.ArrayList;

/**
 * Created by apple on 2018/3/21.
 */

public class ObjPushBean extends BasePushBean {
    ArrayList<ImageBean> details;

    public void setDetails(ArrayList<ImageBean> details) {
        this.details = details;
    }

    public ArrayList<ImageBean> getDetails() {
//        if (details == null) {
//            details = new ArrayList<>();
//        }
        return details;
    }

    @Override
    public String toString() {
        return "ObjPushBean{" +
                "details=" + details +
                '}';
    }
}
