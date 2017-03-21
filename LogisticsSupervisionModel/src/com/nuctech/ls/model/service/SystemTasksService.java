package com.nuctech.ls.model.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemTasksBO;
import com.nuctech.ls.model.bo.system.LsSystemTasksDealBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.dao.SystemDepartmentDao;
import com.nuctech.ls.model.dao.SystemRoleDao;
import com.nuctech.ls.model.dao.SystemTasksDao;
import com.nuctech.ls.model.dao.SystemTasksDealDao;
import com.nuctech.ls.model.dao.SystemUserDao;
import com.nuctech.ls.model.vo.system.SystemTasksLogVO;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
@Transactional
public class SystemTasksService extends LSBaseService {

    @Resource
    private SystemTasksDao tasksDao;
    @Resource
    private SystemTasksDealDao taskDealDao;
    @Resource
    private SystemUserDao userDao;
    @Resource
    private SystemRoleDao systemRoleDao;
    @Resource
    private SystemDepartmentDao departmentDao;// 中心dao

    /**
     * 查询待办任务(接收人为登陆用户本人)
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @param userId
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findUndealMissionList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes, String userId) {
        String queryString = "select t,l,u from LsSystemTasksBO t,LsSystemTasksDealBO l,LsSystemUserBO u where 1=1 "
                + " and t.taskId=l.taskId " + " and t.publisher=u.userId " + " and l.receiveUser='" + userId + "'"
                + " and l.dealType='" + 0 + "'" + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = tasksDao.pageQuery(queryString, pageQuery);
        PageList<SystemTasksLogVO> pageList = new PageList<SystemTasksLogVO>();

        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                SystemTasksLogVO systemTasksVO = new SystemTasksLogVO();
                BeanUtils.copyProperties((LsSystemTasksBO) objs[0], systemTasksVO);
                BeanUtils.copyProperties((LsSystemTasksDealBO) objs[1], systemTasksVO);
                BeanUtils.copyProperties((LsSystemUserBO) objs[2], systemTasksVO);
                pageList.add(systemTasksVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 查出 系统中所有的待办任务
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findUndealMissionList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) {
        String queryString = "select t,l,u from LsSystemTasksBO t,LsSystemTasksDealBO l,LsSystemUserBO u where 1=1 "
                + " and t.taskId=l.taskId " + " and t.publisher=u.userId " + " and l.dealType='" + 0 + "'"
                + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = tasksDao.pageQuery(queryString, pageQuery);
        PageList<SystemTasksLogVO> pageList = new PageList<SystemTasksLogVO>();

        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                SystemTasksLogVO systemTasksVO = new SystemTasksLogVO();
                BeanUtils.copyProperties((LsSystemTasksBO) objs[0], systemTasksVO);
                BeanUtils.copyProperties((LsSystemTasksDealBO) objs[1], systemTasksVO);
                BeanUtils.copyProperties((LsSystemUserBO) objs[2], systemTasksVO);
                pageList.add(systemTasksVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    @SuppressWarnings("unchecked")
    public List<LsSystemTasksBO> findUndealMissionList() {
        return (List<LsSystemTasksBO>) tasksDao.findAllUndealMission();
    }

    @SuppressWarnings("unchecked")
    public List<LsSystemTasksBO> findUndealMissionList(String id) {
        return (List<LsSystemTasksBO>) tasksDao.findAllUndealMission(id);
    }

    public LsSystemTasksDealBO findByIdAndReceiver(String taskId, String userId) {
        return taskDealDao.findLog(taskId, userId);
    }

    public void updateTask(LsSystemTasksBO taskBo) {
        tasksDao.merge(taskBo);
    }

    public void updateTask(LsSystemTasksDealBO taskDeal) {
        taskDealDao.merge(taskDeal);
    }

    public LsSystemTasksBO findById(String id) {
        return tasksDao.findById(id);
    }

    public int findCount(String userId) {
        return (tasksDao.findNumber(userId));
    }

    /**
     * 查找“报警处理”类型的任务(接收人为登陆用户)
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsSystemTasksBO> findAlarmMissionList(String userId) {
        return (List<LsSystemTasksBO>) tasksDao.findAllUndealAlarmMission(userId);
    }

    /**
     * 查找所有的“报警处理”类型的任务
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsSystemTasksBO> findAllAlarmMissionList() {
        return (List<LsSystemTasksBO>) tasksDao.findAllUndealAlarmMission();
    }

    /**
     * 查出巡逻队护送的任务
     * 
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsSystemTasksBO> findEscortMissionList(String userId) {
        return (List<LsSystemTasksBO>) tasksDao.findAllUndealEscortMission(userId);
    }

    @SuppressWarnings("unchecked")
    public List<LsSystemTasksBO> findEscortMissionList() {
        return (List<LsSystemTasksBO>) tasksDao.findAllUndealEscortMission();
    }

    /**
     * 查询设备调度任务
     * 
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsSystemTasksBO> findDispatchMissionList(String userId) {
        return (List<LsSystemTasksBO>) tasksDao.findAllUndealDispatchMission(userId);
    }

    @SuppressWarnings("unchecked")
    public List<LsSystemTasksBO> findDispatchMissionList() {
        return (List<LsSystemTasksBO>) tasksDao.findAllUndealDispatchMission();
    }

    public LsSystemTasksDealBO findByProperty(String id) {
        return taskDealDao.findByProperty("taskId", id);
    }

    public void addTask(LsSystemTasksBO systemTasksBO) {
        tasksDao.save(systemTasksBO);
    }

    public void addTaskDeal(LsSystemTasksDealBO systemTasksDealBO) {
        taskDealDao.save(systemTasksDealBO);
    }
}
