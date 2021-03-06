package com.fancoff.coffeemaker.Application;

import com.fancoff.coffeemaker.bean.MakingStatebean;
import com.fancoff.coffeemaker.ui.making.MakingPageMode;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.rx.RxDataTool;

import java.util.ArrayList;

/**
 * Created by apple on 2018/5/21.
 */

public class TestIoDatas {
    private static final TestIoDatas ourInstance = new TestIoDatas();

    public static TestIoDatas getInstance() {
        return ourInstance;
    }

    private TestIoDatas() {
    }

    ArrayList<String> testMakingList = new ArrayList<>();
    int index = 0;
    int Textindex = 0;
    byte[] testWright;

    public void remake() {
        index = 61;
    }

    public void testwrite() {
        MakingStatebean makingStatebean = MakingPageMode.getIns().getMakingStatebean();
        if (makingStatebean != null && !makingStatebean.isAllFinish()) {
            String ss = testMakingList.get(index).trim().replace(" ", "");
            index++;
            if (index >= testMakingList.size()) {
                index = 0;
            }
            testWright = RxDataTool.hexString2Bytes(ss);
        } else {
            index = 0;
            String test = "AA 01 00 40 00 00 00 00 00 03 54 51 AA BB CC DD 00 00 00 00 00 00 00 00 7F 57 7F 57 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 8D 00 FC 20 17 10 29 01 12 34 B6 A1 BF";
            Textindex++;
//            if (Textindex >= 10) {
////                DataCenter.getInstance().gindex = 0;etNowCoffeeConfigBean().getImage().getLeft_bottoms().clear();
////                DataCenter.getInstance().update(DataCenter.getInstance().getNowCoffeeConfigBean());
////                RxBus.getInstance().post(new MsgEvent(MyConstant.ACTION.REF_ALL));
//                 test = "AA 01 00 40 64 00 00 00 00 03 54 51 AA BB CC DD 00 00 00 00 00 00 00 00 7F 57 7F 57 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 8D 00 FC 20 17 10 29 01 12 34 B6 A1 BF";
//
//            }
////////            else
//////                if (Textindex >= 5) {
//////                    test = "AA 01 00 40 00 00 00 20 00 03 03 53 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD";
//////
//////                }
            String ss = test.trim().replace(" ", "");
            testWright = RxDataTool.hexString2Bytes(ss);
        }
    }

    boolean openSetting = false;
    boolean openHot = false;

    public boolean isOpenHot() {
        return openHot;
    }

    public void setOpenHot(boolean openHot) {
        this.openHot = openHot;
    }

    public boolean isOpenSetting() {
        return openSetting;
    }

    public void setOpenSetting(boolean openSetting) {
        LogUtil.test("setOpenSetting:" + openSetting);
        this.openSetting = openSetting;
    }

    public byte[] getTestReadBytes(byte baciton) {
        if (testWright != null && testWright.length > 0) {
            byte[] ss = RxDataTool.getBytes(testWright, 0, testWright.length - 1);
            ss[1] = baciton;
            if (openSetting) {
                ss[5] = (byte) 0x03;
            } else {
                ss[5] = (byte) 0x00;
            }
            if (openHot) {
                ss[9] = (byte) (ss[9] | 0x03);
            } else {
                ss[9] = (byte) (ss[9] & 0xFC);
            }

            byte b = RxDataTool.getXor(ss);
            testWright = null;
            byte[] ss2 = RxDataTool.byteMerger(ss, new byte[]{b});
            return ss2;
        }

        return null;


    }

