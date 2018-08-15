package com.fancoff.coffeemaker.ui.Imageshow;

/**
 * Created by apple on 2017/10/8.
 */

public class ImageShowContract {

    /**
     * V视图层
     */
    interface IDemoPageView {
        void onBack();

    }

    /**
     * P视图与逻辑处理的连接层
     */
    interface IDemoPagePresenter {
        void requestData();
    }

    interface IDemoResultCallBack {
        public void onResult(String result);
    }

    /**
     * 逻辑处理层
     */
    interface IDemoPageModel {
        void requestResult(IDemoResultCallBack cllback);
    }

}
