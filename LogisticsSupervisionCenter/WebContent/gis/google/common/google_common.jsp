<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript"
	src="http://ditu.google.cn/maps/api/js?key=AIzaSyB26TtMhWyMQ1VkqnqUZkFrZKi7qbkW4Go&sensor=false&libraries=drawing&language=en-US"></script>
<script type="text/javascript">
    var webroot = window.document.location.pathname;
    document.write('<script type="text/javascript" src="'+webroot+'/gis/google/js/GoogleUtil.js"><' + '/script>');
	var script = '<script type="text/javascript" src="'+webroot+'/gis/google/js/markerclusterer';
	if (document.location.search.indexOf('compiled') !== -1) {
	  script += '_compiled';
	}
	script += '.js"><' + '/script>';
	document.write(script);
	document.write('<script type="text/javascript" src="'+webroot+'/gis/google/js/google.js"><' + '/script>');
</script>