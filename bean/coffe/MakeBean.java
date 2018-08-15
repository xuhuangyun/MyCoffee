package com.fancoff.coffeemaker.bean.coffe;

import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.utils.rx.RxDataTool;

import java.io.Serializable;

/**
 * Created by apple on 2018/2/1.
 */

/**
 * 1、饮品参数的读取与赋值
 * 2、甜度字节读取
 * 3、获取
 * 4、获取制作指令的第4、5字节；
 */
public class MakeBean implements Serializable {
    int index;
    int coffee_first;
    int coffee_powder;
    SugarLevelBean sugar_level;
    int ch1_water;
    int ch2_water;
    int ch3_water;
    int ch4_water;
    int coffee_water;
    int coffee_preWater;
    int ch1R_powder_level;
    int ch2L_powder_level;
    int ch2R_powder_level;
    int ch3L_powder_level;
    int ch3R_powder_level;
    int ch4L_powder_level;
    int ch4R_powder_level;

    public boolean isCoffee() {
        return coffee_powder > 0;
    }

    public int getCoffee_first() {
        return coffee_first;
    }

    public void setCoffee_first(int coffee_first) {
        this.coffee_first = coffee_first;
    }

    public int getCoffee_powder() {
        return coffee_powder;
    }

    public void setCoffee_powder(int coffee_powder) {
        this.coffee_powder = coffee_powder;
    }

    public int getCoffee_water() {
        return coffee_water;
    }

    public void setCoffee_water(int coffee_water) {
        this.coffee_water = coffee_water;
    }

    public int getCoffee_preWater() {
        return coffee_preWater;
    }

    public void setCoffee_preWater(int coffee_preWater) {
        this.coffee_preWater = coffee_preWater;
    }

    public SugarLevelBean getSugar_level() {
        if (sugar_level == null) {
            sugar_level = new SugarLevelBean();
        }
        return sugar_level;
    }

    public void setSugar_level(SugarLevelBean sugar_level) {
        this.sugar_level = sugar_level;
    }

    public int getCh1_water() {
        return ch1_water;
    }

    public void setCh1_water(int ch1_water) {
        this.ch1_water = ch1_water;
    }

    public int getCh2_water() {
        return ch2_water;
    }

    public void setCh2_water(int ch2_water) {
        this.ch2_water = ch2_water;
    }

    public int getCh3_water() {
        return ch3_water;
    }

    public void setCh3_water(int ch3_water) {
        this.ch3_water = ch3_water;
    }

    public int getCh4_water() {
        return ch4_water;
    }

    public int getCh1R_powder_level() {
        return ch1R_powder_level;
    }

    public void setCh1R_powder_level(int ch1R_powder_level) {
        this.ch1R_powder_level = ch1R_powder_level;
    }

    public int getCh2L_powder_level() {
        return ch2L_powder_level;
    }

    public void setCh2L_powder_level(int ch2L_powder_level) {
        this.ch2L_powder_level = ch2L_powder_level;
    }

    public int getCh2R_powder_level() {
        return ch2R_powder_level;
    }

    public void setCh2R_powder_level(int ch2R_powder_level) {
        this.ch2R_powder_level = ch2R_powder_level;
    }

    public int getCh3L_powder_level() {
        return ch3L_powder_level;
    }

    public void setCh3L_powder_level(int ch3L_powder_level) {
        this.ch3L_powder_level = ch3L_powder_level;
    }

    public int getCh3R_powder_level() {
        return ch3R_powder_level;
    }

    public void setCh3R_powder_level(int ch3R_powder_level) {
        this.ch3R_powder_level = ch3R_powder_level;
    }

    public int getCh4L_powder_level() {
        return ch4L_powder_level;
    }

    public void setCh4L_powder_level(int ch4L_powder_level) {
        this.ch4L_powder_level = ch4L_powder_level;
    }

    public int getCh4R_powder_level() {
        return ch4R_powder_level;
    }

    public void setCh4R_powder_level(int ch4R_powder_level) {
        this.ch4R_powder_level = ch4R_powder_level;
    }

    /*
    D1:咖啡工艺编号(1~64)，默认 1 D2B0~B3 糖量选择
000:无糖 001:低糖 010:中糖 011:高糖
D2B4:0 自动落杯，1 人工放杯
     */
    /**
     *  根据甜度，返回对应的字节
     */
    public int getSugarVMC(int sugarType) {
//        boolean handcup = RxSPTool.getBoolean(MyApp.getIns(), "handCup");//是否手动落杯
//        if (sugarType == MyConstant.SUGAR.SUGAR_LOW) {
//            if (handcup) {
//                return 0x09;
//            } else {
//                return 0x01;
//            }
//
//        } else if (sugarType == MyConstant.SUGAR.SUGAR_MIDDLE) {
//            if (handcup) {
//                return 0x0A;
//            } else {
//                return 0x02;
//            }
//        } else if (sugarType == MyConstant.SUGAR.SUGAR_HIGHT) {
//            if (handcup) {
//                return 0x0B;
//            } else {
//                return 0x03;
//            }
//        } else {
//            if (handcup) {
//                return 0x08;
//            } else {
//                return 0x00;
//            }
//        }
        if (sugarType == MyConstant.SUGAR.SUGAR_LOW) {
            return 0x01;
        } else if (sugarType == MyConstant.SUGAR.SUGAR_MIDDLE) {
            return 0x02;
        } else if (sugarType == MyConstant.SUGAR.SUGAR_HIGHT) {
            return 0x03;
        } else {
            return 0x00;
        }
    }


