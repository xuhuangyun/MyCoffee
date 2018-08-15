package com.fancoff.coffeemaker.ui.screen;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.bean.coffe.ImageBean;

import java.util.ArrayList;

/**
 * Created by apple on 2017/9/10.
 */

public class ScreenPageMode implements ScreenPageContract.IScreenPageModel {

    /**M层调用数据中心的屏保图片列表，返回回调给P层*/
    @Override
    public void requestResult(ScreenPageContract.IScreenResultCallBack cllback) {
        ArrayList<ImageBean> list = DataCenter.getInstance().getScreenBannerlist();
        cllback.onResult(list);
    }


}
