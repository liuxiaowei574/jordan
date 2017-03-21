<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="elock.addElock" /></title>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel">车载台状态详细报告</h4>
	</div>
	<form class="form-horizontal row" id="elockAddForm" method="post">
		<div class="modal-body">
			<div class="col-md-6">
				<div class="form-group ">
					<label class="col-sm-4 control-label">位置类型</label>
					<div class="col-sm-8">
						<input type="text" id="monitorVehicleGpsBO.locationType"
							name="monitorVehicleGpsBO.locationType"
							value="${monitorVehicleGpsBO.locationType}"
							class="form-control input-sm">
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-4 control-label">定位时间</label>
					<div class="col-sm-8">
						<input type="text" id="monitorVehicleGpsBO.locationTime"
							name="monitorVehicleGpsBO.locationTime"
							value="${monitorVehicleGpsBO.locationTime}"
							class="form-control input-sm">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label">定位状态</label>
					<div class="col-sm-8">
						<input type="text" id="monitorVehicleGpsBO.locationStatus"
							name="monitorVehicleGpsBO.locationStatus"
							value="${monitorVehicleGpsBO.locationStatus}"
							class="form-control input-sm">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label">防拆状态</label>
					<div class="col-sm-8">
						<input type="text" id="monitorVehicleGpsBO.brokenStatus"
							name="monitorVehicleGpsBO.brokenStatus"
							value="${monitorVehicleGpsBO.brokenStatus}"
							class="form-control input-sm">
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-4 control-label">经度</label>
					<div class="col-sm-8">
						<input type="text" id="monitorVehicleGpsBO.longitude"
							name="monitorVehicleGpsBO.longitude"
							value="${monitorVehicleGpsBO.longitude}"
							class="form-control input-sm">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label">纬度</label>
					<div class="col-sm-8">
						<input type="text" id="monitorVehicleGpsBO.latitude"
							name="monitorVehicleGpsBO.latitude"
							value="${monitorVehicleGpsBO.latitude}"
							class="form-control input-sm">
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-4 control-label">海拔</label>
					<div class="col-sm-8">
						<input type="text" id="monitorVehicleGpsBO.altitude"
							name="monitorVehicleGpsBO.altitude"
							value="${monitorVehicleGpsBO.altitude}"
							class="form-control input-sm">
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-4 control-label">速度</label>
					<div class="col-sm-8">
						<input type="text" id="monitorVehicleGpsBO.elockSpeed"
							name="monitorVehicleGpsBO.elockSpeed"
							value="${monitorVehicleGpsBO.elockSpeed}"
							class="form-control input-sm">
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-4 control-label">行驶方向</label>
					<div class="col-sm-8">
						<input type="text" id="monitorVehicleGpsBO.direction"
							name="monitorVehicleGpsBO.direction"
							value="${monitorVehicleGpsBO.direction}"
							class="form-control input-sm">
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-4 control-label">电量</label>
					<div class="col-sm-8">
						<input type="text" id="monitorVehicleGpsBO.electricityValue"
							name="monitorVehicleGpsBO.electricityValue"
							value="${monitorVehicleGpsBO.electricityValue}"
							class="form-control input-sm">
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-4 control-label">关联设备</label>
					<div class="col-sm-8">
						<input type="text" id="monitorVehicleGpsBO.relatedDevice"
							name="monitorVehicleGpsBO.relatedDevice"
							value="${monitorVehicleGpsBO.relatedDevice}"
							class="form-control input-sm">
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="addElockButton">
				<fmt:message key="common.button.add" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal"
				id="btnCancel">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</form>
</body>
</html>