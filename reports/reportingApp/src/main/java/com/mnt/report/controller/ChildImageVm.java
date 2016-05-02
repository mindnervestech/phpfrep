package com.mnt.report.controller;

import java.util.Date;

public class ChildImageVm {

	public Long DN_ID;
	public String DC_IMAGENAME;
	public Long DN_CREATED_BY;
	public String DC_HEIGHT;
	public String DC_WIDTH;
	public String isCompleted;
	public Long DN_PARENT_IMAGE_ID;
	public String childThumb;
	
	public String getChildThumb() {
		return childThumb;
	}
	public void setChildThumb(String childThumb) {
		this.childThumb = childThumb;
	}
	public Long getDN_ID() {
		return DN_ID;
	}
	public void setDN_ID(Long dN_ID) {
		DN_ID = dN_ID;
	}
	public String getDC_IMAGENAME() {
		return DC_IMAGENAME;
	}
	public void setDC_IMAGENAME(String dC_IMAGENAME) {
		DC_IMAGENAME = dC_IMAGENAME;
	}
	public Long getDN_CREATED_BY() {
		return DN_CREATED_BY;
	}
	public void setDN_CREATED_BY(Long dN_CREATED_BY) {
		DN_CREATED_BY = dN_CREATED_BY;
	}
	public String getDC_HEIGHT() {
		return DC_HEIGHT;
	}
	public void setDC_HEIGHT(String dC_HEIGHT) {
		DC_HEIGHT = dC_HEIGHT;
	}
	public String getDC_WIDTH() {
		return DC_WIDTH;
	}
	public void setDC_WIDTH(String dC_WIDTH) {
		DC_WIDTH = dC_WIDTH;
	}
	public String getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}
	public Long getDN_PARENT_IMAGE_ID() {
		return DN_PARENT_IMAGE_ID;
	}
	public void setDN_PARENT_IMAGE_ID(Long dN_PARENT_IMAGE_ID) {
		DN_PARENT_IMAGE_ID = dN_PARENT_IMAGE_ID;
	}
	
}
