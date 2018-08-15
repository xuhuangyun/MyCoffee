package com.fancoff.coffeemaker.Application;

import com.fancoff.coffeemaker.bean.VMCState;
import com.fancoff.coffeemaker.bean.coffe.CategorieBean;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.bean.coffe.CoffeeConfigBean;
import com.fancoff.coffeemaker.bean.coffe.ImageBean;
import com.fancoff.coffeemaker.bean.coffe.PositionBean;
import com.fancoff.coffeemaker.bean.coffe.SeckillBean;
import com.fancoff.coffeemaker.bean.coffe.TextBean;
import com.fancoff.coffeemaker.bean.machine.CleanTime;
import com.fancoff.coffeemaker.bean.machine.MachineConfiBean;
import com.fancoff.coffeemaker.io.CMDUtil;
import com.fancoff.coffeemaker.service.HeatUtil;
import com.fancoff.coffeemaker.service.TaskUtil;
import com.fancoff.coffeemaker.utils.DbUtil;
import com.fancoff.coffeemaker.utils.GsonUtil;
import com.fancoff.coffeemaker.utils.MediaFile;
import com.fancoff.coffeemaker.utils.StringUtil;
import com.fancoff.coffeemaker.utils.XmlUtils;
import com.fancoff.coffeemaker.utils.glide.ImageLoadUtils;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.fancoff.coffeemaker.utils.rx.RxFileTool;
import com.kingsoft.media.httpcache.KSYProxyService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 2018/2/5.
 * 数据中心，所有的数据缓存统一在此管理
 */

/**
 * 1、CoffeeConfigBean对象nowCoffeeConfigBean：咖啡工艺表
 *    ArrayList<Categorie>  categories;
 *    Images    image;
 *    Videos    video;
 *    Texts     text;
 *    Parameter parameter;
 *    String    version;
 * 2、MachineConfiBean对象machineConfig：机器工艺表
 * 3、初始化咖啡工艺、机器工艺、心跳时间
 * 4、更新工艺、更新饮品、存储工艺表；
 * 5、解析vmc数据；
 * 6、下载视频、视频进度；
 * 7、更新秒杀列表；各部分图片、各部分视频列表；
 * 8、VMC清洗时间列表、清洗水量；VMC个状态：温度、开关等
 */
public class DataCenter {

    private static final DataCenter ourInstance = new DataCenter();


    public static DataCenter getInstance() {
        return ourInstance;
    }

    private DataCenter() {

    }


    CoffeeConfigBean nowCoffeeConfigBean;

    /**饮品列表目录：咖啡、奶类、餐包等*/
    public ArrayList<CategorieBean> getCategoriesList() {
        if (nowCoffeeConfigBean != null) {
            return nowCoffeeConfigBean.getCategories();
        }
        return null;
    }

    public CoffeeConfigBean getNowCoffeeConfigBean() {
        return nowCoffeeConfigBean;
    }

    public void setNowCoffeeConfigBean(CoffeeConfigBean nowCoffeeConfigBean) {
        this.nowCoffeeConfigBean = nowCoffeeConfigBean;
    }

    /**是否有咖啡工艺：有返回true*/
    public boolean hasCoffeeConfig() {
        return nowCoffeeConfigBean != null;

    }

    /**是否有机器工艺：无机器工艺false，有机器工艺true*/
    public boolean hasMAtion() {
        return machineConfig != null && !machineConfig.isNull();

    }

    /*
    resetSkillDb：是否将秒杀信息写入数据库 默认false，本机调试本地数据是传 true
     */
    /**
     * 初始化本地数据
     *     咖啡工艺：视频下载、饮品添加到商品列表goodsMap中，是否将咖啡工艺写入到vmc中，是否有本地无咖啡工艺的错误代码；
     *     机器工艺：Pull解析machineConfig.xml文件，并将机器工艺写入到VMC中；没有机器工艺则置位相应的错误代码
     *     初始化心跳时间：从咖啡工艺中获取心跳时间，最低为3秒；
     */
    public void initLocalData(boolean resetSkillDb) {
        initCoffesConfi(resetSkillDb);
        initMation();

        HeatUtil.getIns().initHeatTime();
    }

