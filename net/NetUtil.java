package com.fancoff.coffeemaker.net;

import com.fancoff.coffeemaker.Application.DeviceInfo;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.bean.PayBean;
import com.fancoff.coffeemaker.bean.heatbit.HeatParams;
import com.fancoff.coffeemaker.net.RequstBean.CoffeeConfigResultBean;
import com.fancoff.coffeemaker.net.RequstBean.HeadBeatBean;
import com.fancoff.coffeemaker.net.RequstBean.Order;
import com.fancoff.coffeemaker.net.RequstBean.PayQrCodeBean;
import com.fancoff.coffeemaker.net.RequstBean.PayResultBean;
import com.fancoff.coffeemaker.net.RequstBean.PickByCodeBean;
import com.fancoff.coffeemaker.net.RequstBean.PickByNumberBean;
import com.fancoff.coffeemaker.net.RequstBean.PickQrCodeBean;
import com.fancoff.coffeemaker.net.RequstBean.RegistBean;
import com.fancoff.coffeemaker.net.RequstBean.TestBean;
import com.fancoff.coffeemaker.net.RequstBean.UploadDatasBean;
import com.fancoff.coffeemaker.net.RequstBean.VersionBean;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.StringUtil;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import retrofit2.Call;


/**
 * Created by apple on 2017/11/17.
 * http接口调用类
 */

public class NetUtil {
    String noRegishtError = "机器未注册";
    DeviceInfo commonUtil = DeviceInfo.getInstance();
    RequestBodyManage reqInfo = RequestBodyManage.getInstance();

    public void setRegistBean(RegistBean registBean) {
        this.registBean = registBean;
    }

    private RegistBean registBean;

    public boolean isRegist() {
        return registBean != null && registBean.getIsSuccess();
    }


    private static NetUtil ourInstance = new NetUtil();

    public static NetUtil getInstance() {
        return ourInstance;
    }


    private NetUtil() {

    }

    //测试
    public <T> void actioTest(Observer<TestBean> obser) {
        JSONObject jsonObject = reqInfo.getTestJson();
        RetrofitClient.getInstance().test(jsonObject, obser);
    }

    public interface RegistCallBack {
        void onCallBack(boolean isRegist);
    }

    /**
     * 注册
     * 1、没有注册：
     *   1.1、设备ID为空：
     *      日志打印no mac address；
     *      registCallBack.onCallBack(false);谁调用actionRegist方法，谁实现该接口方法
     *      DeviceInfo.regist = false;
     *      返回
     *   1.2、获得注册json数据包；
     *        retrofit+okhttp+rxjava发送json数据包，并监听服务器返回的数据：
     *            返回的return_code=1:
     *                                服务器返回的RegistBean传入本类的RegistBean对象registBean；
     *                                DeviceInfo.regist=true;DeviceInfo.returnMsd="";
     *                                打印日志：regist success；
     *                                registCallBack.onCallBack(true);谁调用actionRegist方法，谁实现该接口方法
     *                  return_code!=1:
     *                                DeviceInfo.regist=false;
     *                                   return_code=0:DeviceInfo.returnMsd=设备未注册(mac地址或设备ID)
     *                                   return_code!=0:DeviceInfo.returnMsd=msg（系统返回的msg）
     *                                打印日志:"regist failed:" + msgerr
     *                                registCallBack.onCallBack(false);谁调用actionRegist方法，谁实现该接口方法
     * 2、已经注册：
     *        registCallBack.onCallBack(true);谁调用actionRegist方法，谁实现该接口方法
     */
    public void actionRegist(final RegistCallBack registCallBack) {
        if (!isRegist()) {//还没有注册
            if (StringUtil.isStringEmpty(DeviceInfo.getInstance().getDeviceId())) {//设备ID为空
                String err = "no mac address";
                LogUtil.debugNet(err);
                registCallBack.onCallBack(false);//谁调用actionRegist方法，谁实现该接口方法
                DeviceInfo.getInstance().setRegist(false);//DeviceInfo.regist = false;
                DeviceInfo.getInstance().setReturnMsd(err);//DeviceInfo.returnMsd=err
                return;
            }
            JSONObject jsonObject = reqInfo.getRegistJson();  //获得注册json数据包
            RetrofitClient.getInstance().regist(jsonObject, new BaseObserver<RegistBean>() {
                @Override
                public void onHandleSuccess(RegistBean registBean) {//服务器返回的return_code=1;代表注册成功了
                    setRegistBean(registBean);  //服务器返回的RegistBean传入本类的RegistBean对象registBean；
                    DeviceInfo.getInstance().setRegist(true);//DeviceInfo.regist=true;
                    DeviceInfo.getInstance().setReturnMsd("");//DeviceInfo.returnMsd="";
                    LogUtil.debugNet("regist success");//日志打印注册成功
                    registCallBack.onCallBack(true);//谁调用actionRegist方法，谁实现该接口方法
                }

                @Override
                public void onHandleError(int code, String msg) {//服务器返回的return_code!=1;代表注册失败了
                    DeviceInfo.getInstance().setRegist(false);
                    String msgerr = "";
                    if (code == 0) {
                        if(MyApp.MAC){
                            msgerr = "设备未注册\n(mac地址：" + DeviceInfo.getInstance().getDeviceId() +
                                    ")";
                        }else{
                            msgerr = "设备未注册\n(设备id：" + DeviceInfo.getInstance().getDeviceId() +
                                    ")";
                        }

                        DeviceInfo.getInstance().setReturnMsd(msgerr);
                    } else {
                        msgerr = msg;
                        DeviceInfo.getInstance().setReturnMsd(msg);
                    }
                    super.onHandleError(code, msg);
                    LogUtil.debugNet("regist failed:" + msgerr);
                    registCallBack.onCallBack(false);
                }
            });
        } else {//已经注册
            registCallBack.onCallBack(true);
        }

    }

