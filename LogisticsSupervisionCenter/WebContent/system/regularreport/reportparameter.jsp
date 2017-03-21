<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title>定时报告参数设置</title>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
     <div class="wrapper-content margint95 margin60">
        <!-- 参数内容添加-->
		<div class="modal add_user_box" id="parametersAddModal" tabindex="-1" role="dialog" aria-labelledby="noticeAddModalTitle">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		    </div>
		  </div>
		</div>
		
		<!-- 参数内容编辑 -->
		<div class="modal add_user_box" id="parametersEditModal" tabindex="-1" role="dialog" aria-labelledby="parametersEditModal">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		    </div>
		  </div>
		</div>
		
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title">定时报告参数设置</div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
						<div class="form-group col-md-6">
							<label class="col-sm-4 control-label">报告名称</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="s_noticeTitle"
									name="s_noticeTitle">
							</div>
						</div>
						<div class="clearfix"></div>
						<div class="form-group">
							<div class="col-sm-offset-9 col-md-3">
								<button type="submit" class="btn btn-danger" onclick="search();"><fmt:message key="common.button.query"/></button>
								<button type="button" class="btn btn-darch" onclick="doRest();"><fmt:message key="common.button.reset"/></button>
							</div>
						</div>
					</form>
				</div>
			</div>
			
			<!--/search form-->
			<!--my result-->
			<div class="tab-content">
			  <div class="tab-cotent-title">
			  	<div class="Features pull-right">
					<ul>
						<li><a id="addBtn"class="btn btn-info"><fmt:message key="common.button.add"/></a></li>
						<li><a id="editBtn"class="btn btn-info"><fmt:message key="common.button.modify"/></a></li>
	         			<li><a id="deleteBtn"class="btn btn-info" onclick="delObject();"><fmt:message key="common.button.delete"/></a></li>
						<li><a id="dispatch"class="btn btn-info" onclick="deviceDispatch();">停用</a></li>
					</ul>
				</div>
				定时报告参数设置列表
			  </div>
			  	<div class="search_table">
					<div>
						<table id="parameterTable"></table>
					</div>
				</div>
			</div>
		</div>
      </div>
	</div>
	<script type="text/javascript" src="${root}/system/regularreport/js/list.js"></script>
	<script type="text/javascript">
		var root = "${root}";
		function doRest(){
			$("#searchForm")[0].reset();
		}
	</script>
</body>
</html>