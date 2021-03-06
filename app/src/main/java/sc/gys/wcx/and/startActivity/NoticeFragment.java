package sc.gys.wcx.and.startActivity;

import android.app.ActivityOptions;
import android.content.Intent;
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
import android.widget.LinearLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import sc.gys.wcx.and.activity.AlertPushNewsActivity;
import sc.gys.wcx.and.company.CompanyActivity;
import sc.gys.wcx.and.company.SearchCompanyActivity;
import sc.gys.wcx.and.newdbhelp.AppMothedHelper;
import sc.gys.wcx.and.tools.Configfile;
import sc.gys.wcx.and.view.SimpleLoadingDialog;
import sc.gys.wcx.and.R;
import sc.gys.wcx.and.help.JSON;
import sc.gys.wcx.and.newCompanyPost.ToShouJiaoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 首页界面 */

/**
 * @ 解释: 6大模块总预览
 * @ 分别链接Activity分模块
 *
 */

public class NoticeFragment extends Fragment {

    @ViewInject(R.id.notice_max)
    ImageView serach;

    @ViewInject(R.id.editText)
    EditText mEdit;

    /* 安全检查 */
    @ViewInject(R.id.notice_aujc) //
    private LinearLayout ll;

    /* 警情推送 */
    @ViewInject(R.id.notice_jqts) //
    private LinearLayout ll_jqts;

    /* 法律法规 */
    @ViewInject(R.id.notice_flfg) //
    private LinearLayout ll_flfg;

    /* 一标三十 */
    @ViewInject(R.id.notice_ybss) //
    private LinearLayout ll_ybss;

    /* 全局Dialog */
    SimpleLoadingDialog dialog;


    /* 全局Handler */
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            String obj = (String) msg.obj;
            switch (what){
                case 0X000006:

                    Bundle bundle=new Bundle();
                    bundle.putString("mAdapter",obj);
                    Intent intent=new Intent(getActivity(),AlertPushNewsActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                    dialog.dismiss();
                    break;
                case 0X000004:
                    if(obj != null){
                        dialog.dismiss();
                        Intent intent1 = new Intent(getActivity(), CompanyActivity.class);
                        intent1.putExtra("where",obj);
                        startActivity(intent1,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                    }else {
                        Configfile.Log(getActivity(),"请求数据不存在！");
                        dialog.dismiss();
                    }

                    break;
                case 0X000005:
                    if(obj != null){
                        dialog.dismiss();
                        Intent intent1 = new Intent(getActivity(), SearchCompanyActivity.class);
                        intent1.putExtra("where",obj);
                        startActivity(intent1,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                    }else {
                        Configfile.Log(getActivity(),"请求数据不存在！");
                        dialog.dismiss();
                    }

                    break;
            }
        }
    };


    @OnClick({R.id.notice_max})
    public void onclinkSearch(View v){
        final String e = mEdit.getText().toString();
        Log.e("TAG",new Date()+" >>> 预备搜索的地址 "+ Configfile.COMPANY_URL_SEARCH+e);
        if(e != null){
            showdDialog();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new HttpUtils(10000).send(HttpRequest.HttpMethod.GET,
                            Configfile.COMPANY_URL_SEARCH+e,
                            new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    String result = responseInfo.result;
                                    if(result != null){
                                        try {
                                            /**
                                             * @ 解释：BjcUnitlist[{}]
                                             * @ 解释 pageJson{}
                                             * @ 单位搜索 name=""&pageNow=1++
                                             */
                                            JSONObject jsonObject=new JSONObject(result);
                                            JSONArray array = jsonObject.getJSONArray("BjcUnitlist");
                                            String mConpany = array.toString();
                                            Log.e("TAG",new Date()+" >>> json解释的 BjcUnitlist 值为"+mConpany+" 长度为"+array.length());
                                            if(array.length() != 0){
                                                Message msg=new Message();
                                                msg.what=0X000005;
                                                msg.obj=e; //搜索的条件
                                                mHandler.sendMessage(msg);
                                            }else {
                                                Configfile.Log(getActivity(),"搜索的数据不存在！");
                                                dialog.dismiss();
                                            }

                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    Configfile.Log(getActivity(),"网络链接失败！");
                                    dialog.dismiss();
                                }
                            });
                }
            }).start();
        }else {

            Configfile.Log(getActivity(),"请输入搜索内容！");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_notice,null);
        return view;
    }

    /* 获取用户信息列表 */
    public String getSqliteName(){
        /* 数据库操作 获取当前用户名称 */
        AppMothedHelper helper=new AppMothedHelper(getActivity());
        Map<Object, Object> map = helper.queryByKey(Configfile.USER_DATA_KEY);
        String data = (String) map.get("data");
        Map<String, String> map1 = JSON.GetJsonRegiste(data);
        String s = map1.get("username");
        Log.e("TAG",new Date() + " >>> 当前用户名称 "+s);

        return s;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        // 安全检查
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdDialog();
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
                                            msg.what=0X000004;
                                            msg.obj=result;
                                            mHandler.sendMessage(msg);
                                        }
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {
                                        Configfile.Log(getActivity(),"网络链接失败！");
                                        dialog.dismiss();
                                    }
                                });
                    }
                }).start();
            }
        });

        // 法律法规
        ll_flfg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdDialog();
                new HttpUtils(5000).send(HttpRequest.HttpMethod.GET,
                        Configfile.NEWS_PATH+"1",//表示默认请求第一页
                        new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                String result = responseInfo.result;
                                if(result != null){
                                    Message msg=new Message();
                                    msg.obj=result;
                                    msg.what=0X000006;
                                    mHandler.sendMessage(msg);
                                }else {
                                    Configfile.Log(getActivity(),"数据请求失败");
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                Configfile.Log(getActivity(),"网络连接失败！");
                                dialog.dismiss();
                            }
                        });
            }
        });

        // 警情推送
        ll_jqts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* startActivity(new Intent(getActivity(), AlertPushPoliceActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());*/
            }
        });

        // 一标三十
        ll_ybss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ToShouJiaoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this,view);
    }

    /* 全局Dialog */
    public void showdDialog(){
        dialog=new SimpleLoadingDialog(getActivity());
        dialog.setMessage("正在请求数据...").show();
    }
}
