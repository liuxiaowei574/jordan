<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp"%>
<title><fmt:message key="common.message.project" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="${root}/static/css/bootstrap.css">
<%-- <link rel="stylesheet" href="${root}/static/js/bootstrap/validator/css/bootstrapValidator.min.css"> --%>
<link rel="stylesheet" href="${root }/static/js/bootstrap/validator0.5.2/css/bootstrapValidator.min.css" />
<link rel="stylesheet" href="${root}/static/js/bootstrap/datetimepicker/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${root}/static/js/zTree/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="${root}/static/css/bootstrap-table.min.css">
<link rel="stylesheet" href="${root}/static/css/bootbox.css">
<link rel="stylesheet" href="${root}/static/css/style.css">

<script src="${root}/static/js/jquery.min.js"></script>
<script src="${root}/static/js/jquery.i18n.properties-min-1.0.9.js"></script>
<script src="${root}/static/js/bootstrap.js"></script>
<%-- <script type="text/javascript" src="${root}/static/js/bootstrap/validator/bootstrapValidator.min.js"></script> --%>
<script type="text/javascript" src="${root }/static/js/bootstrap/validator0.5.2/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="${root }/static/js/bootstrap/validator0.5.2/js/language/${userLocale }.js"></script>
<script type="text/javascript" src="${root}/static/js/bootstrap/datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${root}/static/js/zTree/js/jquery.ztree.all.min.js"></script>
<script src="${root}/static/js/bootstrap-table.js"></script>
<script type="text/javascript" src="${root}/static/js/bootstrap/bootbox/bootbox.js"></script>
<script type="text/javascript" src="${root}/static/js/common/common.js"></script>


<script type="text/javascript">
//alert(language);
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
});
</script>


