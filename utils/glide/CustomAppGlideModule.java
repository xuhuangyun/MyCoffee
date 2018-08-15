package com.fancoff.coffeemaker.utils.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.fancoff.coffeemaker.Application.FilesManage;
import com.fancoff.coffeemaker.net.LoggingInterceptor;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


@GlideModule
public final class CustomAppGlideModule extends AppGlideModule {
    OkHttpClient client;
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
    //手动设置超时时间
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
         client = httpBuilder
                .addInterceptor(new LoggingInterceptor())
                .readTimeout(10000, TimeUnit.SECONDS)
                .connectTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS) //设置超时
                .build();
        int diskCacheSizeBytes = 1024 * 1024 * 200; // 200 MB
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取系统分配给应用的总内存大小
        int memoryCacheSize = maxMemory / 8;//设置图片内存缓存占用八分之一
        //设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        builder.setDiskCache(
                new DiskLruCacheFactory(FilesManage.dri.image, diskCacheSizeBytes)
        );
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client);
        registry.replace(GlideUrl.class, InputStream.class,factory);
    }
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}