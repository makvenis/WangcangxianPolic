package com.makvenis.dell.wangcangxianpolic.newCompanyPost;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.cat.CatLoadingView;
import com.makvenis.dell.wangcangxianpolic.otherActivity.HistoryUtils;
import com.makvenis.dell.wangcangxianpolic.sanEntery.JwYesyqYinhuanMsg;
import com.makvenis.dell.wangcangxianpolic.startActivity.HomeActivity;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.NetworkTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

@ContentView(R.layout.activity_to_yes)
public class ToYesActivity extends AppCompatActivity {

    public static final String TAG = "ToYesActivity";

    /* 获取父类的参数信息 */
    private String mBianhao;
    private String mUrl;
    private String mtitle;
    private String id;

    /* 布局Recycle文件查找 */
    @ViewInject(R.id.mCompany_Recycle)
    RecyclerView mRecycle;

    /* toolbar控件查找 */
    /* include 里面的点击事件 */
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.toolbar_callbank_text)
    TextView mBankTextView;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;
    /* 处理toolbar 结束 */

    /* 提交按钮 */
    @ViewInject(R.id.mCompany_Submit)
    Button mCompany_Submit;
    private CatLoadingView mCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        /* 获取父类参数信息 */
        getParemnt();

        /* 设置标题 */
        mTextView.setText(mtitle);

        /* 设置适配器 */
        setRecycleAdapter();
    }

    private void setRecycleAdapter() {
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecycle.setLayoutManager(manager);
        mRecycle.setAdapter(new TestAdapter(this));
    }

    /* 获取参数（上一级页面传递过来的必要参数） */
    public void getParemnt() {
        //页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        mBianhao = bundle.getString("mLocal_bianhao");
        mUrl = bundle.getString("mUrl");
        mtitle = bundle.getString("l_title");
        id = bundle.getString("id");
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkViewImage(View v){
        startActivity(new Intent(this, HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank_text})
    public void oncklinkViewTextView(View v){
        startActivity(new Intent(this, HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 提交 */
    @OnClick({R.id.mCompany_Submit})
    public void submit(View v){

        mCat = new CatLoadingView();
        mCat.show(getSupportFragmentManager(),"");
        Log.e(TAG,"适配器返回的值 >>> \n >>> map1:"+map1.toString() +"\n >>> map2:"+map2.toString()+"\n >>> map3:"+map3.toString());

        if(map1 != null && map3 != null ){
            JwYesyqYinhuanMsg e=new JwYesyqYinhuanMsg();
            /* "readHead","number","document" */
            e.setBjcUnitid(Integer.valueOf(id));                 //单位ID
            e.setBianhao1(map1.get("readHead"));                 //旺公
            e.setBianhao2(Integer.valueOf(map1.get("number")));  //编号3
            e.setContent(map1.get("document"));                  //整改的内容
            e.setYqStarttime(map3.get("number"));                //开始时间
            e.setYqEndtime(map3.get("readHead"));                //结束时间
            e.setTzsTime(map3.get("document"));                  //填表时间
            /* "number","readHead","document" */

            /* 转换JSON */
            String mResult=com.alibaba.fastjson.JSON.toJSONString(e);
            Log.e("TAG"," 预备提交的地址 >>>> "+ Configfile.OVER_POST_CORECT_TONGYI);
            Log.e("TAG"," 预备提交的实体JSON >>>> "+mResult);
            //提交
            NetworkTools.postHttpToolsUaerRegistite(Configfile.OVER_POST_CORECT_TONGYI,mHandler,mResult);
        }

    }

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int what = msg.what;

            switch (what){
                case Configfile.CALLBANK_POST_MSG:
                    String json = (String) msg.obj;
                    Log.e(TAG,json);
                    if(json != null){
                        try {
                            JSONObject opt=new JSONObject(json);
                            String state = opt.optString("state");
                            if (state.equals("OK")){
                                Configfile.Log(ToYesActivity.this,"添加成功");

                                 /* 调用日志文件 */
                                HistoryUtils.executeAddrs(ToYesActivity.this,
                                        id,mtitle,"完成");

                                mCat.dismiss();
                                // TODO: 2018/6/4 跳转
                                String page = opt.optString("bianhao");
                                Intent intent=new Intent(ToYesActivity.this, ToBiLuActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("mLocal_bianhao",page);
                                bundle.putString("id",id);
                                if(mtitle.equals("不同意延期整改治安隐患")){
                                    bundle.putString("mUrl",Configfile.SERVICE_WEB+"toshowNotAgreexqzg");
                                }else if(mtitle.equals("同意延期整改治安隐患")){
                                    bundle.putString("mUrl",Configfile.SERVICE_WEB+"toshowAgreexqzg");
                                }
                                bundle.putString("l_title","检查笔录");
                                intent.putExtras(bundle);
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ToYesActivity.this).toBundle());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else {
                        mCat.dismiss();
                    }

                    break;


            }
        }
    };


    /* 返回的集合查验 */
    Map<String, String> map1;
    Map<String, String> map2;
    Map<String, Date> map3;
    /* 同意整改适配器 */
    public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        Context mContext;

        public TestAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == 0){
                View view = LayoutInflater.from(mContext).inflate(R.layout.test_item, parent, false);
                return new MyFirstViewHolder(view) ;
            }else if(viewType == 1) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.test_item1, parent, false);
                return new MyTwoViewHolder(view) ;
            }else if(viewType == 2){
                View view = LayoutInflater.from(mContext).inflate(R.layout.test_item2, parent, false);
                return new MyThreeViewHolder(view) ;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof MyFirstViewHolder){
                RecyclerView view = ((MyFirstViewHolder) holder).view;
                String[] mTitleName=new String[]{"旺公","编号","违法行为检查结果"};
                String[] key=new String[]{"readHead","number","document"};
                CompanyUtils utils=new CompanyUtils(mContext,view,mTitleName,key);
                map1 =utils.creatEditData();

            }else if(holder instanceof MyTwoViewHolder){
                RecyclerView view = ((MyTwoViewHolder) holder).view;
                String[] mName=new String[]{"检查人签名"};
                String[] mKey=new String[]{"number"};
                CompanyUtils utils=new CompanyUtils(mContext,view,mName,mKey);
                map2 = utils.creatDataGouse();
            }else if(holder instanceof MyThreeViewHolder){
                RecyclerView view = ((MyThreeViewHolder) holder).view;
                String[] mName=new String[]{"同意整改开始时间","同意整改结束时间","填写时间"};
                String[] mKey=new String[]{"number","readHead","document"};
                CompanyUtils utils=new CompanyUtils(mContext,view,mName,mKey);
                map3 = utils.creatTimeData();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0){

                return 0;
            }else if(position == 1){
                return 1;
            }else if(position == 2){
                return 2;
            }else {
                return 3;
            }

        }

        /* 第一组内部类 */
        class MyFirstViewHolder extends RecyclerView.ViewHolder{

            @ViewInject(R.id.mTestRecycle)
            RecyclerView view;
            public MyFirstViewHolder(View itemView) {
                super(itemView);
                ViewUtils.inject(this,itemView);
            }
        }

        /* 第二组内部类 */
        class MyTwoViewHolder extends RecyclerView.ViewHolder{

            @ViewInject(R.id.mTestRecycle)
            RecyclerView view;
            public MyTwoViewHolder(View itemView) {
                super(itemView);
                ViewUtils.inject(this,itemView);
            }
        }

        /* 第三组内部类 */
        class MyThreeViewHolder extends RecyclerView.ViewHolder{

            @ViewInject(R.id.mTestRecycle)
            RecyclerView view;
            public MyThreeViewHolder(View itemView) {
                super(itemView);
                ViewUtils.inject(this,itemView);
            }
        }
    }
}
