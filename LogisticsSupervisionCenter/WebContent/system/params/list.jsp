<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="system.params.title"/></title>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
    <div class="wrapper-content margint95 margin60">
    	<%--导航 --%>
		<c:set var="pageName"><fmt:message key="link.system.params"/></c:set>
		<jsp:include page="../../include/navigation.jsp" >
			<jsp:param value="${pageName }" name="pageName"/>
		</jsp:include>
			
	    <!-- 参数编辑 -->
		<div class="modal add_user_box" id="paramEditModal" tabindex="-1" role="dialog" aria-labelledby="paramEditModalTitle">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		    </div>
		  </div>
		</div>
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message key="system.params.title"/></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
						<%-- <div class="form-group col-md-6">
							<label class="col-sm-4 control-label"><fmt:message key="system.params.paramName"/>:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="s_paramName"
									name="s_paramName">
							</div>
						</div> --%>
						<div class="form-group col-md-6">
							<label class="col-sm-4 control-label"><fmt:message key="system.params.paramCode"/>:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="s_paramCode"
									name="s_paramCode">
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
			<div class="tab-content m-b">
			  <div class="tab-cotent-title">
				<fmt:message key="system.params.paramList"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="paramsListTable"></table>
					</div>
				</div>
			</div>
		</div>
	   </div>
	</div>
	
	<%-- <script type="text/javascript" src="${root}/system/params/js/list.js"></script> --%>
	
	<script type="text/javascript">
	var $table = $("#paramsListTable");
	//刷新tale
	$(window).resize(function(){
		$table.bootstrapTable("resetView");
	});
	/**
	 * 通知列表
	 */
	function searchParamList() {
		var url = root + "/paramsMgmt/list.action";
		$table.bootstrapTable({
			singleSelect: true,
			showRefresh : false,
			search : false,
			showColumns : false,
			showExport : false,
			striped : true,
			height : "100%",
			url : url,
			method : "get",
			idfield: "paramId",
			sortName:"paramCode",
			cache : false,
			pagination : true,
			sidePagination : 'server',
			pageNumber : 1,
			pageSize : 10,
			sortable:true,
			pageList : [ 10, 20, 30 ],
			columns : [ {
				field : '',
				title : $.i18n.prop('system.params.paramName'),
				formatter: formParaName,
				sortable: false
				
			}, {
				field : 'paramCode',
				title :  $.i18n.prop('system.params.paramCode'),
				sortable:true
			}, {
				field : 'paramValue',
				title : $.i18n.prop('system.params.value'),
				sortable:true
			},{
				field : 'paramType',
				title : $.i18n.prop('system.params.type'),
				formatter : paramsTypeFormatter,
				sortable:true
			},  {
				field : 'paramId',
				title : $.i18n.prop('system.params.operate'),
				formatter : operateFormatter
			}]
		});
	}
	/* 
	*参数类型显示
	*/	
	function paramsTypeFormatter(value, row, index) {
		var show;
		 if (value == '0') {
			show = $.i18n.prop('system.params.type.nomal');
		} else if(value == '1') {
			show = $.i18n.prop('system.params.type.alarm');
		}else {
			show = '--';
		}
		return [show].join('');
	}
	/**
	 * 操作显示
	 */
	function operateFormatter(value, row, index) {
		return [
				'<a class="like" href="javascript:modifyParam(\'' + value
						+ '\')" title="Edit">',
				'<i class="glyphicon glyphicon-pencil"></i>',
				'</a>  ',
		].join('');
	}

	/**
	 * 修改参数
	 * @param id
	 */
	function modifyParam(id) {
		var url = root + "/paramsMgmt/toParamEditModal.action?systemParams.paramId="+id;
		$('#paramEditModal').removeData('bs.modal');
		$('#paramEditModal').modal({
			remote : url,
			show : false,
			backdrop: 'static', 
			keyboard: false
		});
		
		$('#paramEditModal').on('loaded.bs.modal', function(e) {
			$('#paramEditModal').modal('show');
		});
		//模态框登录判断
		$('#paramEditModal').on('show.bs.modal', function(e) {
			var content = $(this).find(".modal-content").html();
			needLogin(content);
		});
	}

	/**
	 * 条件查询方法
	 */
	function search(){
		var params = $table.bootstrapTable('getOptions');
		params.queryParams = function(params) {
	        //遍历form 组装json  
	        $.each($("#searchForm").serializeArray(), function(i, field) {  
	            console.info(field.name + ":" + field.value + " ");  
	            //可以添加提交验证                   
	            params[field.name] = field.value;  
	        });  
	        console.log(params);
	        return params;  
	    }
		$table.bootstrapTable('refresh', {});
	}
	
	//查询条件重置
	function doRest(){
		$("#searchForm")[0].reset();
	}

	/**
	 * 参数列表国际化
	 */
	function formParaName(value, row, index){
		var show ;
		if(!!row.paramCode){
			show = $.i18n.prop('params.' + row.paramCode);
		}else{
			show = '--';
		} 
		return [show].join('');
	}

	$(function() {
		searchParamList();
	});
	
	</script>
</body>
</html>