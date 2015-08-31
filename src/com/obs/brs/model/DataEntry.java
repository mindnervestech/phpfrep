package com.obs.brs.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author Jeevanantham
 *
 */
@Entity
@Table(name="tbl_de_data")
public class DataEntry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String adHeadLine;
	private ChildImage childImage;
	private String jobListing;
	private String advertiserType; 
	private String landingPageURL;
	private String adType; 
	private String adOrientation;
	private String adSize;
	private String startCurrencyRange;
	private String endCurrencyRange;
	private short currency;
	private String others;
	private String ocrText;
	private Boolean isqualityCheck;
	private Boolean isDeleted;
	private User created_by;
	private User deleted_by;
	private Date created_On;
	private Date deleted_On;
	private ParentImage parentImage;
	private DeCompany deCompany;
	private DeJob deJobid;
	private int isApproved;
	private String searchValueAdvertisertype;
	private String adCategory;
	private String length;
	private String width;
	private String jobDensity;
	private String otherAdvertisertype;
	private String addColumn;
	private String contactInfo;
	

	/**
	 * Get de_data Id
	 * 
	 * @return int - de_data Id
	 */
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="DN_ID" , nullable = false )
	public long getId() {
		return id;
	}

	/**
	 * Set User Id
	 * 
	 * @param int - User Id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the childImage
	 */
	@OneToOne ( targetEntity = ChildImage.class, fetch = FetchType.EAGER )
	@JoinColumn (name = "DN_CHILD_IMAGE_ID")
	public ChildImage getChildImage() {
		return childImage;
	}
	/**
	 * @param childImage the childImage to set
	 */
	public void setChildImage(ChildImage childImage) {
		this.childImage = childImage;
	}
	/**
	 * @return the isActive
	 */
	@Column(name="DB_QUALITY_CHECK", nullable = true)
	public Boolean getIsqualityCheck() {
		return isqualityCheck;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsqualityCheck(Boolean isqualityCheck) {
		this.isqualityCheck = isqualityCheck;
	}

	/**
	 * @return the isDeleted
	 */
	@Column(name="DB_DELETED", nullable = true)
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	/**
	 * @return the created_by
	 */
	@OneToOne ( targetEntity = User.class, fetch = FetchType.EAGER )
	@JoinColumn(name="DN_CREATED_BY", nullable = true)
	public User getCreated_by() {
		return created_by;
	}

	
	/**
	 * @param created_by the created_by to set
	 */
	public void setCreated_by(User created_by) {
		this.created_by = created_by;
	}

	/**
	 * @return the adHeadLine 
	 * 
	 */
	@Column(name="DC_AD_HEADLINE", nullable = true)
	public String getAdHeadLine() {
		return adHeadLine;
	}
	/**
	 * @param adHeadLine the adHeadLine to set
	 */
	public void setAdHeadLine(String adHeadLine) {
		this.adHeadLine = adHeadLine;
	}

	/**
	 * @return the deleted_by
	 */
	@OneToOne ( targetEntity = User.class, fetch = FetchType.EAGER )
	@JoinColumn(name="DN_DELETED_BY", nullable = true)
	public User getDeleted_by() {
		return deleted_by;
	}

	/**
	 * @param deleted_by the deleted_by to set
	 */
	public void setDeleted_by(User deleted_by) {
		this.deleted_by = deleted_by;
	}

	/**
	 * @return the created_On
	 */
	@Column(name="DD_CREATED_ON", nullable = true)
	public Date getCreated_On() {
		return created_On;
	}

	/**
	 * @param created_On the created_On to set
	 */
	public void setCreated_On(Date created_On) {
		this.created_On = created_On;
	}

	/**
	 * @return the deleted_On
	 */
	@Column(name="DD_DELETED_ON", nullable = true)
	public Date getDeleted_On() {
		return deleted_On;
	}

	/**
	 * @param deleted_On the deleted_On to set
	 */
	public void setDeleted_On(Date deleted_On) {
		this.deleted_On = deleted_On;
	}
	/**
	 * @return the jobListing
	 */
	@Column(name="DC_JOBS_LISTING", nullable = true)
	public String getJobListing() {
		return jobListing;
	}
	/**
	 * @param jobListing the jobListing to set
	 */
	public void setJobListing(String jobListing) {
		this.jobListing = jobListing;
	}
	/**
	 * @return the advertiserType
	 */
	@Column(name="DC_ADVERTISER_TYPE", nullable = true)
	public String getAdvertiserType() {
		return advertiserType;
	}
	/**
	 * @param advertiserType the advertiserType to set
	 */
	public void setAdvertiserType(String advertiserType) {
		this.advertiserType = advertiserType;
	}
	
	/**
	 * @return the landingPageURL
	 */
	@Column(name="DC_PAGE_URL", nullable = true)
	public String getLandingPageURL() {
		return landingPageURL;
	}
	/**
	 * @param landingPageURL the landingPageURL to set
	 */
	public void setLandingPageURL(String landingPageURL) {
		this.landingPageURL = landingPageURL;
	}

	/**
	 * @return the adType
	 */
	@Column(name="DC_AD_TYPE", nullable = true)
	public String getAdType() {
		return adType;
	}
	/**
	 * @param adType the adType to set
	 */
	public void setAdType(String adType) {
		this.adType = adType;
	}

	/**
	 * @return the adOrientation
	 */
	@Column(name="DC_AD_ORIENTATION", nullable = true)
	public String getAdOrientation() {
		return adOrientation;
	}
	/**
	 * @param adOrientation the adOrientation to set
	 */
	public void setAdOrientation(String adOrientation) {
		this.adOrientation = adOrientation;
	}
	/**
	 * @return the adSize
	 */
	@Column(name="DC_AD_SIZE", nullable = true)
	public String getAdSize() {
		return adSize;
	}
	/**
	 * @param adSize the adSize to set
	 */
	public void setAdSize(String adSize) {
		this.adSize = adSize;
	}
	/**
	 * @return the currency
	 */
	@Column(name="DC_CURRENCY", nullable = true)
	public short getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(short currency) {
		this.currency = currency;
	}	
	/**
	 * @return the startCurrencyRange
	 */
	@Column(name="DC_START_CURRENCY_RANGE", nullable = true)
	public String getStartCurrencyRange() {
		return startCurrencyRange;
	}
	/**
	 * @param startCurrencyRange the startCurrencyRange to set
	 */
	public void setStartCurrencyRange(String startCurrencyRange) {
		this.startCurrencyRange = startCurrencyRange;
	}
	/**
	 * @return the endCurrencyRange
	 */
	@Column(name="DC_END_CURRENCY_RANGE", nullable = true)
	public String getEndCurrencyRange() {
		return endCurrencyRange;
	}
	/**
	 * @param endCurrencyRange the endCurrencyRange to set
	 */
	public void setEndCurrencyRange(String endCurrencyRange) {
		this.endCurrencyRange = endCurrencyRange;
	}

	/**
	 * @return the ocrText
	 */
	@Column(name="DC_OCR_TEXT", nullable = true)
	public String getOcrText() {
		return ocrText;
	}
	/**
	 * @param ocrText the ocrText to set
	 */
	public void setOcrText(String ocrText) {
		this.ocrText = ocrText;
	}

	/**
	 * @return the others
	 */
	@Column(name="DC_OTHERS", nullable = true)
	public String getOthers() {
		return others;
	}
	/**
	 * @param others the others to set
	 */
	public void setOthers(String others) {
		this.others = others;
	}

	/**
	 * @return the parentImage
	 */
	@OneToOne ( targetEntity = ParentImage.class, fetch = FetchType.EAGER )
	@JoinColumn (name = "DN_PARENT_IMAGE_ID")
	public ParentImage getParentImage() {
		return parentImage;
	}
	/**
	 * @param parentImage the parentImage to set
	 */
	public void setParentImage(ParentImage parentImage) {
		this.parentImage = parentImage;
	}
	/**
	 * @return the deCompany
	 */
	@OneToOne ( targetEntity = DeCompany.class, fetch = FetchType.EAGER )
	@JoinColumn (name = "DN_DECOMPANY_ID")
	public DeCompany getDeCompany() {
		return deCompany;
	}
	/**
	 * @param deCompany the deCompany to set
	 */
	public void setDeCompany(DeCompany deCompany) {
		this.deCompany = deCompany;
	}

	@OneToOne ( targetEntity = DeJob.class, fetch = FetchType.EAGER )
	@JoinColumn(name="DE_JOB_ID", nullable = true)
	public DeJob getDeJobid() {
		return deJobid;
	}
	/**
	 * 
	 * @param deJobid
	 */
	public void setDeJobid(DeJob deJobid) {
		this.deJobid = deJobid;
	}
	/**
	 * 
	 * @return APPROVED
	 */
	@Column(name="DB_ISAPPROVED", nullable = true)
	public int getIsApproved() {
		return isApproved;
	}
	/**
	 * 
	 * @param isApproved
	 */

	public void setIsApproved(int isApproved) {
		this.isApproved = isApproved;
	}

	/**
	 * 
	 * @return searchValueAdvertisertype
	 */
	@Column(name="DC_SEARCH_ADVERTISER_TYPE", nullable = true)
	public String getSearchValueAdvertisertype() {
		return searchValueAdvertisertype;
	}
	/**
	 * 
	 * @param searchValueAdvertisertype
	 */
	public void setSearchValueAdvertisertype(String searchValueAdvertisertype) {
		this.searchValueAdvertisertype = searchValueAdvertisertype;
	}

	/**
	 * 
	 * @return adCategory
	 */
	@Column(name="DC_AD_CATEGORY", nullable = true)
	public String getAdCategory() {
		return adCategory;
	}
	/**
	 * 
	 * @param adCategory
	 */
	public void setAdCategory(String adCategory) {
		this.adCategory = adCategory;
	}

	/**
	 * 
	 * @return length
	 */
	@Column(name="DC_LENGTH", nullable = true)
	public String getLength() {
		return length;
	}
	/**
	 * 
	 * @param length
	 */
	public void setLength(String length) {
		this.length = length;
	}

	/**
	 * 
	 * @return width
	 */
	@Column(name="DC_WIDTH", nullable = true)
	public String getWidth() {
		return width;
	}
	/**
	 * 
	 * @param width
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * 
	 * @return jobDensity
	 */
	@Column(name="DC_JOB_DENSITY", nullable = true)
	public String getJobDensity() {
		return jobDensity;
	}
	/**
	 * 
	 * @param jobDensity
	 */
	public void setJobDensity(String jobDensity) {
		this.jobDensity = jobDensity;
	}
	/**
	 * 
	 * @return otherAdvertisertype
	 */
	@Column(name="DC_OTHER_ADVERTISER_TYPE", nullable = true)
	public String getOtherAdvertisertype() {
		return otherAdvertisertype;
	}
	/**
	 * 
	 * @param otherAdvertisertype
	 */
	public void setOtherAdvertisertype(String otherAdvertisertype) {
		this.otherAdvertisertype = otherAdvertisertype;
	}
    /**
     * 
     * @return the addColumn
     */
	@Column(name="DC_ADD_COLUMN", nullable = true)
	public String getAddColumn() {
		return addColumn;
	}
    /**
     * 
     * @param addColumn
     *        the addColumn to set
     */
	public void setAddColumn(String addColumn) {
		this.addColumn = addColumn;
	}

	 /**
     * 
     * @return the contactInfo
     */	
	@Column(name="DC_CONTACT_INFO", nullable = true)
	public String getContactInfo() {
		return contactInfo;
	}

    /**
     * 
     * @param contactInfo
     *        the contactInfo to set
     */
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
	
}
