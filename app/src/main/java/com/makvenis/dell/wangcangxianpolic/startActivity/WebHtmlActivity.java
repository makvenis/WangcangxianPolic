package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.CacheUtils;
import com.makvenis.dell.wangcangxianpolic.otherActivity.HistoryUtils;
import com.makvenis.dell.wangcangxianpolic.sanEntery.Jcbilu;
import com.makvenis.dell.wangcangxianpolic.sanEntery.JwNoyqYinhuanMsg;
import com.makvenis.dell.wangcangxianpolic.sanEntery.JwYesyqYinhuanMsg;
import com.makvenis.dell.wangcangxianpolic.sanEntery.JwYinhuanMsgFucha;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.NetworkTools;
import com.makvenis.dell.wangcangxianpolic.view.SimpleLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.mozile.tools.Base64Util;

@ContentView(R.layout.activity_web_html)
public class WebHtmlActivity extends AppCompatActivity {

    /* 全局THIS */
    public final Context mThis=WebHtmlActivity.this;

    @ViewInject(R.id.mHtmlWebView)
    WebView mWebView;
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;

    /* 上一级传递过来的与被加载本地html的名称 */
    private String mLocalHtml_url;
    /* 上一级传递过来的设置toolbar的标题 */
    private String mLocal_title;
    /* 全局Dialog */
    SimpleLoadingDialog looading;

    private String mLocal_bianhao;

    /* 单位ID */
    private String id;

