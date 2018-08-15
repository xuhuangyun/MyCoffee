package com.fancoff.coffeemaker.ui.pay;

import android.content.Context;
import android.graphics.Bitmap;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.bean.PayBean;
import com.fancoff.coffeemaker.ui.buyPage.BuyPageContract;
import com.fancoff.coffeemaker.ui.buyPage.BuyPageMode;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by apple on 2017/9/10.
 */

public class PayPagePresent implements PayPageContract.IPayPagePresenter {
    PayPageContract.IPayPageView imainview;
    PayPageContract.IPayPageModel mainMode;
    BuyPageMode buyPageMode;
    Context mContext;

    public PayPagePresent(PayPageContract.IPayPageView v) {
        imainview = v;
        mainMode = new PayPageMode();
        buyPageMode = BuyPageMode.getIns();
    }

    /**
     * 请求支付数据：
     * 调用M层的requestResult：
     *     onResult:
     *        支付成功，调用V层的onPaySuccess：发送制作action到MAinActivity中
     *     onCancle:
     *        发送ACTION_BACK到MAinActivity中
     */
    @Override
    public void requestPayData(RxFragment f) {
        mainMode.requestResult(f, new PayPageContract.IPayResultCallBack() {
            @Override
            public void onResult(PayBean good, int resultCode, String messge) {
                imainview.onPaySucess(good);
            }

            @Override
            public void onCancle() {
                imainview.onBack();

            }
        });
    }

    /**
     * 解析商品信息：传入PayBean
     *   调用M层mainMode的pareseGoodsDetail方法：
     *   1、M层返回onResult：传入：选择的信息、选择信息英文、支付类型、支付类型英文
     *         V层imainview：UI显示这些信息；
     *         调用showchangebtn方法，对微信、支付宝、和包对应的view进行显示和隐藏；
     *   2、M层返回onResultBmp，传入二维码图片；调用V层的showbmp进行二维码图片显示；
     */
    @Override
    public void parseGoodsDetail(final PayBean goodsBean) {
        mainMode.parseGoodsDetail(goodsBean, new PayPageContract.IParseResultCallBack() {
            @Override
            public void onResult(String title, String titleen, String warn, String warnen) {
                imainview.showGoodDetail(title, titleen, warn, warnen);
                showchangebtn(goodsBean.getPayType(), imainview);
            }

            @Override
            public void onResultBmp(Bitmap bmp) {
                imainview.showbmp(bmp);
            }


        });
    }

    /**
     * 传入支付类型，V层imainview：
     * 如果选择了支付宝，则支付宝view隐藏，微信和和包view开启；
     * 调用V层imainview的showChangeBtnText进行view显示和隐藏；
     */
    private void showchangebtn(int type, PayPageContract.IPayPageView imainview) {
        boolean showAi = true;
        boolean showWei = true;
        boolean showHe = true;
        if (type == MyConstant.PAY.PAY_TYPE_AIPAY) {
            showAi = false;
        } else if (type == MyConstant.PAY.PAY_TYPE_WEIXIN) {
            showWei = false;
        } else if (type == MyConstant.PAY.PAY_TYPE_HE) {
            showHe = false;
        }
        imainview.showChangeBtnText(DataCenter.getInstance().isShowPayBtnAipay()?showAi:false,
                DataCenter.getInstance().isShowPayBtnWeiPay()?showWei:false,
                DataCenter.getInstance().isShowPayBtnHePay()?showHe:false);
    }

    /**
     * 取消支付：
     * 取消定时器，回收微信、支付宝、和包图片
     */
    @Override
    public void canclePay() {
//        imainview.showProgress("正在取消订单...");
        mainMode.canclePay();
    }

    @Override
    public void testPay() {
        mainMode.testPay();

    }

    /**
     * 改变支付方式：
     * 1、UI显示“正在切换支付方式...”
     * 2、调用buyPageMode下的改变支付方式，传入新的支付类型type和回调接口BuyPageContract.IBuyResultCallBack
     *      调用会回调本方法的onSuccess：返回的是PayBean
     *          M层的PayBean设置为goodsBean的支付码链接和支付类型
     *          V层的进度条隐藏
     *          V层的type类型的支付类型隐藏，其他显示；
     *          V层商品信息的显示、二维码图片的显示；
     *      onFailed：返回的是错误码和错误消息
     *          V层的进度条隐藏
     */
    @Override
    public void changePay(final int type) {
        imainview.showProgress("正在切换支付方式...");
        buyPageMode.changePay(type,new BuyPageContract.IBuyResultCallBack() {
            @Override
            public void onSuccess(PayBean goodsBean) {
                mainMode.getPaybean().setPayCode(goodsBean.getPayCode(type), type);
                mainMode.getPaybean().setPayType(type);
                mainMode.getPaybean().setOrders(goodsBean.getOrders());
                imainview.hideProgress();
                showchangebtn(type, imainview);
                parseGoodsDetail(mainMode.getPaybean());
            }

            @Override
            public void onFailed(int code, String msg) {
                imainview.hideProgress();
            }
        });

    }

}
