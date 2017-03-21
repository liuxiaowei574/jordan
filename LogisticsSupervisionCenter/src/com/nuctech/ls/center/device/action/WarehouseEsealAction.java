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
import com.nuctech.ls.model.bo.warehouse.LsWarehouseEsealBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.WarehouseEsealService;
import com.nuctech.ls.model.util.OperateContentType;
import com.nuctech.ls.model.util.OperateEntityType;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.EsealDepartementVO;
import com.nuctech.util.Constant;
import com.nuctech.util.MessageResourceUtil;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 作者：赵苏阳 描述：
 * <p>
 * 子锁管理 增删改查
 * <p>
 * 创建时间2016年5月27
 *
 */
@Namespace("/esealMgmt")
public class WarehouseEsealAction extends LSBaseAction {

    private static final long serialVersionUID = 5281350780559863860L;
    protected static final String DEFAULT_SORT_COLUMNS = "e.esealNumber ASC";
    private LsSystemDepartmentBO systemDepartmentBO;

    /*
     * 子锁对象
     */
    private LsWarehouseEsealBO warehouseEsealBO;
    private String[] esealIds; // 删除的时候获取table行的Id
    private String numbers;
    List<LsWarehouseEsealBO> esealList = new ArrayList<LsWarehouseEsealBO>();
    private List<LsSystemDepartmentBO> esealBelongtoList = new ArrayList<LsSystemDepartmentBO>();
    private List<LsSystemDepartmentBO> esealEditList = new ArrayList<LsSystemDepartmentBO>();
    @Resource
    private SystemModules systemModules;
    @Resource
    private WarehouseEsealService warehouseEsealService;
    @Resource
    private SystemDepartmentService systemDepartmentService;
    @Resource
    private SystemOperateLogService logService;
    private String filePath;

