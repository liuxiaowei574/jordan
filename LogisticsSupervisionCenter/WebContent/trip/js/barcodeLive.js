/***
 * 实时摄像头识别条形码
 */
var barcode = {
    init : function(callback) {
        Quagga.init(this.state, function(err) {
            if (err) {
                console.log(err);
                return;
            }
            barcode.attachListeners();
            Quagga.start();
        });
    },
    attachListeners: function() {
        var self = this;
    },
    detachListeners: function() {
        $(".controls").off("click", "button.stop");
    },
    state: {
        inputStream: {
            type : "LiveStream",
            target: "#barcode",
            constraints: {
                width: 640,
                height: 480,
                facingMode: "environment" // or user
            }
        },
        locator: {
            patchSize: "medium",
            halfSample: true
        },
        numOfWorkers: 1,
        decoder: {
            readers : [{
            	/**
            	 * format:
            	 * code_128_reader (default),
            	 * ean_reader,
            	 * ean_8_reader,
            	 * code_39_reader,
            	 * code_39_vin_reader,
            	 * codabar_reader,
            	 * upc_reader,
            	 * upc_e_reader,
            	 * i2of5_reader
            	 * */
            	format: "ean_reader",
                config: {}
            }]
        },
        locate: true
    }
};
//barcode.init();

Quagga.onProcessed(function(result) {
    var drawingCtx = Quagga.canvas.ctx.overlay,
        drawingCanvas = Quagga.canvas.dom.overlay;

    if (result) {
        if (result.boxes) {
            drawingCtx.clearRect(0, 0, parseInt(drawingCanvas.getAttribute("width")), parseInt(drawingCanvas.getAttribute("height")));
            result.boxes.filter(function (box) {
                return box !== result.box;
            }).forEach(function (box) {
                Quagga.ImageDebug.drawPath(box, {x: 0, y: 1}, drawingCtx, {color: "green", lineWidth: 2});
            });
        }

        if (result.box) {
            Quagga.ImageDebug.drawPath(result.box, {x: 0, y: 1}, drawingCtx, {color: "#00F", lineWidth: 2});
        }

        if (result.codeResult && result.codeResult.code) {
            Quagga.ImageDebug.drawPath(result.line, {x: 'x', y: 'y'}, drawingCtx, {color: 'red', lineWidth: 3});
        }
    }
});

Quagga.onDetected(function(result) {
	var code = result.codeResult.code;
	$("#tripVehicleVO\\.declarationNumber, #s_declarationNumber").val(code);
	//code = '222520131250168837';
	$.ajax({
		url: root + "/monitortrip/loadTripInfo.action", 
		data: {"declarationNumber": code},
		dataType: 'JSON',
		cache: false,
		success: function(data){
			if(!needLogin(data)) {
				if(data.tripVehicleVO) {
					showTripInfo(data);
					validateTripInfos();
				}else if(data.message) {
					bootbox.alert($.i18n.prop("trip.info.getInfoError"));
					console.log(data.message);
				}else{
					bootbox.alert($.i18n.prop("trip.info.recordNotFound"));
				}
			}
		},
		error: function(xhr, textStatus, errorThrown){
			console.log(textStatus || errorThrown);
		}
	});
});
