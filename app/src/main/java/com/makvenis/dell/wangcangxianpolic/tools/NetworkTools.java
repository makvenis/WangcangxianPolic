package com.makvenis.dell.wangcangxianpolic.tools;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 数据网络类
 */
public class NetworkTools {
    /* 用户登陆请求 */
    /**
     * @serialData 使用方法 将请求的地址给定到？之处。再将键值分别置于两组string[] 数组之中
     * @param key 请求的键
     * @param value 请求的值
     * @param path 请求路径 代指相对地址
     */
    public static String GetApacheUserLoginRegister(String[] key,String[] value,String path) {
        if(key.length == value.length && path != null ) {
            String str="";
            for (int i = 0; i < key.length; i++) {
                if(i == 0) {
                    str = "?"+key[0]+"="+value[0]+"&";
                }else if(i == key.length-1) {
                    str+=key[i]+"="+value[i];
                }else {
                    str+=key[i]+"="+value[i]+"&";
                }
            }
            return path+str;
        }else {
            new Exception("键值对不能为空,并且地址不能为空"+key.length+value.length+path);
            return null;
        }
    }


    public static void OkHttpUtils(String[] key, String[] value, String path, final Handler handler){

        String mPath = GetApacheUserLoginRegister(key, value, path);
        Log.e("TAG","用户登陆请求全地址:"+mPath);
        //创建okhttp实例
        OkHttpClient httpClient = new OkHttpClient();
        //创建请求实例 默认为GET请求
        final Request request = new Request.Builder()
                .url(mPath)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call arg0, Response response) throws IOException {
                String Rstring = response.body().string();
                if(Rstring == ""){
                    Message msg=new Message();
                    msg.what=0X000003;
                    msg.obj="ERROR";
                    handler.sendMessage(msg);
                }else {
                    Message msg=new Message();
                    msg.what=0X000003;
                    msg.obj=Rstring;
                    handler.sendMessage(msg);
                }



                Log.e("TAG","用户登陆请求结果:"+Rstring);
            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {
                // TODO Auto-generated method stub

            }
        });
    }

    /*下载工具类，返回byte[] 数组*/
    public static byte[] NetShow(String path){

        try {
            URL u=new URL(path);
            HttpURLConnection conn= (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            if(conn.getResponseCode()==200){
                InputStream k = conn.getInputStream();
                int len=0;
                byte[] by=new byte[1024];
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                while ((len=k.read(by))!=-1){
                    baos.write(by,0,len);
                }
                byte[] bytes = baos.toByteArray();
                return bytes;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


    /*下载工具类，返回byte[] 数组*/
    /*带进度的下载*/
    public static byte[] NetShow(String path,Handler mHandler){

        try {
            Log.e("TAG","调用netShow()开始");
            URL u=new URL(path);
            HttpURLConnection conn= (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            long zongSize = conn.getContentLength();
            Log.e("TAG", "=====");
            if(conn.getResponseCode()==200){
                InputStream k = conn.getInputStream();
                int len = 0;
                byte[] by=new byte[1024];
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                long nowSize = 0;
                while ((len=k.read(by))!=-1){
                    baos.write(by,0,len);
                    //获取当前写入的长度
                    nowSize += baos.size();
                    //Log.e("TAG","当前大小"+nowSize+"/"+zongSize);
                }
                Log.e("TAG","当前大小"+nowSize+"/"+zongSize);

                Log.e("TAG","读写完成");
                Message msg = new Message();

                msg.what = 0X000002;
                msg.obj = "Down";
                mHandler.sendMessage(msg);


                byte[] bytes = baos.toByteArray();
                return bytes;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


    /*OKhttp请求类*/
    public static void getHttpTools(String path, final Handler handler){
        //"http://192.168.1.3:8080/kk/index.html"
        //创建okhttp实例
        OkHttpClient httpClient = new OkHttpClient();
        //创建请求实例 默认为GET请求
        Request request = new Request.Builder()
                .url(path)
                .build();
        Call call = httpClient.newCall(request);
        //这子线程
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("TAG",string);
                Message msg = new Message();
                msg.what = Configfile.GET_CODE;
                msg.obj = string;
                handler.sendMessage(msg);
            }
        });
    }

    /*OKhttp请求类 带message的函数*/
    public static void getHttpTools(String path, final Handler handler,final int code){
        //"http://192.168.1.3:8080/kk/index.html"
        //创建okhttp实例
        OkHttpClient httpClient = new OkHttpClient();
        //创建请求实例 默认为GET请求
        Request request = new Request.Builder()
                .url(path)
                .build();
        Call call = httpClient.newCall(request);
        //这子线程
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("TAG",string);
                Message msg = new Message();
                msg.what = code;
                msg.obj = string;
                handler.sendMessage(msg);
            }
        });
    }


    /*okHttp下载类*/
    /*地址均为完整的地址*/
    public static void downloadTools(String path, final Handler handler){
        OkHttpClient httpClient = new OkHttpClient();

        //解析文件格式
        //String path = "http://192.168.1.3:8080/kk/mousic.mp3";
        //获取文件格式
        String[] split = path.split("/");
        //获取数组最后一位
        final String s = split[split.length - 1];
        Request request = new Request.Builder()
                .url(path)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                Log.d("pppp", SDPath);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(SDPath, s);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.d("h_bl", "progress=" + progress);
                        Message msg = handler.obtainMessage();
                        msg.what = Configfile.DOWN_CODE;
                        msg.arg1 = progress;
                        handler.sendMessage(msg);
                    }
                    fos.flush();
                    Log.d("h_bl", "文件下载成功");
                } catch (Exception e) {
                    Log.d("h_bl", "文件下载失败");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /*okHttp下载类 没有后缀名*/
    /*地址均为完整的地址*/
    public static void downloadToolsNo(String path, final Handler handler){
        OkHttpClient httpClient = new OkHttpClient();

        //解析文件格式
        //String path = "http://192.168.1.3:8080/kk/mousic.mp3";
        //获取文件格式
/*        String[] split = path.split("/");
        //获取数组最后一位
        final String s = split[split.length - 1];*/
        Request request = new Request.Builder()
                .url(path)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                Log.d("pppp", SDPath);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(SDPath, "test.apk");
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.d("h_bl", "progress=" + progress);
                        Message msg = handler.obtainMessage();
                        msg.what = Configfile.DOWN_CODE;
                        msg.arg1 = progress;
                        handler.sendMessage(msg);
                    }
                    fos.flush();
                    Log.d("h_bl", "文件下载成功");
                } catch (Exception e) {
                    Log.d("h_bl", "文件下载失败");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /* OkHttpPost请求 */
     /*OKhttpPOST请求类*/
    /*这里需要重新定义请求头和值*/

    /**
     *
     * @param path
     * @param handler
     * @param user
     */
    public static void postHttpToolsUaerRegistite(String path, final Handler handler, List<String> user){

        String userName = user.get(0);
        String userPass = user.get(1);
        String userTel = user.get(2);
        String userSFZH = user.get(3);
        //创建实例
        OkHttpClient httpClient = new OkHttpClient();
        //请求的对象（类似html的表单请求）
        FormBody body = new FormBody.Builder()
                .add("zc_key1", userName)
                .add("zc_key2", userPass)
                .add("zc_key3", userTel)
                .add("zc_key4", userSFZH)
                .build();

        //创建请求
        Request request = new Request.Builder()
                .url(path)
                .post(body)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Message msg = new Message();
                msg.what = Configfile.USER_PASS;
                msg.obj = string;
                handler.sendMessage(msg);
            }
        });

    }


    public static void postHttpToolsUaerRegistite(String path, final Handler handler, String mDatajson,String name){

        //创建实例
        OkHttpClient httpClient = new OkHttpClient();
        //请求的对象（类似html的表单请求）
        FormBody body = new FormBody.Builder()
                .add("dataJson", mDatajson)
                .add("username", name)
                .build();

        //创建请求
        Request request = new Request.Builder()
                .url(path)
                .post(body)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Message msg = new Message();
                msg.what = Configfile.CALLBANK_POST_MSG;
                msg.obj = string;
                handler.sendMessage(msg);
            }
        });

    }


    public static void postHttpToolsUaerRegistite(String path, final Handler handler, String mDatajson){

        //创建实例
        OkHttpClient httpClient = new OkHttpClient();
        //请求的对象（类似html的表单请求）
        FormBody body = new FormBody.Builder()
                .add("dataJson", mDatajson)
                .build();

        //创建请求
        Request request = new Request.Builder()
                .url(path)
                .post(body)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Message msg = new Message();
                msg.what = Configfile.CALLBANK_POST_MSG;
                msg.obj = string;
                handler.sendMessage(msg);
            }
        });

    }


    public static void postHttpTools(String path, final Handler handler, List<String> user){

        String userName = user.get(0);
        String userPass = user.get(1);
        //创建实例
        OkHttpClient httpClient = new OkHttpClient();
        //请求的对象（类似html的表单请求）
        FormBody body = new FormBody.Builder()
                .add("key1", userName)
                .add("key2", userPass)
                .build();

        //创建请求
        Request request = new Request.Builder()
                .url(path)
                .post(body)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Message msg = new Message();
                msg.what = Configfile.USER_PASS;
                msg.obj = string;
                handler.sendMessage(msg);
            }
        });

    }

    //不带进度条的上传
    /*单纯上传*/
    public static void upload(String path, Context context){
        //获取文件路径
        String mPath =  Configfile.APP_NAME + "UploadServlet";
        Log.e("qq_url",mPath);
        MediaType type= MediaType.parse("multipart/form-data");
            OkHttpClient http=new OkHttpClient();
            //File mFile=new File("D:/json.jar");
            File mFile=new File(path);
            if (!mFile.exists()) {
                //System.out.println("");
                Toast.makeText(context,"文件不存在",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context,"文件存在",Toast.LENGTH_SHORT).show();
                RequestBody fileBody = RequestBody.create(type, mFile);
                RequestBody requestBody = new MultipartBody.Builder()
                        .addFormDataPart("uploadfile", mFile.getName(), fileBody)
                        .build();

                Request requestPostFile = new Request.Builder()
                        .url(mPath)
                        .post(requestBody)
                        .build();
                http.newCall(requestPostFile).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
            }

        }

    //带进度条的上传
    /*文件上传*/
    /**
     * @param strPath 上传文件的路径
     * @param context
     * @param handler
     * @param servicePath 服务器地址
     */
    public static void upload(String strPath, Context context, final Handler handler, String servicePath) {
        //服务器接受文件路径
        //String mPath = Configfile.APP_NAME + "query/UploadServlet";

        //获取文件路径
        MediaType type = MediaType.parse("multipart/form-data");
        OkHttpClient http = new OkHttpClient();
        File mFile = new File(strPath);
		 /*文件后缀及其名称获取*/
        final String[] splite = strPath.split("/");
        int len = splite.length;
        String da = splite[len - 1];

        if (!mFile.exists()) {
            //System.out.println("文件不存在!");
            Toast.makeText(context,"文件不存在",Toast.LENGTH_SHORT).show();
        } else {
            RequestBody fileBody = RequestBody.create(type, mFile);
            RequestBody requestBody = new MultipartBody.Builder()
                    /**
                     * @? addFromDataPart(服务器接收的路径, 文件名, 文件的类型(提交方式));
                     * @？ da 服务器接收文件的文件名称
                     * @？ filebody 表示上传文件以及上传的文件体
                     */
                    .addFormDataPart("uploadfile", da, fileBody)
                    .build();

             /*获取文件总大小*/
            final long gone = mFile.length();
             /*子线程执行*/
            ProgressRequestBodyListener.OnProgressRequestListener p = new ProgressRequestBodyListener.OnProgressRequestListener() {
                /**
                 * @param k 当前的上传量
                 * @param m 文件的总大小 但是这里使用错误 所以在上面定义获取文件的总大小
                 * @param done bool 上传完毕则表示为true 接口定义k==m
                 */
                @Override
                public void onRequestProgress(long k, long m, boolean done) {
                    //System.out.println((100 * k) / m +  "%");
                    //System.out.println(k+"======"+m+"====="+gone);

                    int cd = (int) ((int) (k * 100) / gone);
                    //System.out.println(cd + "%");
                    /*通知主线程不停的更新ui界面*/
                    Message msg=new Message();
                    msg.what=0x1002;
                    msg.obj=cd;//int值
                    handler.sendMessage(msg);
                }
            };

			/*请求体*/
            Request requestPostFile = new Request.Builder()
                    .url(servicePath)
                    //.post(requestBody) //不带监听上传进度的方式采用
                    .post(ProgressRequestBodyListener.addProgressRequestListener(requestBody, p))
                    .build();
            http.newCall(requestPostFile).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    String string = response.body().string();
                    Log.e("TAG", new Date()+" >>> (上传)服务器返回的json "+string);
                    if(string != null){
                        Message msg=new Message();
                        msg.obj=string;
                        msg.what=0X1003;
                        handler.sendMessage(msg);
                    }
                }
            });
        }
    }


    /* HttpUtils的数据下载请求 */
    public static void HttpUtilsGet(final Context context, String path, final Handler handler){
        new HttpUtils(10000).send(HttpRequest.HttpMethod.GET,
                path,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        if(result != null){
                            Message msg=new Message();
                            msg.what=0X000006;
                            msg.obj=result;
                            handler.sendMessage(msg);
                        }else {
                            Configfile.Log(context,"暂未有数据！！");
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Configfile.Log(context,"连接网络失败！");
                    }
                });
    }


    /**
     *
     * {@link NetworkTools #httpUpload}
     * @param method 请求方式
     * @param data 需要上传的数据
     * @param servicePath 上传服务器的路径
     * @param dataHead 设置请求头
     * @param mHandler 回调的数据Handler
     */
    public static void httpUpload(final HttpRequest.HttpMethod method, final String dataHead, final Handler mHandler, final String servicePath, final String data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(method == HttpRequest.HttpMethod.GET){
                    /* 通过地址拼接请求的地址 */
                    String mPath = servicePath+"?"+dataHead+"="+data;
                    new HttpUtils(5000).send(HttpRequest.HttpMethod.GET,
                            mPath,
                            new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    String result = responseInfo.result;

                                    if(result != null){
                                        Message msg=new Message();
                                        msg.what = 0X101;
                                        msg.obj = result;
                                        mHandler.sendMessage(msg);
                                        Log.e("HTTP",result);
                                    }
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    Message msg=new Message();
                                    msg.what = 0X102;
                                    msg.obj = e.toString();
                                    mHandler.sendMessage(msg);
                                }

                                @Override
                                public void onLoading(long total, long current, boolean isUploading) {
                                    super.onLoading(total, current, isUploading);
                                    int cd = (int) ((int) (current * 100) / total);
                                    Message msg=new Message();
                                    msg.what = 0X103;
                                    msg.obj = current;
                                    mHandler.sendMessage(msg);
                                }
                            });



                }else {

                    RequestParams params=new RequestParams();
                    params.addBodyParameter(dataHead,data);

                    new HttpUtils(5000).send(HttpRequest.HttpMethod.POST,
                            servicePath,
                            params,
                            new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    String result = responseInfo.result;

                                    if(result != null){
                                        Message msg=new Message();
                                        msg.what = 0X101;
                                        msg.obj = result;
                                        mHandler.sendMessage(msg);
                                        Log.e("HTTP",result);
                                    }
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    Message msg=new Message();
                                    msg.what = 0X102;
                                    msg.obj = e.toString();
                                    mHandler.sendMessage(msg);
                                }

                                @Override
                                public void onLoading(long total, long current, boolean isUploading) {
                                    super.onLoading(total, current, isUploading);
                                    /*int cd = (int) ((int) (current * 100) / total);
                                    Message msg=new Message();
                                    msg.what = 0X103;
                                    msg.obj = cd;
                                    mHandler.sendMessage(msg);*/
                                }
                            });

                }
            }
        }).start();


    }


    /**
     *
     * {@link #httpload(HttpRequest.HttpMethod, String[], String[], Handler, String)}  多参数、请求头的
     * 上传
     */
    public static final int POST_LOADING_CALLBANK=0X000020;
    public static final int POST_OK_CALLBANK=0X000020;

    public static void httpload(HttpRequest.HttpMethod method,
                                String[] head,
                                String[] data,
                                final Handler mHandler,
                                String servicePath){
        RequestParams params=new RequestParams();
        if(head.length == data.length){
            for (int i = 0; i < head.length; i++) {
                params.addBodyParameter(head[i],data[i]);
            }
            //执行上传
            new HttpUtils(10000).send(method,
                    servicePath,
                    params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            String result = responseInfo.result;
                            if(result != null){
                                Message msg=new Message();
                                msg.what=NetworkTools.POST_OK_CALLBANK;
                                msg.obj=result;
                                mHandler.sendMessage(msg);
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {

                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            super.onLoading(total, current, isUploading);
                            int i = (int) ((current * 100) / total);
                            Message msg=new Message();
                            msg.what=NetworkTools.POST_LOADING_CALLBANK;
                            msg.obj=i;
                            mHandler.sendMessage(msg);
                            Log.e("TAG","当前上传进度"+i+"%");
                        }
                    });

        }


    }


}
