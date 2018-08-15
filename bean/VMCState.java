package com.fancoff.coffeemaker.bean;

import com.fancoff.coffeemaker.Application.DeviceInfo;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.io.CMDUtil;
import com.fancoff.coffeemaker.service.TaskUtil;
import com.fancoff.coffeemaker.ui.making.MakingPageMode;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.StringUtil;
import com.fancoff.coffeemaker.utils.rx.RxDataTool;

import java.util.HashMap;

/**
 * 应答包解析对象
 */
public class VMCState {
    /******************************************************咖啡机故障****************************************************************************/
    private String[] cf_error_values = new String[]{"DOSER故障", "咖啡机加热器故障", "咖啡机流量计故障", "压粉电机超时故障", "压粉机构安装故障", "缺豆"};//故障等级
    private String[] cf_error_keys = new String[]{MyConstant.ERROR_CODE.COFFEE_ERROR_BIT0, MyConstant.ERROR_CODE.COFFEE_ERROR_BIT1, MyConstant.ERROR_CODE.COFFEE_ERROR_BIT2, MyConstant.ERROR_CODE.COFFEE_ERROR_BIT3, MyConstant.ERROR_CODE.COFFEE_ERROR_BIT4, MyConstant.ERROR_CODE.COFFEE_ERROR_BIT5};
    /******************************************************app状态****************************************************************************/
    private String[] vmc_state_values = new String[]
            {MyConstant.ERROR_CODE.VMC_STETE_CLEANING};
    private String[] vmc_state_contents = new String[]
            {"清洗中"};
    /******************************************************vmc故障****************************************************************************/

    private String[] vmc_error_values = new String[]
            {"咖啡机通信超时", "VMC 加热故障", "缺杯", "落杯器故障", "导轨运动故障"
                    , "废水箱满", "大门未关", "取杯门未关", "存储器错误"
                    , "缺水", "保留", "保留", "保留", "落杯器卡杯", "主状态机超时", "有脏杯"};
    private String[] vmc_error_keys = new String[]
            {MyConstant.ERROR_CODE.VMC_ERROR_BIT0, MyConstant.ERROR_CODE.VMC_ERROR_BIT1, MyConstant.ERROR_CODE.VMC_ERROR_BIT2, MyConstant.ERROR_CODE.VMC_ERROR_BIT3, MyConstant.ERROR_CODE.VMC_ERROR_BIT4
                    , MyConstant.ERROR_CODE.VMC_ERROR_BIT5, MyConstant.ERROR_CODE.VMC_ERROR_BIT6, MyConstant.ERROR_CODE.VMC_ERROR_BIT7, MyConstant.ERROR_CODE.VMC_ERROR_BIT8, MyConstant.ERROR_CODE.VMC_ERROR_BIT9
                    , MyConstant.ERROR_CODE.VMC_ERROR_BIT10, MyConstant.ERROR_CODE.VMC_ERROR_BIT11, MyConstant.ERROR_CODE.VMC_ERROR_BIT12, MyConstant.ERROR_CODE.VMC_ERROR_BIT13, MyConstant.ERROR_CODE.VMC_ERROR_BIT14, MyConstant.ERROR_CODE.VMC_ERROR_BIT15};


    /**根据故障类型，判断是否执行清洗任务（废水箱满、缺水、正在制作中返回false）*/
    public boolean canClean() {
        if (isVmcErrorBit5()//废水箱满
                || isVmcErrorBit9()  //缺水
                || MakingPageMode.getIns().isMakeing()) {//正在制作中
            return false;
        }
        return true;
    }

    //DOSER 故障 故障等级1
    public boolean isCfErrorBit0() {
        return cf_error_map.get(MyConstant.ERROR_CODE.COFFEE_ERROR_BIT0) != null;
    }

    //咖啡机加热器故障 故障等级2
    public boolean isCfErrorBit1() {
        return cf_error_map.get(MyConstant.ERROR_CODE.COFFEE_ERROR_BIT1) != null;
    }

