package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemUserLogBO;
import com.nuctech.ls.model.vo.statistic.UserLogVo;

/**
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 用户登录详细日志信息 Dao
 * </p>
 * 创建时间：2016年5月17日
 */
@Repository
public class SystemUserLogDao extends LSBaseDao<LsSystemUserLogBO, Serializable> {

    /**
     * 根据用户名称查询用户信息及用户（历史累计）在线时长
     * 
     * @param userName
     * @return
     */
    // public PageList<UserLogVo> userDetail(PageQuery<Map> pageQuery, String userName ) {
    //
    // String queryString = "select u,l from LsSystemUserBO u, LsSystemUserLogBO l where u.userId =
    // l.logUser "+
    // "/~ and u.userName like '%[userName]%' ~/ "+
    // "/~ order by [sortColumns] ~/";
    // PageList<Object> queryList = pageQuery(queryString, pageQuery);
    // PageList<UserLogVo> tempPageList = new PageList<UserLogVo>();
    // PageList<UserLogVo> pageList = new PageList<UserLogVo>();
    //
    // if (queryList != null && queryList.size() > 0) {
    // for (Object obj : queryList) {
    // Object[] objs = (Object[]) obj;
    // UserLogVo ul = new UserLogVo();
    // BeanUtils.copyProperties(objs[0], ul);
    // BeanUtils.copyProperties(objs[1], ul);
    // tempPageList.add(ul);
    // }
    // }
    // if (tempPageList != null && tempPageList.size() > 0) {
    // for (int i = 0; i < tempPageList.size(); i++) {
    // long onlineTime = 0;//在线时长
    // if(tempPageList.get(i).getLogoutTime() != null){
    // long logoutTime1 = tempPageList.get(i).getLogoutTime().getTime();
    // long logonTime1 = tempPageList.get(i).getLogonTime().getTime();
    // onlineTime = (logoutTime1 - logonTime1)/(1000*60);
    // }
    // for (int j = i+1; j < tempPageList.size(); j++) {
    // if(tempPageList.get(i).getUserId()==tempPageList.get(j).getUserId()){
    // if(tempPageList.get(j).getLogoutTime() != null){
    // long logoutTime = tempPageList.get(j).getLogoutTime().getTime();
    // long logonTime = tempPageList.get(j).getLogonTime().getTime();
    // onlineTime += (logoutTime - logonTime)/(1000*60);
    // }
    //// tempPageList.remove(tempPageList.get(j));
    // }else{
    // j++;
    // }
    //
    // }
    //
    // if(onlineTime != 0){
    // tempPageList.get(i).setOnlineTime(onlineTime);
    // pageList.add(tempPageList.get(i));
    // }
    // }
    // }
    // pageList.setPage(pageQuery.getPage());
    // pageList.setPageSize(pageQuery.getPageSize());
    // pageList.setTotalItems(queryList.getTotalItems());
    // return pageList;
    // }

    @SuppressWarnings("unchecked")
    public List<UserLogVo> userDetail(String userName) {

        // String String1 = "select u.*,l.* FROM LS_SYSTEM_USER u inner join LS_SYSTEM_USER_LOG l on
        // u.USER_ID=l.LOG_USER";
        String sql = "select u.USER_ID,u.USER_ACCOUNT,u.USER_NAME,l.LOGON_TIME,l.LOGOUT_TIME "
                + "FROM LS_SYSTEM_USER as u " + "inner join LS_SYSTEM_USER_LOG as l " + "on u.USER_ID=l.LOG_USER";

        if (userName != null && userName.length() > 0) {
            sql += " where u.USER_NAME like '%" + userName + "%'";
        }

        List<UserLogVo> queryList = new ArrayList<UserLogVo>();
        queryList = this.getSession().createSQLQuery(sql).list();

        List<UserLogVo> tempPageList = new ArrayList<UserLogVo>();
        List<UserLogVo> pageList = new ArrayList<UserLogVo>();

        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                UserLogVo ul = new UserLogVo();
                ul.setUserId((String) objs[0]);
                ul.setUserAccount((String) objs[1]);
                ul.setUserName((String) objs[2]);
                ul.setLogonTime((Date) objs[3]);
                ul.setLogoutTime((Date) objs[4]);
                tempPageList.add(ul);
            }
        }
        if (tempPageList != null && tempPageList.size() > 0) {
            for (int i = 0; i < tempPageList.size(); i++) {
                long onlineTime = 0;// 在线时长
                if (tempPageList.get(i).getLogoutTime() != null) {
                    long logoutTime1 = tempPageList.get(i).getLogoutTime().getTime();
                    long logonTime1 = tempPageList.get(i).getLogonTime().getTime();
                    onlineTime = (logoutTime1 - logonTime1) / (1000 * 60);
                }
                for (int j = i + 1; j < tempPageList.size(); j++) {
                    if ((tempPageList.get(j).getUserName()).equals(tempPageList.get(i).getUserName())) {
                        if (tempPageList.get(j).getLogoutTime() != null) {
                            long logoutTime = tempPageList.get(j).getLogoutTime().getTime();
                            long logonTime = tempPageList.get(j).getLogonTime().getTime();
                            onlineTime += (logoutTime - logonTime) / (1000 * 60);
                        }
                        tempPageList.remove(j);
                        j--;
                    }

                }

                if (onlineTime != 0) {
                    tempPageList.get(i).setOnlineTime(onlineTime);
                    pageList.add(tempPageList.get(i));
                }
            }
        }
        return pageList;
    }
}
