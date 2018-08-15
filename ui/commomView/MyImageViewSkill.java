package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.fancoff.coffeemaker.bean.coffe.ImageBean;
import com.fancoff.coffeemaker.service.DurationUtil;
import com.fancoff.coffeemaker.utils.glide.ImageLoadUtils;

/**
 * Created by apple on 2017/10/12.
 */

public class MyImageViewSkill extends android.support.v7.widget.AppCompatImageView {
    public MyImageViewSkill(Context context) {
        super(context);
    }

    public MyImageViewSkill(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DurationUtil.getIns().onTouchScreen(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        DurationUtil.getIns().onTouchScreen(event);
        return super.dispatchTouchEvent(event);
    }

    ImageBean imageBean;

    public void update(ImageBean imageBean) {

        this.imageBean = imageBean;
        ImageLoadUtils.getInstance().loadNetImage(this, imageBean.getUrl());


    }

}
