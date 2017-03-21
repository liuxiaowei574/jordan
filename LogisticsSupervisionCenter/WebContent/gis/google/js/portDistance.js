/**
 * 单独用于港口测试（为尽快实现港口测距功能，GIS功能提取完毕后，可不再使用此功能）
 */

/**
 * 港口距离（测试）
 * @param oriPoint
 * @param desPoint
 * @param callback
 */
function measurePathLength(oriPoint, desPoint, callback) {
	var ori, des;
	if (typeof (oriPoint.lat) != "undefined"
			&& typeof (oriPoint.lng) != "undefined") {
		ori = new google.maps.LatLng(oriPoint.lat,oriPoint.lng);
	} else {
		ori = oriPoint;
	}
	if (typeof (desPoint.lat) != "undefined"
			&& typeof (desPoint.lng) != "undefined") {
		des = new google.maps.LatLng(desPoint.lat,desPoint.lng);
	} else {
		des = desPoint;
	}
	// 路线服务
	var directionsService = new google.maps.DirectionsService();
	// 自助寻找路径
	var request = {
		origin : ori,
		destination : des,
		optimizeWaypoints : true,// 降低路线的总体成本
		travelMode : google.maps.TravelMode.DRIVING
	};
	directionsService.route(request, function(response, status) {
		if (status == google.maps.DirectionsStatus.OK) {
			var distance = response.routes[0].legs[0].distance.text;
			var duration = response.routes[0].legs[0].duration.text;
			var analysisObj = {
				distance : distance,
				duration : duration
			};
			callback(analysisObj);
		}
	});
}



var startPoints = new Array();
var endPoints = new Array();
var sysDepData = null;
var ii=1;
var jj=1;
var endObjs = null;
var startObjs = null;
function getAllSystemDepartments(){
	var sysDepUrl = getRootPath() + "systemdepartmenttest/findAllSystemDepartmentBOTest.action";
	$.ajax({
		type : "POST",
		url : sysDepUrl,
		dataType : "json",
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(jsonObj) {
			var startPoint = null;
			var startName = "";
			sysDepData = jsonObj;
			startObjs = jsonObj;
			endObjs = jsonObj;
			if(null!=sysDepData&&sysDepData.length>0)
			 for (var int = 0; int < sysDepData.length; int++) {
				var obj = sysDepData[int];
				var point = {lat:obj.latitude,lng:obj.longitude};
				startPoints.push(point);
			 }
			 endPoints = startPoints;
			 measurePathLengthstatus(startPoints[1],endPoints[1],getlengthSuccess,startObjs[1],endObjs[1]);
		}
	});
}
function getlengthSuccess(obj){
	var distDuration = obj;
	console.log(obj.startName+"---"+obj.endName+"----"+obj.distance);
	//alert(obj.startName+"---"+obj.endName+"----"+obj.distance);
}


var r = 0;
function measurePathLengthstatus(oriPoint, desPoint,callback,startobj,endobj) {
	r++;
	console.log(r);
	//console.log("ii----"+ii);
	jj++;
	if(ii>sysDepData.length-1)return;
	if(jj>sysDepData.length-1){
		jj=0;
		ii++;
	}
	
	var ori, des;
	if("undefined" == typeof (oriPoint)||"undefined" == typeof (oriPoint)){
		//ii++;
		jj++;
		measurePathLengthstatus(startPoints[ii],endPoints[jj],getlengthSuccess,startObjs[ii],endObjs[jj]);
	}
	if("undefined" == typeof (desPoint)||"undefined" == typeof (desPoint)){
		//ii++;
		jj++;
		measurePathLengthstatus(startPoints[ii],endPoints[jj],getlengthSuccess,startObjs[ii],endObjs[jj]);
	}
	if (typeof (oriPoint.lat) != "undefined"
			&& typeof (oriPoint.lng) != "undefined") {
		ori = new google.maps.LatLng(oriPoint.lat,oriPoint.lng);
	} else {
		ori = oriPoint;
	}
	if (typeof (desPoint.lat) != "undefined"
			&& typeof (desPoint.lng) != "undefined") {
		des = new google.maps.LatLng(desPoint.lat,desPoint.lng);
	} else {
		des = desPoint;
	}
	// 路线服务
	var directionsService = new google.maps.DirectionsService();
	// 自助寻找路径
	var request = {
		origin : ori,
		destination : des,
		optimizeWaypoints : true,// 降低路线的总体成本
		travelMode : google.maps.TravelMode.DRIVING
	};
	 var analysisObj = {};
	 directionsService.route(request, function(response, status) {
		if (status == google.maps.DirectionsStatus.OK) {
			var distance = response.routes[0].legs[0].distance.text;
	        var duration = response.routes[0].legs[0].duration.text;
	        var indexspace = distance.lastIndexOf(' ');
	        distance = distance.substring(0,indexspace);
	        analysisObj.routeDistance = distance;
	        //analysisObj.duration = duration;
	        analysisObj.startId = startobj.organizationId;
	        analysisObj.startName = startobj.organizationName;
	        analysisObj.startLatitude = startobj.latitude;
	        analysisObj.startLongtitude = startobj.longitude
	        analysisObj.endId = endobj.organizationId;
	        analysisObj.endName = endobj.organizationName;
	        analysisObj.endLatitude = endobj.latitude;
	        analysisObj.endLongtitude = endobj.longitude
	       // analysisObj.routeDistance = distance;
	        analysisObj.routeAreaName = analysisObj.startName+"-"+analysisObj.endName;
	        analysisObj.routeAreaType = '0';
	        analysisObj.belongToPort = '0000';
	        analysisObj.routeAreaStatus = '0';
	        analysisObj.routeAreaBuffer = '20';
	        analysisObj.routeCost = duration;
	        var param = JSON.stringify(analysisObj);
	        var myRoute = response.routes[0];
			var jsonPoint = getPlanRouteArray(myRoute);
	        insertBeforePlanRoute(param,jsonPoint);
	        console.log(startobj.organizationName+"---"+endobj.organizationName+"----"+distance);
//	        if(startobj.organizationName==endobj.organizationName){
//	    		jj++;
//	    	}
	        var t= setTimeout( "measurePathLengthstatus(startPoints[ii],endPoints[jj],getlengthSuccess,startObjs[ii],endObjs[jj])",10000);
	       // callback(analysisObj);
		} else {
		    
	  }
   });
	//return analysisObj;
}

/**
 * 预存线路规划
 */
function insertBeforePlanRoute(param,jsonPoint){
//	var aaccUrl = getRootPath() + "systemdepartmenttest/beforPlanRouteArea.action";
//	$.post(aaccUrl,{aceeptData:param,routeAreaPtCol:jsonPoint},function(data){
//		console.log();
//		//alert("add rapoint ")
//	});
	
	var aaccUrl = getRootPath() + "systemdepartmenttest/beforPlanRouteArea.action";
	$.ajax({
		type : "POST",
		url : aaccUrl,
		data : "aceeptData="+param+"&routeAreaPtCol="+jsonPoint,
		dataType : "json",
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(jsonObj) {
			//alert("add sucess");
		}
	});
}



