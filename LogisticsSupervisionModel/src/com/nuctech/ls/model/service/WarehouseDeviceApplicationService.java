package com.nuctech.ls.model.service;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.dao.WarehouseDeviceApplicationDao;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.ApplicationUserVO;
import com.nuctech.ls.model.vo.warehouse.ElockDepartmentVO;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 调度申请 Service
 * </p>
 * 创建时间：2016年6月4日
 */
@Service
@Transactional
public class WarehouseDeviceApplicationService extends LSBaseService {

    @Resource
    private WarehouseDeviceApplicationDao warehouseDeviceApplicationDao;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findDeviceApplicationList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) {
        HttpSession session = ServletActionContext.getRequest().getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
        String organizationId = sessionUser.getOrganizationId();
        String queryString = "";
        queryString = "select t,u from LsWarehouseDeviceApplicationBO t,LsSystemUserBO u where 1=1 and u.userId=t.applyUser ";
        //口岸用户只能看到该口岸申请设备的调度申请
        if(sessionUser.getRoleId().equals(RoleType.portUser.getType())){
            queryString +=" and t.applcationPort='"+organizationId+"'"; 
        }
        queryString += "and (t.applyStatus = "
                + Constant.DEVICE_ALREADY_APPLIED+" or t.applyStatus="+Constant.DEVICE_ALREADY_FINISH+")" + "/~ and t.applicationId = '[applicationId]' ~/"
                + "/~ and t.applyUser like '%[applyUser]%' ~/"+ "/~ and t.applyTime >= '[applyStartTime]' ~/"
                + "/~ and t.applyTime <= '[applyEndTime]' ~/" + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = warehouseDeviceApplicationDao.pageQuery(queryString, pageQuery);
        PageList<ApplicationUserVO> pageList = new PageList<ApplicationUserVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                ApplicationUserVO applicationUserVO = new ApplicationUserVO();
                BeanUtils.copyProperties(objs[1], applicationUserVO);
                BeanUtils.copyProperties(objs[0], applicationUserVO);
                pageList.add(applicationUserVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    public void save(LsWarehouseDeviceApplicationBO warehouseDeviceApplication) {
        warehouseDeviceApplicationDao.save(warehouseDeviceApplication);
    }

    /**
     * 根据ID查询申请记录
     * 
     * @param id
     * @return
     */
    public LsWarehouseDeviceApplicationBO findByID(String id) {
        return warehouseDeviceApplicationDao.findById(id);
    }

    /**
     * 修改实体
     * 
     * @param warehouseDeviceApplication
     */
    public void modify(LsWarehouseDeviceApplicationBO warehouseDeviceApplication) {
        warehouseDeviceApplicationDao.update(warehouseDeviceApplication);
    }
}
