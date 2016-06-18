/**
 * GOOGLE地图开发使用工具
 * @author 赵磊峰修改
 * @date 2014-07-24
 * @notice 地图容器的z-index不能小于0，否则鼠标地图无法进行地图操作
 * @updateLog:添加了地图覆盖物视图显示自适应（核心代码map.fitBounds(bounds);），轨迹回放修

改测试
*/
(function(){
    window.map={};
    window.lineFeature=null;
    window.markers=[];
    window.infoWindow=null;
    window.GoogleUtil={};
    window.trackingMarkers=null;
    window.trackRoadPath = null;
    GoogleUtil={
        CONSTANT:{
            mapkey:'AIzaSyAY-HsXXPsBUqsbQLDFO8kpNWLANwH0E7k',
            container:"map",
            DEFAULT_ZOOM:12,
            zoomAddFeature:19,
            centerLat:30.65721817,
            centerLng:104.06594494,
            mapstatus:false,
            isnewMap:false,
            ZOOM_MAX:19,
            ZOOM_MIN:1,
            markerSize:32
        },
        /**
         * 控制地图显示范围为中国
         */
        mapShowBounds:function(){
            var strictBounds = new google.maps.LatLngBounds(
                new google.maps.LatLng(14.48003790418668, 66.28120434863283),
                new google.maps.LatLng(54.44617552862156, 143.71284497363283)
            );
            google.maps.event.addListener(map, 'dragend',function() {
                if (strictBounds.contains(map.getCenter())) return;
                var c = map.getCenter(),
                x = c.lng(),
                y = c.lat(),
                maxX = strictBounds.getNorthEast().lng(),
                maxY = strictBounds.getNorthEast().lat(),
                minX = strictBounds.getSouthWest().lng(),
                minY = strictBounds.getSouthWest().lat();
                if (x < minX) x = minX;
                if (x > maxX) x = maxX;
                if (y < minY) y = minY;
                if (y > maxY) y = maxY;
                map.setCenter(new google.maps.LatLng(y, x));
            });
        },
        /**
         * 控制地图的缩放级别
         */
        limitShowMapZoom:function(zoom){
            this.CONSTANT.zoomMax=zoom;
            var limitedZoom=this.CONSTANT.zoomMax;
            google.maps.event.addListener(map, 'zoom_changed',function() {
                if (map.getZoom() < limitedZoom) map.setZoom(limitedZoom);
            });
        },
        /**
         * 异步加载谷歌API
         */
        loadScript:function(){
            var script = document.createElement("script");
            script.type = "text/javascript";
            script.src = "http://maps.googleapis.com/maps/api/js?v=3&key="+this.CONSTANT.mapkey+"&sensor=false&libraries=drawing,places";
            document.body.appendChild(script);
        },
        /**
         * 谷歌街道
         */
        initStreetMap:function(key){
          this.CONSTANT.mapkey=key|| this.CONSTANT.mapkey;
          var mapOptions = {
                center: new google.maps.LatLng

(GoogleUtil.CONSTANT.centerLat,GoogleUtil.CONSTANT.centerLng),
                zoom: this.CONSTANT.DEFAULT_ZOOM,
                panControl: true,
                zoomControl: true,
                mapTypeControl: false,
                scaleControl: true,
                scrollwheel:true,
                draggable:true,
                overviewMapControl: true,
                streetViewControl:true,
                mapTypeId: google.maps.MapTypeId.ROADMAP,
                navigationControlOptions: {
                        style: google.maps.NavigationControlStyle.ZOOM_PAN,
                        position: google.maps.ControlPosition.TOP_LEFT
                },
                zoomControlOptions:{
                     style: google.maps.ZoomControlStyle.SMALL,//DEFAULT,LARGE,SMALL
                     position: google.maps.ControlPosition.TOP_LEFT
                }
            };
            map = new google.maps.Map(document.getElementById

(this.CONSTANT.container),mapOptions);
        },
        /**
         * 谷歌卫星
         */
        initSatelliteMap:function(key){
            this.CONSTANT.mapkey=key|| this.CONSTANT.mapkey;
            var mapOptions = {
                    center: new google.maps.LatLng

(GoogleUtil.CONSTANT.centerLat,GoogleUtil.CONSTANT.centerLng),
                    zoom: this.CONSTANT.DEFAULT_ZOOM,
                    panControl: true,
                    zoomControl: true,
                    mapTypeControl: false,
                    scrollwheel:true,
                    draggable:true,
                    scaleControl: true,
                    overviewMapControl: true,
                    mapTypeId: google.maps.MapTypeId.SATELLITE,
                    navigationControlOptions: {
                        style: google.maps.NavigationControlStyle.ZOOM_PAN,
                        position: google.maps.ControlPosition.TOP_LEFT
                    },
                    zoomControlOptions:{
                         style: google.maps.ZoomControlStyle.SMALL,//DEFAULT,LARGE,SMALL
                         position: google.maps.ControlPosition.TOP_LEFT
                    }
                };
            map = new google.maps.Map(document.getElementById

(this.CONSTANT.container),mapOptions);
        },
        /**
         * 谷歌手机
         */
        initMobileStreetMap:function(container,key){
             this.CONSTANT.mapkey=key|| this.CONSTANT.mapkey;
              var mapOptions = {
                    center: new google.maps.LatLng

(GoogleUtil.CONSTANT.centerLat,GoogleUtil.CONSTANT.centerLng),
                    zoom: this.CONSTANT.DEFAULT_ZOOM,
                    panControl: false,
                    zoomControl: true,
                    mapTypeControl: false,
                    scaleControl: true,
                    scrollwheel:true,
                    draggable:true,
                    overviewMapControl: true,
                    streetViewControl:true,
                    mapTypeId: google.maps.MapTypeId.ROADMAP,
                    navigationControlOptions: {
                            style: google.maps.NavigationControlStyle.ZOOM_PAN,
                            position: google.maps.ControlPosition.TOP_LEFT
                    },
                    zoomControlOptions:{
                         style: google.maps.ZoomControlStyle.SMALL,//DEFAULT,LARGE,SMALL
                         position: google.maps.ControlPosition.TOP_LEFT
                    }
                };
                map = new google.maps.Map(document.getElementById(container||

this.CONSTANT.container),mapOptions);
                //this.mapShowBounds();
        },
        /**
         * 居中或缩放
         */
        centerAndZoom:function(latlng,zoom){
            if(latlng) map.setCenter(latlng);
            if(zoom) map.setZoom(zoom);
        },
        /**
         * 获取图片对象
         */
        getIcon:function(imageUrl,size){
            var imgSize=size||32;
            var offSize=imgSize/2;
            var defaultSize=new google.maps.Size(imgSize, imgSize);
            var myIcon={
                    url: imageUrl,
                    size: defaultSize,
                    scaledSize:new google.maps.Size(imgSize,imgSize),
                    origin: new google.maps.Point(0,0),
                    anchor: new google.maps.Point(offSize,offSize)
            };
            return myIcon;
        },
        /**
         * 创建一个地图bounds对象
         * @param points
         */
        createBounds:function(points){
            if(points) {
                var bounds=new google.maps.LatLngBounds();
                for ( var i = 0; i < points.length; i++) {
                    var point=points[i];
                    if(point){
                        bounds.extend(point);
                    }
                }
                return bounds;
            }
            return null;
        },
        /**
         * 设置适合的地图边界范围Bounds
         * @param points
         */
        panToBounds:function(points){
            if(points){
                var bounds=this.createBounds(points);
                if(bounds) map.panToBounds(bounds);
            }
        },
        /**
         * 设置合适的覆盖物显示范围(覆盖物聚合)
         */
        getViewport:function(points){
            if(points){
                var bounds=this.createBounds(points);
                if(bounds) {
                    map.fitBounds(bounds);
                }
            }
        }
    };
    
    var iterator=0,scount=0,playStatus=0;
    var vehicleMarker;
    var infowindows = [];
    var lastIndex=-1;  
    GoogleUtil.tools={
        /**
         * 创建信息窗体
         */
        createInfoWindow:function(latlng,htmlContent){
            var infowindow = new google.maps.InfoWindow({
                  content: htmlContent,
                  position:latlng,
                  disableAutoPan:false
              });
            return infowindow;
        },
        /**
         * 添加信息窗体
         */
        addInfoWindow:function(latlng,htmlContent,isCenter){
            if(!infoWindow){
                infoWindow=this.createInfoWindow(latlng, htmlContent);
            }else{
                infoWindow.close();
                infoWindow.setPosition(latlng);
                infoWindow.setContent(htmlContent);
            }
            infoWindow.open(map);
            if(isCenter) map.setCenter(latlng);
        },
        /**
         * 创建标注
         */
        
        createMarker:function(id,title,point,icon){
            var marker = new google.maps.Marker({
                position: point,
                map: map,
                icon:icon,
                id:id
             });
             marker.id=id;
             marker.setTitle(title);
            
            return marker;
        },
        /**
         * 添加标注
         */
         addMarker:function(id,title,point,icon){
             var marker =this.createMarker(id,title,point,icon);
             markers.push(marker);
             marker.setMap(map);
             return marker;
         },
         /**
          * 批量添加标注
          */
         addMarkers:function(points){
             if(points){
                 for ( var i = 0; i < points.length; i++) {
                    var point=points[i];
                    this.addMarker(point);
                 }
             }
         },
         /**
          * 添加跟踪轨迹线条
          */
         addLineFeature:function(id,points,style){
             var lineSymbol = {
                       path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
                       //scale: 2,
                       strokeColor: 'red'
                 };
             var defaultStyle={
                        path: points,
                        icons: [{
                              icon: lineSymbol,
                              offset: '0%'
                                }],
                        map: map
                      };
             lineFeature = new google.maps.Polyline(style||defaultStyle);
             lineFeature.id=id;
             lineFeature.track=id;
             markers.push(lineFeature);
             return lineFeature;
         },
         /**
          * 添加折线(轨迹,包括起点、终点)
          */
         addLineFeatureAndStartAndEndPoint:function(spObj,points, 

startImageUrk,endImageUrk,lineStyle){
             var len=points.length;
             var index =len - 1;
             var startPoint = points[0];
             var endPoint =points[index];
             var startIcon = GoogleUtil.getIcon(startImageUrk,20);
             var endIcon = GoogleUtil.getIcon(endImageUrk,20);
             this.addMarker("start", spObj.start, startPoint, startIcon);
             this.addMarker("end", spObj.end, endPoint, endIcon);
             if(len>=2){
                var d=(len/2)+"";
                d=parseInt(d);
                GoogleUtil.centerAndZoom(points[d],12);
             }
             this.addLineFeature("track_line",points,lineStyle);
         },
         /**
          * 标注动画
          */
         markerAnimate:{
             dropSetTimeout:{
                 drop:function(points){
                     iterator=0;
                     for (var i = 0; i < points.length; i++) {
                            setTimeout(function() {
                                GoogleUtil.tools.markerAnimate.dropSetTimeout.addMarker

(points);
                            }, i * 200);
                     }
                 },
                 addMarker:function(points){
                     markers.push(new google.maps.Marker({
                            position: points[iterator],
                            map: map,
                            draggable: false,
                            animation: google.maps.Animation.DROP
                    }));
                    iterator++;
                 }
             }
         },
         /**
          * 轨迹操作
          */
         track:{
             /**
              * 添加轨迹线条
              */
             addLineTrack:function(points,roadPath){
                 if(points){
                	 trackRoadPath = roadPath;
                     var lineCoordinates=[];
                     for ( var i = 0; i < points.length; i++) {
                          var point=points[i];
                          if(point){
                              lineCoordinates.push(point);
                          }
                     }
                     var lineSymbol = {
                               path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
                               //scale: 2,
                               strokeColor: 'green'
                     };
                     if(null!=lineFeature){
                    	 lineFeature.setMap(null);
                     }
                     lineFeature = new google.maps.Polyline({
                           path: lineCoordinates,
                           strokeColor: '#00ff00',
                           strokeWeight : 3,
                           strokeOpacity : 1,
                           icons: [{
                              icon: lineSymbol,
                              offset: '0%'
                                }],
                           
                           map: map
                    });
                    lineFeature.id="track_line";
                 }
             },
             /**
              * 添加轨迹线条
              */
             clearLineTrack:function(){
            	 if(null!=lineFeature){
                	 lineFeature.setMap(null);
                 }
             },
             /**
              * 添加自定义的轨迹线条或者区域
              */
             addProLineTrack:function(points, preType, preColor, preWeight, preOpacity){
                 if(points){
                     var lineCoordinates=[];
                     for ( var i = 0; i < points.length; i++) {
                          var point=points[i];
                          if(point){
                              lineCoordinates.push(point);
                          }
                     }
                     if(preType == "AREA"){
                 		var prePolygon = new google.maps.Polygon({
                 			paths:lineCoordinates, 
                 			strokeColor:preColor, 
                 			strokeWeight:1, 
                 			strokeOpacity:preOpacity, 
                 			fillColor:preColor,
                 			fillOpacity: 0.5,
                 			map: map
                 		});
                 	}else if(preType="LINE"){
                 		var prePolyline = new google.maps.Polyline({
                            path: lineCoordinates,
                            strokeColor: preColor,
                            strokeWeight : preWeight,
                            strokeOpacity : preOpacity,
                            map: map
                 		});
                	}else if(plannedRouteArea.type="POINT"){
                		
                	}
                 }
             },
             /**
              * 轨迹回放操作
              */
             operate:{
            	 multiple:null,
                 count:0,
                 object:null,
                 addListener:function(moveIcon,times,callback){
                      var animate=setInterval(function() {
                            if(typeof(lineFeature.getPath())!='undefined'){
                        		if(playStatus==1){
                    				scount = scount + 1;
	                            	if(vehicleMarker){
	                            		if(vehicleMarker.getVisible()==false){
	                            			 vehicleMarker.setVisible(true);
	                            		};
	                            		vehicleMarker.setPosition(lineFeature.getPath().getAt(scount-1));
	                            	}else{
	                            		vehicleMarker = new google.maps.Marker({position:lineFeature.getPath().getAt(scount-1),icon:moveIcon,map:map});
	                            	}
	                            	callback(trackRoadPath[scount-1]);
		                            //终点停车
		                            if(scount>=(lineFeature.getPath().length)){
		                                 clearInterval(this);
		                            }
                        		}else if(playStatus==3){
                    				if(scount!=0){
                						scount = scount + 1;
		                            	if(vehicleMarker){
		                            		vehicleMarker.setPosition(lineFeature.getPath().getAt(scount-1));
		                            	}else{
		                            		vehicleMarker = new google.maps.Marker({position:lineFeature.getPath().getAt(scount-1),icon:moveIcon,map:map});
		                            	}
			                            //终点停车
			                            if(scount>=(lineFeature.getPath().length)){
			                                 clearInterval(this);
			                            }
			                            callback(trackRoadPath[scount-1]);
                    				}
                        		}else if(playStatus==4){
                        			if(this.object)clearInterval(this.object);
                        			if(this)clearInterval(this);
	                            	scount=0;
	                            	if(vehicleMarker){
	                            		if(vehicleMarker.getVisible()==false){
	                            			 vehicleMarker.setVisible(true);
	                            		};
	                            		vehicleMarker.setPosition(lineFeature.getPath().getAt(scount-1));
	                            	}else{
	                            		vehicleMarker = new google.maps.Marker({position:lineFeature.getPath().getAt(scount-1),icon:moveIcon,map:map});
	                            	}
	                            	callback(null);
		                            //终点停车
		                            if(scount>=(lineFeature.getPath().length)){
		                                 clearInterval(this);
		                            }
                        		}else if(playStatus==5){
                        			scount = scount + 1;
	                            	if(vehicleMarker){
	                            		if(vehicleMarker.getVisible()==false){
	                            			 vehicleMarker.setVisible(true);
	                            		};
	                            		vehicleMarker.setPosition(lineFeature.getPath().getAt(scount-1));
	                            	}else{
	                            		vehicleMarker = new google.maps.Marker({position:lineFeature.getPath().getAt(scount-1),icon:moveIcon,map:map});
	                            	}
	                            	callback(trackRoadPath[scount-1]);
		                            //终点停车
		                            if(scount>=(lineFeature.getPath().length)){
		                                 clearInterval(this);
		                            }
                        		}
                             else if(playStatus==6){
                        			scount = scount + 1;
	                            	if(vehicleMarker){
	                            		if(vehicleMarker.getVisible()==false){
	                            			 vehicleMarker.setVisible(true);
	                            		};
	                            		vehicleMarker.setPosition(lineFeature.getPath().getAt(scount-1));
	                            	}else{
	                            		vehicleMarker = new google.maps.Marker({position:lineFeature.getPath().getAt(scount-1),icon:moveIcon,map:map});
	                            	}
	                            	callback(trackRoadPath[scount-1]);
		                            //终点停车
		                            if(scount>=(lineFeature.getPath().length)){
		                                 clearInterval(this);
		                            }
		                          
                        		} 
                            }else{
	                            alert('No path!');
	                            clearInterval(this.object);
	                            return;
                            }
                      }, 1000/this.multiple);
                      this.object=animate; 
                 },
                 play:function(moveIcon,time,callback){
                	  this.multiple = time;
                	 if(this.object)clearInterval(this.object,callback);
                      playStatus=1;
                      scount=0;
                      lineFeature.playStatus=playStatus;
                      this.addListener(moveIcon,this.multiple,callback);
                 },
                 continuePlay:function(moveIcon,times,callback){
                	 this.multiple = times;
                	 if(this.object==null)return;
                	 if(this.object)clearInterval(this.object);
                      playStatus=3;
                      lineFeature.playStatus=playStatus;
                      this.addListener(moveIcon,this.multiple,callback);
                 },
                 pause:function(callback){
                      playStatus=2;
                      lineFeature.playStatus=playStatus;
                      if(this.object==null)return;
                      if(this.object)clearInterval(this.object);
                 	  scount = scount+1;
                      //终点停车        /*
                 	  if(scount>=(lineFeature.getPath().length)){
                          clearInterval(this);
                     }
                 },
                 stop:function(moveIcon,time,callback){
                	 this.multiple = time;
                      if(this.object==null)return;
                      if(this.object)clearInterval(this.object);
                      playStatus=4;
                      lineFeature.playStatus=playStatus;
                      scount=-1;
                      this.addListener(moveIcon,this.multiple,callback);
                 },
                 accelerate:function(moveIcon,time,callback){
                	 if(scount==-1||scount==0)return;
                	 this.multiple = time;
                     playStatus=5;
                	 lineFeature.playStatus=playStatus;
                     if(this.object==null)return;
                     if(this.object)clearInterval(this.object);
                     scount = scount+1;
                     this.addListener(moveIcon,this.multiple,callback);
                },
                decelerate:function(moveIcon,time){
                	if(scount==-1||scount==0)return;
                	this.multiple = time;
                    playStatus=6;
               	    lineFeature.playStatus=playStatus;
                    if(this.object==null)return;
                    if(this.object)clearInterval(this.object);
                    scount = scount+1;
                    this.addListener(moveIcon,this.multiple,callback);
                   
               },
                 Rad:function(d){
                	 return d * Math.PI / 180.0;
                 },
                 //计算两点间的距离
                 distanceByLnglat:function(lng1,lat1,lng2,lat2){
                     var radLat1 = Rad(lat1);
                     var radLat2 = Rad(lat2);
                     var a = radLat1 - radLat2;
                     var b = Rad(lng1) - Rad(lng2);
                     var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + 
                         Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
                     s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
                     s = Math.round(s * 10000) / 10000;
                     return s;
                 }
             }
         },
         getOverlayByNodeId:function(id,value){
             for (var i = 0; i < markers.length; i++) {
                var marker=markers[i];
                if(marker[id]==value){
                    return marker;
                } 
             }
             return null;
         },
         /**
          * 删除或显示覆盖物
          */
         deleteOrShowMarkerOverlayers:function(map){
             for (var i = 0; i < markers.length; i++) {
                    if(map==null) markers[i].setVisible(false);
                    markers[i].setMap(map);
             }
             if(map==null)  markers = [];
         },
         /**
          * 删除轨迹
          */
         deleteTrack:function(){
             if(lineFeature){
                 lineFeature.setVisible(false);
                 lineFeature.setMap(null);
             } 
         },
         /**
          * 移除所有覆盖物
          */
         removeAllOverlays:function(){
             for (var i = 0; i < markers.length; i++) {
                 markers[i].setVisible(false);
                 markers[i].setMap(map);
             }
             markers = [];
         },
         /**
          * 移除一个覆盖物
          */
         removeOverlay:function(propertyName,value){
             if(value){
                 for (var i = 0; i < markers.length; i++) {
                     var marker=markers[i];
                     if(marker[propertyName]==value){
                         markers[i].setVisible(false);
                         markers[i].setMap(map);
                     }
                 }
             }
             if(propertyName=="track"||propertyName=="track_line"){
                 if(lineFeature){
                     lineFeature.setVisible(false);
                     lineFeature.setMap(null);
                     lineFeature=null;
                 }
             }
         },
         /**
          * 显示或隐藏标注
          */
         isToShowMarkers:function(markers,bool){
             if(markers){
                 for (var i = 0; i < markers.length; i++) {
                     var marker=markers[i];
                     marker.setVisible(bool);
                 }
             }
         },
         /**
          * 删除轨迹覆盖物
          */
         removeTrackLineWithStartAndEndOverlay:function(){
             this.removeOverlay("id", "track_line");
             this.removeOverlay("id", "track");
             this.removeOverlay("id", "start");
             this.removeOverlay("id", "end");
             if(lineFeature){
                 lineFeature.setVisible(false);
                 lineFeature.setMap(null);
                 lineFeature=null;
             }
             this.removeAllOverlays();
         }
    };
    
    GoogleUtil.event={
            /**
             * 地图缩放事件
             */
            mapZoomChanged:function(markers,zoom){
                var listener=google.maps.event.addListener(map, 'zoom_changed', function

(event) {
                     if(map.getZoom()<zoom){
                         var myMarkers=markers;
                         GoogleUtil.tools.isToShowMarkers(markers,false);//隐藏标注
                         markers=myMarkers;
                     }else{
                         GoogleUtil.tools.isToShowMarkers(markers,true);//显示标注
                     }
                 });
                return listener;
            },
            /**
             * 点击标注事件
             */
            markerClick:function(marker){
                var listener=google.maps.event.addListener(marker, 'click', function(event) 

             {
                     marker.infoWindow.open(map,marker);
                 });
                return listener;
            },
            /**
             * 移除监听对象
             */
            removeListener:function(listener){
                google.maps.event.removeListener(listener);
            }
    };
    
})();
//window.onload= GoogleUtil.loadScript();