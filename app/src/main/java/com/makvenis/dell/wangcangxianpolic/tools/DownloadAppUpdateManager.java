package com.makvenis.dell.wangcangxianpolic.tools;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * @解释  此类用于当App更新的时候调用 此类包括后台Service下载 封装
 *       广播发送请求 接收之后UI界面交互NotifyCation
 * {@link DownloadAppUpdateManager}
 */

public class DownloadAppUpdateManager {


    public static final String TAG = "DownloadAppUpdateManager";


    /* 承接 */
    private Context mContext;

    /* 预备请求地址 */
    /**
     * @ 服务器地址
     */
    private String mServicePath;

    /* 文件标题名称 */
    /**
     * @ 当使用 mRequest.setTitle(""); 的属性时候 需要传递当前版本的更新的NotifyCation的标题名称
     */
    private String mTitleMessage;

    public DownloadAppUpdateManager(Context mContext, String mServicePath,String mTitleMessage) {
        this.mContext = mContext;
        this.mServicePath = mServicePath;
        this.mTitleMessage = mTitleMessage;
    }

    //下载器
    private DownloadManager mDownloadManager;
    //通过ID来控制
    public long downloadId;

    /**
     * user with 修改版本号
     * @ android {
     * useLibrary 'org.apache.http.legacy'
     * compileSdkVersion 25
     * buildToolsVersion "26.0.2"
     * defaultConfig {
     * applicationId "com.makvenis.dell.wangcangxianpolic"
     * minSdkVersion 23
     * targetSdkVersion 25
     * versionCode 1
     * versionName "1.0"
     *
     *
     */

    /**
     * 查看当前App的版本号
     * 当当前版本小于服务器预置（即 服务器数据库的code版本设置为当前暂未发布的最新版本号）
     * 在gradle里面的版本号 == 服务器数据库的相对应版本号
     *
     * */
    /* 当前版本号 */
    public int getAppVersionCode() {
        try {
            // ---get the package info---
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            //versionName = pi.versionName;
            int versionCode = pi.versionCode;
            if(versionCode != 0){
                return  versionCode;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
        return 0;
    }



    public void post(){

        /**
         * {@link #installApk23 } 详见此方法使用
         * @ 检查传递的地址信息
         */
        Log.e(TAG," >>> App更新地址 "+ mServicePath);

        //创建下载任务
        DownloadManager.Request mRequest=new DownloadManager.Request(Uri.parse(mServicePath));
        //移动网络情况下是否允许漫游
        mRequest.setAllowedOverRoaming(false);

        /* 默认显示 */
        mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        /* 设置标题以及内容 */
        mRequest.setTitle(mTitleMessage);
        mRequest.setDescription("Apk Downloading");
        /* 代之NotifyCation的下载界面是否显示 */
        mRequest.setVisibleInDownloadsUi(true);


        //构建下载的存储地址
        mRequest.setDestinationInExternalPublicDir(Environment.getDownloadCacheDirectory().getAbsolutePath(),mServicePath);
        //获取DownloadManager
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        /* 返回的reference变量是系统为当前的下载请求分配的一个唯一的ID，我们可以通过这个ID重新获得这个下载任务，进行一些自己想要进行的操        *  作或者查询
        */
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        downloadId = mDownloadManager.enqueue(mRequest);

        //注册广播接收者，监听下载状态
        /* 动态注册 */
        mContext.registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    //广播监听下载的各个状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    //检查下载状态
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor c = mDownloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                //下载完成安装APK
                    installAPK();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    //Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"下载失败");
                    break;
            }
        }
        c.close();
    }

    /* Android版本 Build.VERSION.SDK_INT < 23 自动安装 */
    private void installAPK() {
        /* 获取文件下载的地址 */
        Uri uri = mDownloadManager.getUriForDownloadedFile(downloadId);
        //针对API < 6.0
        Log.e(TAG," >>> 当前版本" + Build.VERSION.SDK_INT+"");
        if (uri != null) {
            if(Build.VERSION.SDK_INT >= 23){
                installApk23(mContext,downloadId);
            }else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                mContext.unregisterReceiver(receiver);
            }
        }

    }

    /* Android版本高于23 自动安装*/
    private  void installApk23(Context context, long downloadApkId) {
        /**
         *
         * @在大多数涉及到下载的情况中使用Download Manager都是不错的选择，特别是当用户切换不同的应用以后下载需要在后台继续进行，以及当         * 下载任务顺利完 成非常重要的情况
         * {@link #post } 详见此方法使用
         *
         */
        /* 使用Download Manager，使用getSystemService方法请求系统的DOWNLOAD_SERVICE服务 */
        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadApkId);
        Cursor c = dManager.query(query);
        if(c != null) {
            if (c.moveToFirst()) {
                int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                    String downloadFileUrl = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    startInstall(context, Uri.parse(downloadFileUrl));
                }
            }
            c.close();
        }
    }
    /* Android版本 Build.VERSION.SDK_INT >= 23 */
    private boolean startInstall(Context context, Uri uri) {
        if(!new File( uri.getPath()).exists()) {
            System.out.println( " local file has been deleted! ");
            return false;
        }
        Intent intent = new Intent();
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction( Intent.ACTION_VIEW);
        intent.setDataAndType( uri, "application/vnd.android.package-archive");
        context.startActivity( intent);
        return true;
    }


}
