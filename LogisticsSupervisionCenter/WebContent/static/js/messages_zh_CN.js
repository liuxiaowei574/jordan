/*
 * Translated default messages for the jQuery validation plugin.
 * Language: CN
 * Author: Fayland Lam <fayland at gmail dot com>
 */
jQuery.extend(jQuery.validator.messages, {
        required: "必填字段",
		remote: "系统中已存在,请修正",
		email: "请输入正确格式的电子邮件",
		url: "请输入合法的网址",
		date: "请输入合法的日期",
		dateISO: "请输入合法的日期 (ISO).",
		number: "请输入合法的数字",
		digits: "只能输入整数",
		creditcard: "请输入合法的信用卡号",
		license:"输入格式不正确",
		equalTo: "请再次输入相同的值",
		accept: "请输入拥有合法后缀名的字符串",
		maxlength: jQuery.validator.format("请输入一个长度最多是 {0} 的字符串"),
		minlength: jQuery.validator.format("请输入一个长度最少是 {0} 的字符串"),
		rangelength: jQuery.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
		range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
		max: jQuery.validator.format("请输入一个最大为 {0} 的值"),
		min: jQuery.validator.format("请输入一个最小为 {0} 的值")
});

$.validator.addMethod("unChinese", function (value, element, param) {
	var reg = /^[^\u4e00-\u9fa5]{0,}$/;
	return reg.test(value);
},"请不要输入汉字");

$.validator.addMethod("vCheck", function (value, element, param) {
	var reg = /^[\u4E00-\u9FA5a-zA-Z0-9_-]{2,20}$/;
	return reg.test(value);
},"只能输入汉字， 英文，字母 ，数字， 下划线和横线，2-20位");

var options = {
        pageNumber: 1,
        pageCount: 1,
        totalCount:0,
       
        showFirst: true,
        first: "首页",
        showPrev: true,
        prev: "上一页",
        showNext: true,
        next: "下一页",
        showLast: true,
        last: "尾页",
        
        splitText:"...",
        linkFirstOverflow: true,//是否显示前面
        linkLastOverflow: true,//是否显示后面
        pageStartNum: 2, //最前面显示的页码个数
		currentPageBefore: 3, //当前页面前面显示的页码数
		currentPageAfter: 3, //当前页面后面显示的页码数
		pageEndNum: 2, //最后要显示的页码个数
		
		showTotalRecordsNum: true, //是否显示总记录数
		totalRecordsNumText: "总记录: ",
	    showTotalPageCount: true, //是否显示总页数
	    totalPageCountText: "页数: ",
		showGoPageButton: true,
		pageText: "页码: ",
		goButtonText: "转向"
	};

