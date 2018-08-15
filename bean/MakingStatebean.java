package com.fancoff.coffeemaker.bean;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.coffe.ImageBean;
import com.fancoff.coffeemaker.io.CMDUtil;
import com.fancoff.coffeemaker.service.DurationUtil;
import com.fancoff.coffeemaker.ui.making.MakingPageMode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by apple on 2018/3/31.
 */

public class MakingStatebean implements Serializable {

    public static final int STEP_PAY_SUCEES = 1;//支付成功
    public static final int STEP_MAKING_START = 2;//准备落杯
    public static final int STEP_DOWN_CUP = 3;//落杯中
    public static final int STEP_DIRTY_CUP = 4;//有脏杯
    public static final int STEP_MAKING = 5;//制作中
    public static final int STEP_CUO_PICKING = 6;//取杯中
    public static final int STEP_MAKING_SUCESS = 9;//制作完成
    public static final int STEP_STUCK_CUP = 7;//卡杯
    public static final int STEP_ERROR_CUP = 8;//落杯器故障
    public static final int STEP_HAND_CUP = 10;//手动落杯
    public static final int STEP_FINISH_SUCESS = 1001;//取杯完成,结束


    public static final int STEP_MAKING_FIALED = 1000;//制作失败
//    //从进度条到100到D6返回成功状态 有延迟，为了及时跳出取杯界面，暂定以进度条为准，超时结束取杯，并提示失败！
//    public final static int MACKCF_SUCESS_TIMEOUT = 10000;//从进度条到100开始算起，多少秒后D6状态未返回制作成功标识位，表示制作失败！

    PayBean payBean;//订单
    int makingState = STEP_PAY_SUCEES;
    long timeProgress;//制作进度
    long maxprogress;//制作总时间
    String content;//显示文字
    ArrayList<ImageBean> vedio;
    long startTime;//开始制作时间，用来衡量意外情况导致的超时处理 并且计算进度
    long successTime;//制作成功时间
    long stuckCupTime;  //估计是开始卡杯时间
    long runingTime;  //制作中的进度时间

    public boolean isMakingSuceess() {
        return getMakingState() == MakingStatebean.STEP_MAKING_SUCESS;
    }

    public boolean isPaySuceess() {
        return getMakingState() == MakingStatebean.STEP_PAY_SUCEES;
    }

    public boolean isHandCup() {
        return getMakingState() == MakingStatebean.STEP_HAND_CUP;
    }

    public boolean isErrorCup() {
        return getMakingState() == MakingStatebean.STEP_ERROR_CUP;
    }

    public boolean isStuckCup() {
        return getMakingState() == MakingStatebean.STEP_STUCK_CUP;
    }

    public boolean isDerity() {
        return getMakingState() == MakingStatebean.STEP_DIRTY_CUP;
    }

    public boolean isStartMaking() {
        return getMakingState() == MakingStatebean.STEP_MAKING_START;
    }

    public boolean isDownCuping() {
        return getMakingState() == MakingStatebean.STEP_DOWN_CUP;
    }

    public boolean isMaking() {
        return getMakingState() == MakingStatebean.STEP_MAKING;
    }

    /**是否是取杯状态*/
    public boolean isPickCuping() {
        return getMakingState() == MakingStatebean.STEP_CUO_PICKING;
    }

    /**是否是完成状态*/
    public boolean isFinish() {
        return getMakingState() == MakingStatebean.STEP_FINISH_SUCESS;
    }

    public boolean isFlaled() {
        return getMakingState() == STEP_MAKING_FIALED;
    }

    /** 卡杯界面的超时时间是否到了 */
    public boolean isStuckCupTimeOut() {
        return (System.currentTimeMillis() - stuckCupTime) >= DurationUtil.getIns().getStuckCupTime() * 1000;
    }

    /**设置stuckCupTime=1*/
    public void stuckCannotRemove() {
        stuckCupTime = 1;
    }

    /**设置当前制作状态为makingState*/
    private void setMakingState(int makingState) {
        this.makingState = makingState;

    }

    /**设置为制作成功*/
    public void seMakingSucess() {
        setMakingState(MakingStatebean.STEP_MAKING_SUCESS);
    }

    /**设置为手动放杯*/
    public void setHandCup() {
        setMakingState(MakingStatebean.STEP_HAND_CUP);
    }

    public void setPaySuceessState() {
        setMakingState(MakingStatebean.STEP_PAY_SUCEES);
    }

    /**设置制作状态为制作失败*/
    public void setFailed() {
        setMakingState(MakingStatebean.STEP_MAKING_FIALED);
    }

    /**设置制作状态为脏杯*/
    public void setDirtyState() {

        setMakingState(MakingStatebean.STEP_DIRTY_CUP);

    }

