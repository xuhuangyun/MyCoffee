package com.fancoff.coffeemaker.net;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.Application.DeviceInfo;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.bean.ErrorBean;
import com.fancoff.coffeemaker.bean.PayBean;
import com.fancoff.coffeemaker.bean.TaskBean;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.bean.heatbit.HeatParams;
import com.fancoff.coffeemaker.net.RequstBean.Order;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.MD5Util;
import com.fancoff.coffeemaker.utils.StringUtil;
import com.fancoff.coffeemaker.utils.rx.RxAppTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by apple on 2017/11/17.
 */

public class RequestBodyManage {


    public static final String HOST_URL_TEST = "http://dev.fancoff.net/";
    public static final String HOST_URL_ADMIN = "http://dev.fancoff.com/";
    public static final String HOST_URL = MyApp.TEST_HOST ? HOST_URL_TEST : HOST_URL_ADMIN;

//    public static final String HOST_URL = "http://www.baidu.com";
//    public static final String HOST_URL = "http://wdsc-api.tengw.cn/appversion/findlatest/";

    public static final String VERSION = "2.0";//
    public static final String KEY_TEST = "qweasd123";//KEY
    public static final String KEY_ADMIN = "123456uklopu9087";//KEY
    public static final String KEY = MyApp.TEST_HOST ? KEY_TEST : KEY_ADMIN;

    public static class ACITION_CODE {//接口编码:code
        public static final String TEST = "0000";//测试
        public static final String REGIST = "0001";//注册
        public static final String HEADBEAT = "0002";//心跳
        public static final String GET_PARAM = "0003";//获取工艺参数
        public static final String GET_PAY_CODE = "0004";//获取支付二维码
        public static final String GET_PAYRESULT = "0005";//获取支付状态
        public static final String UPLOAD_DATAS = "0006";//上传销售数据
        public static final String UPDATE_VERSION = "0007";//版本更新接口
        public static final String UPLOAD_LOGS = "0008";//上传日志
        public static final String GET_PICCK_CODE = "0009";//获取取货二维码
        public static final String PICK_BY_CODE = "0010";//二维码取货
        public static final String PICK_BY_NUMBER = "0011";//取货码取货
    }

    private static final RequestBodyManage ourInstance = new RequestBodyManage();

    public static RequestBodyManage getInstance() {
        return ourInstance;
    }

    private RequestBodyManage() {
    }

