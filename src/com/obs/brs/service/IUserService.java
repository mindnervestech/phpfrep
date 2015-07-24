package com.obs.brs.service;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.obs.brs.model.DataEntry;
import com.obs.brs.model.DeCompany;
import com.obs.brs.model.Publication;
import com.obs.brs.model.Region;
import com.obs.brs.model.States;
import com.obs.brs.model.Subscriber;
import com.obs.brs.model.SubscriberPublication;
import com.obs.brs.model.SubscriberUser;
import com.obs.brs.model.User;
import com.obs.brs.model.UserType;
import com.obs.brs.model.Country;

/**
 * 
 * User Service Interface
 * 
 * @author Jeevanantham
 *
 */
public interface IUserService {

	/**
	 * Add User
	 * 
	 * @param  User user
	 */
	public Integer addUser(User user);

	/**
	 * Update User
	 * 
	 * @param  User user
	 */
	public void updateUser(User user);

	/**
	 * Delete User
	 * 
	 * @param  User user
	 */
	public void deleteUser(User user);

	/**
	 * Get User
	 * 
	 * @param  int User Id
	 */
	public User getUserById(int id);

	/**
	 * Get User List
	 * 
	 * @return List - User list
	 */
	public List<User> getUsers();

	/**
	 * Login User
	 * @param userName
	 * @param password
	 * @return
	 */
	public User loginUser(String email, String password);

	/**
	 * Get UserType List
	 * 
	 * @return List - UserType list
	 */
	public List<UserType> getUserTypes();

	/**
	 * Get User User
	 * @param email
	 * 
	 * @return User - User
	 */
	public User getUserByEmail(String email);
	/**
	 * Get the user by identifying encryption value to reset the password
	 * @param encryptionVal
	 * @return
	 */
	public User getUserByForgotPwdEncryption(String encryptionVal);

	/**
	 * Get User List by search value and status
	 * 
	 */
	public List<User> getUserBySeachCriteria(int userId);
	public List<DeCompany> getAllCompanyData();
	
	

	/**
	 * Get User List by search value and status
	 * 
	 */
	public List<User> getUserBySeachCriteria(int userId,int userStatus,String searchValue);

	/**
	 * Get User
	 * @param userName
	 * 
	 * @return User
	 */
	public User getUserByUserName(String userName);

	/**
	 * @param userTypeId
	 * @param UserTypeId
	 * @param UserType
	 * @return
	 */
	public UserType getUserTypeById(long userTypeId);
	/**
	 * 
	 * @param email
	 * @return
	 */
	public SubscriberUser getSubscriberByEmail(String email);

	/**
	 * 
	 * @param subscriber
	 * @return 
	 */
	public Integer addSubscriber(Subscriber subscriber);
	/**
	 * 
	 * @param subscriberUser
	 * @return 
	 */
	public Integer addSubscriberUser(SubscriberUser subscriberUser);
	/**
	 * 
	 * @param subscriberId
	 * @return
	 */
	public Subscriber getSubscriberById(int subscriberId);
	/**
	 * 
	 * @param subscriberUserId
	 * @return
	 */
	public SubscriberUser getSubscriberUserById(int subscriberUserId);
	/**
	 * 
	 * @param subscriber
	 */
	public void updateSubscriber(Subscriber subscriber);

	/**
	 * 
	 * @param subscriberUser
	 */
	public void updateSubscriberUser(SubscriberUser subscriberUser);

	/**
	 * subscriber
	 * @return
	 */
	public List<Subscriber> getSubscriber();
	/**
	 * subscriberUser
	 * @return
	 */
	public List<SubscriberUser> getSubscriberUser();

	/**
	 * subscriberBySeachCriteria list by all Subscriber
	 * @param searchValue
	 * @param statusFilter
	 * @return
	 */
	public List<Subscriber> getSubscriberBySeachCriteria();

	/**
	 * subscriberBySeachCriteria based on status filter
	 * @param searchValue
	 * @param statusFilter
	 * @return
	 */
	public List<Subscriber> getSubscriberBySeachCriteria(String searchValue);
	/**
	 * subscriberBySeachCriteria list by all Subscriber
	 * @param searchValue
	 * @param statusFilter
	 * @return
	 */
	public List<SubscriberUser> getSubscriberUserBySeachCriteria(int SubscriberId);

