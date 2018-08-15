package com.fancoff.coffeemaker.ui.pickup;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.MessageBean;
import com.fancoff.coffeemaker.bean.PayBean;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.CalView;
import com.fancoff.coffeemaker.ui.commomView.MyButton;
import com.fancoff.coffeemaker.ui.commomView.MyImageView;
import com.fancoff.coffeemaker.ui.commomView.MyTextView;
import com.fancoff.coffeemaker.utils.SizeUtil;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;
/**
 * 取货界面
 */
public class PickPageFragment extends BaseFragment implements PickPageContract.IPickPageView {

    MyButton btn_cancle;
    MyImageView img_code;
    CalView calView;
    MyTextView t_title,t_content;
    private PickPagePresent mainPresent;

    public PickPageFragment() {
    }

    public static PickPageFragment newInstance(Bundle args) {
        PickPageFragment fragment = new PickPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static PickPageFragment newInstance() {
        return newInstance(null);
    }

    /** 新建P层实例mainPresent */
    @Override
    public void initData() {

        mainPresent = new PickPagePresent(this);
    }

    @Override
    public boolean pauseMainPage() {
        return false;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_pick;
    }

    @Override
    public void initView(View v) {
        t_title=(MyTextView) v.findViewById(R.id.t_title);
        t_title.setPadding(0,SizeUtil.heightSize(50),0,0);
        t_content=(MyTextView) v.findViewById(R.id.t_content);
        t_content.setPadding(0,SizeUtil.heightSize(50),0,0);
        btn_cancle=(MyButton)v.findViewById(R.id.btn_cancle);
        RxView.clicks(btn_cancle).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                onBack();

            }
        });
        img_code= (MyImageView) v.findViewById(R.id.img_code);
        calView= (CalView) v.findViewById(R.id.calView);
        calView.setCallBack(new CalView.CallBack() {
            @Override
            public void onOk(String code) {
               mainPresent.requestData(code);
            }
        });
        calView.setLayoutParams(new LinearLayout.LayoutParams(SizeUtil.screenWidth()/5*4,LinearLayout.LayoutParams.WRAP_CONTENT));
        t_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,SizeUtil.getNomalSize());
        t_content.setTextSize(TypedValue.COMPLEX_UNIT_PX,SizeUtil.getNomalSize());
        btn_cancle.setTextSize(TypedValue.COMPLEX_UNIT_PX,SizeUtil.getNomalSize());
//        Observable.create(new ObservableOnSubscribe<Bitmap>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
//                e.onNext(xZingUtil.encodeAsBitmap("12345678"));
//            }
//        }).compose(this.<Bitmap>bindToLifecycle()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Bitmap>() {
//            @Override
//            public void accept(Bitmap bmp) throws Exception {
////                ImageLoadUtils.getInstance().loadBitmap(img_code,bmp);
//                img_code.setImageBitmap(bmp);
//            }
//        });

    }



    /**ACTION_INTENT,INTENT_TO_PICK_MAKING到MAinActivity*/
    @Override
    public String gotoMaking(PayBean payBean) {
        MessageBean msg=new MessageBean(MessageBean.ACTION_INTENT, MyConstant.ACTION.INTENT_TO_PICK_MAKING);
        msg.setObj(payBean);
        sendMsgToActivity(msg);
        return null;
    }

}
