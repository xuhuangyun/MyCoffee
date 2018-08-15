package com.fancoff.coffeemaker.net.RequstBean;

import com.fancoff.coffeemaker.bean.heatbit.AppLogPushBean;
import com.fancoff.coffeemaker.bean.heatbit.BasePushBean;
import com.fancoff.coffeemaker.bean.heatbit.CoffeePushBean;
import com.fancoff.coffeemaker.bean.heatbit.ConfigBean;
import com.fancoff.coffeemaker.bean.heatbit.ImagesPushBean;
import com.fancoff.coffeemaker.bean.heatbit.UpdatePushBean;
import com.fancoff.coffeemaker.bean.heatbit.VedioPushBean;

/**
 * Created by apple on 2017/11/17.
 */

/**
 * 心跳bean：
 * 心跳时收到的任务：截屏、清洗、app日志、app更新、coffees、图片、视频；
 */
public class HeadBeatBean extends BaseBean {
    boolean changeview;//是否刷新数据 标志符号
    ConfigBean coffee_config;//一个string id，一个继承来的push_id；；继承return_code，return_msg
    ConfigBean machine_config;//
    BasePushBean screenshot;  //截屏只有一个push_id；继承return_code，return_msg
    BasePushBean clean;       //清洗只有一个push_id；继承return_code，return_msg
    AppLogPushBean app_log;    //app日志 String date和继承来的push_id；继承return_code，return_msg
    UpdatePushBean app_update; //app更新 String version;String url;和继承来的push_id；继承return_code，return_msg
    CoffeePushBean coffee;     //coffees ArrayList<CoffeeBean> coffees;和继承来的push_id；继承return_code，return_msg
    ImagesPushBean images;     //左下、top、屏保 的images   3个ArrayList<ImageBean> details;和push_id；继承return_code，return_msg
    VedioPushBean videos;      //top、制作的images    2个ArrayList<ImageBean> details;和push_id；继承return_code，return_msg

    public boolean isNoTask() {
        return coffee_config == null && machine_config == null && screenshot == null &&
                clean == null && app_log == null && app_update == null && coffee == null
                && images == null && videos == null;

    }

    public boolean isChangeview() {
        return changeview;
    }

    public void setChangeview(boolean changeview) {
        this.changeview = changeview;
    }

    public ImagesPushBean getImages() {
        return images;
    }

    public void setImages(ImagesPushBean images) {
        this.images = images;
    }

    public VedioPushBean getVideos() {
        return videos;
    }

    public void setVideos(VedioPushBean videos) {
        this.videos = videos;
    }
    //    JSONArray images;
//    JSONArray videos;
//
//    public JSONArray getImages() {
//        return images;
//    }
//
//    public void setImages(JSONArray images) {
//        this.images = images;
//    }
//
//    public JSONArray getVideos() {
//        return videos;
//    }
//
//    public void setVideos(JSONArray videos) {
//        this.videos = videos;
//    }

    public CoffeePushBean getCoffee() {
        return coffee;
    }

    public void setCoffee(CoffeePushBean coffee) {
        this.coffee = coffee;
    }

    public BasePushBean getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(BasePushBean screenshot) {
        this.screenshot = screenshot;
    }

    public BasePushBean getClean() {
        return clean;
    }

    public void setClean(BasePushBean clean) {
        this.clean = clean;
    }

    public AppLogPushBean getApp_log() {
        return app_log;
    }

    public void setApp_log(AppLogPushBean app_log) {
        this.app_log = app_log;
    }

    public UpdatePushBean getApp_update() {
        return app_update;
    }

    public void setApp_update(UpdatePushBean app_update) {
        this.app_update = app_update;
    }

    public ConfigBean getCoffee_config() {
        return coffee_config;
    }

    public void setCoffee_config(ConfigBean coffee_config) {
        this.coffee_config = coffee_config;
    }

    public ConfigBean getMachine_config() {
        return machine_config;
    }

    public void setMachine_config(ConfigBean machine_config) {
        this.machine_config = machine_config;
    }

    @Override
    public String toString() {
        return "HeadBeatBean{" +
                "coffee_config=" + coffee_config +
                ", machine_config=" + machine_config +
                ", screenshot=" + screenshot +
                ", clean=" + clean +
                ", app_log=" + app_log +
                ", app_update=" + app_update +
                ", coffee=" + coffee +
                ", images=" + images +
                ", videos=" + videos +
                '}';
    }
}
