

/**
 * 查询设备调度任务，任务类型为"3"
 */
$('#dispatchMission').on('click', function() {
	//查找巡逻队护送任务
	$("#deviceDispatchList").html("");
	var roleName = $("#roleName").val();
	//控制中心主管可以查看所有未 完成的报警任务，不需要自己也是接收人；
	if(roleName == "contromRoomManager"){
		var portUrl = root + "/undealMission/findAllUndealDispatchMission.action";
	}else{
		var portUrl = root + "/undealMission/findUndealDispatchMission.action";
	}
	
	$.ajax({
		type : "POST",
		url : portUrl,
		dataType : "json",
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(jsonObj) {
			$("#alarmTask").hide();
			$("#escortTask").hide();
			$("#devicedipatchTask").show();
			var dispatchMission = jsonObj;
			
			//优先等级为"1"的背景色为红
			for(var i=0;i<dispatchMission.length;i++){
				var dispatchMissionInfo = dispatchMission[i];
				if(dispatchMissionInfo.TASK_PRIORITY==1){
					var dispacthMissionText=
						"<li style=\"background-color:red\" class=\"red active\"><input type=\"checkbox\"  "
						+ "name=\"taskId\" value=\""+ dispatchMissionInfo.TASK_ID+","+dispatchMissionInfo.TASK_TYPE+"\">"  
						+		"<a onclick=missionContent('"+dispatchMissionInfo.TASK_CONTENT+"','"+dispatchMissionInfo.TASK_PRIORITY+"') href=\"#home\"role=\"tab\" data-toggle=\"tab\">"+dispatchMissionInfo.TASK_TITLE+"<span class=\"glyphicon glyphicon-tasks badge\"></span><p>"+dispatchMissionInfo.DEPLOY_TIME+"</p></a></li>";
					$("#deviceDispatchList").append(dispacthMissionText);
				}
			}
			
			//优先等级为"2"的背景色为黄
			for(var i=0;i<dispatchMission.length;i++){
				var dispatchMissionInfo = dispatchMission[i];
				if(dispatchMissionInfo.TASK_PRIORITY==2){
					var dispacthMissionText2=
						"<li style=\"background-color:yellow\" class=\"red active\"><input type=\"checkbox\"  "
						+ "name=\"taskId\" value=\""+ dispatchMissionInfo.TASK_ID+","+dispatchMissionInfo.TASK_TYPE+"\">"  
						+		"<a onclick=missionContent('"+dispatchMissionInfo.TASK_CONTENT+"','"+dispatchMissionInfo.TASK_PRIORITY+"') href=\"#home\"role=\"tab\" data-toggle=\"tab\">"+dispatchMissionInfo.TASK_TITLE+"<span class=\"glyphicon glyphicon-tasks badge\"></span><p>"+dispatchMissionInfo.DEPLOY_TIME+"</p></a></li>";
					$("#deviceDispatchList").append(dispacthMissionText2);
				}
			}
			
			//优先等级为"3"的背景色为绿色
			for(var i=0;i<dispatchMission.length;i++){
				var dispatchMissionInfo = dispatchMission[i];
				if(dispatchMissionInfo.TASK_PRIORITY==3){
					var dispacthMissionText3=
						"<li style=\"background-color:00FF00\" class=\"red active\"><input type=\"checkbox\"  "
						+ "name=\"taskId\" value=\""+ dispatchMissionInfo.TASK_ID+","+dispatchMissionInfo.TASK_TYPE+"\">"  
						+		"<a onclick=missionContent('"+dispatchMissionInfo.TASK_CONTENT+"','"+dispatchMissionInfo.TASK_PRIORITY+"') href=\"#home\"role=\"tab\" data-toggle=\"tab\">"+dispatchMissionInfo.TASK_TITLE+"<span class=\"glyphicon glyphicon-tasks badge\"></span><p>"+dispatchMissionInfo.DEPLOY_TIME+"</p></a></li>";
					$("#deviceDispatchList").append(dispacthMissionText3);
				}
			}
		}
	});
})

/**
 * 点击任务列表显示详细的信息
 * @param content
 */
function missionContent(content,priority){
	//textarea
	$("#missionContent").val('');
	//给textarea赋值(任务内容)
	$("#missionContent").val(content);
	
	//清空input框
	$("#taskPriority").val('');
	//给input框赋值(任务优先级)
	$("#taskPriority").val(priority);
}

