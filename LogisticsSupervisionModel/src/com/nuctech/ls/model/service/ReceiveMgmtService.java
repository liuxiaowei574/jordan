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
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDispatchDetailBO;
import com.nuctech.ls.model.dao.DeviceApplicationDao;
import com.nuctech.ls.model.dao.DispatchDetailDao;
import com.nuctech.ls.model.dao.DispatchRecordDao;
import com.nuctech.ls.model.vo.warehouse.DispatchRecordDepartmentVO;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
@Transactional
public class ReceiveMgmtService extends LSBaseService {

    @Resource
    private DispatchRecordDao dispatchRecordDao;

    @Resource
    private DispatchDetailDao dispatchDetailDao;

    @Resource
    private DeviceApplicationDao deviceApplicationDao;

    /**
     * 将调度记录显示到receiveIndex页面 (已处理的调度申请（已接收的）不再显示到列表中)
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @param organizationId
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes,
            String organizationId) {

        String queryString = "select e,d,f,a "
                + "from LsWarehouseDeviceDispatchBO e,LsSystemDepartmentBO d,LsSystemDepartmentBO f,"
                + "LsWarehouseDeviceApplicationBO a " + "where 1=1" + " and e.toPort= '" + organizationId + "'"
                + " and e.toPort=d.organizationId " + " and e.applicationId=a.applicationId "
                + " and e.fromPort=f.organizationId " + " and e.dispatchStatus=1 " + " and a.applyStatus!=3 "
                + "/~ and e.toPort like '%[toPort]%' ~/" + "/~ and e.fromPort like '%[fromPort]%' ~/"
                + "/~ order by [sortColumns] ~/";

        PageList<Object> queryList = dispatchRecordDao.pageQuery(queryString, pageQuery);
        PageList<DispatchRecordDepartmentVO> pageList = new PageList<DispatchRecordDepartmentVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                DispatchRecordDepartmentVO dispatchRecordDepartmentVO = new DispatchRecordDepartmentVO();
                if (objs[0] != null) {
                    LsWarehouseDeviceDispatchBO lsWarehouseDeviceDispatchBO = (LsWarehouseDeviceDispatchBO) objs[0];
                    dispatchRecordDepartmentVO.setApplicationId(lsWarehouseDeviceDispatchBO.getApplicationId());
                    dispatchRecordDepartmentVO.setDispatchId(lsWarehouseDeviceDispatchBO.getDispatchId());
                    dispatchRecordDepartmentVO.setDeviceNumber(lsWarehouseDeviceDispatchBO.getDeviceNumber());
                    dispatchRecordDepartmentVO.setEsealNumber(lsWarehouseDeviceDispatchBO.getEsealNumber());
                    dispatchRecordDepartmentVO.setSensorNumber(lsWarehouseDeviceDispatchBO.getSensorNumber());
                    dispatchRecordDepartmentVO.setDispatchStatus(lsWarehouseDeviceDispatchBO.getDispatchStatus());
                    dispatchRecordDepartmentVO.setDispatchUser(lsWarehouseDeviceDispatchBO.getDispatchUser());
                    dispatchRecordDepartmentVO.setDispatchTime(lsWarehouseDeviceDispatchBO.getDispatchTime());
                }
                if (objs[1] != null) {
                    LsSystemDepartmentBO lsSystemDepartmentBO = (LsSystemDepartmentBO) objs[1];
                    dispatchRecordDepartmentVO.setToPortName(lsSystemDepartmentBO.getOrganizationName());
                }
                if (objs[2] != null) {
                    LsSystemDepartmentBO lsSystemDepartmentBO = (LsSystemDepartmentBO) objs[2];
                    dispatchRecordDepartmentVO.setFromPortName(lsSystemDepartmentBO.getOrganizationName());
                    pageList.add(dispatchRecordDepartmentVO);
                }
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    @SuppressWarnings("rawtypes")
    public List getdeviceDetailList(String dispatchId) {
        return dispatchDetailDao.getDeviceList(dispatchId);
    }

    /**
     * 根据id查出设备调配明细表
     * 
     * @param id
     * @return
     */
    public LsWarehouseDispatchDetailBO findByDetailID(String id) {

        return dispatchDetailDao.findById(id);
    }

    /**
     * 修改设备调配明细表
     */
    public void modifyDeviceDetail(LsWarehouseDispatchDetailBO lsWarehouseDispatchDetailBO) {
        dispatchDetailDao.merge(lsWarehouseDispatchDetailBO);
    }

    /**
     * 根据申请主键查出设备调度申请表的记录
     * 
     * @param id
     * @return
     */
    public LsWarehouseDeviceApplicationBO findByApplicationId(String id) {
        return deviceApplicationDao.findById(id);

    }

    /**
     * 修改设备申请表
     */
    public void modifyDeviceApplication(LsWarehouseDeviceApplicationBO lsWarehouseDeviceApplicationBO) {
        deviceApplicationDao.merge(lsWarehouseDeviceApplicationBO);

    }
}
