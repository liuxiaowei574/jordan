package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.dao.SystemDepartmentTestDao;

@Service
@Transactional
public class SystemDepartmentTestService extends LSBaseService{

	@Resource
	SystemDepartmentTestDao departmentTestDao;
	
	List<LsSystemDepartmentBO> departmentBOTests;
	
	public List<LsSystemDepartmentBO> findAllSysDepartment(){
		return departmentTestDao.findAll();
	}
}
