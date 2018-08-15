package com.fancoff.coffeemaker.ui.dialog;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.MessageBean;
import com.fancoff.coffeemaker.io.CMDUtil;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.MyLinearLayout;
import com.fancoff.coffeemaker.ui.commomView.MyTextView;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.SizeUtil;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;

/**
 * 系统故障提醒页面
 */
public class DialogPageFragment extends BaseFragment {
    MyLinearLayout layout_dialog, layout_dialog_co;

    MyTextView tv_sure, tv_cancel, tv_title, tv_content;
    RelativeLayout screenViewlayout;

    public DialogPageFragment() {
    }


    public static DialogPageFragment newInstance(Bundle args) {
        DialogPageFragment fragment = new DialogPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogPageFragment newInstance() {
        return newInstance(null);
    }

    /**设置界面相应的id是否开启；tv_sure（清除故障）点击后：清除错误指令加入到任务列表中准备发送给vmc*/
    @Override
    public void initData() {

        showDilag(new DialogClickCallBack() {
            @Override
            public void onClick() {
                // 跳转到系统的网络设置界面
//                    MyApp.getIns().intentToSetting();
                CMDUtil.getInstance().clearError(null);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dialog;
    }

    @Override
    public void initView(View v) {
        layout_dialog = (MyLinearLayout) v.findViewById(R.id.layout_dialog);
        layout_dialog_co = (MyLinearLayout) v.findViewById(R.id.layout_dialog_co);
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(SizeUtil.screenWidth() * 7 / 9, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_dialog_co.setLayoutParams(l);
//        RelativeLayout.LayoutParams l2=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layout_dialog.setLayoutParams(l2);
        tv_cancel = (MyTextView) v.findViewById(R.id.tv_cancel);

        tv_content = (MyTextView) v.findViewById(R.id.tv_content);
        tv_sure = (MyTextView) v.findViewById(R.id.tv_sure);
        tv_sure = (MyTextView) v.findViewById(R.id.tv_sure);
        tv_title = (MyTextView) v.findViewById(R.id.tv_title);
        tv_title.setText(getArguments().getString("title"));
        tv_content.setText(getArguments().getString("content"));
        LogUtil.action("open DialogPageFragment：" + tv_title.getText().toString() + "--" + tv_content.getText().toString());
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getLargesetSize());
        tv_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getLargesetSize2());
        tv_cancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getSmallSize());
        tv_sure.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getSmallSize());
        screenViewlayout = v.findViewById(R.id.screenViewlayout);
        RxView.clicks(screenViewlayout).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });
    }

    /**tv_title、tv_content内容更新*/
    public void refContent(String title, String content) {
        tv_title.setText(title);
        tv_content.setText(content);
    }

    public interface DialogClickCallBack {
        void onClick();

    }

    public void dissmissDialog() {
        layout_dialog.setVisibility(View.GONE);
    }

//    public void showDilag(String title, String content, String ok, final DialogClickCallBack sure, String cancle, DialogClickCallBack oncancle) {
//        layout_dialog.setVisibility(View.VISIBLE);
//        tv_sure.setVisibility(View.VISIBLE);
//        tv_cancel.setVisibility(View.VISIBLE);
//        RxView.clicks(tv_sure).subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(Object o) throws Exception {
//                sure.onClick();
//
//            }
//        });
//        RxView.clicks(tv_cancel).subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(Object o) throws Exception {
//                sure.onClick();
//            }
//        });
//    }

    /**
     * 1、layout_dialog、tv_sure可视；
     * 2、showBtn=true：tv_sure可视；
     *    showBtn=false：tv_sure不可视；
     * 3、tv_cancel:不可视；
     * 4、tv_sure点击事件监听：回调onClick；
     */
    public void showDilag(final DialogClickCallBack sure) {
        layout_dialog.setVisibility(View.VISIBLE);
        tv_sure.setVisibility(View.VISIBLE);
        boolean showBtn = getArguments().getBoolean("btn");
        if (showBtn) {
            tv_sure.setVisibility(View.VISIBLE);
        } else {
            tv_sure.setVisibility(View.GONE);
        }

        tv_cancel.setVisibility(View.GONE);
        RxView.clicks(tv_sure).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                sure.onClick();

            }
        });
    }

//    public void showDilag(String title, String content, String ok, final DialogClickCallBack sure) {
//        layout_dialog.setVisibility(View.VISIBLE);
//        tv_sure.setVisibility(View.VISIBLE);
//        tv_cancel.setVisibility(View.GONE);
//        RxView.clicks(tv_sure).subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(Object o) throws Exception {
//                sure.onClick();
//
//            }
//        });
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.action("close DialogPageFragment");
    }

    @Override
    public void onBack() {
        sendMsgToActivity(new MessageBean(MessageBean.ACTION_BACK, this.getClass().getName()));
    }

}
