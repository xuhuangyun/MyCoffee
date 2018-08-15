package com.fancoff.coffeemaker.Application;

import com.fancoff.coffeemaker.utils.SdCardUtil;
import com.fancoff.coffeemaker.utils.StringUtil;

import java.io.File;

/**
 * Coffee_Logs：目录下用来存机器运行的log；每天一个log文件；
 * theme：目录下用来存放公司logo、营业执照、食品流通许可证、CE证书、工商备案表等；
 * menu_icons：目录存放“咖啡类”、“奶、奶茶类”、“餐包类”，菜单下的饮品图标；
 * screen_photos：目录下存放屏保图片；
 * video：目录下存放区域1中播放的视频；
 * video_photos：目录下用来存放区域1中播放的图片；
 * info_photos：目录下用来存放区域3中左边部分的图片；
 * coffeeConfig.xml：饮品配置表；
 * machineConfig.xml：机器配置表；
 */

public class FilesManage {
    public static final String ROOT_DRI = "coffee";
    public static final String ROOT_PATH = SdCardUtil.getNormalSDCardPath() + "/" + ROOT_DRI;
    public static File f = new File(ROOT_PATH + "/screen_shoot/screen.png");

    public static File getScreenFilePath() {
        return f;
    }

    /**  路径/2012010101.txt */
    public static String getLogPath(String date) {
        if (date.length() == 10) {
            return dri.logs + "/" + date.substring(0, 8) + "/" + date + ".txt";
        } else {
            return dri.logs + "/" + date.substring(0, 8) + "/" + date + "00" + ".txt";
        }

//        if (date.length() == 10) {
//            return dri.logs + "/" + date + ".txt";
//        } else {
//            return dri.logs + "/" + date + "00" + ".txt";
//        }

    }

    public static boolean isGIf(String url) {
        if(!StringUtil.isStringEmpty(url)&&url.toLowerCase().endsWith("gif")){
            return true;
        }
        return false;
    }

    public static class dri {
        public static final String image = ROOT_PATH + "/image";
        //        public static final String menu_icons = ROOT_PATH + "/menu_icons";
        public static final String video = ROOT_PATH + "/video";
        //        public static final String video_photos = ROOT_PATH + "/video_photos";
//        public static final String info_photos = ROOT_PATH + "/info_photos";
        public static final String logs = ROOT_PATH + "/coffee_logs";
        public static final String db = ROOT_PATH + "/db";
        public static final String crashlogs = logs + "/crash";
    }

    public static class path {
        public static final String apk_path = ROOT_PATH + "/apk_update" + "/coffemaker.apk";
        public static final String coffeeConfig = ROOT_PATH + "/coffeeConfig.json";
        public static final String machineConfig = ROOT_PATH + "/machineConfig.xml";


    }

    public static String getCoffeeConfig() {
        return path.coffeeConfig;
    }

    public static String getMachineConfig() {
        return path.machineConfig;
    }

}
