<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../include/include.jsp" />
<title>Performance Analysis</title>
<style type="text/css">
#portForm .input-group-btn>button {
	height: 30px;
}
</style>
</head>
<body>
<%@ include file="../include/left.jsp" %>
<div class="row site">
	<div class="wrapper-content margint95 margin60">
		<%--导航 --%>
		<c:set var="pageName"><fmt:message key="link.statistic.analysis.elock"/></c:set>
		<jsp:include page="../include/navigation.jsp" >
			<jsp:param value="${pageName }" name="pageName"/>
		</jsp:include>
		
		<div class="col-sm-3 kalb">
			<div class="panel panel-default">
				<div class="panel-heading">
       			  	<div class="pull-right col-sm-8 text-right">
       			  		<form id="portForm" onsubmit="return false;">
        			  		<div class="input-group">
	        			  		<input type="text" class="form-control" id="portName" name="portName" placeholder="<fmt:message key="performance.label.search"/>" onkeyup="treeFilter('tree','portName',this.value)">
        			  			<span class="input-group-btn">
        			  		    	<button class="btn btn-default" type="submit" onclick="treeFilter('tree','portName',document.getElementById('portName').value)">
        			  		       		<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
        			  		       </button>
								</span>
        			  		 </div><!-- /input-group -->
       			  		 </form>
       			  	</div> 	
       			  	<fmt:message key="OrganizationType.Port" />		  	
       			  </div>	        			 
       			  <ul id="tree" class="ztree" style="width:260px; overflow:auto;"></ul>
       		</div>
         </div>
         
           <div class="col-sm-9 right-content">
       				<div class="row row_three margint20 ">
	            		<div class="col-sm-4">
		            			<s:select name="devicetype" 
									emptyOption="true"
									cssClass="form-control" theme="simple"
									list="@com.nuctech.ls.model.util.DeviceType@values()"
									listKey="type"
									listValue="key" 
									value="">
								</s:select>
						</div>
						<div class="col-sm-3">
						<button type="submit" class="btn btn-danger" onclick="filter();"><fmt:message key="common.button.query"/></button>
						</div>
	            		</div>
            		</div>
         
         <div class="col-sm-9 right-content">
       		<div class="row row_three margint20 ">
	            		<div class="col-sm-12">
	            			<div class="panel panel-default">
	            			  <div class="panel-heading heading_ico04"><fmt:message key="warehouse.device.instock"/></div>
	            			  <div class="panel-body" id="deviceInventoryCharts" style="width: 100%;height:550px;overflow-x:hidden;">
		            			  <%--  <img src="${root }/static/images/tongji_03.png" alt=""> --%>
	            			  </div>
	            			</div>
	            		</div>
            		</div>
         </div>
          <div class="col-sm-9 right-content" id="elockDetail">
     			<div class="row row_three margint20 ">
           		<div class="search_table col-sm-12">
					<div>
						<table id="detailTable" >
						</table>
					</div>
				</div>
           		</div>
            </div>
            
            <div class="col-sm-9 right-content" id="esealDetail">
     			<div class="row row_three margint20 ">
           		<div class="search_table col-sm-12">
					<div>
						<table id="esealDetailTable" >
						</table>
					</div>
				</div>
           		</div>
            </div>
            
             <div class="col-sm-9 right-content" id="sensorDetail">
     			<div class="row row_three margint20 ">
           		<div class="search_table col-sm-12">
					<div>
						<table id="sensorDetailTable" >
						</table>
					</div>
				</div>
           		</div>
            </div>
	</div>
</div>	       

<script type="text/javascript" src="${root}/static/zTree_v3/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${root}/static/zTree_v3/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="${root}/static/zTree_v3/js/jquery.ztree.exhide.js"></script>

<script type="text/javascript" src="${root}/static/echarts/echarts.min.js"></script>

<script src="${root}/static/moment/moment-with-locales.min.js"></script>
<script type="text/javascript">



//树
var zNodes = ${deptArr};
Array.prototype.contains = function(item){
	for(var v in this){
		if(item==this[v]){
			return true;
		}
	}
    return false;
};

var zTree;
var demoIframe;

