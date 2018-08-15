package com.fancoff.coffeemaker.service;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.Application.FilesManage;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.bean.ErrorBean;
import com.fancoff.coffeemaker.bean.MsgEvent;
import com.fancoff.coffeemaker.bean.TaskBean;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.bean.heatbit.AppLogPushBean;
import com.fancoff.coffeemaker.bean.heatbit.BasePushBean;
import com.fancoff.coffeemaker.bean.heatbit.CoffeePushBean;
import com.fancoff.coffeemaker.bean.heatbit.ConfigBean;
import com.fancoff.coffeemaker.bean.heatbit.ImagesPushBean;
import com.fancoff.coffeemaker.bean.heatbit.ObjPushBean;
import com.fancoff.coffeemaker.bean.heatbit.UpdatePushBean;
import com.fancoff.coffeemaker.bean.heatbit.VedioPushBean;
import com.fancoff.coffeemaker.io.CMDUtil;
import com.fancoff.coffeemaker.io.IoCallBack;
import com.fancoff.coffeemaker.net.BaseObserver;
import com.fancoff.coffeemaker.net.NetUtil;
import com.fancoff.coffeemaker.net.RequstBean.CoffeeConfigResultBean;
import com.fancoff.coffeemaker.net.RequstBean.HeadBeatBean;
import com.fancoff.coffeemaker.net.download.AppUpdateUtil;
import com.fancoff.coffeemaker.net.download.JsDownloadListener;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.RxBus;
import com.fancoff.coffeemaker.utils.StringUtil;
import com.fancoff.coffeemaker.utils.rx.RxFileTool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by apple on 2018/3/21.
 * 推送任务接受管理类
 * HashMap<Integer, TaskBean> tasks = new HashMap<Integer, TaskBean>();
 * HashMap<String, ErrorBean> errorMap = new HashMap<String, ErrorBean>();
 */

public class TaskUtil {
    HashMap<Integer, TaskBean> tasks = new HashMap<Integer, TaskBean>();
    HashMap<String, ErrorBean> errorMap = new HashMap<String, ErrorBean>();

    /**返回id代表的任务是否为空，为空返回true*/
    public boolean checkTaskById(int id) {
        return getTasks().get(id) == null;

    }

    public HashMap<Integer, TaskBean> getTasks() {
        return tasks;
    }

    public void setTasks(HashMap<Integer, TaskBean> tasks) {
        this.tasks = tasks;
    }

    public static final TaskUtil ourInstance = new TaskUtil();

    public static TaskUtil getInstance() {
        return ourInstance;
    }

    private TaskUtil() {
    }

    /**
     * 移除已执行任务
     */
    public void removeTask(ArrayList<TaskBean> taskBeens) {
        synchronized (FLAG_TASK) {
            if (taskBeens != null && taskBeens.size() > 0) {
                for (TaskBean task : taskBeens) {
                    TaskUtil.getInstance().getTasks().remove(task.getId());
                    task = null;
                }
            }
        }
    }

    private final static String FLAG_TASK = "pushtask";
    private final static String ERROR_TASK = "errortask";
    ArrayList<TaskBean> pushs = new ArrayList<>();

    /**
     * 得到需要上传的错误列表
     * 1、ArrayList<ErrorBean> errorList清除；
     * 2、遍历HashMap<String, ErrorBean> errorMap：
     *    得到当前的ErrorBean，不为空则添加到错误列表errorList；
     * 3、返回错误列表erroList；
     */
    public ArrayList<ErrorBean> getErrorList() {
        synchronized (ERROR_TASK) {
            errorList.clear();
            for (Map.Entry<String, ErrorBean> entry : errorMap.entrySet()) {
                ErrorBean t = entry.getValue();
                if (t != null) {
                    errorList.add(t);
                }
            }
            return errorList;
        }

    }

    /**
     * 得到需要push的任务列表
     * 1、ArrayList<TaskBean> pushs清除
     * 2、遍历任务：HashMap<Integer, TaskBean> tasks
     *       获取当前遍历到的任务TaskBean
     *       TaskBean.result>=0,没上传；任务添加到pushs中；
     *  3、返回pushs
     */
    public ArrayList<TaskBean> getPushs() {
        synchronized (FLAG_TASK) {
            pushs.clear();
            for (Map.Entry<Integer, TaskBean> entry : TaskUtil.getInstance().getTasks().entrySet()) {
                TaskBean t = entry.getValue();
                if (t.getResult() >= 0 && !t.isUpload()) {
                    pushs.add(t);
                }
            }
            return pushs;
        }
    }

