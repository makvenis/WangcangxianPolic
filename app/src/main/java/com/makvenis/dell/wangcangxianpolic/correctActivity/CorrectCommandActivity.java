package com.makvenis.dell.wangcangxianpolic.correctActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.support.annotation.IdRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.CacheUtils;
import com.makvenis.dell.wangcangxianpolic.help.PermissionsUtils;
import com.makvenis.dell.wangcangxianpolic.otherActivity.HistoryUtils;
import com.makvenis.dell.wangcangxianpolic.sanEntery.CorrecTmsgWithBLOBs;
import com.makvenis.dell.wangcangxianpolic.sanEntery.JwNowchufamsg;
import com.makvenis.dell.wangcangxianpolic.sanEntery.JwYinhuanMsg;
import com.makvenis.dell.wangcangxianpolic.startActivity.BaseActivity;
import com.makvenis.dell.wangcangxianpolic.startActivity.WebPostRemarkActivity;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.NetworkTools;
import com.makvenis.dell.wangcangxianpolic.view.SimpleLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.mozile.tools.Base64Util;


/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 责令()通知书 */

/**
 * @ 解释: 采用全局页面 通过传递需要显示CardView 来构建数据的输入输出
 * @ 使用: 所有列表页面(整改，抗诉等...)
 */

@ContentView(R.layout.activity_correct_command)
public class CorrectCommandActivity extends BaseActivity {

