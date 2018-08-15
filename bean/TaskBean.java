package com.fancoff.coffeemaker.bean;

import com.fancoff.coffeemaker.net.RequstBean.HeadBeatBean;

/**
 * Created by apple on 2018/2/9.
 */

/**
 * 任务类数据封装：
 *    1、 任务type：清洗、截屏、app日志、app更新、饮品更新、获取机器和咖啡工艺、制作视频、top视频
 *     top图片、屏保图片、秒杀；
 *    2、HeadBeatBean数据封装：
 *    3、id为服务端任务流水号；result成功失败或取消(1=成功 0=失败 2=取消)；obj截屏或日志内容；
 */
public class TaskBean {
    public final static int RESULT_OK = 1;//
    public final static int RESULT_FAILED = 0;//
    public final static int TASK_TYPE_CLEAN = 249;//清洗
    public final static int TASK_TYPE_CUT_SCREEN = 248;//截屏幕
    public final static int TASK_TYPE_APP_UPDATE = 247;//app更新
    public final static int TASK_TYPE_COFFEE_MATION = 246;//获取机器工艺
    public final static int TASK_TYPE_COFFEE_CONFIG = 245;//获取咖啡工艺
    public final static int TASK_TYPE_GOODS = 244;//  商品
    public final static int TASK_TYPE_VIDEO_MAKING = 243;//  制作视频
    public final static int TASK_TYPE_VIDEO_TOP = 242;//  top视频
    public final static int TASK_TYPE_IMAGE_TOP = 241;//  top图片
    public final static int TASK_TYPE_IMAGE_SCREENSAVER = 240;//  屏保图片
    public final static int TASK_TYPE_APPLOG = 250;//  app日志
    public final static int TASK_TYPE_IMAGE_VEDIO = 111;//
    public final static int TASK_TYPE_SKILL = 318;//  秒杀
    boolean isRunging;
    int type;
    int id;
    String value;     //有用来存日期，app更新的url，咖啡工艺版本，机器工艺版本；
    int result = -1;  //状态
    boolean upload;//估计是否在上传
    String obj;    //有用来存日志，app更新版本
    HeadBeatBean headBeatBean;

    public HeadBeatBean getHeadBeatBean() {
        return headBeatBean;
    }

    public void setHeadBeatBean(HeadBeatBean headBeatBean) {
        this.headBeatBean = headBeatBean;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public boolean isRunging() {
        return isRunging;
    }

    public void setRunging(boolean runging) {
        isRunging = runging;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResult() {
        return result;
    }

    public void ok() {
        this.result = RESULT_OK;
    }

    public void failed() {
        this.result = RESULT_FAILED;
    }

    public void setResult(int result) {
        this.result = result;
    }


    public TaskBean(int type, int id, String value) {
        this.type = type;
        this.id = id;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasResult() {
        return result >= 0;
    }

    /**
     * isRunging=false,result<0,upload=false;则返回true；
     */
    public boolean hasNoThisTask() {
        return !isRunging()
                && !hasResult()
                && !isUpload();
    }

    @Override
    public String toString() {
        return "TaskBean{" +
                "isRunging=" + isRunging +
                ", type=" + type +
                ", id=" + id +
                ", value='" + value + '\'' +
                ", result=" + result +
                ", upload=" + upload +
                ", obj='" + obj + '\'' +
                ", headBeatBean=" + headBeatBean +
                '}';
    }
}
