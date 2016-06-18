<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include/include.jsp" />
<title>库存报告</title>
</head>
<body>
	<%@ include file="../include/left.jsp"%>
	<div class="row site">
		<div class="wrapper-content margint95 margin60">
		<div class="profile profile_box02">
		<div class="tab-content m-b">
			  <div class="tab-cotent-title">库存统计条件</div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="searchForm">
						<div class="form-group col-md-6">
							<label class="col-sm-4 control-label">统计方式:</label>
							<div class="col-sm-8">
								<select class="form-control">
									<option>按年统计</option>
									<option>按月统计</option>
									<option>按日统计</option>
								</select>
							</div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label">按年统计</label>
							<div class="input-group date col-sm-9" id="form_checkinStartTime">
								<input type="text" class="form-control" id="s_checkinStartTime" name="s_checkinStartTime" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
						</div>
						<div class="clearfix"></div>
						<div class="form-group">
							<div class="col-sm-offset-9 col-md-3">
								<button type="button" class="btn btn-danger" onclick="search();"><fmt:message key="common.button.query"/></button>
								<button type="submit" class="btn btn-darch"><fmt:message key="common.button.reset"/></button>
							</div>
						</div>
					</form>
				</div>
			</div>
				<div class="col-sm-4">
					<div class="dashboard-stat blue-madison">
						<div class="visual">
							<i class="fa fa-comments">T</i>
						</div>
						<div class="details">
							<div class="number">关锁</div>
							<div class="desc">
								<ul>
									<li>
										<a href="javascript:void(0)" onclick="searchTrackDeviceFlowIn()">
											<i class="icon iconfont"></i> 流入 &nbsp;<span class="redfont">${deviceInventory.trackDeviceFlowIn }</span>
										</a>
									</li>
									<li>
										<a href="javascript:void(0)" onclick="searchTrackDeviceFlowOut()">
											<i class="icon iconfont"></i> 流出 &nbsp;<span class="greenfont">${deviceInventory.trackDeviceFlowOut }</span>
										</a>
									</li>
									<div class="clearfix"></div>
									<li>
										<a href="javascript:void(0)" onclick="searchTrackDeviceTurnIn()"> 
											<i class="icon iconfont"></i> 转入 &nbsp;<span class="redfont">${deviceInventory.trackDeviceTurnIn }</span>
										</a>
									</li>
									<li>
										<a href="javascript:void(0)" onclick="searchTrackDeviceTurnOut()">
											<i class="icon iconfont"></i> 转出 &nbsp;<span class="greenfont">${deviceInventory.trackDeviceTurnOut }</span>
										</a>
									</li>
								</ul>
								<div class="clearfix"></div>
							</div>
						</div>
						<a class="more" href="javascript:;"> </a>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="dashboard-stat green-haze">
						<div class="visual">
							<i class="fa fa-comments">S</i>
						</div>
						<div class="details">
							<div class="number">子锁</div>
							<div class="desc">
								<ul>
									<li>
										<a href="javascript:void(0)" onclick="searchEsealFlowIn()">
											<i class="icon iconfont"></i> 流入 &nbsp;<span class="redfont">${deviceInventory.esealFlowIn }</span>
										</a>
									</li>
									<li>
										<a href="javascript:void(0)" onclick="searchEsealFlowOut()">
											<i class="icon iconfont"></i> 流出 &nbsp;<span class="greenfont">${deviceInventory.esealFlowOut }</span>
										</a>
									</li>
									<div class="clearfix"></div>
									<li>
										<a href="javascript:void(0)" onclick="searchEsealTurnIn()"> 
											<i class="icon iconfont"></i> 转入 &nbsp;<span class="redfont">${deviceInventory.esealTurnIn }</span>
										</a>
									</li>
									<li>
										<a href="javascript:void(0)" onclick="searchEsealTurnOut()">
											<i class="icon iconfont"></i> 转出 &nbsp;<span class="greenfont">${deviceInventory.esealTurnOut }</span>
										</a>
									</li>
								</ul>
								<div class="clearfix"></div>
							</div>
						</div>
						<a class="more" href="javascript:;"> </a>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="dashboard-stat purple-plum">
						<div class="visual">
							<i class="fa fa-comments">E</i>
						</div>
						<div class="details">
							<div class="number">传感器</div>
							<div class="desc">
								<ul>
									<li>
										<a href="javascript:void(0)" onclick="searchSensorFlowIn()">
											<i class="icon iconfont"></i> 流入 &nbsp;<span class="redfont">${deviceInventory.sensorFlowIn }</span>
										</a>
									</li>
									<li>
										<a href="javascript:void(0)" onclick="searchSensorFlowOut()">
											<i class="icon iconfont"></i> 流出 &nbsp;<span class="greenfont">${deviceInventory.sensorFlowOut }</span>
										</a>
									</li>
									<div class="clearfix"></div>
									<li>
										<a href="javascript:void(0)" onclick="searchSensorTurnIn()"> 
											<i class="icon iconfont"></i> 转入 &nbsp;<span class="redfont">${deviceInventory.sensorTurnIn }</span>
										</a>
									</li>
									<li>
										<a href="javascript:void(0)" onclick="searchSensorTurnOut()">
											<i class="icon iconfont"></i> 转出 &nbsp;<span class="greenfont">${deviceInventory.sensorTurnOut }</span>
										</a>
									</li>
								</ul>
								<div class="clearfix"></div>
							</div>
						</div>
						<a class="more" href="javascript:;"> </a>
					</div>
				</div>
				<div class="clearfix"></div>			
				<!-- 详细列表信息 -->
				<div class="tab-content">
				  <div class="tab-cotent-title" id="deviceListTable">
					库存详细列表信息
				  </div>
				  	<div class="search_table">
						<div>
							<table id="inventoryListTable"></table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${root}/report/js/inventory.js"></script>
	<script type="text/javascript">
		var root = "${root}";
	</script>
</body>
</html>