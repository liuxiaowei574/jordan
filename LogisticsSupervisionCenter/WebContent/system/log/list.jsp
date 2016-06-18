<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
 <!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title>用户操作日志</title>
</head>
<body>
<div class="profile profile_box02">
 <div class="search_form">
 	<form class="form-horizontal row" id="searchForm">
 	  <div class="form-group col-md-4">
 	    <label class="col-sm-4 control-label"><fmt:message key="user.userAccount"/></label>
 	    <div class="col-sm-8">
 	      <input type="text" class="form-control" id="s_userAccount" name="s_userAccount">
 	    </div>
 	  </div>
 	  <div class="form-group col-md-4">
 	    <label class="col-sm-4 control-label"><fmt:message key="user.userName"/></label>
 	    <div class="col-sm-8">
 	      <input type="text" class="form-control" id="s_userName" name="s_userName">
 	    </div>
 	  </div>
 	  <div class="form-group col-md-4">
 	    <label class="col-sm-4 control-label"><fmt:message key="user.userPhone"/></label>
 	    <div class="col-sm-8">
 	      <input type="text" class="form-control" id="s_userPhone" name="s_userPhone">
 	    </div>
 	  </div>
 	  <div class="form-group col-md-4">
 	    <label class="col-sm-4 control-label"><fmt:message key="user.userEmail"/></label>
 	    <div class="col-sm-8">
 	      <input type="text" class="form-control" id="s_userEmail" name="s_userEmail">
 	    </div>
 	  </div>
 	  <div class="form-group">
 	      <div class="col-sm-offset-9 col-md-3">
 	        <button type="button" class="btn btn-danger" onclick="doSearch();"><fmt:message key="common.button.query"/></button>
 	        <button type="submit" class="btn btn-darch"><fmt:message key="common.button.reset"/></button>
 	      </div>
 	    </div>
 	</form>
 </div>
 <!--/search form-->
 <!--my result-->
 <div class="row">
     <div class="col-md-12 my_news">
         <div class="title_news">
         	<div class="Features pull-right">
         		<ul>
         			<li><a id="addBtn"><fmt:message key="common.button.add"/></a></li>
         			<li><a id="editBtn"><fmt:message key="common.button.modify"/></a></li>
         			<li><a id="enableBtn"><fmt:message key="common.button.enable"/></a></li>
         			<li><a id="disableBtn"><fmt:message key="common.button.disable"/></a></li>
         			<%-- <li><a id="deleteBtn"><fmt:message key="common.button.delete"/></a></li> --%>
         			<li><a id="initBtn"><fmt:message key="user.button.password.init"/></a></li>
         		</ul>
         	</div>
             <h2><fmt:message key="system.user.list.title"/></h2>
         </div>
         <div class="search_table">
          	<div>
         		<table id="userListTable"></table>
		   	</div>
       </div>
   </div>
</div>
</div>
<script type="text/javascript">
var ids=[];
var $table = $('#userListTable');
function doSearch(){
	var params = $table.bootstrapTable('getOptions');
	params.queryParams = function(params) {
        //遍历form 组装json  
        $.each($("#searchForm").serializeArray(), function(i, field) {  
            console.info(field.name + ":" + field.value + " ");  
            //可以添加提交验证                   
            params[field.name] = field.value;  
        });  
        return params;  
    }
	$table.bootstrapTable('refresh', params);
}

function resetQuery() {
	$table.bootstrapTable('refresh', {});
}
function getIdSelections() {
	return $table.bootstrapTable('getSelections');
}

function getIdSelections() {
	return $.map($table.bootstrapTable('getSelections'),
			function(row) {
				return row.userId
			});
}