    /**
     * 初始化咖啡工艺：
     * 1、从内存中得到咖啡工艺的File；
     * 2、将咖啡工艺转换为string字符串，并通过Gson将CoffeeConfig.json转换为CoffeeConfigBean类中；
     * 3、下载url对应的视频或者图片（未下载完成的）；
     * 4、初始化咖啡工艺：秒杀、id等，并将咖啡饮品存入到商品列表goodsMap，第一个参数为true的话将咖啡工艺写到vmc中；
     * 5、添加错误代码和文字到错误列表errorMap中：本地无咖啡工艺
     */
    private void initCoffesConfi(boolean resetSkillDb) {
        File config = new File(FilesManage.getCoffeeConfig());//内存中的coffeeConfig.json
        if (config != null && config.exists()) {//咖啡工艺存在
            String json = RxFileTool.readFile2String(FilesManage.getCoffeeConfig(), "utf-8");  //将咖啡工艺转换为string字符串
            nowCoffeeConfigBean = GsonUtil.getGson().fromJson(json, CoffeeConfigBean.class);  //json的咖啡工艺到CoffeeConfigBean
            initAllVedio();//主要用来下载视频或者图片
            initGoddds(true, resetSkillDb);//true：需要将机器工艺写入vmc；
            TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0002, null);
        } else {
            TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0002, MyConstant.APP_ERROR_CODE.ERROR0002_S);
            //ERROR0002本地无咖啡工艺表
        }

    }

    /**
     * 初始化机器工艺：
     *   1、从内存中获得机器工艺MachineConfig.xml；
     *   2、存在机器工艺：
     *          读取machineconfig.xml为文件输入流；通过Pull将xml文件转换为MachineConfiBean类
     *          DataCenter类的机器工艺为m，并发送机器工艺到vmc
     *   3、不存在机器工艺：MachineConfiBean.isNull = true;错误代码app_error0001键值为无本地机器工艺
     */
    private void initMation() {
        File dri = new File(FilesManage.getMachineConfig());
        if (dri != null && dri.exists()) {
            try {
                FileInputStream slideInputStream = new FileInputStream(FilesManage.getMachineConfig());
                MachineConfiBean m = XmlUtils.getInstance().pullxmlM(slideInputStream);//将xml文件转换为MachineConfiBean类
                setMachineConfig(m);  //DataCenter类的机器工艺为m，并发送机器工艺到vmc
                TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0001, null);
                TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0004, null);
            } catch (FileNotFoundException e) {
                LogUtil.error(e.toString());
                TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0001, MyConstant.APP_ERROR_CODE.ERROR0001_S);
            } catch (Exception e) {
                TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0004, MyConstant.APP_ERROR_CODE.ERROR0004_S);
                LogUtil.error(e.toString());
            }
        } else {
            machineConfig.setNull(true);
            TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0001, MyConstant.APP_ERROR_CODE.ERROR0001_S);
        }

    }

    /**
     * 将机器工艺写入到机器工艺文件中:
     * 解析机器工艺文件到MachineConfiBean类实例m中；发送机器工艺到vmc中；
     */
    public void saveMachine_config(String mation) {
        MachineConfiBean m = null;
        try {
            RxFileTool.writeFileFromString(FilesManage.getMachineConfig(), mation, false);//将字符串文件mation写入到机器工艺
            m = XmlUtils.getInstance().pullxmlM(mation);//解析机器工艺到类MachineConfiBean中
            setMachineConfig(m);
            TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0001, null);
            TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0004, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化视频、更新饮品，咖啡工艺类转换为json并写入咖啡工艺文件中
     */
    public void update(CoffeeConfigBean coffeeConfigBean) {
        update(coffeeConfigBean, true);


    }

    /**
     * 更新饮品：饮品列表ArrayList<CoffeeBean> coffees
     *     更新现有饮品id与原来饮品id相同的饮品：原价与原价位置、现价与现价位置、现价小数位置；
     *     售卖状态、替换数据库中原有的秒杀列表、更新具体饮品CoffeeBean的属性seckills;
     */
    public void updateGoods(ArrayList<CoffeeBean> coffees) {
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getCategories() != null
                    && nowCoffeeConfigBean.getCategories().size() > 0) {
                for (CategorieBean c : nowCoffeeConfigBean.getCategories()) {//遍历类别目录
                    if (c != null && c.getCoffees() != null) {
                        for (CoffeeBean cc : c.getCoffees()) {//遍历类别中的饮品目录
                            if (cc != null) {
                                for (CoffeeBean ss : coffees) {//遍历传入的饮品目录
                                    if (ss != null) {
                                        if (cc.getId() == ss.getId()) {//原工艺表饮品的id与新传入的饮品id一样
                                            if (ss.getOrg_price() != null) {//更新原始价格与原始价格位置
                                                cc.getOrg_price().setValue(ss.getOrg_price().getValue());
                                                if (ss.getOrg_price().getPosition() != null) {
                                                    cc.getOrg_price().setPosition(ss.getPrice().getPosition());
                                                }
                                            }
                                            if (ss.getPrice() != null) {//更新现价、现价位置；
                                                cc.getPrice().setValue(ss.getPrice().getValue());
                                                if (ss.getPrice().getPosition() != null) {
                                                    cc.getPrice().setPosition(ss.getPrice().getPosition());
                                                }
                                                if (ss.getPrice().getFractional_position() != null) {//现价小数位置
                                                    cc.getPrice().setFractional_position(ss.getPrice().getFractional_position());
                                                }
                                            }

                                            if (ss.getStatus() >= 0) {//更新饮品状态；1正常、2热卖
                                                cc.setStatus(ss.getStatus());
                                            }

                                            ArrayList<SeckillBean> list = ss.getSeckills();//遍历到的当前饮品的秒杀列表
                                            if (list != null) {
                                                //删除数据库中id的原商品秒杀列表，传入id的新秒杀列表list
                                                ArrayList<SeckillBean> listskill = (ArrayList<SeckillBean>) updateSkillList(true, list, cc.getId());
                                                cc.reSetSeckills(listskill);//当前饮品coffeeBean的属性seckill设置为listskill
                                            }

                                        }
                                    }

                                }
                            }
                        }

                    }
                }


            }
        }
    }

    /**
     * 初始化饮品：saveVmc=true,需要将咖啡工艺写入vmc；resetDb
     * 1、饮品列表清零；coffeeBean
     * 2、有咖啡工艺、Categories；遍历Categories（咖啡类、奶类、其他。。。），每个类中存在咖啡饮品则遍历咖啡饮品种类：
     *      更新秒杀列表，CoffeeBean中重新传入秒杀列表listskill（resetDb=true返回的是咖啡工艺中的秒杀列表；false返回的是原数据库中的秒杀列表）;
     *      设置该饮品的id(0开始累加，用来转换饮品id，便于vmc识别)，同时将饮品增加到ArrayList<CoffeeBean> goodsMap中
     *      饮品种类小于12，则剩余部分填充空白；
     * 3、saveVmc标志置位，有饮品种类，则设置咖啡工艺到vmc
     */
    private void initGoddds(boolean saveVmc, boolean resetDb) {
        goodsMap.clear();
        int indx = 0;
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getCategories() != null
                    && nowCoffeeConfigBean.getCategories().size() > 0) {
                for (CategorieBean c : nowCoffeeConfigBean.getCategories()) {//遍历CategorieBean列表：咖啡类、奶类、其他。。。。
                    if (c != null && c.getCoffees() != null) {//有饮品
                        c.setSize(c.getCoffees().size());  //饮品的种类，有多少种饮品，CategorieBean.size
                        for (CoffeeBean cc : c.getCoffees()) {//遍历CategorieBean中的CoffeeBean列表
                            if (cc != null) {
                                ArrayList<SeckillBean> listskill = (ArrayList<SeckillBean>) updateSkillList(resetDb, cc.getSeckills(), cc.getId());
                                //传入的咖啡工艺中带有秒杀列表
                                //resetDb=true，则删除原来数据库中的秒杀列表，添加咖啡工艺的秒杀列表cc.getSeckills()，返回cc.getSeckills秒杀列表；
                                //resetDb=false,返回的是原来数据库中的秒杀列表；
                                cc.reSetSeckills(listskill);  //CoffeeBean中重新传入秒杀列表listskill
                                cc.getMake().setIndex(indx);  //该饮品id设置为indx
                                goodsMap.add(cc);    //增加饮品到map中
                                indx++;
                            }
                        }

                        if (c.getCoffees().size() < 12 && c.getCoffees().size() > 0) {
                            for (int i = c.getCoffees().size(); i < 12; i++) {
                                c.getCoffees().add(null);
                            }
                        }
                    }
                }
            }
        }
        if (saveVmc && goodsMap.size() > 0) {//saveVmc标志置其，有饮品种类，则设置咖啡工艺到vmc
            CMDUtil.getInstance().setCoffe(goodsMap);//设置咖啡工艺，只取粉水部分即MakeBean
        }
    }

    /**
     * 初始化下载视频、更新饮品、咖啡工艺类转换为json并写入咖啡工艺文件内存中
     */
    public void update(CoffeeConfigBean coffeeConfigBean, boolean saveVmc) {
        LogUtil.test("miaosha update");
        nowCoffeeConfigBean = coffeeConfigBean;
        initAllVedio();//下载视频
        initGoddds(saveVmc, false);
        String json = GsonUtil.getGson().toJson(nowCoffeeConfigBean);
        RxFileTool.writeFileFromString(FilesManage.getCoffeeConfig(), json, false);
        TaskUtil.getInstance().addUploadErrorTask(MyConstant.APP_ERROR_CODE.ERROR0002, null);

    }

    /** 缓存的列表，url和进度值 */
    HashMap<String, Integer> map = new HashMap<>();

    public HashMap<String, Integer> getCachingPercentsMap() {
        return map;
    }

    HashMap<String, ImageBean> allVedios = new HashMap<String, ImageBean>();
    HashMap<String, ImageBean> allgifs = new HashMap<String, ImageBean>();
    /**
     *  下载各部分对应的未下载完成的视频或图片列表；
     * 1、将所视频列表添加到HashMap mallVedios中；最终复制到allVedios中；键是url、值是ImageBean；
     * 2、在allVedios中，缓存中有url，但咖啡工艺中没有url对应的ImageBean，则删除下载好的文件，或者清空对应的缓存；
     * 3、下载咖啡工艺中各url对应的视频或者图片（url对应的视频或图片未下载完成才能下载）
     */
    public void initAllVedio() {
        allgifs.clear();
        allVedios.clear();
        KSYProxyService proxy = MyApp.getProxy(MyApp.getIns());
        ArrayList<ImageBean> v1 = getTopVedioa();
        ArrayList<ImageBean> v2 = getMakingVedio();
        ArrayList<ImageBean> v3 = getCompleteVedio();
        ArrayList<ImageBean> v4 = getFailedVedio();
        ArrayList<ImageBean> v5 = getDirtyVedio();
        ArrayList<ImageBean> v6 = getStuckCupVedio();
        for (ImageBean v : v1) {
            if (!MediaFile.isVideoFileType(v.getUrl())) {
                allgifs.put(v.getUrl(), v);
            } else {
                allVedios.put(v.getUrl(), v);
            }
        }
        for (ImageBean v : v2) {
            if (!MediaFile.isVideoFileType(v.getUrl())) {
                allgifs.put(v.getUrl(), v);
            } else {
                allVedios.put(v.getUrl(), v);
            }
        }
        for (ImageBean v : v3) {
            if (!MediaFile.isVideoFileType(v.getUrl())) {
                allgifs.put(v.getUrl(), v);
            } else {
                allVedios.put(v.getUrl(), v);
            }
        }
        for (ImageBean v : v4) {
            if (!MediaFile.isVideoFileType(v.getUrl())) {
                allgifs.put(v.getUrl(), v);
            } else {
                allVedios.put(v.getUrl(), v);
            }
        }
        for (ImageBean v : v5) {
            if (!MediaFile.isVideoFileType(v.getUrl())) {
                allgifs.put(v.getUrl(), v);
            } else {
                allVedios.put(v.getUrl(), v);
            }
        }
        for (ImageBean v : v6) {
            if (!MediaFile.isVideoFileType(v.getUrl())) {
                allgifs.put(v.getUrl(), v);
            } else {
                allVedios.put(v.getUrl(), v);
            }
        }

        /**
         * 完成的缓存中有的url键对应的文件file，但是url不在咖啡工艺中，则删除该文件
         * 获得缓存中已完成的文件列表：url和缓存文件；
         * 遍历缓存文件列表，发现url对应的ImageBean不存在，则删除缓存文件
         */
        HashMap<String, File> mapFile = proxy.getCachedFileList();//获得缓存区中已缓存完成的文件列表（url和缓存文件）
        if (mapFile.size() > 0) {
            for (Map.Entry<String, File> entry : mapFile.entrySet()) {
                String url = entry.getKey();
                File file = entry.getValue();  //缓存中的url和其对应的file文件；
                ImageBean img = allVedios.get(url);  //咖啡工艺表中url对应的ImageBean
                if (img == null) {  //缓存的url键对应的img不存在工艺列表中，则删除在缓存区中该url键对应的缓存文件
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }

        /**
         * 未完成的缓存中的url在咖啡工艺中不存在，则删除未完成的缓存文件
         * 获得缓存中未完成的文件列表：url和缓存完成百分比；
         * 遍历缓存文件列表，发现url对应的ImageBean不存在，删除某个url对于的缓存
         */
        HashMap<String, Integer> map = proxy.getCachingPercentsList();//获得缓存区中缓存未完成的文件列表（url和缓存完成百分比）
        if (map.size() > 0) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String url = entry.getKey();
                ImageBean img = allVedios.get(url);
                if (img == null) {
                    MyApp.getProxy(MyApp.getIns()).cleanCache(url);
                }
            }
        }
        startDownLoadVedio(allVedios);
        startDownLoadGifs(allgifs);


    }

    int downLoadProgress;

    public int getDownLoadProgress() {
        return downLoadProgress;
    }

    public void setDownLoadProgress(int downLoadProgress) {
        this.downLoadProgress = downLoadProgress;
    }

    //下载未下载完成的视频
    /**
     * 计算出户视频下载进度：downLoadProgress
     */
    public void checkUnDownLoadVedios() {
        KSYProxyService proxy = MyApp.getProxy(MyApp.getIns());
        map = proxy.getCachingPercentsList();//获得缓存区中缓存未完成的文件列表（url和缓存完成百分比）
        int maxprogress = 100;
        if (map.size() > 0) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                //遍历有缓存的列表，获得url和进度值
                String url = entry.getKey();
                int valeu = entry.getValue();
                maxprogress += 100 - valeu;
//                if (valeu < maxprogress) {
//                    maxprogress = valeu;
//                }
                ImageBean img = allVedios.get(url);
                if (img == null) {
                    MyApp.getProxy(MyApp.getIns()).cleanCache(url);
                }
            }
            downLoadProgress = (int) (100 - (maxprogress * 1.00 / allVedios.size()));
            if (downLoadProgress < 0) {
                downLoadProgress = 0;
            }

        }


    }
    /**遍历ImageBean对象列表，下载ImageBean中url对应的 图片（url的缓存没有完成）*/
    private void startDownLoadGifs( HashMap<String, ImageBean> vedios) {
        if (vedios != null && vedios.size() > 0) {
            for (Map.Entry<String, ImageBean> entry : vedios.entrySet()) {
                ImageBean img=entry.getValue();
                startDownLoadgif(img);
            }
        }

    }
	/**
     * 遍历ImageBean对象列表，下载ImageBean中url对应的视频 （url的缓存没有完成）
     */
    private void startDownLoadVedio( HashMap<String, ImageBean> vedios) {
        if (vedios != null && vedios.size() > 0) {
            for (Map.Entry<String, ImageBean> entry : vedios.entrySet()) {
                ImageBean img=entry.getValue();
                startDownLoadVedio(img);
            }
        }

    }

    /**
     * 如果url对应的缓存没有完成，下载url对应的视频和图片
     * 预缓存功能, 该功能主要目标是在用户开始播放视频前就可以将视频预先缓存至本地。
     */
    private void startDownLoadVedio(final ImageBean img) {
        final KSYProxyService proxy = MyApp.getProxy(MyApp.getIns());
        if (!proxy.isCached(img.getUrl())) {//查询img的url是否缓存完成
            proxy.startPreDownload(img.getUrl()); //开启视频预加载, 需要在startServer()之后调用，在MyAPP中已经开启
        }
    }

    /**阻塞式下载图片*/
    private void startDownLoadgif(final ImageBean img) {
        ImageLoadUtils.getInstance().preloadNetImage(img.getUrl());
    }

    ArrayList<CoffeeBean> goodsMap = new ArrayList<>();


    /**
     * 根据字节数组bytes，解析这串数据：A域的64个字节
     * 1、bytes为空，VMCState.isNull = true;
     * 2、bytes不为空，解析这串从VMC中获得的数据，解析出来的状态信息放入VMCState中的属性中
     *        有故障，每个15秒打印日志；
     *        没故障，在制作中、vmc握手响应、vmc指令响应等状态下，打印日志
     */
    public void saveVmcBytes(byte[] bytes) {
        VMCState.getIns().initBytes(bytes);
    }


    /**
     * 找到数据库中id为cfid的本地秒杀列表listLocalDb
     * 如果resetDb为true则删除数据库中本来有的秒杀列表listLocalDb，并重新存入参数中的秒杀列表listFile，并返回重新存入的秒杀列表listFile
     * 如果resetDb为false返回原秒杀列表listLocalDb
     */
    private List<SeckillBean> updateSkillList(boolean resetDb, ArrayList<SeckillBean> listFile, int cfid) {

        List<SeckillBean> listLocalDb
                = DbUtil.getInstance().selectSkillsBeanById(cfid);  //得到数据库id为cfid的秒杀列表
        if (resetDb) {
            DbUtil.getInstance().deleteSkillBean((ArrayList<SeckillBean>) listLocalDb);//删除listLocalDb秒杀列表

            if (listFile != null) {
                LogUtil.test("save SkillList to db id " + cfid + "   size:" + listFile.size() + "count");
                DbUtil.getInstance().saveSkillList(listFile, cfid);
            }
            return listFile;
        } else {
            return listLocalDb;
        }
    }

    /**
     * parameter.pay_type支持支付宝支付返回1
     */
    public boolean isShowPayBtnAipay() {
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getParameter().getPay_type() != 0) {
                int result = nowCoffeeConfigBean.getParameter().getPay_type() & MyConstant.PAY.PAY_TYPE_AIPAY;
                return result > 0;
            }
        }
        return false;
    }
    /**
     * parameter.pay_type支持微信支付返回1
     */
    public boolean isShowPayBtnWeiPay() {
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getParameter().getPay_type() != 0) {
                int result = nowCoffeeConfigBean.getParameter().getPay_type() & MyConstant.PAY.PAY_TYPE_WEIXIN;
                return result > 0;
            }
        }
        return false;
    }
    /**
     * parameter.pay_type支持支和包支付返回1
     */
    public boolean isShowPayBtnHePay() {
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getParameter().getPay_type() != 0) {
                int result = nowCoffeeConfigBean.getParameter().getPay_type() & MyConstant.PAY.PAY_TYPE_HE;
                return result > 0;
            }
        }
        return false;
    }

    /**
     * 屏保图片：获取咖啡工艺中的屏保图片列表，并进行返回
     */
    public ArrayList<ImageBean> getScreenBannerlist() {
        ArrayList<ImageBean> bannerlist = new ArrayList<ImageBean>();
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getImage() != null) {
                if (nowCoffeeConfigBean.getImage().getScreensaver() != null
                        && nowCoffeeConfigBean.getImage().getScreensaver().size() > 0) {
                    bannerlist.addAll(nowCoffeeConfigBean.getImage().getScreensaver());
                }
            }

        }
        return bannerlist;
    }

    /**
     * 顶部视频和图片：获取顶部图片和视频列表，并返回该列表
     */
    public ArrayList<ImageBean> getBannerlist() {
        ArrayList<ImageBean> bannerlist = new ArrayList<ImageBean>();
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getVideo() != null) {
                if (nowCoffeeConfigBean.getVideo().getTop() != null
                        && nowCoffeeConfigBean.getVideo().getTop().size() > 0) {
                    bannerlist.addAll(nowCoffeeConfigBean.getVideo().getTop());
                    //顶部视频列表
                }
            }
            if (nowCoffeeConfigBean.getImage() != null) {
                if (nowCoffeeConfigBean.getImage().getTop() != null
                        && nowCoffeeConfigBean.getImage().getTop().size() > 0) {
                    bannerlist.addAll(nowCoffeeConfigBean.getImage().getTop());
                    //顶部图片列表
                }
            }

        }
        return bannerlist;
    }

    /**
     * 左下图片：获取左下图片列表，并返回该列表
     */
    public ArrayList<ImageBean> getLeftBottoms() {
        ArrayList<ImageBean> bannerlist = new ArrayList<ImageBean>();
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getImage() != null
                    && nowCoffeeConfigBean.getImage().getLeft_bottoms() != null
                    && nowCoffeeConfigBean.getImage().getLeft_bottoms().size() > 0) {
                bannerlist.addAll(nowCoffeeConfigBean.getImage().getLeft_bottoms());
            }

        }
        return bannerlist;
    }

    ImageBean defult;
    /**
     * 默认的图片:
     *    defult=null,ImageBean.url="",ImageBean.position为0,0
     *    defult！=null，则就是实际的defult
     *    返回默认的ImageBean defult
     */
    public ImageBean getDefultImageBean() {
        if (defult == null) {
            defult = new ImageBean("");
            defult.setUrl("");
            defult.setPosition(new PositionBean(0, 0));
        }
        return defult;

    }

    TextBean defultText;

    public TextBean getDefultTextBean() {
        if (defultText == null) {
            defultText = new TextBean();
            defultText.setValue("");
            defultText.setPosition(new PositionBean(0, 0));
        }
        return defultText;

    }

    /**
     * 获取右下图片1，并进行返回；没有返回默认图片
     */
    public ImageBean getRightBottom1() {

        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getImage() != null) {
                return nowCoffeeConfigBean.getImage().getRight_bottom1();
            }

        }
        return getDefultImageBean();
    }

    /**
     * 售罄图片：获取售罄图片，并进行返回；没有则返回默认图片；
     */
    public ImageBean getSellOut() {
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getImage() != null) {
                ImageBean img = nowCoffeeConfigBean.getImage().getSellout();
                if (img != null) {
                    return img;
                }
            }
        }
        return getDefultImageBean();
    }

    /**
     * 秒杀图片：获得秒杀图片，并进行返回；没有就返回默认图片；
     */
    public ImageBean getSeckill() {
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getImage() != null) {
                ImageBean img = nowCoffeeConfigBean.getImage().getSeckill();
                if (img != null) {
                    return img;
                }
            }
        }
        return getDefultImageBean();
    }

    /**
     * 秒杀文字：返回秒杀文字实例 textBean（咖啡工艺中的text.seckill）；
     */
    public TextBean getSeckillText() {
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getText() != null) {
                TextBean textBean = nowCoffeeConfigBean.getText().getSeckill();
                if (textBean != null) {
                    return textBean;
                }
            }
        }
        return null;
    }

    /**
     * 获得热卖图片
     */
    public ImageBean getHot() {
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getImage() != null) {
                ImageBean img = nowCoffeeConfigBean.getImage().getHot();
                if (img != null) {
                    return img;
                }
            }
        }
        return getDefultImageBean();
    }

    /**
     * 获得右下角2图片
     */
    public ImageBean getRightBottom2() {
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getImage() != null) {
                return nowCoffeeConfigBean.getImage().getRight_bottom2();
            }

        }
        return getDefultImageBean();
    }

    /**
     * 获得右下角3图片
     */
    public ImageBean getRightBottom3() {
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getImage() != null) {
                return nowCoffeeConfigBean.getImage().getRight_bottom3();
            }

        }
        return getDefultImageBean();
    }

    /**
     * 获得空的商品的图片的url
     */
    public String getEmptyGoodsUrl() {
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getImage() != null
                    && nowCoffeeConfigBean.getImage().getNone_coffee() != null) {
                return nowCoffeeConfigBean.getImage().getNone_coffee().getUrl();
            }
        }
        return "";
    }

