/**
 * 用户查看未处理任务，处理完之后将其标记为已处理
 */
function handleMission() {
	var userId = $("#userId").val();
	var taskIds = "";
	//获取选中checkbox的值
	$("input[name='taskId']:checkbox").each(function() {
		if ($(this).prop("checked")) {
			taskIds += $(this).val() + ",";
		}
	});
	if (taskIds.length <= 0) {
		bootbox.alert($.i18n.prop("mission.choose.one.more"));
		return;
	} else {
		var ajaxUrl = root+"/undealMission/dealMission.action";
		$.ajax({
			url : ajaxUrl,
			type : "post",
			dataType : "json",
			data : {
				taskIds : taskIds,
				userId : userId
			},
			success : function(data) {
				if(data.type==1){
					$("#escortMission").click();
				}else if(data.type==2){
					$("#alarmMission").click();
				}else{
					$("#dispatchMission").click();
				}
			}
		});
	}
}
