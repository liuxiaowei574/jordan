package com.nuctech.ls.model.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.dao.PerformanceAnalysisDao;
import com.nuctech.ls.model.vo.analysis.PerformanceAnalysisVo;

import net.sf.json.JSONObject;

@Service
@Transactional
public class PerformanceAnalysisService extends LSBaseService {
	@Resource
	private PerformanceAnalysisDao performanceAnalysisDao;

	public List<PerformanceAnalysisVo> findPerformanceAnalysis(PageQuery<Map> pageQuery) {
//		List<PerformanceAnalysisVo> list = new ArrayList<>();
		List<PerformanceAnalysisVo> userOnlineList = performanceAnalysisDao.findUserOnline(pageQuery);
		JSONObject dealAlarmJson = performanceAnalysisDao.findDealAlarm();
		if(userOnlineList!=null){
			for(PerformanceAnalysisVo entity : userOnlineList){
				String key = String.format("%s%s%s", entity.getUserId(),entity.getUserAccount(),entity.getUserName());
				if(dealAlarmJson.containsKey(key)){
					JSONObject subJson = dealAlarmJson.getJSONObject(key);
					if(subJson.containsKey("0")){//0转发
						JSONObject json = subJson.getJSONObject("0");
						entity.setForwardingAlarmTotalAmount(Long.parseLong(json.getString("sum")));
						entity.setForwardingDealAlarmTime(Long.parseLong(json.getString("time")));
					}
					if(subJson.containsKey("1")){//1处理
						JSONObject json = subJson.getJSONObject("1");
						entity.setDealAlarmTotalAmount(Long.parseLong(json.getString("sum")));
						entity.setDealAlarmTime(Long.parseLong(json.getString("time")));
					}
				}
			}
		}
		return userOnlineList;
	}

}
