<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${root}/static/bootstrap-slider/css/bootstrap-slider.min.css">
<jsp:include page="../include/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Risk Analysis</title>
<style type="text/css">
.in.modal-backdrop{
	/* z-index: 1100; */
}

.tooltip-main{
	z-index: 1;
}

</style>
<script type="text/javascript">
var lprArr = ${lprArr};
var driverArr = ${driverArr};
</script>

</head>
<body>
<%@ include file="../include/left.jsp" %>
<div class="row site">
	<div class="wrapper-content margint95 margin60">   
		<div class="profile profile_box02">
            
            <!-- modal -->
			<div class="modal fade" id="myModal" tabindex="-1" role="dialog"  style="z-index:1101;"
			   aria-labelledby="myModalLabel" aria-hidden="true">
			   <div class="modal-dialog" style="width:70%;">
			      <div class="modal-content">
			      </div><!-- /.modal-content -->
			   </div><!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->         	
    
      	  <!--search form-->
	      <div class="tab-content m-b">
	        <div class="tab-cotent-title">
	        	<div class="Features pull-right">
					<ul>
						<li><a id="btnShow" class="btn btn-info"><fmt:message key="risk.button.show"/></a></li>
						<li><a id="btnHide" class="btn btn-info hidden"><fmt:message key="risk.button.hide"/></a></li>
					</ul>
				</div>
	        	<fmt:message key="risk.analysis.parameters"/>
	        </div>
	          <div class="row" id="riskParams" style="display: none;">
	            <div class="col-sm-4">
	              <div class="media gray_bg">
	                    <div class="media-body">
	                      <h4 class="media-heading"><fmt:message key="risk.trip.head" />  </h4>
	                      <input type="hidden" readonly="readonly" class="form-control" id="RISK_ALARM_TRIP_PERCENT_YELLOW" placeholder="Green Threshold" value="33">
	                      <input type="hidden" readonly="readonly" class="form-control" id="RISK_ALARM_TRIP_PERCENT_RED" placeholder="Yellow Threshold" value="67">
	                     	 <fmt:message key="risk.trip.description" />
	                      <input id="RISK_ALARM_TRIP_PERCENT" type="text"/>
	                    </div>
	                  </div>
	            </div>
	            <div class="col-sm-4">
	              <div class="media gray_bg">
	                    <div class="media-body">
	                      <h4 class="media-heading"><fmt:message key="risk.trip.serious.head"/></h4>
	                      <input type="hidden" readonly="readonly" class="form-control" id="RISK_SERIOUS_ALARM_TRIP_PERCENT_YELLOW" placeholder="Green Threshold" value="33">
	      				  <input type="hidden" readonly="readonly" class="form-control" id="RISK_SERIOUS_ALARM_TRIP_PERCENT_RED" placeholder="Yellow Threshold" value="67">
	                      	<fmt:message key="risk.trip.serious.description"/>
	                      <input id="RISK_SERIOUS_ALARM_TRIP_PERCENT" type="text"/>
	                    </div>
	                  </div>
	            </div>
	            <div class="col-sm-4">
	              <div class="media gray_bg">
	                    <div class="media-body">
	                      <h4 class="media-heading"><fmt:message key="risk.trip.normal.head"/></h4>
	                      <input type="hidden" readonly="readonly" class="form-control" id="RISK_NORMAL_ALARM_TRIP_PERCENT_YELLOW" placeholder="Green Threshold" value="33">
	      				  <input type="hidden" readonly="readonly" class="form-control" id="RISK_NORMAL_ALARM_TRIP_PERCENT_RED" placeholder="Yellow Threshold" value="67">
	                      	<fmt:message key="risk.trip.normal.description"/>
	                      <input id="RISK_NORMAL_ALARM_TRIP_PERCENT" type="text"/>
	                    </div>
	                  </div>
	            </div>
	            <div class="col-sm-4">
	              <div class="media gray_bg">
	                    <div class="media-body">
	                      <h4 class="media-heading"><fmt:message key="risk.serious.number.head"/></h4>
	                      <input type="hidden" readonly="readonly" class="form-control" id="RISK_SERIOUS_ALARM_NUMBER_PERCENT_YELLOW" placeholder="Green Threshold" value="33">
	      				  <input type="hidden" readonly="readonly" class="form-control" id="RISK_SERIOUS_ALARM_NUMBER_PERCENT_RED" placeholder="Yellow Threshold" value="67">
	                      	<fmt:message key="risk.serious.number.description"/>
	                      <input id="RISK_SERIOUS_ALARM_NUMBER_PERCENT" type="text"/>
	                    </div>
	                  </div>
	            </div>
	            <div class="col-sm-4">
	              <div class="media gray_bg">
	                    <div class="media-body">
	                      <h4 class="media-heading"><fmt:message key="risk.normal.number.head"/></h4>
	                      <input type="hidden" readonly="readonly" class="form-control" id="RISK_NORMAL_ALARM_NUMBER_PERCENT_YELLOW" placeholder="Green Threshold" value="33">
	      				  <input type="hidden" readonly="readonly" class="form-control" id="RISK_NORMAL_ALARM_NUMBER_PERCENT_RED" placeholder="Yellow Threshold" value="67">
	                     	 <fmt:message key="risk.normal.number.description"/>
	                      <input id="RISK_NORMAL_ALARM_NUMBER_PERCENT" type="text"/>
	                    </div>
	                  </div>
	            </div>
	            <div class="col-sm-4">
	              <div class="media gray_bg">
	                    <div class="media-body">
	                      <h4 class="media-heading"><fmt:message key="risk.final.head"/></h4>
	                      <input type="hidden" readonly="readonly" class="form-control" id="RISK_FINAL_YELLOW" placeholder="Green Threshold" value="33">
	      				  <input type="hidden" readonly="readonly" class="form-control" id="RISK_FINAL_RED" placeholder="Yellow Threshold" value="67">
	                      	<fmt:message key="risk.final.description"/>
	                      <input id="RISK_FINAL" type="text"/>
	                    </div>
	                  </div>
	            </div>
	            <div class="clearfix"></div>
	            <div class="form-group">
	              <div class="col-sm-offset-9 col-md-3">
	                <button type="button" class="btn btn-danger" onclick="relfesh();"><fmt:message key="risk.final.analysisbtn"/></button>
	              </div>
	            </div>
	            <div class="clearfix"></div>
	          </div>
	 
	      </div>
	      <!--search form end-->
      
	      <!--my result-->
	      <div class="tab-content m-b">
	        <div class="tab-cotent-title">
	          <div class="Features pull-right">
	            <!-- <ul>
	              <li><a id="exportBtn" class="btn btn-info">导出Excel</a></li>
	            </ul> -->
	          </div>
	          <fmt:message key="risk.final.analysistable"/></div>
	        <div class="search_table">
	          <table id="lpntable"></table><!-- 车牌风险表 -->
			  <table id="drivertable"></table><!-- 司机风险表 -->
			  <table id="goodstable"></table><!-- 货物风险表 -->
	        </div>
	      </div>
	      <!--my result end-->
		</div>
        <div class="clearfix"></div>
	</div>
