package com.makvenis.dell.wangcangxianpolic.company;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.addCompany.AddCompanyActivity;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.newsnotescheck.NotesNewActivity;
import com.makvenis.dell.wangcangxianpolic.startActivity.BaseActivity;
import com.makvenis.dell.wangcangxianpolic.startActivity.HomeActivity;
import com.makvenis.dell.wangcangxianpolic.startActivity.WebHtmlActivity;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.view.SimpleLoadingDialog;
import com.makvenis.dell.wangcangxianpolic.view.SimpleRecycleScollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 被检查单位信息列表页面 */

@ContentView(R.layout.activity_company)
public class CompanyActivity extends BaseActivity{

    public Context mContext=CompanyActivity.this;

    /* 全局数据 */
    private String obj;

    /* 全局Dialog */
    private SimpleLoadingDialog mDialogLoding4;

    /* 全局Handler */
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what){
                case 0X000004:
                    obj = ((String) msg.obj);
                    Bundle data = msg.getData();
                    Intent intentType1 = new Intent(CompanyActivity.this,
                            NotesNewActivity.class);
                    intentType1.putExtras(data);

                    /* 跳转之前关闭Dialog */
                    mDialogLoding4.dismiss();

                    CompanyActivity.this.startActivity(intentType1);
                    break;
                case 0X000005:
                    String result = ((String) msg.obj);
                    if(result != null){
                        List<Map<String, String>> list = JSON.GetJson(result, new String[]{"address", "attr", "name", "photoUrl","id"});
                        if(maps != null){
                            maps.removeAll(maps);
                            for (int i = 0; i < list.size(); i++) {
                                maps.add(list.get(i));
                            }
                        }

                        for (int i = 0; i < list.size(); i++) {
                            maps.add(list.get(i));
                        }

                        mSwipeRefreshLayout.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    /* 处理toolbar 开始 version=2  */
    /* include 里面的点击事件 */
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.toolbar_callbank_text)
    TextView mBankTextView;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;
    /* 处理toolbar 结束 */

    /* 添加单位 */
    @ViewInject(R.id.mToolbar_add)
    TextView mAdd;
    // TODO: 2018/6/2  添加单位

    CompanyAdapter mAdapter;

    @ViewInject(R.id.company_recycle)
    private SimpleRecycleScollView mRecycleView;

    @ViewInject(R.id.company_swipe)
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /* 适配集合 */
    List<Map<String, String>> maps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        swipeData();

        SetAdapters();

        //刷新组件
        mSwipeRefreshLayout.setColorSchemeColors(new int[]{Color.RED,Color.GREEN,Color.BLUE});
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeData();
                Configfile.Log(CompanyActivity.this,"刷新成功！");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mTextView.setText("单位列表信息");
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkView(View v){
        startActivity(new Intent(mContext, HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }


    /* 获取单位信息 */
    // TODO: 2018/4/13 后期只修改此处的数据来源即可
    public List<Map<String, String>> GetCompanyDatabase(){
        // TODO: 2018/4/6 获取单位信息
        Intent intent = getIntent();
        String extra = intent.getStringExtra("mData");
        Log.e("TAG",new Date()+" >>> 第一个Fragment 检查传递过来的初始数据 "+extra);
        List<Map<String, String>> data = JSON.GetJson(extra, new String[]{"address", "attr", "name", "photoUrl","id"});
        return data;
    }

    /* 刷新使用 下载使用 */
    public void swipeData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtils(5000).send(HttpRequest.HttpMethod.GET,
                        Configfile.COMPANY_URL+getSqliteName(),
                        new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                String result = responseInfo.result;
                                if(result != null){
                                    Message msg=new Message();
                                    msg.what=0X000005;
                                    msg.obj=result;
                                    mHandler.sendMessage(msg);
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                Configfile.Log(CompanyActivity.this,"网络链接失败！");
                            }
                        });
            }
        }).start();
    }


    /* 适配 */
    public void SetAdapters(){

        mAdapter = new CompanyAdapter(maps,this);

        RecyclerView.LayoutManager manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,
                false);

        mRecycleView.setLayoutManager(manager);

        mRecycleView.setAdapter(mAdapter);

        mAdapter.SetCallBankItemCheck(new CompanyAdapter.CallBankItemCheck() {
            @Override
            public void show(String id,final int postion) {

                Configfile.Log(CompanyActivity.this,"回调被删除的单位ID"+id);

                final String path = Configfile.DELETE_COMPANY_DATA+id;
                new HttpUtils(5000).send(HttpRequest.HttpMethod.GET,
                                path,
                                new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        String result = responseInfo.result;
                                        if(result != null){
                                            try {
                                                JSONObject mJson=new JSONObject(result);
                                                if(mJson.get("state").equals("OK")){
                                                    Configfile.Log(mContext,"删除成功");
                                                    //mSwipeRefreshLayout.setRefreshing(true);
                                                    maps.remove(postion);
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }else {
                                            Configfile.Log(mContext,"删除失败！");
                                        }
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {
                                        Configfile.Log(mContext,"网络连接失败！");
                                    }
                                });

            }
        });

        /* 适配器的回调事件 */
        mAdapter.SetOnclinkRecycleItem(new CompanyAdapter.OnclinkRecycleItem() {
            @Override
            public void OnclinkRecycleItemListent(RecyclerView recyclerView, View view, int postion, Map<String, String> map,String id) {
               Log.e("TAG",new Date()+" >>> 单位ID(CompanyActivity) " + id);
                //实例化布局
                View viewItem = LayoutInflater.from(CompanyActivity.this).inflate(R.layout.item_dialog_type,null);
                //找到并对自定义布局中的控件进行操作的示例
                final RadioGroup mGroup = (RadioGroup) viewItem.findViewById(R.id.select);
                //启动AlertDialog
                //创建对话框
                AlertDialog.Builder dialog = new AlertDialog.Builder(CompanyActivity.this);
                dialog.setIcon(R.mipmap.icon_app);//设置对话框icon
                dialog.setView(viewItem);
                dialog.setTitle("请选择检查的项目");//设置对话框标题
                //显示对话框
                dialog.create().show();

                //RadioGroup的点击事件
                getRadioGroupCheck(mGroup,id);
            }
        });
    }


    @OnClick({R.id.mToolbar_add})
    public void addCompany(View v){
        Intent intent=new Intent(CompanyActivity.this, AddCompanyActivity.class);
        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }















    /* ------------------------------Dialog的处理事件--------------------------------- */
    public void getRadioGroupCheck(RadioGroup mGroup, final String id){

        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.select_1:
                        //参数（单位id,是否具有头部文件，头部文件id，是否具有脚部文件，脚部文件id）
                        Bundle bundle1 = creadCondationDefault(id,false, 0, true, R.layout.public_item_notes_foot,Configfile.FORM_SQL_DATABASE_1,"旅店安全检查表",Configfile.RESULT_HTML_TYPE_1);
                        getNetJsonString(CompanyActivity.this,Configfile.FORM_GET_TABLE_1,mHandler,Configfile.FORM_SQL_DATABASE_1,bundle1);
                        loadingDialog();
                        break;
                    case R.id.select_2:
                        //参数（是否具有头部文件，头部文件id，是否具有脚部文件，脚部文件id）
                        Bundle bundle2 = creadCondationDefault(id,false, 0, true, R.layout.public_item_notes_foot,Configfile.FORM_SQL_DATABASE_2,"金融机构检查登记表",Configfile.RESULT_HTML_TYPE_2);
                        getNetJsonString(CompanyActivity.this,Configfile.FORM_GET_TABLE_2,mHandler,Configfile.FORM_SQL_DATABASE_2,bundle2);
                        loadingDialog();
                        break;
                    case R.id.select_3:
                        //参数（是否具有头部文件，头部文件id，是否具有脚部文件，脚部文件id）
                        Bundle bundle3 = creadCondationDefault(id,false, 0, true, R.layout.public_item_notes_foot,Configfile.FORM_SQL_DATABASE_3,"寄递物流业安全检查登记表",Configfile.RESULT_HTML_TYPE_3);
                        getNetJsonString(CompanyActivity.this,Configfile.FORM_GET_TABLE_3,mHandler,Configfile.FORM_SQL_DATABASE_3,bundle3);
                        loadingDialog();
                        break;
                    case R.id.select_4:
                        //参数（是否具有头部文件，头部文件id，是否具有脚部文件，脚部文件id）
                        Bundle bundle4 = creadCondationDefault(id,false, 0, true, R.layout.public_item_notes_foot,Configfile.FORM_SQL_DATABASE_4,"校园治安保卫工作检查登记表",Configfile.RESULT_HTML_TYPE_4);
                        getNetJsonString(CompanyActivity.this,Configfile.FORM_GET_TABLE_4,mHandler,Configfile.FORM_SQL_DATABASE_4,bundle4);
                        loadingDialog();
                        break;
                    case R.id.select_5:
                        Intent intent = new Intent(CompanyActivity.this, WebHtmlActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("l_url","fuchaServiceUp.html");
                        bundle.putString("l_title","复查意见书");
                        bundle.putString("id",id);
                        bundle.putString("mLocal_bianhao","100");
                        intent.putExtras(bundle);
                        startActivity(intent,
                                ActivityOptions.makeSceneTransitionAnimation(CompanyActivity.this).toBundle());
                        break;
                }
            }
        });
    }

    /**
     * @param id 单位ID
     * @param boolhead 是否具有头部文件
     * @param headid   头部文件id
     * @param boolfoot 是否具有脚部文件
     * @param footid   脚部文件id
     * @param Title    头部标题
     * @return Bundle  Bundle对象添加到Intent中
     */
    /* 数据构建方法 */
    public Bundle creadCondationDefault(String id,
                                        boolean boolhead,
                                        int headid,
                                        boolean boolfoot,
                                        int footid,
                                        String TagDb,
                                        String Title,
                                        String TagUrl){
        /* 创建数据 */
        Bundle bundle=new Bundle();

        if(boolhead == false){
            /* 是否具有头部文件 */
            bundle.putBoolean("boolhead",false);
            /* 头部文件id */
            bundle.putInt("headid",0);
        }else {
            /* 是否具有头部文件 */
            bundle.putBoolean("boolhead",boolhead);
            /* 头部文件id */
            bundle.putInt("headid",headid);
        }

        if(boolfoot == false){
            /* 是否具有脚部文件 */
            bundle.putBoolean("boolfoot",false);
            /* 脚部文件id */
            bundle.putInt("footid",0);
        }else {
            /* 是否具有脚部文件 */
            bundle.putBoolean("boolfoot",boolfoot);
            /* 脚部文件id */
            bundle.putInt("footid",footid);
        }
        /* 传递数据库的Key值 是为了在下一个页面便于查询 */
        bundle.putString("TagDb",TagDb);
        /* 传递标题 是为了在下一个页面便于赋值Toolbar的Title值 */
        bundle.putString("Title",Title);
        /* 传递当前表格Html地址 是为了在下一个页面便于再提交成功之后跳转WebViewActivity去赋值WebView的load地址 */
        bundle.putString("TagUrl",TagUrl);
        /* 单位ID */
        bundle.putString("id",id);
        return bundle;
    }

    /* 数据下载 */
    public void getNetJsonString(final Context context, //上下文
                                 final String path, //下载的地址
                                 final Handler mHandler, //线程通讯
                                 final String mKey, // 存储或者查询的key值
                                 final Bundle mBundle //Handler 中Activity所需要传递的bundle
    ) {
        new HttpUtils(10000).send(HttpRequest.HttpMethod.GET,
                path,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        if(result != ""){
                            Message msg=new Message();
                            msg.what=0X000004;
                            msg.obj=result;
                            msg.setData(mBundle);
                            mHandler.sendMessage(msg);
                        }
                        Log.e("TAG","请求的数据 >>> "+ result);
                        setLocahostSqliteDatabase(context,mKey,result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                        Configfile.Log(CompanyActivity.this,"网络请求失败！请稍后重试");
                        mDialogLoding4.dismiss();
                    }
                });
    }

    /* 调用数据库存储 */
    public void setLocahostSqliteDatabase(Context context,String mKey, String mData){
        /* 判断当表格的数据 */
        AppMothedHelper helper=new AppMothedHelper(context);
        Map<Object, Object> map = helper.queryByKey(mKey);
        boolean isData = helper.isDismisData(context, mKey);
        Log.e("TAG","即将跳转之前查询的数据 >>>> "+ map.get("data")+" >>>> "+ mKey + " >>>> " + isData +" >>>" + mData);
        if(isData == true){
            //存在 执行更新
            helper.update(mKey,mData);
        }else {
            helper.dbInsert(new String[]{mKey,mData});
        }
    }

    /* 调用Dialog模拟等待 */
    public void loadingDialog(){
        mDialogLoding4 = new SimpleLoadingDialog(CompanyActivity.this);
        mDialogLoding4.setMessage("正在请求数据中").show();
    }


}
