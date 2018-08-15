package com.fancoff.coffeemaker.ui.pickup;

import com.fancoff.coffeemaker.bean.PayBean;

/**
 * Created by apple on 2017/9/10.
 */

public class PickPageMode implements PickPageContract.IBuyPageModel {

    /**返回PayBean*/
    @Override
    public void requestPayResult(PickPageContract.IPickResultCallBack cllback) {
        PayBean payBean=new PayBean();
        cllback.onResult(payBean);
    }


}
