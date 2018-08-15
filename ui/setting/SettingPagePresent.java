package com.fancoff.coffeemaker.ui.setting;

import android.content.Context;

/**
 * Created by apple on 2017/9/10.
 */

public class SettingPagePresent implements SettingPageContract.ISettingPagePresenter {
    SettingPageContract.ISettingPageView imainview;
    SettingPageContract.ISettingPageModel mainMode;

    Context mContext;
    public SettingPagePresent(SettingPageContract.ISettingPageView v) {
        imainview= v;
        mainMode=new SettingPageMode();
    }


    @Override
    public void requestData() {
        mainMode.requestResult(new SettingPageContract.ISettingResultCallBack() {
            @Override
            public void onResult(String result) {
            }
        });


    }


}
