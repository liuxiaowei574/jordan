package com.nuctech.ls.model.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;

/**
 * 监管车辆信息Dao
 * 
 * @author liushaowei
 *
 */
@Repository
public class CommonVehicleDao extends LSBaseDao<LsCommonVehicleBO, Serializable> {

	/**
	 * 新增车辆信息
	 * 
	 * @param lsCommonVehicleBO
	 */
	public void addCommonVehicle(LsCommonVehicleBO lsCommonVehicleBO) {
		persist(lsCommonVehicleBO);
	}

	/**
	 * 更新车辆信息
	 * 
	 * @param lsCommonVehicleBO
	 */
	public void updateCommonVehicle(LsCommonVehicleBO lsCommonVehicleBO) {
		update(lsCommonVehicleBO);
	}
}
