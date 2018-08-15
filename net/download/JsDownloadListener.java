package com.fancoff.coffeemaker.net.download;

/**
 * Description: 下载进度回调
 * //TaskUtil类中的runtimeToPushTask()方法实现该接口方法
 */
public interface JsDownloadListener {

    void onStartDownload();

    void onProgress(int progress);

    void onFinishDownload();

    void onFail(String errorInfo);

}