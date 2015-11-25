package com.obs.brs.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 
 * @author Jeevanantham
 *
 */
@Entity
@Table(name="tbl_parent_image")
public class ParentImage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String imageName;
	private int status;
	private User createdBy;
	private User deletedBy;
	private Date createdOn;
	private Date deletedOn;
	private Publication publicationTitle;
	private Publication section;
	private String page;
	private Date issueDate;
	private String sectionspecialRegional;
	private String sectionspecialTopic;
	private String sectionother;
	private String imageHeight;
	private String imageWidth;
	private String memo;

	@Transient
	public List<ChildImage> childImageList = new ArrayList<>();
	
	@Transient
	public Boolean ableToDone = true;
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="DN_ID" , nullable = false )
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	@Column(name="DC_IMAGENAME", nullable = true)
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	/**
	 * @return the created_by
	 */
	@OneToOne ( targetEntity = User.class, fetch = FetchType.EAGER )
	@JoinColumn(name="DN_CREATED_BY", nullable = true)
	public User getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param created_by the created_by to set
	 */
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the deleted_by
	 */
	//@OneToOne ( targetEntity = User.class, fetch = FetchType.EAGER )
	@Column(name="DN_DELETED_BY", nullable = true)
	public User getDeletedBy() {
		return deletedBy;
	}

	/**
	 * @param deleted_by the deleted_by to set
	 */
	public void setDeletedBy(User deletedBy) {
		this.deletedBy = deletedBy;
	}

	/**
	 * @return the created_On
	 */
	@Column(name="DD_CREATED_ON", nullable = true)
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param created_On the created_On to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the deleted_On
	 */
	@Column(name="DD_DELETED_ON", nullable = true)
	public Date getDeletedOn() {
		return deletedOn;
	}

	/**
	 * @param deleted_On the deleted_On to set
	 */
	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}
	/**
	 * @return the status
	 */
	@Column(name="DN_STATUS", nullable = true)
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * @return the publicationTitle
	 */
	@OneToOne ( targetEntity = Publication.class, fetch = FetchType.EAGER )
	@JoinColumn(name="DC_PUBLICATION_TITLE", nullable = true)
	public Publication getPublicationTitle() {
		return publicationTitle;
	}

	/**
	 * @param publicationTitle the publicationTitle to set
	 */
	public void setPublicationTitle(Publication publicationTitle) {
		this.publicationTitle = publicationTitle;
	}

	/**
	 * @return the section
	 */
	@OneToOne ( targetEntity = Publication.class, fetch = FetchType.EAGER )
	@JoinColumn(name="DC_SECTION", nullable = true)
	public Publication getSection() {
		return section;
	}

	/**
	 * @param section the section to set
	 */
	public void setSection(Publication section) {
		this.section = section;
	}
	/**
	 * @return the page
	 */
	@Column(name="DC_PAGE", nullable = true)
	public String getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(String page) {
		this.page = page;
	}
	/**
	 * @return the issueDate
	 */
	@Column(name="DD_ISSUE_DATE", nullable = true)
	public Date getIssueDate() {
		return issueDate;
	}

	/**
	 * @param issueDate the issueDate to set
	 */
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	/**
	 * 
	 * @return sectionspecialRegional
	 */
	@Column(name="DC_SECTION_SPECIAL_REGIONAL", nullable = true)
	public String getSectionspecialRegional() {
		return sectionspecialRegional;
	}
	/**
	 * 
	 * @param sectionspecialRegional
	 */
	public void setSectionspecialRegional(String sectionspecialRegional) {
		this.sectionspecialRegional = sectionspecialRegional;
	}
	/**
	 * 
	 * @return sectionspecialTopic
	 */
	@Column(name="DC_SECTION_SPECIAL_TOPIC", nullable = true)
	public String getSectionspecialTopic() {
		return sectionspecialTopic;
	}
	/**
	 * 
	 * @param sectionspecialTopic
	 */
	public void setSectionspecialTopic(String sectionspecialTopic) {
		this.sectionspecialTopic = sectionspecialTopic;
	}
	/**
	 * 
	 * @return sectionother
	 */
	@Column(name="DC_SECTION_OTHER", nullable = true)
	public String getSectionother() {
		return sectionother;
	}
	/**
	 * 
	 * @param sectionother
	 */
	public void setSectionother(String sectionother) {
		this.sectionother = sectionother;
	}
	
	/**
	 * 
	 * @return
	 */
	@Column(name="DC_HEIGHT", nullable = true)
	public String getImageHeight() {
		return imageHeight;
	}
	/**
	 * 
	 * @param imageHeight
	 */
	public void setImageHeight(String imageHeight) {
		this.imageHeight = imageHeight;
	}
	/**
	 * 
	 * @return
	 */
	@Column(name="DC_WIDTH", nullable = true)
	public String getImageWidth() {
		return imageWidth;
	}
	/**
	 * 
	 * @param imageWidth
	 */
	public void setImageWidth(String imageWidth) {
		this.imageWidth = imageWidth;
	}
	
	@Transient
	public List<ChildImage> getChildImageList() {
		return childImageList;
	}
	
	@Transient
	public Boolean getAbleToDone() {
		return this.ableToDone;
		
	}
	@Transient
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
