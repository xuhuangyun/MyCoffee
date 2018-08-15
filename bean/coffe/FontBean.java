package com.fancoff.coffeemaker.bean.coffe;

import java.io.Serializable;

/**
 * Created by apple on 2018/2/1.
 */

/**
 *
 */
public class FontBean implements Serializable {
    String file;
    int size;
    String color;
    String weight;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "FontBean{" +
                "file='" + file + '\'' +
                ", size=" + size +
                ", color='" + color + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }
}
