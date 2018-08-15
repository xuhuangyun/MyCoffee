package com.fancoff.coffeemaker.ui.pickup;

import com.fancoff.coffeemaker.bean.PayBean;

/**
 * Created by apple on 2017/10/8.
 */

public class PickPageContract {

    /**
     * V：视图层
     */
    interface IPickPageView {
        String gotoMaking(PayBean payBean);
        void onBack();
    }

    /**
     * P：视图与逻辑处理的连接层
     */
    interface IPickPagePresenter {
        void requestData(String code);
    }

    interface IPickResultCallBack {
        public void onResult(PayBean payBean);
    }

    /**
     * M：逻辑处理层
     */
    interface IBuyPageModel {
        void requestPayResult(IPickResultCallBack cllback);
    }

}
