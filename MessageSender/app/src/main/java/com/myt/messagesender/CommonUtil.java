package com.myt.messagesender;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommonUtil {

    private static final String TAG = "CommonUtil";

    /**
     * 向SD卡写入一个App配置文件
     */
    public static void saveAppXML(List<Map<String, String>> SettingData) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    "AppSetting.xml");
            FileOutputStream fos = new FileOutputStream(file);
            // 获得一个序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "utf-8");
            // 设置文件头
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "AppSetting");
            for (int i = 0; i < SettingData.size(); i++) {
                Map<String, String> Item = SettingData.get(i);
                // 应用名称
                serializer.startTag(null, "name");
                serializer.text(Item.get("name"));
                serializer.endTag(null, "name");
                // 时间
                serializer.startTag(null, "time");
                serializer.text(Item.get("time"));
                serializer.endTag(null, "time");
                // 包名
                serializer.startTag(null, "packagename");
                serializer.text(Item.get("packagename"));
                serializer.endTag(null, "packagename");

            }

            serializer.endTag(null, "AppSetting");
            serializer.endDocument();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 读取SD卡中的App配置文件,使用pull解析
     */
    public static List<Map<String, String>> readAppXML() {
        List<Map<String, String>> SettingMap = new ArrayList<>();
        List<String> AllNames = new ArrayList<>();
        List<String> AllTimes = new ArrayList<>();
        List<String> AllPackageNames = new ArrayList<>();

        try {
            File path = new File(Environment.getExternalStorageDirectory(), "AppSetting.xml");
            FileInputStream fis = new FileInputStream(path);

            // 获得pull解析器对象
            XmlPullParser parser = Xml.newPullParser();
            // 指定解析的文件和编码格式
            parser.setInput(fis, "utf-8");

            int eventType = parser.getEventType(); // 获得事件类型

            String name = null;
            String time = null;
            String packagename = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName(); // 获得当前节点的名称

                switch (eventType) {
                    case XmlPullParser.START_TAG: // 当前等于开始节点 <person>
                        if ("AppSetting".equals(tagName)) { // <persons>
                        } else if ("name".equals(tagName)) { // <name>
                            name = parser.nextText();
                            AllNames.add(name);
                        } else if ("time".equals(tagName)) { // <age>
                            time = parser.nextText();
                            AllTimes.add(time);
                        }else if ("packagename".equals(tagName)) { // <age>
                            packagename = parser.nextText();
                            AllPackageNames.add(packagename);
                        }
                        break;
                    case XmlPullParser.END_TAG: // </persons>
                        if ("person".equals(tagName)) {
                            Log.i(TAG, "id---" + time);
                            Log.i(TAG, "name---" + name);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next(); // 获得下一个事件类型
            }

            for (int i = 0;i<AllNames.size();i++){
                Map<String, String> Item = new HashMap<>();
                Item.put("name",AllNames.get(i));
                Item.put("time",AllTimes.get(i));
                Item.put("packagename",AllPackageNames.get(i));
                SettingMap.add(Item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return SettingMap;
    }


    /**
     * 将字符串数据保存到本地
     * @param context 上下文
     * @param filename 生成XML的文件名
     * @param  map <生成XML中每条数据名,需要保存的数据>
     */
    public static void saveSettingNoteStr(Context context, String filename , Map<String, String> map) {
        SharedPreferences.Editor note = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            note.putString(entry.getKey(), String.valueOf(entry.getValue()));
        }
        note.commit();
    }

    /**
     * 从本地取出要保存的数据
     * @param context 上下文
     * @param filename 文件名
     * @param dataname 生成XML中每条数据名
     * @return 对应的数据(找不到为NUll)
     */
    public static String getSettingNoteStr(Context context, String filename , String dataname) {
        SharedPreferences read = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        String Value = read.getString(dataname, "");
        try {
            return Value;
        }catch (Exception ex){
            return "";
        }
    }


    /**
     * 将字符串数据保存到本地
     * @param context 上下文
     * @param filename 生成XML的文件名
     * @param  map <生成XML中每条数据名,需要保存的数据>
     */
    public static void saveSettingNote(Context context, String filename , Map<String, Float> map) {
        SharedPreferences.Editor note = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        Iterator<Map.Entry<String, Float>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Float> entry = (Map.Entry<String, Float>) it.next();
            note.putString(entry.getKey(), String.valueOf(entry.getValue()));
        }
        note.commit();
    }

    /**
     * 从本地取出要保存的数据
     * @param context 上下文
     * @param filename 文件名
     * @param dataname 生成XML中每条数据名
     * @return 对应的数据(找不到为NUll)
     */
    public static float getSettingNote(Context context, String filename , String dataname) {
        SharedPreferences read = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        String Value = read.getString(dataname, "");
        try {
            return Float.parseFloat(Value);
        }catch (Exception ex){
            return 0;
        }
    }


    /**
     * 将字符串数据保存到本地
     * @param context 上下文
     * @param key 键
     * @param value 值
     */
    public static void saveStringByKey(Context context, String key, String value) {
        String fileName = context.getPackageName();
        SharedPreferences.Editor note = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        note.putString(key, value);
        note.commit();
    }

    /**
     * 获取本地的字符串
     * @param context 上下文
     * @param key 键
     */
    public static String getStringByKey(Context context, String key) {
        String fileName = context.getPackageName();
        SharedPreferences read = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String Value = read.getString(key, "");
        try {
            return Value;
        }catch (Exception ex){
            return "";
        }
    }

    public static Application application;

    public static int getIntByKey(String key) {
        Context context = application;
        String fileName = context.getPackageName();
        SharedPreferences read = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        int Value = read.getInt(key, -1);
        return Value;
    }

    public static int getIntByKey(String key, int defaultInt) {
        Context context = application;
        String fileName = context.getPackageName();
        SharedPreferences read = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        int Value = read.getInt(key, defaultInt);
        return Value;
    }

    public static void setIntByKey(String key, int value) {
        Context context = application;
        String fileName = context.getPackageName();
        SharedPreferences.Editor note = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        note.putInt(key, value);
        note.commit();
    }


    public static void saveIntByKey(Context context, String key, int value) {
        String fileName = context.getPackageName();
        SharedPreferences.Editor note = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        note.putString(key, String.valueOf(value));
        note.commit();
    }

    public static int getIntByKey(Context context, String key) {
        String fileName = context.getPackageName();
        SharedPreferences read = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String Value = read.getString(key, "");
        try {
            return Integer.parseInt(Value);
        }catch (Exception ex){
            return -1;
        }
    }

    public static int getIntByKey(Context context, String key, int defaultInt) {
        String fileName = context.getPackageName();
        SharedPreferences read = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        int Value = read.getInt(key, defaultInt);
        return Value;
    }

    public static boolean getBoolByKey(Context context, String key, boolean defaultBool) {
        String fileName = context.getPackageName();
        SharedPreferences read = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        boolean Value = read.getBoolean(key, defaultBool);
        return Value;
    }

    public static void setBoolByKey(Context context, String key, boolean value) {
        String fileName = context.getPackageName();
        SharedPreferences.Editor note = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        note.putBoolean(key, value);
        note.commit();
    }

    public static void deleteKey(Context context, String key) {
        String fileName = context.getPackageName();
        SharedPreferences read = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = read.edit().remove(key);
        editor.apply();
    }

    public static void deleteKey(Context context, List<String> keyList) {
        String fileName = context.getPackageName();
        SharedPreferences read = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = read.edit();
        for (int i = 0; i < keyList.size(); i++) {
            editor.remove(keyList.get(i));
        }
        editor.apply();
    }

    public static boolean deleteKeyNow(Context context, String key) {
        String fileName = context.getPackageName();
        SharedPreferences read = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = read.edit().remove(key);
        return editor.commit();
    }
}
