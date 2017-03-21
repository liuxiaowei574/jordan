package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.system.LsSystemFunctionsBO;
import com.nuctech.ls.model.dao.SystemFunctionsDao;
import com.nuctech.ls.model.vo.ztree.TreeNode;

/**
 * 作者： 赵苏阳
 *
 * 描述：
 * <p>
 * 系统功能 Service
 * </p>
 * 创建时间：2016年8月9日
 */
@Service
@Transactional
public class SystemFunctionService extends LSBaseService {

    @Resource
    private SystemFunctionsDao systemFunctionsDao;

    public List<LsSystemFunctionsBO> getSystemFunctionList() {
        return systemFunctionsDao.getSysFunctionList();
    }

    /**
     * 功能树构建
     * 
     * @return
     */
    public List<TreeNode> findFunctionTree() {
        return systemFunctionsDao.findFunTree();
    }

    public LsSystemFunctionsBO findByFunctionId(String id) {
        return systemFunctionsDao.findById(id);
    }
}
