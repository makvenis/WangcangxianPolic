package com.makvenis.dell.wangcangxianpolic.tools;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 重写请求定义
 */
public class ProgressRequestBodyListener extends RequestBody {

    //实际的待包装请求体
    public RequestBody requestBody;
    //进度回调接口
    public  OnProgressRequestListener onprogressListener;
    //包装完成的BufferedSink
    public BufferedSink bufferedSink;

    /*构造器*/
    /**
     * 构造函数，赋值
     * @param requestBody 待包装的请求体
     * @param Listener 回调接口
     */
    public ProgressRequestBodyListener(RequestBody requestBody, OnProgressRequestListener Listener) {
        this.requestBody = requestBody;
        this.onprogressListener = Listener;
    }

    @Override
    public MediaType contentType() {
        // 返回请求类型
        return requestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink k) throws IOException {
        if(bufferedSink == null){
            //包装
            bufferedSink = Okio.buffer(sink(k));
        }
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调
                onprogressListener.onRequestProgress(bytesWritten, contentLength, bytesWritten == contentLength);
            }
        };
    }

    /*回调监听接口*/
    public interface  OnProgressRequestListener{
        void onRequestProgress(long bytesWritten, long contentLength, boolean done);
    }

    /**
     * 包装请求体用于上传文件的回调
     * @param requestBody 请求体RequestBody
     * @param mprogressRequestListener 进度回调接口
     * @return 包装后的进度回调请求体
     */
    public static ProgressRequestBodyListener addProgressRequestListener(RequestBody requestBody, OnProgressRequestListener mprogressRequestListener){
        //包装请求体
        return new ProgressRequestBodyListener(requestBody,mprogressRequestListener);
    }

}
