package com.fancoff.coffeemaker.ui.logo;

/**
 * Created by apple on 2017/9/10.
 */

public class LogoPageMode implements LogoPageContract.ILogoPageModel {


    @Override
    public void requestResult(LogoPageContract.ILogoResultCallBack cllback) {
        cllback.onResult("");
    }


}
