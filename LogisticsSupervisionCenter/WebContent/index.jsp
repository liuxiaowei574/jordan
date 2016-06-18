<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${root}/gis/map.js.jsp"></script>
<script type="text/javascript" src="${root}/static/js/initMap.js"></script>
<script type="text/javascript" src="${root}/gis/gisBusinessMgt.js"></script>
<title>E-Tracking System</title>
</head>
<body style="overflow:hidden; ">
	<!-- 通知Modal -->
	<div class="modal fade add_user_box" id="noticeMsgModal" tabindex="-1"
		role="dialog" aria-labelledby="noticeModalMsgTitle">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="noticeModalTitle"><fmt:message key="system.notice.tip"/></h4>
				</div>
				<div class="modal-body">
					<form id="msgForm" class="form-horizontal row">
						<input type="hidden" id="log.noticeId" name="log.noticeId">
						<input type="hidden" id="log.receiveUser" name="log.receiveUser">
						<div class="col-md-10">
							<div class="form-group ">
								<label class="col-sm-4 control-label"><fmt:message key="notice.title"/></label>
								<div class="col-sm-8">
									<input type="text" id="msgTitle" class="form-control input-sm">
								</div>
							</div>
							<div class="form-group ">
								<label class="col-sm-4 control-label"><fmt:message key="notice.content"/></label>
								<div class="col-sm-8">
									<textarea rows="10" cols="15" id="msgContent" class="form-control input-sm"></textarea>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-danger" data-dismiss="modal" onclick="msgClose()"><fmt:message key="common.button.close" /></button>
				</div>
			</div>
		</div>
	</div>
	<!-- /Modal -->
	<%@ include file="/monitor/map.jsp" %>
	<%@ include file="/include/foot.jsp" %>

	<div class="row site">
		<div class="col-md-1 sub_menu">
			<div class="user_box dropdown">
				<a href="" class="avatar dropdown-toggle" id="drop1"  data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><img src="${root }/static/images/avatar_03.png" alt=""></a>
			</div>
			<ul>
				<li><a href=""><span class="glyphicon zhuye"></span></a></li>
				<li><a href=""><span class="glyphicon clgl"></span></a></li>
				<li><a href=""><span class="glyphicon xxgl"></span></a></li>
				<li><a href=""><span class="glyphicon bjzx"></span></a></li>
				<li><a href=""><span class="glyphicon bbgl"></span></a></li>
				<li><a href=""><span class="glyphicon tjfx"></span></a></li>
				<li><a href=""><span class="glyphicon kcgl"></span></a></li>
				<li><a href=""><span class="glyphicon BIfx"></span></a></li>
				<li class="dropdown">
	              <a id="drop2" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
	                <span class="glyphicon xtgl"></span>
	              </a>
	              <ul class="dropdown-menu" aria-labelledby="drop2">
	                <li><a href="#">用户管理</a></li>
	                <li><a href="#">分类管理</a></li>
	                <li><a href="#">操作日志</a></li>
	                <li><a href="#">访问日志</a></li>
	              </ul>
            	</li>
			</ul>
		</div>

		<div class="col-md-12 profile profile02 profile_closed" id="profile">
			<div class="">
				<span class="close_span" id="open_span"><a href="#"
					class="close-profile-link clooses" id="link_open">+</a></span>
				 <!--User info-->
            	<div class="row">
                <div class="col-md-12">
                    <div class="user">
                       <div class="user_box">
                                    <a href="" class="avatar"><img src="static/images/avatar_03.png" alt=""/></a>
                        </div>
                        <div class="about_user">
                            <h2>马田野啊</h2>
                            <ul>
                                <li>工号: <a href="#">59 places</a></li>
                                <li>电话： <a href="#">59 places</a></li>
                                <li>RFID号 <a href="#">3 places</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="buttons">
                        <a href="#" class="btn btn-darch btn-sm"><i></i></a>
                        <a href="#" class="btn btn-darch btn-sm"><i class="message"></i><span>1</span> </a>
                        <a href="#" class="btn btn-darch btn-sm"><i class="exit"></i></a>
                    </div>
                </div>
            </div>
            <!--/User info-->
			</div>
		</div> 
	</div>
	<script type="text/javascript">
		$(function () {
			  $('[data-toggle="popover"]').popover();
			  $('#link_open').on('click', function () {
		          if ($('#link_open').hasClass("clooses")) {
		              $("#open_span").removeClass("close_span").addClass("open_span");
		              $("#profile").removeClass("profile_closed");
		              $("#link_open").removeClass("clooses");
		              $("#cont").addClass("none");
		          }
		          else {
		              $("#open_span").addClass("close_span").removeClass("open_span");
		              $("#profile").addClass("profile_closed");
		              $("#link_open").addClass("clooses");
		              $("#cont").removeClass("none");
		          }
		      })
		 })
	     /* $(document).ready(function () {
	           
	     }); */
	    </script>
	<script type="text/javascript" src="${root}/static/js/status.js"></script>
	<script type="text/javascript" src="${root}/static/js/websocket.js"></script>
	
  
	<script type="text/javascript">
	function msgClose() {
		var url = '${root}/notice/noticeRead.action';
		var serialize = $("#msgForm").serialize();
		$.post(url, serialize, function(data) {
		});
	}
	var sessionUserId = '${sessionUser.userId}';
	//alert(sessionUser);
	
	/**
	 * 重写地图的侧边工具栏的位置
	 */
	function changeLocation(){
		var isHidden = $("#cont").is(":hidden");
        if(isHidden){
        	$(".gm-bundled-control .gmnoprint,.gm-china .gmnoprint:last-child").css({
        		"left": "0px"
        	});
        }else{
        	$(".gm-china .gmnoprint:last-child,.gm-china .gmnoprint:last-child").css({
        		"left": "270px"
        	});
        }
	}
	
	function autoLoad(){
	    $(".gm-china .gmnoprint:last-child,.gm-china .gmnoprint:last-child").css({
	    	"left": "270px"
	    });
	    
	    GisAddEventForVehicle(map, 'idle', changeLocation);
	    GisAddEventForVehicle(map, 'zoom_changed', changeLocation);
	    GisAddEventForVehicle(map, 'bounds_changed', changeLocation); 
	    
	    /* google.maps.event.addListener(map, 'idle', changeLocation);
	    google.maps.event.addListener(map, 'zoom_changed', changeLocation);
	    google.maps.event.addListener(map, 'bounds_changed', changeLocation);  */
	}
	
	window.onload = autoLoad;
	
	</script>

</body>
</html>