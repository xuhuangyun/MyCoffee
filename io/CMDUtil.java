package com.fancoff.coffeemaker.io;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.Application.TestIoDatas;
import com.fancoff.coffeemaker.bean.CheckHotBean;
import com.fancoff.coffeemaker.bean.VMCState;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.bean.machine.MachineConfiBean;
import com.fancoff.coffeemaker.io.data.SendByteBean;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.rx.RxDataTool;

import java.util.ArrayList;

/**
 * Created by apple on 2018/1/3.
 * vmc指令集工具类
 */

/**
 * 1、IOService ioService;
 * 2、发送制作咖啡、关闭工程菜单、清洗、加热、清除进度条、清除错误、设置工艺表等指令
 */
public class CMDUtil {
    public final static int VMC_TIMEOUT = 10000;//vmc超时时间 毫秒
    IOService ioService;
    long lastReadTime;

   IOService getIoService() {
        return ioService;
    }

    public void setIoService(IOService ioService) {
        this.ioService = ioService;
    }

    public void start() {
        this.ioService.start();
    }

    public void reStart() {
        this.ioService.reStart();
    }

    /**
     * 设置VMCState.isNull = true;
     * MainViewCallBack.showViewByVmc(VMCState.getIns()):返回主界面，MainActivity实现该方法；
     */
    public void onVmcFailed() {
        DataCenter.getInstance().saveVmcBytes(null);
        getMainViewCallBack().showViewByVmc(VMCState.getIns());
    }

    public static final CMDUtil ourInstance = new CMDUtil();

    public static CMDUtil getInstance() {

        return ourInstance;
    }

    private CMDUtil() {
        lastReadTime=System.currentTimeMillis();
    }

    /**
     * 握手命令用的是这个回调接口；
     * 默认的IoCallBack回调接口对象defaultcmdCallBack
     * 定义接口方法：
     *    onFailed(): 回到主界面
     *    onSuccess(): 解析rb数据并存入VMCState的属性中；
     *                 回到主界面
     *                 开启加热功能
     */
    private IoCallBack defultcmdCallBack = new IoCallBack() {
        @Override
        public void onFailed(byte[] send, int error) {
            onVmcFailed();//回到主界面
        }

        @Override
        public void onSuccess(byte[] send, byte[] rb) {
            DataCenter.getInstance().saveVmcBytes(rb);  //成功，将数组rb进行解析（A域64个字节），并打印日志
            getMainViewCallBack().showViewByVmc(VMCState.getIns());//回到主界面
            CheckHotBean.getIns().sendHotCMD();  //加热
        }
    };

    /**
     * 发送数据：字节数据bytes，回调接口IoCallBack cmdCallBack
     *     1、如果cmdCallBack为空，使用defultcmdCallBack的回调接口定义；
     *     2、ioService服务为空，且cmdCallBack不为空，则调用cmdCallBack.onFailed，app服务没开启
     *     3、调用ioService的发送数据命令，向串口输出流传入数据，并进行输入流数据读取
     */
    private void send(byte[] bytes, IoCallBack cmdCallBack) {
        if (cmdCallBack == null) {
            cmdCallBack = getDefultcmdCallBack();
        }

        if (ioService == null) {
            if (cmdCallBack != null) {
                cmdCallBack.onFailed(null, IoCallBack.ERROR_NO_SERVICE);
            }
            return;
        }
        ioService.sendCMDWithResult(bytes, cmdCallBack);
    }

    //制作咖啡

    /**
     * 制作咖啡：数据BodyByte，回调接口cmdCallBack，其他地方调用该方法时候回调接口为null
     */
    public void maikeCoffee(byte[] BodyByte, IoCallBack cmdCallBack) {

        LogUtil.vmc("制作咖啡:", BodyByte);
        send(BodyByte, cmdCallBack);
    }

    //关闭工程模式

    /**
     * 关闭工程菜单
     * 组装命令字节数组，发送数据；其他地方调用该方法回调接口为null
     */
    public void cancleDebug(IoCallBack cmdCallBack) {
        SendByteBean s = new SendByteBean(IOConstans.OPTION.OPTION_CALCLE, null);//命令字节为0x13,字节长度和字节数组为空
        byte[] send = s.getSendData(); //关闭工程菜单的命令字节数组
        LogUtil.vmc("关闭工程模式:", send);
        send(send, cmdCallBack);  //发送数据
        if(MyApp.IOTEST){
            TestIoDatas.getInstance().setOpenSetting(false);
        }
    }

    //清洗
    /*
    D1:咖啡机清洗水量，20~200ml，建议 50ml D2:辅料清洗水量,50~200ml，建议 90ml
     */

    /**
     * 清洗指令
     * 获得咖啡机及3个出水通道的水量；
     * 封装清洗指令的字节数组，并发送清洗指令
     * TaskUtil类的runtimeToPushTask()方法会调用该方法；
     * 其他地方调用该方法，回调接口为null；
     */
    public void clean(IoCallBack cmdCallBack) {
        byte[] bs = new byte[4];
        bs[0] = (byte) DataCenter.getInstance().getCleanMainWater();
        bs[1] = (byte) DataCenter.getInstance().getCleanAssistWater();
        bs[2] = (byte) DataCenter.getInstance().getCleanAssistWater();
        bs[3] = (byte) DataCenter.getInstance().getCleanAssistWater();
        SendByteBean s = new SendByteBean(IOConstans.OPTION.OPTION_CLEAN, bs);
        byte[] send = s.getSendData();
        LogUtil.vmc("清洗:", send);
        send(send, cmdCallBack);
    }

