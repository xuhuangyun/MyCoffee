package com.fancoff.coffeemaker.ui.buyPage;

import android.content.Context;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.bean.PayBean;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.bean.coffe.SugarLevelBean;

/**
 * Created by apple on 2017/9/10.
 */

public class BuyPagePresent implements BuyPageContract.IBuyPagePresenter {
    BuyPageContract.IBuyPageView imainview;
    BuyPageContract.IBuyPageModel mainMode;

    Context mContext;

    /**BuyPageFragment类会调用该构造方法：
     * 1、会将buyPageFragment传给imainview
     * 2、会调用BuyPageMode的构造方法新建一个buyPageMode对象，并传给mainMode
     */
    public BuyPagePresent(BuyPageContract.IBuyPageView v) {
        imainview = v;
        mainMode = BuyPageMode.getIns();
    }

    /**
     * 实现BuyPageContract.IBuyPagePresenter的showDetail()方法
     * 购买界面的甜度、饮品名字、支付方式的显示：
     * 1、显示购买界面的甜度选择菜单
     * 2、coffeeBean赋给BuyPageMode的coffeeBean
     * 3、设置购买界面饮品的中英文名字
     * 4、支付方式的显示：如支付宝、微信、和包
     */
    @Override
    public void showDetail(CoffeeBean coffeeBean) {//传入单个饮品
        if (coffeeBean.getMake() != null) {//有饮品粉水参数
            SugarLevelBean sugarLevelBean = coffeeBean.getMake().getSugar_level();//糖
            if (sugarLevelBean != null) {
                int selet = 0;


                boolean show3 = sugarLevelBean.getMiddle() >= 0;
                boolean show4 = sugarLevelBean.getHigh() >= 0;
                boolean show2 = sugarLevelBean.getLow() >= 0;
                boolean show1 = sugarLevelBean.getNone() >= 0;
                if (show1) {
                    selet = 0;
                } else if (show2) {
                    selet = 1;
                } else if (show3) {
                    selet = 2;
                } else if (show4) {
                    selet = 3;
                }
                imainview.showRadioButton(show1,
                        show2,
                        show3,
                        show4, selet);
            }

        }
        mainMode.showDetail(coffeeBean);//coffeeBean赋给BuyPageMode的coffeeBean
        imainview.showName(coffeeBean.getName().getValue(), coffeeBean.getSub_name().getValue());
        imainview. showPayBtn(DataCenter.getInstance().isShowPayBtnAipay(),
                DataCenter.getInstance().isShowPayBtnWeiPay(),
                DataCenter.getInstance().isShowPayBtnHePay());

    }

    /**
     * 实现BuyPageContract.IBuyPagePresenter的reqGoodsOrder()方法
     * 1、调用V层BuyPageFragment的showProgress方法，显示"正在生成订单..."
     * 2、调用M层reqGoodsOrder：
     *       获得二维码成功：回收到PayBean
     *           隐藏progress对话框
     *           调用gotoView：发送消息到MAinActivity，主要用来启动payPageFragment
     *       获得二维码失败：回收到错误码、错误消息
     *           隐藏progress对话框
     *           Toast显示msg内容
     */
    @Override
    public void reqGoodsOrder(int paytype) {
        imainview.showProgress("正在生成订单...");
        mainMode.reqGoodsOrder(false, paytype, imainview.getSugarSelect(), new BuyPageContract.IBuyResultCallBack() {
            @Override
            public void onSuccess(PayBean goodsBean) {
                imainview.hideProgress();
                imainview.gotoView(goodsBean);
            }

            @Override
            public void onFailed(int code, String msg) {
                imainview.hideProgress();
                imainview.showToast(msg);

            }
        });

    }

    /**实现BuyPageContract.IBuyPagePresenter的reqGoodsOrder()方法*/
    @Override
    public void addCar() {
        mainMode.addCar(imainview.getSugarSelect(), new BuyPageContract.IAddCarResultCallBack() {
            @Override
            public void onResult(CoffeeBean g) {
                imainview.onAddSuccess();
            }
        });

    }

}
