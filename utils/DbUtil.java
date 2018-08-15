package com.fancoff.coffeemaker.utils;

import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.fancoff.coffeemaker.Application.FilesManage;
import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.bean.coffe.SeckillBean;
import com.fancoff.coffeemaker.greendao.gen.DaoMaster;
import com.fancoff.coffeemaker.greendao.gen.DaoSession;
import com.fancoff.coffeemaker.greendao.gen.OrderDao;
import com.fancoff.coffeemaker.greendao.gen.SeckillBeanDao;
import com.fancoff.coffeemaker.net.RequstBean.Order;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by apple on 2017/11/30.
 * 数据库操作类
 */

/**
 * 默认情况下，新创建的数据存储在data的包名目录下，设备如果不root的话，是无法查看SQLite数据库文件的。
 * 而实际应用中，我们往往需要copy数据库，或借用第三方工具查阅或编辑数据库内容。
 * 此时我们可以通过重写Context的
 *     getDatabasePath(String name)、
 *     openOrCreateDatabase(String name, int mode, CursorFactory factory)、
 *     openOrCreateDatabase(String name, int mode, CursorFactory factory, DatabaseErrorHandler errorHandler)等
 *     三个方法来修改SQLite文件的存储路径。
 */

public class DbUtil {
    private static final String DB_NAME = "coffee.db";
    private static final DbUtil ourInstance = new DbUtil();

    public static DbUtil getInstance() {
        return ourInstance;
    }

    private DbUtil() {
    }

    DaoSession daoSession;
    DaoMaster daoMaster;

    /**获取DaoSession对象；完成对数据库的添加、删除、修改、查询操作，仅仅是一个接口*/
    public DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = getDaoMaster().newSession();//实例化DaoSession
        }
        return daoSession;
    }

    /**
     * 获取DaoMaster对象，修改数据库的存储路径为ROOT_PATH + "/db"，名字为ROOT_PATH + "/db"目录下的name：
     * 默认情况下，新创建的数据存储在data的包名目录下，设备如果不root的话，是无法查看SQLite数据库文件的。
     * 而实际应用中，我们往往需要copy数据库，或借用第三方工具查阅或编辑数据库内容。
     * 此时我们可以通过重写Context的getDatabasePath(String name)、
     * openOrCreateDatabase(String name, int mode, CursorFactory factory)、
     * openOrCreateDatabase(String name, int mode, CursorFactory factory, DatabaseErrorHandler errorHandler)
     * 等三个方法来修改SQLite文件的存储路径。
     */
    private DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            try {
                ContextWrapper wrapper = new ContextWrapper(MyApp.getIns()) {
                    /**
                     * 获得数据库路径，如果不存在，则创建对象
                     */
                    @Override
                    public File getDatabasePath(String name) {
                        // 判断是否存在sd卡
                        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
                        if (!sdExist) {// 如果不存在,
                            return null;
                        } else {// 如果存在
                            // 获取sd卡路径
                            String dbDir = FilesManage.dri.db;   //db目录在内存的路径
                            String dbPath = dbDir + "/" + name;// 数据库路径
                            // 判断目录是否存在，不存在则创建该目录
                            File dirFile = new File(dbDir);
                            if (!dirFile.exists())
                                dirFile.mkdirs();

                            // 数据库文件是否创建成功
                            boolean isFileCreateSuccess = false;
                            // 判断文件是否存在，不存在则创建该文件
                            File dbFile = new File(dbPath);
                            if (!dbFile.exists()) {
                                try {
                                    isFileCreateSuccess = dbFile.createNewFile();// 创建文件
                                } catch (IOException e) {
                                    LogUtil.error(e.toString());
                                }
                            } else
                                isFileCreateSuccess = true;
                            // 返回数据库文件对象
                            if (isFileCreateSuccess)
                                return dbFile;
                            else
                                return super.getDatabasePath(name);
                            //返回文件系统上使用openOrCreateDatabase(字符串、int、SQLiteDatabase.CursorFactory)创建的数据库的绝对路径
                        }
                    }

                    /**
                     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
                     */
                    @Override
                    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
                        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
                    }

                    /**
                     * Android 4.0会调用此方法获取数据库。
                     *
                     * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String,
                     *      int,
                     *      android.database.sqlite.SQLiteDatabase.CursorFactory,
                     *      android.database.DatabaseErrorHandler)
                     * @param name
                     * @param mode
                     * @param factory
                     * @param errorHandler
                     */
                    @Override
                    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
                        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
                    }
                };
                DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(wrapper, DB_NAME, null);
                //首先获取一个DevOpenHelper对象，这个类有点类似于我们使用的SqliteOpenHelper，我们主要在这个类中对数据库的版本进行管理。
                daoMaster = new DaoMaster(helper.getWritableDatabase()); //获取未加密的数据库


