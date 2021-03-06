package com.fancoff.coffeemaker.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.fancoff.coffeemaker.bean.coffe.SeckillBean;
import com.fancoff.coffeemaker.net.RequstBean.Order;

import com.fancoff.coffeemaker.greendao.gen.SeckillBeanDao;
import com.fancoff.coffeemaker.greendao.gen.OrderDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig seckillBeanDaoConfig;
    private final DaoConfig orderDaoConfig;

    private final SeckillBeanDao seckillBeanDao;
    private final OrderDao orderDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        seckillBeanDaoConfig = daoConfigMap.get(SeckillBeanDao.class).clone();
        seckillBeanDaoConfig.initIdentityScope(type);

        orderDaoConfig = daoConfigMap.get(OrderDao.class).clone();
        orderDaoConfig.initIdentityScope(type);

        seckillBeanDao = new SeckillBeanDao(seckillBeanDaoConfig, this);
        orderDao = new OrderDao(orderDaoConfig, this);

        registerDao(SeckillBean.class, seckillBeanDao);
        registerDao(Order.class, orderDao);
    }
    
    public void clear() {
        seckillBeanDaoConfig.clearIdentityScope();
        orderDaoConfig.clearIdentityScope();
    }

    public SeckillBeanDao getSeckillBeanDao() {
        return seckillBeanDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

}
