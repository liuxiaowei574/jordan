<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

 <div class="simpleDirections" role="presentation" id="dir" widgetid="dir" style="width:220px;">
    <div data-dojo-attach-point="_widgetContainer" class="esriDirectionsContainer esriStopsAdd esriStopsOptionsEnabled esriStopsRemovable" role="presentation">
        <div class="esriStopsContainer" role="presentation">
            
            <div class="esriStopsTableContainer">
                <table class="esriStops dojoDndSource dojoDndTarget dojoDndContainer" data-dojo-attach-point="_dndNode">
                <tr class="esriStop dojoDndItem esriStopOrigin" style="" id="dojoUnique1">
		                <td class="dojoDndHandle esriStopDnDHandleHidden"></td>
		                <td class="esriStopIconColumn">
			                <div class="esriStopIcon dojoDndHandle" data-center-at="true">1</div>
		                </td>
		                <td class="esriStopGeocoderColumn"><div role="presentation" class="arcgisSearch esriInnerGeocoder" id="esri_dijit_Search_1" widgetid="esri_dijit_Search_1" style="display: block;">
							  <div role="presentation" class="searchGroup hasValue" data-dojo-attach-point="containerNode">
							    <div data-dojo-attach-point="expandNode" class="searchExpandContainer">
							      <div class="searchAnimate">
							        <div role="button" title="搜索" id="esri_dijit_Search_1_menu_button" class="searchBtn searchToggle" tabindex="0" data-dojo-attach-point="sourcesBtnNode">
							          <span aria-hidden="true" role="presentation" class="searchIcon esri-icon-down-arrow"></span><span class="sourceName" data-dojo-attach-point="sourceNameNode">Esri World Geocoder</span>
							        </div>
							        <div class="searchInputGroup">
							          <form data-dojo-attach-point="formNode">
							            <input maxlength="128" autocomplete="off" type="text" tabindex="0" class="searchInput" value="" aria-haspopup="true" id="esri_dijit_Search_1_input" data-dojo-attach-point="inputNode" role="textbox" placeholder="查找地址或地点" title="停止#1">
							          </form>
							          <div role="button" class="searchClear" tabindex="0" data-dojo-attach-point="clearNode" title="清除搜索"><span aria-hidden="true" class="searchIcon esri-icon-close searchClose"></span><span aria-hidden="true" class="searchIcon esri-icon-loading-indicator searchSpinner"></span>
							          </div>
							          <div data-dojo-attach-point="suggestionsNode" class="searchMenu suggestionsMenu"><div><ul role="menu"><li data-index="0" data-source-index="0" role="menuitem" tabindex="-1"><strong>北京</strong>, Beijing, China</li><li data-index="1" data-source-index="0" role="menuitem" tabindex="-1"><strong>北京</strong>首都国际机场, Beijing, China</li><li data-index="2" data-source-index="0" role="menuitem" tabindex="-1"><strong>北京</strong>, China</li><li data-index="3" data-source-index="0" role="menuitem" tabindex="-1"><strong>北京</strong>动物园, Beijing, China</li><li data-index="4" data-source-index="0" role="menuitem" tabindex="-1"><strong>北京</strong>, 三原市, 広島県, 日本</li><li data-index="5" data-source-index="0" role="menuitem" tabindex="-1"><strong>北京</strong>ハウス, 四国中央市, 愛媛県, 日本</li></ul></div></div>
							        </div>
							      </div>
							    </div>
							    <div role="button" title="搜索" class="searchBtn searchSubmit" tabindex="0" data-dojo-attach-point="submitNode">
							      <span aria-hidden="true" role="presentation" class="searchIcon esri-icon-search"></span>
							      <span class="searchButtonText">搜索</span>
							    </div>
							    <div data-dojo-attach-point="sourcesNode" class="searchMenu sourcesMenu"></div>
							    <div data-dojo-attach-point="noResultsMenuNode" class="searchMenu noResultsMenu"></div>
							    <div class="searchClearFloat"></div>
							  </div>
							</div>
						</td>
						<td class="esriStopIconRemoveColumn">
							<div class="esriStopIconRemoveHidden" role="button" data-remove="true"></div>
						</td>
						<td data-reverse-td="true" rowspan="6" class="esriStopReverseColumn"><div role="button" class="esriStopsReverse" data-reverse-stops="true" title="反转方向"></div></td>
				</tr>


				<tr class="esriStop dojoDndItem" style="" id="dojoUnique5">
					<td class="dojoDndHandle esriStopDnDHandleHidden"></td>
					<td class="esriStopIconColumn"><div class="esriStopIcon dojoDndHandle" data-center-at="true">2</div></td>
					<td class="esriStopGeocoderColumn"><div role="presentation" class="arcgisSearch esriInnerGeocoder" id="esri_dijit_Search_5" widgetid="esri_dijit_Search_5" style="display: block;">
					  <div role="presentation" class="searchGroup hasValue" data-dojo-attach-point="containerNode">
					    <div data-dojo-attach-point="expandNode" class="searchExpandContainer">
					      <div class="searchAnimate">
					        <div role="button" title="搜索" id="esri_dijit_Search_5_menu_button" class="searchBtn searchToggle" tabindex="0" data-dojo-attach-point="sourcesBtnNode">
					          <span aria-hidden="true" role="presentation" class="searchIcon esri-icon-down-arrow"></span><span class="sourceName" data-dojo-attach-point="sourceNameNode">Esri World Geocoder</span>
					        </div>
					        <div class="searchInputGroup">
					          <form data-dojo-attach-point="formNode">
					            <input maxlength="128" autocomplete="off" type="text" tabindex="0" class="searchInput" value="" aria-haspopup="true" id="esri_dijit_Search_5_input" data-dojo-attach-point="inputNode" role="textbox" placeholder="查找地址或地点" title="停止#2">
					          </form>
					          <div role="button" class="searchClear" tabindex="0" data-dojo-attach-point="clearNode" title="清除搜索"><span aria-hidden="true" class="searchIcon esri-icon-close searchClose"></span><span aria-hidden="true" class="searchIcon esri-icon-loading-indicator searchSpinner"></span>
					          </div>
					          <div data-dojo-attach-point="suggestionsNode" class="searchMenu suggestionsMenu"><div><ul role="menu"><li data-index="0" data-source-index="0" role="menuitem" tabindex="-1"><strong>上海</strong>, Shanghai, China</li><li data-index="1" data-source-index="0" role="menuitem" tabindex="-1"><strong>上海</strong>虹桥国际机场, Shanghai, China</li><li data-index="2" data-source-index="0" role="menuitem" tabindex="-1"><strong>上海</strong>浦东国际机场, Shanghai, China</li><li data-index="3" data-source-index="0" role="menuitem" tabindex="-1"><strong>上海</strong>, China</li><li data-index="4" data-source-index="0" role="menuitem" tabindex="-1"><strong>上海</strong>府体育館, 村上市, 新潟県, 日本</li><li data-index="5" data-source-index="0" role="menuitem" tabindex="-1"><strong>上海</strong>道歯科, 桜井市, 奈良県, 日本</li></ul></div></div>
					        </div>
					      </div>
					    </div>
					    <div role="button" title="搜索" class="searchBtn searchSubmit" tabindex="0" data-dojo-attach-point="submitNode">
					      <span aria-hidden="true" role="presentation" class="searchIcon esri-icon-search"></span>
					      <span class="searchButtonText">搜索</span>
					    </div>
					    <div data-dojo-attach-point="sourcesNode" class="searchMenu sourcesMenu"></div>
					    <div data-dojo-attach-point="noResultsMenuNode" class="searchMenu noResultsMenu"></div>
					    <div class="searchClearFloat"></div>
					  </div>
					</div>
					</td>
					<td class="esriStopIconRemoveColumn">
					<div class="esriStopIconRemoveHidden" role="button" data-remove="true"></div>
					</td>
				</tr>

				<tr class="esriStop dojoDndItem dojoDndItemAnchor esriStopDestination dojoDndItemOver" style="" id="dojoUnique6">
					<td class="dojoDndHandle esriStopDnDHandle"><!-- 切换左侧鼠标以上效果esriStopDnDHandleHidden --></td>
					<td class="esriStopIconColumn"><div class="esriStopIcon dojoDndHandle" data-center-at="true">3</div></td>
					<td class="esriStopGeocoderColumn"><div role="presentation" class="arcgisSearch esriInnerGeocoder" id="esri_dijit_Search_6" widgetid="esri_dijit_Search_6" style="display: block;">
					  <div role="presentation" class="searchGroup hasValue" data-dojo-attach-point="containerNode">
					    <div data-dojo-attach-point="expandNode" class="searchExpandContainer">
					      <div class="searchAnimate">
					        <div role="button" title="搜索" id="esri_dijit_Search_6_menu_button" class="searchBtn searchToggle" tabindex="0" data-dojo-attach-point="sourcesBtnNode">
					          <span aria-hidden="true" role="presentation" class="searchIcon esri-icon-down-arrow"></span><span class="sourceName" data-dojo-attach-point="sourceNameNode">Esri World Geocoder</span>
					        </div>
					        <div class="searchInputGroup">
					          <form data-dojo-attach-point="formNode">
					            <input maxlength="128" autocomplete="off" type="text" tabindex="0" class="searchInput" value="" aria-haspopup="true" id="esri_dijit_Search_6_input" data-dojo-attach-point="inputNode" role="textbox" placeholder="查找地址或地点" title="停止#3">
					          </form>
					          <div role="button" class="searchClear" tabindex="0" data-dojo-attach-point="clearNode" title="清除搜索"><span aria-hidden="true" class="searchIcon esri-icon-close searchClose"></span><span aria-hidden="true" class="searchIcon esri-icon-loading-indicator searchSpinner"></span>
					          </div>
					          <div data-dojo-attach-point="suggestionsNode" class="searchMenu suggestionsMenu"><div><ul role="menu"><li data-index="0" data-source-index="0" role="menuitem" tabindex="-1"><strong>杭州</strong>, Zhejiang, China</li><li data-index="1" data-source-index="0" role="menuitem" tabindex="-1"><strong>杭州</strong>萧山国际机场, Zhejiang, China</li><li data-index="2" data-source-index="0" role="menuitem" tabindex="-1"><strong>杭州</strong>, 盛岡市, 岩手県, 日本</li><li data-index="3" data-source-index="0" role="menuitem" tabindex="-1"><strong>杭州</strong>叫花麵館, 台灣台北市漢口街二段</li><li data-index="4" data-source-index="0" role="menuitem" tabindex="-1"><strong>杭州</strong>城茶藝ktv, 台灣台中市十甲東路 691</li><li data-index="5" data-source-index="0" role="menuitem" tabindex="-1"><strong>杭州</strong>小籠湯包, 台灣台北市<strong>杭州</strong>南路二段 17</li></ul></div></div>
					        </div>
					      </div>
					    </div>
					    <div role="button" title="搜索" class="searchBtn searchSubmit" tabindex="0" data-dojo-attach-point="submitNode">
					      <span aria-hidden="true" role="presentation" class="searchIcon esri-icon-search"></span>
					      <span class="searchButtonText">搜索</span>
					    </div>
					    <div data-dojo-attach-point="sourcesNode" class="searchMenu sourcesMenu"></div>
					    <div data-dojo-attach-point="noResultsMenuNode" class="searchMenu noResultsMenu"></div>
					    <div class="searchClearFloat"></div>
					  </div>
					</div>
					</td>
					<td class="esriStopIconRemoveColumn"><div class=" esriStopIconRemove" role="button" data-remove="true"><!-- 切换显示esriStopIconRemoveHidden --></div></td>
				</tr>

				<tr class="esriStop dojoDndItem" style="" id="dojoUnique8"><td class="dojoDndHandle esriStopDnDHandleHidden"></td><td class="esriStopIconColumn"><div class="esriStopIcon dojoDndHandle" data-center-at="true">4</div></td><td class="esriStopGeocoderColumn"><div role="presentation" class="arcgisSearch esriInnerGeocoder" id="esri_dijit_Search_8" widgetid="esri_dijit_Search_8" style="display: block;">
				  <div role="presentation" class="searchGroup" data-dojo-attach-point="containerNode">
				    <div data-dojo-attach-point="expandNode" class="searchExpandContainer">
				      <div class="searchAnimate">
				        <div role="button" title="搜索" id="esri_dijit_Search_8_menu_button" class="searchBtn searchToggle" tabindex="0" data-dojo-attach-point="sourcesBtnNode">
				          <span aria-hidden="true" role="presentation" class="searchIcon esri-icon-down-arrow"></span><span class="sourceName" data-dojo-attach-point="sourceNameNode">Esri World Geocoder</span>
				        </div>
				        <div class="searchInputGroup">
				          <form data-dojo-attach-point="formNode">
				            <input maxlength="128" autocomplete="off" type="text" tabindex="0" class="searchInput" value="" aria-haspopup="true" id="esri_dijit_Search_8_input" data-dojo-attach-point="inputNode" role="textbox" placeholder="查找地址或地点" title="停止#4">
				          </form>
				          <div role="button" class="searchClear" tabindex="0" data-dojo-attach-point="clearNode"><span aria-hidden="true" class="searchIcon esri-icon-close searchClose"></span><span aria-hidden="true" class="searchIcon esri-icon-loading-indicator searchSpinner"></span>
				          </div>
				          <div data-dojo-attach-point="suggestionsNode" class="searchMenu suggestionsMenu"></div>
				        </div>
				      </div>
				    </div>
				    <div role="button" title="搜索" class="searchBtn searchSubmit" tabindex="0" data-dojo-attach-point="submitNode">
				      <span aria-hidden="true" role="presentation" class="searchIcon esri-icon-search"></span>
				      <span class="searchButtonText">搜索</span>
				    </div>
				    <div data-dojo-attach-point="sourcesNode" class="searchMenu sourcesMenu"></div>
				    <div data-dojo-attach-point="noResultsMenuNode" class="searchMenu noResultsMenu"></div>
				    <div class="searchClearFloat"></div>
				  </div>
				</div></td>
				<td class="esriStopIconRemoveColumn"><div class="esriStopIconRemoveHidden" role="button" data-remove="true"></div></td></tr>


