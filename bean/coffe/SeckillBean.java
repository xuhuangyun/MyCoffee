package com.fancoff.coffeemaker.bean.coffe;

import com.fancoff.coffeemaker.utils.GsonUtil;
import com.fancoff.coffeemaker.utils.TimeUtil;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;

/**
 * Created by apple on 2018/3/21.
 */
/**
 * @Entity：将我们的java普通类变为一个能够被greenDAO识别的数据库类型的实体类;
 * @nameInDb：在数据库中的名字，如不写则为实体中类名；
 * @Id：选择一个long / Long属性作为实体ID。 在数据库方面，它是主键。 参数autoincrement是设置ID值自增；
 * @NotNull：使该属性在数据库端成为“NOT NULL”列。 通常使用@NotNull标记原始类型（long，int，short，byte）是有意义的；
 * @Transient：表明这个字段不会被写入数据库，只是作为一个普通的java类字段，用来临时存储数据的，不会被持久化。
 *
 */
@Entity
public class SeckillBean implements Serializable {
    private static final long serialVersionUID = 3L;
    @Id(autoincrement = true)
    private Long _id;
    int coffee_id;
    @Convert(converter = TextBeanConverter.class, columnType = String.class)
    TextBean price;
    String effect_time;
    String expiration_time;
    long l_effect_time;
    long l_expiration_time;
    int total;


    /**影响时间，失效时间*/
    public void initTimeL() {
        l_effect_time = TimeUtil.getInstance().StringToDataTime(effect_time);  //
        l_expiration_time = TimeUtil.getInstance().StringToDataTime(expiration_time);//

    }

    @Generated(hash = 1961404016)
    public SeckillBean(Long _id, int coffee_id, TextBean price, String effect_time,
                       String expiration_time, long l_effect_time, long l_expiration_time, int total) {
        this._id = _id;
        this.coffee_id = coffee_id;
        this.price = price;
        this.effect_time = effect_time;
        this.expiration_time = expiration_time;
        this.l_effect_time = l_effect_time;
        this.l_expiration_time = l_expiration_time;
        this.total = total;
    }

    @Generated(hash = 1395446057)
    public SeckillBean() {
    }


    public int getCoffee_id() {
        return coffee_id;
    }

    public void setCoffee_id(int coffee_id) {
        this.coffee_id = coffee_id;
    }

    public TextBean getPrice() {
        return price;
    }

    public void setPrice(TextBean price) {
        this.price = price;
    }

    public String getEffect_time() {
        return effect_time;
    }

    public void setEffect_time(String effect_time) {
        this.effect_time = effect_time;
    }

    public String getExpiration_time() {
        return expiration_time;
    }

    public void setExpiration_time(String expiration_time) {
        this.expiration_time = expiration_time;
    }

    public int getTotal() {

        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    public String getShowLast() {
        return TimeUtil.getInstance().showLast(effect_time, expiration_time);
    }

    //
    public boolean isToday() {

        return TimeUtil.getInstance().isToday(effect_time, expiration_time);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }


    /** String和TextBean转换 */
    public static class TextBeanConverter implements PropertyConverter<TextBean, String> {
        @Override
        public TextBean convertToEntityProperty(String databaseValue) {
            //
            if (databaseValue == null) {
                return null;
            }
            return GsonUtil.getGson().fromJson(databaseValue, TextBean.class);
            //databaseValue的Json转换为TextBean
        }

        @Override
        public String convertToDatabaseValue(TextBean entityProperty) {
            //convertToDatabaseValue
            if (entityProperty == null) {
                return null;
            }
            return GsonUtil.getGson().toJson(entityProperty);
            //TextBean转换为Json
        }
    }

    @Override
    public String toString() {
        return "SeckillBean{" +
                "_id=" + _id +
                ", coffee_id=" + coffee_id +
                ", price=" + price +
                ", effect_time='" + effect_time + '\'' +
                ", expiration_time='" + expiration_time + '\'' +
                ", l_effect_time=" + l_effect_time +
                ", l_expiration_time=" + l_expiration_time +
                ", total=" + total +
                '}';
    }

    public long getL_effect_time() {
        return this.l_effect_time;
    }

    public void setL_effect_time(long l_effect_time) {
        this.l_effect_time = l_effect_time;
    }

    public long getL_expiration_time() {
        return this.l_expiration_time;
    }

    public void setL_expiration_time(long l_expiration_time) {
        this.l_expiration_time = l_expiration_time;
    }
}
