package com.fancoff.coffeemaker.net.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by apple on 2018/4/24.
 */

public class JsDownloadInterceptor implements Interceptor {
    private JsDownloadListener downloadListener;

    public JsDownloadInterceptor(JsDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    /**
     * intercept(Chain)方法进行拦截，返回一个Response对象，
     * 那么我们可以在这里通过Response对象的建造器Builder对其进行修改，把Response.body()替换成我们的JsResponseBody即可
     */
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(
                new JsResponseBody(response.body(), downloadListener)).build();
    }
}
