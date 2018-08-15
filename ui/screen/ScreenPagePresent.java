package com.fancoff.coffeemaker.ui.screen;

import android.content.Context;

import com.fancoff.coffeemaker.bean.coffe.ImageBean;

import java.util.ArrayList;

/**
 * Created by apple on 2017/9/10.
 */

public class ScreenPagePresent implements ScreenPageContract.IScreenPagePresenter {
    ScreenPageContract.IScreenPageView imainview;
    ScreenPageContract.IScreenPageModel mainMode;

    Context mContext;
    public ScreenPagePresent(ScreenPageContract.IScreenPageView v) {
        imainview= v;
        mainMode=new ScreenPageMode();
    }

    /**
     * 调用M层的requestResult，实例化一个接口方法；M层执行后会回调这个实例化的方法，传屏保图片列表数据给P层
     *    P层收到屏保列表后，调用V层的showBanner（屏保图片轮播）
     */
    @Override
    public void requestData() {
        mainMode.requestResult(new ScreenPageContract.IScreenResultCallBack() {
            @Override
            public void onResult(ArrayList<ImageBean> result) {
                imainview.showBanner(result);
            }
        });


    }


}
