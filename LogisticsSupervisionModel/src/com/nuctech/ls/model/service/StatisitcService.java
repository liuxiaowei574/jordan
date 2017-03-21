package com.nuctech.ls.model.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.dao.SystemDepartmentDao;
import com.nuctech.ls.model.dao.SystemUserLogDao;
import com.nuctech.ls.model.dao.TripStatisitcDao;
import com.nuctech.ls.model.dao.VehicleStatisitcDao;
import com.nuctech.ls.model.vo.statistic.DepartmentVo;
import com.nuctech.ls.model.vo.statistic.TripStaticVo;
import com.nuctech.ls.model.vo.statistic.UserLogVo;
import com.nuctech.ls.model.vo.statistic.VehicleStaticVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 统计报表
 * 
 * @author liangpengfei
 *
 */
@Service
@Transactional
public class StatisitcService extends LSBaseService {

	@Resource
	private VehicleStatisitcDao vehicleStatisitcDao;
	@Resource
	private TripStatisitcDao tripStatisitcDao;
	@Resource
	private SystemDepartmentDao systemDepartmentDao;
	@Resource
	private SystemUserLogDao systemUserLogDao;

	/**
	 * 
	 * 车辆监管统计报告
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONArray queryVehicleStaic(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
		List<VehicleStaticVo> list = vehicleStatisitcDao.countVehicle(pageQuery);
		return fromArrayList(list, jsonConfig, ignoreDefaultExcludes);
	}

	/**
	 * 
	 * 车辆监管统计报告-按照部门名称查询详细信息
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject queryVehicleStaicDetail(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
			boolean ignoreDefaultExcludes) {
		PageList<VehicleStaticVo> list = vehicleStatisitcDao.countVehicleDetail(pageQuery);
		return fromObjectList(list, jsonConfig, ignoreDefaultExcludes);
	}

	/**
	 * 
	 * 行程监管统计报告
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONArray queryTripStaic(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
		List<TripStaticVo> list = tripStatisitcDao.checkCount(pageQuery);
		return fromArrayList(list, jsonConfig, ignoreDefaultExcludes);
	}

	/**
	 * 
	 * 车辆监管统计报告-按照部门名称查询详细信息
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject queryTripStaicDetail(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
			boolean ignoreDefaultExcludes) {
		PageList<TripStaticVo> list = tripStatisitcDao.tripDetail(pageQuery);
		return fromObjectList(list, jsonConfig, ignoreDefaultExcludes);
	}

	/**
	 * 
	 * 已对接国家/公司统计报告-按照机构类型查询详细信息
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject queryDepartmentDetail(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
			boolean ignoreDefaultExcludes) {
		PageList<LsSystemDepartmentBO> list = systemDepartmentDao.countDepartmentDetail(pageQuery);
		return fromObjectList(list, jsonConfig, ignoreDefaultExcludes);
	}

	/**
	 * 
	 * 已对接国家/公司统计报告-总数
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	public List<DepartmentVo> querycountCountry(String organizationType) {
		return systemDepartmentDao.countDepartment(organizationType);
	}

	/**
	 * 查询系统中国家的数量
	 * 
	 * @param organizationType
	 * @return
	 */
	public int querycount() {
		return systemDepartmentDao.count();
	}

	/**
	 * 根据用户名称查询用户信息及用户（历史累计）在线时长
	 * 
	 * @param userName
	 * @return
	 */
	public JSONArray queryUserDetail(String userName) {
		List<UserLogVo> list = systemUserLogDao.userDetail(userName);
		return JSONArray.fromObject(list);
	}

}
