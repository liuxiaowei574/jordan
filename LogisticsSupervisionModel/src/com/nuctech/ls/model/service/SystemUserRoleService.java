package com.nuctech.ls.model.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.system.LsSystemUserRoleBO;
import com.nuctech.ls.model.dao.SystemUserRoleDao;

@Service
@Transactional
public class SystemUserRoleService extends LSBaseService {

    @Resource
    public SystemUserRoleDao systemUserRoleDao;

    public LsSystemUserRoleBO findRoleByUserId(String userId) {
        LsSystemUserRoleBO lsSystemUserRoleBO = systemUserRoleDao.getSystemUserRoleByUserId(userId);
        return lsSystemUserRoleBO;
    }

}
