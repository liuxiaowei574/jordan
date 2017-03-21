package com.nuctech.ls.model.vo.monitor;

/**
 * 车辆、司机信息VO类
 * 
 * @author liushaowei
 *
 */
public class CommonVehicleDriverVO {

    /** 车辆主键 */
    private String vehicleId;

    /** 车牌号 */
    private String vehiclePlateNumber;

    /** 追踪终端号 */
    private String trackingDeviceNumber;

    /** 子锁号，多个用英文逗号隔开 */
    private String esealNumber;

    /** 传感器编号，多个用英文逗号隔开 */
    private String sensorNumber;

    /** 集装箱号，多个用英文逗号隔开 */
    private String containerNumber;

    /** 车辆国家 */
    private String vehicleCountry;

    /** 拖车号 */
    private String trailerNumber;

    /** 车辆类型：0-普通车辆; */
    private String vehicleType;

    /** 子锁顺序 */
    private String esealOrder;

    /** 传感器顺序 */
    private String sensorOrder;

    /** 货物分类，多个用英文逗号隔开 */
    private String goodsType;

    /** 货物分类Name，多个用英文逗号隔开 */
    private String goodsTypeName;

    /** 检入图片路径，多个用英文逗号隔开 */
    private String checkinPicture;

    /** 检出图片路径，多个用英文逗号隔开 */
    private String checkoutPicture;

    /** 司机主键 */
    private String driverId;

    /** 行程主键 */
    private String tripId;

    /** 司机姓名 */
    private String driverName;

    /** 司机国家 */
    private String driverCountry;

    /** 司机idCard（唯一） */
    private String driverIdCard;

    /**
     * 0. 绿色 1. 黄色 2. 红色
     */
    private String riskStatus;

    public CommonVehicleDriverVO() {
        super();
    }

    public CommonVehicleDriverVO(String vehicleId, String vehiclePlateNumber, String trackingDeviceNumber,
            String esealNumber, String sensorNumber, String containerNumber, String vehicleCountry,
            String trailerNumber, String vehicleType, String esealOrder, String sensorOrder, String goodsType,
            String goodsTypeName, String checkinPicture, String checkoutPicture, String driverId, String tripId,
            String driverName, String driverCountry, String driverIdCard, String riskStatus) {
        super();
        this.vehicleId = vehicleId;
        this.vehiclePlateNumber = vehiclePlateNumber;
        this.trackingDeviceNumber = trackingDeviceNumber;
        this.esealNumber = esealNumber;
        this.sensorNumber = sensorNumber;
        this.containerNumber = containerNumber;
        this.vehicleCountry = vehicleCountry;
        this.trailerNumber = trailerNumber;
        this.vehicleType = vehicleType;
        this.esealOrder = esealOrder;
        this.sensorOrder = sensorOrder;
        this.goodsType = goodsType;
        this.goodsTypeName = goodsTypeName;
        this.checkinPicture = checkinPicture;
        this.checkoutPicture = checkoutPicture;
        this.driverId = driverId;
        this.tripId = tripId;
        this.driverName = driverName;
        this.driverCountry = driverCountry;
        this.driverIdCard = driverIdCard;
        this.riskStatus = riskStatus;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    public String getVehicleCountry() {
        return vehicleCountry;
    }

    public void setVehicleCountry(String vehicleCountry) {
        this.vehicleCountry = vehicleCountry;
    }

    public String getTrailerNumber() {
        return trailerNumber;
    }

    public void setTrailerNumber(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverCountry() {
        return driverCountry;
    }

    public void setDriverCountry(String driverCountry) {
        this.driverCountry = driverCountry;
    }

    public String getDriverIdCard() {
        return driverIdCard;
    }

    public void setDriverIdCard(String driverIdCard) {
        this.driverIdCard = driverIdCard;
    }

    public String getTrackingDeviceNumber() {
        return trackingDeviceNumber;
    }

    public void setTrackingDeviceNumber(String trackingDeviceNumber) {
        this.trackingDeviceNumber = trackingDeviceNumber;
    }

    public String getEsealNumber() {
        return esealNumber;
    }

    public void setEsealNumber(String esealNumber) {
        this.esealNumber = esealNumber;
    }

    public String getSensorNumber() {
        return sensorNumber;
    }

    public void setSensorNumber(String sensorNumber) {
        this.sensorNumber = sensorNumber;
    }

    public String getEsealOrder() {
        return esealOrder;
    }

    public void setEsealOrder(String esealOrder) {
        this.esealOrder = esealOrder;
    }

    public String getSensorOrder() {
        return sensorOrder;
    }

    public void setSensorOrder(String sensorOrder) {
        this.sensorOrder = sensorOrder;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getCheckoutPicture() {
        return checkoutPicture;
    }

    public void setCheckoutPicture(String checkoutPicture) {
        this.checkoutPicture = checkoutPicture;
    }

    public String getCheckinPicture() {
        return checkinPicture;
    }

    public void setCheckinPicture(String checkinPicture) {
        this.checkinPicture = checkinPicture;
    }

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

}
