package com.fancoff.coffeemaker.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * Created by apple on 2017/5/4.
 * json解析类
 */

public class GsonUtil {
    static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.addSerializationExclusionStrategy(new ExclusionStrategy() {   //序列化的时候过滤
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    final Expose expose = f.getAnnotation(Expose.class);
                    return expose != null && !expose.serialize();
                }

                @Override
                public boolean shouldSkipClass(Class<?> arg0) {
                    return false;
                }
            }).addDeserializationExclusionStrategy(new ExclusionStrategy() {//反序列的时候过滤
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    final Expose expose = f.getAnnotation(Expose.class);
                    return expose != null && !expose.deserialize();
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            });
            gson = builder.create();
        }
        return gson;
    }
}