    /**
     * 落杯器故障的时候，打开咖啡门，设置开始故障时间stuckCupTime为当前时间:
     * 不是MakingStatebean.STEP_ERROR_CUP：开咖啡门；
     * 设置状态为STEP_ERROR_CUP；
     * stuckCupTime == 0，则stuckCupTime=当前时间；
     */
    public void setErrorCupCup() {
        if (!isErrorCup()) {
            CMDUtil.getInstance().openDoor(false, true, null);
        }
        setMakingState(MakingStatebean.STEP_ERROR_CUP);
        if (stuckCupTime == 0) {
            stuckCupTime = System.currentTimeMillis();
        }

    }
    /** 设置制作状态为卡杯状态；开始卡杯的时间stuckCupTime=当前时间 */
    public void setStuckCup() {
        setMakingState(MakingStatebean.STEP_STUCK_CUP);
        if (stuckCupTime == 0) {
            stuckCupTime = System.currentTimeMillis();
        }

    }

    boolean isBusy;

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    /** 设置制作状态为开始制作；制作进度的时间runingTime=当前时间 */
    public void setStartMakingState() {
        setMakingState(MakingStatebean.STEP_MAKING_START);

        runingTime = System.currentTimeMillis();
    }

    public void setDownCupingState() {
        setMakingState(MakingStatebean.STEP_DOWN_CUP);
    }

    public void setMakingState() {
        setMakingState(MakingStatebean.STEP_MAKING);
    }

    /**
     * 设置取杯状态，制作进度timeProgress等于最大进度maxprogress；
     * 制作成功时间successTime为0的话则设置为当前时间；
     */
    public void setPickCupingState() {
        setMakingState(MakingStatebean.STEP_CUO_PICKING);
        timeProgress = maxprogress;
        if (successTime == 0) {
            successTime = System.currentTimeMillis();
        }
    }

    //    public boolean isPickCupTimeOut() {
//        return System.currentTimeMillis()-successTime>=MACKCF_SUCESS_TIMEOUT;
//    }
    /**设置制作状态为取杯成功*/
    public void setPickCupFinishState() {
        setMakingState(MakingStatebean.STEP_FINISH_SUCESS);
    }

    /**
     * 制作成功时候的时间successTime与当前时间进行比较，然后计算出剩余取杯时间；
     * 1、从工艺表获取取杯超时时间
     * 2、successTime：设置当前状态为取杯状态时，获取的系统时间
     * 3、计算出剩余的取杯时间
     */
    public int getLastPickTime() {
        int pickUpTime = DurationUtil.getIns().getPickCupTime();
        if (this.makingState == STEP_CUO_PICKING) {
            int la = (int) ((System.currentTimeMillis() - successTime) / 1000);
            if (la >= pickUpTime) {
                la = pickUpTime;
            }
            return pickUpTime - la;
        }
        return pickUpTime;
    }

    public boolean isAllFinish() {
        return (makingState == STEP_FINISH_SUCESS) || (makingState == STEP_MAKING_FIALED);
    }

    /**
     * 传入PayBean，进度条最大时间maxProgress
     * 开始制作时间startTime为当前时间ms
     */
    public MakingStatebean(PayBean payBean, int maxProgress) {
        this.payBean = payBean;
        this.maxprogress = maxProgress * 1000;
        startTime = System.currentTimeMillis();
    }

    /**
     * 进度值
     */
    public int getP() {
        int p = ((int) (timeProgress * 1.00 * 100 / maxprogress));
        if (p >= 100) {
            p = 98;
        }
        if (makingState == STEP_MAKING_SUCESS || makingState == STEP_CUO_PICKING || isFinish()) {
            return 100;
        }
        return p;
    }

    /**开始制作时间startTime=当前时间，制作进度timeProgress = 0;*/
    public void initStartTime() {
        startTime = System.currentTimeMillis();
        timeProgress = 0;

    }

    /**
     * 主要是获取从制作开始到现在的时间差值，作为进度值的参数；
     * 开始制作的时候会获取当前的时间给runningTime
     * 1、支付成功、脏杯、卡杯、手动放杯情况下：timeProgress=0
     *    其他情况：timeProgress += (noWtime - runingTime);
     * 2、timeProgress制作进度超过最大进度maxprogress时，制作进度赋值为最大进度；
     */
    public int getTimeProgress() {
        long noWtime = System.currentTimeMillis();
        if (makingState == STEP_PAY_SUCEES || makingState == STEP_DIRTY_CUP
                || makingState == STEP_STUCK_CUP || makingState == STEP_ERROR_CUP || makingState == STEP_HAND_CUP) {
            timeProgress = 0;
        } else {
            timeProgress += (noWtime - runingTime);
        }
        runingTime = noWtime;

        if (timeProgress >= maxprogress) {
            timeProgress = maxprogress;
        }
        return getP();
    }
    /** 获取进度值%  */
    public String getProgressShow() {
        return getP() + "%";
    }