    /**
     * 心跳：
     * 传入观察者obser，发送next
     *   调用注册方法，实现接口对象RegistCallBack
     *       1、已经注册了或注册成功：
     *           POST心跳数据，ob发送next，obser进行监听
     *       2、注册失败：
     *          obser.onHandleError(-1, noRegishtError);
     * AppService类中的runTimeToHeadBeat()方法会调用actionHeadBeat，并实现BaseObserver接口方法
     */
    public void actionHeadBeat(final HeatParams h, final BaseObserver<HeadBeatBean> obser, final Consumer<HeadBeatBean> next) {
        actionRegist(new RegistCallBack() {//调用注册方法，实现接口对象
            @Override
            public void onCallBack(boolean isRegist) {
                if (isRegist) {
                    reqInfo.getHeadBeatJson(h, new RequestBodyManage.HeadBeatCallBack() {
                        @Override
                        public void hasFile(JSONObject jsonObject, boolean hasfile) {
                            RetrofitClient.getInstance().headBeat(jsonObject, obser, next, hasfile);
                        }
                    });


                } else {
                    obser.onHandleError(-1, noRegishtError);
                }
            }

        });

    }

    /**
     * 获取工艺参数
     * 调用注册方法，实现接口对象RegistCallBack
     *       1、已经注册了或注册成功：
     *           POST请求参数数据，ob发送next，obser进行监听
     *       2、注册失败：
     *          obser.onHandleError(-1, noRegishtError);
     * TaskUtil类中的getConfig()方法会调用actionParam，并实现BaseObserver接口方法
     */
    public void actionParam(final String coffee, final String machine, final BaseObserver<CoffeeConfigResultBean> obser, final Consumer<CoffeeConfigResultBean> next) {
        actionRegist(new RegistCallBack() {
            @Override
            public void onCallBack(boolean isRegist) {
                if (isRegist) {
                    JSONObject jsonObject = reqInfo.getParamJson(coffee, machine);//向服务器请求工艺参数数据

                    RetrofitClient.getInstance().param(jsonObject, obser, next);
                } else {
                    obser.onHandleError(-1, noRegishtError);
                }
            }
        });

    }

    /**
     * 获取支付二维码
     * 已经注册了或注册成功：
     *           POST请求参数数据，obser进行监听
     * BuyPageMode类的reqGoodsOrder方法会调用actionQrCode(),并实现BaseObserver接口方法；
     */
    public void actionQrCode(final PayBean payBean, final int paytype, final BaseObserver<PayQrCodeBean> obser) {
        actionRegist(new RegistCallBack() {
            @Override
            public void onCallBack(boolean isRegist) {
                if (isRegist) {
                    JSONObject jsonObject = reqInfo.getQrCodeJson(payBean, paytype);
                    RetrofitClient.getInstance().getPayCode(jsonObject, obser);
                } else {
                    obser.onHandleError(-1, noRegishtError);
                }
            }
        });

    }

