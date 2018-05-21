package com.makvenis.dell.wangcangxianpolic.minFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.view.SimpleImageViewCircleBitmap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心 ---- 个人信息
 */

public class PersonalCenterFragemnt extends Fragment {

    @ViewInject(R.id.mPersonal_listView)
    ListView listView;

    SimpleImageViewCircleBitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.min_fragment_personal_center, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //获取用户信息
        Map<String, String> maps = getPersonalData();
        final List<Map<String, String>> data=new ArrayList<>();
        data.add(maps);

        SimpleAdapter adapter=new SimpleAdapter(getActivity(),
                data,
                R.layout.layout_personal_item,
                        new String[]{"username","truename","phone","headPortrait"},
                        new int[]{R.id.min_personal_name, //用户名
                                  R.id.min_personal_type, //职务
                                  R.id.min_personal_remark,//个人简介
                                  R.id.min_personal_poto  //头像
                        }){
            @Override
            public void setViewImage(ImageView v, String value) {
                super.setViewImage(v, value);
                if(v.getId() == R.id.min_personal_poto){
                    // TODO: 2018/5/21 头像地址
                    String path="http://b1.hucdn.com/upload/item/1804/16/59603443964511_800x800.jpg";
                    Picasso.with(getActivity()).load(path).into(v);
                }
            }

            /* Item里面的内一个子空间的点击事件 */
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                bitmap = (SimpleImageViewCircleBitmap) view.findViewById(R.id.min_personal_poto);
                bitmap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Configfile.Log(getActivity(),"SimpleImageViewCircleBitmap");
                        Intent intent=new Intent(getActivity(),PersonalCenterActivity.class);
                        // TODO: 2018/5/21 模拟原始图片地址
                        String path="http://b1.hucdn.com/upload/item/1804/16/59603443964511_800x800.jpg";
                        intent.putExtra("old_img_url",path);
                        startActivity(intent);
                    }
                });


                return view;

            }
        };

        listView.setAdapter(adapter);



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this,view);
        Log.e("TAG","Fragment >>>  注解绑定成功！");
    }

    /* 数据库查找用户信息 */
    public Map<String, String> getPersonalData() {
        /* 用户数据读取 */
        AppMothedHelper helper=new AppMothedHelper(getContext());
        Map<Object, Object> map = helper.queryByKey(Configfile.USER_DATA_KEY);
        String data = (String) map.get("data");

        Log.e("TAG","===== >>>>> 为解析前 "+data); // []

        if(data != null){
            Log.e("TAG","PersonalCenterFragemnt 在使用用户的数据 >>>> map集合大小"+map.size());
            //调用json 解析相关数据
            Map<String, String> registe = JSON.GetJsonRegiste(data);
            if(registe.size() != 0)
                return registe;
        }else {
            Log.e("TAG","PersonalCenterFragemnt 在使用用户的数据 >>>> 暂未查询到数据 ");
        }
        return new HashMap<>();
    }


}
