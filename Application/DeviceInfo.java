package com.fancoff.coffeemaker.Application;

import com.fancoff.coffeemaker.utils.StringUtil;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.rx.RxDeviceTool;
import com.fancoff.coffeemaker.utils.rx.RxSPTool;

/**
 * Created by apple on 2017/11/17.
 * 机器信息类
 */

public class DeviceInfo {
    String deviceId;
    boolean regist = true;
    String returnMsd = "";

    /**服务器返回的msg*/
    public String getReturnMsd() {
        return returnMsd;
    }

    public void setReturnMsd(String returnMsd) {
        this.returnMsd = returnMsd;
    }

    /**返回是否注册  regist*/
    public boolean isRegist() {
        return regist;
    }

    public void setRegist(boolean regist) {
        this.regist = regist;
    }

    /**
     * 获得咖啡机ID。12位数字或字母（可以用MAC地址或自定义字符串）
     *
     * */
    public String getDeviceId() {


        if (MyApp.testMac) {
            return "AABBCCDDEEFF";
//            return "442C05DA30A6";
//            return "CCB8A82E32FA";
        } else {
            if (isNunnMAc(deviceId)) {//deviceID为空
                try {
                    deviceId = getDeviceID();
                    LogUtil.debug("deviceId：" + deviceId);
                } catch (Exception e) {
                    LogUtil.error(e.toString());
                }
            }
            return deviceId;
        }
    }

    /**
     *  MyApp.MAC=true：获取MAC；
     *  MyApp.MAC=false：获取android id
     */
    private String getDeviceID() {
        String androidId = "";
        if (MyApp.MAC) {
            androidId = RxDeviceTool.getMacAddresseth0().replace("\\:", "").replace(":", "").toUpperCase();
        } else {
            androidId = RxDeviceTool.getAndroidId(MyApp.getIns());
        }
        return androidId;
    }

    /**mac为空或者为020000000000、000000000000*/
    private boolean isNunnMAc(String mac) {
        return StringUtil.isStringEmpty(mac)
                || mac.equals("020000000000")
                || mac.equals("000000000000");

    }

    /*主控板的软件版本号*/
    public String getController_version() {
        return "178";
    }

    private static final DeviceInfo ourInstance = new DeviceInfo();

    public static DeviceInfo getInstance() {
        return ourInstance;
    }

    private DeviceInfo() {

    }

    String systemId;

    public void setLocalSystemId(String systemId) {
        RxSPTool.putString(MyApp.getIns(), "systemId", systemId);
        this.systemId = systemId;
    }

    public String getSystemId() {
        return systemId;
    }

    public String getLocalSystemId() {
        if (StringUtil.isStringEmpty(systemId)) {
            return RxSPTool.getString(MyApp.getIns(), "systemId");
        } else {
            return systemId;
        }
    }


}
