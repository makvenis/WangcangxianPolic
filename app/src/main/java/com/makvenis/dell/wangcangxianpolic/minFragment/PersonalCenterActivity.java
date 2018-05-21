package com.makvenis.dell.wangcangxianpolic.minFragment;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.NetworkTools;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

/*
*@解析用户头像的上传
*@同用类 注释尽量写明白 便于下次植入
*
* */

@ContentView(R.layout.activity_personal_center)
public class PersonalCenterActivity extends AppCompatActivity {

    /* 中间的用户头像(老的) 当更新之后就是新的 */
    @ViewInject(R.id.old_user_poto)
    ImageView oldImageView;

    /* 用户选择上传（当前还处于查找手机上面的图片）的点击事件按钮 */
    @ViewInject(R.id.select_user_poto)
    Button mButton;

    /* 上传按钮的点击事件 */
    @ViewInject(R.id.upload_user_poto)
    TextView mUploadTextView;


    /* 用户没有更新头像之前的图片全路径 */
    String oldImagePath;


    /* 剪切之后的图片(也就是新地址)等待上传服务器的图片地址 */
    private String newImagePath;

    /* 上传布局的隐藏与显示 */
    @ViewInject(R.id.mInvisible)
    LinearLayout mInvisible;
    /* TextView的UI更新 */
    @ViewInject(R.id.mProgressBar_int)
    TextView mProgressBar_int;
    /* mProgressBar的回调事件 */
    @ViewInject(R.id.mProgressBar)
    ProgressBar mProgressBar;


    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x1002){
                int obj = (int) msg.obj;
                if(obj > 100){
                    mProgressBar_int.setText("上传完成");
                    /* 当上传完成之后 隐藏mProgressBar */
                    if(mProgressBar.getVisibility() == View.VISIBLE){
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                    Configfile.Log(PersonalCenterActivity.this,"头像更新成功");
                }else {
                    mProgressBar_int.setText("上传中 "+obj+"%");
                }

                mProgressBar.setProgress(obj);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_personal_center);
        ViewUtils.inject(this);

        /* 首先接收父类传递过来的老图片地址 old_img_url */
        getParment();
    }


    /* 首先接收父类传递过来的老图片地址 */
    public void getParment() {
        Intent intent = getIntent();
        oldImagePath = intent.getStringExtra("old_img_url");
        /* 为了增加用户体验，那么在没有更新头像之前首先让用户能够看见之前的头像 */
        Picasso.with(this).load(oldImagePath).into(oldImageView);
    }

    /* 用户选择上传（当前还处于查找手机上面的图片）的点击事件按钮 */
    @OnClick({R.id.select_user_poto})
    public void selectPoto(View v){
        /* 让用户选择上传图片的方式 */
        //用户选择图片 底部弹出PopWindow
        bottomwindow(v);
    }

    /* 用户已确认上传图片的点击事件 */
    @OnClick({R.id.upload_user_poto})
    public void uploadImage(View v){
        /* 启动上传 */
        //设置mInvisible的显示
        if(mInvisible.getVisibility() == View.INVISIBLE){
            mInvisible.setVisibility(View.VISIBLE);
        }
        if(newImagePath != null){
            //开始上传
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetworkTools.upload(newImagePath,PersonalCenterActivity.this,mHandler);
                }
            }).start();
        }else {
            Snackbar.make(v,"未选择头像",Snackbar.LENGTH_LONG).setAction("选择图片", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* 当系统提示未选择照片的时候 当点击选择照片 执行选取头像的事件 */
                    bottomwindow(v);
                }
            }).show();
        }

    }


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
                // TODO: 2018/5/21  启动本地图片选择
                getActionPoto();
                popupWindow.dismiss();
            }
        });

        /* 启动照相机拍照 */
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/5/21  启动照相机拍照
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


    /* 启动本地图片的选择 开始 */

    /**
     * @? 获取图片资源
     * @？ 获取图片资源的路径
     * @？ 通过调用android自带的图片剪切
     * @？ 保存剪切的图片资源
     *
     */
    //获取图片资源
    public void getActionPoto(){
        //创建意图
        Intent intent=new Intent();
        //设置获取资源的意图
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //设置获取资源的类型
        intent.setType("image/*");
        //设置如果意图加载失败不崩溃
        ComponentName componentName = intent.resolveActivity(getPackageManager());
        if(componentName !=null){
            startActivityForResult(intent,1);
        }
    }

    //剪切图片 并把剪切之后的图片发出去
    public void getCropPoto(Uri uri) throws IOException {
        //设置裁剪图片后存储位置 crop  文件名称
        File file=new File(getExternalCacheDir(),"crop");
        Log.e("TAG","剪切之后的图片存储路径到文件夹"+file);
        if(!file.exists()){
            file.mkdirs();
        }
        //设置文件名称
        File img=new File(file,"newCrop"+".jpg");
        if(!img.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("crop","剪切之后的图片存储完整路径"+img);
        //调用android 自带的图片剪切
        Intent intent=new Intent("com.android.camera.action.CROP");
        //设置图片需要地址
        intent.setDataAndType(uri,"image/*");
        //设置属性 都是通过固定的键值对应的关系
        intent.putExtra("crop","true");
        //设置图片宽高比例及其大小
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX",200);
        intent.putExtra("outputY",200);
        //设置图片不反回
        intent.putExtra("return-data",false);
        //设置剪切之后的图片保存位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(img));
        //设置输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //是否取消人脸识别
        intent.putExtra("noFaceDetecation",true);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                try {
                    Uri path = data.getData();
                    Log.e("crop",path.toString()+"获取的是选择需要剪切的图片地址");
                    getCropPoto(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                newImagePath = getExternalCacheDir() + File.separator + "crop" + File.separator + "newCrop"+".jpg";
                Log.e("crop","保存的截图---"+newImagePath);
                Bitmap bitmap= BitmapFactory.decodeFile(newImagePath);
                oldImageView.setImageBitmap(bitmap);
                break;
        }
    }

    /* 启动本地图片的选择 结束 */
}