    ArrayList<ErrorBean> errorList = new ArrayList<>();

    /**
     * 增加错误代码和文字到errorMap中
     *    msg为空，则error键对应的值为空
     *    msg不为空，将键error和值ErrorBean传入错误列表errorList
     */
    public void addUploadErrorTask(String error, String msg) {
        synchronized (ERROR_TASK) {
            if (StringUtil.isStringEmpty(msg)) {//msg为空
                errorMap.put(error, null);
            } else {
                ErrorBean e = new ErrorBean();
                e.setCode(error);
                e.setMsg(msg);
                errorMap.put(error, e);
            }

        }
    }


    public void removeErrorTask(ArrayList<ErrorBean> errorList) {
//        synchronized (ERROR_TASK) {
//            if (errorList != null && errorList.size() > 0) {
//                for (ErrorBean task : errorList) {
//                    errorMap.remove(task.getCode());
//                    task = null;
//                }
//            }
//        }
    }

    public interface TaskCallBack {
        void result(int result);
    }

    /**
     * 1、传入咖啡工艺编号、机器工艺编号，向服务器请求工艺参数；
     *    请求成功：咖啡工艺编号不为空
     *        MsgEvent(MyConstant.ACTION.REF_ALL)：消息类型为REF_ALL；发送消息到UI线程（MainActivity中）
     *        调用runtimeToPushTask的实现方法result；
     *    请求失败：调用runtimeToPushTask的实现方法result；
     *  2、coffeeConfigBean存在
     *      2.1、CoffeeConfigBean coffee_config;存在：
     *           更新咖啡工艺表：视频、饮品、存工艺等；
     *      2.2、String machine_config;存在
     *           更新机器工艺，发送到vmc
     */
    public void getConfig(final String coffee, String machine, final TaskCallBack callback) {


        NetUtil.getInstance().actionParam(coffee, machine, new BaseObserver<CoffeeConfigResultBean>() {
            @Override
            public void onHandleSuccess(CoffeeConfigResultBean paramBean) {
                if (!StringUtil.isStringEmpty(coffee)) {
                    RxBus.getInstance().post(new MsgEvent(MyConstant.ACTION.REF_ALL));
                }
                callback.result(TaskBean.RESULT_OK);
            }

            @Override
            public void onHandleStart() {
                super.onHandleStart();
            }

            @Override
            public void onHandleError(int code, String msg) {
                super.onHandleError(code, msg);
                callback.result(TaskBean.RESULT_FAILED);
            }

            @Override
            public void onHandleComplete() {
                super.onHandleComplete();
            }
        }, new Consumer<CoffeeConfigResultBean>() {
            @Override
            public void accept(CoffeeConfigResultBean coffeeConfigBean) throws Exception {
                if (coffeeConfigBean.getIsSuccess()) {
                    if (coffeeConfigBean.getCoffee_config() != null) {
                        DataCenter.getInstance().update(coffeeConfigBean.getCoffee_config());
                    }
                    if (!StringUtil.isStringEmpty(coffeeConfigBean.getMachine_config())) {
                        DataCenter.getInstance().saveMachine_config(coffeeConfigBean.getMachine_config());
                    }

                }
            }
        });

    }

    /**
     * 添加已执行成功任务：
     * isRunning=true；result=RESULT_OK；isRunning=true；
     * 添加任务task到任务列表tasks中；
     */   
    public void addSuccessTask(int id, TaskBean task) {
        task.setRunging(true);
        task.ok();
        task.setRunging(false);
        getTasks().put(id, task);
    }

    /**添加等待执行任务*/
    public void addTask(int id, TaskBean task) {
        getTasks().put(id, task);
    }

    int mprogress;

