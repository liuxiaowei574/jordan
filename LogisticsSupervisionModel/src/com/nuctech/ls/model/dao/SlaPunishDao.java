package com.nuctech.ls.model.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.sla.LsSlaPunishBO;

@Repository
public class SlaPunishDao extends LSBaseDao<LsSlaPunishBO, Serializable> {

    /**
     * 作者赵苏阳
     * 
     * 描述
     * <p>
     * 罚款Dao
     * <p>
     * 
     * @author zsy
     *         创建时间2016年6月23日
     *
     */
    private LSBaseDao<LsSlaPunishBO, Serializable> basedao = null;

}
