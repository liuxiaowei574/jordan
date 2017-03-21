package com.nuctech.ls.model.util;

import org.springframework.beans.BeanUtils;

public class ModelUtils {

    /**
     * 将originalObj中的属性值赋给destinationObj中的属性
     * 
     **/
    public static void cloneObject(Object originalObj, Object destinationObj) {
        BeanUtils.copyProperties(originalObj, destinationObj);
    }

    /**
     * A common method for all enums since they can't have another base class
     * 
     * @param <T>
     *        Enum type
     * @param c
     *        enum type. All enums must be all caps.
     * @param string
     *        case insensitive
     * @return corresponding enum, or null
     */
    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string) {
        if (c != null && string != null) {
            try {
                return Enum.valueOf(c, string.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

}
