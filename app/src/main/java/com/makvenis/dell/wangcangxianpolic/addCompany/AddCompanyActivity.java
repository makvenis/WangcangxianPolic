package com.makvenis.dell.wangcangxianpolic.addCompany;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.makvenis.dell.wangcangxianpolic.company.CompanyActivity;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.help.PermissionsUtils;
import com.makvenis.dell.wangcangxianpolic.sanEntery.BjcUnit;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.NetworkTools;
import com.makvenis.dell.wangcangxianpolic.view.SimpleLoadingDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.mozile.schManage.pojo.JwTrade;


@ContentView(R.layout.activity_scrolling)
public class AddCompanyActivity extends AppCompatActivity {


    /* TAG */
    public final String TAG = "AddCompanyActivity";

    /* 单位名称 */
    @ViewInject(R.id.mMore_Name)
    EditText mMore_Name;

    /* 单位地址 */
    @ViewInject(R.id.mMore_Addrs)
    EditText mMore_Addrs;

    /* 法人姓名 */
    @ViewInject(R.id.mMore_LevName)
    EditText mMore_LevName;

    /* 管辖单位 */
    @ViewInject(R.id.mMore_GovName)
    TextView mMore_GovName;

    /* 单位类型 */
    @ViewInject(R.id.mMore_AddrsType)
    TextView mMore_AddrsType;

    /* 单位电话 */
    @ViewInject(R.id.mMore_Phone)
    EditText mMore_Phone;

    /* 法人性别 */
    @ViewInject(R.id.mMore_Sex)
    TextView mMore_Sex;

    /* 管理等级 */
    @ViewInject(R.id.mMore_Lev)
    EditText mMore_Lev;

    /* 添加单位照片 */
    @ViewInject(R.id.mMore_Addrs_photo)
    ImageView mMore_Addrs_photo;

    /* mMore_Submit 信息提交 */
    @ViewInject(R.id.mMore_Submit)
    Button mMore_Submit;

    /* 法人证件类型  */
    @ViewInject(R.id.mMore_LevType)
    TextView mMore_LevType;

    /* 法人证件号码 mMore_Num */
    @ViewInject(R.id.mMore_Num)
    TextView mMore_Num;

    /* 法人证件年龄 mMore_Age */
    @ViewInject(R.id.mMore_Age)
    TextView mMore_Age;

    /* 上传地址 (百分百) */
    @ViewInject(R.id.mMore_Uri)
    TextView mMore_Uri;

