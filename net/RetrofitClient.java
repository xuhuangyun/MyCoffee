package com.fancoff.coffeemaker.net;

import com.fancoff.coffeemaker.net.RequstBean.CoffeeConfigResultBean;
import com.fancoff.coffeemaker.net.RequstBean.HeadBeatBean;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by apple on 2017/11/17.
 */

public class RetrofitClient {
    private static final RetrofitClient ourInstance = new RetrofitClient();

    public static RetrofitClient getInstance() {
        return ourInstance;
    }

    HashMap<String, ApiService> hashMap = new HashMap<>();

    private RetrofitClient() {
    }

    Retrofit retrofit;

    /**
     * 用来返回TIME_OUT和key对应的apiService：
     *  传入超时变量和字符串key；
     *  从HashMap<String, ApiService> hashMap中获取TIME_OUT+key对应的apiService；
     *  apiService不为空：
     *      返回apiService；
     *  apiService为空：
     *      创建OkHttpClient client，
     *      Retrofit retrofit（关联okhttp）
     *      retrofit.create(ApiService.class);  //建立apiService
     *      将apiService添加到hashMap中；
     *      返回apiService
     */
    private ApiService getRetrofitService(int TIME_OUT, String key) {
        ApiService apiService = hashMap.get(TIME_OUT + key);
        if (apiService != null) {
            return apiService;
        } else {
            /**
             * 构造函数私有化
             * 并在构造函数中进行retrofit的初始化
             */
            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
            OkHttpClient client = httpBuilder  //配置OKhttp请求参数
                    .addInterceptor(new LoggingInterceptor())  //添加头部信息
                    .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(TIME_OUT, TimeUnit.SECONDS) //设置超时
                    .retryOnConnectionFailure(false)//方法为设置出现错误不进行重新连接
                    .build();
            /**
             * 由于retrofit底层的实现是通过okhttp实现的，所以可以通过okHttp来设置一些连接参数
             * 如超时等
             */
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RequestBodyManage.HOST_URL) //添加基础url
                    .client(client) //关联OKhttp
                    .addConverterFactory(GsonConverterFactory.create())//设置请求回来的数据为Gson
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//设置service接口可以作为Observable返回
                    .build();

            ApiService apiS = retrofit.create(ApiService.class);  //建立apiService
            hashMap.put(TIME_OUT + key, apiS);
            return apiS;
        }

    }

    private static final int TIME_OUT_FILE = 360;
    private static final int TIME_OUT_PARAMS = 30;
    private static final int TIME_OUT = 5;

    /**
     * 将json数据，根据指定格式转换为RequestBody body，并返回RequestBody body
     */
    public RequestBody initRequestBody(JSONObject jsonObject) {
        RequestBody body = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                jsonObject.toString());//post上传json
        return body;

    }

    /**
     * sub订阅observer；
     */
    public <T> void initObser(Observable<T> observer, Observer<T> sub) {
        observer.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sub);
    }

    /**
     * sub订阅observer，sub进行接收，接收进行next处理；
     */
    public <T> void initNextObser(Observable<T> observer, Observer<T> sub, Consumer<T> next) {
        observer.doOnNext(next)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(sub);
    }

    public <T> void initTimeObser(Observable<T> observer, Observer<T> sub) {
        observer
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sub);
    }

    public void test(JSONObject json, Observer<TestBean> observer) {
        Observable<TestBean> ob = getRetrofitService(TIME_OUT, "test").test(initRequestBody(json));
        initObser(ob, observer);
    }

    /**
     * 注册：
     *  1、打印注册json数据；
     *  2、getRetrofitService(TIME_OUT, "regist")：得到TIME_OUT和regist对应的apiService；
     *  3、initRequestBody(json)：json数据根据指定格式转换为RequestBody；
     *  4、apiService的regist得到Observable<RegistBean> ob
     *  5、传入的observer订阅创建的ob，并进行监听；
     */
    public void regist(JSONObject json, Observer<RegistBean> observer) {
        LogUtil.debugNet("regist：" + json.toString());
        Observable<RegistBean> ob = getRetrofitService(TIME_OUT, "regist").regist(initRequestBody(json));
        initObser(ob, observer);
    }

    /**
     * 心跳：
     *  1、getRetrofitService(file ? TIME_OUT_FILE : TIME_OUT_PARAMS, "headBeat")：得到对应的apiService；
     *  2、initRequestBody(json)：json数据根据指定格式转换为RequestBody；
     *  3、apiService的headBeat得到Observable<RegistBean> ob
     *  4、传入的observer订阅创建的ob，ob发送next，observer进行监听；
     */
    public void headBeat(JSONObject json, Observer<HeadBeatBean> observer, Consumer<HeadBeatBean> next, boolean file) {
        Observable<HeadBeatBean> ob = getRetrofitService(file ? TIME_OUT_FILE : TIME_OUT_PARAMS, "headBeat").headBeat(initRequestBody(json));
        initNextObser(ob, observer, next);
    }

    //获取工艺参数
    public void param(JSONObject json, Observer<CoffeeConfigResultBean> observer, Consumer<CoffeeConfigResultBean> next) {
        LogUtil.debugNet("getCoffeeConfig：" + json.toString());

        Observable<CoffeeConfigResultBean> ob = getRetrofitService(TIME_OUT_PARAMS, "param").param(initRequestBody(json));
        initNextObser(ob, observer, next);
    }

    //获取支付二维码
    public void getPayCode(JSONObject json, Observer<PayQrCodeBean> observer) {
        LogUtil.debugNet("get Pay qrCode：" + json.toString());
        Observable<PayQrCodeBean> ob = getRetrofitService(TIME_OUT, "getPayCode").payqrcode(initRequestBody(json));
        initObser(ob, observer);
    }

    public interface PayCallBack {
        void onsuccess(PayResultBean payResultBean);

    }

    /**
     * 获取支付状态
     *   打印日志：getPayState + 请求参数
     *   POST请求支付状态：同步阻塞式请求，结果返回PayResultBean tasks
     *   回调payCallBack.onsuccess(tasks);
     */
    public Call<PayResultBean> payResult(JSONObject json, PayCallBack payCallBack) {
        LogUtil.debugNet("getPayState：:" + json.toString());
        Call<PayResultBean> ob = getRetrofitService(TIME_OUT, "payResult").payResult(initRequestBody(json));
        PayResultBean tasks = null;
        try {
            tasks = ob.execute().body();//同步请求 非异步方式，会阻塞线程，等待返回结果

        } catch (IOException e) {
            LogUtil.error("payResult IOException :" + e.toString());
        }
        payCallBack.onsuccess(tasks);
        return ob;
    }

    /**
     * 上传销售数据：
     *   打印日志：DEBUG_OKHTTP:upload Sell datas：{"code":"0006","device_id":"d3936a2c8d3970e2","nonce":"vipg43bw","timestamp":1532579703429,"version":"2.0","sign":"32DE95BB17F0850B82A119AA16450424","sale_time":"20180726123503","order_id":259393}
     *             2018-07-26 12:35:04,105-[120] DEBUG_ACTION:请取杯，剩余37秒
     *
     */
    public void uploadData(JSONObject json, Observer<UploadDatasBean> observer, Consumer<UploadDatasBean> next) {
        LogUtil.debugNet("upload Sell datas：" + json.toString());
        Observable<UploadDatasBean> ob = getRetrofitService(TIME_OUT, "uploadData").uploadData(initRequestBody(json));
        initNextObser(ob, observer, next);
    }

    //同步上传销售数据
    public UploadDatasBean uploadDataSys(JSONObject json) {
        LogUtil.debugNet("upload Sell datas：" + json.toString());
        Call<UploadDatasBean> ob = getRetrofitService(TIME_OUT, "uploadDataSys").uploadDataSys(initRequestBody(json));
        UploadDatasBean tasks;
        try {
            tasks = ob.execute().body();
            return tasks;
        } catch (IOException e) {
            LogUtil.error(e.toString());
        }
        return null;
    }

    //版本更新接口
    public void updataVersion(JSONObject json, Observer<VersionBean> observer) {
        LogUtil.debugNet("updte app version：" + json.toString());
        Observable<VersionBean> ob = getRetrofitService(TIME_OUT, "updataVersion").version(initRequestBody(json));
        initObser(ob, observer);
    }

