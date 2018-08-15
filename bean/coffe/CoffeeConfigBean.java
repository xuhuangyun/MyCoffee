package com.fancoff.coffeemaker.bean.coffe;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by apple on 2018/2/1.
 */

/**
 *
 */
public class CoffeeConfigBean implements Serializable {
    ArrayList<CategorieBean> categories;
    ImagesBean image;
    VideosBean video;
    TextsBean text;
    ParameterBean parameter;
    String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<CategorieBean> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategorieBean> categories) {
        this.categories = categories;
    }

    public ImagesBean getImage() {
        return image;
    }

    public void setImage(ImagesBean image) {
        this.image = image;
    }

    public VideosBean getVideo() {
        return video;
    }

    public void setVideo(VideosBean video) {
        this.video = video;
    }

    public TextsBean getText() {
        return text;
    }

    public void setText(TextsBean text) {
        this.text = text;
    }

    public ParameterBean getParameter() {
        return parameter;
    }

    public void setParameter(ParameterBean parameter) {
        this.parameter = parameter;
    }
}
