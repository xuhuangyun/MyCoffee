package com.fancoff.coffeemaker.ui.setting;

/**
 * Created by apple on 2017/10/8.
 */

public class SettingPageContract {

    /**
     * V视图层
     */
    interface ISettingPageView {
        void onBack();

    }

    /**
     * P视图与逻辑处理的连接层
     */
    interface ISettingPagePresenter {
        void requestData();
    }

    interface ISettingResultCallBack {
        public void onResult(String result);
    }

    /**
     * 逻辑处理层
     */
    interface ISettingPageModel {
        void requestResult(ISettingResultCallBack cllback);
    }

}
