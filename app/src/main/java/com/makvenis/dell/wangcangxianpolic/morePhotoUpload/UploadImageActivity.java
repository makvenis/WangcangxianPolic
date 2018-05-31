package com.makvenis.dell.wangcangxianpolic.morePhotoUpload;

import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.details.MoreDetailsActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * 采用知乎多图选择框架
 * https://github.com/zhihu/Matisse
 */

@ContentView(R.layout.activity_upload_image)
public class UploadImageActivity extends AppCompatActivity {

    @ViewInject(R.id.mRecycle)
    RecyclerView mRecycle;
    private UploadAAdapter adapter;

    List<Uri> mPath=new ArrayList<>();

    @ViewInject(R.id.mToolbar_upload)
    TextView uploadImage;
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

        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
        + getResources().getResourcePackageName(R.drawable.icon_upload_add) + "/"
        + getResources().getResourceTypeName(R.drawable.icon_upload_add) + "/"
        + getResources().getResourceEntryName(R.drawable.icon_upload_add));

        mPath.add(uri);

        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        mRecycle.setLayoutManager(manager);
        adapter = new UploadAAdapter(this,mPath);
        mRecycle.setAdapter(adapter);

        adapter.setAddItemOnClink(new UploadAAdapter.AddItemOnClink() {
            @Override
            public void show() {

                Matisse.from(UploadImageActivity.this)
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                        .theme(R.style.Matisse_Dracula)
                        .countable(false)
                        .maxSelectable(12)
                        .imageEngine(new PicassoEngine())
                        .forResult(1);
                adapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            Toast.makeText(UploadImageActivity.this,mSelected.size()+"",Toast.LENGTH_SHORT).show();
            Log.e("TAG", "mSelected: " + mSelected.toString() + " 大小 " +mSelected.size());
            for (int i = 0; i < mSelected.size(); i++) {
                mPath.add(mSelected.get(i));
            }
            adapter.notifyDataSetChanged();
        }
    }


    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkViewImage(View v){
        Intent intent = new Intent(this, MoreDetailsActivity.class);
        intent.putExtra("bank_id",2);
        SharedPreferences xmlId = getSharedPreferences("xmlId", Context.MODE_PRIVATE);
        String xmlIdString = xmlId.getString("id", "0");
        intent.putExtra("id",xmlIdString);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank_text})
    public void oncklinkViewTextView(View v){
        Intent intent = new Intent(this, MoreDetailsActivity.class);
        intent.putExtra("bank_id",2);
        SharedPreferences xmlId = getSharedPreferences("xmlId", Context.MODE_PRIVATE);
        String xmlIdString = xmlId.getString("id", "0");
        intent.putExtra("id",xmlIdString);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }




}
