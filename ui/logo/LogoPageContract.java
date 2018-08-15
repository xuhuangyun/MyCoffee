package com.fancoff.coffeemaker.ui.logo;

/**
 * Created by apple on 2017/10/8.
 */

public class LogoPageContract {

    /**
     * V：视图层
     */
    interface ILogoPageView {
        void onBack();

    }

    /**
     * P：视图与逻辑处理的连接层
     */
    interface ILogoPagePresenter {
        void requestData();
    }

    interface ILogoResultCallBack {
        public void onResult(String result);
    }

    /**
     * M：逻辑处理层
     */
    interface ILogoPageModel {
        void requestResult(ILogoResultCallBack cllback);
    }

}