	/**
	 * subscriberBySeachCriteria based on status filter
	 * @param searchValue
	 * @param statusFilter
	 * @return
	 */
	public List<SubscriberUser> getSubscriberUserBySeachCriteria(int SubscriberId,String searchValue);
	/**
	 * 
	 * @return subscriberArcheivedUserBySeachCriteria
	 */
	public List<Subscriber> getSubscriberArcheivedUserBySeachCriteria(String searchValue);
	/**
	 * 
	 * @param subscriberId
	 * @return
	 */
	public SubscriberUser getSubscriberUserBySubscriberId(int subscriberId);
	/**
	 * 
	 * @param searchValue
	 * @param userStatus
	 * @return
	 */
	public List<User> getUserBySeachCriteriaStatus(int userId, String searchValue);

	/**
	 * Login Subscriber
	 * @param userName
	 * @param password
	 * @return
	 */
	public SubscriberUser loginSubscriberUser(String email,String generateEncryptedPwd);

	/**
	 * 
	 * @param email
	 * @return
	 */
	public SubscriberUser getSubscriberUserByEmail(String email);
	/**
	 * 
	 * @param email
	 * @return
	 */
	public SubscriberUser getSubscriberUserByForgotPwdEncryption(String encryptionVal);
	/**
	 * 
	 * @param countryId
	 * @return
	 */
	public Country findCountryById(long countryId);
	/**
	 * 
	 * @return
	 */
	public List<Country> findAll();

	/**
	 * Add Region
	 * 
	 * @param  Region region
	 */
	public Integer addRegion(Region region);

	/**
	 * Update Region
	 * 
	 * @param  Region region
	 */
	public void updateRegion(Region region);

	/**
	 * Delete Region
	 * 
	 * @param  Region region
	 */
	public void deleteRegion(Region region);

	/**
	 * Get Region
	 * 
	 * @param  int Region Id
	 */
	public Region getRegionById(int id);

	/**
	 * Get Region List
	 * 
	 * @return List - Region list
	 */
	public List<Region> getRegions();

	/**
	 * Get Region List by search value and status
	 * 
	 */
	public List<Region> getRegionBySeachCriteria(int subscriberId);

	/**
	 * Get Region List by search value and status
	 * 
	 */
	public List<Region> getRegionBySeachCriteria(int subscriberId,String searchValue);
	/**
	 * 
	 * @param region
	 * @return
	 */
	public Region getRegionByRegion(String region);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public List<SubscriberUser> getSubscriberUserListBySubscriberId(int id, String searchValue);

	/**
	 * Add Publication
	 * 
	 * @param  Publication publication
	 */
	public Integer addPublication(Publication publication);

	/**
	 * Update Publication
	 * 
	 * @param  Publication publication
	 */
	public void updatePublication(Publication publication);

	/**
	 * Delete Publication
	 * 
	 * @param  Publication publication
	 */
	public void deletePublication(Publication publication);

	/**
	 * Get Publication
	 * 
	 * @param  int Publication Id
	 */
	public Publication getPublicationById(int id);

	/**
	 * Get Publication List
	 * 
	 * @return List - Publication list
	 */
	public List<Publication> getPublications();

	/**
	 * Get Publication List by search value and status
	 * 
	 */
	public List<Publication> getPublicationBySeachCriteria();

	/**
	 * Get Publication List by search value and status
	 * 
	 */
	public List<Publication> getPublicationBySeachCriteria(String searchValue);
	/**
	 * 
	 * @param Publication
	 * @return
	 */
	public Publication getPublicationByPublicationTitle(String publicationTitle);

	/**
	 * 
	 * @return
	 */
	public List<Publication> findAllPublication();

	/**
	 * 
	 * @return
	 */
	public List<Publication> findAllSection();
	/**
	 * 
	 * @param regionId
	 * @param region
	 * @return
	 */
	public Region getRegionByRegion(int regionId, String region);
	/**
	 * 
	 * @param publicationTitle
	 * @param publicationId
	 * @return
	 */
	public Publication getPublicationByPublicationTitle(String publicationTitle,
			int publicationId);
	/**
	 * 
	 * @param configurationType
	 * @return
	 */
	public List<Publication> getPublicationByTypeSeachCriteria(
			int publicationStatus, String searchValue);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public List<Region> getRegionByCountryId(long countryId);
	/**
	 * 
	 * @param regionId
	 * @return
	 */
	public String getRegionCountBySeachCriteria(long countryId);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public List<User> getUserListById(int id);
	/**
	 * 
	 * @param userId
	 * @param email
	 * @return
	 */
	public User getUserByEmail(int userId, String email);
	/**
	 * 
	 * @param subscriberPublication
	 */
	public Integer addSubscriberPublication(SubscriberPublication subscriberPublication);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public List<SubscriberPublication> findPublicationBySubscriber(int id);