//    //上传日志
//    public void uploadLog(JSONObject json, Observer<UploadLogBean> observer) {
//        LogUtil.debugNet("upload logs");
//        Observable<UploadLogBean> ob = getRetrofitService(TIME_OUT,"uploadLog").uploadLog(initRequestBody(json));
//        initObser(ob, observer);
//    }

    //获取取货二维码
    public void getPickCode(JSONObject json, Observer<PickQrCodeBean> observer) {
        LogUtil.debugNet("获取取货二维码：" + json.toString());
        Observable<PickQrCodeBean> ob = getRetrofitService(TIME_OUT, "getPickCode").pickQrcode(initRequestBody(json));
        initObser(ob, observer);
    }

    //二维码取货
    public void pickByCodeBean(JSONObject json, Observer<PickByCodeBean> observer) {
        LogUtil.debugNet("二维码取货：" + json.toString());
        Observable<PickByCodeBean> ob = getRetrofitService(TIME_OUT, "pickByCodeBean").pickbycode(initRequestBody(json));
        initObser(ob, observer);
    }

    //取货码取货
    public void pickByNumber(JSONObject json, Observer<PickByNumberBean> observer) {
        LogUtil.debugNet("取货码取货：" + json.toString());
        Observable<PickByNumberBean> ob = getRetrofitService(TIME_OUT, "pickByNumber").pickByNumer(initRequestBody(json));
        initObser(ob, observer);
    }

}
