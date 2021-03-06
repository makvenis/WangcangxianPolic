package sc.gys.wcx.and.startActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import sc.gys.wcx.and.R;
import sc.gys.wcx.and.help.JSON;
import sc.gys.wcx.and.help.MessageEvent;
import sc.gys.wcx.and.help.ScaleImage;
import sc.gys.wcx.and.sanEntery.JwScenePhoto;
import sc.gys.wcx.and.tools.Configfile;
import sc.gys.wcx.and.tools.NetworkTools;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@ContentView(R.layout.activity_upload_check)
public class UploadCheckActivity extends AppCompatActivity {

    public final String TAG="UploadCheckActivity";

    /* bundle 中获取的Url */
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

    /* 处理toolbar 开始 version=2  */
    /* include 里面的点击事件 */
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.toolbar_callbank_text)
    TextView mBankTextView;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;
    /* 处理toolbar 结束 */


    /**
     * 上传成功之后的返回当前的数据库ID 此处id并非单位id和编号一类的
     */
    public String mDataBase_id;


    /* 上传 */
    @ViewInject(R.id.mToolbar_upload)
    TextView mTextSubmit;



    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int what = msg.what;

            switch (what){
                case 1:
                    String json = (String) msg.obj;
                    List<Map<String, String>> maps = JSON.GetJson(json, new String[]{"url", "message"});
                    if(maps.size() >= 0){
                        // 更新数据库
                        Log.e(TAG," 更新数据库 "+maps.size()+" >>> " + maps.toString());
                        updateDatabase(maps);
                    }else {
                        Configfile.Log(UploadCheckActivity.this,"[ERROR]"+ json);
                    }
                    break;

                case NetworkTools.POST_OK_CALLBANK:
                    String obj = ((String) msg.obj);
                    if(obj != null){
                        Log.e("TAG"," 更新之后返回的数据 "+obj);
                        try {
                            JSONObject opt=new JSONObject(obj);
                            String state = opt.optString("state");
                            if(state.equals("OK")){
                                Configfile.Log(UploadCheckActivity.this,"上传成功");
                                String mDataBase_id = opt.optString("id");
                                EventBus.getDefault().post(new MessageEvent(mDataBase_id));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Log.e("TAG"," 更新之后返回的数据 "+obj);
                        Configfile.Log(UploadCheckActivity.this,"上传失败");
                    }
                    break;

                case 0x000010: //进度UI更新
                    int num = (int) msg.obj;
                    if(num >= 100){
                        dialog.dismiss();
                    }else {
                        dialog.setProgress(num);
                    }
                    break;
            }
        }
    };

    /* 上传完毕 更新数据库 */
    public void updateDatabase(List<Map<String, String>> maps){
        JwScenePhoto e=new JwScenePhoto();
        e.setAddtime(new Date());
        e.setBianhao(Integer.valueOf(mCid));
        e.setUsername(null);

        String mPath="";

        for (int i = 0; i < maps.size(); i++) {
            Map<String, String> map = maps.get(i);
            String url = map.get("url");
            mPath+=url+",";
            if(i == 0){
                e.setPhotourl1(url);
            }else if( i == 1) {
                e.setPhotourl2(url);
            }else if( i == 2 ){
                e.setPhotourl3(url);
            }
        }
        /* 转换JSON */
        String mResult=com.alibaba.fastjson.JSON.toJSONString(e);
        Log.e("TAG"," 旺苍县公安局责令改正通知书ID >>>> "+id);
        Log.e("TAG"," 预备提交的地址 >>>> "+Configfile.UPLOAD_TRUE_IMAGE);
        Log.e("TAG"," 预备提交的实体JSON >>>> "+mResult);

        /* 发送 */
        EventBus.getDefault().post(new MessageEvent(mPath));

        /* 设置所有的请求头 */
        String[] head=new String[]{"dataJson","photoname1","photoname2","photoname3"};


        String mText1 = mShowText.getText().toString();
        String mText2 = mShowText2.getText().toString();
        String mText3 = mShowText3.getText().toString();

        /* 存储当前的三张图片地址 */
        SharedPreferences pref = getSharedPreferences("UploadCheckActivity",MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        editor.putString("url1",e.getPhotourl1());
        editor.putString("url2",e.getPhotourl1());
        editor.putString("url3",e.getPhotourl1());
        editor.apply();

        if(mText1 != "" && mText2 != "" && mText3 != ""){
            /* 设置所有的值 */
            String[] data=new String[]{mResult,mText1,mText2,mText3};
            /* 开始上传 */
            NetworkTools.httpload(HttpRequest.HttpMethod.POST,head,data,mHandler,Configfile.UPLOAD_TRUE_IMAGE);
            /* 旧版本提交方式 */
            //NetworkTools.httpUpload(HttpRequest.HttpMethod.POST,"dataJson",mHandler,Configfile.UPLOAD_TRUE_IMAGE,
            //        mResult);
            EventBus.getDefault().post(new MessageEvent(mPath));
        }else {
            Configfile.Log(this,"随笔记录不可以为空");
            //mCat.dismiss();
        }
    }

    /* 上传 */
    @OnClick({R.id.mToolbar_upload})
    public void upload(View v){
        //mCat = new CatLoadingView();
        //mCat.show(getSupportFragmentManager(),"");
        showDialogProgrous();
        uploadImage();
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkViewImage(View v){
        Intent intent=new Intent(UploadCheckActivity.this,WebViewActivity.class);
        /**
         *mTitle_intent = bundle.getString("mTitle");
         mUrl_intent = bundle.getString("mUrl");
         mCid = bundle.getString("mCid");
         id = bundle.getString("id");
         */
        intent.putExtra("mTitle",mTitle_intent);
        intent.putExtra("mUrl",mUrl_intent );
        intent.putExtra("mCid", mCid);
        intent.putExtra("id",id);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(UploadCheckActivity.this).toBundle());
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank_text})
    public void oncklinkViewTextView(View v){
        Intent intent=new Intent(UploadCheckActivity.this,WebViewActivity.class);
        /**
         *mTitle_intent = bundle.getString("mTitle");
         mUrl_intent = bundle.getString("mUrl");
         mCid = bundle.getString("mCid");
         id = bundle.getString("id");
         */
        intent.putExtra("mTitle",mTitle_intent);
        intent.putExtra("mUrl",mUrl_intent );
        intent.putExtra("mCid", mCid);
        intent.putExtra("id",id);

        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(UploadCheckActivity.this).toBundle());
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


    /* 开启获取图片 */
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
                Bitmap bitmap = ScaleImage.execute(UploadCheckActivity.this, mSelected.get(i));
                ImageView view = img.get(i);
                view.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * {@link #getRealPathFromUri(Context, Uri)}
     * 通过此方法 将context:// 代表的是一个路径 存在Android系统数据库中
     * 需要查询拿到这张图片的地址代表的文件
     */
    public String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void uploadImage() {

        if (mSelected.size() == 0) {
            Configfile.Log(this, "未选取任何文件");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                final RequestParams params = new RequestParams();
                for (int i = 0; i < mSelected.size(); i++) {
                    params.addBodyParameter("msg" + i,
                                    new File(getRealPathFromUri(UploadCheckActivity.this,
                                    mSelected.get(i))));
                    Log.e(TAG,"文件个数"+i);

                    /* 新版本的执行压缩 */
                    //Bitmap bitmap = ScaleImage.execute(UploadCheckActivity.this, mSelected.get(i));
                    /* 存储这张照片 */
                    //返回的是当前存储的图片在Android的数据库中的地址
                    //String s = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null);
                    //拿到具体的路径才能上传
                    //Uri uri=Uri.parse(s);
                    //String fromUri = getRealPathFromUri(UploadCheckActivity.this, uri);
                    //params.addBodyParameter("msg" + i,new File(fromUri));
                }
                new HttpUtils(5000).send(HttpRequest.HttpMethod.POST,
                        Configfile.UPLOAD_FILE_PATH_ALL,
                        params,
                        new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                String result = responseInfo.result;
                                if (result != null) {
                                    Message msg=new Message();
                                    msg.what = 1;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {}

                            @Override
                            public void onLoading(long total, long current, boolean isUploading) {
                                super.onLoading(total, current, isUploading);
                                int i = (int) ((current * 100) / total);
                                Message msg=new Message();
                                msg.what=0x000010;
                                msg.obj=i;
                                mHandler.sendMessage(msg);
                                Log.e(TAG,"当前上传进度"+i+"%");
                            }
                        });
            }
        }).start();
    }

    /* 上传进度条 */
    ProgressDialog dialog;
    public void showDialogProgrous(){
        dialog=new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setProgress(R.mipmap.ic_launcher);
        dialog.setSecondaryProgress(Color.RED);//设置二级进度条的背景
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setIcon(R.drawable.icon_upload_dialog);//
        // 设置提示的title的图标，默认是没有的，需注意的是如果没有设置title的话只设置Icon是不会显示图标的
        dialog.setTitle("上传中...");
        dialog.setProgress(0);
        dialog.setMessage("Upload...");
        dialog.setMax(100);
        dialog.show();
    }
}
