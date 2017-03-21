package com.nuctech.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 * 
 * 类描述： 日期时间类型值的Struts类型转换器
 */
public class DateTimeConverter extends StrutsTypeConverter {

    Logger log = Logger.getLogger(this.getClass());
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressWarnings("rawtypes")
    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values == null) {
            return null;
        }
        if (values.length == 0) {
            return null;
        }
        String value = values[0];
        if (value.length() == 0) {
            return null;
        }
        if (toClass.equals(Date.class)) {
            try {
                return dateFormat.parse(value);
            } catch (ParseException e) {
                log.warn("value \"" + value + "\" convert to java.util.Date failed.");
            }
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String convertToString(Map context, Object o) {
        if (o instanceof Date) {
            return dateFormat.format((Date) o);
        }
        log.warn("Object " + o + " is not a instance of java.util.Date.");
        return null;
    }

}
