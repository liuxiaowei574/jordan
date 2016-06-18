<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title></title>

</head>
<body>
	<form id="routeAreaForm">

		<ul class="Custom_list" id="addRapoint">
			<li>
				<label class="col-sm-6 control-label" for="routeAreaName">路线区域名称</label>
				<input type="text" class="form-control input-sm" id="routeAreaName" name="routeAreaName"> 
				<div class="hidden" id="routeAreaTypeDiv">
					<label class="col-sm-6 control-label" for="routeAreaType">路线区域类型</label>
					<select class="form-control input-sm" id="routeAreaType" name="routeAreaType">
						<option value="">-请选择-</option>
						<option value="1">安全区域</option>
						<option value="2">危险区域</option>
						<option value="4">区域划分</option>
					</select>
				</div>
				<!-- <input type="hidden" class="form-control input-sm" id="routeAreaType" name="routeAreaType">  -->
				<input id="routeAreaId" name="routeAreaId" type="hidden" />
				
				<label class="col-sm-6 control-label" for="belongToPort">所属口岸</label> 
				<input type="text" class="form-control input-sm" id="belongToPort" name="belongToPort"> 
				
				<!-- <label class="col-sm-6 control-label" for="createUser">创建人</label> 
				<input type="text" class="form-control input-sm" id="createUser" name="createUser">

				<label class="col-sm-6 control-label" for="updateUser">更新人</label> 
				<input type="text" class="form-control input-sm" id="updateUser" name="updateUser">  -->
				
				<label class="col-sm-6 control-label" for="routeAreaStatus">路线区域状态</label> 
				<select class="form-control input-sm" id="routeAreaStatus" name="routeAreaStatus">
					<option value="">-请选择-</option>
					<option value="0">有效</option>
					<option value="1">无效</option>
				</select>

				<label class="col-sm-6 control-label" for="updateUser">缓冲区</label> 
				<input type="text" class="form-control input-sm" id="routeAreaBuffer" name="routeAreaBuffer">
				
				<label class="col-sm-6 control-label" for="routeCost">路线用时</label> 
				<input type="text" class="form-control input-sm" id="routeCost" name="routeCost"> 
				
				<input type="hidden" name="routeAreaPtCol" id="routeAreaPtCol" class="form-control input-sm">

				<button id="addRouteAreaBtn" type="button" class="btn btn-danger">
					<fmt:message key="common.button.add" />
				</button>
				<button id="editRouteAreaBtn" type="button" class="btn btn-danger">
					<fmt:message key="common.button.modify" />
				</button>
				<button type="reset" class="btn btn-darch">
					<fmt:message key="common.button.reset" />
				</button></li>

		</ul>
	</form>

	<script>
     $('#addRouteAreaBtn').on('click', function () {
    	 addPlanRoute();
     });
     function getDrawRouteArea(jsondata){
    	 alert(jsondata)
     }
     function addPlanRoute() {
    	 $("#routeAreaPtCol").val(routeAreaCol);
    	 if(""==$("#routeAreaPtCol").val()||null==$("#routeAreaPtCol").val()||typeof($("#routeAreaPtCol").val())=="undefined"){
    		 bootbox.alert($.i18n.prop("gis.label.routeArea.isNull"));
    		 return;
    	 }
    	var param = $("#routeAreaForm").serialize();
  		var portUrl = getRootPath() + "monitorroutearea/planRouteArea.action?ids="+menuType;
  		$.ajax({
			type : "POST",
			url : portUrl,
			data: param,
			error : function(e, message, response) {
				console.log("Status: " + e.status + " message: " + message);
			},
			success : function() {
				routeAreaCol="";
				bootbox.alert($.i18n.prop("map.routeArea.save.success"));
				findAllRouteAreaList();
				
			}
		});

  	}
     
     $('#editRouteAreaBtn').on('click', function () {
    	 updatePlanRoute();
     });
     
     function updatePlanRoute() {
    	var param = $("#routeAreaForm").serialize();
  		var portUrl = getRootPath() + "monitorroutearea/updateRouteArea.action?ids="+menuType;
  		$.ajax({
			type : "POST",
			url : portUrl,
			data: param,
			error : function(e, message, response) {
				console.log("Status: " + e.status + " message: " + message);
			},
			success : function() {
				bootbox.alert($.i18n.prop("map.routeArea.update.success"));
				findAllRouteAreaList();
			}
		}); 

  	}
   </script>

</body>