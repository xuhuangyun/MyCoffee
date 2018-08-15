package com.fancoff.coffeemaker.bean.machine;


import com.fancoff.coffeemaker.utils.StringUtil;
import com.fancoff.coffeemaker.utils.rx.RxDataTool;

import java.io.Serializable;

/**
 * Created by apple on 2017/9/10.
 */
public class MachineConfiBean implements Serializable {
    int machine_type;
    int report_duration = 60;
    int coffee_type;
    String serial;
    int preWater;
    int waterType;
    int dirty_delay;
    int cupStuck_delay;
    int finish_delay;
    int armOut_delay;
    int armIn_delay;
    int blender_delay;
    int auto_heat;
    Light greenLight;
    Light redLight;
    Main_temper main_temper;
    Assist_temper assist_temper;
    CleanBean clean;
    Service_provider service_provider;
    boolean isNull;

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    public Light getGreenLight() {
        return greenLight;
    }

    public void setGreenLight(Light greenLight) {
        this.greenLight = greenLight;
    }

    public Light getRedLight() {
        return redLight;
    }

    public void setRedLight(Light redLight) {
        this.redLight = redLight;
    }


    public Service_provider getService_provider() {
        return service_provider;
    }

    public void setService_provider(Service_provider service_provider) {
        this.service_provider = service_provider;
    }

    public CleanBean getClean() {
        return clean;
    }

    public void setClean(CleanBean clean) {
        this.clean = clean;
    }

    public Main_temper getMain_temper() {
        return main_temper;
    }

    public void setMain_temper(Main_temper main_temper) {
        this.main_temper = main_temper;
    }

    public Assist_temper getAssist_temper() {
        return assist_temper;
    }

    public void setAssist_temper(Assist_temper assist_temper) {
        this.assist_temper = assist_temper;
    }

    public int getPreWater() {
        return preWater;
    }

    public void setPreWater(int preWater) {
        this.preWater = preWater;
    }