    /**
     * 获取支付状态
     *    请求支付状态数据
     *    同步请求支付状态结果，结果回调payCallBack接口：payCallBack.onsuccess(tasks);
     *    PayPageMode类的reqinterval()会调用
     */
    public Call<PayResultBean>  actionPayResult(final PayBean payBean, RetrofitClient.PayCallBack payCallBack) {

//        actionRegist(new RegistCallBack() {
//            @Override
//            public void onCallBack(boolean isRegist) {
        JSONObject jsonObject = reqInfo.getPayResultJson(payBean);
        return RetrofitClient.getInstance().payResult(jsonObject,payCallBack);
//            }
//        });

    }

    /**
     * 上传销售数据：AppService类的runTimeToActionTask()方法会调用该方法：
     *   获得上传销售数据、并进行上传销售数据；
     *  AppService类的runTimeToActionTask()方法会调用；
     *
     */
    public void actionUploadDatas(final Order order, final BaseObserver<UploadDatasBean> obser, final Consumer<UploadDatasBean> next) {
        actionRegist(new RegistCallBack() {
            @Override
            public void onCallBack(boolean isRegist) {//注册会回调该方法，传入是否注册参数
                if (isRegist) {//注册成功了
                    JSONObject jsonObject = reqInfo.getUploadDatasJson(order);
                    RetrofitClient.getInstance().uploadData(jsonObject, obser, next);
                } else {//注册失败
                    obser.onHandleError(-1, noRegishtError);
                }
            }
        });

    }

    //版本更新接口
    public void actionUpdataVersion(final BaseObserver<VersionBean> obser) {
        actionRegist(new RegistCallBack() {
            @Override
            public void onCallBack(boolean isRegist) {
                if (isRegist) {
                    JSONObject jsonObject = reqInfo.getUpdataVersionJson();
                    RetrofitClient.getInstance().updataVersion(jsonObject, obser);
                } else {
                    obser.onHandleError(-1, noRegishtError);
                }
            }
        });

    }

//    //上传日志
//    public void actionUploadLog(final BaseObserver<UploadLogBean> obser) {
//
//        actionRegist(new RegistCallBack() {
//            @Override
//            public void onCallBack(boolean isRegist) {
//                if (isRegist) {
//                    JSONObject jsonObject = reqInfo.getUploadLogJson();
//                    RetrofitClient.getInstance().uploadLog(jsonObject, obser);
//                } else {
//                    obser.onHandleError(-1, noRegishtError);
//                }
//            }
//        });
//
//    }

    //获取取货二维码
    public void actionGetPickQrCode(final BaseObserver<PickQrCodeBean> obser) {
        actionRegist(new RegistCallBack() {
            @Override
            public void onCallBack(boolean isRegist) {
                if (isRegist) {
                    JSONObject jsonObject = reqInfo.getGetPickQrCodeJson();
                    RetrofitClient.getInstance().getPickCode(jsonObject, obser);
                } else {
                    obser.onHandleError(-1, noRegishtError);
                }
            }
        });

    }

    //二维码取货
    public void actionPickByQrCode(final BaseObserver<PickByCodeBean> obser) {
        actionRegist(new RegistCallBack() {
            @Override
            public void onCallBack(boolean isRegist) {
                if (isRegist) {
                    JSONObject jsonObject = reqInfo.getPickByQrCodeJson();
                    RetrofitClient.getInstance().pickByCodeBean(jsonObject, obser);
                } else {
                    obser.onHandleError(-1, noRegishtError);
                }
            }
        });

    }

    //取货码取货
    public void actionPickByNumber(final BaseObserver<PickByNumberBean> obser) {
        actionRegist(new RegistCallBack() {
            @Override
            public void onCallBack(boolean isRegist) {
                if (isRegist) {
                    JSONObject jsonObject = reqInfo.getPickByNumberJson();
                    RetrofitClient.getInstance().pickByNumber(jsonObject, obser);
                } else {
                    obser.onHandleError(-1, noRegishtError);
                }
            }
        });

    }
}
