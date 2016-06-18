package com.nuctech.ls.model.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.dao.CommonVehicleDao;

/**
 * 监管车辆信息Service
 * 
 * @author liushaowei
 *
 */
@Service
@Transactional
public class CommonVehicleService extends LSBaseService {

	@Resource
	private CommonVehicleDao commonVehicleDao;

	/**
	 * 新增车辆信息
	 * 
	 * @param lsCommonVehicleBO
	 */
	public void addCommonVehicle(LsCommonVehicleBO lsCommonVehicleBO) {
		commonVehicleDao.addCommonVehicle(lsCommonVehicleBO);
	}

	/**
	 * 根据Id查找车辆信息
	 * 
	 * @param id
	 * @return
	 */
	public LsCommonVehicleBO findById(String id) {
		return commonVehicleDao.findById(id);
	}

	/**
	 * 更新车辆信息
	 * 
	 * @param lsCommonVehicleBO
	 */
	public void updateCommonVehicle(LsCommonVehicleBO lsCommonVehicleBO) {
		commonVehicleDao.updateCommonVehicle(lsCommonVehicleBO);
	}
}