    public String getSerial() {
        if (StringUtil.isStringEmpty(serial)) {
            serial = "0";
        }
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public int getCoffee_type() {
        return coffee_type;
    }

    public void setCoffee_type(int coffee_type) {
        this.coffee_type = coffee_type;
    }

    public int getMachine_type() {
        return machine_type;
    }

    public void setMachine_type(int machine_type) {
        this.machine_type = machine_type;
    }

    public int getReport_duration() {
        return report_duration;
    }

    public void setReport_duration(int report_duration) {
        this.report_duration = report_duration;
    }

    public int getWaterType() {
        return waterType;
    }

    public void setWaterType(int waterType) {
        this.waterType = waterType;
    }

    public int getDirty_delay() {
        return dirty_delay;
    }

    public void setDirty_delay(int dirty_delay) {
        this.dirty_delay = dirty_delay;
    }

    public int getCupStuck_delay() {
        return cupStuck_delay;
    }

    public void setCupStuck_delay(int cupStuck_delay) {
        this.cupStuck_delay = cupStuck_delay;
    }

    public int getFinish_delay() {
        return finish_delay;
    }

    public void setFinish_delay(int finish_delay) {
        this.finish_delay = finish_delay;
    }

    public int getArmOut_delay() {
        return armOut_delay;
    }

    public void setArmOut_delay(int armOut_delay) {
        this.armOut_delay = armOut_delay;
    }

    public int getArmIn_delay() {
        return armIn_delay;
    }

    public void setArmIn_delay(int armIn_delay) {
        this.armIn_delay = armIn_delay;
    }

    public int getBlender_delay() {
        return blender_delay;
    }

    public void setBlender_delay(int blender_delay) {
        this.blender_delay = blender_delay;
    }

    public int getAuto_heat() {
        return auto_heat;
    }

    public void setAuto_heat(int auto_heat) {
        this.auto_heat = auto_heat;
    }

    @Override
    public String toString() {
        return "MachineConfiBean{" +
                "machine_type=" + machine_type +
                ", report_duration=" + report_duration +
                ", coffee_type=" + coffee_type +
                ", serial='" + serial + '\'' +
                ", preWater=" + preWater +
                ", waterType=" + waterType +
                ", dirty_delay=" + dirty_delay +
                ", cupStuck_delay=" + cupStuck_delay +
                ", finish_delay=" + finish_delay +
                ", armOut_delay=" + armOut_delay +
                ", armIn_delay=" + armIn_delay +
                ", blender_delay=" + blender_delay +
                ", auto_heat=" + auto_heat +
                ", greenLight=" + greenLight +
                ", redLight=" + redLight +
                ", main_temper=" + main_temper +
                ", assist_temper=" + assist_temper +
                ", clean=" + clean +
                ", service_provider=" + service_provider +
                '}';
    }

    public byte[] getBytes() {
        StringBuffer s = new StringBuffer();
        //D1 咖啡机目标加热温度，单位摄氏度，取值范围:70~95 度，系统默认 85 度
        String bytestr1 = RxDataTool.numToHex8(getMain_temper().getGoal());
        s.append(bytestr1);
        //D2 咖啡机加热回差，单位摄氏度，取值范围 1~5 度，系统默认 2 度
        String bytestr2 = RxDataTool.numToHex8(getMain_temper().getBacklash());
        s.append(bytestr2);
        //D3 辅料锅炉加热目标温度，单位摄氏度，取值范围:70~95 度，系统默认 85 度
        String bytestr3 = RxDataTool.numToHex8(getAssist_temper().getGoal());
        s.append(bytestr3);
        //D4 辅料锅炉加热回差，单位摄氏度，取值范围 1~5 度，系统默认 2 度
        String bytestr4 = RxDataTool.numToHex8(getAssist_temper().getBacklash());
        s.append(bytestr4);
        //D5咖啡机浸润水量，单位 ml，取值范围 5~30ml，系统默认 15ml
        String bytestr5 = RxDataTool.numToHex8(getPreWater());
        s.append(bytestr5);
        //D6 保留
        String bytestr6 = RxDataTool.numToHex8(0);
        s.append(bytestr6);
        //D7 CH1 通道水路补偿，D7B7=1 表示负补偿，为 0 表示正补偿 D7B0~D7B6:补偿量，单位 ml，取值范围 0~100ml，系统默认 0
        String bytestr7 = RxDataTool.numToHex8(0);
        s.append(bytestr7);
        //D8 CH2 通道水路补偿，D8B7=1 表示负补偿，为 0 表示正补偿 D8B0~D7B6:补偿量，单位 ml，取值范围 0~100ml，系统默认 0
        String bytestr8 = RxDataTool.numToHex8(0);
        s.append(bytestr8);
        //D9 CH3 通道水路补偿，D9B7=1 表示负补偿，为 0 表示正补偿 D9B0~D7B6:补偿量，单位 ml，取值范围 0~100ml，系统默认 0
        String bytestr9 = RxDataTool.numToHex8(0);
        s.append(bytestr9);
        //D10 CH4 通道水路补偿，D10B7=1 表示负补偿，为 0 表示正补偿 D10B0~D7B6:补偿量，单位 ml，取值范围 0~100ml，系统默认 0——保留参数
        String bytestr10 = RxDataTool.numToHex8(0);
        s.append(bytestr10);
        //D11 CH1L 粉路补偿，B7 为 1 表示负补偿，0 表示正补偿，无单位，取值范围 0~100，默认 0
        String bytestr11 = RxDataTool.numToHex8(0);
        s.append(bytestr11);
        //D12 CH1R 粉路补偿，B7 为 1 表示负补偿，0 表示正补偿，无单位，取值范围 0~100，默认 0
        String bytestr12 = RxDataTool.numToHex8(0);
        s.append(bytestr12);
        //D13 CH2L 粉路补偿，B7 为 1 表示负补偿，0 表示正补偿，无单位，取值范围 0~100，默认 0
        String bytestr13 = RxDataTool.numToHex8(0);
        s.append(bytestr13);
        //D14 CH2R 粉路补偿，B7 为 1 表示负补偿，0 表示正补偿，无单位，取值范围 0~100，默认 0
        String bytestr14 = RxDataTool.numToHex8(0);
        s.append(bytestr14);
        //D15 CH3L 粉路补偿，B7 为 1 表示负补偿，0 表示正补偿，无单位，取值范围 0~100，默认 0
        String bytestr15 = RxDataTool.numToHex8(0);
        s.append(bytestr15);
        //D16 CH3R 粉路补偿，B7 为 1 表示负补偿，0 表示正补偿，无单位，取值范围 0~100，默认 0
        String bytestr16 = RxDataTool.numToHex8(0);
        s.append(bytestr16);
        //D17 CH4L 粉路补偿，B7 为 1 表示负补偿，0 表示正补偿，无单位，取值范围 0~100，默认 0
        String bytestr17 = RxDataTool.numToHex8(0);
        s.append(bytestr17);
        //D18 CH4R 粉路补偿，B7 为 1 表示负补偿，0 表示正补偿，无单位，取值范围 0~100，默认 0
        String bytestr18 = RxDataTool.numToHex8(0);
        s.append(bytestr18);
        //D19 CH1 水路浸润水量，单位 ml，取值范围 5~20ml，系统默认 5ml
        String bytestr19 = RxDataTool.numToHex8(0);
        s.append(bytestr19);
        //D20 CH2 水路浸润水量，单位 ml，取值范围 5~20ml，系统默认 5ml
        String bytestr20 = RxDataTool.numToHex8(0);
        s.append(bytestr20);
        //D21 CH3 水路浸润水量，单位 ml，取值范围 5~20ml，系统默认 5ml
        String bytestr21 = RxDataTool.numToHex8(0);
        s.append(bytestr21);
        //D22 CH4 水路浸润水量，单位 ml，取值范围 5~20ml，系统默认 5ml
        String bytestr22 = RxDataTool.numToHex8(0);
        s.append(bytestr22);
        //D23 搅拌器延迟关闭时间，单位 s，取值范围 0~5s，默认 2s
        String bytestr23 = RxDataTool.numToHex8(0);
        s.append(bytestr23);
        //D24-D30保留
        for (int i = 0; i < 7; i++) {
            String ap = RxDataTool.numToHex8(0);
            s.append(ap);
        }
        //D31~D32 固定写入 AA55，写入其他值，系统不保存该参数列表
        String bytestr31 = RxDataTool.numToHex8(0xaa);
        s.append(bytestr31);
        String bytestr32 = RxDataTool.numToHex8(0x55);
        s.append(bytestr32);
        return RxDataTool.hexString2Bytes(s.toString());
    }
}
