

/**
 * 查询报警处理任务，任务类型为"2"
 */
$('#alarmMission').on('click', function() {
	//查找报警处理任务
	$("#alarmMissionList").html("");
	var roleName = $("#roleName").val();
	//控制中心主管可以查看所有未 完成的报警任务，不需要自己也是接收人；
	if(roleName == "contromRoomManager"){
		var portUrl = root + "/undealMission/findAllUndealAlarmMission.action";
	}else{
		var portUrl = root + "/undealMission/findUndealAlarmMission.action";
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
			$("#alarmTask").show();
			$("#escortTask").hide();
			$("#devicedipatchTask").hide();
			var alarmMission = jsonObj;
			//优先等级为"1"的背景色为红
			for(var i=0;i<alarmMission.length;i++){
				var alarmMissionInfo = alarmMission[i];
				if(alarmMissionInfo.TASK_PRIORITY==1){
					var alarmMissionText=
						"<li style=\"background-color:red\" class=\"red active\"><input type=\"checkbox\"  "
														+ "name=\"taskId\" value=\""+ alarmMissionInfo.TASK_ID+","+alarmMissionInfo.TASK_TYPE+"\">" 
														+		"<a onclick=missionContent('"+alarmMissionInfo.TASK_CONTENT+"','"+alarmMissionInfo.TASK_PRIORITY+"') href=\"#home\"role=\"tab\" data-toggle=\"tab\">"+alarmMissionInfo.TASK_TITLE+"<span class=\"glyphicon glyphicon-tasks badge\"></span><p>"+alarmMissionInfo.DEPLOY_TIME+"</p></a></li>";
					$("#alarmMissionList").append(alarmMissionText);
				}
			}
			//优先等级为"2"的背景色为黄
			for(var i=0;i<alarmMission.length;i++){
				var alarmMissionInfo = alarmMission[i];
				if(alarmMissionInfo.TASK_PRIORITY==2){
					var alarmMissionText2=
						"<li style=\"background-color:yellow\" class=\"red active\"><input type=\"checkbox\"  "
														+ "name=\"taskId\" value=\""+ alarmMissionInfo.TASK_ID+","+alarmMissionInfo.TASK_TYPE+"\">" 
														+		"<a onclick=missionContent('"+alarmMissionInfo.TASK_CONTENT+"','"+alarmMissionInfo.TASK_PRIORITY+"') href=\"#home\"role=\"tab\" data-toggle=\"tab\">"+alarmMissionInfo.TASK_TITLE+"<span class=\"glyphicon glyphicon-tasks badge\"></span><p>"+alarmMissionInfo.DEPLOY_TIME+"</p></a></li>";
					$("#alarmMissionList").append(alarmMissionText2);
				}
			}
			
			//优先等级为"3"的背景色为绿色
			for(var i=0;i<alarmMission.length;i++){
				var alarmMissionInfo = alarmMission[i];
				if(alarmMissionInfo.TASK_PRIORITY==3){
					var alarmMissionText3=
						"<li style=\"background-color:00FF00\" class=\"red active\"><input type=\"checkbox\"  "
														+ "name=\"taskId\" value=\""+ alarmMissionInfo.TASK_ID+","+alarmMissionInfo.TASK_TYPE+"\">" 
														+		"<a onclick=missionContent('"+alarmMissionInfo.TASK_CONTENT+"','"+alarmMissionInfo.TASK_PRIORITY+"') href=\"#home\"role=\"tab\" data-toggle=\"tab\">"+alarmMissionInfo.TASK_TITLE+"<span class=\"glyphicon glyphicon-tasks badge\"></span><p>"+alarmMissionInfo.DEPLOY_TIME+"</p></a></li>";
					$("#alarmMissionList").append(alarmMissionText3);
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

$(function() {
	$("#alarmMission").click();
	
})