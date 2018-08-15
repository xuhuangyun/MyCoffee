package com.fancoff.coffeemaker.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.Application.DeviceInfo;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.CheckHotBean;
import com.fancoff.coffeemaker.bean.VMCState;
import com.fancoff.coffeemaker.io.CMDUtil;
import com.fancoff.coffeemaker.net.download.AppUpdateUtil;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.MyButton;
import com.fancoff.coffeemaker.ui.commomView.MyCheckBox;
import com.fancoff.coffeemaker.ui.commomView.MyRadioButton;
import com.fancoff.coffeemaker.ui.commomView.MyRadioGroup;
import com.fancoff.coffeemaker.ui.commomView.MySeekBar;
import com.fancoff.coffeemaker.ui.commomView.MyTextView;
import com.fancoff.coffeemaker.utils.SizeUtil;
import com.fancoff.coffeemaker.utils.StringUtil;
import com.fancoff.coffeemaker.utils.audio.AudioUtil;
import com.fancoff.coffeemaker.utils.rx.RxAppTool;
import com.fancoff.coffeemaker.utils.rx.RxSPTool;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;

/**
 * 工程菜单界面
 */
public class SettingPageFragment extends BaseFragment implements SettingPageContract.ISettingPageView {
    MyCheckBox checkBox;
    MyButton btn_cancle, btn_clear, btn_unlock, btn_updte, btn_clear_error, btn_set;
    //    private SettingPagePresent mainPresent;
    MyTextView t_version, t_mac, t_state, t_error, t_temp, t_coffeeId, t_vmcId, t_config, t_hotstate,t_seekBarTitle;
    MySeekBar seekBar;
    MyRadioButton rb_auto, rb_ahand;
    MyRadioGroup myRadioGroup;

    public SettingPageFragment() {
    }

