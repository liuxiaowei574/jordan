/**
 * 
 */
package com.nuctech.ls.model.util;

import com.nuctech.util.MessageResourceUtil;

/**
 * 报警类型
 * 
 * @author sunming
 *
 */
public enum GoodsType {
    /**
     * 容器
     */
    GOODS_TYPE0("0"),
    /**
     * 活动物及动物制品
     */
    GOODS_TYPE1("1"),
    /**
     * 蔬菜制品
     */
    GOODS_TYPE2("2"),
    /**
     * 动物或植物脂肪和油及其精制食用油脂
     */
    GOODS_TYPE3("3"),
    /**
     * 精制食物、饮料、酒和醋、烟草
     */
    GOODS_TYPE4("4"),
    /**
     * 矿物质
     */
    GOODS_TYPE5("5"),
    /**
     * 化学相关产业产品
     */
    GOODS_TYPE6("6"),
    /**
     * 塑料及其制品
     */
    GOODS_TYPE7("7"),
    /**
     * 生皮及皮革
     */
    GOODS_TYPE8("8"),
    /**
     * 木及木制品
     */
    GOODS_TYPE9("9"),
    /**
     * 纸浆木材或其他纤维
     */
    GOODS_TYPE10("10"),
    /**
     * 纺织品和纺织制品
     */
    GOODS_TYPE11("11"),
    /**
     * 鞋、帽、伞类
     */
    GOODS_TYPE12("12"),
    /**
     * 石料、石膏、水泥
     */
    GOODS_TYPE13("13"),
    /**
     * 天然或养殖珍珠
     */
    GOODS_TYPE14("14"),
    /**
     * 贱金属
     */
    GOODS_TYPE15("15"),
    /**
     * 机器及机械设备
     */
    GOODS_TYPE16("16"),
    /**
     * 车辆、飞机、船舶和相关的运输设备
     */
    GOODS_TYPE17("17"),
    /**
     * 光学、照相、电影设备
     */
    GOODS_TYPE18("18"),
    /**
     * 武器、弹药
     */
    GOODS_TYPE19("19"),
    /**
     * 杂项制品
     */
    GOODS_TYPE20("20"),
    /**
     * 艺术品
     */
    GOODS_TYPE21("21"),
    /**
     * 特殊交易品
     */
    GOODS_TYPE22("22");

    private String type;

    private GoodsType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 从资源文件读取国际化值
     * 
     * @return
     */
    public String getKey() {
        return MessageResourceUtil.getMessageInfo(this.getClass().getSimpleName() + "." + this.name());
    }

}
