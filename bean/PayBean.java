package com.fancoff.coffeemaker.bean;


import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.net.RequstBean.Order;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by apple on 2017/9/10.
 */
/**
 * String out_trade_no;
 * int payType;
 * String  weipayCode;
 * String  aipayCode;
 * String  hePayCode;
 * ArrayList<Order> orders;
 */
public class PayBean  implements Serializable {

    String out_trade_no;
    int payType;
    String  weipayCode;
    String  aipayCode;
    String  hePayCode;
    ArrayList<Order> orders;

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {

        this.orders = orders;
    }

    public String getPayCode() {
        if(payType== MyConstant.PAY.PAY_TYPE_WEIXIN){
            return weipayCode;
        }else if(payType== MyConstant.PAY.PAY_TYPE_AIPAY){
            return aipayCode;
        }else if(payType== MyConstant.PAY.PAY_TYPE_HE){
            return hePayCode;
        }
        return "";
    }
    public String getPayCode(int payType) {
        if(payType== MyConstant.PAY.PAY_TYPE_WEIXIN){
            return weipayCode;
        }else if(payType== MyConstant.PAY.PAY_TYPE_AIPAY){
            return aipayCode;
        }else if(payType== MyConstant.PAY.PAY_TYPE_HE){
            return hePayCode;
        }
        return "";
    }
    public void setPayCode(String payCode,int payType) {
        if(payType== MyConstant.PAY.PAY_TYPE_WEIXIN){
            this.weipayCode = payCode;
        }else if(payType== MyConstant.PAY.PAY_TYPE_AIPAY){
            this.aipayCode = payCode;
        }else if(payType== MyConstant.PAY.PAY_TYPE_HE){
            this.hePayCode = payCode;
        }
    }

    ArrayList<CoffeeBean> goods=new ArrayList<>();

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public ArrayList<CoffeeBean> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<CoffeeBean> goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "PayBean{" +
                "out_trade_no='" + out_trade_no + '\'' +
                ", payType=" + payType +
                ", weipayCode='" + weipayCode + '\'' +
                ", aipayCode='" + aipayCode + '\'' +
                ", hePayCode='" + hePayCode + '\'' +
                ", orders=" + orders +
                ", goods=" + goods +
                '}';
    }
}
