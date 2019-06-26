package com.test.entity;

import java.io.Serializable;

/**
 * 
* 类名称：用户
* 类描述： 
* @author FH 
* 作者单位： 
* 联系方式：
* 修改时间：2018年6月28日
* @version 1.0
 */
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5265752198983986689L;
	private String USER_ID;		//用户id
	private String USERNAME;	//用户名
	private String PASSWORD; 	//密码
	private String NAME;		//姓名
	private String RIGHTS;		//权限
	private String ROLE_ID;		//角色id
	private String ROLE_IDS;	//副职角色id
	private String LAST_LOGIN;	//最后登录时间
	private String IP;			//用户登录ip地址
	private String STATUS;		//状态
	private Role role;			//角色对象
	private Page page;			//分页对象
	private String SKIN;		//皮肤
	private String ROLE_NAME;	//名称
	private String companyID;   //公司ID
	private String ddUserId;   //钉钉ID
	private String warehouseNum;   //仓库编号（1、总厂，2、实木厂，3、沙发厂，4、床垫厂，5、呆滞厂）
	private String productionLine;   //所属产线（1、门板线，2、标准线，3、柜体线，4、沙发厂，5、床垫厂）
	private String lyWarehouseNum;   //针对领用仓库编号（1、总厂，2、实木厂，3、沙发厂，4、床垫厂，5、呆滞厂）
	private String PHONE;//电话号码

	public String getPHONE() {
		return PHONE;
	}

	public void setPHONE(String PHONE) {
		this.PHONE = PHONE;
	}

	public String getLyWarehouseNum() {
		return lyWarehouseNum;
	}

	public void setLyWarehouseNum(String lyWarehouseNum) {
		this.lyWarehouseNum = lyWarehouseNum;
	}

	public String getProductionLine() {
		return productionLine;
	}

	public void setProductionLine(String productionLine) {
		this.productionLine = productionLine;
	}

	public String getWarehouseNum() {
		return warehouseNum;
	}

	public void setWarehouseNum(String warehouseNum) {
		this.warehouseNum = warehouseNum;
	}

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	public String getROLE_NAME() {
		return ROLE_NAME;
	}

	public void setROLE_NAME(String ROLE_NAME) {
		this.ROLE_NAME = ROLE_NAME;
	}

	public String getSKIN() {
		return SKIN;
	}
	public void setSKIN(String sKIN) {
		SKIN = sKIN;
	}
	
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getUSERNAME() {
		return USERNAME;
	}
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}
	public String getPASSWORD() {
		return PASSWORD;
	}
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getRIGHTS() {
		return RIGHTS;
	}
	public void setRIGHTS(String rIGHTS) {
		RIGHTS = rIGHTS;
	}
	public String getROLE_ID() {
		return ROLE_ID;
	}
	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}
	public String getROLE_IDS() {
		return ROLE_IDS;
	}
	public void setROLE_IDS(String rOLE_IDS) {
		ROLE_IDS = rOLE_IDS;
	}
	public String getLAST_LOGIN() {
		return LAST_LOGIN;
	}
	public void setLAST_LOGIN(String lAST_LOGIN) {
		LAST_LOGIN = lAST_LOGIN;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Page getPage() {
		if(page==null)
			page = new Page();
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}

	public String getDdUserId() {
		return ddUserId;
	}

	public void setDdUserId(String ddUserId) {
		this.ddUserId = ddUserId;
	}
	
}
