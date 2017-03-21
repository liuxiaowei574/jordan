<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%-- <jsp:include page="../../include/include.jsp" /> --%>
<title></title>
<style>
#routeAreaForm button {
	padding: 6px 18px;
}
</style>
</head>
<body>
	<form id="routeAreaForm">

		<ul class="Custom_list hidden" id="addRapoint" style="padding-bottom: 18px;">
			<li>
				<label class="col-sm-6 control-label" for="routeAreaName"><fmt:message key="main.list.routeArea.routeAreaName" /></label>
				<input type="text" class="form-control input-sm" id="routeAreaName" name="routeAreaName"> 
				<div class="hidden" id="routeAreaTypeDiv">
					<label class="col-sm-6 control-label" for="routeAreaType"><fmt:message key="main.list.routeArea.routeAreaType" /></label>
					<select class="form-control input-sm" id="routeAreaType" name="routeAreaType"  onchange="routeChangeType()">
						<option value="0"><fmt:message key="main.list.option.select" /></option>
						<option value="1"><fmt:message key="main.list.routeArea.routeAreaType.option.safeArea" /></option>
						<option value="2"><fmt:message key="main.list.routeArea.routeAreaType.option.dangerArea" /></option>
						<option value="3"><fmt:message key="main.list.routeArea.routeAreaType.option.monitorArea" /></option>
						<option value="4"><fmt:message key="main.list.routeArea.routeAreaType.option.separateArea" /></option>
						<option value="5"><fmt:message key="main.list.routeArea.routeAreaType.option.targetZoon" /></option>
					</select>
				</div>
				<!-- <input type="hidden" class="form-control input-sm" id="routeAreaType" name="routeAreaType">  -->
				<input id="routeAreaId" name="routeAreaId" type="hidden" />
				<div class="hidden" id="routeAreaBufferDiv">
				    
					<label class="col-sm-12 control-label" for="startId"><fmt:message key="main.list.routeArea.startPort" /></label>
					<select class="form-control input-sm" id="startId" name="startId">
						<option value=""></option>
					</select>
					
					<label class="col-sm-12 control-label" for="endId"><fmt:message key="main.list.routeArea.endPort" /></label>
					<select class="form-control input-sm" id="endId" name="endId">
						<option value=""></option>
					</select>
					
					<label class="col-sm-12 control-label"  for="routeDistanceFormatted"><fmt:message key="main.list.routeArea.distance" /></label> 
					<input type="text" class="form-control input-sm" id="routeDistanceFormatted" name="routeDistanceFormatted">
					<input type="hidden" id="routeDistance" name="routeDistance">
					
					<label class="col-sm-12 control-label"  for="routeCost"><fmt:message key="main.list.routeArea.routeCost" /></label> 
					<input type="text" class="form-control input-sm" id="routeCost" name="routeCost">
					
					<label class="col-sm-12 control-label" for="routeAreaBuffer"><fmt:message key="main.list.routeArea.routeAreaBuffer" /></label> 
					<input type="text" class="form-control input-sm" value="20"  id="routeAreaBuffer" name="routeAreaBuffer">
				</div>
				<label class="col-sm-6 control-label" for="routeAreaStatus"><fmt:message key="main.list.routeArea.routeAreaStatus" /></label> 
				<select class="form-control input-sm" id="routeAreaStatus" name="routeAreaStatus">
					<option value="0"><fmt:message key="main.list.routeArea.routeAreaStatus.option.valid" /></option>
					<option value="1"><fmt:message key="main.list.routeArea.routeAreaStatus.option.invalid" /></option>
				</select>
		
				<div   id="colorSelect" class="m2">
					<label class="col-sm-6 control-label" for="routeAreaColor">Color Select</label> 
<!-- 					<input type="text" class="form-control input-sm" id="routeAreaColor" name="routeAreaColor"> -->
<!-- 					<input  type="button" style="width:120px;border-color: transparent;" id="picker"></button> -->
<!-- 				<input type="text" id="position-bottom-right" class="form-control demo" data-position="bottom right" value="#0088cc"> -->
				<input id="jscolor" class="jscolor wd150px form-control input-sm" autocomplete="off"  name="routeAreaColor"  value="66ff00">
<!-- 				<input  type="button" class="btn btn-info"   id="preview"></input> -->
<!--                  <button id="previewBtn"  type="button" class="btn btn-info"> -->
<!-- 					preview -->
<!-- 				</button> -->
				
