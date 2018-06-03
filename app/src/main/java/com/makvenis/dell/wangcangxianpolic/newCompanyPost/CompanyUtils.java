package com.makvenis.dell.wangcangxianpolic.newCompanyPost;

/* 适配器的传值过程 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyUtils {

    public Context mContext; //赋值必须的上下文
    public RecyclerView mRecycle; //控件
    public String[] mName; //必须的控件名称
    public String[] mKey;  //必须的控件参数值


    public CompanyUtils(Context mContext, RecyclerView mRecycle, String[] mName, String[] mKey) {
        this.mContext = mContext;
        this.mRecycle = mRecycle;
        this.mName = mName;
        this.mKey = mKey;
    }

    /* 构建类型 */
    public String TYPE_EDIT = "TYPE_EDIT";
    public String TYPE_TIME = "TYPE_TIME";
    public String TYPE_GOUES = "TYPE_GOUES";

    /* Map 键值对 */
    public String KEY = "key";
    public String VALUE = "value";

    public Map<String,String> creatDataGouse(){
        /*String[] mName=new String[]{"检查人签名","被检查人签名","所长签名"};
        String[] mKey=new String[]{"number","readHead","document"};*/

        //返回的数据集合
        final Map<String,String> mGouse=new HashMap<>();

        List<CompanyGouseEntry> mData=new ArrayList<>();

        /**
         * {@emue 列出枚举类型}
         * 那么对应的也是三种布局 分别是输入框 时间选择框 签名框
         * 准备三个集合 分别装载
         */
        for (int i = 0; i < mName.length; i++) {
            CompanyGouseEntry e = new CompanyGouseEntry();
            e.setType(TYPE_EDIT);
            e.setName(mName[i]);
            e.setKey(mKey[i]);
            e.setInteger(null);
            mData.add(e);
        }

        // TODO: 2018/6/3 测试
        RecyclerView.LayoutManager manager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        mRecycle.setLayoutManager(manager);
        CompanyGouseAdapter adapter=new CompanyGouseAdapter(mContext,mData);
        mRecycle.setAdapter(adapter);

        adapter.setOnItemClick(new CompanyGouseAdapter.OnClinkItem() {
            @Override
            public void saveImage(Bitmap bitmap, String key ,String path) {
                mGouse.put(key,path);
            }
        });


        return mGouse;

    }


    public Map<String,String> creatEditData() {
        /* 创建标题 */
        String[] mName=new String[]{"编号","旺公","违法行为"};
        String[] mKey=new String[]{"number","readHead","document"};
        List<CompanyItemEntry> mData=new ArrayList<>();

        //返回的数据集合
        final Map<String,String> mEdit=new HashMap<>();

        /**
         * {@emue 列出枚举类型}
         * 那么对应的也是三种布局 分别是输入框 时间选择框 签名框
         * 准备三个集合 分别装载
         */
        for (int i = 0; i < mName.length; i++) {
            CompanyItemEntry e = new CompanyItemEntry();
            e.setType(TYPE_EDIT);
            e.setName(mName[i]);
            e.setKey(mKey[i]);
            e.setValue(null);
            mData.add(e);
        }

        // TODO: 2018/6/3 测试
        RecyclerView.LayoutManager manager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        mRecycle.setLayoutManager(manager);
        CompanyEditAdapter adapter=new CompanyEditAdapter(mContext,mData);
        mRecycle.setAdapter(adapter);

        final List<String> save = new ArrayList<>();
        final Map<String,String> saveMap = new HashMap<>();
        adapter.setOnItemClick(new CompanyEditAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void loadingEdit(int position,String key, String string) {
                mEdit.put(key,string);
            }

            @Override
            public void overFoucesEdit(int postion, String type, String edit) {
                saveMap.put(type,edit);
                //save.add(edit);
                //Configfile.Log(CompanyEditActivity.this,"已存入集合 "+edit);

                //mEdit.put(type,edit);
            }

        });

        return mEdit;

    }





}
