package sc.gys.wcx.and.startActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import sc.gys.wcx.and.R;
import sc.gys.wcx.and.tools.Configfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* 证据事实上传的图片查看 */
@ContentView(R.layout.activity_show_upload_image)
public class ShowUploadImageActivity extends AppCompatActivity {

    @ViewInject(R.id.mDatailsRoll)
    RollPagerView mRollPagerView;

    /* bundle 中获取的Url */
    /* bundle 中获取的标题 */
    private String mTitle_intent;
    private String mUrl_intent;
    private String mCid;
    /* 单位id */
    private String id;

    /* 处理toolbar 开始 version=2  */
    /* include 里面的点击事件 */
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.toolbar_callbank_text)
    TextView mBankTextView;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;
    /* 处理toolbar 结束 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        mTextView.setText("查看");

        getParment();

        /* 装载集合 */
        List<Map<String, String>> maps = new ArrayList<>();
        SharedPreferences pref = getSharedPreferences("UploadCheckActivity",MODE_PRIVATE);
        String url1 = pref.getString("url1", "");
        String url2 = pref.getString("url2", "");
        String url3 = pref.getString("url3", "");

        Log.e("TAG",url1+url2+url3);

        String[] mUrl=new String[]{url1.replace("../../",Configfile.SERVICE_WEB_IMG),
                url2.replace("../../",Configfile.SERVICE_WEB_IMG),
                url3.replace("../../",Configfile.SERVICE_WEB_IMG)};

        for (int i = 0; i < mUrl.length; i++) {

            Map<String, String> map=new HashMap<>();
            map.put("img_url",mUrl[i]);
            maps.add(map);
        }

        setRoallAdapter(maps);
    }

    /* 接收上层父类传递的参数 */
    public void getParment() {
        //页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        mTitle_intent = bundle.getString("mTitle");
        mUrl_intent = bundle.getString("mUrl");
        mCid = bundle.getString("mCid");
        id = bundle.getString("id");
    }

    /* 设置适配器 */
    public void setRoallAdapter(List<Map<String, String>> maps){
        //设置适配器
        mRollPagerView.setAdapter(new MyPagerAdapter(maps, this));
        /* 启动自动翻页 */
        mRollPagerView.pause();
        mRollPagerView.resume();
        mRollPagerView.isPlaying();
        mRollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(ShowUploadImageActivity.this, "Item " + position + " clicked", Toast.LENGTH_SHORT).show();
            }
        });


    }


    public class MyPagerAdapter extends StaticPagerAdapter {

        private int[] image = {R.drawable.icon_test, R.drawable.icon_test, R.drawable.icon_test, R.drawable.icon_test};

        List<Map<String, String>> imgData;
        Context mContext;

        public MyPagerAdapter(List<Map<String, String>> imgData, Context mContext) {
            this.imgData = imgData;
            this.mContext = mContext;
        }

        // SetScaleType(ImageView.ScaleType.CENTER_CROP);
        // 按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());

            //imageView.setImageResource(image[position]);
            String path="https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=488179422,3251067872&fm=200&gp=0.jpg";

            Map<String, String> map = imgData.get(position);
            String url = map.get("img_url");
            Log.e("Adapter",url);
            /* http://ssdaixiner.oicp.net:26168/wcjw/resources/images/nopic2.png */
            if(url != null){
                String[] split = url.split("//");
                String xy = split[0];
                if(xy.equals("http:") || xy.equals("https:")){
                    Picasso.with(mContext)
                            .load(url)
                            .placeholder(R.drawable.icon_normal_no_photo)
                            .error(R.drawable.icon_normal_404)
                            .into(imageView); //
                }else {
                    Picasso.with(mContext)
                            .load(Configfile.SERVICE_WEB_IMG)
                            .placeholder(R.drawable.icon_normal_no_photo)
                            .error(R.drawable.icon_normal_404)
                            .into(imageView); //
                }
            }

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return imageView;
        }

        @Override
        public int getCount() {
            return imgData.size();
        }
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkViewImage(View v){
        Intent intent = new Intent(this, WebHtmlActivity.class);
        intent.putExtra("mTitle",mTitle_intent);
        intent.putExtra("mUrl",mUrl_intent );
        intent.putExtra("mCid", mCid);
        intent.putExtra("id",id);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank_text})
    public void oncklinkViewTextView(View v){
        Intent intent = new Intent(this, WebHtmlActivity.class);
        intent.putExtra("mTitle",mTitle_intent);
        intent.putExtra("mUrl",mUrl_intent );
        intent.putExtra("mCid", mCid);
        intent.putExtra("id",id);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}
