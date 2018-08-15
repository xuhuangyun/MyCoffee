package com.fancoff.coffeemaker.ui.Imageshow;

import android.content.Context;

/**
 * Created by apple on 2017/9/10.
 */

public class ImageShowPresent implements ImageShowContract.IDemoPagePresenter {
    ImageShowContract.IDemoPageView imainview;
    ImageShowContract.IDemoPageModel mainMode;

    /**ImageShowFragment类的initData会新建实例对象*/
    Context mContext;
    public ImageShowPresent(ImageShowContract.IDemoPageView v) {
        imainview= v;
        mainMode=new ImageShowMode();
    }


    /**调用M层的requestResult，M层会回调P层的onResult方法*/
    @Override
    public void requestData() {
        mainMode.requestResult(new ImageShowContract.IDemoResultCallBack() {
            @Override
            public void onResult(String result) {
            }
        });


    }


}
