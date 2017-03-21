package com.nuctech.ls.model.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.dao.SystemNoticeLogDao;

/**
 * 通知处理详细信息
 * 
 * @author zhaosuyang 创建时间：2016年8月26日
 */
@Service
@Transactional
public class SystemNoticeLogService extends LSBaseService {

    @Resource
    private SystemNoticeLogDao systemNoticeLogDao;

    /**
     * 根据用户id（接收人id）查询未读的通知
     * 
     * @param userId
     * @return
     */
    public int findCount(String userId) {
        return (systemNoticeLogDao.findNumber(userId));
    }
}