	/**
	 * 
	 * @param subscriberPublication
	 */
	public void updateSubscriberPublication(SubscriberPublication subscriberPublication);
	/**
	 * 
	 * @param valueOf
	 * @return
	 */
	public SubscriberPublication findBySubscriberPublicationId(int id);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public List getCalendarViewReportList(int id,int reportCurrentMonthFirst, int reportCurrentYearFirst, int reportCurrentMonthSecond, int reportCurrentYearSecond,int reportCurrentMonthThird, int reportCurrentYearThird,int reportCurrentMonthFour, int reportCurrentYearFour);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public List getCalendarViewReportDetails(int id,int reportMonth, int reportYear, long deCompanyId);
	/**
	 * Get DeCompany List 
	 * 
	 */
	public List<Country> getCountry();
	/**
	 * Get DeCompany List
	 * 
	 */
	public List<States> getStates();
	/**
	 * 
	 * @param id
	 * @return
	 */
	public List<SubscriberUser> getSubscriberUserListBySubscriberId(int id);
	/**
	 * 
	 * @param email
	 * @param subscriberUserId
	 * @return
	 */
	public SubscriberUser getSubscriberUserByEmail(String email,
			int subscriberUserId);
	/**
	 * 
	 * @return
	 */
	public List<Publication> findAllAdvertiserType();
	/**
	 * 
	 * @return
	 */
	public List<Publication> findAllInstitutionType();
	/**
	 * 
	 * @return
	 */
	public List<Publication> findAllSearchAdvertiserType();

	/**
	 * 
	 * @param id
	 * @return
	 */
	public List getCompanyNameListBySubscriber(int id);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public List getPublicationListBySubscriber(int id);
/**
 * 
 * @param id
 * @param fromMonthRange
 * @param fromYearRange
 * @param toMonthRange
 * @param toYearRange
 * @param companyName
 * @param publicationSection
 * @param publicationPage
 * @param adSize
 * @param companyType
 * @param adType
 * @param state
 * @param country
 * @param publicationTitle
 * @return
 */
	public List searchViewReportList(int id,String fromDateValue,String toDateValue,
			List<String> companyName, List<String> publicationSection,
			List<String> publicationPage, List<String> adSize,
			List<String> companyType, List<String> adType, List<String> state,
			List<String> country, List<String> publicationTitle);
/**
 * 
 * @param id
 * @param fromMonthRange
 * @param fromYearRange
 * @param toMonthRange
 * @param toYearRange
 * @param searchViewCompanyName
 * @param searchViewPublicationSection
 * @param searchViewPublicationPage
 * @param searchViewAdSize
 * @param searchViewCompanyType
 * @param searchViewAdType
 * @param searchViewState
 * @param searchViewCountry
 * @param searchViewPublicationTitle
 * @return
 */
public List searchViewReportDetails(int id,  String fromDateValue,String toDateValue,
		String searchViewCompanyName, String searchViewPublicationSection,
		String searchViewPublicationPage, String searchViewAdSize,
		String searchViewAdOrientation,/*String searchViewCompanyType,*/ String searchViewAdType,
		String searchViewState, String searchViewCountry,
		String searchViewPublicationTitle,
		String searchViewJobDensity);
/**
 * 
 * @param id
 * @return
 */
public List getAdvertiserViewReportList(int id,int reportCurrentMonthFirst, int reportCurrentYearFirst, int reportCurrentMonthSecond, int reportCurrentYearSecond,int reportCurrentMonthThird, int reportCurrentYearThird,int reportCurrentMonthFour, int reportCurrentYearFour);
/**
 * 
 * @param id
 * @return
 */
public List getJournalViewReportList(int id);
/**
 * 
 * @param id
 * @return
 */
public List getAdvertiserViewReportDetails(int id,int reportMonth, int reportYear, long deCompanyId);
/**
 * 
 * @param id
 * @return
 */
public List getSubscriberJournalList(int id);
/**
 * 
 * @param id
 * @return
 */
public List getJournalViewReportDetails(int id, long deCompanyId, long deJournalId);
/**
 * SearchValueAdvertisertype
 * @param publicationId
 * @return
 */
public Publication getPublicationBySearchValueAdvertisertype(String publicationId);

Publication getPublicationByPublicationTitleandType(String publicationTitle,
		int publicationType);

}
