function initTree() {
	var setting = {
			async: {
	            enable: true,
	            type: "get",
	            url : root + '/deptMgmt/findDepartmentTree.action'
	        },
			check: {
				enable:false,
				chkboxType: { "Y": "s", "N": "s" }
			},
			view: {
				dblClickExpand: false
			},
			data: {
	            keep: {
	                parent: true
	            },
	            key: {
	                name: "name",
	                title: "name"
	            },
	            simpleData: {
	                enable: true,
	                idKey: "id",
	                pidKey: "pId",
	                rootId: 0
	            }
	        },
			callback: {
				/*beforeClick: beforeClick,*/
				onClick: onClick
			}
		};
	$.ajax({
	    url : root + '/deptMgmt/findDepartmentTree.action',
	    dataType: "json",
	    cache: false,
	    success: function(data) {
	    	$.fn.zTree.init($("#departmentTree"), setting, data.departmentList);
	    }
	});
}



function onClick(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("departmentTree"),
	nodes = zTree.getSelectedNodes()
	var checkedName = "";
	var checkedId = "";
	/*for (var i=0, l=nodes.length; i<l; i++) {
		var isParent = nodes[i].isParent;
		if (isParent != true) {
			checkedName += nodes[i].name
			checkedId += nodes[i].id;
		}
	}*/
	
	for (var i=0, l=nodes.length; i<l; i++) {
			checkedName += nodes[i].name
			checkedId += nodes[i].id;
	}
	var userShow = $("#userChecked");
	var users = $('#s_parentId');
	userShow.val();
	userShow.val(checkedName);
	users.val();
	users.val(checkedId);
}


function onBodyDown(event) {
	if (!(event.target.id == "userChecked" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
		hideMenu();
	}
} 
function showMenu() {
	var deparmentOffset = $('#userChecked').offset();
	//alert(usersOffset);
	
	$("#menuContent").slideDown("fast");
	$("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}
$(function() {
	initTree();
});