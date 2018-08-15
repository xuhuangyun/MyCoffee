package com.fancoff.coffeemaker.bean.coffe;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by apple on 2018/2/4.
 */

public class ImagesBean implements Serializable {
    ImageBean hot;
    ImageBean sellout;
    ImageBean seckill;
    ImageBean none_coffee;
    ArrayList<ImageBean> top;
    ArrayList<ImageBean> screensaver;   //
    ArrayList<ImageBean> left_bottoms;
    ImageBean right_bottom1;
    ImageBean right_bottom2;
    ImageBean right_bottom3;

    public ArrayList<ImageBean> getTop() {
        return top;
    }

    public void setTop(ArrayList<ImageBean> top) {
        this.top = top;
    }

    public ImageBean getHot() {
        return hot;
    }

    public void setHot(ImageBean hot) {
        this.hot = hot;
    }

    public ImageBean getSellout() {
        return sellout;
    }

    public void setSellout(ImageBean sellout) {
        this.sellout = sellout;
    }

    public ImageBean getSeckill() {
        return seckill;
    }

    public void setSeckill(ImageBean seckill) {
        this.seckill = seckill;
    }

    public ImageBean getNone_coffee() {
        return none_coffee;
    }

    public void setNone_coffee(ImageBean none_coffee) {
        this.none_coffee = none_coffee;
    }

    public ArrayList<ImageBean> getLeft_bottoms() {
        return left_bottoms;
    }

    public void setLeft_bottoms(ArrayList<ImageBean> left_bottoms) {
        this.left_bottoms = left_bottoms;
    }

    public ImageBean getRight_bottom1() {
        return right_bottom1;
    }

    public void setRight_bottom1(ImageBean right_bottom1) {
        this.right_bottom1 = right_bottom1;
    }

    public ImageBean getRight_bottom2() {
        return right_bottom2;
    }

    public void setRight_bottom2(ImageBean right_bottom2) {
        this.right_bottom2 = right_bottom2;
    }

    public ImageBean getRight_bottom3() {
        return right_bottom3;
    }

    public void setRight_bottom3(ImageBean right_bottom3) {
        this.right_bottom3 = right_bottom3;
    }

    public ArrayList<ImageBean> getScreensaver() {
        return screensaver;
    }

    public void setScreensaver(ArrayList<ImageBean> screensaver) {
        this.screensaver = screensaver;
    }

    @Override
    public String toString() {
        return "ImagesBean{" +
                "hot=" + hot +
                ", sellout=" + sellout +
                ", seckill=" + seckill +
                ", none_coffee=" + none_coffee +
                ", top=" + top +
                ", screensaver=" + screensaver +
                ", left_bottoms=" + left_bottoms +
                ", right_bottom1=" + right_bottom1 +
                ", right_bottom2=" + right_bottom2 +
                ", right_bottom3=" + right_bottom3 +
                '}';
    }
}
