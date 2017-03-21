<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${root}/static/bootstrap-slider/css/bootstrap-slider.min.css">
<jsp:include page="../include/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${root}/static/bootstrap-slider/bootstrap-slider.min.js"></script>
<script src="${root}/static/bootstrap-table/bootstrap-table.js"></script>

<script src="${root}/static/jspdf/jspdf.js"></script>
<script src="${root}/static/bootstrap-table/tableExport.js"></script>
<script src="${root}/static/bootstrap-table/extensions/export/bootstrap-table-export.js"></script>

<title>Dynamic Report</title>
<style type="text/css">
.in.modal-backdrop{
	/* z-index: 1100; */
}

.tooltip-main{
	z-index: 1;
}

</style>
</head>
<body>
<%@ include file="../include/left.jsp" %>
<div class="row site">
	<div class="wrapper-content margint95 margin60">  
	<div class="col-sm-3 kalb">
			<div class="panel panel-default">
				<div class="panel-heading">
       			  	<div class="pull-right col-sm-8 text-right">
       			  		<form id="portForm" onsubmit="return false;">
        			  		<div class="input-group">
	        			  		<input type="text" class="form-control" id="name" name="name" placeholder="<fmt:message key="performance.label.search"/>" onkeyup="treeFilter('tree','name',this.value)">
        			  			<span class="input-group-btn">
        			  		    	<button class="btn btn-default" type="submit" onclick="treeFilter('tree','name',document.getElementById('name').value)">
        			  		       		<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
        			  		       </button>
								</span>
        			  		 </div><!-- /input-group -->
       			  		 </form>
       			  	</div> 	
       			  	<fmt:message key="statistic.dynamic.dimtheme"/>
       			  </div>	        			 
       			  <ul id="tree" class="ztree" style="width:260px; overflow:auto;"></ul>
       		</div>
         </div> 
        <div class="col-sm-9 right-content">
       		 <!-- 查询条件 -->
        	<div class="tab-content m-b">
			  <div class="tab-cotent-title search_title"></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="ElockForm" action=""
				onsubmit="return false;">
				<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="statistic.dynamic.time"/></label>
							<div class="input-group date col-sm-4" id="form_alarmStartTime">
								<input type="text" class="form-control" id="s_timeStart" name="s_timeStart" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
							<label class="col-sm-1 control-label" style="text-align: center;">-</label>
							<div class="input-group date col-sm-4" id="form_alarmEndTime">
								<input type="text" class="form-control" id="s_timeEnd" name="s_timeEnd" readonly>
								<input type="text" hidden="hidden" name="s_timeFormat" value="yyyy-MM-dd HH:mm:ss">
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
				</div>
				<div class="clearfix"></div>
				<div class="form-group">
					<div class="col-sm-offset-9 col-md-3">
						<button type="submit" class="btn btn-danger" onclick="doSearch();"><fmt:message key="common.button.query"/></button>
						<button  type="reset"  class="btn btn-darch">
							<fmt:message key="common.button.reset" />
						</button>
					</div>
				</div>
			</form>
				</div>
			</div>
			<!--/search form-->
			<!--my result-->
			<div class="tab-content">
			  <div class="tab-cotent-title">
			  	<div class="Features pull-right">
					<ul>
						<li><a id="addElockBtn" class="btn btn-info">报表定制</a></li>
						<li><a id="editElockBtn" class="btn btn-info excel_exp">导出Excel</a></li>
						<li><a id="editElockBtn" class="btn btn-info word_exp">导出Word</a></li>
						
					</ul>
				</div>
				<fmt:message key="statistic.dynamic.result"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="table">	
						</table>
					</div>
				</div>
			</div>
		</div>
        <div class="clearfix"></div>
	</div>
</div>
<script type="text/javascript">
//构建树
//树
var zNodes = ${theme};//树结构数据
var themeRequest="";//主题的请求链接
var dimensionArr=[];//维度
Array.prototype.contains = function(item){
	for(var v in this){
		if(item==this[v]){
			return true;
		}
	}
    return false;
};
var $table = $('#table');
var zTree;
var requestUrl="";//报表的url
var curTableID="";//当前table表的ID
var curRequestNode;
var preFlag="_mutilple_select_";

