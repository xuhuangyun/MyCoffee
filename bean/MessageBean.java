package com.fancoff.coffeemaker.bean;

/**
 * Created by apple on 2017/10/11.
 */
/**
 *
 */
public class MessageBean {
    public static final int ACTION_INTENT = 1;
    public static final int ACTION_BACK = 2;
    public static final int ACTION_CONTENT = 3;
    public static final int ACTION_PROGRESS = 4;
    public static final int ACTION_HIDEPROGRESS = 5;

    public MessageBean(int action) {
        this.action = action;
    }
    public MessageBean(int action,String msg) {
        this.msg=msg;
        this.action = action;
    }
    public MessageBean(int action,int what) {
        this.action=action;
        this.what = what;
    }
    public MessageBean(int action,int what,String msg) {
        this.action=action;
        this.what = what;
        this.msg=msg;
    }
    int action;
    Object obj;
    int what;
    String msg;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }


    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
