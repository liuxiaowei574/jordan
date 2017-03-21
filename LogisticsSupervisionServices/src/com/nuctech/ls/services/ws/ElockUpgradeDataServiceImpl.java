package com.nuctech.ls.services.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.dao.WarehouseElockDao;
import com.nuctech.ls.services.ws.vo.ElockUpgradeDataVO;
import com.nuctech.util.NuctechUtil;
import com.thoughtworks.xstream.XStream;

/**
 * 关锁升级的服务
 * 
 * @author 姜永权
 *
 */
@Component(value = "elockUpgradeDataServiceImpl")
@WebService(endpointInterface = "com.nuctech.ls.services.ws.ElockUpgradeDataService",
        serviceName = "ElockUpgradeDataService", targetNamespace = "http://ws.elock.upgrade.nuctech.com/")
public class ElockUpgradeDataServiceImpl implements ElockUpgradeDataService {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Resource
    private WarehouseElockDao warehouseDao;

    /**
     * 将口岸的关锁数据同步到中心
     */
    @Override
    public String getElockInfo() {
        List<Object[]> list = warehouseDao.elockUpgradeInfo();
        List<ElockUpgradeDataVO> lockUpgradeVOList = new ArrayList<ElockUpgradeDataVO>();
        for (int i = 0; i < list.size(); i++) {
            ElockUpgradeDataVO ElockUpgradeDataVO = new ElockUpgradeDataVO();

            LsWarehouseElockBO warehouseElockBO = (LsWarehouseElockBO) (list.get(i)[0]);
            // 设置关锁号
            ElockUpgradeDataVO.setElockNumber(warehouseElockBO.getElockNumber());
            // 所属口岸名称
            ElockUpgradeDataVO.setBelongToPortName(warehouseElockBO.getBelongTo());
            // 是否在途
            String isTripActivate = "";
            if (NuctechUtil.isNotNull(warehouseElockBO.getElockStatus())) {
                // isTripActivate，1表示在途,0表示不是
                isTripActivate = warehouseElockBO.getElockStatus().equals("2") ? "1" : "0";
            }
            ElockUpgradeDataVO.setIsTripActivate(isTripActivate);
            if (NuctechUtil.isNotNull(list.get(i)[1])) {
                ElockUpgradeDataVO.setElectricityValue(((LsMonitorVehicleStatusBO) list.get(i)[1])
                        .getElectricityValue());
            }
            lockUpgradeVOList.add(ElockUpgradeDataVO);
        }
        XStream stram = new XStream();
        stram.processAnnotations(ElockUpgradeDataVO.class);
        return stram.toXML(lockUpgradeVOList);
    }
}
