<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include/include.jsp" />
<style>
#listTable{
	table-layout: fixed;
}
#listTable td{
	text-align: left;
	overflow: auto;
	word-break: break-all;
}
#listTable td>textarea{
    width: 100%;
    word-break: break-all;
    overflow: auto;
}
</style>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
	<div class="wrapper-content margint95 margin60">
		<%--导航 --%>
		<c:set var="pageName">调试Redis(内部)</c:set>
		<jsp:include page="../include/navigation.jsp" >
			<jsp:param value="${pageName }" name="pageName"/>
		</jsp:include>
		
		<div class="profile profile_box02">
			<h3 class="text-center" id="caution">注意：本页面只供开发测试人员调试用!</h1>
			<h3 class="text-center hidden" id="searching">查询中......</h1>
			
			<div class="tab-content m-b">
				<div class="tab-cotent-title">查询条件</div>
				
				<!--search form-->
				<div class="search_form">
					<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
						<div class="form-group col-md-4">
							<label class="col-sm-4 control-label">IP</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="ip" name="ip">
							</div>
						</div>
						<div class="form-group col-md-4">
							<label class="col-sm-4 control-label">端口</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="port" name="port">
							</div>
						</div>
						<div class="form-group col-md-4">
							<label class="col-sm-4 control-label">口令</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="password" name="password">
							</div>
						</div>
						<div class="clearfix"></div>
						<%--key --%>
						<div class="form-group col-md-4">
							<label class="col-sm-4 control-label">Key名称(模式匹配)</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="key" name="key" value="*">
							</div>
						</div>
						<div class="clearfix"></div>
						<div class="form-group">
							<div class="col-sm-offset-7 col-md-5">
								<button type="submit" class="btn btn-danger" onclick="search();">查询</button>
								<button type="reset" class="btn btn-darch" onclick="doRest();">重置</button>
								<button type="button" class="btn btn-warning" onclick="clearKey();">清空全部key</button>
							</div>
						</div>
					</form>
				</div>
				<!--search form end-->
			</div>
			
			<!--my result-->
			<div class="tab-content m-b">
				<div class="tab-cotent-title">
					结果列表
					<span class="badge"></span>
				</div>
				<div class="search_table">
					<div>
						<table id="listTable" class="table table-hover table-striped" style="width: 100%;">
							<thead>
								<tr>
									<th style="width: 5%;">No.</th>
									<th style="width: 20%;">Key</th>
									<th style="width: 6%;">类型</th>
									<th>值</th>
									<th style="width: 6%;">操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!--my result end-->
			
		</div>
	</div>
</div>
<script type="text/javascript">
	var root = "${root}";
	var redisUrl = "${redisUrl}";
</script>

<script type="text/javascript" src="${root}/test/js/redis.js"></script>
</body>
</html>