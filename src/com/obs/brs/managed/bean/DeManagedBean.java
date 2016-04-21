package com.obs.brs.managed.bean;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import javax.servlet.http.HttpServletRequest;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.obs.brs.ocr.Ocr;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.expression.spel.ast.OpNE;
import org.springframework.scheduling.annotation.Scheduled;

import com.obs.brs.controller.ScoreData;
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
import com.obs.brs.service.ChildImageService;
import com.obs.brs.service.DeService;
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
	
	private List<ScoreData> dataEntries = new ArrayList<ScoreData>();
	private Map<Long, Boolean> checkedDe = new HashMap<Long, Boolean>();
	private Map<Long, Boolean> checkedQc = new HashMap<Long, Boolean>();
	private long id;
	private long companyId;
	private String companyName;
	private String companyURL;
	private String department;
	private String address;
	private String address1;
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
	private String contactInfo;
	private String startCurrencyRange;
	private String endCurrencyRange;
	private String ocrText;
	private Boolean isqualityCheck;
	private Boolean isDeleted;
	private int isApproved;
	private User user;
	private Date created_On;
	private Date deleted_On;
	private boolean isSendToQC = false;
	//search & filters
	private String searchValue ="";
	private String searchIssueDate = "";	
	boolean order = true ;
	List<DataEntry> dataEntryuserList;
	User currentUser;
	List<DeJob> deJobList;
	List<ParentImage> parentImageList = new ArrayList<ParentImage>();
	List<ParentImage> parentImageListfornewSci = new ArrayList<>();

	List<ScoreData> deReleavanceImageList = new ArrayList<ScoreData>();

	List<ChildImage> childImageList = new ArrayList<ChildImage>();
	List<DataEntry> childImagesDataList = new ArrayList<DataEntry>();
	List<ChildImage> currentParentsChild = new ArrayList<ChildImage>();
	String filterParentImage = "0";
	//For cropping 
	private float cropWidth;
	private float cropHeight;
	private float cropX1;
	private float cropX2;
	private float cropY1;
	private float cropY2;
	private String croppedImageName;
	private ParentImage parentImage;
	private String msgLabel;
	private String msgWarnLabel;
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
	private String createdBy;
	private String publicationId;
	private String jobStatusGot;
	private String issueDatePubSearch;
	private String createdByDeo;
	
	private Long companyIdEd = null;
	private String companyNameEd = "";
	private String companyURLEd = "";
	private String departmentEd = "";
	private String addressEd = "";
	private String address1Ed = "";
	private String cityEd = "";
	private String stateEd = "";
	private String countryEd = "";
	private String pincodeEd = "";
	private HashMap hm = new HashMap<>() ;
	private long saveNewCmpId;
	private boolean duplicate = true;
	
	private DeCompany selectedCompany;
	private Integer selectedCompanyId;
	private boolean isFilter = true;
	private Integer pages[];
	private Integer pagesOcr[];
	private Integer pagesNewSci[];
	
	private int pageRange = 10;
	private int pageRangeOcr = 4;
	private int pageRangeNewSci = 4;

	private int reviewPageRange = 3;
	private String totalStr;
	private String totalStrOcr;
	private String totalStrNewSci;
	
	private Set<String> duplicateFileNames;
	private List<String> duplicateNames;
	private String iframeUrl;
	private int count = 1;
	private long liveStatusId;
	private Long[] liveStatusArr;
	private String selectedPub;

	public String getSelectedPub() {
		return selectedPub;
	}

	public void setSelectedPub(String selectedPub) {
		this.selectedPub = selectedPub;
	}

	public Long[] getLiveStatusArr() {
		return liveStatusArr;
	}

	public void setLiveStatusArr(Long[] liveStatusArr) {
		this.liveStatusArr = liveStatusArr;
	}

	public int getPageRangeOcr() {
		return pageRangeOcr;
	}

	public void setPageRangeOcr(int pageRangeOcr) {
		this.pageRangeOcr = pageRangeOcr;
	}
	
	public int getPageRangeNewSci() {
		return pageRangeNewSci;
	}

	public void setPageRangeNewSci(int pageRangeNewSci) {
		this.pageRangeNewSci = pageRangeNewSci;
	}
	public Integer[] getPagesOcr() {
		//HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		loadPaginationOcr();
		return pagesOcr;
	}

	public void setPagesOcr(Integer[] pagesOcr) {
		this.pagesOcr = pagesOcr;
	}
	
	public Integer[] getPagesNewSci() {
		loadPaginationNewSci();
		
		return pagesNewSci;
	}

	public void setPagesNewSci(Integer[] pagesNewSci) {
		this.pagesNewSci = pagesNewSci;
	}

	
	public long getLiveStatusId() {
		return liveStatusId;
	}

	public void setLiveStatusId(long liveStatusId) {
		this.liveStatusId = liveStatusId;
	}

	private Map<Long, Boolean> selectedIds = new HashMap<Long, Boolean>();
	  public Map<Long, Boolean> getSelectedIds() {
		  //System.out.println("selectedIds : "+selectedIds.size());
	    return selectedIds;
	  }
	
	public int getCount() {
		return (count++);
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getTotalStrOcr() {
		StringBuffer buffer = new StringBuffer();
		buffer.append((this.imageOffsetOcr/this.imagePerPageOcr)+1);
		buffer.append("/");
		buffer.append(((this.deReleavanceImageList.size()/this.imagePerPageOcr)+1));
		return buffer.toString();
		
	}

	public void setTotalStrOcr(String totalStrOcr) {
		this.totalStrOcr = totalStrOcr;
	}

	
	private static Map<String,String> titleMap = new HashMap<String,String>(6);
	private static Map<String,String> sectionMap = new HashMap<String,String>(4);
	static {
		titleMap.put("natus", "1084");
		titleMap.put("natuk", "1085");
		titleMap.put("natgbl", "1086");
		titleMap.put("newus", "520");
		titleMap.put("newuk", "521");
		titleMap.put("cell", "522");
		titleMap.put("scius", "518");
		sectionMap.put("SR", "973");
		sectionMap.put("ST", "1032");
		sectionMap.put("CL", "644");
		sectionMap.put("OT", "1033");
	}
	
	public String getIframeUrl() {
		User user = (User)this.sessionManager.getSessionAttribute("login_user");
		return "http://enter-fracts.com/webapp/report/index.html?userId="+user.getId()+"&subscriberId=-1";
	}
	
	public void setIframeUrl(String iframeUrl) {
		this.iframeUrl = iframeUrl;
	}

	public List<String> getDuplicateNames() {
		return duplicateNames;
	}

	public void setDuplicateNames(List<String> duplicateNames) {
		this.duplicateNames = duplicateNames;
	}

	public Set<String> getDuplicateFileNames() {
		return duplicateFileNames;
	}

	public void setDuplicateFileNames(Set<String> duplicateFileNames) {
		this.duplicateFileNames = duplicateFileNames;
	}

	public String getTotalStr() {
		StringBuffer buffer = new StringBuffer();
		buffer.append((this.imageOffset/this.imagePerPage)+1);
		buffer.append("/");
		buffer.append(((this.getParentImageList().size()/this.imagePerPage)+1));
		return buffer.toString();
	}
	
	public String getTotalStrNewSci() {
		StringBuffer buffer = new StringBuffer();
		buffer.append((this.imageOffsetnewSci/this.imagePerPageNewSci)+1);
		buffer.append("/");
		buffer.append(((this.parentImageListfornewSci.size()/this.imagePerPageNewSci)+1));
		return buffer.toString();
		
	}

	public void setTotalStrNewSci(String totalStrNewSci) {
		this.totalStrNewSci = totalStrNewSci;
	}
	
	public String getReviewPageTotal() {
		StringBuffer buffer = new StringBuffer();
		buffer.append((this.reviewPageOffset/this.getRowsPerPage())+1);
		buffer.append("/");
		buffer.append(((this.getDeJobListBySeachCriteria().size()/this.getRowsPerPage())+1));
		return buffer.toString();
	}

	public String getMsgWarnLabel() {
		return msgWarnLabel;
	}

	public void setMsgWarnLabel(String msgWarnLabel) {
		this.msgWarnLabel = msgWarnLabel;
	}

	public void setTotalStr(String totalStr) {
		this.totalStr = totalStr;
	}

	private void loadPagination() {
		int totalRows = this.getParentImageList().size();
		int currentPage = (this.imageOffset/this.imagePerPage)+1;
        int totalPages = (totalRows / this.imagePerPage) + ((totalRows % this.imagePerPage != 0) ? 1 : 0);
        int pagesLength = Math.min(pageRange, totalPages);
        pages = new Integer[pagesLength];
        int firstPage = Math.min(Math.max(0, currentPage - (pageRange / 2)), totalPages - pagesLength);
        for (int i = 0; i < pagesLength; i++) {
            pages[i] = ++firstPage;
        }
	}
	
	/** load pagination for the ocr text**/
	private void loadPaginationOcr() {
		int totalRows = this.getDeReleavanceImageList().size();
		int currentPage = (this.imageOffsetOcr/this.imagePerPageOcr)+1;
        int totalPages = (totalRows / this.imagePerPageOcr) + ((totalRows % this.imagePerPageOcr != 0) ? 1 : 0);
        int pagesLength = Math.min(pageRangeOcr, totalPages);
        pagesOcr = new Integer[pagesLength];
        int firstPage = Math.min(Math.max(0, currentPage - (pageRangeOcr / 2)), totalPages - pagesLength);
        for (int i = 0; i < pagesLength; i++) {
            pagesOcr[i] = ++firstPage;
        }
	}

	
	/** load pagination for the new sci text**/
	private void loadPaginationNewSci() {
		int totalRows = this.getParentImageListfornewSci().size();
		int currentPage = (this.imageOffsetnewSci/this.imagePerPageNewSci)+1;
        int totalPages = (totalRows / this.imagePerPageNewSci) + ((totalRows % this.imagePerPageNewSci != 0) ? 1 : 0);
        int pagesLength = Math.min(pageRangeNewSci, totalPages);
        pagesNewSci = new Integer[pagesLength];
        int firstPage = Math.min(Math.max(0, currentPage - (pageRangeNewSci / 2)), totalPages - pagesLength);
        for (int i = 0; i < pagesLength; i++) {
            pagesNewSci[i] = ++firstPage;
        }
	}
	
	
	private void loadReviewPagination() {
		int totalRows = this.getDeJobListBySeachCriteria().size();
		int currentPage = (this.reviewPageOffset/this.getRowsPerPage())+1;
        int totalPages = (totalRows / this.getRowsPerPage()) + ((totalRows % this.getRowsPerPage() != 0) ? 1 : 0);
        int pagesLength = Math.min(reviewPageRange, totalPages);
        pages = new Integer[pagesLength];
        int firstPage = Math.min(Math.max(0, currentPage - (reviewPageRange / 2)), totalPages - pagesLength);
        for (int i = 0; i < pagesLength; i++) {
            pages[i] = ++firstPage;
        }
	}
	
	public Integer[] getPages() {
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if(origRequest.getRequestURI().indexOf("manage_job")!=-1) {
			loadReviewPagination();
		}
		if(origRequest.getRequestURI().indexOf("gallery")!=-1) {
			loadPagination();
		}
		System.out.println("origRequest.getRequestURI(): "+origRequest.getRequestURI());
		if(origRequest.getRequestURI().indexOf("ocr_releavance")!=-1) {
			loadPaginationOcr();
		}
		return pages;
	}

	public void setPages(Integer[] pages) {
		this.pages = pages;
	}

	public int getReviewPageRange() {
		return reviewPageRange;
	}

	public void setReviewPageRange(int reviewPageRange) {
		this.reviewPageRange = reviewPageRange;
	}

	public int getReviewPageOffset() {
		String offset = this.sessionManager.getSessionAttribute("reviewPageOffset")!=null?this.sessionManager.getSessionAttribute("reviewPageOffset").toString():null;
		if(offset!=null) {
			reviewPageOffset = Integer.parseInt(offset);
		}
		return reviewPageOffset;
	}

	public void setReviewPageOffset(int reviewPageOffset) {
		this.reviewPageOffset = reviewPageOffset;
	}

	public boolean isFilter() {
		return isFilter;
	}

	public void setFilter(boolean isFilter) {
		this.isFilter = isFilter;
	}

	public Integer getSelectedCompanyId() {
		return selectedCompanyId;
	}

	public void setSelectedCompanyId(Integer selectedCompanyId) {
		this.selectedCompanyId = selectedCompanyId;
	}

	public DeCompany getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(DeCompany selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public String getCityEd() {
		return cityEd;
	}

	public void setCityEd(String cityEd) {
		this.cityEd = cityEd;
	}

	public Long getCompanyIdEd() {
		return companyIdEd;
	}

	public void setCompanyIdEd(Long companyIdEd) {
		this.companyIdEd = companyIdEd;
	}

	public String getCompanyNameEd() {
		return companyNameEd;
	}

	public void setCompanyNameEd(String companyNameEd) {
		this.companyNameEd = companyNameEd;
	}

	public String getCompanyURLEd() {
		return companyURLEd;
	}

	public void setCompanyURLEd(String companyURLEd) {
		this.companyURLEd = companyURLEd;
	}

	public String getDepartmentEd() {
		return departmentEd;
	}

	public void setDepartmentEd(String departmentEd) {
		this.departmentEd = departmentEd;
	}

	public String getAddressEd() {
		return addressEd;
	}

	public void setAddressEd(String addressEd) {
		this.addressEd = addressEd;
	}

	public String getAddress1Ed() {
		return address1Ed;
	}

	public void setAddress1Ed(String address1Ed) {
		this.address1Ed = address1Ed;
	}

	public String getStateEd() {
		return stateEd;
	}

	public void setStateEd(String stateEd) {
		this.stateEd = stateEd;
	}

	public String getCountryEd() {
		return countryEd;
	}

	public void setCountryEd(String countryEd) {
		this.countryEd = countryEd;
	}

	public String getPincodeEd() {
		return pincodeEd;
	}

	public void setPincodeEd(String pincodeEd) {
		this.pincodeEd = pincodeEd;
	}


	public String getCreatedByDeo() {
		return createdByDeo;
	}

	public void setCreatedByDeo(String createdByDeo) {
		this.createdByDeo = createdByDeo;
	}

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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
	public void setFilterParentImage(String filterParentImage) {
		this.filterParentImage = filterParentImage;
	}
	public String getFilterParentImage() {
		return filterParentImage;
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
	private int childImagePerPage = 20;
	private int childImageOffset = 0;
	private int reviewPageOffset = 0;
	private int imagePerPageOcr = 20;
	private int imagePerPageNewSci = 20;
	private int imageOffsetOcr = 0;
	private int imageOffsetnewSci = 0;
	
	public int getImageOffsetnewSci() {
		try{
			Object offset = sessionManager.getSessionAttribute(SessionManager.IMAGEOFFSETNEWSCI);
			if(offset!=null){
				imageOffsetnewSci = Integer.valueOf(offset.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return imageOffsetnewSci;
	}

	public void setImageOffsetnewSci(int imageOffsetnewSci) {
		this.imageOffsetnewSci = imageOffsetnewSci;
	}
	

	public int getImageOffsetOcr() {
		
		try{
			Object offset = sessionManager.getSessionAttribute(SessionManager.IMAGEOFFSET);
			if(offset!=null){
				imageOffsetOcr = Integer.valueOf(offset.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return imageOffsetOcr;
	}

	public void setImageOffsetOcr(int imageOffsetOcr) {
		this.imageOffsetOcr = imageOffsetOcr;
	}

	public int getImagePerPageOcr() {
		
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGEOCR); 
			if(rowsPerPage!=null){
				imagePerPageOcr = Integer.valueOf(rowsPerPage.toString());
			}
		}catch(Exception e){
		}
		return imagePerPageOcr;
	}

	public void setImagePerPageOcr(int imagePerPageOcr) {
		this.imagePerPageOcr = imagePerPageOcr;
	}

	public int getImagePerPageNewSci() {
		
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGENEWSCI); 
			if(rowsPerPage!=null){
				imagePerPageNewSci = Integer.valueOf(rowsPerPage.toString());
			}
		}catch(Exception e){
		}
		return imagePerPageNewSci;
	}

	public void setImagePerPageNewSci(int imagePerPageNewSci) {
		this.imagePerPageNewSci = imagePerPageNewSci;
	}
	
	public int getChildImagePerPage() {
		return childImagePerPage;
	}

	public void setChildImagePerPage(int childImagePerPage) {
		this.childImagePerPage = childImagePerPage;
	}

	public int getChildImageOffset() {
		return childImageOffset;
	}

	public void setChildImageOffset(int childImageOffset) {
		this.childImageOffset = childImageOffset;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}
	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}
	public int getImagePerPage() {
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGE); 
			if(rowsPerPage!=null){
				imagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
		}catch(Exception e){
		}
		return imagePerPage;
	}
	public void setImagePerPage(int imagePerPage) {
		this.imagePerPage = imagePerPage;
	}
	public int getImageOffset() {
		try{
			Object offset = sessionManager.getSessionAttribute(SessionManager.IMAGEOFFSET);
			if(offset!=null){
				imageOffset = Integer.valueOf(offset.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
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
	public float getCropWidth() {
		return cropWidth;
	}
	/**
	 * @param cropWidth the cropWidth to set
	 */
	public void setCropWidth(float cropWidth) {
		this.cropWidth = cropWidth;
	}
	/**
	 * @return the cropHeight
	 */
	public float getCropHeight() {
		return cropHeight;
	}
	/**
	 * @param cropHeight the cropHeight to set
	 */
	public void setCropHeight(float cropHeight) {
		this.cropHeight = cropHeight;
	}
	/**
	 * @return the cropX1
	 */
	public float getCropX1() {
		return cropX1;
	}
	/**
	 * @param cropX1 the cropX1 to set
	 */
	public void setCropX1(float cropX1) {
		this.cropX1 = cropX1;
	}
	/**
	 * @return the cropX2
	 */
	public float getCropX2() {
		return cropX2;
	}
	/**
	 * @param cropX2 the cropX2 to set
	 */
	public void setCropX2(float cropX2) {
		this.cropX2 = cropX2;
	}
	/**
	 * @return the cropY1
	 */
	public float getCropY1() {
		return cropY1;
	}
	/**
	 * @param cropY1 the cropY1 to set
	 */
	public void setCropY1(float cropY1) {
		this.cropY1 = cropY1;
	}
	/**
	 * @return the cropY2
	 */
	public float getCropY2() {
		return cropY2;
	}
	/**
	 * @param cropY2 the cropY2 to set
	 */
	public void setCropY2(float cropY2) {
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
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
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

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
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
		this.contactInfo = dataEntry.getContactInfo();
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
		this.createdBy = dataEntry.getCreated_by().getFirstName();
		this.selectedCompany = dataEntry.getDeCompany();
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
		dataEntry.setContactInfo(this.contactInfo);
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
		deCompany.setAddress1(this.address1);
		return deCompany;
	}
	public void clearAllDeCompanyDetails(){
		this.companyName = "";
		this.address = "";
		this.city = "";
		this.state = "";
		this.country = "";
		this.pincode = "";
		this.department = "";
		this.companyURL = "";
		this.address1 = "";
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
				//DeJob deJob = deService.getDeJobByParentImageId(deJobid);
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
									callAddDEOcr();
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
									callAddDEOcr();
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
							updateDataEntryOcr(dataEntry);
						}else{
							dataEntry = new DataEntry();
							dataEntry.setIsDeleted(false);
							dataEntry.setIsqualityCheck(false);
							dataEntry.setDeJobid(deJob);
							dataEntry.setCreated_by(currentUser);
							dataEntry.setParentImage(deJob.getParentImage());
							deService.addDataEntry(dataEntry);	
							callAddDEOcr();
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
	
	private void callAddDEOcr() {
		try {
			AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
			asyncHttpClient.prepareGet("http://localhost:8080/webapp/addUnProcessedOcr").execute(new AsyncCompletionHandler<Response>(){
				
				@Override
				public Response onCompleted(Response response) throws Exception{
					return response;
				}

				@Override
				public void onThrowable(Throwable t){
				}
			});
		} catch(Exception e){
		}
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
						Object childImageSessionObj = sessionManager.getSessionAttribute(SessionManager.CHILDIMAGEID);
						int childImageIdSession   = childImageSessionObj!=null?((Integer)childImageSessionObj).intValue():0; // get job id
						if(childImageIdSession > 0){
							dataEntry = deService.getDataEntryByChildImageId(childImageIdSession);
						}
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
									File image=new File(imageBasePath+CommonProperties.getParentImagePath()+this.parentImageId+"/"+this.parentImageName);
									ImageIO.scanForPlugins();
									String result = new Ocr().doOCR(image);
									System.out.println("result: "+result);
									if(result != null){
										this.ocrText = result;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if(this.childImageId > 0 && this.ocrText == null){
								try {
									File image=new File(imageBasePath+CommonProperties.getChildImagePath()+this.parentImageId+"/"+this.childImageId+"/"+dataEntry.getChildImage().getImageName());
									ImageIO.scanForPlugins();
									String result = new Ocr().doOCR(image);
									System.out.println("result: "+result);
									if(result != null){
										this.ocrText = result;
									}
								} catch (Exception e) {
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
		sessionManager.removeSessionAttributeInSession(SessionManager.CHILDIMAGEID);
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
		this.contactInfo ="";
		this.searchValueInCompanyName = "";
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
	public void doneParentImage(){
		String val = facesUtils.getRequestParameterMap("parentImg");
		int imageId = Integer.valueOf(val!=null?val:"0");
		if(imageId >0){
			ParentImage parentImage = getParentImageService().getParentImageById(imageId);
			parentImage.setStatus(2);
			getParentImageService().updateParentImage(parentImage);
			parentImageList = new ArrayList<ParentImage>();
		}
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
	
	public List<ScoreData> getDeReleavanceImageList() {
		if(deReleavanceImageList == null || deReleavanceImageList.isEmpty() ) {
			deReleavanceImageList = deService.getDeoByReleavance();
			liveStatusArr = new Long[deReleavanceImageList.size()];
		}
		return deReleavanceImageList;
	}

	public void setDeReleavanceImageList(List<ScoreData> deReleavanceImageList) {
		this.deReleavanceImageList = deReleavanceImageList;
	}
	
	/**
	 * Get Parent Image List 
	 * 
	 * @return List - ParentImage 
	 */
	public void filterParentImages() {
		count  = 1;
		long start = System.currentTimeMillis();
		parentImageList = new ArrayList<ParentImage>();
		if(filterParentImage != null){
			parentImageList.addAll(getParentImageService().getParentImageByFilter(filterParentImage));
			for(ParentImage image : parentImageList){
				image.childImageList.addAll(getChildImageService().getChildImagesByParent(image.getId()));
			}
			Collections.reverse(parentImageList);
		}
		System.out.println("deBean : filterParentImages :" + (System.currentTimeMillis() - start)/1000);
	}

	
	
	/**
	 * Get Parent Image List 
	 * 
	 * @return List - ParentImage 
	 */
	private List<ParentImage> buildParentImageListForNewSci() {
		long start = System.currentTimeMillis();
		parentImageListfornewSci  = new ArrayList<ParentImage>();
		Set<String> duplicateNames = new HashSet<String>();
		if(this.duplicateNames==null) {
			this.duplicateNames = new ArrayList<String>();
		} else {
			this.duplicateNames.clear();
		}
		if(filterParentImage != null){
			parentImageListfornewSci.addAll(getParentImageService().getParentImageForSciByFilter(filterParentImage));
			for(ParentImage image : parentImageListfornewSci){
				if(!duplicateNames.add(image.getImageName())) {
					if(!this.duplicateNames.contains(image.getImageName()))
						this.duplicateNames.add(image.getImageName());
				}
				List<ChildImage> childImages = getChildImageService().getChildImagesByParent(image.getId());
				image.childImageList.addAll(childImages);
				if(childImages.size() == 0){
					image.ableToDone = false;
					continue;
				} else {
					Set<Long> set = new HashSet<Long>(childImages.size());
					for(ChildImage childImage : childImages){
						set.add(childImage.getId());
					}
					List<DataEntry> entries = deService.getDataEntryByChildImageIds(set);
					List<Long> idsAdded = new ArrayList<Long>(set.size()); 
					if(entries != null) {
						for(DataEntry entry:entries) {
							for(ChildImage childImage : image.childImageList){
								if(entry.getChildImage().getId() == childImage.getId()) {
									if(entry.getDeCompany()!=null) {
										childImage.setIsCompleted("complete");
									} else {
										childImage.setIsCompleted("incomplete");
									}
									idsAdded.add(childImage.getId());
									break;
								}
							}
						}
						for(ChildImage childImage : image.childImageList){
							if(!idsAdded.contains(childImage.getId())) {
								childImage.setIsCompleted("incomplete");
							}
						}
					}
				} 
				/*for(ChildImage childImage : childImages){
					DataEntry entry = deService.getDataEntryByChildImageId(childImage.getId());
					if(entry != null) {
						if(entry.getDeCompany() == null){
							image.ableToDone = false;
							break;
						}
					}
				}*/
				
			}
			
			Collections.reverse(parentImageListfornewSci);
		}

		System.out.println("buildParentImageList :" + (System.currentTimeMillis() - start)/1000);
		List<ParentImage> plfinal =  new ArrayList<ParentImage>();

		for(ParentImage pi : parentImageListfornewSci){
        	if(pi.getImageName() != null){
        		String []imgnamearr = pi.getImageName().split("_");
        		System.out.println("imgnamearr[imgnamearr.length -1]: "+imgnamearr[imgnamearr.length -1] );	
        		String pubtype = imgnamearr[imgnamearr.length -1].split("\\.")[0];
        		if(pubtype.equalsIgnoreCase("A") || pubtype.equalsIgnoreCase("B") || pubtype.equalsIgnoreCase("C")){
        				
        		}else{
        		  plfinal.add(pi);
        		}
        	}
        }

        return plfinal;
	}
	
	/**
	 * Get Parent Image List 
	 * 
	 * @return List - ParentImage 
	 */
	private List<ParentImage> buildParentImageList() {
		long start = System.currentTimeMillis();
		parentImageList = new ArrayList<ParentImage>();
		Set<String> duplicateNames = new HashSet<String>();
		if(this.duplicateNames==null) {
			this.duplicateNames = new ArrayList<String>();
		} else {
			this.duplicateNames.clear();
		}
		if(filterParentImage != null){
			parentImageList.addAll(getParentImageService().getParentImageByFilter(filterParentImage));
			for(ParentImage image : parentImageList){
				if(!duplicateNames.add(image.getImageName())) {
					if(!this.duplicateNames.contains(image.getImageName()))
						this.duplicateNames.add(image.getImageName());
				}
				List<ChildImage> childImages = getChildImageService().getChildImagesByParent(image.getId());
				image.childImageList.addAll(childImages);
				if(childImages.size() == 0){
					image.ableToDone = false;
					continue;
				} else {
					Set<Long> set = new HashSet<Long>(childImages.size());
					for(ChildImage childImage : childImages){
						set.add(childImage.getId());
					}
					List<DataEntry> entries = deService.getDataEntryByChildImageIds(set);
					List<Long> idsAdded = new ArrayList<Long>(set.size()); 
					if(entries != null) {
						for(DataEntry entry:entries) {
							for(ChildImage childImage : image.childImageList){
								if(entry.getChildImage().getId() == childImage.getId()) {
									if(entry.getDeCompany()!=null) {
										childImage.setIsCompleted("complete");
									} else {
										childImage.setIsCompleted("incomplete");
									}
									idsAdded.add(childImage.getId());
									break;
								}
							}
						}
						for(ChildImage childImage : image.childImageList){
							if(!idsAdded.contains(childImage.getId())) {
								childImage.setIsCompleted("incomplete");
							}
						}
					}
				} 
				/*for(ChildImage childImage : childImages){
					DataEntry entry = deService.getDataEntryByChildImageId(childImage.getId());
					if(entry != null) {
						if(entry.getDeCompany() == null){
							image.ableToDone = false;
							break;
						}
					}
				}*/
				
			}
			Collections.reverse(parentImageList);
		}
		System.out.println("buildParentImageList :" + (System.currentTimeMillis() - start)/1000);
		return parentImageList;
	}
	
	
	public List<ParentImage> getParentImageList() {
		if(parentImageList == null || parentImageList.size() ==0) {
			parentImageList = buildParentImageList();
		}
		return parentImageList;
	}
	
	public List<ParentImage> getParentImageListfornewSci() {
		if(parentImageListfornewSci == null || parentImageListfornewSci.size() ==0) {
			parentImageListfornewSci = buildParentImageListForNewSci();
		}
	
		return parentImageListfornewSci;
	}

	public void setParentImageListfornewSci(
			List<ParentImage> parentImageListfornewSci) {
		this.parentImageListfornewSci = parentImageListfornewSci;
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
	
	public String isCompleatedChild(String id) {
		int childImgId = Integer.valueOf(id!=null?id:"0");
		if(childImgId > 0){
			DataEntry dataEntry = deService.getDataEntryByChildImageId(childImgId);
			if(dataEntry != null){
				if(dataEntry.getDeCompany() != null){
					return "complete";
				} else {
					return "incomplete";
				}
			} else {
				return "incomplete";
			}
		}
		return null;
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
				System.out.println("heigth : "+getCropHeight());
				System.out.println("width : "+getCropWidth());
				System.out.println("x1 : "+getCropX1());
				System.out.println("x2 : "+getCropX2());
				System.out.println("y1 : "+getCropY1());
				System.out.println("y2 : "+getCropY2());
				int transparency = ((BufferedImage) orig).getColorModel().getTransparency();
				BufferedImage bi = new BufferedImage(Math.round(getCropWidth()), Math.round(getCropHeight()), transparency);
				bi.createGraphics().drawImage(orig, 0, 0, 
						Math.round(getCropWidth()), 
						Math.round(getCropHeight()), 
						Math.round(getCropX1()), 
						Math.round(getCropY1()),
								Math.round(getCropX2()), 
										Math.round(getCropY2()), null);

				//save cropped area as new image file
				new File(imageBasePath+CommonProperties.getTempPath()+currentUser.getId()).mkdirs();
				int random = (int)(double)((Math.random()*(double)1000));
				File newFile=new File(imageBasePath+CommonProperties.getTempPath()+currentUser.getId()+"/crp_"+random+"_"+parentImage.getImageName());
				try{
					ImageIO.write(bi,"jpg",newFile );
					//set image to page
					croppedImageName = currentUser.getId()+"/crp_"+random+"_"+parentImage.getImageName();
					RequestContext.getCurrentInstance().execute("PF('dlg1').show();$('div[id*=\"basicDialog\"]').css('top','10px')");
					try {
						ImageIO.scanForPlugins();
						String result = new Ocr().doOCR(newFile);
						System.out.println("result: "+result);
						
						if(result != null){
							DeJob deJob = 	deService.getDeJobByParentImageId(parentImage.getId());
							DataEntry entry = new DataEntry();
							entry.setOcrText(result); 
							entry.setDeJobid(deJob);
							entry.setParentImage(parentImage);
							System.out.println("childImage on cropping:" + childImage);
							entry.setChildImage(childImage);
							deService.addDataEntry(entry);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
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
	
	public String editCompany() {
		
		if(selectedCompany!=null ) {
			List<DeCompany> companies = null; 
			//Store entry (Key/Value)of HashMap in set
	        Set mapSet = (Set) hm.entrySet();
	        //Create iterator on Set 
	        Iterator mapIterator = mapSet.iterator();
	       /* while (mapIterator.hasNext()) {
	                Map.Entry mapEntry = (Map.Entry) mapIterator.next();
	                // getKey Method of HashMap access a key of map
	                //getValue method returns corresponding key's value
	                if(this.searchValueInCompanyName.equalsIgnoreCase(mapEntry.getValue().toString()) || this.searchValueInCompanyName.equals(mapEntry.getValue().toString()) ){
	                 companies = deService.getDeCompanyBySeachCriteriaId(selectedCompany.getId());
	                }
	                System.out.println("Key : " + mapEntry.getKey() + "= Value : " + mapEntry.getValue());
	        }
			*/
	        companies = deService.getDeCompanyBySeachCriteriaId(selectedCompany.getId());
	        
			if(companies == null || companies.isEmpty()){
				 companies = deService.getDeCompanyBySeachCriteriaId(saveNewCmpId);
			}
			
			if(companies.size()>0) {
				this.companyIdEd = companies.get(0).getId();
				this.companyNameEd = companies.get(0).getCompanyName();
				this.companyURLEd = companies.get(0).getCompanyURL();
				this.departmentEd = companies.get(0).getDepartment();
				this.addressEd = companies.get(0).getAddress();
				this.address1Ed = companies.get(0).getAddress1();
				this.cityEd = companies.get(0).getCity();
				this.stateEd = companies.get(0).getState();
				this.countryEd = companies.get(0).getCountry();
				this.pincodeEd = companies.get(0).getPincode();
				RequestContext.getCurrentInstance().execute("PF('editCompany').show();setTop();");
				return null;
			} else {
				messageService.messageError(null, "Company not found.");
				return null;
			}
		}
		messageService.messageError(null, "Please select company to edit.");
		return null;
	}
	
	
/*	
	public String getCompany() {
		System.out.println("Company Name :"+this.selectedCompany + "Id "+this.selectedCompany.getId());
		if(this.selectedCompany!=null ) {
			this.selectedCompany.getId();
			List<DeCompany> companies = null; 
			//Store entry (Key/Value)of HashMap in set
	        Set mapSet = (Set) hm.entrySet();
	        //Create iterator on Set 
	        Iterator mapIterator = mapSet.iterator();
	        while (mapIterator.hasNext()) {
	                Map.Entry mapEntry = (Map.Entry) mapIterator.next();
	                //getValue method returns corresponding key's value
	                if(this.searchValueInCompanyName.equalsIgnoreCase(mapEntry.getValue().toString()) || this.searchValueInCompanyName.equals(mapEntry.getValue().toString()) ){
	                 companies = deService.getDeCompanyBySeachCriteriaId(Long.parseLong(mapEntry.getKey().toString()));
	                }
	         }
	      
	        companies = deService.getDeCompanyBySeachCriteriaId(selectedCompany.getId());
	        
	    	 if(companies == null || companies.isEmpty() ){
            	 companies = deService.getDeCompanyBySeachCriteriaId(saveNewCmpId);
               }

			if(companies.size()>0) {
				selectedCompany = companies.get(0);
				this.companyIdEd = companies.get(0).getId();
				this.companyNameEd = companies.get(0).getCompanyName();
				this.companyURLEd = companies.get(0).getCompanyURL();
				this.departmentEd = companies.get(0).getDepartment();
				this.addressEd = companies.get(0).getAddress();
				this.address1Ed = companies.get(0).getAddress1();
				this.cityEd = companies.get(0).getCity();
				this.stateEd = companies.get(0).getState();
				this.countryEd = companies.get(0).getCountry();
				this.pincodeEd = companies.get(0).getPincode();
				//RequestContext.getCurrentInstance().execute("PF('editCompany').show();setTop();");
				return null;
			} else {
				messageService.messageError(null, "Company not found.");
				return null;
			}
		}
		messageService.messageError(null, "Please select company to edit.");
		return null;
	}*/
	
	
	public void clearEditCompany() {
		this.companyIdEd = null;
		this.companyNameEd = "";
		this.companyURLEd = "";
		this.departmentEd = "";
		this.addressEd = "";
		this.address1Ed = "";
		this.cityEd = "";
		this.stateEd = "";
		this.countryEd = "";
		this.pincodeEd = "";
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
		
		String imgID = facesUtils.getRequestParameterMap("imgId");
		if(imgID != null) {
			try {
				//read image to crop and getting file extension
				File f=new File(imageBasePath+CommonProperties.getParentImagePath()+imgID+"/"+parentImage.getImageName());
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
					//RequestContext.getCurrentInstance().execute("PF('dlg1').show()");
				} catch (Exception e) {
					e.printStackTrace();
				}


		} catch (Exception e) {
			e.printStackTrace();
		}
	
		}
		
		System.out.println("IMG ID: "+imgID);
		String sourcePath = imageBasePath+CommonProperties.getTempPath()+croppedImageName;
		String targetPath = imageBasePath+CommonProperties.getChildImagePath()+parentImage.getId();
		System.out.println("src path :"+sourcePath);
		System.out.println("target path :"+ targetPath);
		try{
			if(parentImage!=null){
				File cropFile = new File(sourcePath);
				if(cropFile.isFile()){
					System.out.println("Saving parent image");
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
					childImage.setId(imgId);
					try {
						ImageIO.scanForPlugins();
						File newFile=new File(imageBasePath+CommonProperties.getTempPath()+currentUser.getId()+"/crp_"+random+"_"+parentImage.getImageName());
						String result = new Ocr().doOCR(newFile);
						System.out.println("result: "+result);
						if(result != null) {
							DeJob deJob = deService.getDeJobByParentImageId(parentImage.getId());
							DataEntry entry = new DataEntry();
							entry.setOcrText(result); 
							entry.setDeJobid(deJob);
							entry.setParentImage(parentImage);
							entry.setChildImage(fetchChildImageById(imgId));
							deService.addDataEntry(entry);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					targetPath = targetPath+"/"+imgId;
					new File(targetPath).mkdirs();
					IoUtils.copyImages(sourcePath,targetPath,filename);
					try {	
						Thumbnails.of(new File(targetPath+File.separator+filename))
			        	.size(200, 300).keepAspectRatio(true)
			        	.outputFormat("jpg")
			        	.toFile(targetPath+File.separator+filename.split("\\.")[0]+"_thumb.jpg");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					new File(sourcePath).delete();
					messageService.messageInformation(null, "Image cropped Successfully.");
					RequestContext.getCurrentInstance().execute("PF('dlg1').hide()");
					//return RETURN_CHILD_IMAGE;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return  null ;
	}

	private ChildImage fetchChildImageById(Long childImageId) {
		System.out.println("fetchChildImageById " + childImageId);
		ChildImage childImage	= childImageService.getChildImageById(childImageId);
		System.out.println("fetchChildImageById object " + childImage);
		return childImage;
	}

	/**
	 * delete parent image from db and path
	 */
	public void deleteParentImage(){
		parentImageList = new ArrayList<ParentImage>();
		long start = System.currentTimeMillis();
		currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
		String imgId = facesUtils.getRequestParameterMap("imgId");
		if(imgId!=null){
			ParentImage parentImage = getParentImageService().getParentImageById(Long.valueOf(imgId));
			if(parentImage!=null){
				DeJob deJob = 	deService.getDeJobByParentImageId(parentImage.getId());
				List<ChildImage> child = getChildImageService().getChildImagesByParent(Long.valueOf(imgId));
				DeletedImage deletedImage=new DeletedImage();
				deletedImage.setImageId(parentImage.getId());
				deletedImage.setImageName(parentImage.getImageName());
				deletedImage.setDeleted_by(currentUser.getId());
				deletedImage.setDeleted_On(new Date());
				deletedImage.setIsChild(false);
				Long flag=getDeletedImageService().addDeletedImage(deletedImage);
				if(flag>0){
					if(child != null || child.size() != 0 ){
						for(ChildImage chImage : child){
							deleteChildImageByID(chImage.getId());
						}
					}
					getParentImageService().deleteParentImage(parentImage);
					if(deJob != null){
						deService.deleteDeJob(deJob);
					}

					messageService.messageInformation(null, "Image deleted Successfully.");
				}
				else{
					messageService.messageFatal(null, "Image can not be deleted.");
				}
			}
		}
		System.out.println("deleteParentImage" + (System.currentTimeMillis() - start)/1000l);
	}

	/**
	 * delete child image from db and path
	 */
	public void deleteChildImage(){
		currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
		String imgId = facesUtils.getRequestParameterMap("childImgId");
		if(imgId!=null){
			if(deleteChildImageByID(Long.valueOf(imgId))){
				messageService.messageInformation(null, "Image deleted Successfully.");
			} else {
				messageService.messageInformation(null, "Image can not be deleted.");
			}
		}
		parentImageList = new ArrayList<ParentImage>();
	}

	private boolean deleteChildImageByID(Long imgId) {
		ChildImage childImage = getChildImageService().getChildImageById(imgId);
		if(childImage!=null){
			DataEntry dataEntry=deService.getDataEntryByChildImageId(imgId);
			DeletedImage deletedImage=new DeletedImage();
			deletedImage.setImageId(childImage.getId());
			deletedImage.setImageName(childImage.getImageName());
			deletedImage.setDeleted_by(currentUser.getId());
			deletedImage.setDeleted_On(new Date());
			deletedImage.setIsChild(true);
			Long flag=getDeletedImageService().addDeletedImage(deletedImage);
			if(flag>0){
				if(dataEntry != null){
					deService.deleteDataEntry(dataEntry);
				}
				getChildImageService().deleteChildImage(childImage);
				return true;
			}
		}
		return false;
	}

	/**
	 * save cropped image from temp path
	 * @throws IOException 
	 */
	
	public boolean isDuplicate(String name) {
		return duplicateNames!=null ? duplicateNames.contains(name) : false;
	}
	
	public String saveParentImage() throws IOException{
		currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
		String sourcePath = imageBasePath+CommonProperties.getParentImageTempPath()+"/"+currentUser.getId();
		String fileNameWithExt ="";
		String extension ="";
		Publication publication = null;
		Publication sectionTitle = null;
		String dateField = "";
		if(duplicateFileNames==null) {
			duplicateFileNames = new HashSet<String>();
		} else {
			if(!duplicateFileNames.isEmpty()) {
				duplicateFileNames.clear();
			}
		}
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
							String orgFilename = files[i].getName();
							System.out.println("before orgFilename: "+orgFilename);
							String[] split = filename.split("\\.");
							extension = split[split.length - 1];
							String file=split[split.length - 2];
							orgFilename = file+"."+extension;
							file=file.replaceAll(" ", "_");
							filename=file+"."+extension;
							if(extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif")){
								ParentImage duplicateImage = getParentImageService().getParentImageByName(filename);
								if(duplicateImage!=null) {
									duplicateFileNames.add(filename);
								}
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
								parentImage.setId(imgId);
								sourcePathImage = sourcePath+"/"+parentImage.getImageName();
								targetPathImage = targetPathImage+"/"+imgId;
								new File(targetPathImage).mkdirs();
								IoUtils.copyImages(sourcePathImage,targetPathImage,parentImage.getImageName());
								try {	
									Thumbnails.of(new File(targetPathImage+File.separator+parentImage.getImageName()))
						        	.size(200, 300).keepAspectRatio(true)
						        	.outputFormat("jpg")
						        	.toFile(targetPathImage+File.separator+parentImage.getImageName().split("\\.")[0]+"_thumb.jpg");
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								String publicationTitle = null;
								String section = null;
								Integer day = null;
								Integer month = null;
								Integer year = null;
								if(filename != null && !filename.isEmpty()) {
									String[] arr = filename.split("_");
									
									System.out.println("arr file  name len: "+arr.length);
									if(arr.length==4 || arr.length==3) {
										if(arr[0].contains("newsci")) {
											if(arr.length == 4){
												String ver = arr[3].split("\\.")[0];
												if(ver.equalsIgnoreCase("A")) {
													publicationTitle = titleMap.get("newus");
												} else if(ver.equalsIgnoreCase("B")) {
													publicationTitle = titleMap.get("newuk");
												} else {
													publicationTitle = titleMap.get("natgbl");
												}
											} else {
												publicationTitle = titleMap.get("natgbl");
											}
										} else {
											publicationTitle = titleMap.get(arr[0]);
										}
										this.page = arr[2];
										String[] arr1  = arr[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
										try {
											if(arr1.length==2) {
												day = Integer.parseInt(arr1[0].substring(4, 6));
												month = Integer.parseInt(arr1[0].substring(2, 4));
												section = sectionMap.get(arr1[1].toUpperCase());
												year = 2000+Integer.parseInt(arr1[0].substring(0, 2));
											} else {
												try {
													Integer.parseInt(arr1[0]);
													day = Integer.parseInt(arr1[0].substring(4, 6));
													month = Integer.parseInt(arr1[0].substring(2, 4));
													year = 2000+Integer.parseInt(arr1[0].substring(0, 2));
												} catch(Exception e) {
													section = sectionMap.get(arr1[1]);
												}
											}
										} catch(Exception e) {
											e.printStackTrace();
										}
									}
									if(publicationTitle != null && !publicationTitle.isEmpty()){
										publication = userService.getPublicationById(Integer.valueOf(publicationTitle));
									}
									if(section != null && !section.isEmpty()){
										sectionTitle = userService.getPublicationById(Integer.valueOf(section));
									}
									if(day != null && month != null && year != null ){
										dateField=year+"-"+month+"-"+day;
									}
									parentImage.setSectionother("");
									parentImage.setSectionspecialRegional("");
									parentImage.setSectionspecialTopic("");
									if(parentImage != null && publication != null && sectionTitle != null && dateField != null){
										parentImage.setPublicationTitle(publication);
										SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
										Date issueDate;
										try {
											issueDate = (Date)formatter.parse(dateField);
											parentImage.setIssueDate(issueDate);
										} catch (ParseException e) {
										}
										parentImage.setSection(sectionTitle);
										parentImage.setPage(this.page);
										parentImageService.updateParentImage(parentImage);
										messageService.messageInformation(null, " Publication has been Updated  Successfully.");
									}
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
								}
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
		this.msgWarnLabel = "";
		if(!duplicateFileNames.isEmpty()) {
			StringBuffer sb = new StringBuffer();
			sb.append("These file with names ");
			for(String s:duplicateFileNames) {
				sb.append(s).append(",");
			}
			sb.append(" are duplicated.");
			this.msgWarnLabel = sb.toString() ;
			messageService.messageWarning(null, this.msgWarnLabel);
		}
		return "/pages/de/gallery.xhtml?msgLabel="+this.msgLabel+"&msgFormat="+this.msgFormat+"&faces-redirect=true&msgWarnLabel="+this.msgWarnLabel;
	}

	/**
	 * 
	 * @param event
	 */
	public void updateParentImageByPub(ValueChangeEvent event){ 
			//assign new value to localeCode
			this.selectedPub  = event.getNewValue().toString();
	}
	
	public void updateparentImagePubById(String id){
		ParentImage p = parentImageService.getParentImageById(Long.parseLong(id));
		if(selectedPub.equalsIgnoreCase("US") ){
	    	String publicationTitle = new String();
	    	publicationTitle = titleMap.get("newus");
	    	if(publicationTitle != null && !publicationTitle.isEmpty()){
	    		Publication publication = userService.getPublicationById(Integer.valueOf(publicationTitle));
	    		p.setPublicationTitle(publication);
	    	}	
	    
		}else if(selectedPub.equalsIgnoreCase("UK")){
	    	publicationTitle = titleMap.get("newuk");
	    
	    	if(publicationTitle != null && !publicationTitle.isEmpty()){
	    		Publication publication = userService.getPublicationById(Integer.valueOf(publicationTitle));
	    		p.setPublicationTitle(publication);
	    	}
	    
		}else if(selectedPub.equalsIgnoreCase("GBL")){
	    	publicationTitle = titleMap.get("natgbl");
	    	
	    	if(publicationTitle != null && !publicationTitle.isEmpty()){
	    		Publication publication = userService.getPublicationById(Integer.valueOf(publicationTitle));
	    		p.setPublicationTitle(publication);
	    	}
	    }
		
	    String publicationName  =  p.getImageName();
	    if(selectedPub.equalsIgnoreCase("US") ){
		   String pubs[]=  p.getImageName().split("\\.");
	    	publicationName = pubs[0]+"_"+"A"+"."+pubs[1];;   	
	    }else if(selectedPub.equalsIgnoreCase("UK")){
	    	String pubs[]=  p.getImageName().split("\\.");
	    	publicationName = pubs[0]+"_"+"B"+"."+pubs[1];
	    }else if(selectedPub.equalsIgnoreCase("GBL")){
	    	String pubs[]=  p.getImageName().split("\\.");
	    	publicationName = pubs[0]+"_"+"C"+"."+pubs[1];
	    }
	   
	    p.setImageName(publicationName);
	    parentImageService.updateParentImage(p);
		
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
		if(com.obs.brs.utils.StringUtility.isNotEmpty(this.msgWarnLabel))
		{
			FacesContext.getCurrentInstance().addMessage("deo", new FacesMessage(FacesMessage.SEVERITY_WARN,this.msgWarnLabel,this.msgWarnLabel));
			this.msgWarnLabel="";
		}
	}
	/** 
	 *  edit publication if id > 0
	 */
	public String updateAndExitPublicationByParentImage() 
	{
		String label = "";
		if(this.msgLabel!=null)
			label = this.msgLabel;
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
						|| (this.issueDay == 0 && this.issueDayNext == 0 &&  this.issueDayNextThird == 0 && this.issueDayNextFour == 0 )
						|| (this.issueMonth == 0 && this.issueMonthNext == 0)
						|| this.issueYear  == 0){
					messageService.messageFatal(null, "You must fill all required fields");
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
						label = " Publication has been Updated  Successfully.";
						messageService.messageInformation(null, label);
						return "/pages/de/gallery.xhtml?msgLabel="+label+"&msgFormat=1&faces-redirect=true";
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
				return "/pages/de/gallery.xhtml?msgLabel="+label+"faces-redirect=true";
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return "/pages/de/gallery.xhtml?msgLabel="+label+"faces-redirect=true";
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
			
			this.section ="";
			this.sectionNextValue="";
			this.sectionother="";
			this.sectionspecialRegional="";
			this.sectionspecialTopic="";
			
			User currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);	
			FacesUtils facesUtils = new FacesUtils();
			String val = facesUtils.getRequestParameterMap("parentImg");
			String checkin = facesUtils.getRequestParameterMap("checkin");
			
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
			if(checkin!=null) {
				if(this.parentImageName!=null && !this.parentImageName.isEmpty()) {
					String[] arr = this.parentImageName.split("_");
					if(arr.length==3) {
						this.publicationTitle = titleMap.get(arr[0]);
						String[] arr1  = arr[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
						try {
							if(arr1.length==2) {
								int day = Integer.parseInt(arr1[0].substring(4, 6));
								int month = Integer.parseInt(arr1[0].substring(2, 4));
								if(day<9) {
									this.setIssueDay(day);
								} else if(day<17) {
									this.setIssueDayNext(day);
								} else if(day<25) {
									this.setIssueDayNextThird(day);
								} else {
									this.setIssueDayNextFour(day);
								}
								if(month<7) {
									this.setIssueMonth(month);
								} else {
									this.setIssueMonthNext(month);
								}
								
								if(sectionMap.get(arr1[1].toUpperCase()).equals("644") || sectionMap.get(arr1[1].toUpperCase()).equals("1033")) {
									this.setSection(sectionMap.get(arr1[1].toUpperCase()));
								} else {
									this.setSectionNextValue(sectionMap.get(arr1[1].toUpperCase()));
								}
								this.setIssueYear(2000+Integer.parseInt(arr1[0].substring(0, 2)));
								
							} else {
								try {
									Integer.parseInt(arr1[0]);
									this.setIssueDay(Integer.parseInt(arr1[0].substring(4, 6)));
									this.setIssueMonth(Integer.parseInt(arr1[0].substring(2, 4)));
									this.setIssueYear(2000+Integer.parseInt(arr1[0].substring(0, 2)));
								} catch(Exception e) {
									this.setSection(sectionMap.get(arr1[1]));
								}
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
						this.page = arr[2].split("\\.")[0];
					}
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private long parentImgIdOcr;
	public long getParentImgIdOcr() {
		return parentImgIdOcr;
	}

	public void setParentImgIdOcr(long parentImgIdOcr) {
		this.parentImgIdOcr = parentImgIdOcr;
	}

	public String getParentImgChildImgNameOcr() {
		return parentImgChildImgNameOcr;
	}

	public void setParentImgChildImgNameOcr(String parentImgChildImgNameOcr) {
		this.parentImgChildImgNameOcr = parentImgChildImgNameOcr;
	}

	public long getParentImgChildIdOcr() {
		return parentImgChildIdOcr;
	}

	public void setParentImgChildIdOcr(long parentImgChildIdOcr) {
		this.parentImgChildIdOcr = parentImgChildIdOcr;
	}

	private String  parentImgChildImgNameOcr;
	private long parentImgChildIdOcr;
	
	
	
	public long getParentParId() {
		return parentParId;
	}

	public void setParentParId(long parentParId) {
		this.parentParId = parentParId;
	}

	public long getParentParChildId() {
		return parentParChildId;
	}

	public void setParentParChildId(long parentParChildId) {
		this.parentParChildId = parentParChildId;
	}

	public String getParentParChildName() {
		return parentParChildName;
	}

	public void setParentParChildName(String parentParChildName) {
		this.parentParChildName = parentParChildName;
	}

	private long parentParId;
	private long parentParChildId;
	private String parentParChildName;
	
    public String callOcrRelvancePopup(){
    	
		FacesUtils facesUtils = new FacesUtils();
	    this.parentImgChildIdOcr = Long.parseLong(facesUtils.getRequestParameterMap("parentImgChildIdOcr"));
	    this.parentImgChildImgNameOcr = facesUtils.getRequestParameterMap("parentImgChildImgNameOcr");
	    this.parentImgIdOcr = Long.parseLong (facesUtils.getRequestParameterMap("parentImgIdOcr"));
    
	    this.parentParId = Long.parseLong(facesUtils.getRequestParameterMap("parentParId"));
	    this.parentParChildName = facesUtils.getRequestParameterMap("parentParChildName");
	    this.parentParChildId = Long.parseLong (facesUtils.getRequestParameterMap("parentParChildId"));
	    
	    System.out.println("parentParId: "+this.parentParId);
	    System.out.println("parentParChildName; "+this.parentParChildName);
	    System.out.println("parentImgChildIdOcr: "+this.parentImgChildIdOcr);
    	System.out.println("parentImgChildImgNameOcr: "+this.parentImgChildImgNameOcr);
    	System.out.println("parentImgIdOcr: "+this.parentImgIdOcr);
    	
	    return null;
    }	
	
	public String showImage (){
		String parentId = facesUtils.getRequestParameterMap("parentId");
 		String parentImage = facesUtils.getRequestParameterMap("parentImg");
		String childId = facesUtils.getRequestParameterMap("childId");
		String childImage = facesUtils.getRequestParameterMap("childImg");
		System.out.println("Child Id "+childId);
		
		System.out.println("parent id: "+parentId);
		this.parentImageId = Long.parseLong(parentId);
		this.parentImageName = parentImage;
		this.childImageName = childImage;
		this.childImageId = Long.parseLong(childId);
		
		return null;
	
	}
	
	/**
	 * Get CompanyUser List
	 * 
	 * @return List - CompanyUser List
	 */
	public List<DeJob> getDeJobListBySeachCriteria() {
		if(deJobList == null || isSendToQC){
			isSendToQC = false;
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
			for(DeJob job : deJobList) {
				List<DataEntry> dataEntries = getDeService().getImagesByJobid(job.getId());
				List<ChildImage> childImages = getChildImageService().getChildImagesByParent(job.getParentImage().getId());
				long compleated = 0;
				for(DataEntry data : dataEntries){
					if(data.getDeCompany() != null){
						compleated++;
					}
				}
				job.setIsCompleted(compleated+"/"+childImages.size());			
				job.setRender(compleated == childImages.size());
			}
			
			Collections.reverse(deJobList);
		}
		
		return deJobList;
	}
	
	public void sendJobToQC() {
		int jobid = Integer.valueOf( facesUtils.getRequestParameterMap("deJobId")!=null? facesUtils.getRequestParameterMap("deJobId"):"0");
		getDeService().sendJobToQC(jobid);
		isSendToQC = true;
	}
	
	
	public List<DeJob> gettestDeJobListBySeachCriteria() {
		
		if(deJobList != null){
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
		}
		return deJobList;
	}
	
	public String getStatus(int id) {
		//Object sessionObj = sessionManager.getSessionAttribute(SessionManager.CROPIMAGE);
	
		String status="";
		status = getDeService().getStatusOfChildImageCompletion(id);
		if(status == null){
			status = 0+"";
		}
		
		return status;
	}
	
	public boolean isRender(int id) {
		return getDeService().isRender(id);
	}
	
	
	public List<DataEntry> getQcJobListBySeachCriteria() {
		if(this.isFilter) {
			this.isFilter = false;
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
			this.createdByDeo = (String) sessionManager.getSessionAttribute(sessionManager.CREATEDBYDEO);
			if(this.issueDatePubSearch == null && (this.publicationId==null || this.publicationId.equals(""))){
				dataEntryuserList.addAll(getDeService().geAllQcJob());
				return dataEntryuserList;
			}
			if(this.issueDatePubSearch == null){
				this.issueDatePubSearch = "";
			}
			if(this.createdByDeo == null) {
				this.createdByDeo = "";
			}
			if(this.jobStatus == 0 && this.searchValue.equals("")){
				dataEntryuserList.addAll(getDeService().geQcJobBySeach(this.publicationId,this.issueDatePubSearch,this.createdByDeo));
			}else if(this.jobStatus >= 0 ){
				dataEntryuserList.addAll(getDeService().geQcJobBySeachCriteria(this.jobStatus,this.searchValue,this.publicationId,this.issueDatePubSearch,this.createdByDeo));
			}
			Collections.reverse(dataEntryuserList);
			for(DataEntry dataEntry: dataEntryuserList){
				checkedQc.put(dataEntry.getId(),Boolean.FALSE);
			}
		}
		return dataEntryuserList;
	}
	public List getQcJobListBySeachCriteriaNew(){
		List qcjobByJournal = new ArrayList();
		qcjobByJournal = getDeService().getDeJobByQC();
		return qcjobByJournal;
		
	}
	
	public List getAllDeo() {
		List result = getDeService().getAllDeo();
		return result;
	}
	
	public List getQcJournalView(){
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
		if(this.publicationId != null && this.publicationId !="" && !this.publicationId.equals("") ){
			issueDateByPublication = getDeService().getIssueDateByPublication(this.publicationId);
		}
		List issueDateString  = new ArrayList();
		for(int i=0;i<issueDateByPublication.size();i++){
			//Date[] obj=(Date[])issueDateByPublication.get(i);
			String DateStr = formatter.format(issueDateByPublication.get(i));
			issueDateString.add(DateStr);
		}
		return issueDateString;
	}
	
	public void sendQcJournalView(ValueChangeEvent event)	
	{ 
		this.isFilter = true;
		this.publicationId =((String) event.getNewValue());
		if(this.publicationId.equals("all"))
		{
			sessionManager.setUserInSession(SessionManager.PUBLICATIONID, "");
		}else{
			sessionManager.setUserInSession(SessionManager.PUBLICATIONID, this.publicationId);
		}
		
	}

	public void sendIssueDate(ValueChangeEvent event)	
	{ 
		this.isFilter = true;
		this.issueDatePubSearch =((String) event.getNewValue());
		if(this.issueDatePubSearch.equals("all"))
		{
			sessionManager.setUserInSession(SessionManager.ISSUEDATE, "");
		}else{
			sessionManager.setUserInSession(SessionManager.ISSUEDATE, this.issueDatePubSearch);
		}
		
	}
	
	public void sendCretedByDeo(ValueChangeEvent event)	
	{ 
		this.isFilter = true;
		this.createdByDeo =((String) event.getNewValue());
		if(this.createdByDeo.equals("all"))
		{
			sessionManager.setUserInSession(SessionManager.CREATEDBYDEO, "");
		}else{
			sessionManager.setUserInSession(SessionManager.CREATEDBYDEO, this.createdByDeo);
		}
		
	}
	
	/**
	 * redirect add or edit company users 
	 * @return
	 */
	public String loadAddOrEditDeChild(){
		String flag=null;
		int jobid = Integer.valueOf( facesUtils.getRequestParameterMap("jobid")!=null? facesUtils.getRequestParameterMap("jobid"):"0");
		int deJobId = (int) deService.getDeJobByParentImageId(Integer.valueOf(facesUtils.getRequestParameterMap("deJobId")!=null?facesUtils.getRequestParameterMap("deJobId"):"0")).getId();
		if(deJobId >0 ){
			sessionManager.setSessionAttributeInSession(SessionManager.EDITUSER, deJobId);
			flag=saveAndExitDeData();
		}
		else
			sessionManager.removeSessionAttributeInSession(SessionManager.EDITUSER);
		if(jobid >0 ){
			sessionManager.setSessionAttributeInSession(SessionManager.CHILDIMAGEID, jobid);
			flag=saveAndExitDeData();
		}
		else
			sessionManager.removeSessionAttributeInSession(SessionManager.CHILDIMAGEID);
		
		if(deJobId >0 && flag==null){
			return DATAENTRY;
		}
		return null;
	}
	
	
	public void changeNextVal()
	{
		clear();
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
					File image=new File(imageBasePath+CommonProperties.getChildImagePath()+childImage.getParentImage().getId()+"/"+this.childImageId+"/"+this.childImageName);
					ImageIO.scanForPlugins();
					String result = new Ocr().doOCR(image);
					if(result != null){
						this.ocrText = result;
					}
				} catch (Exception e) {
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
	
	public String resetVal() {
		clear();
		FacesUtils facesUtils = new FacesUtils();
		long baseId = Long.valueOf(facesUtils.getRequestParameterMap("baseId"));
		long jobid = Long.valueOf(facesUtils.getRequestParameterMap("jobId"));
		DataEntry childImage=deService.getCurrentEntry(baseId,jobid);
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
					File image=new File(imageBasePath+CommonProperties.getChildImagePath()+childImage.getParentImage().getId()+"/"+this.childImageId+"/"+this.childImageName);
					ImageIO.scanForPlugins();
					String result = new Ocr().doOCR(image);
					if(result != null){
						this.ocrText = result;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//setAllAdDetails(childImages);
			if(childImage.getDeCompany() != null){
				setAllDeCompanyDetails(childImage.getDeCompany());
			}
		}
		if(childImage==null){
			this.checkVal=true;
		} else if(this.childImageId==childImagesDataList.get(childImagesDataList.size()-1).getChildImage().getId()){
			this.checkVal=true;
		} else{
			this.checkVal=false;
		}
		if(childImage==null){
			this.checkPreVal=true;
		} else if(this.childImageId==childImagesDataList.get(0).getChildImage().getId()){
			this.checkPreVal=true;
		} else{
			this.checkPreVal=false;
		}
		return null;
	}

	public void changePreVal()
	{
		clear();
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
						File image=new File(imageBasePath+CommonProperties.getParentImagePath()+childImages.getParentImage().getId()+"/"+this.parentImageName);
						ImageIO.scanForPlugins();
						String result = new Ocr().doOCR(image);
						if(result != null){
							this.ocrText = result;
						}
					} catch (Exception e) {
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
	public List<DeCompany> companyDetails(String query) {
		
		List<String> results = new ArrayList<String>();
		 hm = new HashMap();
		deCompanyList = deService.getDeCompany();
		List<DeCompany> deCompanySearchQueryList = new ArrayList<DeCompany>();
		if(deCompanyList != null &&  deCompanyList.size() > 0){
			for(int i = 0; deCompanyList.size()>i; i++) {
				DeCompany deCompany = deCompanyList.get(i);
				if(deCompany.getCompanyName()!=null && query!=null && deCompany.getCompanyName().toLowerCase().startsWith(query.toLowerCase())) {
					results.add(deCompany.getCompanyName());
					hm.put(deCompany.getId(),deCompany.getCompanyName());
					deCompanySearchQueryList.add(deCompanyList.get(i));
				}
			}
		}
		
		//public List<String>	getcompaniesId(String query);
		return deCompanySearchQueryList;
	}

	
public List<String> getcompaniesId(String query) {
		
		List<String> results = new ArrayList<String>();
		deCompanyList = deService.getDeCompany();
		if(deCompanyList != null &&  deCompanyList.size() > 0){
			for(int i = 0; deCompanyList.size()>i; i++) {
				DeCompany deCompany = deCompanyList.get(i);
				if(deCompany.getCompanyName().toLowerCase().startsWith(query.toLowerCase())) {
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
				if(advertiserType.getPublicationTitle().toLowerCase().startsWith(query.toLowerCase())) {
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
				if(country.getCountryName().toLowerCase().startsWith(query.toLowerCase())) {
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
				if(state.getStateName().toLowerCase().startsWith(query.toLowerCase())) {
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
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateMemo(ParentImage image,Integer index) {
		if(image.getSection().getPublicationTitle().equals("Special-Topic")) {
			image.setSectionspecialTopic(image.getMemo());
		} else if(image.getSection().getPublicationTitle().equals("Special-Regional")) {
			image.setSectionspecialRegional(image.getMemo());
		} else {
			image.setSectionother(image.getMemo());
		}
		deService.updateParentImage(image);
		int i;
		for(i = this.imageOffset; i<this.imageOffset+this.imagePerPage;i++) {
			if(getParentImageList().get(i).getId() == image.getId()) {
				break;
			}
		}
		getParentImageList().set(i, image);
	}
	
	public String makeLiveByScore(int index) {
		DataEntry dataEntry = null;
		ScoreData scoreData = this.deReleavanceImageList.get(index);
		System.out.println("------- index -----"+index);
		System.out.println("------- liveStatusId -----"+this.liveStatusArr[index]);
		for(DataEntry d : scoreData.dataEntry ) {
			System.out.println("id in dataentries :"+d.getId());
			if(d.getId() == this.liveStatusArr[index]) {
				dataEntry = d;
				break;
			}
		}
		
		DataEntry mainEntry = scoreData.getdEntry();
		mainEntry.setAdCategory(dataEntry.getAdCategory());
		mainEntry.setAddColumn(dataEntry.getAddColumn());
		mainEntry.setAdHeadLine(dataEntry.getAdHeadLine());
		mainEntry.setAdOrientation(dataEntry.getAdCategory());
		mainEntry.setAdSize(dataEntry.getAdSize());
		mainEntry.setAdvertiserType(dataEntry.getAdvertiserType());
		mainEntry.setContactInfo(dataEntry.getContactInfo());
		mainEntry.setCurrency(dataEntry.getCurrency());
		mainEntry.setEndCurrencyRange(dataEntry.getEndCurrencyRange());
		mainEntry.setIsProcessedOcr(0);
		mainEntry.setJobDensity(dataEntry.getJobDensity());
		mainEntry.setLandingPageURL(dataEntry.getLandingPageURL());
		mainEntry.setOcrText(dataEntry.getOcrText());
		mainEntry.setStartCurrencyRange(dataEntry.getStartCurrencyRange());
		mainEntry.setDeCompany(dataEntry.getDeCompany());
		mainEntry.setSearchValueAdvertisertype(dataEntry.getSearchValueAdvertisertype());
		deService.updateDataEntry(mainEntry);
		List<DataEntry> dataEntries = getDeService().getImagesByJobid(mainEntry.getDeJobid().getId());
		if(dataEntries.size() == 1) {
			mainEntry.getDeJobid().setStatus(dataEntry.getDeJobid().getStatus());
			deService.updateDeJob(mainEntry.getDeJobid());
			mainEntry.getParentImage().setStatus(2);
			deService.updateParentImage(mainEntry.getParentImage());
			this.deReleavanceImageList.remove(scoreData);
		} else {
			System.out.println("here in ");
			int completed = 0;
			for(DataEntry data : dataEntries){
				if(data.getDeCompany() != null){
					completed++;
				}
			}
			
			System.out.println("completed ------ "+completed);
			
			System.out.println("total ------ "+dataEntries.size());
			
			if(completed == dataEntries.size()) {
				mainEntry.getDeJobid().setStatus(dataEntry.getDeJobid().getStatus());
				deService.updateDeJob(mainEntry.getDeJobid());
				mainEntry.getParentImage().setStatus(2);
				deService.updateParentImage(mainEntry.getParentImage());
				this.deReleavanceImageList.remove(scoreData);
			}
		}
		
		System.out.println("Score data dEntry Id: "+scoreData.getdEntry().getId());
		//System.out.println("Score data dEntry Id: "+scoreData.);
		getParentImageService().updateOcrTextIDDupDetails(String.valueOf(scoreData.getdEntry().getId()),liveStatusArr[index]);
		System.out.println("size after : "+this.deReleavanceImageList.size());
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
		this.isFilter = true;
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
			for(int i=0;i<publicationList.size();i++)
			{
				Publication publication = publicationList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(publication.getId());
				sItem.setLabel(publication.getPublicationTitle());
				sectionList.add(sItem);
			}
			return sectionList;

		} catch (Exception e) {
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
			String rowsPerPage = this.sessionManager.getSessionAttribute("rowsPerPage")!=null?this.sessionManager.getSessionAttribute("rowsPerPage").toString():null;
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
		count  = 1;
		String perPage = event.getNewValue().toString();
		imagePerPage = Integer.parseInt(perPage);
		sessionManager.setSessionAttributeInSession(SessionManager.IMAGEPERPAGE, perPage);
		loadPagination();
	}
	
	
	/**
	 * 
	 * @param event
	 */
	public void localImageChangedOcr(ValueChangeEvent event)	
	{ 
		count  = 1;
		String perPage = event.getNewValue().toString();
		imagePerPageOcr = Integer.parseInt(perPage);
		sessionManager.setSessionAttributeInSession(SessionManager.IMAGEPERPAGEOCR, perPage);
		loadPaginationOcr();
	}
	/**
	 * 
	 * @param event
	 */
	public void localImageChangedNewSci(ValueChangeEvent event)	
	{ 
		count  = 1;
		String perPage = event.getNewValue().toString();
		imagePerPageNewSci = Integer.parseInt(perPage);
		sessionManager.setSessionAttributeInSession(SessionManager.IMAGEPERPAGENEWSCI, perPage);
		loadPaginationNewSci();
	}
	
	
	
	
	/**
	 * pagination method for next page 
	 */
	public void nxtPage(){
		count  = 1;
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGE); 
			//System.out.println("rowsPerPage"+rowsPerPage);
			if(rowsPerPage!=null){
				imagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
			imageOffset +=Integer.valueOf(imagePerPage);
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSET, imageOffset);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * pagination method for next page 
	 */
	public void nxtPageOcr(){
		count  = 1;
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGEOCR); 
			//System.out.println("rowsPerPage"+rowsPerPage);
			if(rowsPerPage!=null){
				imagePerPageOcr = Integer.valueOf(rowsPerPage.toString());
			}
			imageOffsetOcr +=Integer.valueOf(imagePerPageOcr);
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSETOCR, imageOffsetOcr);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * pagination method for next page 
	 */
	public void nxtPageNewSci(){
		count  = 1;
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGENEWSCI); 
			//System.out.println("rowsPerPage"+rowsPerPage);
			if(rowsPerPage!=null){
				imagePerPageNewSci = Integer.valueOf(rowsPerPage.toString());
			}
			
			imageOffsetnewSci +=Integer.valueOf(imagePerPageNewSci);
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSETNEWSCI, imageOffsetnewSci);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * for previous page
	 */
	public void prevPage(){
		count  = 1;
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGE);
			if(rowsPerPage!=null){
				imagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
			if(imageOffset>1)
				imageOffset = imageOffset - Integer.valueOf(imagePerPage);
			
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSET, imageOffset);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * for previous page
	 */
	public void prevPageNewSci(){
		
		count  = 1;
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGENEWSCI);
			if(rowsPerPage!=null){
				imagePerPageNewSci = Integer.valueOf(rowsPerPage.toString());
			}
			if(imageOffsetnewSci>1)
				imageOffsetnewSci = imageOffsetnewSci - Integer.valueOf(imagePerPageNewSci);
			
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSETNEWSCI, imageOffsetnewSci);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * for previous page Ocr releavance
	 */
	public void prevPageOcr(){
		count  = 1;
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGEOCR);
			if(rowsPerPage!=null){
				imagePerPageOcr = Integer.valueOf(rowsPerPage.toString());
			}
			if(imageOffsetOcr>1)
				imageOffsetOcr = imageOffsetOcr - Integer.valueOf(imagePerPageOcr);
			
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSETOCR, imageOffsetOcr);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void gotoPage() {
		count  = 1;
		int page = Integer.valueOf(facesUtils.getRequestParameterMap("page"));
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGE);
			if(rowsPerPage!=null){
				imagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
			imageOffset = (page-1) * imagePerPage;
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSET, imageOffset);
			loadPagination();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void gotoPageOcr() {
		count  = 1;
		int page = Integer.valueOf(facesUtils.getRequestParameterMap("page"));
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGEOCR);
			if(rowsPerPage!=null){
				imagePerPageOcr = Integer.valueOf(rowsPerPage.toString());
			}
			imageOffsetOcr = (page-1) * imagePerPageOcr;
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSETOCR, imageOffsetOcr);
			loadPaginationOcr();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void gotoPageNewSci() {
		System.out.println("in  gotoPageNewSci");
		count  = 1;
		int page = Integer.valueOf(facesUtils.getRequestParameterMap("page"));
		
		System.out.println("page: "+page);
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGENEWSCI);
			
			if(rowsPerPage!=null){
				imagePerPageNewSci = Integer.valueOf(rowsPerPage.toString());
			}

			imageOffsetnewSci = (page-1) * imagePerPageNewSci;
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSETNEWSCI, imageOffsetnewSci);
			loadPaginationNewSci();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void changeParentImages() {
		if(this.duplicate) {
			List<ParentImage> images = new ArrayList<ParentImage>();
			for(ParentImage image:getParentImageList()) {
				if(duplicateNames.contains(image.getImageName())) {
					images.add(image);
				}
			}
			this.parentImageList = images;
			this.duplicate = false;
		} else {
			this.parentImageList = null;
			getParentImageList();
			this.duplicate = true;
		}
	}
	
	/**
	 * for first page record
	 */
	public void firstPage(){
		count =1;
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGE);
			if(rowsPerPage!=null){
				imagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
			if(imageOffset>1)
				imageOffset = 0;
			
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSET, imageOffset);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * for first page record
	 */
	public void firstPageNewSci(){
			count =1;
			try{
				Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGENEWSCI);
				if(rowsPerPage!=null){
					imagePerPageNewSci = Integer.valueOf(rowsPerPage.toString());
				}
				if(imageOffsetnewSci > 1)
					imageOffsetnewSci = 0;
				
				sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSETNEWSCI, imageOffsetnewSci);

			}catch(Exception e){
				e.printStackTrace();
			}
		
		
	}
	
	
	
	/**
	 * for first page record for ocr releavance
	 */
	
	public void firstPageOcr(){
		count =1;
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGEOCR);
			if(rowsPerPage!=null){
				imagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
			if(imageOffsetOcr > 1)
				imageOffsetOcr = 0;
			
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSETOCR, imageOffsetOcr);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * for first page record
	 */
	public void lastPage(){
		count  = 1;
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
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSET, imageOffset);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * for first page record
	 */
	public void lastPageOcr(){
		count  = 1;
		try{
			int listSize = 0;
			int pagecount;
			int remainder;
			FacesUtils facesUtils = new FacesUtils();
			String catagory=facesUtils.getRequestParameterMap("type");
			// parent image list
			if( getDeReleavanceImageList() != null && deReleavanceImageList.size()>0 )
			{
				listSize = deReleavanceImageList.size();
			}
			
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGEOCR); 
			if(rowsPerPage!=null)
			{
				imagePerPageOcr = Integer.valueOf(rowsPerPage.toString());
			}
			pagecount=(listSize/Integer.valueOf(imagePerPageOcr));
			remainder=(listSize%Integer.valueOf(imagePerPageOcr));
			if(pagecount>0 && remainder >0)
			{
				imageOffsetOcr =listSize-(listSize%Integer.valueOf(imagePerPageOcr));
			}
			else
			{
				imageOffsetOcr =listSize-(Integer.valueOf(imagePerPageOcr));
			}
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSETOCR, imageOffsetOcr);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * for first page record
	 */
	public void lastPageNewSci(){
		count  = 1;
		try{
			int listSize = 0;
			int pagecount;
			int remainder;
			FacesUtils facesUtils = new FacesUtils();
			String catagory=facesUtils.getRequestParameterMap("type");
			// parent image list
			if( getParentImageListfornewSci() != null && parentImageListfornewSci.size()>0 )
			{
				listSize = parentImageListfornewSci.size();
			}
			
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.IMAGEPERPAGENEWSCI); 
			if(rowsPerPage!=null)
			{
				imagePerPageNewSci = Integer.valueOf(rowsPerPage.toString());
			}
			pagecount=(listSize/Integer.valueOf(imagePerPageNewSci));
			remainder=(listSize%Integer.valueOf(imagePerPageNewSci));
			if(pagecount>0 && remainder >0)
			{
				imageOffsetnewSci =listSize-(listSize%Integer.valueOf(imagePerPageNewSci));
			}
			else
			{
				imageOffsetnewSci =listSize-(Integer.valueOf(imagePerPageNewSci));
			}
			sessionManager.setSessionAttributeInSession(SessionManager.IMAGEOFFSETNEWSCI, imageOffsetnewSci);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * pagination method for next page 
	 */
	public void reviewNxtPage(){
		try{
			String rowsPerPage = this.sessionManager.getSessionAttribute("rowsPerPage")!=null?this.sessionManager.getSessionAttribute("rowsPerPage").toString():null; 
			//System.out.println("rowsPerPage"+rowsPerPage);
			int reviewPerPage = 15;
			if(rowsPerPage!=null){
				reviewPerPage = Integer.valueOf(rowsPerPage);
			}
			reviewPageOffset +=Integer.valueOf(reviewPerPage);
			sessionManager.setSessionAttributeInSession("reviewPageOffset", reviewPageOffset);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * for previous page
	 */
	public void reviewPrevPage(){
		try{
			String rowsPerPage = this.sessionManager.getSessionAttribute("rowsPerPage")!=null?this.sessionManager.getSessionAttribute("rowsPerPage").toString():null;
			int reviewPerPage = 15;
			if(rowsPerPage!=null){
				reviewPerPage = Integer.valueOf(rowsPerPage);
			}
			if(reviewPageOffset>1)
				reviewPageOffset = reviewPageOffset - Integer.valueOf(reviewPerPage);
			
			sessionManager.setSessionAttributeInSession("reviewPageOffset", reviewPageOffset);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void reviewGotoPage() {
		int page = Integer.valueOf(facesUtils.getRequestParameterMap("page"));
		try{
			String rowsPerPage = this.sessionManager.getSessionAttribute("rowsPerPage")!=null?this.sessionManager.getSessionAttribute("rowsPerPage").toString():null;
			int reviewPerPage = 15;
			if(rowsPerPage!=null){
				reviewPerPage = Integer.valueOf(rowsPerPage);
			}
			reviewPageOffset = (page-1) * reviewPerPage;
			sessionManager.setSessionAttributeInSession("reviewPageOffset", reviewPageOffset);
			loadPagination();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * for first page record
	 */
	public void reviewFirstPage(){
		try{
			String rowsPerPage = this.sessionManager.getSessionAttribute("rowsPerPage")!=null?this.sessionManager.getSessionAttribute("rowsPerPage").toString():null;
			if(reviewPageOffset>1)
				reviewPageOffset = 0;
			
			sessionManager.setSessionAttributeInSession("reviewPageOffset", reviewPageOffset);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * for first page record
	 */
	public void reviewLastPage(){
		try{
			int listSize = 0;
			int pagecount;
			int remainder;
			int reviewPerPage = 15;
			listSize=getDeJobListBySeachCriteria().size();

			Object rowsPerPage = sessionManager.getSessionAttribute("rowsPerPage"); 
			if(rowsPerPage!=null)
			{
				reviewPerPage = Integer.valueOf(rowsPerPage.toString());
			}
			pagecount=(listSize/Integer.valueOf(reviewPerPage));
			remainder=(listSize%Integer.valueOf(reviewPerPage));
			if(pagecount>0 && remainder >0)
			{
				reviewPageOffset =listSize-(listSize%Integer.valueOf(reviewPerPage));
			}
			else
			{
				reviewPageOffset =listSize-(Integer.valueOf(reviewPerPage));
			}
			sessionManager.setSessionAttributeInSession("reviewPageOffset", reviewPageOffset);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void childNxtPage(){
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.CHILDIMAGEPERPAGE); 
			//System.out.println("rowsPerPage"+rowsPerPage);
			if(rowsPerPage!=null){
				childImagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
			childImageOffset +=Integer.valueOf(childImagePerPage);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * for previous page
	 */
	public void childPrevPage(){
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.CHILDIMAGEPERPAGE);
			if(rowsPerPage!=null){
				childImagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
			if(childImageOffset>1)
				childImageOffset = childImageOffset - Integer.valueOf(childImagePerPage);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * for first page record
	 */
	public void childFirstPage(){
		try{
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.CHILDIMAGEPERPAGE);
			if(rowsPerPage!=null){
				childImagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
			if(childImageOffset>1)
				childImageOffset = 0;
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * for first page record
	 */
	public void childLastPage(){
		try{
			int listSize = 0;
			int pagecount;
			int remainder;
			if(childImageList !=null && childImageList.size()>0 ) {
				listSize=childImageList.size();
			}
			Object rowsPerPage = sessionManager.getSessionAttribute(SessionManager.CHILDIMAGEPERPAGE); 
			if(rowsPerPage!=null) {
				childImagePerPage = Integer.valueOf(rowsPerPage.toString());
			}
			pagecount=(listSize/Integer.valueOf(childImagePerPage));
			remainder=(listSize%Integer.valueOf(childImagePerPage));
			if(pagecount>0 && remainder >0) {
				childImageOffset =listSize-(listSize%Integer.valueOf(childImagePerPage));
			}
			else {
				childImageOffset =listSize-(Integer.valueOf(childImagePerPage));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
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
			selectedCompany = new DeCompany();
		    this.setSearchValueInCompanyName(this.companyName);
		    
			DeCompany deCompany  = null;
			currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(currentUser != null)
			{
				if(!(this.country.isEmpty()))
				{
					//shashank
								if(this.companyName != null && !this.companyName.isEmpty()){
									DeCompany duplicate = deService.getDeCompanyNameByCompanyName(this.companyName);
									if(duplicate == null){
										deCompany = new DeCompany();
										deCompany = getAllDeCompanyDetails(deCompany);
										deCompany.setIsDeleted(false);
										deCompany.setCreated_by(currentUser);
										//deService.addDeCompany(deCompany);
										//System.out.println("test data save: "+deService.addDeCompany(deCompany));
										saveNewCmpId = deService.addDeCompany(deCompany);
										if(saveNewCmpId != 0){
											selectedCompany = deCompany;
										}
										
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

				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public String updateCompany() {
		
		DeCompany deCompany = deService.getDeCompanySeachByCompanyNameId(selectedCompany.getId());
		if(deCompany == null)
			deCompany = new DeCompany();
		
		deCompany = setEditCompanyValue(deCompany);
		deService.updateDeCompany(deCompany);
		selectedCompany = deCompany;
		messageService.messageInformation(null, "Company updated successfully.");
		return null;
	}
	
	public DeCompany setEditCompanyValue(DeCompany company) {
		company.setAddress(this.addressEd);
		company.setAddress1(this.address1Ed);
		company.setCity(this.cityEd);
		company.setCompanyName(this.companyNameEd);
		company.setCompanyURL(this.companyURLEd);
		company.setCountry(this.countryEd);
		company.setId(this.companyIdEd);
		company.setDepartment(this.departmentEd);
		company.setPincode(this.pincodeEd);
		company.setState(this.stateEd);
		return company;
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
				if(dataEntry != null && this.selectedCompany != null ){
					DeCompany duplicate = deService.getDeCompanySeachByCompanyNameId(this.selectedCompany.getId());
					if(duplicate == null){
						deCompany = new DeCompany();
						deCompany = getAllDeCompanyDetails(deCompany);
						deCompany.setIsDeleted(false);
						deCompany.setCreated_by(currentUser);
						deService.addDeCompany(deCompany);
					} else {
						deCompany = duplicate;
					}
					
					
					/*else{
						messageService.messageFatal(null, "CompanyName already exist.");
						System.out.println("CompanyName already exist");
						return null;
					}*/
				}
				else{
					if(this.selectedCompany != null ){
						//Store entry (Key/Value)of HashMap in set
				        Set mapSet = (Set) hm.entrySet();
				        //Create iterator on Set 
				        Iterator mapIterator = mapSet.iterator();
				       /* while (mapIterator.hasNext()) {
				                Map.Entry mapEntry = (Map.Entry) mapIterator.next();
				                // getKey Method of HashMap access a key of map
				                //getValue method returns corresponding key's value
				                if(this.searchValueInCompanyName.equalsIgnoreCase(mapEntry.getValue().toString()) ){
				                 deCompany = deService.getDeCompanySeachByCompanyNameId(Long.parseLong(mapEntry.getKey().toString()));	
				                }
				                System.out.println("Key : " + mapEntry.getKey() + "= Value : " + mapEntry.getValue());
				        }*/
						
				        deCompany = deService.getDeCompanySeachByCompanyNameId(this.selectedCompany.getId());
						
						
					}else if(this.companyId > 0){
						deCompany = deService.getDeCompanyById(this.companyId);
						selectedCompany = deCompany;
					} else {
						messageService.messageFatal(null, "CompanyName is required");
						return null; 
					}
				}
				if(dataEntry != null && deCompany != null && selectedCompany != null){
					/*if(this.startCurrencyRange==null ||this.startCurrencyRange.isEmpty()) {
						messageService.messageFatal(null, "Start Currency Range is required");
						return null; 
					} else if(this.endCurrencyRange==null ||this.endCurrencyRange.isEmpty()) {
						messageService.messageFatal(null, "End Currency Range is required");
						return null; 
					} else {*/
						dataEntry = getAllAdDetails(dataEntry);
						dataEntry.setIsDeleted(false);
						dataEntry.setIsqualityCheck(true);
						dataEntry.setDeCompany(deCompany);
						dataEntry.setCreated_by(currentUser);
						dataEntry.setOcrText(this.ocrText);
						dataEntry.setContactInfo(this.contactInfo);
						deService.updateDataEntry(dataEntry);
						updateDataEntryOcr(dataEntry);
						messageService.messageInformation(null, "Data Entry has been Updated successfully.");
					
				}else{
					messageService.messageFatal(null, "CompanyName is required");
				}
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		selectedCompany = new DeCompany();
		return null;
	}
	
	private void updateDataEntryOcr(DataEntry dataEntry) {
		try {
			AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
			asyncHttpClient.prepareGet(
					"http://localhost:8080/webapp/updateDEOcr?id="
							+ dataEntry.getId()).execute(
					new AsyncCompletionHandler<Response>() {

						@Override
						public Response onCompleted(Response response)
								throws Exception {
							return response;
						}

						@Override
						public void onThrowable(Throwable t) {
						}
					});
		} catch (Exception e) {
		}
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
									deCompany.setId(deService.addDeCompany(deCompany));
									dataEntry.setDeCompany(deCompany);
									this.selectedCompany = deCompany;
									clearAllDeCompanyDetails();
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
								updateDataEntryOcr(dataEntry);
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
							updateDataEntryOcr(dataEntry);
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
						updateDataEntryOcr(dataEntry);
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
						updateDataEntryOcr(dataEntry);
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

	public void sortDeJobList(final String sortOn) {

		List<DeJob> jobs = deJobList;
		this.reviewPageOffset = 0;
		if(order){
			order = false;
			Collections.sort(jobs, new Comparator<DeJob>() {
				public int compare(DeJob o1, DeJob o2) {
					switch(sortOn){
					case "id":
						return o1.getId()<o2.getId()?-1:1;
					case "imageName":
						return o1.getParentImage().getImageName().toLowerCase().compareTo(o2.getParentImage().getImageName().toLowerCase());
					case "publication":
						return o1.getParentImage().getPublicationTitle().getPublicationTitle().compareTo(o2.getParentImage().getPublicationTitle().getPublicationTitle());
					case "section":
						return o1.getParentImage().getSection().getPublicationTitle().compareTo(o2.getParentImage().getSection().getPublicationTitle());
					case "date":
						return o1.getParentImage().getIssueDate().compareTo(o2.getParentImage().getIssueDate());
					case "page":
						return Integer.parseInt(o1.getParentImage().getPage())>Integer.parseInt(o2.getParentImage().getPage())?-1:1;
					case "createdBy":
						return o1.getParentImage().getCreatedBy().getFirstName().compareTo(o2.getParentImage().getCreatedBy().getFirstName());
					case "progress":
						return getStatus((int) o1.getId()).compareTo( getStatus((int) o2.getId()));
					}
					return 0;
				}
			});
		} else {
			order = true;
			Collections.sort(jobs, new Comparator<DeJob>() {
				public int compare(DeJob o1, DeJob o2) {
					switch(sortOn){
					case "id":
						return o2.getId()<o1.getId()?-1:1;
					case "imageName":
						return o2.getParentImage().getImageName().toLowerCase().compareTo(o1.getParentImage().getImageName().toLowerCase());
					case "publication":
						return o2.getParentImage().getPublicationTitle().getPublicationTitle().compareTo(o1.getParentImage().getPublicationTitle().getPublicationTitle());
					case "section":
						return o2.getParentImage().getSection().getPublicationTitle().compareTo(o1.getParentImage().getSection().getPublicationTitle());
					case "date":
						return o2.getParentImage().getIssueDate().compareTo(o1.getParentImage().getIssueDate());
					case "page":
						return Integer.parseInt(o2.getParentImage().getPage())>Integer.parseInt(o1.getParentImage().getPage())?-1:1;
					case "createdBy":
						return o2.getParentImage().getCreatedBy().getFirstName().compareTo(o1.getParentImage().getCreatedBy().getFirstName());
					case "progress":
						return getStatus((int) o2.getId()).compareTo( getStatus((int) o1.getId()));
					}
					return 0;
				}
			});
		}
	}

	public static void main(String[] arr) {
		String test = "140201CL";
		for(String a:test.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")) {
			System.out.println(a);
		}
		//int input1[] = {2,4,5,6,3,4,8,9};
		//int input2 = 8;
		//System.out.println(test(input1,input2));
	}
	public static int test(int[] input1,int input2) {
		int firstSelection = 0;
    	int secondSelection = 0;
    	boolean isFirst = true;
    	boolean isSecond = false;
        for(int i=0;i<input1.length-1;i++) {
        	if(input1[i]<input1[i+1]) {
        		if(isFirst) {
        			firstSelection += input1[i+1] - input1[i];
        		} else if(!isSecond) {
        			secondSelection += input1[i+1] - input1[i];
        		}
        	} else {
        		if(isFirst)
        			isSecond = false;
        		isFirst = false;
        	}
        }
        return firstSelection+secondSelection;
	}

	public boolean isDuplicate() {
		return duplicate;
	}

	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}

	public List<ChildImage> getCurrentParentsChild() {
		List<ChildImage> childImages = null;
		if(this.parentImage!=null) {
			currentParentsChild.clear();
			childImages = getChildImageService().getChildImagesByParent(this.parentImage.getId());
			currentParentsChild.addAll(childImages);
		}
		return currentParentsChild;
	}

	public void setCurrentParentsChild(List<ChildImage> currentParentsChild) {
		this.currentParentsChild = currentParentsChild;
	}
	
	public void changeActive(){
		 count   = 1;
		getParentImageService().updateParentImagesStatus(selectedIds);	
		buildParentImageList();
		selectedIds = new HashMap<Long, Boolean>();
	}
	public void makeLive(){
		count   = 1;
		getParentImageService().updateParentImagesStatusLive(selectedIds);	
		buildParentImageList();
		selectedIds = new HashMap<Long, Boolean>();
	}

	public String makeLiveAsNotDuplicateByScore(String id,int index){
		
		System.out.println("index: "+index);
		System.out.println("id:"+id);
		long selectedId = liveStatusArr[index];
		getParentImageService().updateOcrTextIDDupDetails(id, selectedId);	
		ScoreData scoreData = this.deReleavanceImageList.get(index);
	    this.deReleavanceImageList.remove(scoreData);

	    return null;
		//getDeReleavanceImageList();
	}
	
	/*public void selectAllparentImages(){
		for (Map.Entry<Long, Boolean> entry : selectedIds.entrySet()) {
		
			if(selectedIds.containsKey(entry.getKey())){
	        	selectedIds.put(entry.getKey(), true);
	           }  
	    	}
		
		   for (Map.Entry<Long, Boolean> entry : selectedIds.entrySet()) {
	           System.out.println("value after  changed:  "+entry.getValue());
	    	}	
	}*/
	
	public void processChild() {
		List<DataEntry> dataEntries = deService.getDataEntry();
		List<Long> ids = new ArrayList<Long>(dataEntries.size());
		for(DataEntry d : dataEntries) {
			if(d.getChildImage() != null) {
				ids.add(d.getChildImage().getId());
			}
		}
		System.out.println("size :"+ids.size());
		List<ChildImage> childImages = childImageService.getAllChildImageNotInList(ids);
		System.out.println("childImages :"+childImages.size());
		for(ChildImage childImage :childImages) {
			DataEntry entry = new DataEntry();
			entry.setChildImage(childImage);
			entry.setParentImage(childImage.getParentImage());
			entry.setDeJobid(deService.getDeJobByParentImageId(childImage.getParentImage().getId()));
			try {
				File image=new File(imageBasePath+CommonProperties.getChildImagePath()+childImage.getParentImage().getId()+"/"+childImage.getId()+"/"+childImage.getImageName());
				ImageIO.scanForPlugins();
				String result = new Ocr().doOCR(image);
				System.out.println("result: "+result);
				entry.setOcrText(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			deService.addDataEntry(entry);
		}
	}
}