    /**
     * 子锁添加
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "addEseal", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String addElock() throws Exception {
        try {
            if (warehouseEsealBO != null) {
                // 判断子锁号是否已经存在，不得重复添加
                String esealNumber = warehouseEsealBO.getEsealNumber();
                LsWarehouseEsealBO esealBO = warehouseEsealService.findByEsealNumber(esealNumber);
                if (NuctechUtil.isNotNull(esealBO)) {
                    return ERROR;
                } else {
                    SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                    warehouseEsealBO.setEsealId(generatePrimaryKey());// 设置子锁的主键，自动存储到数据库中；
                    warehouseEsealBO.setCreateTime(new Date()); // 设置子锁的创建时间。
                    warehouseEsealBO.setCreateUser(sessionUser.getUserId());// 获取子锁创建人即登陆人用户名

                    warehouseEsealBO.setBelongTo(request.getParameter("s_belongTo"));// 获取前台下拉列表框中的内容(所属节点)
                    warehouseEsealBO.setEsealStatus(request.getParameter("s_esealStatus"));// 获取前台下拉列表框中的内容(状态)
                    warehouseEsealService.add(warehouseEsealBO);

                    addLog(OperateContentType.ADD.toString(), OperateEntityType.ESEAL.toString(),
                            warehouseEsealBO.toString());
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
    @Action(value = "addModal", results = { @Result(name = "success", location = "/device/esealMgmt/esealAdd.jsp"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String addModal() {
    	SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        esealBelongtoList = systemDepartmentService.findAllPortByUserId(userId);
        //口岸用户默认口岸为当前口岸
		if(sessionUser.getRoleId().equals(RoleType.portUser.getType())){
			request.setAttribute("orgId", sessionUser.getOrganizationId());
			request.setAttribute("disabled", true);
		}   else{
			request.setAttribute("disabled", false);
		}
        
        warehouseEsealBO = new LsWarehouseEsealBO();
        return SUCCESS;
    }

    /**
     * 编辑modal
     * 
     * @return
     */
    @Action(value = "editModal", results = { @Result(name = "success", location = "/device/esealMgmt/esealEdit.jsp"),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editModal() {
        if (warehouseEsealBO != null) {
            String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
            esealEditList = systemDepartmentService.findAllPortByUserId(userId);

            String esealId = warehouseEsealBO.getEsealId();
            if (!NuctechUtil.isNull(esealId)) {
                warehouseEsealBO = this.warehouseEsealService.findById(esealId);
                systemDepartmentBO = systemDepartmentService.findById(warehouseEsealBO.getBelongTo());
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
     * 子锁修改
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "editEseal", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editEseal() throws Exception {
        if (warehouseEsealBO != null) {
            this.warehouseEsealService.modify(warehouseEsealBO);

            addLog(OperateContentType.EDIT.toString(), OperateEntityType.ESEAL.toString(), warehouseEsealBO.toString());

            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    /**
     * 子锁删除
     * 
     * @param warehouseElockBO
     * @return
     * 
     * 
     * 
     */
    @Action(value = "delEsealById", results = { @Result(name = "success", type = "json", params = { "root", "true" }) })
    public String delEsealById() {
        if (esealIds != null) {
            String s[] = esealIds[0].split(",");
            for (int i = 0; i < s.length; i++) {
                this.warehouseEsealService.deleteById(s[i]);
                addLog(OperateContentType.DELETE.toString(), OperateEntityType.ESEAL.toString(), "esealId:" + s[i]);
            }
        }

        return SUCCESS;
    }

    /**
     * 子锁表与机构表的关联查询
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

        JSONObject retJson = warehouseEsealService.fromObjectList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 前台 页面 分页 (手动选择)
     * 
     * @return
     */
    @Action(value = "dlist")
    public void dlist() throws IOException {
        /*
         * List<LsWarehouseEsealBO> esealList =
         * warehouseEsealService.findAllEseal();单表查询
         */

        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String organizationId = sessionUser.getOrganizationId();

        List<EsealDepartementVO> esealList = warehouseEsealService.getEsealDepartmentlist(organizationId);// 两张表联合查询

        JSONArray jsonArray = JSONArray.fromObject(esealList);
        PrintWriter out = response.getWriter();
        out.print(jsonArray.toString());
        out.flush();
        out.close();
    }

    @Action(value = "toList", results = { @Result(name = "success", location = "/device/esealMgmt/esealList.jsp") })
    public String toList() {
        String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        esealBelongtoList = systemDepartmentService.findAllPortByUserId(userId);
        
        //口岸用户隐藏"所属节点"过滤
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        if(sessionUser.getRoleId().equals(RoleType.portUser.getType())){
            request.setAttribute("filter", false);
        }else{
            request.setAttribute("filter", true);
        }
        return SUCCESS;
    }

    /*
     * //调度Modal调用方法
     * @Action(value = "addDispatchModal", results = {
     * @Result(name = "success", location =
     * "/device/esealMgmt/esealDispatch.jsp"),
     * @Result(name = "error", type = "json", params = { "root", "errorMessage"
     * }) }) public String addDispatchModal() {
     * request.setAttribute("arrayList", numbers); return SUCCESS; }
     */

    // 子锁调度
    @Action(value = "esealDispatch",
            results = { @Result(name = "success", location = "/device/warehouseElock/DeviceDispatch.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String elockDispatch() {
        if (esealIds != null) {
            String s[] = esealIds[0].split(",");
            for (int i = 0; i < s.length; i++) {
                warehouseEsealBO = warehouseEsealService.findById(s[i]);
                esealList.add(warehouseEsealBO);

            }
        }
        return SUCCESS;
    }

    /**
     * 根据子锁号查询记录
     * 
     * @throws IOException
     */
    @Action(value = "findByEsealNumber")
    public void findByEsealNumber() throws IOException {
        String esealNumber = request.getParameter("esealNumber");
        LsWarehouseEsealBO warehouseEsealBO = warehouseEsealService.findByEsealNumber(esealNumber);

        JSONObject json = new JSONObject();
        json.put("warehouseEsealBO", warehouseEsealBO);
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
     * 子锁重复验证
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "repeate")
    public void repeate() throws Exception {
        JSONObject jsonObject = new JSONObject();
        String esealNumber = warehouseEsealBO.getEsealNumber();
        LsWarehouseEsealBO warehouseEsealBO = warehouseEsealService.findByEsealNumber(esealNumber);
        if (NuctechUtil.isNull(warehouseEsealBO)) {
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
            fileName = "Eseal_ZTemplate.xlsx";
        } else {
            fileName = "Eseal_ETemplate.xlsx";
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
            String esealNumber = "";
            
            //不能导入重复子锁号的数据
            for (int i = 2; i <= totalRowNum; i++) {
            	  Row row = sheet.getRow(i);
                  // 获得获得第i行第0列的 String类型对象(关锁号)
                  Cell cell = row.getCell((short) 0);
                  cell.setCellType(Cell.CELL_TYPE_STRING);
                  esealNumber = cell.getStringCellValue().toString();
                  //不能导入重复子锁号的数据
                  if(!NuctechUtil.isNull(warehouseEsealService.findByEsealNumber(esealNumber))){
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
                esealNumber = cell.getStringCellValue().toString();
                
                // 向数据库中存入数据
                SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                LsWarehouseEsealBO esealBO = new LsWarehouseEsealBO();
                esealBO.setEsealId(generatePrimaryKey());
                esealBO.setCreateTime(new Date());
                esealBO.setCreateUser(sessionUser.getUserId());
                esealBO.setEsealStatus("1");// 默认添加的为正常
                esealBO.setBelongTo(sessionUser.getOrganizationId());
                esealBO.setCreateUser(sessionUser.getUserName());
                // 读取excel文件中的数据
                esealBO.setEsealNumber(esealNumber);
                warehouseEsealService.add(esealBO);
            }
            result = "true";
            return SUCCESS;
        } catch (Exception e) {
            result = "false";
            return ERROR;
        }
    }

    private File file;
    private String fileFileName;
    private String fileFileContentType;
    private String messageString;
    private JSONArray json_uploadimage_response;
    private String result;

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

    public LsWarehouseEsealBO getWarehouseEsealBO() {
        return warehouseEsealBO;
    }

    public void setWarehouseEsealBO(LsWarehouseEsealBO warehouseEsealBO) {
        this.warehouseEsealBO = warehouseEsealBO;
    }

    public WarehouseEsealService getWarehouseEsealService() {
        return warehouseEsealService;
    }

    public void setWarehouseEsealService(WarehouseEsealService warehouseEsealService) {
        this.warehouseEsealService = warehouseEsealService;
    }

    public String[] getEsealIds() {
        return esealIds;
    }

    public void setEsealIds(String[] esealIds) {
        this.esealIds = esealIds;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public List<LsWarehouseEsealBO> getEsealList() {
        return esealList;
    }

    public void setEsealList(List<LsWarehouseEsealBO> esealList) {
        this.esealList = esealList;
    }

    public List<LsSystemDepartmentBO> getEsealBelongtoList() {
        return esealBelongtoList;
    }

    public void setEsealBelongtoList(List<LsSystemDepartmentBO> esealBelongtoList) {
        this.esealBelongtoList = esealBelongtoList;
    }

    public List<LsSystemDepartmentBO> getEsealEditList() {
        return esealEditList;
    }

    public void setEsealEditList(List<LsSystemDepartmentBO> esealEditList) {
        this.esealEditList = esealEditList;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
