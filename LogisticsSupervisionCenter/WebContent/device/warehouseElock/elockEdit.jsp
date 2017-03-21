<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="WarehouseElock.list.title"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="myModalLabel"><fmt:message key="elock.modifyElock"/></h4>
</div>
<form class="form-horizontal row" id="elockEditForm" method="post">
	<input type="hidden" class="form-control input-sm" id="warehouseElockBO.elockId" 
	   	      name="warehouseElockBO.elockId" value="${warehouseElockBO.elockId }">
	   	      
	<input type="hidden" class="form-control input-sm" id="warehouseElockBO.createTime" 
	   	      name="warehouseElockBO.createTime" value="${warehouseElockBO.createTime }">
	   	      
 	<input type="hidden" class="form-control input-sm" id="warehouseElockBO.createUser" 
   	      name="warehouseElockBO.createUser" value="${warehouseElockBO.createUser }">  	      
	   	      
	<input type="hidden" class="form-control input-sm" id="warehouseElockBO.lastUseTime" 
   	      name="warehouseElockBO.lastUseTime" value="${warehouseElockBO.lastUseTime }"> 
   	      
   	 <input type="hidden" class="form-control input-sm" id="warehouseElockBO.timeNotInUse" 
   	      name="warehouseElockBO.timeNotInUse" value="${warehouseElockBO.timeNotInUse }"> 
     
	 <input type="hidden" class="form-control input-sm" id="warehouseElockBO.lastUser" 
   	      name="warehouseElockBO.lastUser" value="${warehouseElockBO.lastUser }"> 
   	      
 	 <input type="hidden" class="form-control input-sm" id="warehouseElockBO.belongTo" 
 	      name="warehouseElockBO.belongTo" value="${warehouseElockBO.belongTo }"> 
	<div class="modal-body">
	  	<div class="col-md-6">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseElockBO.elockNumber"><em>*</em><fmt:message key="WarehouseElock.elockNumber"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseElockBO.elockNumber" 
	   	      name="warehouseElockBO.elockNumber" readonly="true" value="${warehouseElockBO.elockNumber}">
	   	    </div>
	   	  </div>
	   	  <div class="form-group">
				<label class="col-sm-4 control-label"><em>*</em><fmt:message
						key="WarehouseElock.belongTo" /></label>
				<div class="col-sm-8">
					<%-- <select id="warehouseElockBO.belongTo"
						name="warehouseElockBO.belongTo" class="form-control">
						
					<option  value="${systemDepartmentBO.organizationId}">${systemDepartmentBO.organizationName}</option>
						
							<c:forEach var="SystemDepartmentBO" items="${deptEditList}">
								<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
							</c:forEach>
					</select> --%>
					<input type="text" class="form-control input-sm" id="warehouseElockBO_belongTo" 
	   	            name="warehouseElockBO_belongTo" readonly="true" value="${systemDepartmentBO.organizationName}">
				</div>
		  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseElockBO.simCard"><fmt:message key="WarehouseElock.simCard"/></label>
	   	    <div class="col-sm-8">
	   	    	<c:choose>
	   	    		<c:when test="${warehouseElockBO.elockStatus == '2' }">
	   	    			<input type="text" class="form-control input-sm" id="warehouseElockBO.simCard" 
		   	            name="warehouseElockBO.simCard" readonly="true" value="${warehouseElockBO.simCard}">
	   	    		</c:when>	
	   	    		<c:when test="${warehouseElockBO.elockStatus != '2' }">
		   	    		<input type="text" class="form-control input-sm" id="warehouseElockBO.simCard" 
		   	            name="warehouseElockBO.simCard" value="${warehouseElockBO.simCard}">
	   	            </c:when>
	   	    	</c:choose>
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseElockBO.interval"><fmt:message key="WarehouseElock.interval"/></label>
	   	    <div class="col-sm-8">
	   	    	<c:choose>
	   	    		<c:when test="${warehouseElockBO.elockStatus == '2' }">
		   	    		<input type="text" class="form-control input-sm" id="warehouseElockBO.interval" 
		   	            name="warehouseElockBO.interval" readonly="true" value="${warehouseElockBO.interval}">
	   	    		</c:when>	
	   	    		<c:when test="${warehouseElockBO.elockStatus != '2' }">
		   	    		<input type="text" class="form-control input-sm" id="warehouseElockBO.interval" 
		   	            name="warehouseElockBO.interval" value="${warehouseElockBO.interval}">
	   	            </c:when>
	   	    	</c:choose>
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseElockBO.gatewayAddress"><fmt:message key="WarehouseElock.gatewayAddress"/></label>
	   	    <div class="col-sm-8">
	   	      <c:choose>
	   	    		<c:when test="${warehouseElockBO.elockStatus == '2' }">
	   	    		    <input type="text" class="form-control input-sm" id="warehouseElockBO.gatewayAddress" 
	   	                name="warehouseElockBO.gatewayAddress" readonly="true" value="${warehouseElockBO.gatewayAddress}">
	   	    		</c:when>	
	   	    		<c:when test="${warehouseElockBO.elockStatus != '2' }">
	   	    			<input type="text" class="form-control input-sm" id="warehouseElockBO.gatewayAddress" 
	   	                name="warehouseElockBO.gatewayAddress" value="${warehouseElockBO.gatewayAddress}">
	   	            </c:when>
	   	      </c:choose>
	   	    </div>
	   	  </div>
	   	  
	   	  <%-- <div class="form-group">
				<label class="col-sm-4 control-label"><fmt:message key="WarehouseElock.elockStatus"/></label>
				<div class="col-sm-8">
					<select id="warehouseEsealBO.elockStatus"name="warehouseElockBO.elockStatus" class="form-control">
						<option value=${warehouseElockBO.elockStatus}>${warehouseElockBO.elockStatus}</option>
						<option value="0">报废</option>
						<option value="1">正常</option>	
						<option value="2">在途</option>
						<option value="3">损坏</option>
						<option value="4">维修</option>
					</select>
				</div>
			</div>  --%>
			
			<div class="form-group ">
				<label class="col-sm-4 control-label"><em>*</em><fmt:message key="WarehouseElock.elockStatus"/></label>
				    <c:choose>
		   	    		<c:when test="${warehouseElockBO.elockStatus == '2' }">
			   	    		  <div class="col-sm-8">
									<s:select name="warehouseElockBO.elockStatus" 
									emptyOption="true"
									cssClass="form-control" theme="simple"
									list="@com.nuctech.ls.model.util.DeviceStatus@values()"
									listKey="text"
									listValue="key" 
									disabled="true"
									id="onWay"
									>
									</s:select>
							</div>
		   	    		</c:when>	
		   	    		<c:when test="${warehouseElockBO.elockStatus != '2' }">
			   	    		  <div class="col-sm-8">
								<s:select name="warehouseElockBO.elockStatus" 
								emptyOption="false"
								cssClass="form-control" theme="simple"
								list="@com.nuctech.ls.model.util.DeviceStatusExceptOnway@values()"
								listKey="text"
								listValue="key" 
								>
							    </s:select>
						      </div>
		   	    		</c:when>
	   	    	    </c:choose>
			</div> 
	  	</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
	  <button type="submit" class="btn btn-danger" id="modifyButton" ><fmt:message key="common.button.modify"/></button>
	  <button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="common.button.cancle"/></button>
	</div>
