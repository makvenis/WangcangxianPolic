package com.makvenis.dell.wangcangxianpolic.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.makvenis.dell.wangcangxianpolic.R;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 自定义Dialog */

/**
 * @ 解释: 加载custom_progress_draw.xml
 * @ 解释: 背景图
 *
 */


public class SimpleLoadingDialog extends Dialog {

    private TextView tv_text;

    public SimpleLoadingDialog(@NonNull Context context) {
        super(context);
        /**设置对话框背景透明*/
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.loading);
        tv_text = (TextView) findViewById(R.id.tv_text);
        setCanceledOnTouchOutside(false);
    }

    /**
     * 为加载进度个对话框设置不同的提示消息
     *
     * @param message 给用户展示的提示信息
     * @return build模式设计，可以链式调用
     */
    public SimpleLoadingDialog setMessage(String message) {
        tv_text.setText(message);
        return this;
    }


}