var setting = {
	check: {
		enable: true
	},
	view: {
		dblClickExpand: false,
		showLine: true,
		selectedMulti: false,
		fontCss: getFontCss
	},
	data: {
		key: {
			name: "portName",
			title: "ggg",
			icon: "icon"
		},
		simpleData: {
			enable:true,
			idKey: "portId",
			pIdKey: "parentId",
			rootPId: ""
		}
	},
	callback: {
		beforeClick: function(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("tree");
			if (treeNode.isParent) {
				zTree.expandNode(treeNode);
				return false;
			} else {
				//demoIframe.attr("src",treeNode.file + ".html");
				return true;
			}
		},
		onCheck : function(event, treeId, treeNode){
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			var nodes = treeObj.getCheckedNodes(true);
			var sNodeArr = [];
			for(var i=0;i<nodes.length;i++){
				sNodeArr.push(nodes[i].portId);
			}
			chartPaint(arr,sNodeArr);
		    $("#detailTable").bootstrapTable('destroy'); //清除关锁表格
			$("#esealDetailTable").bootstrapTable('destroy');  //清除子锁表格
			$("#sensorDetailTable").bootstrapTable('destroy');  //清除传感器表格
		}
	}
};
//初始化
$(document).ready(function(){
	var t = $("#tree");
	t = $.fn.zTree.init(t, setting, zNodes);
	chartPaint(arr,[]);
});

function getFontCss(treeId, treeNode) {  
    return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};  
}

//树搜索
var nodeList = zNodes;
function treeFilter(id,key,value){  
  treeId = id;
  if(value!=""){
	  var treeObj = $.fn.zTree.getZTreeObj(treeId);
	  var nodes = treeObj.getNodesByParamFuzzy(key, value);
	  var ids = [];
	  for(var i=0;i<nodes.length;i++){
		  var node = nodes[i];
		  ids.push(node.portId);
		  while(node.level!=0){
			 
			  node = treeObj.getNodeByTId(node.parentTId);
			  if(!ids.contains(node.portId)){
				  ids.push(node.portId);
			  }
		  }
	  }
	
	  for(var i=0;i<nodeList.length;i++){
		  var json = nodeList[i];
		 
		  if(ids.contains(json.portId)){
			  //json.highlight = true;
			   
			  json.isHidden = false;
		  }else{
			  //json.highlight = false;
			  json.isHidden = true;
		  }
	  }
	  updateNodes(nodeList);
  }else{
	  for(var i=0;i<nodeList.length;i++){
		  var json = nodeList[i];
		  //json.highlight = false;
		  json.isHidden = false;
	  }
	  updateNodes(nodeList);
  }
    
}  
function updateNodes(nodeList) {  
  var t = $("#tree");
  t = $.fn.zTree.init(t, setting, nodeList);
}

