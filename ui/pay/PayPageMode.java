package com.fancoff.coffeemaker.ui.pay;

import android.graphics.Bitmap;

import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.MakingStatebean;
import com.fancoff.coffeemaker.bean.PayBean;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.io.CMDUtil;
import com.fancoff.coffeemaker.io.IOConstans;
import com.fancoff.coffeemaker.io.data.SendByteBuilder;
import com.fancoff.coffeemaker.net.NetUtil;
import com.fancoff.coffeemaker.net.RequstBean.PayResultBean;
import com.fancoff.coffeemaker.net.RetrofitClient;
import com.fancoff.coffeemaker.ui.making.MakingPageMode;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.xZingUtil;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;


/**
 * Created by apple on 2017/9/10.
 */

public class PayPageMode implements PayPageContract.IPayPageModel {
    PayBean paybean;
    PayPageContract.IPayResultCallBack cllback;


    /**
     * 请求M层的结果，会传入PayPageContract.IPayResultCallBack cllback参数
     */
    @Override
    public void requestResult(RxFragment f, final PayPageContract.IPayResultCallBack cllback) {
        if (timer != null) {
            return;
        }
        this.cllback = cllback;
        reqinterval();
    }

    /**取消定时器timer，打印日志： DEBUG_ACTION：end---request pay state*/
    private void cancleTIme() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            LogUtil.action("end---request pay state");
        }
    }

    boolean text;
    Timer timer;
    boolean first = true;
    Call<PayResultBean> ob;

    /**
     *  1、取消定时器timer，打印日志：start---request pay state
     *  2、新建定时器timer，开启1000ms执行一次的定时任务：
     *      1、first=true：打印日志：run---request pay state
     *      2、text=true：
     *            组合一串制作咖啡的命令
     *            设置MakingPageMode的makingStatebean
     *            将制作咖啡命令的任务添加到writeList任务列表；
     *            回调PayPagePresent.requestPayData的onResult：传入PayBean，1，“”
     *         text=false:
     *            发送支付请求状态，等待回调payCallBack.onsuccess(tasks);
     *               如果返回状态为1：打印日志：pay success
     *               制作指令添加到任务列表
     *               回调P层的requestPayData的onResult：传入参数PayBean，result，“”
     */
    public void reqinterval() {
        cancleTIme();
        LogUtil.action("start---request pay state");
        first = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (first) {
                    LogUtil.action("run---request pay state");
                }
                first = false;
                if (text) {//估计是测试用的
                    CoffeeBean coffeeBean = paybean.getGoods().get(0);//第0号饮品
                    final byte[] bytes = SendByteBuilder.build().creatByteArray(
                            IOConstans.OPTION.OPTION_MAKING,
                            coffeeBean.getMakigBodyByte());//组合一串制作饮品的命令
                    MakingPageMode.getIns().setMakingStatebean(new MakingStatebean(paybean, coffeeBean.getMake_duration()));
                    CMDUtil.getInstance().maikeCoffee(bytes, null);//将制作咖啡命令添加到writeList任务；
                    cllback.onResult(paybean, 1, "");
                    cancleTIme();
                } else {
                    ob = NetUtil.getInstance().actionPayResult(paybean, new RetrofitClient.PayCallBack() {
                        //回调：payCallBack.onsuccess(tasks);
                        @Override
                        public void onsuccess(PayResultBean payResultBean) {

                            if (payResultBean != null) {
                                if (payResultBean.getIsPaySuccess()) {//有返回状态
                                    LogUtil.debugNet("pay success");
                                    MakingPageMode.getIns().startMaking(paybean);//制作指令添加到任务列表
                                    cllback.onResult(paybean, payResultBean.getReturn_code(), "");
                                    cancleTIme();
                                }
                            } else {
                                LogUtil.debugNet("pay failed");
                            }

                        }
                    });

                }

            }
        }, 0, 1000);
    }


    /**返回string类型 无糖、低糖、中糖、高糖*/
    public String getSugar(int sugerType) {
        if (sugerType == MyConstant.SUGAR.SUGAR_NO) {
            return MyApp.getIns().getResources().getString(R.string.nosugarzn);
        } else if (sugerType == MyConstant.SUGAR.SUGAR_LOW) {
            return MyApp.getIns().getResources().getString(R.string.lowsugarzn);
        } else if (sugerType == MyConstant.SUGAR.SUGAR_MIDDLE) {
            return MyApp.getIns().getResources().getString(R.string.middlesugarzn);
        } else if (sugerType == MyConstant.SUGAR.SUGAR_HIGHT) {
            return MyApp.getIns().getResources().getString(R.string.hightsugarzn);
        } else {
            return MyApp.getIns().getResources().getString(R.string.nosugarzn);
        }

    }

    /**string类型的no sugar、low sugar、middle sugar、high sugar*/
    public String getSugarEn(int sugerType) {
        if (sugerType == MyConstant.SUGAR.SUGAR_NO) {
            return MyApp.getIns().getResources().getString(R.string.nosugaren);
        } else if (sugerType == MyConstant.SUGAR.SUGAR_LOW) {
            return MyApp.getIns().getResources().getString(R.string.lowsugaren);
        } else if (sugerType == MyConstant.SUGAR.SUGAR_MIDDLE) {
            return MyApp.getIns().getResources().getString(R.string.middlesugaren);
        } else if (sugerType == MyConstant.SUGAR.SUGAR_HIGHT) {
            return MyApp.getIns().getResources().getString(R.string.hightsugaren);
        } else {
            return MyApp.getIns().getResources().getString(R.string.nosugaren);
        }


    }

    /**string类型的支付宝支付、微信支付、和包支付*/
    public String getPay(int type) {
        if (type == MyConstant.PAY.PAY_TYPE_AIPAY) {
            return MyApp.getIns().getResources().getString(R.string.alipay);
        } else if (type == MyConstant.PAY.PAY_TYPE_WEIXIN) {
            return MyApp.getIns().getResources().getString(R.string.weixinpay);
        } else if (type == MyConstant.PAY.PAY_TYPE_HE) {
            return MyApp.getIns().getResources().getString(R.string.hepay);
        } else {
            return MyApp.getIns().getResources().getString(R.string.alipay);
        }
    }
    /**string类型的 alipay to pay、weChat to pay、he to pay*/
    public String getPayen(int type) {
        if (type == MyConstant.PAY.PAY_TYPE_AIPAY) {
            return MyApp.getIns().getResources().getString(R.string.alipayen);
        } else if (type == MyConstant.PAY.PAY_TYPE_WEIXIN) {
            return MyApp.getIns().getResources().getString(R.string.weixinpayen);
        } else if (type == MyConstant.PAY.PAY_TYPE_HE) {
            return MyApp.getIns().getResources().getString(R.string.hepayen);
        } else {
            return MyApp.getIns().getResources().getString(R.string.alipayen);
        }
    }

    /**根据类型返回对应的drawable中图片id*/
    public int getPayenDr(int type) {
        if (type == MyConstant.PAY.PAY_TYPE_AIPAY) {
            return R.drawable.ico_zhifubao;
        } else if (type == MyConstant.PAY.PAY_TYPE_WEIXIN) {
            return R.drawable.ico_weixin;
        } else if (type == MyConstant.PAY.PAY_TYPE_HE) {
            return R.drawable.cf_hepay;
        } else {
            return R.drawable.ico_zhifubao;
        }
    }

    Bitmap bhe;
    Bitmap bWx;
    Bitmap bAi;

    /**
     * 解析商品信息：传入PayBean，接口PayPageContract.IParseResultCallBack对象
     * 1、回调P层的onRessult：选择的信息、选择信息英文、支付类型、支付类型英文
     * 2、runTask任务：parseGoodsDetail:
     *      run:
     *         从PayBean获取二维码code，并打印日志show QrCode + codes
     *         根据PayBean的支付类型：选择获取对应的二维码；
     *      ok:
     *         根据PayBean的支付类型，返回对应的二维码图片；
     */
    @Override
    public void parseGoodsDetail(final PayBean goodsBean, final PayPageContract.IParseResultCallBack cllback) {
        this.paybean = goodsBean;
        cllback.onResult(
                "您选择了 " + goodsBean.getGoods().get(0).getName().getValue() + " " + getSugar(goodsBean.getGoods().get(0).getSugarType()) + " 请完成支付",
                "You chose " + goodsBean.getGoods().get(0).getSub_name().getValue()
                        + " " + getSugarEn(goodsBean.getGoods().get(0).getSugarType())
                        + "\nPlease scan the QR code finish your order",
                getPay(goodsBean.getPayType()), getPayen(goodsBean.getPayType()));

        MyApp.getIns().runTask("parseGoodsDetail", new MyApp.TaskRun() {
            @Override
            public void run() {

                String codes = goodsBean.getPayCode();
                LogUtil.action("show QrCode:" + codes);
                if (goodsBean.getPayType() == MyConstant.PAY.PAY_TYPE_AIPAY) {
                    if (bAi == null) {
                        bAi = xZingUtil.encodeAsBitmap(codes, getPayenDr(goodsBean.getPayType()));
                    }

                } else if (goodsBean.getPayType() == MyConstant.PAY.PAY_TYPE_WEIXIN) {
                    if (bWx == null) {
                        bWx = xZingUtil.encodeAsBitmap(codes, getPayenDr(goodsBean.getPayType()));
                    }

                } else if (goodsBean.getPayType() == MyConstant.PAY.PAY_TYPE_HE) {
                    if (bhe == null) {
                        bhe = xZingUtil.encodeAsBitmap(codes, getPayenDr(goodsBean.getPayType()));
                    }

                }
            }

            @Override
            protected void onOk() {
                if (goodsBean.getPayType() == MyConstant.PAY.PAY_TYPE_AIPAY) {
                    cllback.onResultBmp(bAi);

                } else if (goodsBean.getPayType() == MyConstant.PAY.PAY_TYPE_WEIXIN) {
                    cllback.onResultBmp(bWx);

                } else if (goodsBean.getPayType() == MyConstant.PAY.PAY_TYPE_HE) {
                    cllback.onResultBmp(bhe);

                }

            }
        });

    }

    public PayBean getPaybean() {
        return paybean;
    }

    public void setPaybean(PayBean paybean) {
        this.paybean = paybean;
    }

    /**
     * 取消支付：
     *  Call<PayResultBean> ob 不为空，取消Call
     *  取消定时器timer，打印日志： DEBUG_ACTION：end---request pay state
     *  支付宝图片、微信图片、和包图片不为空或没有回收，则进行回收并设为空
     */
    @Override
    public void canclePay() {
        if (ob != null) {
            ob.cancel();
        }
        cancleTIme();
        if (bWx != null && !bWx.isRecycled()) {
            bWx.recycle();
            bWx = null;
        }
        if (bhe != null && !bhe.isRecycled()) {
            bhe.recycle();
            bhe = null;
        }
        if (bAi != null && !bAi.isRecycled()) {
            bAi.recycle();
            bAi = null;
        }


    }

    @Override
    public void testPay() {
        text = true;
    }


}
