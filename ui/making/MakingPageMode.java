package com.fancoff.coffeemaker.ui.making;

import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.Application.TestIoDatas;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.MakingStatebean;
import com.fancoff.coffeemaker.bean.PayBean;
import com.fancoff.coffeemaker.bean.VMCState;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.bean.coffe.ImageBean;
import com.fancoff.coffeemaker.bean.coffe.SeckillBean;
import com.fancoff.coffeemaker.io.CMDUtil;
import com.fancoff.coffeemaker.io.IOConstans;
import com.fancoff.coffeemaker.io.IoCallBack;
import com.fancoff.coffeemaker.io.data.SendByteBuilder;
import com.fancoff.coffeemaker.net.RequstBean.Order;
import com.fancoff.coffeemaker.utils.DbUtil;
import com.fancoff.coffeemaker.utils.MediaFile;
import com.fancoff.coffeemaker.utils.TimeUtil;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;

import java.util.ArrayList;

/**
 * Created by apple on 2017/9/10.
 */

public class MakingPageMode implements MakingPageContract.IMakingPageModel {
    static MakingPageMode makingPageMode;

    public static MakingPageMode getIns() {
        if (makingPageMode == null) {
            makingPageMode = new MakingPageMode();
        }
        return makingPageMode;
    }

    int type = -100;

    /**
     * 停止制作：
     * 在在HashMap<String, RunCallBack> mapRuns移除tag键对应的mapRuns
     */
    @Override
    public void stopMaking() {
        TimeUtil.getInstance().removeTimerschedule(tag);
    }

    final String tag = "making";

    /**
     * 制作中各种UI显示内容回调给P层；
     * 在HashMap<String, RunCallBack> mapRuns添加key和runcallback；添加任务并实现回调接口方法；
     *   任务执行的时候，制作中各种UI显示内容回调给P层；
     * 在制作任务结束的时候会在mapRun中取消这个任务；
     */
    @Override
    public void showMakingContent(final MakingStatebean makingStatebean, final MakingPageContract.IMakingResultCallBack cllback) {
        type = -100;
        making(makingStatebean, cllback);
        TimeUtil.getInstance().addTimerschedule(tag, new TimeUtil.RunCallBack() {
            @Override
            public void runHander() {//制作任务在，1s中会回调该接口方法
                making(makingStatebean, cllback);
            }
        });
    }

    /**
     * 1、根据不同的制作状态下，进行提示:title背景设置、进度条显示或关闭、视频提示；
     * 2、回调P层：title背景、进度条、显示内容、卡杯、错误、视频列表；
	  内容有变
     */
    public void making(final MakingStatebean makingStatebean, final MakingPageContract.IMakingResultCallBack cllback) {
        if (makingStatebean != null) {
            makingStatebean.update();//会将背景、进度、视频信息存入makingStatebean中；
            cllback.showTitleBg(makingStatebean.getTitleRes());
            cllback.showProgress(makingStatebean.getShowProgress(), makingStatebean.getTimeProgress());
            cllback.showContent(makingStatebean.getContent());
            cllback.showStuckCupBtn(makingStatebean.isStuckCup() || makingStatebean.isErrorCup());
            cllback.showErrorContent(makingStatebean.getErrorShowView());
            int mtype = makingStatebean.getVedioType();
            if (mtype != type) {
                type = mtype;
                ArrayList<ImageBean> vedios = makingStatebean.getVedio();
                if (vedios != null && vedios.size() > 0) {
                    if (MediaFile.isVideoFileType(vedios.get(0).getUrl())) {
                        cllback.showGif(null);
                        cllback.showVedio(vedios);
                    } else {
                        cllback.showVedio(null);
                        cllback.showGif(vedios);
                    }

                } else {
                    cllback.showVedio(null);
                    cllback.showGif(null);
                }
            }

        }
    }

    /**
     * 1、0号饮品；
     * 2、PayBean，ArrayList<Order> orders 不空且大于0
     *    获取秒杀，有秒杀，则秒杀的总杯数减1，秒杀数据库更新
     */
    public void saveOrder(final PayBean payBean) {
        CoffeeBean cof = payBean.getGoods().get(0);
        if (payBean.getOrders() != null && payBean.getOrders().size() > 0) {
            if (!MyApp.getIns().getPayDebug()) {//点击二维码模拟支付成功
                String timesell = TimeUtil.getInstance().getNowTimeShort();
                ArrayList<Order> orders = payBean.getOrders();
                if (orders != null) {
                    for (Order oder : orders) {
                        oder.setSale_time(timesell);
                    }
                }
                DbUtil.getInstance().saveOrders(orders);
            }
            SeckillBean seckillBean = cof.isSkilling();
            if (seckillBean != null) {
                seckillBean.setTotal(seckillBean.getTotal() - 1);
                DbUtil.getInstance().update(seckillBean);
            }
        }
    }