  /**
     * 执行服务端的推送的任务 1s中执行一次 AppService类的startTime会开启该任务
     * 有任务，遍历任务：
     *     1、从任务列表获得单个任务类TaskBean task
     *     2、任务类型：isRunging=false,result<0,upload=false：
     *        2.1、更新咖啡工艺表：
     *             isRunging=true；调用getConfig方法，并实现接口TaskCallBack的方法result
     *             getConfig方法会发送消息到MAinActivity中，会更新工艺表，会传result给result()方法；
     *        2.2、更新机器工艺表：
     *
     *        2.3、清洗：
     *             将清洗任务添加到任务列表writeList，
     *        2.4、app更新：
     *             调用download方法，实现接口方法
     *        2.5、日志：
     *             日志以string字符串形式放在TaskBean中，TaskBean.obj=content
     *        2.6、截屏：
     *             截屏以string字符串形式放在TaskBean中，TaskBean.obj=content
     */
    public void runtimeToPushTask() {
        synchronized (FLAG_TASK) {

            if (TaskUtil.getInstance().getTasks().size() > 0) {
                for (Map.Entry<Integer, TaskBean> entry : TaskUtil.getInstance().getTasks().entrySet()) {
                    final TaskBean task = entry.getValue();
                    //咖啡工艺:在心跳中收到的设置result=1,isRunning=fals;
                    if (task.getType() == TaskBean.TASK_TYPE_COFFEE_CONFIG
                            && task.hasNoThisTask()) {//isRunging=false,result<0,upload=false;则返回true
                        task.setRunging(true);//TaskBean.isRunging=true
                        getConfig(task.getValue(), "", new TaskCallBack() {
                            @Override
                            public void result(int result) {
                                task.setResult(result);//TaskBean.result=result
                                task.setRunging(false);//TaskBean.isRunning=false
                            }
                        });
                    } else
                        //机器工艺
                        if (task.getType() == TaskBean.TASK_TYPE_COFFEE_MATION
                                && task.hasNoThisTask()) {
                            task.setRunging(true);//TaskBean.isRunging=true
                            getConfig("", task.getValue(), new TaskCallBack() {
                                @Override
                                public void result(int result) {
                                    task.setResult(result);//TaskBean.result=result
                                    task.setRunging(false);//TaskBean.isRunning=false
                                }
                            });

                        }
                        //清洗
                        else if (task.getType() == TaskBean.TASK_TYPE_CLEAN
                                && task.hasNoThisTask()) {
                            task.setRunging(true);//TaskBean.isRunning=true;
                            CMDUtil.getInstance().clean(new IoCallBack() {
                                @Override
                                public void onFailed(byte[] send, int error) {
                                    if (error != IoCallBack.ERROR_TIME_OUT) {
                                        task.ok();//TaskBean.restult=ok
                                    } else {
                                        task.failed();//TaskBean.restult=failed
                                    }
                                    task.setRunging(false);//TaskBean.isRunning=false
                                }

                                @Override
                                public void onSuccess(byte[] send, byte[] rb) {
                                    task.ok();
                                    task.setRunging(false);
                                }
                            });


                        }
                        //app更新
                        else if (task.getType() == TaskBean.TASK_TYPE_APP_UPDATE
                                && task.hasNoThisTask()) {
                            task.setRunging(true);//TaskBean.isRunning=true
                            mprogress = -1;
                            final String filePath = task.getValue();//估计是下载地址
                            AppUpdateUtil.getIns().download(filePath, FilesManage.path.apk_path, new JsDownloadListener() {
                                //实现接口JsDownloadListener方法
                                @Override
                                public void onStartDownload() {
                                    LogUtil.action("update app onStartDownload");
                                    AppUpdateUtil.getIns().setDowning(true);//AppUpdateUtil.isDowning=true
                                }

                                @Override
                                public void onProgress(int progress) {//JsResponseBody中的source方法会传入下载进度值
                                    if (progress != mprogress) {
                                        LogUtil.test("update app onProgress " + progress);//打印app下载进度日志
                                    }
                                    mprogress = progress;
                                    AppUpdateUtil.getIns().setProgress(progress);//AppUpdateUtil.progress=pregress
                                }

                                @Override
                                public void onFinishDownload() {
                                    LogUtil.action("update app onFinishDownload");
                                    task.ok();//TaskBean.ok=RESULT_OK
                                    task.setRunging(false);//TaskBean.isRunning=false
                                    AppUpdateUtil.getIns().setDowning(false);//AppUpdateUtil.isDowning=false
                                }

                                @Override
                                public void onFail(String errorInfo) {
                                    LogUtil.action("update app onFail");
                                    task.failed();//TaskBean.ok=RESULT_FAILED
                                    task.setRunging(false);//TaskBean.isRunning=false
                                    AppUpdateUtil.getIns().setDowning(false);//AppUpdateUtil.isDowning=false
                                }
                            });

                        }
                        //日志
                        else if (task.getType() == TaskBean.TASK_TYPE_APPLOG
                                && task.hasNoThisTask()) {
                            task.setRunging(true);//TaskBean.isRunning=true
                            MyApp.getIns().runTask("key_upload_logs", new MyApp.TaskRun() {
                                @Override
                                public void run() {
                                    String value = task.getValue()
                                            .replace("\\.", "")
                                            .replace("_", "");
                                    File file = new File(FilesManage.getLogPath(value));
//                                    File file = new File(FilesManage.dri.logs + "/" + value + ".txt");
                                    if (file != null && file.exists()) {
                                        String content = RxFileTool.readFile2String(file, "utf-8");//读取日志文件
                                        task.setObj(content);//TaskBean.obj=content
                                    } else {
                                        task.setObj("该日期日志不存在");
                                    }

                                }

                                @Override
                                protected void onError() {
                                    super.onError();
                                    task.failed();//TaskBean.ok=RESULT_FAILED
                                    task.setRunging(false);//isRuning=false
                                }

                                @Override
                                public void onOk() {

                                    task.ok();//TaskBean.ok=RESULT_OK
                                    task.setRunging(false);//isRuning=false
                                }
                            });


                        }
                        //截屏
                        else if (task.getType() == TaskBean.TASK_TYPE_CUT_SCREEN
                                && task.hasNoThisTask()) {
                            task.setRunging(true);
                            MyApp.getIns().runTask("key_cut_screen", new MyApp.TaskRun() {
                                @Override
                                public void run() {
                                    String bitbase64 = MyApp.getIns().screenShot(FilesManage.getScreenFilePath());
                                    task.setObj(bitbase64);
                                }

                                @Override
                                protected void onError() {
                                    super.onError();
                                    task.failed();
                                    task.setRunging(false);
                                }

                                @Override
                                public void onOk() {
                                    task.ok();
                                    task.setRunging(false);
                                }
                            });

                        }
//                        else if (task.getType() == TaskBean.TASK_TYPE_IMAGE_VEDIO
//                                && task.hasNoThisTask()) {
//                            task.setRunging(true);
//                            task.ok();
//                            task.setRunging(false);
//                        }
                }

            }
        }
    }

