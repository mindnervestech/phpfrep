package com.mnt.report.controller;

import java.util.ArrayList;
import java.util.List;

public class TaskVm {

	public Long id;
	public String name;
	public String desc;
	public String status;
	public String createdBy;
	public String createdOn;
	public String firstName;
	public String lastName;
	public String operation;
	public String dataField;
	public String alert;
	
	
	
	public List<TaskImageVm> listVm = new ArrayList<TaskImageVm>();

	public List<TaskImageVm> getListVm() {
		return listVm;
	}

	public void setListVm(List<TaskImageVm> listVm) {
		this.listVm = listVm;
	}


	
	
}
