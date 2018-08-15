package com.fancoff.coffeemaker.io.data;

import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.Application.TestIoDatas;
import com.fancoff.coffeemaker.io.CMDUtil;
import com.fancoff.coffeemaker.io.IoCallBack;
import com.fancoff.coffeemaker.io.SeriaPortUtil;
import com.fancoff.coffeemaker.service.TaskUtil;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.rx.RxDataTool;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by apple on 2018/1/3.
 */

/**
 * 属性：任务的发送头，发送字节数组、接收字节数组、回调接口对象
 *      readBytes的读写有同步synchronized
 *  读取输入数据流超时方法；
 *  从输入流读取串口收到的数据，并进行校验放入CMDTask对象的属性readBytes中；
 *  从CMDTask对象的属性sendBytes中获取数据，并从输出流从串口输出；
 */
public class CMDTask {
    byte[] HEAD;
    boolean isStartRead;
    byte[] sendBytes;
    byte[] readBytes;
    String key;
    IoCallBack ioCallBack;
    long startReadTime;

    /**
     * 清空CMDTask任务的属性内容
     */
    public void release() {
        HEAD = null;
        sendBytes = null;
        ioCallBack = null;
        readBytes = null;
        isStartRead = false;
//        clearVmcData();
    }

    public boolean isTimeOut() {
        if (startReadTime != 0 && (System.currentTimeMillis() - startReadTime > CMDUtil.VMC_TIMEOUT)) {
            return true;
        }
        return false;
    }

    /**
     * 读取标志 isStartRead
     */
    public boolean isStartRead() {
        return isStartRead;
    }

    /**
     * 设置标志 isStartRead，并记录当前时间给startReadTime
     */
    public void setStartRead(boolean startRead) {
        isStartRead = startRead;
        if (startRead) {
            startReadTime = System.currentTimeMillis();
        }
    }

    /**返回本类的接口对象ioCallBack*/
    public IoCallBack getIoCallBack() {
        return ioCallBack;
    }

    /**将外部接口对象ioCallBack传入赋予本类的接口对象ioCallBack*/
    public void setIoCallBack(IoCallBack ioCallBack) {
        this.ioCallBack = ioCallBack;
    }

    /**获取本类中的属性 sendBytes*/
    public byte[] getSendBytes() {
        return sendBytes;
    }

    /**
     * 传入要发送的字节数组sendBytes到本类的属性sendBytes中
     * 提前发送数组的头到HEAD[]中
     */
    public void setSendBytes(byte[] sendBytes) {
        HEAD = new byte[2];
        HEAD[0] = sendBytes[0];
        HEAD[1] = sendBytes[1];
        this.sendBytes = sendBytes;
    }

    /**获取本类的属性 readBytes*/
    public byte[] getReadBytes() {
        synchronized ("read") {
            return readBytes;
        }

    }

    /**将外部传入的readBytes字节数组传入本来的属性readBytes*/
    public void setReadBytes(byte[] readBytes) {
        synchronized ("read") {
            this.readBytes = readBytes;

        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void clearVmcData() {
        if (!MyApp.IOTEST) {
            if (mInputStream == null) {
                mInputStream = SeriaPortUtil.getInstance().getmInputStream();
            }

            if (mInputStream != null) {
                try {
                    int available = mInputStream.available();// 可读取多少字节内容
                    if (available > 0) {
                        byte[] lenbytearr = RxDataTool.readSizeOne(mInputStream, 2);//长度
                        lenbytearr = null;
                    }

                } catch (IOException e) {
                    LogUtil.error(e.toString());
                }
            }
        }
    }

    //读取vmc超时监测
    /**
     * 发送线程中断在该方法（超时会占用10s）
     * 1、readBytes不为空，没有VMC通讯故障，收到数据，回调ioCallBack的onSuccess(getSendBytes(), getReadBytes());；
     * 2、timeout时间内没有收到vmc返回的数据，代表vmc通讯超时了，回调ioCallBack的onFailed(getReadBytes(), IoCallBack.ERROR_TIME_OUT);
     */
    public boolean checkTimeOut(int timeout) {
        int time = 0;
        while (true) {
            try {
                if (getReadBytes() != null && getReadBytes().length > 0) {//readBytes不为空，read读完正确的一串数据才会设置readBytes数组
                    TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0006, null);//没有VMC通讯故障
                    if (getIoCallBack() != null) {
                        getIoCallBack().onSuccess(getSendBytes(), getReadBytes());
                    }
                    return true;
                }
                if (time >= timeout) {
                    LogUtil.vmc("receive timeout");
                    TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0006, MyConstant.APP_ERROR_CODE.ERROR0006_S);
                    if (getIoCallBack() != null) {
                        getIoCallBack().onFailed(getReadBytes(), IoCallBack.ERROR_TIME_OUT);
                    }
                    return false;
                }
                Thread.sleep(100);//100ms
                time += 100;

            } catch (Exception e) {
                LogUtil.error("checkTimeOut:" + e.toString());
                if (getIoCallBack() != null) {
                    getIoCallBack().onFailed(getReadBytes(), IoCallBack.ERROR_TIME_OUT);
                }
                return false;
            }

        }
    }

