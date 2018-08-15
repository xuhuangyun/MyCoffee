package com.fancoff.coffeemaker.bean.heatbit;

/**
 * Created by apple on 2018/3/18.
 */

public class ImagesPushBean  {

    ObjPushBean left_bottoms;
    ObjPushBean top;
    ObjPushBean screensaver;

    public ObjPushBean getLeft_bottoms() {
        return left_bottoms;
    }

    public void setLeft_bottoms(ObjPushBean left_bottoms) {
        this.left_bottoms = left_bottoms;
    }

    public ObjPushBean getTop() {
        return top;
    }

    public void setTop(ObjPushBean top) {
        this.top = top;
    }

    public ObjPushBean getScreensaver() {
        return screensaver;
    }

    public void setScreensaver(ObjPushBean screensaver) {
        this.screensaver = screensaver;
    }

    @Override
    public String toString() {
        return "ImagesPushBean{" +
                "top=" + top +
                ", screensaver=" + screensaver +
                '}';
    }
}
