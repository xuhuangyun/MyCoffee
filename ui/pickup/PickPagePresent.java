package com.fancoff.coffeemaker.ui.pickup;

import android.content.Context;

import com.fancoff.coffeemaker.bean.PayBean;

/**
 * Created by apple on 2017/9/10.
 */

public class PickPagePresent implements PickPageContract.IPickPagePresenter {
    PickPageContract.IPickPageView imainview;
    PickPageContract.IBuyPageModel mainMode;

    Context mContext;
    public PickPagePresent(PickPageContract.IPickPageView v) {
        imainview= v;
        mainMode=new PickPageMode();
    }

    /**
     * 调用M层的requestPayResult,M层返回PayBean；
     *    调用V层制作饮品、V层onBack
     */
    @Override
    public void requestData(String code) {
        mainMode.requestPayResult(new PickPageContract.IPickResultCallBack() {
            @Override
            public void onResult(PayBean payBean) {
                imainview.gotoMaking(payBean);
                imainview.onBack();
            }
        });


    }


}
