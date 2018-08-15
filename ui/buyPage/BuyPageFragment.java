package com.fancoff.coffeemaker.ui.buyPage;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.MessageBean;
import com.fancoff.coffeemaker.bean.PayBean;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.MyImageView;
import com.fancoff.coffeemaker.ui.commomView.MyLinearLayout;
import com.fancoff.coffeemaker.ui.commomView.MyRadioButton;
import com.fancoff.coffeemaker.ui.commomView.MyRadioGroup;
import com.fancoff.coffeemaker.ui.commomView.MyRelativeLayout;
import com.fancoff.coffeemaker.ui.commomView.MyTextView;
import com.fancoff.coffeemaker.ui.commomView.MyView;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.SizeUtil;
import com.fancoff.coffeemaker.utils.ToastUtil;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;

/**
 * 购物选择界面
 * 实现BuyPageContract.IBuyPageView接口方法：
 *
 */
public class BuyPageFragment extends BaseFragment implements BuyPageContract.IBuyPageView {

    MyLinearLayout layout_cancle, layout_weixin, layout_aipay, layout_car, layout_hepay, t_good_layout, l_title_bg;
    MyTextView t_title, t_title_en, t_good_name, t_good_name_en;
    MyRadioButton myRadioButton1, myRadioButton2, myRadioButton3, myRadioButton4;
    MyRelativeLayout root_layout;
    MyImageView img_cancle, img_aipay, img_hepay, img_car, img_weixin;
    MyTextView t_cancle, t_aipay, t_car, t_weixin;
    MyRadioGroup myRadioGroup;
    MyView line1, line2, line3, line4;
    private BuyPagePresent mainPresent;   //P层

    public BuyPageFragment() {
    }

    /**MainActivity中的showBuyFragment实例化该对象*/
    public static BuyPageFragment newInstance(Bundle args) {
        BuyPageFragment fragment = new BuyPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static BuyPageFragment newInstance() {
        return newInstance(null);
    }

    /***
     * 复写接口IBaseFragment中initData()方法
     * 1、实例化BuyPagePresent对象
     * 2、购买界面的甜度选择、饮品名字、支付方式的显示；
     */
    @Override
    public void initData() {

        mainPresent = new BuyPagePresent(this);
        mainPresent.showDetail((CoffeeBean) getArguments().getSerializable("obj"));

    }

    /**
     * 复写接口IBaseFragment中getLayoutId()方法
     * 返回购买对话框的id
     */
    @Override
    public int getLayoutId() {
        return R.layout.fragment_buy;
    }

    /**
     * 复写接口IBaseFragment中initView()方法
     * 1、打印日志：open BuyPageFragment
     * 2、Progress_layout.xml，默认隐藏
     * 3、获得各控件id
     * 4、
     */
    @Override
    public void initView(View v) {
        LogUtil.action("open BuyPageFragment");
        initViewProgress(v);
        root_layout = (MyRelativeLayout) v.findViewById(R.id.root_layout);
        root_layout.setImage(R.drawable.cf_dialog_bg);
        l_title_bg = (MyLinearLayout) v.findViewById(R.id.l_title_bg);
        l_title_bg.setImage(R.drawable.cf_select_sweet_title_bg);
        line1 = (MyView) v.findViewById(R.id.line_1);
        line2 = (MyView) v.findViewById(R.id.line_2);
        line3 = (MyView) v.findViewById(R.id.line_3);
        line4 = (MyView) v.findViewById(R.id.line_4);
        t_good_name = (MyTextView) v.findViewById(R.id.t_good_name);
        t_good_name_en = (MyTextView) v.findViewById(R.id.t_good_name_en);
        t_title = (MyTextView) v.findViewById(R.id.t_title);
//        t_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getLargeSize());
        t_title_en = (MyTextView) v.findViewById(R.id.t_title_en);
//        t_title_en.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getNomalSize());
        t_good_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getLargeSize());
        t_good_name_en.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getNomalSize());
        img_cancle = (MyImageView) v.findViewById(R.id.img_cancle);
        img_cancle.setImage(R.drawable.cf_close);
        img_weixin = (MyImageView) v.findViewById(R.id.img_weixin);
        img_weixin.setImage(R.drawable.ico_weixin);
        img_car = (MyImageView) v.findViewById(R.id.img_car);
        img_aipay = (MyImageView) v.findViewById(R.id.img_aipay);
        img_hepay = (MyImageView) v.findViewById(R.id.img_hepay);
        img_hepay.setImage(R.drawable.cf_hepay);
        img_aipay.setImage(R.drawable.ico_zhifubao);
        t_cancle = (MyTextView) v.findViewById(R.id.t_cancle);
        t_weixin = (MyTextView) v.findViewById(R.id.t_weixin);
        t_car = (MyTextView) v.findViewById(R.id.t_car);
        t_aipay = (MyTextView) v.findViewById(R.id.t_aipay);
        myRadioGroup = (MyRadioGroup) v.findViewById(R.id.myRadioGroup);
        myRadioButton1 = (MyRadioButton) v.findViewById(R.id.myRadioButton1);
        myRadioButton2 = (MyRadioButton) v.findViewById(R.id.myRadioButton2);
        myRadioButton3 = (MyRadioButton) v.findViewById(R.id.myRadioButton3);
        myRadioButton4 = (MyRadioButton) v.findViewById(R.id.myRadioButton4);
        myRadioButton1.setTextP(getResources().getString(R.string.nosugar));
        myRadioButton2.setTextP(getResources().getString(R.string.lowsugar));
        myRadioButton3.setTextP(getResources().getString(R.string.middlesugar));
        myRadioButton4.setTextP(getResources().getString(R.string.hightsugar));
        layout_cancle = (MyLinearLayout) v.findViewById(R.id.layout_cancle);
        layout_weixin = (MyLinearLayout) v.findViewById(R.id.layout_weixin);
        layout_aipay = (MyLinearLayout) v.findViewById(R.id.layout_aipay);
        layout_hepay = (MyLinearLayout) v.findViewById(R.id.layout_hepay);