    /**
     * 检查心跳中的任务：
     * 1、有咖啡工艺或机器工艺任务到tasks；
     * 2、有清洗、截屏任务，则添加到tasks中；
     * 3、有上传日志任务，添加到tasks；
     * 4、有更新app任务，添加到tasks；
     * 5、本地有咖啡工艺，
     */
    public boolean checkTask(HeadBeatBean headBeatBean) {
        synchronized (FLAG_TASK) {
            checkTechConfig(headBeatBean);//异步添加【机器工艺，咖啡工艺】 异步任务
            checkCommomPush(headBeatBean.getClean(), TaskBean.TASK_TYPE_CLEAN);//添加【清洗】异步任务
            checkCommomPush(headBeatBean.getScreenshot(), TaskBean.TASK_TYPE_CUT_SCREEN);//添加【截屏】异步任务
            checkLog(headBeatBean);//添加【日志上传】异步任务到
            checkUpdate(headBeatBean);//添加【更新app】异步任务

            //工艺数据更新（图片 视频 秒杀 促销 停售），只在本地已有工艺表的情况下更新
            if (DataCenter.getInstance().hasCoffeeConfig()) {
                //该心跳数据（图片 视频 秒杀 促销 停售）更新需同步刷新界面，故特殊处理，同步执行。
                // 返回true 表示有新数据（刷新界面），返回false表示无新数据（不刷新界面）
                return checkImagesAndGoods(headBeatBean);

            }
            return false;
        }
    }