    //10 00 00 10 00 00 00 00 06 0A 10 00 00 00 00 00 00 A0 00 6E 00 00 00 00 00 00 00 00 00 00 00 00
//10 00 00 10 00 00 00 00 06 0A 10 00 00 00 00 00 00 A0 00 6E 00 00 00 00 00 00 00 00 00 00 00 00
//    int ch2_water;
//    int ch3_water;
//    int ch4_water;
//    int needCoffee;
//    int coffeePowder;
//    int coffeeWater;
//    int coffeePreWater;
//    int ch1R_powder_level ;
//    int ch2L_powder_level;
//    int ch2R_powder_level;
//    int ch3L_powder_level;
//    int ch3R_powder_level;
//    int ch4L_powder_level;
//    int ch4R_powder_level;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setCh4_water(int ch4_water) {
        this.ch4_water = ch4_water;
    }

    /**
     * 饮品粉水工艺表 字节数组
     */
    public byte[] getByteBody() {
        StringBuffer s = new StringBuffer();
        String bytestr1 = RxDataTool.numToHex8(getSugar_level().getLow() < 0 ? 0 : getSugar_level().getLow());//D1
        s.append(bytestr1);
        String bytestr2 = RxDataTool.numToHex8(getSugar_level().getMiddle() < 0 ? 0 : getSugar_level().getMiddle());//D2
        s.append(bytestr2);
        String bytestr3 = RxDataTool.numToHex8(getSugar_level().getHigh() < 0 ? 0 : getSugar_level().getHigh());//D3
        s.append(bytestr3);
        String bytestr4 = RxDataTool.numToHex8(getCh1R_powder_level());//D4
        s.append(bytestr4);
        String bytestr5 = RxDataTool.numToHex8(getCh2L_powder_level());//D5
        s.append(bytestr5);
        String bytestr6 = RxDataTool.numToHex8(getCh2R_powder_level());//D6
        s.append(bytestr6);
        String bytestr7 = RxDataTool.numToHex8(getCh3L_powder_level());//D7
        s.append(bytestr7);
        String bytestr8 = RxDataTool.numToHex8(getCh3R_powder_level());//D8
        s.append(bytestr8);
        String bytestr9 = RxDataTool.numToHex8(0);//D9保留
        s.append(bytestr9);
        String bytestr10 = RxDataTool.numToHex8(0);//D10保留
        s.append(bytestr10);
        String bytestr11 = RxDataTool.numToHex8(getCh1_water());//D11
        s.append(bytestr11);
        String bytestr12 = RxDataTool.numToHex8(getCh2_water());//D12
        s.append(bytestr12);
        String bytestr13 = RxDataTool.numToHex8(getCh3_water());//D13
        s.append(bytestr13);
        String bytestr14 = RxDataTool.numToHex8(getCh4_water());//D14
        s.append(bytestr14);
        String bytestr15 = RxDataTool.numToHex8(getCoffee_powder());//D15
        s.append(bytestr15);
        String bytestr16 = RxDataTool.numToHex8(getCoffee_water());//D16
        s.append(bytestr16);
        String bytestr17 = RxDataTool.numToHex8(0);//D17
        s.append(bytestr17);
        String bytestr18 = RxDataTool.numToHex8(coffee_first);//D18
        s.append(bytestr18);
        String bytestr19 = RxDataTool.numToHex8(index);//D19
        s.append(bytestr19);
        for (int i = 0; i < 13; i++) {//D20-D32
            String ap = RxDataTool.numToHex8(0);
            s.append(ap);
        }
        return RxDataTool.hexString2Bytes(s.toString());
    }

    /**
     * 1、得到饮品id的string类型；
     * 2、根据甜度得到甜度的string类型；
     * 3、将饮品id和甜度，转换为字节数组；
     * 得到制作指令的第4、5个字节；
     */
    public byte[] getMakigBodyByte(int sugarType) {
        StringBuffer s = new StringBuffer();
        String bytestr1 = RxDataTool.numToHex8(getIndex());//猜估计是将饮品id转换为1个16进制字节string
        s.append(bytestr1);
        String bytestr2 = RxDataTool.numToHex8(getSugarVMC(sugarType));//甜度，1个字节string
        s.append(bytestr2);
        return RxDataTool.hexString2Bytes(s.toString());//将2个string转换为byte[]  如0103 转换为0x01、0x03
    }

    @Override
    public String toString() {
        return "MakeBean{" +
                "index=" + index +
                ", coffee_first=" + coffee_first +
                ", coffee_powder=" + coffee_powder +
                ", sugar_level=" + sugar_level +
                ", ch1_water=" + ch1_water +
                ", ch2_water=" + ch2_water +
                ", ch3_water=" + ch3_water +
                ", ch4_water=" + ch4_water +
                ", coffee_water=" + coffee_water +
                ", coffee_preWater=" + coffee_preWater +
                ", ch1R_powder_level=" + ch1R_powder_level +
                ", ch2L_powder_level=" + ch2L_powder_level +
                ", ch2R_powder_level=" + ch2R_powder_level +
                ", ch3L_powder_level=" + ch3L_powder_level +
                ", ch3R_powder_level=" + ch3R_powder_level +
                ", ch4L_powder_level=" + ch4L_powder_level +
                ", ch4R_powder_level=" + ch4R_powder_level +
                '}';
    }


}
