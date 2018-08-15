package com.fancoff.coffeemaker.ui.logo;

import android.content.Context;

/**
 * Created by apple on 2017/9/10.
 */

public class LogoPagePresent implements LogoPageContract.ILogoPagePresenter {
    LogoPageContract.ILogoPageView imainview;
    LogoPageContract.ILogoPageModel mainMode;

    Context mContext;
    public LogoPagePresent(LogoPageContract.ILogoPageView v) {
        imainview= v;
        mainMode=new LogoPageMode();
    }


    @Override
    public void requestData() {
        mainMode.requestResult(new LogoPageContract.ILogoResultCallBack() {
            @Override
            public void onResult(String result) {
            }
        });


    }


}
