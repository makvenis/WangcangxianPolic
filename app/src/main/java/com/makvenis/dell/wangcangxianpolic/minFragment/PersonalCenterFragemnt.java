package com.makvenis.dell.wangcangxianpolic.minFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.help.MessageEvent;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.view.SimpleImageViewCircleBitmap;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心 ---- 个人信息
 */

public class PersonalCenterFragemnt extends Fragment {

    public final String TAG="PersonalCenterFragemnt";

    @ViewInject(R.id.mPersonal_RecyclerView)
    RecyclerView mRecycle;

    /* 用户头像 */
    SimpleImageViewCircleBitmap bitmap;

    /**
     * @ 全局图片地址
     * @ 解释 当 {@link PersonalCenterFragemnt} 此页面的用户头像地址从数据库中取出来的并且做完全路径拼接之后的地址
     * @ 用途 加载本页面用户的基本信息 里面的用户大图片 在Picasso里面使用到本地址
     *        在{@link PersonalCenterActivity} 在此页面当用户选择更改自己的头像时候 需要显示原图片 所以在做页面的
     *        跳转时候 是加进了Intent里面 故此提成全局变量
     * @ 注意 此地址（用户的本地头像地址）是区别与当用户更新之后的图片地址 更新的地址是不加如数据库的操作
     *
     */
    String mPicasso;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.min_fragment_personal_center, null);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventMessaeData(MessageEvent msg){
        /**
         *
         * @详细信息请查看 {@link #PersonalCenterActivity}
         *
         * @ 承接 PersonalCenterActivity 中通过广播对象 EventBus的对象传递的MessageEvent
         *   的对象，因为需要多次使用用户头像的地址 故在此接收的用户头像地址为 ../../upload/2019051628764.jpg
         */

        Log.e(TAG,new Date() + " >>> PersonalCenterFragemnt " +msg.getMessage());
        // 拼接Picasso 使用的地址
        String mPicassoPath = msg.getMessage().replace("../../", Configfile.SERVICE_WEB_IMG);
        Log.e(TAG,new Date() + " >>> 用户更新之后的头像地址 PersonalCenterActivity "+mPicassoPath);
        Picasso.with(getActivity()).load(mPicassoPath).into(bitmap);
        //更新图片地址
        /**
         * http://ssdaixiner.oicp.net:26168/wcjw/mobile/toUpdatePersonPhoto?url=../../upload/20188987678687467.jpg&id=1
         *
         */
        AppMothedHelper helper=new AppMothedHelper(getActivity());
        boolean dismisData = helper.isDismisData(getActivity(), Configfile.USER_DATA_KEY);
        if(dismisData){
            Map<Object, Object> map = helper.queryByKey(Configfile.USER_DATA_KEY);
            if(map.size() != 0){
                Map<String, String> data = JSON.GetJsonRegiste(((String) map.get("data")));
                //数据库获取用户ID
                String mId = data.get("id");
                //设置地址
                String mMsgPath="../../" + msg.getMessage();
                //拼接地址
                /**
                 * {@value  /toUpdatePersonPhoto?url=../../upload/20188987678687467.jpg&id=1}
                 * {@link PersonalCenterFragemnt} 新地址构成分别有 web地址 + ../../ +id
                 */
                final String mUpdateUrl = Configfile.UPDATE_USER_POTO+"url="+msg.getMessage()+"&"+"id="+mId;
                Log.e(TAG,new Date() + " >>> 预备执行的更新数据库中用户名的头像地址 "+mUpdateUrl);
                new HttpUtils(10000).send(HttpRequest.HttpMethod.GET,
                        mUpdateUrl,
                        new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                if(responseInfo.result != null){
                                    Log.e(TAG,"更新数据库中用户名的头像地址成功");
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                Log.e(TAG,"更新数据库中用户名的头像地址失败！");
                            }
                        });
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //获取用户信息
        final Map<String, String> maps = getPersonalData();
        Log.e(TAG," 适配之前数据库查询结果 "+maps.size()+" >>> "+maps.toString());
        //再次装载 一边SimpleAdapter格式使用
        List<Object> adapterData = creatAdapterData(maps);
        Log.e(TAG," 适配之前数据集合的大小 "+adapterData.size()+"");
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,false);
        mRecycle.setLayoutManager(manager);
        mRecycle.setAdapter(new MyPersonalAdapter(adapterData));



        /*adapter=new SimpleAdapter(getActivity(),
                data,
                R.layout.layout_personal_item,
                        new String[]{"truename","zhiwu","jobid","headPortrait"},
                        new int[]{R.id.min_personal_name, //用户名
                                  R.id.min_personal_type, //职务
                                  R.id.min_personal_remark,//警员编号
                                  R.id.min_personal_poto  //头像
                        }){
            @Override
            public void setViewImage(ImageView v, String value) {
                super.setViewImage(v, value);
                if(v.getId() == R.id.min_personal_poto){
                    // TODO: 2018/5/21 头像地址
                    Log.e(TAG,new Date() + " >>> 用户头像地址 从本地数据库读取" + maps.get("headPortrait"));
                    String path="http://b1.hucdn.com/upload/item/1804/16/59603443964511_800x800.jpg";
                    //判断数据库中是否具有用户的头像信息
                    String headPortrait = maps.get("headPortrait");
                    Log.e(TAG,new Date() + " >>> 用户第一次进入此页面的 获取的头像地址 "+ headPortrait);
                    if(headPortrait != null){
                        *//* 用户头像地址实例 http://ssdaixiner.oicp.net:26168/wcjw/upload/20180522211240011.jpg *//*
                        //拆分 ../../
                        String imgNamePath = headPortrait.replace("../../", "");

                        //拼接地址
                        mPicasso = Configfile.SERVICE_WEB_IMG+imgNamePath;
                        Log.e(TAG,new Date() + " >>> 用户第一次进入此页面的 做完拼接之后的头像地址 Picasso使用 "+ mPicasso);
                        //执行加载
                        Picasso.with(getActivity())
                                .load(mPicasso)
                                .placeholder(R.drawable.icon_normal_user_poto)
                                .error(R.drawable.icon_normal_404)
                                .into(v);
                    }else {
                        //当图片不存在的时候加载 全局暂未有图片的 默认图片
                        Picasso.with(getActivity()).load(Configfile.IMAGE_NO).into(v);
                    }

                }
            }

            *//* Item里面的内一个子空间的点击事件 *//*
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
                        //String path="http://b1.hucdn.com/upload/item/1804/16/59603443964511_800x800.jpg";
                        intent.putExtra("old_img_url",mPicasso);
                        startActivity(intent);
                    }
                });


                return view;

            }
        };

        listView.setAdapter(adapter);*/

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

        Log.e("TAG","===== >>>>> 为解析前 "+data);

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

    /**
     * {@link #getPersonalData 返回的数据不适合适配器的使用 故此在进行加工使其满足适配器的使用}
     */
    public List<Object> creatAdapterData(Map<String, String> map){

        List<Object> obj=new ArrayList<>();

        if(map.size() > 0){
            Map<String,String> imgPath=new HashMap<>();
            imgPath.put("headPortrait",map.get("headPortrait"));
            obj.add(0,imgPath);

            List<Map<String,String>> mMps=new ArrayList<>();
            String[] key=new String[]{"id","zhiwu","danweiid","truename","username","jobid","phone"};
            String[] cnValue=new String[]{"编号","职务","单位ID","姓名","用户名","警员编号","联系方式"};
            for (int i = 0; i < key.length; i++) {
                Map<String,String> imgText=new HashMap<>();
                imgText.put("value",map.get(key[i]));
                imgText.put("type",cnValue[i]);
                mMps.add(imgText);
            }
            obj.add(1,mMps);
            return obj;
        }else return new ArrayList<>();
    }


    /* 本类适配器 */
    public class MyPersonalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public int TYPE_0 = 0;
        public int TYPE_1 = 1;

        List<Object> mData;

        public MyPersonalAdapter(List<Object> mData) {
            this.mData = mData;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == TYPE_0){
                View view = LayoutInflater.from(getActivity())
                        .inflate(R.layout.layout_fragment_personal_image,
                                parent,
                                false);

                return new MyViewHolderImage(view);
            }else if(viewType == TYPE_1){
                View view = LayoutInflater.from(getActivity())
                        .inflate(R.layout.layout_fragment_personal_adapter,
                                parent,
                                false);
                return new MyViewHolderRecycle(view);
            }

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof MyViewHolderImage){
                Map<String, String> map = (Map<String, String>) mData.get(0);

                Log.e(TAG," 适配MyViewHolderImage "+map.size()+" >>> "+map.toString());

                bitmap = ((MyViewHolderImage) holder).mImage;
                /* 获取的地址 */
                mPicasso = map.get("headPortrait").replace("../../", Configfile.SERVICE_WEB_IMG);
                Picasso.with(getActivity()).load(mPicasso).placeholder(R.drawable.icon_normal_no_photo)
                        .error(R.drawable.icon_normal_404).into(bitmap);

                bitmap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //启动本地资源
                        Configfile.Log(getActivity(),"SimpleImageViewCircleBitmap");
                        Intent intent=new Intent(getActivity(),PersonalCenterActivity.class);
                        // TODO: 2018/5/21 模拟原始图片地址
                        Log.e(TAG,"解析的图片全路径地址 >>> "+mPicasso);
                        //String path="http://b1.hucdn.com/upload/item/1804/16/59603443964511_800x800.jpg";
                        intent.putExtra("old_img_url",mPicasso);
                        startActivity(intent);
                    }
                });

            }else if(holder instanceof MyViewHolderRecycle){
                RecyclerView mRecycleView = ((MyViewHolderRecycle) holder).mRecycleView;
                RecyclerView.LayoutManager manager=new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL,
                        false);
                mRecycleView.setLayoutManager(manager);
                List<Map<String, String>> maps = (List<Map<String, String>>) mData.get(1);
                Log.e(TAG," 适配MyViewHolderRecycle "+maps.size()+" >>> "+maps.toString());

                MyAdapterRecycleViewItem mMinAdapter = new MyAdapterRecycleViewItem(maps);
                mRecycleView.setAdapter(mMinAdapter);
            }
        }

        @Override
        public int getItemCount() {
            if(mData.size() != 0){
                return mData.size();
            }else {
                return 0;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0){
                return TYPE_0;
            }else if (position == 1){
                return TYPE_1;
            }else {
                return 2;
            }
        }

        /* 静态内部类 */
        public class MyViewHolderImage extends RecyclerView.ViewHolder{

            @ViewInject(R.id.mPersonalAdapterImage)
            SimpleImageViewCircleBitmap mImage;

            public MyViewHolderImage(View itemView) {
                super(itemView);
                ViewUtils.inject(this,itemView);
            }
        }

        public class MyViewHolderRecycle extends RecyclerView.ViewHolder{

            @ViewInject(R.id.mPersonalAdapterRecycleView)
            RecyclerView mRecycleView;
            public MyViewHolderRecycle(View itemView) {
                super(itemView);
                ViewUtils.inject(this,itemView);
            }
        }


        public class MyAdapterRecycleViewItem extends RecyclerView.Adapter<MyViewHolderRecycleViewItem>{

            List<Map<String,String>> mapList;

            public MyAdapterRecycleViewItem(List<Map<String, String>> mapList) {
                this.mapList = mapList;
            }

            @Override
            public MyViewHolderRecycleViewItem onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_fragment_personal_adapter_item,
                        parent,
                        false);
                return new MyViewHolderRecycleViewItem(view);
            }

            @Override
            public void onBindViewHolder(MyViewHolderRecycleViewItem holder, int position) {
                if(holder instanceof MyViewHolderRecycleViewItem){
                    Map<String, String> map = mapList.get(position);
                    holder.mPersonalAdapterType.setText(map.get("type"));
                    holder.mPersonalAdapterValue.setText(map.get("value"));
                }
            }

            @Override
            public int getItemCount() {
                return mapList.size();
            }
        }

        public class MyViewHolderRecycleViewItem extends RecyclerView.ViewHolder{

            @ViewInject(R.id.mPersonalAdapterType)
            TextView mPersonalAdapterType;

            @ViewInject(R.id.mPersonalAdapterValue)
            TextView mPersonalAdapterValue;

            public MyViewHolderRecycleViewItem(View itemView) {
                super(itemView);
                ViewUtils.inject(this,itemView);
            }
        }



    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