</div>
<script src="${root}/static/bootstrap-slider/bootstrap-slider.min.js"></script>
<script type="text/javascript">
$("#RISK_ALARM_TRIP_PERCENT").slider({
//	id: "slider12c",
	tooltip: 'always',
	min: 0,
	max: 100,
	range: true,
	value: [33, 67]
}).on("slide", function(slideEvt) {
	var vArr = slideEvt.value
	var idArr = [this.id+"_YELLOW",this.id+"_RED"];
	for(var i=0;i<vArr.length;i++){
		$("#"+idArr[i]).val(vArr[i]);
	}
	
});
$("#RISK_SERIOUS_ALARM_TRIP_PERCENT").slider({
//	id: "slider12c",
	tooltip: 'always',
	min: 0,
	max: 100,
	range: true,
	value: [33, 67]
}).on("slide", function(slideEvt) {
	var vArr = slideEvt.value
	var idArr = [this.id+"_YELLOW",this.id+"_RED"];
	for(var i=0;i<vArr.length;i++){
		$("#"+idArr[i]).val(vArr[i]);
	}
	
});
$("#RISK_NORMAL_ALARM_TRIP_PERCENT").slider({
//	id: "slider12c",
	tooltip: 'always',
	min: 0,
	max: 100,
	range: true,
	value: [33, 67]
}).on("slide", function(slideEvt) {
	var vArr = slideEvt.value
	var idArr = [this.id+"_YELLOW",this.id+"_RED"];
	for(var i=0;i<vArr.length;i++){
		$("#"+idArr[i]).val(vArr[i]);
	}
	
});
$("#RISK_SERIOUS_ALARM_NUMBER_PERCENT").slider({
//	id: "slider12c",
	tooltip: 'always',
	min: 0,
	max: 100,
	range: true,
	value: [33, 67]
}).on("slide", function(slideEvt) {
	var vArr = slideEvt.value
	var idArr = [this.id+"_YELLOW",this.id+"_RED"];
	for(var i=0;i<vArr.length;i++){
		$("#"+idArr[i]).val(vArr[i]);
	}
	
});
$("#RISK_NORMAL_ALARM_NUMBER_PERCENT").slider({
//	id: "slider12c",
	tooltip: 'always',
	min: 0,
	max: 100,
	range: true,
	value: [33, 67]
}).on("slide", function(slideEvt) {
	var vArr = slideEvt.value
	var idArr = [this.id+"_YELLOW",this.id+"_RED"];
	for(var i=0;i<vArr.length;i++){
		$("#"+idArr[i]).val(vArr[i]);
	}
	
});
$("#RISK_FINAL").slider({
//	id: "slider12c",
	tooltip: 'always',
	min: 0,
	max: 100,
	range: true,
	value: [33, 67]
}).on("slide", function(slideEvt) {
	var vArr = slideEvt.value
	var idArr = [this.id+"_YELLOW",this.id+"_RED"];
	for(var i=0;i<vArr.length;i++){
		$("#"+idArr[i]).val(vArr[i]);
	}
	
});
</script>
<script type="text/javascript">
function lpnclickFun(field,rowObj,rowNum){
	return '<a style="color: blue;cursor: pointer;" onclick="showTrip(\'s_vehiclePlateNumber\',\''+ encodeURIComponent(rowObj["riskAnalysisId"])  +'\')">'+rowObj["riskAnalysisId"]+'</a>'
	/* var a = "${root}/analysis/listTrip.action?s_vehiclePlateNumber="+encodeURIComponent(rowObj["riskAnalysisId"]);
	return '<a data-toggle="modal" href="'+ a +'" data-target="#myModal">'+rowObj["riskAnalysisId"]+'</a>'; */
}
function driverclickFun(field,rowObj,rowNum){
	return '<a style="color: blue;cursor: pointer;" onclick="showTrip(\'s_driverName\',\''+ encodeURIComponent(rowObj["riskAnalysisId"])  +'\')">'+rowObj["riskAnalysisId"]+'</a>'
	/* var a = "${root}/analysis/listTrip.action?s_driverName="+encodeURIComponent(rowObj["riskAnalysisId"]);
	return '<a data-toggle="modal" href="'+ a +'" data-target="#myModal">'+rowObj["riskAnalysisId"]+'</a>'; */
	/* var a = "${root}/analysis/listTrip.action?s_driverName="+rowObj["riskAnalysisId"];
	return '<a data-toggle="modal" href="'+ a +'" data-target="#myModal">'+rowObj["riskAnalysisId"]+'</a>'; */
}
function showTrip(k,v){
/* 	$('#routeModal').modal({
		remote : "${root}/analysis/listTrip.action?"+k+"="+v,
		show : false,
		backdrop: 'static', 
		keyboard: false
		}).modal('show'); */
	var url = "${root}/analysis/listTrip.action?"+k+"="+v;
	$('#myModal').removeData('bs.modal');
	$('#myModal').modal({
		remote : url,
		show : false,
		backdrop : 'static',
		keyboard : false
	});
	$('#myModal').on('loaded.bs.modal', function(e) {
		$('#myModal').modal('show');
	});
	//模态框登录判断
	$('#myModal').on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
	});
}
//行程报警总数风险
function tripAlarmTotalNumFuc(field,rowObj,rowNum){
	var tripGreenThreshold = $("#RISK_ALARM_TRIP_PERCENT_YELLOW").val();
	var tripYellowThreshold = $("#RISK_ALARM_TRIP_PERCENT_RED").val();
	var tripPercent = 100*rowObj['tripAlarmTotalNum']/rowObj['tripTotalNum'];
	if(tripPercent>tripYellowThreshold){
		return '<div style="float:left;">'+rowObj['tripAlarmTotalNum']+'</div>'+'<div class="safebar redbg" style="float:right;"></div>';
	}else if(tripPercent>tripGreenThreshold){
		return '<div style="float:left;">'+rowObj['tripAlarmTotalNum']+'</div>'+'<div class="safebar yellowbg" style="float:right;"></div>';
	}else{
		return '<div style="float:left;">'+rowObj['tripAlarmTotalNum']+'</div>'+'<div class="safebar greenbg" style="float:right;"></div>';
	}
}
//行程严重报警总数风险
function tripSeriousAlarmTotalNumFuc(field,rowObj,rowNum){
	var tripSeriousGreenThreshold = $("#RISK_SERIOUS_ALARM_TRIP_PERCENT_YELLOW").val();
	var tripSeriousYellowThreshold = $("#RISK_SERIOUS_ALARM_TRIP_PERCENT_RED").val();
	var tripSeriousPercent = 100*rowObj['tripSeriousAlarmTotalNum']/rowObj['tripTotalNum'];
	if(tripSeriousPercent>tripSeriousYellowThreshold){
		return '<div style="float:left;">'+rowObj['tripSeriousAlarmTotalNum']+'</div>'+'<div class="safebar redbg" style="float:right;"></div>';
	}else if(tripSeriousPercent>tripSeriousGreenThreshold){
		return '<div style="float:left;">'+rowObj['tripSeriousAlarmTotalNum']+'</div>'+'<div class="safebar yellowbg" style="float:right;"></div>';
	}else{
		return '<div style="float:left;">'+rowObj['tripSeriousAlarmTotalNum']+'</div>'+'<div class="safebar greenbg" style="float:right;"></div>';
	}
}
//行程轻微报警总数风险
function tripMinorAlarmTotalNumFuc(field,rowObj,rowNum){
	var tripNormalGreenThreshold = $("#RISK_NORMAL_ALARM_TRIP_PERCENT_YELLOW").val();
	var tripNormalYellowThreshold = $("#RISK_NORMAL_ALARM_TRIP_PERCENT_RED").val();
	var tripMinorPercent = 100*rowObj['tripMinorAlarmTotalNum']/rowObj['tripTotalNum'];
	if(tripMinorPercent>tripNormalYellowThreshold){
		return '<div style="float:left;">'+rowObj['tripMinorAlarmTotalNum']+'</div>'+'<div class="safebar redbg" style="float:right;"></div>';
	}else if(tripMinorPercent>tripNormalGreenThreshold){
		return '<div style="float:left;">'+rowObj['tripMinorAlarmTotalNum']+'</div>'+'<div class="safebar yellowbg" style="float:right;"></div>';
	}else{
		return '<div style="float:left;">'+rowObj['tripMinorAlarmTotalNum']+'</div>'+'<div class="safebar greenbg" style="float:right;"></div>';
	}
}
//严重报警总数风险
function seriousAlarmTotalNumFuc(field,rowObj,rowNum){
	var seriousNumberGreenThreshold = $("#RISK_SERIOUS_ALARM_NUMBER_PERCENT_YELLOW").val();
	var seriousNumberYellowThreshold = $("#RISK_SERIOUS_ALARM_NUMBER_PERCENT_RED").val();
	var seriousNumPercent = 100*rowObj['seriousAlarmTotalNum']/(rowObj['seriousAlarmTotalNum']+rowObj['minorAlarmTotalNum']);
	if(seriousNumPercent>seriousNumberYellowThreshold){
		return '<div style="float:left;">'+rowObj['seriousAlarmTotalNum']+'</div>'+'<div class="safebar redbg" style="float:right;"></div>';
	}else if(seriousNumPercent>seriousNumberGreenThreshold){
		return '<div style="float:left;">'+rowObj['seriousAlarmTotalNum']+'</div>'+'<div class="safebar yellowbg" style="float:right;"></div>';
	}else{
		return '<div style="float:left;">'+rowObj['seriousAlarmTotalNum']+'</div>'+'<div class="safebar greenbg" style="float:right;"></div>';
	}
}
//轻微报警总数风险
function minorAlarmTotalNumFuc(field,rowObj,rowNum){
	var normalNumberGreenThreshold = $("#RISK_NORMAL_ALARM_NUMBER_PERCENT_YELLOW").val();
	var normalNumberYellowThreshold = $("#RISK_NORMAL_ALARM_NUMBER_PERCENT_RED").val();
	var normalNumPercent = 100*rowObj['minorAlarmTotalNum']/(rowObj['seriousAlarmTotalNum']+rowObj['minorAlarmTotalNum']);
	if(normalNumPercent>normalNumberYellowThreshold){
		return '<div style="float:left;">'+rowObj['minorAlarmTotalNum']+'</div>'+'<div class="safebar redbg" style="float:right;"></div>';
	}else if(normalNumPercent>normalNumberGreenThreshold){
		return '<div style="float:left;">'+rowObj['minorAlarmTotalNum']+'</div>'+'<div class="safebar yellowbg" style="float:right;"></div>';
	}else{
		return '<div style="float:left;">'+rowObj['minorAlarmTotalNum']+'</div>'+'<div class="safebar greenbg" style="float:right;"></div>';
	}
}
//总风险等级
function dataFormatFuc(field,rowObj,rownum){
	var redNum = 0;
	var greenNum = 0;
	var yellowNum = 0;
	var tripGreenThreshold = $("#RISK_ALARM_TRIP_PERCENT_YELLOW").val();
	var tripYellowThreshold = $("#RISK_ALARM_TRIP_PERCENT_RED").val();
	var tripSeriousGreenThreshold = $("#RISK_SERIOUS_ALARM_TRIP_PERCENT_YELLOW").val();
	var tripSeriousYellowThreshold = $("#RISK_SERIOUS_ALARM_TRIP_PERCENT_RED").val();
	var tripNormalGreenThreshold = $("#RISK_NORMAL_ALARM_TRIP_PERCENT_YELLOW").val();
	var tripNormalYellowThreshold = $("#RISK_NORMAL_ALARM_TRIP_PERCENT_RED").val();
	var seriousNumberGreenThreshold = $("#RISK_SERIOUS_ALARM_NUMBER_PERCENT_YELLOW").val();
	var seriousNumberYellowThreshold = $("#RISK_SERIOUS_ALARM_NUMBER_PERCENT_RED").val();
	var normalNumberGreenThreshold = $("#RISK_NORMAL_ALARM_NUMBER_PERCENT_YELLOW").val();
	var normalNumberYellowThreshold = $("#RISK_NORMAL_ALARM_NUMBER_PERCENT_RED").val();
	var finalGreenThreshold = $("#RISK_FINAL_YELLOW").val();
	var finalYellowThreshold = $("#RISK_FINAL_RED").val();
	//绿、黄、红和为1
	var greenPercent = finalGreenThreshold;
	var yellowPercent = finalYellowThreshold - finalGreenThreshold;	
	var redPercent = 100- finalYellowThreshold;
	
	var tripPercent = 100*rowObj['tripAlarmTotalNum']/rowObj['tripTotalNum'];
	if(tripPercent>tripYellowThreshold){
		redNum++;
	}else if(tripPercent>tripGreenThreshold){
		yellowNum++;
	}else{
		greenNum++;
	}
	var tripSeriousPercent = 100*rowObj['tripSeriousAlarmTotalNum']/rowObj['tripTotalNum'];
	if(tripSeriousPercent>tripSeriousYellowThreshold){
		redNum++;
	}else if(tripSeriousPercent>tripSeriousGreenThreshold){
		yellowNum++;
	}else{
		greenNum++;
	}
	var tripNormalPercent = 100*rowObj['tripMinorAlarmTotalNum']/rowObj['tripTotalNum'];
	if(tripNormalPercent>tripNormalYellowThreshold){
		redNum++;
	}else if(tripNormalPercent>tripNormalGreenThreshold){
		yellowNum++;
	}else{
		greenNum++;
	}

	var seriousNumberPercent = 100*rowObj['seriousAlarmTotalNum']/(rowObj['seriousAlarmTotalNum']+rowObj['minorAlarmTotalNum']);
	if(seriousNumberPercent>tripNormalYellowThreshold){
		redNum++;
	}else if(seriousNumberPercent>tripNormalGreenThreshold){
		yellowNum++;
	}else{
		greenNum++;
	}
	var normalNumberPercent = 100*rowObj['minorAlarmTotalNum']/(rowObj['seriousAlarmTotalNum']+rowObj['minorAlarmTotalNum']);
	if(normalNumberPercent>normalNumberYellowThreshold){
		redNum++;
	}else if(normalNumberPercent>normalNumberGreenThreshold){
		yellowNum++;
	}else{
		greenNum++;
	}
	
	var redNumPercent = 100* redNum/(redNum+greenNum+yellowNum);
	var yellowNumPercent = 100* yellowNum/(redNum+greenNum+yellowNum);
	var greenNumPercent = 100* greenNum/(redNum+greenNum+yellowNum);
	var value = 0;
	if(greenNumPercent>=greenPercent){
		value += 0;
	}else{
		value += greenPercent-greenNumPercent;
	}
	if(yellowNumPercent>=yellowPercent){
		value += yellowPercent;
	}else{
		value += yellowNumPercent;
	}
	if(redNumPercent>=redPercent){
		value += redPercent;
	}else{
		value += redNumPercent;
	}
	
	if(value > finalYellowThreshold){
		return '<div class="safebar redbg"></div>';
	}else if(value>finalGreenThreshold){
		return '<div class="safebar yellowbg"></div>';
	}else{
		return '<div class="safebar greenbg"></div>';
	}
	
}
function relfesh(){
	$('#lpntable').bootstrapTable('load',lprArr);
	$('#drivertable').bootstrapTable('load',driverArr);
}

