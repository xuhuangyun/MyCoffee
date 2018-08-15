package com.fancoff.coffeemaker.utils;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
/*
xml解析Reader
 */
public  class XmlReader{
    private final static String TAG = "XmlReader";
    private XmlPullParser xmlparser = null;
    public static final String charset = "utf-8";

    public XmlReader(){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xmlparser = factory.newPullParser();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"create xml reader failed");
        }
    }

    public void setXML(String xml) throws IOException{
        try {
            xmlparser.setInput(new StringReader(xml));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getNextEvent() throws IOException {
        try {
            return xmlparser.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getNextText() throws IOException{
        try {
            return xmlparser.nextText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getEventType() throws IOException {
        try {
            return xmlparser.getEventType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getName() {
        return xmlparser.getName();
    }

    public String getText() {
        return xmlparser.getText();
    }

    public int attributeCount() {
        return xmlparser.getAttributeCount();
    }

    public String getAttributeName(int i) {
        return xmlparser.getAttributeName(i);
    }

    public String getAttributeValue(String name) {
        String v = xmlparser.getAttributeValue(null, name);
        return v;
    }

    public int getDepth() {
        return xmlparser.getDepth();
    }
}; 