package com.fancoff.coffeemaker.ui.dialog;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.MyRelativeLayout;
import com.fancoff.coffeemaker.ui.commomView.MyTextView;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.SizeUtil;

/*
    sorry退款弹框页面
 */
public class DialogSorryFragment extends BaseFragment {

    MyRelativeLayout root_layout;
    MyTextView t_making_content;


    public DialogSorryFragment() {
    }


    public static DialogSorryFragment newInstance(Bundle args) {
        DialogSorryFragment fragment = new DialogSorryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogSorryFragment newInstance() {
        return newInstance(null);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dialog_sorry;
    }

    @Override
    public void initView(View v) {
        root_layout = (MyRelativeLayout) v.findViewById(R.id.root_layout);
        root_layout.setImage(R.drawable.sorry);
        t_making_content= (MyTextView) v.findViewById(R.id.t_making_content);
        t_making_content.setText(getArguments().getString("content"));
        t_making_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getNomalSize());


    }

    @Override
    public boolean pauseMainPage() {
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.action("close DialogSorryFragment");

    }


}