    /**
     * 根据不同的制作状态下，将title背景设置、进度条显示或关闭、视频提示等存入MakingStatebean；
     * 1、有脏杯情况：
     *        提示：请按下方视频步骤移除。。。。。
     *        设置TitleRes，显示进度关闭；video类型为脏杯；提取出脏杯视频url列表
     * 2、支付成功：
     *        提示：准备落杯了，完成了 进度值%
     *        设置TitleRes，显示进度打开，video类型为制作，提取出制作视频url列表
     * 3、准备制作：
     *        提示：准备落杯了，完成了 进度值%
     *        设置TitleRes，显示进度打开，video类型为制作，提取出制作视频url列表
     * 4、卡杯：
     *        提示：杯子意外卡住了，请按。。。。。
     *        设置TitleRes，显示进度关闭，video类型为卡杯，提取出卡杯视频url列表
     * 5、手动放杯：
     *        提示：手动放杯；
     *        设置TitleRes，显示进度关闭，video类型为卡杯，
     * 6、落杯：
     *        提示：正在落杯了，完成了 进度值%
     *        设置TitleRes，显示进度打开，video类型为制作，提取出制作视频url列表
     * 7、制作中：
     *        提示：正在制作，完成了 进度值%
     *        设置TitleRes，显示进度打开，video类型为制作，提取出制作视频url列表
     * 8、取杯：
     *        提示：请取杯剩余x秒
     *        设置TitleRes，显示进度关，video类型为完成，提取出完成视频url列表
     */
    public void update() {
        //脏杯
        if (makingState == STEP_DIRTY_CUP) {
            content = MyApp.getIns().getString(R.string.dirtycontent);
            TitleRes = R.drawable.cf_title_remove_bg;
            showProgress = false;
            vedioType = VEDIO_TYPE_DIRTY_CUP;
            vedio = DataCenter.getInstance().getDirtyVedio();
        }
        //支付成功
        else if (makingState == STEP_PAY_SUCEES) {
            content = MyApp.getIns().getString(R.string.paysucontent) + getProgressShow();
            TitleRes = R.drawable.cf_title_making_bg;
            showProgress = true;
            vedioType = VEDIO_TYPE_MAKING;
            vedio = DataCenter.getInstance().getMakingVedio();
        }
        //准备制作
        else if (makingState == STEP_MAKING_START) {
            content = MyApp.getIns().getString(R.string.makestartcontent) + getProgressShow();
            TitleRes = R.drawable.cf_title_making_bg;
            showProgress = true;
            vedioType = VEDIO_TYPE_MAKING;
            vedio = DataCenter.getInstance().getMakingVedio();
        }
        //移除卡杯
        else if (makingState == STEP_STUCK_CUP) {
            content = MyApp.getIns().getString(R.string.removecontent);
            TitleRes = R.drawable.cf_title_cup_bg;
            showProgress = false;
            vedioType = VEDIO_TYPE_REMOVE_CUP;
            vedio = DataCenter.getInstance().getStuckCupVedio();
        }
        //移除落杯器故障杯子
        else if (makingState == STEP_ERROR_CUP) {
            content = MyApp.getIns().getString(R.string.removecontentError);
            TitleRes = R.drawable.cf_title_cup_error_bg;
            showProgress = false;
            vedioType = VEDIO_TYPE_REMOVE_CUP;
            vedio = DataCenter.getInstance().getStuckCupVedio();
        }
        //手动落杯
        else if (makingState == STEP_HAND_CUP) {
            content = MyApp.getIns().getString(R.string.handupcontent);
            TitleRes = R.drawable.cf_title_making_bg;
            showProgress = false;
            vedioType = VEDIO_TYPE_HAND_CUP;
            vedio = DataCenter.getInstance().getHandVedio();
        }
        //落杯
        else if (makingState == STEP_DOWN_CUP) {
            content = MyApp.getIns().getString(R.string.downcupcontent) + getProgressShow();
            TitleRes = R.drawable.cf_title_making_bg;
            showProgress = true;
            vedioType = VEDIO_TYPE_MAKING;
            vedio = DataCenter.getInstance().getMakingVedio();
        }
        //制作中
        else if (makingState == STEP_MAKING || makingState == STEP_MAKING_SUCESS) {
            content = MyApp.getIns().getString(R.string.makingcontent) + getProgressShow();
            TitleRes = R.drawable.cf_title_making_bg;
            showProgress = true;
            vedioType = VEDIO_TYPE_MAKING;
            vedio = DataCenter.getInstance().getMakingVedio();
        }
        //取杯
        else if (makingState == STEP_CUO_PICKING) {
            content = MyApp.getIns().getString(R.string.pickuplastcontent).replace("x", getLastPickTime() + "");
            TitleRes = R.drawable.cf_title_makeend_bg;
            showProgress = false;
            vedioType = VEDIO_TYPE_CUO_PICKING;
            vedio = DataCenter.getInstance().getCompleteVedio();
        }
    }

