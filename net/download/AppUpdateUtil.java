package com.fancoff.coffeemaker.net.download;

import android.support.annotation.NonNull;

import com.fancoff.coffeemaker.Application.FilesManage;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.net.ApiService;
import com.fancoff.coffeemaker.net.RequestBodyManage;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.StringUtil;
import com.fancoff.coffeemaker.utils.rx.RxAppTool;
import com.fancoff.coffeemaker.utils.rx.RxFileTool;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by apple on 2018/4/24.
 * apk下载类
 */

public class AppUpdateUtil {
    private static final String TAG = "DownloadUtils";
    private static final int DEFAULT_TIMEOUT = 15;
    static AppUpdateUtil ins;
    private Retrofit retrofit;
    int progress;

    /**返回下载的进度*/
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {//TaskUtil类中的runtimeToPushTask()方法中的app更新会调用该方法
        this.progress = progress;
    }

    private String baseUrl;
    boolean isDowning;

    /**返回是否在下载*/
    public boolean isDowning() {
        return isDowning;
    }

    public void setDowning(boolean downing) {
        isDowning = downing;
    }

    /**
     * 不在下载：本地有apk文件
     *     获取本地apk包名版本号，获取app版本号，包名相同返回apk版本号；return；
     * 删除文件
     */
    public String hasLocalUpdateApk() {
        if (!isDowning) {
            File file = new File(FilesManage.path.apk_path);
            if (file.exists()) {
                try {//得到apk包名、版本，app包名
                    String apkpac = RxAppTool.getApkPackageName(MyApp.getIns(), file.getAbsolutePath());
                    String apkVersionName = RxAppTool.getApkVersion(MyApp.getIns(), file.getAbsolutePath());
                    String apppac = RxAppTool.getAppPackageName(MyApp.getIns());
//                    int appversionCode = RxAppTool.getAppVersionCode(MyApp.getIns());
                    if (!StringUtil.isStringEmpty(apppac) && !StringUtil.isStringEmpty(apkpac) && apkpac.equals(apppac)) {
                        return apkVersionName;
                    }
                } catch (Exception e) {
                    LogUtil.error(e.toString());
                }

            }
        }

        RxFileTool.deleteFile(FilesManage.path.apk_path);
        return null;

    }

    /**
     *  打开apk文件
     */
    public void installLoacalApk() {
        RxAppTool.InstallAPK(MyApp.getIns(), FilesManage.path.apk_path);

    }

    public static AppUpdateUtil getIns() {
        if (ins == null) {
            ins = new AppUpdateUtil();
        }
        return ins;
    }

    /**
     * 新建一个ApiService，
     */
    private ApiService getApiService(JsDownloadListener listener) {

        JsDownloadInterceptor mInterceptor = new JsDownloadInterceptor(listener);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(RequestBodyManage.HOST_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        ApiService apiS = retrofit.create(ApiService.class);
        return apiS;
    }

    /**
     * 开始下载
     *
     */
    public void download(@NonNull String url, final String filePath, final JsDownloadListener listener) {
        progress = 0;
        isDowning = true;
        RxFileTool.createFileByDeleteOldFile(filePath);//删除原来的文件
        ApiService apiService = getApiService(listener);//新建一个ApiService
        listener.onStartDownload();   //调用TaskUtil类中runtimeToPushTask()方法中的onStartDownload
                                      //即isDowning=true；
        // subscribeOn()改变调用它之前代码的线程
        // observeOn()改变调用它之后代码的线程
        apiService.download(url)    //ApiService类中的download
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .map(new Function<ResponseBody, InputStream>() {//ResponseBody转换为InputStream
                    @Override
                    public InputStream apply(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
                        LogUtil.test("apply");
                        return responseBody.byteStream();  //返回字节流
                    }
                })
                .observeOn(Schedulers.computation()) // 用于计算任务
                .doOnNext(new Consumer<InputStream>() {
                    @Override
                    public void accept(InputStream inputStream) throws Exception {
                        LogUtil.test("saveFile");
                        RxFileTool.saveFile(inputStream, filePath);//保存inputStream到文件，保存apk到本地
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InputStream>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull InputStream inputStream) {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        listener.onFail(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        listener.onFinishDownload();
                        LogUtil.test("accept");
                    }
                });

    }


}
