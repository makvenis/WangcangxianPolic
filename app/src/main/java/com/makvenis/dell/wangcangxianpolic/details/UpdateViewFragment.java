package com.makvenis.dell.wangcangxianpolic.details;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;

import java.util.Map;

/* 单位详情（更新） */

public class UpdateViewFragment extends Fragment {

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
    EditText mMore_GovName;

    /* 单位类型 */
    @ViewInject(R.id.mMore_AddrsType)
    EditText mMore_AddrsType;

    /* 单位电话 */
    @ViewInject(R.id.mMore_Phone)
    EditText mMore_Phone;

    /* 法人性别 */
    @ViewInject(R.id.mMore_Sex)
    EditText mMore_Sex;

    /* 法人性别 */
    @ViewInject(R.id.mMore_Lev)
    EditText mMore_Lev;

    /* 添加单位照片 */
    @ViewInject(R.id.mMore_Addrs_photo)
    ImageView mMore_Addrs_photo;

    /* 单位ID */
    public String xmlIdString;

    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0X000001){
                String obj = (String) msg.obj;
                if(!obj.equals("")){
                    Map<String, Object> json = JSON.getObjectJson(obj, new String[]{"address", "attr", "legalaName", "level", "name", "pcs", "phone", "type", "zjnum", "sex", "tradeId", "zjtype"});
                    setEditViewData(json);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_more_update, null);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* 获取固定xmlId 存储的单位id值 */
        SharedPreferences xmlId = getActivity().getSharedPreferences("xmlId", Context.MODE_PRIVATE);
        xmlIdString = xmlId.getString("id", "0");
        Log.e(TAG,"haredPreferences()" + xmlIdString);

        /* 服务地址 */
        String path = Configfile.COMPANY_URL_SEARCH_ID + xmlIdString;
        Log.e("TAG",path);

        /* 查询 */
        new HttpUtils(5000).send(HttpRequest.HttpMethod.GET,
                path,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if(responseInfo.result != null){
                            Message msg=new Message();
                            msg.what = 0X000001;
                            msg.obj = responseInfo.result;
                            mHandler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });

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


        if(!((String) map.get("type")).equals("")){
            mMore_AddrsType.setText(((String) map.get("type")));
        }else mMore_AddrsType.setText("暂无数据");


        if(!((String) map.get("phone")).equals("")){
            mMore_Phone.setText(((String) map.get("phone")));
        }else mMore_Phone.setText("暂无数据");

        if((int) map.get("sex") == 1 ){
            mMore_Sex.setText("男");
        }else mMore_Sex.setText("女");

        if(!((String) map.get("level")).equals("")){
            mMore_Lev.setText(((String) map.get("level")));
        }else mMore_Lev.setText("暂无数据");

    }
}