$(function() {
	//设置传入参数
	function queryParams(params) {
		//遍历form 组装json  
       $.each($("#searchForm").serializeArray(), function(i, field) {  
            console.info(field.name + ":" + field.value + " ");  
            //可以添加提交验证                   
            params += "&" + field.name +"="+ field.value;  
        }); 
        return params;
	}
	$table.bootstrapTable({
		url:'${root}/userMgmt/list.action',
		//height: $(window).height() - 200,
		clickToSelect : true,
		showRefresh : false,
		search : true,
		showColumns : true,
		showExport : false,
		striped : true,
		height : "100%",
		method : "get",
		idfield: "userId",
		sortName:"userId",
		cache : false,
		queryParams : queryParams,
		queryParamsType : "not-limit",
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
	    columns: [{
	    	checkbox : true
	    },{
	    	field: 'userAccount',
	    	title: $.i18n.prop('user.userAccount')
	    },{
	    	field: 'userName',
	    	title: $.i18n.prop('user.userName')
	    },{
	    	field: 'userPhone',
	    	title: $.i18n.prop('user.userPhone')
	    },{
	    	field: 'userEmail',
	    	title: $.i18n.prop('user.userEmail')
	    },{
	    	field: 'userAddress',
	    	title: $.i18n.prop('user.userAddress')
	    },{
	    	field: 'logonSystem',
	    	title: $.i18n.prop('user.logonSystem')
	    },{
	    	field: 'ipAddress',
	    	title: $.i18n.prop('user.ipAddress')
	    },{
	    	field: 'logonTime',
	    	title: $.i18n.prop('user.logonTime')
	    }]
	});
	
	$table.on(
			'check.bs.table uncheck.bs.table '
					+ 'check-all.bs.table uncheck-all.bs.table', function() {
				ids = getIdSelections();
			});
	
	//添加Modal调用方法
	$("#addBtn").click(function() {
		var url ="${root}/userMgmt/addModal.action";
		$('#userAddModal').removeData('bs.modal');
		$('#userAddModal').modal({
			remote : url,
			show : false,
			backdrop: 'static', 
			keyboard: false
		});
	});
	
	$('#userAddModal').on('loaded.bs.modal', function(e) {
		$('#userAddModal').modal('show');
	});
	
	//编辑Modal调用方法
	$("#editBtn").click(function() {
		if(ids.length == 0) {
			bootbox.alert($.i18n.prop("user.modify.choose"));
		} else if(ids.length > 1) {
			bootbox.alert($.i18n.prop("user.modify.choose.only"));
		} else {
			var url ="${root}/userMgmt/editModal.action?systemUser.userId="+ids;
			$('#userEditModal').removeData('bs.modal');
			$('#userEditModal').modal({
				remote : url,
				show : false,
				backdrop: 'static', 
				keyboard: false
			}); 
		}
		
	});
	
	//启用
	$("#enableBtn").click(function() {
		if(ids.length == 0) {
			bootbox.alert($.i18n.prop("user.enable.choose"));
		} else {
			bootbox.confirm($.i18n.prop("user.enable.confirm"), function(result) {
				if(result) {
					var ajaxUrl = '${root}/userMgmt/enableUserByIds.action?ids='+ ids;
					$.ajax({
						url : ajaxUrl, // 请求url
						type : "post", // 提交方式
						dataType : "json", // 数据类型
						data : {"ids" : ids},
						success : function(data) { // 提交成功的回调函数
							if (data) {
								bootbox.alert($.i18n.prop("user.enable.success"));
								resetQuery();
							} else {
							}
						}
					});
				}
			});
		}
	});
	
	//禁用
	$("#disableBtn").click(function() {
		if(ids.length == 0) {
			bootbox.alert($.i18n.prop("user.disable.choose"));
		} else {
			bootbox.confirm($.i18n.prop("user.disable.confirm"), function(result) {
				if(result) {
					var ajaxUrl = '${root}/userMgmt/disableUserByIds.action?ids='+ ids;
					$.ajax({
						url : ajaxUrl, // 请求url
						type : "post", // 提交方式
						dataType : "json", // 数据类型
						data : {"ids" : ids},
						success : function(data) { // 提交成功的回调函数
							if (data) {
								bootbox.alert($.i18n.prop("user.disable.success"));
								resetQuery();
							} else {
							}
						}
					});
				}
			});
		}
	});
	
	//初始化密码
	$("#initBtn").click(function() {
		if(ids.length == 0) {
			bootbox.alert($.i18n.prop("user.resetPassword.chose"));
		} else {
			bootbox.confirm($.i18n.prop("user.resetPassword.confirm"), function(result) {
				if(result) {
					var ajaxUrl = '${root}/userMgmt/resetUserPasswordByIds.action?ids='+ ids;
					$.ajax({
						url : ajaxUrl, // 请求url
						type : "post", // 提交方式
						dataType : "json", // 数据类型
						data : {"ids" : ids},
						success : function(data) { // 提交成功的回调函数
							if (data) {
								bootbox.alert($.i18n.prop("user.resetPassword.success"));
								resetQuery();
							} else {
							}
						}
					});
				}
			});
		}
	});
	
	$('#userEditModal').on('loaded.bs.modal', function(e) {
		$('#userEditModal').modal('show');
	});
});

</script>
</body>
</html>