package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.util.OrganizationType;
import com.nuctech.ls.model.vo.statistic.DepartmentVo;
import com.nuctech.ls.model.vo.warehouse.PortElockStatisitcVO;
import com.nuctech.ls.model.vo.warehouse.PortEsealStatisitcVO;
import com.nuctech.ls.model.vo.warehouse.PortSensorStatisitcVO;
import com.nuctech.ls.model.vo.ztree.TreeNode;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

/**
 * 组织机构 DAO
 * 
 * @author liushaowei
 *
 */
@Repository
public class SystemDepartmentDao extends LSBaseDao<LsSystemDepartmentBO, Serializable> {

    /**
     * 查询国家口岸列表
     * 
     * @param countryId
     *        国家ID
     * @param portName
     *        口岸名称
     * @return
     */

    @SuppressWarnings("unchecked")
    public List<LsSystemDepartmentBO> findCountryPortList(String countryId, String portName) {
        Criteria criteria = getSession().createCriteria(LsSystemDepartmentBO.class);
        criteria.add(Restrictions.eq("organizationType", Constant.ORGANIZATION_TYPE_PORT));
        if (!NuctechUtil.isNull(countryId)) {
            criteria.add(Restrictions.eq("parentId", countryId));
        }
        if (!NuctechUtil.isNull(portName)) {
            criteria.add(Restrictions.like("organizationName", '%' + portName + '%'));
        }
        return criteria.list();
    }

    /**
     * @param countryId
     * @param portName
     * @return
     * 
     *         查询一个国家的所有仓库
     */
    @SuppressWarnings("unchecked")
    public List<LsSystemDepartmentBO> findCountryRoomList(String countryId, String portName) {
        Criteria criteria = getSession().createCriteria(LsSystemDepartmentBO.class);
        // criteria.add();
        Disjunction or = Restrictions.disjunction();
        or.add(Restrictions.eq("organizationType", Constant.ORGANIZATION_TYPE_ROOM));
        Disjunction or2 = Restrictions.disjunction();
        or2.add(Restrictions.eq("isRoom", Constant.ORGANIZATION_IS_ROOM));

        criteria.add(Restrictions.or(or, or2));
        criteria.add(Restrictions.ne("organizationType", Constant.ORGANIZATION_TYPE_PORT));
        if (!NuctechUtil.isNull(countryId)) {
            criteria.add(Restrictions.eq("parentId", countryId));
        }
        if (!NuctechUtil.isNull(portName)) {
            criteria.add(Restrictions.like("organizationName", '%' + portName + '%'));
        }

        return criteria.list();
    }