    MakingStatebean makingStatebean;

    public String showFaied() {

        if (getMakingStatebean() != null) {
            return ",该杯免费";
        } else {
            return "";
        }
    }

    /** 返回制作状态：makingStatebean */
    public MakingStatebean getMakingStatebean() {
        return makingStatebean;
    }

    /**传入makingStatebean   清洗的时候会传入null的参数*/
    public void setMakingStatebean(MakingStatebean makingStatebean) {
        synchronized ("make") {
            this.makingStatebean = makingStatebean;
        }
    }

    public interface RestartMakingCallBack {
        void onSuccess();

        void onFailed(String ss);
    }

    /**
     * 确认移除后重新制作饮品：
     * 1、发送清除错误指令，实现IoCallBack接口方法：
     *     onFailed：
     *         向MakingPageFragment的t_ok按键监听返回onFailed，参数为：“清除故障失败”
     *     onSuccess：
     *         获得PayBean；开始制作
     *         向MakingPageFragment的t_ok按键监听返回onSuccess，参数为：“清除故障失败”

     *  CMDTask类的checkTimeOut中getIoCallBack().onSuccess(getSendBytes(), getReadBytes());
     *                          getIoCallBack().onFailed(getReadBytes(), IoCallBack.ERROR_TIME_OUT);
     *          从串口发送数据后，收到数据会进行回调
     */
    public void restartMaking(final RestartMakingCallBack restartMakingCallBack) {
        CMDUtil.getInstance().clearError(new IoCallBack() {
            @Override
            public void onFailed(byte[] send, int type) {
                restartMakingCallBack.onFailed(MyApp.getIns().getString(R.string.clearErrorFailed));
            }

            @Override
            public void onSuccess(byte[] send, byte[] rb) {
                if (MyApp.IOTEST) {
                    TestIoDatas.getInstance().remake();
                } else {
                    PayBean payBean = makingStatebean.getPayBean();
                    setMakingStatebean(null);
                    startMaking(payBean);
                }
                restartMakingCallBack.onSuccess();
            }
        });

    }

    /**
     * 1、获取PayBean中的第0号饮品；
     * 2、饮品制作命令数据；
     * 3、传入PayBean和进度条
     * 4、制作咖啡指令传入任务列表
     */
    public void startMaking(PayBean paybean) {
        LogUtil.test("startMaking:" + paybean.toString());
        CoffeeBean coffeeBean = paybean.getGoods().get(0);
        final byte[] bytes = SendByteBuilder.build().creatByteArray(
                IOConstans.OPTION.OPTION_MAKING,
                coffeeBean.getMakigBodyByte());
        setMakingStatebean(new MakingStatebean(paybean, coffeeBean.getMake_duration()));
        CMDUtil.getInstance().maikeCoffee(bytes, null);
    }

    //无法移除
    @Override
    public void cannotRemove() {
        if (getMakingStatebean().isErrorCup()) {
            CMDUtil.getInstance().clearError(null);
        }
        getMakingStatebean().stuckCannotRemove();
    }

    /**打印制作失败；设置制作状态为制作失败；*/
    public void makingFailed() {
        LogUtil.action("制作失败");
        getMakingStatebean().setFailed();
//        CMDUtil.getInstance().clearProgresss(null);
//        CMDUtil.getInstance().clearError(null);
        setMakingStatebean(null);
    }

    /**
     * 1、设置制作状态为取杯成功；
     * 2、打印日志：制作成，并上传销售记录；//DEBUG_ACTION:制作成功,并上传销售记录
     * 3、updateOrder=true：更新秒杀及秒杀数据库；
     * 4、setMakingStatebean(null);
     */
    public void makeSuceese(boolean updateOrder) {
        getMakingStatebean().setPickCupFinishState();
        LogUtil.action("制作成功," + (updateOrder ? "并上传销售记录" : ""));
        if (!MyApp.getIns().getPayDebug() && updateOrder) {

            saveOrder(getMakingStatebean().getPayBean());//保存销售记录 上传服务器
        }
        setMakingStatebean(null);

    }

    //返回值 是否故障停止制作

