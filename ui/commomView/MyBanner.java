package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.coffe.ImageBean;
import com.fancoff.coffeemaker.utils.ToastUtil;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.MediaFile;
import com.youth.banner.BannerConfig;
import com.youth.banner.BannerScroller;
import com.youth.banner.WeakHandler;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoaderInterface;
import com.youth.banner.view.BannerViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static android.support.v4.view.ViewPager.OnPageChangeListener;
import static android.support.v4.view.ViewPager.PageTransformer;

public class MyBanner extends FrameLayout implements OnPageChangeListener {
    private MyLinearLayout videoViewlayout;
    private MyVideoView videoView;

    public MyLinearLayout getVideoViewlayout() {
        return videoViewlayout;
    }

    /**MyBanner.videoViewlayout=videoViewlayout*/
    public void setVideoViewlayout(MyLinearLayout videoViewlayout) {
        this.videoViewlayout = videoViewlayout;

    }

    public MyVideoView getVideoView() {
        return videoView;
    }

    /**MyBanner.videoView=videoView;videoView=GONE*/
    public void setVideoView(MyVideoView videoView) {
        this.videoView = videoView;
        videoView.setVisibility(View.GONE);  //消失 不可见，不占有布局空间
    }

    public String tag = "banner";
    private int mIndicatorMargin = BannerConfig.PADDING_SIZE;   //int PADDING_SIZE = 5;指示器之间的间距
    private int mIndicatorWidth;   //指示器圆形按钮的宽度
    private int mIndicatorHeight;  //指示器圆形按钮的高度
    private int indicatorSize;     //指示器大小
    private int bannerBackgroundImage;
    private int bannerStyle = BannerConfig.CIRCLE_INDICATOR; //CIRCLE_INDICATOR = 1  指示器风格
    private int delayTime = BannerConfig.TIME;      //int TIME = 2000; 轮播间隔时间
    private int scrollTime = BannerConfig.DURATION; //int DURATION = 800; 轮播滑动执行时间
    private boolean isAutoPlay = BannerConfig.IS_AUTO_PLAY;  //boolean IS_AUTO_PLAY = true; 是否自动轮播
    private boolean isScroll = BannerConfig.IS_SCROLL;       //boolean IS_SCROLL = true;
    private int mIndicatorSelectedResId = R.drawable.gray_radius;      //指示器选中效果
    private int mIndicatorUnselectedResId = R.drawable.white_radius;   //指示器未选中效果
    private int mLayoutResId = R.layout.banner;
    private int titleHeight;      //标题栏高度
    private int titleBackground;  //reference
    private int titleTextColor;   //标题字体颜色
    private int titleTextSize;    //标题字体大小
    private int count = 0;
    private int currentItem;
    private int gravity = -1;
    private int lastPosition = 1;
    private int scaleType = 1;    //enum 和imageview的ScaleType作用一样
    private List<String> titles;
    private List<ImageBean> imageUrls;
    private List<View> imageViews;
    private List<ImageView> indicatorImages;
    private Context context;
    private BannerViewPager viewPager;
    private TextView bannerTitle, numIndicatorInside, numIndicator;//banner.xml中含有这几个textView
    private LinearLayout indicator, indicatorInside, titleView;//banner.xml中含有这几个LinearLayout
    private ImageView bannerDefaultImage;//banner.xml中含有这个ImageView
    private ImageLoaderInterface imageLoader;
    private BannerPagerAdapter adapter;
    private OnPageChangeListener mOnPageChangeListener;
    private BannerScroller mScroller;// bannerScroller继承自scroller，重写了其中的startScroll这个方法，增加一个setDuration（int time）  从而使得两个banner之间切换的持续时间可控
    private OnBannerClickListener bannerListener;
    private OnBannerListener listener;
    private DisplayMetrics dm;  //描述一个显示的一般信息的结构，例如它的大小、密度和字体大小。
    boolean playVideo = false;

    /**设置本类的playVideo为true或false*/
    public MyBanner setPlayVideo(boolean playVideo) {
        this.playVideo = playVideo;
        return this;
    }

    private WeakHandler handler = new WeakHandler();

