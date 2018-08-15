package com.fancoff.coffeemaker.Application;

/**
 * Created by apple on 2017/10/11.
 */

public class MyConstant {
    public static class APP_ERROR_CODE {
        public static final String ERROR0001 = "app_error0001";//本地无机器配置表
        public static final String ERROR0002 = "app_error0002";//本地无工艺表
        public static final String ERROR0003 = "app_error0003";//咖啡工艺解析出错
        public static final String ERROR0004 = "app_error0004";//机器配置解析出错
        public static final String ERROR0005 = "app_error0005";//校验异常
        public static final String ERROR0006 = "app_error0006";//app与vmc通讯超时


        public static final String ERROR0001_S = "本地无机器配置表";
        public static final String ERROR0002_S = "本地无工艺表";
        public static final String ERROR0003_S = "咖啡工艺解析出错";
        public static final String ERROR0004_S = "机器配置解析出错";
        public static final String ERROR0005_S = "校验异常";
        public static final String ERROR0006_S = "app与vmc通讯超时";

    }
    public class ERROR_CODE {
        public final static String COFFEE_ERROR_BIT0 = "cf_error0001";//DOSER 故障 故障等级1
        public final static String COFFEE_ERROR_BIT1 = "cf_error0002";//咖啡机加热器故障 故障等级2
        public final static String COFFEE_ERROR_BIT2 = "cf_error0004";//咖啡机流量计故障 故障等级3
        public final static String COFFEE_ERROR_BIT3 = "cf_error0008";//压粉电机超时故障 故障等级2
        public final static String COFFEE_ERROR_BIT4 = "cf_error0010";//压粉机构安装故障 故障等级3
        public final static String COFFEE_ERROR_BIT5 = "cf_error0020";//缺豆指示 故障等级1
        //    private int[] CF_ERROR_LEVER_ARR = new int[]{1, 2, 3, 2, 3, 1};//故障等级
        public final static String VMC_ERROR_BIT0 = "vmc_error0001";//咖啡机通信超时 故障等级3
        public final static String VMC_ERROR_BIT1 = "vmc_error0002";//VMC 加热故障 故障等级3
        public final static String VMC_ERROR_BIT2 = "vmc_error0004";//缺杯 故障等级2
        public final static String VMC_ERROR_BIT3 = "vmc_error0008";//落杯器故障 故障等级3
        public final static String VMC_ERROR_BIT4 = "vmc_error0010";//导轨运动故障 故障等级3
        public final static String VMC_ERROR_BIT5 = "vmc_error0020";//废水箱满 故障等级1
        public final static String VMC_ERROR_BIT6 = "vmc_error0040";//大门未关指示 故障等级2
        public final static String VMC_ERROR_BIT7 = "vmc_error0080";//取杯门未关指示 故障等级1
        public final static String VMC_ERROR_BIT8 = "vmc_error0100";//存储器错误 故障等级3
        public final static String VMC_ERROR_BIT9 = "vmc_error0200";//缺水 故障等级2
        public final static String VMC_ERROR_BIT10 = "vmc_error0400";//保留
        public final static String VMC_ERROR_BIT11 = "vmc_error0800";//保留
        public final static String VMC_ERROR_BIT12 = "vmc_error1000";//保留
        public final static String VMC_ERROR_BIT13 = "vmc_error2000";//落杯器卡杯
        public final static String VMC_ERROR_BIT14 = "vmc_error4000";//主状态机超时 故障等级1
        public final static String VMC_ERROR_BIT15 = "vmc_error8000";//有脏杯或手动放杯时长时间未检测到杯子 故障等级1
        public final static String VMC_STETE_CLEANING = "vmc_state0001";//vmc清洗中状态
            }
    public static class ACTION {
        public static final int INTENT_TO_BUY_FRAGMENT = 1;//购物
        public static final int INTENT_TO_PAY_FRAGMENT = 2;//付款
        public static final int INTENT_TO_PICK_FRAGMENT = 3;//取货
        public static final int INTENT_TO_SHOWIMAGE = 31;//大图
        public static final int INTENT_TO_BUYCAR_FRAGMENT = 4;//购物车
        public static final int INTENT_TO_PERSENAL_FRAGMENT = 5;//私人订制
        public static final int INTENT_TO_SCREEN_FRAGMENT = 6;//屏保
        public static final int INTENT_TO_LOGO_FRAGMENT = 8;//logo
        public static final int INTENT_TO_SETTING_FRAGMENT = 9;  /*工程菜单界面*/
        public static final int INTENT_TO_REMOVE = 0;//
        public static final int INTENT_TO_ADDCAR = 10;//
        public static final int INTENT_TO_MAKING = 11;//
        public static final int INTENT_TO_PICK_MAKING = 12;//
        public static final int INTENT_TO_DIALOG_WR = 18;//
        public static final int REF_ALL = 15;
        public static final int REF_ALL_NOGOODS = 16;
        public static final int RESUME_APP = 20;
        public static final int RESUME_FLOAT = 21;
        public static final int HIDE_FLOAT = 22;

    }

    public static class WHAT {
        public static final int MAIN_ONRESUME = 1;//
        public static final int MAIN_ONPAUSE = 2;//
        public static final int SHOW_PROGRESS = 3;//
        public static final int HIDE_PROGRESS = 4;//

    }

    public static class PAY {
        public static final int PAY_TYPE_WEIXIN = 2;//
        public static final int PAY_TYPE_AIPAY = 1;//
        public static final int PAY_TYPE_HE = 16;//
        public static final int PAY_TYPE_CAR = 3;//

    }

    public static class SUGAR {
        public static final int SUGAR_NO = 0;//
        public static final int SUGAR_LOW = 1;//
        public static final int SUGAR_MIDDLE = 2;//
        public static final int SUGAR_HIGHT = 3;//

    }
}
