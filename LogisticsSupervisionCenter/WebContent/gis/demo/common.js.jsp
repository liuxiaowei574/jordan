<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setBundle basename="i18n.messages" var="commonBundle"/>

var vehicleWithAlarmIconSrc = getRootPath()+"/images/car_red.png";
var vehicleWithoutAlarmIconSrc =getRootPath()+ "/images/car_blue.png";
var alarm_vehicle = getRootPath()+"/images/car_red.png";
var alarm_red = getRootPath()+"/images/alarm_red.png";
var alarm_orange =getRootPath()+ "/images/alarm_orange.png";
var alarm_yellow =getRootPath()+ "/images/alarm_yellow.png";
var alarm_blue =getRootPath()+ "/images/alarm_blue.png";

var LabelProperties = {
    padding: '<fmt:message key="LabeledText.style.padding" bundle="${commonBundle}"/>',
    background: '<fmt:message key="LabeledText.style.background" bundle="${commonBundle}"/>',
    borderStyle: '<fmt:message key="LabeledText.style.borderStyle" bundle="${commonBundle}"/>',
    borderWidth: '<fmt:message key="LabeledText.style.borderWidth" bundle="${commonBundle}"/>',
    cursor: '<fmt:message key="LabeledText.style.cursor" bundle="${commonBundle}"/>',
    textAlign: '<fmt:message key="LabeledText.style.textAlign" bundle="${commonBundle}"/>',
    fontFamily: '<fmt:message key="LabeledText.style.fontFamily" bundle="${commonBundle}"/>',
    fontSize: '<fmt:message key="LabeledText.style.fontSize" bundle="${commonBundle}"/>',
    paddingLeft: '<fmt:message key="LabeledText.style.paddingLeft" bundle="${commonBundle}"/>',
    paddingRight: '<fmt:message key="LabeledText.style.paddingRight" bundle="${commonBundle}"/>',
    position: '<fmt:message key="LabeledText.style.position" bundle="${commonBundle}"/>',
    color: '<fmt:message key="LabeledText.style.color" bundle="${commonBundle}"/>',
    fontWeight: '<fmt:message key="LabeledText.style.fontWeight" bundle="${commonBundle}"/>'
};

//为口岸添加标签
var portLabelStyle = {
	border: "1px solid #5B5BFF",
	padding: "2px", 
	color: "#5B5BFF", 
	background: "#ffffff", 
	font: "bold 12px"
};

var HomeControlText = {
    padding: '<fmt:message key="HomeControl.style.padding" bundle="${commonBundle}"/>',
    backgroundColor: '<fmt:message key="HomeControl.style.backgroundColor" bundle="${commonBundle}"/>',
    borderStyle: '<fmt:message key="HomeControl.style.borderStyle" bundle="${commonBundle}"/>',
    borderWidth: '<fmt:message key="HomeControl.style.borderWidth" bundle="${commonBundle}"/>',
    cursor: '<fmt:message key="HomeControl.style.cursor" bundle="${commonBundle}"/>',
    textAlign: '<fmt:message key="HomeControl.style.textAlign" bundle="${commonBundle}"/>',
    fontFamily: '<fmt:message key="HomeControl.style.fontFamily" bundle="${commonBundle}"/>',
    fontSize: '<fmt:message key="HomeControl.style.fontSize" bundle="${commonBundle}"/>',
    paddingLeft: '<fmt:message key="HomeControl.style.paddingLeft" bundle="${commonBundle}"/>',
    paddingRight: '<fmt:message key="HomeControl.style.paddingRight" bundle="${commonBundle}"/>',
    position: '<fmt:message key="HomeControl.style.position" bundle="${commonBundle}"/>',
    color: '<fmt:message key="HomeControl.style.color" bundle="${commonBundle}"/>',
    fontWeight: '<fmt:message key="HomeControl.style.fontWeight" bundle="${commonBundle}"/>'
};

var mapBoundaryStyle = {
    color: '<fmt:message key="MapBoundary.roadMap.strokeColor" bundle="${commonBundle}"/>',
    roadMapColor: '<fmt:message key="MapBoundary.roadMap.strokeColor" bundle="${commonBundle}"/>',
    hybridColor: '<fmt:message key="MapBoundary.hybrid.strokeColor" bundle="${commonBundle}"/>',
    weight: '<fmt:message key="MapBoundary.strokeWeight" bundle="${commonBundle}"/>',
    opacity: '<fmt:message key="MapBoundary.strokeOpacity" bundle="${commonBundle}"/>'
};

