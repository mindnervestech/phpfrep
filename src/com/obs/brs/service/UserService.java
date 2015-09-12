package com.obs.brs.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.obs.brs.dao.IUserDAO;
import com.obs.brs.model.Country;
import com.obs.brs.model.DeCompany;
import com.obs.brs.model.Publication;
import com.obs.brs.model.Region;
import com.obs.brs.model.States;
import com.obs.brs.model.Subscriber;
import com.obs.brs.model.SubscriberPublication;
import com.obs.brs.model.SubscriberUser;
import com.obs.brs.model.Territory;
import com.obs.brs.model.User;
import com.obs.brs.model.UserType;

/**
 * 
 * User Service
 * 
 * @author Jeevanantham
 *
 */
@Transactional(readOnly = true)
public class UserService implements IUserService {

	// UserDAO is injected...
	IUserDAO userDAO;

	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#addUser(com.obs.brs.model.User)
	 */
	@Transactional(readOnly = false)
	@Override
	public Integer addUser(User user) {
		return getUserDAO().addUser(user);
	}

	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#deleteUser(com.obs.brs.model.User)
	 */
	@Transactional(readOnly = false)
	@Override
	public void deleteUser(User user) {
		getUserDAO().deleteUser(user);
	}

	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#updateUser(com.obs.brs.model.User)
	 */
	@Transactional(readOnly = false)
	@Override
	public void updateUser(User user) {
		getUserDAO().updateUser(user);
	}

	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getUserById(int)
	 */
	@Override
	public User getUserById(int id) {
		return getUserDAO().getUserById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getUsers()
	 */
	@Override
	public List<User> getUsers() {	
		return getUserDAO().getUsers();
	}

	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#loginUser(java.lang.String, java.lang.String)
	 */
	@Override
	public User loginUser(String email, String password) {
		return getUserDAO().loginUser(email, password);
	}
    /*
     * get userDAO
     */
	public IUserDAO getUserDAO() {
		return userDAO;
	}

	/*
	 * set userDAO
	 */
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getUserTypes()
	 */
	@Override
	public List<UserType> getUserTypes() {
		return getUserDAO().getUserTypes();
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getUserByEmail(java.lang.String)
	 */
	@Override
	public User getUserByEmail(String email) {
		return getUserDAO().getUserByEmail(email);
	}
	/* (non-Javadoc)
	 * @see com.obs.fms.service.IUserService#getUserByForgotPwdEncryption(java.lang.String)
	 */
	@Override
	public User getUserByForgotPwdEncryption(String encryptionVal) {
		return getUserDAO().getUserByForgotPwdEncryption(encryptionVal);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getUserBySeachCriteria(int)
     */
	@Override
	public List<User> getUserBySeachCriteria(int userId) {
		return getUserDAO().getUserBySeachCriteria(userId);
	}
  /*
  * (non-Javadoc)
  * @see com.obs.brs.service.IUserService#getUserBySeachCriteria(int, int, java.lang.String)
  */
	@Override
	public List<User> getUserBySeachCriteria(int userId,int userStatus,String searchValue) {
		return getUserDAO().getUserBySeachCriteria(userId,userStatus,searchValue);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getUserByUserName(java.lang.String)
     */
	@Override
	public User getUserByUserName(String userName) {
		return getUserDAO().getUserByUserName(userName);
	}
   /*
    * (non-Javadoc)
    * @see com.obs.brs.service.IUserService#getUserTypeById(long)
    */
	@Override
	public UserType getUserTypeById(long userTypeId) {
		return getUserDAO().getUserTypeById(userTypeId);
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getSubscriberByEmail(java.lang.String)
	 */
	@Override
	public SubscriberUser getSubscriberByEmail(String email) {
		return getUserDAO().getSubscriberByEmail(email);
	}

	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#addSubscriber(com.obs.brs.model.Subscriber)
	 */
	@Transactional(readOnly = false)
	@Override
	public Integer addSubscriber(Subscriber subscriber) {
		return getUserDAO().addSubscriber(subscriber);

	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#addSubscriberUser(com.obs.brs.model.SubscriberUser)
	 */
	@Transactional(readOnly = false)
	@Override
	public Integer addSubscriberUser(SubscriberUser subscriberUser) {
		return getUserDAO().addSubscriberUser(subscriberUser);

	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getSubscriberById(int)
	 */
	@Override
	public Subscriber getSubscriberById(int subscriberId) {
		return getUserDAO().getSubscriberById(subscriberId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getSubscriberUserById(int)
     */
	@Override
	public SubscriberUser getSubscriberUserById(int subscriberUserId) {
		return getUserDAO().getSubscriberUserById(subscriberUserId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#updateSubscriber(com.obs.brs.model.Subscriber)
     */
	@Transactional(readOnly = false)
	@Override
	public void updateSubscriber(Subscriber subscriber) {
		getUserDAO().updateSubscriber(subscriber);

	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#updateSubscriberUser(com.obs.brs.model.SubscriberUser)
	 */
	@Transactional(readOnly = false)
	@Override
	public void updateSubscriberUser(SubscriberUser subscriberUser) {
		getUserDAO().updateSubscriberUser(subscriberUser);

	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getSubscriber()
	 */
	@Override
	public List<Subscriber> getSubscriber() {
		return getUserDAO().getSubscriber();
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getSubscriberBySeachCriteria()
	 */
	@Override
	public List<Subscriber> getSubscriberBySeachCriteria() {
		return getUserDAO().getSubscriberBySeachCriteria();
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getSubscriberBySeachCriteria(java.lang.String)
	 */
	@Override
	public List<Subscriber> getSubscriberBySeachCriteria(String searchValue) {
		return getUserDAO().getSubscriberBySeachCriteria(searchValue);
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getSubscriberUserBySeachCriteria(int)
	 */
	@Override
	public List<SubscriberUser> getSubscriberUserBySeachCriteria(int subscriberId) {
		return getUserDAO().getSubscriberUserBySeachCriteria(subscriberId);
	}
/*
 * (non-Javadoc)
 * @see com.obs.brs.service.IUserService#getSubscriberUserBySeachCriteria(int, java.lang.String)
 */
	@Override
	public List<SubscriberUser> getSubscriberUserBySeachCriteria(int subscriberId, String searchValue) {
		return getUserDAO().getSubscriberUserBySeachCriteria(subscriberId, searchValue);
	}

	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getSubscriberArcheivedUserBySeachCriteria(java.lang.String)
	 */
	@Override
	public List<Subscriber> getSubscriberArcheivedUserBySeachCriteria(
			String searchValue) {
		return getUserDAO().getSubscriberArcheivedUserBySeachCriteria(searchValue);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getSubscriberUser()
     */
	@Override
	public List<SubscriberUser> getSubscriberUser() {
		return getUserDAO().getSubscriberUser();
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getSubscriberUserBySubscriberId(int)
     */
	@Override
	public SubscriberUser getSubscriberUserBySubscriberId(int subscriberId) {
		return getUserDAO().getSubscriberUserBySubscriberId(subscriberId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getUserBySeachCriteriaStatus(int, java.lang.String)
     */
	@Override
	public List<User> getUserBySeachCriteriaStatus(int userId, String searchValue) {
		return getUserDAO().getUserBySeachCriteriaStatus(userId,searchValue);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#loginSubscriberUser(java.lang.String, java.lang.String)
     */
	@Override
	public SubscriberUser loginSubscriberUser(String email,String password) {
		return getUserDAO().loginSubscriberUser(email, password);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getSubscriberUserByEmail(java.lang.String)
     */
	@Override
	public SubscriberUser getSubscriberUserByEmail(String email) {
		return getUserDAO().getSubscriberUserByEmail(email);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getSubscriberUserByForgotPwdEncryption(java.lang.String)
     */
	@Override
	public SubscriberUser getSubscriberUserByForgotPwdEncryption(
			String encryptionVal) {
		return getUserDAO().getSubscriberUserByForgotPwdEncryption(encryptionVal);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#findCountryById(long)
     */
	@Override
	public Country findCountryById(long countryId) {
		return getUserDAO().findCountryById(countryId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#findAll()
     */
	@Override
	public List<Country> findAll() {
		return getUserDAO().findAll();
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#addRegion(com.obs.brs.model.Region)
	 */
	@Transactional(readOnly = false)
	@Override
	public Integer addRegion(Region region) {
		return getUserDAO().addRegion(region);
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#updateRegion(com.obs.brs.model.Region)
	 */
	@Transactional(readOnly = false)
	@Override
	public void updateRegion(Region region) {
		getUserDAO().updateRegion(region);

	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#deleteRegion(com.obs.brs.model.Region)
	 */
	@Transactional(readOnly = false)
	@Override
	public void deleteRegion(Region region) {
		getUserDAO().deleteRegion(region);

	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getRegionById(int)
     */
	@Override
	public Region getRegionById(int id) {
		return getUserDAO().getRegionById(id);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getRegions()
     */
	@Override
	public List<Region> getRegions() {
		return getUserDAO().getRegions();
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getRegionBySeachCriteria(int)
     */
	@Override
	public List<Region> getRegionBySeachCriteria(int subscriberId) {
		return getUserDAO().getRegionBySeachCriteria(subscriberId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getRegionBySeachCriteria(int, java.lang.String)
     */
	@Override
	public List<Region> getRegionBySeachCriteria(int subscriberId,String searchValue) {
		return getUserDAO().getRegionBySeachCriteria(subscriberId,searchValue);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getRegionByRegion(java.lang.String)
     */
	@Override
	public Region getRegionByRegion(String region) {
		return getUserDAO().getRegionByRegion(region);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getSubscriberUserListBySubscriberId(int, java.lang.String)
     */
	@Override
	public List<SubscriberUser> getSubscriberUserListBySubscriberId(int id,String searchValue) {
		return getUserDAO().getSubscriberUserListBySubscriberId(id,searchValue);
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#addPublication(com.obs.brs.model.Publication)
	 */
	@Transactional(readOnly = false)
	@Override
	public Integer addPublication(Publication publication) {
		return getUserDAO().addPublication(publication);
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#updatePublication(com.obs.brs.model.Publication)
	 */
	@Transactional(readOnly = false)
	@Override
	public void updatePublication(Publication publication) {
		getUserDAO().updatePublication(publication);

	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#deletePublication(com.obs.brs.model.Publication)
	 */
	@Transactional(readOnly = false)
	@Override
	public void deletePublication(Publication publication) {
		getUserDAO().deletePublication(publication);

	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getPublicationById(int)
     */
	@Override
	public Publication getPublicationById(int id) {
		return getUserDAO().getPublicationById(id);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getPublications()
     */
	@Override
	public List<Publication> getPublications() {
		return getUserDAO().getPublications();
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getPublicationBySeachCriteria()
     */
	@Override
	public List<Publication> getPublicationBySeachCriteria() {
		return getUserDAO().getPublicationBySeachCriteria();
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getPublicationBySeachCriteria(java.lang.String)
     */
	@Override
	public List<Publication> getPublicationBySeachCriteria(String searchValue) {
		return getUserDAO().getPublicationBySeachCriteria(searchValue);
	}
     /*
      * (non-Javadoc)
      * @see com.obs.brs.service.IUserService#getPublicationByPublicationTitleandType(java.lang.String, int)
      */
	@Override
	public Publication getPublicationByPublicationTitleandType(String publicationTitle, int publicationType) {
		return getUserDAO().getPublicationByPublicationTitleandType(publicationTitle, publicationType);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#findAllPublication()
     */
	@Override
	public List<Publication> findAllPublication() {
		return getUserDAO().findAllPublication();
	} 
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#findAllSection()
	 */

	@Override
	public List<Publication> findAllSection() {
		return getUserDAO().findAllSection();
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getRegionByRegion(int, java.lang.String)
     */
	@Override
	public Region getRegionByRegion(int regionId, String region) {
		return getUserDAO().getRegionByRegion(regionId,region);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getPublicationByPublicationTitle(java.lang.String, int)
     */
	@Override
	public Publication getPublicationByPublicationTitle(String publicationTitle,
			int publicationId) {
		return getUserDAO().getPublicationByPublicationTitle(publicationTitle,publicationId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getPublicationByTypeSeachCriteria(int, java.lang.String)
     */
	@Override
	public List<Publication> getPublicationByTypeSeachCriteria(
			int configurationType,String searchValue) {
		return getUserDAO().getPublicationByTypeSeachCriteria(configurationType,searchValue);
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getRegionByCountryId(long)
	 */
	public List<Region> getRegionByCountryId(long countryId){
		return getUserDAO().getRegionByCountryId(countryId);

	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getRegionCountBySeachCriteria(long)
	 */
	public  String getRegionCountBySeachCriteria(long countryId){
		return getUserDAO().getRegionCountBySeachCriteria(countryId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getUserListById(int)
     */
	@Override
	public List<User> getUserListById(int id) {
		return getUserDAO().getUserListById(id);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getUserByEmail(int, java.lang.String)
     */
	@Override
	public User getUserByEmail(int userId, String email) {
		return getUserDAO().getUserByEmail(userId,email);
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#addSubscriberPublication(com.obs.brs.model.SubscriberPublication)
	 */
	@Transactional(readOnly = false)
	@Override
	public Integer addSubscriberPublication(
			SubscriberPublication subscriberPublication) {
		return getUserDAO().addSubscriberPublication(subscriberPublication);

	}
	
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#findPublicationBySubscriber(int)
     */
	@Override
	public List<SubscriberPublication> findPublicationBySubscriber(int id) {
		return getUserDAO().findPublicationBySubscriber(id);
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#updateSubscriberPublication(com.obs.brs.model.SubscriberPublication)
	 */
	@Transactional(readOnly = false)
	@Override
	public void updateSubscriberPublication(
			SubscriberPublication subscriberPublication) {
		getUserDAO().updateSubscriberPublication(subscriberPublication);

	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#findBySubscriberPublicationId(int)
     */
	@Override
	public SubscriberPublication findBySubscriberPublicationId(int id) {
		return getUserDAO().findBySubscriberPublicationId(id);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getCalendarViewReportList(int, int, int, int, int, int, int, int, int)
     */
	@Override
	public List getCalendarViewReportList(int id,int reportCurrentMonthFirst, int reportCurrentYearFirst, int reportCurrentMonthSecond, int reportCurrentYearSecond,int reportCurrentMonthThird, int reportCurrentYearThird,int reportCurrentMonthFour, int reportCurrentYearFour) {
		return getUserDAO().getCalendarViewReportList(id,reportCurrentMonthFirst, reportCurrentYearFirst, reportCurrentMonthSecond, reportCurrentYearSecond,reportCurrentMonthThird, reportCurrentYearThird, reportCurrentMonthFour, reportCurrentYearFour);
	} 
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getCalendarViewReportDetails(int, int, int, long)
	 */
	@Override
	public List getCalendarViewReportDetails(int id,int reportMonth, int reportYear, long deCompanyId) {
		return getUserDAO().getCalendarViewReportDetails(id,reportMonth, reportYear,deCompanyId);
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getCountry()
	 */
	public List<Country> getCountry(){
		return getUserDAO().getCountry();
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getStates()
	 */
	public List<States> getStates(){
		return getUserDAO().getStates();
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getSubscriberUserListBySubscriberId(int)
	 */
	@Override
	public List<SubscriberUser> getSubscriberUserListBySubscriberId(int id) {
		return getUserDAO().getSubscriberUserListBySubscriberId(id);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getSubscriberUserByEmail(java.lang.String, int)
     */
	@Override
	public SubscriberUser getSubscriberUserByEmail(String email,
			int subscriberUserId) {
		return getUserDAO().getSubscriberUserByEmail(email,subscriberUserId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#findAllAdvertiserType()
     */
	@Override
	public List<Publication> findAllAdvertiserType() {
		return getUserDAO().findAllAdvertiserType();
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#findAllInstitutionType()
     */
	@Override
	public List<Publication> findAllInstitutionType() {
		return getUserDAO().findAllInstitutionType();
	}
	
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#findAllSearchAdvertiserType()
     */
	@Override
	public List<Publication> findAllSearchAdvertiserType() {
		return getUserDAO().findAllSearchAdvertiserType();
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getCompanyNameListBySubscriber(int)
	 */
	@Override
	public List getCompanyNameListBySubscriber(int id) {
		return getUserDAO().getCompanyNameListBySubscriber(id);
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getPublicationListBySubscriber(int)
	 */
	@Override
	public List getPublicationListBySubscriber(int id){
		return getUserDAO().getPublicationListBySubscriber(id);
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#searchViewReportList(int, java.lang.String, java.lang.String, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List)
	 */

	@Override
	public List searchViewReportList(int id, String fromDateValue,String toDateValue,
			List<String> companyName, List<String> publicationSection,
			List<String> publicationPage, List<String> adSize,
			List<String> companyType, List<String> adType, List<String> state,
			List<String> country, List<String> publicationTitle) {
		return getUserDAO().searchViewReportList(id,fromDateValue,toDateValue,
				companyName,publicationSection,
				publicationPage,  adSize,
				companyType, adType,  state,
				country,  publicationTitle);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#searchViewReportDetails(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
	@Override
	public List searchViewReportDetails(int id, String fromDateValue,String toDateValue,
			String searchViewCompanyName, String searchViewPublicationSection,
			String searchViewPublicationPage, String searchViewAdSize,
			String searchViewAdOrientation, /*String searchViewCompanyType,*/ String searchViewAdType,
			String searchViewState, String searchViewCountry,
			String searchViewPublicationTitle,
			String searchViewJobDensity) {
		return getUserDAO().searchViewReportDetails(id,fromDateValue,toDateValue,
				searchViewCompanyName, searchViewPublicationSection,
				searchViewPublicationPage, searchViewAdSize,
				searchViewAdOrientation , /*searchViewCompanyType,*/ searchViewAdType,
				searchViewState, searchViewCountry,
				searchViewPublicationTitle, searchViewJobDensity);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getAdvertiserViewReportList(int, int, int, int, int, int, int, int, int)
     */
	@Override
	public List getAdvertiserViewReportList(int id,
			int reportCurrentMonthFirst, int reportCurrentYearFirst,
			int reportCurrentMonthSecond, int reportCurrentYearSecond,
			int reportCurrentMonthThird, int reportCurrentYearThird,
			int reportCurrentMonthFour, int reportCurrentYearFour) {
		return getUserDAO().getAdvertiserViewReportList(id,reportCurrentMonthFirst, reportCurrentYearFirst, reportCurrentMonthSecond, reportCurrentYearSecond,reportCurrentMonthThird, reportCurrentYearThird, reportCurrentMonthFour, reportCurrentYearFour);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getJournalViewReportList(int)
     */
	@Override
	public List getJournalViewReportList(int id) {
		return getUserDAO().getJournalViewReportList(id);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getAdvertiserViewReportDetails(int, int, int, long)
     */
	@Override
	public List getAdvertiserViewReportDetails(int id, int reportMonth,
			int reportYear, long deCompanyId) {
		return getUserDAO().getAdvertiserViewReportDetails(id,reportMonth, reportYear,deCompanyId);
	}
     /*
      * (non-Javadoc)
      * @see com.obs.brs.service.IUserService#getSubscriberJournalList(int)
      */
	@Override
	public List getSubscriberJournalList(int id) {
		return getUserDAO().getSubscriberJournalList(id);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getJournalViewReportDetails(int, long, long)
     */
	@Override
	public List getJournalViewReportDetails(int id, long deCompanyId,
			long deJournalId) {
		return getUserDAO().getJournalViewReportDetails(id,deCompanyId,deJournalId);
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IUserService#getPublicationBySearchValueAdvertisertype(java.lang.String)
	 */
	@Override
	public Publication getPublicationBySearchValueAdvertisertype(String publicationId) {
		return getUserDAO().getPublicationBySearchValueAdvertisertype(publicationId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IUserService#getPublicationByPublicationTitle(java.lang.String)
     */
	@Override
	public Publication getPublicationByPublicationTitle(String publicationTitle) {
		return null;
	}

	@Override
	public List<DeCompany> getAllCompanyData() {
		return getUserDAO().getAllCompanyData();
	}

	@Override
	public List getCountriesForTerritoryBySubscriberId(long subscriberId) {
		return getUserDAO().getCountriesForTerritoryBySubscriberId(subscriberId);
	}

	@Override
	@Transactional(readOnly = false)
	public long saveSubscriberTerritory(String countryIds, long subscriberId,
			String name) {
		return getUserDAO().saveSubscriberTerritory(countryIds, subscriberId, name);
	}

	@Override
	public List<Territory> getSubscriberTerritory(long subscriberId) {
		return getUserDAO().getSubscriberTerritory(subscriberId);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteSubscriberTerritory(long territoryId) {
		getUserDAO().deleteSubscriberTerritory(territoryId);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteSubscriberTerritoryCountry(long territoryId,
			long countryId) {
		getUserDAO().deleteSubscriberTerritoryCountry(territoryId, countryId);
	}

	@Override
	@Transactional(readOnly = false)
	public void addSubscriberTerritoryCountry(long territoryId,
			List<String> countryIds) {
		getUserDAO().addSubscriberTerritoryCountry(territoryId, countryIds);
	}
}
