package com.fancoff.coffeemaker.net.RequstBean;

import java.util.ArrayList;

/**
 * Created by apple on 2017/11/17.
 */

/**

 */
public class PayQrCodeBean extends BaseBean {


    String qrcode;
    ArrayList<Order> goods;

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public ArrayList<Order> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<Order> goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "PayQrCodeBean{" +
                "qrcode='" + qrcode + '\'' +
                ", goods=" + goods +
                '}';
    }
}
