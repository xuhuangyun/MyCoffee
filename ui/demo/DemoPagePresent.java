package com.fancoff.coffeemaker.ui.demo;

import android.content.Context;

/**
 * Created by apple on 2017/9/10.
 */
/*
 *MVP模式示例代码
 */
public class DemoPagePresent implements DemoPageContract.IDemoPagePresenter {
    DemoPageContract.IDemoPageView imainview;
    DemoPageContract.IDemoPageModel mainMode;

    Context mContext;
    public DemoPagePresent(DemoPageContract.IDemoPageView v) {
        imainview= v;
        mainMode=new DemoPageMode();
    }


    @Override
    public void requestData() {
        mainMode.requestResult(new DemoPageContract.IDemoResultCallBack() {
            @Override
            public void onResult(String result) {
            }
        });


    }


}
