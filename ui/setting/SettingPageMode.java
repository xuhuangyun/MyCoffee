package com.fancoff.coffeemaker.ui.setting;

/**
 * Created by apple on 2017/9/10.
 */

public class SettingPageMode implements SettingPageContract.ISettingPageModel {


    @Override
    public void requestResult(SettingPageContract.ISettingResultCallBack cllback) {
        cllback.onResult("");
    }


}
