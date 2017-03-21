package com.nuctech.ls.model.bo.monitor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[手动添加的LandMarker信息]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_MONITOR_LANDMARKER")
public class LsMonitorLandMarkerBO implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 缺省的构造函数
     */
    public LsMonitorLandMarkerBO() {
        super();
    }
  
    private String landId;
    
    private String landName;
    
    private String landImage;
    
    private String latitude;
    
    private String longitude;
    
    private String description;


    @Id
    @Column(name = "LAND_ID", nullable = false, length = 50)
    public String getLandId() {
        return landId;
    }

    
    public void setLandId(String landId) {
        this.landId = landId;
    }

    @Column(name = "LAND_NAME", nullable = true, length = 100)
    public String getLandName() {
        return landName;
    }

    
    public void setLandName(String landName) {
        this.landName = landName;
    }

    @Column(name = "LAND_IMAGE", nullable = true, length = 2000)
    public String getLandImage() {
        return landImage;
    }

    
    public void setLandImage(String landImage) {
        this.landImage = landImage;
    }

    @Column(name = "LATITUDE", nullable = true, length = 100)
    public String getLatitude() {
        return latitude;
    }

    
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Column(name = "LONGITUDE", nullable = true, length = 100)
    public String getLongitude() {
        return longitude;
    }

    
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Column(name = "DESCRIPTION", nullable = true, length = 1000)
    public String getDescription() {
        return description;
    }

    
    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
