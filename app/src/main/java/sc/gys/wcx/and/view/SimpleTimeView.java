package sc.gys.wcx.and.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 倒计时的提示框 用于首页广告*/


public class SimpleTimeView extends View {

    private Paint mPaint;
    /*圆心的坐标*/
    private int Rx;
    private int Ry;
    /*初始值*/
    private int progress = 0;
    /*半径*/
    private int mRaudios = 50;

    public SimpleTimeView(Context context) {
        super(context);innit();
    }

    public SimpleTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);innit();
    }

    public SimpleTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);innit();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMeasure(widthMeasureSpec),getMeasure(heightMeasureSpec));
    }
    public int getMeasure(int measure){

        int mode = MeasureSpec.getMode(measure);
        int size = MeasureSpec.getSize(measure);

        int defaultSize = 0;
        if(mode == MeasureSpec.EXACTLY){
            defaultSize = size;
        }else {
            defaultSize = 140;
            if(mode == MeasureSpec.AT_MOST){

                defaultSize=Math.min(defaultSize,size);
            }
        }

        return defaultSize;

    }
    public void innit(){
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //初始化
        inntRaudiosLike(canvas);
        //运动圆形
        timeProRaudios(canvas);
    }

    private void timeProRaudios(Canvas canvas) {
        progress++;
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(20);
        mPaint.setStrokeWidth(4);
        //定义的圆弧的形状和大小的界限
        RectF rectF = new RectF(Rx-mRaudios+3,Ry-mRaudios+3,Rx+mRaudios+3,Ry+mRaudios+3);

        //根据进度画圆弧
        canvas.drawArc(rectF, 235, -360 * progress / 100, false, mPaint);
        //画内部倒计时与跳过
        mPaint.setTextSize(20);//字体大小
        mPaint.setStrokeWidth(1);//字体粗细
        //设置加载百分比
        if(progress <= 100){ //加载
            mPaint.setColor(Color.BLUE);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText((5-(progress/20))+"S",Rx-mRaudios/4,Ry+10,mPaint);
        }else { //加载完成
            mPaint.setColor(Color.BLUE);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText("跳过",Rx-mRaudios/3,Ry+10,mPaint);
        }
        //每隔10毫秒界面刷新
        postInvalidateDelayed(10);
    }
    //初始化外圆---细圆
    private void inntRaudiosLike(Canvas canvas) {
        //获取远的坐标
        //获取圆心的x坐标
        Rx = getWidth() / 2;
        Ry = getWidth() / 2;
        //内部实心北景圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#ecebeb"));
        canvas.drawCircle(Rx,Ry,mRaudios,mPaint);
    }
}
