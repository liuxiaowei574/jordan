package com.nuctech.ls.common.page;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * JSON 日期格式处理（java转化为JSON）
 * 
 * @author sailor
 */
public class JsonDateValueProcessor implements JsonValueProcessor {

    /**
     * datePattern
     */
    private String datePattern = "yyyy-MM-dd";
    // 长日期格式
    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    // 长日期格式 精确到毫秒
    public static String LONG_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * JsonDateValueProcessor
     */
    public JsonDateValueProcessor() {
        super();
    }

    /**
     * @param format
     */
    public JsonDateValueProcessor(String format) {
        super();
        this.datePattern = format;
    }

    /**
     * @param value
     * @param jsonConfig
     * @return Object
     */
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    /**
     * @param key
     * @param value
     * @param jsonConfig
     * @return Object
     */
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    /**
     * process
     * 
     * @param value
     * @return
     */
    private Object process(Object value) {
        try {
            if (value instanceof Date) {
                SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.getDefault());
                return sdf.format((Date) value);
            }
            return value == null ? "" : value.toString();
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * @return the datePattern
     */
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * @param pDatePattern
     *        the datePattern to set
     */
    public void setDatePattern(String pDatePattern) {
        datePattern = pDatePattern;
    }

}