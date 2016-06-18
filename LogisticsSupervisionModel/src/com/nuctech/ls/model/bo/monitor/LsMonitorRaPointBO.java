package com.nuctech.ls.model.bo.monitor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[自定义规划点(route & area) ]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_MONITOR_RA_POINT")
public class LsMonitorRaPointBO {
	/**
	 * 缺省的构造函数
	 */
	public LsMonitorRaPointBO() {
		super();
	}

	/* 坐标点主键 */
	private String pointId;

	/* 路线区域主键 */
	private String routeAreaId;

	/* 位置序列号 */
	private int gpsSeq;

	/* 经度 */
	private String longitude;

	/* 纬度 */
	private String latitude;

	@Id
	@Column(name = "POINT_ID", nullable = false, length = 50)
	public String getPointId() {
		return this.pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	@Column(name = "ROUTE_AREA_ID", nullable = true, length = 50)
	public String getRouteAreaId() {
		return this.routeAreaId;
	}

	public void setRouteAreaId(String routeAreaId) {
		this.routeAreaId = routeAreaId;
	}

	@Column(name = "GPS_SEQ", nullable = true, length = 50)
	public int getGpsSeq() {
		return this.gpsSeq;
	}

	public void setGpsSeq(int gpsSeq) {
		this.gpsSeq = gpsSeq;
	}

	@Column(name = "LONGITUDE", nullable = true, length = 20)
	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Column(name = "LATITUDE", nullable = true, length = 20)
	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
