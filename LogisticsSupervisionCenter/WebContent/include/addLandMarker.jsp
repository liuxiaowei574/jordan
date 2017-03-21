<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%-- <jsp:include page="../../include/include.jsp" /> --%>
<title></title>
<style>
#landMarkerForm button {
	padding: 6px 18px;
}
</style>
</head>
<body>
	<form id="landMarkerForm">

		<ul class="Custom_list hidden" id="addLandMarker">
			<li>
				<label class="col-sm-6 control-label" for="landName"><fmt:message key="main.list.routeArea.routeAreaName" /></label>
				<input type="text" class="form-control input-sm" id="landName" name="landName"> 
				
				<div id="landImageTypeDiv" class="form-group ">
					<label class="col-sm-8  control-label" for="landImage">LandMarker Image</label>
					<select class="js-example-templating js-states form-control input-sm col-sm-4" id="landImage" name="landImage"  onchange="routeChangeType()">
                         <option value="airplane">airplane</option>
                         <option value="building">building</option>
                         <option value="cabin">cabin</option>
                         <option value="cruise">cruise</option>
                         <option value="hotel">hotel</option>
                         <option value="island">island</option>
                         <option value="placeholder">placeholder</option>
                         <option value="taxi">taxi</option>
					</select>
				</div>
				<!-- <input type="hidden" class="form-control input-sm" id="routeAreaType" name="routeAreaType">  -->
				<input id="landId" name="landId" type="hidden" />
				
				<label class="col-sm-6 control-label" for="latitude">latitude</label>
				<input type="text" class="form-control input-sm" readOnly="readOnly" id="latitude" name="latitude">
				
				<label class="col-sm-6 control-label" for="longitude">longitude</label>
				<input type="text" class="form-control input-sm" readOnly="readOnly" id="longitude" name="longitude"> 
                
                <label class="col-sm-6 control-label" for="description">description</label>
				<input type="text" class="form-control input-sm" id="description" name="description"> 
				
				<button id="addLandMarkerBtn" type="button" class="btn btn-danger">
					<fmt:message key="common.button.add" />
				</button>
				<button id="editLandMarkerBtn" type="button" class="btn btn-danger">
					<fmt:message key="common.button.modify" />
				</button>
				<button type="reset" class="btn btn-darch">
					<fmt:message key="common.button.reset" />
				</button>
				<button id="btnBack" type="button" class="btn btn-darch">
					<fmt:message key="common.button.back" />
				</button>
				
				</li>

		</ul>
	</form>

	<script type="text/javascript">
	$(function() {
	    $('#addLandMarkerBtn').on('click', function () {
	        addLanderMarker();
	     });
	    $('#editLandMarkerBtn').on('click', function () {
	    	updateLanderMarker();
	     });
	    $("#btnBack").on("click", function(){
	    	var id = $("#landId").val();
	    	$("#areaList").click();
			$("#planRouteAreaList").find("[name=landIds][value=" + id + "]").click();
	    });
	    
	});
	function addLanderMarker() {
		trimText();
   		if(!checkLandMarkerInput()) {
   			return false;
   		}
	   	var param = $("#landMarkerForm").serialize();
	 	var portUrl = getRootPath() + "landmarker/addLanderMarker.action";
 		$.ajax({
			type : "POST",
			url : portUrl,
			data: param,
			error : function(e, message, response) {
				console.log("Status: " + e.status + " message: " + message);
			},
			success : function(data) {
			    if(data.result=="success"){
			        bootbox.alert($.i18n.prop('trip.info.success'));	
			        findAllLandMarkers();
				}
			}
 		});
 	}
	function updateLanderMarker() {
		trimText();
   		if(!checkLandMarkerInput()) {
   			return false;
   		}
	   	var param = $("#landMarkerForm").serialize();
	 	var portUrl = getRootPath() + "landmarker/saveOrUpdateLanderMarker.action";
 		$.ajax({
			type : "POST",
			url : portUrl,
			data: param,
			error : function(e, message, response) {
				console.log("Status: " + e.status + " message: " + message);
			},
			success : function(data) {
			    if(data.result=="success"){
			        bootbox.alert($.i18n.prop('trip.info.success'));	
			        findAllLandMarkers();
				}
			}
 		});
 	}
	
	var $states = $(".js-source-states");
	var statesOptions = $states.html();
	$states.remove();

	$(".js-states").append(statesOptions);

	$("[data-fill-from]").each(function () {
	  var $this = $(this);

	  var codeContainer = $this.data("fill-from");
	  var $container = $(codeContainer);

	  var code = $.trim($container.html());

	  $this.text(code);
	  $this.addClass("prettyprint linenums");
	});

	prettyPrint();

	$.fn.select2.amd.require(
	    ["select2/core", "select2/utils", "select2/compat/matcher"],
	    function (Select2, Utils, oldMatcher) {
	  var $basicSingle = $(".js-example-basic-single");
	  var $basicMultiple = $(".js-example-basic-multiple");
	  var $limitMultiple = $(".js-example-basic-multiple-limit");

	  var $dataArray = $(".js-example-data-array");
	  var $dataArraySelected = $(".js-example-data-array-selected");

	  var data = [{ id: 0, text: 'enhancement' }, { id: 1, text: 'bug' }, { id: 2, text: 'duplicate' }, { id: 3, text: 'invalid' }, { id: 4, text: 'wontfix' }];

	  var $ajax = $(".js-example-data-ajax");

	  var $disabledResults = $(".js-example-disabled-results");

	  var $tags = $(".js-example-tags");

	  var $matcherStart = $('.js-example-matcher-start');

	  var $diacritics = $(".js-example-diacritics");
	  var $language = $(".js-example-language");

	  $basicSingle.select2();
	  $basicMultiple.select2();
	  $limitMultiple.select2({
	    maximumSelectionLength: 2
	  });

	  function formatState (state) {
	    if (!state.id) {
	      return state.text;
	    }
	    var $state = $(
	      '<span>' +
	        '<img src="static/images/flag/' +
	          state.element.value +
	        '.png" class="img-flag" /> ' +
	        state.text +
	      '</span>'
	    );
	    return $state;
	  };

	  $(".js-example-templating").select2({
	    templateResult: formatState,
	    templateSelection: formatState
	  });

	  $dataArray.select2({
	    data: data
	  });

	  $dataArraySelected.select2({
	    data: data
	  });

	  function formatRepo (repo) {
	    if (repo.loading) return repo.text;

	    var markup = '<div class="clearfix">' +
	    '<div class="col-sm-1">' +
	    '<img src="' + repo.owner.avatar_url + '" style="max-width: 100%" />' +
	    '</div>' +
	    '<div clas="col-sm-10">' +
	    '<div class="clearfix">' +
	    '<div class="col-sm-6">' + repo.full_name + '</div>' +
	    '<div class="col-sm-3"><i class="fa fa-code-fork"></i> ' + repo.forks_count + '</div>' +
	    '<div class="col-sm-2"><i class="fa fa-star"></i> ' + repo.stargazers_count + '</div>' +
	    '</div>';

	    if (repo.description) {
	      markup += '<div>' + repo.description + '</div>';
	    }

	    markup += '</div></div>';

	    return markup;
	  }

	  function formatRepoSelection (repo) {
	    return repo.full_name || repo.text;
	  }

	  $ajax.select2({
	    ajax: {
	      url: "",
	      dataType: 'json',
	      delay: 250,
	      data: function (params) {
	        return {
	          q: params.term, // search term
	          page: params.page
	        };
	      },
	      processResults: function (data, params) {
	        // parse the results into the format expected by Select2
	        // since we are using custom formatting functions we do not need to
	        // alter the remote JSON data, except to indicate that infinite
	        // scrolling can be used
	        params.page = params.page || 1;

	        return {
	          results: data.items,
	          pagination: {
	            more: (params.page * 30) < data.total_count
	          }
	        };
	      },
	      cache: true
	    },
	    escapeMarkup: function (markup) { return markup; },
	    minimumInputLength: 1,
	    templateResult: formatRepo,
	    templateSelection: formatRepoSelection
	  });

	  $(".js-example-disabled").select2();
	  $(".js-example-disabled-multi").select2();

	  $(".js-example-responsive").select2();

	  $disabledResults.select2();

	  $(".js-example-programmatic").select2();
	  $(".js-example-programmatic-multi").select2();

	  //$eventSelect.select2();

	  $tags.select2({
	    tags: ['red', 'blue', 'green']
	  });

	  $(".js-example-tokenizer").select2({
	    tags: true,
	    tokenSeparators: [',', ' ']
	  });

	  function matchStart (term, text) {
	    if (text.toUpperCase().indexOf(term.toUpperCase()) == 0) {
	      return true;
	    }

	    return false;
	  }

	  $matcherStart.select2({
	    matcher: oldMatcher(matchStart)
	  });

	  $(".js-example-basic-hide-search").select2({
	    minimumResultsForSearch: Infinity
	  });

	  $diacritics.select2();

	  $language.select2({
	    language: "es"
	  });

	  $(".js-example-theme-single").select2({
	    theme: "classic"
	  });

	  $(".js-example-theme-multiple").select2({
	    theme: "classic"
	  });

	  $(".js-example-rtl").select2();
	});
	</script>

</body>