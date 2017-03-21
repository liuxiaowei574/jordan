<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${root}/static/bootstrap-slider/css/bootstrap-slider.min.css">
<jsp:include page="../include/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

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
	<!-- Modify Modal构修改模态框 -->
		<div class="modal  add_user_box" id="updateElockModal" tabindex="-1"
			role="dialog" aria-labelledby="updateElockModal">
			<div class="modal-dialog" role="document">
				<div class="modal-content"></div>
			</div>
		</div>
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
       			  	<fmt:message key="risk.setting.tree.title"/>
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
					<label class="col-sm-4 control-label" for="s_goodtypeId"><fmt:message key="risk.setting.query.no"/></label>
					<div class="col-sm-8">
						<input type="text" id="s_goodtypeId" name="s_goodtypeId"
							class="form-control">
					</div>
				</div>
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label" for="s_gtypeName"><fmt:message key="risk.setting.query.name"/></label>
					<div class="col-sm-8">
						<input type="text" id="s_gtypeName" name="s_gtypeName"
							class="form-control">
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
						<li><a id="editElockBtn" class="btn btn-info"><fmt:message key="common.button.modify"/></a></li>
						
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
var zNodes = ${treeArr};//树结构数据
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
var requestUrl="";//请求url
var setting = {
	check: {
		enable: false,
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
				zTree.expandNode(treeNode);
				return false;
			} else {
				//demoIframe.attr("src",treeNode.file + ".html");
				return true;
			}
		},
		onClick : function(event, treeId, treeNode){

			onclick(treeNode);
		}
	}
};
function onclick(treeNode){
	var zTree = $.fn.zTree.getZTreeObj("tree");
	if(treeNode.level==0){
		
		requestUrl=treeNode.requestUrl;
		replaceSearch(treeNode);
		doSearch();
	}else{
		return false;
	}
}
//设置查询条件
function replaceSearch(node){
	$(".search_title").text(node.name);
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
//刷新tale
$(window).resize(function(){
	$table.bootstrapTable("resetView");
});
//查询
function doSearch(){
	var params = $table.bootstrapTable('getOptions');
	params.queryParams = function(params) {
		//遍历form 组装json  
		$.each($("#ElockForm").serializeArray(), function(i, field) {
			//console.info(field.name + ":" + field.value + " ");
			//可以添加提交验证                   
			params[field.name] = field.value;
		});
		return params;
	}
	$table.bootstrapTable('refresh', params);
	
}
//初始化table
function inittable(){
	
	$table.bootstrapTable("destroy").bootstrapTable({
				url:root+"/analysis/goodTyleriskSetting.action",
				clickToSelect : true,
				showRefresh : false,
				search : false,
				showColumns : false,
				showExport : false,
				striped : true,
				height : "100%",
				method : "get",
				cache : false,
				sortable:true,
				pagination : true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 10,
				pageList : [ 10, 20, 30 ],
	            columns:  [ {
					checkbox : true
				},{
					field : 'iSerial',
					title : '<fmt:message key="risk.setting.query.no"/>',
					formatter:formatEmpty,
					sortable:true
				}, {
					field : 'gtypeName',
					title : '<fmt:message key="risk.setting.query.name"/>',
					formatter:formatEmpty,
					sortable:true
				}, {
					field : 'lowRiskV',
					title : '<fmt:message key="risk.setting.result.fazhi"/>',
					formatter:formatEmpty,
					sortable:true
				}, {
					field : 'bak',
					title : '<fmt:message key="alarmDeal.label.dealDesc"/>',
					formatter:formatEmpty,
					sortable:true
				}
				]
	            
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
		return (node.level==0 && node.isFirstNode);
			
	},false);
	t.selectNode(nodes[0])
	t.expandNode(nodes[0],true,true,true);
	onclick(nodes[0]);
	//
	inittable();
	//修改	//模态窗
	$("#editElockBtn")
	.click(
			function() {
				var ids = $.map($table
						.bootstrapTable('getSelections'),
						function(row) {
							return row.goodtypeId;
						});
				if (ids.length == 0) {
					bootbox
							.alert('<fmt:message key="risk.setting.alert.minlength"/>');
				} else if (ids.length > 1) {
					bootbox
							.alert('<fmt:message key="risk.setting.alert.maxlength"/>');
				} else {
					var url = "${root}/analysis/editGoodType.action?id="
							+ ids;
					$('#updateElockModal').removeData(
							'bs.modal');
					$('#updateElockModal').modal({
						remote : url,
						show : false,
						backdrop : 'static',
						keyboard : false
					});
				}
			});
		$('#updateElockModal').on('loaded.bs.modal', function(e) {
			$('#updateElockModal').modal('show');
		});
		//模态框登录判断
		$('#updateElockModal').on('show.bs.modal', function(e) {
			var content = $(this).find(".modal-content").html();
			needLogin(content);
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
	
	function formatEmpty(value, row, index) {
		var show;
		if (value == 'null') {
			return "-";
		} else if (value == null) {
			return "-";
		} else if (value == "") {
			return "-";
		}else {
			return value;
		}
		
	}
  
</script>
</body>
</html>