//                DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(MyApp.getIns(), DB_NAME, null);//实例化一个DevOpenhelper,相当于sqlit的SQliteOpenHelper
//                //Database dataBase1 = helper.getWritableDb();
////            Database database = helper.getEncryptedWritableDb("123456");
////            helper.getEncryptedReadableDb("123456");
//                daoMaster = new DaoMaster(helper.getWritableDb());//实例化DaoMaster
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return daoMaster;

    }

    /**在Orders数据库中删除order数据
     * AppService类中的runTimeToActionTask方法会调用*/
    public void deleteOrders(Order order) {
        synchronized ("OrderDao") {
            DaoSession daoSession = getDaoSession();
            OrderDao orderDao = daoSession.getOrderDao();
            orderDao.delete(order);
        }
    }

    /**在Orders数据库中增加orders数据
     * MakingPageMode类中的saveOrder方法会调用*/
    public void saveOrders(final ArrayList<Order> orders) {
        synchronized ("OrderDao") {
            DaoSession daoSession = getDaoSession();
            OrderDao orderDao = daoSession.getOrderDao();
            orderDao.insertInTx(orders);   //使用事务操作，将给定的实体集合插入数据库
        }


    }

    /**从OrderDao数据库中返回Order列表
     * AppService类中的runTimeToActionTask方法会调用*/
    public synchronized List<Order> selectOrders() {
        synchronized ("OrderDao") {
            DaoSession daoSession = getDaoSession();
            OrderDao orderDao = daoSession.getOrderDao();
            QueryBuilder queryBuilder = orderDao
                    .queryBuilder();
            List<Order> list = queryBuilder.list();

            return list;
        }

    }


    public synchronized void deleteAllSkillBean() {
        synchronized ("SeckillBean") {
            DaoSession daoSession = getDaoSession();
            SeckillBeanDao sellBeanDao = daoSession.getSeckillBeanDao();
            ;
        }

    }

    /**在Seckill数据库中删除Seckill 列表
     * DataCenter类中的updateSkillList方法会调用*/
    public synchronized void deleteSkillBean(ArrayList<SeckillBean> list) {
        synchronized ("SeckillBean") {
            DaoSession daoSession = getDaoSession();
            SeckillBeanDao sellBeanDao = daoSession.getSeckillBeanDao();
            sellBeanDao.deleteInTx(list);  //使用事务操作删除数据库中给定实体集合中的实体
        }

    }

