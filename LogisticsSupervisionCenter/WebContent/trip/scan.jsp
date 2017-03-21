<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<div class="modal-body">
	<%--barcode --%>
	<div id="barcodeView" style="text-align: center;">
		<button type="button" class="btn btn-default" id="btnCloseBarcode" style="margin-bottom: 5px;"><fmt:message key="trip.activate.button.closeCamera"/></button>
		<div id="barcode"></div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	barcode.init();
	//关闭读取条形码
	$("#btnCloseBarcode").on("click", function(e) {
        e.preventDefault();
        Quagga.stop();
        $('#scanModal').modal('hide');
    });
});
</script>
</body>
</html>