var common_Default ={
	deleteSuccess: "删除信息成功！",
	optionText: "--请选择--",
	selectDel:	"请选择要删除的记录！",
	selectEdit: "请选择要修改的记录！",
	onlyOne: "只能选择一条记录！",
	confirmDel: "确定要删除选中的记录吗？",
	btnDelFile: "删除",
	existing: "该文件已存在！",
	Malformed: "所选图片格式不正确！"	,
	lock: "请选择要锁定的记录！",
	lockSuccess: "锁定成功！",
	unlock: "请选择要解锁的记录！",
	unlockSuccess: "解锁成功！",
	initPassword: "请选择要重置密码的记录！",
	confirmInitPassword: "确定要对选中的记录进行重置密码操作吗？",
	initPasswordSuccess: "重置密码成功！",
	selectYear: "请选择年份！",
	week: "周",
	exportExcel: "请选择条件导出，否则数据量较大,容易造成系统崩溃",
	operatorSelect:"请选择要操作的记录",
	operatorSuccess:"操作成功",
	currVehicleNOBlackList:"当前车辆无法加入黑名单!",
	vehiclesExists:"关联的车辆已加入黑名单!"
};
var new_commons_statement={
		lackvehiclelicenseInfo:"缺失车牌号信息!",
		lackvehicleCustomsCodeInfo:"缺失车辆海关编号信息!",
		relaVehiAddBlackList:"关联的车辆已加入黑名单!",
		requestDataFail:"请求数据失败!",
		plschsdpm:"请选择部门!",
		addsucs:"添加成功!",
		cnclsucs:"取消成功!",
		plsscncrctdcl:"请扫描正确的报关单号!",
		rcdthsdlrtplsscn:"已经记录此报关单，请重新扫描!",
		nothsdlrtorthsdlrtnopss:"无此报关单信息或此报关单还没有放行!",
		print:"打印",
		dcmntnmbr:"单证号",
		plschsprntcnclsn:"请选择你要打印的结论单!",
		nodata:"无数据!",
		nmbrlmt01:"请输入1~10000000之间的数字,小数保留2位!",
		nmbrlmt02:"地磅重一旦补录后将无法修改,请核实确认正确后再保存。是否保存?",
		nmbrlmt03:"关联的任务流水号不存在!",
		nmbrlmt04:"关联的任务流水号不存在!",
		nmbrlmt05:"只能输入大于0的正数!",
		nmbrlmt06:"报关单特批后将不能修改,是否确认特批?",
		nmbrlmt07:"此编号的查验台已存在!",
		nmbrlmt08:"请输入大于等于0,小于9999.99的数,小数位最多为两位!",
		nmbrlmt09:"请输入大于等于0的正整数!",
		nmbrlmt10:"请输入1~100之间的数字!",
		nmbrlmt11:"报关单一旦补录后将无法修改,请核实确认正确后再保存。是否保存?",
		nmbrlmt12:"请填写正确的单证号!",
		nmbrlmt13:"该项不能为空!",
		nmbrlmt14:"已发送至打印机!",
		nmbrlmt15:"已发送至打印机....",
		nmbrlmt16:"请正确选择查询时间！",
		nmbrlmt17:"按年统计，时间最大跨度为5年！",
		nmbrlmt18:"按月统计，时间最大跨度为12个月！",
		nmbrlmt19:"请选择时间区间！",
		nmbrlmt20:"中心获图时间区间不能超过60天!",
		nmbrlmt21:"检入时间区间不能超过60天!",
		nmbrlmt22:"录入时间区间不能超过60天!",
		nmbrlmt23:"您选择的列数不能超过5!",
		nmbrlmt24:"按天统计，时间最大跨度为31天！",
		nmbrlmt25:"申报单位查验统计",
		nmbrlmt26:"需要输入关区编号",
		nmbrlmt27:"需要输入关区名称",
		nmbrlmt28:"请输入一个'0.000001'至'180'之间的数字",
		nmbrlmt29:"请输入一个'0.000001'至'90'之间的数字",
		nmbrlmt30:"该文件已存在！",
		nmbrlmt31:"所选图片格式不正确！",
		nmbrlmt32:"请选择要修改的记录！",
		nmbrlmt33:"修改时只能选择一条记录！",
		nmbrlmt34:"请选择要删除的记录!",
		nmbrlmt35:"删除关区会附带删除该所属关区下的【图片、设备、风险提示信息、关区权限】等.请慎重!确定要删除选中的记录吗？",
		nmbrlmt36:"删除成功！",
		nmbrlmt37:"系统发生异常，异常信息：",
		nmbrlmt38:"格式不正确",
		nmbrlmt39:"请输入英文",
		nmbrlmt40:"请输入字母、数字或下划线，首末位为字母或数字",
		nmbrlmt41:"不能输入特殊字符",
		nmbrlmt42:"请输入数字，最多两位小数",
		total:"共",
		nmbrlmt43:"条记录",
		nmbrlmt44:"关区业务统计",
		nmbrlmt45:"请选择输出列!",
		nmbrlmt46:"您选择的列数不能超过5列!",
		nmbrlmt47:"中心获图时间区间不能超过30天!",
		nmbrlmt48:"检入时间区间不能超过30天!",
		nmbrlmt49:"录入时间区间不能超过30天!",
		nmbrlmt50:"自定义报表",
		nmbrlmt51:"机检中心月度业务统计",
		nmbrlmt52:"机检中心年度业务统计",
		nmbrlmt53:"科室业务统计",
		nmbrlmt54:"人员绩效统计",
		nmbrlmt55:"科室人员绩效统计",
		nmbrlmt56:"任务风险分析统计",
		nmbrlmt57:"任务各环节时间记录",
		nmbrlmt58:"车辆业务情况统计",
		loading:"加载中..."		
};

var logMgmt_alert={
	exportExcel: "请选择条件导出，否则数据量较大,容易造成系统崩溃"
};
var deviceTypeMgmt_alert={
	confirmDel: "删除设备类型会附带删除关联该项的设备信息等.请慎重!确定要删除选中的记录吗？"
};
var deviceMgmt_alert={
	confirmDel: "删除设备会附带删除关联该项的【设备图片、设备故障信息】等.请慎重!确定要删除选中的记录吗？"
};

var siteMgmt_alert={
	confirmDel: "删除进出口关区会附带删除该进出口关区下的【图片、设备、风险提示信息、进出口关区权限】等.请慎重!确定要删除选中的记录吗？"
};

var userPassword = "输入密码不正确";

var login_namgisnull_alert="请输入工号！";
var login_passwordisnull_alert="请输入密码！";

var notChinese = "上传文件名称不要包含汉字";

var flowStatusCheck = "当前流程环节状态不能与下一流程环节状态相同!";
var printmessage = {
		"true":"确定设置嫌疑打印么？",
		"false":"确定设置有嫌疑不打印么？"
};

var mapPrompt = {
	risk: "风险提示信息: 此进出口关区有如下风险，请注意！",
	containerSum: "此进出口关区本月查验的集装箱数量为:",
	siteTitle: "进出口关区信息：",
	siteNum: "进出口关区编号：",
	siteName: "站点名称：",
	siteLongitude: "经&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度：",
	siteLatitude: "纬&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度：",
	description: "进出口关区描述："
};

var noteFilter = {
		vehicleNumber:"卡车号/拖车号/集装箱号/货车车号",
		declamationNo:"公告/TIR配制车辆/报关单号"				
	};


var message = {
		alert:"系统提示 ",
		confirm:"系统提示 ",
		ok: "确定",
		cancel: "取消"
    };


/**
	星期的判定
*/
var dayOfWeek={
		0:"星期日",
		 1:"星期一",
		 2:"星期二",
		 3:"星期三",
		 4:"星期四",
		 5:"星期五",
		 6:"星期六"
};
var dateFormatString="yyyy年M月dd日 w hh:mm:ss";

/**
 * 会话过期
 */
var needlogin = {
		message: "长时间未操作，请重新登录!",
		login: "跳转至登录页"
};