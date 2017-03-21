<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="system.notice.title"/></title>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
     <div class="wrapper-content margint95 margin60">
		<%--导航 --%>
		<c:set var="pageName"><fmt:message key="WarehouseElock.Management"/></c:set>
		<jsp:include page="../../include/navigation.jsp" >
			<jsp:param value="${pageName }" name="pageName"/>
		</jsp:include>
		
		<!-- Modal 关锁添加模态框-->
		<div class="modal  add_user_box" id="addElockModal" tabindex="-1"
			role="dialog" aria-labelledby="addElockModalTitle">
			<div class="modal-dialog" role="document">
				<div class="modal-content"></div>
			</div>
		</div>
		<!-- /Modal -->
	
	
		<!-- Modify Modal关锁修改模态框 -->
		<div class="modal  add_user_box" id="updateElockModal" tabindex="-1"
			role="dialog" aria-labelledby="updateElockModal">
			<div class="modal-dialog" role="document">
				<div class="modal-content"></div>
			</div>
		</div>
		<!-- Modify Modal -->
	
	
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message
							key="WarehouseElock.Management" /></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="ElockForm" action=""
						onsubmit="return false;">
						<div class="form-group col-md-6">
							<label class="col-sm-4 control-label"><fmt:message
									key="WarehouseElock.elockNumber" /></label>
							<div class="col-sm-8">
								<input type="text" id="s_elockNumber" name="s_elockNumber"
									class="form-control">
							</div>
						</div>
				
						<c:if test="${filter}">
							<div class="col-md-6">
								<label for="roleIds" class="col-sm-4 control-label"><fmt:message
										key="WarehouseElock.belongTo" /></label>
								<div class="col-sm-8">
									<select style="/* font-size:10px */" id="s_belongTo" name="s_belongTo" class="form-control">
									<option  value=""></option>
										<c:forEach var="SystemDepartmentBO" items="${deptList}">
											<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</c:if>
						
						<div class="clearfix"></div>
						
						<div class="form-group col-md-6">
							<label class="col-sm-4 control-label"><fmt:message
									key="WarehouseElock.simCard" /></label>
							<div class="col-sm-8">
								<input type="text" id="s_simCard" name="s_simCard"
									class="form-control">
							</div>
						</div>
				
						<div class="col-md-6">
							<label class="col-sm-4 control-label"><fmt:message key="WarehouseElock.elockStatus"/></label>
							<div class="col-sm-8">
							<%-- <s:i18n name="LocalizationResource">
							<s:text name =<fmt:message key="WarehouseElock.elockStatus"/> id ="aa"></s:text>
							</s:i18n> --%>
							  
								<s:select name="s_elockStatus" 
										emptyOption="true"
										cssClass="form-control" theme="simple"
										list="@com.nuctech.ls.model.util.DeviceStatus@values()"
										listKey="text"
										listValue="key" 
										value="">
								</s:select>
							</div>
						</div>
			
						<div class="clearfix"></div>
						<div class="form-group">
							<div class="col-sm-offset-9 col-md-3">
								<button type="submit" class="btn btn-danger" onclick="doSearch();"><fmt:message key="common.button.query"/></button>
								<button  type="button"  class="btn btn-darch" onclick="doRest();">
									<fmt:message key="common.button.reset" />
								</button>
							</div>
						</div>
						<input type="file" id="fileToUpload" name="file" style="filter:alpha(opacity=0);opacity:0;width: 0;height: 0;"/> 
					</form>
				</div>
			</div>
			
			<!--/search form-->
			<!--my result-->
			<div class="tab-content">
			  <div class="tab-cotent-title">
			  	<div class="Features pull-right">
					<ul>
						<li><a id="addElockBtn"class="btn btn-info btn-weight"><fmt:message key="common.button.add"/></a></li>
	         			<li><a id="deletea"class="btn btn-info btn-weight" onclick="delObject();"><fmt:message key="common.button.delete"/></a></li>
	         			<li><a id="editElockBtn"class="btn btn-info btn-weight"><fmt:message key="common.button.modify"/></a></li>
	         			<li><a id="downloadBtn"class="btn btn-info btn-weight" onclick="downloadTem();"><fmt:message key="WarehouseElock.download.template"/></a></li>
						<li><a id="upload"class="btn btn-info btn-weight" ><fmt:message key="WarehouseElock.batch.import"/></a></li>
				   		<c:if test="${systemModules.isDispatchOn()}">
							<li><a id="dispatch"class="btn btn-info" onclick="deviceDispatch();"><fmt:message key="common.button.dispatch"/></a></li>
						</c:if>
					</ul>
				</div>
				<fmt:message key="elock.list"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="table" >	</table>
					</div>
				</div>
			</div>
		</div>
      </div>
	</div>
	<script type="text/javascript">
		var selections = [];
		var $table = $('#table');
		//刷新tale
		$(window).resize(function(){
			$table.bootstrapTable("resetView");
		});
		
		function doSearch() {
			var params = $table.bootstrapTable('getOptions');
			params.queryParams = function(params) {
				//遍历form 组装json  
				$.each($("#ElockForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params[field.name] = field.value;
				});
				return params;
			}
			$table.bootstrapTable('refresh', params);
		}

		
		function getIdSelections() {
			return $table.bootstrapTable('getSelections');
		}
		$(function() {
			//设置传入参数
			function queryParams(params) {
				//遍历form 组装json  
				$.each($("#ElockForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params += "&" + field.name + "=" + field.value;
				});
				return params;
			}
			$table.bootstrapTable({
					url:'${root}/warehouseElock/list.action',
					clickToSelect : true,
					showRefresh : false,
					search : false,
					showColumns : false,
					showExport : false,
					striped : true,
					//height : "1000",
					method : "get",
					idfield: "elockId",
					sortName:"elockNumber",
					cache : false,
					sortable:true,
					pagination : true,
					sidePagination : 'server',
					pageNumber : 1,
					pageSize : 10,
					pageList : [ 10, 20, 30 ],
				columns: [{
			    	checkbox : true
			    },{
			    	field: 'elockNumber',
			    	title: $.i18n.prop('WarehouseElock.elockNumber'),
					sortable:true
			    },{
			    	field: 'organizationName',//组织机构表里面的"机构名称"
			    	title: $.i18n.prop('WarehouseElock.belongTo'),
					sortable:true
			    },{
			    	field: 'simCard',
			    	title: $.i18n.prop('WarehouseElock.simCard'),
					sortable:true
			    },{
			    	field: 'interval',
			    	title: $.i18n.prop('WarehouseElock.interval'),
					sortable:true
			    },{
			    	field: 'gatewayAddress',
			    	title: $.i18n.prop('WarehouseElock.gatewayAddress'),
					sortable:true
			    },{
			    	field: 'elockStatus',
			    	title: $.i18n.prop('WarehouseElock.elockStatus'),
			    	formatter : elockStatusFormat,
					sortable:true
			    },{
			    	field: 'lastUseTime',
			    	title: $.i18n.prop('WarehouseElock.lastUseTime'),
					sortable:true
			    },{
			    	field: 'lastUserName',
			    	title: $.i18n.prop('WarehouseElock.lastUser'),
					sortable:false
			    },{
			    	field: 'timeNotInUse',
			    	title: $.i18n.prop('WarehouseElock.timeNotInUse'),
			    	sortable:true,
					formatter : dateFormatter
			    }]
/* 			    onLoadSuccess: function (data) {
				}
 */			});

			//添加Modal调用方法
			$("#addElockBtn").click(function() {
				var url = "${root}/warehouseElock/addModal.action";
				$('#addElockModal').removeData('bs.modal');
				$('#addElockModal').modal({
					remote : url,
					show : false,
					backdrop : 'static',
					keyboard : false
				});
			});

			$('#addElockModal').on('loaded.bs.modal', function(e) {
				$('#addElockModal').modal('show');
			});
			//模态框登录判断
			$('#addElockModal').on('show.bs.modal', function(e) {
				var content = $(this).find(".modal-content").html();
				needLogin(content);
			});

			//编辑Modal调用方法
		 	$("#editElockBtn")
					.click(
							function() {
								var ids = $.map($table
										.bootstrapTable('getSelections'),
										function(row) {
											return row.elockId
										});
								if (ids.length == 0) {
									bootbox.alert($.i18n.prop("elock.modify.choose"));
								} else if (ids.length > 1) {
									bootbox.alert($.i18n.prop("elock.modify.choose.only"));
								} else {
									var url = "${root}/warehouseElock/editModal.action?warehouseElockBO.elockId="+ ids;
									//给url添加时间戳（ps：ie浏览器中，修改数据后在模态框中不跟着改变）
									url = addUrlStam(url);
									$('#updateElockModal').removeData('bs.modal');
									$('#updateElockModal').modal({
										remote : url,
										show : false,
										backdrop : 'static',
										keyboard : false
									});
								}
							}); 
			/* $("#editElockBtn").click(
					function(){
						var list = $('#table').bootstrapTable('getSelections');
						var ids = new Array();
						for ( var o in list) {
							ids.push(list[o].elockId);
						}					
						
					}
				)	 */			
			$('#updateElockModal').on('loaded.bs.modal', function(e) {
				$('#updateElockModal').modal('show');
			});
			//模态框登录判断
			$('#updateElockModal').on('show.bs.modal', function(e) {
				var content = $(this).find(".modal-content").html();
				needLogin(content);
			});
		});
	</script>
	<!-- 给url添加时间戳 -->
	<script type="text/javascript">
		function addUrlStam(url){
			var newUrl = "";
			var date = new Date();
			var stamp = date.getTime();
			if(url.indexOf("?") ==-1){
				newUrl = url +"?s=" +stamp;
			}else {
				newUrl = url +"&s="+stamp;
			}
			return newUrl;
		}
		
	</script>
	
	<!-- 删除记录 -->
	<script type="text/javascript">
		function delObject() {
			var list = $('#table').bootstrapTable('getSelections'); //获取表的行
			var ids = new Array();
			var elockStatus = new Array();
			for ( var o in list) {
				ids.push(list[o].elockId);
				elockStatus.push(list[o].elockStatus);
			}
			var elockIds = ids.join(",");
			if(list.length<=0){
				bootbox.alert($.i18n.prop("warehouseElock.delete.choose"));
				return;
			//判断删除的关锁是否包含"在途"状态
			}else if(elockStatus.indexOf('2')!=-1){
				bootbox.alert($.i18n.prop("warehouseElock.delete.not.Inway"));
				return;
			}else{
				var ajaxUrl = "${root}/warehouseElock/delwarehouseById.action";  /*路径改过，原先是相对路径 */
				bootbox.confirm($.i18n.prop("are.you.sure"), function(result){
					if (result){
						$.ajax({
							url : ajaxUrl,
							type : "post",
							dataType : "json",
							data : {
								elockIds : elockIds
								},
							success : function(data) {
									if(data == true){
										bootbox.alert($.i18n.prop("elock.delete.success"));
										$('#table').bootstrapTable('refresh', {});
									}
							}
						});
					}
				})
			}
		}
	</script>
	<%--搜索表单重置 --%>
	<script type="text/javascript">
		function doRest(){
			$("#ElockForm")[0].reset();
		}
	</script>
	
	<!--调度按钮方法  -->
	<script type="text/javascript">
	function deviceDispatch(){
		var url = "${root}/dispatch/toList.action";
 		window.location.href=url;
	}
	</script>
	
	
	
	<!-- 将原先从数据库中查出来的值换为中文 -->
     <script type="text/javascript">
		function elockStatusFormat(value, row, index){
			var show;
			if(value =='0'){
				show = $.i18n.prop('DeviceStatus.Scrap');
			}else if(value =='1'){
				show =$.i18n.prop('DeviceStatus.Normal');
			}else if(value =='2'){
				show = $.i18n.prop('DeviceStatus.Inway');
			}else if(value =='3'){
				show = $.i18n.prop('DeviceStatus.Destory');
			}else if(value =='4'){
				show = $.i18n.prop('DeviceStatus.Maintain');
			}else{
				show = '--'
			}
			return [show].join('');
		}
		
		//关锁多长时间未使用
		function dateFormatter(value, row, index){
			var diff;
			if(!!row.lastUseTime) {//最后使用时间作为计算起点
				console.log(row.lastUseTime);
				var lastUseTime = new Date(row.lastUseTime);
				diff = timeDifference(lastUseTime, new Date());
			}else{//关锁创建时间作为计算起点
				var createTime = new Date(row.createTime);
				diff = timeDifference(createTime, new Date());
			}
			
			//如果关锁处于"在途"状态，则将"多久未被使用"设为0；
			if(row.elockStatus =="2"){
				diff = 0;
			}
			
			var time = convertTime(diff);
			/* if(isNaN(time.hours) || isNaN(time.minutes)) {
				return '--';
			}
			return time.hours + ":" + ('00' + time.minutes).slice(-2); */
			if(isNaN(time.hours)) {
				return '--';
			}
			return time.hours ;
		}
		
		function convertTime(milliseconds) {
			var totalMinutes = parseInt(milliseconds / 1000 / 60);
			//var minutes = totalMinutes % 60;
			//var hours = parseInt(totalMinutes / 60);
			var hours = (totalMinutes / 60).toFixed(1);//精确到小时的小数点后一位
			return {
				//"minutes": minutes,
				"hours": hours
			};

		}
		function timeDifference(beginDate, endDate) {
			return endDate.getTime() - beginDate.getTime();
		}
    </script> 
    
    <!-- 下载excel模板 -->
    <script type="text/javascript">
	    function downloadTem(){
	    	//var filePath = "${root}/template/Elock_${sessionScope.userLocale}.xlsx";
	    	var filePath = "${root}/template";//(excel模板存储的位置)
	    	var url = "${root}/warehouseElock/downTemplate.action?filePath="+filePath;
	 		window.location.href=url;
	    }
    </script> 
    <!-- 批量导入excel数据 -->
    <script type="text/javascript">  
		$(function(){  
		    //点击打开文件选择器  
		    $("#upload").on('click', function() {  
		        $('#fileToUpload').click();  
		    });  
		      var url="${root}/warehouseElock/batchImportexcel.action";
		    //选择文件之后执行上传  
		    $('#fileToUpload').on('change', function() {  
		        $.ajaxFileUpload({  
		            url:url,  
		            secureuri:false,  
		            fileElementId:'fileToUpload',//file标签的id  
		            dataType: 'json',//返回数据的类型  
		            data:{name:'logan'},//一同上传的数据  
		            success: function (data, status) {  
		            	if(data.result=="true"){
		            		bootbox.alert($.i18n.prop("elock.batch.import.data.sucess"),function(){
		            			$('#table').bootstrapTable('refresh', {});
		            		});
		            	}
		            	if(data.result=="excelEnd"){
		            		bootbox.alert($.i18n.prop("please.select.excel.type.file"));
		            	}
		            	
		            	if(data.result=="false"){
		            		bootbox.alert($.i18n.prop("elock.batch.import.data.fail"));
		            	}
		            	
		            	if(data.result=="existed"){
		            		bootbox.alert($.i18n.prop("elock.batch.import.elocknumber.existed"));
		            	}
		            },  
		            error: function (data, status, e) {  
		            }  
		        });  
		    });  
		      
		});  
</script>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
</body>
</html>