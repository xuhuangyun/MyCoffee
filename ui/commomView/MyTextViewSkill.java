package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.bean.coffe.FontBean;
import com.fancoff.coffeemaker.bean.coffe.SeckillBean;
import com.fancoff.coffeemaker.bean.coffe.TextBean;
import com.fancoff.coffeemaker.service.DurationUtil;
import com.fancoff.coffeemaker.utils.TimeUtil;
import com.fancoff.coffeemaker.utils.glide.ImageLoadUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by apple on 2017/10/12.
 */

public class MyTextViewSkill extends android.support.v7.widget.AppCompatTextView {
    public MyTextViewSkill(Context context) {
        super(context);
        init();
    }

    private void init() {

    }


    public MyTextViewSkill(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DurationUtil.getIns().onTouchScreen(event);
        return super.onTouchEvent(event);
    }

    TextBean textBean;
    CoffeeBean goodsBean;

    public void update(String tag, CoffeeBean goodsBean, TextBean textBean) {
        this.textBean = textBean;
        this.goodsBean = goodsBean;
        updateBindeView();
        if (goodsBean != null) {
            TimeUtil.getInstance().addTimerschedule(tag + "", new TimeUtil.RunCallBack() {
                @Override
                public void runHander() {
                    updateBindeView();
                }
            });
        } else {
            TimeUtil.getInstance().removeTimerschedule(tag + "");
        }
    }

    public void updateSkill(TextBean textBean, SeckillBean seckillBean) {
        String value = textBean.getValue();
        String totalvaulearr[] = value.split("\\[0\\]");
        String vaule1 = totalvaulearr[0];
        String vaule2 = seckillBean.getShowLast();
        String vaule3 = totalvaulearr[1].split("\\[1\\]")[0];
        String vaule4 = seckillBean.getTotal() + "";
        String vaule5 = totalvaulearr[1].split("\\[1\\]")[1];
        int size1 = vaule1.length();
        int size2 = size1 + vaule2.length();
        int size3 = size2 + vaule3.length();
        int size4 = size3 + vaule4.length();
        int size5 = size4 + vaule5.length();
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        sBuilder = addsu(sBuilder, 0, size1, vaule1, textBean.getFont());
        sBuilder = addsu(sBuilder, size1, size2, vaule2, textBean.getFont0());
        sBuilder = addsu(sBuilder, size2, size3, vaule3, textBean.getFont());
        sBuilder = addsu(sBuilder, size3, size4, vaule4, textBean.getFont1());
        sBuilder = addsu(sBuilder, size4, size5, vaule5, textBean.getFont());
        setText(sBuilder, BufferType.SPANNABLE);
        setX(textBean.getPosition().getLeft());
        setY(textBean.getPosition().getTop());
    }

    private void updateBindeView() {
        if (goodsBean != null) {
            if (goodsBean.getStatus() == 1) {//1正常
                img_type.setVisibility(View.GONE);
            } else if (goodsBean.getStatus() == 2) {//热卖
                img_type.setVisibility(View.VISIBLE);
                img_type.update(DataCenter.getInstance().getHot());
            } else {
                img_type.setVisibility(View.VISIBLE);
                img_type.update(DataCenter.getInstance().getSellOut());
            }
            SeckillBean seckillBean = goodsBean.isSkilling();
            if (seckillBean != null) {
                if (goodsBean.getStatus() == 1) {//1正常
                    img_skill.update(DataCenter.getInstance().getSeckill());
                    updateSkill(this.textBean, seckillBean);
                } else if (goodsBean.getStatus() == 2) {//热卖
                    img_type.setVisibility(View.GONE);
                    img_skill.update(DataCenter.getInstance().getSeckill());
                    updateSkill(this.textBean, seckillBean);
                } else {
                    ImageLoadUtils.getInstance().loadNetImage(img_skill, "");
                    setText("");
                }

                t_xian.updatePrice("￥", goodsBean.getPrice(), seckillBean);
                t_xian2.updatePriceFractional(goodsBean.getPrice(), seckillBean);
            } else {
                setText("");
                ImageLoadUtils.getInstance().loadNetImage(img_skill, "");
                t_xian.updatePrice("￥", goodsBean.getPrice(), null);
                t_xian2.updatePriceFractional(goodsBean.getPrice(), null);
            }

        } else {
            img_type.setVisibility(View.GONE);
            ImageLoadUtils.getInstance().loadNetImage(img_type, "");
            ImageLoadUtils.getInstance().loadNetImage(img_skill, "");
            setText("");
        }
    }


    private SpannableStringBuilder addsu(SpannableStringBuilder sBuilder, int start, int end, String content, FontBean font) {
        sBuilder.append(content);

        CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(
                TypefaceUtils.load(MyApp.getIns().getAssets(), "fonts/" + font.getFile()));
        sBuilder.setSpan(typefaceSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (font.getWeight().equals("bold")) {
            StyleSpan span = new StyleSpan(Typeface.BOLD);
            sBuilder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (font.getWeight().equals("italic")) {
            StyleSpan span = new StyleSpan(Typeface.ITALIC);
            sBuilder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (font.getWeight().equals("normal")) {
            StyleSpan span = new StyleSpan(Typeface.NORMAL);
            sBuilder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        sBuilder.setSpan(new AbsoluteSizeSpan(font.getSize()), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sBuilder.setSpan(new ForegroundColorSpan(Color.parseColor(font.getColor())), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sBuilder;
    }

    MyImageView img_skill, img_type;
    MyTextView t_xian, t_xian2;

    public void bind(MyImageView img_skill, MyImageView img_type, MyTextView t_xian, MyTextView t_xian2) {
        this.img_skill = img_skill;
        this.img_type = img_type;
        this.t_xian = t_xian;
        this.t_xian2 = t_xian2;
    }


}
