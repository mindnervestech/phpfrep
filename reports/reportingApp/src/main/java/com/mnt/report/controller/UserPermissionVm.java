package com.mnt.report.controller;


public class UserPermissionVm {

	public Long userTypeId;
	public String tabName;
	public Long tabId;
	public boolean status;
	
	
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Long getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(Long userTypeId) {
		this.userTypeId = userTypeId;
	}
	
	public Long getTabId() {
		return tabId;
	}
	public void setTabId(Long tabId) {
		this.tabId = tabId;
	}
	
	

}