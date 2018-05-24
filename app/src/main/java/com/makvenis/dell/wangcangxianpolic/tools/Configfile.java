package com.makvenis.dell.wangcangxianpolic.tools;


import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.makvenis.dell.wangcangxianpolic.startActivity.NotiflyActivity;

/**
 * 配置文件
 */
public class Configfile {

    /* 服务器前端地址 */
    public static final String SERVICE_WEB="http://ssdaixiner.oicp.net:26168/wcjw/mobile/";
    public static final String SERVICE_WEB_IMG="http://ssdaixiner.oicp.net:26168/wcjw/";

    /*服务器地址*/
    public static final String REGISTE_URL="http://ssdaixiner.oicp.net:26168/wcjw/mobile/getloginInfo";
    /*是否启用广告界面*/
    public static final boolean IS_ADV = false;

    /* 广告默认下载地址 也作为测试地址 */
    public static final String IMAGERVIEW_PATH = "http://pic1.win4000.com/mobile/d/59bccd99693d0.png";

    /* 页面Log */
    public static void Log(Context context, String mLog){
        Toast.makeText(context,mLog,Toast.LENGTH_SHORT).show();
    }

    /* APP手机推送地址 请求的服务地址 */
    //http://ssdaixiner.oicp.net:26168/wcjw/mobile/news/msglist?msgname=ssdai
    public static String MESSAGE_PATH="http://ssdaixiner.oicp.net:26168/wcjw/mobile/news/msglist?msgname=";

    /* 文件上传地址 */
    public static final String UPLOAD_FILE_PATH="http://ssdaixiner.oicp.net:26168/wcjw/static/doUploadFileMuti";

    /* 用户更新数据库中用户头像请求地址 */
    public static final String UPDATE_USER_POTO="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toUpdatePersonPhoto?";

    /* 新闻页面的详情页面 */
    /**
     * @解释 新闻页面的详情页面 供WebView使用
     * {@link NotiflyActivity}
     */
    public static final String NEWS_ALL_CONTENT_PATH="http://ssdaixiner.oicp.net:26168/wcjw/mobile/news/newsDetail2?id=";

    /* 当图片不存在的时候加载贲张图片 */
    public static final String IMAGE_NO="http://ssdaixiner.oicp.net:26168/wcjw/resources/images/nopic2.png";

