package com.fancoff.coffeemaker.net;

import com.fancoff.coffeemaker.utils.log4j.LogUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
/*
    接口日志拦截
 */

/**
 * okhttp拦截日志，主要用来打印网络返回的数据：如
 *      2018-07-02 11:01:41,297-[120] DEBUG_OKHTTP:responseBody{"return_code":1}
 */
public class LoggingInterceptor implements Interceptor {
    private final Charset UTF8 = Charset.forName("UTF-8");

    /**
    * 在拦截器的回调中，我们可以拿到两个重要的参数Request和Response，
    * 而接口在回调时，会接收一个Chain类型的参数，这个参数保存了Request和Response的相关数据。
     */
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();   //请求体
        long t1 = System.nanoTime();//请求发起的时间
        String body = null;

        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            body = buffer.readString(charset);
        }

        Response response = chain.proceed(request);
        //在没有本地缓存的情况下，每个拦截器都必须至少调用chain.proceed(request)一次
        //用来发起请求

        long t2 = System.nanoTime();//收到响应的时间

        ResponseBody responseBody = response.peekBody(1024 * 1024);//长度
        LogUtil.debugNet("responseBody" + responseBody.string());
        //举例： 2018-07-02 11:01:41,297-[120] DEBUG_OKHTTP:responseBody{"return_code":1}

//        try {
//            JSONObject bodyjson = new JSONObject(body);
//            String code = bodyjson.getString("code");
//            if (!code.equals("0005")) {
//                if (code.equals("0002")) {
////                    LogUtil.debugNet("t1:" + t1 + "headBeat：" + body, 10000);
//                    LogUtil.debugNet("headBeat " +
//                            body + " \nresponseBody" + responseBody.string(), 10000);//控制心跳打印频率最低10秒
//                } else {
//                    LogUtil.debugNet("responseBody" + responseBody.string());
//                }
//
//            }
//        } catch (JSONException e) {
//            LogUtil.error(e.toString());
//        }


        return response;
    }
}