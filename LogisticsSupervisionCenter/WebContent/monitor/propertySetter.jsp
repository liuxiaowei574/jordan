<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../../include/taglib.jsp"%>
<%-- <%@ include file="../../include/include.jsp"%> --%>
<%-- <jsp:include page="../../include/include.jsp" /> --%>
<title></title>
</head>

<body>


    <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel"> Vehicle Setting
		</h4>
	</div>
	<div class="modal-body">
	<form id="vehiclePropertyForm">
	   <table>
	     <tbody>
	        <tr>
	          <td><label class="parentSelect"> <input  id="showAllPts1" type="checkbox">All Points</label></td>
	           <td><label class="parentSelect"><input id="seleVehiclPts1" type="checkbox">
		             <input id="selectNum1" class="parentSelect" type="number" value="12" step="1" min="0" max="10000"/>Select Points</label> </td>
<!-- 	           <td><label class="parentSelect"><input value="Save Point" type="button"  id="saveVehicleTrack"></label></td> -->
	        </tr>
	     </tbody>
	   </table>

       <table>
	     <tbody>
	      <tr>
	          <td>
	              <label>Freeze Alarm</label>
	          </td>
	          <td>
	              
	          <input id="switch-size" type="checkbox" checked data-size="mini">
	          </td>
	         </tr>
	     </tbody>
	    </table>
	     
	</form>
      </div>
	<script>
	$(function() {
		 var freezeAlarm='${freezeAlarm}';
		 if(freezeAlarm=="0"){
		     $('input[id="switch-size"]').bootstrapSwitch('state', true, true);
		 }else{
		     $('input[id="switch-size"]').bootstrapSwitch('state', false, false);
		 }
		 
		 $('input[id="switch-size"]').on('switchChange.bootstrapSwitch', function(event, state) {
              var freezeAlarm = "0";
			  if(true==state){
				  freezeAlarm = "0"; 
			  }else if(false == state){
				  freezeAlarm = "1";
			  }
			  updateVehicleFreezeAlarm(freezeAlarm);
			});
// 	      debugger;
// 	      return alert($("#switch-size").bootstrapSwitch("state"));
		    var $showAll = $("#showAllPts1");
			var $selectPts = $("#seleVehiclPts1");
			var $selectNum = $("#selectNum1");
			$showAll.on('click',function(){
				$selectPts.prop("checked", false);
				//nucVehicleStatusData = obj;
				if(typeof(nucVehicleStatusData)!="undefined"){
					getVehiclePaths(nucVehicleStatusData,ALL_POINT);
				}
			});
            $('input[id="seleVehiclPts1"]:checked').attr("checked",'true');
            if (($selectPts).attr('checked')) {
                alert("111111")
            }
			$selectPts.on('click',function(){
				$showAll.prop("checked", false);
				if(typeof(nucVehicleStatusData)!="undefined"){
					
					selectNumber = Number($selectNum.val());
					getVehiclePaths(nucVehicleStatusData,SELECT_POINT);
				}
			});
			
		
	});  
	
	
	
	
	
    function updateVehicleFreezeAlarm(freezeAlarm){
    	 var freezeAlarmUrl = root + "/commonvehicle/updateVehicleFreezeAlarm.action?vehicleId="+freazeAlarmVehicleId+"&freezeAlarm="+freezeAlarm;
    	$.ajax({
	 		type : "POST",
	 		url : freezeAlarmUrl,
	 		dataType : "json",
	 		cache : false,
	 		async : false,
	 		error : function(e, message, response) {
	 			console.log("Status: " + e.status + " message: " + message);
	 		},
	 		success : function(jsonObj) {
	 			if(freezeAlarm==0){
	 				bootbox.alert($.i18n.prop("vehicle.freeze.yes"));
	 			}else{
	 				bootbox.alert($.i18n.prop("vehicle.freeze.no"));
	 			}
	 			
	 		}
    	});
    	 
    }
   </script>

</body>