    public static SettingPageFragment newInstance(Bundle args) {
        SettingPageFragment fragment = new SettingPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SettingPageFragment newInstance() {
        return newInstance(null);
    }

    AudioManager mAudioManager;

    /**
     * 1、新建一个PlayThread，频率为800；
     * 2、获取当前的音量值；并在seekBar中设置当前的音量值；
     * 3、t_version设置当前app版本；t_mac设置设备id；
     * 4、使能加热显示当前的加热状态；并设置点击事件监听；
     * 5、更新工程菜单的内容；
     */
    @Override
    public void initData() {
        AudioUtil.getInstance().playSound(800);
//        mainPresent = new SettingPagePresent(this);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBar.setProgress(currentVolume);
        t_version.setText(RxAppTool.getAppVersionName(MyApp.getIns()));
        t_mac.setText(DeviceInfo.getInstance().getDeviceId());
        checkBox.setOnCheckedChangeListener(null);  //使能加热按钮
        checkBox.setChecked(CheckHotBean.getIns().isHot());//显示当前是否加热
        checkBox.setOnCheckedChangeListener(laa);//点击事件监听
        refView(VMCState.getIns());//更新工程菜单的内容
    }

    @Override
    public void onDestroy() {
        AudioUtil.getInstance().release();
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }

    /**根据isChecked设置是否加热*/
    CompoundButton.OnCheckedChangeListener laa = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CheckHotBean.getIns().setHot(isChecked);
        }
    };

    /**
     * 1、得到系统的Audio管理，获得最大的音量；
     * 2、获得个控件的id；
     * 3、获得handCup键的boolean状态：根据boolean状态显示手动或自动；
     *    rb_auto自动落杯监听：
     * 4、CheckBox使能加热监听；
     * 5、seekBar音量监听：
     * 6、隐藏、更新app、清洗、清除错误、设置、开锁等点击事件监听；
     */
    @Override
    public void initView(View v) {
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seekBar = (MySeekBar) v.findViewById(R.id.seekBar);
        t_seekBarTitle = (MyTextView) v.findViewById(R.id.t_seekBarTitle);
        t_config = (MyTextView) v.findViewById(R.id.t_config);
        t_state = (MyTextView) v.findViewById(R.id.t_state);
        t_error = (MyTextView) v.findViewById(R.id.t_error);
        t_hotstate = (MyTextView) v.findViewById(R.id.t_hotstate);
        t_temp = (MyTextView) v.findViewById(R.id.t_temp);
        btn_set = (MyButton) v.findViewById(R.id.btn_set);
        t_coffeeId = (MyTextView) v.findViewById(R.id.t_coffeeId);
        t_vmcId = (MyTextView) v.findViewById(R.id.t_vmcId);
        checkBox = (MyCheckBox) v.findViewById(R.id.checkBox);
        rb_auto = (MyRadioButton) v.findViewById(R.id.rb_auto);
        rb_ahand = (MyRadioButton) v.findViewById(R.id.rb_hand);
        btn_updte = (MyButton) v.findViewById(R.id.btn_updte);
        btn_clear_error = (MyButton) v.findViewById(R.id.btn_clear_error);
        btn_unlock = (MyButton) v.findViewById(R.id.btn_unlock);
        myRadioGroup = (MyRadioGroup) v.findViewById(R.id.rg);
        btn_cancle = (MyButton) v.findViewById(R.id.btn_cancle);
        btn_clear = (MyButton) v.findViewById(R.id.btn_clear);
        t_version = (MyTextView) v.findViewById(R.id.t_version);
        t_mac = (MyTextView) v.findViewById(R.id.t_mac);

        if(MyApp.IOTEST){
            t_seekBarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            t_config.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            t_state.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            t_error.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            t_hotstate.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            t_temp.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            btn_set.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            t_coffeeId.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            rb_auto.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            rb_ahand.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            btn_updte.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            btn_clear_error.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            btn_unlock.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            t_vmcId.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            btn_cancle.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            btn_clear.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            t_version.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());
            t_mac.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());

            t_seekBarTitle.setPadding(0,10,0,10);
            t_config.setPadding(0,10,0,10);
            t_state.setPadding(0,10,0,10);
            t_error.setPadding(0,10,0,10);
            t_hotstate.setPadding(0,10,0,10);
            t_temp.setPadding(0,10,0,10);
            checkBox.setPadding(0,10,0,10);
            t_coffeeId.setPadding(0,10,0,10);
            checkBox.setPadding(0,10,0,10);
            rb_auto.setPadding(0,10,0,10);
            rb_ahand.setPadding(0,10,0,10);
            t_vmcId.setPadding(0,10,0,10);
            t_version.setPadding(0,10,0,10);
            t_mac.setPadding(0,10,0,10);



        }

        boolean handcup = RxSPTool.getBoolean(getActivity(), "handCup");
        if (handcup) {
            rb_ahand.setChecked(true);
            rb_auto.setChecked(false);
        } else {
            rb_ahand.setChecked(false);
            rb_auto.setChecked(true);
        }
        rb_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RxSPTool.putBoolean(getActivity(), "handCup", !isChecked);
                rb_ahand.setChecked(!isChecked);
            }
        });

        checkBox.setOnCheckedChangeListener(laa);
        seekBar.setMax(maxVolume);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekBar.getProgress(), 0);

            }
        });
        //隐藏：关闭工程菜单
        RxView.clicks(btn_cancle).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                cancle();

            }
        });
        //更新：打开apk文件
        RxView.clicks(btn_updte).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                AppUpdateUtil.getIns().installLoacalApk();
            }
        });
        //清洗：并发送清洗指令
        RxView.clicks(btn_clear).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                CMDUtil.getInstance().clean(null);
            }
        });
        //开锁
        RxView.clicks(btn_unlock).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                CMDUtil.getInstance().openDoor(true, true, null);
            }
        });
        //清除错误：发送清除错误指令
        RxView.clicks(btn_clear_error).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                CMDUtil.getInstance().clearError(null);
            }
        });
        //设置:启动设置界面
        RxView.clicks(btn_set).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                MyApp.getIns().intentToSetting(getActivity());
            }
        });

        btn_updte.setEnabled(false);

    }

    @Override
    public boolean pauseMainPage() {
        return false;
    }

    /**
     * 更新工程菜单的内容：
     * 1、设置btn_updte不使能；
     * 2、正在下载app：
     *       则btn_updte显示：新版本下载中+下载进度；
     *    没在下载app：
     *       获取apk版本号：版本号不为空，设置文本内容“安装新版本+版本号”，设置btn_updte使能；
     *                      版本号为空，设置文本内容“无新版本”；
     * 3、t_version显示app版本号；t_tmep显示当前温度；t_hotstate显示加热状态；t_state显示咖啡机状态；
     *    t_error显示错误状态；t_config显示设备id；t_mac显示工艺文件版本；t_coffeeid显示咖啡机版本；
     *    t_vmcId显示vmc版本；
     */
    public void refView(VMCState vmcStateBean) {
        btn_updte.setEnabled(false);//设置此视图的使能状态
        if (AppUpdateUtil.getIns().isDowning()) {
            btn_updte.setText("新版本下载中" + AppUpdateUtil.getIns().getProgress() + "%");
        } else {
            String version = AppUpdateUtil.getIns().hasLocalUpdateApk();
            if (!StringUtil.isStringEmpty(version)) {
                btn_updte.setText("安装新版本（" + version + "）");
                btn_updte.setEnabled(true);
            } else {
                btn_updte.setText("无新版本");
            }
        }
        t_version.setText("app版本：" + RxAppTool.getAppVersionName(MyApp.getIns()));
        t_temp.setText("当前温度(锅炉/咖啡机)：" + DataCenter.getInstance().getAssistTemp() + "/" + DataCenter.getInstance().getMainTemp());
        t_hotstate.setText("加热状态(锅炉/咖啡机)：" + (DataCenter.getInstance().isHotAssistOn() ? "开" : "关") + "/" + (DataCenter.getInstance().isHotMainOn() ? "开" : "关"));

        t_state.setText("主咖啡状态：" + "准备就绪");

        t_error.setText("错误状态：" + DataCenter.getInstance().getErrorState());
        if (MyApp.MAC) {
            t_config.setText("mac地址：" + DeviceInfo.getInstance().getDeviceId());
        } else {
            t_config.setText("设备id：" + DeviceInfo.getInstance().getDeviceId());
        }

        t_mac.setText("工艺文件版本：" + DataCenter.getInstance().getCoffee());
        t_coffeeId.setText("咖啡机版本：" + vmcStateBean.getCoffeeVersion());
        t_vmcId.setText("VMC版本：" + vmcStateBean.getVmcVersion());
    }

    boolean runCancle;

    /**
     * 隐藏工程菜单：
     * 发送关闭工程菜单命令给VMC
     */
    public void cancle() {
        if (runCancle) {
            return;
        }
        runCancle = true;
        CMDUtil.getInstance().cancleDebug(null);
        runCancle = false;
    }
}
