package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemFunctionsBO;
import com.nuctech.ls.model.vo.ztree.TreeNode;
import com.nuctech.util.Constant;
import com.nuctech.util.ConstantConfig;
import com.nuctech.util.MessageResourceUtil;

/**
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 系统菜单 DAO
 * </p>
 * 创建时间：2016年5月18日
 */
@Repository
public class SystemFunctionsDao extends LSBaseDao<LsSystemFunctionsBO, Serializable> {

    @Resource
    private SystemModules systemModules;

    /**
     * 查询有效菜单ID
     * 
     * @param id
     * @return
     */
    public LsSystemFunctionsBO findValidFunctionById(String id) {
        String inValidFlag = ConstantConfig.readValue(Constant.VALID_FLAG);
        Criteria criteria = getSession().createCriteria(LsSystemFunctionsBO.class);
        criteria.add(Restrictions.eq("functionId", id));
        criteria.add(Restrictions.eq("isEnable", inValidFlag));
        return (LsSystemFunctionsBO) criteria.uniqueResult();
    }

    /**
     * 查询二级有效菜单ID
     * 
     * @param functionId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsSystemFunctionsBO> findSubFunctions(String functionId) {
        String inValidFlag = ConstantConfig.readValue(Constant.VALID_FLAG);
        Criteria criteria = getSession().createCriteria(LsSystemFunctionsBO.class);
        criteria.add(Restrictions.eq("parentId", functionId));
        criteria.add(Restrictions.eq("isEnable", inValidFlag));
        return criteria.list();
    }

    /**
     * 查出所有二级功能菜单
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsSystemFunctionsBO> getSysFunctionList() {
        Session session = this.getSession();
        String sql = "SELECT f.* FROM LS_SYSTEM_FUNCTIONS f where f.FUNCTION_TYPE='1'";
        try {
            if (!systemModules.isPatrolOn()) {
                sql += " and f.FUNCTION_ID not in(37,48,51,54)";
            }
            if (!systemModules.isRiskOn()) {
                sql += " and f.FUNCTION_ID not in(29,52)";
            }
            if (!systemModules.isDispatchOn()) {
                sql += " and f.FUNCTION_ID not in(25,26,27,47)";
            }
            if (!systemModules.isAlarmPushOn()) {
                sql += " and f.FUNCTION_ID not in(14,34)";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // String sql = "SELECT f.* FROM LS_SYSTEM_FUNCTIONS f ";
        org.hibernate.Query query = session.createSQLQuery(sql).addEntity("f", LsSystemFunctionsBO.class);
        List<LsSystemFunctionsBO> list = query.list();
        return list;
    }

    /**
     * 构建功能树
     */
    @SuppressWarnings("unchecked")
    public List<TreeNode> findFunTree() {
        List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
        TreeNode root = new TreeNode();
        root.setId("0");
        root.setName(MessageResourceUtil.getMessageInfo("Authority.management.Authority"));
        // root.setName("功能树");
        root.setOpen(true);
        root.setpId(null);
        treeNodeList.add(root);
        Session session = this.getSession();
        // 先查出菜单（其功能类型的值为"0"）
        String sql = "SELECT f.* FROM LS_SYSTEM_FUNCTIONS f where f.FUNCTION_TYPE='0'";
        List<LsSystemFunctionsBO> functionList = session.createSQLQuery(sql).addEntity("f", LsSystemFunctionsBO.class)
                .list();
        for (LsSystemFunctionsBO functionsBO : functionList) {
            TreeNode rootMenu = new TreeNode();
            rootMenu.setId(functionsBO.getFunctionId());
            // rootMenu.setName(functionsBO.getFunctionName());
            rootMenu.setName(MessageResourceUtil.getMessageInfo(functionsBO.getFunctionName()));// 从国际化资源文件中读取
            rootMenu.setpId("0");
            treeNodeList.add(rootMenu);

            String sqlFun = "SELECT f.* FROM LS_SYSTEM_FUNCTIONS f where f.PARENT_ID=" + functionsBO.getFunctionId();
            if (!systemModules.isPatrolOn()) {
                sqlFun += " and f.FUNCTION_ID not in(37,48,51,54)";
            }
            if (!systemModules.isRiskOn()) {
                sqlFun += " and f.FUNCTION_ID not in(29,52)";
            }
            if (!systemModules.isDispatchOn()) {
                sqlFun += " and f.FUNCTION_ID not in(25,26,27,47)";
            }
            if (!systemModules.isAlarmPushOn()) {
                sqlFun += " and f.FUNCTION_ID not in(14,34)";
            }
            List<LsSystemFunctionsBO> funcList = session.createSQLQuery(sqlFun)
                    .addEntity("f", LsSystemFunctionsBO.class).list();
            for (LsSystemFunctionsBO f : funcList) {
                TreeNode tree = new TreeNode();
                tree.setId(f.getFunctionId());
                tree.setpId(f.getParentId());
                // tree.setName(f.getFunctionName());
                tree.setName(MessageResourceUtil.getMessageInfo(f.getFunctionName()));
                treeNodeList.add(tree);
            }

        }

        //

        return treeNodeList;
    }

    public SystemModules getSystemModules() {
        return systemModules;
    }

    public void setSystemModules(SystemModules systemModules) {
        this.systemModules = systemModules;
    }
}