    /**从str序列中随机生成length个数*/
    public String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            //从这个随机数生成器的序列返回一个伪随机值，该值分布均匀，介于0(包括)和指定值(排除)之间。
            sb.append(str.charAt(number));
            //charAt:返回指定索引处的 char 值
        }
        return sb.toString();
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     *  jsonObject的公共参数给content；进行MD5加密并大写，然后进行返回签名结果；
     */
    public String getSign(JSONObject jsonObject, String Key) {
        String content = jsonObject.optString("code") +
                jsonObject.optString("device_id") +
                jsonObject.optString("nonce") +           //8位随机码
                jsonObject.optString("timestamp") +
                jsonObject.optString("version") + Key;
        return MD5Util.toMD5Capital(content).toUpperCase();


    }

    /**
     * ********************************************************接口********************************************************
     */
    /**
     * jsonObject放入公共参数：接口编码code，设备id：device_id，8位随机数nonce，当前时间timestamp，版本号version，签名sign；
     * 返回jsonObject
     */
    public JSONObject getBaseJson(String actionCode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", actionCode);
            jsonObject.put("device_id", DeviceInfo.getInstance().getDeviceId());
            jsonObject.put("nonce", getRandomString(8));
            jsonObject.put("timestamp", currentTimeMillis());
            jsonObject.put("version", VERSION);
            jsonObject.put("sign", getSign(jsonObject, KEY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }


    //测试
    public JSONObject getTestJson() {
        JSONObject data = getBaseJson(ACITION_CODE.TEST);
        try {
            data.put("deviceType", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    //注册

    /**
     * 公共参数，app版本号，主控版本软件，销售咖啡种类数，工艺参数对象，咖啡参数标识符，机器参数标识符，机器类型
     * 返回封装好的data数据；（有些参数需要有配置表的时候才能获得）
     */
    public JSONObject getRegistJson() {
        JSONObject data = getBaseJson(ACITION_CODE.REGIST);//公共参数
        try {
            data.put("app_version", RxAppTool.getAppVersionName(MyApp.getIns()));
            data.put("controller_version", DeviceInfo.getInstance().getController_version()); //"178"
            data.put("coffee_type", DataCenter.getInstance().getGoodSize());   //咖啡种类数，在咖啡工艺表
            data.put("machine_type", DataCenter.getInstance().getMachine_type());  //机器类型在机器工艺表里
            JSONObject tech_config = new JSONObject();//工艺参数对象
            tech_config.put("coffee", DataCenter.getInstance().getCoffee());//咖啡工艺的版本号
            tech_config.put("machine", DataCenter.getInstance().getMachine());//机器工艺的版本号
            data.put("tech_config", tech_config);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public interface HeadBeatCallBack {
        void hasFile(JSONObject jsonObject, boolean hasfile);
    }

    /**
     * 心跳：公共参数、（咖啡机温度、辅助锅炉温度）、错误列表、push列表
     *  NetUtil类实现HeadBeatCallBack接口方法hasFile
     */
    public void getHeadBeatJson(HeatParams h, HeadBeatCallBack headBeatCallBack) {
        boolean hasFile = false;
        JSONObject data = getBaseJson(ACITION_CODE.HEADBEAT);

        try {
            JSONObject temper = new JSONObject();
            temper.put("main", DataCenter.getInstance().getMainTemp());
            temper.put("assist", DataCenter.getInstance().getAssistTemp());
            data.put("temper", temper);

            /**********************封装错误*****************/
            if (h.getError() != null && h.getError().size() > 0) {//有错误
                JSONArray jsonArray = new JSONArray();  //code1、msg1；code2、msg2 ......
                for (ErrorBean errorbean : h.getError()) {
                    JSONObject jsonObject = new JSONObject();//code、msg
                    jsonObject.put("code", errorbean.getCode());
                    jsonObject.put("msg", errorbean.getMsg());
                    jsonArray.put(jsonObject);
                }
                data.put("error", jsonArray);
            }
            LogUtil.debugNet("heatBeat：" + data.toString());
            /************************** push *****************************/
            if (h.getPush() != null
                    && h.getPush().size() > 0) {//有push
                JSONArray jsonArray = new JSONArray();
                for (TaskBean push : h.getPush()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", push.getId());
                    jsonObject.put("result", push.getResult());
                    String file = push.getObj();//截屏或日志内容
                    if (!StringUtil.isStringEmpty(file)) {//文件内容不为空
                        hasFile = true;
                        if (file.length() < 100) {//打印长度小于20的日志 防止日志重复
                            jsonObject.put("file", file);
                            LogUtil.debugNet("heatBeat push with file：" + jsonObject.toString());
                        } else {
                            LogUtil.debugNet("heatBeat push with file：" + jsonObject.toString());
                            jsonObject.put("file", file);
                        }
                    } else {//文件内容为空，打印
                        LogUtil.debugNet("heatBeat push ：" + jsonObject.toString());
                    }
                    jsonArray.put(jsonObject);
                }
                data.put("push", jsonArray);
            }

        } catch (JSONException e) {
            LogUtil.error(e.toString());
        }
        headBeatCallBack.hasFile(data, hasFile);
    }

    /**
     * 获取工艺参数
     * 公共参数、咖啡工艺编号、机器工艺编号；
     */
    public JSONObject getParamJson(String coffee, String machine) {
        JSONObject data = getBaseJson(ACITION_CODE.GET_PARAM);
        try {
            JSONObject tech_config = new JSONObject();//工艺参数对象
            tech_config.put("coffee", coffee);//咖啡工艺编号
            tech_config.put("machine", machine);//机器工艺编号
            data.put("tech_config", tech_config);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 获取支付二维码的请求参数
     *  公共参数、订单号、支付类型、商品列表（id、甜度、价格）
     */
    public JSONObject getQrCodeJson(PayBean payBean, int paytype) {

        JSONObject data = getBaseJson(ACITION_CODE.GET_PAY_CODE);

        try {
            data.put("out_trade_no", payBean.getOut_trade_no());
            JSONArray jsonArray = new JSONArray();
            for (CoffeeBean g : payBean.getGoods()) {
                JSONObject good = new JSONObject();
                good.put("id", g.getId());
                good.put("sugar_level", g.getSugarType());
                good.put("price", g.getPayPrice());
                jsonArray.put(good);
            }

            data.put("type", paytype);

            data.put("goods", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 获取支付状态
     * 公共参数、订单号
     */
    public JSONObject getPayResultJson(final PayBean payBean) {
        JSONObject data = getBaseJson(ACITION_CODE.GET_PAYRESULT);
        try {
            data.put("out_trade_no", payBean.getOut_trade_no());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 上传销售数据
     * 公共参数、销售时间、服务订单号；
     */
    public JSONObject getUploadDatasJson(Order order) {
        JSONObject data = getBaseJson(ACITION_CODE.UPLOAD_DATAS);
        try {
            data.put("sale_time", order.getSale_time());
            data.put("order_id", order.getOrder_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    //版本更新接口
    public JSONObject getUpdataVersionJson() {
        JSONObject data = getBaseJson(ACITION_CODE.UPDATE_VERSION);
        try {
            data.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    //上传日志
    public JSONObject getUploadLogJson() {
        JSONObject data = getBaseJson(ACITION_CODE.UPLOAD_LOGS);
        try {
            data.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    //获取取货二维码
    public JSONObject getGetPickQrCodeJson() {
        JSONObject data = getBaseJson(ACITION_CODE.GET_PICCK_CODE);
        try {
            data.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    //二维码取货
    public JSONObject getPickByQrCodeJson() {
        JSONObject data = getBaseJson(ACITION_CODE.PICK_BY_CODE);
        try {
            data.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    //取货码取货
    public JSONObject getPickByNumberJson() {
        JSONObject data = getBaseJson(ACITION_CODE.PICK_BY_NUMBER);
        try {
            data.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

}
