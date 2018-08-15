package com.fancoff.coffeemaker.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.Application.TestIoDatas;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.MessageBean;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.bean.coffe.CoffeeConfigBean;
import com.fancoff.coffeemaker.bean.coffe.ImageBean;
import com.fancoff.coffeemaker.service.DurationUtil;
import com.fancoff.coffeemaker.ui.baseview.BaseVideoFragment;
import com.fancoff.coffeemaker.ui.commomView.MyBanner;
import com.fancoff.coffeemaker.ui.commomView.MyImageView;
import com.fancoff.coffeemaker.ui.commomView.MyLinearLayout;
import com.fancoff.coffeemaker.ui.commomView.MyRelativeLayout;
import com.fancoff.coffeemaker.ui.commomView.MyVideoView;
import com.fancoff.coffeemaker.ui.commomView.MyViewPager;
import com.fancoff.coffeemaker.ui.goods.GoodsFragmentAdapter;
import com.fancoff.coffeemaker.utils.SizeUtil;
import com.fancoff.coffeemaker.utils.ToastUtil;
import com.fancoff.coffeemaker.utils.glide.ImageLoadUtils;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * 商品首页主界面
 */
public class MainPageFragment extends BaseVideoFragment implements MainPageContract.IMainPageView, GoodsFragmentAdapter.OnItemClick {
    /*
        视频或图片区域
     */

    public final static int topHeight = 607;
    private MyRelativeLayout topLayout;
    private MyBanner banner;

    /*
       购买菜单区域
     */
    private MyImageView img_tab1, img_tab2, img_tab3;
    private MyViewPager mViewPager;

    public final static int tabItemHeight = 202;
    public final static int tabItemWith = 360;
    private MyLinearLayout tab_layout, recyclerLayout;
//    private ImageView img_tab1, img_tab2, img_tab3;


    /*
         信息区域
     */
    MyLinearLayout menu_layout;
    public final static int menuHeight = 303;
    public final static int colume = 3;
    private MyBanner img_show;//广告图片
    private MyImageView img_buycar, img_pickup, img_personal;

    private MainPagePresent mainPresent;

    public MainPageFragment() {
    }

    public static MainPageFragment newInstance(Bundle args) {
        MainPageFragment fragment = new MainPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MainPageFragment newInstance() {
        return newInstance(null);
    }

    @Override
    public void initData() {
        mainPresent = new MainPagePresent(this);
        mainPresent.requestData(MainPageContract.REF_TYP_ALL);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    //    public void selsetItem(int positon){
//
//        if(positon==0){
//
//        }else if(positon==1){
//
//        }else if(positon==2){
//
//        }
//    }
    ArrayList<MyImageView> tabViews = new ArrayList<MyImageView>();

    /**
     * 相当于修改ArrayList<MyImageView> tabViews被点击的图片加深：
     *  1、咖啡工艺中的咖啡饮品目录为空，则return false；
     *  2、遍历ArrayList<MyImageView> tabViews：
     *         遍历Categories（就是咖啡、奶类、餐包三大类）：
     *             position=index：相当于被点击了，加载被点击的图片
     *             position！=index：相当于没有被点击了，加载没有被点击的图片
     */
    public boolean selectTab(int position) {
        if (DataCenter.getInstance().getCategoriesList() == null
                || (DataCenter.getInstance().getCategoriesList().get(position).isEmpty())) {
            return false;
        }
        int index = 0;
        for (MyImageView tab : tabViews) {

            if (index < DataCenter.getInstance().getCategoriesList().size()) {

                if (position == index) {
                    ImageLoadUtils.getInstance().loadNetImage(tab,
                            DataCenter.getInstance().getCategoriesList().get(index).getImage().getSelected().getUrl());
                    //三个大类菜单被点击时候的图片；
                } else {
                    ImageLoadUtils.getInstance().loadNetImage(tab,
                            DataCenter.getInstance().getCategoriesList().get(index).getImage().getUnselect().getUrl());
                    //三个大类菜单没有被点击时候的图片；
                }

            } else {
                tab.setImageBitmap(null);
            }
            index++;
        }
        return true;
    }

    MyVideoView videoView;
    MyLinearLayout videoViewlayout;

    @Override
    public void initView(View v) {
        /*
        视频或图片区域
        */
        topLayout = (MyRelativeLayout) v.findViewById(R.id.topLayout);
        topLayout.setLayoutParams(new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtil.heightSize(topHeight)));
        banner = (MyBanner) v.findViewById(R.id.banner);
        videoView =v.findViewById(R.id.videoView);
        videoViewlayout = (MyLinearLayout) v.findViewById(R.id.videoViewlayout);
        if (MyApp.IOTEST) {
            RxView.clicks(videoViewlayout).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    TestIoDatas.getInstance().setOpenSetting(true);
                }
            });
        }
        banner.setVideoView(videoView);//MyBanner.videoView=videoView;videoView=GONE
        banner.setVideoViewlayout(videoViewlayout);//MyBanner.videoViewlayout=videoViewlayout

        img_tab1 = (MyImageView) v.findViewById(R.id.img_tab1);
        img_tab2 = (MyImageView) v.findViewById(R.id.img_tab2);
        img_tab3 = (MyImageView) v.findViewById(R.id.img_tab3);
        tabViews.add(img_tab1);
        tabViews.add(img_tab2);
        tabViews.add(img_tab3);
        mViewPager = (MyViewPager) v.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);//表示三个界面之间来回切换都不会重新加载
        tab_layout = (MyLinearLayout) v.findViewById(R.id.tab_layout);
        tab_layout.setLayoutParams(new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtil.heightSize(tabItemHeight)));
        RxView.clicks(img_tab1).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

                if (selectTab(0)) {
                    mViewPager.setCurrentItem(0, false);//当前选择的page为0；
                } else {
                    showToast(getString(R.string.emptygodds));//敬请期待
                }


            }
        });
        RxView.clicks(img_tab2).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (selectTab(1)) {
                    mViewPager.setCurrentItem(1, false);//当前选择的page为1；
                } else {
                    showToast(getString(R.string.emptygodds));
                }

            }
        });
        RxView.clicks(img_tab3).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (selectTab(2)) {
                    mViewPager.setCurrentItem(2, false);//当前选择的page为2；
                } else {
                    showToast(getString(R.string.emptygodds));
                }

            }
        });


        recyclerLayout = (MyLinearLayout) v.findViewById(R.id.recyclerLayout);
        recyclerLayout.setLayoutParams(new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtil.heightSize(tabItemHeight * 4)));

         /*
         信息菜单区域
          */
        menu_layout = (MyLinearLayout) v.findViewById(R.id.menu_layout);
        img_show = (MyBanner) v.findViewById(R.id.img_show);
        img_buycar = (MyImageView) v.findViewById(R.id.img_buycar);
        img_pickup = (MyImageView) v.findViewById(R.id.img_pickup);
        img_personal = (MyImageView) v.findViewById(R.id.img_personal);
        img_show.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (position < DataCenter.getInstance().getLeftBottoms().size()) {
                    ImageBean bottom = DataCenter.getInstance().getLeftBottoms().get(position);
                    showViewBig(bottom);
                }