var rowheaders=[];//复合表头
var setting = {
	check: {
		enable: true,
		chkStyle:"checkbox"
	},
	view: {
		dblClickExpand: false,
		showLine: true,
		selectedMulti: false,
		fontCss: getFontCss
	},
	data: {
		key: {
			name: "name",
			title: "name",
			icon: "icon"
		},
		simpleData: {
			enable:true,
			idKey: "id",
			pIdKey: "pid",
			rootPId: ""
		}
	},
	callback: {
		beforeClick: function(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("tree");
			if (treeNode.isParent) {
				if(treeNode.id==curTableID){
					zTree.expandNode(treeNode);
					return true;
				}
				if(""==curTableID){
					zTree.expandNode(treeNode);
					return true;
				}
				if(treeNode.id!=curTableID && !treeNode.open){
					zTree.expandNode(treeNode);
					return true;
				}
				
				return true;
			} else {
				//demoIframe.attr("src",treeNode.file + ".html");
				return false;
			}
		},
		onClick : function(event, treeId, treeNode){
			onclick(treeNode);
		}
		,onCheck:function(event, treeId, treeNode){
			if(treeNode.pid!=curTableID){
				bootbox.error("<fmt:message key="statistic.dynamic.alert"/>");
				return false;
			}
			
			dimesions=treeNode.columns;
			if(treeNode.checked&& treeNode.level==2){
				var dim=[];
				for(var i=0;i<rowheaders.length;i++){
					if(treeNode.columns[0]!=rowheaders[i]){
						dim.push(rowheaders[i]);
					}
				}
				rowheaders=dim;
			}else{
				rowheaders.push(treeNode.columns[0]);
			}
		
			toggleColumn(treeNode);
			return true;
			
		},
		onNodeCreated:function(event, treeId, treeNode){
			var zTree = $.fn.zTree.getZTreeObj("tree");
			
			if(treeNode.isParent){
				treeNode.nocheck=true;
				zTree.updateNode(treeNode);
				return false;
			}else{
				return true;
			}
			treeNode.name=$.i18n.prop(treeNode.name);
			zTree.updateNode(treeNode);
		}
	}
};
function onclick(treeNode){
	var zTree = $.fn.zTree.getZTreeObj("tree");
	if(treeNode.level==1){
		requestUrl=treeNode.requestUrl;
		zTree.checkAllNodes(false);
		checkedAllChild(treeNode,zTree);
		curTableID=treeNode.id;
		curRequestNode=treeNode;
		replaceSearch(treeNode);
		rowheaders=[];
		doSearch();
	}else{
		return false;
	}
}
//设置查询条件
function replaceSearch(node){
	$(".search_title").text(node.name);
}
//根据树节点的设置，显示/隐藏table列
function toggleColumn(treeNode){
	if(treeNode.level==2){
		for(var i=0;i<treeNode.columns.length;i++){
			if(treeNode.checked){
			 	$table.bootstrapTable("showColumn",treeNode.columns[i]);
			}
			 else{
				 $table.bootstrapTable("hideColumn",treeNode.columns[i]); 
			 }
		}
		
		//隐藏//复合表头代码//暂无效
		for(var i=0;i<treeNode.columns.length;i++){
			if(treeNode.checked){
			 	toggleMutilColumns(treeNode.columns[i],false);
			}
			 else{
				 toggleMutilColumns(treeNode.columns[i],true);
				
			 }
		}
	}
}

//判断复合表头。如果全部隐藏了子项列，那么复合表头也隐藏
function toggleMutilColumns(filed,ishide){
	if(filed.indexOf(preFlag)!=-1){
		if(!ishide){
			$table.find("th[data-field='"+filed+"']").eq(0).show();
		}
		else{
			$table.find("th[data-field='"+filed+"']").eq(0).hide();
			
			
		}
	}
	for(var i=0;i<rowheaders.length;i++){
		$table.find("th[data-field='"+rowheaders[i]+"']").eq(0).hide();
	}
	
}
//选中树节点以及子节点
function checkedAllChild(treeNode,zTree){
	treeNode.checked=true;
	zTree.updateNode(treeNode);
	if(treeNode.isParent){
		var children=treeNode.children;
		for(var i=0;i<children.length;i++){
			checkedAllChild(children[i],zTree)
		}
	}
}
//查询
function doSearch(){
	var $form=$("#ElockForm");
	
	var serialize = $form.serialize();
	$.ajax({
		url:root+requestUrl,
		type:"post",
		async:false,
		cache:false,
		dataType:"json",
		data:serialize,
		success:function(ret){
			$table.bootstrapTable("destroy").bootstrapTable({
	        	method:"post",
	        	pagination:true,
	        	pageSize:20,
	        	pageNumber:1,
	        	pageList : [ 10, 20, 30 ],
	        	search:false,
	        	showColumns:false,
	        	showExport:false,
	        	exportDataType:"basic",
	        	exportTypes:['csv','txt','xml'],
	        	cache:false,
	            columns: ret.columns,
	            data:ret.data,
	            onPostHeader:function(){
	            	
	            },
	            onColumnSwitch:function(field,checked){
	            	//待完善
	            }
	            
	        });
			
			var columnNode=curRequestNode.children;
			for(var i=0;i<columnNode.length;i++){
				toggleColumn(columnNode[i]);
			}
		},
		error:function(msg1,msg2,msg3){
			bootbox.error("<fmt:message key="statistic.dynamic.error"/>");
		}
	});
}
function getFontCss(treeId, treeNode) {  
    return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};  
}
//初始化
$(document).ready(function(){
	var t = $("#tree");
	t = $.fn.zTree.init(t, setting, zNodes);
	//打开第一个节点，并查询数据
	var nodes=t.getNodesByFilter(function(node){
		return (node.level==1 && node.isFirstNode);
			
	},false);
	t.selectNode(nodes[0])
	t.expandNode(nodes[0],true,true,true);
	checkedAllChild(nodes[0],t);
	onclick(nodes[0]);
	//初始化日期组件
	$("#form_alarmStartTime, #form_alarmEndTime").datetimepicker({
		format: "yyyy-mm-dd hh:ii",//ii:分钟
		autoclose: true,
		todayBtn: true,
		language: 'en'
	}).on('changeDate', function(ev){
	    var startTime = $('#s_timeStart').val();//获得开始时间
	    $('#form_alarmEndTime').datetimepicker('setStartDate', startTime);//设置结束时间（大于开始时间）
	});
	
	//导出
	$(".excel_exp").on("click",function(){
		 $table.tableExport({
			 type:"excel",
			escape:false
		});
	});
	//导出
	$(".word_exp").on("click",function(){
		 $table.tableExport({
			 type:"word",
			 escape:false
		});
	});

});
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
			  ids.push(node.id);
			  while(node.level!=0){
				  node = treeObj.getNodeByTId(node.parentTId);
				  if(!ids.contains(node.id)){
					  ids.push(node.id);
				  }
			  }
		  }
		  
		  for(var i=0;i<nodeList.length;i++){
			  var json = nodeList[i];
			  if(ids.contains(json.id)){
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
	//刷新tale
	$(window).resize(function(){
		$table.bootstrapTable("resetView");
	});
</script>
</body>
</html>