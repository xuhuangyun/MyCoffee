package com.fancoff.coffeemaker.ui.pay;

import android.graphics.Bitmap;

import com.fancoff.coffeemaker.bean.PayBean;
import com.fancoff.coffeemaker.ui.baseview.IBaseView;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by apple on 2017/10/8.
 */

public class PayPageContract {

    /**
     * V：视图层
     */
    interface IPayPageView extends IBaseView {
        void showGoodDetail(String title, String titleen, String warn, String warnen);
        void showbmp(Bitmap bmp);
        void onPaySucess(PayBean good);
        void showChangeBtnText(boolean showai,boolean showwei,boolean showHe);
    }

    /**
     * P：视图与逻辑处理的连接层
     */
    interface IPayPagePresenter {
        void requestPayData(RxFragment f);
        void parseGoodsDetail(PayBean goodsBean);
        void canclePay();
        void testPay();
        void changePay(int payType);

    }

    interface IPayResultCallBack {
        public void onResult(PayBean good,int resultCode,String messge);
        public void onCancle();
    }

    interface IParseResultCallBack {
        public void onResult(String title, String titleen, String warn, String warnen);
        public void onResultBmp(Bitmap bmp);

    }

    /**
     * M:逻辑处理层
     */
    interface IPayPageModel {
        void requestResult(RxFragment f,IPayResultCallBack cllback);
        void parseGoodsDetail(PayBean goodsBean, IParseResultCallBack cllback);
        void canclePay();
        void testPay();
        PayBean getPaybean();
    }

}
