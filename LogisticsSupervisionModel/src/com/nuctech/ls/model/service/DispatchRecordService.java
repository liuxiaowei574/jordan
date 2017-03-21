package com.nuctech.ls.model.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.dao.DispatchRecordDao;
import com.nuctech.ls.model.vo.warehouse.DispatchRecordDepartmentVO;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 作者： 赵苏阳
 *
 * 描述：
 * <p>
 * 设备调度的记录即“保存”相关信息 Service
 * </p>
 * 创建时间：2016年6月12日
 */

@Service
@Transactional
public class DispatchRecordService extends LSBaseService {

    @Resource
    private DispatchRecordDao dispatchRecordDao;

    /**
     * 根据调度id查询出需要修改的调度表记录
     * 
     * @param id
     * @return
     */
    public LsWarehouseDeviceDispatchBO findByDispatchID(String id) {
        return dispatchRecordDao.findById(id);
    }

    /**
     * 修改调度记录表的内容
     * 
     * @param warehouseElock
     */
    public void modifyDispatchRecord(LsWarehouseDeviceDispatchBO deviceRecord) {
        dispatchRecordDao.update(deviceRecord);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes,
            String organizationId, String roleId) {

        String queryString = "select e,d,f,s "
                + "from LsWarehouseDeviceDispatchBO e,LsSystemDepartmentBO d,LsSystemDepartmentBO f,LsSystemRoleBO s "
                + "where 1=1" + " and e.fromPort= '" + organizationId + "'" + " and e.fromPort=d.organizationId "
                + " and s.roleId= '" + roleId + "'" + " and e.toPort=f.organizationId "
                + "/~ and e.toPort like '%[toPort]%' ~/" + "/~ and e.fromPort like '%[fromPort]%' ~/"
                + "/~ order by [sortColumns] ~/";

        PageList<Object> queryList = dispatchRecordDao.pageQuery(queryString, pageQuery);
        PageList<DispatchRecordDepartmentVO> pageList = new PageList<DispatchRecordDepartmentVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                DispatchRecordDepartmentVO dispatchRecordDepartmentVO = new DispatchRecordDepartmentVO();
                /*
                 * BeanUtils.copyProperties((LsWarehouseDeviceDispatchBO)
                 * objs[0], dispatchRecordDepartmentVO);
                 * BeanUtils.copyProperties((LsSystemDepartmentBO) objs[1],
                 * dispatchRecordDepartmentVO);
                 */
                if (objs[0] != null) {
                    LsWarehouseDeviceDispatchBO lsWarehouseDeviceDispatchBO = (LsWarehouseDeviceDispatchBO) objs[0];
                    dispatchRecordDepartmentVO.setDispatchId(lsWarehouseDeviceDispatchBO.getDispatchId());
                    dispatchRecordDepartmentVO.setDeviceNumber(lsWarehouseDeviceDispatchBO.getDeviceNumber());
                    dispatchRecordDepartmentVO.setEsealNumber(lsWarehouseDeviceDispatchBO.getEsealNumber());
                    dispatchRecordDepartmentVO.setSensorNumber(lsWarehouseDeviceDispatchBO.getSensorNumber());
                    dispatchRecordDepartmentVO.setDispatchStatus(lsWarehouseDeviceDispatchBO.getDispatchStatus());
                    dispatchRecordDepartmentVO.setDispatchUser(lsWarehouseDeviceDispatchBO.getDispatchUser());
                    dispatchRecordDepartmentVO.setDispatchTime(lsWarehouseDeviceDispatchBO.getDispatchTime());
                    dispatchRecordDepartmentVO.setRoleName(lsWarehouseDeviceDispatchBO.getDispatchRole());
                }
                if (objs[1] != null) {
                    LsSystemDepartmentBO lsSystemDepartmentBO = (LsSystemDepartmentBO) objs[1];
                    dispatchRecordDepartmentVO.setFromPortName(lsSystemDepartmentBO.getOrganizationName());
                }
                if (objs[2] != null) {
                    LsSystemDepartmentBO lsSystemDepartmentBO = (LsSystemDepartmentBO) objs[2];
                    dispatchRecordDepartmentVO.setToPortName(lsSystemDepartmentBO.getOrganizationName());
                    pageList.add(dispatchRecordDepartmentVO);
                }
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }
}
