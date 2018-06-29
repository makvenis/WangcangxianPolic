package sc.gys.wcx.and.view;

import android.app.Dialog;
import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import sc.gys.wcx.and.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * 自定义弹出框 确认 取消两个点击事件都必须重写
 */

public class SimpleDialogNameBitemapView extends Dialog {

    private Context context;
    private GestureOverlayView mGestureOverlayView;
    private setOnclinkDialogCancelListener onclinkDialogCancelListener;
    private setOnclinkDialogSureListener onclinkDialogSureListener;
    private ImageView mImageView;


    public void setmImageView(ImageView mImageView) {
        this.mImageView = mImageView;
    }

    public void setmGestureOverlayView(GestureOverlayView mGestureOverlayView) {
        this.mGestureOverlayView = mGestureOverlayView;
    }

    public SimpleDialogNameBitemapView(@NonNull Context context) {
        super(context);this.context=context;
    }

    public SimpleDialogNameBitemapView(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);this.context=context;
    }

    protected SimpleDialogNameBitemapView(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.layout_user_ui_dialog_name_bitmap_style, null);
        setContentView(view);
        innt(view);

    }
    public TextView sumit_sure,submit_cancel;

    public void setOnclinkDialogCancelListener(setOnclinkDialogCancelListener onclinkDialogCancelListener) {
        this.onclinkDialogCancelListener = onclinkDialogCancelListener;
    }

    public void setOnclinkDialogSureListener(setOnclinkDialogSureListener onclinkDialogSureListener) {
        this.onclinkDialogSureListener = onclinkDialogSureListener;
    }

    private void innt(View view) {
        sumit_sure = ((TextView) view.findViewById(R.id.sumit_sure));
        submit_cancel = ((TextView) view.findViewById(R.id.submit_cancel));
        mGestureOverlayView = ((GestureOverlayView) view.findViewById(R.id.mGestureOverlayView));

        setGesture(null);

        submit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclinkDialogCancelListener.OnClinkCancelListener();
            }
        });

        sumit_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclinkDialogSureListener.OnClinkSureListener();
            }
        });


    }

    private void setGesture(final ImageView imageView){
        mGestureOverlayView.setFadeOffset(8000);
        mGestureOverlayView.setGestureColor(Color.GREEN);//设置手势的颜色
        mGestureOverlayView.setGestureStrokeWidth(2);//设置手势的画笔粗细
        mGestureOverlayView.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                //将手势转换成图片
                Bitmap bitmap = gesture.toBitmap(128,128,10,0xffff0000);
                //String fileName = "/mnt/sdcard/kkk.png";

                File dir = getContext().getExternalCacheDir();
                FileOutputStream bitmapWtriter = null;
                try {
                    bitmapWtriter = new FileOutputStream(dir);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, bitmapWtriter);
            }
        });
    }


    public interface setOnclinkDialogSureListener{
        Void OnClinkSureListener();
    }
    public interface setOnclinkDialogCancelListener{
        Void OnClinkCancelListener();
    }



}
