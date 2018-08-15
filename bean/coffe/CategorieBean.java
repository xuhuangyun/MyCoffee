package com.fancoff.coffeemaker.bean.coffe;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by apple on 2018/2/1.
 */


public class CategorieBean implements Serializable {
    int id;
    SelectorImageBean image;

    ArrayList<CoffeeBean> coffees;
    @Expose(serialize = false, deserialize = false)
    int size;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SelectorImageBean getImage() {
        return image;
    }

    public void setImage(SelectorImageBean image) {
        this.image = image;
    }

    public ArrayList<CoffeeBean> getCoffees() {
        return coffees;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setCoffees(ArrayList<CoffeeBean> coffees) {
        this.coffees = coffees;
    }

    @Override
    public String toString() {
        return "CategorieBean{" +
                "id=" + id +
                ", image=" + image +
                ", coffees=" + coffees +
                '}';
    }

    public boolean isEmpty() {
        return size<=0;
    }
}
