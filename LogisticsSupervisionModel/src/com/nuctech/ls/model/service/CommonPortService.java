package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.common.LsCommonPortBO;
import com.nuctech.ls.model.dao.CommonPortDao;

@Service
@Transactional
public class CommonPortService extends LSBaseService{

	
	@Resource
	private CommonPortDao commonPortDao;
	
	public List<LsCommonPortBO> findAllCommonPort(){
		return commonPortDao.findAll();
	}
}
