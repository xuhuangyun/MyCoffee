package com.fancoff.coffeemaker.utils;

import android.util.Xml;

import com.fancoff.coffeemaker.bean.machine.Assist_temper;
import com.fancoff.coffeemaker.bean.machine.CleanBean;
import com.fancoff.coffeemaker.bean.machine.CleanTime;
import com.fancoff.coffeemaker.bean.machine.MachineConfiBean;
import com.fancoff.coffeemaker.bean.machine.Main_temper;
import com.fancoff.coffeemaker.bean.machine.Service_provider;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by apple on 2017/5/16.
 * xml解析类
 */

public class XmlUtils {
    String TAG = "XmlUtils";
    XmlReader XR = new XmlReader();
    private static final XmlUtils ourInstance = new XmlUtils();

    public static XmlUtils getInstance() {
        return ourInstance;
    }

    private XmlUtils() {
    }

    //经典的pull解析方法
    /**
     * Pull解析machineConfig.xml，解析的数据封装到MachineConfiBean类的对象中，并返回该对象；
     */
    public MachineConfiBean pullxmlM(InputStream is) throws Exception {
        XmlPullParser parse = Xml.newPullParser(); //获取XmlPullParser对象
        parse.setInput(is, "utf-8");//XML数据设置进去就可以进行解析
        return getMation(parse);  //解析数据
    }

    /**
     * 传入xml解析对象
     * 解析出来的数据存入MachineConfiBean对象中，并返回MachineConfiBean类型；
     */
    private MachineConfiBean getMation(XmlPullParser parse) throws Exception {
        MachineConfiBean ma = new MachineConfiBean();
        //产生第一个事件,就像游标一样
        int eventype = parse.getEventType();  //得到当前的解析事件
        CleanBean cleanBean = null;
        ArrayList<CleanTime> arrayList = new ArrayList<>();  //清洗时间列表
        while (eventype != XmlPullParser.END_DOCUMENT) {//解析结束标志
            switch (eventype) {
                //判断当前是不是文档开始的事件
                case XmlPullParser.START_DOCUMENT:
                    ma = new MachineConfiBean();
                    break;
                //判断当前事件是不是标签元素的开始事件
                case XmlPullParser.START_TAG:
                    String name = parse.getName();//获取当前解析器指向的元素名称
                    if (name.equals("armIn_delay")) {
                        ma.setArmIn_delay(Integer.valueOf(parse.nextText()));
                    } else if (name.equals("armOut_delay")) {
                        ma.setArmOut_delay(Integer.valueOf(parse.nextText()));
                    } else if (name.equals("auto_heat")) {
                        ma.setAuto_heat(Integer.valueOf(parse.nextText()));
                    } else if (name.equals("blender_delay")) {
                        ma.setBlender_delay(Integer.valueOf(parse.nextText()));
                    } else if (name.equals("coffee_type")) {
                        ma.setCoffee_type(Integer.valueOf(parse.nextText()));
                    } else if (name.equals("cupStuck_delay")) {
                        ma.setCupStuck_delay(Integer.valueOf(parse.nextText()));
                    } else if (name.equals("dirty_delay")) {
                        ma.setDirty_delay(Integer.valueOf(parse.nextText()));
                    } else if (name.equals("finish_delay")) {
                        ma.setFinish_delay(Integer.valueOf(parse.nextText()));
                    } else if (name.equals("preWater")) {
                        ma.setPreWater(Integer.valueOf(parse.nextText()));
                    } else if (name.equals("machine_type")) {
                        ma.setMachine_type(Integer.valueOf(parse.nextText()));
                    } else if (name.equals("report_duration")) {
                        ma.setReport_duration(Integer.valueOf(parse.nextText()));
                    } else if (name.equals("waterType")) {
                        ma.setWaterType(Integer.valueOf(parse.nextText()));
                    } else if (name.equals("auto_heat")) {
                        ma.setAuto_heat(Integer.valueOf(parse.nextText()));
                    } else if (name.equals("serial")) {
                        ma.setSerial(parse.nextText());
                    } else if (name.equals("service_provider")) {
                        Service_provider service_provider = new Service_provider();
                        LogUtil.test("xmlParse:service_provider");
                        service_provider.setName(parse.getAttributeValue(null, "name"));//根据name获取节点属性值
                        ma.setService_provider(service_provider);
                    } else if (name.equals("main_temper")) {
                        Main_temper main_temper = new Main_temper();
                        String backlash = parse.getAttributeValue(null, "backlash");//根据backlash获取节点属性值
                        LogUtil.test("xmlParse:backlash" + backlash);
                        main_temper.setBacklash(Integer.valueOf(backlash));
                        main_temper.setGoal(Integer.valueOf(parse.getAttributeValue(null, "goal")));//根据goal获取节点属性值
                        main_temper.setMin(Integer.valueOf(parse.getAttributeValue(null, "min")));//根据min获取节点属性值
                        ma.setMain_temper(main_temper);
                    } else if (name.equals("assist_temper")) {
                        LogUtil.test("xmlParse:assist_temper");
                        Assist_temper main_temper = new Assist_temper();
                        main_temper.setBacklash(Integer.valueOf(parse.getAttributeValue(null, "backlash")));//根据backlash获取节点属性值
                        main_temper.setGoal(Integer.valueOf(parse.getAttributeValue(null, "goal")));//根据goal获取节点属性值
                        ma.setAssist_temper(main_temper);
                    } else if (name.equals("cleans")) {
                        LogUtil.test("xmlParse:cleans start");
                        cleanBean = new CleanBean();
                        cleanBean.setAssist_water(Integer.valueOf(parse.getAttributeValue(null, "assist_water")));//根据assist_water获取节点属性值
                        cleanBean.setCnt(Integer.valueOf(parse.getAttributeValue(null, "cnt")));//根据cnt获取节点属性值
                        cleanBean.setMain_water(Integer.valueOf(parse.getAttributeValue(null, "main_water")));//根据main_water获取节点属性值

                    } else if (name.equals("time")) {
                        LogUtil.test("xmlParse:time");
                        CleanTime time = new CleanTime();
                        time.setTime(parse.nextText());
                        arrayList.add(time);//清洗时间加入到arrayList列表

                    }
                    break;
                // 判断当前事件是否为标签元素结束事件
                case XmlPullParser.END_TAG:

                    String nameEnd = parse.getName();//获取当前解析器指向的元素名称
                    if (nameEnd.equals("cleans")) {//最后解析的节点为cleans，将清洗时间添加到CleanBean中
                        LogUtil.test("xmlParse:cleans end");
                        cleanBean.setTimes(arrayList);
                        ma.setClean(cleanBean);
                    }

                    break;
            }
            // 进入下一个元素并触发相应事件
            eventype = parse.next();  //解析事件还没有完，next进入下一个解析事件
            //测试xmlList是否有值
        }
        LogUtil.test("xmlParse:result" + ma.toString());
        return ma;
    }

    //经典的pull解析方法
    public MachineConfiBean pullxmlM(String xml) throws Exception {
        XmlPullParser parse = Xml.newPullParser();
        parse.setInput(new StringReader(xml));
        return getMation(parse);

    }


}
