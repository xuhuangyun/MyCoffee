package com.fancoff.coffeemaker.ui.buyPage;

import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.bean.PayBean;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.net.BaseObserver;
import com.fancoff.coffeemaker.net.NetUtil;
import com.fancoff.coffeemaker.net.RequstBean.PayQrCodeBean;
import com.fancoff.coffeemaker.utils.GsonUtil;
import com.fancoff.coffeemaker.utils.StringUtil;
import com.fancoff.coffeemaker.utils.TimeUtil;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;

import java.util.ArrayList;

/**
 * Created by apple on 2017/9/10.
 */

/**
 * Mode层
 *
 */
public class BuyPageMode implements BuyPageContract.IBuyPageModel {
    CoffeeBean coffeeBean;
    PayBean payBean;
    static BuyPageMode buyPageMode;

    /**
     * BuyPagePresent会调用该构造方法，构造方法新建一个buyPageMode对象，并传给BuyPagePresent；
     */
    public static BuyPageMode getIns() {
        if (buyPageMode == null) {
            buyPageMode = new BuyPageMode();
        }
        return buyPageMode;

    }

    /**
     * 本类对象的支付订单号为空；
     * 传入的咖啡饮品给本类对象的咖啡饮品；
     */
    @Override
    public void showDetail(CoffeeBean coffeeBean) {
        setPayOrderNo("");//本类对象的支付订单号为空
        this.coffeeBean = coffeeBean;

    }

    @Override
    public void requestPayResult(BuyPageContract.IBuyResultCallBack cllback) {

    }

    /**
     * 改变支付方式：传入支付类型，回调接口iBuyResultCallBack；
     *
     */
    @Override
    public void changePay(int paytype, final BuyPageContract.IBuyResultCallBack iBuyResultCallBack) {

        reqGoodsOrder(true, paytype, 0, iBuyResultCallBack);
    }

    String payOrderNo;//支付订单号

    /**传入支付订单号到本类对象的支付订单号*/
    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    /**
     * 获取支付订单号payOrderNo：
     *   payOrderNo为空字符：返回当前时间；
     *   payOrderNo为非空字符：返回payOrderNo；
     */
    public String getPayOrderNo() {
        if (StringUtil.isStringEmpty(payOrderNo)) {
            this.payOrderNo = TimeUtil.getInstance().getNowTime();
        }
        return payOrderNo;
    }

    String text = "{\"qrcode\":\"http://m.fancoff.com/Order/DeviceOrder/Pay?id=565aXmfTnOTm0Mfnjn0kPCd5%2bpuYFQXwxKj50Ug78SI%3d\",\"goods\":[{\"id\":3,\"order_id\":224830}],\"return_code\":1}";

    /**
     * 请求商品二维码：传入参数change；支付类型paytype；甜度sugar；回调接口iBuyResultCallBack；
     * 1、change=false：
     *       payBean设置支付订单号payOrderNo（有支付订单号则返回支付订单号；没有返回当前时间）；
     *       payBean设置支付类型为paytype；
     *       coffeeBean设置甜度sugar；
     *       ArrayList<CoffeeBean> goods添加coffeeBean；
     *       payBean增加商品（商品订单）
     *    change=true：
     *       打印日志；
     * 2、根据paytype判断weipayCode、aipayCode、hePayCode是否为空：
     *    空：
     *       请求二维码，实现BaseObserver接口方法：
     *           onHandleSuccess：
     *                二维码链接、商品信息，传入payBean；
     *                回调：iBuyResultCallBack.onSuccess(payBean);
     *           onHandleError：
     *                回调：iBuyResultCallBack.onFailed(code, msg);
     *    非空：回调iBuyResultCallBack.onSuccess(payBean);
     *
     * BuyPagePresent类的reqGoodsOrder方法会实现IBuyResultCallBack的onSuccess、onFailed方法；
     * P层调用M层，M层反馈给P层；
     * PayPagePresent类的changePay会最终调用该方法，该方法完成后将回调它的onSuccess(payBean);和onFailed(code, msg);
     */
    @Override
    public void reqGoodsOrder(final boolean change, final int paytype, int sugar, final BuyPageContract.IBuyResultCallBack iBuyResultCallBack) {
        if (!change) {//false
            LogUtil.action("paytype：" + paytype);
            payBean = new PayBean();
            payBean.setOut_trade_no(getPayOrderNo());//订单号
            payBean.setPayType(paytype);
            ArrayList<CoffeeBean> goods = new ArrayList<>();
            coffeeBean.setSugarType(sugar);
            goods.add(coffeeBean);
            payBean.setGoods(goods);
        } else {
            LogUtil.action("change paytype ：" + paytype);
        }
        if (StringUtil.isStringEmpty(payBean.getPayCode(paytype))) {//weipayCode、aipayCode、hePayCode为空
            NetUtil.getInstance().actionQrCode(payBean, paytype, new BaseObserver<PayQrCodeBean>() {
                @Override
                public void onHandleSuccess(PayQrCodeBean pay) {
                    LogUtil.debugNet("get QrCode succeess：" + pay.toString());
                    //DEBUG_OKHTTP:get QrCode succeess：
                    //PayQrCodeBean{qrcode='http://m.fancoff.com/Order/DeviceOrder/Pay?id=uBYDO9W7B9PMhTzGcNbFJuTotIgnjrwfuaXFxefxtxI%3d',
                    //goods=[Order{sale_time='20180702113133', _id=null, id=16, order_id=246982}]}
                    payBean.setPayCode(pay.getQrcode(), paytype);//二维码链接和支付类型传入
                    payBean.setOrders(pay.getGoods());//订单商品
                    iBuyResultCallBack.onSuccess(payBean);
                }

                @Override
                public void onHandleError(int code, String msg) {
                    super.onHandleError(code, msg);
                    if (MyApp.NONET) {
                        PayQrCodeBean pay = GsonUtil.getGson().fromJson(text, PayQrCodeBean.class);
                        payBean.setPayCode(pay.getQrcode(), paytype);
                        payBean.setOrders(pay.getGoods());
                        iBuyResultCallBack.onSuccess(payBean);
                    } else {
                        LogUtil.debugNet("get QrCode failed：" + msg);
                        iBuyResultCallBack.onFailed(code, msg);
                    }

                }
            });
        } else {//非空，打印日志，并回调商品支付码成功
            LogUtil.debugNet("get QrCode succeess from disk:" + payBean.getPayCode());
            iBuyResultCallBack.onSuccess(payBean);
        }


    }


    @Override
    public void addCar(int sugarSelect, BuyPageContract.IAddCarResultCallBack callback) {
        callback.onResult(coffeeBean);

    }


}
