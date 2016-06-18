package com.nuctech.ls.model.service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.common.LsCommonPortBO;
import com.nuctech.ls.model.dao.CommonPatrolDao;
import com.nuctech.ls.model.dao.CommonPortDao;
import com.nuctech.util.Constant;

@Service
@Transactional
public class CommonPatrolService extends LSBaseService{

	
	@Resource
	private CommonPatrolDao commonPatrolDao;
	
	public List<LsCommonPatrolBO> findAllCommonPatrol(){
		HashMap<String, String> orderbyMap = new HashMap<String, String>();
        orderbyMap.put("createTime", "desc");
		return commonPatrolDao.findAllBy("deleteMark",Constant.MARK_UN_DELETED,orderbyMap);
	}
}
