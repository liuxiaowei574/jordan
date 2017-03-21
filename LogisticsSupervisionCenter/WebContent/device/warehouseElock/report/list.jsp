<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../../include/include.jsp" />
<title><fmt:message key="link.chart.elockDispatch.report"/></title>

</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../../include/tripMsgModal.jsp" %>
<%@ include file="../../../include/left.jsp" %>
<div class="row site">
     <div class="wrapper-content margint95 margin60">
      <!-- Modal 关锁添加模态框-->
	<div class="modal  add_user_box" id="addElockModal" tabindex="-1"
		role="dialog" aria-labelledby="addElockModalTitle">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- /Modal -->


	<!-- Modify Modal关锁修改模态框 -->
	<div class="modal  add_user_box" id="updateElockModal" tabindex="-1"
		role="dialog" aria-labelledby="updateElockModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- Modify Modal -->
	
	
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message
							key="link.chart.elockDispatch.report" /></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="ElockForm" action=""
				onsubmit="return false;">
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message
							key="WarehouseElock.elockNumber" /></label>
					<div class="col-sm-8">
						<input type="text" id="s_elockNumber" name="s_elockNumber"
							class="form-control">
					</div>
				</div>
				
				<div class="form-group col-sm-6">
					<label for="roleIds" class="col-sm-4 control-label"><fmt:message key="warehouseelock.jsp.report.applcationPortName"/></label>
					<div class="col-sm-8">
						<select style="/* font-size:10px */" id="s_toPort" name="s_toPort" class="form-control">
						<option  value=""></option>
							<c:forEach var="SystemDepartmentBO" items="${deptList}">
								<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message
							key="WarehouseElock.simCard" /></label>
					<div class="col-sm-8">
						<input type="text" id="s_simCard" name="s_simCard"
							class="form-control">
							<!-- 仅仅查询在途的数据 -->
						<input type="hidden" id="s_elockStatus" name="s_elockStatus" value="2"
							class="form-control">
					</div>
				</div>
				<div class="form-group col-sm-6">
						<label for="roleIds" class="col-sm-4 control-label"><fmt:message key="warehouseelock.jsp.report.frmport"/></label>
						<div class="col-sm-8">
							<select style="/* font-size:10px */" id="s_fromPort" name="s_fromPort" class="form-control">
							<option  value=""></option>
								<c:forEach var="SystemDepartmentBO" items="${deptList}">
									<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				
				<div class="form-group">
					<div class="col-sm-offset-9 col-md-3">
						<button type="submit" class="btn btn-danger" onclick="doSearch();"><fmt:message key="common.button.query"/></button>
						<button  type="button"  class="btn btn-darch" onclick="doRest();">
							<fmt:message key="common.button.reset" />
						</button>
					</div>
				</div>
			</form>
				</div>
			</div>
			
			<!--/search form-->
			<!--my result-->
			<div class="tab-content">
			  <div class="tab-cotent-title">
				<fmt:message key="link.chart.elockDispatch.report.result"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="table" >	</table>
					</div>
				</div>
			</div>
		</div>
      </div>
	</div>
</body>
</html>
<script type="text/javascript" src="${root}/device/warehouseElock/report/js/list.js"></script>