    public MyBanner(Context context) {
        this(context, null);
    }

    public MyBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     *
     * @param context
     * @param attrs  xml中属性的集合
     * @param defStyle
     */
    public MyBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        titles = new ArrayList<>();
        imageUrls = new ArrayList<>();
        imageViews = new ArrayList<>();
        indicatorImages = new ArrayList<>();
        dm = context.getResources().getDisplayMetrics();  //获得屏幕的尺寸
        indicatorSize = dm.widthPixels / 80;  // 1920/80=24
        initView(context, attrs);
    }

    /**
     * 1、处理属性attrs值赋予本来的属性mIndicatorWidth、mIndicatorHeight等；
     * 2、加载布局文件R.layout.banner;
     * 3、获得布局文件个控件的id、设置默认背景bannerBackgroundImage；
     * 4、控制viewpager滑动时间为800
     */
    private void initView(Context context, AttributeSet attrs) {
        imageViews.clear();
        handleTypedArray(context, attrs);
        View view = LayoutInflater.from(context).inflate(mLayoutResId, this, true);//R.layout.banner;
        bannerDefaultImage = (ImageView) view.findViewById(R.id.bannerDefaultImage);
        viewPager = (BannerViewPager) view.findViewById(R.id.bannerViewPager);
        titleView = (LinearLayout) view.findViewById(R.id.titleView);
        indicator = (LinearLayout) view.findViewById(R.id.circleIndicator);
        indicatorInside = (LinearLayout) view.findViewById(R.id.indicatorInside);
        bannerTitle = (TextView) view.findViewById(R.id.bannerTitle);
        numIndicator = (TextView) view.findViewById(R.id.numIndicator);
        numIndicatorInside = (TextView) view.findViewById(R.id.numIndicatorInside);
        bannerDefaultImage.setImageResource(bannerBackgroundImage);
        initViewPagerScroll();
    }

    /**
     * 处理属性attrs值赋予本来的属性mIndicatorWidth、mIndicatorHeight等；
     */
    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        //obtainStyledAttributes(AttributeSet set, int[] attrs)从layout设置的属性集中获取attrs中的属性
        //attrs:int[],就是告诉系统需要获取那些属性的值
        //set：表示从layout文件中直接为这个View添加的属性的集合
        //TypedArray实例是个属性的容器
        //我的理解： R.styleable.Banner为Banner下有一些属性；attrs把这些属性组合成一个集合
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);

        //Banner_indicator_width，如果没有，则使用默认的值，indicatorSize。
        //在索引中检索维度单元属性，以作为原始像素的大小。
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_width, indicatorSize);
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_height, indicatorSize);
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_margin, BannerConfig.PADDING_SIZE);
        //检索索引处属性的资源标识符。
        mIndicatorSelectedResId = typedArray.getResourceId(R.styleable.Banner_indicator_drawable_selected, R.drawable.gray_radius);
        mIndicatorUnselectedResId = typedArray.getResourceId(R.styleable.Banner_indicator_drawable_unselected, R.drawable.white_radius);
        //检索索引处属性的整数值。
        scaleType = typedArray.getInt(R.styleable.Banner_image_scale_type, scaleType);
        delayTime = typedArray.getInt(R.styleable.Banner_delay_time, BannerConfig.TIME);
        scrollTime = typedArray.getInt(R.styleable.Banner_scroll_time, BannerConfig.DURATION);
        isAutoPlay = typedArray.getBoolean(R.styleable.Banner_is_auto_play, BannerConfig.IS_AUTO_PLAY);
        titleBackground = typedArray.getColor(R.styleable.Banner_title_background, BannerConfig.TITLE_BACKGROUND);
        titleHeight = typedArray.getDimensionPixelSize(R.styleable.Banner_title_height, BannerConfig.TITLE_HEIGHT);
        titleTextColor = typedArray.getColor(R.styleable.Banner_title_textcolor, BannerConfig.TITLE_TEXT_COLOR);
        titleTextSize = typedArray.getDimensionPixelSize(R.styleable.Banner_title_textsize, BannerConfig.TITLE_TEXT_SIZE);
        mLayoutResId = typedArray.getResourceId(R.styleable.Banner_banner_layout, mLayoutResId);
        bannerBackgroundImage = typedArray.getResourceId(R.styleable.Banner_banner_default_image, R.drawable.emptybg);
        typedArray.recycle();
    }

    /**
     * 反射机制,控制viewpager滑动时间为800
     */
    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new BannerScroller(viewPager.getContext());
            mScroller.setDuration(scrollTime);
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            Log.e(tag, e.getMessage());
        }
    }

    /**设置本类的isAutoPlay为true或false*/
    public MyBanner isAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    /**
     * 接口实例传入MyBannner对象的imageLoader
     */
    public MyBanner setImageLoader(ImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    /**设置本来对象的delayTime为传入参数delayTime*/
    public MyBanner setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    /**设置gravity类型*/
    public MyBanner setIndicatorGravity(int type) {
        switch (type) {
            case BannerConfig.LEFT:
                this.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                break;
            case BannerConfig.CENTER:
                this.gravity = Gravity.CENTER;
                break;
            case BannerConfig.RIGHT:
                this.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }
        return this;
    }

    /** */
    public MyBanner setBannerAnimation(Class<? extends PageTransformer> transformer) {
        try {
            setPageTransformer(true, transformer.newInstance());
        } catch (Exception e) {
            Log.e(tag, "Please set the PageTransformer class");
        }
        return this;
    }

    /**
     * Set the number of pages that should be retained to either side of the
     * current page in the view hierarchy in an idle state. Pages beyond this
     * limit will be recreated from the adapter when needed.
     *
     * @param limit How many pages will be kept offscreen in an idle state.
     * @return Banner
     */
    public MyBanner setOffscreenPageLimit(int limit) {
        if (viewPager != null) {
            viewPager.setOffscreenPageLimit(limit);
        }
        return this;
    }

    /**
     * Set a {@link PageTransformer} that will be called for each attached page whenever
     * the scroll position is changed. This allows the application to apply custom property
     * transformations to each page, overriding the default sliding look and feel.
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param transformer         PageTransformer that will modify each page's animation properties
     * @return Banner
     */
    /**
     * 设置ViewPager切换时的动画效果
     * 每个附件页面滚动位置发生改变时，页面转换器将被调用。
     * reverseDrawingOrder=true：最后的到最前，代替最前到最后；
     */
    public MyBanner setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
        return this;
    }

    public MyBanner setBannerTitles(List<String> titles) {
        this.titles = titles;
        return this;
    }

    public MyBanner setBannerStyle(int bannerStyle) {
        this.bannerStyle = bannerStyle;
        return this;
    }

    public MyBanner setViewPagerIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
        return this;
    }

    ImageBean empty = new ImageBean("");

    /**参数的imageUrls赋予本类的imageUrsl，并返回本类this*/
    public MyBanner setImages(List<ImageBean> imageUrls) {
        if (imageUrls == null) {
            imageUrls = new ArrayList<>();
        }
//        if (imageUrls.size() == 0) {
//            imageUrls.add(empty);
//        }
        this.imageUrls.clear();
        this.imageUrls.addAll(imageUrls);//参数imageUrls增加到本类imageUrls的尾部
        this.count = imageUrls.size();
        return this;
    }

    /**更新titles列表，将参数titles列表添加到本类的titles列表
     * 更新imageUrls列表：
     */
    public void update(List<ImageBean> imageUrls, List<String> titles) {
        this.titles.clear();
        this.titles.addAll(titles);
        update(imageUrls);
    }

    /**
     * 更新imageUrls列表：
     * imageUrls、imageViews、indicatorImages清空
     * 参数imageUrls列表赋予本类的imageUrls列表；
     */
    public void update(List<ImageBean> imageUrls) {
        this.imageUrls.clear();
        this.imageViews.clear();
        this.indicatorImages.clear();
        this.imageUrls.addAll(imageUrls);
        this.count = this.imageUrls.size();
        start();
    }

    public void updateBannerStyle(int bannerStyle) {
        indicator.setVisibility(GONE);
        numIndicator.setVisibility(GONE);
        numIndicatorInside.setVisibility(GONE);
        indicatorInside.setVisibility(GONE);
        bannerTitle.setVisibility(View.GONE);
        titleView.setVisibility(View.GONE);
        this.bannerStyle = bannerStyle;
        start();
    }

    /**
     * 1、设置指示器和标题模式：
     * 2、设置图片轮播顺序；
     * 3、设置viewPager适配器，进行图片轮播；
     */
    public MyBanner start() {
        setBannerStyleUI();
        setImageList(imageUrls);
        setData();
        return this;
    }

    /**
     * 设置标题模式：
     * 1、设置titleView背景颜色
     * 2、titleView设置Layout参数：width=MARCH_PARENT,height=titleHeight
     * 3、设置bannerTitle的字体颜色
     * 4、设置bannerTitle的字体大小
     * 5、设置bannerTitle内容，bannerTitle、titleView可视
     */
    private void setTitleStyleUI() {
        if (titles.size() != imageUrls.size()) {//title和image数量不相等
            throw new RuntimeException("[Banner] --> The number of titles and images is different");
            //用指定的详细消息构造一个新的运行时异常。
        }
        if (titleBackground != -1) {//title背景不为-1，则设置titleView背景颜色为titleBackground
            titleView.setBackgroundColor(titleBackground);
        }
        if (titleHeight != -1) {//标题栏高度不为-1，titleView设置Layout参数：width=MARCH_PARENT,height=titleHeight
            titleView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titleHeight));
        }
        if (titleTextColor != -1) {//标题栏字体颜色不为-1，设置bannerTitle的字体颜色为titleTextColor
            bannerTitle.setTextColor(titleTextColor);
        }
        if (titleTextSize != -1) {//标题栏字体大小不为-1，设置bannerTitle的字体大小为titleTextSize（PX）
            bannerTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        }
        if (titles != null && titles.size() > 0) {//设置bannerTitle内容，bannerTitle、titleView可视
            bannerTitle.setText(titles.get(0));
            bannerTitle.setVisibility(View.VISIBLE);
            titleView.setVisibility(View.VISIBLE);
        }
    }

    boolean showCircle = false;

    /**设置本类的showCircle为传入的参数show*/
    public MyBanner showCircle(boolean show) {
        showCircle = show;
        return this;
    }

    /**
     * 设置指示器和标题模式：
     * 1、可视变量visibility：
     *     showCircle=true： count>1    VISIBLE,可视
     *                       count<=1   GONE,不可视
     *     showCircle=false：GONE    不可视
     *  2、根据bannerStyle调用不同的状态：
     *     圆形指示器、数字指示器、数字指示器+标题、圆形指示器+标题、圆形指示器+标题(垂直显示)、圆形指示器和标题(水平显示)
     */
    private void setBannerStyleUI() {
        int visibility = showCircle ? (count > 1 ? View.VISIBLE : View.GONE) : View.GONE;
        switch (bannerStyle) {
            case BannerConfig.CIRCLE_INDICATOR://显示圆形指示器
                indicator.setVisibility(visibility);
                break;
            case BannerConfig.NUM_INDICATOR://显示数字指示器
                numIndicator.setVisibility(visibility);
                break;
            case BannerConfig.NUM_INDICATOR_TITLE://显示数字指示器和标题
                numIndicatorInside.setVisibility(visibility);
                setTitleStyleUI();
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE://显示圆形指示器和标题(垂直显示)
                indicator.setVisibility(visibility);
                setTitleStyleUI();
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE://显示圆形指示器和标题(水平显示)
                indicatorInside.setVisibility(visibility);
                setTitleStyleUI();
                break;
        }
    }

    /**
     * 初始化images（指示器中视图）：
     * 1、imageViews列表清空；
     * 2、是圆形指示器、圆形指示器+标题、圆形指示器+标题，水平显示，则创建指示器LinearLayout中的视图view
     * 3、数字指示器+标题，numIndicatorInside设置文字为“1/count”
     * 4、数字指示器，numIndicator设置文字为“1/count”
     */
    private void initImages() {
        imageViews.clear();
        if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ||   //圆形指示器
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE ||  //圆形指示器+标题
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE) {//圆形指示器+标题，水平显示
            createIndicator();
        } else if (bannerStyle == BannerConfig.NUM_INDICATOR_TITLE) {//数字指示器+标题
            numIndicatorInside.setText("1/" + count);
        } else if (bannerStyle == BannerConfig.NUM_INDICATOR) {//数字指示器
            numIndicator.setText("1/" + count);
        }
    }

    /**
     * bannerDefaultImage的显示和隐藏，i=0~count+1图片轮播显示
     * imagesUrl为空：
     *     设置bannerDefaultImage为可视，返回；
     * imagesUrl不为空：
     *     设置bannerDefaultImage不可视；
     *     初始化images：（指示器中视图）；
     *     count：
     *         设置imageView类型:CENTER、等；
     *         并将imageView添加到imageViews；
     *         imageLoader显示imageView:imageLoader.displayImage(context, url, imageView)；
     *            1、调用ScreenPageFragment类中的GlideImageLoader()方法实现的displayImage
     *            2、调用MainPageFragment类中的GlideImageLoader()方法实现的displayImage
     *            加载网络图片到imageView中
     */
    private void setImageList(List<?> imagesUrl) {
        if (imagesUrl == null || imagesUrl.size() <= 0) {
            bannerDefaultImage.setVisibility(VISIBLE);
            Log.e(tag, "The image data set is empty.");
            return;
        }
        bannerDefaultImage.setVisibility(GONE);
        initImages();
        for (int i = 0; i <= count + 1; i++) {
            View imageView = null;
            if (imageLoader != null) { //imageLoader不为空，
                imageView = imageLoader.createImageView(context);//创建一个ImageView
            }
            if (imageView == null) {//imageView为空，新建imageView
                imageView = new ImageView(context);
            }
            setScaleType(imageView);
            Object url = null;
            if (i == 0) {//图片轮播顺序：如有3张图，则0,1,2,3,4代表get2,get0,get1,get2,get0
                url = imagesUrl.get(count - 1);
            } else if (i == count + 1) {
                url = imagesUrl.get(0);
            } else {
                url = imagesUrl.get(i - 1);
            }
            imageViews.add(imageView);
            if (imageLoader != null)
                imageLoader.displayImage(context, url, imageView);
            else
                Log.e(tag, "Please set images loader.");
        }
    }

    /**
     * 传入的对象是ImageView的实例，将imageView转换为ImageView赋予view；
     * 根据scaleType设置view的类型：CENTER、CENTER_CROP、CENTER_INSIDE、FIT_CENTER、FIT_END、FIT_START、FIT_XY、MATRIX
     */
    private void setScaleType(View imageView) {
        if (imageView instanceof ImageView) {//imageView是否是ImageView的实例，是返回true
            ImageView view = ((ImageView) imageView);//
            switch (scaleType) {
                case 0:
                    view.setScaleType(ScaleType.CENTER);//
                    break;
                case 1:
                    view.setScaleType(ScaleType.CENTER_CROP);
                    break;
                case 2:
                    view.setScaleType(ScaleType.CENTER_INSIDE);
                    break;
                case 3:
                    view.setScaleType(ScaleType.FIT_CENTER);
                    break;
                case 4:
                    view.setScaleType(ScaleType.FIT_END);
                    break;
                case 5:
                    view.setScaleType(ScaleType.FIT_START);
                    break;
                case 6:
                    view.setScaleType(ScaleType.FIT_XY);
                    break;
                case 7:
                    view.setScaleType(ScaleType.MATRIX);
                    break;
            }

        }
    }

    /**
     * 创建指示器LinearLayout中的视图view：
     * 1、List<ImageView> indicatorImages  清空
     * 2、LinearLayout indicator 移除所以视图
     * 3、LinearLayout indicatorInside 移除所以视图
     * 4、count循环
     *       新建ImageView对象，定义图像为同一缩放图像；
     *       LinearLayout参数：mIndicatorWidth,mIndicatorHeight;
     *                         mIndicatorMargin，mIndicatorMargin
     *       设置imageView资源内容：i=0时为R.drawable.gray_radius；其他为R.drawable.white_radius；
     *       List<ImageView> indicatorImages添加imageView；
     *       圆形指示器和圆形指示器+标题，LinearLayout indicator增加视图（imageView和LinearLayout参数）
     *       圆形指示器+标题 水平，LinearLayout indicatorInside添加视图（imageView和LinearLayout参数）
     */
    private void createIndicator() {
        indicatorImages.clear();
        indicator.removeAllViews();
        indicatorInside.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ScaleType.CENTER_CROP);//控制图像的大小或移动方式以匹配此ImageView的大小
            //统一缩放图像 (保持图像的长宽比), 使图像的尺寸 (宽度和高度) 大于视图的相应尺寸 (不包括内边距（padding）)。

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
            params.leftMargin = mIndicatorMargin;
            params.rightMargin = mIndicatorMargin;
            if (i == 0) {
                imageView.setImageResource(mIndicatorSelectedResId);//设置drawable：R.drawable.gray_radius作为imageView内容
            } else {
                imageView.setImageResource(mIndicatorUnselectedResId);//设置drawable：R.drawable.white_radius作为imageView内容
            }
            indicatorImages.add(imageView);
            if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ||
                    bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE)
                indicator.addView(imageView, params);
            else if (bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                indicatorInside.addView(imageView, params);
        }
    }

    /**
     * 设置viewPager适配器，进行轮播：
     *   1、adapter=null，新建一个实例adapter，
     *   2、viewPager添加适配器adapter，设置此视图可以接收焦点，选择当前为1的page；
     *   3、设置子视图怎么放置；
     *   4、isScroll=true，count>1,设置为可轮播；
     *   5、isAutoPlay=true，
     *   6、开启轮播；
     */
    private void setData() {
        currentItem = 1;
        if (adapter == null) {
            adapter = new BannerPagerAdapter();
            viewPager.addOnPageChangeListener(this);//添加一个侦听器，该侦听器将在页面更改或增量滚动时被调用
        }
        viewPager.setAdapter(adapter);//设置一个PagerAdapter，根据需要为该viewPager提供视图。
        viewPager.setFocusable(true); //设置此视图是否可以接收焦点
        viewPager.setCurrentItem(1);//设置当前选择的page，1
        if (gravity != -1)
            indicator.setGravity(gravity);//设置子视图怎么放置。
        //设置子视图怎么放置。默认为GRAVITY_TOP。
        // 如果是一个VERTICAL方位的布局，这个方法控制所有子视图的放置（如果它有额外的垂直空间）。
        // 如果是一个HORIZONTAL方位的布局，这个方法控制子项的对齐。
        if (isScroll && count > 1) {
            viewPager.setScrollable(true);
        } else {
            viewPager.setScrollable(false);
        }
        if (isAutoPlay)
            startAutoPlay();
    }

    /**
     * 开启自动播放：
     * 1、删除挂起的Runnabel task；
     * 2、delayTime后执行task任务；即进行轮播
     */
    public void startAutoPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);//2s
    }

    /**停止轮播*/
    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }

    /**先移除task，然后立即执行task：开始轮播*/
    public void startAutoPlayNext() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, 0);
    }

    /**
     * task任务：
     *    count>1,isAutoPlay=true;
     *     1、2、3...count、1、2、3...轮播page
     */
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;//  1、2、3...count、1、2、3...
//                Log.i(tag, "curr:" + currentItem + " count:" + count);
                if (currentItem == 1) {
                    viewPager.setCurrentItem(currentItem, false);
                    //设置当前的page；false：立即过渡到；true：平滑滚动
                    handler.post(task);//继续运行task
                } else {
                    viewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task, delayTime);//延迟delayTime运行task，默认2s
                }
            }
        }
    };

    /**
     * 将触摸屏运动事件传递给目标视图:
     * isAutoPlay = true:
     *    action为ACTION_UP、ACTION_CANCEL、ACTION_OUTSIDE，开始轮播
     *    action为ACTION_DOWN，停止轮播
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i(tag, ev.getAction() + "--" + isAutoPlay);
        if (isAutoPlay) {
            int action = ev.getAction();//返回正在执行的动作
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 返回真实的位置
     *
     * @param position
     * @return 下标从0开始
     */
    public int toRealPosition(int position) {
        int realPosition = (position - 1) % count;
        if (realPosition < 0)
            realPosition += count;
        return realPosition;
    }

    /**
     * 实现滑动的效果
     * 继承pageradapter，至少必须重写下面的四个方法
     *   1. instantiateItem(ViewGroup, int)
     *   2. destroyItem(ViewGroup, int, Object)
     *   3. getCount()
     *   4. isViewFromObject(View, Object)
     */
    class BannerPagerAdapter extends PagerAdapter {

        //// 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
        @Override
        public int getCount() {
            return imageViews.size();
        }

        // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(imageViews.get(position)); //我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回
            View view = imageViews.get(position);//获得imageView列表中position位置的view
            if (bannerListener != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(tag, "你正在使用旧版点击事件接口，下标是从1开始，" +
                                "为了体验请更换为setOnBannerListener，下标从0开始计算");
                        bannerListener.OnBannerClick(position);
                    }
                });
            }
            if (listener != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.OnBannerClick(toRealPosition(position));
                        //MainPageFragment类中：img_show.setOnBannerListener(new OnBannerListener()
                        //ScreenPageFragment类中：screenbanner.setOnBannerListener(new OnBannerListener()
                    }
                });
            }
            return view;
        }

        // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    /**
     *  页面状态改变时调用，设置当前页面，用来进行无限轮播：
     *  页面状态分为静止、滑动时和滑动后：
     *     SCROLL_STATE_IDLE：空闲状态，
     *         0号page时候，设置当前page为count；
     *         count+1号page时，设置当前page为1；
     *     SCROLL_STATE_DRAGGING：滑动状态
     *         当滑到0号page时，设置当前page为count；
     *         当滑到count+1号page时，设置当前page为1；
     *         举例：coutn=3
     *            0     1     2     3     4
     *           第三  第一  第二  第三  第一
     *           当item=0(第三)时，设置item=3(第三)，next为item=4(第一)；
     *           当item=4(第一)时，设置item=1(第一)，next为item=2(第二)，再next为item=3(第三)；
     *     SCROLL_STATE_SETTLING：滑动后自然沉降的状态
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
//        Log.i(tag,"currentItem: "+currentItem);
        switch (state) {
            case 0://No operation  //空闲状态
                if (currentItem == 0) {
                    viewPager.setCurrentItem(count, false);
                } else if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                }
                break;
            case 1://start Sliding
                if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                } else if (currentItem == 0) {
                    viewPager.setCurrentItem(count, false);
                }
                break;
            case 2://end Sliding
                break;
        }
    }

    /**
     * 当页面在滑动时至滑动被停止之前，此方法会一直调用
     * 三个参数：
     *   当前页面，及你点击滑动的页面
     *   当前页面偏移的百分比
     *   当前页面偏移的像素位置
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(toRealPosition(position), positionOffset, positionOffsetPixels);
        }
    }

    /**
     * videoViewlayout背景颜色设置为透明
     * videoView为不可视
     * 设置为可轮播
     * 开始轮播
     */
    private void showNext() {
        LogUtil.test("showNext");
        videoViewlayout.setBackgroundColor(Color.TRANSPARENT);
        videoView.setVisibility(View.GONE);
        isAutoPlay(true);
        startAutoPlayNext();
    }

    /**
     *  根据文件url路径，判断文件是否是vedio：
     *  是vedio：
     *      停止轮播，自动轮播取消，设置vedio的url路径，播放vedio；
     *      设置vedioView的layout颜色为黑色，vedioView为可视；
     *      播放完成回调：
     *         只有一个vedio：播放完一个循环播放；
     *         还有其他vedio：开始轮播；
     *      播放错误回调：直接轮播
     *   不是vedio：
     *      videoView不可视；videoViewlayout透明；
     */
    private void showItem(int position) {

        if (MediaFile.isVideoFileType(imageUrls.get(position).getUrl()) && !videoView.isPlaying()) {
            stopAutoPlay();
            isAutoPlay(false);
            videoView.setVideoPath(imageUrls.get(position).getUrl());
            videoView.start();
            videoViewlayout.setBackgroundColor(Color.BLACK);
            videoView.setVisibility(View.VISIBLE);
            //播放完成回调
            videoView.setOnCompletionListener(new IjkMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer mediaPlayer) {
                    if (imageUrls.size() == 1 && MediaFile.isVideoFileType(imageUrls.get(0).getUrl())) {
                        mediaPlayer.start();//开始播放
                    } else {
                        showNext();
                    }

                }
            });
            //播放错误回调
            videoView.setOnErrorListener(new IjkMediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(IMediaPlayer mediaPlayer, int i, int i1) {
                    showNext();
                    return true;
                }
            });
        } else {
            if (videoView != null && !videoView.isPlaying()) {
                videoView.setVisibility(View.GONE);
                if (videoViewlayout != null) {
                    videoViewlayout.setBackgroundColor(Color.TRANSPARENT);
                }
            }

        }
    }

    /**
     * 停止轮播，videoVIew挂起并释放对象
     * imageUrls、imageViews、indicatorImages清空；
     */
    public void release() {
        stopAutoPlay();
        if (videoView != null) {
            onPause();
            videoView.suspend();
        }
        this.imageUrls.clear();
        this.imageViews.clear();
        this.indicatorImages.clear();
        this.count = this.imageUrls.size();
    }

    /**videoView可视：开始播放；不可视则自动轮播*/
    public void onResume() {
        if (videoView.getVisibility() == View.VISIBLE) {
            videoView.start();
        } else {
            startAutoPlay();
        }
    }

    /**video正在播放则pause，并且停止轮播*/
    public void onPause() {
        if (videoView.isPlaying()) {
            videoView.pause();
        }
        stopAutoPlay();
    }

    /**
     *  页面跳转完后调用：
     *  1、获取当前页面的位置编号；
     *  2、playVideo=true:
     *       播放当前位置的视频，并进行完成回调和播放错误回调；继续轮播
     *  3、指示器为圆形类的：圆形指示器，圆形指示器+标题，圆形指示器+标题（水平）
     *     当前的指示器为选择的加颜色，上一个指示器为灰色的
     *  4、position=0时，position=count；position>count，position=1；
     *  5、根据指示器类型，选择指示器的文本显示内容
     */
    @Override
    public void onPageSelected(int position) {
        currentItem = position;  //当前选中的页面的位置编号
//        if (mOnPageChangeListener != null) {
//            mOnPageChangeListener.onPageSelected(toRealPosition(position));
//        }
        if (playVideo) {
            showItem(toRealPosition(position));
        }
        if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ||
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE ||
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE) {
            indicatorImages.get((lastPosition - 1 + count) % count).setImageResource(mIndicatorUnselectedResId);
            indicatorImages.get((position - 1 + count) % count).setImageResource(mIndicatorSelectedResId);
            lastPosition = position;
        }
        if (position == 0) position = count;
        if (position > count) position = 1;
        switch (bannerStyle) {
            case BannerConfig.CIRCLE_INDICATOR:
                break;
            case BannerConfig.NUM_INDICATOR:
                numIndicator.setText(position + "/" + count);
                break;
            case BannerConfig.NUM_INDICATOR_TITLE:
                numIndicatorInside.setText(position + "/" + count);
                bannerTitle.setText(titles.get(position - 1));
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE:
                bannerTitle.setText(titles.get(position - 1));
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE:
                bannerTitle.setText(titles.get(position - 1));
                break;
        }

    }

    @Deprecated
    public MyBanner setOnBannerClickListener(OnBannerClickListener listener) {
        this.bannerListener = listener;
        return this;
    }

    /**
     * 废弃了旧版接口，新版的接口下标是从1开始，同时解决下标越界问题
     *
     * @param listener
     * @return
     */
    public MyBanner setOnBannerListener(OnBannerListener listener) {
        this.listener = listener;
        return this;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    /**删除回调的任何挂起的帖子，并发送obj为令牌的消息。如果令牌为空，则将删除所有回调和消息*/
    public void releaseBanner() {
        handler.removeCallbacksAndMessages(null);
    }
}
