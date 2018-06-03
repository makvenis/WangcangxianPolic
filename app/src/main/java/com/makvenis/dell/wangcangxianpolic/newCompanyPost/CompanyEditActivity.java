package com.makvenis.dell.wangcangxianpolic.newCompanyPost;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;

import java.util.Map;

/**
 * {@link CompanyEditActivity} 需要使用
 *
 *
 */

@ContentView(R.layout.activity_company_edit)
public class CompanyEditActivity extends AppCompatActivity {


    private String mBianhao;
    private String mUrl;
    private String mtitle;
    private String id;

    /* 构建类型 */
    public String TYPE_EDIT = "TYPE_EDIT";
    public String TYPE_TIME = "TYPE_TIME";
    public String TYPE_GOUES = "TYPE_GOUES";

    /* Map 键值对 */
    public String KEY = "key";
    public String VALUE = "value";

    /* 布局文件查找 */
    @ViewInject(R.id.mCompany_Recycle)
    RecyclerView mRecycle;

    /* 测试数据 */
    @ViewInject(R.id.mCompany_Test)
    TextView mTextView;
    @ViewInject(R.id.mCompanySubmit)
    Button mCompanySubmit;


    Map<String, String> map1;
    Map<String, String> map2;


    /* 点击事件 */
    @OnClick({R.id.mCompanySubmit})
    public void show(View v){
        if(map1 != null || map2 != null){
            mTextView.setText(map1.toString()+" >>> " + map2.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        /* 获取父类的参数 */
        //getParemnt();

        RecyclerView.LayoutManager manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecycle.setLayoutManager(manager);
        mRecycle.setAdapter(new TestAdapter(this));

    }


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
            }

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof MyFirstViewHolder){
                RecyclerView view = ((MyFirstViewHolder) holder).view;
                String[] mTitleName=new String[]{"编号","旺公","违法行为"};
                String[] key=new String[]{"number","readHead","document"};
                CompanyUtils utils=new CompanyUtils(mContext,view,mTitleName,key);
                map1 =utils.creatEditData();

            }else if(holder instanceof MyTwoViewHolder){
                RecyclerView view = ((MyTwoViewHolder) holder).view;
                String[] mName=new String[]{"检查人签名","被检查人签名","所长签名"};
                String[] mKey=new String[]{"number","readHead","document"};
                CompanyUtils utils=new CompanyUtils(mContext,view,mName,mKey);
                map2 = utils.creatDataGouse();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0){

                return 0;
            }else if(position == 1){
                return 1;
            }else {
                return 2;
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
    }
}
