<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<div class="panel-body text-left" id="portTable">
 <ul>
  <c:forEach var="port" items="${dispatchPortList }">
  	<li>
  		<input type="checkbox" name="check-box" value="${port.portId }"/>
  		<p class="port_name">${port.portName }</p>
  		<a href="#" class="Tracking-device">${port.trackDeviceNumber }</a>
  		<a href="#" class="E-Seal">${port.eseal }</a>
  		<a href="#" class="Sensor">${port.sensor }</a>
  	</li>
  </c:forEach>
 </ul>
</div>
<script src="${root}/static/js/index.js"></script>