</form>
<script type="text/javascript">
$(function() {
	buildElockEditForm();
});

/**
 * 
 */
function buildElockEditForm() {
	//设置验证
	  $('#elockEditForm').bootstrapValidator({
		  message: $.i18n.prop("common.message.form.validator"),
	      fields: {
	    	'warehouseElockBO.elockNumber': {
	              validators: {
	            	  notEmpty: {},
	            	  stringLength: {
							max: 50
					  }
	              }
	          },  
	      	'warehouseElockBO_belongTo': {
	              validators: {
	            	  notEmpty: {}
	              }
	          },  
	          'warehouseElockBO.simCard': {
	              validators: {
	            	  stringLength: {
							max: 100
						}
	              }
	          },
	          'warehouseElockBO.interval': {
	              validators: {
	            	  stringLength: {
							max: 20
						}
	                }
	            },
	          'warehouseElockBO.gatewayAddress': {
	              validators: {
	            	  stringLength: {
							max: 20
						}
	                }
	            },
	            'warehouseElockBO.elockStatus': {
		              validators: {
		            	   notEmpty: {}
		                }
		         }
	      }/* ,
	      
	      submitHandler: function(validator, form, submitButton){
	    	var serialize = $("#elockEditForm").serialize();
	  		var url = '${root }/warehouseElock/editElock.action'
	  		$.post(url, serialize, function(data) {
	  			bootbox.success($.i18n.prop("elock.modifyElock.success"));
	  			$('#table').bootstrapTable('refresh', {});
	  			$('#updateElockModal').modal('hide');
	  		}, "json");
	      }  */
	  }).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
	      e.preventDefault();//避免表单重复提交
	      $("#onWay").removeAttr("disabled");//提交前让下拉列表可用
	      //$("#warehouseElockBO.belongTo").removeAttr("disabled");//提交前让所属下拉列表可用 
	      var $form = $('#elockEditForm');
	      
	      var bv = $form.data('bootstrapValidator');
	      var serialize = $form.serialize();
	      var url = '${root }/warehouseElock/editElock.action'
		  $.post(url, serialize, function(data) {
			  if(!needLogin(data)) {
				if(data) {
					bootbox.success($.i18n.prop("elock.modifyElock.success"));
		  			$('#updateElockModal').modal('hide');
		  			$table.bootstrapTable('refresh', {});
		  			$table.bootstrapTable('resetView');
				} else {
					bootbox.error($.i18n.prop("elock.modify.fail"));
					$('#updateElockModal').modal('hide');
					$table.bootstrapTable('refresh', {});
				}
			  }
		 }, "json");
	  });
	}
</script>

</body>
</html>