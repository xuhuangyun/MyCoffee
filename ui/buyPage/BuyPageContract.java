package com.fancoff.coffeemaker.ui.buyPage;

import com.fancoff.coffeemaker.bean.PayBean;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;

/**
 * Created by apple on 2017/10/8.
 */
/**
 * 接口及接口中抽象方法定义：
 * interface IBuyPageView
 * interface IBuyPagePresenter
 * interface IBuyResultCallBack
 * interface IAddCarResultCallBack
 * interface IBuyPageModel
 */
public class BuyPageContract {

    /**
     * V：视图层
     */
    interface IBuyPageView {

        int getSugarSelect();   //BuyPageFragment
        void showName(String name,String nameen);   //BuyPageFragment
        void gotoView(PayBean goodsBean);           //BuyPageFragment

        void onAddSuccess();            //BuyPageFragment
        void showProgress(String msg);  //BaseFragment
        void hideProgress();            //BaseFragment
        void showToast(String msg);     //BaseFragment
        void showRadioButton(boolean radioButton1,boolean radioButton2,boolean radioButton3,boolean radioButton4,int select);//BuyPageFragment
        void showPayBtn(boolean aipay,boolean weipay,boolean hepay);//BuyPageFragment

    }

    /**
     * P：视图与逻辑处理的连接层
     */
    interface IBuyPagePresenter {
        void showDetail(CoffeeBean coffeeBean);
        void reqGoodsOrder(int controlType);
        void addCar();
    }

    public interface IBuyResultCallBack {
        public void onSuccess(PayBean goodsBean);
        public void onFailed(int code, String msg);
    }
    interface IAddCarResultCallBack {
        public void onResult(CoffeeBean g);
    }

    /**
     * M：逻辑处理层
     */
    interface IBuyPageModel {
        void showDetail(CoffeeBean coffeeBean);//传入咖啡饮品，支付订单号为空
        void requestPayResult(IBuyResultCallBack cllback);
        void changePay(int paytype,BuyPageContract.IBuyResultCallBack iBuyResultCallBack);
        void reqGoodsOrder(boolean change,int controlType, int sugar,IBuyResultCallBack iBuyResultCallBack);

        void addCar(int sugarSelect,IAddCarResultCallBack callback);
    }

}
