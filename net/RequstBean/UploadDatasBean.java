package com.fancoff.coffeemaker.net.RequstBean;

import java.util.List;

/**
 * Created by apple on 2017/11/17.
 */

public class UploadDatasBean extends  BaseBean{
    List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
