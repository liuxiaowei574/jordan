
//移除已选择的关锁
var $table = $('#elockTable'),
    $button = $('#button');
$(function () {
    $button.click(function () {
    	debugger;
        var list = $('#elockTable').bootstrapTable('getSelections'); //获取表的行
		var elcokNumber = new Array();
		for ( var o in list) {
			elcokNumber.push(list[o].ELOCK_NUMBER);
		}
        $table.bootstrapTable('remove', {
            field: 'ELOCK_NUMBER',
            values: elcokNumber
        });
        
    });
});

/*$(function(){  
	$button.click(function () {
		debugger;
        var trs = $("#elockTable").find("tr");
        for(var j = 1 ; j < trs.length; j ++){
            trs[j].remove();
        }                                                                                                                                                                                                                                                                                                                
    });
});  */