    public void intTestMakingList() {

        testMakingList.add("AA 20 00 40 00 00 00 2A 00 03 54 50 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5B 7F 5B 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 01 00 CC B0 90 00 FC 20 17 10 29 01 12 34 B6 A1 C9");


////        //脏杯
//        testMakingList.add("AA 01 00 40 80 00 00 20 00 04 56 53 AA BB CC DD 00 00 00 00 00 00 00 00 7F 6E 7F 6E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1D 03 00 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 EB");
//        testMakingList.add("AA 01 00 40 80 00 00 20 00 04 56 53 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 80 00 00 20 00 04 56 53 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 80 00 00 20 00 04 56 53 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 80 00 00 20 00 04 56 53 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 80 00 00 20 00 04 56 53 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 80 00 00 20 00 04 56 53 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 80 00 00 20 00 04 56 53 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//

////        //卡杯
//        testMakingList.add("AA 01 00 40 80 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 80 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 80 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 80 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");
//        testMakingList.add("AA 01 00 40 00 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 08 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A 00 03 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 AD");


        testMakingList.add("AA 01 00 40 81 00 00 20 00 43 56 53 AA BB CC DD 00 00 00 00 00 00 00 00 7F 6E 7F 6E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1D 03 00 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 EB");
        testMakingList.add("AA 01 00 40 84 00 00 20 00 03 56 53 AA BB CC DD 00 00 00 00 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 2A 00 02 01 00 CC B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 98");
        testMakingList.add("AA 01 00 40 85 00 1F 20 20 03 56 53 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 4A 00 00 01 00 00 B0 86 00 7C 20 17 10 29 01 12 34 B6 E1 08");
        testMakingList.add("AA 01 00 40 87 00 1F 20 20 03 56 53 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 4A 00 00 01 00 01 B0 87 00 7C 20 17 10 29 01 12 34 B6 E1 0A");


//        testMakingList.add("AA 01 00 40 64 00 00 00 00 03 55 5C AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 6E 00 FC 20 17 10 29 01 12 34 B6 E1 14");
//        testMakingList.add("AA 01 00 40 64 00 00 00 00 03 55 5C AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 6E 00 FC 20 17 10 29 01 12 34 B6 E1 14");


        testMakingList.add("AA 01 00 40 BE 00 1F 00 00 03 55 5C AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 01 B0 6D 00 FC 20 17 10 29 01 12 34 B6 E1 12 ");
        testMakingList.add("AA 01 00 40 C0 00 1F 00 00 03 56 5C AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 01 B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 6E ");
        testMakingList.add("AA 01 00 40 C3 00 1F 00 00 03 55 5C AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 01 B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 6E ");
        testMakingList.add("AA 01 00 40 C5 00 1F 00 00 03 56 5D AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 01 B0 6B 00 FC 20 17 10 29 01 12 34 B6 E1 6D");
        testMakingList.add("AA 01 00 40 C8 00 1C 00 00 03 55 5C AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 6C 07 FC 20 17 10 29 01 12 34 B6 E1 62 ");
        testMakingList.add("AA 01 00 40 CA 00 1C 00 00 03 56 5C AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 6C 09 FE 20 17 10 29 01 12 34 B6 E1 6F ");
        testMakingList.add("AA 01 00 40 CC 00 18 00 00 03 55 5C AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 00 B0 6D 00 FA 20 17 10 29 01 12 34 B6 E1 60 ");
        testMakingList.add("AA 01 00 40 CF 00 18 00 00 03 55 5C AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 6E 00 FA 20 17 10 29 01 12 34 B6 E1 62");
        testMakingList.add("AA 01 00 40 D1 00 18 00 00 03 55 5B AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 03 B0 70 00 7A 20 17 10 29 01 12 34 B6 E1 E4");
        testMakingList.add("AA 01 00 40 D4 00 18 00 00 03 55 5B AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 03 B0 71 00 FA 20 17 10 29 01 12 34 B6 E1 60");
        testMakingList.add("AA 01 00 40 D6 00 18 00 00 03 55 5A AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 72 00 7A 20 17 10 29 01 12 34 B6 E1 E7");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 03 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");

        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");
        testMakingList.add("AA 01 00 40 D8 00 18 00 00 04 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 74 00 7A 20 17 10 29 01 12 34 B6 E1 EC ");



        testMakingList.add("AA 01 00 40 DB 00 18 00 00 03 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 75 00 7A 20 17 10 29 01 12 34 B6 E1 EE");
        testMakingList.add("AA 01 00 40 DD 00 18 00 00 03 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 75 00 FA 20 17 10 29 01 12 34 B6 E1 68 ");
        testMakingList.add("AA 01 00 40 DF 00 18 00 00 03 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 76 00 7A 20 17 10 29 01 12 34 B6 E1 E9");
        testMakingList.add("AA 01 00 40 E2 00 18 00 00 03 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 76 00 FA 20 17 10 29 01 12 34 B6 E1 54 ");
        testMakingList.add("AA 01 00 40 E2 00 18 00 00 03 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 76 00 FA 20 17 10 29 01 12 34 B6 E1 54");
        testMakingList.add("AA 01 00 40 E2 00 18 00 00 03 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 76 00 FA 20 17 10 29 01 12 34 B6 E1 54 ");
        testMakingList.add("AA 01 00 40 E2 00 18 00 00 03 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 75 00 FA 20 17 10 29 01 12 34 B6 E1 57");
        testMakingList.add("AA 01 00 40 E2 00 18 00 00 03 55 59 AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 04 B0 75 00 7A 20 17 10 29 01 12 34 B6 E1 D7");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 00 10 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 00 10 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 00 10 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 00 10 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5C AA BB CC DD 00 00 00 10 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DB 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 4D");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DB 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 4D ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DB 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 4D");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DB 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 4D ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DB 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 4D");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DB 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 4D ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DB 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 4D");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DB 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 4D ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DB 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 4D");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DB 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 4D ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 10 00 00 03 55 5A AA BB CC DD 00 00 40 08 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 00 00 01 00 02 B0 73 08 FE 20 17 10 29 01 12 34 B6 E1 50 ");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DB 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 4D");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DB 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 4D ");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DB 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 4D");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 0B 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DC 00 00 01 00 CC B0 6C 00 FC 20 17 10 29 01 12 34 B6 E1 42");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 0B 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DC 00 00 01 00 CC B0 6D 00 FC 20 17 10 29 01 12 34 B6 E1 43");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 0B 55 5C AA BB CC DD 00 00 40 08 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DC 00 00 01 00 CC B0 6D 00 FC 20 17 10 29 01 12 34 B6 E1 43");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 0B 55 5C AA BB CC DD 00 00 40 04 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DC 00 00 01 00 CC B0 6D 00 FC 20 17 10 29 01 12 34 B6 E1 43");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 0B 55 5C AA BB CC DD 00 00 40 04 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DC 00 00 01 00 CC B0 6D 00 FC 20 17 10 29 01 12 34 B6 E1 43");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 0B 55 5C AA BB CC DD 00 00 40 04 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DC 00 00 01 00 CC B0 6D 00 FC 20 17 10 29 01 12 34 B6 E1 43");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 0B 55 5C AA BB CC DD 00 00 40 04 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DC 00 00 01 00 CC B0 6D 00 FC 20 17 10 29 01 12 34 B6 E1 43");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 0B 55 5C AA BB CC DD 00 00 40 04 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DC 00 00 01 00 CC B0 6D 00 FC 20 17 10 29 01 12 34 B6 E1 43");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 0B 55 5C AA BB CC DD 00 00 40 04 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DC 00 00 01 00 CC B0 6D 00 FC 20 17 10 29 01 12 34 B6 E1 43");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 0B 55 5C AA BB CC DD 00 00 40 04 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DC 00 00 01 00 CC B0 6D 00 FC 20 17 10 29 01 12 34 B6 E1 43");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 0B 55 5C AA BB CC DD 00 00 40 04 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DC 00 00 01 00 CC B0 6D 00 FC 20 17 10 29 01 12 34 B6 E1 43");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 0B 55 5C AA BB CC DD 00 00 40 04 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DC 00 00 01 00 CC B0 6D 00 FC 20 17 10 29 01 12 34 B6 E1 43");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 0B 55 5C AA BB CC DD 00 00 40 04 00 00 00 00 7F 4E 7F 4E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DC 00 00 01 00 CC B0 6D 00 FC 20 17 10 29 01 12 34 B6 E1 43");
        testMakingList.add("AA 01 00 40 E4 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 04 00 00 00 00 7F 5F 7F 5F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 E1 00 03 01 00 CC B0 6E 00 FC 20 17 10 29 01 12 34 B6 E1 76 ");
        testMakingList.add("AA 01 00 40 64 00 00 00 00 03 55 5C AA BB CC DD 00 00 40 04 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 6E 00 FC 20 17 10 29 01 12 34 B6 E1 14");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5B AA BB CC DD 00 00 40 04 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 6F 00 FC 20 17 10 29 01 12 34 B6 E1 76");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5B AA BB CC DD 00 00 40 04 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 6F 00 FC 20 17 10 29 01 12 34 B6 E1 76");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5B AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 01 CC B0 70 00 FC 20 17 10 29 01 12 34 B6 E1 68");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5B AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 70 00 FC 20 17 10 29 01 12 34 B6 E1 69");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5B AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 71 00 FC 20 17 10 29 01 12 34 B6 E1 68");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5B AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 71 00 FC 20 17 10 29 01 12 34 B6 E1 68");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5B AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 71 00 FC 20 17 10 29 01 12 34 B6 E1 68");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5B AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 71 00 FC 20 17 10 29 01 12 34 B6 E1 68");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5B AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 71 00 FC 20 17 10 29 01 12 34 B6 E1 68");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5B AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 71 00 FC 20 17 10 29 01 12 34 B6 E1 68");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5B AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 71 00 FC 20 17 10 29 01 12 34 B6 E1 68");
        testMakingList.add("AA 01 00 40 00 00 00 00 00 03 55 5B AA BB CC DD 00 00 00 00 00 00 00 00 7F 5D 7F 5D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 CC B0 71 00 FC 20 17 10 29 01 12 34 B6 E1 68");


    }

    /************************************************************************************************************************/
}
