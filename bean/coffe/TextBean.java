package com.fancoff.coffeemaker.bean.coffe;

import java.io.Serializable;

/**
 * Created by apple on 2018/2/1.
 */
/**

 */
public class TextBean implements Serializable {
    String value;
    FontBean font;
    FontBean font0;
    FontBean font1;
    FontBean fractional_font;
    PositionBean fractional_position;
    PositionBean position;

    public FontBean getFractional_font() {
        return fractional_font;
    }

    public void setFractional_font(FontBean fractional_font) {
        this.fractional_font = fractional_font;
    }

    public PositionBean getFractional_position() {
        return fractional_position;
    }

    public void setFractional_position(PositionBean fractional_position) {
        this.fractional_position = fractional_position;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FontBean getFont() {
        return font;
    }

    public void setFont(FontBean font) {
        this.font = font;
    }

    public PositionBean getPosition() {
        return position;
    }

    public void setPosition(PositionBean position) {
        this.position = position;
    }

    public FontBean getFont0() {
        return font0;
    }

    public void setFont0(FontBean font0) {
        this.font0 = font0;
    }

    public FontBean getFont1() {
        return font1;
    }

    public void setFont1(FontBean font1) {
        this.font1 = font1;
    }

    @Override
    public String toString() {
        return "TextBean{" +
                "value='" + value + '\'' +
                '}';
    }
}
