package com.mnt.report.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mnt.report.service.FractsController.CroppedVM;

public class ImagesVM {

	public Long DN_ID;
	public String DC_IMAGENAME;
	public Integer DN_STATUS;
	public Long DN_CREATED_BY;
	public String CREATED_BY;
	public String DC_PAGE;
	public String DC_PUBLICATION_TITLE;
	public String DC_SECTION;
	public String DC_SECTION_OTHER;
	public String DC_SECTION_SPECIAL_REGIONAL;
	public String DC_SECTION_SPECIAL_TOPIC;
	public String DC_HEIGHT;
	public String DC_WIDTH;
	public Date DD_ISSUE_DATE;
	public Date DD_CREATED_ON;
	public String imageUrl;
	public boolean duplicate;
	public String dateissue;
	public String createDate;
	
	public String thumb;
	public String thumbChild;
	
	
	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public String getThumbChild() {
		return thumbChild;
	}


	public void setThumbChild(String thumbChild) {
		this.thumbChild = thumbChild;
	}


	public String getThumb() {
		return thumb;
	}


	public void setThumb(String thumb) {
		this.thumb = thumb;
	}


	public Long DN_PARENT_IMAGE_ID;
	
	public List<ChildImageVm> listVm = new ArrayList<ChildImageVm>();
	
	
	public String getDateissue() {
		return dateissue;
	}


	public void setDateissue(String dateissue) {
		this.dateissue = dateissue;
	}


	public boolean isDuplicate() {
		return duplicate;
	}


	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public Date getDD_CREATED_ON() {
		return DD_CREATED_ON;
	}


	public void setDD_CREATED_ON(Date dD_CREATED_ON) {
		DD_CREATED_ON = dD_CREATED_ON;
	}


	public String getCREATED_BY() {
		return CREATED_BY;
	}


	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}


	public List<ChildImageVm> getListVm() {
		return listVm;
	}


	public void setListVm(List<ChildImageVm> listVm) {
		this.listVm = listVm;
	}


	public Date getDD_ISSUE_DATE() {
		return DD_ISSUE_DATE;
	}


	public void setDD_ISSUE_DATE(Date dD_ISSUE_DATE) {
		DD_ISSUE_DATE = dD_ISSUE_DATE;
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


	public Integer getDN_STATUS() {
		return DN_STATUS;
	}


	public void setDN_STATUS(Integer dN_STATUS) {
		DN_STATUS = dN_STATUS;
	}


	public Long getDN_CREATED_BY() {
		return DN_CREATED_BY;
	}


	public void setDN_CREATED_BY(Long dN_CREATED_BY) {
		DN_CREATED_BY = dN_CREATED_BY;
	}


	public String getDC_PAGE() {
		return DC_PAGE;
	}


	public void setDC_PAGE(String dC_PAGE) {
		DC_PAGE = dC_PAGE;
	}


	public String getDC_PUBLICATION_TITLE() {
		return DC_PUBLICATION_TITLE;
	}


	public void setDC_PUBLICATION_TITLE(String dC_PUBLICATION_TITLE) {
		DC_PUBLICATION_TITLE = dC_PUBLICATION_TITLE;
	}


	public String getDC_SECTION() {
		return DC_SECTION;
	}


	public void setDC_SECTION(String dC_SECTION) {
		DC_SECTION = dC_SECTION;
	}


	public String getDC_SECTION_OTHER() {
		return DC_SECTION_OTHER;
	}


	public void setDC_SECTION_OTHER(String dC_SECTION_OTHER) {
		DC_SECTION_OTHER = dC_SECTION_OTHER;
	}


	public String getDC_SECTION_SPECIAL_REGIONAL() {
		return DC_SECTION_SPECIAL_REGIONAL;
	}


	public void setDC_SECTION_SPECIAL_REGIONAL(String dC_SECTION_SPECIAL_REGIONAL) {
		DC_SECTION_SPECIAL_REGIONAL = dC_SECTION_SPECIAL_REGIONAL;
	}


	public String getDC_SECTION_SPECIAL_TOPIC() {
		return DC_SECTION_SPECIAL_TOPIC;
	}


	public void setDC_SECTION_SPECIAL_TOPIC(String dC_SECTION_SPECIAL_TOPIC) {
		DC_SECTION_SPECIAL_TOPIC = dC_SECTION_SPECIAL_TOPIC;
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


	public Long getDN_PARENT_IMAGE_ID() {
		return DN_PARENT_IMAGE_ID;
	}


	public void setDN_PARENT_IMAGE_ID(Long dN_PARENT_IMAGE_ID) {
		DN_PARENT_IMAGE_ID = dN_PARENT_IMAGE_ID;
	}


	public List<CroppedVM> getRevelanceList() {
		return revelanceList;
	}


	public void setRevelanceList(List<CroppedVM> revelanceList) {
		this.revelanceList = revelanceList;
	}


	public List<CroppedVM> revelanceList;
}
