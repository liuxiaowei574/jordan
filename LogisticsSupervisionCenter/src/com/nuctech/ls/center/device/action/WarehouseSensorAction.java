package com.nuctech.ls.center.device.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseSensorBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.WarehouseSensorService;
import com.nuctech.ls.model.util.OperateContentType;
import com.nuctech.ls.model.util.OperateEntityType;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.SensorDepartmentVO;
import com.nuctech.util.Constant;
import com.nuctech.util.MessageResourceUtil;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 作者：赵苏阳 描述：
 * <p>
 * 传感器管理 增删改查
 * <p>
 * 创建时间2016年5月28
 *
 */
@Namespace("/sensorMgmt")
public class WarehouseSensorAction extends LSBaseAction {

    private static final long serialVersionUID = -3413844565457684391L;
    protected static final String DEFAULT_SORT_COLUMNS = "e.sensorNumber ASC";
    private List<LsSystemDepartmentBO> sensorMgmtList = new ArrayList<LsSystemDepartmentBO>();
    private List<LsSystemDepartmentBO> sensorEditList = new ArrayList<LsSystemDepartmentBO>();
    private LsSystemDepartmentBO systemDepartmentBO;
    @Resource
    private SystemModules systemModules;
    private List randomSensorList = new ArrayList();

    /**
     * 传感器对象
     */
    private LsWarehouseSensorBO warehouseSensorBO;
    private String[] sensorIds; // 删除的时候获取前台表格传到后台的Id，
    // set方式注入
    @Resource
    private WarehouseSensorService warehouseSensorService;
    @Resource
    private SystemDepartmentService systemDepartmentService;
    @Resource
    private SystemOperateLogService logService;

