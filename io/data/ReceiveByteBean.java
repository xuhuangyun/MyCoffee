package com.fancoff.coffeemaker.io.data;

/**
 * Created by apple on 2017/12/11.
 */

public class ReceiveByteBean {
    private  final static  int OPTION_READ=0x03;
    private  final static  int OPTION_WRITE_SIGLE=0x10;
    private  final static  int OPTION_WRITE_=0;

    byte headByte;
    byte optionByte;
    byte[] body;
    byte [] endByte;

    public ReceiveByteBean(int option, byte[] body) {

        this.body = body;
    }
}
