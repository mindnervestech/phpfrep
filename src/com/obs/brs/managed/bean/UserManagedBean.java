package com.obs.brs.managed.bean;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.google.gson.Gson;
import com.obs.brs.email.EmailManager;
import com.obs.brs.messages.IMessagesService;
import com.obs.brs.model.Country;
import com.obs.brs.model.DeCompany;
import com.obs.brs.model.Publication;
import com.obs.brs.model.Region;
import com.obs.brs.model.Subscriber;
import com.obs.brs.model.SubscriberPublication;
import com.obs.brs.model.SubscriberUser;
import com.obs.brs.model.User;
import com.obs.brs.model.UserType;
import com.obs.brs.service.IDeService;
import com.obs.brs.service.ISettingsService;
import com.obs.brs.service.IUserService;
import com.obs.brs.session.manager.FacesUtils;
import com.obs.brs.session.manager.SessionManager;
import com.obs.brs.utils.CommonProperties;
import com.obs.brs.utils.CommonUtils;
import com.obs.brs.utils.StringUtility;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;

@ManagedBean(name="userBean")
@ViewScoped
public class UserManagedBean
implements Serializable
{
	private static final long serialVersionUID = 1L;
	CommonUtils commonUtils = CommonUtils.getInstance();
	SessionManager sessionManager = new SessionManager();
	@ManagedProperty("#{UserService}")
	IUserService userService;
	@ManagedProperty("#{SettingsService}")
	ISettingsService settingsService;
	@ManagedProperty("#{MessageService}")
	IMessagesService messageService;
	@ManagedProperty("#{DeService}")
	IDeService deService;
	private static final String RETURNHOME = "returnhome";
	private static final String HOME = "home";
	private static final String MANAGE_DATAENTRY = "managedataentry";
	private static final String EDIT_USER = "editUser";
	private static final String EDIT_ADMIN_USER = "editAdminUser";
	private static final String LOGOUT = "home";
	private static final String MANAGE_USER = "manageUser";
	private static final String RETURN_MANAGE_USER = "returnmanageuser";
	private static final String RETURN_PROFILE_ADMIN = "returnAdminuserProfile";
	private static final String ADD_USER = "addUser";
	private static final String MANAGE_SUBSCRIBER = "manageSubscriber";
	private static final String RETURN_MANAGE_SUBSCRIBER = "returnmanageSubscriber";
	private static final String ADD_SUBSCRIBER = "addSubscriber";
	private static final String EDIT_SUBSCRIBER = "editSubscriber";
	private static final String VIEW_SUBSCRIBER_DETAILS = "viewSubscriberDetails";
	private static final String VIEW_USER_DETAILS = "viewUserDetails";
	private static final String MANAGE_MESSAGES = "manageMessage";
	private static final String MANAGE_QC_USER = "manageQc";
	private static final String ADMIN_DASHBOARD = "admindashboard";
	private static final String DEO_DASHBOARD = "deodashboard";
	private static final String QC_DASHBOARD = "qcdashboard";
	private static final String SUBSCRIBER_PROFILE = "subscriberProfile";
	private static final String USER_PROFILE = "userProfile";
	private static final String IMAGE_UPLOAD = "imageupload";
	private static final String SUPER_ADMIN_DASHBOARD = "superadmindashboard";
	private static final String GALLERY = "gallery";
	private static final String SUBSCRIBER_DASHBOARD = "subscriberadmindashboard";
	private static final String SUBSCRIBER_USER_DASHBOARD = "subscriberuserdashboard";
	private static final String MANAGE_SUBSCRIBER_USER = "manageSubscriberUser";
	private static final String RETURN_MANAGE_SUBSCRIBER_USER = "returnmanageSubscriberUser";
	private static final String ADD_SUBSCRIBER_USER = "addSubscriberUser";
	private static final String EDIT_SUBSCRIBER_USER = "editSubscriberUser";
	private static final String VIEW_SUBSCRIBER_USER_DETAILS = "viewSubscriberUserDetails";
	private static final String EDIT_SUBSCRIBER_PROFILE = "editSubscriberProfile";
	private static final String RETURN_SUBSCRIBER_PROFILE = "returnsubscriberProfile";
	private static final String RETURNSUBSCRIBERHOME = "returnsubscriberhome";
	private static final String MANAGE_REGION = "manageRegion";
	private static final String VIEW_REGION_DETAILS = "viewRegion";
	private static final String RETURN_MANAGE_REGION = "returnManageRegion";
	private static final String EDIT_REGION = "editRegion";
	private static final String ADD_REGION = "addRegion";
	private static final String ADD_SUBSCRIBER_USER_BYADMIN = "addSubscriberUserByAdmin";
	private static final String EDIT_SUBSCRIBER_USER_BYADMIN = "editSubscriberUserByAdmin";
	private static final String VIEW_SUBSCRIBER_USER_DETAILS_BYADMIN = "viewSubscriberUserDetailsByAdmin";
	private static final String MANAGE_PUBLICATION = "managePublication";
	private static final String VIEW_PUBLICATION_DETAILS = "viewPublication";
	private static final String RETURN_MANAGE_PUBLICATION = "returnManagePublication";
	private static final String EDIT_PUBLICATION = "editPublication";
	private static final String ADD_PUBLICATION = "addPublication";
	private static final String MANAGE_SUBSCRIBER_USER_BYADMIN = "manageSubscriberUserDetailsByAdmin";
	private static final String RETURN_MANAGE_SUBSCRIBER_USER_BYADMIN = "returnManageSubscriberUserDetailsByAdmin";
	private static final String MANAGE_SETTING = "setting";
	private static final String MANAGE_REPORTS = "managereports";
	private static final String SEARCHREPORTVIEW = "searchReportView";
	private static final String MANAGE_QCJOB = "view_qcjob_by_journal";
	private static final String MANAGE_COMPANY = "manage_company";
	List<User> userList;
	List<Subscriber> subscriberList;
	List<Region> regionList;
	List<SubscriberUser> subscriberUserList;
	List<Publication> publicationList;
	Region regionCountList;
	private long countOfRegions;
	private Map<Integer, Boolean> checkedUser = new HashMap();
	private Map<Long, Boolean> checkedRegion = new HashMap();
	private String firstName;
	private String lastName;
	private short gender;
	private String address;
	private String city;
	private String state;
	private String country;
	private String pincode;
	private String email;
	private String companyEmail;
	private String phoneNo;
	private String password;
	private String newPassword;
	private String confirmPassword;
	private String forgotPwdOption = "0";
	private String forgotPwdEncryptVal;
	private String companyName;
	private String companyLocation;
	private String notes;
	private String subscriberPassword;
	private long userTypeId;
	private UserType userType;
	private int id;
	private Date dateofBirth;
	private int pageRedirect = 0;
	private int userStatus = 1;
	private String searchValue = "";
	private String statusFilter;
	private String msgLabel;
	private String region;
	private long regionCountryId;
	private int noOfregion;
	private String userAvailableImage = null;
	private String userNotAvailableImage = null;
	private int subscriberUserId;
	private String publicationTitle;
	private int publicationType;
	private int publicationStatus = 1;
	private int regionStatus;
	private List<Region> noOfregionList = new ArrayList();
	private List<Region> deleteRegionList = new ArrayList();
	private List<User> userDateils = new ArrayList();
	private List<SubscriberPublication> deletePublicationList = new ArrayList();
	private int regionCount;
	private int isApproved;
	private String fromDate;
	private String toDate;
	private String publicationId;
	private List<SubscriberPublication> noOfpublicationList = new ArrayList();
	private List<String[]> publicationStringList = new ArrayList();
	private List<String[]> deletePublicationStringList = new ArrayList();
	private List<String[]> editPublicationStringList = new ArrayList();
	private Boolean checkAdmin = Boolean.valueOf(false);
	private List<String[]> addSubscriberUserList = new ArrayList();
	private List<String[]> deleteSubscriberUserList = new ArrayList();
	private List<String[]> editSubscriberUserList = new ArrayList();
	private List<String[]> editMoreSubscriberUserList = new ArrayList();
	private  List<DeCompany> allCompnayList = new ArrayList<DeCompany>();
	private int rowIndex = -1;
	private Date curdate = new Date();
	private int subscriberUserIdByAdmin;
	private String fromPage;
	private String infoMessage;
	private String errormessage;
	User currentUser;
	private Boolean isPopup=false;

     private  String compList;
	public Date getCurdate()
	{
		return this.curdate;
	}

	public void setCurdate(Date curdate)
	{
		this.curdate = curdate;
	}

	public List<String[]> getEditMoreSubscriberUserList()
	{
		return this.editMoreSubscriberUserList;
	}

	public void setEditMoreSubscriberUserList(List<String[]> editMoreSubscriberUserList)
	{
		this.editMoreSubscriberUserList = editMoreSubscriberUserList;
	}

	public int getSubscriberUserIdByAdmin()
	{
		return this.subscriberUserIdByAdmin;
	}

	public void setSubscriberUserIdByAdmin(int subscriberUserIdByAdmin)
	{
		this.subscriberUserIdByAdmin = subscriberUserIdByAdmin;
	}

	public int getRowIndex()
	{
		return this.rowIndex;
	}

	public void setRowIndex(int rowIndex)
	{
		this.rowIndex = rowIndex;
	}

	public List<String[]> getEditSubscriberUserList()
	{
		return this.editSubscriberUserList;
	}

	public List<String[]> getEditPublicationStringList()
	{
		return this.editPublicationStringList;
	}

	public void setEditPublicationStringList(List<String[]> editPublicationStringList)
	{
		this.editPublicationStringList = editPublicationStringList;
	}

	public void setEditSubscriberUserList(List<String[]> editSubscriberUserList)
	{
		this.editSubscriberUserList = editSubscriberUserList;
	}

	public Boolean getCheckAdmin()
	{
		return this.checkAdmin;
	}

	public void setCheckAdmin(Boolean checkAdmin)
	{
		this.checkAdmin = checkAdmin;
	}

	public List<String[]> getAddSubscriberUserList()
	{
		return this.addSubscriberUserList;
	}

	public void setAddSubscriberUserList(List<String[]> addSubscriberUserList)
	{
		this.addSubscriberUserList = addSubscriberUserList;
	}

	public List<String[]> getDeleteSubscriberUserList()
	{
		return this.deleteSubscriberUserList;
	}

	public void setDeleteSubscriberUserList(List<String[]> deleteSubscriberUserList)
	{
		this.deleteSubscriberUserList = deleteSubscriberUserList;
	}

	public List<String[]> getDeletePublicationStringList()
	{
		return this.deletePublicationStringList;
	}

	public void setDeletePublicationStringList(List<String[]> deletePublicationStringList)
	{
		this.deletePublicationStringList = deletePublicationStringList;
	}

	public String getPublicationId()
	{
		return this.publicationId;
	}

	public void setPublicationId(String publicationId)
	{
		this.publicationId = publicationId;
	}

	public List<String[]> getPublicationStringList()
	{
		return this.publicationStringList;
	}

	public void setPublicationStringList(List<String[]> publicationStringList)
	{
		this.publicationStringList = publicationStringList;
	}

	public List<SubscriberPublication> getDeletePublicationList()
	{
		return this.deletePublicationList;
	}

	public void setDeletePublicationList(List<SubscriberPublication> deletePublicationList)
	{
		this.deletePublicationList = deletePublicationList;
	}

	public Map<Long, Boolean> getCheckedRegion()
	{
		return this.checkedRegion;
	}

	public void setCheckedRegion(Map<Long, Boolean> checkedRegion)
	{
		this.checkedRegion = checkedRegion;
	}

	public String getFromDate()
	{
		return this.fromDate;
	}

	public void setFromDate(String fromDate)
	{
		this.fromDate = fromDate;
	}

	public String getToDate()
	{
		return this.toDate;
	}

	public void setToDate(String toDate)
	{
		this.toDate = toDate;
	}

	public List<SubscriberPublication> getNoOfpublicationList()
	{
		return this.noOfpublicationList;
	}

	public void setNoOfpublicationList(List<SubscriberPublication> noOfpublicationList)
	{
		this.noOfpublicationList = noOfpublicationList;
	}

	public List<User> getUserDateils()
	{
		if (this.userDateils.size() == 0) {
			this.userDateils.add(new User());
		}
		return this.userDateils;
	}

	public void setUserDateils(List<User> userDateils)
	{
		this.userDateils = userDateils;
	}

	public long getCountOfRegions()
	{
		return this.countOfRegions;
	}

	public void setCountOfRegions(long countOfRegions)
	{
		this.countOfRegions = countOfRegions;
	}

	public int getRegionCount()
	{
		return this.regionCount;
	}

	public void setRegionCount(int regionCount)
	{
		this.regionCount = regionCount;
	}

	public void setNoOfregionList(List<Region> noOfregionList)
	{
		this.noOfregionList = noOfregionList;
	}

	public List<Region> getDeleteRegionList()
	{
		return this.deleteRegionList;
	}

	public void setDeleteRegionList(List<Region> deleteRegionList)
	{
		this.deleteRegionList = deleteRegionList;
	}

	public List<Region> getNoOfregionList()
	{
		try
		{
			if (this.regionCount > 0)
			{
				if ((this.noOfregionList != null) && (this.noOfregionList.size() > 0) && (this.regionCount > this.noOfregionList.size())) {
					for (int k = this.noOfregionList.size(); k < this.regionCount; k++) {
						this.noOfregionList.add(new Region());
					}
				} else if ((this.noOfregionList != null) && (this.noOfregionList.size() > 0) && (this.regionCount < this.noOfregionList.size())) {
					for (int k = this.regionCount; k < this.noOfregionList.size(); k++) {
						this.noOfregionList.remove(k);
					}
				} else if ((this.noOfregionList == null) || (this.noOfregionList.size() == 0)) {
					for (int k = 0; k < this.regionCount; k++) {
						this.noOfregionList.add(new Region());
					}
				}
			}
			else if (this.noOfregionList.size() == 0) {
				this.noOfregionList.add(new Region());
			}
		}
		catch (Exception e)
		{
			System.out.println("UserFormBean::getViewRegion" + e.getMessage());
			e.printStackTrace();
		}
		return this.noOfregionList;
	}

	public void localChanged(AjaxBehaviorEvent event)
	{
		this.regionCount = ((Integer)event.getComponent().getAttributes().get("value")).intValue();
	}

	public int getRegionStatus()
	{
		return this.regionStatus;
	}

	public void setRegionStatus(int regionStatus)
	{
		this.regionStatus = regionStatus;
	}

	public int getNoOfregion()
	{
		return this.noOfregion;
	}

	public void setNoOfregion(int noOfregion)
	{
		this.noOfregion = noOfregion;
	}

	public int getPublicationStatus()
	{
		return this.publicationStatus;
	}

	public void setPublicationStatus(int publicationStatus)
	{
		this.publicationStatus = publicationStatus;
	}

	public int getPublicationType()
	{
		return this.publicationType;
	}

	public void setPublicationType(int publicationType)
	{
		this.publicationType = publicationType;
	}

	public int getSubscriberUserId()
	{
		return this.subscriberUserId;
	}

	public void setSubscriberUserId(int subscriberUserId)
	{
		this.subscriberUserId = subscriberUserId;
	}

	public String getPublicationTitle()
	{
		return this.publicationTitle;
	}

	public void setPublicationTitle(String publicationTitle)
	{
		this.publicationTitle = publicationTitle;
	}

	private int changeRowsPerPage = 15;

	public String getNotes()
	{
		return this.notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	public String getSubscriberPassword()
	{
		return this.subscriberPassword;
	}

	public void setSubscriberPassword(String subscriberPassword)
	{
		this.subscriberPassword = subscriberPassword;
	}

	public String getRegion()
	{
		return this.region;
	}

	public void setRegion(String region)
	{
		this.region = region;
	}

	public long getRegionCountryId()
	{
		return this.regionCountryId;
	}

	public void setRegionCountryId(long regionCountryId)
	{
		this.regionCountryId = regionCountryId;
	}

	public int getPageRedirect()
	{
		return this.pageRedirect;
	}

	public void setPageRedirect(int pageRedirect)
	{
		this.pageRedirect = pageRedirect;
	}

	public List<Publication> getPublicationList()
	{
		this.publicationList = new ArrayList();
		this.publicationList.addAll(getUserService().getPublications());
		return this.publicationList;
	}

	public void setPublicationList(List<Publication> publicationList)
	{
		this.publicationList = publicationList;
	}

	public List<Region> getRegionList()
	{
		this.regionList = new ArrayList();
		this.regionList.addAll(getUserService().getRegions());
		return this.regionList;
	}

	public void setRegionList(List<Region> regionList)
	{
		this.regionList = regionList;
	}

	public int getChangeRowsPerPage()
	{
		return this.changeRowsPerPage;
	}

	public void setChangeRowsPerPage(int changeRowsPerPage)
	{
		this.changeRowsPerPage = changeRowsPerPage;
	}

	public String getMsgLabel()
	{
		return this.msgLabel;
	}

	public void setMsgLabel(String msgLabel)
	{
		this.msgLabel = msgLabel;
	}

	public int getId()
	{
		if(id == 0) { // by any reason
			currentUser = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			if(currentUser != null)
			{
				id = currentUser.getId();
			}
		}
		System.out.println("current User" + id);
		return this.id;
	}

	public long getUserTypeId()
	{
		return this.userTypeId;
	}

	public void setUserTypeId(long userTypeId)
	{
		this.userTypeId = userTypeId;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public UserType getUserType()
	{
		return this.userType;
	}

	public void setUserType(UserType userType)
	{
		this.userType = userType;
	}

	public Date getDateofBirth()
	{
		return this.dateofBirth;
	}

	public void setDateofBirth(Date dateofBirth)
	{
		this.dateofBirth = dateofBirth;
	}

	public List<Subscriber> getSubscriberList()
	{
		this.subscriberList = new ArrayList();
		this.subscriberList.addAll(getUserService().getSubscriber());
		return this.subscriberList;
	}

	public void setSubscriberList(List<Subscriber> subscriberList)
	{
		this.subscriberList = subscriberList;
	}

	public String getCompanyName()
	{
		return this.companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public String getCompanyLocation()
	{
		return this.companyLocation;
	}

	public void setCompanyLocation(String companyLocation)
	{
		this.companyLocation = companyLocation;
	}

	public String getCompanyEmail()
	{
		return this.companyEmail;
	}

	public void setCompanyEmail(String companyEmail)
	{
		this.companyEmail = companyEmail;
	}

	public List<SubscriberUser> getSubscriberUserList()
	{
		this.subscriberUserList = new ArrayList();
		this.subscriberUserList.addAll(getUserService().getSubscriberUser());
		return this.subscriberUserList;
	}

	public void setSubscriberUserList(List<SubscriberUser> subscriberUserList)
	{
		this.subscriberUserList = subscriberUserList;
	}

	public int getUserStatus()
	{
		return this.userStatus;
	}

	public void setUserStatus(int userStatus)
	{
		this.userStatus = userStatus;
	}

	public IUserService getUserService()
	{
		return this.userService;
	}

	public void setUserService(IUserService userService)
	{
		this.userService = userService;
	}

	public ISettingsService getSettingsService()
	{
		return this.settingsService;
	}

	public void setSettingsService(ISettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	public IDeService getDeService()
	{
		return this.deService;
	}

	public void setDeService(IDeService deService)
	{
		this.deService = deService;
	}

	public IMessagesService getMessageService()
	{
		return this.messageService;
	}

	public void setMessageService(IMessagesService messageService)
	{
		this.messageService = messageService;
	}

	public void setUserList(List<User> userList)
	{
		this.userList = userList;
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getPhoneNo()
	{
		return this.phoneNo;
	}

	public void setPhoneNo(String phoneNo)
	{
		this.phoneNo = phoneNo;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public short getGender()
	{
		return this.gender;
	}

	public void setGender(short gender)
	{
		this.gender = gender;
	}

	public String getAddress()
	{
		return this.address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getCity()
	{
		return this.city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return this.state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getCountry()
	{
		return this.country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getPincode()
	{
		return this.pincode;
	}

	public void setPincode(String pincode)
	{
		this.pincode = pincode;
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return this.password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getNewPassword()
	{
		return this.newPassword;
	}

	public void setNewPassword(String newPassword)
	{
		this.newPassword = newPassword;
	}

	public String getConfirmPassword()
	{
		return this.confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword)
	{
		this.confirmPassword = confirmPassword;
	}

	public String getForgotPwdOption()
	{
		return this.forgotPwdOption;
	}

	public Map<Integer, Boolean> getCheckedUser()
	{
		return this.checkedUser;
	}

	public void setForgotPwdOption(String forgotPwdOption)
	{
		this.forgotPwdOption = forgotPwdOption;
	}

	public void setCheckedUser(Map<Integer, Boolean> checkedUser)
	{
		this.checkedUser = checkedUser;
	}

	public String getForgotPwdEncryptVal()
	{
		return this.forgotPwdEncryptVal;
	}

	public void setForgotPwdEncryptVal(String forgotPwdEncryptVal)
	{
		this.forgotPwdEncryptVal = forgotPwdEncryptVal;
	}

	public String getSearchValue()
	{
		return this.searchValue;
	}

	public void setSearchValue(String searchValue)
	{
		this.searchValue = searchValue;
	}

	public String getStatusFilter()
	{
		return this.statusFilter;
	}

	public void setStatusFilter(String statusFilter)
	{
		this.statusFilter = statusFilter;
	}

	public String getUserAvailableImage()
	{
		return this.userAvailableImage;
	}

	public void setUserAvailableImage(String userAvailableImage)
	{
		this.userAvailableImage = userAvailableImage;
	}

	public String getUserNotAvailableImage()
	{
		return this.userNotAvailableImage;
	}

	public void setUserNotAvailableImage(String userNotAvailableImage)
	{
		this.userNotAvailableImage = userNotAvailableImage;
	}

	public User getCurrentUser()
	{
		return this.currentUser;
	}

	public void setCurrentUser(User currentUser)
	{
		this.currentUser = currentUser;
	}

	public String getFromPage()
	{
		return this.fromPage;
	}

	public void setFromPage(String fromPage)
	{
		this.fromPage = fromPage;
	}

	public String getInfoMessage()
	{
		return this.infoMessage;
	}

	public void setInfoMessage(String infoMessage)
	{
		this.infoMessage = infoMessage;
	}

	public String getErrormessage()
	{
		return this.errormessage;
	}

	public void setErrormessage(String errormessage)
	{
		this.errormessage = errormessage;
	}
	
	public Boolean getIsPopup() {
		return isPopup;
	}
	
	public void setIsPopup(Boolean isPopup) {
		this.isPopup = isPopup;
	}
	
	public User getLoginUser()
	{
		User user = (User)this.sessionManager.getSessionAttribute("login_user");
		if (user != null) {
			return user;
		}
		try
		{
			FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "login.xhtml");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new User();
	}

	// Login the user by username and password
	public String login(){
		String returnPage = null;
		try {
			User user = loginUser(); 
			if(user != null)
			{
		/*		sessionManager.setUserInSession(SessionManager.LOGINUSER,user);
				if(user.getIsActive() && user.getUserType().getUserType().equals("Super Admin")){
					returnPage = SUPER_ADMIN_DASHBOARD;
				}
				else if(user.getIsActive() && user.getUserType().getUserType().equals("Admin")){
					returnPage = ADMIN_DASHBOARD;
				}
				else if(user.getIsActive() && user.getUserType().getUserType().equals("Quality Check")){
					returnPage = QC_DASHBOARD;
				}
				else if(user.getIsActive() && user.getUserType().getUserType().equals("Data Entry")){
					returnPage = DEO_DASHBOARD;
				}
				return returnPage;*/
				sessionManager.setUserInSession(SessionManager.LOGINUSER,user);
				if(user.getIsActive() && user.getUserType().getUserType().equals("Super Admin")){
					returnPage = MANAGE_USER;
				}
				else if(user.getIsActive() && user.getUserType().getUserType().equals("Admin")){
					returnPage = MANAGE_USER;
				}
				else if(user.getIsActive() && user.getUserType().getUserType().equals("Quality Check")){
					returnPage = MANAGE_QCJOB;
				}
				else if(user.getIsActive() && user.getUserType().getUserType().equals("Data Entry")){
					returnPage = MANAGE_DATAENTRY;
				}
				return returnPage;
			}
			else{
				messageService.messageError(null, "Login Failed");
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String loadAddOrEditUser()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("userId");
		int userId = Integer.valueOf(val != null ? val : "0").intValue();
		if (userId > 0) {
			this.sessionManager.setSessionAttributeInSession("edit_user", Integer.valueOf(userId));
		} else {
			this.sessionManager.removeSessionAttributeInSession("edit_user");
		}
		if (userId > 0) {
			return "editUser";
		}
		return "addUser";
	}

	public void setUserDetails(User user)
	{
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.userTypeId = user.getUserType().getId();
		this.address = user.getAddress();
		this.gender = user.getGender();
		this.city = user.getCity();
		this.state = user.getState();
		this.country = user.getCountry();
		this.pincode = user.getPincode();
		this.phoneNo = user.getPhoneNo();
		this.dateofBirth = user.getDateofBirth();
	}

	public String changePassword()
	{
		try
		{
			User user = (User)this.sessionManager.getSessionAttribute("login_user");
			if (user != null) {
				if (user.getPassword().equals(this.password))
				{
					if (this.newPassword.equals(this.confirmPassword))
					{
						user.setPassword(this.newPassword);
						this.userService.updateUser(user);

						this.msgLabel = "Your Password has been Saved Successfully.";
						return "/pages/admin/userProfile.xhtml?msgLabel=" + this.msgLabel + "&faces-redirect=true";
					}
					this.messageService.messageError(null, "Your new password mismatch with confirmation password.");
				}
				else
				{
					this.messageService.messageError(null, "Incorrect old password");
					return null;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public User getAllUserValues(User user)
	{
		user.setFirstName(this.firstName);
		user.setLastName(this.lastName);
		user.setEmail(this.email);
		if (this.userTypeId > 0L) {
			this.userType = this.userService.getUserTypeById(this.userTypeId);
		} else {
			this.userType = this.userService.getUserTypeById(1L);
		}
		user.setUserType(this.userType);
		user.setAddress(this.address);
		user.setGender(this.gender);
		user.setCity(this.city);
		user.setPhoneNo(this.phoneNo);
		user.setPincode(this.pincode);
		user.setState(this.state);
		user.setCountry(this.country);
		user.setDateofBirth(this.dateofBirth);
		return user;
	}

	public String forgotPassword()
	{
		try
		{
			if (StringUtility.isNotEmpty(this.email))
			{
				User user = this.userService.getUserByEmail(this.email);
				if (user != null)
				{
					String encryptedVal = this.commonUtils.generateEncryptedPwd(user.getFirstName());
					this.userService.updateUser(user);
					String resetLink = CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "pages/reset_password.xhtml?pwd=" + encryptedVal;
					EmailManager.forgotPwd(this.email, user.getFirstName(), resetLink);
					this.messageService.messageInformation(null, "Reset link has sent Successfully to your Email Id.");
					return "returnhome";
				}
				this.messageService.messageError(null, "Given email Id is not matched");
				return null;
			}
			this.messageService.messageError(null, "Please enter the email id");
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return "login.xhtml?faces-redirect=false";
	}

	public String resetNewPassword()
	{
		try
		{
			if (this.forgotPwdEncryptVal == null)
			{
				this.messageService.messageError(null, "Not a valid URL link.");
			}
			else if (this.newPassword.equals(this.confirmPassword))
			{
				User user = this.userService.getUserByForgotPwdEncryption(this.forgotPwdEncryptVal);
				if (user != null)
				{
					user.setPassword(this.newPassword);
					this.userService.updateUser(user);
					this.messageService.messageInformation(null, "Your new password has changed Successfully.");
					return "returnhome";
				}
				this.messageService.messageError(null, "Not a valid URL link.");
			}
			else
			{
				this.messageService.messageError(null, "Your new password mismatch with confirmation password.");
				this.confirmPassword = "";
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "returnhome";
	}

	public void loadErrorMessage(ComponentSystemEvent event)
	{
		try
		{
			if ((this.errormessage != null) && (StringUtility.isNotEmpty(this.errormessage))) {
				this.messageService.messageInformation(null, this.errormessage);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void loadResetPwdUser(ComponentSystemEvent event)
	{
		try
		{
			if (this.forgotPwdEncryptVal == null)
			{
				this.messageService.messageError(null, "Not a valid URL link.");
			}
			else
			{
				User user = this.userService.getUserByForgotPwdEncryption(this.forgotPwdEncryptVal);
				if (user != null) {
					this.firstName = user.getFirstName();
				} else {
					this.messageService.messageError(null, "Not a valid URL link.");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public String logout()
	{
		this.sessionManager.removeSessionAttributeInSession("login_user");
		this.sessionManager.removeSessionAttributeInSession("edit_user");
		this.sessionManager.removeSessionAttributeInSession("login_subscriber_user");
		this.sessionManager.removeSessionAttributeInSession("edit_user");
		this.sessionManager.removeSessionAttributeInSession("brs.menu");
		this.sessionManager.removeSession();
		return "home";
	}

	public void reset()
	{
		setFirstName("");
		setLastName("");
	}

	public List<User> getUserList()
	{
		this.userList = new ArrayList();
		this.userList.addAll(getUserService().getUsers());
		return this.userList;
	}

	public List<User> getUserListBySeachCriteria()
	{
		User user = (User)this.sessionManager.getSessionAttribute("login_user");
		this.userList = new ArrayList();
		if (user != null) {
			if ((this.searchValue.isEmpty()) && (this.userStatus == 1)) {
				this.userList.addAll(getUserService().getUserBySeachCriteria(user.getId()));
			} else if ((!this.searchValue.isEmpty()) && (this.userStatus == 1)) {
				this.userList.addAll(getUserService().getUserBySeachCriteriaStatus(user.getId(), this.searchValue));
			} else {
				this.userList.addAll(getUserService().getUserBySeachCriteria(user.getId(), this.userStatus, this.searchValue));
			}
		}
		Collections.reverse(this.userList);
		return this.userList;
	}

	public User loginUser()
	{
		User user = null;
		user = getUserService().loginUser(this.email, this.password);
		return user;
	}

	public SubscriberUser loginSubscriberUser()
	{
		SubscriberUser subscriberUser = null;
		subscriberUser = getUserService().loginSubscriberUser(this.email, this.password);
		return subscriberUser;
	}

	public String redirctMenuLink(){
	 
		
		
		String redirectLink = null;
		FacesUtils facesUtils = new FacesUtils();
		String menuIdStr = facesUtils.getRequestParameterMap("menuId");
		this.sessionManager.setUserInSession("brs.menu", menuIdStr);
		int menuId = Integer.valueOf(menuIdStr).intValue();
		User user = (User)this.sessionManager.getSessionAttribute("login_user");
		switch (menuId)
		{
		case 2: 
			redirectLink = "manageUser";
			break;
		case 3: 
			redirectLink = "manageSubscriber";
			break;
		case 4: 
			redirectLink = "managedataentry";
			break;
		case 5: 
			redirectLink = "/pages/de/image_upload.xhtml?userId=" + user.getId() + "&faces-redirect=true";
			break;
		case 6: 
			redirectLink = "gallery";
			break;
		case 7: 
			redirectLink = "managePublication";
			break;
		case 8: 
			redirectLink = "setting";
			break;
		case 9: 
			redirectLink = "view_qcjob_by_journal";
			break;
		case 12: 
			redirectLink = "manage_company";
			break;
		}
		return redirectLink;
	}

	public List<DeCompany> getAllCompnayList() {
		return this.allCompnayList;
	}

	public void setAllCompnayList(List<DeCompany> allCompnayList) {
		this.allCompnayList = allCompnayList;
	}

	public String getActiveMenu()
	{
		String menu = (String)this.sessionManager.getSessionAttribute("brs.menu");
		if (StringUtils.isNotEmpty(menu)) {
			return menu;
		}
		return null;
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

	public String activiateUser()
	{
		try
		{
			int flag = 0;
			String usrname = "";
			User user = (User)this.sessionManager.getSessionAttribute("login_user");
			if (user == null) {
				FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "login.xhtml");
			}
			FacesUtils facesUtils = new FacesUtils();
			int status = Integer.valueOf(facesUtils.getRequestParameterMap("status")).intValue();
			for (Map.Entry<Integer, Boolean> entry : this.checkedUser.entrySet()) {
				if (((Boolean)entry.getValue()).booleanValue())
				{
					user = this.userService.getUserById(((Integer)entry.getKey()).intValue());
					if ((status == 0) && (user.getIsActive().booleanValue()))
					{
						user.setIsActive(Boolean.valueOf(false));
						usrname = usrname + user.getFirstName();
						flag = 1;
					}
					else if ((status == 1) && (!user.getIsActive().booleanValue()))
					{
						user.setIsActive(Boolean.valueOf(true));
						usrname = usrname + user.getFirstName();
						flag = 2;
					}
					else if ((status == 0) && (!user.getIsActive().booleanValue()))
					{
						this.messageService.messageInformation(null, "User has been already Deactivated.");
					}
					else
					{
						this.messageService.messageInformation(null, "User has been already Activated.");
					}
					this.userService.updateUser(user);
				}
			}
			if ((status == 0) && (flag == 1)) {
				this.messageService.messageInformation(null, "User has been Deactivated Successfully.");
			} else if ((status == 1) && (flag == 2)) {
				this.messageService.messageInformation(null, "User has been Activated Successfully.");
			}
		}
		catch (Exception e)
		{
			System.out.println("userBean:activiateUser" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public String deleteUser()
	{
		try
		{
			User user = (User)this.sessionManager.getSessionAttribute("login_user");
			if (user == null) {
				FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "login.xhtml");
			}
			for (Map.Entry<Integer, Boolean> entry : this.checkedUser.entrySet()) {
				if (((Boolean)entry.getValue()).booleanValue())
				{
					User userObj = this.userService.getUserById(((Integer)entry.getKey()).intValue());
					userObj.setIsDeleted(Boolean.valueOf(true));
					this.userService.updateUser(userObj);
				}
			}
			this.checkedUser = new HashMap();
			this.messageService.messageInformation(null, "User has been Deleted Successfully.");
		}
		catch (Exception e)
		{
			System.out.println("userBean:deleteUser" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void scrollerAction(ActionEvent event)
	{
		ScrollerActionEvent scrollerEvent = (ScrollerActionEvent)event;
		FacesContext.getCurrentInstance().getExternalContext().log(
				"scrollerAction: facet: " + 
						scrollerEvent.getScrollerfacet() + 
						", pageindex: " + 
						scrollerEvent.getPageIndex());
	}

	public String saveAndExitUser()
	{
		try
		{
			this.currentUser = ((User)this.sessionManager.getSessionAttribute("login_user"));
			if ((this.currentUser != null) && (this.firstName != null) && (StringUtility.isNotEmpty(this.firstName)) && (this.email != null) && (StringUtility.isNotEmpty(this.email)) && (this.lastName != null) && (StringUtility.isNotEmpty(this.lastName)))
			{
				Date today = new Date();
				if ((this.dateofBirth == null) || (this.dateofBirth.before(today)))
				{
					User user = new User();
					String userTypeValue = "";
					User duplicate = this.userService.getUserByEmail(this.email);
					if (duplicate == null)
					{
						if ((this.password != null) && (this.password.equals(this.confirmPassword)))
						{
							user = getAllUserValues(user);
							if (this.userTypeId == 3L) {
								userTypeValue = "Data Entry";
							} else if (this.userTypeId == 4L) {
								userTypeValue = "Admin";
							} else {
								userTypeValue = "Quality Check";
							}
							String encrptPwd = this.password;
							user.setPassword(encrptPwd);
							user.setIsActive(Boolean.valueOf(true));
							user.setCreated_by(this.currentUser.getId());
							user.setIsDeleted(Boolean.valueOf(false));
							this.userService.addUser(user);
							this.messageService.messageInformation(null, "User has been Added Successfully.");
							EmailManager.newUser(this.email, this.firstName, this.email, this.password, userTypeValue, this.currentUser.getEmail() != null ? this.currentUser.getEmail() : "");
							return "returnmanageuser";
						}
						this.messageService.messageError(null, "Password doesn't match.");
						return null;
					}
					this.messageService.messageError(null, "Email already exist.");
					return null;
				}
				this.messageService.messageError(null, "Please check your Date of Birth");
				return null;
			}
			this.messageService.messageError(null, "Please enter the First name and Last name.");
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String updateAndExitUser()
	{
		try
		{
			String userTypeValue = "";
			int flag = 0;
			this.currentUser = ((User)this.sessionManager.getSessionAttribute("login_user"));
			Object sessionObj = this.sessionManager.getSessionAttribute("edit_user");
			int userId = sessionObj != null ? ((Integer)sessionObj).intValue() : 0;
			if ((this.currentUser != null) && (userId > 0) && (this.firstName != null) && (StringUtility.isNotEmpty(this.firstName)) && (this.email != null) && (StringUtility.isNotEmpty(this.email)) && (this.lastName != null) && (StringUtility.isNotEmpty(this.lastName)))
			{
				Date today = new Date();
				if ((this.dateofBirth == null) || (this.dateofBirth.before(today)))
				{
					User duplicate = this.userService.getUserByEmail(userId, this.email);
					if (duplicate == null)
					{
						User user = this.userService.getUserById(userId);
						if (user != null)
						{
							if (this.userTypeId != user.getUserType().getId())
							{
								flag = 1;
								if (this.userTypeId == 3L) {
									userTypeValue = "Data Entry";
								} else if (this.userTypeId == 4L) {
									userTypeValue = "Admin";
								} else {
									userTypeValue = "Quality Check";
								}
							}
							user = getAllUserValues(user);
							user.setIsActive(Boolean.valueOf(true));
							user.setIsDeleted(Boolean.valueOf(false));
							this.userService.updateUser(user);
							this.messageService.messageInformation(null, "User has been Updated Successfully.");
							if (flag == 1) {
								EmailManager.userByRole(this.email, this.firstName, this.email, userTypeValue, this.currentUser.getEmail() != null ? this.currentUser.getEmail() : "");
							}
							return "returnmanageuser";
						}
					}
					else
					{
						this.messageService.messageError(null, "Email already exist.");
						return null;
					}
				}
				else
				{
					this.messageService.messageError(null, "Please Check your Date of Birth");
					return null;
				}
			}
			else
			{
				this.messageService.messageError(null, "Please enter the First name ,Last name and Email Address.");
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void loadUserInfo(ComponentSystemEvent event)
	{
		try
		{
			if (!FacesContext.getCurrentInstance().isPostback())
			{
				Object sessionObj = this.sessionManager.getSessionAttribute("edit_user");
				this.id = (sessionObj != null ? ((Integer)sessionObj).intValue() : 0);
				if (this.id > 0)
				{
					User user = this.userService.getUserById(this.id);
					setUserDetails(user);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String saveAndExitSubscriber()
	{
		try
		{
			this.currentUser = ((User)this.sessionManager.getSessionAttribute("login_user"));
			if ((this.currentUser != null) && (this.companyName != null) && (StringUtility.isNotEmpty(this.companyName)) && (this.companyLocation != null) && (StringUtility.isNotEmpty(this.companyLocation)))
			{
				Date today = new Date();
				if ((this.dateofBirth == null) || (this.dateofBirth.before(today)))
				{
					SubscriberPublication subscriberPublication = new SubscriberPublication();
					Subscriber subscriber = new Subscriber();
					String subscriberType = "";
					String subscriberUserType = "";
					subscriber = getAllSubscriberValues(subscriber);
					subscriberType = "Subscriber";
					subscriber.setIsActive(Boolean.valueOf(true));
					subscriber.setCreated_by(this.currentUser);
					subscriber.setIsDeleted(Boolean.valueOf(false));
					this.userService.addSubscriber(subscriber);
					if ((this.publicationStringList != null) && (this.publicationStringList.size() > 0)) {
						for (String[] publicationString : this.publicationStringList)
						{
							Publication publication = this.userService.getPublicationById(Integer.valueOf(publicationString[0]).intValue());
							if (publication != null)
							{
								subscriberPublication.setPublicationTitle(publication);

								SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
								Date formdate = formatter.parse(publicationString[2]);
								Date todate = formatter.parse(publicationString[3]);
								subscriberPublication.setFromDate(formdate);
								subscriberPublication.setToDate(todate);
								subscriberPublication.setIsDeleted(Boolean.valueOf(false));
								subscriberPublication.setSubscriber(subscriber);
								this.userService.addSubscriberPublication(subscriberPublication);
							}
						}
					}
					if ((this.addSubscriberUserList != null) && (this.addSubscriberUserList.size() > 0)) {
						for (String[] subscriberString : this.addSubscriberUserList)
						{
							SubscriberUser subscriberUser = new SubscriberUser();
							if (subscriberString != null)
							{
								subscriberUserType = "Subscriber User";
								subscriberUser.setFirstName(subscriberString[0]);
								subscriberUser.setLastName(subscriberString[1]);
								subscriberUser.setEmail(subscriberString[2]);
								String encrptPwd = subscriberString[3];
								subscriberUser.setPassword(encrptPwd);
								subscriberUser.setIsActive(Boolean.valueOf(true));
								subscriberUser.setCreated_by(this.currentUser.getId());
								subscriberUser.setIsDeleted(Boolean.valueOf(false));
								if (subscriberString[4].equals("6")) {
									this.userType = this.userService.getUserTypeById(6L);
								} else {
									this.userType = this.userService.getUserTypeById(5L);
								}
								subscriberUser.setUserType(this.userType);
								subscriberUser.setSubscriber(subscriber);
								if ((subscriberString[6] != null) && (!subscriberString[6].isEmpty())) {
									subscriberUser.setGender(Short.valueOf(subscriberString[6]).shortValue());
								}
								subscriberUser.setAddress(subscriberString[7]);
								subscriberUser.setCity(subscriberString[8]);
								subscriberUser.setState(subscriberString[9]);
								subscriberUser.setPhoneNo(subscriberString[10]);
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
								if ((subscriberString[11] != null) && (!subscriberString[11].isEmpty()))
								{
									Date dob = formatter.parse(subscriberString[11]);
									subscriberUser.setDateofBirth(dob);
								}
								subscriberUser.setCountry(subscriberString[12]);
								subscriberUser.setPincode(subscriberString[13]);
								this.userService.addSubscriberUser(subscriberUser);
								if (subscriberString[4].equals("6")) {
									EmailManager.newUser(subscriberString[2], subscriberString[0], subscriberString[2], subscriberString[3], subscriberUserType, this.currentUser.getEmail() != null ? this.currentUser.getEmail() : "");
								} else {
									EmailManager.newUser(subscriberString[2], subscriberString[0], subscriberString[2], subscriberString[3], subscriberType, this.currentUser.getEmail() != null ? this.currentUser.getEmail() : "");
								}
							}
						}
					}
					this.messageService.messageInformation(null, "Subscriber has been Added  Successfully.");
					return "returnmanageSubscriber";
				}
				this.messageService.messageError(null, "Please Check your Date of Birth");
				return null;
			}
			this.messageService.messageError(null, "Please enter company Name and company Location");
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private Subscriber getAllSubscriberValues(Subscriber subscriber)
	{
		subscriber.setCompanyName(this.companyName);
		subscriber.setCompanyLocation(this.companyLocation);
		subscriber.setNotes(this.notes);
		return subscriber;
	}

	private SubscriberUser getAllsubscriberUserValues(SubscriberUser subscriberUser)
	{
		subscriberUser.setFirstName(this.firstName);
		subscriberUser.setLastName(this.lastName);
		subscriberUser.setGender(this.gender);
		subscriberUser.setEmail(this.email);
		subscriberUser.setAddress(this.address);
		subscriberUser.setCity(this.city);
		subscriberUser.setPhoneNo(this.phoneNo);
		subscriberUser.setPincode(this.pincode);
		subscriberUser.setState(this.state);
		subscriberUser.setCountry(this.country);
		subscriberUser.setDateofBirth(this.dateofBirth);
		if (this.companyName == null) {
			this.userType = this.userService.getUserTypeById(6L);
		} else {
			this.userType = this.userService.getUserTypeById(5L);
		}
		subscriberUser.setUserType(this.userType);
		return subscriberUser;
	}

	public String updateAndExitSubscriber()
	{
		try
		{
			Boolean flag = Boolean.valueOf(false);
			this.currentUser = ((User)this.sessionManager.getSessionAttribute("login_user"));
			Object sessionObj = this.sessionManager.getSessionAttribute("edit_user");
			int subscriberId = sessionObj != null ? ((Integer)sessionObj).intValue() : 0;
			if ((this.currentUser != null) && (subscriberId > 0) && (this.companyName != null) && (StringUtility.isNotEmpty(this.companyName)) && (this.companyLocation != null) && (StringUtility.isNotEmpty(this.companyLocation)))
			{
				Subscriber subscriber = this.userService.getSubscriberById(subscriberId);
				if (subscriber != null)
				{
					subscriber = getAllSubscriberValues(subscriber);
					subscriber.setIsActive(Boolean.valueOf(true));
					subscriber.setIsDeleted(Boolean.valueOf(false));
					this.userService.updateSubscriber(subscriber);
					SubscriberPublication subscriberPublication;
					if ((this.publicationStringList != null) && (this.publicationStringList.size() > 0)) {
						for (String[] publicationString : this.publicationStringList)
						{
							subscriberPublication = this.userService.findBySubscriberPublicationId(Integer.valueOf(publicationString[0]).intValue());
							if (subscriberPublication != null)
							{
								subscriberPublication.setPublicationTitle(subscriberPublication.getPublicationTitle());

								SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
								Date formdate = formatter.parse(publicationString[2]);
								Date todate = formatter.parse(publicationString[3]);
								subscriberPublication.setFromDate(formdate);
								subscriberPublication.setToDate(todate);
								subscriberPublication.setSubscriber(subscriber);
								subscriberPublication.setIsDeleted(Boolean.valueOf(false));
								this.userService.updateSubscriberPublication(subscriberPublication);
							}
							else
							{
								Publication publication = this.userService.getPublicationById(Integer.valueOf(publicationString[0]).intValue());
								if (publication != null)
								{
									subscriberPublication = new SubscriberPublication();
									subscriberPublication.setPublicationTitle(publication);
									SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
									Date formdate = formatter.parse(publicationString[2]);
									Date todate = formatter.parse(publicationString[3]);
									subscriberPublication.setFromDate(formdate);
									subscriberPublication.setToDate(todate);
									subscriberPublication.setSubscriber(subscriber);
									subscriberPublication.setIsDeleted(Boolean.valueOf(false));
									this.userService.addSubscriberPublication(subscriberPublication);
								}
							}
						}
					}
					if ((addSubscriberUserList != null) && (addSubscriberUserList.size() > 0))
					{
						SubscriberUser subscriberUser = null;
						for (String[] addSubscriberUserString : addSubscriberUserList)   {
							if ((addSubscriberUserString[5] != null) && (!addSubscriberUserString[5].isEmpty())) {
								subscriberUser = this.userService.getSubscriberUserById(Integer.valueOf(addSubscriberUserString[5]).intValue());
							}
							if ((subscriberUser != null) && (addSubscriberUserString != null) && (addSubscriberUserString[5] != null) && (!addSubscriberUserString[5].isEmpty()))
							{
								subscriberUser.setFirstName(addSubscriberUserString[0]);
								subscriberUser.setLastName(addSubscriberUserString[1]);
								if (subscriberUser.getEmail().equals(addSubscriberUserString[2]))
								{
									subscriberUser.setEmail(addSubscriberUserString[2]);
									flag = Boolean.valueOf(false);
								}
								else
								{
									subscriberUser.setEmail(addSubscriberUserString[2]);
									flag = Boolean.valueOf(true);
								}
								if ((addSubscriberUserString[3] != null) && (!addSubscriberUserString[3].isEmpty()))
								{
									String encrptPwd = addSubscriberUserString[3];
									subscriberUser.setPassword(encrptPwd);
								}
								subscriberUser.setIsActive(Boolean.valueOf(true));
								subscriberUser.setCreated_by(this.currentUser.getId());
								subscriberUser.setIsDeleted(Boolean.valueOf(false));
								if (addSubscriberUserString[4].equals("6")) {
									this.userType = this.userService.getUserTypeById(6L);
								} else {
									this.userType = this.userService.getUserTypeById(5L);
								}
								subscriberUser.setUserType(this.userType);
								subscriberUser.setSubscriber(subscriber);
								subscriberUser.setIsActive(Boolean.valueOf(true));
								subscriberUser.setIsDeleted(Boolean.valueOf(false));
								if ((addSubscriberUserString[6] != null) && (!addSubscriberUserString[6].isEmpty())) {
									subscriberUser.setGender(Short.valueOf(addSubscriberUserString[6]).shortValue());
								}
								subscriberUser.setAddress(addSubscriberUserString[7]);
								subscriberUser.setCity(addSubscriberUserString[8]);
								subscriberUser.setState(addSubscriberUserString[9]);
								subscriberUser.setPhoneNo(addSubscriberUserString[10]);
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
								if ((addSubscriberUserString[11] != null) && (!addSubscriberUserString[11].isEmpty()))
								{
									Date dob = formatter.parse(addSubscriberUserString[11]);
									subscriberUser.setDateofBirth(dob);
								}
								subscriberUser.setCountry(addSubscriberUserString[12]);
								subscriberUser.setPincode(addSubscriberUserString[13]);
								this.userService.updateSubscriberUser(subscriberUser);
								if ((flag.booleanValue()) && (addSubscriberUserString[4].equals("6")) && (addSubscriberUserString[3] != null) && (!addSubscriberUserString[3].isEmpty())) {
									EmailManager.userByRole(addSubscriberUserString[2], addSubscriberUserString[0], addSubscriberUserString[2], "Subscriber User", this.currentUser.getEmail() != null ? this.currentUser.getEmail() : "");
								} else if ((flag.booleanValue()) && (addSubscriberUserString[4].equals("5")) && (addSubscriberUserString[3] != null) && (!addSubscriberUserString[3].isEmpty())) {
									EmailManager.userByRole(addSubscriberUserString[2], addSubscriberUserString[0], addSubscriberUserString[2], "Subscriber", this.currentUser.getEmail() != null ? this.currentUser.getEmail() : "");
								}
							}
							else
							{
								subscriberUser = new SubscriberUser();
								subscriberUser.setFirstName(addSubscriberUserString[0]);
								subscriberUser.setLastName(addSubscriberUserString[1]);
								subscriberUser.setEmail(addSubscriberUserString[2]);
								String encrptPwd = addSubscriberUserString[3];
								subscriberUser.setPassword(encrptPwd);
								subscriberUser.setIsActive(Boolean.valueOf(true));
								subscriberUser.setCreated_by(this.currentUser.getId());
								subscriberUser.setIsDeleted(Boolean.valueOf(false));
								if (addSubscriberUserString[4].equals("6")) {
									this.userType = this.userService.getUserTypeById(6L);
								} else {
									this.userType = this.userService.getUserTypeById(5L);
								}
								subscriberUser.setUserType(this.userType);
								subscriberUser.setSubscriber(subscriber);
								if ((addSubscriberUserString[6] != null)&&(!addSubscriberUserString[6].isEmpty())) {
									subscriberUser.setGender(Short.valueOf(addSubscriberUserString[6]).shortValue());
								}
								subscriberUser.setAddress(addSubscriberUserString[7]);
								subscriberUser.setCity(addSubscriberUserString[8]);
								subscriberUser.setState(addSubscriberUserString[9]);
								subscriberUser.setPhoneNo(addSubscriberUserString[10]);
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
								if ((addSubscriberUserString[11] != null) && (!addSubscriberUserString[11].isEmpty()))
								{
									Date dob = formatter.parse(addSubscriberUserString[11]);
									subscriberUser.setDateofBirth(dob);
								}
								subscriberUser.setCountry(addSubscriberUserString[12]);
								subscriberUser.setPincode(addSubscriberUserString[13]);
								this.userService.addSubscriberUser(subscriberUser);
								if (addSubscriberUserString[4].equals("6")) {
									EmailManager.newUser(addSubscriberUserString[2], addSubscriberUserString[0], addSubscriberUserString[2], addSubscriberUserString[3], "Subscriber User", this.currentUser.getEmail() != null ? this.currentUser.getEmail() : "");
								} else {
									EmailManager.newUser(addSubscriberUserString[2], addSubscriberUserString[0], addSubscriberUserString[2], addSubscriberUserString[3], "Subscriber", this.currentUser.getEmail() != null ? this.currentUser.getEmail() : "");
								}
							}
						}
					}
					if ((deletePublicationStringList != null) && (deletePublicationStringList.size() > 0)) {
						for (String[] publicationString : deletePublicationStringList) {
							SubscriberPublication subscriberPublications = userService.findBySubscriberPublicationId(Integer.valueOf(publicationString[0]));
							if (subscriberPublications != null)
							{
								subscriberPublications.setIsDeleted(Boolean.valueOf(true));
								this.userService.updateSubscriberPublication(subscriberPublications);
							}
						}
					}
					if ((this.deleteSubscriberUserList != null) && (this.deleteSubscriberUserList.size() > 0)) {
						for (String[] deleteSubscriberUserString : deleteSubscriberUserList) {
							SubscriberUser subscriberUser = userService.getSubscriberUserById(Integer.valueOf(deleteSubscriberUserString[5]));
							if(subscriberUser != null){
								subscriberUser.setIsDeleted(Boolean.valueOf(true));
								this.userService.updateSubscriberUser(subscriberUser);
							}
						}
					}
					this.messageService.messageInformation(null, "Subscriber has been Updated Successfully.");
					return "returnmanageSubscriber";
				}
			}
			else
			{
				this.messageService.messageError(null, "Please enter company Name and company Location");
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void loadSubscriberInfo(ComponentSystemEvent event)
	{
		try
		{
			if (!FacesContext.getCurrentInstance().isPostback())
			{
				Object sessionObj = this.sessionManager.getSessionAttribute("edit_user");
				this.id = (sessionObj != null ? ((Integer)sessionObj).intValue() : 0);
				if (this.id > 0)
				{
					Subscriber subscriber = this.userService.getSubscriberById(this.id);
					

					List<SubscriberUser> subscriberUser = this.userService.getSubscriberUserListBySubscriberId(this.id);
					Collections.reverse(subscriberUser);
					setSubscriberDetails(subscriber);
					List<SubscriberPublication> subscriberPublication = this.userService.findPublicationBySubscriber(this.id);
					if ((subscriberPublication != null) && (subscriberPublication.size() > 0)) {
						for (int i = 0; subscriberPublication.size() > i; i++)
						{
							String[] publication = new String[4];
							publication[0] = String.valueOf(((SubscriberPublication)subscriberPublication.get(i)).getId());
							publication[1] = ((SubscriberPublication)subscriberPublication.get(i)).getPublicationTitle().getPublicationTitle();
							Date fromDate = ((SubscriberPublication)subscriberPublication.get(i)).getFromDate();
							Date toDate = ((SubscriberPublication)subscriberPublication.get(i)).getToDate();
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							publication[2] = formatter.format(fromDate);
							publication[3] = formatter.format(toDate);
							this.publicationStringList.add(publication);
						}
					}
					if ((subscriberUser != null) && (subscriberUser.size() > 0)) {
						for (int i = 0; subscriberUser.size() > i; i++)
						{
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							String[] subscriberList = new String[15];
							subscriberList[0] = ((SubscriberUser)subscriberUser.get(i)).getFirstName();
							subscriberList[1] = ((SubscriberUser)subscriberUser.get(i)).getLastName();
							subscriberList[2] = ((SubscriberUser)subscriberUser.get(i)).getEmail();
							this.subscriberUserIdByAdmin = ((SubscriberUser)subscriberUser.get(i)).getId();
							subscriberList[4] = String.valueOf(((SubscriberUser)subscriberUser.get(i)).getUserType().getId());
							subscriberList[5] = String.valueOf(((SubscriberUser)subscriberUser.get(i)).getId());
							subscriberList[6] = String.valueOf(((SubscriberUser)subscriberUser.get(i)).getGender());
							subscriberList[7] = ((SubscriberUser)subscriberUser.get(i)).getAddress();
							subscriberList[8] = ((SubscriberUser)subscriberUser.get(i)).getCity();
							subscriberList[9] = ((SubscriberUser)subscriberUser.get(i)).getState();
							subscriberList[10] = ((SubscriberUser)subscriberUser.get(i)).getPhoneNo();
							if (((SubscriberUser)subscriberUser.get(i)).getDateofBirth() != null) {
								subscriberList[11] = formatter.format(((SubscriberUser)subscriberUser.get(i)).getDateofBirth());
							}
							subscriberList[12] = ((SubscriberUser)subscriberUser.get(i)).getCountry();
							subscriberList[13] = ((SubscriberUser)subscriberUser.get(i)).getPincode();
							this.gender = ((SubscriberUser)subscriberUser.get(i)).getGender();
							this.dateofBirth = ((SubscriberUser)subscriberUser.get(i)).getDateofBirth();
							this.address = ((SubscriberUser)subscriberUser.get(i)).getAddress();
							this.city = ((SubscriberUser)subscriberUser.get(i)).getCity();
							this.state = ((SubscriberUser)subscriberUser.get(i)).getState();
							this.country = ((SubscriberUser)subscriberUser.get(i)).getCountry();
							this.pincode = ((SubscriberUser)subscriberUser.get(i)).getPincode();
							this.phoneNo = ((SubscriberUser)subscriberUser.get(i)).getPhoneNo();
							this.addSubscriberUserList.add(subscriberList);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void setSubscriberDetails(Subscriber subscriber)
	{
		this.id = subscriber.getId();
		this.companyName = subscriber.getCompanyName();
		this.companyLocation = subscriber.getCompanyLocation();
		this.notes = subscriber.getNotes();
	}

	private void setSubscriberUserDetails(SubscriberUser subscriberUser)
	{
		this.subscriberUserId = subscriberUser.getId();
		this.firstName = subscriberUser.getFirstName();
		this.lastName = subscriberUser.getLastName();
		this.gender = subscriberUser.getGender();
		this.email = subscriberUser.getEmail();
		this.dateofBirth = subscriberUser.getDateofBirth();
		this.address = subscriberUser.getAddress();
		this.city = subscriberUser.getCity();
		this.state = subscriberUser.getState();
		this.country = subscriberUser.getCountry();
		this.pincode = subscriberUser.getPincode();
		this.phoneNo = subscriberUser.getPhoneNo();
	}

	private void setSubscriberDetailsAdmin(Subscriber subscriber)
	{
		this.id = subscriber.getId();
		this.companyName = subscriber.getCompanyName();
		this.companyLocation = subscriber.getCompanyLocation();
		this.notes = subscriber.getNotes();
	}

	private void setSubscriberUserDetailsAdmin(SubscriberUser subscriberUser)
	{
		this.id = subscriberUser.getId();
		this.firstName = subscriberUser.getFirstName();
		this.lastName = subscriberUser.getLastName();
		this.gender = subscriberUser.getGender();
		this.email = subscriberUser.getEmail();
		this.dateofBirth = subscriberUser.getDateofBirth();
		this.address = subscriberUser.getAddress();
		this.city = subscriberUser.getCity();
		this.state = subscriberUser.getState();
		this.country = subscriberUser.getCountry();
		this.pincode = subscriberUser.getPincode();
		this.phoneNo = subscriberUser.getPhoneNo();
	}

	public String loadAddOrEditSubscriber()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("subscriberId");
		int subscriberId = Integer.valueOf(val != null ? val : "0").intValue();
		if (subscriberId > 0) {
			this.sessionManager.setSessionAttributeInSession("edit_user", Integer.valueOf(subscriberId));
		} else {
			this.sessionManager.removeSessionAttributeInSession("edit_user");
		}
		if (subscriberId > 0) {
			return "editSubscriber";
		}
		return "addSubscriber";
	}

	public List<Subscriber> getSubscriberListBySeachCriteria()
	{
		this.subscriberList = new ArrayList();
		this.currentUser = ((User)this.sessionManager.getSessionAttribute("login_user"));
		if ((this.currentUser != null) && (this.searchValue.isEmpty())) {
			this.subscriberList.addAll(getUserService().getSubscriberBySeachCriteria());
		} else {
			this.subscriberList.addAll(getUserService().getSubscriberBySeachCriteria(this.searchValue));
		}
		Collections.reverse(this.subscriberList);
		return this.subscriberList;
	}

	
	public void  getAllCompanies(){
		Gson gson = new Gson();
		gson.toJson(this.allCompnayList.addAll(getUserService().getAllCompanyData()));
		compList = gson.toJson(this.allCompnayList.addAll(getUserService().getAllCompanyData()));
		compList = gson.toJson(this.allCompnayList);
		System.out.println("compList: "+compList);
		//System.out.println("userListBySeachCriteria");
	}
	
	public String getCompList() {
		System.out.println("compList"+compList);
		return this.compList;
	}

	public void setCompList(String compList) {
		System.out.println("set compList"+compList);
		this.compList = compList;
	}

	public List<SubscriberUser> getSubscriberUserListBySeachCriteria()
	{
		SubscriberUser subscriberUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
		this.subscriberUserList = new ArrayList();
		if ((subscriberUser != null) && (this.searchValue.isEmpty())) {
			this.subscriberUserList.addAll(getUserService().getSubscriberUserBySeachCriteria(subscriberUser.getSubscriber().getId()));
		} else {
			this.subscriberUserList.addAll(getUserService().getSubscriberUserBySeachCriteria(subscriberUser.getSubscriber().getId(), this.searchValue));
		}
		Collections.reverse(this.subscriberUserList);
		return this.subscriberUserList;
	}

	public String activiateSubscriber()
	{
		try
		{
			int flag = 0;
			String subscriberNmae = "";
			User user = (User)this.sessionManager.getSessionAttribute("login_user");
			if (user == null) {
				FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "login.xhtml");
			}
			FacesUtils facesUtils = new FacesUtils();
			int status = Integer.valueOf(facesUtils.getRequestParameterMap("status")).intValue();
			for (Map.Entry<Integer, Boolean> entry : this.checkedUser.entrySet()) {
				if (((Boolean)entry.getValue()).booleanValue())
				{
					Subscriber subscriberObj = this.userService.getSubscriberById(((Integer)entry.getKey()).intValue());
					if (subscriberObj != null)
					{
						SubscriberUser subscriberUserObj = this.userService.getSubscriberUserBySubscriberId(((Integer)entry.getKey()).intValue());
						if (subscriberUserObj != null)
						{
							if ((status == 0) && (subscriberUserObj.getIsActive().booleanValue()))
							{
								subscriberUserObj.setIsActive(Boolean.valueOf(false));
								subscriberObj.setIsActive(Boolean.valueOf(false));
								subscriberNmae = subscriberNmae + subscriberObj.getCompanyName();
								flag = 1;
							}
							else if ((status == 1) && (!subscriberUserObj.getIsActive().booleanValue()))
							{
								subscriberUserObj.setIsActive(Boolean.valueOf(true));
								subscriberObj.setIsActive(Boolean.valueOf(true));
								subscriberNmae = subscriberNmae + subscriberObj.getCompanyName();
								flag = 2;
							}
							else if ((status == 0) && (!subscriberUserObj.getIsActive().booleanValue()))
							{
								this.messageService.messageInformation(null, "Subscriber has been already Deactivated.");
							}
							else
							{
								this.messageService.messageInformation(null, "Subscriber has been already Activated.");
							}
							this.userService.updateSubscriberUser(subscriberUserObj);
							this.userService.updateSubscriber(subscriberObj);
						}
					}
				}
			}
			if ((status == 0) && (flag == 1)) {
				this.messageService.messageInformation(null, "Subscriber has been Deactivated Successfully.");
			} else if ((status == 1) && (flag == 1)) {
				this.messageService.messageInformation(null, "Subscriber has been Activated Successfully.");
			}
		}
		catch (Exception e)
		{
			System.out.println("userBean:activiateCompanyUser" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public String deleteSubscriber()
	{
		try
		{
			User user = (User)this.sessionManager.getSessionAttribute("login_user");
			if (user == null) {
				FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "login.xhtml");
			}
			for (Map.Entry<Integer, Boolean> entry : this.checkedUser.entrySet()) {
				if (((Boolean)entry.getValue()).booleanValue())
				{
					Subscriber subscriberObj = this.userService.getSubscriberById(((Integer)entry.getKey()).intValue());
					if (subscriberObj != null)
					{
						SubscriberUser subscriberUserObj = this.userService.getSubscriberUserBySubscriberId(((Integer)entry.getKey()).intValue());
						if (subscriberUserObj != null)
						{
							subscriberUserObj.setIsDeleted(Boolean.valueOf(true));
							subscriberObj.setIsDeleted(Boolean.valueOf(true));
							this.userService.updateSubscriberUser(subscriberUserObj);
							this.userService.updateSubscriber(subscriberObj);
						}
						else
						{
							subscriberObj.setIsDeleted(Boolean.valueOf(true));
							this.userService.updateSubscriber(subscriberObj);
						}
					}
				}
			}
			this.checkedUser = new HashMap();
			this.messageService.messageInformation(null, "Subscriber has been Deleted Successfully.");
		}
		catch (Exception e)
		{
			System.out.println("userBean:deleteCompanyUser" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public String viewUser()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("userId");
		int userId = Integer.valueOf(val != null ? val : "0").intValue();
		if (userId > 0)
		{
			this.sessionManager.setSessionAttributeInSession("edit_user", Integer.valueOf(userId));
			return "viewUserDetails";
		}
		return null;
	}

	public String userProfile()
	{
		User user = (User)this.sessionManager.getSessionAttribute("login_user");
		if (user != null)
		{
			int userId = user.getId();
			if ((userId > 0) && ((user.getUserType().getUserType().equals("Super Admin")) || (user.getUserType().getUserType().equals("Admin")) || (user.getUserType().getUserType().equals("Data Entry")) || (user.getUserType().getUserType().equals("Quality Check"))))
			{
				this.sessionManager.setSessionAttributeInSession("edit_user", Integer.valueOf(userId));
				String menuIdStr = "111";
				this.sessionManager.setUserInSession("brs.menu", menuIdStr);
				return "userProfile";
			}
		}
		return null;
	}

	public String viewSubscriber()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("subscriberId");
		int subscriberId = Integer.valueOf(val != null ? val : "0").intValue();
		if (subscriberId > 0)
		{
			this.sessionManager.setSessionAttributeInSession("edit_user", Integer.valueOf(subscriberId));
			return "viewSubscriberDetails";
		}
		return null;
	}

	public void localeChanged(ValueChangeEvent event)
	{
		String rowsPerPage = event.getNewValue().toString();
		this.sessionManager.setSessionAttributeInSession("rowsPerPage", rowsPerPage);
	}

	public String loadEditUser()
	{
		User user = (User)this.sessionManager.getSessionAttribute("login_user");
		int userId = user.getId();
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("pageRedirect");
		this.pageRedirect = Integer.valueOf(val != null ? val : "0").intValue();
		if ((userId > 0) && ((user.getUserType().getUserType().equals("Super Admin")) || (user.getUserType().getUserType().equals("Admin")) || (user.getUserType().getUserType().equals("Data Entry")) || (user.getUserType().getUserType().equals("Quality Check")))) {
			this.sessionManager.setSessionAttributeInSession("edit_user", Integer.valueOf(userId));
		} else {
			this.sessionManager.removeSessionAttributeInSession("edit_user");
		}
		if ((userId > 0) && ((user.getUserType().getUserType().equals("Super Admin")) || (user.getUserType().getUserType().equals("Admin")) || (user.getUserType().getUserType().equals("Data Entry")) || (user.getUserType().getUserType().equals("Quality Check")))) {
			return "editAdminUser";
		}
		return null;
	}

	public String loadEditSubscriber()
	{
		SubscriberUser subscriberUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
		int subscriberId = subscriberUser.getSubscriber().getId();
		if (subscriberId > 0) {
			this.sessionManager.setSessionAttributeInSession("edit_subscriber_user", Integer.valueOf(subscriberId));
		} else {
			this.sessionManager.removeSessionAttributeInSession("edit_subscriber_user");
		}
		if (subscriberId > 0) {
			return "editSubscriberProfile";
		}
		return null;
	}

	public String logoNavigation()
	{
		String menuIdStr = null;
		User user = (User)this.sessionManager.getSessionAttribute("login_user");
		if (user != null)
		{
			int userId = user.getId();
			if ((userId > 0) && (user.getUserType().getUserType().equals("Data Entry")))
			{
				menuIdStr = "2";
				this.sessionManager.setUserInSession("brs.menu", menuIdStr);
				return "managedataentry";
			}
			if ((userId > 0) && (user.getUserType().getUserType().equals("Quality Check")))
			{
				menuIdStr = "2";
				this.sessionManager.setUserInSession("brs.menu", menuIdStr);
				return "manageqcjob";
			}
			menuIdStr = "2";
			this.sessionManager.setUserInSession("brs.menu", menuIdStr);
			return "manageUser";
		}
		return null;
	}

	public void localeChangedValue(ValueChangeEvent event)
	{
		this.userStatus = ((Integer)event.getNewValue()).intValue();
	}

	public void localChangedAjax(AjaxBehaviorEvent event)
	{
		this.userStatus = ((Integer)event.getComponent().getAttributes().get("value")).intValue();
	}

	public void loadImageUserInfo(ComponentSystemEvent event)
	{
		try
		{
			if (!FacesContext.getCurrentInstance().isPostback())
			{
				User user = (User)this.sessionManager.getSessionAttribute("login_user");
				if (user != null)
				{
					this.id = user.getId();
					if (this.id > 0)
					{
						user = this.userService.getUserById(this.id);
						setUserDetails(user);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public String subscriberLogin(){
		String returnPage = null;
		try {
			SubscriberUser subscriberUser = loginSubscriberUser(); 
			if(subscriberUser != null)
			{
				sessionManager.setUserInSession(SessionManager.LOGINSUBSCRIBERUSER,subscriberUser);
				if(subscriberUser.getIsActive() && subscriberUser.getUserType().getUserType().equals("Subscriber Admin")){
					returnPage = MANAGE_SUBSCRIBER_USER;
				}
				else if(subscriberUser.getIsActive() && subscriberUser.getUserType().getUserType().equals("Subscriber RegularUser")){
					returnPage = MANAGE_SUBSCRIBER_USER;
				}
				return returnPage;
			}
			else{
				messageService.messageError(null, "Login failed");
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public SubscriberUser getLoginSubscriberUser()
	{
		//SubscriberUser subscriberUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		if (subscriberUser != null) {
			return subscriberUser;
		}
		try
		{
			FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "subscriber_login.xhtml");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new SubscriberUser();
	}

	/**
	 * redirect to the subscriber menu link
	 * set activeMenu id in session.
	 * @return
	 */
	public String redirctSubscriberMenuLink(){
		String redirectLink = null;
		FacesUtils facesUtils = new FacesUtils();
		String menuIdStr = facesUtils.getRequestParameterMap("menuId");
		sessionManager.setUserInSession(SessionManager.BRSMENU,menuIdStr);
		int menuId = Integer.valueOf(menuIdStr);
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		switch (menuId) {
		case 2:
			redirectLink = MANAGE_SUBSCRIBER_USER;
			break;
		case 3:
			redirectLink = MANAGE_REGION;
			break;
		case 4:
			redirectLink = SEARCHREPORTVIEW;
			break;
		default:
			redirectLink = MANAGE_SUBSCRIBER_USER;
			break;
		}
		return redirectLink;
	}

	public String subscriberProfile()
	{
		SubscriberUser subscriberUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
		if (subscriberUser != null)
		{
			int subscriberUserId = subscriberUser.getSubscriber().getId();
			if ((subscriberUser.getUserType().getUserType().equals("Subscriber Admin")) || (subscriberUser.getUserType().getUserType().equals("Subscriber RegularUser")))
			{
				this.sessionManager.setSessionAttributeInSession("edit_user", Integer.valueOf(subscriberUserId));
				String menuIdStr = "111";
				this.sessionManager.setUserInSession("brs.menu", menuIdStr);
				return "subscriberProfile";
			}
		}
		return null;
	}

	public void loadSubscriberUserInfo(ComponentSystemEvent event)
	{
		try
		{
			if (!FacesContext.getCurrentInstance().isPostback())
			{
				Object sessionObj = this.sessionManager.getSessionAttribute("edit_subscriber_user_byadmin");
				int subscriberUserId = sessionObj != null ? ((Integer)sessionObj).intValue() : 0;
				if (subscriberUserId > 0)
				{
					SubscriberUser subscriberUser = this.userService.getSubscriberUserById(subscriberUserId);
					setSubscriberUserDetailsAdmin(subscriberUser);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String loadAddOrEditSubscriberUser()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("subscriberId");
		int subscriberId = Integer.valueOf(val != null ? val : "0").intValue();
		if (subscriberId > 0) {
			this.sessionManager.setSessionAttributeInSession("edit_subscriber_user_byadmin", Integer.valueOf(subscriberId));
		} else {
			this.sessionManager.removeSessionAttributeInSession("edit_subscriber_user_byadmin");
		}
		if (subscriberId > 0) {
			return "editSubscriberUser";
		}
		return "addSubscriberUser";
	}

	public String viewSubscriberUser()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("subscriberId");
		int subscriberId = Integer.valueOf(val != null ? val : "0").intValue();
		if (subscriberId > 0)
		{
			this.sessionManager.setSessionAttributeInSession("edit_subscriber_user_byadmin", Integer.valueOf(subscriberId));
			return "viewSubscriberUserDetails";
		}
		return null;
	}

	public String updateAndExitSubscriberUser()
	{
		try
		{
			SubscriberUser currentUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
			Object sessionObj = this.sessionManager.getSessionAttribute("edit_subscriber_user_byadmin");
			int subscriberId = sessionObj != null ? ((Integer)sessionObj).intValue() : 0;
			if ((currentUser != null) && (subscriberId > 0) && (this.firstName != null) && (StringUtility.isNotEmpty(this.firstName)) && (this.email != null) && (StringUtility.isNotEmpty(this.email)))
			{
				Date today = new Date();
				if ((this.dateofBirth == null) || (this.dateofBirth.before(today)))
				{
					SubscriberUser subscriberUser = this.userService.getSubscriberUserById(subscriberId);
					if (subscriberUser != null)
					{
						subscriberUser = getAllsubscriberUserValues(subscriberUser);
						subscriberUser.setIsActive(Boolean.valueOf(true));
						subscriberUser.setIsDeleted(Boolean.valueOf(false));
						subscriberUser.setSubscriber(currentUser.getSubscriber());
						this.userService.updateSubscriberUser(subscriberUser);
						this.messageService.messageInformation(null, "Subscriber User has been Updated Successfully.");
						return "returnmanageSubscriberUser";
					}
				}
				else
				{
					this.messageService.messageError(null, "Please check your Date of Birth");
					return null;
				}
			}
			else
			{
				this.messageService.messageError(null, "Please enter the First name and Email Address.");
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String saveAndExitSubscriberUser()
	{
		try
		{
			SubscriberUser currentUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
			if ((currentUser != null) && (this.firstName != null) && (StringUtility.isNotEmpty(this.firstName)) && (this.email != null) && (StringUtility.isNotEmpty(this.email)))
			{
				Date today = new Date();
				if ((this.dateofBirth == null) || (this.dateofBirth.before(today)))
				{
					SubscriberUser subscriberUser = new SubscriberUser();
					String subscriberUserType = "";
					SubscriberUser duplicate = this.userService.getSubscriberUserByEmail(this.email);
					if (duplicate == null)
					{
						if ((this.password != null) && (this.password.equals(this.confirmPassword)))
						{
							subscriberUser = getAllsubscriberUserValues(subscriberUser);
							subscriberUserType = "Subscriber User";
							String encrptPwd = this.password;
							subscriberUser.setPassword(encrptPwd);
							subscriberUser.setIsActive(Boolean.valueOf(true));
							subscriberUser.setCreated_by(currentUser.getSubscriber().getId());
							subscriberUser.setIsDeleted(Boolean.valueOf(false));
							subscriberUser.setSubscriber(currentUser.getSubscriber());
							this.userService.addSubscriberUser(subscriberUser);
							this.messageService.messageInformation(null, "Subscriber User has been Added  Successfully.");
							EmailManager.newUser(this.email, this.firstName, this.email, this.password, subscriberUserType, currentUser.getEmail() != null ? currentUser.getEmail() : "");
							return "returnmanageSubscriberUser";
						}
						this.messageService.messageError(null, "Password doesn't match.");
						return null;
					}
					this.messageService.messageError(null, "Email already exist.");
					return null;
				}
				this.messageService.messageError(null, "Please check your Date of Birth");
				return null;
			}
			this.messageService.messageError(null, "Please enter the First name and Email Address.");
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String activiateSubscriberUser()
	{
		try
		{
			int flag = 0;
			SubscriberUser subscriberUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
			if (subscriberUser == null) {
				FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "subscriber_login.xhtml");
			}
			FacesUtils facesUtils = new FacesUtils();
			int status = Integer.valueOf(facesUtils.getRequestParameterMap("status")).intValue();
			for (Map.Entry<Integer, Boolean> entry : this.checkedUser.entrySet()) {
				if (((Boolean)entry.getValue()).booleanValue())
				{
					SubscriberUser subscriberUserObj = this.userService.getSubscriberUserById(((Integer)entry.getKey()).intValue());
					if (subscriberUserObj != null)
					{
						if ((status == 0) && (subscriberUserObj.getIsActive().booleanValue()))
						{
							subscriberUserObj.setIsActive(Boolean.valueOf(false));
							flag = 1;
						}
						else if ((status == 1) && (!subscriberUserObj.getIsActive().booleanValue()))
						{
							subscriberUserObj.setIsActive(Boolean.valueOf(true));
							flag = 2;
						}
						else if ((status == 0) && (!subscriberUserObj.getIsActive().booleanValue()))
						{
							this.messageService.messageInformation(null, "Subscriber  User has been already Deactivated.");
						}
						else
						{
							this.messageService.messageInformation(null, "Subscriber User has been already Activated.");
						}
						this.userService.updateSubscriberUser(subscriberUserObj);
					}
				}
			}
			if ((status == 0) && (flag == 1)) {
				this.messageService.messageInformation(null, "Subscriber User has been Deactivated Successfully.");
			} else if ((status == 1) && (flag == 2)) {
				this.messageService.messageInformation(null, "Subscriber User has been Activated Successfully.");
			}
		}
		catch (Exception e)
		{
			System.out.println("userBean:activiateSubscriberUser" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public String deleteSubscriberUser()
	{
		try
		{
			SubscriberUser subscriberUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
			if (subscriberUser == null) {
				FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "subscriber_login.xhtml");
			}
			for (Map.Entry<Integer, Boolean> entry : this.checkedUser.entrySet()) {
				if (((Boolean)entry.getValue()).booleanValue())
				{
					SubscriberUser subscriberUserObj = this.userService.getSubscriberUserById(((Integer)entry.getKey()).intValue());
					if (subscriberUserObj != null)
					{
						subscriberUserObj.setIsDeleted(Boolean.valueOf(true));
						this.userService.updateSubscriberUser(subscriberUserObj);
					}
				}
			}
			this.checkedUser = new HashMap();
			this.messageService.messageInformation(null, "Subscriber User has been Deleted Successfully.");
		}
		catch (Exception e)
		{
			System.out.println("userBean:deleteSubscriberUser" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public String updateAndExitSubscriberProfile()
	{
		try
		{
			SubscriberUser subscriberUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
			Object sessionObj = this.sessionManager.getSessionAttribute("edit_subscriber_user");
			int subscriberId = sessionObj != null ? ((Integer)sessionObj).intValue() : 0;
			if ((subscriberUser != null) && (subscriberId > 0) && (this.companyName != null) && (StringUtility.isNotEmpty(this.companyName)) && (this.companyLocation != null) && (StringUtility.isNotEmpty(this.companyLocation)))
			{
				Subscriber subscriber = this.userService.getSubscriberById(subscriberId);
				if (subscriber != null)
				{
					subscriberUser = this.userService.getSubscriberUserBySubscriberId(subscriberId);
					if (subscriberUser != null)
					{
						subscriber = getAllSubscriberValues(subscriber);
						subscriber.setCompanyName(this.companyName);
						subscriber.setCompanyLocation(this.companyLocation);
						subscriber.setNotes(this.notes);
						subscriber.setIsActive(Boolean.valueOf(true));
						subscriber.setIsDeleted(Boolean.valueOf(false));
						
						subscriberUser = getAllsubscriberUserValues(subscriberUser);
						subscriberUser.setFirstName(this.firstName);
						subscriberUser.setLastName(this.lastName);
						subscriberUser.setPhoneNo(this.phoneNo);
						subscriberUser.setIsActive(Boolean.valueOf(true));
						subscriberUser.setIsDeleted(Boolean.valueOf(false));
						
						this.userService.updateSubscriber(subscriber);
						this.userService.updateSubscriberUser(subscriberUser);
						// after change login user
						sessionManager.setUserInSession(SessionManager.LOGINSUBSCRIBERUSER,subscriberUser);
						
						this.msgLabel = "Profile has been Updated Successfully.";
						return "/pages/subscriber/subscriber_profile.xhtml?msgLabel=" + this.msgLabel + "&faces-redirect=true";
					}
				}
			}
			else
			{
				this.messageService.messageError(null, "Please enter company Name and company Location");
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String subscriberLogoNavigation()
	{
		String menuIdStr = null;
		SubscriberUser subscriberUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
		if (subscriberUser != null)
		{
			int userId = subscriberUser.getId();
			if ((userId > 0) && (subscriberUser.getUserType().getUserType().equals("Subscriber Admin")))
			{
				menuIdStr = "1";
				this.sessionManager.setUserInSession("brs.menu", menuIdStr);
				return "manageSubscriberUser";
			}
			if ((userId > 0) && (subscriberUser.getUserType().getUserType().equals("Subscriber RegularUser")))
			{
				menuIdStr = "1";
				this.sessionManager.setUserInSession("brs.menu", menuIdStr);
				return "manageSubscriberUser";
			}
			menuIdStr = "1";
			this.sessionManager.setUserInSession("brs.menu", menuIdStr);
			return "manageSubscriberUser";
		}
		return null;
	}

	public String forgotSubscriberPassword()
	{
		try
		{
			if (StringUtility.isNotEmpty(this.email))
			{
				SubscriberUser subscriberUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
				subscriberUser = this.userService.getSubscriberUserByEmail(this.email);
				if (subscriberUser != null)
				{
					String encryptedVal = this.commonUtils.generateEncryptedPwd(subscriberUser.getFirstName());
					this.userService.updateSubscriberUser(subscriberUser);
					String resetLink = CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "pages/subscriber_reset_password.xhtml?pwd=" + encryptedVal;
					EmailManager.forgotPwd(this.email, subscriberUser.getFirstName(), resetLink);
					this.messageService.messageInformation(null, "Reset link has sent Successfully to your Email Id.");
					return "returnsubscriberhome";
				}
				this.messageService.messageError(null, "Given email Id is not matched");
				return null;
			}
			this.messageService.messageError(null, "Please enter the email id");
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return "subscriber_login.xhtml?faces-redirect=false";
	}

	public void loadSubscriberResetPwdUser(ComponentSystemEvent event)
	{
		try
		{
			if (this.forgotPwdEncryptVal == null)
			{
				this.messageService.messageError(null, "Not a valid URL link.");
			}
			else
			{
				SubscriberUser subscriberUser = this.userService.getSubscriberUserByForgotPwdEncryption(this.forgotPwdEncryptVal);
				if (subscriberUser != null) {
					this.firstName = subscriberUser.getFirstName();
				} else {
					this.messageService.messageError(null, "Not a valid URL link.");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public String resetSubscriberNewPassword()
	{
		try
		{
			if (this.forgotPwdEncryptVal == null)
			{
				this.messageService.messageError(null, "Not a valid URL link.");
			}
			else if (this.newPassword.equals(this.confirmPassword))
			{
				SubscriberUser subscriberUser = this.userService.getSubscriberUserByForgotPwdEncryption(this.forgotPwdEncryptVal);
				if (subscriberUser != null)
				{
					subscriberUser.setPassword(this.newPassword);
					this.userService.updateSubscriberUser(subscriberUser);
					this.messageService.messageInformation(null, "Your new password has changed Successfully.");
					return "returnsubscriberhome";
				}
				this.messageService.messageError(null, "Not a valid URL link.");
			}
			else
			{
				this.messageService.messageError(null, "Your new password mismatch with confirmation password.");
				this.confirmPassword = "";
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "returnsubscriberhome";
	}

	public List<SelectItem> getAllCountry()
	{
		List<Country> countryList = new ArrayList();
		List<SelectItem> countryList1 = new ArrayList();
		try
		{
			countryList = this.userService.findAll();
			for (int i = 0; i < countryList.size(); i++)
			{
				Country country = (Country)countryList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(Long.valueOf(country.getCountryId()));
				sItem.setLabel(country.getCountryName());
				countryList1.add(sItem);
			}
			return countryList1;
		}
		catch (Exception e)
		{
			System.out.println("userBean::ViewCountryList" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void loadMessage(ComponentSystemEvent event)
	{
		if (StringUtility.isNotEmpty(this.msgLabel))
		{
			FacesContext.getCurrentInstance().addMessage("userBean", new FacesMessage(FacesMessage.SEVERITY_INFO, this.msgLabel, this.msgLabel));
			this.msgLabel = "";
		}
	}

	public String saveAndExitRegion()
	{
		try
		{
			Country country = null;
			Region duplicate = null;
			SubscriberUser currentUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
			if (currentUser != null)
			{
				if ((this.noOfregionList != null) && (this.noOfregionList.size() > 0)) {
					for (int i = 0; this.noOfregionList.size() > i; i++) {
						if ((this.noOfregionList.get(i) != null) && (((Region)this.noOfregionList.get(i)).getRegion() != null) && (!((Region)this.noOfregionList.get(i)).getRegion().isEmpty()))
						{
							if ((((Region)this.noOfregionList.get(i)).getRegion() != null) && (((Region)this.noOfregionList.get(i)).getId() > 0)) {
								duplicate = this.userService.getRegionByRegion(((Region)this.noOfregionList.get(i)).getId(), ((Region)this.noOfregionList.get(i)).getRegion());
							} else {
								duplicate = this.userService.getRegionByRegion(((Region)this.noOfregionList.get(i)).getRegion());
							}
							if (duplicate == null)
							{
								if ((((Region)this.noOfregionList.get(i)).getId() == 0) && (((Region)this.noOfregionList.get(i)).getRegion() != null) && (!((Region)this.noOfregionList.get(i)).getRegion().isEmpty()))
								{
									Region newRegion = new Region();
									newRegion.setRegion(((Region)this.noOfregionList.get(i)).getRegion());
									country = this.userService.findCountryById(this.regionCountryId);
									if (country != null) {
										newRegion.setCountry(country);
									}
									newRegion.setCreated_by(currentUser);
									newRegion.setSubscriber(currentUser.getSubscriber());
									newRegion.setIsDeleted(Boolean.valueOf(false));
									this.userService.addRegion(newRegion);
								}
								else
								{
									Region region = this.userService.getRegionById(((Region)this.noOfregionList.get(i)).getId());
									if (region != null)
									{
										region.setRegion(((Region)this.noOfregionList.get(i)).getRegion());
										country = this.userService.findCountryById(this.regionCountryId);
										if (country != null) {
											region.setCountry(country);
										}
										region.setCreated_by(currentUser);
										region.setSubscriber(currentUser.getSubscriber());
										region.setIsDeleted(Boolean.valueOf(false));
										this.userService.updateRegion(region);
									}
								}
							}
							else
							{
								this.messageService.messageError(null, ((Region)this.noOfregionList.get(i)).getRegion() + "  Region already exist.");
								return null;
							}
						}
						else
						{
							this.messageService.messageError(null, "Region should not be empty.");
							return null;
						}
					}
				}
				if ((this.deleteRegionList != null) && (this.deleteRegionList.size() > 0)) {
					for (int i = 0; this.deleteRegionList.size() > i; i++) {
						if (((Region)this.deleteRegionList.get(i)).getId() > 0)
						{
							Region deleteRegion = this.userService.getRegionById(((Region)this.deleteRegionList.get(i)).getId());
							if (deleteRegion != null)
							{
								deleteRegion.setIsDeleted(Boolean.valueOf(true));
								this.userService.updateRegion(deleteRegion);
							}
						}
					}
				}
				this.messageService.messageInformation(null, "Region has been Updated  Successfully.");
				return "returnManageRegion";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String loadAddOrEditRegion()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("countryId");
		long countryId = Long.valueOf(val != null ? val : "0").longValue();
		if (countryId > 0L) {
			this.sessionManager.setSessionAttributeInSession("edit_subscriber_user", Long.valueOf(countryId));
		} else {
			this.sessionManager.removeSessionAttributeInSession("edit_subscriber_user");
		}
		if (countryId > 0L) {
			return "editRegion";
		}
		return "addRegion";
	}

	public String viewRegion()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("countryId");
		long countryId = Long.valueOf(val != null ? val : "0").longValue();
		if (countryId > 0L)
		{
			this.sessionManager.setSessionAttributeInSession("edit_subscriber_user", Long.valueOf(countryId));
			return "viewRegion";
		}
		return null;
	}

	public void loadRegionInfo(ComponentSystemEvent event)
	{
		try
		{
			if (!FacesContext.getCurrentInstance().isPostback())
			{
				Object sessionObj = this.sessionManager.getSessionAttribute("edit_subscriber_user");
				this.regionCountryId = (sessionObj != null ? ((Long)sessionObj).longValue() : 0L);
				if (this.regionCountryId > 0L)
				{
					List<Region> region = this.userService.getRegionByCountryId(this.regionCountryId);
					if ((region != null) && (region.size() > 0))
					{
						Country country = this.userService.findCountryById(this.regionCountryId);
						if (country != null) {
							this.country = country.getCountryName();
						}
						this.noOfregionList.addAll(region);
						this.regionCount = region.size();
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public List<Region> getRegionListBySeachCriteria()
	{
		this.regionList = new ArrayList();
		SubscriberUser subscriberUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
		if (this.searchValue.isEmpty())
		{
			this.regionList.addAll(getUserService().getRegionBySeachCriteria(subscriberUser.getSubscriber().getId()));
			if ((this.regionList != null) && (this.regionList.size() > 0)) {
				for (int i = 0; this.regionList.size() > i; i++)
				{
					long countryId = ((Region)this.regionList.get(i)).getCountry().getCountryId();
					String count = this.userService.getRegionCountBySeachCriteria(countryId);
					if (count != null) {
						((Region)this.regionList.get(i)).setCountOfRegions(Integer.valueOf(count).intValue());
					}
				}
			}
		}
		else
		{
			this.regionList.addAll(getUserService().getRegionBySeachCriteria(subscriberUser.getSubscriber().getId(), this.searchValue));
			if ((this.regionList != null) && (this.regionList.size() > 0)) {
				for (int i = 0; this.regionList.size() > i; i++)
				{
					long countryId = ((Region)this.regionList.get(i)).getCountry().getCountryId();
					String count = this.userService.getRegionCountBySeachCriteria(countryId);
					if (count != null) {
						((Region)this.regionList.get(i)).setCountOfRegions(Integer.valueOf(count).intValue());
					}
				}
			}
		}
		Collections.reverse(this.regionList);
		return this.regionList;
	}

	public String deleteRegion()
	{
		try
		{
			SubscriberUser subscriberUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
			if (subscriberUser == null) {
				FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "subscriber_login.xhtml");
			}
			for (Map.Entry<Long, Boolean> entry : this.checkedRegion.entrySet()) {
				if (((Boolean)entry.getValue()).booleanValue())
				{
					List<Region> regionObj = this.userService.getRegionByCountryId(((Long)entry.getKey()).longValue());
					if ((regionObj != null) && (regionObj.size() > 0)) {
						for (int i = 0; regionObj.size() > i; i++)
						{
							((Region)regionObj.get(i)).setIsDeleted(Boolean.valueOf(true));
							this.userService.updateRegion((Region)regionObj.get(i));
						}
					}
				}
			}
			this.checkedRegion = new HashMap();
			this.messageService.messageInformation(null, "Region has been Deleted Successfully.");
		}
		catch (Exception e)
		{
			System.out.println("userBean:deleteRegion" + e.getMessage());
			e.printStackTrace();
		}
		return "returnManageRegion";
	}

	public String addSubscriberUserByAdmin()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("subscriberId");
		int subscriberId = Integer.valueOf(val != null ? val : "0").intValue();
		if (subscriberId > 0)
		{
			this.sessionManager.setSessionAttributeInSession("edit_user", Integer.valueOf(subscriberId));
			return "addSubscriberUserByAdmin";
		}
		return null;
	}

	public String saveAndExitSubscriberUserByAdmin()
	{
		try
		{
			User currentUser = (User)this.sessionManager.getSessionAttribute("login_user");
			Object sessionObj = this.sessionManager.getSessionAttribute("edit_user");
			this.id = (sessionObj != null ? ((Integer)sessionObj).intValue() : 0);
			if ((currentUser != null) && (this.id > 0) && (this.firstName != null) && (StringUtility.isNotEmpty(this.firstName)) && (this.email != null) && (StringUtility.isNotEmpty(this.email)))
			{
				SubscriberUser subscriberUser = new SubscriberUser();
				String subscriberUserType = "";
				Subscriber subscriber = this.userService.getSubscriberById(this.id);
				SubscriberUser duplicate = this.userService.getSubscriberUserByEmail(this.email);
				if (duplicate == null)
				{
					if ((this.password != null) && (this.password.equals(this.confirmPassword)))
					{
						subscriberUser = getAllsubscriberUserValues(subscriberUser);
						subscriberUserType = "Subscriber User";
						String pwd = this.email;
						String encrptPwd = pwd;
						subscriberUser.setPassword(encrptPwd);
						subscriberUser.setIsActive(Boolean.valueOf(true));
						subscriberUser.setCreated_by(currentUser.getId());
						subscriberUser.setIsDeleted(Boolean.valueOf(false));
						if (subscriber != null) {
							subscriberUser.setSubscriber(subscriber);
						}
						this.userService.addSubscriberUser(subscriberUser);
						this.messageService.messageInformation(null, "Subscriber User has been Added  Successfully.");
						EmailManager.newUser(this.email, this.firstName, this.email, pwd, subscriberUserType, currentUser.getEmail() != null ? currentUser.getEmail() : "");
						return "returnManageSubscriberUserDetailsByAdmin";
					}
					this.messageService.messageError(null, "Password doesn't match.");
					return null;
				}
				this.messageService.messageError(null, "Email already exist.");
				return null;
			}
			this.messageService.messageError(null, "Please enter the First name and Email Address.");
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String loadAddOrEditSubscriberUserByAdmin()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("subscriberId");
		int subscriberId = Integer.valueOf(val != null ? val : "0").intValue();
		Object sessionObj = this.sessionManager.getSessionAttribute("edit_user");
		this.id = (sessionObj != null ? ((Integer)sessionObj).intValue() : 0);
		if ((subscriberId > 0) && (this.id > 0)) {
			this.sessionManager.setSessionAttributeInSession("edit_subscriber_user_byadmin", Integer.valueOf(subscriberId));
		} else {
			this.sessionManager.removeSessionAttributeInSession("edit_subscriber_user_byadmin");
		}
		if ((subscriberId > 0) && (this.id > 0)) {
			return "editSubscriberUserByAdmin";
		}
		return "addSubscriberUserByAdmin";
	}

	public String viewSubscriberUserByAdmin()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("subscriberId");
		int subscriberId = Integer.valueOf(val != null ? val : "0").intValue();
		if (subscriberId > 0)
		{
			this.sessionManager.setSessionAttributeInSession("edit_subscriber_user_byadmin", Integer.valueOf(subscriberId));
			return "viewSubscriberUserDetailsByAdmin";
		}
		return null;
	}

	public String updateAndExitSubscriberUserByAdmin()
	{
		try
		{
			User currentUser = (User)this.sessionManager.getSessionAttribute("login_user");
			FacesUtils facesUtils = new FacesUtils();
			String val = facesUtils.getRequestParameterMap("subscriberUserId");
			int subscriberId = Integer.valueOf(val != null ? val : "0").intValue();
			if ((currentUser != null) && (subscriberId > 0) && (this.firstName != null) && (StringUtility.isNotEmpty(this.firstName)) && (this.email != null) && (StringUtility.isNotEmpty(this.email)))
			{
				SubscriberUser subscriberUser = this.userService.getSubscriberUserById(subscriberId);
				if (subscriberUser != null)
				{
					subscriberUser = getAllsubscriberUserValues(subscriberUser);
					subscriberUser.setIsActive(Boolean.valueOf(true));
					subscriberUser.setIsDeleted(Boolean.valueOf(false));
					subscriberUser.setSubscriber(subscriberUser.getSubscriber());
					this.userService.updateSubscriberUser(subscriberUser);
					this.messageService.messageInformation(null, "Subscriber User has been Updated Successfully.");
					return "returnManageSubscriberUserDetailsByAdmin";
				}
			}
			else
			{
				this.messageService.messageError(null, "Please enter the First name and Email Address.");
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String saveAndExitPublication()
	{
		try
		{
			User currentUser = (User)this.sessionManager.getSessionAttribute("login_user");
			if ((currentUser != null) && (this.publicationTitle != null) && (StringUtility.isNotEmpty(this.publicationTitle)))
			{
				Publication duplicate = this.userService.getPublicationByPublicationTitleandType(this.publicationTitle, this.publicationType);
				if (duplicate == null)
				{
					Publication publication = new Publication();
					publication.setPublicationTitle(this.publicationTitle);
					publication.setPublicationType(this.publicationType);
					publication.setCreated_by(currentUser);
					publication.setIsDeleted(Boolean.valueOf(false));
					this.userService.addPublication(publication);
					this.messageService.messageInformation(null, "Configuration has been Added  Successfully.");
					return "returnManagePublication";
				}
				this.messageService.messageError(null, "Configuration already exist.");
				return null;
			}
			this.messageService.messageError(null, "Please enter the  Configuration title.");
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String updateAndExitPublication()
	{
		try
		{
			User currentUser = (User)this.sessionManager.getSessionAttribute("login_user");
			Object sessionObj = this.sessionManager.getSessionAttribute("edit_user");
			int publicationId = sessionObj != null ? ((Integer)sessionObj).intValue() : 0;
			if ((currentUser != null) && (publicationId > 0) && (this.publicationTitle != null) && (StringUtility.isNotEmpty(this.publicationTitle)))
			{
				Publication duplicate = this.userService.getPublicationByPublicationTitle(this.publicationTitle, publicationId);
				if (duplicate == null)
				{
					Publication publication = this.userService.getPublicationById(publicationId);
					if (publication != null)
					{
						publication.setPublicationTitle(this.publicationTitle);
						publication.setPublicationType(this.publicationType);
						publication.setCreated_by(currentUser);
						publication.setIsDeleted(Boolean.valueOf(false));
						this.userService.updatePublication(publication);
						this.messageService.messageInformation(null, "Configuration has been Updated Successfully.");
						return "returnManagePublication";
					}
				}
				else
				{
					this.messageService.messageError(null, "Configuration already exist.");
					return null;
				}
			}
			else
			{
				this.messageService.messageError(null, "Please enter the  Configuration title.");
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String loadAddOrEditPublication()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("publicationId");
		int publicationId = Integer.valueOf(val != null ? val : "0").intValue();
		if (publicationId > 0) {
			this.sessionManager.setSessionAttributeInSession("edit_user", Integer.valueOf(publicationId));
		} else {
			this.sessionManager.removeSessionAttributeInSession("edit_user");
		}
		if (publicationId > 0) {
			return "editPublication";
		}
		return "addPublication";
	}

	public String viewPublication()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("publicationId");
		int publicationId = Integer.valueOf(val != null ? val : "0").intValue();
		if (publicationId > 0)
		{
			this.sessionManager.setSessionAttributeInSession("edit_user", Integer.valueOf(publicationId));
			return "viewPublication";
		}
		return null;
	}

	public void loadPublicationInfo(ComponentSystemEvent event)
	{
		try
		{
			if (!FacesContext.getCurrentInstance().isPostback())
			{
				Object sessionObj = this.sessionManager.getSessionAttribute("edit_user");
				this.id = (sessionObj != null ? ((Integer)sessionObj).intValue() : 0);
				if (this.id > 0)
				{
					Publication publication = this.userService.getPublicationById(this.id);
					if (publication != null)
					{
						this.publicationTitle = publication.getPublicationTitle();
						this.publicationType = publication.getPublicationType();
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public List<Publication> getPublicationListBySeachCriteria()
	{
		this.publicationList = new ArrayList();
		if ((this.searchValue.isEmpty()) && (this.publicationStatus == 1)) {
			this.publicationList.addAll(getUserService().getPublicationBySeachCriteria());
		} else if ((!this.searchValue.isEmpty()) && (this.publicationStatus == 1)) {
			this.publicationList.addAll(getUserService().getPublicationBySeachCriteria(this.searchValue));
		} else {
			this.publicationList.addAll(getUserService().getPublicationByTypeSeachCriteria(this.publicationStatus, this.searchValue));
		}
		Collections.reverse(this.publicationList);
		return this.publicationList;
	}

	public String deletePublication()
	{
		try
		{
			User user = (User)this.sessionManager.getSessionAttribute("login_user");
			if (user == null) {
				FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "login.xhtml");
			}
			for (Map.Entry<Integer, Boolean> entry : this.checkedUser.entrySet()) {
				if (((Boolean)entry.getValue()).booleanValue())
				{
					Publication publicationObj = this.userService.getPublicationById(((Integer)entry.getKey()).intValue());
					if (publicationObj != null)
					{
						publicationObj.setIsDeleted(Boolean.valueOf(true));
						this.userService.updatePublication(publicationObj);
					}
				}
			}
			this.checkedUser = new HashMap();
			this.messageService.messageInformation(null, "Publication has been Deleted Successfully.");
		}
		catch (Exception e)
		{
			System.out.println("userBean:deletePublication" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public List<SelectItem> getAllPublicationTitle()
	{
		List<Publication> publicationList = new ArrayList();
		List<SelectItem> publicationList1 = new ArrayList();
		try
		{
			publicationList = this.userService.findAllPublication();
			for (int i = 0; i < publicationList.size(); i++)
			{
				Publication publication = (Publication)publicationList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(Integer.valueOf(publication.getId()));
				sItem.setLabel(publication.getPublicationTitle());
				publicationList1.add(sItem);
			}
			return publicationList1;
		}
		catch (Exception e)
		{
			System.out.println("userBean::ViewPublicationList" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public List<SelectItem> getAllSection()
	{
		List<Publication> publicationList = new ArrayList();
		List<SelectItem> sectionList = new ArrayList();
		try
		{
			publicationList = this.userService.findAllSection();
			for (int i = 0; i < publicationList.size(); i++)
			{
				Publication publication = (Publication)publicationList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(Integer.valueOf(publication.getId()));
				sItem.setLabel(publication.getPublicationTitle());
				sectionList.add(sItem);
			}
			return sectionList;
		}
		catch (Exception e)
		{
			System.out.println("userBean::ViewPublicationList" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public String viewSubscriberUserListByAdmin()
	{
		FacesUtils facesUtils = new FacesUtils();
		String val = facesUtils.getRequestParameterMap("subscriberId");
		int subscriberId = Integer.valueOf(val != null ? val : "0").intValue();
		if (subscriberId > 0)
		{
			this.sessionManager.setSessionAttributeInSession("edit_user", Integer.valueOf(subscriberId));
			return "manageSubscriberUserDetailsByAdmin";
		}
		return null;
	}

	public List<SubscriberUser> getSubscriberUserListByAdminSeachCriteria()
	{
		User user = (User)this.sessionManager.getSessionAttribute("login_user");
		Object sessionObj = this.sessionManager.getSessionAttribute("edit_user");
		this.id = (sessionObj != null ? ((Integer)sessionObj).intValue() : 0);
		this.subscriberUserList = new ArrayList();
		if ((user != null) && (this.searchValue.isEmpty())) {
			this.subscriberUserList.addAll(getUserService().getSubscriberUserBySeachCriteria(this.id));
		} else {
			this.subscriberUserList.addAll(getUserService().getSubscriberUserBySeachCriteria(this.id, this.searchValue));
		}
		Collections.reverse(this.subscriberUserList);
		return this.subscriberUserList;
	}

	public String activiateSubscriberUserByAdmin()
	{
		try
		{
			int flag = 0;
			User user = (User)this.sessionManager.getSessionAttribute("login_user");
			if (user == null) {
				FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "subscriber_login.xhtml");
			}
			FacesUtils facesUtils = new FacesUtils();
			int status = Integer.valueOf(facesUtils.getRequestParameterMap("status")).intValue();
			for (Map.Entry<Integer, Boolean> entry : this.checkedUser.entrySet()) {
				if (((Boolean)entry.getValue()).booleanValue())
				{
					SubscriberUser subscriberUserObj = this.userService.getSubscriberUserById(((Integer)entry.getKey()).intValue());
					if (subscriberUserObj != null)
					{
						if ((status == 0) && (subscriberUserObj.getIsActive().booleanValue()))
						{
							subscriberUserObj.setIsActive(Boolean.valueOf(false));
							flag = 1;
						}
						else if ((status == 1) && (!subscriberUserObj.getIsActive().booleanValue()))
						{
							subscriberUserObj.setIsActive(Boolean.valueOf(true));
							flag = 2;
						}
						else if ((status == 0) && (!subscriberUserObj.getIsActive().booleanValue()))
						{
							this.messageService.messageInformation(null, "Subscriber  User has been already Deactivated.");
						}
						else
						{
							this.messageService.messageInformation(null, "Subscriber User has been already Activated.");
						}
						this.userService.updateSubscriberUser(subscriberUserObj);
					}
				}
			}
			if ((status == 0) && (flag == 1)) {
				this.messageService.messageInformation(null, "Subscriber User has been Deactivated Successfully.");
			} else if ((status == 1) && (flag == 2)) {
				this.messageService.messageInformation(null, "Subscriber User has been Activated Successfully.");
			}
		}
		catch (Exception e)
		{
			System.out.println("userBean:activiateSubscriberUser" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public String deleteSubscriberUserByAdmin()
	{
		try
		{
			User user = (User)this.sessionManager.getSessionAttribute("login_user");
			if (user == null) {
				FacesContext.getCurrentInstance().getExternalContext().redirect(CommonProperties.getBaseURL() + CommonProperties.getContextPath() + "subscriber_login.xhtml");
			}
			for (Map.Entry<Integer, Boolean> entry : this.checkedUser.entrySet()) {
				if (((Boolean)entry.getValue()).booleanValue())
				{
					SubscriberUser subscriberUserObj = this.userService.getSubscriberUserById(((Integer)entry.getKey()).intValue());
					if (subscriberUserObj != null)
					{
						subscriberUserObj.setIsDeleted(Boolean.valueOf(true));
						this.userService.updateSubscriberUser(subscriberUserObj);
					}
				}
			}
			this.checkedUser = new HashMap();
			this.messageService.messageInformation(null, "Subscriber User has been Deleted Successfully.");
		}
		catch (Exception e)
		{
			System.out.println("userBean:deleteSubscriberUser" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void localeChangedPublicationValue(ValueChangeEvent event)
	{
		this.publicationStatus = ((Integer)event.getNewValue()).intValue();
	}

	public List<SelectItem> getNoOfRegions()
	{
		List noRegions = new ArrayList();
		try
		{
			int startNo = 1;
			int maxNo = 10;
			for (int i = startNo; i <= maxNo; i++)
			{
				SelectItem sitem = new SelectItem(String.valueOf(i));
				noRegions.add(sitem);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("UserFormbean::getNoOfRegions" + e.getMessage());
		}
		return noRegions;
	}

	public void removeRegionList()
	{
		FacesUtils facesUtils = new FacesUtils();
		String rowindex = facesUtils.getRequestParameterMap("rowIndex");
		if ((rowindex != null) && (StringUtils.isNotEmpty(rowindex)) && (this.noOfregionList != null) && (this.noOfregionList.size() > 0))
		{
			this.deleteRegionList.add((Region)this.noOfregionList.get(Integer.valueOf(rowindex).intValue()));
			this.noOfregionList.remove(this.noOfregionList.get(Integer.valueOf(rowindex).intValue()));
			this.regionCount = this.noOfregionList.size();
		}
	}

	public String resetRegion()
	{
		return "manageRegion";
	}

	public void loadSubscriberUserInfoByAdmin(ComponentSystemEvent event)
	{
		try
		{
			if (!FacesContext.getCurrentInstance().isPostback())
			{
				Object sessionObj = this.sessionManager.getSessionAttribute("edit_subscriber_user_byadmin");
				int subscriberUserId = sessionObj != null ? ((Integer)sessionObj).intValue() : 0;
				if (subscriberUserId > 0)
				{
					SubscriberUser subscriberUser = this.userService.getSubscriberUserById(subscriberUserId);
					setSubscriberUserDetails(subscriberUser);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void hideFields()
	{
		if ((this.userDateils != null) && (this.userDateils.size() > 0)) {
			this.userDateils.removeAll(this.userDateils);
		}
	}

	public String updateAndExitAdminUser()
	{
		try
		{
			this.currentUser = ((User)this.sessionManager.getSessionAttribute("login_user"));
			Object sessionObj = this.sessionManager.getSessionAttribute("edit_user");
			int userId = sessionObj != null ? ((Integer)sessionObj).intValue() : 0;
			if ((this.currentUser != null) && (userId > 0))
			{
				Date today = new Date();
				if ((this.dateofBirth == null) || (this.dateofBirth.before(today)))
				{
					User user = this.userService.getUserById(userId);
					if ((user != null) && (this.firstName != null) && (StringUtility.isNotEmpty(this.firstName)) && (this.email != null) && (StringUtility.isNotEmpty(this.email)))
					{
						user = getAllUserValues(user);
						user.setIsActive(Boolean.valueOf(true));
						user.setIsDeleted(Boolean.valueOf(false));
						this.userService.updateUser(user);
						this.msgLabel = "Profile has been Updated Successfully.";
						return "/pages/admin/userProfile.xhtml?msgLabel=" + this.msgLabel + "&faces-redirect=true";
					}
					this.messageService.messageError(null, "Please enter the First name and Email Address.");
					return null;
				}
				this.messageService.messageError(null, "Please Check your Date of Birth");
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String changePasswordBySubscriber()
	{
		try
		{
			SubscriberUser subscriberUser = (SubscriberUser)this.sessionManager.getSessionAttribute("login_subscriber_user");
			if (subscriberUser != null) {
				if (subscriberUser.getPassword().equals(this.commonUtils.generateEncryptedPwd(this.password)))
				{
					if (this.newPassword.equals(this.confirmPassword))
					{
						subscriberUser.setPassword(this.newPassword);
						this.userService.updateSubscriberUser(subscriberUser);

						this.msgLabel = "Your Password has been Saved Successfully.";
						return "/pages/subscriber/subscriber_profile.xhtml?msgLabel=" + this.msgLabel + "&faces-redirect=true";
					}
					this.messageService.messageError(null, "Your new password mismatch with confirmation password.");
				}
				else
				{
					this.messageService.messageError(null, "Incorrect old password");
					return null;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String addmorePublication()
			throws ParseException
			{
		int indexRow = 0;
		if ((this.publicationId != null) && (StringUtility.isNotEmpty(this.publicationId)) && (this.fromDate != null) && (StringUtility.isNotEmpty(this.fromDate)) && (this.toDate != null) && (StringUtility.isNotEmpty(this.toDate)))
		{
			String[] publicationList = new String[4];
			DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			SimpleDateFormat formatterStr = new SimpleDateFormat("yyyy-MM-dd");
			Date formdateD = formatter.parse(this.fromDate);
			Date todateD = formatter.parse(this.toDate);
			if (todateD.compareTo(formdateD) > 0)
			{
				if ((this.editPublicationStringList != null) && (!this.editPublicationStringList.isEmpty()))
				{
					for (String[] editPublication : editPublicationStringList) {
						publicationList[0] = editPublication[0];
						publicationList[1] = editPublication[1];
						publicationList[2] = formatterStr.format(formdateD);
						publicationList[3] = formatterStr.format(todateD);
					}
					int i = 0;
					for (Iterator iterator = this.publicationStringList.iterator(); iterator.hasNext();)
					{
						String[] type = (String[])iterator.next();
						if (type[1].equals(this.publicationTitle)) {
							indexRow = i;
						}
						i++;
					}
					this.publicationStringList.set(indexRow, publicationList);
					this.editPublicationStringList.clear();
				}
				else
				{
					Publication publication = this.userService.getPublicationById(Integer.valueOf(this.publicationId).intValue());
					publicationList[0] = this.publicationId;
					publicationList[1] = publication.getPublicationTitle();
					publicationList[2] = formatterStr.format(formdateD);
					publicationList[3] = formatterStr.format(todateD);
					boolean flag = removeDummyPublication(this.publicationStringList, publicationList);
					if (flag) {
						this.publicationStringList.add(publicationList);
					}
				}
				this.publicationId = "";
				this.publicationTitle = "";
				this.fromDate = "";
				this.toDate = "";
			}
			else
			{
				this.fromDate = "";
				this.toDate = "";
				this.messageService.messageError(null, "FromDate must be lesser than ToDate");
				return null;
			}
		}
		return null;
			}

	private boolean removeDummyPublication(List<String[]> publicationStringList2, String[] publicationList2)
	{
		if (publicationStringList2.isEmpty()) {
			return true;
		}
		Iterator iterator = publicationStringList2.iterator();
		while (iterator.hasNext())
		{
			String[] strings = (String[])iterator.next();
			if (strings[1].equals(publicationList2[1])) {
				return false;
			}
		}
		return true;
	}

	public void removePublication()
	{
		FacesUtils facesUtils = new FacesUtils();
		String rowindex = facesUtils.getRequestParameterMap("rowIndex");
		if ((rowindex != null) && (StringUtils.isNotEmpty(rowindex)) && (this.publicationStringList != null) && (this.publicationStringList.size() > 0))
		{
			this.deletePublicationStringList.add((String[])this.publicationStringList.get(Integer.valueOf(rowindex).intValue()));
			this.publicationStringList.remove(this.publicationStringList.get(Integer.valueOf(rowindex).intValue()));
		}
	}

	public void editPublication()
			throws ParseException
			{
		FacesUtils facesUtils = new FacesUtils();
		String rowindex = facesUtils.getRequestParameterMap("rowIndex");
		if ((rowindex != null) && (StringUtils.isNotEmpty(rowindex)) && (this.publicationStringList != null) && (this.publicationStringList.size() > 0))
		{
			this.editPublicationStringList.clear();
			SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyyy");
			this.editPublicationStringList.add((String[])this.publicationStringList.get(Integer.valueOf(rowindex).intValue()));
			if ((this.editPublicationStringList != null) && (this.editPublicationStringList.size() > 0)) {
				for (String[] editPublicationString : this.editPublicationStringList)
				{
					Publication publication = this.userService.getPublicationByPublicationTitle(editPublicationString[1]);
					this.publicationId = String.valueOf(publication.getId());
					this.publicationTitle = editPublicationString[1];
					Date from = oldformat.parse(editPublicationString[2]);
					Date to = oldformat.parse(editPublicationString[3]);
					this.fromDate = newformat.format(from);
					this.toDate = newformat.format(to);
				}
			}
		}
			}

	public int getIsApproved()
	{
		return this.isApproved;
	}

	public void setIsApproved(int isApproved)
	{
		this.isApproved = isApproved;
	}

	public String addmoreSubscriberUser()
			throws ParseException
			{
		SubscriberUser duplicateDb = null;
		int rowindexValue = -1;
		String[] subscriberList = new String[15];
		String duplicate = "";
		FacesUtils facesUtils = new FacesUtils();
		String rowindex = facesUtils.getRequestParameterMap("rowIndex");
		String subscriberUser = facesUtils.getRequestParameterMap("subscriberUserId");
		System.out.println("password" + this.password);
		System.out.println("Cpassword" + this.confirmPassword);
		if ((rowindex != null) && (!rowindex.isEmpty())) {
			rowindexValue = Integer.valueOf(rowindex).intValue();
		}
		if ((subscriberUser != null) && (!subscriberUser.isEmpty())) {
			this.subscriberUserId = Integer.valueOf(subscriberUser).intValue();
		}
		if ((this.addSubscriberUserList != null) && (this.addSubscriberUserList.size() > 0) && (rowindexValue == -1)) {
			for (String[] subscriberString : this.addSubscriberUserList) {
				if (subscriberString != null) {
					if (this.email.equals(subscriberString[2])) {
						duplicate = duplicate + subscriberString[2];
					} else {
						duplicate = null;
					}
				}
			}
		} else if ((this.addSubscriberUserList != null) && (this.addSubscriberUserList.size() > 0) && (rowindexValue > -1)) {
			for (int i = 0; this.addSubscriberUserList.size() > i; i++)
			{
				String[] subscriberString = (String[])this.addSubscriberUserList.get(i);
				if (subscriberString != null) {
					if ((this.email.equals(subscriberString[2])) && (i != rowindexValue)) {
						duplicate = duplicate + subscriberString[2];
					} else {
						duplicate = null;
					}
				}
			}
		} else {
			duplicate = null;
		}
		if ((this.addSubscriberUserList != null) && (this.addSubscriberUserList.size() > 0) && (rowindexValue > -1) && (this.subscriberUserId > 0)) {
			for (int i =0; addSubscriberUserList.size()>i;i++) {
				String[]  subscriberString = addSubscriberUserList.get(i);
				if (subscriberString != null) {
					if ((i == rowindexValue & this.email.equals(subscriberString[2]))) {
						duplicateDb = this.userService.getSubscriberUserByEmail(this.email, Integer.valueOf(subscriberString[5]).intValue());
					} else if (((i == rowindexValue ? 1 : 0) & (this.email.equals(subscriberString[2]) ? 0 : 1)) != 0) {
						duplicateDb = this.userService.getSubscriberUserByEmail(this.email);
					}
				}
			}
		} else {
			duplicateDb = this.userService.getSubscriberUserByEmail(this.email);
		}
		System.out.println("2password" + this.password);
		System.out.println("2Cpassword" + this.confirmPassword);
		if ((duplicate == null) && (duplicateDb == null))
		{
			if ((this.password != null) && (StringUtility.isNotEmpty(this.password)) && (StringUtility.isNotEmpty(this.confirmPassword)))
			{
				if (this.password.equals(this.confirmPassword))
				{
					if ((this.firstName != null) && (StringUtility.isNotEmpty(this.firstName)) && (this.email != null) && (StringUtility.isNotEmpty(this.email)) && (rowindexValue == -1))
					{
						subscriberList[0] = this.firstName;
						subscriberList[1] = this.lastName;
						subscriberList[2] = this.email;
						subscriberList[3] = this.password;
						if (this.checkAdmin.booleanValue()) {
							subscriberList[4] = "5";
						} else {
							subscriberList[4] = "6";
						}
						this.addSubscriberUserList.add(subscriberList);
					}
					else if ((this.firstName != null) && (StringUtility.isNotEmpty(this.firstName)) && (this.email != null) && (StringUtility.isNotEmpty(this.email)) && (rowindexValue > -1) && (this.subscriberUserId == 0))
					{
						this.editSubscriberUserList = new ArrayList();
						this.editSubscriberUserList.add((String[])this.addSubscriberUserList.get(Integer.valueOf(rowindex).intValue()));
						if ((this.editSubscriberUserList != null) && (this.editSubscriberUserList.size() > 0)) {
							for (String[] editString:editSubscriberUserList)
							{
								subscriberList[4] = editString[4];
								subscriberList[5] = editString[5];
								subscriberList[6] = editString[6];
								subscriberList[7] = editString[7];
								subscriberList[8] = editString[8];
								subscriberList[9] = editString[9];
								subscriberList[10] = editString[10];
								subscriberList[11] = editString[11];
								subscriberList[12] = editString[12];
								subscriberList[13] = editString[13];
							}
						}
						subscriberList[0] = this.firstName;
						subscriberList[1] = this.lastName;
						subscriberList[2] = this.email;
						subscriberList[3] = this.password;
						this.addSubscriberUserList.set(rowindexValue, subscriberList);
					}
					else if ((this.firstName != null) && (StringUtility.isNotEmpty(this.firstName)) && (this.email != null) && (StringUtility.isNotEmpty(this.email)) && (rowindexValue > -1) && (this.subscriberUserId != 0))
					{
						this.editSubscriberUserList = new ArrayList();
						this.editSubscriberUserList.add((String[])this.addSubscriberUserList.get(Integer.valueOf(rowindex).intValue()));
						if ((this.editSubscriberUserList != null) && (this.editSubscriberUserList.size() > 0)) {
							for (String[] editString :editSubscriberUserList)
							{
								subscriberList[4] = editString[4];
								subscriberList[5] = editString[5];
								subscriberList[6] = editString[6];
								subscriberList[7] = editString[7];
								subscriberList[8] = editString[8];
								subscriberList[9] = editString[9];
								subscriberList[10] = editString[10];
								subscriberList[11] = editString[11];
								subscriberList[12] = editString[12];
								subscriberList[13] = editString[13];
							}
						}
						subscriberList[0] = this.firstName;
						subscriberList[1] = this.lastName;
						subscriberList[2] = this.email;
						subscriberList[3] = this.password;
						this.addSubscriberUserList.set(rowindexValue, subscriberList);
					}
					else
					{
						if(this.firstName==null || this.firstName.isEmpty())
							this.messageService.messageError(null, "Please enter first name.");
						else if(this.email==null || this.email.isEmpty())
							this.messageService.messageError(null, "Please enter email address.");
						return null;
					}
				}
				else
				{
					this.messageService.messageError(null, "Mis Match Password");
					return null;
				}
			}
			else
			{
				this.messageService.messageError(null, "Please enter Password");
				return null;
			}
			this.firstName = "";
			this.email = "";
			this.password = "";
			this.confirmPassword = "";
			this.lastName = "";
			this.checkAdmin = Boolean.valueOf(false);
		}
		else
		{
			this.messageService.messageError(null, "Email already exist.");
			return null;
		}
		return null;
			}

	public void removeSubscriberUser()
	{
		FacesUtils facesUtils = new FacesUtils();
		String rowindex = facesUtils.getRequestParameterMap("rowIndex");
		if ((rowindex != null) && (StringUtils.isNotEmpty(rowindex)) && (this.addSubscriberUserList != null) && (this.addSubscriberUserList.size() > 0))
		{
			this.deleteSubscriberUserList.add((String[])this.addSubscriberUserList.get(Integer.valueOf(rowindex).intValue()));
			this.addSubscriberUserList.remove(this.addSubscriberUserList.get(Integer.valueOf(rowindex).intValue()));
		}
	}

	public void editSubscriberUser()
	{
		FacesUtils facesUtils = new FacesUtils();
		String rowindex = facesUtils.getRequestParameterMap("rowIndex");
		this.rowIndex = Integer.valueOf(rowindex).intValue();
		this.editSubscriberUserList.clear();
		if ((rowindex != null) && (StringUtils.isNotEmpty(rowindex)) && (this.addSubscriberUserList != null) && (this.addSubscriberUserList.size() > 0))
		{
			this.editSubscriberUserList.add((String[])this.addSubscriberUserList.get(Integer.valueOf(rowindex).intValue()));
			if ((this.editSubscriberUserList != null) && (this.editSubscriberUserList.size() > 0)) {
				for (String[] editSubscriberUserString : this.editSubscriberUserList)
				{
					this.firstName = editSubscriberUserString[0];
					this.lastName = editSubscriberUserString[1];
					this.email = editSubscriberUserString[2];
				}
			}
		}
	}

	public void addMoreSubscriberUser()
			throws ParseException
			{
		FacesUtils facesUtils = new FacesUtils();
		String[] subscriberList = new String[15];
		String rowindex = facesUtils.getRequestParameterMap("rowIndex");
		this.rowIndex = Integer.valueOf(rowindex).intValue();
		if ((rowindex != null) && (StringUtils.isNotEmpty(rowindex)) && (this.addSubscriberUserList != null) && (this.addSubscriberUserList.size() > 0))
		{
			this.editMoreSubscriberUserList.add((String[])this.addSubscriberUserList.get(Integer.valueOf(rowindex).intValue()));
			if ((this.editMoreSubscriberUserList != null) && (this.editMoreSubscriberUserList.size() > 0)) {
				for (String[] editString : this.editMoreSubscriberUserList)
				{
					subscriberList[0] = editString[0];
					subscriberList[1] = editString[1];
					subscriberList[2] = editString[2];
					subscriberList[3] = editString[3];
					subscriberList[4] = editString[4];
					subscriberList[5] = editString[5];
					subscriberList[6] = editString[6];
					subscriberList[7] = editString[7];
					subscriberList[8] = editString[8];
					subscriberList[9] = editString[9];
					subscriberList[10] = editString[10];
					subscriberList[11] = editString[11];
					subscriberList[12] = editString[12];
					subscriberList[13] = editString[13];
				}
			}
			SimpleDateFormat formatterStr = new SimpleDateFormat("yyyy-MM-dd");
			this.gender = (subscriberList[6] != null ? Short.valueOf(subscriberList[6]).shortValue() : 0);
			this.dateofBirth = (subscriberList[11] != null ? formatterStr.parse(subscriberList[11]) : new Date());
			this.address = subscriberList[7];
			this.city = subscriberList[8];
			this.state = subscriberList[9];
			this.country = subscriberList[12];
			this.pincode = subscriberList[13];
			this.phoneNo = subscriberList[10];
			this.isPopup = true;
		}
			}

	public void updateMoreSubscriberUser()
	{
		String[] subscriberList = new String[15];
		FacesUtils facesUtils = new FacesUtils();
		String rowindex = facesUtils.getRequestParameterMap("rowIndex");
		this.rowIndex = Integer.valueOf(rowindex).intValue();
		this.isPopup=false;
		if ((rowindex != null) && (StringUtils.isNotEmpty(rowindex)) && (this.addSubscriberUserList != null) && (this.addSubscriberUserList.size() > 0))
		{
			this.editMoreSubscriberUserList.add((String[])this.addSubscriberUserList.get(Integer.valueOf(rowindex).intValue()));
			if ((this.editMoreSubscriberUserList != null) && (this.editMoreSubscriberUserList.size() > 0)) {
				for (String[] editString : this.editMoreSubscriberUserList)
				{
					subscriberList[0] = editString[0];
					subscriberList[1] = editString[1];
					subscriberList[2] = editString[2];
					subscriberList[3] = editString[3];
					subscriberList[4] = editString[4];
					subscriberList[5] = editString[5];
				}
			}
			subscriberList[6] = String.valueOf(this.gender);
			subscriberList[7] = this.address;
			subscriberList[8] = this.city;
			subscriberList[9] = this.state;
			subscriberList[10] = this.phoneNo;
			SimpleDateFormat formatterStr = new SimpleDateFormat("yyyy-MM-dd");
			if (this.dateofBirth != null) {
				subscriberList[11] = formatterStr.format(this.dateofBirth);
			}
			subscriberList[12] = this.country;
			subscriberList[13] = this.pincode;
			this.addSubscriberUserList.set(this.rowIndex, subscriberList);
		}
	}
	public void loadSubscriberProfile(ComponentSystemEvent event)
	{
		try
		{
			if (!FacesContext.getCurrentInstance().isPostback())
			{
				SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
				if(subscriberUser!=null){
					setSubscriberUserDetails(subscriberUser);
				}
				/*Object sessionObj = this.sessionManager.getSessionAttribute("edit_subscriber_user_byadmin");
				int subscriberUserId = sessionObj != null ? ((Integer)sessionObj).intValue() : 0;
				if (subscriberUserId > 0)
				{
					SubscriberUser subscriberUser = this.userService.getSubscriberUserById(subscriberUserId);
					setSubscriberUserDetails(subscriberUser);
				}*/
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}