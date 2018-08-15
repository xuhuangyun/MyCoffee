package com.fancoff.coffeemaker.ui.demo;

/**
 * Created by apple on 2017/9/10.
 */
/*
 *MVP模式示例代码
 */
public class DemoPageMode implements DemoPageContract.IDemoPageModel {


    @Override
    public void requestResult(DemoPageContract.IDemoResultCallBack cllback) {
        cllback.onResult("");
    }


}
