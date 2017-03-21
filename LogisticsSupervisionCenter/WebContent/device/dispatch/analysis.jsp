<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/include.jsp" />
	<title><fmt:message key="warehouse.dispatch.title"/></title>
	<script type="text/javascript">
	jQuery.i18n.properties({//加载资浏览器语言对应的资源文件
        name : 'LocalizationResource_center', //资源文件名称
        path : _getRootPath() + "/i18n/", //资源文件路径
        mode : 'map', //用Map的方式使用资源文件中的值
        language :language,
        callback : function() {
        	//alert($.i18n.prop('warehouse.dispatch.success'));
        }
    });
	</script>
	<script src="${root}/static/js/echarts/echarts.min.js"></script>
	<script type="text/javascript" src="${root}/device/dispatch/js/analysis.js"></script>
	<script type="text/javascript" src="${root}/gis/map.js.jsp"></script>
	<script type="text/javascript">
		var root = "${root}";
		var planPortNameArr = ${planPortNameArr };
		var planDistanceArr = ${planDistanceArr};
	</script>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %> 
	<div class="row site">
            <div class="wrapper-content margint95 margin60">
            	<div class="col-sm-3 kalb">
            		<div class="panel panel-default">
        			  <div class="panel-heading">
        			  	<div class="pull-right col-sm-8 text-right">
        			  		<form id="portForm" action="${root }/warehouseDispatchAnalysis/listPort.action">
	        			  		<div class="input-group">
		        			  			 <input type="text" class="form-control" id="portName" name="portName" placeholder="Search for...">
		        			  		     <span class="input-group-btn">
		        			  		       <button class="btn btn-default" type="submit"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></button>
		        			  		     </span>
	        			  		 </div><!-- /input-group -->
        			  		 </form>
        			  	</div>
        			  	<fmt:message key="warehouse.dispatch.port.list"/>
        			  </div>
        			  <%@ include file="port_table.jsp" %>
        			  <div class="clearfix"></div>
        			  <div class="panel-footer">
        			  	<ul>
        			  		<li><b>T</b> -<fmt:message key="warehouse.device.elock"/></li>
        			  		<div class="clearfix"></div>
        			  		<li><b>E</b> -<fmt:message key="warehouse.device.eseal"/></li>
        			  		<li><b>S</b>-<fmt:message key="warehouse.device.sensor"/></li>
        			  	</ul>
        			  	<div class="clearfix"></div>
        			  </div>
        			</div>
            	</div>
            	<div class="col-sm-9 right-content">
            		<div class="xqka form-horizontal">
            			<div class="form-group">
            			    <label for="inputEmail3" class="col-sm-3 control-label"><fmt:message key="warehouse.dispatch.demand.port"/>：</label>
            			    <div class="col-sm-9 kaabc">${deviceApplication.applcationPortName }</div>
            			</div>
            			<div class="form-group">
            				<input type="hidden" id="applicationId" value="${deviceApplication.applicationId }">
            				<input type="hidden" id="applicationPort" value="${deviceApplication.applcationPort }">
            				<input type="hidden" id="applicationDeviceNumber" value="${deviceApplication.deviceNumber }">
            				<input type="hidden" id="applicationEsealNumber" value="${deviceApplication.esealNumber }">
            				<input type="hidden" id="applicationSensorNumber" value="${deviceApplication.sensorNumber }">
            				<input type="hidden" id="applicationLongitude" value="${applicationDepartment.longitude }">
            				<input type="hidden" id="applicationLatitude" value="${applicationDepartment.latitude }">
            			    <label for="inputEmail3" class="col-sm-3 control-label"><fmt:message key="warehouse.dispatch.quantity"/>：</label>
            			    <div class="col-sm-9 kaabc">
            			    	<fmt:message key="warehouse.device.elock"/> <b>${deviceApplication.deviceNumber }</b> <fmt:message key="warehouse.device.eseal"/> <b>${deviceApplication.esealNumber }</b><fmt:message key="warehouse.device.sensor"/> <b>${deviceApplication.sensorNumber }</b>
            			    </div>
            			</div>
            		</div>
            		<div class="row row_three margint20 ">
	            		<div class="col-sm-12">
	            			<div class="panel panel-default">
	            			  <div class="panel-heading heading_ico04"><fmt:message key="warehouse.device.instock"/></div>
	            			  <div class="panel-body" id="deviceInventoryCharts" style="width: 98%;height:380px;overflow-x: hidden;">
		            			  <%--  <img src="${root }/static/images/tongji_03.png" alt=""> --%>
	            			  </div>
	            			</div>
	            		</div>
            		</div>
            		<div class="tab_box tab-content">
					  <!-- Nav tabs -->
					  <ul class="nav nav-tabs" role="tablist">
					    <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab"><fmt:message key="warehouse.dispatch.recommend.program"/></a></li>
					    <!-- <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">推荐方案2</a></li> -->
					  </ul>
					  <!-- Tab panes -->
					  <div class="tab-content">
					    <div role="tabpanel" class="tab-pane active" id="home">
					    	<div class="row row_three margint20">
	    	            		<div class="col-sm-7">
	    	            			<div class="panel panel-default">
	    	            			  <div class="panel-heading heading_ico05"><fmt:message key="warehouse.dispatch.distance.analysis"/></div>
	    	            			  <div class="panel-body" id="planProgramChart">
	    		            			   <%-- <img src="${root }/static/images/tongji_03.png" alt=""> --%>
	    	            			  </div>
	    	            			</div>

	    	            		</div>
	    	            		<div class="col-sm-5">
	    	            			<div class="panel panel-default">
	    	            			  <div class="panel-heading heading_ico06"><fmt:message key="warehouse.dispatch.title"/></div>
	    	            			  <div class="panel-body ">
	    		            			  <table class="table table-bordered table-striped">
		  	            			         <thead>
		  	            			           <tr>
		  	            			             <th><fmt:message key="warehouse.dispatch.port"/></th>
		  	            			             <th><fmt:message key="warehouse.device.elock"/></th>
		  	            			             <th><fmt:message key="warehouse.device.eseal"/></th>
		  	            			             <th><fmt:message key="warehouse.device.sensor"/></th>
		  	            			           </tr>
		  	            			         </thead>
		  	            			         <tbody>
		  	            			           <c:forEach var="plan" items="${dispatchPlanList }">
		  	            			           	<tr>
		  	            			           		<td>${plan.portName }</td>
		  	            			           		<td>${plan.trackDeviceNumber }</td>
		  	            			           		<td>${plan.esealNumber }</td>
		  	            			           		<td>${plan.sensor }</td>
		  	            			           	</tr>
		  	            			           </c:forEach>
		  	            			         </tbody>
		  	            			       </table>
	    	            			  </div>
	    	            			</div>
	    	            		</div>
					    	 </div>
					    </div>
					    <!-- <div role="tabpanel" class="tab-pane" id="profile">...</div> -->
					</div>
            	</div>
            	
	    	       <div class="tab_box tab-content">
					   <ul class="nav nav-tabs" role="tablist">
					    <li role="presentation" class="tab-cotent-title"><fmt:message key="warehouse.dispatch.actual.program"/></li>
					  </ul>
					  <!-- Tab panes -->
					  <div class="tab-content">
					    <div role="tabpanel" class="tab-pane active" id="home">
					    	<div class="row row_three margint20">
	    	            		<div class="col-sm-7">
	    	            			<div class="panel panel-default">
	    	            			  <div class="panel-heading heading_ico05"><fmt:message key="warehouse.dispatch.distance.analysis"/></div>
	    	            			  <div class="panel-body" id="actualProgramChart">
	    	            			  </div>
	    	            			</div>
	    	            		</div>
	    	            		<div class="col-sm-5">
	    	            			<div class="panel panel-default">
	    	            			  <div class="panel-heading heading_ico06"><fmt:message key="warehouse.dispatch.title"/></div>
	    	            			  <div class="panel-body ">
	    	            			  <form id="actualProgramForm" action="" method="post">
	    	            			  	 <table class="table table-bordered table-striped" id="actualTable">
		  	            			         <thead>
		  	            			           <tr>
		  	            			           	 <th style="display: none;"></th>
		  	            			             <th><fmt:message key="warehouse.dispatch.port"/></th>
		  	            			             <th><fmt:message key="warehouse.device.elock"/></th>
		  	            			             <th><fmt:message key="warehouse.device.eseal"/></th>
		  	            			             <th><fmt:message key="warehouse.device.sensor"/></th>
		  	            			           </tr>
		  	            			         </thead>
		  	            			         <tbody>
		  	            			         </tbody>
	  	            			       </table>
	    	            			  </form>
	    	            			  </div>
	    	            			</div>
	    	            		</div>
					    	 </div>
					    </div>
						<div class=" text-center button-box">
							<button id="dispatchExecuteButton" class="btn btn btn-primary" type="submit"><fmt:message key="warehouse.dispatch.execute"/></button>
							<button id="goBackButton" class="btn btn-default"><fmt:message key="warehouse.dispatch.close"/></button>
						</div>
					 </div>
            	</div>
            	<div class="clearfix"></div>
            </div>
	</div>
	<script src="${root}/static/js/jquery.form.js"></script>
</body>
</html>