<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
 <!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="system.user.list.title"/></title>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
	<div class="wrapper-content margint95 margin60">
		<%--导航 --%>
		<c:set var="pageName"><fmt:message key="link.system.userMgmt"/></c:set>
		<jsp:include page="../../include/navigation.jsp" >
			<jsp:param value="${pageName }" name="pageName"/>
		</jsp:include>
		
		<!-- Modal -->
		<div class="modal add_user_box" id="userAddModal" tabindex="-1" role="dialog" aria-labelledby="userAddModalTitle">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		    </div>
		  </div>
		</div>
		
		<!-- Modify Modal -->
		<div class="modal add_user_box" id="userEditModal" tabindex="-1" role="dialog" aria-labelledby="userEditModal">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		    </div>
		  </div>
		</div>
		<div class="profile profile_box02">
			<div class="tab-content m-b">
				<div class="tab-cotent-title"><fmt:message key="link.system.userMgmt"/></div>
				<div class="search_form">
				 	<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
				 	  <div class="form-group col-md-4">
				 	    <label class="col-sm-3 control-label"><fmt:message key="user.userAccount"/></label>
				 	    <div class="col-sm-8">
				 	      <input type="text" class="form-control" id="s_userAccount" name="s_userAccount">
				 	    </div>
				 	  </div>
				 	  <div class="form-group col-md-4">
				 	    <label class="col-sm-3 control-label"><fmt:message key="user.userName"/></label>
				 	    <div class="col-sm-8">
				 	      <input type="text" class="form-control" id="s_userName" name="s_userName">
				 	    </div>
				 	  </div>
				 	  <%-- 
				 	  <div class="form-group col-md-4">
				 	    <label class="col-sm-3 control-label"><fmt:message key="user.userPhone"/></label>
				 	    <div class="col-sm-8">
				 	      <input type="text" class="form-control" id="s_userPhone" name="s_userPhone">
				 	    </div>
				 	  </div>
				 	  <div class="form-group col-md-4">
				 	    <label class="col-sm-3 control-label"><fmt:message key="user.userEmail"/></label>
				 	    <div class="col-sm-8">
				 	      <input type="text" class="form-control" id="s_userEmail" name="s_userEmail">
				 	    </div>
				 	  </div>
				 	   --%>
				 	  
				 	 <div class="form-group col-sm-4">
						<label class="col-sm-3 control-label"><fmt:message key="user.portID" /></label>
						<div class="col-sm-8">
							<select  id="s_organizationId" name="s_organizationId" class="form-control">
							<option  value=""></option>
								<c:forEach var="SystemDepartmentBO" items="${deptList}">
									<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="clearfix"></div>
					
					<div class="form-group col-sm-4">
						<label class="col-sm-3 control-label"><fmt:message key="user.role" /></label>
						<div class="col-sm-8">
							<select  id="s_roleId" name="s_roleId" class="form-control">
							<option  value=""></option>
								<c:forEach var="role" items="${roleList}">
									<option value="${role.roleId }"><fmt:message key="system.role.${role.roleName}"/></option>
								</c:forEach>
							</select>
						</div>
					</div>
				 	  <div class="clearfix"></div>
				 	  <div class="form-group">
				 	      <div class="col-sm-offset-9 col-md-3">
				 	        <button type="submit" class="btn btn-danger" onclick="doSearch();"><fmt:message key="common.button.query"/></button>
				 	        <button type="button" class="btn btn-darch" onclick="doRest();"><fmt:message key="common.button.reset"/></button>
				 	      </div>
				 	    </div>
				 	</form>
				 </div>
			</div>
		 <!--/search form-->
		 <div class="tab-content">
		 	<div class="tab-cotent-title">
			  	<div class="Features pull-right">
					<ul>
						<li><a id="addBtn" class="btn btn-info btn-weight1"><fmt:message key="common.button.add"/></a></li>
	         			<li><a id="editBtn" class="btn btn-info btn-weight1"><fmt:message key="common.button.modify"/></a></li>
	         			<li><a id="enableBtn" class="btn btn-info btn-weight1"><fmt:message key="common.button.enable"/></a></li>
	         			<li><a id="disableBtn" class="btn btn-info btn-weight1"><fmt:message key="common.button.disable"/></a></li>
	         			<%-- <li><a id="deleteBtn"><fmt:message key="common.button.delete"/></a></li> --%>
	         			<li><a id="initBtn" class="btn btn-info "><fmt:message key="user.button.password.init"/></a></li>
					</ul>
				</div>
				<fmt:message key="system.user.list.title"/>
			  </div>
			  <div class="search_table">
		          	<div>
		         		<table id="userListTable"></table>
				   	</div>
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
function doRest(){
	$("#searchForm")[0].reset();
}
function resetQuery() {
	$table.bootstrapTable('refresh', {});
}


