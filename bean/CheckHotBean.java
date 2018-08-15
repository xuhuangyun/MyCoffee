package com.fancoff.coffeemaker.bean;

import com.fancoff.coffeemaker.io.CMDUtil;

/**
 * Created by apple on 2018/5/9.
 */

/**
 * 1、CheckHotBean实例checkHotBean;
 * 2、boolean isHot; 同步读写isHot；
 * 3、vmc加热开关没打开，没有缺水；则进行加热
 */
public class CheckHotBean {
    static CheckHotBean checkHotBean;

    public static CheckHotBean getIns() {
        if (checkHotBean == null) {
            checkHotBean = new CheckHotBean();
        }
        return checkHotBean;
    }

    boolean isHot = true;

    /**根据传入参数设置本类的isHot状态*/
    public void setHot(boolean hot) {
        synchronized ("hot") {
            isHot = hot;
        }
    }

    /**
     *  vmc加热开关没打开，没有缺水；则进行加热
     */
    public void sendHotCMD() {
        synchronized ("hot") {
            if (!VMCState.getIns().isNull()
                    && isHot != VMCState.getIns().isHotOn()
                    && !VMCState.getIns().isVmcErrorBit9()) {
                CMDUtil.getInstance().sethot(isHot, null);//加热状态
            }

        }
    }


    public boolean isHot() {
        synchronized ("hot") {
            return isHot;
        }
    }
}
