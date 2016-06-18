/**
 *国家简写验证，两位大写英文字母
 */
jQuery.validator.addMethod("countryShortName", function(value, element) {
	return this.optional(element) || /^[A-Z][A-Z]$/.test(value);
}, new_commons_statement.nmbrlmt38);


/**
 * 英文名称
 */
jQuery.validator.addMethod("english", function(value, element) {
	return this.optional(element) || /^[a-zA-Z][a-zA-Z\s\']*[a-zA-Z]$/.test(value);
},new_commons_statement.nmbrlmt39);



/**
 * 电话号码
 */
jQuery.validator.addMethod("phone", function(value, element) {
	return this.optional(element) || /^[0-9][0-9\-]*[0-9]$/.test(value);
},new_commons_statement.nmbrlmt38);
/**
 * 联系电话
 */
jQuery.validator.addMethod("contactPhone", function(value, element) {
	return this.optional(element) || /^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{5}|\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{5}|\d{4}|\d{3}|\d{2}|\d{1}))$/.test(value);
},new_commons_statement.nmbrlmt38);
/**
 * 手机号码
 */
jQuery.validator.addMethod("mobile", function(value, element) {
	return this.optional(element) || /^(13[0-9]|15[0|1|2|3|5|6|7|8|9]|18[8|9])\d{8}$/.test(value);
},new_commons_statement.nmbrlmt38);



/**
 * 邮政编码
 */
jQuery.validator.addMethod("postCode", function(value, element) {
	return this.optional(element) || /^[0-9]{6}$/.test(value);
},new_commons_statement.nmbrlmt38);

/**
 * 整数，包括负整数
 */
jQuery.validator.addMethod("integer", function(value, element) {
	return this.optional(element) || (/^\-[1-9][0-9]*$/.test(value) || /^[1-9][0-9]*$/.test(value)||/^[0]$/.test(value));
},'请输入整数');
/**
 * 注册用户名
 */
jQuery.validator.addMethod("loginName", function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9]([a-zA-Z0-9]|\_){2,28}[a-zA-Z0-9]$/.test(value);
},new_commons_statement.nmbrlmt40);



/**
 * 不能输入 特殊字符验证
 */
jQuery.validator.addMethod("refuseSpecialChar", function(value, element) {
	 var newValue = value.replace(/\s|\<|\>|\\|\&|\*/g, "");
	if(value.length > newValue.length){
		return false;
	}else{
		return true;
	}

},new_commons_statement.nmbrlmt41);

/**
 * 经纬度的秒，两位小数
 */
jQuery.validator.addMethod("secondNumber", function(value, element) {
	return this.optional(element) || /^[0-9]{1,2}$/.test(value) || /^[0-9]{1,2}\.[0-9]{1,2}$/.test(value);
},new_commons_statement.nmbrlmt42);

