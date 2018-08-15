package com.fancoff.coffeemaker.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fancoff.coffeemaker.Application.FilesManage;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.utils.MediaFile;
import com.fancoff.coffeemaker.utils.StringUtil;
import com.fancoff.coffeemaker.utils.ToastUtil;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by apple on 2017/5/10.
 */

public class ImageLoadUtils {
    private static final ImageLoadUtils ourInstance = new ImageLoadUtils();

    public static ImageLoadUtils getInstance() {
        return ourInstance;
    }

    /**
     * 加载网络图片url到imageView中；
     */
    public void loadNetImage(ImageView imageView, String url) {
        if (url == null) {
            url = "";
        }
        if (MediaFile.isVideoFileType(url)) {
		//Glide加载一张网络静态图片最基本的用法
            GlideApp
                    .with(MyApp.getIns())
                    .load("")
                    .into(imageView);
        } else {
            GlideApp
                    .with(MyApp.getIns())
                    .load(url)//需要加载的图片，大多数情况下就是网络图片的链接。
                    .into(imageView);//用来展现图片的ImageView.
        }


    }

    /**
     * url空返回；
     */
    public void preloadNetImage(final String url) {

        if (StringUtil.isStringEmpty(url)) {
            return;
        }
        GlideApp
                .with(MyApp.getIns())
                .load(url)//需要加载的图片，大多数情况下就是网络图片的链接。
                .submit();//如果我们只想下载图片，不加载到view上，可以如下。submit()是线程阻塞的，需要在子线程中完成


    }

    public void clear() {
        GlideApp.get(MyApp.getIns()).clearDiskCache();

    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public void release() {
        for (Map.Entry<Integer, BimapDr> entry : drawableHashMap.entrySet()) {
            BimapDr b = entry.getValue();
            if (b != null) {
                b.release();
            }
        }
    }

    /**Bitmap、Drawable类*/
    class BimapDr {
        Bitmap bmp;
        Drawable drawable;

        public BimapDr() {
        }

        /**获得bmp*/
        public Bitmap getBmp() {
            return bmp;
        }

        /**设置bmp和drawable*/
        public void setBmp(Bitmap bmp) {
            this.bmp = bmp;
            this.drawable = new BitmapDrawable(MyApp.getIns().getResources(), bmp);
        }

        public Drawable getDrawable() {
            return drawable;
        }


        public void release() {
            if (bmp != null && !bmp.isRecycled()) {
                bmp.recycle();
                bmp = null;
            }
            drawable = null;

        }

        public boolean isNull() {
            return bmp == null || bmp.isRecycled();
        }
    }

    HashMap<Integer, BimapDr> drawableHashMap = new HashMap<>();


    /**
     * 没有res键，则从资源文件中读取该键的bitmap，然后存入drawableHashMap中；
     * imageView设置为drawableHashMap中res键对应的值的drawable
     */
    public void setLayoutImage(ViewGroup imageView, int res) {
        BimapDr dra = drawableHashMap.get(res);
        if (dra == null || dra.isNull()) {
            dra = new BimapDr();
            dra.setBmp(BitmapFactory.decodeResource(MyApp.getIns().getResources(), res));
            //获得资源文件中，res(id)对应的bitmap
            drawableHashMap.put(res, dra);
        }
        imageView.setBackground(dra.getDrawable());
    }

    public void setTextViewImage(TextView imageView, int res) {
        BimapDr dra = drawableHashMap.get(res);
        if (dra == null || dra.isNull()) {
            dra = new BimapDr();
            dra.setBmp(BitmapFactory.decodeResource(MyApp.getIns().getResources(), res));
            drawableHashMap.put(res, dra);
        }
        imageView.setBackground(dra.getDrawable());
    }

    /**
     * 没有res键，则从资源文件中读取该键的bitmap，然后存入drawableHashMap中；
     * imageView设置为drawableHashMap中res键对应的值的drawable
     */
    public void setbtnImage(Button imageView, int res) {
        BimapDr dra = drawableHashMap.get(res);
        if (dra == null || dra.isNull()) {
            dra = new BimapDr();
            dra.setBmp(BitmapFactory.decodeResource(MyApp.getIns().getResources(), res));
            drawableHashMap.put(res, dra);
        }
        imageView.setBackground(dra.getDrawable());
    }

    /**
     * 得到键为res的值dra；
     * dra为空，新建BimapDr对象，设置id为res的位图为bmp和drawable，并添加到hashmap中
     * imageView传入bmp；
     */
    public void setImage(ImageView imageView, int res) {

        BimapDr dra = drawableHashMap.get(res);
        if (dra == null || dra.isNull()) {
            dra = new BimapDr();
            dra.setBmp(BitmapFactory.decodeResource(MyApp.getIns().getResources(), res));
            drawableHashMap.put(res, dra);
        }
        imageView.setImageBitmap(dra.getBmp());

    }
}
