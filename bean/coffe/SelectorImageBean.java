package com.fancoff.coffeemaker.bean.coffe;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by apple on 2018/2/1.
 */
/**
 *
 */
public class SelectorImageBean implements Serializable {
    @SerializedName(value = "default")
    ImageBean unselect;
    ImageBean selected;

    public ImageBean getUnselect() {
        return unselect;
    }
                                                   
    public void setUnselect(ImageBean unselect) {
        this.unselect = unselect;
    }

    public ImageBean getSelected() {
        return selected;
    }

    public void setSelected(ImageBean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "SelectorImageBean{" +
                "unselect=" + unselect +
                ", selected=" + selected +
                '}';
    }
}
