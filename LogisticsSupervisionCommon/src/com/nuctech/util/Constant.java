package com.nuctech.util;

/**
 * Constant
 *
 */

public class Constant {

    /**
     * 车辆推送websocket访问地址
     */
    public static final String WSGPSURL = "wsGpsUrl";
    /**
     * 通知推送websocket访问地址
     */
    public static final String WSNOTICEURL = "wsNoticeUrl";

    /**
     * 有效标记
     */
    public static final String VALID_FLAG = "validFlag";

    /**
     * 无效标记
     */
    public static final String INVALID_FLAG = "invalidFlag";

    /**
     * session 用户
     */
    public static final String SESSION_USER = "sessionUser";

    /**
     * 登录用户日志ID
     */
    public static final String USER_LOG_ID = "userLogId";
    /**
     * 约旦日期格式化字符串
     */
    public static final String JordanTimeFormat = "yyyy-MM-dd HH:mm:ss";
    /**
     * 行程状态-待激活-0
     */
    public static final String TRIP_STATUS_TO_START = "0";
    /**
     * 行程状态-进行中-1
     */
    public static final String TRIP_STATUS_STARTED = "1";
    /**
     * 行程状态-待结束-2
     */
    public static final String TRIP_STATUS_TO_FINISH = "2";
    /**
     * 行程状态-已结束-3
     */
    public static final String TRIP_STATUS_FINISHED = "3";

    /**
     * 通知状态 -起草 -0
     */
    public static final String NOTICE_STATE_DRAFT = "0";
    /**
     * 通知状态 -发布 -1
     */
    public static final String NOTICE_STATE_PUBLISH = "1";
    /**
     * 通知状态 -完成 -2
     */
    public static final String NOTICE_STATE_FINISH = "2";

    /**
     * 通知接收人 -未处理接收通知 -0
     */
    public static final String NOTICE_UN_HANDLED = "0";

    /**
     * 通知接收人 -已处理接收通知 -1
     */
    public static final String NOTICE_HANDLED = "1";
    /**
     * GPS推送数据
     */
    public static final String WEBSOCKET_DATA_TYPE_GPS = "0";
    /**
     * 通知推送数据
     */
    public static final String WEBSOCKET_DATA_TYPE_NOTICE = "1";

    /**
     * 报警处理通知推送数据
     */
    public static final String WEBSOCKET_DATA_TYPE_ALARMHANDLE = "4";
    /**
     * 行程请求推送数据
     */
    public static final String WEBSOCKET_DATA_TYPE_TRIP = "2";
    /**
     * 行程结果推送数据
     */
    public static final String WEBSOCKET_DATA_TYPE_TRIP_RESULT = "3";
    /**
     * 调度完成向巡逻队推送数据
     */
    public static final String WEBSOCKET_DATA_TYPE_DISPATCH_TOPATROL = "5";
    /**
     * 向用户推送下线消息
     */
    public static final String WEBSOCKET_DATA_TYPE_OFFLINE = "6";
    /**
     * 调度完成向控制中心推送消息
     */
    public static final String WEBSOCKET_DATA_TYPE_DISPATCH_TOCONTROLROOM = "7";
    
    /**
     * 报警中心向巡逻队推送任务通知
     */
    public static final String WEBSOCKET_DATA_TYPE_SEND_MISSION = "8";
    /**
     * 控制中心普通员工发起交班请求，请求准管审批
     */
    public static final String WEBSOCKET_DATA_TYPE_MANAGER_AUDIT = "9";
    /**
     * 控制中心普通员工发起交班请求，请求准管审批，审批通过返回消息给交班请求人员
     */
    public static final String WEBSOCKET_DATA_TYPE_MANAGER_ALLOW_AUDIT = "10";
    /**
     * 控制中心普通员工发起交班请求，请求准管审批，审批通过返回消息给任务接收人
     */
    public static final String WEBSOCKET_DATA_TYPE_TASK_TO_RECEIVER = "11";

    /**
     * 控制中心普通员工发起交班请求，请求准管审批，审批不通过返回消息给任务接收人
     */
    public static final String WEBSOCKET_DATA_TYPE_MANAGER_NOT_ALLOW_AUDIT = "12";

    /**
     * 控制中心接到巡逻队到达临界区域时，选择巡逻队发送通知准备交接调度设备
     */
    public static final String WEBSOCKET_DATA_TYPE_DISPATCH_HANDOVER = "13";

    /**
     * followUpUser接到巡逻队到达临界区域时，选择巡逻队发送通知准备行程护送交接
     */
    public static final String WEBSOCKET_DATA_TYPE_ESCORT_HANDOVER = "14";
    
    /**
     * 控制中心用户执行调度后给有关口岸用户发送通知
     */
    public static final String WEBSOCKET_DATA_TYPE_NOTICE_TOPORT = "15";

    /**
     * 组织机构类型-国家-1
     */
    public static final String ORGANIZATION_TYPE_COUNTRY = "1";
    /**
     * 组织机构类型-口岸-2
     */
    public static final String ORGANIZATION_TYPE_PORT = "2";
    /**
     * 组织机构类型-中心（控制中心，质量中心等）-3
     */
    public static final String ORGANIZATION_CENTER = "3";
    /**
     * 组织机构类型-监管场所-3
     */
    public static final String ORGANIZATION_TYPE_JGCS = "3";
    /**
     * 组织机构类型-建管中心-4
     */
    public static final String ORGANIZATION_TYPE_CENTER = "4";
    /**
     * 组织机构类型-口岸-2
     */
    public static final String ORGANIZATION_TYPE_ROOM = "5";

