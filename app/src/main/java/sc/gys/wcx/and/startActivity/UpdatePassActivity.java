package sc.gys.wcx.and.startActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import sc.gys.wcx.and.R;
import sc.gys.wcx.and.tools.Configfile;
import sc.gys.wcx.and.tools.NetworkTools;
import sc.gys.wcx.and.view.SimpleLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * @解释 用户密码找回
 * @全局 采用
 */

@ContentView(R.layout.activity_update_pass)
public class UpdatePassActivity extends BaseActivity {

    @ViewInject(R.id.user_name)
    EditText userName;

    @ViewInject(R.id.user_password)
    EditText userPass;

    @ViewInject(R.id.user_password_two)
    EditText userPassTwo;

    @ViewInject(R.id.user_submit)
    TextView mSubmit;

    /* 全局Dialog */
    SimpleLoadingDialog looading;

    /* 处理toolbar 开始 version=2  */
    /* include 里面的点击事件 */
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.toolbar_callbank_text)
    TextView mBankTextView;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;
    /* 处理toolbar 结束 */

    /* 全局用户名称 */
    public String mName;

    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String obj = (String) msg.obj;
            Log.e("TAG",new Date()+" >>> 当前服务器返回的值 "+ obj);
            JSONObject object= null;
            try {
                object = new JSONObject(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(msg.what == 0X000007){
                if(object.optString("state").equals("OK")){
                    Configfile.Log(UpdatePassActivity.this,(String) msg.obj);
                    startActivity(new Intent(UpdatePassActivity.this,HomeActivity.class),ActivityOptions.makeSceneTransitionAnimation(UpdatePassActivity.this).toBundle());
                    looading.dismiss();
                }else {
                    Configfile.Log(UpdatePassActivity.this,"请求失败");
                    looading.dismiss();
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_update_pass);
        ViewUtils.inject(this);

        mTextView.setText("修改密码");
        /* 赋值当前用户名称 */
        userName.setText(mName);

    }

    @Override
    public void getUserName(String s) {
        this.mName=s;
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkViewImage(View v){
        Intent intent=new Intent(this,HomeActivity.class);
        intent.putExtra("bank_id",2);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank_text})
    public void oncklinkViewTextView(View v){
        Intent intent=new Intent(this,HomeActivity.class);
        intent.putExtra("bank_id",2);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 提交密码修改 */
    @OnClick({R.id.user_submit})
    public void Submit(View v){
        looading=new SimpleLoadingDialog(UpdatePassActivity.this);
        looading.setMessage("正在提交...").show();
        String passFirst = userPass.getText().toString();
        String passTwo = userPassTwo.getText().toString();
        /* ssdaixiner.oicp.net:26168/wcjw/mobile/doUpdatePassword?username=ssdai&passwordOld=&passwordNew= */
        if(passFirst != null && passTwo != null && passFirst != passTwo){
            //
            String path= Configfile.UPDATE_PASS+"?username="+mName+"&passwordOld="+passFirst+"&passwordNew="+passTwo;
            Log.e("TAG",new Date()+" >>>> 密码修改预备提交的地址 "+path);
            NetworkTools.getHttpTools(path,mHandler,0X000007);
        }else {
            Configfile.Log(UpdatePassActivity.this,"不能使用近期的密码或输入为空");
        }
    }

}
