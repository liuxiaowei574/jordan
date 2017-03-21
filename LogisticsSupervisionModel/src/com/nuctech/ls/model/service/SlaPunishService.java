package com.nuctech.ls.model.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.sla.LsSlaPunishBO;
import com.nuctech.ls.model.dao.SlaPunishDao;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

@Service
@Transactional
public class SlaPunishService extends LSBaseService {

    @Resource
    private SlaPunishDao slaPunishDao;

    /**
     * 新增罚款记录
     * 
     * @param lsSlaPunishBO
     */
    public void add(LsSlaPunishBO lsSlaPunishBO) {
        try {
            slaPunishDao.save(lsSlaPunishBO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改罚款记录
     * 
     * @param lsSlaPunishBO
     */
    public void modify(LsSlaPunishBO lsSlaPunishBO) {
        slaPunishDao.merge(lsSlaPunishBO);
    }

    // 返回JSON单表查询
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONArray fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        String queryString = "select s from LsSlaPunishBO s where 1=1" + "/~ and s.punishName like '%[punishName]%' ~/"
                + "/~ and s.slaType like '%[slaType]%' ~/" + "/~ order by [sortColumns] ~/";
        PageList<Object> pageList = slaPunishDao.pageQuery(queryString, pageQuery);
        return fromArrayList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 删除罚单记录
     * 
     * @param lsSlaPunishBO
     */
    public void deleteById(String punishId) {
        slaPunishDao.deleteById(punishId);
    }

    /**
     * 根据Id查询罚单记录
     * 
     * @param id
     * @return
     */
    public LsSlaPunishBO find(String id) {
        return slaPunishDao.findById(id);
    }

}
