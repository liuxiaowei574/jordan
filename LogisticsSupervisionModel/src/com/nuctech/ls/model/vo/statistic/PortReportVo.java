package com.nuctech.ls.model.vo.statistic;

import com.nuctech.util.MessageResourceUtil;

/**
 * @author liangpengfei
 * 
 * 
 *         一个示例，口岸动态报表，
 *         该类没有实际业务价值，可删除
 *
 */
public class PortReportVo extends DynamicSetting<PortReportVo> {

    private String name;
    private String weixius;
    private String keyongs;
    private String zaitus;
    private String sunhuais;
    private String xingchengkaishi;
    private String xingchengjieshu;
    private String zaixianshichang;
    private String avgtimes;
    private String arrival;
    private String departure;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getWeixius() {
        return weixius;
    }

    public void setWeixius(String weixius) {
        this.weixius = weixius;
    }

    public String getKeyongs() {
        return keyongs;
    }

    public void setKeyongs(String keyongs) {
        this.keyongs = keyongs;
    }

    public String getZaitus() {
        return zaitus;
    }

    public void setZaitus(String zaitus) {
        this.zaitus = zaitus;
    }

    public String getSunhuais() {
        return sunhuais;
    }

    public void setSunhuais(String sunhuais) {
        this.sunhuais = sunhuais;
    }

    public String getXingchengkaishi() {
        return xingchengkaishi;
    }

    public void setXingchengkaishi(String xingchengkaishi) {
        this.xingchengkaishi = xingchengkaishi;
    }

    public String getXingchengjieshu() {
        return xingchengjieshu;
    }

    public void setXingchengjieshu(String xingchengjieshu) {
        this.xingchengjieshu = xingchengjieshu;
    }

    public String getZaixianshichang() {
        return zaixianshichang;
    }

    public void setZaixianshichang(String zaixianshichang) {
        this.zaixianshichang = zaixianshichang;
    }

    public String getAvgtimes() {
        return avgtimes;
    }

    public void setAvgtimes(String avgtimes) {
        this.avgtimes = avgtimes;
    }

    @Override
    protected String tgetColumns() {
        return "[[" + "{" + "field: 'name'," + "title: '" + MessageResourceUtil.getMessageInfo("jgxctj.jsp.port") + "',"
                + "align: 'center'," + "switchable:false," + "rowspan:2," + "sortable:true" + "},{"
                + "field: '_mutilple_select_1'," + "title: '"
                + MessageResourceUtil.getMessageInfo("statistic.dynamic.port.shebei") + "'," + "align: 'center',"
                + "colspan:4" + "},{" + "field: '_mutilple_select_2'," + "title: '"
                + MessageResourceUtil.getMessageInfo("statistic.dynamic.port.renyuan") + "'," + "align: 'center',"
                + "colspan:4" + "},{" + "field: '_mutilple_select_3'," + "title: '"
                + MessageResourceUtil.getMessageInfo("statistic.dynamic.dim.xincheng") + "'," + "align: 'center',"
                + "colspan:4" + "}],[" + "{" + "  field: 'weixius'," + "  title: '"
                + MessageResourceUtil.getMessageInfo("statistic.dynamic.weixius") + "'," + "  align: 'center',"
                + " switchable:true," + "  sortable:true" + "}, {" + "   field: 'keyongs'," + "  title: '"
                + MessageResourceUtil.getMessageInfo("statistic.dynamic.keyongs") + "'," + " sortable:true,"
                + "align: 'center'" + "}, {" + "   field: 'zaitus'," + "  title: '"
                + MessageResourceUtil.getMessageInfo("statistic.dynamic.zaitus") + "'," + " sortable:true,"
                + "align: 'center'" + "}, {" + "   field: 'sunhuais'," + "  title: '"
                + MessageResourceUtil.getMessageInfo("statistic.dynamic.sunhuas") + "'," + " sortable:true,"
                + "align: 'center'" + "},{" + "   field: 'xingchengkaishi'," + "  title: '"
                + MessageResourceUtil.getMessageInfo("analysis.portanalysis.jsp.tripstartnum") + "',"
                + " sortable:true," + " switchable:true," + "align: 'center'" + "}, {" + "   field: 'xingchengjieshu',"
                + "  title: '" + MessageResourceUtil.getMessageInfo("analysis.portanalysis.jsp.tripendnum") + "',"
                + " sortable:true," + "align: 'center'" + "}, {" + "   field: 'zaixianshichang'," + "  title: '"
                + MessageResourceUtil.getMessageInfo("yhhyd.jsp.loadtimes") + "'," + " sortable:true,"
                + "align: 'center'" + "}, {" + "   field: 'avgtimes'," + "  title: '"
                + MessageResourceUtil.getMessageInfo("analysis.portanalysis.jsp.avgtime") + "'," + " sortable:true,"
                + "align: 'center'" + "}, {" + "   field: 'arrival'," + "  title: '"
                + MessageResourceUtil.getMessageInfo("jgxctj.jsp.arrival") + "'," + " sortable:true,"
                + "align: 'center'" + "}, {" + "   field: 'departure'," + "  title: '"
                + MessageResourceUtil.getMessageInfo("jgxctj.jsp.departure") + "'," + " sortable:true,"
                + "align: 'center'" + "}" + "]]";
    }

    @Override
    public String tgetTheamName() {
        // TODO Auto-generated method stub
        return MessageResourceUtil.getMessageInfo("statistic.dynamic.port");
    }

    @Override
    public ReportDimensionVo[] tgetDimentionsName() {

        return new ReportDimensionVo[] {
                new ReportDimensionVo()
                        .addColumns(new String[] { "_mutilple_select_1", "weixius", "keyongs", "zaitus", "sunhuais" })
                        .addName(MessageResourceUtil.getMessageInfo("statistic.dynamic.port.shebei")),
                new ReportDimensionVo()
                        .addColumns(new String[] { "_mutilple_select_2", "xingchengkaishi", "xingchengjieshu",
                                "avgtimes", "zaixianshichang" })
                        .addName(MessageResourceUtil.getMessageInfo("statistic.dynamic.port.renyuan")),
                new ReportDimensionVo().addColumns(new String[] { "_mutilple_select_3", "", "departure", "arrival" })
                        .addName(MessageResourceUtil.getMessageInfo("statistic.dynamic.dim.xincheng")) };
    }

    @Override
    public String tgetRequestUrl() {
        // TODO Auto-generated method stub
        return "/statisitc/queryData.action";
    }

}
