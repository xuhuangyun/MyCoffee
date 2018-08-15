package com.fancoff.coffeemaker.io.data;

/**
 * Created by apple on 2017/12/11.
 */

public class SendByteBuilder {
    static SendByteBuilder sendByteBuilder;
    public static  SendByteBuilder build(){
        if(sendByteBuilder==null){
            sendByteBuilder=new SendByteBuilder();
        }
        return sendByteBuilder;
    }
    public byte[] creatByteArray(byte optionByte, int value){
        SendByteBean sendByteBean=new SendByteBean(
                optionByte,
                value);
        return sendByteBean.getSendData();
    }

    /**

     */
    public byte[] creatByteArray(byte optionByte, byte[] value){

        SendByteBean sendByteBean=new SendByteBean(
                optionByte,
                value);
        return sendByteBean.getSendData();
    }

}
