package com.mnt.report.controller;

import java.util.ArrayList;
import java.util.List;

public class PublicationVm {
	
	String publication;
	
	public List<ChildPublicationVm> listVm = new ArrayList<ChildPublicationVm>();
	
	public String getPublication() {
		return publication;
	}
	public void setPublication(String publication) {
		this.publication = publication;
	}
	public List<ChildPublicationVm> getListVm() {
		return listVm;
	}
	public void setListVm(List<ChildPublicationVm> listVm) {
		this.listVm = listVm;
	}
	
	

}