    @ViewInject(R.id.CollapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    /* 全局采用寻找控件方法查找 */
    /* 通过采用获取方式提交数据 */

    /* 获取的单位ID */
    private String id;

    /* Start Find  */
    @ViewInject(R.id.item_mEditView_danwei)
    EditText mTextView_danwei; //单位名称

    @ViewInject(R.id.item_mRadioGroup)
    RadioGroup mRadioGroup_RadioGroup; //性别

    @ViewInject(R.id.item_mEditView_nianling)
    EditText mTextView_nianling;

    @ViewInject(R.id.item_mImageView_onClink)
    ImageView mImageView_nianling_onClink; //出生日期图片点击事件

    @ViewInject(R.id.item_mEditView_shengri)
    TextView mTextView_nianling_set; //出生日期设置赋值

    @ViewInject(R.id.item_Spinner)
    Spinner mSpinner; //身份证种类

    @ViewInject(R.id.item_mEditView_sfzNum)
    EditText mEditText_sfzNum; //证件号码

    @ViewInject(R.id.item_mEditView_faren)
    EditText mEditText_faren; //法定代表人

    @ViewInject(R.id.item_mEditView_danweiaddress)
    EditText mEditText_danweiaddress; //单位地址

    @ViewInject(R.id.item_mEditView_chaming)
    EditText mEditText_chaming; //查明

    @ViewInject(R.id.item_mEditView_zhengshi)
    EditText mEditText_zhengshi; //证据证实

    @ViewInject(R.id.item_mEditView_tiaoKuanName)
    EditText mEditText_tiaoKuanName; //条款名称/条/款
    @ViewInject(R.id.item_mEditView_tiaoName)
    EditText mEditText_tiaoName; //条款名称/条/款
    @ViewInject(R.id.item_mEditView_kuanName)
    EditText mEditText_kuanName; //条款名称/条/款
    @ViewInject(R.id.item_mEditView_xiang)
    EditText mEditText_xiang; //条款名称/条/款

    @ViewInject(R.id.item_mEditView_chufa)
    EditText mEditText_chufa; //给予的处罚

    @ViewInject(R.id.item_mImageView_DingweiOnClink)
    ImageView mImageView_DingweiOnClink; //处罚地点 图片的点击事件

    @ViewInject(R.id.item_mEditView_Dingwei_set)
    TextView mTextView_Dingwei_set; //给予的处罚 地址赋值

    @ViewInject(R.id.item_mImageView_setName)
    ImageView mImageView_setName; //签名 赋值

    @ViewInject(R.id.item_mImageView_setNameonClink)
    ImageView mTextView_setNameonClink; //签名 图片的点击事件

    @ViewInject(R.id.item_mImageView_bjc_setName) // 被检查人的签名
    ImageView mImageView_bjc_setName;

    @ViewInject(R.id.item_mImageView_bjc_setNameonClink) // 被检查人的签名 item_mImageView_bjc_setNameonClink
    ImageView mImageView_bjc_Oncklink;

    @ViewInject(R.id.mRadioGroup_zxcs)
    RadioGroup mRadioGroup_cuoshi; //整改措施

    @ViewInject(R.id.item_mLinearLayout)
    LinearLayout mRadioGroup_RadioGroup_mLinearLayout; //整改措施 当立即停止的时候出现的布局

    @ViewInject(R.id.item_mImageView_onClink_Cfrq)
    ImageView mImageView_Cfrq_onClink; //检查日期图片点击事件

    @ViewInject(R.id.item_mEditView_cfrq)//
    TextView mTextView_Cfrq_set; //检查日期设置赋值

    @ViewInject(R.id.item_mImageView_onClink_overTime)
    ImageView mImageView_overTime_onClink; //整改完毕图片点击事件

    @ViewInject(R.id.item_mEditView_overTime)//
    TextView mTextView_overTime_set; //整改完毕日期设置赋值

    @ViewInject(R.id.item_mTextView_timer_zgcsOnClink) //当选择的不是 立即停止 那么就会出现另一种方案
    ImageView mOverTime_set_onclink; // 在规定时间内必须整改完毕

    @ViewInject(R.id.item_mTextView_timer_zgcs) // 在规定时间内整改内容
    TextView mOverTime_set_zgcs;

    @ViewInject(R.id.mText_overTime_set) // 在规定时间内整改内容
    TextView mOverTime_set_text;

    @ViewInject(R.id.mEdit_type_wg) // 旺公
    EditText mEdit_type_wg;

    @ViewInject(R.id.mEdit_type_ztz) // 责通字   // mEdit_type_xq_wg  mEdit_type_xq_ztz  mEdit_type_xq_num
    EditText mEdit_type_ztz;

    @ViewInject(R.id.mEdit_type_xq_wg) // 旺公(限期整改)
    EditText mEdit_type_xq_wg;

    @ViewInject(R.id.mEdit_type_xq_ztz) // 限字(限期整改)
    EditText mEdit_type_xq_ztz;

    @ViewInject(R.id.mEdit_type_xq_num) // 第()号(限期整改)
    EditText mEdit_type_xq_num;         //

    @ViewInject(R.id.mEdit_type_xq_tiaoli) // 根据《企业事业单位内部治安保卫条例》() 条(限期整改)
    EditText mEdit_type_xq_tiaoli;

    @ViewInject(R.id.mEdit_type_xq_cuoshi) // 采取措施(限期整改)
    EditText mEdit_type_xq_cuoshi;

    @ViewInject(R.id.mRadioGroup_chufa_zxfs) //执行方式 { 当场处罚决定书 }
    RadioGroup mRadioGroup_chufa_zxfs;

    @ViewInject(R.id.mEdit_type_Chinese_type) //签名中文
    EditText mEditText_chanies;

    @ViewInject(R.id.mCardView_1)
    CardView mCardView_1;

    @ViewInject(R.id.mCardView_2)
    CardView mCardView_2;

    @ViewInject(R.id.mCardView_3)
    CardView mCardView_3;

    @ViewInject(R.id.mCardView_4)
    CardView mCardView_4;

    @ViewInject(R.id.mCardView_5)
    CardView mCardView_5;

    @ViewInject(R.id.mCardView_6)
    CardView mCardView_6;

    @ViewInject(R.id.mCardView_7)
    CardView mCardView_7;

    @ViewInject(R.id.mCardView_8)
    CardView mCardView_8;

    @ViewInject(R.id.mCardView_9)
    CardView mCardView_9;

    @ViewInject(R.id.mCardView_10)
    CardView mCardView_10;

    @ViewInject(R.id.mCardView_11)
    CardView mCardView_11;

    @ViewInject(R.id.mCardView_12)
    CardView mCardView_12;

    @ViewInject(R.id.mCardView_13)
    CardView mCardView_13;

    @ViewInject(R.id.mCardView_14)
    CardView mCardView_14;

    @ViewInject(R.id.mCardView_15)
    CardView mCardView_15;

    @ViewInject(R.id.mCardView_16)
    CardView mCardView_16;

    @ViewInject(R.id.mCardView_17)
    CardView mCardView_17;

    @ViewInject(R.id.mCardView_18)
    CardView mCardView_18;

    @ViewInject(R.id.mCardView_19)
    CardView mCardView_19;

    @ViewInject(R.id.mCardView_20)
    CardView mCardView_20;

    @ViewInject(R.id.mCardView_21)
    CardView mCardView_21;

    @ViewInject(R.id.mCardView_22)
    CardView mCardView_22;

    @ViewInject(R.id.mCardView_23)
    CardView mCardView_23;
    /* End Find  */

/*    @ViewInject(R.id.toolbar_bank)
    ImageView mImageView_bank;*/

    /* 提交按钮 */
    @ViewInject(R.id.mPostData)
    Button mButton;

    /* 获取上一级传递过来的标题 */
    private String mTitleParment;
    private ArrayList<String> mGoneParment;
    /* 获取回调地址 */
    private String mPath;

    /* 预备提交的数据集合 */
    final List<String> mObjectData=new ArrayList<>();
    final Map<String,String> mObjectMapData = new HashMap<>();
    /* 全局Dialog */
    SimpleLoadingDialog dialog;

    /* 全局Handler */
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            String obj = (String) msg.obj;
            JSONObject object= null;
            try {
                object = new JSONObject(obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (what){
                case Configfile.CALLBANK_POST_MSG:
                    if(mTitleParment.equals("旺苍县公安局责令改正通知书")){
                        String state = object.optString("state");
                        if(state.equals("OK")){
                            Configfile.Log(CorrectCommandActivity.this,"提交成功");
                            String bianhao = object.optString("bianhao");
                            Log.e("TAG","旺苍县公安局责令改正通知书--预备跳转的地址和编号"+Configfile.SERVICE_WEB+"toshowxqzg"+" 编号 "+bianhao);
                            jumpActivity(bianhao,"旺苍县公安局责令改正通知书",Configfile.SERVICE_WEB+"toshowgaizheng");
                        }
                        dialog.dismiss();
                    }else if(mTitleParment.equals("旺苍县公安局当场处罚决定书")){
                        String state = object.optString("state");
                        if(state.equals("OK")){
                            Configfile.Log(CorrectCommandActivity.this,"提交成功");
                            String bianhao = object.optString("bianhao");
                            jumpActivity(bianhao,"旺苍县公安局当场处罚决定书",Configfile.SERVICE_WEB+"toshowchufa");
                        }
                        dialog.dismiss();
                    }else if(mTitleParment.equals("旺苍县公安局责令限期整改治安隐患通知书")){
                        String state = object.optString("state");
                        if(state.equals("OK")){
                            Configfile.Log(CorrectCommandActivity.this,"提交成功");
                            String bianhao = object.optString("bianhao");
                            jumpActivity(bianhao,"旺苍县公安局责令限期整改治安隐患通知书",Configfile.SERVICE_WEB+"toshowxqzg");
                        }
                        dialog.dismiss();
                    }else {
                        Configfile.Log(CorrectCommandActivity.this,"提交成功失败！");
                        dialog.dismiss();
                    }

                    break;
            }
        }
    };


    /* 提交成功跳向详情界面 */
    public void jumpActivity(String bianhao,String mtitle,String url){
        Intent intent=new Intent(this, WebPostRemarkActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("bianhao",bianhao);
        bundle.putString("mUrl",url);
        bundle.putString("mtitle",mtitle);
        bundle.putString("id",id);
        intent.putExtras(bundle);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        /* 调用日志文件 */
        HistoryUtils.executeAddrs(CorrectCommandActivity.this,
                id,mtitle,"完成");
    }

    /* 数据提交 */
    public void setDataUser(){

        //final List<String> mObjectData=new ArrayList<>();
        for (int i = 0; i < mGoneParment.size() ; i++) {
            String defaultGone = mGoneParment.get(i);
            if(defaultGone .equals("mCardView_1") ){
                String danwei = mTextView_danwei.getText().toString();
                mObjectMapData.put("mCardView_1",danwei);
                mObjectData.add(danwei);
            }else if (defaultGone .equals("mCardView_2")){
                mRadioGroup_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        switch (checkedId){
                            case R.id.item_mTextView_sex_man:
                                mObjectData.add("男");
                                mObjectMapData.put("mCardView_2","男");
                                break;
                            case R.id.item_mTextView_sex_woman:
                                mObjectData.add("女");
                                mObjectMapData.put("mCardView_2","女");
                                break;
                        }
                    }
                });
            }else if (defaultGone .equals("mCardView_3")){
                String nianling = mTextView_nianling.getText().toString();
                mObjectMapData.put("mCardView_3",nianling);
                mObjectData.add(nianling);
            }else if (defaultGone .equals("mCardView_4")){
                String chuehngnianyue = mTextView_nianling_set.getText().toString();
                mObjectMapData.put("mCardView_4",chuehngnianyue);
                mObjectData.add(chuehngnianyue);
            }else if (defaultGone .equals("mCardView_5")){

            }else if (defaultGone .equals("mCardView_6")){
                String sfzNum = mEditText_sfzNum.getText().toString();
                mObjectMapData.put("mCardView_6",sfzNum);
                mObjectData.add(sfzNum);
            }else if (defaultGone .equals("mCardView_7")){
                String faren = mEditText_faren.getText().toString();
                mObjectMapData.put("mCardView_7",faren);
                mObjectData.add(faren);
            }else if (defaultGone .equals("mCardView_8")){
                String danweiPath = mEditText_danweiaddress.getText().toString();
                mObjectMapData.put("mCardView_8",danweiPath);
                mObjectData.add(danweiPath);
            }else if (defaultGone .equals("mCardView_9")){
                String chaming = mEditText_chaming.getText().toString();
                mObjectMapData.put("mCardView_9",chaming);
                mObjectData.add(chaming);
            }else if (defaultGone .equals("mCardView_10")){
                String zhengshi = mEditText_zhengshi.getText().toString();
                mObjectMapData.put("mCardView_10",zhengshi);
                mObjectData.add(zhengshi);
            }else if (defaultGone .equals("mCardView_11")){
                String tiao = mEditText_tiaoKuanName.getText().toString();
                String kuan = mEditText_tiaoName.getText().toString();
                String xiang = mEditText_kuanName.getText().toString();
                String xiang2 = mEditText_xiang.getText().toString();
                mObjectMapData.put("tiao",tiao); // 名称
                mObjectMapData.put("kuan",kuan); // 条
                mObjectMapData.put("xiang",xiang); // 款
                mObjectMapData.put("xiang2",xiang); // 项
                mObjectData.add(tiao);
                mObjectData.add(kuan);
                mObjectData.add(xiang);
            }else if (defaultGone .equals("mCardView_12")){
                String geiyuchufa = mEditText_chufa.getText().toString();
                mObjectMapData.put("mCardView_12",geiyuchufa);
                mObjectData.add(geiyuchufa);
            }else if (defaultGone .equals("mCardView_13")){

            }else if (defaultGone .equals("mCardView_14")){
                String dingwei = mTextView_Dingwei_set.getText().toString();
                mObjectMapData.put("mCardView_14",dingwei);
                mObjectData.add(dingwei);
            }else if (defaultGone .equals("mCardView_15")){
                /* 路径 */
                File mFile = this.getExternalFilesDir(null);
                String path = mFile.getAbsoluteFile() + "ming" + ".png";
                byte[] bytes = Base64Util.getImageStr(path);
                mObjectMapData.put("mCardView_15","data:image/png;base64," + new String(bytes));
                mObjectData.add("data:image/png;base64," + new String(bytes));
            }else if (defaultGone .equals("mCardView_16")){
                String cfrq = mTextView_Cfrq_set.getText().toString();
                mObjectMapData.put("cfrq",cfrq); // 处罚日期
            }else if (defaultGone .equals("mCardView_17")){ //整改完毕的时间
                String time = mTextView_overTime_set.getText().toString();
                mObjectMapData.put("mCardView_17",time);
                mObjectData.add(time);
            }else if(defaultGone.equals("mCardView_18")){    //mCardView_18
                String wg = mEdit_type_wg.getText().toString();
                String ztz = mEdit_type_ztz.getText().toString();
                mObjectMapData.put("wg",wg);
                mObjectMapData.put("ztz",ztz);

            }else if(defaultGone.equals("mCardView_19")){    //mCardView_19
                String xq_wg = mEdit_type_xq_wg.getText().toString();
                String xq_ztz = mEdit_type_xq_ztz.getText().toString();
                String xq_num = mEdit_type_xq_num.getText().toString();
                mObjectMapData.put("xq_wg",xq_wg);
                mObjectMapData.put("xq_ztz",xq_ztz);
                mObjectMapData.put("xq_num",xq_num);

            }else if(defaultGone.equals("mCardView_20")){    //mCardView_20
                String xq_tiaoli = mEdit_type_xq_tiaoli.getText().toString();
                String xq_cuoshi = mEdit_type_xq_cuoshi.getText().toString();
                mObjectMapData.put("xq_tiaoli",xq_tiaoli);
                mObjectMapData.put("xq_cuoshi",xq_cuoshi);

            }else if(defaultGone.equals("mCardView_21")){    //mCardView_21
                //处罚 三种算则方式 不需要复制 隶属于RadioGroup

            }else if(defaultGone.equals("mCardView_22")){    //mCardView_22
                /* 路径 */
                File mFile = this.getExternalFilesDir(null);
                String path = mFile.getAbsoluteFile() + "bjcming" + ".png";
                byte[] bytes = Base64Util.getImageStr(path);
                mObjectMapData.put("mCardView_22","data:image/png;base64," + new String(bytes));
                mObjectData.add("data:image/png;base64," + new String(bytes));

            }else if(defaultGone.equals("mCardView_23")){    //mCardView_22
                String chaines = mEditText_chanies.getText().toString();
                mObjectMapData.put("mCardView_23",chaines);
            }

        }

    }

    /* 根据不同的的表提交不同的地址 */
    public String postPathService(){
        if(mTitleParment.equals("旺苍县公安局责令限期整改治安隐患通知书")){
            return Configfile.OVER_POST_CORECT_YINHUAN;
        }else if(mTitleParment.equals("旺苍县公安局责令改正通知书")){
            return Configfile.OVER_POST_CORECT;
        }else if(mTitleParment.equals("旺苍县公安局当场处罚决定书")){
            return Configfile.OVER_POST_CORECT_CHUFA;
        }

        return null;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_correct_command);
        ViewUtils.inject(this);
        /* 获取读写权限 */
        PermissionsUtils permissionsUtils=new PermissionsUtils();
        permissionsUtils.SetPermissionForNormal(this);

        /* 获取上一级页面WebViewActivity传递过来的标题 */
        Bundle bundle = getIntent().getExtras();
        mTitleParment = bundle.getString("mTitle");//mGoneData
        mGoneParment = bundle.getStringArrayList("mGoneData");//mGoneData
        mCollapsingToolbarLayout.setTitle(mTitleParment);
        /**
         * 扩张时候的title颜色：
         * mCollapsingToolbarLayout.setExpandedTitleColor();
         * 收缩后在Toolbar上显示时的title的颜色：
         * mCollapsingToolbarLayout.setCollapsedTitleTextColor();
         */
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.BLUE);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);


        id = bundle.getString("id");

        /* 获取当前允许显示的字段 */
        //List<String> mGoneData = new ArrayList<>();
        getParentViewGone(mGoneParment);

        /* 部分点击事件的处理 例如 地址、签名等 */
        setAllOnClinkListener();

        /* 其他点击事件 */
        //setOtherClinkListener();

    }