</script>
<script type="text/javascript">
	    
    	var arr = ${deptArr};
    	
    	//图表
    	function chartPaint(dat,containArr){
    		var deviceType = $("#devicetype").val();
    		var deptarr = []; //口岸
        	var keyongs = []; //可用关锁
        	var sunhuais = [] //损坏关锁
        	var baofeis = [] //报废关锁
        	var zaitus = [] //在途关锁
        	var weixius = [] //维修关锁
        	
        	var ekeyongs = []; //可用子锁
        	var esunhuais = [] //损坏子锁
        	var ebaofeis = [] //报废子锁
        	var ezaitus = [] //在途子锁
        	var eweixius = [] //维修子锁
        	
        	var skeyongs = []; //可用传感器
        	var ssunhuais = [] //损坏传感器
        	var sbaofeis = [] //报废传感器
        	var szaitus = [] //在途传感器
        	var sweixius = [] //维修传感器
        if(deviceType==""){
        	for(var i=0;i<dat.length;i++){
        		var json = dat[i];
        		if(containArr.length==0 || containArr.contains(json.portId)){
        			deptarr.push(json.portName);
        			keyongs.push(json.keyongs);
        			sunhuais.push(json.sunhuais);
        			baofeis.push(json.baofeis);
        			zaitus.push(json.zaitus);
        			weixius.push(json.weixius);
        			//子锁
        			ekeyongs.push(json.ekeyongs);
        			esunhuais.push(json.esunhuais);
        			ebaofeis.push(json.ebaofeis);
        			ezaitus.push(json.ezaitus);
        			eweixius.push(json.eweixius);
        			//传感器
        			skeyongs.push(json.skeyongs);
        			ssunhuais.push(json.ssunhuais);
        			sbaofeis.push(json.sbaofeis);
        			szaitus.push(json.szaitus);
        			sweixius.push(json.sweixius);
        		}
        		
        	}
            // 基于准备好的dom，初始化echarts实例
            var timeChart = echarts.init(document.getElementById('deviceInventoryCharts'));
            // 指定图表的配置项和数据-在线时长
            var timeOption = {
                title: {
                    text: ''
                },
                tooltip: {
                	trigger: 'axis',//axis  item
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    },
                },
                grid: {
					left: '3%',
					right: '2%',
					bottom: 80,
					containLabel: true
				},
                legend: {
                	itemHeight:10,
                    data:['<fmt:message key="statistic.elock.keyong" />','<fmt:message key="statistic.elock.zaitu" />','<fmt:message key="statistic.elock.sunhuai" />','<fmt:message key="statistic.elock.weixiu" />','<fmt:message key="statistic.elock.baofei" />'
                      ,'<fmt:message key="statistic.eseal.keyong" />','<fmt:message key="statistic.eseal.zaitu" />','<fmt:message key="statistic.eseal.sunhuai" />','<fmt:message key="statistic.eseal.weixiu" />','<fmt:message key="statistic.eseal.baofei" />'    
                      ,'<fmt:message key="statistic.sensor.keyong" />','<fmt:message key="statistic.sensor.zaitu" />','<fmt:message key="statistic.sensor.sunhuai" />','<fmt:message key="statistic.sensor.weixiu" />','<fmt:message key="statistic.sensor.baofei" />']
                },
                xAxis: {
                    data: deptarr,
                    axisLabel : {
                    	interval: 0,
	                    rotate: 35
                    }
                },
                yAxis: {},
                series: [{
                    name: '<fmt:message key="statistic.elock.keyong" />',
                    type: 'bar',
                    data: keyongs
                },{
                    name: '<fmt:message key="statistic.elock.zaitu" />',
                    type: 'bar',
                    data: zaitus
                },{
                    name: '<fmt:message key="statistic.elock.sunhuai" />',
                    type: 'bar',
                    data: sunhuais
                },{
                    name: '<fmt:message key="statistic.elock.weixiu" />',
                    type: 'bar',
                    data: weixius
                },{
                    name: '<fmt:message key="statistic.elock.baofei" />',
                    type: 'bar',
                    data: baofeis
                }, {
                    name: '<fmt:message key="statistic.eseal.keyong" />',
                    type: 'bar',
                    data: ekeyongs
                },{
                    name: '<fmt:message key="statistic.eseal.zaitu" />',
                    type: 'bar',
                    data: ezaitus
                },{
                    name: '<fmt:message key="statistic.eseal.sunhuai" />',
                    type: 'bar',
                    data: esunhuais
                },{
                    name: '<fmt:message key="statistic.eseal.weixiu" />',
                    type: 'bar',
                    data: eweixius
                },{
                    name: '<fmt:message key="statistic.eseal.baofei" />',
                    type: 'bar',
                    data: ebaofeis
                }, {
                    name: '<fmt:message key="statistic.sensor.keyong" />',
                    type: 'bar',
                    data: skeyongs
                },{
                    name: '<fmt:message key="statistic.sensor.zaitu" />',
                    type: 'bar',
                    data: szaitus
                },{
                    name: '<fmt:message key="statistic.sensor.sunhuai" />',
                    type: 'bar',
                    data: ssunhuais
                },{
                    name: '<fmt:message key="statistic.sensor.weixiu" />',
                    type: 'bar',
                    data: sweixius
                },{
                    name: '<fmt:message key="statistic.sensor.baofei" />',
                    type: 'bar',
                    data: sbaofeis
                }],
            };
            // 显示图表。
            timeChart.setOption(timeOption);
            //点击柱状图响应事件
            timeChart.on('click',function(params){
            		showTable(params);
            });
        } 
        	
       	 if(deviceType=="TRACKING_DEVICE"){
       		var deptarr = []; //口岸
        	var keyongs = []; //可用关锁
        	var sunhuais = [] //损坏关锁
        	var baofeis = [] //报废关锁
        	var zaitus = [] //在途关锁
        	var weixius = [] //维修关锁
        	for(var i=0;i<arr.length;i++){
        		var json = arr[i];
        		if(containArr.length==0 || containArr.contains(json.portId)){
        			deptarr.push(json.portName);
        			keyongs.push(json.keyongs);
        			sunhuais.push(json.sunhuais);
        			baofeis.push(json.baofeis);
        			zaitus.push(json.zaitus);
        			weixius.push(json.weixius);
        		}
        	}
            // 基于准备好的dom，初始化echarts实例
            var timeChart = echarts.init(document.getElementById('deviceInventoryCharts'));
            // 指定图表的配置项和数据-在线时长
            var timeOption = {
                title: {
                    text: ''
                },
                tooltip: {
                	trigger: 'axis',//axis  item
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    },
                },
                grid: {
					left: '3%',
					right: '2%',
					bottom: 80,
					containLabel: true
				},
                legend: {
                    data:['<fmt:message key="statistic.elock.keyong" />','<fmt:message key="statistic.elock.zaitu" />','<fmt:message key="statistic.elock.sunhuai" />','<fmt:message key="statistic.elock.weixiu" />','<fmt:message key="statistic.elock.baofei" />']
                },
                xAxis: {
                    data: deptarr,
                    axisLabel : {
                    	interval: 0,
	                    rotate: 35
                    }
                },
                yAxis: {},
                series: [{
                    name: '<fmt:message key="statistic.elock.keyong" />',
                    type: 'bar',
                    data: keyongs
                },{
                    name: '<fmt:message key="statistic.elock.zaitu" />',
                    type: 'bar',
                    data: zaitus
                },{
                    name: '<fmt:message key="statistic.elock.sunhuai" />',
                    type: 'bar',
                    data: sunhuais
                },{
                    name: '<fmt:message key="statistic.elock.weixiu" />',
                    type: 'bar',
                    data: weixius
                },{
                    name: '<fmt:message key="statistic.elock.baofei" />',
                    type: 'bar',
                    data: baofeis
                }]
            };
            // 显示图表。
            timeChart.setOption(timeOption);
            timeChart.on('click',function(params){
        		showTable(params);
       		 });
       	 }
       	 
       	 if(deviceType=="ESEAL"){
       		var deptarr = []; //口岸
        	var ekeyongs = []; //可用子锁
        	var esunhuais = [] //损坏子锁
        	var ebaofeis = [] //报废子锁
        	var ezaitus = [] //在途子锁
        	var eweixius = [] //维修子锁
        	
        	for(var i=0;i<arr.length;i++){
        		var json = arr[i];
        		if(containArr.length==0 || containArr.contains(json.portId)){
        			deptarr.push(json.portName);
        			//子锁
        			ekeyongs.push(json.ekeyongs);
        			esunhuais.push(json.esunhuais);
        			ebaofeis.push(json.ebaofeis);
        			ezaitus.push(json.ezaitus);
        			eweixius.push(json.eweixius);
        		}
        	}
            // 基于准备好的dom，初始化echarts实例
            var timeChart = echarts.init(document.getElementById('deviceInventoryCharts'));
            // 指定图表的配置项和数据-在线时长
            var timeOption = {
                title: {
                    text: ''
                },
                tooltip: {
                	trigger: 'axis',//axis  item
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    },
                },
                grid: {
					left: '3%',
					right: '2%',
					bottom: 80,
					containLabel: true
				},
                legend: {
                    data:['<fmt:message key="statistic.eseal.keyong" />','<fmt:message key="statistic.eseal.zaitu" />','<fmt:message key="statistic.eseal.sunhuai" />','<fmt:message key="statistic.eseal.weixiu" />','<fmt:message key="statistic.eseal.baofei" />']
                },
                xAxis: {
                    data: deptarr,
                    axisLabel : {
                    	interval: 0,
	                    rotate: 35
                    }
                },
                yAxis: {},
                series: [{
                    name: '<fmt:message key="statistic.eseal.keyong" />',
                    type: 'bar',
                    data: ekeyongs
                },{
                    name: '<fmt:message key="statistic.eseal.zaitu" />',
                    type: 'bar',
                    data: ezaitus
                },{
                    name: '<fmt:message key="statistic.eseal.sunhuai" />',
                    type: 'bar',
                    data: esunhuais
                },{
                    name: '<fmt:message key="statistic.eseal.weixiu" />',
                    type: 'bar',
                    data: eweixius
                },{
                    name: '<fmt:message key="statistic.eseal.baofei" />',
                    type: 'bar',
                    data: ebaofeis
                }]
            };
            // 显示图表。
            timeChart.setOption(timeOption);
            timeChart.on('click',function(params){
        		showTable(params);
       		 });
       	 }
       	if(deviceType=="SENSOR"){
    		var deptarr = []; //口岸
        	var skeyongs = []; //可用传感器
        	var ssunhuais = [] //损坏传感器
        	var sbaofeis = [] //报废传感器
        	var szaitus = [] //在途传感器
        	var sweixius = [] //维修传感器
        	for(var i=0;i<arr.length;i++){
        		var json = arr[i];
        		if(containArr.length==0 || containArr.contains(json.portId)){
        			deptarr.push(json.portName);
        			//传感器
        			skeyongs.push(json.skeyongs);
        			ssunhuais.push(json.ssunhuais);
        			sbaofeis.push(json.sbaofeis);
        			szaitus.push(json.szaitus);
        			sweixius.push(json.sweixius);
        		}
        	}
            // 基于准备好的dom，初始化echarts实例
            var timeChart = echarts.init(document.getElementById('deviceInventoryCharts'));
            // 指定图表的配置项和数据-在线时长
            var timeOption = {
                title: {
                    text: ''
                },
                tooltip: {
                	trigger: 'axis',//axis  item
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    },
                },
                grid: {
					left: '3%',
					right: '2%',
					bottom: 80,
					containLabel: true
				},
                legend: {
                    data:['<fmt:message key="statistic.sensor.keyong" />','<fmt:message key="statistic.sensor.zaitu" />','<fmt:message key="statistic.sensor.sunhuai" />','<fmt:message key="statistic.sensor.weixiu" />','<fmt:message key="statistic.sensor.baofei" />']
                },
                xAxis: {
                    data: deptarr,
                    axisLabel : {
                    	interval: 0,
	                    rotate: 35
                    }
                },
                yAxis: {},
                series: [{
                    name: '<fmt:message key="statistic.sensor.keyong" />',
                    type: 'bar',
                    data: skeyongs
                },{
                    name: '<fmt:message key="statistic.sensor.zaitu" />',
                    type: 'bar',
                    data: szaitus
                },{
                    name: '<fmt:message key="statistic.sensor.sunhuai" />',
                    type: 'bar',
                    data: ssunhuais
                },{
                    name: '<fmt:message key="statistic.sensor.weixiu" />',
                    type: 'bar',
                    data: sweixius
                },{
                    name: '<fmt:message key="statistic.sensor.baofei" />',
                    type: 'bar',
                    data: sbaofeis
                }]
            };
            // 显示图表。
            timeChart.setOption(timeOption);
            timeChart.on('click',function(params){
        		showTable(params);
        	});
    		}
    	}	
    	//根据设备类型过滤柱状图
    function filter1(){
    	var treeObj = $.fn.zTree.getZTreeObj("#tree");
		var nodes = treeObj.getCheckedNodes(true);
		var sNodeArr = [];
		for(var i=0;i<nodes.length;i++){
			sNodeArr.push(nodes[i].portId);
		}
		chartPaint(arr,sNodeArr);
    }
    function filter(){
    	var deviceType = $("#devicetype").val();
    	//获取被选中的树节点
    	var treeObj = $.fn.zTree.getZTreeObj("tree");
		var nodes = treeObj.getCheckedNodes(true);
		var sNodeArr = [];
		for(var i=0;i<nodes.length;i++){
			sNodeArr.push(nodes[i].portId);
		}
		
    	if(deviceType=="TRACKING_DEVICE"){
    		var deptarr = []; //口岸
        	var keyongs = []; //可用关锁
        	var sunhuais = [] //损坏关锁
        	var baofeis = [] //报废关锁
        	var zaitus = [] //在途关锁
        	var weixius = [] //维修关锁
        	
    		if(sNodeArr.length > 0){//选中了口岸树的节点
    			for(var i=0;i<arr.length;i++){
    				for (var j = 0; j < sNodeArr.length; j++) {
						if(arr[i].portId.trim() == sNodeArr[j].trim()){
							var json1 = arr[i];
	            			deptarr.push(json1.portName);
	            			keyongs.push(json1.keyongs);
	            			sunhuais.push(json1.sunhuais);
	            			baofeis.push(json1.baofeis);
	            			zaitus.push(json1.zaitus);
	            			weixius.push(json1.weixius);
						}
					}
    			}
    		}else{//没有选中口岸树的任何节点
    			for(var i=0;i<arr.length;i++){
            		var json = arr[i];
            			deptarr.push(json.portName);
            			keyongs.push(json.keyongs);
            			sunhuais.push(json.sunhuais);
            			baofeis.push(json.baofeis);
            			zaitus.push(json.zaitus);
            			weixius.push(json.weixius);
            		
            	}
    		}
        	
            // 基于准备好的dom，初始化echarts实例
            var timeChart = echarts.init(document.getElementById('deviceInventoryCharts'));
            // 指定图表的配置项和数据-在线时长
            var timeOption = {
                title: {
                    text: ''
                },
                tooltip: {
                	trigger: 'axis',//axis  item
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    },
                },
                grid: {
					left: '3%',
					right: '2%',
					bottom: 80,
					containLabel: true
				},
                legend: {
                    data:['<fmt:message key="statistic.elock.keyong" />','<fmt:message key="statistic.elock.zaitu" />','<fmt:message key="statistic.elock.sunhuai" />','<fmt:message key="statistic.elock.weixiu" />','<fmt:message key="statistic.elock.baofei" />']
                },
                xAxis: {
                    data: deptarr,
                    axisLabel : {
                    	interval: 0,
	                    rotate: 35
                    }
                },
                yAxis: {},
                series: [{
                    name: '<fmt:message key="statistic.elock.keyong" />',
                    type: 'bar',
                    data: keyongs
                },{
                    name: '<fmt:message key="statistic.elock.zaitu" />',
                    type: 'bar',
                    data: zaitus
                },{
                    name: '<fmt:message key="statistic.elock.sunhuai" />',
                    type: 'bar',
                    data: sunhuais
                },{
                    name: '<fmt:message key="statistic.elock.weixiu" />',
                    type: 'bar',
                    data: weixius
                },{
                    name: '<fmt:message key="statistic.elock.baofei" />',
                    type: 'bar',
                    data: baofeis
                }]
            };
            // 显示图表。
            timeChart.setOption(timeOption);
            timeChart.on('click',function(params){
        		showTable(params);
       		 });
            
            //只显示关锁
            $("#elockDetail").show();
			$("#esealDetail").hide();
			$("#sensorDetail").hide();
    		}
    		//子锁
	    	if(deviceType=="ESEAL"){
	    		var deptarr = []; //口岸
	        	var ekeyongs = []; //可用子锁
	        	var esunhuais = [] //损坏子锁
	        	var ebaofeis = [] //报废子锁
	        	var ezaitus = [] //在途子锁
	        	var eweixius = [] //维修子锁
	        	
	        	if(sNodeArr.length > 0){//选中了口岸树的节点
	        		for(var i=0;i<arr.length;i++){
	    				for (var j = 0; j < sNodeArr.length; j++) {
							if(arr[i].portId.trim() == sNodeArr[j].trim()){
								var json1 = arr[i];
			        			deptarr.push(json1.portName);
			        			//子锁
			        			ekeyongs.push(json1.ekeyongs);
			        			esunhuais.push(json1.esunhuais);
			        			ebaofeis.push(json1.ebaofeis);
			        			ezaitus.push(json1.ezaitus);
			        			eweixius.push(json1.eweixius);
							}
						}
	    			}
	        	}else{//没有选中口岸树的任何节点
	        		for(var i=0;i<arr.length;i++){
		        		var json = arr[i];
	        			deptarr.push(json.portName);
	        			//子锁
	        			ekeyongs.push(json.ekeyongs);
	        			esunhuais.push(json.esunhuais);
	        			ebaofeis.push(json.ebaofeis);
	        			ezaitus.push(json.ezaitus);
	        			eweixius.push(json.eweixius);
		        	}
	        	}
	        	
	            // 基于准备好的dom，初始化echarts实例
	            var timeChart = echarts.init(document.getElementById('deviceInventoryCharts'));
	            // 指定图表的配置项和数据-在线时长
	            var timeOption = {
	                title: {
	                    text: ''
	                },
	                tooltip: {
	                	trigger: 'axis',//axis  item
	                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	                    },
	                },
	                grid: {
						left: '3%',
						right: '2%',
						bottom: 80,
						containLabel: true
					},
	                legend: {
	                    data:['<fmt:message key="statistic.eseal.keyong" />','<fmt:message key="statistic.eseal.zaitu" />','<fmt:message key="statistic.eseal.sunhuai" />','<fmt:message key="statistic.eseal.weixiu" />','<fmt:message key="statistic.eseal.baofei" />']
	                },
	                xAxis: {
	                    data: deptarr,
	                    axisLabel : {
	                    	interval: 0,
		                    rotate: 35
	                    }
	                },
	                yAxis: {},
	                series: [{
	                    name: '<fmt:message key="statistic.eseal.keyong" />',
	                    type: 'bar',
	                    data: ekeyongs
	                },{
	                    name: '<fmt:message key="statistic.eseal.zaitu" />',
	                    type: 'bar',
	                    data: ezaitus
	                },{
	                    name: '<fmt:message key="statistic.eseal.sunhuai" />',
	                    type: 'bar',
	                    data: esunhuais
	                },{
	                    name: '<fmt:message key="statistic.eseal.weixiu" />',
	                    type: 'bar',
	                    data: eweixius
	                },{
	                    name: '<fmt:message key="statistic.eseal.baofei" />',
	                    type: 'bar',
	                    data: ebaofeis
	                }]
	            };
	            // 显示图表。
	            timeChart.setOption(timeOption);
	            timeChart.on('click',function(params){
            		showTable(params);
           		 });
	            
	            //只显示子锁
	            $("#esealDetail").show();
				$("#elockDetail").hide();
				$("#sensorDetail").hide();
	    	}
    		//传感器
	    	if(deviceType=="SENSOR"){
	    		var deptarr = []; //口岸
	        	var skeyongs = []; //可用传感器
	        	var ssunhuais = [] //损坏传感器
	        	var sbaofeis = [] //报废传感器
	        	var szaitus = [] //在途传感器
	        	var sweixius = [] //维修传感器
	        	if(sNodeArr.length > 0){//选中了口岸树的节点
	        		for(var i=0;i<arr.length;i++){
	    				for (var j = 0; j < sNodeArr.length; j++) {
							if(arr[i].portId.trim() == sNodeArr[j].trim()){
								var json1 = arr[i];
			        			deptarr.push(json1.portName);
			        			//传感器
			        			skeyongs.push(json1.skeyongs);
			        			ssunhuais.push(json1.ssunhuais);
			        			sbaofeis.push(json1.sbaofeis);
			        			szaitus.push(json1.szaitus);
			        			sweixius.push(json1.sweixius);
							}
						}
	    			}
	        	}else{//没有选中口岸树的任何节点
	        		for(var i=0;i<arr.length;i++){
		        		var json = arr[i];
	        			deptarr.push(json.portName);
	        			//传感器
	        			skeyongs.push(json.skeyongs);
	        			ssunhuais.push(json.ssunhuais);
	        			sbaofeis.push(json.sbaofeis);
	        			szaitus.push(json.szaitus);
	        			sweixius.push(json.sweixius);
		        	}
	        	}
	        	
	            // 基于准备好的dom，初始化echarts实例
	            var timeChart = echarts.init(document.getElementById('deviceInventoryCharts'));
	            // 指定图表的配置项和数据-在线时长
	            var timeOption = {
	                title: {
	                    text: ''
	                },
	                tooltip: {
	                	trigger: 'axis',//axis  item
	                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	                    },
	                },
	                grid: {
						left: '3%',
						right: '2%',
						bottom: 80,
						containLabel: true
					},
	                legend: {
	                    data:['<fmt:message key="statistic.sensor.keyong" />','<fmt:message key="statistic.sensor.zaitu" />','<fmt:message key="statistic.sensor.sunhuai" />','<fmt:message key="statistic.sensor.weixiu" />','<fmt:message key="statistic.sensor.baofei" />']
	                },
	                xAxis: {
	                    data: deptarr,
	                    axisLabel : {
	                    	interval: 0,
		                    rotate: 35
	                    }
	                },
	                yAxis: {},
	                series: [{
	                    name: '<fmt:message key="statistic.sensor.keyong" />',
	                    type: 'bar',
	                    data: skeyongs
	                },{
	                    name: '<fmt:message key="statistic.sensor.zaitu" />',
	                    type: 'bar',
	                    data: szaitus
	                },{
	                    name: '<fmt:message key="statistic.sensor.sunhuai" />',
	                    type: 'bar',
	                    data: ssunhuais
	                },{
	                    name: '<fmt:message key="statistic.sensor.weixiu" />',
	                    type: 'bar',
	                    data: sweixius
	                },{
	                    name: '<fmt:message key="statistic.sensor.baofei" />',
	                    type: 'bar',
	                    data: sbaofeis
	                }]
	            };
	            // 显示图表。
	            timeChart.setOption(timeOption);
	            timeChart.on('click',function(params){
            		showTable(params);
          		 });
	            
	            //只显示传感器
	            $("#sensorDetail").show();
				$("#elockDetail").hide();
				$("#esealDetail").hide();
	    	}
	    	if(deviceType==""){
	    		chartPaint(arr,sNodeArr);
	    		$("#sensorDetail").show();
				$("#elockDetail").show();
				$("#esealDetail").show();
	    	}
    	}	
