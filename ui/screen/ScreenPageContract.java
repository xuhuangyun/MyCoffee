package com.fancoff.coffeemaker.ui.screen;

import com.fancoff.coffeemaker.bean.coffe.ImageBean;

import java.util.ArrayList;

/**
 * Created by apple on 2017/10/8.
 */

public class ScreenPageContract {

    /**
     * V视图层
     */
    interface IScreenPageView {
        void showBanner(ArrayList<ImageBean> list);
        void onBack();

    }

    /**
     * P视图与逻辑处理的连接层
     */
    interface IScreenPagePresenter {
        void requestData();
    }

    interface IScreenResultCallBack {
        public void onResult(ArrayList<ImageBean> list);
    }

    /**
     * 逻辑处理层
     */
    interface IScreenPageModel {
        void requestResult(IScreenResultCallBack cllback);
    }

}
