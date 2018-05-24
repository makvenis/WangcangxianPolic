package com.makvenis.dell.wangcangxianpolic.newsnotescheck;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.help.PermissionsUtils;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.otherActivity.HistoryUtils;
import com.makvenis.dell.wangcangxianpolic.startActivity.BaseActivity;
import com.makvenis.dell.wangcangxianpolic.startActivity.WebViewActivity;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.NetworkTools;
import com.makvenis.dell.wangcangxianpolic.view.SimpleLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.com.mozile.tools.Base64Util;
import cn.mozile.schManage.pojo.JwjcDetails;

public class NotesNewActivity extends BaseActivity implements GestureDetector.OnGestureListener {

    /* 数据缓存 */
    private static final String LOCAL_CACHE_PATH = "/mnt/sdcard/img.png";

    /* 获取中间数据的存储在数据库中的key值 */
    private String TagDb;

    /* 接收页面传递数据是否具有头部文件 */
    private boolean boolhead;

    /* 接收页面传递数据是否具有头部文件---ID */
    private int headid;

    /* 接收页面传递数据是否具有尾部文件 */
    private boolean boolfoot;

    /* 接收页面传递数据是否具有尾部文件---ID */
    private int footid;

    /* 接收页面传递的toolbar里面的标题 */
    private String mtitle;

    /* Toolbar 主要设置标题 */
    private TextView mToolbar;

    /* 接收设置Type的html地址 */
    public String mTagUrl;

    /* 提交的Dialog */
    SimpleLoadingDialog mDialog;

    /* View容器 */
    private ViewFlipper mViewFlipper;

    /* Dialog的时间定义 */
    public String mNowTime;

    //1.定义手势检测器对象
    GestureDetector mGestureDetector;

    //2.定义一个动画数组，用于为ViewFilpper指定切换动画效果。
    Animation[]  animations = new  Animation[4];

    //3.定义手势两点之间的最小距离
    final int FLIP_DISTANCE = 50 ;
    List<Question> mQuestion =  new ArrayList<>();

    //4.小的适配器
    ChineseMedicineReportAdapter adapter;

    //5.答案集合管理
    List<String> postList = new ArrayList<>();

