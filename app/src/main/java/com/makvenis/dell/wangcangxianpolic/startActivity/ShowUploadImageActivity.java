package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;

/* 证据事实上传的图片查看 */
@ContentView(R.layout.activity_show_upload_image)
public class ShowUploadImageActivity extends AppCompatActivity {

    @ViewInject(R.id.mShowImage1)
    ImageView mShowImage1;
    @ViewInject(R.id.mShowImage2)
    ImageView mShowImage2;
    @ViewInject(R.id.mShowImage3)
    ImageView mShowImage3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);



    }
}
