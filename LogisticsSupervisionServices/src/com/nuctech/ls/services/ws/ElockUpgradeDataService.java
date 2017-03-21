package com.nuctech.ls.services.ws;

import javax.jws.WebService;

/**
 * 关锁升级的服务
 * 
 * @author 姜永权
 *
 */
@WebService(targetNamespace = "http://ws.elock.upgrade.nuctech.com/")
public interface ElockUpgradeDataService {

    /**
     * 获取关锁信息
     * 
     * @return
     */
    public String getElockInfo();

}
