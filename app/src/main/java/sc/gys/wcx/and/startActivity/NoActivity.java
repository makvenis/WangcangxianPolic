package sc.gys.wcx.and.startActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import sc.gys.wcx.and.tools.Configfile;
import sc.gys.wcx.and.R;


@ContentView(R.layout.activity_no)
public class NoActivity extends AppCompatActivity {

    @ViewInject(R.id.toolbar_callbank_text)
    TextView callbank_text;
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;

    @ViewInject(R.id.mNetTools)
    TextView mNetTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        mTextView.setText("网络故障");

        mImageView_bank.setVisibility(View.GONE);
        callbank_text.setVisibility(View.GONE);
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

    /* 检查网络 */
    @OnClick({R.id.mNetTools})
    public void coorNetTools(View v){
        int i = boolenNet();
        if( i != 0){
            mImageView_bank.setVisibility(View.VISIBLE);
            callbank_text.setVisibility(View.VISIBLE);
            Intent intent=new Intent(this,HomeActivity.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }else {
            Configfile.Log(this,"网络无法连接");
        }

    }


    /* 网络判断 */
    private int boolenNet(){

        int netType = 0;//没有网络
        ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;// wifi
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager mTelephony = (TelephonyManager) this
                    .getSystemService(this.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    && !mTelephony.isNetworkRoaming()) {
                netType = 2;// 3G网络
            } else {
                netType = 3;// 2G网络
            }
        }
        return netType;
    }

    long mOlTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() ==  KeyEvent.ACTION_DOWN){
            if(System.currentTimeMillis()-2000 > mOlTime){
                mOlTime=System.currentTimeMillis();
                Toast.makeText(this,"再次点击退出",Toast.LENGTH_LONG).show();
            }else {
                System.exit(0);
                // TODO: 2018/4/2 退出APP的事件
            }
        }
        return true;
    }
}
