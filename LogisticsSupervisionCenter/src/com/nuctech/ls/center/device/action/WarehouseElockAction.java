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
import org.apache.struts2.json.JSONException;
import org.hibernate.SessionFactory;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.WarehouseElockService;
import com.nuctech.ls.model.util.OperateContentType;
import com.nuctech.ls.model.util.OperateEntityType;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.ElockDepartmentVO;
import com.nuctech.util.Constant;
import com.nuctech.util.MessageResourceUtil;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 作者：赵苏阳 描述：
 * <p>
 * 关锁管理 增删改查
 * <p>
 * 创建时间2016年5月19
 *
 */
@Namespace("/warehouseElock")
public class WarehouseElockAction extends LSBaseAction {

    private static final long serialVersionUID = 1l;
    protected static final String DEFAULT_SORT_COLUMNS = "e.esealNumber ASC";
    protected static final String SORT_COLUMNS = "d.organizationName ASC";
    private LsWarehouseElockBO warehouseElockBO;
    private LsSystemDepartmentBO systemDepartmentBO;

    private String[] elockIds; // 删除的时候获取table行的Id
    private String numbers;
    private List<String> arrayList = new ArrayList<String>();
    private List<LsSystemDepartmentBO> deptList = new ArrayList<LsSystemDepartmentBO>();
    private List<LsSystemDepartmentBO> deptEditList = new ArrayList<LsSystemDepartmentBO>();
    private List randomList = new ArrayList();
    @Resource
    private SystemModules systemModules;
    @Resource
    private WarehouseElockService warehouseElockService;
    @Resource
    private SystemDepartmentService systemDepartmentService;
    @Resource
    protected SessionFactory sessionFactory;

    List<LsWarehouseElockBO> list = new ArrayList<LsWarehouseElockBO>();
    private JSONArray retJson;
    @Resource
    private SystemOperateLogService logService;
    private String filePath;

