<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title>车载台状态报告查询</title>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
     <div class="wrapper-content margint95 margin60">
		
		<!-- 报告内容详细页面-->
		<div class="modal add_user_box" id="reportDetailModal" tabindex="-1" role="dialog" aria-labelledby="reportDetailModal">
		  <div class="modal-dialog width1200" role="document">
		    <div class="modal-content">
		    </div>
		  </div>
		</div>
		
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title">车载台状态报告</div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
						<div class="form-group col-md-6">
							<label class="col-sm-4 control-label">车载台编号</label>
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
				车载台状态报告列表
			  </div>
			  	<div class="search_table">
					<div>
						<table id="statusReportTable"></table>
					</div>
				</div>
			</div>
		</div>
      </div>
	</div>
	<script type="text/javascript" src="${root}/regularreport/trackunitstatusreport/js/list.js"></script>
	<script type="text/javascript">
		var root = "${root}";
		function doRest(){
			$("#searchForm")[0].reset();
			window.location.reload();
		}
	</script>
</body>
</html>