    /**
     * @param countryId
     * @param portName
     * @return
     * 
     *         根据部门的ID 统计该部门的关锁情况
     * 
     *
     */
    @SuppressWarnings("unchecked")
    public List<PortElockStatisitcVO> countElcokByDepartment(String deptid) {
        List<PortElockStatisitcVO> result = new ArrayList<PortElockStatisitcVO>();
        String sql = "SELECT  DEPT.ORGANIZATION_NAME portName,DEPT.ORGANIZATION_ID portId ,"
                + "DEPT.PARENT_ID pid ,ELOCK.ELOCK_STATUS elockstatus, COUNT(*) keyongs"
                + " FROM LS_WAREHOUSE_ELOCK ELOCK, LS_SYSTEM_DEPARTMENT DEPT"
                + " WHERE ELOCK.BELONG_TO=DEPT.ORGANIZATION_ID" + " AND DEPT.ORGANIZATION_ID='" + deptid
                + "' GROUP BY DEPT.ORGANIZATION_NAME ,DEPT.ORGANIZATION_ID  ,DEPT.PARENT_ID  ,ELOCK.ELOCK_STATUS";
        try {
            result = this.getSession().createSQLQuery(sql).addScalar("portName").addScalar("portId").addScalar("pid")
                    .addScalar("elockstatus").addScalar("keyongs")
                    .setResultTransformer(Transformers.aliasToBean(PortElockStatisitcVO.class)).list();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return result;

    }

    /**
     * @param countryId
     * @param portName
     * @return
     * 
     *         根据部门的ID 统计该部门的子锁情况
     * 
     *
     */
    @SuppressWarnings("unchecked")
    public List<PortEsealStatisitcVO> countEsealByDepartment(String deptid) {
        List<PortEsealStatisitcVO> result = new ArrayList<PortEsealStatisitcVO>();
        String sql = "SELECT  DEPT.ORGANIZATION_NAME portName,DEPT.ORGANIZATION_ID portId ,DEPT.PARENT_ID pid , "
                + "ESEAL.ESEAL_STATUS esealstatus, COUNT (*) ekeyongs"
                + " FROM LS_WAREHOUSE_ESEAL ESEAL, LS_SYSTEM_DEPARTMENT DEPT"
                + " WHERE ESEAL.BELONG_TO=DEPT.ORGANIZATION_ID" + " AND DEPT.ORGANIZATION_ID='" + deptid
                + "' GROUP BY DEPT.ORGANIZATION_NAME ,DEPT.ORGANIZATION_ID  ,DEPT.PARENT_ID  ,ESEAL.ESEAL_STATUS";
        try {
            result = this.getSession().createSQLQuery(sql).addScalar("portName").addScalar("portId").addScalar("pid")
                    .addScalar("esealstatus").addScalar("ekeyongs")
                    .setResultTransformer(Transformers.aliasToBean(PortEsealStatisitcVO.class)).list();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return result;

    }

    /**
     * @param countryId
     * @param portName
     * @return
     * 
     *         根据部门的ID 统计该部门的子锁情况
     * 
     *
     */
    @SuppressWarnings("unchecked")
    public List<PortSensorStatisitcVO> countSensorByDepartment(String deptid) {
        List<PortSensorStatisitcVO> result = new ArrayList<PortSensorStatisitcVO>();
        String sql = "SELECT  DEPT.ORGANIZATION_NAME portName,DEPT.ORGANIZATION_ID portId ,DEPT.PARENT_ID pid , "
                + "SENSOR.SENSOR_STATUS sensorstatus, COUNT (*) skeyongs"
                + " FROM LS_WAREHOUSE_SENSOR SENSOR, LS_SYSTEM_DEPARTMENT DEPT"
                + " WHERE SENSOR.BELONG_TO=DEPT.ORGANIZATION_ID" + " AND DEPT.ORGANIZATION_ID='" + deptid
                + "' GROUP BY DEPT.ORGANIZATION_NAME ,DEPT.ORGANIZATION_ID  ,DEPT.PARENT_ID  ,SENSOR.SENSOR_STATUS";
        try {
            result = this.getSession().createSQLQuery(sql).addScalar("portName").addScalar("portId").addScalar("pid")
                    .addScalar("sensorstatus").addScalar("skeyongs")
                    .setResultTransformer(Transformers.aliasToBean(PortSensorStatisitcVO.class)).list();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return result;

    }

    // 构建组织机构树
    public List<TreeNode> findSDTree() {
        List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
        Session session = this.getSession();
        // 先查出国家（其机构类型的值为"1"）
        String sql = "SELECT d.* FROM LS_SYSTEM_DEPARTMENT d where d.ORGANIZATION_TYPE = '1'";
        @SuppressWarnings("unchecked")
        List<LsSystemDepartmentBO> countryList = session.createSQLQuery(sql).addEntity("d", LsSystemDepartmentBO.class)
                .list();

        for (int i = 0; i < countryList.size(); i++) {
            TreeNode root = new TreeNode();
            root.setId(countryList.get(i).getOrganizationId());
            root.setName(countryList.get(i).getOrganizationName());
            root.setOpen(true);
            root.setpId(null);
            root.setIconSkin("country");
            treeNodeList.add(root);
        }
        for (LsSystemDepartmentBO countryDep : countryList) {
            // 取出该国家下面对应的口岸/监管中心/监管场所
            String sqlport = "SELECT s.* FROM LS_SYSTEM_DEPARTMENT s where "
            		+ " s.LEVEL_CODE like '"+countryDep.getOrganizationId()+"%' and s.LEVEL_CODE !='"+countryDep.getOrganizationId()+"'";
            @SuppressWarnings("unchecked")
            List<LsSystemDepartmentBO> portList = session.createSQLQuery(sqlport)
                    .addEntity("s", LsSystemDepartmentBO.class).list();
            for (LsSystemDepartmentBO port : portList) {
                TreeNode tree = new TreeNode();
                tree.setId(port.getOrganizationId());
                tree.setpId(port.getParentId());
                tree.setName(port.getOrganizationName());
                //给口岸添加图标
                if(port.getOrganizationType().equals("2")){
                    tree.setIconSkin("port");
                }
                //给监管场所添加图标 
                if(port.getOrganizationType().equals("3")){
                    tree.setIconSkin("place");
                }
                //给监管中心添加图标 
                if(port.getOrganizationType().equals("4")){
                    tree.setIconSkin("center");
                }
                treeNodeList.add(tree);
            }
        }
        return treeNodeList;

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<LsSystemDepartmentBO> findByParentId(String countryId) {
        String query = "SELECT * FROM LS_SYSTEM_DEPARTMENT where PARENT_ID ='" + countryId + "'";
        List list = this.getSession().createSQLQuery(query).addEntity(LsSystemDepartmentBO.class).list();
        return list;
    }

    /**
     * 查询国家以外的机构
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<LsSystemDepartmentBO> findExceptCountry() {
        String query = "SELECT * FROM LS_SYSTEM_DEPARTMENT where ORGANIZATION_TYPE!='1'";
        List list = this.getSession().createSQLQuery(query).addEntity(LsSystemDepartmentBO.class).list();
        return list;
    }

    /**
     * 查询指定国家所有口岸
     * 
     * @param countryId
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<LsSystemDepartmentBO> findAllPortByCountryId(String countryId) {
        String query = "SELECT * FROM LS_SYSTEM_DEPARTMENT where PARENT_ID ='" + countryId
                + "' AND ORGANIZATION_TYPE = '" + Constant.ORGANIZATION_TYPE_PORT + "' ";
        List list = this.getSession().createSQLQuery(query).addEntity(LsSystemDepartmentBO.class).list();
        return list;
    }
    /**
     * 查询指定国家所有机构
     * @param countryId
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<LsSystemDepartmentBO> findAllDepartmentByCountryId(String countryId) {
        String query = "SELECT * FROM LS_SYSTEM_DEPARTMENT where PARENT_ID ='" + countryId+"'";
        List list = this.getSession().createSQLQuery(query).addEntity(LsSystemDepartmentBO.class).list();
        return list;
    }
    
    /**
     * 根据组织Id查找国家节点Id
     * 
     * @param deptId
     * @return
     */
    public String findCountryIdByDeptId(LsSystemDepartmentBO departmentBO) {
        if (departmentBO == null || departmentBO.getLevelCode() == null) {
            return null;
        }
        String levelCode = departmentBO.getLevelCode(); // 001.002.003.004
        return levelCode.split("\\.")[0];
    }

    /**
     * 根据组织Id查找国家节点Id
     * 
     * @param deptId
     * @return
     */
    public String findCountryIdByDeptId(String deptId) {
        LsSystemDepartmentBO departmentBO = findById(deptId);
        return findCountryIdByDeptId(departmentBO);
    }

    /**
     * 统计系统已经对接的国家信息
     * 
     * @param deptId
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public PageList<LsSystemDepartmentBO> countDepartmentDetail(PageQuery<Map> pageQuery) {

        String queryString = "select d from LsSystemDepartmentBO d where d.organizationType = '1'"
                + "/~ and d.organizationName like '%[organizationName]%' ~/ " + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = pageQuery(queryString, pageQuery);
        PageList<LsSystemDepartmentBO> pageList = new PageList<LsSystemDepartmentBO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                // Object[] objs = (Object[]) obj;
                pageList.add((LsSystemDepartmentBO) obj);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return pageList;
    }

    /**
     * 统计系统已经对接的国家
     * 
     * @param deptId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<DepartmentVo> countDepartment(String organizationType) {
        String queryString = "SELECT d.* FROM LS_SYSTEM_DEPARTMENT d where d.ORGANIZATION_TYPE = '1'";
        List<LsSystemDepartmentBO> list = this.getSession().createSQLQuery(queryString)
                .addEntity(LsSystemDepartmentBO.class).list();
        List<DepartmentVo> dlist = new ArrayList<DepartmentVo>();
        DepartmentVo d = new DepartmentVo();
        d.setNumber(list.size());
        dlist.add(d);
        return dlist;
    }
    
    /**
     * 查询系统中国家的数量
     * @param organizationType
     * @return
     */
    public int count() {
        int count = 0;
		try {
			String queryString = "SELECT d.* FROM LS_SYSTEM_DEPARTMENT d where d.ORGANIZATION_TYPE = '1'";
			@SuppressWarnings("unchecked")
			List<LsSystemDepartmentBO> list = this.getSession().createSQLQuery(queryString)
	                .addEntity(LsSystemDepartmentBO.class).list();
			count = list.size();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return count;
    }
    
    @SuppressWarnings("unchecked")
    public List<LsSystemDepartmentBO> findAllPort(){
        Criteria crit = sessionFactory.getCurrentSession().createCriteria(LsSystemDepartmentBO.class);
        crit.add(Restrictions.eq("organizationType", OrganizationType.Port.getText()));
        return crit.list();
    }
}