    /**
     * 关锁添加
     * 
     * @param warehouseElockBO
     * @return
     */
    @Action(value = "addElock", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String addElock() throws Exception {
        try {
            if (warehouseElockBO != null) {
                // 判断关锁号是否已经存在，不得重复添加

                String elockNumber = warehouseElockBO.getElockNumber();
                LsWarehouseElockBO elockBO = warehouseElockService.findByElockNumber(elockNumber);
                if (NuctechUtil.isNotNull(elockBO)) {
                    return ERROR;
                } else {

                    SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                    warehouseElockBO.setElockId(generatePrimaryKey());// 设置关锁的主键，自动存储到数据库中；
                    warehouseElockBO.setCreateTime(new Date()); // 设置关锁创建的时间。
                    warehouseElockBO.setCreateUser(sessionUser.getUserId());// 设置创建人

                    warehouseElockBO.setBelongTo(request.getParameter("s_belongTo"));// 获取前台下拉列表框中的内容
                    warehouseElockBO.setElockStatus(request.getParameter("s_elockStatus"));
                    warehouseElockService.add(warehouseElockBO);

                    addLog(OperateContentType.ADD.toString(), OperateEntityType.ELOCK.toString(),
                            warehouseElockBO.toString());
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

    // 添加Modal调用方法
    @Action(value = "addModal", results = { @Result(name = "success", location = "/device/warehouseElock/elockAdd.jsp"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String addModal() {
    	SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        // 所属节点，获取所有口岸，前台循环显示
        String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        deptList = systemDepartmentService.findAllPortByUserId(userId);
        //口岸用户默认口岸为当前口岸
		if(sessionUser.getRoleId().equals(RoleType.portUser.getType())){
			request.setAttribute("orgId", sessionUser.getOrganizationId());
			request.setAttribute("disabled", true);
		}   else{
			request.setAttribute("disabled", false);
		}
        return SUCCESS;
    }

    // 编辑modal
    @Action(value = "editModal",
            results = { @Result(name = "success", location = "/device/warehouseElock/elockEdit.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editModal() {

        if (warehouseElockBO != null) {

            String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
            deptEditList = systemDepartmentService.findAllPortByUserId(userId);
            String elockId = warehouseElockBO.getElockId();
            if (!NuctechUtil.isNull(elockId)) {
                warehouseElockBO = this.warehouseElockService.findById(elockId);
                systemDepartmentBO = systemDepartmentService.findById(warehouseElockBO.getBelongTo());
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
     * 关锁修改
     * 
     * @param warehouseElockBO
     * @return
     */
    @Action(value = "editElock", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editElock() throws Exception {
        if (warehouseElockBO != null) {
            // 关锁号修改之后不能与数据库中重复(无需判断——添加时已经判断，数据库中的关锁号肯定是惟一的，且关锁号为只读)

            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            warehouseElockBO.setUpdateUser(sessionUser.getUserId());
            warehouseElockBO.setUpdateTime(new Date());
            this.warehouseElockService.modify(warehouseElockBO);

            addLog(OperateContentType.EDIT.toString(), OperateEntityType.ELOCK.toString(), warehouseElockBO.toString());

            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    /**
     * 关锁删除
     * 
     * @param warehouseElockBO
     * @return
     */
    @Action(value = "delwarehouseById",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }) })
    public String delwarehouseById() {
        if (elockIds != null) {
            String s[] = elockIds[0].split(",");
            for (int i = 0; i < s.length; i++) {
                warehouseElockService.deleteById(s[i]);
                addLog(OperateContentType.DELETE.toString(), OperateEntityType.ELOCK.toString(), "elockId:" + s[i]);
            }
        }
        return SUCCESS;
    }

    /**
     * 调度 页面前台分页 (手动选择)
     * 
     * @throws IOException
     */
    @Action(value = "dlist", results = { @Result(name = "success", type = "json") })
    public void dlist() throws IOException {
        // 过滤：只选出登录用户所在的口岸的关锁 ；(登录用户的机构代码与关锁的所属机构是一样的)且关锁状态为"正常"
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String organizationId = sessionUser.getOrganizationId();
        List<ElockDepartmentVO> elockList = warehouseElockService.getElockDapatmentlist(organizationId);

        /*
         * List<ElockDepartmentVO> elockList =
         * warehouseElockService.getElockDapatmentlist();
         */

        JSONArray retJson = JSONArray.fromObject(elockList);
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 关锁表与机构组织表的关联查询
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);

        String sortname = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        if ("organizationName".equals(sortname)) {
            pageQuery = this.newPageQuery("d.organizationName asc");
        } else if ("timeNotInUse".equals(sortname)) {
            pageQuery.setSortColumns("e.lastUseTime" + " " + sortOrder);
        }

        JSONObject retJson = warehouseElockService.fromObjectList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    // 给下拉列表中动态赋值
    @Action(value = "toList",
            results = { @Result(name = "success", location = "/device/warehouseElock/elockList.jsp") })
    public String toList() {
        String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        deptList = systemDepartmentService.findAllPortByUserId(userId);

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
     * 
     * 关锁调度报告
     * 
     * @return
     * 
     * 
     */
    @Action(value = "toReport",
            results = { @Result(name = "success", location = "/device/warehouseElock/report/list.jsp") })
    public String toReport() {
        String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        deptList = systemDepartmentService.findAllPortByUserId(userId);

        return SUCCESS;
    }

    /**
     * 关锁调度报告:关锁表与机构组织表的关联查询
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "report")
    public void report() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        if ("organizationName".equals(request.getParameter("sort"))) {
            pageQuery = this.newPageQuery(SORT_COLUMNS);
        }

        String sortname = request.getParameter("sort");
        if ("applcationPortName".equals(sortname)) {
            pageQuery = this.newPageQuery("ea.applcationPortName asc");
        }
        if ("applyTime".equals(sortname)) {
            pageQuery = this.newPageQuery("ea.applyTime asc");
        }
        if ("recviceTime".equals(sortname)) {
            pageQuery = this.newPageQuery("dp.recviceTime asc");
        }
        if ("organizationShort".equals(sortname)) {
            pageQuery = this.newPageQuery("d.organizationShort asc");
        }
        JSONObject retJson = warehouseElockService.elockReport(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 关锁调度报告:详细页面
     * 
     * @throws IOException
     * @throws JSONException
     */
    @Action(value = "detail",
            results = { @Result(name = "success", location = "/device/warehouseElock/report/detail.jsp") })
    public String detail() throws Exception {
        String elockId = request.getParameter("id");
        warehouseElockBO = warehouseElockService.findById(elockId);
        return SUCCESS;

    }

    /**
     * 根据关锁号查询记录
     * 
     * @throws IOException
     */
    @Action(value = "findByElockNumber")
    public void findByElockNumber() throws IOException {
        String elockNumber = request.getParameter("elockNumber");
        LsWarehouseElockBO warehouseElockBO = warehouseElockService.findByElockNumber(elockNumber);

        JSONObject json = new JSONObject();
        json.put("warehouseElockBO", warehouseElockBO);
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

    /*
     * //随机查询
     * @Action(value = "RandomChoose") public void RandomChoose() throws
     * IOException { String n = request.getParameter("enumber");//取到前台文本框中输入
     * 的数量； int m = Integer.parseInt(n); randomList =
     * warehouseElockService.getELock(m); JSONArray jsonArray =
     * JSONArray.fromObject(randomList); response.setCharacterEncoding("UTF-8");
     * response.setContentType("text/html"); PrintWriter out =
     * response.getWriter(); out.print(jsonArray.toString()); out.flush();
     * out.close(); }
     */

    /*
     * //随机查询(跳转location)
     * @Action(value = "RandomChoose", results = { @Result(name = "success",
     * type = "json")}) public String RandomChoose() { String n =
     * request.getParameter("enumber");//取到前台文本框中输入 的数量； int m =
     * Integer.parseInt(n); randomList = warehouseElockService.getELock(m);
     * return SUCCESS; }
     */

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
     * 关锁号重复验证
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "repeate")
    public void repeate() throws Exception {
        JSONObject jsonObject = new JSONObject();
        String elockNumber = warehouseElockBO.getElockNumber();
        LsWarehouseElockBO warehouseElockBO = warehouseElockService.findByElockNumber(elockNumber);
        if (NuctechUtil.isNull(warehouseElockBO)) {
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
            fileName = "Elock_ZTemplate.xlsx";
        } else {
            fileName = "Elock_ETemplate.xlsx";
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
            String elockNumber = "";
            String simCard = "";
            String interval = "";
            String gatewayAddress = "";
            
            //不能导入重复关锁号的数据
            for (int i = 2; i <= totalRowNum; i++) {
            	Row row = sheet.getRow(i);
            	Cell cell = row.getCell((short) 0);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                elockNumber = cell.getStringCellValue().toString();
	        	 if(!NuctechUtil.isNull(warehouseElockService.findByElockNumber(elockNumber))){
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
                elockNumber = cell.getStringCellValue().toString();
                // 获得获得第i行第1列的 String类型对象(sim卡号)
                Cell cell1 = row.getCell((short) 1);
                cell1.setCellType(Cell.CELL_TYPE_STRING);
                simCard = cell1.getStringCellValue().toString();
                // 获得获得第i行第2列的 String类型对象(信息上传频率)
                Cell cell2 = row.getCell((short) 2);
                cell2.setCellType(Cell.CELL_TYPE_STRING);
                interval = cell2.getStringCellValue().toString();
                // 获得获得第i行第2列的 String类型对象(网关地址)
                Cell cell3 = row.getCell((short) 3);
                cell3.setCellType(Cell.CELL_TYPE_STRING);
                gatewayAddress = cell3.getStringCellValue().toString();
                
                // 向数据库中存入数据
                SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                LsWarehouseElockBO warehouseElockBO = new LsWarehouseElockBO();
                warehouseElockBO.setElockId(generatePrimaryKey());
                warehouseElockBO.setCreateTime(new Date());
                warehouseElockBO.setCreateUser(sessionUser.getUserId());
                warehouseElockBO.setElockStatus("1");// 默认添加的为正常
                warehouseElockBO.setBelongTo(sessionUser.getOrganizationId());
                warehouseElockBO.setCreateUser(sessionUser.getUserName());
                // 读取excel文件中的数据
                warehouseElockBO.setElockNumber(elockNumber);
                warehouseElockBO.setSimCard(simCard);
                warehouseElockBO.setInterval(interval);
                warehouseElockBO.setGatewayAddress(gatewayAddress);
                warehouseElockService.add(warehouseElockBO);
            }
            result = "true";
            return SUCCESS;
        } catch (Exception e) {
            result = "false";
            return ERROR;
        }
    }

    public LsWarehouseElockBO getWarehouseElockBO() {
        return warehouseElockBO;
    }

    public void setWarehouseElockBO(LsWarehouseElockBO warehouseElockBO) {
        this.warehouseElockBO = warehouseElockBO;
    }

    public String[] getElockIds() {
        return elockIds;
    }

    public void setElockIds(String[] elockIds) {
        this.elockIds = elockIds;
    }

    public WarehouseElockService getWarehouseElockService() {
        return warehouseElockService;
    }

    public void setWarehouseElockService(WarehouseElockService warehouseElockService) {
        this.warehouseElockService = warehouseElockService;
    }

    public List<LsWarehouseElockBO> getList() {
        return list;
    }

    public void setList(List<LsWarehouseElockBO> list) {
        this.list = list;
    }

    public List<String> getArrayList() {
        return arrayList;
    }

    public void setArrayList(List<String> arrayList) {
        this.arrayList = arrayList;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public List<LsSystemDepartmentBO> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<LsSystemDepartmentBO> deptList) {
        this.deptList = deptList;
    }

    public LsSystemDepartmentBO getSystemDepartmentBO() {
        return systemDepartmentBO;
    }

    public void setSystemDepartmentBO(LsSystemDepartmentBO systemDepartmentBO) {
        this.systemDepartmentBO = systemDepartmentBO;
    }

    public List getRandomList() {
        return randomList;
    }

    public void setRandomList(List randomList) {
        this.randomList = randomList;
    }

    public List<LsSystemDepartmentBO> getDeptEditList() {
        return deptEditList;
    }

    public void setDeptEditList(List<LsSystemDepartmentBO> deptEditList) {
        this.deptEditList = deptEditList;
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
}
