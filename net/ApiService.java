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
import com.fancoff.coffeemaker.net.RequstBean.UploadLogBean;
import com.fancoff.coffeemaker.net.RequstBean.VersionBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by apple on 2017/11/17.
 *接口
 */

public interface ApiService {

    // 测试
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/")
    Observable<TestBean> test( @Body RequestBody jsonBody);

    //注册
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/v2/")
    Observable<RegistBean> regist( @Body RequestBody jsonBody);

    //心跳
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/v2/")
    Observable<HeadBeatBean> headBeat(
            @Body RequestBody jsonBody);

    //取工艺参数
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/v2/")
    Observable<CoffeeConfigResultBean> param(
            @Body RequestBody jsonBody);

    //获取支付二维码
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/v2/")
    Observable<PayQrCodeBean> payqrcode(
            @Body RequestBody jsonBody);

    //获取支付状态
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/v2/")
    Call<PayResultBean> payResult(
            @Body RequestBody jsonBody);

    //上传销售数据
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/v2/")
    Observable<UploadDatasBean> uploadData(
            @Body RequestBody jsonBody);
    //同步上传销售数据
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/v2/")
    Call<UploadDatasBean> uploadDataSys(
            @Body RequestBody jsonBody);

    //版本更新接口
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/v2/")
    Observable<VersionBean> version(
            @Body RequestBody jsonBody);

    //上传日志
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/v2/")
    Observable<UploadLogBean> uploadLog(
            @Body RequestBody jsonBody);

    //获取取货二维码
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/v2/")
    Observable<PickQrCodeBean> pickQrcode(
            @Body RequestBody jsonBody);

    //二维码取货
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/v2/")
    Observable<PickByCodeBean> pickbycode(
            @Body RequestBody jsonBody);

    //取货码取货
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/v2/")
    Observable<PickByNumberBean> pickByNumer(
            @Body RequestBody jsonBody);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