    /**
     * 设备申请状态-已申请-1
     */
    public static final String DEVICE_ALREADY_APPLIED = "1";

    /**
     * 设备申请状态-已处理-2
     */
    public static final String DEVICE_ALREADY_DEAL_WITH = "2";

    /**
     * 设备申请状态-已完成-3
     */
    public static final String DEVICE_ALREADY_FINISH = "3";

    /**
     * 设备调度状态-未调度-0
     */
    public static final String DEVICE_UN_DISPATCH = "0";
    /**
     * 设备调度状态-调度完成-1
     */
    public static final String DEVICE_FINISH_DISPATCH = "1";
    /**
     * 设备接收状态-已接收-1
     */
    public static final String recviceStatus = "1";
    /**
     * 设备接收状态-未接收-0
     */
    public static final String unRecviceStatus = "0";
    /**
     * 库存统计-流入
     */
    public static final String FLOW_IN = "flowIn";

    /**
     * 库存统计-流出
     */
    public static final String FLOW_OUT = "flowOut";
    /**
     * 删除标记-未删除-0
     */
    public static final String MARK_UN_DELETED = "0";
    /**
     * 删除标记-已删除-0
     */
    public static final String MARK_DELETED = "1";
    /**
     * 不是仓库-0
     */
    public static final String ORGANIZATION_IS_NOT_ROOM = "0";
    /**
     * 是仓库-1
     */
    public static final String ORGANIZATION_IS_ROOM = "1";

    /**
     * jsp页面foot按钮点击类型划分： 路线规划0：路线； 场地管理1：监管区域； 区域管理2：安全区域、危险区域、行政区域； 其他：7
     * 巡逻队列表；8 历史车辆列表； 9 车辆列表
     */
    public static final String BUTTON_TYPE_LINE = "0";
    public static final String BUTTON_TYPE_CDGL = "1";
    public static final String BUTTON_TYPE_QYGL = "2";
    public static final String BUTTON_TYPE_XLD = "7";
    public static final String BUTTON_TYPE_LSCL = "8";
    public static final String BUTTON_TYPE_CLLB = "9";
    /**
     * 路线规划：0-路线；场地管理1：3-监管区域；区域管理2：1-安全区域，2-危险区域，4-行政区域；其他 地图规划类型-路线
     */
    public static final String ROUTEAREA_TYPE_LINE = "0";
    /**
     * 地图规划类型-安全区域
     */
    public static final String ROUTEAREA_TYPE_AQQY = "1";
    /**
     * 地图规划类型-危险区域
     */
    public static final String ROUTEAREA_TYPE_WXQY = "2";
    /**
     * 地图规划类型-监管区域
     */
    public static final String ROUTEAREA_TYPE_JGQY = "3";
    /**
     * 地图规划类型-行政区域
     */
    public static final String ROUTEAREA_TYPE_QYHF = "4";
    /**
     * 地图规划类型-Targe Zoon
     */
    public static final String ROUTEAREA_TYPE_TARGET = "5";
    /**
     * 行程通知类型-请求-1
     */
    public static final String NOTICE_TYPE_TRIP_REQUEST = "1";
    /**
     * 行程通知类型-处理结果-2
     */
    public static final String NOTICE_TYPE_TRIP_RESULT = "2";
    /**
     * 行程请求处理结果-通过-1
     */
    public static final String TRIP_RESULT_TYPE_PASS = "1";
    /**
     * 行程请求处理结果-未通过-2
     */
    public static final String TRIP_RESULT_TYPE_NOPASS = "2";
    /**
     * 行程请求类型-激活-1
     */
    public static final String TRIP_ACTIVATE = "1";
    /**
     * 行程请求类型-结束-2
     */
    public static final String TRIP_FINISH = "2";

    /**
     * 功能类型-0-菜单
     */
    public static final String FUNCTION_TYPE_MENU = "0";

    /**
     * 功能类型-1-功能
     */
    public static final String FUNCTION_TYPE_FUNCTION = "1";

    /**
     * 功能类型-2-按钮
     */
    public static final String FUNCTION_TYPE_BUTTON = "2";

    /**
     * 行程照片访问路径
     */
    public static final String TRIP_PHOTO_PATH_HTTP = "tripPhotoPathHttp";
    /**
     * 未经处理的通知的数量
     */
    public static final String needDealNoticeCount = "needDealNoticeCount";
    /**
     * 待办任务数量
     */
    public static final String needDealMissionCount = "needDealMissionCount";
    /**
     * 报警状态
     */
    public static final String alarmStatus_dealed = "2";

    /**
     * 接收报警
     */
    public static final String FREEZE_ALARM_YES = "0";
    /**
     * 不接收报警
     */
    public static final String FREEZE_ALARM_NO = "1";
    /**
     * 行程结束的特殊申请标识
     */
    public static final String TRIP_SPECIAL_APPLY = "1";
    /**
     * 单页最大数据量
     */
    public static final int MAX_PAGE_SIZE = 50000;
}
