<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="system.notice.title"/></title>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
     <div class="wrapper-content margint95 margin60">
     	<%--导航 --%>
		<c:set var="pageName"><fmt:message key="system.notice.title"/></c:set>
		<jsp:include page="../../include/navigation.jsp" >
			<jsp:param value="${pageName }" name="pageName"/>
		</jsp:include>
		
        <!-- 通知添加-->
		<div class="modal add_user_box" id="noticeAddModal" tabindex="-1" role="dialog" aria-labelledby="noticeAddModalTitle">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		    </div>
		  </div>
		</div>
		
		<!-- 通知编辑 -->
		<div class="modal add_user_box" id="noticeEditModal" tabindex="-1" role="dialog" aria-labelledby="noticeEditModalTitle">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		    </div>
		  </div>
		</div>
		
		<!-- 查看接收人列表-->
		<div class="modal add_user_box" id="receiveUserListModal" tabindex="-1" role="dialog" aria-labelledby="receiveUserListModalTitle">
		  <div class="modal-dialog width1200" role="document">
		    <div class="modal-content">
		    </div>
		  </div>
		</div>
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message key="system.notice.title"/></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
						<div class="form-group col-md-6">
							<label class="col-sm-4 control-label"><fmt:message key="notice.title"/>:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="s_noticeTitle"
									name="s_noticeTitle">
							</div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-4 control-label"><fmt:message key="notice.content"/>:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="s_noticeContent"
									name="s_noticeContent">
							</div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-4 control-label"><fmt:message key="notice.status"/>:</label>
							<div class="col-sm-8">
								<select class="form-control" name="s_noticeState" id="s_noticeState">
									<option value=""></option>
									<option value="0"><fmt:message key="notice.draft"/></option>
									<option value="1"><fmt:message key="notice.publish"/></option>
									<option value="2"><fmt:message key="notice.finish"/></option>
						    	</select>
							</div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-4 control-label"><fmt:message key="notice.type"/>:</label>
							<div class="col-sm-8">
								<s:select name="s_noticeType"
									theme="simple"
									emptyOption="true"
									cssClass="form-control"
									list="@com.nuctech.ls.model.util.NoticeType@values()"
									listKey="type"
									listValue="key"
									value="%{#request.pageQuery.filters.noticeType}"
									>
								</s:select>
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
						<li><a id="addBtn" class="btn btn-info btn-weight"><fmt:message key="notice.draft"/></a></li>
						<li><a id="editBtn" class="btn btn-info btn-weight"><fmt:message key="common.button.edit"/></a></li>
						<li><a id="deleteBtn" class="btn btn-info btn-weight"><fmt:message key="common.button.delete"/></a></li>
						<li><a id="publishBtn" class="btn btn-info btn-weight"><fmt:message key="notice.publish"/></a></li>
					</ul>
				</div>
				<fmt:message key="system.notice.list.title"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="noticeListTable"></table>
					</div>
				</div>
			</div>
		</div>
      </div>
	</div>
	<script type="text/javascript" src="${root}/system/notice/js/list.js"></script>
	<script type="text/javascript">
		var root = "${root}";
		function doRest(){
			$("#searchForm")[0].reset();
		}
	</script>
</body>
</html>