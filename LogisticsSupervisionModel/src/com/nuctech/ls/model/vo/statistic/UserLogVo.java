package com.nuctech.ls.model.vo.statistic;

/**
 * 统计模块-用户活跃度统计用户查询实体
 * @author liutonglei
 *
 */
import java.util.Date;

public class UserLogVo {

    /* 用户主键 */
    private String userId;

    /* 用户名 */
    private String userAccount;

    /* 姓名 */
    private String userName;

    /* 登录时间 */
    private Date logonTime;

    /* 登出时间 */
    private Date logoutTime;

    /* 用户登录累计时长-min (分钟) */
    private long onlineTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getLogonTime() {
        return logonTime;
    }

    public void setLogonTime(Date logonTime) {
        this.logonTime = logonTime;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    public long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
    }

}
