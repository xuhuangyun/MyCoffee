package com.fancoff.coffeemaker.bean.coffe;

import java.io.Serializable;

/**
 * Created by apple on 2018/2/4.
 */

/**

 */
public class Clickbean implements Serializable {
    String type;
    String url;
    ShowImageBean image;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ShowImageBean getImage() {
        return image;
    }

    public void setImage(ShowImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Clickbean{" +
                "type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", image=" + image +
                '}';
    }
}
