package com.makvenis.dell.wangcangxianpolic.minFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.view.SimpleImageViewCircleBitmap;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * 个人中心 ---- 个人信息
 */

public class PersonalCenterFragemnt extends Fragment {

    @ViewInject(R.id.min_personal_poto)
    private SimpleImageViewCircleBitmap mPersonal_poto; //头像
    @ViewInject(R.id.min_personal_name)
    private TextView min_personal_name; // 姓名
    @ViewInject(R.id.min_personal_type)
    private TextView min_personal_type; // 职位
    @ViewInject(R.id.min_personal_remark)
    private TextView min_personal_remark; // 个人简介

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.min_fragment_personal_center, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String path="http://p3.so.qhimgs1.com/bdr/_240_/t01ef33386ec2837673.jpg";
        Picasso.with(getContext()).load(path).into(mPersonal_poto);

        /* 用户数据读取 */
        AppMothedHelper helper=new AppMothedHelper(getContext());
        Map<Object, Object> map = helper.queryByKey(Configfile.USER_DATA_KEY);
        String data = (String) map.get("data");

        Log.e("TAG","===== >>>>> 为解析前 "+data); // []

        if(data != null){
            Log.e("TAG","PersonalCenterFragemnt 在使用用户的数据 >>>> map集合大小"+map.size());
            //调用json 解析相关数据
            Map<String, String> registe = JSON.GetJsonRegiste(data);
            Log.e("TAG","===== >>>>> "+registe.toString()); // []

            if(registe != null){
                //赋值数据
                Picasso.with(getContext()).load("https://ps.ssl.qhimg.com/t013658e41e8c191970.jpg").into(mPersonal_poto);
                //Picasso.with(getContext()).load(stringMap.get("headPortrait").replace("../../","")).into(mPersonal_poto);
                min_personal_name.setText(registe.get("truename")); //姓名
                min_personal_type.setText(registe.get("NULL")); //职位
                min_personal_remark.setText(registe.get("email")); //简介
            }else {
                Log.e("TAG","PersonalCenterFragemnt 调用json 解析相关数据 >>>> 暂未分析到数据 ");
            }
        }else {
            Log.e("TAG","PersonalCenterFragemnt 在使用用户的数据 >>>> 暂未查询到数据 ");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this,view);
        Log.e("TAG","Fragment >>>  注解绑定成功！");
    }
}
