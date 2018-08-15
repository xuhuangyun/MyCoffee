package com.fancoff.coffeemaker.bean;

public class MsgEvent {


    private String mMsg;
    private int type;


    public MsgEvent( int type) {
        this.type = type;
    }
    public MsgEvent( int type,String mMsg) {
        this.type = type;
        this.mMsg = mMsg;
    }

    public String getmMsg() {
        return mMsg;
    }

    public void setmMsg(String mMsg) {
        this.mMsg = mMsg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}