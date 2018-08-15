package com.fancoff.coffeemaker.net.download;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Description: 带进度 下载请求体
 */
public class JsResponseBody extends ResponseBody {

    private ResponseBody responseBody;

    private JsDownloadListener downloadListener;

    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private BufferedSource bufferedSource;

    public JsResponseBody(ResponseBody responseBody, JsDownloadListener downloadListener) {
        this.responseBody = responseBody;
        this.downloadListener = downloadListener;
    }

    /**返回响应内容的类型，比如image/jpeg*/
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    /**返回响应内容的长度*/
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    /**返回一个BufferedSource
     * BufferedSource可以理解为一个带有缓冲区的响应体，
     * 因为从网络流读入响应体的时候，Okio先把响应体读入一个缓冲区内，也即是BufferedSource。
     * */
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 利用Okio提供的ForwardingSource来包装我们真正的Source，并在ForwardingSource的read()方法内实现我们的接口回调
     */
    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;//不断统计当前下载好的数据
//                LogUtil.test( "download："+ (int) (totalBytesRead * 100 / responseBody.contentLength()));
                if (null != downloadListener) {
                    if (bytesRead != -1) {//接口回调
                        downloadListener.onProgress((int) (totalBytesRead * 100 / responseBody.contentLength()));
                    }
                }
                return bytesRead;
            }
        };
    }
}