    /**
     * 传感器添加
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "addSensor", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String addSensor() throws Exception {
        try {
            if (warehouseSensorBO != null) {
                // 判断传感器号是否已经存在，不得重复添加
                String sensorNumber = warehouseSensorBO.getSensorNumber();
                LsWarehouseSensorBO sensorBO = warehouseSensorService.findBySensorNumber(sensorNumber);
                if (NuctechUtil.isNotNull(sensorBO)) {
                    return ERROR;
                } else {
                    SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                    warehouseSensorBO.setSensorId(generatePrimaryKey());// 设置传感器的主键，自动存储到数据库中；
                    warehouseSensorBO.setCreateTime(new Date());// 设置传感器的创建时间。
                    warehouseSensorBO.setCreateUser(sessionUser.getUserId());// 获取子锁创建人即登陆人用户名
                    warehouseSensorBO.setSensorStatus(request.getParameter("s_sensorStatus"));
                    warehouseSensorBO.setBelongTo(request.getParameter("s_belongTo"));// 获取前台下拉列表框中的内容
                    warehouseSensorService.add(warehouseSensorBO);

                    addLog(OperateContentType.ADD.toString(), OperateEntityType.SENSOR.toString(),
                            warehouseSensorBO.toString());
                    return SUCCESS;
                }
            } else {
                return ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    /**
     * 添加modal的方法
     * 
     * @return
     */
    @Action(value = "addModal", results = { @Result(name = "success", location = "/device/sensorMgmt/sensorAdd.jsp"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String addModal() {
    	SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        sensorMgmtList = systemDepartmentService.findAllPortByUserId(userId);
      //口岸用户默认口岸为当前口岸
  		if(sessionUser.getRoleId().equals(RoleType.portUser.getType())){
  			request.setAttribute("orgId", sessionUser.getOrganizationId());
  			request.setAttribute("disabled", true);
  		}   else{
  			request.setAttribute("disabled", false);
  		}
        
        warehouseSensorBO = new LsWarehouseSensorBO();
        return SUCCESS;
    }

    /**
     * 编辑modal
     * 
     * @return
     */
    @Action(value = "editModal", results = { @Result(name = "success", location = "/device/sensorMgmt/sensorEdit.jsp"),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editModal() {
        if (warehouseSensorBO != null) {
            String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
            sensorEditList = systemDepartmentService.findAllPortByUserId(userId);

            String sensorId = warehouseSensorBO.getSensorId();
            if (!NuctechUtil.isNull(sensorId)) {
                warehouseSensorBO = this.warehouseSensorService.findById(sensorId);
                systemDepartmentBO = systemDepartmentService.findById(warehouseSensorBO.getBelongTo());
                return SUCCESS;
            } else {
                message = "Find Object Mis!";
                return ERROR;
            }
        } else {
            return ERROR;
        }
    }

    /**
     * 传感器修改
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "editSensor", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editSensor() throws Exception {
        if (warehouseSensorBO != null) {
            this.warehouseSensorService.modify(warehouseSensorBO);

            addLog(OperateContentType.EDIT.toString(), OperateEntityType.SENSOR.toString(),
                    warehouseSensorBO.toString());

            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    /**
     * 传感器删除
     * 
     * @param warehouseElockBO
     * @return
     * 
     */
    @Action(value = "delSensorById",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }) })
    public String delSensorById() {
        if (sensorIds != null) {
            String s[] = sensorIds[0].split(",");
            for (int i = 0; i < s.length; i++) {
                this.warehouseSensorService.deleteById(s[i]);
                addLog(OperateContentType.DELETE.toString(), OperateEntityType.ELOCK.toString(), "sensorId:" + s[i]);
            }
        }
        return SUCCESS;
    }

    /**
     * 传感器查询
     * 
     * @throws IOException
     */
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);

        String sortname = request.getParameter("sort");
        if ("organizationName".equals(sortname)) {
            pageQuery = this.newPageQuery("d.organizationName asc");
        }

        JSONObject retJson = warehouseSensorService.fromObjectList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    @Action(value = "dlist")
    public void dlist() throws IOException {
        /*
         * List<LsWarehouseSensorBO> sensorBOList =
         * warehouseSensorService.findAllelock();
         */// 单表查询
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String organizationId = sessionUser.getOrganizationId();

        List<SensorDepartmentVO> sensorBOList = warehouseSensorService.getSensorDepartmentlist(organizationId);
        JSONArray retJson = JSONArray.fromObject(sensorBOList);
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    @Action(value = "toList", results = { @Result(name = "success", location = "/device/sensorMgmt/sensorList.jsp") })
    public String toList() {
        String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        sensorMgmtList = systemDepartmentService.findAllPortByUserId(userId);
        
        //口岸用户隐藏"所属节点"过滤
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        if(sessionUser.getRoleId().equals(RoleType.portUser.getType())){
            request.setAttribute("filter", false);
        }else{
            request.setAttribute("filter", true);
        }
        return SUCCESS;
    }

    /**
     * 根据传感器号查询记录
     * 
     * @throws IOException
     */
    @Action(value = "findBySensorNumber")
    public void findBySensorNumber() throws IOException {
        String sensorNumber = request.getParameter("sensorNumber");
        LsWarehouseSensorBO warehouseSensorBO = warehouseSensorService.findBySensorNumber(sensorNumber);

        JSONObject json = new JSONObject();
        json.put("warehouseSensorBO", warehouseSensorBO);
        json.put("organizationId", ((SessionUser) session.get(Constant.SESSION_USER)).getOrganizationId());
        response.setCharacterEncoding("UTF-8");
        // 禁止浏览器开辟缓存
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
        out.close();
    }

    /**
     * 日志记录方法
     * 
     * @param content
     * @param params
     */
    private void addLog(String operate, String entity, String params) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        logService.addLog(operate, entity, sessionUser.getUserId(), this.getClass().toString(), params);
    }

    /**
     * 传感器重复验证
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "repeate")
    public void repeate() throws Exception {
        JSONObject jsonObject = new JSONObject();
        String sensorNumber = warehouseSensorBO.getSensorNumber();
        LsWarehouseSensorBO warehouseSensorBO = warehouseSensorService.findBySensorNumber(sensorNumber);
        if (NuctechUtil.isNull(warehouseSensorBO)) {
            jsonObject.put("valid", true);
        } else {
            jsonObject.put("valid", false);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
        out.flush();
        out.close();
    }

    /**
     * 下载excel模板到本地
     * 
     * @throws IOException
     */
    @Action(value = "downTemplate")
    public void downTemplate() throws IOException {
        String fileName = "";
        String root = request.getSession().getServletContext().getRealPath("/");// 获取项目的根目录；
        // 判断是中文环境还是英文环境；
        String language = MessageResourceUtil.getLocale().getLanguage();
        if (language.equals("zh")) {
            fileName = "Sensor_ZTemplate.xlsx";
        } else {
            fileName = "Sensor_ETemplate.xlsx";
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        InputStream inputStream;
        String string = "\\template";
        try {
            inputStream = new FileInputStream(new File(root + string + File.separator + fileName));
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            // 这里主要关闭。
            os.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File file;
    private String fileFileName;
    private String fileFileContentType;
    private String messageString;
    private JSONArray json_uploadimage_response;
    private String result;

    /**
     * 上传excel文件到服务器，并用poi解析上传的excel文件同时刷新数据库
     * 
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "unused", "resource" })
    @Action(value = "batchImportexcel",
            results = { @Result(name = "success", type = "json"), @Result(name = "error", type = "json") })
    public String batchImportexcel() throws IOException {
        try {
            // 接收前台传过来的文件并存放到指定目录下。
            String pathString = ServletActionContext.getServletContext().getRealPath("/");
            HttpSession session = ServletActionContext.getRequest().getSession();
            String filepathString = pathString + "excelTemplate";
            File folder = new File(filepathString);
            if (!folder.exists()) {
                folder.mkdir();
            }
            FileInputStream inputStream = new FileInputStream(this.getFile());
            FileOutputStream outputStream = new FileOutputStream(filepathString + "\\" + fileFileName);
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, length);
            }
            inputStream.close();
            outputStream.flush();
            // poi处理excel文件
            String excelfilePath = filepathString + File.separator + fileFileName;
            // 判断是否为excel类型文件
            if (!excelfilePath.endsWith(".xls") && !excelfilePath.endsWith(".xlsx")) {
                result = "excelEnd";
                return ERROR;
            }
            FileInputStream fis = null;
            Workbook wookbook = null;
            // 获取一个绝对地址的流
            fis = new FileInputStream(excelfilePath);
            // 2007版本的excel，用.xlsx结尾
            wookbook = new XSSFWorkbook(fis);// 得到工作簿
            // 得到一个工作表
            Sheet sheet = wookbook.getSheetAt(0);
            // 获得表头
            Row rowHead = sheet.getRow(0);
            // 获得数据的总行数
            int totalRowNum = sheet.getLastRowNum();
            // 要获得属性
            String sensorNumber = "";
            
          //不能导入重复子锁号的数据
            for (int i = 2; i <= totalRowNum; i++) {
            	 // 获得第i行对象
                Row row = sheet.getRow(i);
                // 获得获得第i行第0列的 String类型对象(关锁号)
                Cell cell = row.getCell((short) 0);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                sensorNumber = cell.getStringCellValue().toString();
                  //不能导入重复子锁号的数据
                if(!NuctechUtil.isNull(warehouseSensorService.findBySensorNumber(sensorNumber))){
	              	result = "existed";
	              	return ERROR;
               }
            }
            
            // 获得所有数据
            for (int i = 2; i <= totalRowNum; i++) {
                // 获得第i行对象
                Row row = sheet.getRow(i);
                // 获得获得第i行第0列的 String类型对象(关锁号)
                Cell cell = row.getCell((short) 0);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                sensorNumber = cell.getStringCellValue().toString();

                // 向数据库中存入数据
                SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                LsWarehouseSensorBO sensorBO = new LsWarehouseSensorBO();
                sensorBO.setSensorId(generatePrimaryKey());
                sensorBO.setCreateTime(new Date());
                sensorBO.setCreateUser(sessionUser.getUserId());
                sensorBO.setSensorStatus("1");// 默认添加的为正常
                sensorBO.setBelongTo(sessionUser.getOrganizationId());
                sensorBO.setCreateUser(sessionUser.getUserName());
                // 读取excel文件中的数据
                sensorBO.setSensorNumber(sensorNumber);
                warehouseSensorService.add(sensorBO);
            }
            result = "true";
            return SUCCESS;
        } catch (Exception e) {
            result = "false";
            return ERROR;
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public String getFileFileContentType() {
        return fileFileContentType;
    }

    public void setFileFileContentType(String fileFileContentType) {
        this.fileFileContentType = fileFileContentType;
    }

    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }

    public JSONArray getJson_uploadimage_response() {
        return json_uploadimage_response;
    }

    public void setJson_uploadimage_response(JSONArray json_uploadimage_response) {
        this.json_uploadimage_response = json_uploadimage_response;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LsWarehouseSensorBO getWarehouseSensorBO() {
        return warehouseSensorBO;
    }

    public void setWarehouseSensorBO(LsWarehouseSensorBO warehouseSensorBO) {
        this.warehouseSensorBO = warehouseSensorBO;
    }

    public String[] getSensorIds() {
        return sensorIds;
    }

    public void setSensorIds(String[] sensorIds) {
        this.sensorIds = sensorIds;
    }

    public WarehouseSensorService getWarehouseSensorService() {
        return warehouseSensorService;
    }

    public void setWarehouseSensorService(WarehouseSensorService warehouseSensorService) {
        this.warehouseSensorService = warehouseSensorService;
    }

    public List<LsSystemDepartmentBO> getSensorMgmtList() {
        return sensorMgmtList;
    }

    public void setSensorMgmtList(List<LsSystemDepartmentBO> sensorMgmtList) {
        this.sensorMgmtList = sensorMgmtList;
    }

    public List<LsSystemDepartmentBO> getSensorEditList() {
        return sensorEditList;
    }

    public void setSensorEditList(List<LsSystemDepartmentBO> sensorEditList) {
        this.sensorEditList = sensorEditList;
    }

    public LsSystemDepartmentBO getSystemDepartmentBO() {
        return systemDepartmentBO;
    }

    public void setSystemDepartmentBO(LsSystemDepartmentBO systemDepartmentBO) {
        this.systemDepartmentBO = systemDepartmentBO;
    }

    public SystemModules getSystemModules() {
        return systemModules;
    }

    public void setSystemModules(SystemModules systemModules) {
        this.systemModules = systemModules;
    }

}