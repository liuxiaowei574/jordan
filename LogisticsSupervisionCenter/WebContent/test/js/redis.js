function trimText() {
	$("input[type=text]").each(function() {
		$(this).val($.trim($(this).val()));
	});
}
function validate(){
	trimText();
	var ip = $("#ip").val();
	var port = $("#port").val();
	var password = $("#password").val();
	if (!ip || !port || !password) {
		return false;
	}
	return true;
}
function clearKey() {
	if(!validate()) {
		return;
	}
	bootbox.confirm("确认清空所有Key？", function(result) {
		if (result) {
			$.post(root + "/redis/clearKey.action", {
				ip : $("#ip").val(),
				port : $("#port").val(),
				password : $("#password").val()
			}, function(data) {
				if (data && data.result) {
					bootbox.alert("清空成功！");
				}
			}, 'json');
		}
	});
}
function search() {
	if(!validate()) {
		return;
	}
	$("#searching, #caution").toggleClass("hidden");
	$.get(root + "/redis/list.action", {
		ip : $("#ip").val(),
		port : $("#port").val(),
		password : $("#password").val(),
		key : $("#key").val()
	}, function(data) {
		if (data && data.keys) {
			var html = '';
			var keys = data.keys.sort();
			$(".tab-cotent-title .badge").html(keys.length);
			keys.forEach(function(value, index) {
				var s = '';
				if (data[value].type == 'list') {
					var size = data[value].value.length;
					s = '<span class="badge" data-toggle="tooltip" title="' + size +' 个元素">' + size + '</span>';
				}
				html += '<tr>';
				html += '	<td>' + (index + 1)+ '</td>';
				html += '	<td>' + value + '</td>';
				html += '	<td>' + data[value].type + s + '</td>';
				html += '	<td><textarea rows=6>' + JSON.stringify(data[value].value) + '</textarea></td>';
				html += '	<td><button type="button" class="btn btn-warning delBtn" name="' + value + '">删除</button></td>';
				html += '</tr>';
			});
			$("#listTable tbody").html(html);
			$("#searching, #caution").toggleClass("hidden");
			tooltip();
		} else if(!data.result) {
			bootbox.alert(data.message);
		}
	}, 'json');
}
function doRest() {
	$("#searchForm")[0].reset();
}
function init(){
	try{
		var password = redisUrl.match(/\/\/:(.*)@/)[1];
		var ip = redisUrl.match(/@(.*):/)[1];
		var port = redisUrl.match(/@.*:(\d*)/)[1];
		$("#password").val(password);
		$("#ip").val(ip);
		$("#port").val(port);
	}catch(e){}
}
function tooltip(){
	$("[data-toggle='tooltip']").tooltip({placement: 'bottom'});
}
$(function() {
	init();
	search();
	
	$("#listTable").delegate(".delBtn", "click", function(){
		if(!validate()) {
			return;
		}
		var key = this.name;
		bootbox.confirm("确认删除Key:<br>" + key, function(result) {
			if (result) {
				$.post(root + "/redis/delKey.action", {
					ip : $("#ip").val(),
					port : $("#port").val(),
					password : $("#password").val(),
					key : key
				}, function(data) {
					if (data && data.result) {
						bootbox.alert("删除成功！", function(){
							window.location.href = window.location.href;
						});
					}
				}, 'json');
			}
		});
	});
});
