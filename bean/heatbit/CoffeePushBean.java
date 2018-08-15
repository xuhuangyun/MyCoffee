package com.fancoff.coffeemaker.bean.heatbit;

import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;

import java.util.ArrayList;

/**
 * Created by apple on 2018/3/18.
 */
/**ArrayList<CoffeeBean> coffees;*/
public class CoffeePushBean extends BasePushBean {
    ArrayList<CoffeeBean> coffees;

    public ArrayList<CoffeeBean> getCoffees() {
        return coffees;
    }

    public void setCoffees(ArrayList<CoffeeBean> coffees) {
        this.coffees = coffees;
    }
}

