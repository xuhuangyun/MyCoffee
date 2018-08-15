package com.fancoff.coffeemaker.ui.screen;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.MessageBean;
import com.fancoff.coffeemaker.bean.coffe.ImageBean;
import com.fancoff.coffeemaker.service.DurationUtil;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.MyBanner;
import com.fancoff.coffeemaker.ui.main.MainPageFragment;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.SizeUtil;
import com.fancoff.coffeemaker.utils.glide.ImageLoadUtils;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

/**
 * 屏保界面
 */
public class ScreenPageFragment extends BaseFragment implements ScreenPageContract.IScreenPageView {

    MyBanner screenbanner;
    private ScreenPagePresent mainPresent;
    RelativeLayout screenViewlayout, parent;

    public ScreenPageFragment() {
    }

    public static ScreenPageFragment newInstance(Bundle args) {
        ScreenPageFragment fragment = new ScreenPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ScreenPageFragment newInstance() {
        return newInstance(null);
    }

    /**
     * 1、新建P层的实例对象mainPresent；
     * 2、用P层去调用M层的请求数据方法，M层返回屏保图片列表给P层；
     */
    @Override
    public void initData() {
        mainPresent = new ScreenPagePresent(this);
        mainPresent.requestData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_screen;
    }

    @Override
    public void initView(View v) {
        LogUtil.action("open ScreenPageFragment");
        parent = (RelativeLayout) v.findViewById(R.id.parent);
        screenbanner = (MyBanner) v.findViewById(R.id.screenbanner);
        screenViewlayout = (RelativeLayout) v.findViewById(R.id.screenViewlayout);
        screenViewlayout.setVisibility(View.GONE);
        screenbanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                DurationUtil.getIns().seTimeToAwake();  //NoActionTime = 0
                onBack();
            }
        });
        parent.setPadding(0, SizeUtil.heightSize(MainPageFragment.topHeight), 0, 0);
//        RxView.clicks(screenViewlayout).subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(Object o) throws Exception {
//                DurationUtil.getIns().seTimeToAwake();
//                onBack();
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.action("close ScreenPageFragment");
        screenbanner.release();
        screenbanner.releaseBanner();
    }

    /**
     * 显示屏保：
     *    1、屏保列表为空，移除该fragment；
     *    2、screenbanner先释放，轮播图片的一些设置，然后进行图片轮播；
     */
    @Override
    public void showBanner(ArrayList<ImageBean> list) {
        if(list==null||list.size()==0){
            onBack();
            return;
        }
        screenbanner.release();
        screenbanner.setImages(list)//传输屏保图片的urls给Mybanner类的属性imageUrls
                .setPlayVideo(false)//设置Mybanner类的属性playVideo=false；
                .isAutoPlay(true)//设置Mybanner类的属性的isAutoPlay=true；
                .setDelayTime(DurationUtil.getIns()//先获得咖啡工艺中屏保的轮询时间，然后将该时间传入给Mybanner类
                        .getScreenBannerTime())
                .setBannerAnimation(Transformer.ZoomOut)  //ZoomOut效果的
                .setImageLoader(new ScreenPageFragment.GlideImageLoader()).start();//指示器、标题、图片轮播顺序、适配器设置，然后进行轮播

    }

    /**
     * 更新屏保图片
     */
    public void refView() {
        mainPresent.requestData();
    }

    /**
     * 加载网络图片url到imageView中
     *    MyBanner类中的setImageList()方法会调用displayImage方法
     */
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            ImageLoadUtils.getInstance().loadNetImage(imageView, ((ImageBean) path).getUrl());
        }

    }

    @Override
    public void onBack() {
        sendMsgToActivity(new MessageBean(MessageBean.ACTION_BACK, this.getClass().getName()));
    }

}
