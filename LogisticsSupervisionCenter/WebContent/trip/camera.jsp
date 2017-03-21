<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="trip.activate.title"/></title>
<style>
.alert {
	position: absolute;
    width: 600px;
    z-index: 101;
    margin: 0 auto;
    text-align: center;
    left: 0;
    right: 0;
    top: 54px;
    bottom: 0;
    height: 53px;
}
</style>
</head>
<body>
<div class="modal-body">
	<!-- camara -->
	<div id="main" class="masthead" style="text-align: center;">
		<div id="face_scan_camera" class="blackbg" style="height:500px; ">
			<div class="btn-group">
				<button type="button" class="btn btn-default" id="btnSnap"><fmt:message key="trip.activate.button.snap"/></button>
				<button type="button" class="btn btn-default" id="btnCameraClose"><fmt:message key="trip.activate.button.closeCamera"/></button>
			</div>
			<div style="width:600px; margin:0 auto;">
				<video id="video" width="600" height="460" autoplay="autoplay" style="margin:0 auto; position:relative; z-index:100;"></video>
			</div>
			<div class="scan-area" style="height:600px; width:500px; display:none; ">
				<canvas id="canvas" width="600" height="460" style="display:inline-block; margin:0 auto; position:relative; left:13px; top:70px; z-index:100;"></canvas>
			</div>
		</div>
		<div id="cream_loading" style="display:none;position:relative; margin-top: -25%;left:48%;height:124px;width:124px;z-index:2001;">
			<img src="${root }/static/images/loading.gif" />
		</div>
	</div>
</div>
<script src="${root}/trip/js/camera.js"></script>
</body>
</html>