    /**
     * 制作的进程：
     * 1、检测是否需要退款；
     * 2、VMC为busy状态：
     *    2.1、制作状态还不是busy状态：
     *         说明第一次进入busy状态，代表饮品开始制作了；
     *         设置制作状态为busy；制作状态为落杯；
     *    2.2、
     *         2.2.1、VMC状态为有脏杯：
     *                  startTime=当前时间，timeProgress = 0;
     *                  制作状态设置为有脏杯
     *         2.2.2、vmc状态为手动放杯：
     *                  startTime=当前时间，timeProgress = 0;
     *                  制作状态设置为手动放杯；
     *         2.2.3、vmc状态为正在清洗中：
     *                  打印制作失败；设置制作状态为制作失败；
     *                  callBack.onError("正在清洗");
     *         2.2.4、进度条100或者制作完成：
     *                2.2.4.1、正在制作中：设置为制作成功；
     *                2.2.4.2、制作成功：设置为取杯；
     *                2.2.4.3、正在取杯中：
     *                         取杯超时：makeSuceese
     *                                     打印：DEBUG_ACTION:making end:makeSuceese
     *                                     设置制作状态为取杯成功；
     *                                     打印：DEBUG_ACTION:制作成功,并上传销售记录
     *                                     isFree=false：更新秒杀及秒杀数据库
     *         2.2.5、VMC状态为落杯：
     *                2.2.5.1、VMC状态为正在掉杯：设置为正在落杯中；
     *                2.2.5.2、制作状态为正在落杯中：设置为制作咖啡；
     * 3、VMC不为busy状态：
     *    设置制作状态不是busy；
     *    3.1、制作结束：
     *         3.1.1、制作结束并且取杯成功：
     *                makeSuceese：
     *                  打印：DEBUG_ACTION:making end:makeSuceese
     *                  设置制作状态为取杯成功；
     *                  打印：DEBUG_ACTION:制作成功,并上传销售记录
     *                  isFree=false：更新秒杀及秒杀数据库
     *         3.1.2、制作失败
     *                3.1.2.1、VMC状态为有脏杯：
     *                        打印日志："making end:isHasdirty");
     *                        打印制作失败；设置制作状态为制作失败
     *                        callBack.onError("脏杯无法移除");
     *                3.1.2.2、处理卡杯；
     *                3.1.2.3、其他情况：
     *                         1、进度100：打印：making end with no PickCup state；
     *                                     makeSuceese；
     *                         2、打印制作失败；设置制作状态为制作失败；callBack.onError("制作失败");
     *    3.2、制作未开始：
     *         制作超时：打印：making isStartTimeOut:isStartTimeOut
     *         1、有卡杯或者免费退款：making isStartTimeOut:checkError"
     *         2、其他：设置制作状态为制作失败callBack.onError("制作失败");
     *     callBack.onSucess();
     */
    public boolean makingProgress(VMCState vmcStateBean, ErrorCallBack callBack) {
        synchronized ("make") {
            getMakingStatebean().checkFreeError();//监测错误
            if (vmcStateBean.isBusy()) {//进入busy状态
                if (!getMakingStatebean().isBusy()) {//第一次进入busy状态，代表制作开始
                    LogUtil.test("making busy:isEnd");
                    getMakingStatebean().setBusy(true);
                    getMakingStatebean().setStartMakingState();//设置状态为开始落杯
                }
                if (vmcStateBean.isHasdirty()) {
                    LogUtil.test("making busy:isHasdirty");
                    getMakingStatebean().initStartTime();
                    getMakingStatebean().setDirtyState();//设置状态为脏杯
                } else if (vmcStateBean.isHandCup()) {
                    LogUtil.test("making busy:isHandCup");
                    getMakingStatebean().initStartTime();
                    getMakingStatebean().setHandCup();//设置状态为手动落杯

                } else if (vmcStateBean.isCleanning()) {//正在清洗中 制作失败
                    LogUtil.test("making busy:isCleanning");
                    makingFailed();
                    callBack.onError("正在清洗");
                    return true;
                }
                //制作进度条
                //从进度条到100到D6返回成功状态 有大概5秒延迟，为了及时跳出取杯界面，暂定以进度条为准
                else if (vmcStateBean.getMakingProgress() == 100
                        || vmcStateBean.isMakeSuceess()
                        ) {//制作完成
                    if (getMakingStatebean().isMaking()) {
                        LogUtil.test("making busy:isMaking");
                        getMakingStatebean().seMakingSucess();//制作成功100%
                    } else if (getMakingStatebean().isMakingSuceess()) {
                        getMakingStatebean().setPickCupingState();//正在取杯中
                    } else if (getMakingStatebean().isPickCuping()) {//正在取杯中
                        LogUtil.test("making busy:isPickCuping");
                        if (getMakingStatebean().isPicKTImeout()) {//取杯超时
                            LogUtil.test("making busy:makeSuceese");
                            makeSuceese(!getMakingStatebean().isFree());
                        }
                    }
                } else {//制作流程中
                    if (vmcStateBean.isDownCup()) {
                        LogUtil.test("making busy:isDownCup");
                        getMakingStatebean().setDownCupingState();//落杯中
                    } else if (getMakingStatebean().isDownCuping()) {
                        LogUtil.test("making busy:isDownCuping");
                        getMakingStatebean().setMakingState();//制咖啡中
                    }
                }

            }
            //不处于busy状态，（制作未开始，或者制作结束）
            else {
                getMakingStatebean().setBusy(false);
                if (getMakingStatebean().isEnd()) {//制作结束
                    //制作结束并且取杯成功
                    if (getMakingStatebean().isMakingSuceess() || getMakingStatebean().isPickCuping()) {
                        LogUtil.action("making end:makeSuceese");
                        makeSuceese(!getMakingStatebean().isFree());
                    }
                    //制作失败
                    else {
                        //脏杯
                        if ((vmcStateBean.isHasdirty())) {
                            LogUtil.action("making end:isHasdirty");
                            makingFailed();
                            callBack.onError("脏杯无法移除");
                            return true;
                        } else if (checkError(vmcStateBean, callBack)) {//卡杯
                            LogUtil.action("making end:checkError");
                            return true;
                        }
                        //其他漏掉的意外情况处理
                        else {
                            if (vmcStateBean.getMakingProgress() == 100) {
                                LogUtil.action("making end with no PickCup state");
                                makeSuceese(!getMakingStatebean().isFree());
                            } else {
                                LogUtil.test("making end:makingFailed");
                                makingFailed();
                                callBack.onError("制作失败");
                                return true;
                            }

                        }

                    }
                }
                //制作未开始
                else {
                    if (getMakingStatebean().isStartTimeOut()) {
                        LogUtil.action("making isStartTimeOut:isStartTimeOut");
                        if (checkError(vmcStateBean, callBack)) {
                            LogUtil.action("making isStartTimeOut:checkError");
                            return true;
                        } else {
                            LogUtil.action("making isStartTimeOut:failed");
                            makingFailed();
                            callBack.onError("制作失败");
                            return true;
                        }
                    }


                }
            }
            callBack.onSucess();
            return false;
        }

    }