$(function() {
	//设置传入参数
	function queryParams(params) {
		/* //遍历form 组装json  
        $.each($("#sform").serializeArray(), function(i, field) {  
            console.info(field.name + ":" + field.value + " ");  
            //可以添加提交验证                   
            params += "&" + field.name +"="+ field.value;  
        });   */
        return params;
	}
	//刷新tale
	$(window).resize(function(){
		$('#lpntable').bootstrapTable("resetView");
		$('#drivertable').bootstrapTable("resetView");
		$('#goodstable').bootstrapTable("resetView");
	});
	//车辆风险分析列表
	$('#lpntable').bootstrapTable({
		//method:'post',
		//url:'${root}/analysis/lpnRiskAnalysis.action',
		//height: $(window).height() - 200,
		striped: true,
		dataType: "json",
		pagination: true, //分页
		idfield: "riskAnalysisId",
		sortName:"riskAnalysisId",
		"queryParamsType": "limit",
		singleSelect: false,
		contentType: "application/x-www-form-urlencoded",
		pageSize: 10,
		pageNumber:1,
		//totalRows:10,
		showColumns: false, //不显示下拉框（选择显示的列）
		queryParams: queryParams,
		sortable:true,
		pageList : [ 10, 20, 30 ],
		search: true, //显示搜索框
		//sidePagination: "server", //服务端处理分页
	    columns: [{
	    	formatter:lpnclickFun,
	        field: 'riskAnalysisId',
	        title: '<fmt:message key="risk.table.lpnId"/>',
			sortable:true
	    },{
	    	field:'tripTotalNum',
	    	title:"<fmt:message key="risk.table.tripTotalNum"/>",
			sortable:true
	    },{
	    	formatter:tripAlarmTotalNumFuc,
	    	field:'tripAlarmTotalNum',
	    	title:"<fmt:message key="risk.table.tripAlarmTotalNum"/>",
			sortable:true
	    },{
	    	formatter:tripSeriousAlarmTotalNumFuc,
	    	field:'tripSeriousAlarmTotalNum',
	    	title:"<fmt:message key="risk.table.tripSeriousAlarmTotalNum"/>",
			sortable:true
	    },{
	    	formatter:tripMinorAlarmTotalNumFuc,
	    	field:'tripMinorAlarmTotalNum',
	    	title:"<fmt:message key="risk.table.tripMinorAlarmTotalNum"/>",
			sortable:true
	    },{
	    	field:'alarmTotalNum',
	    	title:"<fmt:message key="risk.table.alarmTotalNum"/>",
			sortable:true
	    },{
	    	formatter:seriousAlarmTotalNumFuc,
	    	field:'seriousAlarmTotalNum',
	    	title:"<fmt:message key="risk.table.seriousAlarmTotalNum"/>",
			sortable:true
	    },{
	    	formatter:minorAlarmTotalNumFuc,
	    	field:'minorAlarmTotalNum',
	    	title:"<fmt:message key="risk.table.minorAlarmTotalNum"/>",
			sortable:true
	    },{
	    	formatter:dataFormatFuc,
	    	title:'<fmt:message key="risk.table.riskLevel"/>',
			sortable:true
	    }],
	    data:lprArr
	});
	//司机风险分析列表
	$('#drivertable').bootstrapTable({
		//method:'post',
		//url:'${root}/analysis/driverRiskAnalysis.action',
		//height: $(window).height() - 200,
		striped: true,
		dataType: "json",
		pagination: true, //分页
		idfield: "riskAnalysisId",
		sortName:"riskAnalysisId",
		"queryParamsType": "limit",
		singleSelect: false,
		contentType: "application/x-www-form-urlencoded",
		pageSize: 10,
		pageNumber:1,
		sortable:true,
		showColumns: false, //不显示下拉框（选择显示的列）
		//sidePagination: "server", //服务端请求
		queryParams: queryParams,
		pageList: [10,20],
		search: true, //显示搜索框
	    columns: [{
	    	formatter:driverclickFun,
	        field: 'riskAnalysisId',
	        title: '<fmt:message key="risk.table.driverName"/>',
			sortable:true
	    },{
	    	field:'tripTotalNum',
	    	title:"<fmt:message key="risk.table.tripTotalNum"/>",
			sortable:true
	    },{
	    	formatter:tripAlarmTotalNumFuc,
	    	field:'tripAlarmTotalNum',
	    	title:"<fmt:message key="risk.table.tripAlarmTotalNum"/>",
			sortable:true
	    },{
	    	formatter:tripSeriousAlarmTotalNumFuc,
	    	field:'tripSeriousAlarmTotalNum',
	    	title:"<fmt:message key="risk.table.tripSeriousAlarmTotalNum"/>",
			sortable:true
	    },{
	    	formatter:tripMinorAlarmTotalNumFuc,
	    	field:'tripMinorAlarmTotalNum',
	    	title:"<fmt:message key="risk.table.tripMinorAlarmTotalNum"/>",
			sortable:true
	    },{
	    	field:'alarmTotalNum',
	    	title:"<fmt:message key="risk.table.alarmTotalNum"/>",
			sortable:true
	    },{
	    	formatter:seriousAlarmTotalNumFuc,
	    	field:'seriousAlarmTotalNum',
	    	title:"<fmt:message key="risk.table.seriousAlarmTotalNum"/>",
			sortable:true
	    },{
	    	formatter:minorAlarmTotalNumFuc,
	    	field:'minorAlarmTotalNum',
	    	title:"<fmt:message key="risk.table.minorAlarmTotalNum"/>",
			sortable:true
	    },{
	    	formatter:dataFormatFuc,
	    	title:'<fmt:message key="risk.table.riskLevel"/>',
			sortable:true
	    }],
	    data:driverArr
	});
	//货物风险分析列表
	$("#goodstable").bootstrapTable('destroy').bootstrapTable({
		url:root+"/analysis/goodTyleriskSetting.action",
		clickToSelect : true,
		showRefresh : true,
		showColumns : false,
		showExport : false,
		striped : true,
		idfield: "iSerial",
		sortName:"iSerial",
		singleSelect: false,
		method : "get",
		cache : false,
		sortable:true,
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
		queryParams: queryParams,
		//search: true, //显示搜索框
        columns:  [ 
       {
			field : 'iSerial',
			title : '<fmt:message key="risk.setting.query.no"/>',
			sortable:true
		},{
			field : 'gtypeName',
			title : '<fmt:message key="risk.setting.query.name"/>',
			sortable:true
		}, {
			field : 'lowRiskV',
			title : '<fmt:message key="risk.setting.result.fazhi"/>',
			sortable:true
		}
		]
    });	
	
	$("#btnShow").click(function() {
		$("#riskParams").show();
		$("#btnShow, #btnHide").toggleClass("hidden");
	});
	$("#btnHide").click(function() {
		$("#riskParams").hide();
		$("#btnShow, #btnHide").toggleClass("hidden");
	});
});
</script>
</body>
</html>