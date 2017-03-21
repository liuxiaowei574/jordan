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

    @SuppressWarnings("rawtypes")
    public List<PerformanceAnalysisVo> findPerformanceAnalysis(PageQuery<Map> pageQuery) {
        // List<PerformanceAnalysisVo> list = new ArrayList<>();
        List<PerformanceAnalysisVo> userOnlineList = performanceAnalysisDao.findUserOnline(pageQuery);
        JSONObject dealAlarmJson = performanceAnalysisDao.findDealAlarm();
        if (userOnlineList != null) {
            for (PerformanceAnalysisVo entity : userOnlineList) {
                String key = String.format("%s%s%s", entity.getUserId(), entity.getUserAccount(), entity.getUserName());
                if (dealAlarmJson.containsKey(key)) {
                    JSONObject subJson = dealAlarmJson.getJSONObject(key);
                    if (subJson.containsKey("0")) {// 0转发
                        JSONObject json = subJson.getJSONObject("0");
                        entity.setForwardingAlarmTotalAmount(Long.parseLong(json.getString("sum")));
                        entity.setForwardingDealAlarmTime(Long.parseLong(json.getString("time")));
                    }
                    if (subJson.containsKey("1")) {// 1处理
                        JSONObject json = subJson.getJSONObject("1");
                        entity.setDealAlarmTotalAmount(Long.parseLong(json.getString("sum")));
                        entity.setDealAlarmTime(Long.parseLong(json.getString("time")));
                    }
                }
            }
        }
        return userOnlineList;
    }

    /**
     * 巡逻队处理任务统计
     * 
     * @param pageQuery
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<PerformanceAnalysisVo> findPatorlAnalysis(PageQuery<Map> pageQuery) {
        // List<PerformanceAnalysisVo> list = new ArrayList<>();
        // 在线时长
        List<PerformanceAnalysisVo> userOnlineList = performanceAnalysisDao.findPatrolUserOnline(pageQuery);

        // 报警处理次数
        JSONObject dealAlarmJson = performanceAnalysisDao.findPatrolDealAlarm();

        if (userOnlineList != null) {
            for (PerformanceAnalysisVo entity : userOnlineList) {
                String key = String.format("%s%s%s", entity.getUserId(), entity.getUserAccount(), entity.getUserName());
                if (dealAlarmJson.containsKey(key)) {
                    JSONObject subJson = dealAlarmJson.getJSONObject(key);
                    if (subJson.containsKey("0")) {// 0转发
                        JSONObject json = subJson.getJSONObject("0");
                        entity.setForwardingAlarmTotalAmount(Long.parseLong(json.getString("sum")));
                    }
                    if (subJson.containsKey("1")) {// 1处理
                        JSONObject json = subJson.getJSONObject("1");
                        entity.setDealAlarmTotalAmount(Long.parseLong(json.getString("sum")));
                    }
                }
            }
        }
        return userOnlineList;
    }

    /**
     * 口岸绩效分析统计
     * 1，所有人员的在线时长统计
     * 2，所有人员的报警处理统计
     * 3，
     * 
     * @param pageQuery
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<PerformanceAnalysisVo> findPortlAnalysis(PageQuery<Map> pageQuery) {
        // List<PerformanceAnalysisVo> list = new ArrayList<>();
        // 在线时长
        List<PerformanceAnalysisVo> userOnlineList = performanceAnalysisDao.findPortUserOnline(pageQuery);

        // 报警处理次数
        JSONObject dealAlarmJson = null;
        try {
            dealAlarmJson = performanceAnalysisDao.findPortJixiao();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userOnlineList != null) {
            for (PerformanceAnalysisVo entity : userOnlineList) {
                String key = String.format("%s%s%s", entity.getUserId(), entity.getUserAccount(), entity.getUserName());
                if (dealAlarmJson.containsKey(key)) {
                    JSONObject subJson = dealAlarmJson.getJSONObject(key);
                    if (subJson.containsKey("0")) {// 0转发
                        JSONObject json = subJson.getJSONObject("0");
                        entity.setDealCheckins(Long.parseLong(json.getString("tripCheckIn")));
                        entity.setDealCheckouts(Long.parseLong(json.getString("tripCheckOut")));
                        entity.setDealAlarmTotalAmount(Long.parseLong(json.getString("alarm")));
                    }

                }
            }
        }
        return userOnlineList;
    }
}
