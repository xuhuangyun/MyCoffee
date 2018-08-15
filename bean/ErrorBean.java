package com.fancoff.coffeemaker.bean;

/**
 * Created by apple on 2018/3/30.
 */

/**
 * String code;
 * String msg;
 */
public class ErrorBean {
    String code;
    String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ErrorBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
