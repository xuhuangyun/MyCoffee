package com.fancoff.coffeemaker.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;

import java.lang.reflect.Method;
import java.util.List;

/*
    app信息获取类
 */
public class AppInfoUtil {
    /*
         * 杀死后台进程
         */
    public static void killAll(Context context) {

        //获取一个ActivityManager 对象
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取系统中所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager
                .getRunningAppProcesses();
        int count = 0;//被杀进程计数

        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.processName.contains("com.android.system")
                    || appProcessInfo.pid == android.os.Process.myPid())//跳过系统 及当前进程
                continue;
            String[] pkNameList = appProcessInfo.pkgList;//进程下的所有包名
            for (int i = 0; i < pkNameList.length; i++) {
                String pkName = pkNameList[i];
                activityManager.killBackgroundProcesses(pkName);//杀死该进程
                count++;//杀死进程的计数+1
            }
        }

        LogUtil.action("清理进程数量为 : " + count + 1);

    }

    /**
     * 进入ACTION_MAIN
     */
    public static void startMainLauncher(Context context) {
//        Intent mHomeIntent = new Intent(context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        context.startActivity(intent);
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
//        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addCategory("android.intent.category.MYHOME");
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        MyApp.getIns().startActivity(mHomeIntent);
//        try {
//            //虚拟返回按钮
//            Runtime.getRuntime().exec("adb shell input keyevent 3");
//        } catch (Exception e) {
//            LogUtil.error("runtime"+"error");
//        }

    }

    public static void startMainLauncherService(Context context) {
        LogUtil.action("AppInfoUtil:startMainLauncherService");
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory("android.intent.category.MYHOME");
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMain);

    }

    public static void setDefaultHome(Context context, String packeageName, String className, String oldPackage, String oldName, boolean init) {
        PackageManager pm = context.getPackageManager();
        IntentFilter f = new IntentFilter();
        f.addAction(Intent.ACTION_MAIN);
        f.addCategory(Intent.CATEGORY_HOME);
        f.addCategory(Intent.CATEGORY_DEFAULT);

        ComponentName component1 = new ComponentName(packeageName, className);
        ComponentName component2 = new ComponentName(oldPackage, oldName);
        ComponentName[] components = new ComponentName[]{component1, component2};
        pm.clearPackagePreferredActivities(oldPackage);
//        Log.e("AppInfoUtil", "setDefaultHome clearPackagePreferredActivities:" + oldPackage);
        if (init) {
            pm.clearPackagePreferredActivities(packeageName);
//            Log.e("AppInfoUtil", "setDefaultHome clearPackagePreferredActivities:" + packeageName);
        }
        pm.addPreferredActivity(f, IntentFilter.MATCH_CATEGORY_EMPTY, components, component1);
//        Log.e("AppInfoUtil", "setDefaultHome packageName:" + packeageName + "  " + "oldPackage:" + oldPackage);

    }

    public static void killProgress(Context context, String packageName, String className) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.setComponent(new ComponentName(packageName, className));
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMain);

//		Intent startMain = new Intent(Intent.ACTION_MAIN);
//		startMain.addCategory(Intent.CATEGORY_HOME);
//		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(startMain);

        // overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        //finish();
        //android.os.Process.killProcess(android.os.Process.myPid());

    }

    public static String getDefaultlauncherpackage() {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            Method m = cls.getDeclaredMethod("get", String.class, String.class);
            return (String) m.invoke(null, "ro.sw.defaultlauncherpackage", "no");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDefaultlauncherP(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList =
                pm.queryIntentActivities(intent, 0);
        if (resolveInfoList != null) {

            int size = resolveInfoList.size();
            for (int j = 0; j < size; j++) {
                final ResolveInfo resolveInfo = resolveInfoList.get(j);
                if (resolveInfo.activityInfo.packageName.contains("launcher")) {
                    LogUtil.test("getDefaultlauncherclass" + "packageName:" + resolveInfo.activityInfo.packageName + "  " + "classname:" + resolveInfo.activityInfo.name);

                    return resolveInfo.activityInfo.packageName;
                }


            }
        }
        return "";
    }

    public static String getDefaultlauncherclass(Context context, String palauncher) {
        Log.e("getDefaultlauncherclass", "getDefaultlauncherclass1");
        String luacha = "com.android.launcher.Launcher";
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList =
                pm.queryIntentActivities(intent, 0);
        Log.e("getDefaultlauncherclass", "getDefaultlauncherclass2");
        if (resolveInfoList != null) {
            Log.e("getDefaultlauncherclass", "getDefaultlauncherclass3");
            int size = resolveInfoList.size();
            for (int j = 0; j < size; j++) {
                final ResolveInfo resolveInfo = resolveInfoList.get(j);
                Log.e("getDefaultlauncherclass", "getDefaultlauncherclass4");
                if (resolveInfo.activityInfo.packageName.equals(palauncher)) {
                    Log.e("getDefaultlauncherclass", "packageName:" + resolveInfo.activityInfo.packageName + "  " + "classname:" + resolveInfo.activityInfo.name);
                    return resolveInfo.activityInfo.name;
                }


            }
        }
        return luacha;
    }

    public static boolean isAvilible(Context context, String packageName) {

        final PackageManager packageManager = context.getPackageManager();

        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {

            // 循环判断是否存在指定包名
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }

        }
        return false;
    }


    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }

    public static String getPackaename(Context context) {
        return context.getPackageName().replace("package:", "");
    }

    /**
     * 方法描述：判断某一应用是否正在运行
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
//            Log.e("AppInfoUtil", "isAppRunning "+info.baseActivity.getPackageName() +"  packageName:"+ packageName);
            if (info.baseActivity.getPackageName().equals(packageName)) {
//                Log.e("AppInfoUtil", "isAppRunning true");
                return true;
            }
        }
        return false;
    }

    /**
     * 方法描述：判断某一Service是否正在运行*
     *
     * @param context     上下文
     * @param serviceName Service的全路径： 包名 + service的类名
     * @return true 表示正在运行，false 表示没有运行
     */

    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfos) {
            if (serviceInfo.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    public static void putSharedPreferences(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getSharedPreferences(Context context, String key, boolean defValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, defValue);
    }

//	public static void clear(Context context,String pn,String className,String opn,String oclassName) {
//		PackageManager pm = context.getPackageManager();
//		IntentFilter f = new IntentFilter();
//		f.addAction(Intent.ACTION_MAIN);
//		f.addCategory(Intent.CATEGORY_HOME);
//		f.addCategory(Intent.CATEGORY_DEFAULT);
//
//		ComponentName component = new ComponentName(pn,className );
//		ComponentName[] components = new ComponentName[] {new ComponentName(oldPackage,oldName),component};
//
//		pm.clearPackagePreferredActivities(oldPackage);
//		pm.addPreferredActivity(f, IntentFilter.MATCH_CATEGORY_EMPTY, components, component);
//
//	}
}
