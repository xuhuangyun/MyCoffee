package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.fancoff.coffeemaker.service.DurationUtil;
import com.fancoff.coffeemaker.utils.glide.ImageLoadUtils;

/**
 * Created by apple on 2017/10/12.
 */

public class MyButton extends android.support.v7.widget.AppCompatButton {
    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DurationUtil.getIns().onTouchScreen(event);
        return super.onTouchEvent(event);
    }

    public void setImage(int resImage) {
        ImageLoadUtils.getInstance().setbtnImage(this, resImage);
    }
}
