package com.fancoff.coffeemaker.net.RequstBean;

import com.fancoff.coffeemaker.utils.TimeUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;

/**
 * 属性：String sale_time;Long _id;int id;long order_id;
 * @Entity：将我们的java普通类变为一个能够被greenDAO识别的数据库类型的实体类;
 * @nameInDb：在数据库中的名字，如不写则为实体中类名；
 * @Id：选择一个long / Long属性作为实体ID。 在数据库方面，它是主键。 参数autoincrement是设置ID值自增；
 * @NotNull：使该属性在数据库端成为“NOT NULL”列。 通常使用@NotNull标记原始类型（long，int，short，byte）是有意义的；
 * @Transient：表明这个字段不会被写入数据库，只是作为一个普通的java类字段，用来临时存储数据的，不会被持久化。
 */
@Entity
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull
    String sale_time=TimeUtil.getInstance().getNowTimeShort(); //yyyyMMddHHmmss
    @Id(autoincrement = true)
    private Long _id;
    @NotNull
    int id;
    @NotNull

    long order_id;

    @Generated(hash = 1315903261)
    public Order(@NotNull String sale_time, Long _id, int id, long order_id) {
        this.sale_time = sale_time;
        this._id = _id;
        this.id = id;
        this.order_id = order_id;
    }

    @Generated(hash = 1105174599)
    public Order() {
    }

    public String getSale_time() {
        return sale_time;
    }

    public void setSale_time(String sale_time) {
        this.sale_time = sale_time;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Order{" +
                "sale_time='" + sale_time + '\'' +
                ", _id=" + _id +
                ", id=" + id +
                ", order_id=" + order_id +
                '}';
    }
}
