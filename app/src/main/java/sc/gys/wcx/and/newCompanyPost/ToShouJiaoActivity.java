package sc.gys.wcx.and.newCompanyPost;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import sc.gys.wcx.and.cat.CatLoadingView;
import sc.gys.wcx.and.help.CacheUtils;
import sc.gys.wcx.and.sanEntery.JwSjwp;
import sc.gys.wcx.and.sanEntery.JwSjwpDetails;
import sc.gys.wcx.and.tools.Configfile;
import sc.gys.wcx.and.tools.NetworkTools;
import sc.gys.wcx.and.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.com.mozile.tools.Base64Util;
import json.makvenis.com.mylibrary.json.JSON;

@ContentView(R.layout.layout_shoujiao)
public class ToShouJiaoActivity extends AppCompatActivity {

    @ViewInject(R.id.mAddLayout)
    ImageView mAddLayout;

    @ViewInject(R.id.mLinearLayoutAdd)
    LinearLayout mLinearLayoutAdd;

    @ViewInject(R.id.mRemoveLayout)
    LinearLayout mRemoveLayout;

    @ViewInject(R.id.mSubmit)
    Button mSubmit;

    @ViewInject(R.id.mObjectGroup)
    RadioGroup mObjectGroup;

    /* 下列所属物品持有人 */
    @ViewInject(R.id.mObject_cyr)
    EditText mObject_cyr;

    /* 持有人签名 */
    @ViewInject(R.id.item_mObject_cyr_img) //持有人点击事件
    ImageView item_mObject_cyr_img;

    @ViewInject(R.id.item_mObject_cyr_set) //持有人签名设置
    ImageView item_mObject_cyr_set;

    /* 保管人签名 */
    @ViewInject(R.id.item_mObject_bgr_img) //保管人点击事件
    ImageView item_mObject_bgr_img;

    @ViewInject(R.id.item_mObject_bgr_set) //保管人签名设置
    ImageView item_mObject_bgr_set;

    /* 办案民警签名 */
    @ViewInject(R.id.item_mObject_bgr_bamj_img) //保管人点击事件
    ImageView item_mObject_bgr_bamj_img;

    @ViewInject(R.id.item_mObject_bgr_bamj_set) //保管人签名设置
    ImageView item_mObject_bgr_bamj_set;

    /* 布局个数 */
    int  fla = 0;

    /* 全局实体类 */
    JwSjwp e=new JwSjwp();
    private String id;
    private String mCfId;
    private String mTextName;