var basePolylineStyle = {
    color: '<fmt:message key="Polyline.base.strokeColor" bundle="${commonBundle}"/>',
    weight: '<fmt:message key="Polyline.base.strokeWeight" bundle="${commonBundle}"/>',
    opacity: '<fmt:message key="Polyline.base.strokeOpacity" bundle="${commonBundle}"/>'
};

var realPolylineStyle = {
    color: '<fmt:message key="Polyline.real.strokeColor" bundle="${commonBundle}"/>',
    weight: '<fmt:message key="Polyline.real.strokeWeight" bundle="${commonBundle}"/>',
    opacity: '<fmt:message key="Polyline.real.strokeOpacity" bundle="${commonBundle}"/>'
};

var planPolylineStyle = {
    color: '<fmt:message key="Polyline.plan.strokeColor" bundle="${commonBundle}"/>',
    weight: '<fmt:message key="Polyline.plan.strokeWeight" bundle="${commonBundle}"/>',
    opacity: '<fmt:message key="Polyline.plan.strokeOpacity" bundle="${commonBundle}"/>'
};

var drawPolylineStyle = {
    color: '<fmt:message key="Polyline.draw.strokeColor" bundle="${commonBundle}"/>',
    weight: '<fmt:message key="Polyline.draw.strokeWeight" bundle="${commonBundle}"/>',
    opacity: '<fmt:message key="Polyline.draw.strokeOpacity" bundle="${commonBundle}"/>',
    fillColor: '<fmt:message key="Polyline.draw.fillColor" bundle="${commonBundle}"/>',
    fillOpacity: '<fmt:message key="Polyline.draw.fillOpacity" bundle="${commonBundle}"/>'
};

var mapPolygonStyle = {
    color: '<fmt:message key="Polygon.color" bundle="${commonBundle}"/>',
    weight: '<fmt:message key="Polygon.weight" bundle="${commonBundle}"/>',
    opacity: '<fmt:message key="Polygon.opacity" bundle="${commonBundle}"/>',
    fillColor: '<fmt:message key="Polygon.fillColor" bundle="${commonBundle}"/>',
    fillOpacity: '<fmt:message key="Polygon.fillOpacity" bundle="${commonBundle}"/>'
};

/**
 * 得到演示车辆的信息窗口内容
 * @param vehicle
 * @returns {String}
 */
function getYanshiVehicleInfoWindowContent(vehicle){
    var txt = "<div class='vehicleContent'>"
            + "<table>"
            + "<tr><th>车牌号: </th><td>" + vehicle.plateNumber +"</td></tr>"
            + "<tr><th>箱号: </th><td>" + vehicle.containerNum +"</td></tr>"
            + "<tr><th>起止地: </th><td>" + (vehicle.isAsc?vehicle.origination:vehicle.teminal) +" - "+(vehicle.isAsc?vehicle.teminal:vehicle.origination)+"</td></tr>"
            + "<tr><th>出发时间: </th><td>" + showDate(vehicle.lockDate)+"</td></tr>"
            + "<tr><th>关锁状态: </th><td>" + vehicle.elockStatus +"</td></tr>"
            + "<tr><th>报关单号: </th><td><a href='javascript: openYanshiManifest(\""+vehicle.id+"\")'>" + vehicle.manifestNo +"</a></td></tr>"
            + "</table>"
            + "</div>";
    return txt;
}

/**
 * 得到报警的信息窗口内容
 * @param warning
 * @returns {String}
 */
function getAlarmInfoWindowContent(warning){
    var txt = "<div class='alarmContent'>"
            + "<table>"
            + "<tr><th>报警级别: </th><td>Level " + warning.level +"</td></tr>"
            + "<tr><th>报警类型: </th><td>" + warning.type +"</td></tr>"
            + "<tr><th>报警时间: </th><td>" + warning.warningDate+"</td></tr>"
            + "</table>"
            + "</div>";
    return txt;
}

function openYanshiManifest(vehicleId){
    var url = getRootPath() + "/test/openYanshiManifestImg.jsp";
    window.showModalDialog(url,window,'dialogWidth:1000px;dialogHeight:850px');
}