<tr class="esriStop dojoDndItem" style="" id="dojoUnique9"><td class="dojoDndHandle esriStopDnDHandleHidden"></td><td class="esriStopIconColumn"><div class="esriStopIcon dojoDndHandle" data-center-at="true">5</div></td><td class="esriStopGeocoderColumn"><div role="presentation" class="arcgisSearch esriInnerGeocoder" id="esri_dijit_Search_9" widgetid="esri_dijit_Search_9" style="display: block;">
  <div role="presentation" class="searchGroup" data-dojo-attach-point="containerNode">
    <div data-dojo-attach-point="expandNode" class="searchExpandContainer">
      <div class="searchAnimate">
        <div role="button" title="搜索" id="esri_dijit_Search_9_menu_button" class="searchBtn searchToggle" tabindex="0" data-dojo-attach-point="sourcesBtnNode">
          <span aria-hidden="true" role="presentation" class="searchIcon esri-icon-down-arrow"></span><span class="sourceName" data-dojo-attach-point="sourceNameNode">Esri World Geocoder</span>
        </div>
        <div class="searchInputGroup">
          <form data-dojo-attach-point="formNode">
            <input maxlength="128" autocomplete="off" type="text" tabindex="0" class="searchInput" value="" aria-haspopup="true" id="esri_dijit_Search_9_input" data-dojo-attach-point="inputNode" role="textbox" placeholder="查找地址或地点" title="查找地址或地点">
          </form>
          <div role="button" class="searchClear" tabindex="0" data-dojo-attach-point="clearNode"><span aria-hidden="true" class="searchIcon esri-icon-close searchClose"></span><span aria-hidden="true" class="searchIcon esri-icon-loading-indicator searchSpinner"></span>
          </div>
          <div data-dojo-attach-point="suggestionsNode" class="searchMenu suggestionsMenu"></div>
        </div>
      </div>
    </div>
    <div role="button" title="搜索" class="searchBtn searchSubmit" tabindex="0" data-dojo-attach-point="submitNode">
      <span aria-hidden="true" role="presentation" class="searchIcon esri-icon-search"></span>
      <span class="searchButtonText">搜索</span>
    </div>
    <div data-dojo-attach-point="sourcesNode" class="searchMenu sourcesMenu"></div>
    <div data-dojo-attach-point="noResultsMenuNode" class="searchMenu noResultsMenu"></div>
    <div class="searchClearFloat"></div>
  </div>
