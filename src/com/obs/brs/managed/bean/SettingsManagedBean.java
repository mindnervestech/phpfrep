/**
 * 
 */
package com.obs.brs.managed.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import com.obs.brs.messages.IMessagesService;
import com.obs.brs.model.Settings;
import com.obs.brs.model.User;
import com.obs.brs.model.UserType;
import com.obs.brs.service.ISettingsService;
import com.obs.brs.service.IUserService;
import com.obs.brs.session.manager.FacesUtils;
import com.obs.brs.session.manager.SessionManager;
import com.obs.brs.utils.CommonUtils;

/**
 * @author Jeevanantham
 *
 */
@ManagedBean(name="settingsBean")
@ViewScoped
public class SettingsManagedBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CommonUtils commonUtils 		= CommonUtils.getInstance();
	SessionManager sessionManager 	= SessionManager.getMyInstatnce();
	
	FacesUtils facesUtils = new FacesUtils();
	
	@ManagedProperty(value="#{SettingsService}")
	ISettingsService settingsService;

	@ManagedProperty(value="#{MessageService}")
	IMessagesService messagesService;
	
	@ManagedProperty(value="#{UserService}")
	IUserService userService;

	
	// Admin CheckBoxes
	private Boolean aUser;
	private Boolean aSubscriber;
	private Boolean aConfiguration;
	private Boolean aJob;
	private Boolean aImageUpload;
	private Boolean aParentImage;
	 
	// QC CheckBoxes
	private Boolean qUser;
	private Boolean qSubscriber;
	private Boolean qConfiguration;
	private Boolean qJob;
	private Boolean qImageUpload;
	private Boolean qParentImage;
	
    // DEO CheckBoxes
	private Boolean dUser;
	private Boolean dSubscriber;
	private Boolean dConfiguration;
	private Boolean dJob;
	private Boolean dImageUpload;
	private Boolean dParentImage;
	
	List<Settings> settingsList;
	
	// Local Constants 
		private static final String MANAGE_USER 			= "user";
		private static final String MANAGE_SUBSCRIBER 		= "subscriber";
		private static final String MANAGE_CONFIGURATIONS 	= "configurations"; 
		private static final String JOB 			        = "job";
		private static final String IMAGEUPLOAD				= "imageUpload";
		private static final String PARENTIMAGE			    = "parentImage";
		private static final String QCJOB 			        = "qcjob";
		
	/**
	 * 
	 * @return the settingsList
	 */
	public List<Settings> getSettingsList() {
		return settingsList;
	}
    /**
     * 
     * @param settingsList
     *         the settingsList to set
     */
	public void setSettingsList(List<Settings> settingsList) {
		this.settingsList = settingsList;
	}

	private UserType userType_id;
	/**
	 * 
	 * @return the userType_id
	 */
	public UserType getUserType_id() {
		return userType_id;
	}
    /**
     * 
     * @param userType_id
     *         the userType_id to set
     */
	public void setUserType_id(UserType userType_id) {
		this.userType_id = userType_id;
	}
    /**
     * 
     * @return the aUser
     */
	public Boolean getaUser() {
		return aUser;
	}
    /**
     * 
     * @param aUser
     *       the aUser to set
     */
	public void setaUser(Boolean aUser) {
		this.aUser = aUser;
	}
    /**
     * 
     * @return the aSubscriber
     */
	public Boolean getaSubscriber() {
		return aSubscriber;
	}
    /**
     * 
     * @param aSubscriber
     *        the aSubscriber to set
     */
	public void setaSubscriber(Boolean aSubscriber) {
		this.aSubscriber = aSubscriber;
	}
    /**
     * 
     * @return the aConfiguration
     */
	public Boolean getaConfiguration() {
		return aConfiguration;
	}
    /**
     * 
     * @param aConfiguration
     *          the aConfiguration to set
     */
	public void setaConfiguration(Boolean aConfiguration) {
		this.aConfiguration = aConfiguration;
	} 
    /**
     * 
     * @return the aJob
     */
	public Boolean getaJob() {
		return aJob;
	}
    /**
     * 
     * @param aJob
     *      the aJob to set
     */
	public void setaJob(Boolean aJob) {
		this.aJob = aJob;
	}
    /**
     * 
     * @return the aImageUpload
     */
	public Boolean getaImageUpload() {
		return aImageUpload;
	}
    /**
     * 
     * @param aImageUpload
     *        the aImageUpload to set
     */
	public void setaImageUpload(Boolean aImageUpload) {
		this.aImageUpload = aImageUpload;
	}
    /**
     * 
     * @return the aParentImage
     */
	public Boolean getaParentImage() {
		return aParentImage;
	}
    /**
     * 
     * @param aParentImage
     *        the aParentImage to set
     */
	public void setaParentImage(Boolean aParentImage) {
		this.aParentImage = aParentImage;
	}
     /**
      * 
      * @return the qUser
      */
	public Boolean getqUser() {
		return qUser;
	}
    /**
     * 
     * @param qUser
     *       the qUser to set
     */
	public void setqUser(Boolean qUser) {
		this.qUser = qUser;
	}
    /**
     * 
     * @return the qSubscriber
     */
	public Boolean getqSubscriber() {
		return qSubscriber;
	}
    /**
     * 
     * @param qSubscriber
     *       the qSubscriber to set
     */
	public void setqSubscriber(Boolean qSubscriber) {
		this.qSubscriber = qSubscriber;
	}
    /**
     * 
     * @return the qConfiguration
     */
	public Boolean getqConfiguration() {
		return qConfiguration;
	}
    /**
     * 
     * @param qConfiguration
     *        the qConfiguration to set
     */
	public void setqConfiguration(Boolean qConfiguration) {
		this.qConfiguration = qConfiguration;
	}
    /**
     * 
     * @return the qJob
     */
	public Boolean getqJob() {
		return qJob;
	}
    /**
     * 
     * @param qJob
     *        the qJob to set
     */
	public void setqJob(Boolean qJob) {
		this.qJob = qJob;
	}
    /**
     * 
     * @return the qImageUpload
     */
	public Boolean getqImageUpload() {
		return qImageUpload;
	}
    /**
     * 
     * @param qImageUpload
     *         the qImageUpload to set
     */
	public void setqImageUpload(Boolean qImageUpload) {
		this.qImageUpload = qImageUpload;
	}
    /**
     * 
     * @return the qParentImage
     */
	public Boolean getqParentImage() {
		return qParentImage;
	}
    /**
     * 
     * @param qParentImage
     *        the qParentImage to set
     */
	public void setqParentImage(Boolean qParentImage) {
		this.qParentImage = qParentImage;
	}
    /**
     * 
     * @return the dUser
     */
	public Boolean getdUser() {
		return dUser;
	}
  /**
   * 
   * @param dUser
   *       the dUser to set
   */
	public void setdUser(Boolean dUser) {
		this.dUser = dUser;
	}
    /**
     * 
     * @return the dSubscriber
     */
	public Boolean getdSubscriber() {
		return dSubscriber;
	}
    /**
     * 
     * @param dSubscriber
     *       the dSubscriber to set
     */
	public void setdSubscriber(Boolean dSubscriber) {
		this.dSubscriber = dSubscriber;
	}
    /**
     * 
     * @return the dConfiguration
     */
	public Boolean getdConfiguration() {
		return dConfiguration;
	}
    /**
     * 
     * @param dConfiguration
     *       the dConfiguration to set
     */
	public void setdConfiguration(Boolean dConfiguration) {
		this.dConfiguration = dConfiguration;
	}
    /**
     *
     * @return the dJob
     */
	public Boolean getdJob() {
		return dJob;
	} 
    /**
     * 
     * @param dJob
     *      the dJob to set
     */
	public void setdJob(Boolean dJob) {
		this.dJob = dJob;
	}
    /**
     * 
     * @return the dImageUpload
     */
	public Boolean getdImageUpload() {
		return dImageUpload;
	}
    /**
     * 
     * @param dImageUpload
     *        the dImageUpload to set
     */
	public void setdImageUpload(Boolean dImageUpload) {
		this.dImageUpload = dImageUpload;
	}
    /**
     * 
     * @return the dParentImage
     */
	public Boolean getdParentImage() {
		return dParentImage;
	}
    /**
     * 
     * @param dParentImage
     *        the dParentImage to set
     */
	public void setdParentImage(Boolean dParentImage) {
		this.dParentImage = dParentImage;
	}
    /**
     * 
     * @return the userService
     */
	public IUserService getUserService() {
		return userService;
	}
    /**
     * 
     * @param userService
     *        the userService to set
     */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	/**
	 * @return the settingsService
	 */
	public ISettingsService getSettingsService() {
		return settingsService;
	}


	/**
	 * @param settingsService the settingsService to set
	 */
	public void setSettingsService(ISettingsService settingsService) {
		this.settingsService = settingsService;
	}


	/**
	 * @return the messagesService
	 */
	public IMessagesService getMessagesService() {
		return messagesService;
	}

	/**
	 * @param messagesService the messagesService to set
	 */
	public void setMessagesService(IMessagesService messagesService) {
		this.messagesService = messagesService;
	}

	/**
	 * @return the commonUtils
	 */
	public CommonUtils getCommonUtils() {
		return commonUtils;
	}


	/**
	 * @param commonUtils the commonUtils to set
	 */
	public void setCommonUtils(CommonUtils commonUtils) {
		this.commonUtils = commonUtils;
	}
	
	/**
	 * set the DEO role
	 * @param setting
	 * @return
	 */
	private Settings getAllDEORole(Settings setting) {
		setting.setUser(this.dUser);
		setting.setSubscriber(this.dSubscriber);
		setting.setConfigurations(this.dConfiguration);
		setting.setJob(this.dJob);
		setting.setImageUpload(this.dImageUpload);
		setting.setParentImage(this.dParentImage);
		return setting;
	}

	/**
	 * set the QC role
	 * @param setting
	 * @return
	 */
	private Settings getAllQCRole(Settings setting) {
		setting.setUser(this.qUser);
		setting.setSubscriber(this.qSubscriber);
		setting.setConfigurations(this.qConfiguration);
		setting.setJob(this.qJob);
		setting.setImageUpload(this.qImageUpload);
		setting.setParentImage(this.qParentImage);
		return setting;
	}

	/**
	 * set the Admin role
	 * @param setting
	 * @return
	 */
	private Settings getAllAdminRole(Settings setting) {
		setting.setUser(this.aUser);
		setting.setSubscriber(this.aSubscriber);
		setting.setConfigurations(this.aConfiguration);
		setting.setJob(this.aJob);
		setting.setImageUpload(this.aImageUpload);
		setting.setParentImage(this.aParentImage);
		return setting;
	}
	
	/**
	 * save or update user Role
	 * @param isVariant
	 * @return
	 */
	public String saveAndExitUserRole(){
		Settings  settingAdmin = settingsService.getSettingsById(841);
		UserType  userType = userService.getUserTypeById(4);
		settingAdmin.setUserType_id(userType);
		settingAdmin = getAllAdminRole(settingAdmin);
		settingAdmin.setUserType_id(userType);
		settingsService.updateSettings(settingAdmin);
		
		Settings  settingQC = settingsService.getSettingsById(842);
		settingQC= getAllQCRole(settingQC);
		userType = userService.getUserTypeById(2);
		settingQC.setUserType_id(userType);
		settingsService.updateSettings(settingQC);
		
		Settings  settingDEO = settingsService.getSettingsById(843);
		settingDEO= getAllDEORole(settingDEO);
		userType = userService.getUserTypeById(3);
		settingDEO.setUserType_id(userType);
		settingsService.updateSettings(settingDEO);
		messagesService.messageInformation(null, "Role has been Saved Successfully.");
		return null;
		
	}

	/**
	 * set the DEO role
	 * @param setting
	 * @return
	 */
	private void setAllDEORole(Settings setting) {
		this.dUser = setting.getUser();
		this.dSubscriber =setting.getSubscriber();
		this.dConfiguration = setting.getConfigurations();
		this.dJob = setting.getJob();
		this.dImageUpload = setting.getImageUpload();
		this.dParentImage = setting.getParentImage();
	}

	/**
	 * set the QC role
	 * @param setting
	 * @return
	 */
	private void setAllQCRole(Settings setting) {
		this.qUser = setting.getUser();
		this.qSubscriber =setting.getSubscriber();
		this.qConfiguration = setting.getConfigurations();
		this.qJob = setting.getJob();
		this.qImageUpload = setting.getImageUpload();
		this.qParentImage = setting.getParentImage();
	}

	/**
	 * set the Admin role
	 * @param setting
	 * @return
	 */
	private void setAllAdminRole(Settings setting) {
		this.aUser = setting.getUser();
		this.aSubscriber =setting.getSubscriber();
		this.aConfiguration = setting.getConfigurations();
		this.aJob = setting.getJob();
		this.aImageUpload = setting.getImageUpload();
		this.aParentImage = setting.getParentImage();
	}
	
	/**
	 * Page load event for selected company user in admin page
	 * @param event
	 */
	public void loadUserRoleInfo(ComponentSystemEvent event)
	{
		try {
			if (!FacesContext.getCurrentInstance().isPostback()) 
			{
				settingsList = settingsService.getSettings();
				if(settingsList.size() >0 && settingsList != null)
				{
					for(int i =0;settingsList.size()>i;i++){
						Settings settingsObj = settingsList.get(i);
						if(settingsObj.getUserType_id().getId() == 4){
							setAllAdminRole(settingsObj);
						}else if(settingsObj.getUserType_id().getId() == 2){
							setAllQCRole(settingsObj);
						}else if(settingsObj.getUserType_id().getId() == 3){
							setAllDEORole(settingsObj);
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
	 * 
	 * @param menuId
	 * @param accessLink
	 * @return
	 */
	public Boolean checkVisible(int menuId){
		try {
			User user = (User) sessionManager.getSessionAttribute(SessionManager.LOGINUSER);
			String menuValue = menuValueFunction(menuId);
			if(user.getUserType().getId() != 1){
			 return settingsService.roleHasPermission(menuValue,user.getUserType().getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * redirect to the menu link
	 * set activeMenu id in session.
	 * @return
	 */
	public String menuValueFunction(int menuId ){
		String redirectLink = null;
	 switch (menuId) {
		case 2:
			redirectLink = MANAGE_USER;
			break;
		case 3:
			redirectLink = MANAGE_SUBSCRIBER;
			break;
		case 7:
			redirectLink = MANAGE_CONFIGURATIONS;
			break;
		case 4:
			redirectLink = JOB;
			break;
		case 5:
			redirectLink = IMAGEUPLOAD;
			break;
		case 6:
			redirectLink = PARENTIMAGE;
			break;
		case 9:
			redirectLink = QCJOB;
			break;
		default:
			break;
		}
		return redirectLink;
	}
	
	
	
}