    /* 超时提醒 */
    public static void OverTimerLog(final Context context, final String log){
        /* 获取当前时间 */
        CountDownTimer countDownTimer=new CountDownTimer(30*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Configfile.Log(context,log);
            }
        };
    }

    /* 用户新闻列表的请求地址 自动拼接页码标签 */
    public static final String NEWS_PATH="http://ssdaixiner.oicp.net:26168/wcjw/mobile/news/newslist?classid=114&pageNow=";
    /* 用户信息数据库键 */
    public static final String USER_DATA_KEY="USER_SQL";

    /* 表单请求地址 */
    public static final String FORM_POST="http://ssdaixiner.oicp.net:26168/wcjw/mobile/getDataforQuest";

    /* 单位列表信息页面 请求的接口地址 */
    public static final String COMPANY_URL="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toshowdanweis";
    /* 单位列表信息页面 请求的接口地址 */
    public static final String COMPANY_URL_SEARCH="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toshowdanweiByName?name=";

    /* 以下是四张请求表格的数据的JSON地址 */
    public static final String FORM_GET_TABLE_1="http://ssdaixiner.oicp.net:26168/wcjw/mobile/getDataforQuest?type=1";  //旅店安全检查json
    public static final String FORM_GET_TABLE_2="http://ssdaixiner.oicp.net:26168/wcjw/mobile/getDataforQuest?type=2";  //银行业金融机构治安保卫工作检查登记表
    public static final String FORM_GET_TABLE_3="http://ssdaixiner.oicp.net:26168/wcjw/mobile/getDataforQuest?type=3";  //寄递物流业安全检查登记表
    public static final String FORM_GET_TABLE_4="http://ssdaixiner.oicp.net:26168/wcjw/mobile/getDataforQuest?type=4";  //校园治安保卫工作检查登记表

    /* 以下是数据库存储四张JSON的key键 也是取出数据的条件 */
    public static final String FORM_SQL_DATABASE_1="FORM_SQL_DATABASE_1"; //旅店安全检查 存储json
    public static final String FORM_SQL_DATABASE_2="FORM_SQL_DATABASE_2"; //银行业金融机构治安保卫工作检查登记表 存储json
    public static final String FORM_SQL_DATABASE_3="FORM_SQL_DATABASE_3"; //寄递物流业安全检查登记表 存储json
    public static final String FORM_SQL_DATABASE_4="FORM_SQL_DATABASE_4"; //校园治安保卫工作检查登记表 存储json

    /* 以下是每一张json对应的html结果网页地址 只是在WebViewActivity中使用 */
    public static final String RESULT_HTML_TYPE_1="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toshowDengji?type=1";
    public static final String RESULT_HTML_TYPE_2="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toshowDengji2?type=2";
    public static final String RESULT_HTML_TYPE_3="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toshowDengji2?type=3";
    public static final String RESULT_HTML_TYPE_4="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toshowDengji2?type=4";

    /***
     * @ 解释: 当使用检查项目的提交时候分别提交的地址
     * @ 键:dataJson
     * @ 提交方式 post
     * @ 实用 type=(1,2,3,4)
     */
    public static final String FORM_POST_SERVICE_TABLE_JSON_PATH="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toSaveDengjiData";

    /* 当Post请求的时候 回调的数据 msg的值 */
    public static final int CALLBANK_POST_MSG=0X000005;

    /* 责令改正POST数据提交地址 */
    public static final String OVER_POST_CORECT="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toSaveGaiZheng";
    /* 处罚决定书POST数据提交地址 */
    public static final String OVER_POST_CORECT_CHUFA="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toSaveChuFa";
    /* 治安隐患POST数据提交地址 */
    public static final String OVER_POST_CORECT_YINHUAN="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toSaveYanQiZhengGai";

    /* 存储同意延期整改治安隐患通知书 */
    public static final String OVER_POST_CORECT_TONGYI="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toSaveYesYanQiZhengGai";
    /* 存储不同意延期整改治安隐患通知书  */
    public static final String OVER_POST_CORECT_NOTONGYI="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toSaveNoYanQiZhengGai";
    /* 存储检查笔录 */
    public static final String OVER_POST_CORECT_BILU="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toSaveBiLu";
    /* 存储复查登记表 */
    public static final String OVER_POST_CORECT_FUCHA="http://ssdaixiner.oicp.net:26168/wcjw/mobile/toSavefucha";
























    /*app名称*/
    public static final String APP_NAME="kkk/";

    /*数据库连接地址*/
    public static final String DATA_URL=".././webapps/aaa/WEB-INF/yyjs.db";
    /*是否允许相对路径*/
    public static final boolean RELATIVE=true;
    /*设置图片最大缓存*/
    public static final double IMAGE_MAX=400.00;
    /*默认接受下载位置路径*/
    public static final String DEFAULT_PATH="mnt/sdcard/"+APP_NAME+"_cache/";
    /*是否向服务器发送操作码*/
    //public static final int P_CODE=0>1?0:1;
    /*是否获取用户登陆IP地址，并上传*/
    public static final boolean USER_IP=false;
    /*条件符*/
    public static final String SAL="/";
    /*网络请求GET值*/
    public static final int GET_CODE=1;
    /*网络请求下载值*/
    public static final int DOWN_CODE=2;
    /*网络用户账号密码返回值*/
    public static final int USER_PASS=3;
    /*操作涵义 成功*/
    public static boolean ACTION_SUCCESS=true;
    /*操作涵义 成功*/
    public static boolean ACTION_DEFEAT=false;




}
