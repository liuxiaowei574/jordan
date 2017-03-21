/**
 * 
 */
package com.nuctech.util;

/**
 * 设备辅助方法
 * 
 * @author sunming
 *
 */
public class DeviceUtils {

    /**
     * 获取关锁电量百分比
     * 
     * @param vvv
     *        当前电量值 单位：mV毫伏
     * @return
     */
    public static int getElockBatteryPercentageOfHHD(String vvv) {

        if (null != vvv && !"".equals(vvv) && !vvv.contains(".")) {
            Integer v = Integer.valueOf(vvv);

            if (v >= 300 && v < 345)
                return 0;
            else if (v >= 345 && v < 368)
                return 5;
            else if (v >= 368 && v < 374)
                return 10;
            else if (v >= 374 && v < 377)
                return 20;
            else if (v >= 377 && v < 379)
                return 30;
            else if (v >= 379 && v < 382)
                return 40;
            else if (v >= 382 && v < 387)
                return 50;
            else if (v >= 387 && v < 392)
                return 60;
            else if (v >= 392 && v < 398)
                return 70;
            else if (v >= 398 && v < 406)
                return 80;
            else if (v >= 406 && v < 420)
                return 90;
            else if (v == 420)
                return 100;
            else
                return 9999;
        }
        return 9999;
    }

}
