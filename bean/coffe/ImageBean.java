package com.fancoff.coffeemaker.bean.coffe;

import java.io.Serializable;

/**
 * Created by apple on 2018/2/1.
 */

/**

 */
public class ImageBean implements Serializable {
    String url;
    PositionBean position;

    public ImageBean(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    Clickbean click;

    public void setUrl(String url) {
        this.url = url;
    }

    public PositionBean getPosition() {
        return position;
    }

    public void setPosition(PositionBean position) {
        this.position = position;
    }

    public Clickbean getClick() {
        return click;
    }

    public void setClick(Clickbean click) {
        this.click = click;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "url='" + url + '\'' +
                ", position=" + position +
                ", click=" + click +
                '}';
    }
}
