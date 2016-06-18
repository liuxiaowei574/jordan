<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>规划区域或线路管理</title>
</head>
<body>

	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel">添加规划区域或路径</h4>
	</div>
	<form class="form-horizontal row" id="routeAreaForm" method="post"
		action="${root}/monitorroutearea/planRouteArea.action">
		<div class="modal-body">
			<div class="col-md-6">
				<div class="form-group ">
					<label class="col-sm-4 control-label" for="routeAreaName">路线区域名称</label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm"
							id="routeAreaName" name="routeAreaName">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label" for="routeAreaType">路线区域类型</label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm"
							id="routeAreaType" name="routeAreaType">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label" for="belongToPort">所属口岸</label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm" id="belongToPort"
							name="belongToPort">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label" for="createUser">创建人</label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm" id="createUser"
							name="createUser">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label" for="updateUser">更新人</label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm" id="updateUser"
							name="updateUser">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label" for="routeAreaStatus">线路区域状态</label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm"
							id="routeAreaStatus" name="routeAreaStatus">
					</div>
				</div>
				<div class="form-group ">
					<div class="col-sm-12">
						<input type="hidden" name="routeAreaPtCol" id="routeAreaPtCol"
							class="form-control input-sm">
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger">添 加</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">取
				消</button>
		</div>
	</form>

</body>
</html>