    /* 全局等待dialog */
    CatLoadingView cat;

    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what){
                case Configfile.CALLBANK_POST_MSG:
                    String obj = (String) msg.obj;
                    if(obj != null){
                        try {
                            JSONObject mObj = new JSONObject(obj);
                            String state = mObj.optString("state");
                            if(state.equals("OK")){
                                Configfile.Log(ToShouJiaoActivity.this,"提交成功！");
                                cat.dismiss();
                            }else {
                                Configfile.Log(ToShouJiaoActivity.this,"提交失败！");
                                cat.dismiss();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        /* 获取父类传递的数值 */
        getParement();

        mObjectGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.mList_1:
                        e.setTiaokuan(1+"");
                        break;
                    case R.id.mList_2:
                        e.setTiaokuan(2+"");
                        break;
                    case R.id.mList_3:
                        e.setTiaokuan(3+"");
                        break;
                    case R.id.mList_4:
                        e.setTiaokuan(4+"");
                        break;
                }
            }
        });



    }

    /* 处理持有人签名事件 */
    @OnClick({R.id.item_mObject_cyr_set})
    public void cyr(View v){
        getGesture(item_mObject_cyr_img,"cyr");
    }

    /* 处理保管人签名事件 */
    @OnClick({R.id.item_mObject_bgr_set})
    public void bgr(View v){
        getGesture(item_mObject_bgr_img,"bgr");
    }

    /* 处理办案民警签名事件 */
    @OnClick({R.id.item_mObject_bgr_bamj_set})
    public void bamj(View v){
        getGesture(item_mObject_bgr_bamj_img,"bamj");
    }

    /* 获取json类型的数据 */
    public String createJsonTable(){
        /* 获取子视图的个数 */
        int childCount = mLinearLayoutAdd.getChildCount();

        JSONArray array = new JSONArray();

        for (int i = 0; i < childCount; i++) {
            View childView = mLinearLayoutAdd.getChildAt(i);
            /* 获取控件 并且获取里面的值 */
            JSONObject jsonObject = getViewChild(childView);
            array.put(jsonObject);
        }

        return array.toString();
    }

    @OnClick({R.id.mRemoveLayout})
    public void removeLayout(View v){
        if(fla > 0){
            mLinearLayoutAdd.removeViewAt(fla-1);
            fla--;
        }else {
            Configfile.Log(this,"不能再移除了!");
        }
    }

    @OnClick({R.id.mSubmit})
    public void postData(View v){
        /* 启动等待 */
        cat=new CatLoadingView();
        cat.show(getSupportFragmentManager(),"");

        String table = createJsonTable();
        Log.e("TAG","自动生成的数据:"+table);

        /* 物品持有人 */
        String mObjectCyr = mObject_cyr.getText().toString();

        /* 单位id */
        e.setUnitId(id); //单位id

        /* 获取签名的数据（持有人，保管人，办案民警{cyr,bgr,bamj}） */
        String cyr = getQianMingString("cyr");
        String bgr = getQianMingString("bgr");
        String bamj = getQianMingString("bamj");
        e.setCyrSignature(cyr);      //持有人
        e.setBgrSignature(bgr);      //保管人
        e.setPoliceSignature(bamj);  //办案民警

        e.setId(Integer.valueOf(0)); //主键ID
        // TODO: 2018/6/22 处罚ID
        e.setChufaid(Integer.valueOf(mCfId)); //处罚ID
        e.setBianhao1("缴");         //编号1
        e.setBianhao2("收缴");        //编号2
        e.setCyrName(mObjectCyr);    //物品持有人
        e.setSjTime(new Date());     //填写时间

        /* 拆分 */
        List<Map<String, String>> maps = JSON.GetJson(table, new String[]{"mObjectName", "mObjectNumble", "mObjectType", "mObjectSetMethod"});
        List<JwSjwpDetails> mDetails = new ArrayList<>();
        for (int i = 0; i < maps.size(); i++) {
            Map<String, String> map = maps.get(i);
            JwSjwpDetails s=new JwSjwpDetails();
            s.setSjwpnum(Integer.valueOf(map.get("mObjectNumble")));//收缴数量
            s.setSjwpFeature(map.get("mObjectType"));               //物品特征
            s.setSjwpname(map.get("mObjectName"));                  //物品名称
            s.setSjwpDispose(map.get("mObjectSetMethod"));          //收缴物品的处理情况
            mDetails.add(s);
        }
        e.setJwSjwpDetails(mDetails);
        String mResult=com.alibaba.fastjson.JSON.toJSONString(e);
        //Log.e("TAG"," 旺苍县公安局责令限期整改治安隐患通知书ID >>>> "+id);
        //Log.e("TAG"," 预备提交的地址 >>>> "+mPath_post);
        Log.e("V"," 预备提交收缴物品的实体JSON >>>> "+mResult);
        //Configfile
        NetworkTools.postHttpToolsUaerRegistite(Configfile.SHOUJIAO_POST_PATH,mHandler,mResult);
    }

    /* 获取检查签名的字符串 */
    public String getQianMingString(String fileName){
        File mFile = this.getExternalFilesDir(null);
        String cyr_path = mFile.getAbsoluteFile() + fileName + ".png";
        byte[] cyr = Base64Util.getImageStr(cyr_path);
        String data= "data:image/png;base64," + new String(cyr);
        return data;
    }

    @OnClick({R.id.mAddLayout})
    public void addMoreLayout(View v){
        //此处要获取其他xml的控件需要先引入改layout的view(这个linearlayout用于演示添加和删除)
        View view= LayoutInflater.from(this).inflate(R.layout.public_shoujiao_include,null,false);
        mLinearLayoutAdd.addView(view);
        fla++;
        mLinearLayoutAdd.setTag(fla);
        TextView mObjectFlag = (TextView) view.findViewById(R.id.mObjectFlag);
        mObjectFlag.setText("收缴物品序号"+fla+"");
    }

    public JSONObject getViewChild(View v) {
        TextView mObjectFlag = (TextView) v.findViewById(R.id.mObjectFlag);
        EditText mObjectName = (EditText) v.findViewById(R.id.mObjectName);
        EditText mObjectNumble = (EditText) v.findViewById(R.id.mObjectNumble);
        EditText mObjectType = (EditText) v.findViewById(R.id.mObjectType);
        EditText mObjectSetMethod = (EditText) v.findViewById(R.id.mObjectSetMethod);
        String mData1 = mObjectName.getText().toString();
        String mData2 = mObjectNumble.getText().toString();
        String mData3 = mObjectType.getText().toString();
        String mData4 = mObjectSetMethod.getText().toString();
        String mData0 = mObjectFlag.getText().toString();

        try {
            JSONObject opt=new JSONObject();
            opt.put("mObjectFlag",mData0);
            opt.put("mObjectName",mData1);
            opt.put("mObjectNumble",mData2);
            opt.put("mObjectType",mData3);
            opt.put("mObjectSetMethod",mData4);
            return opt;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }


    /* AlertDialog 的点击事件 */
    /**
     * @param imageView 用户画好界面的图之后赋值到UI（imageView）
     * @param fileName 当使用Gesture来实现手势绘图的时候 当绘制完毕需要给图片命名
     */
    public void getGesture(final ImageView imageView,final String fileName) {

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
                CacheUtils.setWLocalCache(bitmap,fileName,ToShouJiaoActivity.this);
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
                        Bitmap localCache = CacheUtils.getLocalCache(fileName,ToShouJiaoActivity.this);
                        imageView.setImageBitmap(localCache);
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

    public void getParement() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id"); //单位idx
        mCfId = bundle.getString("mLocal_bianhao");
        mTextName = bundle.getString("l_title");

    }
}