    //咖啡机流量计故障 故障等级3
    public boolean isCfErrorBit2() {
        return cf_error_map.get(MyConstant.ERROR_CODE.COFFEE_ERROR_BIT2) != null;
    }

    //压粉电机超时故障 故障等级2
    public boolean isCfErrorBit3() {
        return cf_error_map.get(MyConstant.ERROR_CODE.COFFEE_ERROR_BIT3) != null;
    }

    //压粉机构安装故障 故障等级3
    public boolean isCfErrorBit4() {
        return cf_error_map.get(MyConstant.ERROR_CODE.COFFEE_ERROR_BIT4) != null;
    }

    //缺豆指示 故障等级1
    public boolean isCfErrorBit5() {
        return cf_error_map.get(MyConstant.ERROR_CODE.COFFEE_ERROR_BIT5) != null;
    }

    //咖啡机通信超时 故障等级3
    public boolean isVmcErrorBit0() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT0) != null;
    }

    //VMC 加热故障 故障等级3
    public boolean isVmcErrorBit1() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT1) != null;
    }

    //缺杯 故障等级2
    public boolean isVmcErrorBit2() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT2) != null;
    }

    //落杯器故障 故障等级3
    public boolean isVmcErrorBit3() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT3) != null;
    }

    //导轨运动故障 故障等级3
    public boolean isVmcErrorBit4() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT4) != null;
    }

    //废水箱满 故障等级1
    public boolean isVmcErrorBit5() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT5) != null;
    }

    //大门未关指示 故障等级2
    public boolean isVmcErrorBit6() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT6) != null;
    }

    //取杯门未关指示 故障等级1
    public boolean isVmcErrorBit7() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT7) != null;
    }

    //存储器错误 故障等级3
    public boolean isVmcErrorBit8() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT8) != null;
    }

    //缺水 故障等级2
    public boolean isVmcErrorBit9() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT9) != null;
    }

    //保留
    public boolean isVmcErrorBit10() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT10) != null;
    }

    //保留
    public boolean isVmcErrorBit11() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT11) != null;
    }

    //保留
    public boolean isVmcErrorBit12() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT12) != null;
    }

    //卡杯
    public boolean isVmcErrorBit13() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT13) != null;
    }

    //主状态机超时 故障等级1
    public boolean isVmcErrorBit15() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT14) != null;
    }

    //有脏杯或手动放杯时长时间未检测到杯子 故障等级1
    public boolean isVmcErrorBit16() {
        return vmc_error_map.get(MyConstant.ERROR_CODE.VMC_ERROR_BIT15) != null;
    }

    //卡杯
    public boolean stuckCup() {
        return isVmcErrorBit13();
    }
    //落杯器故障
    public boolean errorCup() {
        return isVmcErrorBit3();
    }

    /**************************************************************************************************************/
    boolean isNull = true;//该数据是否为空

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    boolean isRealData;//数据校验是否正确
    static com.fancoff.coffeemaker.bean.VMCState vmcStateBean;

    public static com.fancoff.coffeemaker.bean.VMCState getIns() {
        if (vmcStateBean == null) {
            vmcStateBean = new VMCState();
        }
        return vmcStateBean;
    }

    public int getCoffeeState() {
        return coffeeState;
    }

    public void setCoffeeState(int coffeeState) {
        this.coffeeState = coffeeState;
    }

    public String getCoffeeState_s() {
        return coffeeState_s;
    }

    public void setCoffeeState_s(String coffeeState_s) {
        this.coffeeState_s = coffeeState_s;
    }

    public int getPowder() {
        return powder;
    }

    public void setPowder(int powder) {
        this.powder = powder;
    }

    public String getPowder_s() {
        return powder_s;
    }

    public void setPowder_s(String powder_s) {
        this.powder_s = powder_s;
    }

    public int getPowder2() {
        return powder2;
    }

    public void setPowder2(int powder2) {
        this.powder2 = powder2;
    }

    public String getPowder2_s() {
        return powder2_s;
    }

    public void setPowder2_s(String powder2_s) {
        this.powder2_s = powder2_s;
    }

    public boolean isSureData(byte[] bytes) {
//        LogUtil.vmc(bytes);
//        if (
////                bytes.length == 69&&
//                        bytes[0] == 0xAA
////                        bytes[1] == 0x01&&
////                        bytes[2] == 0x00&&
////                        bytes[3] == 0x40
//                ) {
//            int size = bytes.length - 1;
//            byte[] bs = new byte[size];
//            LogUtil.test("isSureData1");
//            System.arraycopy(bytes, 0, bs, 0, size);
//            byte crc = RxDataTool.getXor(bs);
//            LogUtil.test("isSureData2");
//            if (crc == bytes[size]) {
//                LogUtil.test("isSureData3");
//                return true;
//            }
//        } else {
//            TaskUtil.getInstance().addUploadErrorTask(MyConstant.ERROR_CODE.ERROR0005, MyConstant.ERROR_CODE.ERROR0005_S);//校验异常
//        }
        return true;
    }

    /**咖啡机故障状态*/
    public boolean isCFError() {
        return isCFError;
    }

    public void setCFError(boolean CFError) {
        isCFError = CFError;
    }

    /**VMC故障状态*/
    public boolean isVMCError() {
        return isVMCError;
    }

    //制作失败
    public boolean isVMCErrorStopSell() {
        return isVMCError;
    }

    public void setVMCError(boolean VMCError) {
        isVMCError = VMCError;
    }

    //BUSY
    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }


    public int getVMCState() {
        return VMCState;
    }

    public void setVMCState(int VMCState) {
        this.VMCState = VMCState;
    }

    public String getVMCState_s() {
        return VMCState_s;
    }

    public void setVMCState_s(String VMCState_s) {
        this.VMCState_s = VMCState_s;
    }

    public String[] getArrD6() {
        return arrD6;
    }

    public void setArrD6(String[] arrD6) {
        this.arrD6 = arrD6;
    }

    public String getCf_error_code() {
        return cf_error_code;
    }

    public void setCf_error_code(String cf_error_code) {
        this.cf_error_code = cf_error_code;
    }

    /**咖啡机故障内容*/
    public String getCf_error_content() {
        return cf_error_content;
    }

    public void setCf_error_content(String cf_error_content) {
        this.cf_error_content = cf_error_content;
    }

    public boolean isOnErroring() {
        return isCFError() || isVMCError();
    }


    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    /**咖啡机版本*/
    public String getCoffeeVersion() {
        return coffeeVersion;
    }

    public void setCoffeeVersion(String coffeeVersion) {
        this.coffeeVersion = coffeeVersion;
    }

    /**VMC版本*/
    public String getVmcVersion() {
        return vmcVersion;
    }

    public void setVmcVersion(String vmcVersion) {
        this.vmcVersion = vmcVersion;
    }

    /**辅助锅炉温度*/
    public int getBoilerTemp() {
        return boilerTemp;
    }

    public void setBoilerTemp(int boilerTemp) {
        this.boilerTemp = boilerTemp;
    }

    /**咖啡机温度*/
    public int getCoffeeTemp() {
        return coffeeTemp;
    }

    public void setCoffeeTemp(int coffeeTemp) {
        this.coffeeTemp = coffeeTemp;
    }

    public String[] getCf_error_values() {
        return cf_error_values;
    }

    public void setCf_error_values(String[] cf_error_values) {
        this.cf_error_values = cf_error_values;
    }

    public String[] getCf_error_keys() {
        return cf_error_keys;
    }

    public void setCf_error_keys(String[] cf_error_keys) {
        this.cf_error_keys = cf_error_keys;
    }

    public String[] getVmc_error_values() {
        return vmc_error_values;
    }

    public void setVmc_error_values(String[] vmc_error_values) {
        this.vmc_error_values = vmc_error_values;
    }

    public String[] getVmc_error_keys() {
        return vmc_error_keys;
    }

    public void setVmc_error_keys(String[] vmc_error_keys) {
        this.vmc_error_keys = vmc_error_keys;
    }

    public HashMap<String, String> getCf_error_map() {
        return cf_error_map;
    }

    public void setCf_error_map(HashMap<String, String> cf_error_map) {
        this.cf_error_map = cf_error_map;
    }

    public String getVmc_error_code() {
        return vmc_error_code;
    }

    public void setVmc_error_code(String vmc_error_code) {
        this.vmc_error_code = vmc_error_code;
    }

    /** VMC错误状态集合 */
    public String getVmc_error_content() {
        return vmc_error_content;
    }

    public void setVmc_error_content(String vmc_error_content) {
        this.vmc_error_content = vmc_error_content;
    }

    public HashMap<String, String> getVmc_error_map() {
        return vmc_error_map;
    }

    public void setVmc_error_map(HashMap<String, String> vmc_error_map) {
        this.vmc_error_map = vmc_error_map;
    }

    /*
             *D1
             *D1B7:BUSY 标志 D1B6-D1B0:进度条(0~100)
             *BUSY:当制作咖啡、清洗、落杯等进程时，表示系统 繁忙，只接收握手 0x10 命令
             *进度条:表示清洗或制作咖啡时的进度(APP 用于判 断系统是否执行完毕以及用于 UI  示进度)。
             */
    boolean isBusy;
    int makingProgress;

    /**
     * 解析A域中D1字节，Busy字节，isBusy为b的bit7，makingProgress为bit6-bit0
     */
    public byte parseBusy(byte b) {
        isBusy = RxDataTool.isTrue(b, 7);
        byte b2 = (byte) (b & ~(1 << 7));
        makingProgress = Integer.valueOf(RxDataTool.numToHex8(b2), 16);   //比如“64”,转换为100
        return b;
    }

    /**
     * showDebugMenu显示工作模式
     * D2B0~B1: 工作模式 D2B2~B7:保留
     * VMC 工作模式，该位为非零时，表示为 DEBUG 工程模 式，
     * APP 应弹出工程菜单，工程菜单的取消可以在 UI 上点击也可以由 VMC 通知取消。按下工程按钮，
     * 可以 循环改变该值，取值范围:0~3，由 APP 自行定义。
     */
    boolean showDeBugMenu;

    public byte parseWorkMenu(byte b) {
        showDeBugMenu = RxDataTool.isTrue(b, 0) || RxDataTool.isTrue(b, 1);
        return b;
    }

    /*
        D3
    B0:磨豆;B1:doser; B3:压粉;B4:浸润; B5:冲泡;B6:卸渣 B2/B7:保留
    咖啡机工作状态 咖啡机子状态工作进展，当前 APP 可以忽略
         */
    /**
     * 磨豆机状态集合coffeeState_s，coffeeState从arrD3中获取对应的string赋予coffeeState_s
     */
    int coffeeState;
    String coffeeState_s;
    String arrD3[] = new String[]{"磨豆", "doser", "", "压粉", "浸润", "冲泡", "卸渣", ""};

    private byte parseD3(byte b) {
        coffeeState = b;
        coffeeState_s = byteToResultString(b, arrD3);
        return b;
    }

    /*
    D4
B0:CH1R 落粉;B1:CH1L 落粉 B2:CH2R 落粉;B3:CH2L 落粉 B4:CH3R 落粉;B5:CH3L 落粉 B6:CH4R 落粉;B7:CH4L 落粉
VMC 辅控部分的子状态工作进展，当前 APP 可以忽略
     */
    int powder;
    String powder_s;

    String arrD4[] = new String[]{"CH1R", "CH1L", "CH2R", "CH2L", "CH3R", "CH3L", "CH4R", "CH4L"};

    private byte parseD4(byte b) {
        powder = b;
        powder_s = byteToResultString(b, arrD4);
        return b;
    }

    /*
    D5

B0:CH1 搅拌;B1:CH1 水阀 B2:CH2 搅拌;B3:CH2 水阀 B4:CH3 搅拌;B5:CH3 水阀 B6:CH4 搅拌;B7:CH4 水阀
VMC 辅控部分的子状态工作进展，当前 APP 可以忽略
     */
    int powder2;
    String powder2_s;
    String arrD5[] = new String[]{"CH1 搅拌", "CH1 水阀", "CH2 搅拌", "H2 水阀", "CH3 搅拌", "CH3 水阀", "CH4 搅拌", "CH4 水阀"};

    private byte parseD5(byte b) {
        powder2 = b;
        powder2_s = byteToResultString(b, arrD5);
        return b;
    }

    /*
    D6
B0:辅助锅炉加热状态 B1:咖啡机锅炉加热状态 B2:有脏杯， 醒用户移除 B3:咖啡制作完成， 醒用户取杯 B4: 示手动放杯 B5:当前咖啡机处于清洗流程 B6:落杯准备中
B4~B5:保留
VMC 状态指示
     */
    boolean isHotMainOn;
    boolean isHotassistOn;
    int VMCState;
    String VMCState_s;
    String arrD6[] = new String[]{"辅助锅炉加热状态", "咖啡机锅炉加热状态", "有脏杯", "咖啡制作完成", "提示手动放杯", "当前咖啡机处于清洗流程", "落杯准备中", ""};
    boolean hasdirty;
    boolean ismakingSucc;
    boolean isCleanning;
    boolean handCup;
    boolean downCup;//

    public boolean isDownCup() {
        return downCup;
    }

    public void setDownCup(boolean downCup) {
        this.downCup = downCup;
    }

    public boolean isCleanning() {
        return isCleanning;
    }

    public void setCleanning(boolean cleanning) {
        isCleanning = cleanning;
    }

    public boolean isHandCup() {
        return handCup;
    }

    public void setHandCup(boolean handCup) {
        this.handCup = handCup;
    }
    public boolean isHotOn() {
        return isHotMainOn&&isHotassistOn;
    }

    public boolean isHotMainOn() {
        return isHotMainOn;
    }

    public void setHotMainOn(boolean hotMainOn) {
        isHotMainOn = hotMainOn;
    }

    public boolean isHotassistOn() {
        return isHotassistOn;
    }

    public void setHotassistOn(boolean hotassistOn) {
        isHotassistOn = hotassistOn;
    }

    public boolean isHasdirty() {
        return hasdirty;
    }

    public void setHasdirty(boolean hasdirty) {
        this.hasdirty = hasdirty;
    }

    /**
     * 获取VMC状态指示的各种状态：
     * 辅助锅炉加热状态、咖啡机锅炉加热状态、脏杯状态、制作完成状态、手动放杯状态、清洗状态、落杯状态
     */
    private byte parseVmcState(byte b) {
        VMCState = b;
        VMCState_s = byteToResultString(b, arrD6);
        byte[] bits = RxDataTool.getBooleanArray(b);

        if (RxDataTool.isTrue(bits, 0)) {
            isHotassistOn = true;
        } else {
            isHotassistOn = false;
        }
        if (RxDataTool.isTrue(bits, 1)) {
            isHotMainOn = true;
        } else {
            isHotMainOn = false;
        }

        if (RxDataTool.isTrue(bits, 2)) {
            hasdirty = true;


        } else {
            hasdirty = false;
        }
        if (RxDataTool.isTrue(bits, 3)) {
            ismakingSucc = true;
        } else {
            ismakingSucc = false;
        }
        if (RxDataTool.isTrue(bits, 4)) {
            handCup = true;
        } else {
            handCup = false;
        }
        if (RxDataTool.isTrue(bits, 5)) {
            isCleanning = true;
            TaskUtil.getInstance().addUploadErrorTask(vmc_state_values[0], vmc_state_contents[0]);
            //清洗的时候，增加错误代码和错误状态到错误map中；用来锁住购买界面；
        } else {
            isCleanning = false;
            TaskUtil.getInstance().addUploadErrorTask(vmc_state_values[0], null);
            //错误代码的错误状态为空
        }
        if (RxDataTool.isTrue(bits, 6)) {

            downCup = true;
        } else {
            downCup = false;
        }
        return b;
    }

    public boolean isMakeSuceess() {
        return ismakingSucc;
    }


    public void setIsmakingSucc(boolean ismakingSucc) {
        this.ismakingSucc = ismakingSucc;
    }

    //故障代码 D13 D14 见故障代码列表
    String cf_error_code = "";
    String cf_error_content = "";
    HashMap<String, String> cf_error_map = new HashMap<>();
    boolean isCFError;

    /**
     * 解析出错误咖啡机的错误代码和错误内容，并增添到errorMap中
     */
    private void parseCFError(byte b, byte b2) {
        byte[] da = new byte[2];
        da[0] = b;
        da[1] = b2;
        cf_error_code = RxDataTool.bytes2HexString(da);
        boolean isError = false;
        String error_value = "";
        for (int i = 0; i < cf_error_keys.length; i++) {//0-5
            String error_code = cf_error_keys[i];
            String error_content = cf_error_values[i];
            boolean bo1;
            if (i < 8) {
                bo1 = RxDataTool.isTrue(b2, i);
            } else {
                bo1 = RxDataTool.isTrue(b, i - 8);
            }
            if (bo1) {
                isError = true;
                error_value += error_content + " ";
                cf_error_map.put(error_code, error_content);
                TaskUtil.getInstance().addUploadErrorTask(error_code, error_content);
            } else {
                cf_error_map.put(error_code, null);
                TaskUtil.getInstance().addUploadErrorTask(error_code, null);

            }
        }
        isCFError = isError;
        cf_error_content = error_value;
        da = null;
    }

    //故障代码 D15 D16 见故障代码列表
    String vmc_error_code = "";
    String vmc_error_content = "";
    HashMap<String, String> vmc_error_map = new HashMap<>();
    boolean isVMCError;

    /**
     * 解析出VMC的错误代码和错误内容到错误map中
     */
    private void parseVMCError(byte b, byte b2) {
        byte[] da = new byte[2];
        da[0] = b;
        da[1] = b2;
        vmc_error_code = RxDataTool.bytes2HexString(da);
        boolean isError = false;
        String error_value = "";
        for (int i = 0; i < vmc_error_keys.length; i++) {//0-16
            String error_code = vmc_error_keys[i];
            String error_content = vmc_error_values[i];
            boolean bo1;
            if (i < 8) {
                bo1 = RxDataTool.isTrue(b2, i);
            } else {
                bo1 = RxDataTool.isTrue(b, i - 8);
            }
            if (bo1) {
                isError = true;
                error_value += error_content + " ";
                vmc_error_map.put(error_code, error_content);
                TaskUtil.getInstance().addUploadErrorTask(error_code, error_content);
            } else {
                vmc_error_map.put(error_code, null);
                TaskUtil.getInstance().addUploadErrorTask(error_code, null);

            }
        }
        isVMCError = isError;
        vmc_error_content = error_value;
        da = null;
    }

    /*
    D46~D55
    VMC LOG D46~50coffee;D51~D55vmc
    当 D13~D16 中有故障码时，APP 将此数据上班到服务 器，供唯迪公司故障定位使用
     */
    String vmcLog;

    private byte[] parseErrorLog(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8, byte b9, byte b10) {
        byte[] logs = new byte[10];
        logs[0] = b;
        logs[1] = b2;
        logs[2] = b3;
        logs[3] = b4;
        logs[4] = b5;
        logs[5] = b6;
        logs[6] = b7;
        logs[7] = b8;
        logs[8] = b9;
        logs[9] = b10;
        vmcLog = RxDataTool.bytes2HexString(logs);
        logs = null;
        return logs;
    }

    /*
    D56~D60
    系统ID号
    唯一,20 17 10 29 01，APP 将此数据显示在工程菜单 页面，用于管理机器
     */
    String systemId;

    private byte[] parseSystemId(byte b, byte b2, byte b3, byte b4, byte b5) {
        byte[] ids = new byte[5];
        ids[0] = b;
        ids[1] = b2;
        ids[2] = b3;
        ids[3] = b4;
        ids[4] = b5;
        systemId = RxDataTool.bytes2HexString(ids);
        if (StringUtil.isStringEmpty(DeviceInfo.getInstance().getSystemId())) {
            DeviceInfo.getInstance().setLocalSystemId(systemId);

        }
        ids = null;
        return ids;
    }

    /*
    D60~62
    工艺文件版本号
    由 APP 管理，和工艺表对齐，APP 将此数据显示在工 程菜单页面
     */
    String configVersion;

    private byte[] parseCoffeConfig(byte b, byte b2, byte b3) {
        byte[] ids = new byte[3];
        ids[0] = b;
        ids[1] = b2;
        ids[2] = b3;
        configVersion = RxDataTool.bytes2HexString(ids);
        ids = null;
        return ids;
    }


    /*
    D63
    咖啡机版本号
    咖啡机版本号，当前 0xB3，APP 将此数据显示在工程 菜单页面
     */
    String coffeeVersion;

    private byte parseCoffeeVersion(byte b) {
        coffeeVersion = RxDataTool.numToHex8(b & 0xff);
        return b;
    }

    /*
    D64
    VMC 版本号
    VMC 版本号，当前 0x11，APP 将此数据显示在工程菜 单页面
     */
    String vmcVersion;

    private byte parseVmcVersion(byte b) {
        vmcVersion = RxDataTool.numToHex8(b & 0xff);
        return b;
    }

    int boilerTemp;

    private byte parseBoilerTemp(byte b) {
        boilerTemp = b;
        return b;
    }

    int coffeeTemp;

    private byte parseCoffeeTemp(byte b) {
        coffeeTemp = b;
        return b;
    }

    public int getMakingProgress() {
        return makingProgress;
    }

    public void setMakingProgress(int makingProgress) {
        this.makingProgress = makingProgress;
    }

    public boolean isShowDeBugMenu() {
        return showDeBugMenu;
    }

    public void setShowDeBugMenu(boolean showDeBugMenu) {
        this.showDeBugMenu = showDeBugMenu;
    }

    public boolean isRealData() {
        return isRealData;
    }

    public void setRealData(boolean realData) {
        isRealData = realData;
    }

    byte[] head;

    /**
     * 获得字节数组bytes的前2个字节
     */
    byte[] getbytesStartWithHead(byte[] bytes) {
        if (head == null) {
            head = new byte[2];
        }
        if (bytes != null && bytes.length >= 2) {
            head[0] = bytes[0];
            head[1] = bytes[1];
        }
        return head;

    }

    int logCount = 0;
    final int MaxCount = 10;

    private boolean timeToLog() {
        return logCount >= MaxCount;
    }

    public boolean isReadTimeOut() {
        long nowTime = System.currentTimeMillis();
        return (readTime != 0 && ((nowTime - readTime) >= CMDUtil.VMC_TIMEOUT));
    }

    long readTime;

    /**
     * 解析A域的64个字节
     * 1、传入的字节为空，设置isNull=true；
     * 2、解析A域的64个字节；解析出来的状态放在VMCState类的属性中
     *    有故障，每个15秒打印日志；
     *    没故障:
     *         在制作中：打印日志
     *         不在制作中：vmc握手响应15s打印日志、vmc指令响应立即打印日志，
     */
    public void initBytes(byte[] bytes) {
        if (bytes == null) {
            isNull = true;
            isRealData = false;
        } else {
            isNull = false;

            if (isSureData(bytes)) {
                isRealData = true;
                readTime = System.currentTimeMillis();
                pareBytes(bytes);  //解析A域的64个字节
                if (!MyApp.DEBUG_LOG) {
                    boolean cfError = isCFError();
                    boolean vmcError = isVMCError();
                    if (cfError || vmcError) {
                        LogUtil.vmc("故障：" + getVmc_error_content() + "-", bytes, 15000);//为防止log日志过大，降低打印频率
                    } else {
                        if (MakingPageMode.getIns().isMakeing()) {//正在制作
                            LogUtil.vmc("制作中：", bytes);

                        } else {
                            String head = RxDataTool.bytes2HexString(getbytesStartWithHead(bytes));
                            //获得字节数组bytes的前2个字节，并转化为string
                            if (head.equals("AA01")) {//握手指令
                                LogUtil.vmc("vmc握手响应：", bytes, 15000);//为防止log日志过大，降低打印频率
                            } else {
                                LogUtil.vmc("vmc指令响应：", bytes);
                                LogUtil.timeToLogVmc();
                            }
                        }
                    }
                } else {
                    LogUtil.vmc("vmc receive：", bytes);
                }
            } else {
                isRealData = false;
            }
        }
    }

    /**数据从第4个字节开始*/
    private byte getByte(byte[] bytes, int index) {
        return bytes[index + 3];
    }

    /**
     * 解析A域的64个字节
     */
    private void pareBytes(byte[] bytes) {
        parseBusy(getByte(bytes, 1));      //isBusy  makingProgress：BUSY和进度条
        parseWorkMenu(getByte(bytes, 2));  //showDeBugMenu ：工作模式
        parseD3(getByte(bytes, 3));        //coffeeState：为传入的字节， coffeeState_s：咖啡机运行状态为磨豆、doser、压粉、浸润、冲泡、卸渣的某几项
        parseD4(getByte(bytes, 4));        //int powder; String powder_s; 通道落粉状态
        parseD5(getByte(bytes, 5));        //int powder2;String powder2_s;搅拌水阀状态
        parseVmcState(getByte(bytes, 6));  //VMC状态
        parseBoilerTemp(getByte(bytes, 7)); //boilerTemp:辅助锅炉温度
        parseCoffeeTemp(getByte(bytes, 8)); //coffeeTemp：咖啡机锅炉温度

        parseCFError(getByte(bytes, 13), getByte(bytes, 14));   //咖啡机错误代码
        parseVMCError(getByte(bytes, 15), getByte(bytes, 16));  //vmc错误代码
        parseErrorLog(getByte(bytes, 46), getByte(bytes, 47), getByte(bytes, 48), getByte(bytes, 49), getByte(bytes, 50)
                , getByte(bytes, 51), getByte(bytes, 52), getByte(bytes, 53), getByte(bytes, 54), getByte(bytes, 55));
        parseSystemId(getByte(bytes, 56), getByte(bytes, 57), getByte(bytes, 58), getByte(bytes, 59), getByte(bytes, 60));//systemId
        parseCoffeConfig(getByte(bytes, 60), getByte(bytes, 61), getByte(bytes, 62));
        parseCoffeeVersion(getByte(bytes, 63));//coffeeVersion
        parseVmcVersion(getByte(bytes, 64));   //vmcVersion


    }

    /**存储byte b对应位为1的状态，状态在String[] arr中；如bit0=1，则去arr[0]*/
    private String byteToResultString(byte b, String[] arr) {
        String p = "";
        byte[] bits = RxDataTool.getBooleanArray(b);

        for (int i = 0; i < bits.length; i++) {
            if (RxDataTool.isTrue(bits, i)) {//该i位为1
                p += arr[i] + " ";
            }
        }
        bits = null;
        return p;

    }
}