</script>


<!-- 点击柱状图显示每个口岸的设备的具体情况 -->
<script type="text/javascript">
	function showTable(params){
		//关锁
		var keyongElock = $.i18n.prop("statistic.elock.keyong");
		var zaituElock = $.i18n.prop("statistic.elock.zaitu");
		var sunhuaiElock = $.i18n.prop("statistic.elock.sunhuai");
		var weixiuElock = $.i18n.prop("statistic.elock.weixiu");
		var baofeiElock = $.i18n.prop("statistic.elock.baofei");
		//子锁
		var keyongEseal = $.i18n.prop("statistic.eseal.keyong");
		var zaituEseal = $.i18n.prop("statistic.eseal.zaitu");
		var sunhuaiEseal = $.i18n.prop("statistic.eseal.sunhuai");
		var weixiuEseal = $.i18n.prop("statistic.eseal.weixiu");
		var baofeiEseal = $.i18n.prop("statistic.eseal.baofei");
		//传感器
		var keyongSensor = $.i18n.prop("statistic.sensor.keyong");
		var zaituSensor = $.i18n.prop("statistic.sensor.zaitu");
		var sunhuaiSensor = $.i18n.prop("statistic.sensor.sunhuai");
		var weixiuSensor = $.i18n.prop("statistic.sensor.weixiu");
		var baofeiSensor = $.i18n.prop("statistic.sensor.baofei");
		var root = "${root}";
		var object = params;
		var portName = object.name;
		var seriesName = object.seriesName;
		var deviceType = $("#devicetype").val();
		//如果无过滤条件，点击口岸显示所有设备的库存信息(如，点击柱状图名称为"可用关锁"，列表只显示关锁中状态为"可用的")
		if(deviceType==""){
			//若点击的bar为关锁，只显示关锁的库存信息
			if(seriesName==keyongElock||seriesName==zaituElock||seriesName==sunhuaiElock||seriesName==weixiuElock||seriesName==baofeiElock){
				//关锁库存详细信息
				elockDetail(portName,seriesName);
				$("#elockDetail").show();
				$("#esealDetail").hide();
				$("#sensorDetail").hide();
			}
			
			if(seriesName==keyongEseal||seriesName==zaituEseal||seriesName==sunhuaiEseal||seriesName==weixiuEseal||seriesName==baofeiEseal){
				//子锁库存详细信息
				esealDetail(portName,seriesName);
				$("#esealDetail").show();
				$("#elockDetail").hide();
				$("#sensorDetail").hide();
			}
			
			if(seriesName==keyongSensor||seriesName==zaituSensor||seriesName==sunhuaiSensor||seriesName==weixiuSensor||seriesName==baofeiSensor){
				//传感器库存详细信息
				sensorDeatail(portName,seriesName);
				$("#sensorDetail").show();
				$("#elockDetail").hide();
				$("#esealDetail").hide();
			}
		}
		//如果查询条件为"关锁"，点击口岸刷新关锁的库存信息，并隐藏子锁和传感器的信息
		if(deviceType=="TRACKING_DEVICE"){
			//关锁库存详细信息
			elockDetail(portName,seriesName);
			$("#elockDetail").show();
			$("#esealDetail").hide();
			$("#sensorDetail").hide();
		}
		//如果查询条件为"子锁"，点击口岸刷新子锁的库存信息，并隐藏关锁和传感器的信息
		if(deviceType=="ESEAL"){
			//子锁库存详细信息
			esealDetail(portName,seriesName);
			$("#esealDetail").show();
			$("#elockDetail").hide();
			$("#sensorDetail").hide();
			
		}
		//如果查询条件为"传感器"，点击口岸刷新传感器的库存信息，并隐藏子锁和关锁的信息
		if(deviceType=="SENSOR"){
			//传感器库存详细信息
			sensorDeatail(portName,seriesName);
			$("#sensorDetail").show();
			$("#elockDetail").hide();
			$("#esealDetail").hide();
		}
	}
</script>
<script type="text/javascript" src="${root}/statistics/js/elockStatistic.js"></script>
</body>
</html>