    public interface ErrorCallBack {
        void onError(String error);

        void onSucess();

    }

    /**  增加了
     * 制作超时未开始或者制作结束未成功时，通过该方法处理vmc报的故障，
     * 1、卡杯：
     *    startTime=当前时间，timeProgress = 0;
     *    设置制作状态为卡杯状态；stuckCupTime=当前时间
     *    卡杯界面的超时时间到了或手动点击无法移除：
     *           设置制作状态为制作失败；回调errorCallBack.onError("卡杯无法移除");
     * 2、落杯器故障：
     *    startTime=当前时间，timeProgress = 0;
     *    设置制作状态为卡杯状态；stuckCupTime=当前时间
     *    卡杯界面的超时时间到了或手动点击无法移除：
     *           设置制作状态为制作失败；回调errorCallBack.onError("卡杯无法移除");
     * 3、isFree=true
     *    String errorShow;设置制作状态为制作失败；回调errorCallBack.onError(showError);
     * */
    public boolean checkError(VMCState vmcStateBean, ErrorCallBack errorCallBack) {
        if (vmcStateBean.stuckCup()) {//卡杯
            getMakingStatebean().initStartTime();
            getMakingStatebean().setStuckCup();
            if (getMakingStatebean().isStuckCupTimeOut()) {//手动点击无法移除或移除卡杯超时
                makingFailed();
                errorCallBack.onError(MyApp.getIns().getString(R.string.canotremoveStukCup));
                return true;
            }
            return true;
        } else if (vmcStateBean.errorCup()) {//落杯器故障
            getMakingStatebean().initStartTime();
            getMakingStatebean().setErrorCupCup();
            if (getMakingStatebean().isStuckCupTimeOut()) {//手动点击无法移除或移除卡杯超时
                makingFailed();
                errorCallBack.onError(MyApp.getIns().getString(R.string.canotremoveErrorCup));
                return true;
            }
            return true;
        } else if (getMakingStatebean().isFree()) {
            String showError = getMakingStatebean().getErrorShow();
            makingFailed();
            errorCallBack.onError(showError);
            return true;
        }
        return false;
    }

    /**
     * 有makingStatebean对象，并且制作状态不是完成或失败的状态，返回1
     */
    public synchronized boolean isMakeing() {
        return makingStatebean != null && !makingStatebean.isAllFinish();
    }


}
