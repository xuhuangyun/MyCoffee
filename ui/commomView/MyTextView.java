package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.TextView;

import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.bean.coffe.FontBean;
import com.fancoff.coffeemaker.bean.coffe.SeckillBean;
import com.fancoff.coffeemaker.bean.coffe.TextBean;
import com.fancoff.coffeemaker.service.DurationUtil;
import com.fancoff.coffeemaker.utils.glide.ImageLoadUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by apple on 2017/10/12.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView{
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DurationUtil.getIns().onTouchScreen(event);
        return super.onTouchEvent(event);
    }
    TextBean textBean;
    public void update(TextBean textBean){
        update("",textBean);
    }
    public void update(String title,TextBean textBean){
        update(title,textBean,false);
    }

    /**

     */
    public void updatePrice(String title,TextBean textBean,SeckillBean seckillBean){

        this.textBean=textBean;
        String alls="";
        if(seckillBean!=null&&seckillBean.getPrice()!=null){//
             alls=title+seckillBean.getPrice().getValue();  //
        }else{//
            alls=title+textBean.getValue();//title+text.value
        }

        String []shows=alls.split("\\.");
        SpannableStringBuilder sBuilder =new SpannableStringBuilder();


        sBuilder=addsu(sBuilder,0,shows[0].length(),shows[0],textBean.getFractional_font());
        //

        setText(sBuilder, TextView.BufferType.SPANNABLE); //
        setTextSize(TypedValue.COMPLEX_UNIT_PX,textBean.getFont().getSize());//
        setTextColor(Color.parseColor(textBean.getFont().getColor()));//

        if(seckillBean!=null&&seckillBean.getPrice()!=null){//
            if(seckillBean.getPrice().getPosition()==null){
                seckillBean.getPrice().setPosition(textBean.getPosition());
                //
            }
            setX(seckillBean.getPrice().getPosition().getLeft());
            setY(seckillBean.getPrice().getPosition().getTop());
        }else{
            setX(textBean.getPosition().getLeft());
            setY(textBean.getPosition().getTop());
        }

    }

    /**

     */
    public void updatePriceFractional(TextBean textBean,SeckillBean seckillBean) {
        this.textBean=textBean;
        String alls="";
        if(seckillBean!=null&&seckillBean.getPrice()!=null){
            alls=seckillBean.getPrice().getValue();
        }else{
            alls=textBean.getValue();
        }

        String []shows=alls.split("\\.");
        SpannableStringBuilder sBuilder =new SpannableStringBuilder();
        sBuilder=addsu(sBuilder,0,shows[1].length()+1,"."+shows[1],textBean.getFractional_font());

        setText(sBuilder, TextView.BufferType.SPANNABLE);
        setTextSize(TypedValue.COMPLEX_UNIT_PX,textBean.getFractional_font().getSize());
        setTextColor(Color.parseColor(textBean.getFractional_font().getColor()));

        if(seckillBean!=null&&seckillBean.getPrice()!=null){
            if(seckillBean.getPrice().getFractional_position()==null){
                seckillBean.getPrice().setFractional_position(textBean.getFractional_position());
            }
            setX(seckillBean.getPrice().getFractional_position().getLeft());
            setY(seckillBean.getPrice().getFractional_position().getTop());
        }else{
            setX(textBean.getFractional_position().getLeft());
            setY(textBean.getFractional_position().getTop());
        }
    }

    /**

     */
    public void update(String title,TextBean textBean,boolean spike){
        this.textBean=textBean;
        int size=title.length()+textBean.getValue().length();
        SpannableStringBuilder sBuilder =new SpannableStringBuilder();
        sBuilder=addsu(sBuilder,0,size,title+textBean.getValue(),textBean.getFont());
        //
        if(spike){
            StrikethroughSpan span = new StrikethroughSpan();  //
            sBuilder.setSpan(span, 0, size, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        setText(sBuilder, TextView.BufferType.SPANNABLE);
        setTextSize(TypedValue.COMPLEX_UNIT_PX,textBean.getFont().getSize());
        setTextColor(Color.parseColor(textBean.getFont().getColor()));
        setX(textBean.getPosition().getLeft());
        setY(textBean.getPosition().getTop());
    }

    /**

     */
    private SpannableStringBuilder addsu(SpannableStringBuilder sBuilder, int start,int end,String content,FontBean font) {
        sBuilder.append(content);

        //
        CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(
                TypefaceUtils.load(MyApp.getIns().getAssets(), "fonts/"+font.getFile()));


        sBuilder.setSpan(typefaceSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        /**

         */
        if(font.getWeight().equals("bold")){

            StyleSpan span = new StyleSpan(Typeface.BOLD);
            sBuilder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else if(font.getWeight().equals("italic")){//
            StyleSpan span = new StyleSpan(Typeface.ITALIC);
            sBuilder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else if(font.getWeight().equals("normal")){//
            StyleSpan span = new StyleSpan(Typeface.NORMAL);
            sBuilder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sBuilder;
    }


    public void setImage(int resImage) {
        ImageLoadUtils.getInstance().setTextViewImage(this, resImage);
    }
}