</div></td><td class="esriStopIconRemoveColumn"><div class="esriStopIconRemoveHidden" role="button" data-remove="true"></div></td></tr>

<tr class="esriStop dojoDndItem" style="" id="dojoUnique10"><td class="dojoDndHandle esriStopDnDHandleHidden"></td><td class="esriStopIconColumn"><div class="esriStopIcon dojoDndHandle" data-center-at="true">6</div></td><td class="esriStopGeocoderColumn"><div role="presentation" class="arcgisSearch esriInnerGeocoder" id="esri_dijit_Search_10" widgetid="esri_dijit_Search_10" style="display: block;">
  <div role="presentation" class="searchGroup" data-dojo-attach-point="containerNode">
    <div data-dojo-attach-point="expandNode" class="searchExpandContainer">
      <div class="searchAnimate">
        <div role="button" title="搜索" id="esri_dijit_Search_10_menu_button" class="searchBtn searchToggle" tabindex="0" data-dojo-attach-point="sourcesBtnNode">
          <span aria-hidden="true" role="presentation" class="searchIcon esri-icon-down-arrow"></span><span class="sourceName" data-dojo-attach-point="sourceNameNode">Esri World Geocoder</span>
        </div>
        <div class="searchInputGroup">
          <form data-dojo-attach-point="formNode">
            <input maxlength="128" autocomplete="off" type="text" tabindex="0" class="searchInput" value="" aria-haspopup="true" id="esri_dijit_Search_10_input" data-dojo-attach-point="inputNode" role="textbox" placeholder="查找地址或地点" title="查找地址或地点">
          </form>
          <div role="button" class="searchClear" tabindex="0" data-dojo-attach-point="clearNode"><span aria-hidden="true" class="searchIcon esri-icon-close searchClose"></span><span aria-hidden="true" class="searchIcon esri-icon-loading-indicator searchSpinner"></span>
          </div>
          <div data-dojo-attach-point="suggestionsNode" class="searchMenu suggestionsMenu"></div>
        </div>
      </div>
    </div>
    <div role="button" title="搜索" class="searchBtn searchSubmit" tabindex="0" data-dojo-attach-point="submitNode">
      <span aria-hidden="true" role="presentation" class="searchIcon esri-icon-search"></span>
      <span class="searchButtonText">搜索</span>
    </div>
    <div data-dojo-attach-point="sourcesNode" class="searchMenu sourcesMenu"></div>
    <div data-dojo-attach-point="noResultsMenuNode" class="searchMenu noResultsMenu"></div>
    <div class="searchClearFloat"></div>
  </div>
