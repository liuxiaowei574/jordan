package com.nuctech.ls.model.vo.ztree;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： 徐楠
 *
 * 描述：<p>树形结构对象</p>
 * 创建时间：2016年3月17日
 */
public class TreeNode implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 996071717692225315L;
	/**
     * 唯一标识
     */
    private String id;
    /**
     * 父节点唯一标识
     */
    private String pId;
    /**
     * 节点展示名称
     */
    private String name;
    /**
     * 鼠标划上节点时显示内容
     */
    private String title;
    /**
     * css 内容：<br />
     * 1. 展开和折叠使用相同的样式 <br />
     * .ztree li span.button.diy01_ico_open, .ztree li span.button.diy01_ico_close{...} <br />
     * 2. 展开和折叠使用不同的样式 <br />
     * .ztree li span.button.diy02_ico_open{...} <br />
     * .ztree li span.button.diy02_ico_close{...} <br />
     * 3. 叶子结点的个性化图标样式 <br />
     * .ztree li span.button.diy03_ico_docu{...} <br />
     * js中节点数据如下所示： <br />
     * var nodes = [ <br />
     * //父节点展开 折叠时使用相同的图标 <br />
     * { name:"父节点1", iconSkin:"diy01"}, <br />
     * 
     * //父节点展开 折叠时分别使用不同的图标 <br />
     * { name:"父节点2", iconSkin:"diy02"}, <br />
     * 
     * //叶子节点个性化图标 <br />
     * { name:"叶子节点", iconSkin:"diy03"} <br />
     * ] <br />
     */
    private String iconSkin;
    /**
     * 是否显示为父结点
     */
    private boolean isParent;
    /**
     * 是否初始展开此结点
     */
    private boolean open;
    /**
     * 是否初始展开此结点
     */
    private boolean nocheck;
   /**
    * 初始化的数据设置 是否勾选状态
    */
    private boolean checked ;
    /**
     * 附加属性
     */
    private Map<String, Object> additionalMap = new HashMap<String, Object>();

    /**
     * 返回id
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id
     *        the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 返回pId
     *
     * @return the pId
     */
    public String getpId() {
        return pId;
    }

    /**
	 * 返回checked
	 *
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * 设置checked
	 *
	 * @param checked 
	 *		  the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
     * 设置pId
     *
     * @param pId
     *        the pId to set
     */
    public void setpId(String pId) {
        this.pId = pId;
    }

    /**
     * 返回name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name
     *
     * @param name
     *        the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 返回iconSkin
     *
     * @return the iconSkin
     */
    public String getIconSkin() {
        return iconSkin;
    }

    /**
     * 设置iconSkin
     *
     * @param iconSkin
     *        the iconSkin to set
     */
    public void setIconSkin(String iconSkin) {
        this.iconSkin = iconSkin;
    }

    /**
     * 返回isParent
     *
     * @return the isParent
     */
    public boolean getIsParent() {
        return isParent;
    }

    /**
     * 设置isParent
     *
     * @param isParent
     *        the isParent to set
     */
    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    /**
     * 返回open
     *
     * @return the open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * 设置open
     *
     * @param open
     *        the open to set
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * 返回additionalMap
     *
     * @return the additionalMap
     */
    public Map<String, Object> getAdditionalMap() {
        return additionalMap;
    }

    /**
     * 设置additionalMap
     *
     * @param additionalMap
     *        the additionalMap to set
     */
    public void setAdditionalMap(Map<String, Object> additionalMap) {
        this.additionalMap = additionalMap;
    }

    /**
     * 返回title
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置title
     *
     * @param title
     *        the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 返回nocheck
     *
     * @return the nocheck
     */
    public boolean getNocheck() {
        return nocheck;
    }

    /**
     * 设置nocheck
     *
     * @param nocheck
     *        the nocheck to set
     */
    public void setNocheck(boolean nocheck) {
        this.nocheck = nocheck;
    }
	
}