//    public void saveSkill(SeckillBean seckillBean) {
//        synchronized ("SeckillBean") {
//            DaoSession daoSession = getDaoSession();
//            SeckillBeanDao sellBeanDao = daoSession.getSeckillBeanDao();
//            sellBeanDao.insertInTx(seckillBean);
//        }
//    }

    /**
     * 在Skill数据库中增加orders秒杀列表，coffee_id为参数id
     * DataCenter类中的updateSkillList方法会调用
     */
    public void saveSkillList(final ArrayList<SeckillBean> orders, int id) {
        synchronized ("SeckillBean") {
            DaoSession daoSession = getDaoSession();
            SeckillBeanDao sellBeanDao = daoSession.getSeckillBeanDao();
            for (SeckillBean o : orders) {//
                o.initTimeL();   //获得秒杀的影响时间和失效时间
                o.set_id(null);
                o.setCoffee_id(id);
            }
            sellBeanDao.insertInTx(orders);//使用事务操作，将给定的实体集合插入数据库
        }
    }

    /**
     * 返回数据库中coffee_id与id相等的seckill秒杀列表
     * DataCenter类中的updateSkillList方法会调用
     */
    public synchronized List<SeckillBean> selectSkillsBeanById(int id) {
        synchronized ("SeckillBean") {
            DaoSession daoSession = getDaoSession();
            SeckillBeanDao sellBeanDao = daoSession.getSeckillBeanDao();
            List<SeckillBean> list = sellBeanDao
                    .queryBuilder()
                    .where(SeckillBeanDao.Properties.Coffee_id.eq(id))  //查询Coffee_id与id相等的
                    .list();

            return list;
        }
    }

    /*
     if (startDay<=endDay&&((startDay <= nowDayStart && endDay >= nowDayStart)
                        || (endDay >= nowDayEnd && startDay <= nowDayEnd)
                        ||(startDay >= nowDayStart && endDay <= nowDayEnd))) {

                    return true;

                }
     */
	 /**
     *  1、获得当天的起始时间和结束时间，ms为单位
     *  2、获得Seckill的数据库；
     *  3、根据时间获得当天的秒杀列表；
      *  CoffeeBean类的initSkillMap方法会调用
     */
    public synchronized List<SeckillBean> selectSkillsBeanToday(int cfid) {
        synchronized ("SeckillBean") {
            Date curDate = new Date();
            long nowDayStart = TimeUtil.getInstance().getStartTimeOfDay(curDate).getTime();
            long nowDayEnd = TimeUtil.getInstance().getEndTimeOfDay(curDate).getTime();
            DaoSession daoSession = getDaoSession();
            SeckillBeanDao sellBeanDao = daoSession.getSeckillBeanDao();
            QueryBuilder qq = sellBeanDao
                    .queryBuilder();
            //le <= ;   ge >= ;
            WhereCondition whereCondition1 = qq.and(SeckillBeanDao.Properties.L_effect_time.le(nowDayEnd),
                    SeckillBeanDao.Properties.L_expiration_time.ge(nowDayEnd));
            //L_effect_time小于等于当天结束时间   L_expiration_time大于等于当天结束时间

            WhereCondition whereCondition2 = qq.and(SeckillBeanDao.Properties.L_effect_time.le(nowDayStart),
                    SeckillBeanDao.Properties.L_expiration_time.ge(nowDayStart));
            //L_effect_time小于等于当天起始时间   L_expiration_time大于等于当天起始时间

            WhereCondition whereCondition3 = qq.and(SeckillBeanDao.Properties.L_effect_time.ge(nowDayStart),
                    SeckillBeanDao.Properties.L_expiration_time.le(nowDayEnd));
            //L_effect_time大于等于当天起始时间   L_expiration_time小于等于当天结束时间

            qq.where(SeckillBeanDao.Properties.Coffee_id.eq(cfid), qq.or(whereCondition1, whereCondition2, whereCondition3));

            List<SeckillBean> list = qq.list();

            return list;
        }
    }

    /**更新Seckill数据库，有则更新无则插入
     * MakingPageMode类的saveOrder方法会调用*/
    public void update(SeckillBean seckillBean) {
        synchronized ("SeckillBean") {
            DaoSession daoSession = getDaoSession();
            SeckillBeanDao sellBeanDao = daoSession.getSeckillBeanDao();
            sellBeanDao.insertOrReplace(seckillBean);
        }
    }


}