</div></td><td class="esriStopIconRemoveColumn"><div class="esriStopIconRemoveHidden" role="button" data-remove="true"></div></td>
</tr>
</table>


            </div>
            <div class="esriClear"></div>
            <div class="esriStopsButtons">
                <div class="esriStopsAddDestination">
                    <div tabindex="0" role="button" data-dojo-attach-point="_activateButtonNode" title="单击地图或修改带有路点的路径以增加停靠点" class="esriActivateButton esriDirectionsButton esriDirectionsTabButton" data-blur-on-click="true" style="display: inline-block;"></div>
                    <div role="button" tabindex="0" class="esriLinkButton esriStopsAddDestinationBtn" data-dojo-attach-point="_addDestinationNode" data-blur-on-click="true" style="display: inline;">添加目的地</div>
                </div>
                <div class="esriTravelModesContainer" data-dojo-attach-point="_travelModeContainerNode" style="display: block;">
                    <table class="esriTravelModesDDL dijitSelectFixedWidth dijitValidationTextBoxFixedWidth dijitSelect dijitValidationTextBox" data-dojo-attach-point="_buttonNode,tableNode,focusNode,_popupStateNode" cellspacing="0" cellpadding="0" role="listbox" aria-haspopup="true" tabindex="0" id="dijit_form_Select_0" widgetid="dijit_form_Select_0" aria-expanded="false" aria-invalid="false" style="-webkit-user-select: none; width: 100%;"><tbody role="presentation">
                    <tr role="presentation"><td class="dijitReset dijitStretch dijitButtonContents" role="presentation"><div class="dijitReset dijitInputField dijitButtonText" data-dojo-attach-point="containerNode,textDirNode" role="presentation"><span role="option" aria-selected="true" class="dijitReset dijitInline dijitSelectLabel dijitValidationTextBoxLabel "><div class="esriTravelModesDirectionsIcon esriTravelModesTypeDrivingTime">&nbsp;</div><div class="esriTravelModesTypeName">Driving Time</div></span></div><div class="dijitReset dijitValidationContainer"><input class="dijitReset dijitInputField dijitValidationIcon dijitValidationInner" value="Χ " type="text" tabindex="-1" readonly="readonly" role="presentation"></div><input type="hidden" data-dojo-attach-point="valueNode" value="Driving Time" aria-hidden="true"></td><td class="dijitReset dijitRight dijitButtonNode dijitArrowButton dijitDownArrowButton dijitArrowButtonContainer" data-dojo-attach-point="titleNode" role="presentation"><input class="dijitReset dijitInputField dijitArrowButtonInner" value="▼ " type="text" tabindex="-1" readonly="readonly" role="presentation"></td></tr></tbody></table>
                </div>
                <div tabindex="0" role="button" class="esriLinkButton esriStopsOptionsButton" data-dojo-attach-point="_optionsButtonNode" data-blur-on-click="true">显示更多选项</div>
            	<div class="esriClear"></div>
            </div>
            <div role="presentation" class="esriStopsOptionsMenu" data-dojo-attach-point="_optionsMenuNode">
                <div class="esriOptionsCheckboxes">
                    <ul>
                        <li class="esriFindOptimalOrderOption" data-dojo-attach-point="_findOptimalOrderItemNode" style="display: none;"><input tabindex="0" data-dojo-attach-point="_findOptimalOrderNode" type="checkbox" id="dir_find_optimal_order"><label for="dir_find_optimal_order">优化顺序</label></li>
                        <li class="esriReturnToStartOption" data-dojo-attach-point="_returnToStartItemNode" style="display: block;"><input tabindex="0" data-dojo-attach-point="_returnToStartNode" type="checkbox" id="dir_stopsReturnToStart"><label for="dir_stopsReturnToStart">返回起点</label></li>
                        <li class="esriUseTrafficOption" data-dojo-attach-point="_useTrafficItemNode" style="display: block;"><input tabindex="0" data-dojo-attach-point="_useTrafficNode" type="checkbox" id="dir_stopsUseTraffic"><label for="dir_stopsUseTraffic">使用交通流量</label></li>
                    </ul>
                </div>
                <div class="esriOptionsToggleContainer">
                    <div class="esriOptionsUnitsContainer" data-dojo-attach-point="_agolDistanceUnitsNode" style="display: block;">
                        <div tabindex="0" role="button" class="esriOptionsUnitsMi esriDirectionsButton" data-dojo-attach-point="_useMilesNode" data-blur-on-click="true">mi</div>
                        <div tabindex="0" role="button" class="esriOptionsUnitsKm esriDirectionsButton esriDirectionsTabLastButton esriDirectionsPressedButton" data-dojo-attach-point="_useKilometersNode" data-blur-on-click="true">km</div>
                        <div class="esriClear"></div>
                    </div>
                    <div class="esriClear"></div>
                </div>
                <div class="esriClear"></div>
            </div>
            <div class="esriStopsGetDirectionsContainer">
                <div class="getDirectionsBtnContainer">
                    <div tabindex="0" role="button" class="esriDirectionsButton esriStopsGetDirections" data-dojo-attach-point="_getDirectionsButtonNode" data-blur-on-click="true">导航</div>
                    <div tabindex="0" role="button" class="esriLinkButton esriStopsClearDirections" data-dojo-attach-point="_clearDirectionsButtonNode" data-blur-on-click="true" style="display: none;">清除</div>
                </div>
                <div class="savePrintBtnContainer" data-dojo-attach-point="_savePrintBtnContainer" style="display: inline-block;">
                    <div tabindex="0" role="button" class="esriResultsSave esriDirectionsButton esriDirectionsTabButton" data-blur-on-click="true" data-dojo-attach-point="_saveMenuButton" title="保存并共享路径" style="display: none;"></div>
                    <div tabindex="0" role="button" class="esriResultsPrint esriDirectionsButton esriDirectionsTabButton" data-blur-on-click="true" data-dojo-attach-point="_printButton" title="打印" style="display: inline-block;"></div>
                </div>
            </div>
            <div role="presentation" class="esriStopsOptionsMenu" data-dojo-attach-point="_saveMenuNode" style="display: none;">
                <div class="esriLayerNameLabel">结果图层名称</div>
                <input type="text" data-dojo-attach-point="_outputLayerContainer">
                <div data-dojo-attach-point="_folderSelectorContainer"></div>
                <div tabindex="0" role="button" class="esriDirectionsButton esriSaveButton esriDisabledDirectionsButton" data-dojo-attach-point="_saveButton" data-blur-on-click="true">保存</div>
                <div tabindex="0" role="button" class="esriLinkButton esriSaveAsButton" data-dojo-attach-point="_saveAsButton" data-blur-on-click="true" style="display: none;">另存为新的</div>
            </div>
    	</div>
    	
    	
    	<div class="esriResultsContainer" role="presentation">
        	<div class="esriRoutesContainer" data-dojo-attach-point="_resultsNode" role="presentation"><div class="esriResultsSummary">1,575.08 千米 · 18 小时 17 分钟</div></div>
    	</div>
    </div>
</div>

