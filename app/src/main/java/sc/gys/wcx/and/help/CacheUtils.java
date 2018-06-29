package sc.gys.wcx.and.help;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * APP 缓存处理类
 * @ 解释: 包括 图片的环迅一级读取
 */

public class CacheUtils {


    // 读本地缓存---SD
    public static Bitmap getLocalCache(String url) {
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
    public static void setWLocalCache(Bitmap bitmap, String fileName,Context context) {
        /* 路径 */
        File mFile = context.getExternalFilesDir(null);
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
    public static Bitmap getLocalCache(String fileName,Context context) {
        try {
            /* 路径 */
            File mFile = context.getExternalFilesDir(null);
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
    public static boolean setDeleteFile(String fileName, Context context) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Toast.makeText(context, "清除缓存文件成功", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(context, "清除缓存文件失败", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(context, "清除缓存文件不存在", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //public static String fileName = "C:\\Documents and Settings\\Administrator\\桌面\\monkeytalk";
    // 递归方式 计算文件的大小
    public static long getFileOffSize(final File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getFileOffSize(child);
        return total;
    }
}
