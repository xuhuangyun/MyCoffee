package com.fancoff.coffeemaker.bean.coffe;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by apple on 2018/2/4.
 */

public class VideosBean implements Serializable {
    ImageBean completed;
    ArrayList<ImageBean> top;
    ArrayList<ImageBean> making;
    ImageBean fall_cup_error;
    ImageBean dirty_cup;
    ImageBean make_error;

    public ImageBean getMake_error() {
        return make_error;
    }

    public void setMake_error(ImageBean make_error) {
        this.make_error = make_error;
    }

    public ImageBean getDirty_cup() {
        return dirty_cup;
    }

    public void setDirty_cup(ImageBean dirty_cup) {
        this.dirty_cup = dirty_cup;
    }

    public ImageBean getFall_cup_error() {
        return fall_cup_error;
    }

    public void setFall_cup_error(ImageBean fall_cup_error) {
        this.fall_cup_error = fall_cup_error;
    }

    public ImageBean getCompleted() {
        return completed;
    }

    public void setCompleted(ImageBean completed) {
        this.completed = completed;
    }

    public ArrayList<ImageBean> getTop() {
        return top;
    }

    public void setTop(ArrayList<ImageBean> top) {
        this.top = top;
    }

    public ArrayList<ImageBean> getMaking() {
        return making;
    }

    public void setMaking(ArrayList<ImageBean> making) {
        this.making = making;
    }

    @Override
    public String toString() {
        return "VideosBean{" +
                "completed=" + completed +
                ", top=" + top +
                ", making=" + making +
                ", fall_cup_error=" + fall_cup_error +
                ", dirty_cup=" + dirty_cup +
                ", make_error=" + make_error +
                '}';
    }
}
