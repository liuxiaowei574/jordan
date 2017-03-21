package com.nuctech.ls.model.bo.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[监管车辆的集装箱信息(非必填)]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_COMMON_GOODS_TYPE")
public class LsCommonGoodsTypeBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7564183539671492367L;

    /* 主键id */
    private int goodtypeId;

    /* 类型名称 */
    private String gtypeName;

    /* 国际编码 */
    private String iSerial;
    /* 父类ID */
    private int parentId;
    /* 备注 */
    private String bak;
    /* 低风险等级值 */
    private float lowRiskV;
    /* 中风险等级值 */
    private float midRiskV;
    /* 高风险等级值 */
    private float hightRiskV;

    /* 货物类型 */
    private String goodsType;

    /**
     * 缺省的构造函数
     */
    public LsCommonGoodsTypeBO() {
        super();
    }

    @Id
    @Column(name = "GOODTYPE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getGoodtypeId() {
        return goodtypeId;
    }

    public void setGoodtypeId(int goodtypeId) {
        this.goodtypeId = goodtypeId;
    }

    @Column(name = "GTYPE_NAME", nullable = false, length = 200)
    public String getGtypeName() {
        return gtypeName;
    }

    public void setGtypeName(String gtypeName) {
        this.gtypeName = gtypeName;
    }

    @Column(name = "I_SERIAL", nullable = true, length = 50)
    public String getiSerial() {
        return iSerial;
    }

    public void setiSerial(String iSerial) {
        this.iSerial = iSerial;
    }

    @Column(name = "PARENT_ID")

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Column(name = "BAK", nullable = true, length = 500)
    public String getBak() {
        return bak;
    }

    public void setBak(String bak) {
        this.bak = bak;
    }

    @Column(name = "LOW_RISK_V")
    public float getLowRiskV() {
        return lowRiskV;
    }

    public void setLowRiskV(float lowRiskV) {
        this.lowRiskV = lowRiskV;
    }

    @Column(name = "MID_RISK_V")
    public float getMidRiskV() {
        return midRiskV;
    }

    public void setMidRiskV(float midRiskV) {
        this.midRiskV = midRiskV;
    }

    @Column(name = "HIGHT_RISK_V")
    public float getHightRiskV() {
        return hightRiskV;
    }

    public void setHightRiskV(float hightRiskV) {
        this.hightRiskV = hightRiskV;
    }

    @Column(name = "GOODS_TYPE")
    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }
}
