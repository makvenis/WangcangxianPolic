package sc.gys.wcx.and.details;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import sc.gys.wcx.and.company.CompanyActivity;
import sc.gys.wcx.and.R;
import sc.gys.wcx.and.cat.CatLoadingView;
import sc.gys.wcx.and.help.JSON;
import sc.gys.wcx.and.help.MessageEvent;
import sc.gys.wcx.and.help.PermissionsUtils;
import sc.gys.wcx.and.help.ScaleImage;
import sc.gys.wcx.and.sanEntery.BjcUnit;
import sc.gys.wcx.and.tools.Configfile;
import sc.gys.wcx.and.tools.NetworkTools;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.mozile.schManage.pojo.JwTrade;

import static android.app.Activity.RESULT_OK;

/* 单位详情（更新） */

public class UpdateViewFragment extends Fragment {

    /*  */
    public Context mContext=getActivity();

    /* TAG */
    public final String TAG = "UpdateViewFragment";

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

    /* 添加单位照片 */
    @ViewInject(R.id.mMore_Addrs_photo)
    ImageView mMore_Addrs_photo;

    /* 上传地址 (百分百) */
    @ViewInject(R.id.mMore_Uri)
    TextView mMore_Uri;

    /* 单位ID */
    public String xmlIdString;

    public Integer mInteger;

