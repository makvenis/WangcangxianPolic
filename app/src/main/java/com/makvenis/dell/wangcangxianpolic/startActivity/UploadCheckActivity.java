package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.cat.CatLoadingView;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_upload_check)
public class UploadCheckActivity extends AppCompatActivity {

    public final String TAG="UploadCheckActivity";

    /* bundle 中获取的Url */
    /* bundle 中获取的标题 */
    private String mTitle_intent;
    private String mUrl_intent;
    private String mCid;
    /* 单位id */
    private String id;

    /* 组合控件查找 start */

    @ViewInject(R.id.mShowText)
    EditText mShowText;
    @ViewInject(R.id.mShowImage)
    ImageView mShowImage;

    @ViewInject(R.id.mShowText2)
    EditText mShowText2;
    @ViewInject(R.id.mShowImage2)
    ImageView mShowImage2;

    @ViewInject(R.id.mShowText3)
    EditText mShowText3;
    @ViewInject(R.id.mShowImage3)
    ImageView mShowImage3;

    /* 组合控件查找 end */

    private CatLoadingView mCat;


    /* 上传 */
    @ViewInject(R.id.mToolbar_upload)
    TextView mTextSubmit;


    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int what = msg.what;
            String obj = (String) msg.obj;

            switch (what){

                case 0x1002:
                    /*if(obj != null){
                        List<Map<String, String>> maps = JSON.GetJson(obj,new String[]{"","",""});
                        if(maps.size() != 0){

                        }

                    }*/

                    Log.e(TAG," 用户上传的证据照片 "+obj);
                    mCat.dismiss();
                    break;

                case 0X000002:

                    break;

            }

        }
    };


    @OnClick({R.id.mToolbar_upload})
    public void upload(View v){
        mCat = new CatLoadingView();
        mCat.show(getSupportFragmentManager(),"");
        uploadImage();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        /* 接收上层父类传递的参数 */
        getParment();

    }

    /* 接收上层父类传递的参数 */
    public void getParment() {
        //页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        mTitle_intent = bundle.getString("mTitle");
        mUrl_intent = bundle.getString("mUrl");
        mCid = bundle.getString("mCid");
        id = bundle.getString("id");
    }


    /* 开启图片上传 */
    @OnClick({R.id.mShowImage})
    public void openImage(View v){
        bottomwindow(v);
    }


    /* 打开popWindows */
        /* PopWindows 的操作等（开始） */

    /**
     * @ 解释直接调用 bottomwindow(view)方法
     * @ 解释 详细的不揍操作在方法setButtonListeners()里面
     */
    private PopupWindow popupWindow;
    //使用PopWindows的时候需要给定当前的View
    public void bottomwindow(View view) {

        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_potocrop_popwindows, null);
        popupWindow = new PopupWindow(layout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
        //添加按键事件监听
        setButtonListeners(layout);
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        backgroundAlpha(0.5f);
    }
    //渐变通道
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }
    //PopWindows的控件全局变量
    private LinearLayout callBank,localImage,camera;
    //PopWindows的控件的点击事件
    public void setButtonListeners(LinearLayout view) { //
        callBank = (LinearLayout) view.findViewById(R.id.mHistory_pop_over); //退出
        localImage = (LinearLayout) view.findViewById(R.id.mCrop_local_sd);  //本地图片按钮
        camera = (LinearLayout) view.findViewById(R.id.mCrop_local_camera);  //启动照相机

        /* 启动本地图片选择 */
        localImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Matisse.from(UploadCheckActivity.this)
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                        .theme(R.style.Matisse_Dracula)
                        .countable(false)
                        .maxSelectable(3)
                        .imageEngine(new PicassoEngine())
                        .forResult(1);

                popupWindow.dismiss();
            }
        });

        /* 启动照相机拍照 */
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/5/21  启动照相机拍照
                //getLocalCamera();
                popupWindow.dismiss();
            }
        });

        /* 取消操作 */
        callBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /* PopWindows 的操作等 （结束） */



    List<Uri> mSelected;

    /* 接收参数 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            //Toast.makeText(UploadImageActivity.this,mSelected.size()+"",Toast.LENGTH_SHORT).show();
            Log.e("TAG", "mSelected: " + mSelected.toString() + " 大小 " +mSelected.size());

            List<ImageView> img=new ArrayList<>();
            img.add(mShowImage);
            img.add(mShowImage2);
            img.add(mShowImage3);

            for (int i=0;i<mSelected.size();i++){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelected.get(i));
                    ImageView view = img.get(i);
                    view.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            // TODO: 2018/6/2  拿到数据的地址 将uri上传并且转化

        }
    }

    public void uploadImage(){

        final RequestParams params=new RequestParams();
        for (int i = 0; i < mSelected.size(); i++) {
            params.addBodyParameter("msg",new File(mSelected.get(i).toString()));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtils(5000).send(HttpRequest.HttpMethod.POST,
                        Configfile.UPLOAD_FILE_PATH_ALL,
                        params,
                        new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                String result = responseInfo.result;
                                if(result != null){
                                    Log.e(TAG,result);
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {

                            }

                            @Override
                            public void onLoading(long total, long current, boolean isUploading) {
                                super.onLoading(total, current, isUploading);
                                int l = (int)((total / current)/1000);

                                Log.e(TAG,"上传比例 >>> "+ l + "%");
                            }
                        });
            }
        }).start();




        /*JwScenePhoto e=new JwScenePhoto();
        e.setAddtime(new Date());
        e.setBianhao(Integer.valueOf(mCid));
        e.setUsername(null);

        //解析
        if(mSelected != null){
            for (int i = 0; i < mSelected.size(); i++) {
                Uri uri = mSelected.get(i);
                byte[] imageStr = Base64Util.getImageStr(uri.toString());
                String s = new String(imageStr);
                if(i == 0){
                    e.setPhotourl1("data:image/png;base64,"+s);
                }if(i == 1){
                    e.setPhotourl2("data:image/png;base64,"+s);
                }else if(i == 2){
                    e.setPhotourl3("data:image/png;base64,"+s);
                }
            }


        }*/

        /* 转换JSON *//*
        String mResult=com.alibaba.fastjson.JSON.toJSONString(e);
        Log.e("TAG"," 旺苍县公安局责令改正通知书ID >>>> "+id);
        Log.e("TAG"," 预备提交的地址 >>>> "+Configfile.UPLOAD_TRUE_IMAGE);
        Log.e("TAG"," 预备提交的实体JSON >>>> "+mResult);

        NetworkTools.postHttpToolsUaerRegistite(Configfile.UPLOAD_TRUE_IMAGE,mHandler,mResult);*/

    }
}