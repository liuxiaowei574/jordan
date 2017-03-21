<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<style>
#checkinPictures a img, #checkoutPictures a img {
	width: 150px;
	height: 150px;
}
</style>
</head>

<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="alarmHandlerTitle">
			<fmt:message key="alarm.label.alarmHandler" />
		</h4>
	</div>
	<%-- 
	<div class="panel-group" id="accordion">
		<div class="panel panel-info">
			<div class="panel-heading">
	         <h4 class="panel-title">
	            <a data-toggle="collapse" href="#collapseOne">
	               	<fmt:message key="trip.info.tripInfo"/>
	            </a>
	         </h4>
	      </div>
	      <div id="collapseOne" class="panel-collapse collapse">
	        <div class="panel-body">
				<div id="tripDetailCheckOut" class="fixed-table-container" style="background-color: #f9f9f9; text-align: center; ">
					<table class="table table-hover table-striped">
						<tr>
							<th><div class="th-inner"><fmt:message key="trip.label.trackingDeviceNumber"/></div></th>
							<td id="_tripVehicleVO.trackingDeviceNumber" name="trackingDeviceNumber" style="border-top: 1px solid #ddd;">${tripVehicleVO.trackingDeviceNumber }</td>
							<th><div class="th-inner"><fmt:message key="trip.label.esealNumber"/></div></th>
							<td id="_tripVehicleVO.esealNumber" style="border-top: 1px solid #ddd;">${tripVehicleVO.esealNumber }</td>
						</tr>
						<tr>
							<th><div class="th-inner"><fmt:message key="trip.label.sensorNumber"/></div></th>
							<td id="_tripVehicleVO.sensorNumber">${tripVehicleVO.sensorNumber }</td>
							<th><div class="th-inner"><fmt:message key="trip.label.declarationNumber"/></div></th>
							<td id="_tripVehicleVO.declarationNumber">${tripVehicleVO.declarationNumber }</td>
						</tr>
						<tr>
							<th><div class="th-inner"><fmt:message key="trip.label.vehiclePlateNumber"/></div></th>
							<td id="_tripVehicleVO.vehiclePlateNumber">${tripVehicleVO.vehiclePlateNumber }</td>
							<th><div class="th-inner"><fmt:message key="trip.label.trailerNumber"/></div></th>
							<td id="_tripVehicleVO.trailerNumber">${tripVehicleVO.trailerNumber }</td>
						</tr>
						<tr>
							<th><div class="th-inner"><fmt:message key="trip.label.vehicleCountry"/></div></th>
							<td id="_tripVehicleVO.vehicleCountry">${tripVehicleVO.vehicleCountry }</td>
							<th><div class="th-inner"><fmt:message key="trip.label.driverName"/></div></th>
							<td id="_tripVehicleVO.driverName">${tripVehicleVO.driverName }</td>
						</tr>
						<tr>
							<th><div class="th-inner"><fmt:message key="trip.label.driverCountry"/></div></th>
							<td id="_tripVehicleVO.driverCountry">${tripVehicleVO.driverCountry }</td>
							<th><div class="th-inner"><fmt:message key="trip.label.containerNumber"/></div></th>
							<td id="_tripVehicleVO.containerNumber">${tripVehicleVO.containerNumber }</td>
						</tr>
						<tr>
							<th><div class="th-inner"><fmt:message key="trip.label.checkinPort"/></div></th>
							<td id="_tripVehicleVO.checkinPortName">${tripVehicleVO.checkinPortName }</td>
							<th><div class="th-inner"><fmt:message key="trip.label.checkoutPort"/></div></th>
							<td id="_tripVehicleVO.checkoutPortName">${tripVehicleVO.checkoutPortName }</td>
						</tr>
						<tr>
							<th><div class="th-inner"><fmt:message key="trip.label.checkinUser"/></div></th>
							<td id="_tripVehicleVO.checkinUserName">${tripVehicleVO.checkinUserName }</td>
							<th><div class="th-inner"><fmt:message key="trip.label.checkoutUser"/></div></th>
							<td id="_tripVehicleVO.checkoutUserName">${tripVehicleVO.checkoutUserName }</td>
						</tr>
						<tr>
							<th><div class="th-inner"><fmt:message key="trip.label.checkinTime"/></div></th>
							<td id="_tripVehicleVO.checkinTime">${tripVehicleVO.checkinTime }</td>
							<th><div class="th-inner"><fmt:message key="trip.label.checkoutTime"/></div></th>
							<td id="_tripVehicleVO.checkoutTime">${tripVehicleVO.checkoutTime }</td>
						</tr>
						<tr>
							<th><div class="th-inner"><fmt:message key="trip.label.timeCost"/></div></th>
							<td id="timeCost">${tripVehicleVO.timeCost }</td>
							<th><div class="th-inner"><fmt:message key="trip.label.goodsType"/></div></th>
							<td id="_tripVehicleVO.goodsType">${tripVehicleVO.goodsType }</td>
						</tr>
						<tr>
							<th><div class="th-inner"><fmt:message key="trip.label.checkinPicture"/></div></th>
							<td id="_tripVehicleVO.checkinPicture">
								<!-- 检入图片轮播 -->
								<c:if test="${tripVehicleVO.checkinPicture != null && fn:length(tripVehicleVO.checkinPicture) > 0 }">
					        		<div id="checkinPictures" class="carousel slide" data-ride="carousel">
					        			<!-- 轮播（Carousel）指标 -->
										<ol class="carousel-indicators">
											<c:forEach var="checkinPicture" items="${fn:split(tripVehicleVO.checkinPicture, ',') }" varStatus="status">
											<li data-target="#checkinPictures" data-slide-to="${status.index }" ${(status.index == 0) ? 'class="active"' : '' }></li>
											</c:forEach>
										</ol>
					        			
					        			<!-- 轮播（Carousel）项目 -->
										<div class="carousel-inner" role="listbox">
											<c:forEach var="checkinPicture" items="${fn:split(tripVehicleVO.checkinPicture, ',') }" varStatus="status">
											<c:set var="paths" value="${fn:split(checkinPicture, '/') }" />
					        				<c:set var="index" value="${fn:length(paths) - 1}"/>
					        				<c:set var="imageName" value="${paths[index] }" />
											<div class="item ${(status.index == 0) ? 'active' : '' }">
												<a href="javascript:void(0);">
													<img src="${rootPathHttp + tripPhotoPath + '/' }${checkinPicture }" alt="${imageName }">
												</a>
											</div>
											</c:forEach>
										</div>
										
										<!-- 轮播（Carousel）导航 -->
										<a class="left carousel-control" href="#checkinPictures" role="button" data-slide="prev">
											<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
											<span class="sr-only">Previous</span>
										</a>
										<a class="right carousel-control" href="#checkinPictures" role="button" data-slide="next">
											<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
											<span class="sr-only">Next</span>
										</a>
					        		</div>
				        		</c:if>
							</td>
							<th><div class="th-inner"><fmt:message key="trip.label.checkoutPicture"/></div></th>
							<td id="_tripVehicleVO.checkoutPicture">
								<!-- 检出图片轮播 -->
								<c:if test="${tripVehicleVO.checkoutPicture != null && fn:length(tripVehicleVO.checkoutPicture) > 0}">
					        		<div id="checkoutPictures" class="carousel slide" data-ride="carousel">
					        			<!-- 轮播（Carousel）指标 -->
										<ol class="carousel-indicators">
											<c:forEach var="checkoutPicture" items="${fn:split(tripVehicleVO.checkoutPicture, ',') }" varStatus="status">
											<li data-target="#checkoutPictures" data-slide-to="${status.index }" ${(status.index == 0) ? 'class="active"' : '' }></li>
											</c:forEach>
										</ol>
					        			
					        			<!-- 轮播（Carousel）项目 -->
										<div class="carousel-inner" role="listbox">
											<c:forEach var="checkoutPicture" items="${fn:split(tripVehicleVO.checkoutPicture, ',') }" varStatus="status">
											<c:set var="paths" value="${fn:split(checkoutPicture, '/') }" />
					        				<c:set var="index" value="${fn:length(paths) - 1}"/>
					        				<c:set var="imageName" value="${paths[index] }" />
											<div class="item ${(status.index == 0) ? 'active' : '' }">
												<a href="javascript:void(0);">
													<img src="${rootPathHttp + tripPhotoPath + '/' }${checkoutPicture }" alt="${imageName }">
												</a>
											</div>
											</c:forEach>
										</div>
										
										<!-- 轮播（Carousel）导航 -->
										<a class="left carousel-control" href="#checkoutPictures" role="button" data-slide="prev">
											<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
											<span class="sr-only">Previous</span>
										</a>
										<a class="right carousel-control" href="#checkoutPictures" role="button" data-slide="next">
											<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
											<span class="sr-only">Next</span>
										</a>
					        		</div>
				        		</c:if>
							</td>
						</tr>
					</table>
				</div>
			</div>
			</div>
		</div>
		
		<div class="panel panel-info">
			<div class="panel-heading">
	         <h4 class="panel-title">
	            <a data-toggle="collapse" href="#collapseTwo">Unit Setting
	            </a>
	         </h4>
	      </div>
	      <div id="collapseTwo" class="panel-collapse collapse">
		        <div class="panel-body">
			        <div id="tripDetailCheckOut" class="fixed-table-container" style="background-color: #f9f9f9; text-align: center; ">
						<table class="table table-hover table-striped">
							<tr>
								<th><fmt:message key="track.number"/></th>
								<th><fmt:message key="track.simcard"/></th>
								<th><fmt:message key="track.interval"/></th>
								<th><fmt:message key="track.gatewayAddress"/></th>
								<th><fmt:message key="track.trackUnitStatus"/></th>
							</tr>
							<c:forEach var="unit" items="${unitList }" varStatus="status">
							<tr>
								<td>${unit.trackUnitNumber }</td>
								<td>${unit.simCard }</td>
								<td>${unit.interval }</td>
								<td>${unit.gatewayAddress }</td>
								<td>${unit.trackUnitStatus }</td>
							</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
		</div>
		
		<div class="panel panel-info">
			<div class="panel-heading">
	         <h4 class="panel-title">
	            <a data-toggle="collapse" href="#collapseThree">Unit Event
	            </a>
	         </h4>
	      </div>
	      <div id="collapseThree" class="panel-collapse collapse">
		        <div class="panel-body">
				</div>
			</div>
		</div>
		
		<div class="panel panel-info">
			<div class="panel-heading">
	         <h4 class="panel-title">
	            <a data-toggle="collapse" href="#collapseFour">Notes
	            </a>
	         </h4>
	      </div>
	      <div id="collapseFour" class="panel-collapse collapse">
		        <div class="panel-body">
				</div>
			</div>
		</div>
	</div>
	 --%>

	<form id="alarmHanlerForm" class="row" method="post">
		<input type="hidden" id="trackingDeviceNumber"
			name="trackingDeviceNumber"
			value="${tripVehicleVO.trackingDeviceNumber}">
		<div class="modal-body">
			<div class="col-md-12">
				<div class="form-group col-md-12">
					<label class="col-sm-2 control-label" for="dealMethod"><fmt:message
							key="alarm.label.dealMethod" /></label>

					<div class="form-group col-md-4">
						<%-- 
						<s:select name="dealMethod"
										theme="simple"
										emptyOption="true"
										cssClass="form-control"
										list="@com.nuctech.ls.model.util.AlarmDealMethod@values()"
										listKey="text"
										listValue="key"
										value="%{#request.pageQuery.filters.dealMethod}"
										>
						 </s:select>
						  --%>
						<select id="dealMethod" name="dealMethod" class="form-control">
							<option value=""></option>
							<option value="0"><fmt:message
									key="AlarmDealMethod.FailAlarm" /></option>
							<option value="1"><fmt:message
									key="AlarmDealMethod.NormalDeal" /></option>
						</select>
					</div>
					<c:if test="${systemModules.isAlarmPushOn()}">
						<label class="col-sm-2 control-label" for="isPunish"><fmt:message
								key="alarm.label.isPunish" /></label>
						<div class="form-group col-md-4">
							<select id="isPunish" onchange="noPunish()" name="isPunish"
								class="form-control">
								<option value=""></option>
								<option value="0"><fmt:message
										key="alarm.label.isPunish.yes" /></option>
								<option value="1"><fmt:message
										key="alarm.label.isPunish.no" /></option>
							</select>
						</div>
					</c:if>
				</div>

				<div class="form-group col-md-12">
					<!-- 车辆罚款表的主键 -->
					<c:if test="${systemModules.isAlarmPushOn()}">
						<input type="hidden" class="form-control input-sm"
							id="lsVehiclePunishBo.vpunishId"
							name="lsVehiclePunishBo.vpunishId"
							value="${lsVehiclePunishBo.vpunishType}">
						<!-- 车辆罚款类型 -->
						<label class="col-sm-2 control-label" for="s_vpunishType"><fmt:message
								key="Vehicle.type.punish.type" /></label>


						<div class="col-sm-4 form-group">
							<select style="" id="s_vpunishType" name="s_vpunishType"
								class="form-control">
								<option value=""></option>
								<c:forEach var="lsVehiclePunishBo" items="${vehiclePunishList}">
									<option value=${lsVehiclePunishBo.vpunishValue}>${lsVehiclePunishBo.vpunishType}</option>
								</c:forEach>
							</select>
						</div>
					</c:if>
					<!-- 车辆罚款金额 -->
					<c:if test="${systemModules.isAlarmPushOn()}">
						<label class="col-sm-2 control-label" for="s_vpunishValue"><fmt:message
								key="Vehicle.type.punish.value" /></label>
						<div class="col-sm-4 form-group">
							<input type="text" id="s_vpunishValue" name="s_vpunishValue"
								class="form-control" readonly="true">
						</div>
					</c:if>
				</div>
				<div class="form-group col-md-12">
					<label class="col-sm-2 control-label"
						style="margin-top: 10px; margin-bottom: 10px;"><fmt:message
							key="WarehouseElock.elockStatus" /></label>
					<div class="col-sm-4 checkbox">
						<label for="s_deviceDestroy"> <input type="checkbox"
							id="s_deviceDestroy" name="s_deviceDestroy" value="1"> <fmt:message
								key="DeviceStatus.Destory" />
						</label>
					</div>
				</div>
				<div class="form-group col-md-12">
					<c:if test="${systemModules.isAlarmPushOn()}">
						<label class="col-sm-2 control-label" for="punishContent"><fmt:message
								key="alarm.label.punishContent" /></label>
						<div class="col-sm-10">
							<textarea rows="5" cols="15" class="form-control input-sm"
								id="punishContent" name="punishContent"></textarea>
						</div>
					</c:if>
				</div>
				<div class="clearfix"></div>
				<div class="form-group col-md-12">
					<label class="col-sm-2 control-label" for="dealDesc"><fmt:message
							key="alarm.label.dealResult" /></label>
					<div class="col-sm-10" style="margin-top: 15px">
						<textarea rows="5" cols="15" class="form-control input-sm"
							id="dealDesc" name="dealDesc"></textarea>
					</div>
				</div>


				<div class="form-group  hidden">
					<label class="col-sm-2 control-label" for="dealResult"></label>
					<div class="col-sm-10">
						<input type="text" class="form-control input-sm" id="dealResult"
							name="dealResult">
					</div>
				</div>


				<div class="form-group  hidden">
					<label class="col-sm-2 control-label" for="alarmId"></label>
					<div class="col-sm-10">
						<input type="text" class="form-control input-sm" id="alarmId"
							name="alarmId">
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix margin15"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger"
				id="addAlarmHandlerButton">
				<fmt:message key="common.button.save" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</form>
	<script type="text/javascript">
        var alarmId = '${alarmId}';
        var vehicleId = "${param.vehicleId}";
        $(function() {
            bindAlarmDealAddForm();

            $("#checkinPictures, #checkoutPictures").on("click", "img", function() {
                var clone = $(this).clone();
                clone.css("max-height", "").css("border-radius", "6px");
                $('#imageModal1').removeData('bs.modal');
                $("#imageModal1 .modal-content").append(clone);
                $('#imageModal1').modal({
                    show: false,
                    backdrop: true,
                    keyboard: true
                }).modal('show').css("z-index", 1052);
                $("#imageModal1 .modal-content").css("background-color", "rgba(255, 255, 255, 0)");

                $('#imageModal1 img').on("click", function() {
                    $("#imageModal1 img").empty();
                    $('#imageModal1').modal("hide");
                });
            });
        });

        /**
         * 添加报警处理意见
         */
        function bindAlarmDealAddForm() {
            //设置验证
            $('#alarmHanlerForm')
                    .bootstrapValidator({
                        message: $.i18n.prop("common.message.form.validator"),
                        fields: {
                            'dealMethod': {
                                validators: {
                                    notEmpty: {}
                                }
                            },
                            /*'isPunish' : {
                            	validators : {
                            		notEmpty : {}
                            	}
                            },
                            's_vpunishType' : {
                            	validators : {
                            		notEmpty : {}
                            	}
                            },
                            
                            's_vpunishValue' : {
                            	validators : {
                            		notEmpty : {}
                            	}
                            },
                             
                            'punishContent' : {
                            	validators : {
                            		notEmpty : {}
                            	}
                            },*/
                            'dealDesc': {
                                validators: {
                                    notEmpty: {}
                                }
                            }
                        }
                    })
                    .on(
                            'success.form.bv',
                            function(e) {//bootstrapvalidator 0.5.2用法
                                e.preventDefault();
                                var $form = $(e.target);
                                var bv = $form.data('bootstrapValidator');
                                //$("#dealResult").val("0");
                                $("#alarmId").val(alarmId);
                                var serialize = $form.serialize();
                                //获取下拉列表显示的文本即罚款类型
                                if (systemModules.isAlarmPushOn) {
                                    var obj = document.getElementById('s_vpunishType');
                                    var type = obj && obj.options[obj.selectedIndex].text || '';
                                    var url = '${root }/alarmdeal/addAlarmDeal.action?type=' + type;
                                } else {
                                    var url = '${root }/alarmdeal/addAlarmDeal.action';
                                }
                                $
                                        .post(
                                                url,
                                                serialize,
                                                function(data) {
                                                    var needLoginFlag = false;
                                                    if (typeof needLogin != 'undefined'
                                                            && $.isFunction(needLogin)) {
                                                        needLoginFlag = needLogin(data);
                                                    }
                                                    if (!needLoginFlag) {
                                                        bootbox
                                                                .alert($.i18n
                                                                        .prop("alarm.label.alarmDeal.addSuccess"));
                                                        $('#alarmHandlerModal').modal('hide');
                                                        var lsMonitorAlarmDealBO = data.alarmDealResult.lsMonitorAlarmDealBO;
                                                        var alarmBO = data.alarmDealResult.alarmBO;
                                                        if (alarmBO == null) {
                                                            bootbox
                                                                    .alert($.i18n
                                                                            .prop("alarm.label.alarmId.cannotFind"));
                                                        } else {
                                                            //刷新报警列表
                                                            vehicleId && pop(vehicleId, false);
                                                            //关闭原先弹出框
                                                            GisCloseAllInfowindow();

                                                            var arr = new Array();
                                                            arr.push(alarmMakerWaitForDeal);
                                                            //alarmMakerWaitForDeal.setMap(null);
                                                            GisClearOverlays(arr);
                                                            var loction = {
                                                                lat: alarmBO.alarmLatitude,
                                                                lng: alarmBO.alarmLongitude
                                                            };
                                                            var alarmHasMarker = GisCreateMarker(
                                                                    loction,
                                                                    "static/images/1_03gray.png",
                                                                    $.i18n.prop('AlarmType.'
                                                                            + alarmBO.alarmTypeId),
                                                                    JSON.stringify(alarmBO));
                                                            GisSetShowFront(alarmHasMarker, -10000);
                                                            GisCloseAllInfowindow();
                                                            alarmMakerHasForDeal = alarmHasMarker;
                                                            var alarmContent = createAlarmDealContent(
                                                                    alarmBO, lsMonitorAlarmDealBO);

                                                            //GisShowInfoWindow(
                                                            // 	alarmHasMarker,
                                                            // 	alarmContent);
                                                            GisAddEventForVehicle(
                                                                    alarmHasMarker,
                                                                    "click",
                                                                    function() {
                                                                        var d = dialog({
                                                                            id: alarmContent,
                                                                            title: vehiclePlateNUmber,//$.i18n.prop('trip.info.message'),
                                                                            content: alarmContent,
                                                                            resize: true
                                                                        });
                                                                        d.show();
                                                                    });
                                                            GisClearOverlays(arr);
                                                            findAlarmsByTripId(alarmBO.tripId);
                                                        }
                                                    }

                                                }, "json");

                            });
        }
    </script>
	<!-- 选择罚款类型时，把罚款金额显示到罚款金额的输入框中 -->
	<script type="text/javascript">
        if (systemModules.isAlarmPushOn) {
            document.getElementById('s_vpunishType').onchange = function() {
                document.getElementById('s_vpunishValue').value = this.value;
            }
        }
    </script>
	<!-- 选择"不罚款"的时候，罚款类型和罚款内容改为不可编辑状态  -->
	<script type="text/javascript">
        function noPunish() {
            if ($("#isPunish").val() == "1") {
                //不处罚的情况下,把某些验证禁用（罚款类型/罚款金额/处罚内容可以为空），并重新校验
                $('#alarmHanlerForm').data('bootstrapValidator').enableFieldValidators(
                        's_vpunishType', false).enableFieldValidators('punishContent', false)
                        .validate();
                $("#punishContent").val('').attr("readonly", 'readonly');
                $("#s_vpunishType").val('').attr("disabled", 'disabled');
                $("#s_vpunishValue").val('');
            } else {
                $("#punishContent").removeAttr("readonly");
                $("#s_vpunishType").removeAttr("disabled");

                $('#alarmHanlerForm').data('bootstrapValidator').enableFieldValidators(
                        's_vpunishType', true).enableFieldValidators('punishContent', true)
                        .validate();
            }
        }
    </script>
</body>
</html>
