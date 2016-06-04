package com.mnt.report.controller;

import java.util.ArrayList;
import java.util.List;

public class ChildPublicationVm {

	public Long DN_ID;
	
	public Long DN_PARENT_IMAGE_ID;
	
	public String DC_IMAGENAME;
	
	public int imageStatus;
	
	public String color;
	
	public String childThumb;
	
	
	public String getChildThumb() {
		return childThumb;
	}

	public void setChildThumb(String childThumb) {
		this.childThumb = childThumb;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String status;
	
	

	public int getImageStatus() {
		return imageStatus;
	}

	public void setImageStatus(int imageStatus) {
		this.imageStatus = imageStatus;
	}

	public Long getDN_ID() {
		return DN_ID;
	}

	public void setDN_ID(Long dN_ID) {
		DN_ID = dN_ID;
	}

	public Long getDN_PARENT_IMAGE_ID() {
		return DN_PARENT_IMAGE_ID;
	}

	public void setDN_PARENT_IMAGE_ID(Long dN_PARENT_IMAGE_ID) {
		DN_PARENT_IMAGE_ID = dN_PARENT_IMAGE_ID;
	}

	public String getDC_IMAGENAME() {
		return DC_IMAGENAME;
	}

	public void setDC_IMAGENAME(String dC_IMAGENAME) {
		DC_IMAGENAME = dC_IMAGENAME;
	}
	

}
