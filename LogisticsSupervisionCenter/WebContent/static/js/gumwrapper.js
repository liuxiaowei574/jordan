/*!
 * GumWrapper v0.13
 * By Daniel Davis under MIT License
 * https://github.com/tagawa/GumWrapper
 */

;(function (window, document) {
    'use strict';
    
    window.GumWrapper = function(elements, success, error) {
		var self = this;
        // Define our error message
        function sendError(message) {
            if (error) {
                var e = new Error();
                e.message = message;
                error(e);
            } else {
                console.error(message);
            }
        }
        
        function videoLoadeddata() {
            var attempts = 10;
            
            function checkVideo() {
                if (attempts > 0) {
                    if (self.video.videoWidth > 0 && self.video.videoHeight > 0) {
                        // Execute success callback function
                        if (success) success(self.video);
                    } else {
                        // Wait a bit and try again
                        window.setTimeout(checkVideo, 500);
                    }
                } else {
                    // Give up after 10 attempts
                    sendError('Unable to play video stream. Is webcam working?');
                }
                attempts--;
            }
            
            checkVideo();
        }
        
        // Try to play the media stream
        function play() {
            var video = document.getElementById(elements.video);
            if (!video) {
                sendError('Unable to find the video element.');
                return;
            }

            function successCallback(stream) {
                // Set the source of the video element with the stream from the camera
                if (video.mozSrcObject !== undefined) {
                    video.mozSrcObject = stream;
                } else {
                    video.src = (window.URL && window.URL.createObjectURL(stream)) || stream;
                }
				self.stream = stream;
				self.video = video;
                video.play();
            }

            function errorCallback(error) {
                sendError('Unable to get webcam stream.' + error.message);
                if(error && error.name) {
                	console.error(error.name + ':' + error.message);
                }
            }

            navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;
            window.URL = window.URL || window.webkitURL || window.mozURL || window.msURL;

            // Call the getUserMedia method with our callback functions
            if (navigator.getUserMedia) {
                navigator.getUserMedia({video: true}, successCallback, errorCallback);
            } else {
                sendError('Native web camera streaming (getUserMedia) not supported in this browser.');
            }
            
            // Check the video dimensions when data has loaded
            video.addEventListener('loadeddata', videoLoadeddata, false);
        }

		//Stop the media stream
		function stop() {
			//每次关闭摄像头，解绑vide元素的加载事件
			video.removeEventListener('loadeddata', videoLoadeddata, false);
			if(self.video && self.video.pause) {
				self.video.pause();//html5未提供摄像头关闭API
			}
			if(self.stream && self.stream.stop) {
				self.stream.stop();//Chrome47+不支持了。
			}
		}

        return {
            play: play,
			stop: stop
        };
    };
})(window, document);
