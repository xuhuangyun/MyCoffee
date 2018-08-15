package com.fancoff.coffeemaker.bean.coffe;


import com.fancoff.coffeemaker.utils.DbUtil;
import com.fancoff.coffeemaker.utils.StringUtil;
import com.fancoff.coffeemaker.utils.TimeUtil;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by apple on 2017/9/10.
 */

/**
 * 饮品的信息
 * name、sub_name、price、org_price
 * status、make_duration
 * make:制作的配置成分；
 * image：饮品对应的图片；(url、position、click(type、url、showImage(url、height、width、position)))
 * Seckill：Long _id、int coffee_id、TextBean price、String effect_time、String expiration_time、
 *          long l_effect_time、long l_expiration_time、int total；
 *
 */
public class CoffeeBean implements Serializable {
    int id;
    TextBean name;
    TextBean sub_name;
    TextBean price;
    TextBean org_price;
    int status = -1;//0售罄1正常2热卖
    int make_duration;
    MakeBean make;
    ImageBean image;
    @Expose(serialize = false, deserialize = true)  //不序列化，但反序列化
    ArrayList<SeckillBean> seckills;
    transient int sugarType;
    transient ArrayList<SeckillBean> seckillsToday = new ArrayList<>();//筛选今日秒杀活动，减轻活动过多导致机器负担
    transient long nowDday = 0;//秒杀筛选时间记录，每天23时59分59秒，筛选全部数据一次

    /**判断是否是咖啡饮品 通过咖啡豆量来判断；*/
    public boolean isCoffee() {
        return make.isCoffee();
    }

    //返回秒杀列表
    public ArrayList<SeckillBean> getSeckills() {
        return seckills;
    }

    public void setSeckills(ArrayList<SeckillBean> seckills) {
        this.seckills = seckills;
    }

    /**
     * 传入秒杀列表到本类的seckills属性
     */
    public void reSetSeckills(ArrayList<SeckillBean> seckills) {
        this.seckills = seckills;
        initSkillMap(true);
    }

    /*
        日期变化
     */
    public boolean isDayChange() {
        Date curDate = new Date();
        long day = TimeUtil.getInstance().getStartTimeOfDay(curDate).getTime();
        if (day != nowDday) {
            nowDday = day;
            return true;
        }
        return false;
    }

    /**
     * 初始化秒杀map：日期变化了，或者传入的变量init为true；
     * synchronized：一个时刻只能允许一个线程调用；
     * 获得当天的秒杀列表赋予seckillsToday
     */
    public void initSkillMap(boolean init) {
        synchronized ("skiling") {
            if (isDayChange() || init) {
                seckillsToday = (ArrayList<SeckillBean>) DbUtil.getInstance().selectSkillsBeanToday(id);
            }
        }
    }

    /**
     * 获取当前正在秒杀的商品
     * 1、日期变化了，则需要更新当天的秒杀列表；
     * 2、当天有秒杀，循环商品当天有秒杀数量的秒杀的列表（一个商品当天可能有好几个时间段的秒杀）
     * 3、获取当前商品的当前时间点的秒杀对象
     *
     * DataCenter类的checkSkill方法会调用该方法，遍历每个饮品都会调用该方法；
     */
    public SeckillBean isSkilling() {
        initSkillMap(false);
        synchronized ("skiling") {
            if (seckillsToday != null && seckillsToday.size() > 0) {
                for (SeckillBean sl : seckillsToday) {
                    if (sl.getTotal() > 0) {
                        if (!StringUtil.isStringEmpty(sl.getEffect_time())//影响时间字符串时间不为空，失效时间字符串时间不为空
                                && !StringUtil.isStringEmpty(sl.getExpiration_time())) {
                            boolean is = TimeUtil.getInstance().isBettew(sl.getEffect_time(), sl.getExpiration_time());
                            //当前循环到的秒杀在秒杀时间范围内
                            if (is) {
                                return sl;
                            }
                        }
                    }

                }
            }
            return null;
        }


    }

    public int getSugarType() {
        return sugarType;
    }

    public void setSugarType(int sugarType) {
        this.sugarType = sugarType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TextBean getName() {
        return name;
    }

    public void setName(TextBean name) {
        this.name = name;
    }

    public TextBean getSub_name() {
        return sub_name;
    }

    public void setSub_name(TextBean sub_name) {
        this.sub_name = sub_name;
    }

    public TextBean getPrice() {
        return price;
    }

    public void setPrice(TextBean price) {
        this.price = price;
    }

    public TextBean getOrg_price() {
        return org_price;
    }

    public void setOrg_price(TextBean org_price) {
        this.org_price = org_price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMake_duration() {
        return make_duration;
    }

    public void setMake_duration(int make_duration) {
        this.make_duration = make_duration;
    }

    public MakeBean getMake() {
        return make;
    }

    public void setMake(MakeBean make) {
        this.make = make;
    }

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
    }

    /**
     * 得到制作指令的第4、5字节  sugarType
     */
    public byte[] getMakigBodyByte() {
        return make.getMakigBodyByte(sugarType);
    }

    /**
     * 得到饮品粉水工艺表字节数组  32字节
     */
    public byte[] getByteBody() {
        if (make == null) {
            make = new MakeBean();
        }
        return make.getByteBody();
    }


    /**
     * 1、得到当天第一个秒杀的饮品；
     * 2、有秒杀，得到秒杀价格，秒杀value
     */
    public String getPayPrice() {
        SeckillBean sb = isSkilling();
        return sb != null ? sb.getPrice().getValue() : getPrice().getValue();
    }

    @Override
    public String toString() {
        return "CoffeeBean{" +
                "id=" + id +
                ", name=" + name +
                ", sub_name=" + sub_name +
                ", price=" + price +
                ", org_price=" + org_price +
                ", status=" + status +
                ", make_duration=" + make_duration +
                ", make=" + make +
                ", image=" + image +
                ", seckillsToday=" + seckillsToday +
                '}';
    }
}