        layout_car = (MyLinearLayout) v.findViewById(R.id.layout_car);
        layout_car.setVisibility(View.GONE);
        t_good_layout = (MyLinearLayout) v.findViewById(R.id.t_good_layout);

        //取消事件
        RxView.clicks(layout_cancle).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                LogUtil.action("click  btn close");//日志打印click  btn close
                onBack();

            }
        });
        //点击微信图片事件
        RxView.clicks(layout_weixin).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                mainPresent.reqGoodsOrder(MyConstant.PAY.PAY_TYPE_WEIXIN);//调用p层
            }
        });
        //点击支付宝图片事件
        RxView.clicks(layout_aipay).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                mainPresent.reqGoodsOrder(MyConstant.PAY.PAY_TYPE_AIPAY);

            }
        });
        //点击购物车图片事件
        RxView.clicks(layout_car).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                mainPresent.addCar();

            }
        });
        //点击和包图片事件
        RxView.clicks(layout_hepay).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                mainPresent.reqGoodsOrder(MyConstant.PAY.PAY_TYPE_HE);

            }
        });


    }


    /**
     * 实现接口BuyPageContract.IBuyPageView的getSugarSelect()方法
     * 选择哪个甜度，返回对应甜度的常量值
     */
    @Override
    public int getSugarSelect() {
        if (myRadioButton1.isChecked()) {
            return MyConstant.SUGAR.SUGAR_NO;
        } else if (myRadioButton2.isChecked()) {
            return MyConstant.SUGAR.SUGAR_LOW;
        }
        if (myRadioButton3.isChecked()) {
            return MyConstant.SUGAR.SUGAR_MIDDLE;
        }
        if (myRadioButton4.isChecked()) {
            return MyConstant.SUGAR.SUGAR_HIGHT;
        }
        return MyConstant.SUGAR.SUGAR_NO;
    }
    /**
     * 实现接口BuyPageContract.IBuyPageView的showName()方法
     * 设置购买界面饮品的中英文名字
     */
    @Override
    public void showName(String name, String nameen) {
        t_good_name.setText(name);
        t_good_name_en.setText(nameen);
    }

    /**
     * 实现接口BuyPageContract.IBuyPageView的gotoView()方法
     * 发送action=ACTION_INTENT,obj=payBean和what=付款ACTION消息到MainActivity
     */
    @Override
    public void gotoView(PayBean payBean) {
        MessageBean messageBean = new MessageBean(MessageBean.ACTION_INTENT);
        messageBean.setObj(payBean);  //obj对象为payBean
        messageBean.setWhat(MyConstant.ACTION.INTENT_TO_PAY_FRAGMENT);//what为付款ACTION
        sendMsgToActivity(messageBean);
    }
    /**实现接口BuyPageContract.IBuyPageView的onAddSuccess()方法*/
    @Override
    public void onAddSuccess() {
        ToastUtil.showToast(getActivity(), "添加购物车成功！");
        onBack();

    }

    @Override
    public void onDestroy() {
        hideProgress();
        super.onDestroy();
        LogUtil.action("close BuyPageFragment");

    }
    /**
     * 实现接口BuyPageContract.IBuyPageView的showRadioButton()方法
     * 甜度选择菜单：显示radio和line，选择对应的radio
     * radio和下划线根据传入的参数进行显示或者不可视；根据select选择对应的button；
     */
    @Override
    public void showRadioButton(boolean radioButton1, boolean radioButton2, boolean radioButton3, boolean radioButton4, int select) {
        myRadioButton1.setVisibility(radioButton1 ? View.VISIBLE : View.GONE);
        myRadioButton2.setVisibility(radioButton2 ? View.VISIBLE : View.GONE);
        myRadioButton3.setVisibility(radioButton3 ? View.VISIBLE : View.GONE);
        myRadioButton4.setVisibility(radioButton4 ? View.VISIBLE : View.GONE);
        line1.setVisibility(radioButton1 ? View.VISIBLE : View.GONE);
        line2.setVisibility(radioButton2 ? View.VISIBLE : View.GONE);
        line3.setVisibility(radioButton3 ? View.VISIBLE : View.GONE);
        line4.setVisibility(radioButton4 ? View.VISIBLE : View.GONE);
        if (select == 0) {
            myRadioButton1.setChecked(true);
        } else if (select == 1) {
            myRadioButton2.setChecked(true);
        } else if (select == 2) {
            myRadioButton3.setChecked(true);
        } else if (select == 3) {
            myRadioButton4.setChecked(true);
        }

    }

    /**
     * 实现接口BuyPageContract.IBuyPageView的showPayBtn()方法
     * 支付方式界面的显示：如显示支付宝、微信、和包
     */
    @Override
    public void showPayBtn(boolean aipay, boolean weipay, boolean hepay) {
        layout_aipay.setVisibility(aipay ? View.VISIBLE : View.GONE);
        layout_weixin.setVisibility(weipay ? View.VISIBLE : View.GONE);
        layout_hepay.setVisibility(hepay ? View.VISIBLE : View.GONE);
    }

    /**复写接口IBaseFragment中pauseMainPage()方法*/
    @Override
    public boolean pauseMainPage() {
        return false;
    }
}
