package com.fancoff.coffeemaker.ui.pay;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.MessageBean;
import com.fancoff.coffeemaker.bean.PayBean;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.MyButton;
import com.fancoff.coffeemaker.ui.commomView.MyImageView;
import com.fancoff.coffeemaker.ui.commomView.MyLinearLayout;
import com.fancoff.coffeemaker.ui.commomView.MyRelativeLayout;
import com.fancoff.coffeemaker.ui.commomView.MyTextView;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.SizeUtil;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;

/*
    支付界面
 */
public class PayPageFragment extends BaseFragment implements PayPageContract.IPayPageView {

    MyButton btn_pay, btn_changepayWei, btn_changepayaiPay, btn_changepayHe, btn_cancle;
    private PayPagePresent mainPresent;
    MyRelativeLayout root_layout;
    MyLinearLayout l_title_bg, layout_qrbg, t_warn_layout1, t_warn_layout2, layout_changepayWei, layout_changepayaiPay, layout_changepayHe, layout_cancle;
    MyTextView t_warn_title1, t_warn_title2, t_warn_title_en1, t_warn_title_en2, t_title, t_title_en, myTextView;
    MyImageView img_paycode;

    public PayPageFragment() {
    }

    /**取消支付：取消定时器，微信、支付宝、和包图片进行回收*/
    public void canclePay() {
        mainPresent.canclePay();
    }

    /**
     *
     */
    public static PayPageFragment newInstance(Bundle args) {
        PayPageFragment fragment = new PayPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static PayPageFragment newInstance() {
        return newInstance(null);
    }

    /**P层对象实例化，传入本来对象
     * 选择对应的支付类型进行显示，并显示二维码
     * 请求支付数据：成功与失败都发送消息到MAinActivity进行处理；
     */
    @Override
    public void initData() {
        mainPresent = new PayPagePresent(this);
        mainPresent.parseGoodsDetail((PayBean) getArguments().getSerializable("obj"));
        mainPresent.requestPayData(this);


    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pay;
    }

    /**
     * 1、各控件id获取；
     * 2、点击事件监听及处理；
     */
    @Override
    public void initView(View v) {
        LogUtil.action("open PayPageFragment");
        myTextView = (MyTextView) v.findViewById(R.id.myTextView);
        t_title = (MyTextView) v.findViewById(R.id.t_title);
        t_title_en = (MyTextView) v.findViewById(R.id.t_title_en);
        myTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getSmallSize());
        t_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getNomalSize());
        t_title_en.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getSmallSize());
        btn_cancle = (MyButton) v.findViewById(R.id.btn_cancle);
        btn_changepayWei = (MyButton) v.findViewById(R.id.btn_paywei);
        btn_changepayaiPay = (MyButton) v.findViewById(R.id.btn_aipay);
        btn_changepayHe = (MyButton) v.findViewById(R.id.btn_hepay);
        btn_cancle.setImage(R.drawable.cf_btn_cancle);
        btn_changepayaiPay.setImage(R.drawable.cf_btn_aipay);
        btn_changepayWei.setImage(R.drawable.cf_btn_weipay);
        btn_changepayHe.setImage(R.drawable.cf_btn_hepay);
        layout_changepayWei = (MyLinearLayout) v.findViewById(R.id.layout_paywei);
        layout_changepayaiPay = (MyLinearLayout) v.findViewById(R.id.layout_aipay);
        layout_changepayHe = (MyLinearLayout) v.findViewById(R.id.layout_hepay);
        layout_cancle = (MyLinearLayout) v.findViewById(R.id.layout_cancle);
        btn_pay = (MyButton) v.findViewById(R.id.btn_pay);
//        if (MyApp.getIns().getPayDebug()) {
//            btn_pay.setVisibility(View.VISIBLE);
//        } else {
        btn_pay.setVisibility(View.GONE);
//        }
        l_title_bg = (MyLinearLayout) v.findViewById(R.id.l_title_bg);
        l_title_bg.setImage(R.drawable.cf_title_pay_bg);
        root_layout = (MyRelativeLayout) v.findViewById(R.id.root_layout);
        root_layout.setImage(R.drawable.cf_dialog_bg);
        img_paycode = (MyImageView) v.findViewById(R.id.img_paycode);
        t_warn_layout1 = (MyLinearLayout) v.findViewById(R.id.t_warn_layout1);
        layout_qrbg = (MyLinearLayout) v.findViewById(R.id.layout_qrbg);
        layout_qrbg.setImage(R.drawable.cf_code_bg);
        t_warn_layout2 = (MyLinearLayout) v.findViewById(R.id.t_warn_layout2);
        t_warn_title1 = (MyTextView) v.findViewById(R.id.t_warn_name1);
        t_warn_title2 = (MyTextView) v.findViewById(R.id.t_warn_name2);
        t_warn_title_en1 = (MyTextView) v.findViewById(R.id.t_warn_name_en1);
        t_warn_title_en2 = (MyTextView) v.findViewById(R.id.t_warn_name_en2);
        t_warn_title1.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getNomalSize());
        t_warn_title2.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getNomalSize());
        //取消事件
        RxView.clicks(btn_cancle).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                LogUtil.action("click btn_cancle");
                canclePay();
                onBack();

            }
        });
        //改变为微信支付事件
        RxView.clicks(btn_changepayWei).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                mainPresent.changePay(MyConstant.PAY.PAY_TYPE_WEIXIN);


            }
        });
        //改变为支付宝支付事件
        RxView.clicks(btn_changepayaiPay).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                mainPresent.changePay(MyConstant.PAY.PAY_TYPE_AIPAY);


            }
        });
        //改变为和包支付事件
        RxView.clicks(btn_changepayHe).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                mainPresent.changePay(MyConstant.PAY.PAY_TYPE_HE);


            }
        });
        //测试
        if (MyApp.getIns().getPayDebug()) {
            RxView.clicks(img_paycode).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    mainPresent.testPay();
                }
            });
        }

    }

    @Override
    public boolean pauseMainPage() {
        return false;
    }

    /**
     * P层调用该方法，传入“选择的信息、选择信息英文、支付类型、支付类型英文”
     *   1、打印日志；
     *   2、进行UI显示；
     */
    @Override
    public void showGoodDetail(String title, String titleen, String warn, String warnen) {
        LogUtil.action(title);
        t_warn_title1.setText(title);
        t_warn_title_en1.setText(titleen);
        t_warn_title2.setText(warn);
        t_warn_title_en2.setText(warnen);

    }

    @Override
    public void onDestroy() {
        LogUtil.action("close PayPageFragment");
        canclePay();
        super.onDestroy();
    }

    /**img_paycode显示图片bmp*/
    @Override
    public void showbmp(Bitmap bmp) {
        img_paycode.setImageBitmap(bmp);

    }

    /**
     * 发送ACTION_INTENT,INTENT_TO_MAKING到MainActivity
     */
    @Override
    public void onPaySucess(final PayBean payBean) {
        MessageBean messageBean = new MessageBean(MessageBean.ACTION_INTENT, MyConstant.ACTION.INTENT_TO_MAKING);
        messageBean.setObj(payBean);
        sendMsgToActivity(messageBean);

    }

    /**根据showAi、showwei、showhe显示和隐藏对应的view*/
    @Override
    public void showChangeBtnText(boolean showAi, boolean showwei, boolean showhe) {
        layout_changepayaiPay.setVisibility(showAi ? View.VISIBLE : View.GONE);
        layout_changepayWei.setVisibility(showwei ? View.VISIBLE : View.GONE);
        layout_changepayHe.setVisibility(showhe ? View.VISIBLE : View.GONE);

    }


}