    /**update会获取视频列表*/
    public ArrayList<ImageBean> getVedio() {
        return vedio;
    }

    int vedioType;

    /**1、update会设置该值*/
    public int getVedioType() {
        return vedioType;
    }


    public String getContent() {
        return content;
    }

    int TitleRes = R.drawable.cf_title_making_bg;

    public int getTitleRes() {
        return TitleRes;
    }

    boolean showProgress = false;

    /** 返回是否显示进度条的标志 */
    public boolean getShowProgress() {
        return showProgress;
    }

    public void setContent(String content) {
        this.content = content;
    }


    private static final int VEDIO_TYPE_DIRTY_CUP = 1;
    private static final int VEDIO_TYPE_MAKING = 2;
    private static final int VEDIO_TYPE_CUO_PICKING = 3;
    private static final int VEDIO_TYPE_REMOVE_CUP = 4;
    private static final int VEDIO_TYPE_HAND_CUP = 5;//手动落杯取消，暂无


    public void setVedio(ArrayList<ImageBean> vedio) {
        this.vedio = vedio;
    }

    public PayBean getPayBean() {
        return payBean;
    }

    public void setPayBean(PayBean payBean) {
        this.payBean = payBean;
    }

    public int getMakingState() {
        return makingState;
    }


    /** 返回是否制作超时 */
    public boolean isStartTimeOut() {
        return System.currentTimeMillis() - startTime >= DurationUtil.getIns().getMakeOutTime() * 1000;
    }

    /** 返回是否取杯超时 */
    public boolean isPicKTImeout() {
        int pickUpTime = DurationUtil.getIns().getPickCupTime();
        int la = (int) ((System.currentTimeMillis() - successTime) / 1000);
        return la >= pickUpTime;
    }

    /** 制作过了 */
    public boolean isEnd() {
        return makingState > 1;
    }


    public boolean isTypeCoffee() {
        return payBean.getGoods().get(0).isCoffee();
    }



    public String getErrorShow() {
        return errorShow;
    }

    /*
           制作过程中发生故障处理
           返回值：是否退款，该杯免费
        */
    String errorShow;

    boolean isFree = false;

    public boolean isFree() {
        return isFree;

    }

    /**判断是否发生需要退款的故障*/
    /**
     * 检查是否需要免费退款：
     * 1、咖啡机故障并且是咖啡饮品：
     *    errorShow=咖啡机故障内容+您的款项将于20分钟内退款；
     *    isFree=true;
     * 2、VMC通讯故障：
     *    除了咖啡机通讯超时、vmc加热故障、缺杯、废水箱满、大门未关、取杯门未关、存储器错误、缺水外，
     *    errorShow=错误内容+您的款项将于20分钟内原路返回；isFree=true；
     *
     */
    public void checkFreeError() {
        if (VMCState.getIns().isCFError() && isTypeCoffee()) {//咖啡机故障并且是咖啡类就退款
            errorShow = VMCState.getIns().getCf_error_content() + "," + MyApp.getIns().getString(R.string.sorrycontent);
            isFree = true;
            return;
        } else if (VMCState.getIns().isVMCError()) {
            if ((VMCState.getIns().isVmcErrorBit0() && isTypeCoffee())
                    || (
                    !VMCState.getIns().isVmcErrorBit0()//咖啡机通讯超时
                            && !VMCState.getIns().isVmcErrorBit1()//vmc加热故障
                            && !VMCState.getIns().isVmcErrorBit2()//缺杯
                            && !VMCState.getIns().isVmcErrorBit5()//废水箱满
                            && !VMCState.getIns().isVmcErrorBit6()//大门未关
                            && !VMCState.getIns().isVmcErrorBit7()//取杯门未关
                            && !VMCState.getIns().isVmcErrorBit8()//存储器错误
                            && !VMCState.getIns().isVmcErrorBit9())) //缺水
            {
                errorShow = VMCState.getIns().getVmc_error_content() + "," + MyApp.getIns().getString(R.string.sorrycontent);
                isFree = true;
                return;
            }

        }
        errorShow = "";
        isFree = false;
    }

    /** 返回errorShow：故障退款信息
     * 正在制作或者制作完成  && 制作状态没完成或失败：  返回errorShow
     * */
    public String getErrorShowView() {
        if (isFree && (isPickCuping() || isAllFinish()) && MakingPageMode.getIns().isMakeing()) {
            return errorShow;
        } else {
            return "";
        }
    }

}