    public Handler mHandler=new Handler(){


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;

            switch (what){
                case 0X101:
                    String json = (String) msg.obj;
                    if(json != null){
                        try {
                            JSONObject obj=new JSONObject(json);
                            String state = obj.optString("state");
                            if(state.equals("OK")){
                                Configfile.Log(getActivity(), "修改成功");
                                /* 返回单位列表页 */
                                Intent intent=new Intent(getActivity(), CompanyActivity.class);
                                //intent.putExtra("bank_id","0");
                                //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                                startActivityForResult(intent,200);
                                EventBus.getDefault().post(new MessageEvent("ok"));

                            }else {
                                Configfile.Log(getActivity(), "解析错误"+obj);
                            }
                            mCat.dismiss();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }else {
                        Configfile.Log(getActivity(), "修改失败");
                        mCat.dismiss();
                    }

                    break;
            }


            if(msg.what == 1){
                String obj = (String) msg.obj;
                Log.e(TAG,"赋值前的数据 mHandler obj >>> "+obj);
                Map<String, Object> json = JSON.getObjectJson(obj, new String[]{"address","attr", "id", "legalaName","level", "name", "pcs", "phone", "sex","state", "tradeId", "type", "zjnum" ,"zjtype","age"});
                if(json.size() != 0){
                    setEditViewData(json);
                    mInteger = ((Integer) json.get("id"));
                }
            }

            if(msg.what == 0x000010){
                int num = (int) msg.obj;
                if(num >= 100){
                    mMore_Uri.setText("上传完成");
                }else {
                    mMore_Uri.setText(num+"%");
                }
            }


            if(msg.what == 0x000007){
                Bundle data = msg.getData();
                ArrayList<String> arrayList = data.getStringArrayList("delete_android_img");
                if(arrayList.size() > 0){
                    // TODO: 2018/6/8 执行删除
                    //删除Android数据库中压缩的图片
                    // TODO: 2018/6/8 执行删除的原因是因为再之前插入图片地址到Android数据库中
                    for (int i = 0; i < arrayList.size(); i++) {
                        if(arrayList.get(i) != null){
                            ScaleImage.DeleteImage(arrayList.get(i),getActivity());
                        }
                    }
                }
            }


            if(msg.what == 0x000008){

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

                    Configfile.Log(getActivity(),"上传完成！");
                    /* 设置上传按钮可以被点击 */
                    mMore_Submit.setClickable(true);
                    mMore_Submit.setText("修改");
                    mMore_Submit.setBackgroundColor(Color.rgb(63,81,181));
                    e.setPhotoUrl(mPathMax);
                }else {
                    Configfile.Log(getActivity(),"[ERROR]"+ json);
                }
            }
        }
    };

    public String getUnitId(){
        /* 获取固定xmlId 存储的单位id值 */
        SharedPreferences xmlId = getActivity().getSharedPreferences("xmlId", Context.MODE_PRIVATE);
        String xmlIdString = xmlId.getString("id", "0");
        Log.e(TAG,"haredPreferences()" + xmlIdString);
        return xmlIdString;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_more_update, null);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* 动态权限 */
        PermissionsUtils utils=new PermissionsUtils();
        utils.SetPermissionForNormal(getActivity());

        /* 获取固定xmlId 存储的单位id值 */
        xmlIdString = getUnitId();
        Log.e(TAG,"haredPreferences()" + xmlIdString);

        String path = Configfile.COMPANY_URL_SEARCH_ID + xmlIdString;
        Log.e("TAG","修改当前单位地址 >>> "+path);

        /* 执行下载 首次赋值单位的信息 */
        downData();



    }
    /* 首次或者跟新的时候使用器下载 */
    public void downData(){
        /* 服务地址 */
        final String path = Configfile.COMPANY_URL_SEARCH_ID + xmlIdString;
        Log.e("TAG",path);
        /* 查询 */
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = NetworkTools.NetShow(path);
                String mResult=new String(bytes);
                if(mResult != null){
                    Message msg=new Message();
                    msg.what=1;
                    msg.obj=mResult;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();


        /*new HttpUtils(5000).send(HttpRequest.HttpMethod.GET,
                path,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if(responseInfo.result != null){
                            Message msg=new Message();
                            msg.what = 1;
                            msg.obj = responseInfo.result;
                            mHandler.sendMessage(msg);
                            Log.e(TAG,"赋值前的数据 >>> "+responseInfo.result);
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });*/
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this,view);
    }


    /* 赋值单位信息 */
    void setEditViewData(Map<String,Object> map){

        /**
         * "address":"旺苍县白水镇快活村1组31号附1号",
         "attr":"",
         "id":15,
         "legalaName":"",
         "level":"",
         "name":"白水镇彭波副食经营部",
         "pcs":"白水派出所",
         "phone":"",
         "sex":1,
         "tradeId":0,
         "type":"",
         "zjnum":"",
         "zjtype":1
         */
        if(!((String) map.get("name")).equals("")){
            mMore_Name.setText(((String) map.get("name")));
        }else mMore_Name.setText("暂无数据");


        if(!((String) map.get("address")).equals("")){
            mMore_Addrs.setText(((String) map.get("address")));
        }else mMore_Addrs.setText("暂无数据");


        if(!((String) map.get("legalaName")).equals("")){
            mMore_LevName.setText(((String) map.get("legalaName")));
        }else mMore_LevName.setText("暂无数据");


        if(!((String) map.get("pcs")).equals("")){
            mMore_GovName.setText(((String) map.get("pcs")));
        }else mMore_GovName.setText("暂无数据");


        if(!((String) map.get("attr")).equals("")){
            mMore_AddrsType.setText(((String) map.get("attr")));
        }else mMore_AddrsType.setText("暂无数据");


        if(!((String) map.get("phone")).equals("")){
            mMore_Phone.setText(((String) map.get("phone")));
        }else mMore_Phone.setText("0");

        if((int) map.get("sex") == 1 ){
            mMore_Sex.setText("男");
        }else mMore_Sex.setText("女");

        if(!((String) map.get("level")).equals("")){
            mMore_Lev.setText(((String) map.get("level")));
        }else mMore_Lev.setText("0");


        Integer zjtype = (Integer) map.get("zjtype");
        if(zjtype != null){
            int i = zjtype.intValue();
            if(i == 1){
                mMore_LevType.setText("身份证");
            }else if( i == 2) {
                mMore_LevType.setText("户口本");
            }else {
                mMore_LevType.setText("其他");
            }
        }else {
            mMore_LevType.setText("暂无数据");
        }



        if(!((String) map.get("zjnum")).equals("")){
        mMore_Num.setText(((String) map.get("zjnum")));
        }else mMore_Num.setText("0"); //


        Integer age = (Integer) map.get("age");
        if(age != null){
            String mAge = String.valueOf(age.intValue());
            mMore_Age.setText(mAge);
        }else mMore_Age.setText("0"); //mMore_Age


    }

    @OnClick({R.id.mMore_Addrs_photo})
    public void selectImage(View v){
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .maxSelectable(3)
                .imageEngine(new PicassoEngine())
                .forResult(1);
    }

    BjcUnit e=new BjcUnit();
    CatLoadingView mCat;

    /* 提交按钮 */
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
        e.setId(mInteger);

        /* 转换JSON */
        String mResult=com.alibaba.fastjson.JSON.toJSONString(e);
        Log.e("TAG"," 预备提交的地址 >>>> "+ Configfile.UPDATE_COMPANY);
        Log.e("TAG"," 预备提交的实体JSON >>>> "+mResult);

        //NetworkTools.postHttpToolsUaerRegistite(Configfile.UPDATE_COMPANY,mHandler,mResult);
        //String path="http://ssdaixiner.oicp.net:26168/wcjw/mobile/doUpdateDanwei";
        NetworkTools.httpUpload(HttpRequest.HttpMethod.POST,"dataJson",mHandler,Configfile.UPDATE_COMPANY,
                mResult);

        mCat = new CatLoadingView();
        mCat.show(getChildFragmentManager(),"");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            final List<Uri> mSelected = Matisse.obtainResult(data);

            mMore_Uri.setText("准备中...");
            /* 设置上传按钮为灰色 */
            mMore_Submit.setClickable(false);
            mMore_Submit.setText("正在等待图片上传");
            mMore_Submit.setBackgroundColor(Color.rgb(212,212,212));
            Log.e("TAG", "mSelected: " + mSelected.toString() + " 大小 " +mSelected.size());
            e.setPhotoUrl(mSelected.get(0).toString());

            final ArrayList<String> mFilePath = new ArrayList<String>();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestParams params=new RequestParams();
                    for (int i = 0; i < mSelected.size(); i++) {
                        Bitmap bitmap = ScaleImage.execute(getActivity(), mSelected.get(i));
                        //获取文件的具体路径(数据库地址:content://media/external/images/media/273957)
                        String filePath = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null, null);
                        Uri uri=Uri.parse(filePath);
                        Log.e("IMG","压缩之后的图片路径 \n"+ filePath);
                        String pathFromUri = getRealPathFromUri(getActivity(), uri);
                        params.addBodyParameter("msg"+i,new File(pathFromUri));
                        mFilePath.add(pathFromUri);
                        //删除Android数据库中压缩的图片
                        // TODO: 2018/6/8 执行删除的原因是因为再之前插入图片地址到Android数据库中
                        //ScaleImage.DeleteImage(pathFromUri,AddCompanyActivity.this);
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

                                    if(mFilePath.size() > 0){
                                        Message msg=new Message();
                                        msg.what=0x000007;
                                        Bundle bundle=new Bundle();
                                        bundle.putStringArrayList("delete_android_img",mFilePath);
                                        msg.setData(bundle);
                                        mHandler.sendMessage(msg);

                                    }
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {

                                }

                                @Override
                                public void onLoading(long total, long current, boolean isUploading) {
                                    super.onLoading(total, current, isUploading);
                                    if(total != 0){ //也就是总大小不能为0
                                        int i = (int) ((current * 100) / total);
                                        Message msg=new Message();
                                        msg.what=0x000010;
                                        msg.obj=i;
                                        mHandler.sendMessage(msg);
                                        Log.e(TAG,"当前上传进度"+i+"%");
                                    }else {
                                        Log.e("Error","在使用上传的时候 total 的总大小造成为0");
                                    }

                                }
                            });
                }
            }).start();


        }
    }

    /**
     *
     * @param mContext
     * @param path
     * @param quality 0-100(100为原图)
     * @return
     */
    public String ScaleImage(Context mContext,String path, int quality){

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        Log.e("IMG",path+"\n 原始大小：" + baos.toByteArray().length);
        // 因为质量压缩不是可以无限缩小的，所以一张高质量的图片，再怎么压缩，
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        Log.e("IMG","\n 最终大小" + baos.toByteArray().length);
        Bitmap mBitmap = BitmapFactory.decodeByteArray(
                baos.toByteArray(), 0, baos.toByteArray().length);

        //通过bitmap拿到图片的uri
        String uriPath = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), mBitmap, "", "");
        Uri uri=Uri.parse(uriPath);
        //通过uri获取文件路径（在数据库）
        String fielPath = getRealPathFromUri(mContext, uri);
        return fielPath;
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


    /* 获取当前单位类型 */
    @OnClick({R.id.mMore_AddrsType})
    public void getAddrsType(View v){
        // 创建数据
        final String[] items = new String[] { "旅店业", "银行金融机构", "寄递物流", "校园","民爆物品","加油站" };
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    /* 返回更新数据 */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updete(MessageEvent msg){
        Log.e("DATA",msg.getMessage());
        downData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
}