    /**
     * 1、图片：ImagesPushBean imagespushbeam（ObjPushBean left_bottoms;ObjPushBean top;ObjPushBean screensaver;）
     *   1.1、屏保：ObjPushBean screensaver （ArrayList<ImageBean> details;push_is）
     *        获得屏保图片集存入工艺中，新建屏保任务到完成任务列表tasks中；
     *   1.2、顶部：ObjPushBean imgtop
     *
     *   1.3、左下：ObjPushBean imgleftbottom
     *
     * 2、视频：VedioPushBean vediopushbean（ObjPushBean making，ObjPushBean top）
     *   2.1、制作：ObjPushBean making（ArrayList<ImageBean> details;）
     *        获得制作视频集存入工艺中，新建屏制作视频务到完成任务列表tasks中；
     *   2.2、顶部： ObjPushBean vedioTop
     *
     * 3、饮品：CoffeePushBean coffeesbean（ArrayList<CoffeeBean> coffees;）
     *       更新饮品；新建更新饮品任务到完成任务列表tasks中
     *
     * 4、ischange=true：
     *        下载视频、更新饮品、内存中刚更新工艺
     */
    private boolean checkImagesAndGoods(HeadBeatBean headBeatBean) {
        boolean ischange = false;
        /*********图片**********/
        ImagesPushBean imagespushbeam = headBeatBean.getImages();
        if (imagespushbeam != null) {
            ObjPushBean screensaver = imagespushbeam.getScreensaver();//屏保
            if (screensaver != null) {
                ischange = true;
                DataCenter.getInstance().getNowCoffeeConfigBean().getImage().setScreensaver(screensaver.getDetails());
                int id = screensaver.getPush_id();
                if (checkTaskById(id)) {
                    TaskBean task = new TaskBean(TaskBean.TASK_TYPE_IMAGE_VEDIO, id, "");
                    addSuccessTask(id, task);
                }
            }


            ObjPushBean imgtop = imagespushbeam.getTop();//顶部
            if (imgtop != null) {
                ischange = true;
                DataCenter.getInstance().getNowCoffeeConfigBean().getImage().setTop(imgtop.getDetails());
                int id = imgtop.getPush_id();
                if (checkTaskById(id)) {
                    TaskBean task = new TaskBean(TaskBean.TASK_TYPE_IMAGE_VEDIO, id, "");
                    addSuccessTask(id, task);
                }
            }


            ObjPushBean imgleftbottom = imagespushbeam.getLeft_bottoms();//左下
            if (imgleftbottom != null) {
                ischange = true;
                DataCenter.getInstance()
                        .getNowCoffeeConfigBean().getImage()
                        .setLeft_bottoms(imgleftbottom.getDetails());
                int id = imgleftbottom.getPush_id();
                if (checkTaskById(id)) {
                    TaskBean task = new TaskBean(TaskBean.TASK_TYPE_IMAGE_VEDIO, id, "");
                    addSuccessTask(id, task);
                }
            }
        }
        /*********视频**********/

        VedioPushBean vediopushbean = headBeatBean.getVideos();
        if (vediopushbean != null) {
            ObjPushBean making = vediopushbean.getMaking();//制作
            if (making != null) {
                ischange = true;
                DataCenter.getInstance().getNowCoffeeConfigBean().getVideo().setMaking(making.getDetails());
                int id = making.getPush_id();
                if (checkTaskById(id)) {
                    TaskBean task = new TaskBean(TaskBean.TASK_TYPE_IMAGE_VEDIO, id, "");
                    addSuccessTask(id, task);
                }
            }
            ObjPushBean vedioTop = vediopushbean.getTop();//顶部
            if (vedioTop != null) {
                ischange = true;
                DataCenter.getInstance().getNowCoffeeConfigBean().getVideo().setTop(vedioTop.getDetails());
                int id = vedioTop.getPush_id();
                if (checkTaskById(id)) {
                    TaskBean task = new TaskBean(TaskBean.TASK_TYPE_IMAGE_VEDIO, id, "");
                    addSuccessTask(id, task);
                }
            }
        }
        /*******饮品*******/
        CoffeePushBean coffeesbean = headBeatBean.getCoffee();
        if (coffeesbean != null) {
            ischange = true;
            ArrayList<CoffeeBean> coffees = coffeesbean.getCoffees();
            if (coffees != null && coffees.size() > 0) {
                DataCenter.getInstance().updateGoods(coffees);
            }
            int id = coffeesbean.getPush_id();
            if (checkTaskById(id)) {
                TaskBean task = new TaskBean(TaskBean.TASK_TYPE_GOODS, id, "");
                addSuccessTask(id, task);
            }
        }

        if (ischange) {
            LogUtil.test("miaosha ischange");
            DataCenter.getInstance().update(DataCenter.getInstance().getNowCoffeeConfigBean(), false);
            return true;
        }
        return false;

    }

