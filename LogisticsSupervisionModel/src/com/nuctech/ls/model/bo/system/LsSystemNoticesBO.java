package com.nuctech.ls.model.bo.system;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 业务对象处理的实体-[系统公告表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SYSTEM_NOTICES")
public class LsSystemNoticesBO
{
    /**
     * 缺省的构造函数
     */
    public LsSystemNoticesBO()
    {
        super();
    }

    /* 通知主键 */
    private String noticeId;

    /* 通知主题 */
    private String noticeTitle;

    /* 通知内容 */
    private String noticeContent;

    /* 发布时间 */
    private Date deployTime;

    /* 发布人 */
    private String publisher;

    /* 通知人员 */
    private String noticeUsers;

    /* 通知完成状态
    0.起草 1.发布 2.结束 */
    private String noticeState;

    @Id
    @Column(name = "NOTICE_ID", nullable = false, length = 50)
    public String getNoticeId()
    {
        return this.noticeId;
    }

    public void setNoticeId(String noticeId)
    {
        this.noticeId = noticeId;
    }

    @Column(name = "NOTICE_TITLE", nullable = true, length = 100)
    public String getNoticeTitle()
    {
        return this.noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle)
    {
        this.noticeTitle = noticeTitle;
    }

    @Column(name = "NOTICE_CONTENT", nullable = true, length = 2000)
    public String getNoticeContent()
    {
        return this.noticeContent;
    }

    public void setNoticeContent(String noticeContent)
    {
        this.noticeContent = noticeContent;
    }

    @Column(name = "DEPLOY_TIME", nullable = true)
    public Date getDeployTime()
    {
        return this.deployTime;
    }

    public void setDeployTime(Date deployTime)
    {
        this.deployTime = deployTime;
    }

    @Column(name = "PUBLISHER", nullable = true, length = 50)
    public String getPublisher()
    {
        return this.publisher;
    }

    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    @Column(name = "NOTICE_USERS", nullable = true, length = 2000)
    public String getNoticeUsers()
    {
        return this.noticeUsers;
    }

    public void setNoticeUsers(String noticeUsers)
    {
        this.noticeUsers = noticeUsers;
    }

    @Column(name = "NOTICE_STATE", nullable = true, length = 2)
    public String getNoticeState()
    {
        return this.noticeState;
    }

    public void setNoticeState(String noticeState)
    {
        this.noticeState = noticeState;
    }
}