    /**
     * 加热开关打开
     * 返回加热控制命令的字节数组
     */
    public byte[] getHotBytes(boolean hoton) {
        byte[] bs = new byte[1];
        if (hoton) {
            bs[0] = 0x03;
        } else {
            bs[0] = 0x00;
        }
        SendByteBean s = new SendByteBean(IOConstans.OPTION.OPTION_HOT_ON, bs);
        byte[] send = s.getSendData();
        if(MyApp.IOTEST){
            TestIoDatas.getInstance().setOpenHot(hoton);
        }
        LogUtil.vmc("使能加热:" + (hoton ? "开  " : "关  "), send);
        return send;
    }

    //开锁
    public void openDoor(boolean bigdoor, boolean cfdoor, IoCallBack cmdCallBack) {
        byte[] bs = new byte[1];
        if (bigdoor && cfdoor) {
            bs[0] = 0x03;
        } else if (bigdoor) {
            bs[0] = 0x01;
        } else if (cfdoor) {
            bs[0] = 0x02;
        } else {
            bs[0] = 0x00;
        }
        SendByteBean s = new SendByteBean(IOConstans.OPTION.OPTION_OPEN_DOOR, bs);
        byte[] send = s.getSendData();
        LogUtil.vmc("开锁:", send);
        send(send, cmdCallBack);
    }

    //使能加热

    /**
     * 使能加热
     * 通过getHotBytes()方法获得加热指令字节数组，然后发送加热指令
     * 调用该方法的回调接口为null
     */
    public void sethot(boolean hoton, IoCallBack cmdCallBack) {

        send(getHotBytes(hoton), cmdCallBack);
        //getHotBytes(hoton)：加热开关打开的命令数组
    }

    //除进度条
    /**
     * 封装清除进度条指令，并发送清除进度条命令
     */
    public void clearProgresss(IoCallBack cmdCallBack) {
        SendByteBean s = new SendByteBean(IOConstans.OPTION.OPTION_CLEAR_PROGRESS, null);
        byte[] send = s.getSendData();
        LogUtil.vmc("除进度条:", send);
        send(send, cmdCallBack);
    }

    //清除错误
    /**
     * 清除错误
     * 分装清除错误指令字节数组，并发送清除错误指令
     * 调用该方法的回调接口为null
     */
    public void clearError(IoCallBack cmdCallBack) {

        SendByteBean s = new SendByteBean(IOConstans.OPTION.OPTION_CLEAR_ERROR, null);
        byte[] send = s.getSendData();
        LogUtil.vmc("清除错误:", send);
        send(send, cmdCallBack);
    }

    MainViewCallBack mainViewCallBack;

    public MainViewCallBack getMainViewCallBack() {
        return mainViewCallBack;
    }

    public void setMainViewCallBack(MainViewCallBack mainViewCallBack) {
        this.mainViewCallBack = mainViewCallBack;
    }

    public IoCallBack getDefultcmdCallBack() {
        return defultcmdCallBack;
    }

    /**
     * 将咖啡饮品中的 粉水工艺参数都组合到一个字节数组中
     * getByteBody();方法中会只取make属性，过滤到其他属性
     */
    private byte[] getBytes(ArrayList<CoffeeBean> list) {
        byte b[] = new byte[32];
        for (int key = 0; key < list.size(); key++) {//遍历CoffeeBean列表，即饮品列表

            CoffeeBean g = list.get(key);//key键对应的饮品
            if (g == null) {
                g = new CoffeeBean();  //饮品为空，新建一个饮品类
            }

            if (key == 0) {
                b = g.getByteBody();  //第一个饮品的粉水工艺字节数组 32字节

            } else {
                byte[] b2 = g.getByteBody();

                b = RxDataTool.byteMerger(b, b2); //b2追加到b后面
            }

        }
        return b;

    }

    //机器参数
    /**
     * 设置机器工艺；获得设置及其工艺的字节数组，发送数据
     * 调用该方法的回调接口为null
     */
    public void setMachine(MachineConfiBean machineConfiBean, IoCallBack cmdCallBack) {
        SendByteBean s = new SendByteBean(IOConstans.OPTION.OPTION_SET_MACHINE, machineConfiBean.getBytes());
        byte[] send = s.getSendData();
        LogUtil.vmc("设置机器参数:", send);
        send(send, cmdCallBack);
    }

    //设置工艺参数
    /**
     * 设置咖啡工艺，获得设置咖啡工艺的字节数组（粉水字节组合数组），发送数据
     * 获得CoffeeBean所以对象列表，会只取每个对象的MakeBean对象，过滤掉其他不需要的对象；
     */
    public void setCoffe(ArrayList<CoffeeBean> list) {
        byte[] bbs = getBytes(list);  //得到饮品列表中各饮品的粉水字节数组（总和）
        SendByteBean s = new SendByteBean(IOConstans.OPTION.OPTION_SETCOFFE, bbs);
        //0x41 设置工艺参数。 将0x41和bbs的长度数组和自己数组赋值到SendByteBean对象s中
        byte[] bsned = s.getSendData();  //获得发送数据的数组：0XAA+命令字节+长度+数据+校验
        LogUtil.vmc("设置工艺参数:", bsned);
        send(bsned, null);
    }


}
