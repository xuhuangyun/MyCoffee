package com.fancoff.coffeemaker.net.RequstBean;

/**
 * Created by apple on 2017/11/18.
 */

/**
 * int return_code;
 * String return_msg;
 */
public class BaseBean {
    int return_code;
    String return_msg;
    public boolean getIsSuccess(){
        return return_code==1;

    }
    public int getReturn_code() {
        return return_code;
    }

    public void setReturn_code(int return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "return_code=" + return_code +
                ", return_msg='" + return_msg + '\'' +
                '}';
    }
}
