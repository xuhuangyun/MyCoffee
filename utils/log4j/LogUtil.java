package com.fancoff.coffeemaker.utils.log4j;

import com.fancoff.coffeemaker.Application.FilesManage;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.utils.rx.RxDataTool;
import com.fancoff.coffeemaker.utils.rx.RxFileTool;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by apple on 2017/12/1.
 * 打印日志工具类
 */

public class LogUtil {
    static LogUtil logUtil;
    int maxlen = 10;//保存10天

    public static LogUtil getIns() {
        if (logUtil == null) {
            logUtil = new LogUtil();
        }
        return logUtil;
    }

    /**************vmc日记1********************/
    public static void vmc(String msg) {
        log("DEBUG_VMC:", msg);
    }

    /**************vmc日记2********************/
    public static void vmc(byte[] msg) {
        String mm = RxDataTool.bytes2HexStringWithEmpty(msg);
        log("DEBUG_VMC:", mm);
        mm = null;
    }

    /**************vmc日记3********************/
    public static void vmc(String tag, byte[] msg) {
        String mm = RxDataTool.bytes2HexStringWithEmpty(msg);
        log("DEBUG_VMC:", tag + RxDataTool.bytes2HexStringWithEmpty(msg));
        mm = null;
    }

    static long time = 0;
    static int Vmccount;

    /**time=0;Vmccount=0*/
    public static void timeToLogVmc() {
        time = 0;
        Vmccount = 0;
    }

    public static void timeToLogNet() {
        timeNET = 0;
        VmccountNET = 0;
    }

    static long timeNET = 0;
    static int VmccountNET;

    /**************网络日志********************/
    public static void debugNet(String msg, long mcount) {
        long timenow = System.currentTimeMillis();
        if (timenow - timeNET >= mcount) {
            log("DEBUG_OKHTTP:", msg);
            timeNET = timenow;
            VmccountNET = 0;
        }
        VmccountNET++;
    }

    /**************vmc日记3美隔多少毫秒打印一次********************/
    public static void vmc(String tag, byte[] msg, long mcount) {
        long timenow = System.currentTimeMillis();
        if (timenow - time >= mcount) {
            if (Vmccount > 0) {
                log("DEBUG_VMC" +
                        "（空闲状态，" +
                        mcount / 1000 + "秒打印一次）" + ":", tag + RxDataTool.bytes2HexStringWithEmpty(msg));
            } else {
                log("DEBUG_VMC:", tag + RxDataTool.bytes2HexStringWithEmpty(msg));
            }
            time = timenow;
            Vmccount = 0;
        }
        Vmccount++;

    }

    /**************操作日记********************/
    public static void debug(String msg) {
        log("DEBUG_ACTION:", msg);
    }


    /**************网络日志  DEBUG_OKHTTP:msg ********************/
    public static void debugNet(String msg) {
        log("DEBUG_OKHTTP:", msg);
    }


    /**************代码报错日志********************/
    public static void error(String msg) {
        log("DEBUG_ERROR:", msg);
    }

    /**************操作日志 DEBUG_ACTION:msg ********************/
    public static void action(String msg) {
        log("DEBUG_ACTION:", msg);
    }

    /**************日志输出工具********************/
    /**
     * 调用MyApp.getIns().getgLogger()获得记录器，然后error方法打印日志；
     */
    private static void log(String tag, String msg) {
//        if (MyApp.DEBUG_LOG) {
//            Logger.e(tag + msg);
//        } else {
        MyApp.getIns().getgLogger().error(tag + msg);
//        }
    }

    public static void test(String msg) {
        if (MyApp.DEBUG_LOG) {
//            Logger.e(msg);
            MyApp.getIns().getgLogger().error("DEBUG_TEST：" + msg);
        }

    }

    File driLogd;

    /**
     * 将文件按时间降序排列
     */
    class FileComparator implements Comparator<File> {

        @Override
        public int compare(File file1, File file2) {
            if (file1.lastModified() < file2.lastModified()) {
                return -1;// 最后修改的文件在后
            } else {
                return 1;
            }
        }
    }

    /**
     * 清理日志：感觉是先删除文件，然后返回目录
     * 1、File driLogd=null,新建路径为ROOT_PATH + "/coffee_logs"的File；
     * 2、
     */
    public File[] isCanClear() {
        if (driLogd == null) {
            driLogd = new File(FilesManage.dri.logs);
        }
        File[] files = driLogd.listFiles(new FileFilter() {//FileFilter传过来的路径
            //返回抽象路径名数组，这些路径名表示此抽象路径名表示的目录中满足指定过滤器的文件和目录
            @Override
            public boolean accept(File pathname) {//FileFilter传过来的路径
                if (pathname.isDirectory()) {//测试此抽象路径名表示的文件是否是一个目录
                    return true;

                } else {
                    pathname.delete();
                    //删除此抽象路径名表示的文件或目录。如果此路径名表示一个目录，则该目录必须为空才能删除。
                    return false;
                }

            }
        });
        if (files != null && files.length > maxlen) {
            return files;
        }
        return null;
    }

    /**清理日志*/
    public void clearLocalLogs(File[] files) {
        Arrays.sort(files, new FileComparator());
        for (int i = 0; i < files.length - maxlen; i++) {
            File ff = files[i];
            if (ff != null && ff.exists()) {
                if (ff.isDirectory()) {
                    RxFileTool.deleteDir(ff);
                }
            }
        }


    }

    boolean runClearLogs;
    /**
     * 日期变化了清除日志
     * 1、runClearLogs=true:return;正在清理，则返回；
     * 2、key_clear_logs标签的清除日志的任务加入到Task中；
     */
    public void clearLogs() {
        if (runClearLogs) {
            return;
        }
        if (MyApp.getIns().isDayChange()) {//日期变化
            final File[] files = isCanClear();
            if (files != null) {
                runClearLogs = true;
                MyApp.getIns().runTask("key_clear_logs", new MyApp.TaskRun() {
                    @Override
                    public void run() {
                        clearLocalLogs(files);
                    }

                    @Override
                    protected void onOk() {
                        runClearLogs = false;
                    }
                });
            }
        }

    }

}
