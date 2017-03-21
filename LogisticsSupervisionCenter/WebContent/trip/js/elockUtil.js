var websocket = null;
/**
 * 关锁工具类
 */
var elockUtil = (function() {
	//__elockNo: 关锁号。__sealNo：子锁号。传感器号：__sensorNo。
	var __elockNo, __sealNo, __sensorNo;
	var __readDeviceType;
	var __EqualValue = [
	    10, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 23,
	    24, 25, 26, 27, 28, 29, 30, 31, 32, 34, 35, 36, 37, 38
	];
	var __CheckCode = [
	    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0
	];
	var __HexToAsni = [
	    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 'A', 'B', 'C',
	    'D', 'E', 'F'
	];
	var __isLocked = false; //是否施封
	/**
	 * 施封响应错误信息
	 */
	var __setLockedErrorInfo = {
			'01': $.i18n.prop('trip.setLocked.errorInfo01'), '02': $.i18n.prop('trip.setLocked.errorInfo02'), '03': $.i18n.prop('trip.setLocked.errorInfo03'), 
			'04': $.i18n.prop('trip.setLocked.errorInfo04'), '05': $.i18n.prop('trip.setLocked.errorInfo05'), '06': $.i18n.prop('trip.setLocked.errorInfo06'), 
			'07': $.i18n.prop('trip.setLocked.errorInfo07')
	};
	/**
	 * 解封响应错误信息
	 */
	var __setUnlockedErrorInfo = {
			'01': $.i18n.prop('trip.setUnlocked.errorInfo01'), '03': $.i18n.prop('trip.setUnlocked.errorInfo03'), '04': $.i18n.prop('trip.setUnlocked.errorInfo04'),
			'05': $.i18n.prop('trip.setUnlocked.errorInfo05'), '06': $.i18n.prop('trip.setUnlocked.errorInfo06'), '07': $.i18n.prop('trip.setUnlocked.errorInfo07')
	};
	/**
	 * 解封响应的报警信息
	 */
	var __setUnlockedAlarmInfo = {
			'01': $.i18n.prop('trip.setUnlocked.alarmInfo01'), '02': $.i18n.prop('trip.setUnlocked.alarmInfo02'), '03': $.i18n.prop('trip.setUnlocked.alarmInfo03'),
			'04': $.i18n.prop('trip.setUnlocked.alarmInfo04'), '05': $.i18n.prop('trip.setUnlocked.alarmInfo05')
	};
	/**
	 * 解除报警响应错误信息
	 */
	var __clearAlarmErrorInfo = {
			'02': $.i18n.prop('trip.clearAlarm.errorInfo02'), '03': $.i18n.prop('trip.clearAlarm.errorInfo03'), '04': $.i18n.prop('trip.clearAlarm.errorInfo04')
	};
	/**
	 * 字符串转为字节
	 */
	function stringToBytes(str) {
		var i, ch, st, len, re = [];
		for(i = 0, len = str.length; i < len; i++) {
			ch = str.charCodeAt(i); // get char   
			st = []; // set up "stack"  
			do {
				st.push(ch & 0xFF); // push byte to stack  
				ch = ch >> 8; // shift value down by 1 byte  
			}
			while (ch);
			// add stack contents to result  
			// done because chars have "wrong" endianness  
			re = re.concat(st.reverse());
		}
		// return an array of bytes  
		return re;
	}
	/**
	 * 获取字符的ASCII值
	 */
	function charCode(char) {
		return String(char).charCodeAt(0);
	}
	/**
	 * 根据读取的锁号，解析得到设备SSID。如：
	 * CNNU0018839321 --> 047DDC
	 * CNNU0018661383 --> 047300
	 * CNSP0000000272 --> 000004
	 * 
	 */
	function getElockSsidByElockNumber(elockNumber) {
		var readLockId, ssid, value, i, len;
		var readLockIdstr = "";
		readLockId = getElockId(elockNumber);
		if(!readLockId) {
			return "";
		}
		for(i = 0, len = readLockId.length; i < len; i++) {
			readLockIdstr += fillString(toHexString(readLockId[i]), 2, '0');
		}
		ssid = readLockIdstr.substring(8, 14);
		value = parseInt(ssid, 16) * 4; //16进制转10进制乘4
		return fillString(toHexString(value), 6, '0');
	}
	/**
	 * CNSP0000000272 ---->434e535000000110
	 */
	function getElockId(lockid) {
		var id, strid, strbytes, idbytes, tmpbuf;
		if (lockid.length != 14) {
			return null;
		}
		strid = lockid.substring(0, 4);
		strbytes = stringToBytes(strid);

		strid = lockid.substring(4);
		id = parseInt(strid);
		idbytes = int2byte(id);

		tmpbuf = [].concat(strbytes).concat(idbytes);
		if (checkVerifyCode(tmpbuf)) {
			return tmpbuf;
		} else {
			return null;
		}
	}
	/**
	 * 校验
	 */
	function checkVerifyCode(buf) {
		if (buf == null || buf.length < 8) {
			return false;
		}
		var i = 0;
		var n = 0;
		var vec = [];
		var tempVec = [];
		var tempNum;
		var charCodeA = charCode('A');
		for (i = 0; i < 4; i++) {
			n = buf[i] - charCodeA;
			if (n < 0 || n >= 26) {
				return false;
			}
			vec.push(__EqualValue[n]);
		}
		var num = 0;
		for (i = 4; i < 8; i++) {
			n = buf[i] & 0xF0;
			n = n >> 4;
			n = __HexToAsni[n];
			if ((charCode(n) - charCodeA) >= 0 && (charCode(n) - charCodeA) < 26) {
				vec.push(__EqualValue[charCode(n) - charCodeA]);
			} else if (charCode(n) >= 48 && charCode(n) <= 57) {
				vec.push(n);
			} else {
				return false;
			}
			num++;
			if (num == 7) {
				break;
			}
			n = buf[i] & 0x0F;
			n = __HexToAsni[n];
			if ((charCode(n) - charCodeA) >= 0 && (charCode(n) - charCodeA) < 26) {
				vec.push(__EqualValue[charCode(n) - charCodeA]);
			} else if (charCode(n) >= 48 && charCode(n) <= 57) {
				vec.push(n);
			} else {
				return false;
			}
			num++;
			if (num == 7) {
				break;
			}
		}
		num = 0;
		tempVec = [].concat(vec);
		i = 0;
		while (tempVec.length > 0) {
			tempNum = tempVec.shift(0);
			num += tempNum * (Math.pow(2, i));
			i++;
		}
		n = parseInt(num % 11);
		if (__CheckCode[n] == parseInt(buf[7] & 0x0F)) {
			return true;
		}
		return false;
	}
	function int2byte(num) {
		var targets = new Array(4);

		targets[3] = (num & 0xff); // 最低位
		targets[2] = ((num >> 8) & 0xff); // 次低位
		targets[1] = ((num >> 16) & 0xff); // 次高位
		targets[0] = (num >>> 24); // 最高位,无符号右移。
		return targets;
	}
	/**
	 * 填充数组
	 * @param length 填充后的数组长度
	 */
	function fillArray(array, length) {
		array = array || [];
		return $.map(new Array(length), function(element, index){
			return fillString(array[index]);
		});
	}
	/**
	 * 填充字符串
	 * @param str 原值
	 * @param length 填充后的长度
	 * @param char 用来填充的字符串
	 * @returns
	 */
	function fillString(str, length, char) {
		char = char || '0';
		str = str || char;
		return new Array(length).join(char).concat(str).slice(-length);
	}
	/**
	 * 十进制转为十六进制
	 */
	function toHexString(num) {
		return num.toString(16).toUpperCase();
	}
	/**
	 * 解析读取关锁命令的响应信息
	 */
	function parseElockNo(data) {
		if(__readDeviceType != "getElockNo") {
			return;
		}
		__readDeviceType = "";
		if(data.feedBack == '1') {
			__elockNo = data.sealNo;
			if(typeof checkElockNum != 'undefined' && $.isFunction(checkElockNum)) {
				checkElockNum(__elockNo, true);
			}
			if(typeof refreshVoltage != 'undefined' && $.isFunction(refreshVoltage)) {
				parseInt(data.voltage) && refreshVoltage(parseInt(data.voltage));
			}
			$("#btnSetLocked, #btnSetUnlocked, #btnClearAlarm").removeAttr("disabled");
		}else{
			bootbox.alert($.i18n.prop('trip.info.read.elock.failed'));
		}
	}
	/**
	 * 解析读取子锁命令的响应信息
	 */
	function parseSealNo(data) {
		if(__readDeviceType != "getSealNo") {
			return;
		}
		__readDeviceType = "";
		if(data.feedBack == '1') {
			__sealNo = data.sealNo;
			if(typeof checkEsealNumber != 'undefined' && $.isFunction(checkEsealNumber)) {
				checkEsealNumber(__sealNo, true);
			}
		}else{
			bootbox.alert($.i18n.prop('trip.info.read.eseal.failed'));
		}
	}
	/**
	 * 解析读取传感器命令的响应信息
	 */
	function parseSensorNo(data) {
		if(data.feedBack == '1') {
			__sensorNo = data.sensorNo;
			if(typeof checkSensorNumber != 'undefined' && $.isFunction(checkSensorNumber)) {
				checkSensorNumber(__sensorNo, true);
			}
		}else{
			bootbox.alert($.i18n.prop('trip.info.read.sensor.failed'));
		}
	}
	/**
	 * 解析施封命令的响应信息
	 */
	function parseSetLocked(data){
		if(data.feedBack == '1') {
			__isLocked = true;
			bootbox.success($.i18n.prop('trip.info.setLocked.success'));
		}else{
			var message = data.feedBack.split(/\s*,\s*/).map(function(index, element){
				return (__setLockedErrorInfo[index] && (index + ': ' + __setLockedErrorInfo[index])) || '';
			});
			message = $.i18n.prop('trip.info.setLocked.failed') + '<br>' + message.join('<br>');
			bootbox.alert(message);
		}
	}
	/**
	 * 解析解封命令的响应信息
	 */
	function parseSetUnlocked(data){
		if(data.feedBack == '1') {
			__isLocked = false;
			bootbox.success($.i18n.prop('trip.info.setUnlocked.success'));
		}else{
			var message = data.feedBack.split(/\s*,\s*/).map(function(index, element){
				return (__setUnlockedErrorInfo[index] && (index + ': ' + __setUnlockedErrorInfo[index])) || '';
			});
			var alarmMessage = '';
			var alarmTime = '';
			if(Object.keys(__setUnlockedAlarmInfo).indexOf(data.alarmType) > -1) {
				var alarmInfo = data.alarmType.split(/\s*,\s*/).map(function(index, element){
					return (__setUnlockedAlarmInfo[index] && (index + ': ' + __setUnlockedAlarmInfo[index])) || '';
				});
				if(alarmInfo.length > 0 && alarmInfo.join('').length > 0){
					alarmMessage = '<br>' + $.i18n.prop('trip.info.alarmInfo') + '<br>' + alarmInfo.join('<br>');
				}
				if(data.alarmTime) {
					alarmTime = '<br>' + $.i18n.prop('trip.report.alarmTime') + '<br>' + data.alarmTime.replace(/(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/,'$1/$2/$3 $4:$5:$6');
				}
			}
			message = $.i18n.prop('trip.info.setUnlocked.failed') + '<br>' + message.join('<br>') + alarmMessage + alarmTime;
			bootbox.alert(message);
		}
	}
	/**
	 * 解析解除报警命令的响应信息
	 */
	function parseClearAlarm(data){
		if(data.feedBack == '1') {
			bootbox.success($.i18n.prop('trip.info.clearAlarm.success'));
		}else{
			var message = data.feedBack.split(/\s*,\s*/).map(function(index, element){
				return (__clearAlarmErrorInfo[index] && (index + ': ' + __clearAlarmErrorInfo[index])) || '';
			});
			message = $.i18n.prop('trip.info.clearAlarm.failed') + '<br>' + message.join('<br>');
			bootbox.alert(message);
		}
	}
	/**
	 * 解析绑定封条命令的响应信息
	 */
	function parseSetBindingRelation(data) {
		if(data.feedBack == '1') {
			//bootbox.success($.i18n.prop('trip.info.bind.success'));
			//enableActivateButton();
		}else{
			//bootbox.alert($.i18n.prop('trip.info.bind.failed'));
		}
	}
	/**
	 * 解析读取封条命令的响应信息
	 */
	function parseBindingRelation(data) {
		if(data.feedBack == '1') {
			var nums = data.bindingRelation.split('-');
			nums.slice(0, 6).map(function(value, index){
				return value.slice(0, 6);
			}).forEach(appendEsealNums);
			nums.slice(6, 12).map(function(value, index){
				return value.slice(0, 6);
			}).forEach(appendSensorNums);
		}else{
			bootbox.alert($.i18n.prop('trip.info.read.bind.failed'));
		}
	}
	return {
		/**
		 * 解析关锁SSID函数指令
		 */
		getElockSsidByElockNumber: getElockSsidByElockNumber,
		/**
		 * 按键取关锁号指令
		 */
		getElockNo: {
			request: {"Id": "getSealNo"},
			response: {"Id": "getSealNo", "feedBack": "1", "sealNo": "12345", "voltage": "330"},//demo
			sendRequest: function(){
				__readDeviceType = "getElockNo";
				sendMessage(JSON.stringify(this.request));
			},
			parseResponse: parseElockNo
		},
		/**
		 * 按键取子锁号指令
		 */
		getSealNo: {
			request: {"Id": "getSealNo"},
			response: {"Id": "getSealNo", "feedBack": "1", "sealNo": "12345", "voltage": "330"},//demo
			sendRequest: function(){
				__readDeviceType = "getSealNo";
				sendMessage(JSON.stringify(this.request));
			},
			parseResponse: parseSealNo
		},
		/**
		 * 读传感器号指令
		 */
		getSensorNo: {
			request: {"Id": "getSensorNo","sealNo":"CNSP0000000272"},
			response: {"Id": "getSensorNo", "feedBack": "1", "sensorNo": "12345"},//demo
			sendRequest: function(){
				this.request.sealNo = __elockNo;
				sendMessage(JSON.stringify(this.request));
			},
			parseResponse: parseSensorNo
		},
		/**
		 * 施封指令，setLocked 附带对应关锁号和操作指令commandPWD
		 * 施封指令中的commandPWD，前端应用得考虑到这个指令的安全性，以及跟锁的硬件的解析一致性，需要项目经理去协调
		 * setLocked， 01锁杆未插好，02电压过低，03重复施封，04外壳破损，05响应超时，06未检测到封条，07封条被拆除，1成功.
		 * 				如果返回值不为1，则返回的错误代码可能有多个，每个以“,”分隔
		 * 成功sealNo， voltage有值
		 * 前端存库时要注意存储施封时间
		 */
		setLocked: {
			request: {"Id": "setLocked","sealNo":"CNSP0000000272","commandPWD":"0000000000"},
			response: {"sealNo":"CNSP0000000272","Id":"setLocked","feedBack":"1","voltage":"410"},//demo
			sendRequest: function(){
				this.request.sealNo = __elockNo;
				sendMessage(JSON.stringify(this.request));
			},
			parseResponse: parseSetLocked
		},
		/**
		 * 解封指令，setUnLocked 附带对应关锁号
		 * 指令反馈setUnlocked， 01操作口令不符和，02电压过低(取消)，03重复解封，04未施封状态解封，05报警状态不可以解封，
		 * 						06响应超时，07有封条报警，1成功，如果返回值不为1，则返回的错误代码可能有多个，每个以“，”分隔
		 * 成功sealNo有值
		 * alarmType 01锁杆断开，02非法拆开，03电压过低,04有封条失联，05施封状态下有封条被拆开
		 * "alarmTime":"yyyymmddhhmmss",//锁内最近的一次报警
		 */
		setUnlocked: {
			request: {"Id": "setUnlocked","sealNo":"CNSP0000000272","commandPWD":"0000000000"},
			response: {"sealNo":"CNSP0000000272","Id":"setUnlocked","feedBack":"1","voltage":"410","alarmNum":"0","alarmType":"00","alarmTime":"19700101155959"},//demo
			sendRequest: function(){
				this.request.sealNo = __elockNo;
				sendMessage(JSON.stringify(this.request));
			},
			parseResponse: parseSetUnlocked
		},
		/**
		 * 解除报警指令，clearAlarm附带对应关锁号以及commandPWD
		 * 指令反馈clearAlarm，02口令不符合，03无报警解除，04响应超时，1成功，成功sealNo有值
		 */
		clearAlarm: {
			request: {"Id": "clearAlarm","sealNo":"CNSP0000000272","commandPWD":"0000000000"},
			response: {"sealNo":"CNSP0000000272","Id":"clearAlarm","feedBack":"0","voltage":"410"},//demo
			sendRequest: function(){
				this.request.sealNo = __elockNo;
				sendMessage(JSON.stringify(this.request));
			},
			parseResponse: parseClearAlarm
		},
		/**
		 * 读取封条，如果封条号为"000000",则认为此处没有封条
		 * 指令反馈getBindingRelation，1表示成功，其它失败
		 */
		getBindingRelation: {
			request: {"Id":"getBindingRelation","sealNo":"CNSP0000000272"},
			response: {"Id":"getBindingRelation","feedBack":"1","bindingRelation":"p00001-p00002-p00003-p00004-p00005-p00006-p00007-p00008-p00009-p0000a-p0000b-000000","sealNo":" CNSP0000000272","voltage":"390"},//demo
			sendRequest: function(){
				this.request.sealNo = __elockNo;
				sendMessage(JSON.stringify(this.request));
			},
			parseResponse: parseBindingRelation
		},
		/**
		 * 绑定封条
		 * bindingRelation是数组：必须12个元素，每个元素长度必须6位。
		 * 前6个元素是传感器号，缺的用0补齐，后六个是子锁号，缺的用0补齐，如demo有五个传感器号，四个子锁号。
		 * 指令反馈setBindingRelation，1表示成功，其它失败
		 */
		setBindingRelation: {
			request: {"Id":"setBindingRelation","sealNo":"CNSP0000000272","commandPWD":"0000000000","bindingRelation":["111111","222222","333333","444444","555555","0","666666","777777","888888","999999","0","0"]},//demo
			response: {"Id":"setBindingRelation","feedBack":"1","sealNo":"CNSP0000000272","voltage":"390"},//demo
			sendRequest: function(){
				this.request.sealNo = __elockNo;
				this.request.bindingRelation = [].concat(fillArray(esealNumberArray, 6)).concat(fillArray(sensorNumberArray, 6));
				sendMessage(JSON.stringify(this.request));
			},
			parseResponse: parseSetBindingRelation
		},
		getReadDeviceType: function() {
			return __readDeviceType;
		},
		setElockNo: function(elockNo){
			__elockNo = elockNo;
		},
		isLocked: function(){
			return __isLocked;
		}
	};
})();

function getWebsocketUrl(clientIPAddress){
	return "ws://" + clientIPAddress + ":9000";
}
/**
 * 通过websocket发送消息
 * @param msg
 */
function sendMessage(msg) {
	var state = checkSocketState();
	if(state === null || state !== 0 && state !== 1) {
		initWebSocket();
	}
    if ( websocket != null ) {
        websocket.send( msg );
        console.log("Message sent: %s", msg);
    }
}
/**
 * 发送读关锁号命令
 */
function readElockNum(){
	elockUtil.getElockNo.sendRequest();
}
/**
 * 发送读子锁号命令
 */
function readEsealNum(){
	elockUtil.getSealNo.sendRequest();
}
/**
 * 发送读传感器号命令
 */
function readSensorNum(){
	elockUtil.getSensorNo.sendRequest();
}
/**
 * 发送施封命令
 */
function setLocked(){
	elockUtil.setLocked.sendRequest();
}
/**
 * 发送解封命令
 */
function setUnlocked(){
	elockUtil.setUnlocked.sendRequest();
}
/**
 * 发送解除报警命令
 */
function clearAlarm(){
	elockUtil.clearAlarm.sendRequest();
}
/**
 * 发送读封条命令
 */
function readBindingRelation(){
	elockUtil.getBindingRelation.sendRequest();
}
/**
 * 发送绑定封条命令
 */
function setBindingRelation(){
	elockUtil.setBindingRelation.sendRequest();
}
function initWebSocket(clientIPAddress) {
    try {
        if (typeof MozWebSocket == 'function') {
        	WebSocket = MozWebSocket;
        }
        if ( websocket && websocket.readyState == 1 ) {
        	websocket.close();
        }
        var wsUri = clientIPAddress && getWebsocketUrl(clientIPAddress) || websocket.url;
        websocket = new WebSocket(wsUri, "elockreadercom-websocket");
        websocket.onopen = function (evt) {
        	console.log("WEBSOCKET CONNECTED");
        };
        websocket.onclose = function (evt) {
        	console.log("WEBSOCKET DISCONNECTED");
        };
        websocket.onmessage = function (evt) {
        	try {
        		console.log( "Message received :", evt.data );
	        	var data = JSON.parse(evt.data);
	        	/*
	        	if(data && data.feedBack != '1') { //1代表响应成功
	        		bootbox.alert($.i18n.prop('trip.info.device.response.failed', data.feedBack));
	        		return;
	        	}
	        	*/
            	if(Object.keys(elockUtil).indexOf(data.Id) > -1) {
            		var readDeviceType = elockUtil.getReadDeviceType();
        			if(readDeviceType == "getElockNo") {
        				elockUtil.getElockNo.parseResponse(data);
            		}else if(readDeviceType == "getSealNo") {
            			elockUtil.getSealNo.parseResponse(data);
            		}else{
            			elockUtil[data.Id].parseResponse(data);
            		}
            	}
            }catch(e){
            	console.error(e);
            }
        };
        websocket.onerror = function (evt) {
            console.error('WEBSOCKET ERROR');
        };
    } catch (e) {
    	console.error('WEBSOCKET ERROR: ' + e);
    }
}
function stopWebSocket() {
	if (websocket) {
		websocket.close();
	}
}
function checkSocketState() {
    if (websocket != null) {
        var stateStr;
        switch (websocket.readyState) {
            case 0: {
                stateStr = "CONNECTING";
                break;
            }
            case 1: {
                stateStr = "OPEN";
                break;
            }
            case 2: {
                stateStr = "CLOSING";
                break;
            }
            case 3: {
                stateStr = "CLOSED";
                break;
            }
            default: {
                stateStr = "UNKNOW";
                break;
            }
        }
        console.log("WebSocket state = " + websocket.readyState + " ( " + stateStr + " )");
        return websocket.readyState;
    } else {
    	console.log("WebSocket is null");
    	return null;
    }
}