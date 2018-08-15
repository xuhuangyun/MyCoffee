package com.fancoff.coffeemaker.utils;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/*
    截图类
 */
public class ScreenShot {
    public static String getBase64Bitmap(Activity activity) {
        return bitmapToBase64(takeScreenShot(activity));

    }

    //截图；返回Bitmap
    private static Bitmap takeScreenShot(Activity activity) {

        // View是你需要截图的View

        View view = activity.getWindow().getDecorView();
        //getWindow():检索活动的当前窗口；返回的是windows
        //getDecorView():返回最上层窗口装饰视图；返回的是view

        view.setDrawingCacheEnabled(true);
        //启用或禁用绘图缓存。启用绘图缓存时，对getDrawingCache()或buildDrawingCache()的下一个调用将在位图中绘制视图。

        view.buildDrawingCache();//启用DrawingCache并创建位图

        Bitmap b1 = view.getDrawingCache();//创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收

        // 获取屏幕长和高

        int width = 1080;
        int height = 1920;

        // 去掉标题栏

        Bitmap b = Bitmap.createBitmap(b1, 0, 0, width, height);

        view.destroyDrawingCache();
        //释放绘图缓存使用的资源。
        //如果您手动调用buildDrawingCache()而不调用setDrawingCacheEnabled(true)，那么您应该在以后使用这个方法来清理缓存。

        return b;

    }

    //将图片b压缩到filePath中
    private static void savePic(Bitmap b, File filePath) {

        FileOutputStream fos = null;

        try {

            fos = new FileOutputStream(filePath);

            if (null != fos) {

                b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                //将位图的压缩版本写入指定的outputstream:fos

                fos.flush();

                fos.close();

            }

        } catch (FileNotFoundException e) {

            // e.printStackTrace();

        } catch (IOException e) {

            // e.printStackTrace();

        }

    }

    /**
     * 1、截图；
     * 2、压缩图像；
     * 3、转换为base64；
     * @param a
     * @param filePath
     * @return  返回base64格式的字符串数组
     */
    public static String shoot(Activity a, File filePath) {

        if (filePath == null) {
            return "";

        }
        if (!filePath.getParentFile().exists()) {//父目录不存在创建父目录

            filePath.getParentFile().mkdirs();

        }
        Bitmap b = ScreenShot.takeScreenShot(a);  //获得截图Bitmap
        ScreenShot.savePic(b, filePath);//将图像b压缩到filePath中
        String s = bitmapToBase64(b);//转换为base64
        if (b != null) {
            b.recycle();
            b = null;
        }
        return s;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    private static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;   //字节数组输出流
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();//以字节数组的形式返回ByteArrayOutputStream的内容。
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
                //public static String encodeToString (byte[] input, int offset, int len, int flags)
                //得到的数组数据转换为Base64码；返回一个新的string字符串
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}