package com.fancoff.coffeemaker.ui.dialog;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.MessageBean;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.MyLinearLayout;
import com.fancoff.coffeemaker.ui.commomView.MyProgressBar;
import com.fancoff.coffeemaker.ui.commomView.MyTextView;
import com.fancoff.coffeemaker.utils.SizeUtil;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;

/**
 * progress请求进度页面
 */
public class ProgressPageFragment extends BaseFragment {
    MyLinearLayout progressLayout;
    MyProgressBar progressBar;
    MyTextView t_content;
    RelativeLayout screenViewlayout;

    public ProgressPageFragment() {
    }

    /**
     * 传入的参数：
     *     键progress、值：
     *     键content、值：
     */
    public static ProgressPageFragment newInstance(Bundle args) {
        ProgressPageFragment fragment = new ProgressPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProgressPageFragment newInstance() {
        return newInstance(null);
    }

    /**
     * 1、根据传入的参数，得到showprogress
     * 2、showprogress=true:progressBar可视
     *    showprogress=false:progressBar不可视
     * 3、从参数中获得键content所对应的内容
     */
    @Override
    public void initData() {
        boolean showprogress = getArguments().getBoolean("progress");
        if (showprogress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
        showProgress(getArguments().getString("content"));

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_progress;
    }

    @Override
    public void initView(View v) {

        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(SizeUtil.screenWidth() * 7 / 9, LinearLayout.LayoutParams.WRAP_CONTENT);
//        RelativeLayout.LayoutParams l2=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layout_dialog.setLayoutParams(l2);
        progressLayout = (MyLinearLayout) v.findViewById(R.id.progressLayout);
        t_content = (MyTextView) v.findViewById(R.id.t_content);
        progressBar = (MyProgressBar) v.findViewById(R.id.progress);
        t_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
        screenViewlayout = v.findViewById(R.id.screenViewlayout);
        RxView.clicks(screenViewlayout).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });
    }

    public void ref(boolean progress, String content) {
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
        showProgress(content);
    }

    public interface DialogClickCallBack {
        void onClick();

    }

    /**progressLayout隐藏*/
    public void hideProgress() {
        progressLayout.setVisibility(View.GONE);
    }

    /**
     * progressLayout可视、设置t_content的内容msg；
     */
    public void showProgress(String msg) {
        t_content.setText(msg);
        progressLayout.setVisibility(View.VISIBLE);

    }

    /**发送ACTION_BACK和本类名到MainActivity*/
    @Override
    public void onBack() {
        sendMsgToActivity(new MessageBean(MessageBean.ACTION_BACK, this.getClass().getName()));
    }

}
