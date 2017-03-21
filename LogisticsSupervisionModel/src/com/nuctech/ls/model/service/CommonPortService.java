package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.dao.SystemDepartmentDao;
import com.nuctech.ls.model.util.OrganizationType;

@Service
@Transactional
public class CommonPortService extends LSBaseService {

    @Resource
    private SystemDepartmentDao systemDepartmentDao;

    public List<LsSystemDepartmentBO> findAllCommonPort() {
        return systemDepartmentDao.findAllBy("organizationType", OrganizationType.Port.getText());
    }

    public LsSystemDepartmentBO findPortById(String portId) {
        return systemDepartmentDao.findById(portId);
    }
}
