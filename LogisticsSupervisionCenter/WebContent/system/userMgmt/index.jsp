<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User Manager Example</title>
<jsp:include page="../../include/include.jsp" />
<script type="text/javascript">
	function doSearch(){
		var params = $('#table').bootstrapTable('getOptions') 
		params.queryParams = function(params) {
            //遍历form 组装json  
            $.each($("#sform").serializeArray(), function(i, field) {  
                console.info(field.name + ":" + field.value + " ");  
                //可以添加提交验证                   
                params[field.name] = field.value;  
            });  
            return params;  
        }
		$("#table").bootstrapTable('refresh', params)  
	}
</script>
</head>
<body>
<form id="sform" action="" onsubmit="return false;">
	<table>
		<tr>
			<td>
				用户Id： <input id="s_userId" name="s_userId" value="" />
			</td>
		</tr>		
	</table>
	<input type="button" onclick="doSearch();" value="查询">
</form>
 <div>
    <div>
        <div class="col-sm-12">
            <div id="toolbar">
                <div class="btn btn-primary" data-toggle="modal" data-target="#addModal">添加记录</div>
            </div>
            <table id="table"></table>
            <div class="modal fade" id="addModal" tabindex="-1" aria-hidden="true">
               <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h4 class="modal-title" id="myModalLabel">添加记录</h4>
                        </div>
                        <div class="modal-body">
                            <form action="#">
                                <div class="form-group col-sm-12">
                                	<label class="col-lg-4 control-label">用户账号</label>
                                	<div class="col-lg-8">
                                		<input type="text" class="form-control" id="name" placeholder="请输入名称">
                                	</div>
                                </div>
                                <div class="form-group col-sm-12">
                                	<label class="col-lg-4 control-label">用户年龄</label>
                                	<div class="col-lg-8">
                                		<input type="text" class="form-control" id="age" placeholder="请输入年龄">
                                	</div>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" id="addRecord">提交</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
$(function() {
	//设置传入参数
	function queryParams(params) {
		//遍历form 组装json  
        $.each($("#sform").serializeArray(), function(i, field) {  
            console.info(field.name + ":" + field.value + " ");  
            //可以添加提交验证                   
            params += "&" + field.name +"="+ field.value;  
        });  
        return params;
	}
	$('#table').bootstrapTable({
		method:'post',
		url:root+'/userMgmt/list.action',
		//height: $(window).height() - 200,
		striped: true,
		dataType: "json",
		pagination: true, //分页
		idfield: "userId",
		sortName:"userId",
		"queryParamsType": "limit",
		singleSelect: false,
		contentType: "application/x-www-form-urlencoded",
		pageSize: 10,
		pageNumber:1,
		//totalRows:10,
		search: false, //不显示 搜索框
		showColumns: false, //不显示下拉框（选择显示的列）
		sidePagination: "server", //服务端请求
		queryParams: queryParams,
		//minimunCountColumns: 2,
		//responseHandler: responseHandler,
		pageList : [ 10, 20, 30 ],
		// search: true, //显示搜索框
		//sidePagination: "server", //服务端处理分页
		//pageSize: 3,
		//pageNumber:1,
	    columns: [{
	        field: 'userId',
	        title: 'User ID'
	    },{
	    	field:'userAccount',
	    	title:"User Account"
	    },{
	    	field:'userName',
	    	title:"User Name"
	    },{
	    	field:'userPhone',
	    	title:"User Phone"
	    },{
	    	field:'userEmail',
	    	title:"User Email"
	    },{
	    	field:'userAddress',
	    	title:"User Address"
	    },{
	    	field:'logonSystem',
	    	title:"Logon System"
	    },{
	    	field:'ipAddress',
	    	title:"Login IP"
	    },{
	    	field:'isEnable',
	    	title:"Enable Status"
	    },{
	    	field:'logonTime',
	    	title:"Logon Time"
	    }]
	});
});

</script>
</body>
</html>