/*****************************获得视频url列表*************************************************/
    ArrayList<ImageBean> faiedVedio;
    ArrayList<ImageBean> dirtyVedio;
    ArrayList<ImageBean> handVedio;
    ArrayList<ImageBean> makingdVedio;
    ArrayList<ImageBean> completeVedio;
    ArrayList<ImageBean> stuckCupVedio;
    ArrayList<ImageBean> topVedio;


    public ArrayList<ImageBean> getStuckCupVedio() {
        if (stuckCupVedio == null) {
            stuckCupVedio = new ArrayList<>();
        } else {
            stuckCupVedio.clear();
        }
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getVideo() != null
                    && nowCoffeeConfigBean.getVideo().getFall_cup_error() != null) {
                stuckCupVedio.add(nowCoffeeConfigBean.getVideo().getFall_cup_error());
            }
        }
        return stuckCupVedio;
    }

    public ArrayList<ImageBean> getHandVedio() {
        if (handVedio == null) {
            handVedio = new ArrayList<>();
        } else {
            handVedio.clear();
        }
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getVideo() != null
                    && nowCoffeeConfigBean.getVideo().getFall_cup_error() != null) {
                handVedio.add(nowCoffeeConfigBean.getVideo().getFall_cup_error());
            }
        }
        return handVedio;
    }

    /**获取脏杯视频列表*/
    public ArrayList<ImageBean> getDirtyVedio() {
        if (dirtyVedio == null) {
            dirtyVedio = new ArrayList<>();
        } else {
            dirtyVedio.clear();
        }
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getVideo() != null
                    && nowCoffeeConfigBean.getVideo().getDirty_cup() != null) {
                dirtyVedio.add(nowCoffeeConfigBean.getVideo().getDirty_cup());
            }
        }
        return dirtyVedio;
    }

    /**获取制作失败视频列表*/
    public ArrayList<ImageBean> getFailedVedio() {
        if (faiedVedio == null) {
            faiedVedio = new ArrayList<>();
        } else {
            faiedVedio.clear();
        }
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getVideo() != null
                    && nowCoffeeConfigBean.getVideo().getMake_error() != null) {
                faiedVedio.add(nowCoffeeConfigBean.getVideo().getMake_error());
            }
        }
        return faiedVedio;
    }

    public ArrayList<ImageBean> getCompleteVedio() {
        if (completeVedio == null) {
            completeVedio = new ArrayList<>();
        } else {
            completeVedio.clear();
        }
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getVideo() != null
                    && nowCoffeeConfigBean.getVideo().getCompleted() != null) {
                completeVedio.add(nowCoffeeConfigBean.getVideo().getCompleted());
            }
        }
        return completeVedio;
    }

    /**
     * 获得咖啡工艺中top部分的列表
     * 在咖啡工艺中有top部分的图片或视频，则将咖啡工艺中的top添加到topVedio列表
     * 咖啡工艺中没有top，则是一个空的arrayList列表
     */
    public ArrayList<ImageBean> getTopVedioa() {
        if (topVedio == null) {
            topVedio = new ArrayList<>();
        } else {
            topVedio.clear();
        }
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getVideo() != null
                    && nowCoffeeConfigBean.getVideo().getTop() != null) {
                topVedio.addAll(nowCoffeeConfigBean.getVideo().getTop());
            }
        }
        return topVedio;
    }

    public ArrayList<ImageBean> getMakingVedio() {
        if (makingdVedio == null) {
            makingdVedio = new ArrayList<>();
        } else {
            makingdVedio.clear();
        }
        if (nowCoffeeConfigBean != null) {
            if (nowCoffeeConfigBean.getVideo() != null
                    && nowCoffeeConfigBean.getVideo().getMaking() != null) {
                makingdVedio.addAll(nowCoffeeConfigBean.getVideo().getMaking());
            }
        }
        return makingdVedio;
    }


    /*********************************************/

    MachineConfiBean machineConfig = new MachineConfiBean();
    ArrayList<CleanTime> times = new ArrayList<>();

    /**
     * 获得机器工艺中的清洗时间列表
     */
    public ArrayList<CleanTime> getCleanTimeList() {
        if (machineConfig.getClean() != null && machineConfig.getClean().getTimes() != null) {
            times = machineConfig.getClean().getTimes();
        }
        return times;
    }

    public MachineConfiBean getMachineConfig() {
        return machineConfig;
    }

    /**
     * 传入机器工艺类MachineConfiBean对象machineConfig，
     * 并调用CMDUtil类的setMachine方法来发送机器工艺到VMC
     */
    public void setMachineConfig(MachineConfiBean machineConfig) {
        if (machineConfig != null) {
            CMDUtil.getInstance().setMachine(machineConfig, null);
        }
        this.machineConfig = machineConfig;

    }


    /*咖啡参数的标识符*/
    /**
     * 咖啡工艺的版本号
     */
    public String getCoffee() {
        if (nowCoffeeConfigBean != null) {
            String version = nowCoffeeConfigBean.getVersion();//获得咖啡工艺的版本
            if (!StringUtil.isStringEmpty(version)) {//version字符串不为空，返回版本号
                return version;
            }
        }
        return "0";
    }

    /*机器参数的标识符*/

    /**
     * 机器工艺的版本号
     */
    public String getMachine() {

        return machineConfig.getSerial();
    }

    /* 机器类型*/
    public int getMachine_type() {
        return machineConfig.getMachine_type();
    }

    /*销售咖啡种类数*/
    public int getCoffee_type() {
        return machineConfig.getCoffee_type();
    }

    public int getGoodSize() {
        return goodsMap.size();
    }

    HashMap<Integer, CoffeeBean> skillgodds = new HashMap<Integer, CoffeeBean>();
    HashMap<Integer, CoffeeBean> skillgoddsTemp = new HashMap<Integer, CoffeeBean>();
    boolean isrunskill = false;

    public CoffeeBean getSkillGoodById(int id) {
        return skillgodds.get(id);
    }

    /**
     * 今日秒杀商品信息更新；
     * 检查秒杀：isrunskill
     *   1、true
     *      正在秒杀，则返回；之前调用过checkSkill();
     *   2、false
     *      isrunskill=true;
     *      run():获取咖啡工艺中当前时间点有秒杀的商品，并存入到秒杀列表hashmap实例skillgodds中；
     */
    public void checkSkill() {
        if (isrunskill) {
            return;
        }
        isrunskill = true;
        MyApp.getIns().runTask("key_check_skill", new MyApp.TaskRun() {
            @Override
            public void run() {
            //将秒杀商品的id和CoffeeBean存入到秒杀列表缓存hashmap中
                skillgoddsTemp.clear();  //HashMap<Integer, CoffeeBean> skillgoddsTemp;
                if (nowCoffeeConfigBean != null) {
                    if (nowCoffeeConfigBean.getCategories() != null
                            && nowCoffeeConfigBean.getCategories().size() > 0) {
                        for (CategorieBean c : nowCoffeeConfigBean.getCategories()) {
                            if (c != null && c.getCoffees() != null) {
                                for (CoffeeBean cc : c.getCoffees()) {
                                    if (cc != null) {
                                        if (cc.isSkilling() != null) {//当前饮品的当前时间点有秒杀
                                            skillgoddsTemp.put(cc.getId(), cc);
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
                skillgodds.putAll(skillgoddsTemp);//复制到秒杀列表的hashmap中
            }

            @Override
            protected void onError() {
                super.onError();
                isrunskill = false;
            }

            @Override
            public void onOk() {
                isrunskill = false;
            }
        });

    }

    /**
     * VMCState有实例不为空，数据校验正确，获取咖啡机温度
     */
    public int getMainTemp() {
        VMCState vmcStateBean = VMCState.getIns();
        if (!vmcStateBean.isNull()) {
            if (vmcStateBean.isRealData()) {
                return vmcStateBean.getCoffeeTemp();
            }
        }
        return 0;
    }

    /**
     * vmc状态类中，数据校验正确，读取辅助锅炉温度；
     */
    public int getAssistTemp() {
        VMCState vmcStateBean = VMCState.getIns();
        if (!vmcStateBean.isNull()) {
            if (vmcStateBean.isRealData()) {
                return vmcStateBean.getBoilerTemp();
            }
        }
        return 0;
    }

    public boolean isHotMainOn() {
        VMCState vmcStateBean = VMCState.getIns();
        if (!vmcStateBean.isNull()) {
            if (vmcStateBean.isRealData()) {
                return vmcStateBean.isHotMainOn();
            }
        }
        return false;
    }

    public boolean isHotAssistOn() {
        VMCState vmcStateBean = VMCState.getIns();
        if (!vmcStateBean.isNull()) {
            if (vmcStateBean.isRealData()) {
                return vmcStateBean.isHotassistOn();
            }
        }
        return false;
    }

    public String getErrorState() {
        VMCState vmcStateBean = VMCState.getIns();
        if (!vmcStateBean.isNull()) {
            if (vmcStateBean.isRealData()) {
                if (vmcStateBean.isCFError() || vmcStateBean.isVMCError()) {
                    return vmcStateBean.getCf_error_content() + vmcStateBean.getVmc_error_content();
                } else {
                    return "正常";
                }
            } else {
                return "vmc校验错误";
            }
        } else {
            return "VMC通讯故障";
        }
    }

    /**
     * 获取机器工艺的咖啡机清洗水量，默认设置为50
     */
    public int getCleanMainWater() {
        if (getMachineConfig().getClean() != null) {
            return getMachineConfig().getClean().getMain_water();
        }
        return 50;
    }

    /**
     * 获得工期工艺的辅助清洗水量，默认90
     */
    public int getCleanAssistWater() {
        if (getMachineConfig().getClean() != null) {
            return getMachineConfig().getClean().getAssist_water();
        }
        return 90;
    }
}
