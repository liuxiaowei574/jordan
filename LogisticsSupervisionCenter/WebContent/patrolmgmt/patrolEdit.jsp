<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="WarehouseElock.list.title" /></title>
<style>
.button-margin button {
	margin-bottom: 5px;
}

#barcode {
	position: relative;
}

#barcode>canvas, #barcode>video {
	max-width: 50%;
	width: 50%;
}

canvas.drawing, canvas.drawingBuffer {
	position: absolute;
	/* left: 0; */
	left: 25%;
	top: 0;
}
</style>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel">
			<fmt:message key="patrol.modify" />
		</h4>
	</div>
	<form class="form-horizontal row" id="patrolEditForm" method="post">
		<input type="hidden" class="form-control input-sm"
			id="lsCommonPatrolBO.patrolId" name="lsCommonPatrolBO.patrolId"
			value="${lsCommonPatrolBO.patrolId}"> <input type="hidden"
			class="form-control input-sm" id="lsCommonPatrolBO.createTime"
			name="lsCommonPatrolBO.createTime"
			value="${lsCommonPatrolBO.createTime }"> <input type="hidden"
			class="form-control input-sm" id="lsCommonPatrolBO.createUser"
			name="lsCommonPatrolBO.createUser"
			value="${lsCommonPatrolBO.createUser}"> <input type="hidden"
			class="form-control input-sm" id="lsCommonPatrolBO.deleteMark"
			name="lsCommonPatrolBO.deleteMark"
			value="${lsCommonPatrolBO.deleteMark}">


		<div class="modal-body">
			<div class="col-md-12">
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="patrol.number" /></label>
					<div class="col-sm-8">
						<input type="text" id="lsCommonPatrolBO.patrolNumber"
							name="lsCommonPatrolBO.patrolNumber" class="form-control"
							value="${lsCommonPatrolBO.patrolNumber}">
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="patrol.type" /></label>
					<div class="col-sm-8">
						<s:select name="lsCommonPatrolBO.patrolType" emptyOption="true"
							cssClass="form-control" theme="simple"
							list="@com.nuctech.ls.model.util.Patroltype@values()"
							listKey="text" listValue="key">
						</s:select>
					</div>
				</div>
				
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="patrol.label.trackUnitNumber" /></label>
					<div class="col-sm-8">
						<select style="" id="lsCommonPatrolBO.trackUnitNumber"
							name="lsCommonPatrolBO.trackUnitNumber" class="form-control">
							<option value="${lsCommonPatrolBO.trackUnitNumber}">${lsCommonPatrolBO.trackUnitNumber}</option>
							<c:forEach var="LsWarehouseTrackUnitBO" items="${trackList}">
								<option value=${LsWarehouseTrackUnitBO.trackUnitNumber}>${LsWarehouseTrackUnitBO.trackUnitNumber}</option>
							</c:forEach>
						</select>
					</div>
				</div>


				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="patrol.vehicle.number" /></label>
					<div class="col-sm-8">
						<select style="" id="lsCommonPatrolBO.vehiclePlateNumber"
							name="lsCommonPatrolBO.vehiclePlateNumber" class="form-control">
							<option value="${lsCommonPatrolBO.vehiclePlateNumber}">${lsCommonPatrolBO.vehiclePlateNumber}</option>
							<c:forEach var="lsCommonPatrolBO" items="${vehicleList}">
								<option value=${lsCommonPatrolBO.vehiclePlateNumber}>${lsCommonPatrolBO.vehiclePlateNumber}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="main.list.commonPatrol.belongToArea" /></label>
					<div class="col-sm-8">
						<select style="" id="lsCommonPatrolBO.belongToArea"
							name="lsCommonPatrolBO.belongToArea" class="form-control">
							<option value="${lsMonitorRouteAreaBO.routeAreaId}">${lsMonitorRouteAreaBO.routeAreaName}</option>
							<c:forEach var="LsMonitorRouteAreaBO" items="${areaList}">
								<option value=${LsMonitorRouteAreaBO.routeAreaId}>${LsMonitorRouteAreaBO.routeAreaName}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<%--
				<div class="form-group">
					<label class="col-sm-4 control-label"><fmt:message
							key="WarehouseElock.belongTo" /></label>
					<div class="col-sm-8">
						<select id="lsCommonPatrolBO.belongToPort"
							name="lsCommonPatrolBO.belongToPort" class="form-control">

							<option value="${lsSystemDepartmentBO.organizationId}">${lsSystemDepartmentBO.organizationName}</option>

							<c:forEach var="SystemDepartmentBO" items="${deptList}">
								<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				 --%>

				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="monitorTrip.label.potralUser" /></label>
					<div class="col-sm-8">
						<select style="" id="lsCommonPatrolBO.potralUser"
							name="lsCommonPatrolBO.potralUser" class="form-control">
							<option value="${lsSystemUserBO.userId}">${lsSystemUserBO.userAccount}</option>
							<c:forEach var="LsSystemUserBO" items="${userList}">
								<option value=${LsSystemUserBO.userId}>${LsSystemUserBO.userAccount}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<!-- 修改已经传的照片（先把照片显示出来，再重新拍照）-->
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="patrol.trackunit.install.image" /></label>
					<div class="col-sm-8">
						<div class="panel panel-info">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" data-parent="#accordion"
										href="#collapseTwo"> <fmt:message
											key="trip.activate.title.imageInfo" />
									</a>
								</h4>
							</div>
							<!--从http或者本地获取图片路径显示出来  -->
							<div id="trackUnitInstallPhoto">
								<div class="carousel-inner" role="listbox">
									<c:forEach var="patrolInstalPicture"
										items="${fn:split(lsCommonPatrolBO.patrolInstalPicture, ',') }"
										varStatus="status">
										<c:set var="paths"
											value="${fn:split(patrolInstalPicture, '/') }" />
										<c:set var="index" value="${fn:length(paths) - 1}" />
										<c:set var="imageName" value="${paths[index] }" />
										<div class="item ${(status.index == 0) ? 'active' : '' }">
											<a href="javascript:void(0);"> <img id="trackPhoto"
												src="${rootPathHttp }${trackUnitPhotoPath }${patrolInstalPicture }"
												alt="${imageName }">
											</a>
										</div>
									</c:forEach>
								</div>
								<div>
									<a href="javascript:void(0);" class="delete"
										onclick="deletePhoto();"> <span
										class="glyphicon glyphicon-trash"></span>
									</a>
								</div>
							</div>

							<div id="collapseTwo" class="panel-collapse collapse in">
								<div class="panel-body">
									<div class="col-sm-12 has-error file-help-block"
										style="display: none;">
										<small class="help-block" style=""><fmt:message
												key="trip.activate.info.image.required" /></small>
									</div>
									<div class="row" id="row"></div>
									<span name="info-upload" style="padding-left: 15px;"> <fmt:message
											key="trip.activate.info.selectFiles">
											<fmt:param>0</fmt:param>
											<fmt:param>0KB</fmt:param>
										</fmt:message>
									</span> , <span name="info-camera"> <fmt:message
											key="trip.activate.info.cameraPhotos">
											<fmt:param>0</fmt:param>
											<fmt:param>0KB</fmt:param>
										</fmt:message>
									</span>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!--添加车载台安装照片  -->
				<div>
					<div class="col-sm-4"></div>
					<div class="col-sm-8">
						<ul>
							<li><a href="#" id="btnCamera"><fmt:message
										key="trip.activate.button.fromCamera" /></a></li>
						</ul>
					</div>
				</div>
			</div>
			<!-- camara -->
			<div id="main" class="masthead"
				style="display: none; text-align: center;">
				<div id="face_scan_camera" class="blackbg" style="height: auto;">
					<div style="width: 600px; margin: 0 auto;">
						<video id="video" width="600" height="460" autoplay="autoplay"
							style="margin: 0 auto; position: relative; z-index: 100;"></video>
					</div>
					<div class="scan-area"
						style="height: 600px; width: 500px; display: none;">
						<canvas id="canvas" width="600" height="460"
							style="display: inline-block; margin: 0 auto; position: relative; left: 13px; top: 70px; z-index: 100;"></canvas>
					</div>
					<div class="btn-group">
						<button type="button" class="btn btn-default" id="btnSnap">
							<fmt:message key="trip.activate.button.snap" />
						</button>
						<button type="button" class="btn btn-default" id="btnCameraClose">
							<fmt:message key="trip.activate.button.closeCamera" />
						</button>
					</div>
				</div>
				<div id="cream_loading"
					style="display: none; position: relative; margin-top: -25%; left: 48%; height: 124px; width: 124px; z-index: 2001;">
					<img src="${root }/static/images/loading.gif" />
				</div>
			</div>
		</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="modifyButton">
				<fmt:message key="common.button.save" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</form>
	<script type="text/javascript">
		$(function() {
			buildElockEditForm();
		});

		/**
		 * 
		 */
		function buildElockEditForm() {
			//设置验证
			$('#patrolEditForm').bootstrapValidator({
				message : $.i18n.prop("common.message.form.validator"),
				fields : {
					'lsCommonPatrolBO.trackUnitNumber' : {
						validators : {
							notEmpty : {}
						}
					},
					'lsCommonPatrolBO.belongToArea' : {
						validators : {
							notEmpty : {}
						}
					},
					/*
					'lsCommonPatrolBO.belongToPort' : {
						validators : {
							notEmpty : {}
						}
					},
					 */
					'lsCommonPatrolBO.potralUser' : {
						validators : {
							notEmpty : {}
						}
					},
					'lsCommonPatrolBO.patrolNumber' : {
						validators : {
							notEmpty : {}
						}
					}
				/* 'lsCommonPatrolBO.vehiclePlateNumber' : {
					validators : {
						notEmpty : {}
					}
				}, */
				}
			}).on(
					'success.form.bv',
					function(e) {//bootstrapvalidator 0.5.2用法
						e.preventDefault();
						var $form = $(e.target);
						var bv = $form.data('bootstrapValidator');
						var serialize = $form.serialize();
						var url = '${root }/patrolMgmt/editPatrol.action'
						$.post(url, serialize, function(data) {
							if (!needLogin(data)) {
								if (data) {
									bootbox.success($.i18n
											.prop('patrol.modify.success'));
									$('#updatePatrolModal').modal('hide');
									$table.bootstrapTable('refresh', {});
								} else {
									bootbox.error($.i18n
											.prop('patrol.modify.fail'));
									$('#updatePatrolModal').modal('hide');
									$table.bootstrapTable('refresh', {});
								}
							}
						}, "json");
					});
		}
	</script>

	<script type="text/javascript">
		function deletePhoto() {
			$("#trackUnitInstallPhoto").empty();
		}
	</script>
	<script type="text/javascript" src="${root }/static/js/gumwrapper.js"></script>
	<script type="text/javascript"
		src="${root}/patrolmgmt/trackUnitImage.js"></script>
</body>
</html>