    //全局Handler
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what){

                case Configfile.CALLBANK_POST_MSG:
                    try {
                        JSONObject object=new JSONObject((String) msg.obj);
                        String bianhao = object.optString("bianhao");

                        Configfile.Log(NotesNewActivity.this,"提交成功！"+ ((String) msg.obj));
                        mDialog.dismiss();

                         /* 创建数据 */
                        Bundle bundle=new Bundle();
                        bundle.putString("mUrl",mTagUrl);//网址
                        bundle.putString("mTitle",mtitle);//标题
                        bundle.putString("mCid",bianhao);//标题
                        bundle.putString("id",id);//单位ID

                        /* 跳转 */
                        Intent mIntent=new Intent(NotesNewActivity.this,WebViewActivity.class);
                        mIntent.putExtras(bundle);
                        startActivity(mIntent);

                        /* 调用日志文件 */
                        HistoryUtils.executeAddrs(NotesNewActivity.this,
                                id,mtitle,"完成");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    /* 单位ID */
    private String id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity_main);

        /* 动态授权 */
        PermissionsUtils mPermissions=new PermissionsUtils();
        mPermissions.SetPermissionForNormal(this);

        /* 寻找Toolbar */
        mToolbar = ((TextView) findViewById(R.id.toolbar_name));

        /* 查找View容器 */
        mViewFlipper = ((ViewFlipper) findViewById(R.id.mViewFlipper));

        //页面接收数据
        Bundle bundle = getIntent().getExtras();

        /* 接收页面传递数据库的存储标签 */
        TagDb = bundle.getString("TagDb");

        /* 接收页面传递数据是否具有头部文件 */
        boolhead = bundle.getBoolean("boolhead");

        /* 接收页面传递数据是否具有头部文件---ID */
        headid = bundle.getInt("headid");

        /* 接收页面传递数据是否具有尾部文件 */
        boolfoot = bundle.getBoolean("boolfoot");

        /* 接收页面传递数据是否具有尾部文件---ID */
        footid = bundle.getInt("footid");

        /* 接收页面传递的toolbar里面的标题  */
        mtitle = bundle.getString("Title");

        /* 接收设置Toolbar的标题 */
        mToolbar.setText(mtitle);

        /* 接收设置Type的html地址 */
        mTagUrl = bundle.getString("TagUrl");

        /* 获取单位ID */
        id = bundle.getString("id");

        Log.e("TAG",new Date()+" >>>  获取单位ID" + id);

        //1.构建手势检测器
        mGestureDetector =  new GestureDetector(this,this);


        /* 判断头部文件 */
        if(boolhead == true){ //判断当前文件有头部文件吗？
            /* 添加头部文件 */
            addHeadView();
        }

        /* 加载中间公有的文件 */
        addBodyView();

        /* 判断脚部文件 */
        if(boolfoot == true){ //判断当前文件有头脚部文件吗
            addFootView();
        }


        //4.初始化Animation数组
        animations[0] = AnimationUtils.loadAnimation(this,R.anim.left_in);
        animations[1] = AnimationUtils.loadAnimation(this,R.anim.left_out);
        animations[2] = AnimationUtils.loadAnimation(this,R.anim.right_in);
        animations[3] = AnimationUtils.loadAnimation(this,R.anim.right_out);
    }

    private void addFootView() {
        /* 添加尾部文件 */
        View view = View.inflate(this, footid, null);
        mViewFlipper.addView(view);
        /* 添加尾部文件事件处理 */
        setOnclinkOtherLitenterFoot(view);
    }

    private void addBodyView() {

        /* 数据库查询 */
        AppMothedHelper helper=new AppMothedHelper(this);
        boolean dismisData = helper.isDismisData(this, TagDb);
        if(dismisData == true){
            Map<Object, Object> map = helper.queryByKey(TagDb);
            String data = (String) map.get("data");
            /* 准备数据 */
            List<Question> questions = initData(data);
            Log.e("TAG","questions"+questions.size());
            mQuestion.addAll(questions);
            Log.e("TAG","最外层集合mQuestion"+mQuestion.size());
        }

        //添加body(中间部分)的子View
        for (int i = 0;i<mQuestion.size();i++){
            Question question = mQuestion.get(i);
            mViewFlipper.addView(addQuestionView(question,i));
        }

    }

    private void addHeadView() {
        /* 添加头部文件 */
        View view = View.inflate(this, headid, null);
        mViewFlipper.addView(view);
        /* 添加头部文件事件处理 */

    }



    /**
     * @ 解释: json参数为type=(2,3,4)的页面参数适配器  此页面为共有页面 于type=(1) 分别查找控件
     * @param footView 被寻找到的View
     * @ 控件全局定义:
     */
    private TextView jc_zgnr;
    private EditText jc_zgtl_set;
    private TextView jc_zgsj;
    private TextView jc_zgsj_set;
    private TextView jc_other_jcryqz;
    private ImageView jc_other_jcryqz_set;
    private TextView jc_other_bjcryqz;
    private ImageView jc_other_bjcryqz_set;
    private Button mPost;
    private TextView jc_cqcs;
    //尾部View整改的事件处理
    private void setOnclinkOtherLitenterFoot(View footView) {
        /* 控件查找 */
        jc_zgnr = (TextView) footView.findViewById(R.id.jc_zgnr); //整改内容
        jc_zgtl_set = (EditText) footView.findViewById(R.id.jc_zgtl_set); //整改设置

        jc_zgsj = (TextView) footView.findViewById(R.id.jc_zgsj); //整改时间的点击
        jc_zgsj_set = (TextView) footView.findViewById(R.id.jc_zgsj_set); //整改时间的点击

        jc_other_jcryqz = (TextView) footView.findViewById(R.id.jc_other_jcryqz); //检查人签名的点击事件
        jc_other_jcryqz_set = (ImageView) footView.findViewById(R.id.jc_other_jcryqz_set); //签名

        jc_other_bjcryqz = (TextView) footView.findViewById(R.id.jc_other_bjcryqz); //被检查人的签名点击事件
        jc_other_bjcryqz_set = (ImageView) footView.findViewById(R.id.jc_other_bjcryqz_set); //被签名  //jc_cqcs

        jc_cqcs = (TextView) footView.findViewById(R.id.jc_cqcs); //在规定期限(安全行为)采取措施,确保安全


        /* 时间的选择点击事件 */
        jc_zgsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getTimer(NotesNewActivity.this,jc_zgsj_set);
            }
        });

        /* jc_other_jcryqz 检查人签名的点击事件 */
        jc_other_jcryqz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGesture(jc_other_jcryqz_set,"jc");
            }
        });

        /* jc_other_bjcryqz 检查人签名的点击事件 */
        jc_other_bjcryqz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGesture(jc_other_bjcryqz_set,"bjc");
            }
        });

        /* 提交页面 button按钮 */
        mPost = (Button) footView.findViewById(R.id.jc_other_button_post); //整改事件的点击

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Button的提交处理页面 */
                SetButtonPostOnclinkListener();
            }
        });

    }

    /* 触发时间事件选择器 */
    public void getTimer(final Context context, final TextView text){

        final List<String> mTime=new ArrayList<>();
        Calendar c = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(context,
                // 绑定监听器
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        text.setText(year + "年" + monthOfYear+ "月" + dayOfMonth + "日");
                        Log.e("TAG","获取事件"+year + "年" + monthOfYear+ "月" + dayOfMonth + "日");
                    }
                }
                // 设置初始日期
                , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH)).show();

    }

    /**
     * @ 解释: json参数为type=(1)的页面参数适配器  此页面为单有页面 于type=(2,3,4)分别查找控件
     * @param v 被寻找到的View
     * @deprecated 暂不启用此方法
     */
    /* 最后一个视图的操作 tag标签为 Tag = 0 */
    public void setViewNameTimer(View v) {
        final TextView jc_bjcryqz = (TextView) v.findViewById(R.id.jc_bjcryqz); //被检查人签名
        final TextView jc_bjcryqz_timer = (TextView) v.findViewById(R.id.jc_bjcryqz_time); //被检查人员时间
        final Button mButton = (Button) v.findViewById(R.id.jc_button_post);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG","===postMap集合大小:"+postList.size());
                startActivity(new Intent(NotesNewActivity.this, WebViewActivity.class));
            }
        });
    }



    /* type=(2,3,4) 的页面的Post处理事件 */
    private void SetButtonPostOnclinkListener() {

        /* 启动dialog */
        mDialog=new SimpleLoadingDialog(this);
        mDialog.setMessage("正在提交数据中...").show();

        /* 获取输入的用户数据 */
        String m_jc_zgnr_text = jc_zgnr.getText().toString(); //检查整改内容的用户输入数据
        String m_jc_zgtl_set = jc_zgtl_set.getText().toString(); //检查整改条例
        String m_jc_zgsj_set = jc_zgsj_set.getText().toString(); //整改完毕时间
        String m_jc_cqcs_set = jc_cqcs.getText().toString();


        /* 开始获取用户的签名图片 并且转为Base64 */
        //获取用户的图片地址
        File mFile = getExternalFilesDir(null);
        String path_jc = mFile.getAbsoluteFile() + "jc" + ".png"; //检查人的图片地址
        String path_bjc = mFile.getAbsoluteFile() + "bjc" + ".png"; //被检查人的图片地址
        //...采用流读取出来
        /* 添加图片转码 转换成Base64 */
        Log.e("TAG",path_jc + " >>> " + path_bjc + "地址");
        byte[] mByteString_jc = Base64Util.getImageStr(path_jc);
        byte[] mByteString_bjc = Base64Util.getImageStr(path_bjc);


        /* 获取当前的结果集合 */
        JwjcDetails mEntry=new JwjcDetails();

        /* 整改条例 */
        int setTiaolinum = Integer.parseInt(m_jc_zgtl_set);

        /* 处理结果集 */
        List<Integer> mPostDismis=new ArrayList<Integer>();
        if(postList.size() == mQuestion.size()){
            Log.e("TAG","当前答案的结果集大小为--"+postList.size()+">>>传递过来的集合大小为--"+mQuestion.size());
            for (int i = 0; i < postList.size(); i++) {
                String s = postList.get(i);
                Log.e("TAG","结果转换前i-->>>"+i);
                if(s == "是"){
                    int mItnt = 1;
                    Integer integer = Integer.valueOf(mItnt);
                    mPostDismis.add(integer);
                }else if(s == "否"){
                    int mItnt = 2;
                    Integer integer = Integer.valueOf(mItnt);
                    mPostDismis.add(integer);
                }
            }


            /* 添加 答案*/
            mEntry.setAnswers(mPostDismis);//答案

            /* 添加其他选项 */
            mEntry.setBjcSignature("data:image/png;base64,"+new String(mByteString_bjc)); //获取被检查人的图片字符串
            mEntry.setJcBeizhu(m_jc_zgnr_text); //备注
            mEntry.setJcProjectid(1); //
            mEntry.setJcResult(1); // 注释
            mEntry.setJcSignature("data:image/png;base64,"+new String(mByteString_jc)); //获取检查人的图片字符串
            mEntry.setJcTime(new Date());
            mEntry.setJcUnitid(Integer.valueOf(id));
            mEntry.setTiaolinum(setTiaolinum);
            mEntry.setYinhuan(m_jc_zgnr_text);
            mEntry.setZgendtime(new Date());
            mEntry.setZgmethod(m_jc_cqcs_set);


            /* 查看图片是否回调成功 */
            Log.e("TAG","被检查 >>>>" + mByteString_bjc.toString());
            Log.e("TAG","检查 >>>>" + mByteString_jc.toString());

            /* 转换JSON */
            String mResult=com.alibaba.fastjson.JSON.toJSONString(mEntry);

            Log.e("TAG","上传数据的总长度-->>>"+mResult.length());
            Log.e("TAG","用户输入答案-->>>"+mPostDismis.toString());
            Log.e("TAG","需要上传的数据--->>>原始数据:"+mResult);



            /* 启用子线程 */
            NetworkTools.postHttpToolsUaerRegistite(Configfile.FORM_POST_SERVICE_TABLE_JSON_PATH,
                    mHandler,mResult);


        }else {
            Configfile.Log(this,"请完善所有的答案！");
            Log.e("TAG","当前答案的结果集大小为--"+postList.size()+">>>传递过来的集合大小为--"+mQuestion.size());
            mDialog.dismiss();
        }

    }

    /**
     * @ 解释: 获取APP缓存目录
     *
     * @ 使用技巧:File mFile = getExternalFilesDir(null);
     *          String path = mFile.getAbsoluteFile() + fileName + ".png";
     *          File dir = new File(path);
     *          Log.e("TAG", "存储图片的URL >>> " + path);
     */

    /* AlertDialog 的点击事件 */
    /**
     * @param imageView 用户画好界面的图之后赋值到UI（imageView）
     * @param fileName 当使用Gesture来实现手势绘图的时候 当绘制完毕需要给图片命名
     */
    public void getGesture(final ImageView imageView,final String fileName) {

        View myView = LayoutInflater.from(this).inflate(R.layout.my_gesure_xml, null, false);
        GestureOverlayView gesure = (GestureOverlayView) myView.findViewById(R.id.dialog_gesture);
        gesure.setGestureColor(Color.GREEN);//设置手势的颜色
        gesure.setGestureStrokeWidth(2);//设置手势的画笔粗细
        gesure.setFadeOffset(3000); //手势淡出时间
        gesure.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                //将手势转换成图片
                Bitmap bitmap = gesture.toBitmap(128,128,10,0xffff0000);
                //保存
                setWLocalCache(bitmap,fileName);
            }
        });


        // 创建构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle("提示").setIcon(R.mipmap.icon_app)
                .setMessage("请等待签名缓存完毕并且淡出！如果签名失败请清除缓存")
                .setView(myView)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {// 积极
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bitmap localCache = getLocalCache(LOCAL_CACHE_PATH,fileName);
                        imageView.setImageBitmap(localCache);
                    }
                })
                .setNegativeButton("清除缓存", new DialogInterface.OnClickListener() {// 消极
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        setDeleteFile(LOCAL_CACHE_PATH);
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {// 中间级
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                    }
                });
        builder.create().show();
    }

    // 写本地缓存---SD
    public void setWLocalCache(Bitmap bitmap) {
        File dir = new File(LOCAL_CACHE_PATH);
        FileOutputStream bitmapWtriter=null;
        try {
            bitmapWtriter = new FileOutputStream(dir);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bitmapWtriter);


    }

    // 读本地缓存---SD
    public Bitmap getLocalCache(String url) {
        try {
            File cacheFile = new File(url);

            if (cacheFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));
                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 写本地缓存---APP
    public void setWLocalCache(Bitmap bitmap, String fileName) {
        /* 路径 */
        File mFile = getExternalFilesDir(null);
        String path = mFile.getAbsoluteFile() + fileName + ".png";
        File dir = new File(path);
        Log.e("TAG", "存储图片的URL >>> " + path);
        FileOutputStream bitmapWtriter = null;
        try {
            bitmapWtriter = new FileOutputStream(dir);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bitmapWtriter);
    }

    // 读本地缓存---APP
    public Bitmap getLocalCache(String url, String fileName) {
        try {
            /* 路径 */
            File mFile = getExternalFilesDir(null);
            String path = mFile.getAbsoluteFile() + fileName + ".png";
            Log.e("TAG", "读取图片的URL >>> " + path);

            File cacheFile = new File(path);

            if (cacheFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));
                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //删除单个文件
    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean setDeleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Toast.makeText(this, "清除缓存文件成功", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(this, "清除缓存文件失败", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "清除缓存文件不存在", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    /*-------------------------------------------不需要修改--------------------------------------*/
    // 多个视图的创建
    private View addQuestionView(Question question, final int postion) {

        //加载集合界面
        //查找布局 也就是每一道题的布局
        View view = View.inflate(this, R.layout.activity_chnihealthreport, null);
        //查找布局中的控件
        TextView tes = (TextView) view.findViewById(R.id.tv_question);//
        ListView listview = (ListView) view.findViewById(R.id.lv_question_answer);
        Button button_next = (Button) view.findViewById(R.id.button_next);
        TextView seek_bfb = (TextView) view.findViewById(R.id.seek_bfb);
        ProgressBar mSeekBar = (ProgressBar) view.findViewById(R.id.seek);

        /* 隐藏相应的kong'jian控件 */
        if(postion == mQuestion.size()){
            button_next.setVisibility(View.INVISIBLE);
        }else if(postion == 0){

        }

        /* 获取当前进度 */
        int now = postion;
        int max = mQuestion.size();
        int i = (now*100)/max;
        if(postion == max){
            mSeekBar.setProgress(100);
            seek_bfb.setText("完成"+100+"%");
        }else {
            mSeekBar.setProgress(i);
            seek_bfb.setText("当前进度"+i+"%");
        }

        Log.e("TAG",i+postion+"----"+max+"-----"+i);

        //设置适配器
        adapter = new ChineseMedicineReportAdapter(this,question);
        listview.setAdapter(adapter);
        //设置题目标题
        tes.setText(question.getQuestion());
        /* item点击事件 */
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mViewFlipper.getDisplayedChild() == mQuestion.size()) {
                    mViewFlipper.stopFlipping();
                    return;
                }else {
                    mViewFlipper.setInAnimation(animations[0]);
                    mViewFlipper.setOutAnimation(animations[1]);
                    mViewFlipper.showNext();
                }
            }
        });

        /* 回调集合数据 */
        adapter.setmOnCallBackCheck(new ChineseMedicineReportAdapter.OnCallBackCheck() {
            @Override
            public void OnClinkCheckBool(String s) {
                String valueOf = String.valueOf(postion);
                postList.add(s);
                //Configfile.Log(NotesNewActivity.this,"回调的结果为"+s+"====pstMap集合大小:"+postList.size()+"===当前postion"+postion);
                Log.e("TAG","回调的结果为"+s+"====pstMap集合大小:"+postList.size()+"===当前postion"+postion);
            }
        });

        //点击下一题
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewFlipper.getDisplayedChild() == mQuestion.size()) {
                    mViewFlipper.stopFlipping();
                    return;
                }else {
                    mViewFlipper.setInAnimation(animations[0]);
                    mViewFlipper.setOutAnimation(animations[1]);
                    mViewFlipper.showNext();
                }
            }
        });



        //每一个Item 也就是每一道题的点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(NotesNewActivity.this,position+"",Toast.LENGTH_SHORT).show();
                Toast.makeText(NotesNewActivity.this,position+"",Toast.LENGTH_SHORT).show();
                if (mViewFlipper.getDisplayedChild() == mQuestion.size()) {
                    Toast.makeText(NotesNewActivity.this,"最后一个题",Toast.LENGTH_SHORT).show();
                    mViewFlipper.stopFlipping();
                    return;
                }else {
                    mViewFlipper.setInAnimation(animations[0]);
                    mViewFlipper.setOutAnimation(animations[1]);
                    mViewFlipper.showNext();
                }
            }
        });

        return view;
    }

    //创建数据
    private List<Question> initData(String data) {

        List<Map<String, String>> mList = JSON.GetJson(data, new String[]{"content", "id", "tradeId"});

        List<Question> questions = new ArrayList<>();

        Log.e("TAG",mList.size()+"===");

        if(mList.size() != 0){
           for (int i = 0; i < mList.size(); i++) {
                Map<String, String> stringMap = mList.get(i);
                String content = stringMap.get("content");
                Question q1 = new Question();
                q1.setQuestion(content);
                List<Question.Answer> mA = new ArrayList<>();
                Question.Answer  a1 = new Question.Answer();
                a1.setAnswerMessage("是");
                a1.setAnswerMessage("否");
                mA.add(a1);
                q1.setAnswer(mA);
                questions.add(q1);
            }
        }
        return questions;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e2.getX() - e1.getX()>FLIP_DISTANCE){
            if (mViewFlipper.getDisplayedChild() == 0) {
                mViewFlipper.stopFlipping();
                Toast.makeText(NotesNewActivity.this,"第一个题",Toast.LENGTH_SHORT).show();
                return false;
            } else {
                mViewFlipper.setInAnimation(animations[2]);
                mViewFlipper.setOutAnimation(animations[3]);
                mViewFlipper.showPrevious();
                return  true;
            }
        }else if (e1.getX() - e2.getX()>FLIP_DISTANCE){
            if (mViewFlipper.getDisplayedChild() == mQuestion.size()) {
                mViewFlipper.stopFlipping();
                return false;
            }else {
                mViewFlipper.setInAnimation(animations[0]);
                mViewFlipper.setOutAnimation(animations[1]);
                mViewFlipper.showNext();
                return true;
            }
        }
        return false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将Activity上的触发的事件交个GestureDetector处理
        return this.mGestureDetector.onTouchEvent(event);
    }


}