/*    private void setOtherClinkListener() {
        // 返回WebViewActivity主页
        mImageView_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CorrectCommandActivity.this, WebViewActivity.class));
            }
        });
    }*/

    private void setAllOnClinkListener() {
        /* 出生日期的选择 */
        mImageView_nianling_onClink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTimer(CorrectCommandActivity.this,mTextView_nianling_set);
            }
        });
        /* 身份证选择 */
        getSelect(mSpinner,R.array.langItem);

        /* 性别的选择  */
        mRadioGroup_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.item_mTextView_sex_man:
                        // TODO: 2018/4/19 存入集合上传到数据库  男
                        Configfile.Log(CorrectCommandActivity.this,"点击了男");

                        break;
                    case R.id.item_mTextView_sex_woman:
                        // TODO: 2018/4/19 存入集合上传到数据库  女
                        Configfile.Log(CorrectCommandActivity.this,"点击了女");
                        break;
                }
            }
        });

        /* 整改措施 mRadioGroup_RadioGroup_cuoshi */
        mRadioGroup_cuoshi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.item_mRadioGroup_zxcs_1:
                        // TODO: 2018/4/19 存入集合上传到数据库  立即予以改正
                        if(mRadioGroup_RadioGroup_mLinearLayout.getVisibility() == View.VISIBLE){
                            mRadioGroup_RadioGroup_mLinearLayout.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.item_mRadioGroup_zxcs_2:
                        // TODO: 2018/4/19 存入集合上传到数据库  立即停
                        mRadioGroup_RadioGroup_mLinearLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        /* 处罚执行方式 { 限当场处罚使用 } */
        mRadioGroup_chufa_zxfs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.mRadioGroup_chufa_zxcs_1:
                        mObjectMapData.put("chufa_zxfs","当场训诫");
                        break;
                    case R.id.mRadioGroup_chufa_zxcs_2:
                        mObjectMapData.put("chufa_zxfs","当场收缴罚款");
                        break;
                    case R.id.mRadioGroup_chufa_zxcs_3:
                        mObjectMapData.put("chufa_zxfs","被处罚人持本决定书在十五日内到银行缴纳罚");
                        break;

                }
            }
        });

        /* 被检查人签名 */
        mImageView_bjc_Oncklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGesture(mImageView_bjc_setName,"bjcming");
            }
        });


        /* 签名事件 监察人签名 */
        mTextView_setNameonClink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGesture(mImageView_setName,"ming");
            }
        });

        /* 处罚地址 */
        mImageView_DingweiOnClink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView_Dingwei_set.setText(mPath);
                mTextView_Dingwei_set.setTextColor(Color.RED);
                mTextView_Dingwei_set.setTextSize(14f);
            }
        });

        /* 检查日期 */
        mImageView_Cfrq_onClink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTimer(CorrectCommandActivity.this,mTextView_Cfrq_set);
            }
        });

        /* 在规定时间内必须整改完毕的点击事件 */
        mOverTime_set_onclink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTimer(CorrectCommandActivity.this,mOverTime_set_zgcs);
            }
        });

        mImageView_overTime_onClink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTimer(CorrectCommandActivity.this,mTextView_overTime_set);
            }
        });

        /* 提交 */
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动dialog
                dialog=new SimpleLoadingDialog(CorrectCommandActivity.this);
                dialog.setMessage("提交数据中");
                dialog.show();
                // 获取用户输入
                setDataUser();
                Log.e("TAG"," 预备提交的数据  >>>> " + mObjectData.toString());
                // 获取提交的路径
                String mPath_post = postPathService();
                if(mTitleParment.equals("旺苍县公安局责令改正通知书")){
                    CorrecTmsgWithBLOBs e=new CorrecTmsgWithBLOBs();
                    e.setBianhao1(mObjectMapData.get("wg"));                    // 旺公[]
                    e.setBianhao2(Integer.valueOf(mObjectMapData.get("ztz")));  // 责通字[]
                    e.setBianhao3(Integer.valueOf(0));                          // 自增字段
                    e.setContent(mObjectMapData.get("mCardView_9"));            // 违法行为
                    e.setFagui(mObjectMapData.get("tiao"));                     // 法规
                    e.setId(Integer.valueOf(0));                                // 主键
                    e.setUnitid(Integer.valueOf(id));                           // 单位ID
                    e.setBjcSignature(mObjectMapData.get("mCardView_15"));      // 图片
                    e.setContent(mObjectMapData.get("mCardView_9"));            // 存在的具体违法行为
                    /* 拼接 13 */
                    String zgcs_time = mOverTime_set_zgcs.getText().toString();
                    String zgcs_neirong = mOverTime_set_text.getText().toString();
                    Log.e("DATA","整改完毕时间"+zgcs_time);
                    if(!zgcs_time.equals("")){
                        e.setGzWay("在"+zgcs_time+"之前改正或者整改完毕，并将结果函告我单位。在期限届满之前，你（单位）必须"+ zgcs_neirong);
                    }else {
                        e.setGzWay("立即停止");
                    }
                    /* 时间规范化 */
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String t = mObjectMapData.get("cfrq");
                    try {
                        Date date = sdf.parse(t);
                        e.setTzsTime(new Date());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    /* 转换JSON */
                    String mResult=com.alibaba.fastjson.JSON.toJSONString(e);
                    Log.e("TAG"," 旺苍县公安局责令改正通知书ID >>>> "+id);
                    Log.e("TAG"," 预备提交的地址 >>>> "+mPath_post);
                    Log.e("TAG"," 预备提交的实体JSON >>>> "+mResult);

                    NetworkTools.postHttpToolsUaerRegistite(mPath_post,mHandler,mResult);
                }else if(mTitleParment.equals("旺苍县公安局当场处罚决定书")){
                    /**
                     *
                     *mObjectMapData.put("tiao",tiao); // 名称
                     *mObjectMapData.put("kuan",kuan); // 条
                     *mObjectMapData.put("xiang",xiang); // 款
                     *mObjectMapData.put("xiang2",xiang); // 项
                     */

                    JwNowchufamsg e=new JwNowchufamsg();
                    e.setUnitid(Integer.valueOf(id));                           //单位ID
                    e.setBianhao(Integer.valueOf("0"));                         //编号
                    e.setChufaAddress(mObjectMapData.get("mCardView_14"));      //触发地点
                    e.setChufaContent(mObjectMapData.get("mCardView_12"));      //给予的触发
                    e.setChufamesTime(new Date());                              //出大下达的时间
                    e.setId(Integer.valueOf(0));                                //主键
                    e.setKuannum(Integer.valueOf(mObjectMapData.get("xiang")));  //款
                    e.setTiaoli(mObjectMapData.get("tiao"));                     //条例《...》
                    e.setXiangnum(Integer.valueOf(mObjectMapData.get("xiang2")));//项
                    e.setTiaonum(Integer.valueOf(mObjectMapData.get("kuan")));   //条
                    e.setPolicename(mObjectMapData.get("mCardView_15"));         //警官名称
                    e.setBjcSignature(mObjectMapData.get("mCardView_22"));       //被检查人签名
                    e.setContent1(mObjectMapData.get("mCardView_12"));           //证据事实
                    e.setContent2(mObjectMapData.get("mCardView_9"));            //查明
                    e.setChufaZxfs(mObjectMapData.get("chufa_zxfs"));            //执行方式
                    /* 转换JSON */
                    String mResult=com.alibaba.fastjson.JSON.toJSONString(e);
                    Log.e("TAG"," 旺苍县公安局当场处罚决定书ID >>>> "+id);
                    Log.e("TAG"," 预备提交的地址 >>>> "+mPath_post);
                    Log.e("TAG"," 预备提交的实体JSON >>>> "+mResult);

                    NetworkTools.postHttpToolsUaerRegistite(mPath_post,mHandler,mResult);
                }else if(mTitleParment.equals("旺苍县公安局责令限期整改治安隐患通知书")){
                    /**
                     * mObjectMapData.put("xq_wg",xq_wg);
                     * mObjectMapData.put("xq_ztz",xq_ztz);
                     * mObjectMapData.put("xq_num",xq_num);
                     */
                    JwYinhuanMsg e=new JwYinhuanMsg();
                    e.setBjcUnitid(Integer.valueOf(id));                        //被检查单位ID
                    e.setContent(mObjectMapData.get("mCardView_9"));            //存在的违法行为
                    e.setBianhao1(mObjectMapData.get("xq_wg"));                 //旺公（）
                    e.setBianhao2(Integer.valueOf(mObjectMapData.get("xq_ztz"))); //责通字
                    e.setBianhao3(Integer.valueOf(0));                          //第()号
                    e.setId(Integer.valueOf(0));                                //主键ID
                    //e.setJcName(mObjectMapData.get("mCardView_22"));          //被检查单位签名
                    e.setJcName(mObjectMapData.get("mCardView_23"));            //被检查单位签名
                    e.setTzsTime(new Date());                                   //检查提交的时间
                    // TODO: 2018/5/7  整改完毕的时间
                    if(mObjectMapData.get("mCardView_17") != null){
                        String view17 = mObjectMapData.get("mCardView_17");
                        Log.e("TAG","===="+view17);
                        String replace = view17.replace("年", "-");
                        String replace1 = replace.replace("月", "-");
                        String replace2 = replace1.replace("日", "");
                        String strData = replace2+" "+"12:12:12";
                        Log.e("TAG","===="+strData);
                        e.setZgTime(strData);                                   //整改完毕的时间
                    }

                    e.setClauseId(mObjectMapData.get("xq_tiaoli"));             //治安条例条例(数字)
                    /**
                     * mObjectMapData.put("xq_tiaoli",xq_tiaoli);
                     * mObjectMapData.put("xq_cuoshi",xq_cuoshi);
                     *
                     */
                    e.setZgWay(mObjectMapData.get("xq_cuoshi"));                //改正措施
                    /* 转换JSON */
                    String mResult=com.alibaba.fastjson.JSON.toJSONString(e);
                    Log.e("TAG"," 旺苍县公安局责令限期整改治安隐患通知书ID >>>> "+id);
                    Log.e("TAG"," 预备提交的地址 >>>> "+mPath_post);
                    Log.e("TAG"," 预备提交的实体JSON >>>> "+mResult);
                    //提交
                    NetworkTools.postHttpToolsUaerRegistite(mPath_post,mHandler,mResult);
                }else {
                    Configfile.Log(CorrectCommandActivity.this,"参数错误！");
                }
            }
        });


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
                CacheUtils.setWLocalCache(bitmap,fileName,CorrectCommandActivity.this);
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
                        Bitmap localCache = CacheUtils.getLocalCache(fileName,CorrectCommandActivity.this);
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


    /* 触发下拉选择器 */
    public void getSelect(Spinner mSpinner, int Resourcesid){

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /* 获取arrays 自定义的item */
                String[] languages = getResources().getStringArray(R.array.langItem);
                //Toast.makeText(getContext(), "你点击的是:"+languages[position],Toast.LENGTH_SHORT).show();
                mObjectData.add(languages[position]);
                mObjectMapData.put("mCardView_5",languages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /* 触发时间事件选择器 */
    public void getTimer(final Context context,final TextView text){

        final List<String> mTime=new ArrayList<>();
        Calendar c = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(context,
                // 绑定监听器
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        text.setText(year + "年" + (monthOfYear+1)+ "月" + dayOfMonth + "日");
                        Log.e("TAG","获取事件"+year + "年" + monthOfYear+ "月" + dayOfMonth + "日");
                    }
                }
                // 设置初始日期
                , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH)).show();

    }

    /* 继承父类的获取本地地址方法 */
    @Override
    public void getLocalPath(String str) {
        super.getLocalPath(str);
        mPath=str;
        Log.e("TAG","CorrectCommandActivity >>> 中的地址"+str);
    }


    public void getParentViewGone(ArrayList<String> mGoneData) {
        Log.e("TAG", "当前允许开启视图大小 >>> " + mGoneData.size());
        for (int i = 0; i < mGoneData.size(); i++) {
            String defaultGone = mGoneData.get(i);
            Log.e("TAG", "当前允许开启视图列表 >>> " + defaultGone);
            if (defaultGone.equals("mCardView_1")) {
                mCardView_1.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_2")) {
                mCardView_2.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_3")) {
                mCardView_3.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_4")) {
                mCardView_4.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_5")) {
                mCardView_5.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_6")) {
                mCardView_6.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_7")) {
                mCardView_7.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_8")) {
                mCardView_8.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_9")) {
                mCardView_9.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_10")) {
                mCardView_10.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_11")) {
                mCardView_11.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_12")) {
                mCardView_12.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_13")) {
                mCardView_13.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_14")) {
                mCardView_14.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_15")) {
                mCardView_15.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_16")) {
                mCardView_16.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_17")) {
                mCardView_17.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_18")) {
                mCardView_18.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_19")) {
                mCardView_19.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_20")) {
                mCardView_20.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_21")) {
                mCardView_21.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_22")) {
                mCardView_22.setVisibility(View.VISIBLE);
            } else if (defaultGone.equals("mCardView_23")) {
                mCardView_23.setVisibility(View.VISIBLE);
            }
        }
    }
}
