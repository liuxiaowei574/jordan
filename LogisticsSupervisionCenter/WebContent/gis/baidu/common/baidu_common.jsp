<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=dNunWpXR8N0UcE2eDDtVGKzv"></script>
<!-- <script type="text/javascript" src="http://api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay_min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer_min.js"></script> -->
<script type="text/javascript">
  var webroot = window.document.location.pathname;
  document.write('<script type="text/javascript" src="'+webroot+'/gis/baidu/js/TextIconOverlay_min.js"><' + '/script>');
  document.write('<script type="text/javascript" src="'+webroot+'/gis/baidu/js/MarkerClusterer_min.js"><' + '/script>');
  document.write('<script type="text/javascript" src="'+webroot+'/gis/baidu/js/DrawingManager.js"><' + '/script>');
  document.write('<script type="text/javascript" src="'+webroot+'/gis/baidu/js/EventWrapper.js"><' + '/script>');
  var script = '<script type="text/javascript" src="'+webroot+'/gis/baidu/js/LuShu';
  if (document.location.search.indexOf('min') !== -1) {
     script += '_min';
  }
  script += '.js"><' + '/script>';
  document.write(script);
  document.write('<script type="text/javascript" src="'+webroot+'/gis/baidu/js/baidu.js"><' + '/script>');
</script>