    //    //接受数据并校验
//    public boolean read(FileInputStream mInputStream) throws IOException {
//
//        byte reads[] = new byte[256];
//        int size = mInputStream.read(reads);
//        if (size > 4) {
//            if (reads[0] == HEAD[0] && reads[1] == HEAD[1]) {
//                byte[] lenth = new byte[2];
//                lenth[0] = reads[2];
//                lenth[1] = reads[3];
//                int len=Integer.valueOf(RxDataTool.bytes2HexString(lenth),16);
//
//                byte[] bs = new byte[len+4];
//                System.arraycopy(reads, 0, bs, 0, len+4);
//
//                byte b = RxDataTool.getXor(bs);
//
//                if (b == reads[len+4]) {
//                    byte all[] = RxDataTool.byteMerger(bs, new byte[]{b});
//                    setReadBytes(all);
//                    return true;
//                }
//            }
//        }
//
//        return false;
//
//    }
    public interface CMDCallBack {
        void getData(byte[] head, byte[] body, byte[] all);
    }

    //    CMDCallBack cmdCallBack;
//
//    public CMDCallBack getCmdCallBack() {
//        return cmdCallBack;
//    }
//
//    public void setCmdCallBack(CMDCallBack cmdCallBack) {
//        this.cmdCallBack = cmdCallBack;
//    }
    FileInputStream mInputStream = null;


    //接受数据并校验

    /**
     * 从串口输入流读取数据，并进行校验
     *     校验正确，收到的数据传给本类的readBytes
     *     校验错误，增加校验错误到错误列表中，并打印日志
     */
    public boolean read() throws IOException {
        if (SeriaPortUtil.getInstance().isInit()) {//串口初始化，并成功
            if (mInputStream == null) {
                mInputStream = SeriaPortUtil.getInstance().getmInputStream();
            }
            if (mInputStream != null) {
                int available = mInputStream.available();// 可读取多少字节内容
                if (available >= 4) {
                    byte[] readHead = new byte[2];
                    if (RxDataTool.readByte(mInputStream, HEAD[0])) {//输入流第一个字节与HEAD[0]相等(HEAD[0]来自发送时候的字符串)
                        readHead[0] = HEAD[0];
                        byte[] head1 = RxDataTool.readSizeOne(mInputStream, 1);//读取输入流中1个字节
                        readHead[1] = head1[0];
                        byte[] lenbytearr = RxDataTool.readSizeOne(mInputStream, 2);//长度  读取2个字节
                        int len = Integer.valueOf(RxDataTool.bytes2HexString(lenbytearr), 16);//2个长度字节转换为int型
                        //valueOf: HexString用16进制解析为int len
                        if (len == 64) {//长度为64字节
                            byte[] last = RxDataTool.readSizeTimeOut(mInputStream, len + 1, 2000);//循环读取,2s超时，占用读线程
                            if (last != null && last.length == (len + 1)) {//读取了len+1个数据
                                byte[] bodys = RxDataTool.getBytes(last, 0, len);  //需要的数据
                                byte[] xor = RxDataTool.getBytes(last, len, 1);    //异或校验码
                                byte[] first = RxDataTool.byteMerger(RxDataTool.byteMerger(readHead, lenbytearr), bodys);//头+命令字+长度+数据
                                byte b = RxDataTool.getXor(first); //用异或校验数据
                                if (b == xor[0]) {//校验符合
                                    byte all[] = RxDataTool.byteMerger(first, xor);//整串数据
                                    setReadBytes(all);//将all传给本类的readBytes
                                    TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0005, null);//没有校验异常错误
                                    setStartRead(false);  //isStartRead = false
                                    return true;
                                } else {//校验不符合
                                    TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0005, MyConstant.APP_ERROR_CODE.ERROR0005_S);
                                    byte all[] = RxDataTool.byteMerger(first, xor);
                                    LogUtil.vmc("vmc read 校验失败", all);
                                }
                            }
                        }
                    }

                }
                if (MyApp.IOTEST) {
                    if (mInputStream != null) {
                        mInputStream.close();
                        mInputStream = null;
                    }
                }

            }
        }
        return false;

    }

    FileOutputStream mOutputStream = null;

    /**
     * 从本类获得sendBytes数据，然后从串口输出数据
     *  设置开始读取的时间
     */
    public boolean write() {
        if (SeriaPortUtil.getInstance().isInit()) {//有串口输入输出流
            try {
                if (mOutputStream == null) {
                    mOutputStream = SeriaPortUtil.getInstance().getmOutputStream();
                }
                if (mOutputStream != null) {
                    if (MyApp.IOTEST) {
                        TestIoDatas.getInstance().testwrite();
                        mOutputStream.write(TestIoDatas.getInstance().getTestReadBytes(getSendBytes()[1]));
                        mOutputStream.close();
                        mOutputStream = null;
                    } else {
                        mOutputStream.write(getSendBytes());
                        //获得sendBytes，通过输出流输出
                    }
                    setStartRead(true);//设置开始读取的时间
                }

            } catch (Exception e) {
                LogUtil.error("write2:" + e.toString());
                if (getIoCallBack() != null) {
                    getIoCallBack().onFailed(getSendBytes(), IoCallBack.ERROR_NO_SERVICE);
                }
            }
            return true;
        } else {//没有串口输入输出流
            if (getIoCallBack() != null) {
                getIoCallBack().onFailed(getSendBytes(), IoCallBack.ERROR_NO_SERVICE);
            }
            return false;
        }
    }
}