function getIdSelections() {
	return $.map($table.bootstrapTable('getSelections'),
			function(row) {
				return row.userId
			});
}
//刷新tale
$(window).resize(function(){
	$table.bootstrapTable("resetView");
});
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
		clickToSelect : true,
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : false,
		striped : true,
		//height : "100%",
		method : "get",
		idfield: "userId",
		sortName:"userAccount",
		sortOrder: "asc",
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
	    	field: 'userAccount',
	    	title: $.i18n.prop('user.userAccount'),
			sortable:true
	    },{
	    	field: 'userName',
	    	title: $.i18n.prop('user.userName'),
			sortable:true
	    },{
	    	field: 'userPhone',
	    	title: $.i18n.prop('user.userPhone'),
			sortable:true
	    },{
	    	field: 'userEmail',
	    	title: $.i18n.prop('user.userEmail'),
			sortable:true
	    },{
	    	field: 'roleName',
	    	title: $.i18n.prop('user.role'),
	    	formatter : roleFormatter,
			sortable:true
	    },{
	    	field: 'organizationName',
	    	title: $.i18n.prop('user.portID'),
			sortable:true
	    },
	    /*
	    {
	    	field: 'level',
	    	title: $.i18n.prop('user.level'),
			sortable:true
	    },
	    */
	    {
	    	field: 'position',
	    	title: $.i18n.prop('user.position'),
			sortable:true
	    },{
	    	field: 'isEnable',
	    	title: $.i18n.prop('user.isEnable'),
	    	formatter : stateFormatter,
			sortable:true
	    }]
	});
	
	/**
	 * 状态显示
	 * 
	 * @param value
	 * @param row
	 * @param index
	 */
	function stateFormatter(value, row, index) {
		var show;
		if(value == '0') {
			show = $.i18n.prop('user.isEnable.no');
		} else if (value == '1') {
			show = $.i18n.prop('user.isEnable.yes');
		} else {
			show = '--';
		}
		return [show].join('');
	}
	/**
	 *  角色显示
	 * 
	 * @param value
	 * @param row
	 * @param index
	 */
	function roleFormatter(value, row, index) {
		var show;
		if(value) {
			show = $.i18n.prop('system.role.' + value);
		} else {
			show = '--';
		}
		return [show].join('');
	}
	
	
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
	//模态框登录判断
	$('#userAddModal').on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
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
							if(!needLogin(data)) {
								if (data) {
									bootbox.alert($.i18n.prop("user.enable.success"));
									resetQuery();
								} else {
								}
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
							if(!needLogin(data)) {
								if (data) {
									bootbox.alert($.i18n.prop("user.disable.success"));
									resetQuery();
								} else {
								}
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
							if(!needLogin(data)) {
								if (data) {
									bootbox.alert($.i18n.prop("user.resetPassword.success"));
									resetQuery();
								} else {
								}
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
	//模态框登录判断
	$('#userEditModal').on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
	});
});

</script>
</body>
</html>