    /* 全局Handler */
    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            String obj = (String) msg.obj;
            Log.e("TAG",new Date()+" >>> 当前服务器返回的值 "+ obj);
            JSONObject object= null;
            try {
                object = new JSONObject(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (what){
                case Configfile.CALLBANK_POST_MSG:
                    if(mLocal_title.equals("不同意延期整改治安隐患") || mLocal_title.equals("同意延期整改治安隐患")){
                        if((object.optString("state")).equals("OK")){ //说明请求成功
                            Configfile.Log(mThis,"请求成功");
                            looading.dismiss();
                            String page = object.optString("bianhao");
                            Intent intent=new Intent(WebHtmlActivity.this, WebPostRemarkActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("bianhao",page);
                            bundle.putString("id",id);
                            if(mLocal_title.equals("不同意延期整改治安隐患")){
                                bundle.putString("mUrl",Configfile.SERVICE_WEB+"toshowNotAgreexqzg");
                            }else if(mLocal_title.equals("同意延期整改治安隐患")){
                                bundle.putString("mUrl",Configfile.SERVICE_WEB+"toshowAgreexqzg");
                            }
                            bundle.putString("mtitle",mLocal_title);
                            intent.putExtras(bundle);
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(WebHtmlActivity.this).toBundle());
                            /* 调用日志文件 */
                            HistoryUtils.executeAddrs(WebHtmlActivity.this,
                                    id,mLocal_title,"完成");
                        }else {
                            Configfile.Log(mThis,"请求失败");
                            looading.dismiss();
                        }
                    }else if(mLocal_title.equals("检查笔录")){
                        if(object.optString("state").equals("OK")){
                            Configfile.Log(mThis,"请求成功");

                            /* 调用日志文件 */
                            HistoryUtils.executeAddrs(WebHtmlActivity.this,
                                    id,mLocal_title,"完成");
                            looading.dismiss();
                            startActivity(new Intent(WebHtmlActivity.this,HomeActivity.class),ActivityOptions.makeSceneTransitionAnimation(WebHtmlActivity.this).toBundle());
                        }else {
                            Configfile.Log(mThis,"请求失败");
                            looading.dismiss();
                        }
                    }else if(mLocal_title.equals("复查意见书")){
                        if(object.optString("state").equals("OK")){
                            Configfile.Log(mThis,"请求成功");
                            startActivity(new Intent(WebHtmlActivity.this,HomeActivity.class),ActivityOptions.makeSceneTransitionAnimation(WebHtmlActivity.this).toBundle());

                            /* 调用日志文件 */
                            HistoryUtils.executeAddrs(WebHtmlActivity.this,
                                    id,mLocal_title,"完成");
                            looading.dismiss();
                        }else {
                            Configfile.Log(mThis,"请求失败");
                            looading.dismiss();
                        }
                    }else if(mLocal_title.equals("收缴物品清单")){

                    }else {
                        Configfile.Log(mThis,"参数错误！未获取到 {" + mLocal_title+" }");
                    }


                   break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        /* 获取父类传递过来的预加载html文件名称 */
        getParment();

        Log.e("TAG",new Date() + " >>> WebHtmlActivity（ID）"+id);

        /* 设置标题 */
        mTextView.setText(mLocal_title);

        /* 设置WebView */
        //开启JavaScript支持
        mWebView.getSettings().setJavaScriptEnabled(true);
        //放在assets的html需加上android_asset 也可以用网络上的文件
        mWebView.loadUrl("file:///android_asset/"+mLocalHtml_url);
        mWebView.addJavascriptInterface(new JSinterface(), "JS");// 设置本地调用对象及其JS访问名称
    }

    /* 获取父类传递过来的预加载html文件名称 */
    public void getParment() {
        Bundle bundle = getIntent().getExtras();
        mLocalHtml_url = bundle.getString("l_url");
        mLocal_title = bundle.getString("l_title");
        mLocal_bianhao = bundle.getString("mLocal_bianhao");
        id = bundle.getString("id");
        Log.e("TAG",new Date()+" >>> 被检查单位ID "+id);
    }

    /* 构建中间类 */
    public class JSinterface{
        @JavascriptInterface
        public void showDialog(){

        }
        @JavascriptInterface
        public void showTimer(){

        }

        //检查人（签名）:
        @JavascriptInterface
        public void showName(){
            Log.e("TAG","此方法被调用 showName() >>> 测试是否能否赋值");
            getGesture(mWebView,"jcr");
        }

        //记录人（签名）:
        @JavascriptInterface
        public void showNameJiLu(){
            Log.e("TAG","此方法被调用 showName() >>> 测试是否能否赋值");
            getGesture(mWebView,"jlr");
        }

        //被检查单位负责人或者陪同检查人（签名）:
        @JavascriptInterface
        public void showNameBeiJianCha(){
            Log.e("TAG","此方法被调用 showName() >>> 测试是否能否赋值");
            getGesture(mWebView,"bjcr");
        }

        @JavascriptInterface
        public void postLoading(String var){
            looading=new SimpleLoadingDialog(WebHtmlActivity.this);
            looading.setMessage("正在提交...").show();
            postUpdata(var);
        }
    }

    public void postUpdata(String var){
        List<String> list = JavaScriptTypeData(var);
        Configfile.Log(this,var+"===大小==="+list.size());
        if(mLocal_title.equals("同意延期整改治安隐患")){
            JwYesyqYinhuanMsg e=new JwYesyqYinhuanMsg();
            /**
             * @ 解释：获取的集合大小判断 按照html标准进行添加
             *
             */
            if(list.size() >= 5){  //也就是同意延期整改治安隐患 需要填写的数据是8个 对应的集合大小应该为7 否则要报数组下标越界
                e.setBjcUnitid(Integer.valueOf(id));        //单位ID
                e.setBianhao1(list.get(0));                 //编号1
                e.setBianhao2(Integer.valueOf(list.get(1)));//编号3
                e.setContent(list.get(3));                  //整改的内容
                e.setYqStarttime(stringByDate(list.get(4)+" "+"12:12:12")); //开始时间
                e.setYqEndtime(stringByDate(list.get(5)+" "+"12:12:12"));   //结束时间
                // TODO: 2018/5/13  list.get(6) 等于被检查的单位名称
                e.setTzsTime(stringByDate(list.get(6)+" "+"12:12:12"));     //填表时间

                 /* 转换JSON */
                String mResult=com.alibaba.fastjson.JSON.toJSONString(e);
                Log.e("TAG"," 预备提交的地址 >>>> "+Configfile.OVER_POST_CORECT_TONGYI);
                Log.e("TAG"," 预备提交的实体JSON >>>> "+mResult);
                //提交
                NetworkTools.postHttpToolsUaerRegistite(Configfile.OVER_POST_CORECT_TONGYI,mHandler,mResult);

            }else {
                Configfile.Log(this,"请完善所有的选项");
                looading.dismiss();
            }


        }else if(mLocal_title.equals("不同意延期整改治安隐患")){
            JwNoyqYinhuanMsg e1=new JwNoyqYinhuanMsg();

            if(list.size() >= 0){  //也就是不同意延期整改治安隐患 需要填写的数据是8个 对应的集合大小应该为7 否则要报数组下标越界
                e1.setBjcUnitid(Integer.valueOf(id));        //单位ID
                e1.setId(Integer.valueOf(0));                //主键
                e1.setBianhao1(list.get(0));                 //编号1
                e1.setBianhao2(Integer.valueOf(list.get(1)));//编号3
                e1.setContent(list.get(3));                   //整改的内容
                // TODO: 2018/5/13  list.get(6) 等于被检查的单位名称
                e1.setTzsTime(stringByDate(list.get(4)+" "+"12:12:12"));

                 /* 转换JSON */
                String mResult=com.alibaba.fastjson.JSON.toJSONString(e1);
                Log.e("TAG"," 预备提交的地址 >>>> "+Configfile.OVER_POST_CORECT_NOTONGYI);
                Log.e("TAG"," 预备提交的实体JSON >>>> "+mResult);

                //提交
                NetworkTools.postHttpToolsUaerRegistite(Configfile.OVER_POST_CORECT_NOTONGYI,mHandler,mResult);
            }else {
                Configfile.Log(this,"请完善所有的选项");
                looading.dismiss();
            }

        }else if(mLocal_title.equals("收缴物品清单")){
            // TODO: 2018/5/14 暂未写html

        }else if(mLocal_title.equals("检查笔录")){
            Jcbilu e1=new Jcbilu();

            if(list.size() >= 0){
                e1.setJcProjectid(Integer.valueOf(id));      //单位ID
                e1.setId(Integer.valueOf(0));                //主键
                e1.setJlStarttime(stringByDate(list.get(0)));//开始时间
                e1.setJlEndtime(stringByDate(list.get(1)));  //结束时间
                e1.setJcProjectid(Integer.valueOf(0));       //被检查单位ID list.get(2))
                e1.setJcResult(list.get(2));                 //检查内容及其结果
                // TODO: 2018/5/15 被检查人员ID 没有弄明白
                e1.setJcStaffid(Integer.valueOf(0));         //被检查人员ID
                e1.setJcSignature(NameImg.get("jcr"));       //检查人
                e1.setJlSignature(NameImg.get("jlr"));       //记录人
                e1.setBjcSignature(NameImg.get("bjcr"));     //被检查人

                 /* 转换JSON */
                String mResult=com.alibaba.fastjson.JSON.toJSONString(e1);
                Log.e("TAG"," 预备提交的地址 >>>> "+Configfile.OVER_POST_CORECT_BILU);
                Log.e("TAG"," 预备提交的实体JSON >>>> "+mResult);
                //提交
                NetworkTools.postHttpToolsUaerRegistite(Configfile.OVER_POST_CORECT_BILU,mHandler,mResult);
            }else {
                Configfile.Log(this,"请完善所有的选项");
                looading.dismiss();
            }
        }else if(mLocal_title.equals("复查意见书")){
            JwYinhuanMsgFucha e1=new JwYinhuanMsgFucha();

            if(list.size() == 6){
                Log.e("TAG",new Date() + " >>> 复查意见书的单位ID");

                e1.setBjcUnitid(Integer.valueOf(id));        //单位ID
                e1.setId(Integer.valueOf(0));                //主键
                e1.setBianhao1(list.get(0));                 //编号1
                e1.setBianhao2(Integer.valueOf(list.get(1)));//编号3

                e1.setFcmsgTime(stringByDate(list.get(3)));  //复查角标时间
                e1.setContent(list.get(4));                  //复查的内容
                e1.setFcTime(stringByDate(list.get(3)));     //复查时间
                if(list.get(5).equals("true")){
                    e1.setDabiao(Integer.valueOf(1));
                }else {
                    e1.setDabiao(Integer.valueOf(2));
                }

                if(list.get(5).equals("true")){
                    e1.setHege(Integer.valueOf(1));
                }else {
                    e1.setHege(Integer.valueOf(2));
                }
                e1.setYinghuanmsgid(Integer.valueOf(0));

                 /* 转换JSON */
                String mResult=com.alibaba.fastjson.JSON.toJSONString(e1);
                Log.e("TAG"," 预备提交的地址 >>>> "+Configfile.OVER_POST_CORECT_FUCHA);
                Log.e("TAG"," 预备提交的实体JSON >>>> "+mResult);
                //提交
                NetworkTools.postHttpToolsUaerRegistite(Configfile.OVER_POST_CORECT_FUCHA,mHandler,mResult);
            }else {
                Configfile.Log(this,"请完善所有的选项");
                looading.dismiss();
            }
        }else {
            Configfile.Log(this,"参数错误 Error(为传递任何信息)");
            looading.dismiss();
        }
    }

    /* 处理String类型的时间转化成data类型时间 */
    public Date stringByDate(String t){
        try {
            SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fm.parse(t);
            //System.out.println(date.toString());
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    /* javascript 传递的数据处理化 */
    public List<String> JavaScriptTypeData(String var){
        List<String> javascript=new ArrayList<>();
        if(var != null){
            String[] split = var.split("@");
            for (int i = 0; i < split.length; i++) {
                javascript.add(i,split[i]);
            }
            return javascript;
        }else {
            return new ArrayList<>();
        }

    }


    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkView(View v){
        startActivity(new Intent(this, HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
    /* 处罚android签名 */
    /* AlertDialog 的点击事件 */
    /**
     * @param WebView 用当传递的webView过来是为了在子线程中使用wenView.post(new Runnable)来实现更新调用javascript脚本
     * @param fileName 当使用Gesture来实现手势绘图的时候 当绘制完毕需要给图片命名
     */
    private Map<String,String> NameImg=new HashMap<>();
    public void getGesture(final WebView WebView,final String fileName) {

        View myView = LayoutInflater.from(this).inflate(R.layout.my_gesure_xml, null, false);
        GestureOverlayView gesure = (GestureOverlayView) myView.findViewById(R.id.dialog_gesture);
        gesure.setGestureColor(Color.GREEN);//设置手势的颜色
        gesure.setGestureStrokeWidth(2);//设置手势的画笔粗细
        gesure.setFadeOffset(3000); //手势淡出时间
        gesure.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                //将手势转换成图片
                Bitmap bitmap = gesture.toBitmap(128,128,10,0xffff0000);
                //保存
                CacheUtils.setWLocalCache(bitmap,fileName,WebHtmlActivity.this);
            }
        });

        // 创建构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle("提示").setIcon(R.mipmap.icon_app)
                .setMessage("请等待签名缓存完毕并且淡出！如果签名失败请清除缓存")
                .setView(myView)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {// 积极
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Bitmap localCache = CacheUtils.getLocalCache(fileName,WebHtmlActivity.this);
                        //imageView.setImageBitmap(localCache);
                        File mFile = WebHtmlActivity.this.getExternalFilesDir(null);
                        final String path = mFile.getAbsoluteFile() + fileName + ".png";
                        /* 子线程更新javaScript脚本语言 */
                        WebView.post(new Runnable() {
                            @Override
                            public void run() {
                                if(fileName.equals("jcr")){
                                    WebView.loadUrl("javascript: setImg('" + path + "')");
                                    byte[] imageStr = Base64Util.getImageStr(path);
                                    String string=new String(imageStr);
                                    NameImg.put(fileName,"data:image/png;base64," +string);
                                }else if(fileName.equals("jlr")){
                                    WebView.loadUrl("javascript: setImgJiLu('" + path + "')");
                                    byte[] imageStr = Base64Util.getImageStr(path);
                                    String string=new String(imageStr);
                                    NameImg.put(fileName,"data:image/png;base64," +string);
                                }else if(fileName.equals("bjcr")){
                                    WebView.loadUrl("javascript: setImgBeiJianCha('" + path + "')");
                                    byte[] imageStr = Base64Util.getImageStr(path);
                                    String string=new String(imageStr);
                                    NameImg.put(fileName,"data:image/png;base64," +string);
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("清除缓存", new DialogInterface.OnClickListener() {// 消极
                    @Override
                    public void onClick(DialogInterface dialog,int which) {

                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {// 中间级
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                    }
                });
        builder.create().show();
    }

}
