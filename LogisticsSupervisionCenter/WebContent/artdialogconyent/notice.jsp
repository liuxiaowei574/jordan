<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include/include.jsp" />
<title><fmt:message key="system.notice.title" /></title>
</head>
<body>

    <input type="hidden" class="form-control input-sm"	id="userId" name="userId" value="${userId}">
	<div class="search_table" style="height:300px;overflow:auto">
		<div>
			<table id="noticeTable">
			</table>
		</div>
	</div>
	
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
	  <button type="submit" class="btn btn-danger"  onclick="handleNotice()"  id="modifyButton"><fmt:message key="notice.log.status.read" /></button>
	  <button type="button" class="btn btn-darch1" onclick="closeWindow()" id="btnCancel"><fmt:message key="notice.close.window" /></button>
	</div>
<script type="text/javascript">
	var $table = $('#noticeTable');
	$(function() {
		$table.bootstrapTable({
			url : '${root}/undealNotice/list.action',
			clickToSelect : true,
			showRefresh : false,
			search : false,
			showColumns : false,
			showExport : false,
			striped : true,
			//height : "1000",
			method : "get",
			idfield: "noticeId",
			cache : false,
			sortable:true,
			pagination : true,
			sidePagination : 'server',
			pageNumber : 1,
			pageSize : 10,
			pageList : [ 10, 20, 30 ],
			 columns: [{
		    	checkbox : true
		    },{
		    	field: 'noticeTitle',
		    	title: $.i18n.prop('notice.title'),
		    	sortable:true,
		    },{
		    	field: 'noticeContent',
		    	title: $.i18n.prop('notice.content'),
		    	sortable:true,
		    },{
		    	field: 'deployTime',
		    	title:$.i18n.prop('notice.deployTime'),
		    	sortable:true,
		    },{
		    	field: 'userName',
		    	title: $.i18n.prop('notice.publisher'),
		    	sortable:true,
		    }], 
		});
	});
</script>

<!-- 处理通知 -->
<script type="text/javascript">
var userId = $("#userId").val();
function handleNotice(){
	var list = $('#noticeTable').bootstrapTable('getSelections'); //获取表的行
	var noticeIds = new Array();
	for ( var o in list) {
		noticeIds.push(list[o].noticeId);
	}
	var noticeIds = noticeIds.join(",");
	if(list.length<=0){
		bootbox.alert($.i18n.prop("notice.choose.one.more"));
		return;
	}else{
		var ajaxUrl = "${root}/undealNotice/dealNotice.action";  
		$.ajax({
			url : ajaxUrl,
			type : "post",
			dataType : "json",
			data : {
				noticeIds : noticeIds,
				userId : userId
				},
			success : function(data) {
				//更新index页面（通知artdialog的父页面）的"未读通知"的数量
				window.parent.document.getElementById("needDealNoticeCount").innerText=data;
				$('#noticeTable').bootstrapTable('refresh', {});
			}
		});
	}
}
</script>

<script type="text/javascript">
	function closeWindow(){
		parent.window.closeNotice();
	}
</script>

</body>
</html>