<!-- 				<a id="previewBtn" href="javascript:void(0)" style="text-decoration:underline">Preview</a> -->
				</div>
				
				<input type="hidden" name="routeAreaPtCol" id="routeAreaPtCol" class="form-control input-sm">

				<button id="addRouteAreaBtn" type="button" class="btn btn-danger">
					<fmt:message key="common.button.add" />
				</button>
				<button id="editRouteAreaBtn" type="button" class="btn btn-danger">
					<fmt:message key="common.button.modify" />
				</button>
				<button type="reset" class="btn btn-darch">
					<fmt:message key="common.button.reset" />
				</button>
				<button id="backBtn" type="button" class="btn btn-darch">
					<fmt:message key="common.button.back" />
				</button>
				
				</li>

		</ul>
	</form>

	<script>
	$(function() {
		$(".demo").each( function() {
			$(this).minicolors({
				animationSpeed: 50,
				animationEasing: 'swing',
				change: null,
				changeDelay: 0,
				control: 'hue',
				defaultValue: '',
				hide: null,
				hideSpeed: 100,
				letterCase: 'lowercase',
				opacity: false,
				position: 'bottom left',
				show: null,
				showSpeed: 100,
				theme: 'default'
			});
		});   
		    
		$("#jscolor").change(function(){
		    previewRoadColor();
		  });
// 		$("#jscolor").on('input',function(e){  
// 		    previewRoadColor(); 
// 		});
		$("#routeAreaBuffer").on('input',function(e){  
		    previewRoadColor(); 
		});
// 		$("#routeAreaBuffer").change(function(){
// 		    previewRoadColor();
// 		  });
		
	});  
	 function routeChangeType () {
    	 if(['3','5'].indexOf($('#routeAreaType option:selected').val()) < 0){
    	     $("#belongToPort").attr("disabled",true).children("option").removeAttr("selected");
    	 }else{
    	     $("#belongToPort").attr("disabled",false);
    	 }
     };
     $('#addRouteAreaBtn').on('click', function () {
    	 addPlanRoute();
     });
    
     
	 $('#previewBtn').on('click', function () {
    	 previewRoadColor();
     });
     function getDrawRouteArea(jsondata){
    	 alert(jsondata)
     }
     function addPlanRoute() {
    	 trimText();
    	 //debugger;
    	 
    	// $('#progressModal').modal('show')
    	 
    	 /*用于google自动分析路坐标*/
    	//$("#routeAreaPtCol").val(routeAreaCol);
    	 
    	 
    	$("#routeAreaPtCol").val(getEditRouteArray(overlaysArray[0]));
    	if(""==$("#routeAreaPtCol").val()||null==$("#routeAreaPtCol").val()||typeof($("#routeAreaPtCol").val())=="undefined"){
    		 bootbox.alert($.i18n.prop("gis.label.routeArea.isNull"));
    		 return;
    	}
    	if(menuType=="0"){
    		if(!checkRouteAreaInput()) {
    			return false;
    		}
    	} else if(menuType=="1"){
    		if(!checkSiteInput()) {
    			return false;
    		}
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
			success : function(data) {
				data = JSON.parse(data);
				if(!needLogin(data)) {
					 //$('#progressModal').modal('hide')
					if(data.success) {
						routeAreaCol="";
						bootbox.success($.i18n.prop("map.routeArea.save.success"));
						findAllRouteAreaList();
					}else{
						bootbox.alert($.i18n.prop("map.routeArea.save.failure") + '<br>' + data.message);
					}
				}
				
			}
		});

  	}
     
     var previewArr = [];
     function previewRoadColor(){
    	 if(overlaysArray.length>0){
    		 var roadPts = GisGetEditRouteArray(overlaysArray[0]);
    		 GisClearOverlays(overlaysArray);
    		 var pts =  eval(roadPts);
    		 bufferColor = "#"+$("#jscolor").val();
    		 bufferWidth =  $("#routeAreaBuffer").val();
    		 var drawPolylineStyle = {
						"color" : "#"+$("#jscolor").val()||"#00ff00",
						"weight" : $("#routeAreaBuffer").val()||20,
						"opacity" : 0.3,
						"zIndex":-1
					}
				var polyline = GisShowPolyLineInMap(pts,true, drawPolylineStyle);
    		   overlaysArray.push(polyline);
    		   //GisEnableEdit();
    		  // GisEnableEdit();
    		
    	 }
     }
     
     
     $('#editRouteAreaBtn').on('click', function () {
    	 updatePlanRoute();
     });
     var editparam ;
     function updatePlanRoute() {
    	 trimText();
//     	 $('#progressModal').modal({
//     		 backdrop: 'static', 
//     		  keyboard: false
//     		})
    	//
    	 if(RouteAreaType==POLYLINE){
    		// if(points.length>8){
    			// var pointparam = GisGetEditRouteArray(overlaysArray[0]);
    	          //	editparam = pointparam;
    	          //	$("#routeAreaPtCol").val(pointparam); 
				//}else{
					/*google自动分析路径*/
					
					//$("#routeAreaPtCol").val(routeAreaCol);
				//}
					$("#routeAreaPtCol").val(getEditRouteArray(overlaysArray[0]));
      	}else{
      		// $("#routeAreaPtCol").val(getEditRouteArray(overlaysArray[0]));
      		var pointparam = GisGetEditRouteArray(overlaysArray[0]);
          	editparam = pointparam;
          	$("#routeAreaPtCol").val(pointparam); 
      	}
		if(isNotNull(routeAreaCol)){
			$("#routeAreaPtCol").val(routeAreaCol);
		}
     	if(""==$("#routeAreaPtCol").val()||null==$("#routeAreaPtCol").val()||typeof($("#routeAreaPtCol").val())=="undefined"){
     		 bootbox.alert($.i18n.prop("gis.label.routeArea.isNull"));
     		 return;
     	}
     	
     	if(menuType=="0"){
    		if(!checkRouteAreaInput()) {
    			return false;
    		}
    	} else if(menuType=="1"){
    		if(!checkSiteInput()) {
    			return false;
    		}
    	}
    	var param = $("#routeAreaForm").serialize();
  		var portUrl = getRootPath() + "monitorroutearea/updateRouteArea.action?ids="+menuType;
  		$.ajax({
			type : "POST",
			url : portUrl,
			data: param,
			error : function(e, message, response) {
				console.log("Status: " + e.status + " message: " + message);
			},
			success : function(data) {
				data = JSON.parse(data);
				if(!needLogin(data)) {
					if(data.success) {
						routeAreaCol="";
						$('#progressModal').modal('hide')
						bootbox.success($.i18n.prop("map.routeArea.update.success"));
						findAllRouteAreaList();
						getPointsByRouteAreaId($("#routeAreaId").val(),$("#routeAreaBuffer").val())
					}else{
						bootbox.alert($.i18n.prop("map.routeArea.update.failure") + '<br>' + data.message);
					}
				}
			}
		}); 

  	}
   </script>

</body>