package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.common.LsCommonGoodsTypeBO;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.vo.statistic.GoodsTypeStatisVo;
import com.nuctech.util.MessageResourceUtil;
import com.nuctech.util.NuctechUtil;

@Repository
public class GoodsStatisticsDao extends LSBaseDao<LsCommonGoodsTypeBO, Serializable> {
    // int type[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
    // 18, 19, 20, 21, 22 };

    @SuppressWarnings("unchecked")
    public List<GoodsTypeStatisVo> getGoodsList(String goodsType) {
        Session session = this.getSession();
        List<Integer> numList = new ArrayList<Integer>();
        List<String> nameList = new ArrayList<String>();
        String sql = "";
        String s1 = "GOODS_TYPE";
        List<GoodsTypeStatisVo> goodsTypeStatisVos = new ArrayList<>();
        if (NuctechUtil.isNotNull(goodsType)) {
            int goodsTypes = 0;
            sql = "SELECT v.* FROM LS_COMMON_VEHICLE v where v.GOODS_TYPE like '%" + goodsType + "%'";
            List<LsCommonVehicleBO> vehicleList = session.createSQLQuery(sql).addEntity(LsCommonVehicleBO.class).list();
            for (int i = 0; i < vehicleList.size(); i++) {
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + goodsType + ",") != -1) {
                    goodsTypes++;
                }
            }

            numList.add(goodsTypes);
            String s2 = "GoodsType." + s1 + goodsType;
            String s3 = MessageResourceUtil.getMessageInfo(s2);

            nameList.add(s3);

            GoodsTypeStatisVo goodsTypeStatisVo = new GoodsTypeStatisVo();
            goodsTypeStatisVo.setGtypeName((String) nameList.get(0));
            goodsTypeStatisVo.setGtypeNum(numList.get(0));
            goodsTypeStatisVos.add(goodsTypeStatisVo);

        } else {
            int goodsType1 = 0;
            int goodsType2 = 0;
            int goodsType3 = 0;
            int goodsType4 = 0;
            int goodsType5 = 0;
            int goodsType6 = 0;
            int goodsType7 = 0;
            int goodsType8 = 0;
            int goodsType9 = 0;
            int goodsType10 = 0;
            int goodsType11 = 0;
            int goodsType12 = 0;
            int goodsType13 = 0;
            int goodsType14 = 0;
            int goodsType15 = 0;
            int goodsType16 = 0;
            int goodsType17 = 0;
            int goodsType18 = 0;
            int goodsType19 = 0;
            int goodsType20 = 0;
            int goodsType21 = 0;
            int goodsType22 = 0;
            sql = "SELECT v.* FROM LS_COMMON_VEHICLE v";
            List<LsCommonVehicleBO> vehicleList = session.createSQLQuery(sql).addEntity(LsCommonVehicleBO.class).list();
            for (int i = 0; i < vehicleList.size(); i++) {
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "1" + ",") != -1) {
                    goodsType1++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "2" + ",") != -1) {
                    goodsType2++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "3" + ",") != -1) {
                    goodsType3++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "4" + ",") != -1) {
                    goodsType4++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "5" + ",") != -1) {
                    goodsType5++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "6" + ",") != -1) {
                    goodsType6++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "7" + ",") != -1) {
                    goodsType7++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "8" + ",") != -1) {
                    goodsType8++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "9" + ",") != -1) {
                    goodsType9++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "10" + ",") != -1) {
                    goodsType10++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "11" + ",") != -1) {
                    goodsType11++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "12" + ",") != -1) {
                    goodsType12++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "13" + ",") != -1) {
                    goodsType13++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "14" + ",") != -1) {
                    goodsType14++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "15" + ",") != -1) {
                    goodsType15++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "16" + ",") != -1) {
                    goodsType16++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "17" + ",") != -1) {
                    goodsType17++;
                }

                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "18" + ",") != -1) {
                    goodsType18++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "19" + ",") != -1) {
                    goodsType19++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "20" + ",") != -1) {
                    goodsType20++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "21" + ",") != -1) {
                    goodsType21++;
                }
                if (NuctechUtil.isNotNull(vehicleList.get(i).getGoodsType())
                        && ("," + vehicleList.get(i).getGoodsType() + ",").indexOf("," + "22" + ",") != -1) {
                    goodsType22++;
                }
            }

            numList.add(goodsType1);
            numList.add(goodsType2);
            numList.add(goodsType3);
            numList.add(goodsType4);
            numList.add(goodsType5);
            numList.add(goodsType6);
            numList.add(goodsType7);
            numList.add(goodsType8);
            numList.add(goodsType9);
            numList.add(goodsType10);
            numList.add(goodsType11);
            numList.add(goodsType12);
            numList.add(goodsType13);
            numList.add(goodsType14);
            numList.add(goodsType15);
            numList.add(goodsType16);
            numList.add(goodsType17);
            numList.add(goodsType18);
            numList.add(goodsType19);
            numList.add(goodsType20);
            numList.add(goodsType21);
            numList.add(goodsType22);

            String goodstype1 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE1");
            String goodstype2 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE2");
            String goodstype3 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE3");
            String goodstype4 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE4");
            String goodstype5 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE5");
            String goodstype6 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE6");
            String goodstype7 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE7");
            String goodstype8 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE8");
            String goodstype9 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE9");
            String goodstype10 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE10");
            String goodstype11 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE11");
            String goodstype12 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE12");
            String goodstype13 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE13");
            String goodstype14 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE14");
            String goodstype15 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE15");
            String goodstype16 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE16");
            String goodstype17 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE17");
            String goodstype18 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE18");
            String goodstype19 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE19");
            String goodstype20 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE20");
            String goodstype21 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE21");
            String goodstype22 = MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE22");
            nameList.add(goodstype1);
            nameList.add(goodstype2);
            nameList.add(goodstype3);
            nameList.add(goodstype4);
            nameList.add(goodstype5);
            nameList.add(goodstype6);
            nameList.add(goodstype7);
            nameList.add(goodstype8);
            nameList.add(goodstype9);
            nameList.add(goodstype10);
            nameList.add(goodstype11);
            nameList.add(goodstype12);
            nameList.add(goodstype13);
            nameList.add(goodstype14);
            nameList.add(goodstype15);
            nameList.add(goodstype16);
            nameList.add(goodstype17);
            nameList.add(goodstype18);
            nameList.add(goodstype19);
            nameList.add(goodstype20);
            nameList.add(goodstype21);
            nameList.add(goodstype22);

            for (int i = 0; i < 22; i++) {
                GoodsTypeStatisVo goodsTypeStatisVo = new GoodsTypeStatisVo();
                goodsTypeStatisVo.setGtypeName((String) nameList.get(i));
                goodsTypeStatisVo.setGtypeNum(numList.get(i));
                goodsTypeStatisVos.add(goodsTypeStatisVo);
            }
        }
        return goodsTypeStatisVos;
    }
}