    /**
     * 将push_id和type代表的任务添加到任务列表tasks中；
     * BasePushBean basePushBean不为空：
     *   获得push_id,push_id键对应的任务不在tasks中：
     *       新建push_id+type(清洗、截屏任务)，并添加到任务列表tasks中；
     */
    private void checkCommomPush(BasePushBean basePushBean, int type) {
        if (basePushBean != null) {
            int id = basePushBean.getPush_id();
            if (checkTaskById(id)) {
                TaskBean task = new TaskBean(type, id, "");
                addTask(id, task);
            }
        }
    }

    /**
     * 获得UpdatePushBean basePushBean；（String version;String url;push_id）
     * 不为空：获得push_id,该id代表的任务为空，新建更新app任务，并添加任务到tasks中；
     */
    private void checkUpdate(HeadBeatBean headBeatBean) {
        UpdatePushBean basePushBean = headBeatBean.getApp_update();
        if (basePushBean != null) {
            int id = basePushBean.getPush_id();
            if (checkTaskById(id)) {
                TaskBean task = new TaskBean(TaskBean.TASK_TYPE_APP_UPDATE, id, basePushBean.getUrl());
                task.setObj(basePushBean.getVersion());
                addTask(id, task);
            }
        }
    }

    
    /**
     * 1、从HeadBeatBean headBeatBean得到AppLogPushBean basePushBean（String date和的push_id）
     * 2、basePushBean不为空，获得push_id,
     *                        检查push_id代表的任务十分在tasks中，没有新建上传日志任务，并加入到任务列表tasks中；
     */
    private void checkLog(HeadBeatBean headBeatBean) {
        AppLogPushBean basePushBean = headBeatBean.getApp_log();
        if (basePushBean != null) {
            int id = basePushBean.getPush_id();
            if (checkTaskById(id)) {
                TaskBean task = new TaskBean(TaskBean.TASK_TYPE_APPLOG, id, basePushBean.getDate());
                addTask(id, task);
            }
        }
    }

     /**
     * 获取咖啡工艺和机器工艺任务
     * 1、获得HeadBeatBean headBeatBean中的ConfigBean tech_config(属性String id)
     *    ConfigBean tech_config不为空，得到属性String id
     *       String id不为空：获得push_id；
     *       push_id为空：新建TaskBean task,参数为：TASK_TYPE_COFFEE_CONFIG，push_id,id;
     *                    将id和任务添加到任务列表tasks中；
     * 2、获得获得HeadBeatBean headBeatBean中的ConfigBean machine_config(属性String id)
     *     ConfigBean machine_config不为空，得到属性String id
     *        String id不为空：获得push_id；
     *        push_id为空：新建TaskBean task,参数为：TASK_TYPE_COFFEE_MATION，push_id,id;
     *                    将id和任务添加到任务列表tasks中；
     */
    private void checkTechConfig(HeadBeatBean headBeatBean) {
        ConfigBean tech_config = headBeatBean.getCoffee_config();
        if (tech_config != null) {
            String coffee = tech_config.getId();
            if (!StringUtil.isStringEmpty(coffee)) {
                int id = tech_config.getPush_id();
                if (checkTaskById(tech_config.getPush_id())) {//id的任务为空
                    TaskBean task = new TaskBean(TaskBean.TASK_TYPE_COFFEE_CONFIG, id, coffee);
                    addTask(id, task);
                }
            }
        }
        ConfigBean machine_config = headBeatBean.getMachine_config();
        if (machine_config != null) {
            String mation = machine_config.getId();
            if (!StringUtil.isStringEmpty(mation)) {
                int id = machine_config.getPush_id();
                if (checkTaskById(machine_config.getPush_id())) {
                    TaskBean task = new TaskBean(TaskBean.TASK_TYPE_COFFEE_MATION, id, mation);
                    addTask(id, task);
                }
            }
        }

    }

}
