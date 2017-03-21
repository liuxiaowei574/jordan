package com.nuctech.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {
    
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private DateFormat dateFormat;
    
    /**
     * 构造方法.
     * 
     * @param datePattern 日期格式
     */
    public DateJsonValueProcessor(String datePattern) {
        
        if (null == datePattern) {
            this.dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        } else {
            this.dateFormat = new SimpleDateFormat(datePattern);
        }
        
    }
    
    /*
     * （非 Javadoc）
     * @see net.sf.json.processors.JsonValueProcessor#processArrayValue(java.lang.Object, net.sf.json.JsonConfig)
     */
    @Override
    public Object processArrayValue(Object arg0, JsonConfig arg1) {
        // TODO 自动生成方法存根
        return this.process(arg0);
    }
    
    /*
     * （非 Javadoc）
     * @see net.sf.json.processors.JsonValueProcessor#processObjectValue(java.lang.String, java.lang.Object, net.sf.json.JsonConfig)
     */
    @Override
    public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
        // TODO 自动生成方法存根
        return this.process(arg1);
    }
    
    // 修改日期为空时出错
    private Object process(Object value) {
        if (value != null) {
            return this.dateFormat.format((Date) value);
        } else {
            return null;
        }
    }
    
}
