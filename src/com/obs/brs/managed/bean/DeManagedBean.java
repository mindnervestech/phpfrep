package com.obs.brs.managed.bean;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;
import org.primefaces.context.RequestContext;


import com.obs.brs.messages.IMessagesService;
import com.obs.brs.model.ChildImage;
import com.obs.brs.model.Country;
import com.obs.brs.model.DataEntry;
import com.obs.brs.model.DeCompany;
import com.obs.brs.model.DeJob;
import com.obs.brs.model.DeletedImage;
import com.obs.brs.model.ParentImage;
import com.obs.brs.model.Publication;
import com.obs.brs.model.States;
import com.obs.brs.model.User;
import com.obs.brs.ocr.Ocr;
import com.obs.brs.service.IChildImageService;
import com.obs.brs.service.IDeService;
import com.obs.brs.service.IDeletedImageService;
import com.obs.brs.service.IParentImageService;
import com.obs.brs.service.ISettingsService;
import com.obs.brs.service.IUserService;
import com.obs.brs.session.manager.FacesUtils;
import com.obs.brs.session.manager.SessionManager;
import com.obs.brs.utils.CommonProperties;
import com.obs.brs.utils.CommonUtils;
import com.obs.brs.utils.IoUtils;
import com.obs.brs.utils.StringUtility;
/**
 * DeManaged Bean
 * @author Jeevanantham
 *
 */
@ManagedBean(name="deBean")
@ViewScoped
public class DeManagedBean implements Serializable{


	private static final long serialVersionUID = 1L;
	CommonUtils commonUtils 		= CommonUtils.getInstance();
	SessionManager sessionManager 	= new SessionManager();
	FacesUtils facesUtils 			= new FacesUtils();
	public String imageBasePath = CommonProperties.getBasePath()+CommonProperties.getImageContextPath();

	//Spring User Service is injected...
	@ManagedProperty(value ="#{MessageService}")
	IMessagesService messageService;

	@ManagedProperty(value="#{UserService}")
	IUserService userService;

	@ManagedProperty(value="#{SettingsService}")
	ISettingsService settingsService;

	@ManagedProperty(value ="#{DeService}")
	IDeService deService;

	@ManagedProperty(value ="#{ParentImageService}")
	IParentImageService parentImageService;

	@ManagedProperty(value ="#{ChildImageService}")
	IChildImageService childImageService;

	@ManagedProperty(value ="#{DeletedImageService}")
	IDeletedImageService deletedImageService;

	private static final String QCJOB ="manageqcjob";
	private static final String QCAPPROVAL = "approvalqcjob";
	private static final String DATAENTRY = "dataentry";
	private static final String RETURN_MANAGE_DATAENTRY = "returnmanagedataentry";
	private static final String CROP_IMAGE = "cropImage";
	private static final String CHILD_IMAGE = "childImage";
	private static final String IMAGE_UPLOAD = "parentimageupload";
	private static final String GALLERY = "gallery";
	private static final String RETURN_CHILD_IMAGE = "returnchildImage";
	private static final String LIST_PUBLICATION = "listpublication";
	private static final String QCJOBBYJOURNAL = "view_qcjob_by_journal";

	private Map<Long, Boolean> checkedDe = new HashMap<Long, Boolean>();
	private Map<Long, Boolean> checkedQc = new HashMap<Long, Boolean>();
	private long id;
	private long companyId;
	private String companyName;
	private String companyURL;
	private String department;
	private String address;
	private String city;
	private String state;
	private String country;
	private String pincode;
	private String adHeadLine;
	private ChildImage childImage;
	private String jobListing;
	private String advertiserType; 
	private String advertiserTypeSec;
	private String adType; 
	private String adOrientation;
	private String adSize;
	private String others;
	private short currency;
	private String startCurrencyRange;
	private String endCurrencyRange;
	private String ocrText;
	private Boolean isqualityCheck;
	private Boolean isDeleted;
	private int isApproved;
	private User user;
	private Date created_On;
	private Date deleted_On;
	//search & filters
	private String searchValue ="";
	private String searchIssueDate = "";	

	List<DataEntry> dataEntryuserList;
	User currentUser;
	List<DeJob> deJobList;
	List<ParentImage> parentImageList = new ArrayList<ParentImage>();
	List<ChildImage> childImageList = new ArrayList<ChildImage>();
	List<DataEntry> childImagesDataList = new ArrayList<DataEntry>();
	//For cropping 
	private int cropWidth;
	private int cropHeight;
	private int cropX1;
	private int cropX2;
	private int cropY1;
	private int cropY2;
	private String croppedImageName;
	private ParentImage parentImage;
	private String msgLabel;
	private int msgFormat;
	//For Publication
	private String publicationTitle;
	private String publicationTitleNextValue;
	private String section;
	private String page;
	private Date issueDate; 
	private String parentImageName;
	private String childImageName;
	private long parentImageId;
	private long deDataId;
	private long deJobid;
	private long childImageId;
	private Boolean checkVal=false;
	private Boolean checkPreVal=false;
	private String searchValueInCompanyName;
	private int issueDay;
	private int issueDayNext;
	private int issueDayNextThird;
	private int issueDayNextFour;
	private int issueMonth;
	private int issueYear;
	private int publicationStatus;
	private String issueDateString;

	private String issueDayStr;
	private String issueDayNextStr;
	private String issueMonthStr;
	private String issueYearStr;
	private Date issueDateSerach;
 
	private int jobStatus;
	private String searchText;
	private int firstOff;
	private int secondOff;
	private int issueMonthNext;
	private int sectionFirstOff;
	private int sectionSeconfOff;
	private int  advertiserTypeFirstOff;
	private int  advertiserTypeSecondOff;
	private int institutionTypeFirstOff;
	private int institutionTypeSecondOff;
	private String sectionNextValue;
	private String searchValueAdvertisertype;
	List<DeCompany> deCompanyList = new ArrayList<DeCompany>();
	List<Country> countryResults = new ArrayList<Country>();
	List<States> stateResults = new ArrayList<States>();
	List<Publication> advertiserTypeList = new ArrayList<Publication>();
	private String adCategory;
	private String adCategorySec;
	private String length;
	private String width;
	private String jobDensity;
	private String otherAdvertisertype;
	private String landingPageURL;
	private String sectionspecialRegional;
	private String sectionspecialTopic;
	private String sectionother;
	private String addColumn;
	private String imageHeight;
	private String imageWidth;
	private int changeRowsPerPage = 15;
	private String advertiserName;
	private String publicationId;
	private String jobStatusGot;
	private String issueDatePubSearch;

	public String getIssueDatePubSearch() {
		return issueDatePubSearch;
	}

	public void setIssueDatePubSearch(String issueDatePubSearch) {
		this.issueDatePubSearch = issueDatePubSearch;
	}

	private int		jobGot;
	
	public int getJobGot() {
		return jobGot;
	}

	public void setJobGot(int jobGot) {
		this.jobGot = jobGot;
	}

	public String getSearchIssueDate() {
		return searchIssueDate;
	}
	
	public void setSearchIssueDate(String searchIssueDate) {
		this.searchIssueDate = searchIssueDate;
	}
	public String getJobStatusGot() {
		return jobStatusGot;
	}
	public void setJobStatusGot(String jobStatusGot) {
		this.jobStatusGot = jobStatusGot;
	}
	public String getPublicationId() {
		return publicationId;
	}
	public void setPublicationId(String publicationId) {
		this.publicationId = publicationId;
	}
	
	DecimalFormat decimalFormat=new DecimalFormat("#.##");
	//paging
	private int imagePerPage = 20;
	private int imageOffset = 0;

