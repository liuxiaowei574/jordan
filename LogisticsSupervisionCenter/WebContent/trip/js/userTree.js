function initTree() {
	var setting = {
			async: {
	            enable: true,
	            type: "get",
	            url: root+'/notice/findUserTree.action'
	        },
			check: {
				enable: true,
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
				beforeClick: beforeClick,
				onCheck: onCheck
			}
		};
	$.ajax({
	    url: root+'/notice/findUserTree.action',
	    dataType: "json",
	    cache: false,
	    success: function(data) {
	    	$.fn.zTree.init($("#userTree"), setting, data.userTreeList);
	    }
	});
}
function initTree1() {
	var setting = {
			async: {
	            enable: true,
	            type: "get",
	            url: root+'/notice/findUserTree.action'
	        },
			check: {
				enable: true,
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
				beforeClick: beforeClick1,
				onCheck: onCheck1
			}
		};
	$.ajax({
	    url: root+'/notice/findUserTree.action',
	    dataType: "json",
	    cache: false,
	    success: function(data) {
	    	$.fn.zTree.init($("#userTree1"), setting, data.userTreeList);
	    }
	});
}

function beforeClick(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("userTree");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function beforeClick1(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("userTree1");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}
function onCheck(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("userTree"),
	nodes = zTree.getCheckedNodes(true),
	checkedName = "";
	checkedId = "";
	for (var i=0, l=nodes.length; i<l; i++) {
		var isParent = nodes[i].isParent;
		if (isParent != true) {
			checkedName += nodes[i].name + ",";
			checkedId += nodes[i].id + ",";
		}
	}
	if (checkedName.length > 0 ) {
		checkedName = checkedName.substring(0, checkedName.length-1);
	}
	if (checkedId.length > 0 ) {
		checkedId = checkedId.substring(0, checkedId.length-1);
	}
	var userShow = $("#userChecked");
	var users = $('#s_checkinUserId');
	userShow.val();
	userShow.val(checkedName);
	users.val();
	users.val(checkedId);
}
function onCheck1(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("userTree1"),
	nodes = zTree.getCheckedNodes(true),
	checkedName = "";
	checkedId = "";
	for (var i=0, l=nodes.length; i<l; i++) {
		var isParent = nodes[i].isParent;
		if (isParent != true) {
			checkedName += nodes[i].name + ",";
			checkedId += nodes[i].id + ",";
		}
	}
	if (checkedName.length > 0 ) {
		checkedName = checkedName.substring(0, checkedName.length-1);
	}
	if (checkedId.length > 0 ) {
		checkedId = checkedId.substring(0, checkedId.length-1);
	}
	var userShow = $("#userChecked1");
	var users = $('#s_checkoutUserId');
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
function onBodyDown1(event) {
	if (!(event.target.id == "userChecked1" || event.target.id == "menuContent1" || $(event.target).parents("#menuContent1").length>0)) {
		hideMenu1();
	}
} 
function showMenu() {
	var usersOffset = $('#userChecked').offset();
	//alert(usersOffset);
	
	$("#menuContent").slideDown("fast");
	$("body").bind("mousedown", onBodyDown);
}
function showMenu1() {
	var usersOffset = $('#userChecked1').offset();
	//alert(usersOffset);
	
	$("#menuContent1").slideDown("fast");
	$("body").bind("mousedown", onBodyDown1);
}
function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}
function hideMenu1() {
	$("#menuContent1").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown1);
}
$(function() {
	initTree();
	initTree1();
});