//                MessageBean msg = new MessageBean(MessageBean.ACTION_INTENT);
//                msg.setWhat(MyConstant.ACTION.INTENT_TO_SETTING_FRAGMENT);
//                sendMsgToActivity(msg);
            }
        });

        RxView.clicks(img_buycar).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                ImageBean bottom = DataCenter.getInstance().getRightBottom1();
                showViewBig(bottom);

            }
        });
        RxView.clicks(img_pickup).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                ImageBean bottom = DataCenter.getInstance().getRightBottom2();
                showViewBig(bottom);
            }
        });
        RxView.clicks(img_personal).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                ImageBean bottom = DataCenter.getInstance().getRightBottom3();
                showViewBig(bottom);
            }
        });

        menu_layout.setLayoutParams(new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtil.heightSize(menuHeight)));

    }

    /**
     * 功能：左下和右下图片显示大图
     * 1、发送ACTION_INTENT,INTENT_TO_SHOWIMAGE到MAinActivity
     */
    private void showViewBig(ImageBean bottom) {


        if (bottom != null && bottom.getClick() != null) {
            if (bottom.getClick().getType().equals("showImage")) {
                LogUtil.test("showViewBig:" + bottom.toString());
                MessageBean msg = new MessageBean(MessageBean.ACTION_INTENT);
                msg.setWhat(MyConstant.ACTION.INTENT_TO_SHOWIMAGE);
                msg.setObj(bottom.getClick().getImage());//ShowImageBean
                sendMsgToActivity(msg);
            }
        }

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    /**
     * GoodsFragment类中的Item按键监听会调用该方法：
     *   1、有饮品的图片被点击：
     *        饮品的status=0，显示售罄；返回
     *        打印日志：DEBUG_ACTION:click item CoffeeBean{id=36, name=TextBean{value='美式特浓'},
     *        sub_name=TextBean{value='Double Espresso Americano'}, price=TextBean{value='5.00'},
     *        org_price=TextBean{value='15.00'}, status=1, make_duration=99,
     *        make=MakeBean{index=1, coffee_first=0, coffee_powder=22,
     *        sugar_level=SugarLevelBean{none=0, low=6, middle=10, high=16},
     *        ch1_water=115, ch2_water=0, ch3_water=0, ch4_water=0, coffee_water=200,
     *        coffee_preWater=15, ch1R_powder_level=0, ch2L_powder_level=0, ch2R_powder_level=0,
     *        ch3L_powder_level=0, ch3R_powder_level=0, ch4L_powder_level=0, ch4R_powder_level=0},
     *        image=ImageBean{url='http://srv.fancoff.com/Upload/02010404/f3ea930ae5ff4fe48ffff40a5054756a.png', position=null, click=null},seckillsToday=[]}
     *        ACTION_INTENT、INTENT_TO_BUY_FRAGMENT到MainActivity（打开购买界面）；
     *
     *   2、无饮品的图片被点击：
     *        显示：敬请期待；打印日志：click item  no coffee Detail
     */
    @Override
    public void onGoodsItemClick(int position, CoffeeBean coffeeBean) {
        if (coffeeBean != null) {

            if (coffeeBean.getStatus() == 0) {
                LogUtil.debug("click item  coffee sellout");
                ToastUtil.showToast(getContext(), "已售罄！");
                return;
            }
            LogUtil.debug("click item " + coffeeBean.toString());
            MessageBean msg = new MessageBean(MessageBean.ACTION_INTENT);

            msg.setObj(coffeeBean);
            msg.setWhat(MyConstant.ACTION.INTENT_TO_BUY_FRAGMENT);
            sendMsgToActivity(msg);
        } else {
            ToastUtil.showToast(getContext(), getString(R.string.emptygodds));
            LogUtil.debug("click item  no coffee Detail");
        }

    }

    GoodsFragmentAdapter goodsFragmentAdapternew;

    /**
     * 1、goodsFragmentAdapternew为空，新建一个实例对象；设置item点击事件；ViewPager适配器加载；设置咖啡类加深为主菜单；
     * 2、不为空：设置当前的item对应的图片加深（咖啡类或奶类或餐包类），传入咖啡工艺到适配器的属性，刷新商品列表；
     */
    @Override
    public void showGoodsFragments(CoffeeConfigBean coffeeConfigBean) {
        if (goodsFragmentAdapternew == null) {
            goodsFragmentAdapternew = new GoodsFragmentAdapter(getActivity(), getFragmentManager(), coffeeConfigBean);
            goodsFragmentAdapternew.setOnItemClick(this);
            mViewPager.setAdapter(goodsFragmentAdapternew);
            if (coffeeConfigBean != null && coffeeConfigBean.getCategories() != null &&
                    coffeeConfigBean.getCategories().size() > 0) {
                selectTab(0);
            }
        } else {
            selectTab(mViewPager.getCurrentItem());
            goodsFragmentAdapternew.setCoffeeConfigBean(coffeeConfigBean);
            goodsFragmentAdapternew.ref();
        }

//        mViewPager.setCurrentItem(0,false);
//        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void showBarnnerScreen(ArrayList<ImageBean> list) {

    }

    /**
     *  banner释放；视频或图片轮播的各种设置：列表、playVideo、isAutoPlay、delayTime；
     *  并进行轮播
     */
    @Override
    public void showBarnner(ArrayList<ImageBean> list) {
        banner.release();
        banner
                .setImages(list)//MyBanner的imageUrls赋予list
                .setPlayVideo(true)//playVideo为true
                .isAutoPlay(true)//isAutoPlay为true
                .setDelayTime(DurationUtil.getIns().getTopBannerTime())//获得咖啡工艺中top的轮询时间 s,delayTime为该时间
                .setBannerAnimation(Transformer.Default)
                .setImageLoader(new GlideImageLoader())//MyBanner的imageLoader=new GlideImageLoader()；
                .start();
    }
    /**
     *  img_show释放；视频或图片轮播的各种设置：左下图片列表、showCircle=false;playVideo=false、isAutoPlay=true、delayTime；
     *  并进行轮播
     *  加载右下角的3张网络图片
     */
    @Override
    public void showImage(ArrayList<ImageBean> left_bottoms, ImageBean right_bottom1, ImageBean right_bottom2, ImageBean right_bottom3) {
        img_show.release();
        img_show.setImages(left_bottoms).showCircle(false).setPlayVideo(false)
                .isAutoPlay(true)
                .setDelayTime(DurationUtil.getIns().getBottomBannerTime())
                .setBannerAnimation(Transformer.Default)
                .setImageLoader(new GlideImageLoader()).start();

        img_buycar.update(right_bottom1);
        img_pickup.update(right_bottom2);
        img_personal.update(right_bottom3);
    }

    /**
     * 更新顶部视频、左下右下图片，购买菜单、屏保图片
     */
    public void refAllView() {
        mainPresent.requestData(MainPageContract.REF_TYP_ALL);
    }

    /**
     * 加载网络图片url到imageView中
     * MyBanner类中的setImageList会调用该类的displayImage方法；
     */
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            ImageLoadUtils.getInstance().loadNetImage(imageView, ((ImageBean) path).getUrl());
        }

    }

    @Override
    public void onDestroy() {
        banner.release();
        banner.releaseBanner();
        img_show.releaseBanner();
        videoView.suspend();
        videoView=null;
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.onPause();
    }

}
