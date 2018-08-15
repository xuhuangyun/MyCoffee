package com.fancoff.coffeemaker.ui.Imageshow;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.coffe.ShowImageBean;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.MyImageView;
import com.fancoff.coffeemaker.ui.commomView.MyRelativeLayout;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.glide.ImageLoadUtils;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;
/**
 * 大图界面
 */
public class ImageShowFragment extends BaseFragment implements ImageShowContract.IDemoPageView {

    MyRelativeLayout showimglayout;
    MyImageView showimg;
    private ImageShowPresent mainPresent;

    public ImageShowFragment() {
    }

    /** MainActivity中的showImageFragment()会新建实例对象，并传送参数过来*/
    public static ImageShowFragment newInstance(Bundle args) {
        ImageShowFragment fragment = new ImageShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImageShowFragment newInstance() {
        return newInstance(null);
    }

    /**
     * 1、新建P层实例对象：mainPresent；
     * 2、从MAinActivity中传过来的参数中提取出ShowImageBean；
     * 3、showImageBean！=null：
     *        ImageView的showimg的宽度、高度，最左、最上位置；
     *        加载网络图片url到imageView中；
     */
    @Override
    public void initData() {
        mainPresent = new ImageShowPresent(this);
        ShowImageBean showImageBean = (ShowImageBean) getArguments().getSerializable("data");
        if (showImageBean != null) {
            showimg.setLayoutParams(new RelativeLayout.LayoutParams(showImageBean.getWidth(), showImageBean.getHeight()));
            showimg.setX(showImageBean.getPosition().getLeft());
            showimg.setY(showImageBean.getPosition().getTop());
            ImageLoadUtils.getInstance().loadNetImage(showimg, showImageBean.getUrl());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_imageshow;
    }

    /** 打印日志：DEBUG_ACTION:close ImageShowFragment */
    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.action("close ImageShowFragment");
    }

    /**
     * 打印日志：DEBUG_ACTION:open ImageShowFragment
     * 获取控件id：showimg、showimglayout
     * showimglayout点击事件监听：发送ACTION_BACK到MAinActivity中；
     */
    @Override
    public void initView(View v) {
        LogUtil.action("open ImageShowFragment");
        showimg = (MyImageView) v.findViewById(R.id.showimg);

        showimglayout = (MyRelativeLayout) v.findViewById(R.id.showimglayout);
        RxView.clicks(showimglayout).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                onBack();
            }
        });

    }

    @Override
    public boolean pauseMainPage() {
        return false;
    }
}