	public String getAdvertiserName() {
		return advertiserName;
	}
	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}
	public int getImagePerPage() {
		return imagePerPage;
	}
	public void setImagePerPage(int imagePerPage) {
		this.imagePerPage = imagePerPage;
	}
	public int getImageOffset() {
		return imageOffset;
	}
	public void setImageOffset(int imageOffset) {
		this.imageOffset = imageOffset;
	}
	public String getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(String imageHeight) {
		this.imageHeight = imageHeight;
	}
	public String getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(String imageWidth) {
		this.imageWidth = imageWidth;
	}


	public String getAddColumn() {
		return addColumn;
	}
	public void setAddColumn(String addColumn) {
		this.addColumn = addColumn;
	}
	public String getChildImageName() {
		return childImageName;
	}
	public void setChildImageName(String childImageName) {
		this.childImageName = childImageName;
	}
	public int getInstitutionTypeFirstOff() {
		return institutionTypeFirstOff;
	}
	public void setInstitutionTypeFirstOff(int institutionTypeFirstOff) {
		this.institutionTypeFirstOff = institutionTypeFirstOff;
	}
	public int getInstitutionTypeSecondOff() {
		return institutionTypeSecondOff;
	}
	public void setInstitutionTypeSecondOff(int institutionTypeSecondOff) {
		this.institutionTypeSecondOff = institutionTypeSecondOff;
	}
	public String getAdvertiserTypeSec() {
		return advertiserTypeSec;
	}
	public void setAdvertiserTypeSec(String advertiserTypeSec) {
		this.advertiserTypeSec = advertiserTypeSec;
	}
	public int getAdvertiserTypeFirstOff() {
		return advertiserTypeFirstOff;
	}
	public void setAdvertiserTypeFirstOff(int advertiserTypeFirstOff) {
		this.advertiserTypeFirstOff = advertiserTypeFirstOff;
	}
	public int getAdvertiserTypeSecondOff() {
		return advertiserTypeSecondOff;
	}
	public void setAdvertiserTypeSecondOff(int advertiserTypeSecondOff) {
		this.advertiserTypeSecondOff = advertiserTypeSecondOff;
	}
	public String getAdCategorySec() {
		return adCategorySec;
	}
	public void setAdCategorySec(String adCategorySec) {
		this.adCategorySec = adCategorySec;
	}
	public String getSectionspecialRegional() {
		return sectionspecialRegional;
	}
	public void setSectionspecialRegional(String sectionspecialRegional) {
		this.sectionspecialRegional = sectionspecialRegional;
	}
	public String getSectionspecialTopic() {
		return sectionspecialTopic;
	}
	public void setSectionspecialTopic(String sectionspecialTopic) {
		this.sectionspecialTopic = sectionspecialTopic;
	}
	public String getSectionother() {
		return sectionother;
	}
	public void setSectionother(String sectionother) {
		this.sectionother = sectionother;
	}
	public String getOtherAdvertisertype() {
		return otherAdvertisertype;
	}
	public void setOtherAdvertisertype(String otherAdvertisertype) {
		this.otherAdvertisertype = otherAdvertisertype;
	}
	public String getLandingPageURL() {
		return landingPageURL;
	}
	public void setLandingPageURL(String landingPageURL) {
		this.landingPageURL = landingPageURL;
	}
	public String getJobDensity() {
		return jobDensity;
	}
	public void setJobDensity(String jobDensity) {
		this.jobDensity = jobDensity;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getAdCategory() {
		return adCategory;
	}
	public void setAdCategory(String adCategory) {
		this.adCategory = adCategory;
	}
	public List<Publication> getAdvertiserTypeList() {
		return advertiserTypeList;
	}
	public void setAdvertiserTypeList(List<Publication> advertiserTypeList) {
		this.advertiserTypeList = advertiserTypeList;
	}
	public String getSearchValueAdvertisertype() {
		return searchValueAdvertisertype;
	}
	public void setSearchValueAdvertisertype(String searchValueAdvertisertype) {
		this.searchValueAdvertisertype = searchValueAdvertisertype;
	}
	public String getSectionNextValue() {
		return sectionNextValue;
	}
	public void setSectionNextValue(String sectionNextValue) {
		this.sectionNextValue = sectionNextValue;
	}
	public int getSectionFirstOff() {
		return sectionFirstOff;
	}
	public void setSectionFirstOff(int sectionFirstOff) {
		this.sectionFirstOff = sectionFirstOff;
	}
	public int getSectionSeconfOff() {
		return sectionSeconfOff;
	}
	public void setSectionSeconfOff(int sectionSeconfOff) {
		this.sectionSeconfOff = sectionSeconfOff;
	}
	public int getIssueDayNextFour() {
		return issueDayNextFour;
	}
	public void setIssueDayNextFour(int issueDayNextFour) {
		this.issueDayNextFour = issueDayNextFour;
	}
	public List<Country> getCountryResults() {
		return countryResults;
	}
	public void setCountryResults(List<Country> countryResults) {
		this.countryResults = countryResults;
	}
	public List<States> getStateResults() {
		return stateResults;
	}
	public void setStateResults(List<States> stateResults) {
		this.stateResults = stateResults;
	}
	public int getIssueMonthNext() {
		return issueMonthNext;
	}
	public void setIssueMonthNext(int issueMonthNext) {
		this.issueMonthNext = issueMonthNext;
	}
	public int getIssueDayNextThird() {
		return issueDayNextThird;
	}
	public void setIssueDayNextThird(int issueDayNextThird) {
		this.issueDayNextThird = issueDayNextThird;
	}
	public int getFirstOff() {
		return firstOff;
	}
	public void setFirstOff(int firstOff) {
		this.firstOff = firstOff;
	}
	public int getSecondOff() {
		return secondOff;
	}
	public void setSecondOff(int secondOff) {
		this.secondOff = secondOff;
	}

	public String getPublicationTitleNextValue() {
		return publicationTitleNextValue;
	}

	public void setPublicationTitleNextValue(String publicationTitleNextValue) {
		this.publicationTitleNextValue = publicationTitleNextValue;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public Date getIssueDateSerach() {
		return issueDateSerach;
	}

	public void setIssueDateSerach(Date issueDateSerach) {
		this.issueDateSerach = issueDateSerach;
	}

	public int getIssueDay() {
		return issueDay;
	}

	public void setIssueDay(int issueDay) {
		this.issueDay = issueDay;
	}

	public int getIssueDayNext() {
		return issueDayNext;
	}

	public void setIssueDayNext(int issueDayNext) {
		this.issueDayNext = issueDayNext;
	}

	public int getIssueMonth() {
		return issueMonth;
	}

	public void setIssueMonth(int issueMonth) {
		this.issueMonth = issueMonth;
	}

	public int getIssueYear() {
		return issueYear;
	}

	public void setIssueYear(int issueYear) {
		this.issueYear = issueYear;
	}

	public String getIssueDayStr() {
		return issueDayStr;
	}

	public void setIssueDayStr(String issueDayStr) {
		this.issueDayStr = issueDayStr;
	}

	public String getIssueDayNextStr() {
		return issueDayNextStr;
	}

	public void setIssueDayNextStr(String issueDayNextStr) {
		this.issueDayNextStr = issueDayNextStr;
	}

	public String getIssueMonthStr() {
		return issueMonthStr;
	}

	public void setIssueMonthStr(String issueMonthStr) {
		this.issueMonthStr = issueMonthStr;
	}

	public String getIssueYearStr() {
		return issueYearStr;
	}

	public void setIssueYearStr(String issueYearStr) {
		this.issueYearStr = issueYearStr;
	}

	public String getIssueDateString() {
		return issueDateString;
	}

	public void setIssueDateString(String issueDateString) {
		this.issueDateString = issueDateString;
	}
	public int getPublicationStatus() {
		return publicationStatus;
	}
	public void setPublicationStatus(int publicationStatus) {
		this.publicationStatus = publicationStatus;
	}

	public String getSearchValueInCompanyName() {
		return searchValueInCompanyName;
	}
	public void setSearchValueInCompanyName(String searchValueInCompanyName) {
		this.searchValueInCompanyName = searchValueInCompanyName;
	}
	public List<DeCompany> getDeCompanyList() {
		return deCompanyList;
	}
	public void setDeCompanyList(List<DeCompany> deCompanyList) {
		this.deCompanyList = deCompanyList;
	}
	public long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	public int getMsgFormat() {
		return msgFormat;
	}
	public void setMsgFormat(int msgFormat) {
		this.msgFormat = msgFormat;
	}

	public Boolean getCheckPreVal() {
		return checkPreVal;
	}
	public void setCheckPreVal(Boolean checkPreVal) {
		this.checkPreVal = checkPreVal;
	}
	public Boolean getCheckVal() {
		return checkVal;
	}
	public void setCheckVal(Boolean checkVal) {
		this.checkVal = checkVal;
	}
	public long getChildImageId() {
		return childImageId;
	}
	public void setChildImageId(long childImageId) {
		this.childImageId = childImageId;
	}
	public long getDeJobid() {
		return deJobid;
	}
	public void setDeJobid(long deJobid) {
		this.deJobid = deJobid;
	}
	public long getDeDataId() {
		return deDataId;
	}
	public void setDeDataId(long deDataId) {
		this.deDataId = deDataId;
	}
	public long getParentImageId() {
		return parentImageId;
	}
	public void setParentImageId(long parentImageId) {
		this.parentImageId = parentImageId;
	}
	public String getParentImageName() {
		return parentImageName;
	}
	public void setParentImageName(String parentImageName) {
		this.parentImageName = parentImageName;
	}
	public String getPublicationTitle() {
		return publicationTitle;
	}
	public void setPublicationTitle(String publicationTitle) {
		this.publicationTitle = publicationTitle;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * @return the cropWidth
	 */
	public int getCropWidth() {
		return cropWidth;
	}
	/**
	 * @param cropWidth the cropWidth to set
	 */
	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}
	/**
	 * @return the cropHeight
	 */
	public int getCropHeight() {
		return cropHeight;
	}
	/**
	 * @param cropHeight the cropHeight to set
	 */
	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}
	/**
	 * @return the cropX1
	 */
	public int getCropX1() {
		return cropX1;
	}
	/**
	 * @param cropX1 the cropX1 to set
	 */
	public void setCropX1(int cropX1) {
		this.cropX1 = cropX1;
	}
	/**
	 * @return the cropX2
	 */
	public int getCropX2() {
		return cropX2;
	}
	/**
	 * @param cropX2 the cropX2 to set
	 */
	public void setCropX2(int cropX2) {
		this.cropX2 = cropX2;
	}
	/**
	 * @return the cropY1
	 */
	public int getCropY1() {
		return cropY1;
	}
	/**
	 * @param cropY1 the cropY1 to set
	 */
	public void setCropY1(int cropY1) {
		this.cropY1 = cropY1;
	}
	/**
	 * @return the cropY2
	 */
	public int getCropY2() {
		return cropY2;
	}
	/**
	 * @param cropY2 the cropY2 to set
	 */
	public void setCropY2(int cropY2) {
		this.cropY2 = cropY2;
	}
	/**
	 * @return the croppedImage
	 */
	public String getCroppedImageName() {
		return croppedImageName;
	}
	/**
	 * @param croppedImage the croppedImage to set
	 */
	public void setCroppedImageName(String croppedImageName) {
		this.croppedImageName = croppedImageName;
	}
	/**
	 * @return the parentImage
	 */
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
	 * get searchValue
	 * @return
	 */
	public String getSearchValue() {
		return searchValue;
	}
	/**
	 * set  searchValue
	 * @param searchValue
	 */
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	/**
	 * get  dataEntryuserList
	 * @return
	 */
	public List<DataEntry> getDataEntryuserList() {
		return dataEntryuserList;
	}
	/**
	 * set dataEntryuserList
	 * @param dataEntryuserList
	 */
	public void setDataEntryuserList(List<DataEntry> dataEntryuserList) {
		this.dataEntryuserList = dataEntryuserList;
	}

	/**
	 * get checkedUser
	 * @return
	 */
	public Map<Long, Boolean> getCheckedDe() {
		return checkedDe;
	}
	/**
	 * get checkedUser
	 * @return
	 */
	public Map<Long, Boolean> getCheckedQc() {
		return checkedQc;
	}
	/**
	 * 
	 * 
	 * @return int - de_data Id
	 */
	public long getId() {
		return id;
	}

	/**
	 * 
	 * 
	 * @param int -  de_data Id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	/**
	 * @return the companyName
	 */
	public String getCompanyURL() {
		return companyURL;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyURL(String companyURL) {
		this.companyURL = companyURL;
	}
	
	/**
	 * @return the address1
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the address1
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the pincode
	 */
	public String getPincode() {
		return pincode;
	}

	/**
	 * @param pincode the pincode to set
	 */
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	/**
	 * @return the userType
	 */
	public ChildImage getChildImage() {
		return childImage;
	}
	/**
	 * @param userType the userRole to set
	 */
	public void setChildImage(ChildImage childImage) {
		this.childImage = childImage;
	}
	/**
	 * @return the isActive
	 */
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
	 * @return the adHeadLine 
	 * 
	 */
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
	public User getUser() {
		return user;
	}
	/**
	 * @param deleted_by the deleted_by to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return the created_On
	 */
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
	 * @return the adOrientation
	 */
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
	public String getAdSize() {
		return adSize;
	}
	/**
	 * @param adSize the adSize to set
	 */
	public void setAdSize(String adSize) {
		this.adSize = adSize;
	}

	public short getCurrency() {
		return currency;
	}
	public void setCurrency(short currency) {
		this.currency = currency;
	}
	/**
	 * @return the ocrText
	 */
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
	 * Get messageService
	 * 
	 * @return messageService - messageService
	 */
	public IMessagesService getMessageService() {
		return messageService;
	}
	/**
	 * Set messageService
	 * 
	 * @param messageService - messageService
	 */
	public void setMessageService(IMessagesService messageService) {
		this.messageService = messageService;
	}
	/**
	 * Get User Service
	 * 
	 * @return IUserService - User Service
	 */
	public IUserService getUserService() {
		return userService;
	}
	/**
	 * Set User Service
	 * 
	 * @param IUserService - User Service
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	/**
	 * Get settingsService
	 * 
	 * @return settingsService - settingsService
	 */
	public ISettingsService getSettingsService() {
		return settingsService;
	}
	/**
	 * Set settingsService
	 * 
	 * @param settingsService -settingsService
	 */
	public void setSettingsService(ISettingsService settingsService) {
		this.settingsService = settingsService;
	}
	/**
	 * Get deService
	 * 
	 * @return deService -deService
	 */
	public IDeService getDeService() {
		return deService;
	}
	/**
	 * Set deService
	 * 
	 * @param deService - deService
	 */
	public void setDeService(IDeService deService) {
		this.deService = deService;
	}
	/**
	 * Get parentImageService
	 * 
	 * @return parentImageService - parentImageService
	 */
	public IParentImageService getParentImageService() {
		return parentImageService;
	}
	/**
	 * Set parentImageService
	 * 
	 * @param parentImageService - parentImageService
	 */
	public void setParentImageService(IParentImageService parentImageService) {
		this.parentImageService = parentImageService;
	}
	/**
	 * Get childImageService
	 * 
	 * @return childImageService - childImageService
	 */
	public IChildImageService getChildImageService() {
		return childImageService;
	}
	/**
	 * Set childImageService
	 * 
	 * @param childImageService - childImageService
	 */
	public void setChildImageService(IChildImageService childImageService) {
		this.childImageService = childImageService;
	}
	/**
	 * 
	 * @return
	 */
	public IDeletedImageService getDeletedImageService() {
		return deletedImageService;
	}
	/**
	 * 
	 * @param deletedImageService
	 */
	public void setDeletedImageService(IDeletedImageService DeletedImageService) {
		deletedImageService = DeletedImageService;
	}
	/**
	 * Get adType
	 * 
	 * @return adType - adType
	 */
	public String getAdType() {
		return adType;
	}
	/**
	 * Set adType
	 * 
	 * @param adType - adType
	 */
	public void setAdType(String adType) {
		this.adType = adType;
	}

	/**
	 * Get others
	 * 
	 * @return others - others
	 */
	public String getOthers() {
		return others;
	}

	/**
	 * Set others
	 * 
	 * @param others - others
	 */
	public void setOthers(String others) {
		this.others = others;
	}

	/**
	 * Get startCurrencyRange
	 * 
	 * @return startCurrencyRange - startCurrencyRange
	 */
	public String getStartCurrencyRange() {
		return startCurrencyRange;
	}
	/**
	 * Set startCurrencyRange
	 * 
	 * @param startCurrencyRange - startCurrencyRange
	 */
	public void setStartCurrencyRange(String startCurrencyRange) {
		this.startCurrencyRange = startCurrencyRange;
	}

	/**
	 * Get endCurrencyRange
	 * 
	 * @return endCurrencyRange - endCurrencyRange
	 */
	public String getEndCurrencyRange() {
		return endCurrencyRange;
	}
	/**
	 * Set endCurrencyRange
	 * 
	 * @param endCurrencyRange - endCurrencyRange
	 */
	public void setEndCurrencyRange(String endCurrencyRange) {
		this.endCurrencyRange = endCurrencyRange;
	}

	public String getMsgLabel() {
		return msgLabel;
	}
	public void setMsgLabel(String msgLabel) {
		this.msgLabel = msgLabel;
	}

	/**
	 * set All ad Details 
	 * @return
	 */
	public void setAllAdDetails(DataEntry dataEntry){
		//this.id = dataEntry.getId();
		this.deDataId=dataEntry.getId();
		this.deJobid=dataEntry.getDeJobid().getId()!= 0L?dataEntry.getDeJobid().getId():0L;
		this.adHeadLine = dataEntry.getAdHeadLine();
		this.jobListing = dataEntry.getJobListing();
		this.advertiserType = dataEntry.getAdvertiserType();
		this.adType = dataEntry.getAdType();
		this.adOrientation = dataEntry.getAdOrientation();
		this.adSize = dataEntry.getAdSize();
		this.currency = dataEntry.getCurrency();
		this.startCurrencyRange = dataEntry.getStartCurrencyRange();
		this.endCurrencyRange = dataEntry.getEndCurrencyRange();
		this.ocrText = dataEntry.getOcrText();
		this.isApproved=dataEntry.getIsApproved();
		this.adCategory =dataEntry.getAdCategory(); 
		this.width = dataEntry.getWidth();
		this.length = dataEntry.getLength();
		this.jobDensity = dataEntry.getJobDensity();
		this.searchValueAdvertisertype = dataEntry.getSearchValueAdvertisertype();
		this.otherAdvertisertype = dataEntry.getOtherAdvertisertype();
		this.landingPageURL = dataEntry.getLandingPageURL();
		this.addColumn = dataEntry.getAddColumn();
	}

	// getAll AdDetails who is currently in session 
	public DataEntry getAllAdDetails(DataEntry dataEntry){
		dataEntry.setAdvertiserType(this.advertiserType);
		dataEntry.setAdType(this.adType);
		dataEntry.setAdOrientation(this.adOrientation);
		dataEntry.setAdHeadLine(this.adHeadLine);
		dataEntry.setAdSize(this.adSize);
		dataEntry.setCurrency(this.currency);
		dataEntry.setStartCurrencyRange(this.startCurrencyRange);
		dataEntry.setEndCurrencyRange(this.endCurrencyRange);
		dataEntry.setOcrText(this.ocrText);
		dataEntry.setAdCategory(this.adCategory);
		dataEntry.setWidth(this.width);
		dataEntry.setOthers(this.others);
		dataEntry.setLength(this.length);
		dataEntry.setJobDensity(this.jobDensity);
		dataEntry.setSearchValueAdvertisertype(this.searchValueAdvertisertype);
		dataEntry.setOtherAdvertisertype(this.otherAdvertisertype);
		dataEntry.setLandingPageURL(this.landingPageURL);
		dataEntry.setAddColumn(this.addColumn);
		return dataEntry;
	}

	/**
	 * set All ad Details 
	 * @return
	 */
	public void setAllDeCompanyDetails(DeCompany deCompany){
		this.companyId = deCompany.getId();
		/*this.companyName = deCompany.getCompanyName();

		this.address = deCompany.getAddress();
		this.city = deCompany.getCity();
		this.state = deCompany.getState();
		this.country = deCompany.getCountry();
		this.pincode = deCompany.getPincode();*/
	}

	// getAll AdDetails who is currently in session 
	public DeCompany getAllDeCompanyDetails(DeCompany deCompany){
		deCompany.setCompanyName(this.companyName);
		deCompany.setAddress(this.address);
		deCompany.setCity(this.city);
		deCompany.setState(this.state);
		deCompany.setCountry(this.country);
		deCompany.setPincode(this.pincode);
		deCompany.setDepartment(this.department);
		deCompany.setCompanyURL(this.companyURL);
		return deCompany;
	}

	/** 
	 * 	  save ad Details 
	 *    add new DE
	 */
	public String saveAndExitDeData() 
	{
		String flag=null;
		try
		{
			currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(currentUser != null)
			{
				Object sessionObj = sessionManager.getSessionAttribute(SessionManager.EDITUSER);
				int deJobId  = sessionObj!=null?((Integer)sessionObj).intValue():0;
				DeJob deJob = 	deService.getDeJobById(deJobId);
				if(deJob != null){
					DataEntry dataEntry = deService.getDataEntryByParentImageId(deJob.getParentImage().getId());
					List<ChildImage> childImageList=childImageService.getChildImagesByParent(deJob.getParentImage().getId());
					//Collections.reverse(childImageList);
					if(childImageList!=null && childImageList.size() >0){
						if(dataEntry != null && childImageList!=null && childImageList.size() >0 ){
							for(int i=0;childImageList.size()>i;i++)
							{
								DataEntry dataChild = deService.getDataEntryByChildImageId(childImageList.get(i).getId());
								if(dataChild!=null){
									dataChild.setChildImage(childImageList.get(i));
									dataChild.setIsDeleted(false);
									dataChild.setIsqualityCheck(false);
									dataChild.setDeJobid(deJob);
									dataChild.setCreated_by(currentUser);
									dataChild.setParentImage(deJob.getParentImage());
									dataChild.setWidth(String.valueOf(childImageList.get(i).getImageWidth()));
									dataChild.setLength(String.valueOf(childImageList.get(i).getImageHeight()));
									deService.updateDataEntry(dataChild);
								}else{
									dataEntry = new DataEntry();
									dataEntry.setIsDeleted(false);
									dataEntry.setIsqualityCheck(false);
									dataEntry.setDeJobid(deJob);
									dataEntry.setCreated_by(currentUser);
									dataEntry.setParentImage(deJob.getParentImage());
									dataEntry.setChildImage(childImageList.get(i));
									dataEntry.setWidth(String.valueOf(childImageList.get(i).getImageWidth()));
									dataEntry.setLength(String.valueOf(childImageList.get(i).getImageHeight()));
									deService.addDataEntry(dataEntry);				
								}
							}
						}else if(dataEntry == null && childImageList!=null && childImageList.size() >0){
							dataEntry = new DataEntry();
							dataEntry.setIsDeleted(false);
							dataEntry.setIsqualityCheck(false);
							dataEntry.setDeJobid(deJob);
							dataEntry.setCreated_by(currentUser);
							dataEntry.setParentImage(deJob.getParentImage());
							if(childImageList!=null)
							{
								for(int i=0;childImageList.size()>i;i++)
								{
									dataEntry.setChildImage(childImageList.get(i));
									dataEntry.setWidth(String.valueOf(childImageList.get(i).getImageWidth()));
									dataEntry.setLength(String.valueOf(childImageList.get(i).getImageHeight()));
									deService.addDataEntry(dataEntry);				
								}
							}
						}else if(dataEntry != null && childImageList.isEmpty()){
							dataEntry.setIsDeleted(false);
							dataEntry.setIsqualityCheck(false);
							dataEntry.setDeJobid(deJob);
							dataEntry.setCreated_by(currentUser);
							dataEntry.setParentImage(deJob.getParentImage());
							//dataEntry.setChildImage(deJob.getParentImage());
							deService.updateDataEntry(dataEntry);		
						}else{
							dataEntry = new DataEntry();
							dataEntry.setIsDeleted(false);
							dataEntry.setIsqualityCheck(false);
							dataEntry.setDeJobid(deJob);
							dataEntry.setCreated_by(currentUser);
							dataEntry.setParentImage(deJob.getParentImage());
							deService.addDataEntry(dataEntry);		
						}
					} else {
						flag=String.valueOf(deJob.getParentImage().getId());
					}
				}
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * Set Approval to job qcApprovedJob
	 */
	public String qcApprovedJob() 
	{
		try
		{
			currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(currentUser != null)
			{
				FacesUtils facesUtils = new FacesUtils();
				long deDataId = Long.valueOf(facesUtils.getRequestParameterMap("baseId"));
				if(deDataId >0){
					DataEntry dataEntry = deService.getDataEntryById(deDataId);
					if(dataEntry != null ){
						dataEntry = getAllAdDetails(dataEntry);
						dataEntry.setIsApproved(2);
						deService.updateDataEntry(dataEntry);
						messageService.messageInformation(null, "Data Entry has been Approval successfully.");
						return QCJOB;
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}
	/**
	 * Set Reject the job
	 */
	public String qcRejectJob() 
	{
		try
		{
			currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(currentUser != null)
			{
				FacesUtils facesUtils = new FacesUtils();
				long deDataId = Long.valueOf(facesUtils.getRequestParameterMap("baseId"));
				if(deDataId >0){
					DataEntry dataEntry = deService.getDataEntryById(deDataId);
					if(dataEntry != null ){
						dataEntry = getAllAdDetails(dataEntry);
						dataEntry.setIsApproved(3);
						deService.updateDataEntry(dataEntry);
						messageService.messageInformation(null, "Data Entry has been Rejected successfully.");
						return QCJOB;
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}
	public String qcRevertJob() 
	{
		try
		{
			currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(currentUser != null)
			{
				FacesUtils facesUtils = new FacesUtils();
				long deDataId = Long.valueOf(facesUtils.getRequestParameterMap("baseId"));
				if(deDataId >0){
					DataEntry dataEntry = deService.getDataEntryById(deDataId);
					if(dataEntry != null ){
						dataEntry = getAllAdDetails(dataEntry);
						dataEntry.setIsApproved(1);
						deService.updateDataEntry(dataEntry);
						messageService.messageInformation(null, "Data Entry has been Rejected successfully.");
						return QCJOB;
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}
	/**
	 * Page load event for selected company user in admin page
	 * @param event
	 */
	public void loadDeInfo(ComponentSystemEvent event)
	{
		try {
			if (!FacesContext.getCurrentInstance().isPostback()) 
			{
				Object sessionObj = sessionManager.getSessionAttribute(SessionManager.EDITUSER);
				this.id   = sessionObj!=null?((Integer)sessionObj).intValue():0; // get job id
				this.checkPreVal=true;
				if(this.id >0)
				{
					DeJob deJob = 	deService.getDeJobById(this.id);
					if(deJob != null){
						this.publicationTitle = deJob.getParentImage().getPublicationTitle().getPublicationTitle(); 
						this.issueDate =  deJob.getParentImage().getIssueDate();
						this.section =deJob.getParentImage().getSection().getPublicationTitle();
						this.page = deJob.getParentImage().getPage();
						this.parentImageId = deJob.getParentImage().getId();
						if(this.parentImageId>0){
							this.parentImageName = deJob.getParentImage().getImageName();
						}
						DataEntry dataEntry = deService.getDataEntryByParentImageId(deJob.getParentImage().getId());
						/*this.childImageId=dataEntry.getChildImage()!=null?dataEntry.getChildImage().getId():0L;
						if(this.childImageId >0){
							this.childImageName =dataEntry.getChildImage().getImageName();
						}else{
							this.childImageName = deJob.getParentImage().getImageName();
						}*/
						/*if(dataEntry != null){
							this.ocrText = dataEntry.getOcrText();
						}*/
						if(dataEntry != null && !dataEntry.getIsDeleted()){
							if(dataEntry != null){
								this.childImageId=dataEntry.getChildImage()!=null?dataEntry.getChildImage().getId():0L;
								if(this.childImageId >0){
									this.childImageName =dataEntry.getChildImage().getImageName();
								}else{
									this.childImageName = deJob.getParentImage().getImageName();
								}
								setAllAdDetails(dataEntry);
							}
							if(dataEntry.getDeCompany() != null){
								setAllDeCompanyDetails(dataEntry.getDeCompany());
							}
							if(this.childImageId == 0 && this.ocrText == null){
								try {
									System.out.println("deBean:loadDeInfo:: Loading this path(=0):"+(imageBasePath+CommonProperties.getParentImagePath()+this.parentImageId+"/"+this.parentImageName));
									File image=new File(imageBasePath+CommonProperties.getParentImagePath()+this.parentImageId+"/"+this.parentImageName);
									ImageIO.scanForPlugins();
									String result = new Ocr().doOCR(image);
									System.out.println("deBean:loadDeInfo:: Data from Image:"+result);
									if(result != null){
										this.ocrText = result;
									}
								} catch (Exception e) {
									System.err.println("deBean:loadDeInfo"+e.getMessage());
									e.printStackTrace();
								}
							}
							if(this.childImageId > 0 && this.ocrText == null){
								try {
									System.out.println("deBean:loadDeInfo:: Loading this path(>0):"+(imageBasePath+CommonProperties.getChildImagePath()+this.parentImageId+"/"+this.childImageId+"/"+dataEntry.getChildImage().getImageName()));
									File image=new File(imageBasePath+CommonProperties.getChildImagePath()+this.parentImageId+"/"+this.childImageId+"/"+dataEntry.getChildImage().getImageName());
									ImageIO.scanForPlugins();
									String result = new Ocr().doOCR(image);
									System.out.println("deBean:loadDeInfo:: Data from Image:"+result);
									if(result != null){
										this.ocrText = result;
									}
								} catch (Exception e) {
									System.err.println("deBean:loadDeInfo"+e.getMessage());
									e.printStackTrace();
								}
							}
						}
						//else{
						//clear();
						//}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * redirect add or edit company users 
	 * @return
	 */
	public String loadAddOrEditDe(){
		String flag=null;
		String val = facesUtils.getRequestParameterMap("deJobId");
		int deJobId = Integer.valueOf(val!=null?val:"0");
		if(deJobId >0){
			sessionManager.setSessionAttributeInSession(SessionManager.EDITUSER, deJobId);
			flag=saveAndExitDeData();
		}
		else
			sessionManager.removeSessionAttributeInSession(SessionManager.EDITUSER);
		if(deJobId >0 && flag==null){
			return DATAENTRY;
		}
		else
		{	
			int imageId = Integer.valueOf(flag!=null?flag:"0");
			if(imageId >0){
				sessionManager.setSessionAttributeInSession(SessionManager.CROPIMAGE, imageId);
				messageService.messageInformation(null, "Please Crop Your Advertisment");
				return RETURN_CHILD_IMAGE;
			}
		}
		return null;
	}
	/**
	 * redirect add or edit company users 
	 * @return
	 */
	public String loadAddOrEditQc(){

		int jobId = Integer.valueOf(facesUtils.getRequestParameterMap("qcJobId"));
		if(jobId >0){
			sessionManager.setSessionAttributeInSession(SessionManager.EDITUSER, jobId);
		}
		else
			sessionManager.removeSessionAttributeInSession(SessionManager.EDITUSER);
		if(jobId >0){
			return QCAPPROVAL;
		}
		else
			return QCAPPROVAL;
	}

	/**
	 * Get CompanyUser List
	 * 
	 * @return List - CompanyUser List
	 */
	public List<DataEntry> getDeListBySeachCriteria() { 
		User user = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
		dataEntryuserList = new ArrayList<DataEntry>();
		if(this.searchValue == null){
			dataEntryuserList.addAll(getDeService().getDeBySeachCriteria(user.getId()));
		}else{
			dataEntryuserList.addAll(getDeService().getDeBySeachCriteria(user.getId(),this.searchValue));	
		}
		Collections.reverse(dataEntryuserList);
		return dataEntryuserList;
	}

	/**
	 * change the  Company User status
	 * @return null
	 */
	public String activiateDe()
	{
		try
		{ 
			User user = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(user == null){
				FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL()+CommonProperties.getContextPath()+"login.xhtml");
			}
			FacesUtils facesUtils = new FacesUtils();
			int status = Integer.valueOf(facesUtils.getRequestParameterMap("status"));
			for (Map.Entry<Long, Boolean> entry : checkedDe.entrySet()) {
				if(entry.getValue()){
					DataEntry dataEntryObj = deService.getDataEntryById(entry.getKey());
					if(status == 0)
						dataEntryObj.setIsqualityCheck(false);
					else
						dataEntryObj.setIsqualityCheck(true);
					deService.updateDataEntry(dataEntryObj);
				}
			}
		}		
		catch(Exception e)
		{
			System.out.println("deBean:activiateDE"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * change the  delete DeJob 
	 * @return null
	 */
	public String deleteDeJob()
	{
		try
		{ 
			User user = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(user == null){
				FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL()+CommonProperties.getContextPath()+"login.xhtml");
			}
			for (Map.Entry<Long, Boolean> entry : checkedDe.entrySet()) {
				if(entry.getValue()){
					DeJob deJobObj = deService.getDeJobById(entry.getKey());
					deJobObj.setIsDeleted(true);
					deService.updateDeJob(deJobObj);
				}
			}
			checkedDe = new HashMap<Long, Boolean>();
			messageService.messageInformation(null, "Job has been Deleted successfully.");
		}		
		catch(Exception e)
		{
			System.out.println("deBean:deleteDeJob"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * change the  delete DeJob 
	 * @return null
	 */
	public String deleteQcJob()
	{
		try
		{ 
			User user = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(user == null){
				FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL()+CommonProperties.getContextPath()+"login.xhtml");
			}
			for (Map.Entry<Long, Boolean> entry : checkedQc.entrySet()) {
				if(entry.getValue()){
					DataEntry qcJobObj = deService.getDataEntryById(entry.getKey());
					qcJobObj.setIsDeleted(true);
					deService.updateDataEntry(qcJobObj);
				}
			}
			checkedQc = new HashMap<Long, Boolean>();
			messageService.messageInformation(null, "Job has been Deleted successfully.");
		}		
		catch(Exception e)
		{
			System.out.println("deBean:deleteQcJob"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * clear all fields
	 */
	private void clear() {
		this.companyName ="";
		this.address ="";
		this.city ="";
		this.state = "";
		this.country ="";
		this.pincode = "";
		this.adHeadLine = "";
		this.jobListing = "";
		this.advertiserType ="";
		this.adType ="";
		this.adOrientation = "";
		this.adSize = "";
		this.currency = 0;
		this.startCurrencyRange = "";
		this.endCurrencyRange ="";
		//this.ocrText = "";
	}

	/**
	 * redirect crop image 
	 * @return
	 */
	public String callCrop(){
		String val = facesUtils.getRequestParameterMap("img");
		int imageId = Integer.valueOf(val!=null?val:"0");
		if(imageId >0)
			sessionManager.setSessionAttributeInSession(SessionManager.CROPIMAGE, imageId);
		return CROP_IMAGE;
	}

	/**
	 * redirect crop image 
	 * @return
	 */
	public String callChildPage(){
		String val = facesUtils.getRequestParameterMap("img");
		int imageId = Integer.valueOf(val!=null?val:"0");
		if(imageId >0)
			sessionManager.setSessionAttributeInSession(SessionManager.CROPIMAGE, imageId);
		return CHILD_IMAGE;
	}

	/**
	 * Page load event for selected image
	 * @param event
	 */
	public void loadCropImageInfo(ComponentSystemEvent event)
	{
		try {			
			Object sessionObj = sessionManager.getSessionAttribute(SessionManager.CROPIMAGE);
			int imageId  = sessionObj!=null?((Integer)sessionObj).intValue():0;
			if(imageId >0)
			{
				parentImage = getParentImageService().getParentImageById(imageId);
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get Parent Image List 
	 * 
	 * @return List - ParentImage 
	 */
	public List<ParentImage> getParentImageList() {
		parentImageList = new ArrayList<ParentImage>();
		parentImageList.addAll(getParentImageService().getParentImage());
		Collections.reverse(parentImageList);
		return parentImageList;
	}

	/**
	 * Get Child Image List 
	 * 
	 * @return List - ChildImage 
	 */
	public List<ChildImage> getChildImageList() {
		childImageList = new ArrayList<ChildImage>();
		childImageList.addAll(getChildImageService().getChildImage());
		Collections.reverse(childImageList);
		return childImageList;
	}

	/**
	 * Get Child Image List 
	 * 
	 * @return List - ChildImage 
	 */
	public List<ChildImage> getChildImageListFromParent() {
		Object sessionObj = sessionManager.getSessionAttribute(SessionManager.CROPIMAGE);
		int imageId  = sessionObj!=null?((Integer)sessionObj).intValue():0;
		if(imageId >0)
		{
			childImageList = new ArrayList<ChildImage>();
			childImageList.addAll(getChildImageService().getChildImagesByParent(imageId));
		}
		Collections.reverse(childImageList);
		return childImageList;
	}
	
	// Added by Shashank
	public List<ChildImage> getChildImageListCount(int id) {
		//Object sessionObj = sessionManager.getSessionAttribute(SessionManager.CROPIMAGE);
	
		int imageId  = id;
		if(imageId >0)
		{
			childImageList = new ArrayList<ChildImage>();
			childImageList.addAll(getChildImageService().getChildImagesByParent(imageId));
		}
		Collections.reverse(childImageList);
		return childImageList;
	}
	
	
	

	/**
	 * Image cropping using selected x1,y1 and x,y coordinates from parent image
	 */
	public String cropping()
	{
		currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
		String imageBaseURL = CommonProperties.getBaseURL()+CommonProperties.getImageContextPath();
		String imgId = facesUtils.getRequestParameterMap("imgId");
		try {
			if(cropX1 > 0){
				//read image to crop and getting file extension
				File f=new File(imageBasePath+CommonProperties.getParentImagePath()+imgId+"/"+parentImage.getImageName());
				String fileName=f.getName();
				String[] split = fileName.split("\\.");
				String ext = split[split.length - 1];
				//URL f= getClass().getResource(imageBasePath+CommonProperties.getParentImagePath()+imgId+"/"+parentImage.getImageName());
				//draw image based on cropped co ordinates
				Image orig = ImageIO.read(f);

				int transparency = ((BufferedImage) orig).getColorModel().getTransparency();
				BufferedImage bi = new BufferedImage(getCropWidth(), getCropHeight(), transparency);
				bi.createGraphics().drawImage(orig, 0, 0, getCropWidth(), getCropHeight(), getCropX1(), getCropY1(),getCropX2(), getCropY2(), null);

				//save cropped area as new image file
				new File(imageBasePath+CommonProperties.getTempPath()+currentUser.getId()).mkdirs();
				int random = (int)(double)((Math.random()*(double)1000));
				File newFile=new File(imageBasePath+CommonProperties.getTempPath()+currentUser.getId()+"/crp_"+random+"_"+parentImage.getImageName());
				try{
					ImageIO.write(bi,"jpg",newFile );
					//set image to page
					croppedImageName = currentUser.getId()+"/crp_"+random+"_"+parentImage.getImageName();
					RequestContext.getCurrentInstance().execute("PF('dlg1').show()");
				} catch (Exception e) {
					e.printStackTrace();
				}


			}else{
				messageService.messageFatal(null, "Please Select Image.");	
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**Added by Shashank
	 *  takeFullImageAsChild
	 */
	public String takeFullImageAsChild()
	{
		currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
		String imageBaseURL = CommonProperties.getBaseURL()+CommonProperties.getImageContextPath();
		String imgId = facesUtils.getRequestParameterMap("imgId");
		try {
				//read image to crop and getting file extension
				File f=new File(imageBasePath+CommonProperties.getParentImagePath()+imgId+"/"+parentImage.getImageName());
				String fileName=f.getName();
				String[] split = fileName.split("\\.");
				String ext = split[split.length - 1];
				//URL f= getClass().getResource(imageBasePath+CommonProperties.getParentImagePath()+imgId+"/"+parentImage.getImageName());
				//draw image based on cropped co ordinates

				BufferedImage bi = ImageIO.read(f);

				//save cropped area as new image file
				new File(imageBasePath+CommonProperties.getTempPath()+currentUser.getId()).mkdirs();
				int random = (int)(double)((Math.random()*(double)1000));
				File newFile=new File(imageBasePath+CommonProperties.getTempPath()+currentUser.getId()+"/crp_"+random+"_"+parentImage.getImageName());
				try{
					ImageIO.write(bi,"jpg",newFile );
					//set image to page
					croppedImageName = currentUser.getId()+"/crp_"+random+"_"+parentImage.getImageName();
					RequestContext.getCurrentInstance().execute("PF('dlg1').show()");
				} catch (Exception e) {
					e.printStackTrace();
				}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * save cropped image from temp path
	 */
	public String saveCroppedImage(){
		currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
		String sourcePath = imageBasePath+CommonProperties.getTempPath()+croppedImageName;
		String targetPath = imageBasePath+CommonProperties.getChildImagePath()+parentImage.getId();
		try{
			if(parentImage!=null){
				File cropFile = new File(sourcePath);
				if(cropFile.isFile()){
					int random = (int)(double)((Math.random()*(double)1000));
					String filename = "crp_"+random+"_"+parentImage.getImageName();
					ChildImage childImage = new ChildImage();
					childImage.setParentImage(parentImage);
					childImage.setImageName(filename);
					childImage.setCreated_by(currentUser);
					childImage.setCreatedOn(new Date());
					BufferedImage image = ImageIO.read(cropFile);
					int height = image.getHeight();
					int width = image.getWidth();				
					String heightCM=decimalFormat.format(((double)height/96)*2.54*0.9575);
					String widthCM=decimalFormat.format(((double)width/96)*2.54*0.9575);					
					childImage.setImageHeight(String.valueOf(heightCM));
					childImage.setImageWidth(String.valueOf(widthCM));
					Long imgId = getChildImageService().addChildImage(childImage);
					targetPath = targetPath+"/"+imgId;
					new File(targetPath).mkdirs();
					IoUtils.copyImages(sourcePath,targetPath,filename);
					new File(sourcePath).delete();
					messageService.messageInformation(null, "Image cropped Successfully.");	
					return RETURN_CHILD_IMAGE;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * delete parent image from db and path
	 */
	public void deleteParentImage(){
		currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
		String imgId = facesUtils.getRequestParameterMap("imgId");

		if(imgId!=null){
			ParentImage parentImage = getParentImageService().getParentImageById(Long.valueOf(imgId));
			DeJob deJob = 	deService.getDeJobByParentImageId(parentImage.getId());
			List<ChildImage> child = getChildImageService().getChildImagesByParent(Long.valueOf(imgId));
			if(child ==null || child.size() == 0 ){
				if(deJob==null){
					/*File image=new File(imageBasePath+CommonProperties.getParentImagePath()+imgId+"/"+parentImage.getImageName());
					boolean flag = image.delete();*/
					if(parentImage!=null){
						DeletedImage deletedImage=new DeletedImage();
						deletedImage.setImageId(parentImage.getId());
						deletedImage.setImageName(parentImage.getImageName());
						deletedImage.setDeleted_by(currentUser.getId());
						deletedImage.setDeleted_On(new Date());
						deletedImage.setIsChild(false);
						Long flag=getDeletedImageService().addDeletedImage(deletedImage);
						if(flag>0){
							parentImage.setId(Long.valueOf(imgId));
							getParentImageService().deleteParentImage(parentImage);
							messageService.messageInformation(null, "Image deleted Successfully.");
						}
						else{
							messageService.messageFatal(null, "Image can not be deleted.");
						}
					}
				} else {
					messageService.messageFatal(null, "Image can not be deleted. Image added in Publication");
				}
			}else{
				messageService.messageFatal(null, "Image can not be deleted. Image have a Child image");
			}
		}
	}

	/**
	 * delete child image from db and path
	 */
	public void deleteChildImage(){
		currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
		String imgId = facesUtils.getRequestParameterMap("imgId");
		if(imgId!=null){
			ChildImage childImage = getChildImageService().getChildImageById(Long.valueOf(imgId));
			/*File image=new File(imageBasePath+CommonProperties.getChildImagePath()+childImage.getParentImage().getId()+"/"+imgId+"/"+childImage.getImageName());
			boolean flag = image.delete();*/
			if(childImage!=null){
				DataEntry dataEntry=deService.getDataEntryByChildImageId(childImage.getId());
				if(dataEntry==null){
					DeletedImage deletedImage=new DeletedImage();
					deletedImage.setImageId(childImage.getId());
					deletedImage.setImageName(childImage.getImageName());
					deletedImage.setDeleted_by(currentUser.getId());
					deletedImage.setDeleted_On(new Date());
					deletedImage.setIsChild(true);
					Long flag=getDeletedImageService().addDeletedImage(deletedImage);
					if(flag>0){
						getChildImageService().deleteChildImage(childImage);
						messageService.messageInformation(null, "Image deleted Successfully.");
					}
					else{
						messageService.messageFatal(null, "Image can not be deleted.");
					}
				} else {
					messageService.messageFatal(null, "Image can not be deleted. Image added in Publication.");
				}
			}
		}
	}

	/**
	 * save cropped image from temp path
	 * @throws IOException 
	 */
	public String saveParentImage() throws IOException{
		currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
		String sourcePath = imageBasePath+CommonProperties.getParentImageTempPath()+"/"+currentUser.getId();
		String fileNameWithExt ="";
		String extension ="";
		boolean flag=false;
		if(sourcePath!=null){
			File cropFile = new File(sourcePath);
			if(cropFile.isDirectory()){
				File [] files = cropFile.listFiles();
				if(files.length > 0){
					for (int i = 0; i <files.length; i++){
						String sourcePathImage = imageBasePath+CommonProperties.getParentImageTempPath()+"/"+currentUser.getId();
						String targetPathImage = imageBasePath+CommonProperties.getParentImagePath();
						if(sourcePathImage != null){
							ParentImage parentImage = new ParentImage();
							String filename = files[i].getName();
							String[] split = filename.split("\\.");
							extension = split[split.length - 1];
							String file=split[split.length - 2];
							file=file.replaceAll(" ", "_");
							filename=file+"."+extension;
							if(extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif")){
								parentImage.setImageName(filename);
								parentImage.setCreatedBy(currentUser);
								parentImage.setCreatedOn(new Date());
								BufferedImage image = ImageIO.read(files[i]);
								int height = image.getHeight();
								int width = image.getWidth();
								String heightCM=decimalFormat.format(((double)height/96)*2.54*0.9575);
								String widthCM=decimalFormat.format(((double)width/96)*2.54*0.9575);								
								parentImage.setImageHeight(String.valueOf(heightCM));
								parentImage.setImageWidth(String.valueOf(widthCM));
								Long imgId = getParentImageService().addParentImage(parentImage);
								sourcePathImage = sourcePath+"/"+parentImage.getImageName();
								targetPathImage = targetPathImage+"/"+imgId;
								new File(targetPathImage).mkdirs();
								IoUtils.copyImages(sourcePathImage,targetPathImage,parentImage.getImageName());
							}else{
								flag =true;
								// Added by Shashank
								messageService.messageFatal(null, " Please select  proper image  format.");
								fileNameWithExt = fileNameWithExt + filename;
								return null;
							}


						}
					}
				}
				else{
					messageService.messageFatal(null, " Please select at least one image");
					return null;
				}
				//messageService.messageInformation(null, "Parent Image has been Saved Successfully.");
				if(files.length >0){
					for (int i = 0; i <files.length; i++){
						files[i].delete();
					}
				}
			}
		}
		String menuIdStr="6";
		sessionManager.setUserInSession(SessionManager.BRSMENU,menuIdStr);
		//return GALLERY;
		if(flag == true){
			msgFormat =0;
			msgLabel = fileNameWithExt + " this format of files not Saved";

		}
		else{
			msgFormat=1;
			msgLabel = "Parent Image has been Saved Successfully";

		}
		return "/pages/de/gallery.xhtml?msgLabel="+this.msgLabel+"&msgFormat="+this.msgFormat+"&faces-redirect=true";
	}

	public void loadMessage(ComponentSystemEvent event)
	{
		if(com.obs.brs.utils.StringUtility.isNotEmpty(this.msgLabel) && this.msgFormat==0)
		{
			FacesContext.getCurrentInstance().addMessage("deo", new FacesMessage(FacesMessage.SEVERITY_FATAL,this.msgLabel,this.msgLabel));
			this.msgLabel="";
		}
		if(com.obs.brs.utils.StringUtility.isNotEmpty(this.msgLabel) && this.msgFormat==1)
		{
			FacesContext.getCurrentInstance().addMessage("deo", new FacesMessage(FacesMessage.SEVERITY_INFO,this.msgLabel,this.msgLabel));
			this.msgLabel="";
		}
	}
	/** 
	 *  edit publication if id > 0
	 */
	public String updateAndExitPublicationByParentImage() 
	{
		try
		{
			String dateField ="";
			User currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			Publication  publication=null;
			Publication  sectionTitle=null;
			if(currentUser != null)
			{

				FacesUtils facesUtils = new FacesUtils();
				String val = facesUtils.getRequestParameterMap("parentImg");
				long parentImg = Long.valueOf(val!=null?val:"0");
				ParentImage parentImage = getParentImageService().getParentImageById(parentImg);
				// Removed by Shashank
				if( ((this.publicationTitle == null ||  this.publicationTitle.isEmpty())) 
						|| ((this.section == null || this.section.isEmpty()) && (this.sectionNextValue == null || this.sectionNextValue.isEmpty())) 
						|| this.page==null || this.page.isEmpty()
						|| (this.issueDay == 0 && this.issueDayNext ==0 &&  this.issueDayNextThird ==0 && this.issueDayNextFour ==0 )
						|| (this.issueMonth == 0 && this.issueMonthNext ==0)
						|| this.issueYear  == 0){
					messageService.messageFatal(null, "You must fill all required feilds");
				} else {
					if(this.publicationTitle != null && !this.publicationTitle.isEmpty()){
						publication = userService.getPublicationById(Integer.valueOf(this.publicationTitle));
					}else if(this.publicationTitleNextValue != null && !this.publicationTitleNextValue.isEmpty()){
						publication = userService.getPublicationById(Integer.valueOf(this.publicationTitleNextValue));
					}
					if(this.section != null && !this.section.isEmpty()){
						sectionTitle = userService.getPublicationById(Integer.valueOf(this.section));
					}else if(this.sectionNextValue != null && !this.sectionNextValue.isEmpty()){
						sectionTitle = userService.getPublicationById(Integer.valueOf(this.sectionNextValue));
					}
					if(this.issueDay <= 8 && this.issueDayNext ==0 &&  this.issueDayNextThird ==0 && this.issueDayNextFour ==0 ){
						this.issueDayStr =String.valueOf(this.issueDay);
					}
					else if(this.issueDayNext >8 && this.issueDayNext <= 16){
						this.issueDayStr =String.valueOf(this.issueDayNext);
					}
					else if(this.issueDayNextThird > 16 && this.issueDayNextThird <=24 ){
						this.issueDayStr =String.valueOf(this.issueDayNextThird);
					}
					else if(this.issueDayNextFour >24){
						this.issueDayStr =String.valueOf(this.issueDayNextFour);
					}
					if(this.issueMonth <=6 && this.issueMonthNext ==0){
						this.issueMonthStr = String.valueOf(this.issueMonth);
					}else if(this.issueMonthNext >6){
						this.issueMonthStr = String.valueOf(this.issueMonthNext);
					}
					if(this.issueYear > 0){
						this.issueYearStr = String.valueOf(this.issueYear);	
					}
					if(this.issueYearStr != null && this.issueMonthStr != null && this.issueDayStr != null ){
						dateField=this.issueYearStr+"-"+this.issueMonthStr+"-"+this.issueDayStr;
					}
					if(parentImage != null && publication != null && sectionTitle != null && dateField != null){
						parentImage.setPublicationTitle(publication);
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						Date issueDate = (Date)formatter.parse(dateField);
						parentImage.setIssueDate(issueDate);
						parentImage.setSection(sectionTitle);
						if(this.sectionother != null ){
							parentImage.setSectionother(this.sectionother);
						}
						if( this.sectionspecialRegional != null){
							parentImage.setSectionspecialRegional(this.sectionspecialRegional);
						}
						if(this.sectionspecialTopic != null){
							parentImage.setSectionspecialTopic(this.sectionspecialTopic);
						}
						parentImage.setPage(this.page);
						parentImageService.updateParentImage(parentImage);
						DeJob deJob = 	deService.getDeJobByParentImageId(parentImage.getId());
						if(deJob != null){
							deJob.setParentImage(parentImage);
							deJob.setCreated_by(currentUser);
							deJob.setCreated_On(new Date());
							deJob.setIsActive(true);
							deJob.setIsDeleted(false);
							deService.updateDeJob(deJob);
						}
						else{
							deJob = new  DeJob();
							deJob.setParentImage(parentImage);
							deJob.setCreated_by(currentUser);
							deJob.setIsActive(true);
							deJob.setCreated_On(new Date());
							deJob.setIsDeleted(false);
							deService.addDeJob(deJob);
						}
						messageService.messageInformation(null, " Publication has been Updated  Successfully.");
						//return "/pages/de/gallery.xhtml?msgLabel="+this.msgLabel+"&faces-redirect=true";
					}
				}
				this.publicationTitle ="";
				this.publicationTitleNextValue="";
				this.section="";
				this.sectionNextValue="";
				this.page="";
				this.issueDay =0;
				this.issueDayNext =0;
				this.issueDayNextThird =0;
				this.issueDayNextFour =0;
				this.issueMonth=0;
				this.issueMonthNext=0;
				this.issueYear=0;
				return null;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public void loadAllPublicationInfo(ComponentSystemEvent event){
		try
		{
			if (!FacesContext.getCurrentInstance().isPostback()) 
			{
				User currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
				FacesUtils facesUtils = new FacesUtils();
				String val = facesUtils.getRequestParameterMap("parentImg");
				this.id = Integer.valueOf(val!=null?val:"0");
				if(currentUser != null)
				{
					if(this.id>0){
						ParentImage parentImage = getParentImageService().getParentImageById(this.id);
						if(parentImage != null && parentImage.getPublicationTitle() != null){
							this.parentImageName = parentImage.getImageName();
							this.publicationTitle =String.valueOf(parentImage.getPublicationTitle().getId());
							this.publicationTitleNextValue = String.valueOf(parentImage.getPublicationTitle().getId());
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							String DateStr  = formatter.format(parentImage.getIssueDate());
							String[] split = DateStr.split("-");
							int issueDays = Integer.valueOf(split[2]);
							if(issueDays <= 8){
								this.issueDay = Integer.valueOf(split[2]);
							}
							else if(issueDays > 8 && issueDays <= 16){
								this.issueDayNext = Integer.valueOf(split[2]);
							}
							else if(issueDays >16 && issueDays <= 24){
								this.issueDayNextThird = Integer.valueOf(split[2]); 
							}
							else if(issueDays > 24){
								this.issueDayNextFour = Integer.valueOf(split[2]); 
							}
							if(Integer.valueOf(split[1]) <= 6){
								this.issueMonth = Integer.valueOf(split[1]);
							}else{
								this. issueMonthNext =Integer.valueOf(split[1]);
							}
							this.issueYear =Integer.valueOf(split[0]);
							this.section = String.valueOf(parentImage.getSection().getId());
							this.sectionNextValue = String.valueOf(parentImage.getSection().getId());
							this.page = parentImage.getPage();
						}
					}
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public String callPublication(){
		try
		{
			User currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);	
			FacesUtils facesUtils = new FacesUtils();
			String val = facesUtils.getRequestParameterMap("parentImg");
			this.id = Integer.valueOf(val!=null?val:"0");
			String jobIdval = facesUtils.getRequestParameterMap("jobId");
			this.deJobid = Integer.valueOf(jobIdval!=null?jobIdval:"0");
			if(currentUser != null)
			{
				if(this.id>0){
					ParentImage parentImage = getParentImageService().getParentImageById(this.id);
					this.parentImageName = parentImage.getImageName();
					if(parentImage != null && parentImage.getPublicationTitle() != null){
						if(parentImage.getPublicationTitle().getId() >0){
							this.publicationTitle =String.valueOf(parentImage.getPublicationTitle().getId());
							this.publicationTitleNextValue =String.valueOf(parentImage.getPublicationTitle().getId());
						}else{
							this.publicationTitle="";
							this.publicationTitleNextValue="";
						}
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						if(parentImage.getIssueDate() != null){
							String DateStr  = formatter.format(parentImage.getIssueDate());
							String[] split = DateStr.split("-");
							int issueDays = Integer.valueOf(split[2]);
							if(issueDays <= 8){
								this.issueDay = Integer.valueOf(split[2]);
							}
							else if(issueDays > 8 && issueDays <= 16){
								this.issueDayNext = Integer.valueOf(split[2]);
							}else if(issueDays > 16 && issueDays <= 24){
								this.issueDayNextThird = Integer.valueOf(split[2]); 
							}
							else{
								this.issueDayNextFour = Integer.valueOf(split[2]); 
							}
							if(Integer.valueOf(split[1]) <= 6){
								this.issueMonth = Integer.valueOf(split[1]);
							}else{
								this. issueMonthNext =Integer.valueOf(split[1]);
							}
							this.issueYear =Integer.valueOf(split[0]);
						}else{
							this.issueDay=0;
							this.issueDayNext=0;
							this.issueDayNextThird=0;
							this.issueDayNextFour=0;
							this.issueMonth=0;
							this. issueMonthNext=0;
							this.issueYear=0;


						}
						if(parentImage.getSection().getId()>0){
							this.section = String.valueOf(parentImage.getSection().getId());
							this.sectionNextValue=String.valueOf(parentImage.getSection().getId());
							if(parentImage.getSectionother() != null && !parentImage.getSectionother().isEmpty()){
								this.sectionother=String.valueOf(parentImage.getSectionother());
							}
							if(parentImage.getSectionspecialRegional() != null && !parentImage.getSectionspecialRegional().isEmpty()){
								this.sectionspecialRegional=String.valueOf(parentImage.getSectionspecialRegional());
							}
							if(parentImage.getSectionspecialTopic() != null && !parentImage.getSectionspecialTopic().isEmpty()){
								this.sectionspecialTopic=String.valueOf(parentImage.getSectionspecialTopic());
							}
						}else{
							this.section ="";
							this.sectionNextValue="";
							this.sectionother="";
							this.sectionspecialRegional="";
							this.sectionspecialTopic="";
						}
						if(parentImage.getPage() != null && StringUtility.isNotEmpty(parentImage.getPage())){
							this.page = parentImage.getPage();
						}else{
							this.page ="";
						}
					}else{
						this.publicationTitle="";
						this.publicationTitleNextValue="";
						this.issueDay=0;
						this.issueDayNext=0;
						this.issueDayNextThird=0;
						this.issueDayNextFour=0;
						this.issueMonth=0;
						this. issueMonthNext=0;
						this.issueYear=0;
						this.section ="";
						this.sectionNextValue="";
						this.sectionother="";
						this.sectionspecialRegional="";
						this.sectionspecialTopic="";
						this.page ="";
					}
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get CompanyUser List
	 * 
	 * @return List - CompanyUser List
	 */
	public List<DeJob> getDeJobListBySeachCriteria() {
		deJobList = new ArrayList<DeJob>();
		String DATE_FORMAT = "yyyy-MM-dd";
		DateFormat dateFormat = new SimpleDateFormat (DATE_FORMAT);	
		if(this.searchValue != null && !this.searchValue.isEmpty()){
			this.searchText = this.searchValue;
		}else if(this.issueDateSerach != null){
			this.searchText = dateFormat.format(this.issueDateSerach);
		}

		if(this.searchValue.isEmpty() && this.issueDateSerach == null){
			deJobList.addAll(getDeService().getDeJobBySeachCriteria());
		}else{
			deJobList.addAll(getDeService().getDeJobBySeachCriteria(this.searchText));	
		}
		Collections.reverse(deJobList);
		return deJobList;
	}
	public List<DataEntry> getQcJobListBySeachCriteria() {
		User user = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
		this.publicationId = (String) sessionManager.getSessionAttribute(SessionManager.PUBLICATIONID);
		this.jobStatusGot = (String) sessionManager.getSessionAttribute(SessionManager.JOBSTATUS);
		if(this.jobStatusGot != null && this.jobStatusGot != ""){
			this.jobStatus= Integer.parseInt(this.jobStatusGot);
		}
		String DATE_FORMAT = "yyyy-MM-dd";
		DateFormat dateFormat = new SimpleDateFormat (DATE_FORMAT);	
		dataEntryuserList = new ArrayList<DataEntry>();
		this.issueDatePubSearch = (String) sessionManager.getSessionAttribute(sessionManager.ISSUEDATE);
		if(this.issueDatePubSearch == null && this.publicationId != ""){
			dataEntryuserList.addAll(getDeService().geAllQcJob());
			return dataEntryuserList;
		}
		if(this.issueDatePubSearch != null){
			this.issueDatePubSearch = this.issueDatePubSearch;
		}else{
			this.issueDatePubSearch = "";
		}
		if(this.jobStatus == 0 && this.searchValue.equals("")){
			dataEntryuserList.addAll(getDeService().geQcJobBySeach(this.publicationId,this.issueDatePubSearch));
		}else if(this.jobStatus >= 0 ){
				dataEntryuserList.addAll(getDeService().geQcJobBySeachCriteria(this.jobStatus,this.searchValue,this.publicationId,this.issueDatePubSearch));
		}
		Collections.reverse(dataEntryuserList);
		return dataEntryuserList;
	}
	public List getQcJobListBySeachCriteriaNew(){
		List qcjobByJournal = new ArrayList();
		qcjobByJournal = getDeService().getDeJobByQC();
		return qcjobByJournal;
		
	}
	
	public String loadQcJournalView(){
		this.publicationId = facesUtils.getRequestParameterMap("publicationId");
		sessionManager.setUserInSession(SessionManager.PUBLICATIONID,this.publicationId);
		this.jobStatusGot = facesUtils.getRequestParameterMap("status");
		sessionManager.setUserInSession(SessionManager.JOBSTATUS, this.jobStatusGot);
		return QCJOBBYJOURNAL;
	}
	
	public List getIssueDateByPublication(){
		List issueDateByPublication = new ArrayList();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		this.publicationId = (String) sessionManager.getSessionAttribute(SessionManager.PUBLICATIONID);
		issueDateByPublication = getDeService().getIssueDateByPublication(this.publicationId);
		List issueDateString  = new ArrayList();
		for(int i=0;i<issueDateByPublication.size();i++){
			//Date[] obj=(Date[])issueDateByPublication.get(i);
			String DateStr = formatter.format(issueDateByPublication.get(i));
			issueDateString.add(DateStr);
		}
		return issueDateString;
	}

	public void sendIssueDate(ValueChangeEvent event)	
	{ 
		this.issueDatePubSearch =((String) event.getNewValue());
		if(this.issueDatePubSearch.equals("all"))
		{
			sessionManager.setUserInSession(SessionManager.ISSUEDATE, "");
		}else{
			sessionManager.setUserInSession(SessionManager.ISSUEDATE, this.issueDatePubSearch);
		}
		
	}
	public void changeNextVal()
	{
		FacesUtils facesUtils = new FacesUtils();
		long baseId = Long.valueOf(facesUtils.getRequestParameterMap("baseId"));
		long jobid = Long.valueOf(facesUtils.getRequestParameterMap("jobId"));
		DataEntry childImage=deService.getNextDataEntry(baseId,jobid);
		if(childImage!=null){
			/*childImageList=childImageService.getChildImagesByParent(childImage.getParentImage().getId());
			Collections.reverse(childImageList);*/
			childImagesDataList=deService.getImagesByJobid(jobid);
			//Collections.reverse(childImagesDataList);
		}
		if(childImage!=null && childImage.getChildImage() != null){
			this.checkPreVal=false;
			this.deDataId=childImage.getId();
			this.childImageId=childImage.getChildImage().getId();
			this.parentImageName=childImage.getParentImage().getImageName();
			this.childImageName = childImage.getChildImage().getImageName();
			setAllAdDetails(childImage);
			if(this.ocrText==null || this.ocrText.isEmpty()){
				try {
					System.out.println("debean:changeNextVal:: loading this path: "+(imageBasePath+CommonProperties.getChildImagePath()+childImage.getParentImage().getId()+"/"+this.childImageId+"/"+this.childImageName));
					File image=new File(imageBasePath+CommonProperties.getChildImagePath()+childImage.getParentImage().getId()+"/"+this.childImageId+"/"+this.childImageName);
					ImageIO.scanForPlugins();
					String result = new Ocr().doOCR(image);
					System.out.println("debean:changeNextVal:: Data from Image:"+result);
					if(result != null){
						this.ocrText = result;
					}
				} catch (Exception e) {
					System.err.println("deBean:loadDeInfo"+e.getMessage());
					e.printStackTrace();
				}
			}
			//setAllAdDetails(childImages);
			if(childImage.getDeCompany() != null){
				setAllDeCompanyDetails(childImage.getDeCompany());
			}
			else{
				clear();
			}
		}
		if(childImage==null){
			this.checkVal=true;
		} else if(this.childImageId==childImagesDataList.get(childImagesDataList.size()-1).getChildImage().getId()){
			this.checkVal=true;
		} else{
			this.checkVal=false;
		}

	}

	public void changePreVal()
	{
		FacesUtils facesUtils = new FacesUtils();
		long baseId = Long.valueOf(facesUtils.getRequestParameterMap("baseId"));
		long jobid = Long.valueOf(facesUtils.getRequestParameterMap("jobId"));
		DataEntry childImages=deService.getPrevDataEntry(baseId,jobid);
		if(childImages!=null){
			//childImageList=childImageService.getChildImagesByParent(childImages.getParentImage().getId());
			//Collections.reverse(childImageList);
			childImagesDataList=deService.getImagesByJobid(jobid);
		}
		if(childImages!=null )
		{
			this.checkVal=false;
			if(childImages.getChildImage()== null )  // for Parent image
			{
				this.childImageId=childImages.getChildImage()!=null?childImages.getChildImage().getId():0L;
				this.deDataId=childImages.getId();
				this.parentImageName=childImages.getParentImage().getImageName();
				setAllAdDetails(childImages);
				if(this.ocrText==null || this.ocrText.isEmpty()){
					try {
						System.out.println("debean:changePreVal:: loading this path: "+(imageBasePath+CommonProperties.getChildImagePath()+childImage.getParentImage().getId()+"/"+this.childImageId+"/"+this.childImageName));
						File image=new File(imageBasePath+CommonProperties.getParentImagePath()+childImages.getParentImage().getId()+"/"+this.parentImageName);
						ImageIO.scanForPlugins();
						String result = new Ocr().doOCR(image);
						System.out.println("debean:changePreVal:: Data from Image:"+result);
						if(result != null){
							this.ocrText = result;
						}
					} catch (Exception e) {
						System.err.println("deBean:loadDeInfo"+e.getMessage());
						e.printStackTrace();
					}
				}
				//setAllAdDetails(childImages);
				if(childImages.getDeCompany() != null){
					setAllDeCompanyDetails(childImages.getDeCompany());
				}
				else{
					clear();
				}
			}
			else 
			{
				this.childImageId=childImages.getChildImage().getId();
				this.deDataId=childImages.getId();
				this.parentImageName=childImages.getParentImage().getImageName();
				this.childImageName = childImages.getChildImage().getImageName();
				setAllAdDetails(childImages);
				if(this.ocrText==null || this.ocrText.isEmpty()){
					try {
						File image=new File(imageBasePath+CommonProperties.getChildImagePath()+childImages.getParentImage().getId()+"/"+this.childImageId+"/"+this.childImageName);
						ImageIO.scanForPlugins();
						String result = new Ocr().doOCR(image);
						if(result != null){
							this.ocrText = result;
						}
					} catch (Exception e) {
						System.out.println("deBean:loadDeInfo"+e.getMessage());
						e.printStackTrace();
					}
				}
				//setAllAdDetails(childImages);
				if(childImages.getDeCompany() != null){
					setAllDeCompanyDetails(childImages.getDeCompany());
				}
				else{
					clear();
				}
			}
			if(childImages.getChildImage()== null){
				this.checkPreVal=true;
			}
		}
		if(childImages==null){
			this.checkPreVal=true;
		} else if(this.childImageId==childImagesDataList.get(0).getChildImage().getId()){
			this.checkPreVal=true;
		} else{
			this.checkPreVal=false;
		}
	}
	/**
	 * 
	 * @param query
	 * @return
	 */
	public List<String> companyDetails(String query) {
		List<String> results = new ArrayList<String>();
		deCompanyList = deService.getDeCompany();
		if(deCompanyList != null &&  deCompanyList.size() > 0){
			for(int i = 0; deCompanyList.size()>i; i++) {
				DeCompany deCompany = deCompanyList.get(i);
				if(deCompany.getCompanyName().toLowerCase().startsWith(query)) {
					results.add(deCompany.getCompanyName());
				}
			}
		}
		return results;
	}
	/**
	 * 
	 * @param query
	 * @return
	 */
	public List<String> advertisertypeDetails(String query) {
		List<String> results = new ArrayList<String>();
		advertiserTypeList = userService.findAllAdvertiserType();
		if(advertiserTypeList != null &&  advertiserTypeList.size() > 0){
			for(int i = 0; advertiserTypeList.size()>i; i++) {
				Publication advertiserType = advertiserTypeList.get(i);
				if(advertiserType.getPublicationTitle().toLowerCase().startsWith(query)) {
					results.add(advertiserType.getPublicationTitle());
				}
			}
		}
		return results;
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public List<String> countryDetails(String query) {
		List<String> countryResult = new ArrayList<String>();
		countryResults = userService.getCountry();
		if(countryResults != null &&  countryResults.size() > 0){
			for(int i = 0; countryResults.size()>i; i++) {
				Country country = countryResults.get(i);
				if(country.getCountryName().toLowerCase().startsWith(query)) {
					countryResult.add(country.getCountryName());
				}
			}
		}
		return countryResult;
	}
	/**
	 * 
	 * @param query
	 * @return
	 */
	public List<String> stateDetails(String query) {
		List<String> stateResult = new ArrayList<String>();
		stateResults = userService.getStates();
		if(stateResults != null &&  stateResults.size() > 0){
			for(int i = 0; stateResults.size()>i; i++) {
				States state = stateResults.get(i);
				if(state.getStateName().toLowerCase().startsWith(query)) {
					stateResult.add(state.getStateName());
				}
			}
		}
		return stateResult;
	}

	/** list the noDays details
	 * 
	 * @return
	 */
	/*	public List<String> getNoOfDays()
	{
		try
		{
			int startNo = 1;
			int maxNo =15;
			for (int i=startNo;i<=maxNo;i++)
			{
				noDays.add(String.valueOf(i));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("deBean::getNoOfDays"+e.getMessage());
		}
		return noDays;
	}*/
	public List<SelectItem> getNoOfDays()
	{
		List noDays = new ArrayList();
		try
		{
			int startNo = 01;
			int maxNo =8;
			for (int i=startNo;i<=maxNo;i++){
				SelectItem sitem = new SelectItem(String.valueOf(i));
				noDays.add(sitem);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("deBean::getNoOfDays"+e.getMessage());
		}
		return noDays;
	}
	public List<SelectItem> getNoOfDaysNext()
	{
		List noDays = new ArrayList();
		try
		{
			int startNo = 9;
			int maxNo =16;
			for (int i=startNo;i<=maxNo;i++){
				SelectItem sitem = new SelectItem(String.valueOf(i));
				noDays.add(sitem);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("deBean::getNoOfDaysNext"+e.getMessage());
		}
		return noDays;
	} 
	public List<SelectItem> getNoOfDaysThird()
	{
		List noDays = new ArrayList();
		try
		{
			int startNo = 17;
			int maxNo =24;
			for (int i=startNo;i<=maxNo;i++){
				SelectItem sitem = new SelectItem(String.valueOf(i));
				noDays.add(sitem);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("deBean::getNoOfDaysThird"+e.getMessage());
		}
		return noDays;
	}
	public List<SelectItem> getNoOfDaysFour()
	{
		List noDays = new ArrayList();
		try
		{
			int startNo = 25;
			int maxNo =31;
			for (int i=startNo;i<=maxNo;i++){
				SelectItem sitem = new SelectItem(String.valueOf(i));
				noDays.add(sitem);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("deBean::getNoOfDaysThird"+e.getMessage());
		}
		return noDays;
	}
	/** list the noMonth details
	 * 
	 * @return
	 */
	public List<SelectItem> getNoOfMonth()
	{
		List noMonth = new ArrayList();
		try
		{
			int startNo = 01;
			int maxNo = 6;
			for (int i=startNo;i<=maxNo;i++)
			{
				SelectItem sitem = new SelectItem(String.valueOf(i));
				noMonth.add(sitem);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("deBean::getNoOfMonth"+e.getMessage());
		}
		return noMonth;
	}
	/** list the noMonth details
	 * 
	 * @return
	 */
	public List<SelectItem> getNoOfMonthNext()
	{
		List noMonth = new ArrayList();
		try
		{
			int startNo = 7;
			int maxNo = 12;
			for (int i=startNo;i<=maxNo;i++)
			{
				SelectItem sitem = new SelectItem(String.valueOf(i));
				noMonth.add(sitem);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("deBean::getNoOfMonth"+e.getMessage());
		}
		return noMonth;
	}
	/** list the noMonth details
	 * 
	 * @return
	 */
	public List<SelectItem> getNoOfYear()
	{
		List noYear = new ArrayList();
		try
		{
			int startNo = 2014;
			int maxNo = 2020;
			for (int i=startNo;i<=maxNo;i++)
			{
				SelectItem sitem = new SelectItem(String.valueOf(i));
				noYear.add(sitem);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("deFormbean::getNoOfYear"+e.getMessage());
		}
		return noYear;
	}
	/** Find the  configurationType status
	 * Active and Inactive
	 * @param event
	 */
	public void localeChangedPublicationValue(ValueChangeEvent event)	
	{ 
		publicationStatus = (Integer) event.getNewValue();
	}

	/**
	 * search Invoice Details by fromDate, toDate, Payment status, Payment Date
	 * @return
	 */
	public String searchDate() 
	{
		try {
			String DATE_FORMAT = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat (DATE_FORMAT);	
			if(issueDate != null){
				issueDateString = dateFormat.format(issueDate);
			}
			else{
				issueDateString="";
			}				
			return "/pages/de/manage_job.xhtml?issueDateString='"+issueDateString+"'&faces-redirect=true";
		} catch (Exception e) {
			System.out.println("deBean:searchDate"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	public void hideValueNext(AjaxBehaviorEvent event) {
		issueDayNext = 0;
		issueDayNextThird=0;
		issueDayNextFour=0;
	}
	public void hideValue(AjaxBehaviorEvent event) {
		issueDay = 0;
		issueDayNextThird=0;
		issueDayNextFour=0;
	}
	public void hideValueThird(AjaxBehaviorEvent event) {
		issueDay = 0;
		issueDayNext = 0;
		issueDayNextFour=0;
	}
	public void hideValueFour(AjaxBehaviorEvent event) {
		issueDay = 0;
		issueDayNext = 0;
		issueDayNextThird=0;
	}

	public void hidePublicationValue(AjaxBehaviorEvent event) {
		publicationTitleNextValue = "";

	}
	public void hidePublicationNextValue(AjaxBehaviorEvent event) {
		publicationTitle = "";
	}
	public void hideSectionValue(AjaxBehaviorEvent event) {
		sectionNextValue = "";
		section = (String) event.getComponent().getAttributes().get("value");
		//sectionother = section;
	}
	public void hideSectionNextValue(AjaxBehaviorEvent event) {
		section = "";
		sectionNextValue = (String) event.getComponent().getAttributes().get("value");
		//sectionspecialRegional = sectionNextValue;
	}

	public void hideMonthValueNext(AjaxBehaviorEvent event) {
		issueMonth = 0;
	}
	public void hideMonthValue(AjaxBehaviorEvent event) {
		issueMonthNext= 0;
	}
	public void hideAdCategory(AjaxBehaviorEvent event) {
		adCategorySec="";
	}
	public void hideAdCategorySec(AjaxBehaviorEvent event) {
		adCategory="";
	}
	public void hideAdvertiserType(AjaxBehaviorEvent event) {
		advertiserType="";
	}
	public void hideAdvertiserTypeSec(AjaxBehaviorEvent event) {
		advertiserTypeSec="";
	}

	public void checkVisible(){
		try {
			if (!FacesContext.getCurrentInstance().isPostback()) 
			{
				Object sessionObj = sessionManager.getSessionAttribute(SessionManager.EDITUSER);
				this.id   = sessionObj!=null?((Integer)sessionObj).intValue():0;
				if(this.id >0)
				{
					DeJob deJob =  deService.getDeJobById(this.id);
					if(deJob != null){
						//childImageList = childImageService.getChildImagesByParent(deJob.getParentImage().getId());
						childImagesDataList=deService.getImagesByJobid(deJob.getId());
					}
				}
				if(childImagesDataList!=null && !childImagesDataList.isEmpty()) {
					if(childImagesDataList.size()==1){
						this.checkVal=true;
						this.checkPreVal=true;
					}else{
						this.checkVal=false;
						this.checkPreVal=true;
					} 
				}
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public int getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(int isApproved) {
		this.isApproved = isApproved;
	}		
	public int getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(int jobStatus) {
		this.jobStatus = jobStatus;
	}
	public void localeJobChangedValue(ValueChangeEvent event)	
	{ 
		this.jobStatusGot =((String) event.getNewValue());
		if(!this.jobStatusGot.equals("0"))
		{
		sessionManager.setUserInSession(SessionManager.JOBSTATUS, this.jobStatusGot);
		}else{
			sessionManager.setUserInSession(SessionManager.JOBSTATUS, "0");
		}
		
	}

	
	
	/**
	 * Page load event for selected company user in admin page
	 * @param event
	 */
	public void loadQcInfo(ComponentSystemEvent event)
	{
		try {
			if (!FacesContext.getCurrentInstance().isPostback()) 
			{
				Object sessionObj = sessionManager.getSessionAttribute(SessionManager.EDITUSER);
				this.id   = sessionObj!=null?((Integer)sessionObj).intValue():0;
				if(this.id >0)
				{
					DataEntry dataEntry = 	deService.getDataEntryById(this.id);
					this.publicationTitle = dataEntry.getParentImage().getPublicationTitle().getPublicationTitle(); 
					this.issueDate =  dataEntry.getParentImage().getIssueDate();	
					this.parentImageId = dataEntry.getParentImage().getId();
					this.parentImageName = dataEntry.getParentImage().getImageName();
					if(dataEntry.getChildImage()==null){
						this.parentImageName = dataEntry.getParentImage().getImageName();
					} else {						
						this.childImageId=dataEntry.getChildImage()!=null?dataEntry.getChildImage().getId():0L;
						this.childImageName = dataEntry.getChildImage().getImageName();
					}
					this.jobStatus=dataEntry.getIsApproved();
					if(dataEntry != null){
						setAllAdDetails(dataEntry);
						Publication publication= this.userService.getPublicationBySearchValueAdvertisertype(this.searchValueAdvertisertype);
						if(publication!=null){
							this.advertiserName=publication.getPublicationTitle();
						}
					}
					if(dataEntry.getDeCompany() != null){
						setAllDeCompanyDetails(dataEntry.getDeCompany());
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** list the country details
	 * 
	 * @return
	 */
	public List<SelectItem> getAllPublicationFirstOff()
	{
		List<Publication> publicationList = new ArrayList<Publication>();
		List<SelectItem> publicationList1 = new ArrayList<SelectItem>();
		try {
			publicationList = userService.findAllPublication();
			Double  publicationListSize = (double) publicationList.size(); 
			firstOff = (int) Math.round(publicationListSize/2);
			//Shashank
			for(int i=0;i<publicationList.size();i++)
			{
				Publication publication = publicationList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(publication.getId());
				sItem.setLabel(publication.getPublicationTitle());
				publicationList1.add(sItem);
			}
			return publicationList1;

		} catch (Exception e) {
			System.out.println("deBean::getAllPublicationFirstOff"+e.getMessage());
			e.printStackTrace();
		}
		return null;		
	}

	/** list the country details
	 * 
	 * @return
	 */
	public List<SelectItem> getAllPublicationSecondOff()
	{
		List<Publication> publicationList = new ArrayList<Publication>();
		List<SelectItem> publicationList1 = new ArrayList<SelectItem>();
		try {
			publicationList = userService.findAllPublication();
			secondOff = publicationList.size();
			for(int i=(firstOff);i<secondOff;i++)
			{
				Publication publication = publicationList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(publication.getId());
				sItem.setLabel(publication.getPublicationTitle());
				publicationList1.add(sItem);
			}
			return publicationList1;

		} catch (Exception e) {
			System.out.println("deBean::getAllPublicationSecondOff"+e.getMessage());
			e.printStackTrace();
		}
		return null;		
	}

	/** list the section details
	 * 
	 * @return
	 */
	public List<SelectItem> getAllSectionFirstOff()
	{
		List<Publication> publicationList = new ArrayList<Publication>();
		List<SelectItem> sectionList = new ArrayList<SelectItem>();
		try {
			publicationList = userService.findAllSection();
			Double  sectionListSize = (double) publicationList.size(); 
			sectionFirstOff = (int) Math.round(sectionListSize/2);
			for(int i=0;i<sectionFirstOff;i++)
			{
				Publication publication = publicationList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(publication.getId());
				sItem.setLabel(publication.getPublicationTitle());
				sectionList.add(sItem);
			}
			return sectionList;

		} catch (Exception e) {
			System.out.println("deBean::AllSectionFirstOff"+e.getMessage());
			e.printStackTrace();
		}
		return null;		
	}
	/** list the section details
	 * 
	 * @return
	 */
	public List<SelectItem> getAllSectionSecondOff()
	{
		List<Publication> publicationList = new ArrayList<Publication>();
		List<SelectItem> sectionList = new ArrayList<SelectItem>();
		try {
			publicationList = userService.findAllSection();
			sectionSeconfOff = publicationList.size();
			for(int i=(sectionFirstOff);i<sectionSeconfOff;i++)
			{
				Publication publication = publicationList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(publication.getId());
				sItem.setLabel(publication.getPublicationTitle());
				sectionList.add(sItem);
			}
			return sectionList;

		} catch (Exception e) {
			System.out.println("deBean::AllSectionSecondOff"+e.getMessage());
			e.printStackTrace();
		}
		return null;		
	}

	/** list the section details
	 * 
	 * @return
	 */
	public List<SelectItem> getNoOfAdCategoryFirstOff()
	{
		List<Publication> advertiserTypeList = new ArrayList<Publication>();
		List<SelectItem> sectionList = new ArrayList<SelectItem>();
		try {
			advertiserTypeList = userService.findAllAdvertiserType();
			Double  advertiserTypeSize = (double) advertiserTypeList.size(); 
			advertiserTypeFirstOff = (int) Math.round(advertiserTypeSize/2);
			//for(int i=0;i<advertiserTypeFirstOff;i++)
			for(int i=0;i<advertiserTypeList.size();i++)
			{
				Publication publication = advertiserTypeList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(publication.getId());
				sItem.setLabel(publication.getPublicationTitle());
				sectionList.add(sItem);
			}
			return sectionList;

		} catch (Exception e) {
			System.out.println("deBean::getNoOfAdCategoryFirstOff"+e.getMessage());
			e.printStackTrace();
		}
		return null;		
	}


	/** list the section details
	 * 
	 * @return
	 */
	public List<SelectItem> getNoOfAdCategorySecondOff()
	{
		List<Publication> advertiserTypeList = new ArrayList<Publication>();
		List<SelectItem> sectionList = new ArrayList<SelectItem>();
		try {
			advertiserTypeList = userService.findAllAdvertiserType();
			advertiserTypeSecondOff= advertiserTypeList.size();
			for(int i=(advertiserTypeFirstOff);i<advertiserTypeSecondOff;i++)
			{
				Publication publication = advertiserTypeList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(publication.getId());
				sItem.setLabel(publication.getPublicationTitle());
				sectionList.add(sItem);
			}
			return sectionList;

		} catch (Exception e) {
			System.out.println("deBean::getNoOfAdCategorySecondOff"+e.getMessage());
			e.printStackTrace();
		}
		return null;		
	}
	/** list the section details
	 * 
	 * @return
	 */
	public List<SelectItem> getNoOfInstitutionTypeFirstOff()
	{
		List<Publication> institutionTypeList = new ArrayList<Publication>();
		List<SelectItem> sectionList = new ArrayList<SelectItem>();
		try {
			institutionTypeList = userService.findAllInstitutionType();
			Double  institutionTypeSize = (double) institutionTypeList.size(); 
			institutionTypeFirstOff = (int) Math.round(institutionTypeSize/2);
			//for(int i=0;i<institutionTypeFirstOff;i++)
			for(int i=0;i<institutionTypeList.size();i++)
			{
				Publication publication = institutionTypeList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(publication.getId());
				sItem.setLabel(publication.getPublicationTitle());
				sectionList.add(sItem);
			}
			return sectionList;

		} catch (Exception e) {
			System.out.println("deBean::getNoOfInstitutionType"+e.getMessage());
			e.printStackTrace();
		}
		return null;		
	}
	/** list the section details
	 * 
	 * @return
	 */
	public List<SelectItem> getNoOfInstitutionTypeSecondOff()
	{
		List<Publication> institutionTypeList = new ArrayList<Publication>();
		List<SelectItem> sectionList = new ArrayList<SelectItem>();
		try {
			institutionTypeList = userService.findAllInstitutionType();
			institutionTypeSecondOff = institutionTypeList.size();
			for(int i=(institutionTypeFirstOff);i<institutionTypeSecondOff;i++)
			{
				Publication publication = institutionTypeList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(publication.getId());
				sItem.setLabel(publication.getPublicationTitle());
				sectionList.add(sItem);
			}
			return sectionList;

		} catch (Exception e) {
			System.out.println("deBean::getNoOfInstitutionType"+e.getMessage());
			e.printStackTrace();
		}
		return null;		
	}
	/** list the section details
	 * 
	 * @return
	 */
	public List<SelectItem> getNoOfSearchValueAdvertisertype()
	{
		List<Publication> searchValueAdvertisertypeList = new ArrayList<Publication>();
		List<SelectItem> sectionList = new ArrayList<SelectItem>();
		try {
			searchValueAdvertisertypeList = userService.findAllSearchAdvertiserType();
			for(int i=0;i<searchValueAdvertisertypeList.size();i++)
			{
				Publication publication = searchValueAdvertisertypeList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(publication.getId());
				sItem.setLabel(publication.getPublicationTitle());
				sectionList.add(sItem);
			}
			return sectionList;

		} catch (Exception e) {
			System.out.println("deBean::getNoOfSearchValueAdvertisertype"+e.getMessage());
			e.printStackTrace();
		}
		return null;		
	}

	public void localChanged(AjaxBehaviorEvent event) {
		searchValueAdvertisertype= (String) event.getComponent().getAttributes().get("value");
		//System.out.println("searchValueAdvertisertype"+searchValueAdvertisertype);
	}
	public void localChangedAdSize(AjaxBehaviorEvent event) {
		adSize= (String) event.getComponent().getAttributes().get("value");
		//System.out.println("adSize"+adSize);
	}
	public void localChangedJobDensity(AjaxBehaviorEvent event) {
		jobDensity = (String) event.getComponent().getAttributes().get("value");
		//System.out.println("adSize"+adSize);
	}
	public void makeEmptyTempParentFolder(ComponentSystemEvent event){
		try{
			currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			String sourcePath = imageBasePath+CommonProperties.getParentImageTempPath()+"/"+currentUser.getId();
			if(sourcePath!=null){
				File tempParentFolder = new File(sourcePath);
				if(tempParentFolder.isDirectory()){
					File [] files = tempParentFolder.listFiles();
					if(files.length >0){
						for (int i = 0; i <files.length; i++){
							files[i].delete();
						}
					}
				}		
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * set pagination
	 * @param event
	 */
	public void scrollerAction(ActionEvent event)
	{
		ScrollerActionEvent scrollerEvent = (ScrollerActionEvent)event;
		FacesContext.getCurrentInstance().getExternalContext().log(
				"scrollerAction: facet: " + 
						scrollerEvent.getScrollerfacet() + 
						", pageindex: " + 
						scrollerEvent.getPageIndex());
	}
	public void localeChanged(ValueChangeEvent event)
	{
		String rowsPerPage = event.getNewValue().toString();
		this.sessionManager.setSessionAttributeInSession("rowsPerPage", rowsPerPage);
	}
	public int getChangeRowsPerPage()
	{
		return this.changeRowsPerPage;
	}

	public void setChangeRowsPerPage(int changeRowsPerPage)
	{
		this.changeRowsPerPage = changeRowsPerPage;
	}
	public int getRowsPerPage()
	{
		try
		{
			String rowsPerPage = (String)this.sessionManager.getSessionAttribute("rowsPerPage");
			if (!StringUtils.isEmpty(rowsPerPage)) {
				return Integer.valueOf(rowsPerPage).intValue();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return this.changeRowsPerPage;
	}

	public void changeImagesPerPage(AjaxBehaviorEvent event)	
	{ 
		Object result = event.getComponent().getAttributes().get("value");
		if(result!=null && StringUtils.isNotEmpty(String.valueOf(result))){
			imagePerPage = (Integer)result;
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEPERPAGE, result);
			imageOffset = 0;
		}
	}

	/**
	 * displaying  row per page
	 * default value is 10
	 * @return
	 */
	public Integer getImagesPerPage(){
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGE); 
			if(rowsPerPage!=null){
				imagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return imagePerPage;
	}
	/**
	 * 
	 * @param event
	 */
	public void localImageChanged(ValueChangeEvent event)	
	{ 
		String perPage = event.getNewValue().toString();
		sessionManager.setSessionAttributeInSession(SessionManager.IMAGEPERPAGE, perPage);
	}
	/**
	 * pagination method for next page 
	 */
	public void nxtPage(){
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGE); 
			//System.out.println("rowsPerPage"+rowsPerPage);
			if(rowsPerPage!=null){
				imagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
			imageOffset +=Integer.valueOf(imagePerPage);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * for previous page
	 */
	public void prevPage(){
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGE);
			if(rowsPerPage!=null){
				imagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
			if(imageOffset>1)
				imageOffset = imageOffset - Integer.valueOf(imagePerPage);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * for first page record
	 */
	public void firstPage(){
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGE);
			if(rowsPerPage!=null){
				imagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
			if(imageOffset>1)
				imageOffset = 0;

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * for first page record
	 */
	public void lastPage(){
		try{
			int listSize = 0;
			int pagecount;
			int remainder;
			FacesUtils facesUtils = new FacesUtils();
			String catagory=facesUtils.getRequestParameterMap("type");
			// parent image list
			if(catagory.equals("parent") && parentImageList !=null && parentImageList.size()>0 )
			{
				listSize=parentImageList.size();
			}
			// child image list
			if(catagory.equals("child") && childImageList !=null && childImageList.size()>0 )
			{
				listSize=childImageList.size();
			}

			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGE); 
			if(rowsPerPage!=null)
			{
				imagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
			pagecount=(listSize/Integer.valueOf(imagePerPage));
			remainder=(listSize%Integer.valueOf(imagePerPage));
			if(pagecount>0 && remainder >0)
			{
				imageOffset =listSize-(listSize%Integer.valueOf(imagePerPage));
			}
			else
			{
				imageOffset =listSize-(Integer.valueOf(imagePerPage));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * load all galary page image
	 */
	public void allImage(){
		if(parentImageList !=null && parentImageList.size()>0 )
		{
			imagePerPage=parentImageList.size();
		}
		sessionManager.setSessionAttributeInSession(SessionManager.IMAGEPERPAGE, imagePerPage);
	}
	/** 
	 * 	deleteParticularDeData 
	 *   delete child job
	 */
	public String deleteDeData() 
	{
		try
		{
			currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(currentUser != null)
			{
				FacesUtils facesUtils = new FacesUtils();
				long deDataId = Long.valueOf(facesUtils.getRequestParameterMap("baseId"));
				if(deDataId >0){
					DataEntry dataEntry = deService.getDataEntryById(deDataId);
					if(dataEntry != null){
						dataEntry.setIsDeleted(true);
						dataEntry.setDeleted_by(currentUser);
						deService.updateDataEntry(dataEntry);
						messageService.messageInformation(null, "Data Entry has been Deleted Successfully.");
					}

				}
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public int stateDetailsChecking(String query) {
		stateResults = userService.getStates();
		if(stateResults != null &&  stateResults.size() > 0){
			for(int i = 0; stateResults.size()>i; i++) {
				States state = stateResults.get(i);
				if(state.getStateName().equals(query)) {
					return 1;
				}
			}
		}
		return 0;
	}
	/**
	 * 
	 * @param query
	 * @return
	 */
	public int countryDetailsChecking(String query) {
		countryResults = userService.getCountry();
		if(countryResults != null &&  countryResults.size() > 0){
			for(int i = 0; countryResults.size()>i; i++) {
				Country country = countryResults.get(i);
				if(country.getCountryName().equals(query)) {
					return 1;
				}
			}
		}
		return 0;
	}
	/**
	 * shashank
	 * @return
	 */
	public String addNewCompany() 
	{
		try
		{
			DeCompany deCompany  = null;
			currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(currentUser != null)
			{
				if(!(this.country.isEmpty()) && !(this.state.isEmpty()))
				{
					if(countryDetailsChecking(this.country) == 1)
					{
						if(stateDetailsChecking(this.state) == 1)
						{//shashank
								if(this.companyName != null && !this.companyName.isEmpty()){
									DeCompany duplicate = deService.getDeCompanyNameByCompanyName(this.companyName);
									if(duplicate == null){
										deCompany = new DeCompany();
										deCompany = getAllDeCompanyDetails(deCompany);
										deCompany.setIsDeleted(false);
										deCompany.setCreated_by(currentUser);
										deService.addDeCompany(deCompany);
										messageService.messageInformation(null, "Company added successfully.");
									} else {
										messageService.messageFatal(null, "CompanyName already exist.");
										return null;
									}
								} else {
									if(this.searchValueInCompanyName != null && !this.searchValueInCompanyName.isEmpty()){
										deCompany = deService.getDeCompanySeachByCompanyName(this.searchValueInCompanyName);	
									}else if(this.companyId > 0){
										deCompany = deService.getDeCompanyById(this.companyId);	
									} else {
										messageService.messageFatal(null, "CompanyName is required");
										return null; 
									}
								}

						}else{
							messageService.messageFatal(null, "StateName not found.");
						}

					}else{
						messageService.messageFatal(null, "CountryName not found.");
					}

				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public String saveAndExitDataAndCompany() 
	{
		try
		{
			DeCompany deCompany  = null;
			currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			/*if(currentUser != null)
			{
				if(!(this.country.isEmpty()) && !(this.state.isEmpty()))
				{
				if(countryDetailsChecking(this.country) == 1)
				{
					if(stateDetailsChecking(this.state) == 1)
					{
						FacesUtils facesUtils = new FacesUtils();
						long deDataId = Long.valueOf(facesUtils.getRequestParameterMap("baseId"));
						if(deDataId >0){
							DataEntry dataEntry = deService.getDataEntryById(deDataId);
							if(dataEntry != null && this.companyName != null && !this.companyName.isEmpty()){
								DeCompany duplicate = deService.getDeCompanyNameByCompanyName(this.companyName);
								if(duplicate == null){
									deCompany = new DeCompany();
									deCompany = getAllDeCompanyDetails(deCompany);
									deCompany.setIsDeleted(false);
									deCompany.setCreated_by(currentUser);
									deService.addDeCompany(deCompany);
								}
								else{
								}
							}
							else{
								if(this.searchValueInCompanyName != null && !this.searchValueInCompanyName.isEmpty()){
									deCompany = deService.getDeCompanySeachByCompanyName(this.searchValueInCompanyName);	
								}else if(this.companyId > 0){
									deCompany = deService.getDeCompanyById(this.companyId);	
								} else {
									messageService.messageFatal(null, "CompanyName is required");
									return null; 
								}
							}
							if(dataEntry != null && deCompany != null ){
								dataEntry = getAllAdDetails(dataEntry);
								dataEntry.setIsDeleted(false);
								dataEntry.setIsqualityCheck(true);
								dataEntry.setDeCompany(deCompany);
								dataEntry.setCreated_by(currentUser);
								deService.updateDataEntry(dataEntry);
								messageService.messageInformation(null, "Data Entry has been Updated successfully.");
							}
						}

					}else{
						messageService.messageFatal(null, "StateName not found.");
					}

				}else{
					messageService.messageFatal(null, "CountryName not found.");
				}
		
				}else{
					
				}
			}*/
			
			
			FacesUtils facesUtils = new FacesUtils();
			long deDataId = Long.valueOf(facesUtils.getRequestParameterMap("baseId"));
			if(deDataId >0){
				DataEntry dataEntry = deService.getDataEntryById(deDataId);
				if(dataEntry != null && this.companyName != null && !this.companyName.isEmpty()){
					DeCompany duplicate = deService.getDeCompanyNameByCompanyName(this.companyName);
					if(duplicate == null){
						deCompany = new DeCompany();
						deCompany = getAllDeCompanyDetails(deCompany);
						deCompany.setIsDeleted(false);
						deCompany.setCreated_by(currentUser);
						deService.addDeCompany(deCompany);
					}
					else{
						messageService.messageFatal(null, "CompanyName already exist.");
						return null;
					}
				}
				else{
					if(this.searchValueInCompanyName != null && !this.searchValueInCompanyName.isEmpty()){
						deCompany = deService.getDeCompanySeachByCompanyName(this.searchValueInCompanyName);	
					}else if(this.companyId > 0){
						deCompany = deService.getDeCompanyById(this.companyId);	
					} else {
						messageService.messageFatal(null, "CompanyName is required");
						return null; 
					}
				}
				if(dataEntry != null && deCompany != null ){
					dataEntry = getAllAdDetails(dataEntry);
					dataEntry.setIsDeleted(false);
					dataEntry.setIsqualityCheck(true);
					dataEntry.setDeCompany(deCompany);
					dataEntry.setCreated_by(currentUser);
					deService.updateDataEntry(dataEntry);
					messageService.messageInformation(null, "Data Entry has been Updated successfully.");
				}
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @return
	 */
	public String updateDataEntry() 
	{
		try
		{
			DeCompany deCompany  = null;
			currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(currentUser != null)
			{
				if(!(this.country.isEmpty()) && !(this.state.isEmpty()))
				{
				if(countryDetailsChecking(this.country) == 1)
				{
					if(stateDetailsChecking(this.state) == 1)
					{
						FacesUtils facesUtils = new FacesUtils();
						long deDataId = Long.valueOf(facesUtils.getRequestParameterMap("baseId"));
						if(deDataId >0){
							DataEntry dataEntry = deService.getDataEntryById(deDataId);
							if(dataEntry != null && this.companyName != null && !this.companyName.isEmpty()){
								DeCompany duplicate = deService.getDeCompanyNameByCompanyName(this.companyName);
								if(duplicate == null){
									deCompany = new DeCompany();
									deCompany = getAllDeCompanyDetails(deCompany);
									deCompany.setIsDeleted(false);
									//deCompany.setCreated_by(currentUser);
									deService.addDeCompany(deCompany);
								}
								else{
									messageService.messageFatal(null, "CompanyName already exist.");
									return null;
								}
							}
							else{
								if(this.searchValueInCompanyName != null && !this.searchValueInCompanyName.isEmpty()){
									deCompany = deService.getDeCompanySeachByCompanyName(this.searchValueInCompanyName);	
								}else if(this.companyId > 0){
									deCompany = deService.getDeCompanyById(this.companyId);	
								} else {
									messageService.messageFatal(null, "CompanyName is required");
									return null; 
								}
							}
							if(dataEntry != null){
								dataEntry = getAllAdDetails(dataEntry);
								dataEntry.setIsDeleted(false);
								dataEntry.setIsqualityCheck(true);
								dataEntry.setCreated_by(currentUser);
								deService.updateDataEntry(dataEntry);
								messageService.messageInformation(null, "Data Entry has been Updated successfully.");
							}

						}
					}else{
						messageService.messageFatal(null, "StateName not found.");
					}

				}else{
					messageService.messageFatal(null, "CountryName not found.");
				}
				}else{
					FacesUtils facesUtils = new FacesUtils();
					long deDataId = Long.valueOf(facesUtils.getRequestParameterMap("baseId"));
					if(deDataId >0){
						DataEntry dataEntry = deService.getDataEntryById(deDataId);
						if(dataEntry != null && this.companyName != null && !this.companyName.isEmpty()){
							DeCompany duplicate = deService.getDeCompanyNameByCompanyName(this.companyName);
							if(duplicate == null){
								deCompany = new DeCompany();
								deCompany = getAllDeCompanyDetails(deCompany);
								deCompany.setIsDeleted(false);
								//deCompany.setCreated_by(currentUser);
								deService.addDeCompany(deCompany);
							}
							else{
								messageService.messageFatal(null, "CompanyName already exist.");
								return null;
							}
						}
						else{
							if(this.searchValueInCompanyName != null && !this.searchValueInCompanyName.isEmpty()){
								deCompany = deService.getDeCompanySeachByCompanyName(this.searchValueInCompanyName);	
							}else if(this.companyId > 0){
								deCompany = deService.getDeCompanyById(this.companyId);	
							} else {
								messageService.messageFatal(null, "CompanyName is required");
								return null; 
							}
						}
						if(dataEntry != null){
							dataEntry = getAllAdDetails(dataEntry);
							dataEntry.setIsDeleted(false);
							dataEntry.setIsqualityCheck(true);
							dataEntry.setCreated_by(currentUser);
							deService.updateDataEntry(dataEntry);
							messageService.messageInformation(null, "Data Entry has been Updated successfully.");
						}

					}
				}
				}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String callParentImage(){
		try
		{
			User currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);	
			FacesUtils facesUtils = new FacesUtils();
			String val = facesUtils.getRequestParameterMap("parentImg");
			this.id = Integer.valueOf(val!=null?val:"0");
			String jobIdval = facesUtils.getRequestParameterMap("jobId");
			this.deJobid = Integer.valueOf(jobIdval!=null?jobIdval:"0");
			if(currentUser != null && this.id>0){
				ParentImage parentImage = getParentImageService().getParentImageById(this.id);
				this.parentImageName = parentImage.getImageName();
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String callChildImage(){
		try
		{
			User currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);	
			FacesUtils facesUtils = new FacesUtils();
			String val = facesUtils.getRequestParameterMap("parentImg");
			this.id = Integer.valueOf(val!=null?val:"0");
			String childVal = facesUtils.getRequestParameterMap("childImg");
			this.childImageId = Integer.valueOf(childVal!=null?childVal:"0");
			String jobIdval = facesUtils.getRequestParameterMap("jobId");
			this.deJobid = Integer.valueOf(jobIdval!=null?jobIdval:"0");
			if(currentUser != null && this.childImageId>0){
				ChildImage childImage = getChildImageService().getChildImageById(this.childImageId);
				this.childImageName = childImage.getImageName();
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Set Approval to job qcApprovedJob
	 */
	public String qcApprovedJobByList() 
	{
		try
		{
			currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(currentUser != null)
			{
				FacesUtils facesUtils = new FacesUtils();
				long deDataId = Long.valueOf(facesUtils.getRequestParameterMap("baseId"));
				if(deDataId >0){
					DataEntry dataEntry = deService.getDataEntryById(deDataId);
					if(dataEntry != null ){
						dataEntry = getAllAdDetails(dataEntry);
						dataEntry.setIsApproved(2);
						deService.updateDataEntry(dataEntry);
						messageService.messageInformation(null, "Data Entry has been Approval successfully.");
						return null;
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}
	/**
	 * Set Reject the job
	 */
	public String qcRejectJobByList() 
	{
		try
		{
			currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(currentUser != null)
			{
				FacesUtils facesUtils = new FacesUtils();
				long deDataId = Long.valueOf(facesUtils.getRequestParameterMap("baseId"));
				if(deDataId >0){
					DataEntry dataEntry = deService.getDataEntryById(deDataId);
					if(dataEntry != null ){
						dataEntry = getAllAdDetails(dataEntry);
						dataEntry.setIsApproved(3);
						deService.updateDataEntry(dataEntry);
						messageService.messageInformation(null, "Data Entry has been Rejected successfully.");
						return null;
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}
}