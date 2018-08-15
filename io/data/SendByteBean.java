package com.fancoff.coffeemaker.io.data;

import com.fancoff.coffeemaker.utils.rx.RxDataTool;

/**
 * Created by apple on 2017/12/11.
 */

/**
 * 属性：发送头headByte=0xaa、命令字节optionByte、发送内容body数组；
 */
public class SendByteBean {
    private byte headByte = (byte) 0xaa;
    private byte optionByte;
    private byte[] body;

    /**

     */
    public SendByteBean(byte optionByte, int body) {
        init(optionByte, RxDataTool.intToBytesArc(body));

    }

    /**
     * this.optionByte = optionByte;
     * this.body（数据长度+数据内容）
     */
    public SendByteBean(byte optionByte, byte[] body) {
        init(optionByte, body);
    }

    /**
     * this.optionByte = optionByte;
     * this.body（数据长度+数据内容）
     */
    public void init(byte optionByte, byte[] body) {
        this.optionByte = optionByte;
        if(body==null){
            body=new byte[0];
        }
        byte[] num = RxDataTool.intToBytesArc(body.length);//字节长度：2个字节
        this.body = byteMerger(num, body);

    }

    /**数据长度和数据内容组合成一个数组 */
    private byte[] byteMerger(byte[] bt1, byte[] bt2) {
        if(bt2==null){
            return bt1;
        }
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    /**
     * 起始头+命令字节+数据长度+数据内容+校验
     */
    public byte[] getSendData() {
        byte[] sendHeadBody = byteMerger(new byte[]{headByte, optionByte}, body);
        byte crc = RxDataTool.getXor(sendHeadBody);
        byte[] bytes = new byte[1];
        bytes[0] = crc;
        return byteMerger(sendHeadBody, bytes);

    }

}