    /* 处理toolbar 开始 version=2  */
    /* include 里面的点击事件 */
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.toolbar_callbank_text)
    TextView mBankTextView;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;
    /* 处理toolbar 结束 */

    
    /* 上下文 */
    public final Context mContext = AddCompanyActivity.this;
    private CatLoadingView mCat;
    private SimpleLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        mTextView.setText("添加单位基本信息");
        PermissionsUtils permissionsUtils=new PermissionsUtils();
        permissionsUtils.SetPermissionForNormal(this);

    }

    /* 获取当前单位类型 */
    @OnClick({R.id.mMore_AddrsType})
    public void getAddrsType(View v){
        // 创建数据
        final String[] items = new String[] { "旅店业", "银行金融机构", "寄递物流", "校园","民爆物品","加油站" };
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // 设置参数
        builder.setIcon(R.drawable.icon).setTitle("类型选择")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMore_AddrsType.setText(items[which]);
                        e.setTradeId(which);
                    }
                });
        builder.create().show();
    }

    /* 管辖单位 */
    @OnClick({R.id.mMore_GovName})
    public void getXqu(View v){

        final String[] items = new String[] { "旺苍公安局", "东河派出所", "英萃派出所", "普及派出所","白水派出所","治城派出所",
        "三江派出所","国华派出所","木门派出所","金溪派出所","张华派出所","鼓城派出所","黄洋派出所","嘉川水陆派出所","交通大队嘉川中队","环境监察执法大队","文化出版物稽查大队","劳动保险监察大队","农业行政执法大队","烟草专卖局稽查大队","治安管理大队","特巡警大队","西城派出所","五权派出所","双汇派出所","尚武派出所"};
        // 创建对话框构建器
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // 设置参数
        builder.setIcon(R.drawable.icon).setTitle("类型选择")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMore_GovName.setText(items[which]+"");

                    }
                });
        builder.create().show();
    }

    /* 法人性别 mMore_Sex */
    @OnClick({R.id.mMore_Sex})
    public void getSex(View v){
        final String[] items = new String[] { "男", "女" };
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // 设置参数
        builder.setIcon(R.drawable.icon).setTitle("类型选择")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMore_Sex.setText(items[which]);
                    }
                });
        builder.create().show();
    }

    /* 证件类型 */
    @OnClick({R.id.mMore_LevType})
    public void getNumType(View v){
        final String[] items = new String[] { "身份证", "户口本" ,"其他"};
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // 设置参数
        builder.setIcon(R.drawable.icon).setTitle("类型选择")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMore_LevType.setText(items[which]+"");
                        e.setZjtype(Integer.valueOf(which+1));
                    }
                });
        builder.create().show();
    }

    /* 获取图片 */
    @OnClick({R.id.mMore_Addrs_photo})
    public void photo(View v){
        Matisse.from(AddCompanyActivity.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .maxSelectable(3)
                .imageEngine(new PicassoEngine())
                .forResult(1);
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkViewImage(View v){
        startActivity(new Intent(this, CompanyActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank_text})
    public void oncklinkViewTextView(View v){
        startActivity(new Intent(this, CompanyActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            final List<Uri> mSelected = Matisse.obtainResult(data);
            //Toast.makeText(UploadImageActivity.this,mSelected.size()+"",Toast.LENGTH_SHORT).show();
            Log.e("TAG", "mSelected: " + mSelected.toString() + " 大小 " +mSelected.size());
            e.setPhotoUrl(mSelected.get(0).toString());
            mMore_Uri.setText(mSelected.get(0).toString()+"");

            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), mSelected.get(0));

            dialog = new SimpleLoadingDialog(mContext);
            dialog.setMessage("等待图片上传完成...");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestParams params=new RequestParams();
                    for (int i = 0; i < mSelected.size(); i++) {
                        String uri = getRealPathFromUri(mContext, mSelected.get(i));
                        params.addBodyParameter("msg"+i,new File(uri));
                    }

                    new HttpUtils(5000).send(HttpRequest.HttpMethod.POST,
                            Configfile.UPLOAD_FILE_PATH_ALL,
                            params,
                            new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    Log.e(TAG,"上传完成返回的结果"+responseInfo.result+"");
                                    String result = responseInfo.result;
                                    if(result != null){
                                        Message msg=new Message();
                                        msg.what=0x000008;
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

                                    Message msg=new Message();
                                    msg.what=0x000009;
                                    msg.obj=current;
                                    mHandler.sendMessage(msg);
                                    Log.e(TAG,"当前上传进度"+current+"");
                                }
                            });
                }
            }).start();


        }
    }

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


    BjcUnit e=new BjcUnit();

    @OnClick({R.id.mMore_Submit})
    public void addCompany(View v){
        // TODO: 2018/6/2 提交信息
        //http://ssdaixiner.oicp.net:26168/wcjw/mobile/doAddDanwei?dataJson=
        /**
         * `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '被检查的单位ID', 1
         `name` varchar(255) DEFAULT NULL COMMENT '被检查的单位名称', 1
         `trade_id` int(11) DEFAULT NULL COMMENT '被检查单位所属行业ID', 1
         `legala_name` varchar(255) DEFAULT NULL COMMENT '被检查单位法人代表姓名', 1
         `address` varchar(1000) DEFAULT NULL COMMENT '被检查单位地址', 1
         `phone` varchar(255) DEFAULT NULL COMMENT '被检查单位联系电话', 1
         `sex` int(1) DEFAULT NULL COMMENT '该公司法人性别 1. 男  2. 女', 1
         `age` int(3) DEFAULT NULL COMMENT '该公司法人年龄', 1
         `birthday` datetime DEFAULT NULL,
         `zjtype` int(1) DEFAULT NULL COMMENT '证件类型 1.身份证 2.其它', 1
         `zjNum` varchar(255) DEFAULT NULL COMMENT '证件编号', 1
         `type` varchar(255) DEFAULT NULL COMMENT '单位分类',
         `pcs` varchar(255) DEFAULT NULL COMMENT '所属派出所',
         `attr` varchar(255) DEFAULT NULL COMMENT '单位属性',
         `level` varchar(255) DEFAULT NULL COMMENT '管理等级',
         `photoUrl` varchar(255) DEFAULT NULL COMMENT '照片地址',
         `state` int(1) DEFAULT '1' COMMENT '1.正常状态  2.删除状态',
         PRIMARY KEY (`id`)
         ) ENGINE=InnoDB AUTO_INCREMENT=702 DEFAULT CHARSET=utf8;


         */
        String Name = mMore_Name.getText().toString(); //单位名称
        String Addrs = mMore_Addrs.getText().toString(); //单位地址
        String LevName = mMore_LevName.getText().toString(); //法人姓名
        String AddrsType = mMore_AddrsType.getText().toString(); //单位类型
        String GovName = mMore_GovName.getText().toString(); //管辖单位
        String Phone = mMore_Phone.getText().toString(); //联系电话
        String Sex = mMore_Sex.getText().toString(); //法人性别
        String Lev = mMore_Lev.getText().toString(); //管理等级
        String LevType = mMore_LevType.getText().toString(); //证件类型
        String Num = mMore_Num.getText().toString(); //证件号码
        String Age = mMore_Age.getText().toString(); //法人年龄


        JwTrade m=new JwTrade();
        m.setId(Integer.valueOf(0));
        m.setTradeName(AddrsType);
        e.setTrade(m);

        e.setAddress(Addrs);
        e.setAge(Integer.valueOf(Age));
        e.setLegalaName(LevName);
        e.setPcs(GovName);
        e.setZjnum(Num);
        e.setType(LevType);
        e.setName(Name);


        if(Sex.equals("男")){
            e.setSex(Integer.valueOf(0));
        }else e.setSex(Integer.valueOf(1));

        e.setPhone(Phone);
        e.setState(Integer.valueOf(1));
        e.setAttr(AddrsType);
        // TODO: 2018/6/2 单位图片
        e.setLevel(Lev);
        e.setBirthday(new Date());


        /* 转换JSON */
        String mResult=com.alibaba.fastjson.JSON.toJSONString(e);
        Log.e("TAG"," 预备提交的地址 >>>> "+ Configfile.INSERT_COMPANY);
        Log.e("TAG"," 预备提交的实体JSON >>>> "+mResult);

        //NetworkTools.postHttpToolsUaerRegistite(Configfile.INSERT_COMPANY,mHandler,mResult);
        NetworkTools.httpUpload(HttpRequest.HttpMethod.POST,
                "dataJson",mHandler,Configfile.INSERT_COMPANY,mResult);


        mCat = new CatLoadingView();
        mCat.show(getSupportFragmentManager(),"");


    }

    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what){
                case 0X101:
                    String jsonCall = (String) msg.obj;
                    if(jsonCall != null){
                        try {
                            JSONObject obj=new JSONObject(jsonCall);
                            String state = obj.optString("state");
                            if(state.equals("OK")){
                                Configfile.Log(AddCompanyActivity.this, "添加成功");
                                /* 返回单位列表页 */
                                Intent intent=new Intent(AddCompanyActivity.this, CompanyActivity.class);
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(AddCompanyActivity.this).toBundle());
                            }else {
                                Configfile.Log(AddCompanyActivity.this, "解析错误"+obj);
                            }
                            mCat.dismiss();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }else {
                        Configfile.Log(AddCompanyActivity.this, "添加失败");
                        mCat.dismiss();
                    }

                    break;
                case 0x000008:
                    String json = (String) msg.obj;
                    List<Map<String, String>> maps = JSON.GetJson(json, new String[]{"url", "message"});
                    if(maps.size() >= 0){

                        String mPathMax="";

                        for (int i = 0; i < maps.size(); i++) {

                            Map<String, String> map = maps.get(i);
                            String s = map.get("message");
                            if(s.equals("ok")){
                                String value = map.get("url");
                                mPathMax+=value+",";
                            }
                        }
                        Configfile.Log(mContext,"上传完成！");
                        dialog.dismiss();
                        e.setPhotoUrl(mPathMax);
                    }else {
                        Configfile.Log(mContext,"[ERROR]"+ json);
                        dialog.dismiss();
                    }

                    break;

                case 0x000009:
                    long num = (long) msg.obj;
                    mMore_Uri.setText(num+"");
                    break;
            }
        }
    };
}
