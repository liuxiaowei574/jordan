function initTree() {
	var setting = {
			async: {
	            enable: true,
	            type: "get",
	            url: root+'/notice/findNewUserTree.action'
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
	    url: root+'/notice/findNewUserTree.action',
	    dataType: "json",
	    cache: false,
	    success: function(data) {
	    	$.fn.zTree.init($("#userTree"), setting, data.userTreeList);
	    }
	});
}

function beforeClick(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("userTree");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function onCheck(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("userTree"),
	nodes = zTree.getCheckedNodes(true),
	checkedName = "";
	checkedId = "";
	for (var i=0, l=nodes.length; i<l; i++) {
		//var isParent = nodes[i].isParent;//父节点不会被选中
		var level = nodes[i].level;
		if (level == 2) {//只选择用户树的第三级（用户），第一、二级分别为国家和机构
		checkedName += nodes[i].name + ",";
		checkedId += nodes[i].id + ",";
		}
	}
	if (checkedName.length > 0 ) checkedName = checkedName.substring(0, checkedName.length-1);
	if (checkedId.length > 0 ) checkedId = checkedId.substring(0, checkedId.length-1);
	var userShow = $("#userChecked");
	var users = $('#notice\\.noticeUsers');
	console.log(checkedName);
	console.log(checkedId);
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
	var users = $('#userChecked');
	var usersOffset = $('#userChecked').offset();
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