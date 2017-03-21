<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp"%>
<title><fmt:message key="common.message.project" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="${root}/static/css/bootstrap.css">
<link rel="stylesheet" href="${root}/static/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="${root }/static/js/bootstrap/validator0.5.2/css/bootstrapValidator.min.css" />
<link rel="stylesheet" href="${root}/static/js/bootstrap/datetimepicker/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${root}/static/js/zTree/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="${root}/static/css/bootstrap-table.min.css">
<link rel="stylesheet" href="${root}/static/css/bootbox.css">
<link rel="stylesheet" href="${root}/static/css/jquery.minicolors.css">
<link rel="stylesheet" href="${root}/static/css/select2.min.css">
<link rel="stylesheet" href="${root}/static/css/highlight.css" rel="stylesheet">
<link rel="stylesheet" href="${root}/static/css/bootstrap-switch.css" rel="stylesheet">
<link rel="stylesheet" href="${root}/static/css/ui-dialog.css">
<link rel="stylesheet" href="${root}/static/css/mask/style.css">

<link rel="stylesheet" href="${root}/static/css/bootstrap-submenu.min.css">
<link rel="stylesheet" href="${root}/static/css/style.css">
<link rel="stylesheet" href="${root}/static/css/claro.css">
<link rel="stylesheet" href="${root}/static/css/esri.css">

<script src="${root}/static/js/jquery.min.js"></script>
<script src="${root}/static/js/jquery.i18n.properties-min-1.0.9.js"></script>
<script src="${root}/static/js/bootstrap.js"></script>
<script type="text/javascript" src="${root }/static/js/bootstrap/validator0.5.2/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="${root }/static/js/bootstrap/validator0.5.2/js/language/${userLocale }.js"></script>
<script type="text/javascript" src="${root}/static/js/bootstrap/datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${root}/static/js/zTree/js/jquery.ztree.all.min.js"></script>
<script src="${root}/static/js/bootstrap-table.js"></script>
<script src="${root}/static/bootstrap-table/locale/bootstrap-table-<fmt:message key='fmt.btlocal'/>.min.js"></script>
<script type="text/javascript" src="${root}/static/js/bootstrap/bootbox/bootbox.js"></script>
<script type="text/javascript" src="${root}/static/js/common/common.js"></script>
<script type="text/javascript" src="${root}/static/js/websocket.js"></script>
<script type="text/javascript" src="${root}/static/js/reconnecting-websocket.min.js"></script><%--断线重连功能--%>
<script type="text/javascript" src="${root}/static/js/jquery.minicolors.js"></script>
<script type="text/javascript" src="${root}/static/js/jscolor.js"></script>
<script type="text/javascript" src="${root}/static/js/jquery.form.js"></script>
<script type="text/javascript" src="${root}/static/js/html2canvas.js"></script>
<script type="text/javascript" src="${root}/static/js/jquery.Jcrop.js"></script>
<script type="text/javascript" src="${root}/static/js/highlight.js"></script>
<script type="text/javascript" src="${root}/static/js/bootstrap-switch.js"></script>
<script type="text/javascript" src="${root}/static/js/bootstrap-submenu.min.js" defer></script>
<script type="text/javascript" src="${root}/static/js/alarm.js"></script>

<script type="text/javascript" src="${root}/static/js/ajaxfileupload.js"></script>


<!-- artdialog  -->
<script src="${root}/static/js/artDialog/dialog-min.js"></script>
<script src="${root}/static/js/artDialog/dialog-plus.js"></script>
<!-- jscolor -->
<script src="${root}/static/js/select/select2.full.js"></script>
<script src="${root}/static/js/select/prettify.js"></script>
<script type="text/javascript" src="${root}/static/js/select/placeholders.jquery.min.js"></script>



<script type="text/javascript">
$(function() {
	jQuery.i18n.properties({//加载资浏览器语言对应的资源文件
        name : 'LocalizationResource_center', //资源文件名称
        path : _getRootPath() + "/i18n/", //资源文件路径
        mode : 'map', //用Map的方式使用资源文件中的值
        language :language,
        callback : function() {
        	//alert(path);
        	//alert($.i18n.prop("common.message.form.validator"))
        }
    });
	/* bootbox.setDefaults("locale",locale); */
	initTripInfoModal();
	intervalGetDealAlarm();
});
initWebSocket();
var sessionUserId = '${sessionUser.userId}';
var sessionUserName = '${sessionUser.userName}';
var sessionId = '${pageContext.session.id}';
var roleName = '${sessionUser.roleName }';
var organizationId = '${sessionUser.organizationId }';

Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, //month
		"d+" : this.getDate(), //day
		"HH+" : this.getHours(), //hour
		"hh+" : this.getHours(), //hour
		"m+" : this.getMinutes(), //minute
		"s+" : this.getSeconds(), //second
		"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter
		"S" : this.getMilliseconds()
	//millisecond
	}
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}
</script>
