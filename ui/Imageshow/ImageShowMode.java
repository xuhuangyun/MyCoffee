package com.fancoff.coffeemaker.ui.Imageshow;

/**
 * Created by apple on 2017/9/10.
 */

public class ImageShowMode implements ImageShowContract.IDemoPageModel {


    /**M层返回一个“”空字符给P层的requestResult*/
    @Override
    public void requestResult(ImageShowContract.IDemoResultCallBack cllback) {
        cllback.onResult("");
    }


}
