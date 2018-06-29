package sc.gys.wcx.and.startActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import sc.gys.wcx.and.adapter.SimpleViewPage;
import sc.gys.wcx.and.minFragment.PersonalCenterFragemnt;
import sc.gys.wcx.and.R;
import sc.gys.wcx.and.help.JSON;
import sc.gys.wcx.and.help.MessageEvent;
import sc.gys.wcx.and.minFragment.PersonalCenterActivity;
import sc.gys.wcx.and.minFragment.TaskCenterFragemnt;
import sc.gys.wcx.and.newdbhelp.AppMothedHelper;
import sc.gys.wcx.and.otherActivity.SetActivity;
import sc.gys.wcx.and.tools.Configfile;
import sc.gys.wcx.and.view.SimpleImageViewCircleBitmap;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 首页界面 */

public class LinkFragment extends Fragment{

    public String TAG = "LinkFragment";

    @ViewInject(R.id.mViewPage)
    private ViewPager mViewPage;
    @ViewInject(R.id.mTabLayout)
    private TabLayout mTabLayout;

    /* 设置CollapsingToolbarLayout缩放时候字体颜色 */
    @ViewInject(R.id.CollapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    /* 返回 */
    @ViewInject(R.id.mHomeLink_bank)
    ImageView mHomeBank;

    /* tool 快速进入设置页面 */
    @ViewInject(R.id.mHomeLink_set)
    ImageView mSetImage;

    /* 更新时间 */
    @ViewInject(R.id.mTime)
    TextView mTimer;

    @ViewInject(R.id.mPersonal_Photo)
    SimpleImageViewCircleBitmap mPersonal_Photo;

    /* 用户头像初始化地址 */
    String mPicasso;


    /**
     * 重写此方法是为了注册广播
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    /**
     * @ 当接受 {@link PersonalCenterActivity}
     *   传递的更新地址后 执行操作
     *
     * */
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
        Picasso.with(getActivity()).load(mPicassoPath).into(mPersonal_Photo);
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

    /**
     * 数据库查找用户信息 初始化使用
     * @return
     */
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_link,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SimpleDateFormat fm = new SimpleDateFormat("MM-dd");
        String time = fm.format(new Date());


        /**
         *
         * 初始化用户头像 地址来源数据库的查找
         * {@link #getPersonalData()}
         */
        Map<String, String> map = getPersonalData();
        mPicasso = map.get("headPortrait").replace("../../", Configfile.SERVICE_WEB_IMG);
        if(mPicasso == ""){

        }else {
            /*Log.e(TAG,map.get("headPortrait"));
            Picasso.with(getActivity()).load(mPicasso).placeholder(R.drawable.icon_normal_no_photo)
                    .error(R.drawable.icon_normal_404).into(mPersonal_Photo);*/
        }


        mPersonal_Photo.setOnClickListener(new View.OnClickListener() {
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

        if(map.get("truename") != null){
            mTimer.setText(map.get("truename")+" 最近更新:"+time);
        }else mTimer.setText("最近更新:"+time);




        /**
         * 扩张时候的title颜色：
         * mCollapsingToolbarLayout.setExpandedTitleColor();
         * 收缩后在Toolbar上显示时的title的颜色：
         * mCollapsingToolbarLayout.setCollapsedTitleTextColor();
         */
        mCollapsingToolbarLayout.setEnabled(true);
        mCollapsingToolbarLayout.setTitle(map.get("truename")+"MAK");
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.RED);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.RED);

        List<Fragment> dataFragment = new ArrayList<>();
        dataFragment.add(new PersonalCenterFragemnt());
        dataFragment.add(new TaskCenterFragemnt());
        mTabLayout.setupWithViewPager(mViewPage);
        mViewPage.setAdapter(new SimpleViewPage(getChildFragmentManager(),dataFragment));

    }

    @OnClick({R.id.mHomeLink_bank})
    public void bank(View view){
        startActivity(new Intent(getActivity(),HomeActivity.class));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this,view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * {@link #onResume 接收页面的回跳显示}
     */
    @Override
    public void onResume() {
        super.onResume();
        int id = getActivity().getIntent().getIntExtra("bank_id", 0);
        if (id == 2) {
            mViewPage.setCurrentItem(1);
        }else if(id==1){
            mViewPage.setCurrentItem(0);
        }
    }


    /* 快速进入设置页面 */
    @OnClick({R.id.mHomeLink_set})
    public void setOnclink(View v){
        Intent intent = new Intent(getActivity(), SetActivity.class);
        // TODO: 2018/6/5
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }




}
