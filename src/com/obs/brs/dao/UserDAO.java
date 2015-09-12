package com.obs.brs.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;



import com.obs.brs.model.Country;
import com.obs.brs.model.DataEntry;
import com.obs.brs.model.DeCompany;
import com.obs.brs.model.ParentImage;
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
 * User DAO
 * 
 * @author Jeevanantham
 *
 */
public class UserDAO implements IUserDAO {

	private SessionFactory sessionFactory;

	/**
	 * Get Hibernate Session Factory
	 * 
	 * @return SessionFactory - Hibernate Session Factory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Set Hibernate Session Factory
	 * 
	 * @param SessionFactory - Hibernate Session Factory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Add User
	 * 
	 * @param  User user
	 */
	@Override
	public Integer addUser(User user) {
		return (Integer)getSessionFactory().getCurrentSession().save(user);
	}

	/**
	 * Delete User
	 * 
	 * @param  User user
	 */
	@Override
	public void deleteUser(User user) {
		getSessionFactory().getCurrentSession().delete(user);
	}

	/**
	 * Update User
	 * 
	 * @param  User user
	 */
	@Override
	public void updateUser(User user) {
		getSessionFactory().getCurrentSession().update(user);
	}

	/**
	 * Get User
	 * 
	 * @param  int User Id
	 * @return User 
	 */
	@Override
	public User getUserById(int id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from User where id=? and isDeleted = 0")
				.setParameter(0,id).list();
		return list!=null?(User)list.get(0):null;
	}

	/**
	 * Get User List
	 * 
	 * @return List - User list
	 */
	@Override
	public List<User> getUsers() {
		String SQL = "from User as m WHERE isDeleted = 0 AND isActive = 1 ";
		List list = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		return list;
	}
	/**
	 *  @return the loginUser
	 */
	@Override
	public User loginUser(String email, String password) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from User where email=? and password=? and isDeleted = 0 and isActive=1")
				.setParameter(0, email)
				.setParameter(1, password).list();
		return !list.isEmpty()?(User)list.get(0):null;
	}
	/**
	 * @return the UserTypes
	 */
	@Override
	public List<UserType> getUserTypes() {
		return getSessionFactory().getCurrentSession().createQuery("from UserType where id!=1  AND id!=5").list();
	}
	/**
	 * @return the UserByEmail
	 */
	@Override
	public User getUserByEmail(String email) {
		List<User> userList = getSessionFactory().getCurrentSession().createQuery("from User where email=? and isDeleted=0 ").setParameter(0, email).list();
		if (userList.size() > 0 ) {
			return userList.get(0);
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see com.obs.fms.dao.IUserDAO#getUserByForgotPwdEncryption(java.lang.String)
	 */
	@Override
	public User getUserByForgotPwdEncryption(String encryptionVal) {

		List<User> userList = getSessionFactory().getCurrentSession().createQuery("from User where forgotpwd_encp=? ").setParameter(0, encryptionVal).list();
		if (userList.size() > 0 ) {
			return userList.get(0);
		}
		return null;
	}
    
    /**
     * @return the UserBySeachCriteria
     */
	@Override
	public List<User> getUserBySeachCriteria(int userId) {
		String 	SQL = "From User as m where m.isDeleted=0 and m.userType.id!=1 and m.id!="+userId;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
	/**
	 * @return the UserBySeachCriteria
	 */
	@Override
	public List<User> getUserBySeachCriteria(int userId,int userStatus,String searchValue) {
		String appendStr = "";
		String statusAppendStr = "";
		if(StringUtils.isNotBlank(searchValue)){
			appendStr  = appendStr+" m.firstName LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.lastName LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.email LIKE '%"+searchValue+"%'";
			appendStr  = "AND ( "+appendStr+" )";
		}
		String 	SQL = "From User as m where m.isDeleted=0 and m.userType.id!=1 and m.userType.id="+userStatus+" and m.id!="+userId+appendStr;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}

	@Override
	public User getUserByUserName(String userName) {
		List<User> userList = getSessionFactory().getCurrentSession().createQuery("from User where userName=? ").setParameter(0, userName).list();
		if (userList.size() > 0 ) {
			return userList.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.obs.fms.dao.IUserDAO#getUserByRole(int)
	 */
	@Override
	public List<User> getUserByRole(int role) {
		@SuppressWarnings("unchecked")
		List<User> userList = getSessionFactory().getCurrentSession().createQuery("FROM User WHERE userRole.id="+role).list();
		return userList;
	}
	//return the userType based on id
	@Override
	public UserType getUserTypeById(long userTypeId) {
		List<UserType> userTypeList = getSessionFactory().getCurrentSession().createQuery("from UserType where id=? ").setParameter(0, userTypeId).list();
		if (userTypeList.size() > 0 ) {
			return userTypeList.get(0);
		}
		return null;
	}

	@Override
	public SubscriberUser getSubscriberByEmail(String email) {
		List<SubscriberUser> subscriberList = getSessionFactory().getCurrentSession().createQuery("from SubscriberUser where email=? and isDeleted = 0").setParameter(0, email).list();
		if (subscriberList.size() > 0 ) {
			return subscriberList.get(0);
		}
		return null;
	}

	/**
	 * Add CompanyUser
	 * 
	 * @param  SubscriberUser companyUser
	 */
	@Override
	public Integer addSubscriber(Subscriber subscriber) {
		return (Integer)getSessionFactory().getCurrentSession().save(subscriber);
	}
	/**
	 * Get CompanyUser
	 * 
	 * @param  int CompanyUser Id
	 * @return CompanyUser 
	 */
	@Override
	public Subscriber getSubscriberById(int subscriberId) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from Subscriber where id=? and isDeleted = 0")
				.setParameter(0,subscriberId).list();
		return list!=null?(Subscriber)list.get(0):null;
	}
	/**
	 * Update Company USer
	 * 
	 * @param  SubscriberUser  companyUser
	 */
	@Override
	public void updateSubscriber(Subscriber subscriber) {
		getSessionFactory().getCurrentSession().update(subscriber);
	}
	/**
	 * List all the CompanyUser
	 */
	@Override
	public List<Subscriber> getSubscriber() {
		String SQL = "from Subscriber as m WHERE isDeleted = 0 AND isActive = 1 ";
		List list = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		return list;
	}
	/**
	 * List all the CompanyUser based on the status filter
	 */
	@Override
	public List<Subscriber> getSubscriberBySeachCriteria() {
		String 	SQL = "From Subscriber as m where m.isDeleted=0";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
	/**
	 * List all the CompanyUser based on the status filter
	 */
	@Override
	public List<Subscriber> getSubscriberBySeachCriteria(String searchValue) {
		String appendStr = "";
		if(StringUtils.isNotBlank(searchValue)){
			appendStr  = appendStr+" m.companyName LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.notes LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.companyLocation LIKE '%"+searchValue+"%'";
			appendStr  = "AND ( "+appendStr+" )";
		}
		String 	SQL = "From Subscriber as m where m.isDeleted=0"+appendStr;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
	/**
	 * List all the CompanyUser based on the status filter
	 */
	@Override
	public List<Subscriber> getSubscriberArcheivedUserBySeachCriteria(
			String searchValue) {
		String appendStr = "";
		String statusAppendStr = "";
		if(StringUtils.isNotBlank(searchValue)){
			appendStr  = appendStr+" m.companyName LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.notes LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.companyLocation LIKE '%"+searchValue+"%'";
			appendStr  = "AND ( "+appendStr+" )";
		}
		String 	queryString = "From Subscriber as m where m.isDeleted=1 "+appendStr+" ORDER BY m.id DESC";

		return getSessionFactory().getCurrentSession().createQuery(queryString).list();
	}
    /**
     * @return the  addSubscriberUser
     */
	@Override
	public Integer addSubscriberUser(SubscriberUser subscriberUser) {
		return (Integer)getSessionFactory().getCurrentSession().save(subscriberUser);
	}
    /**
     * @return the SubscriberUserById
     */
	@Override
	public SubscriberUser getSubscriberUserById(int subscriberUserId) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from SubscriberUser where id=? and isDeleted = 0")
				.setParameter(0,subscriberUserId).list();
		return list!=null?(SubscriberUser)list.get(0):null;
	}
    /**
     * @return the updateSubscriberUser
     */
	@Override
	public void updateSubscriberUser(SubscriberUser subscriberUser) {
		getSessionFactory().getCurrentSession().update(subscriberUser);

	}
    /**
     * @return the SubscriberUser
     */
	@Override
	public List<SubscriberUser> getSubscriberUser() {
		String SQL = "from SubscriberUser as m WHERE isDeleted = 0 AND isActive = 1 ";
		List list = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		return list;
	}
    /**
     * @return the SubscriberUserBySeachCriteria
     */
	@Override
	public List<SubscriberUser> getSubscriberUserBySeachCriteria(int subscriberId) {
		String 	SQL = "From SubscriberUser as m where m.isDeleted=0 and m.userType.id=6 and m.subscriber="+subscriberId;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
    /**
     * @return the SubscriberUserBySeachCriteria
     */
	@Override
	public List<SubscriberUser> getSubscriberUserBySeachCriteria(int subscriberId,
			String searchValue) {
		String appendStr = "";
		String statusAppendStr = "";
		if(StringUtils.isNotBlank(searchValue)){
			appendStr  = appendStr+" m.firstName LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.lastName LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.email LIKE '%"+searchValue+"%'";
			appendStr  = "AND ( "+appendStr+" )";
		}
		String 	SQL = "From SubscriberUser as m where m.isDeleted=0 and m.userType.id=6 and m.subscriber="+subscriberId+appendStr;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
    /**
     * @return the SubscriberUserBySubscriberId
     */
	@Override
	public SubscriberUser getSubscriberUserBySubscriberId(int subscriberId) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from SubscriberUser as su where su.subscriber.id=? and su.isDeleted = 0")
				.setParameter(0,subscriberId).list();
		return !list.isEmpty()?(SubscriberUser)list.get(0):null;
	}
    /**
     * @return the UserBySeachCriteriaStatus
     */
	@Override
	public List<User> getUserBySeachCriteriaStatus(int userId,String  searchValue) {
		/*String 	SQL = "From User as m where m.isDeleted=0 and m.userType.id!=1  and m.userType.id="+userStatus+" and m.id!="+userId;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();*/
		String appendStr = "";
		if(StringUtils.isNotBlank(searchValue)){
			appendStr  = appendStr+" m.firstName LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.lastName LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.email LIKE '%"+searchValue+"%'";
			appendStr  = "AND ( "+appendStr+" )";
		}
		String 	SQL = "From User as m where m.isDeleted=0 and m.userType.id!=1 and m.id!="+userId+appendStr;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
    /**
     * @return the loginSubscriberUser
     */
	@Override
	public SubscriberUser loginSubscriberUser(String email,
			String password) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from SubscriberUser where email=? and password=? and isDeleted = 0 and isActive=1")
				.setParameter(0, email)
				.setParameter(1, password).list();
		return !list.isEmpty()?(SubscriberUser)list.get(0):null;
	}
    /**
     * @return the SubscriberUserByEmail
     */
	@Override
	public SubscriberUser getSubscriberUserByEmail(String email) {
		List<SubscriberUser> subscriberUserList = getSessionFactory().getCurrentSession().createQuery("from SubscriberUser where email=? and isDeleted = 0").setParameter(0, email).list();
		if (subscriberUserList.size() > 0 ) {
			return subscriberUserList.get(0);
		}
		return null;
	}
    /**
     * @return the SubscriberUserByForgotPwdEncryption
     */
	@Override
	public SubscriberUser getSubscriberUserByForgotPwdEncryption(
			String encryptionVal) {
		List<SubscriberUser> subscriberUserList = getSessionFactory().getCurrentSession().createQuery("from SubscriberUser where forgotpwd_encp=? ").setParameter(0, encryptionVal).list();
		if (subscriberUserList.size() > 0 ) {
			return subscriberUserList.get(0);
		}
		return null;
	}
    /**
     * @return the findCountryById
     */
	@Override
	public Country findCountryById(long countryId) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from Country where id=?")
				.setParameter(0,countryId).list();
		return list!=null?(Country)list.get(0):null;
	}
    /**
     * @return the list
     */
	@Override
	public List<Country> findAll() {
		String SQL = "from Country ORDER BY countryName ASC";
		List list = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		return list;
	}
    
	
	//Add Region
	@Override
	public Integer addRegion(Region region) {
		return (Integer) getSessionFactory().getCurrentSession().save(region);
	}
    //update Region
	@Override
	public void updateRegion(Region region) {
		getSessionFactory().getCurrentSession().update(region);

	}
     // delete Region
	@Override
	public void deleteRegion(Region region) {
		getSessionFactory().getCurrentSession().delete(region);

	}
    /**
     * @return the RegionById
     */
	@Override
	public Region getRegionById(int id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from Region where id=? and isDeleted = 0")
				.setParameter(0,id).list();
		return list!=null?(Region)list.get(0):null;
	}
    /**
     * @return the getRegions
     */
	@Override
	public List<Region> getRegions() {
		String SQL = "from Region as m WHERE isDeleted = 0";
		List list = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		return list;
	}
    /**
     * @return the getRegionBySeachCriteria
     */
	@Override
	public List<Region> getRegionBySeachCriteria(int subscriberId) {
		String 	SQL = "From Region as m where m.isDeleted=0 and m.subscriber="+subscriberId +"group by m.country.countryId ";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
    /**
     * @return the RegionBySeachCriteria
     */
	@Override
	public List<Region> getRegionBySeachCriteria( int subscriberId,String searchValue) {
		String appendStr = "";
		String statusAppendStr = "";
		if(StringUtils.isNotBlank(searchValue)){
			appendStr  = appendStr+" m.region LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.country.countryName LIKE '%"+searchValue+"%'";
			appendStr  = "AND ( "+appendStr+" )";
		}
		String 	SQL = "From Region as m where m.isDeleted=0 and m.subscriber="+subscriberId+appendStr+"group by m.country.countryId ";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
    /**
     * @return the RegionByRegion
     */
	@Override
	public Region getRegionByRegion(String region) {
		List<Region> regionList = getSessionFactory().getCurrentSession().createQuery("from Region where region=? and isDeleted=0").setParameter(0, region).list();
		if (regionList.size() > 0 ) {
			return regionList.get(0);
		}
		return null;
	}
    /**
     * 
     * @return the SubscriberUserListBySubscriberId
     */
	@Override
	public List<SubscriberUser> getSubscriberUserListBySubscriberId(int id,String searchValue) {
		String appendStr = "";
		String statusAppendStr = "";
		if(StringUtils.isNotBlank(searchValue)){
			appendStr  = appendStr+" m.subscriber.companyName LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.subscriber.companyEmail LIKE '%"+searchValue+"%'";
			appendStr  = "AND ( "+appendStr+" )";
		}
		String 	SQL = "From SubscriberUser as m where m.isDeleted=0 and m.userType.id=6 and m.subscriber="+id+appendStr;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
   // add Publication
	@Override
	public Integer addPublication(Publication publication) {
		return (Integer) getSessionFactory().getCurrentSession().save(publication);
	}
     //update Publication
	@Override
	public void updatePublication(Publication publication) {
		getSessionFactory().getCurrentSession().update(publication);

	}
     // delete Publication
	@Override
	public void deletePublication(Publication publication) {
		getSessionFactory().getCurrentSession().delete(publication);

	}
    /**
     * @return the PublicationById
     */
	@Override
	public Publication getPublicationById(int id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from Publication where id=? and isDeleted = 0")
				.setParameter(0,id).list();
		return list!=null?(Publication)list.get(0):null;
	}
    /**
     * @return the Publications
     */
	@Override
	public List<Publication> getPublications() {
		String SQL = "from Publication as m WHERE isDeleted = 0";
		List list = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		return list;
	}
     /**
      * @return the PublicationBySeachCriteria
      */
	@Override
	public List<Publication> getPublicationBySeachCriteria() {
		String 	SQL = "From Publication as m where m.isDeleted=0";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
    /**
     * @return the PublicationBySeachCriteria
     */
	@Override
	public List<Publication> getPublicationBySeachCriteria(String searchValue) {
		String appendStr = "";
		String statusAppendStr = "";
		if(StringUtils.isNotBlank(searchValue)){
			appendStr  = appendStr+" m.publicationTitle LIKE '%"+searchValue+"%'";
			appendStr  = "AND ( "+appendStr+" )";
		}
		String 	SQL = "From Publication as m where m.isDeleted=0"+appendStr;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
    /**
     * @return the PublicationByPublicationTitleandType
     */
	@Override
	public Publication getPublicationByPublicationTitleandType(String publicationTitle, int publicationType) {
		List<Publication> publicationList = getSessionFactory().getCurrentSession().createQuery("from Publication where publicationTitle=? and publicationType=? and isDeleted=0").setParameter(0, publicationTitle).setParameter(1, publicationType).list();
		if (publicationList.size() > 0 ) {
			return publicationList.get(0);
		}
		return null;
	}
    /**
     * @return the findAllPublication
     */
	@Override
	public List<Publication> findAllPublication() {
		String SQL = "from Publication where isDeleted=0 and publicationType=2 ORDER BY publicationTitle ASC";
		List list = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		return list;
	}
    /**
     * @return the findAllSection
     */
	@Override
	public List<Publication> findAllSection() {
		String SQL = "from Publication where isDeleted=0 and publicationType=3 ORDER BY publicationTitle ASC";
		List list = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		return list;
	}
    /**
     * @return the RegionByRegion
     */
	@Override
	public Region getRegionByRegion(int regionId, String region) {
		List<Region> regionList = getSessionFactory().getCurrentSession().createQuery("from Region where region=? and isDeleted=0 and id!="+regionId).setParameter(0, region).list();
		if (regionList.size() > 0 ) {
			return regionList.get(0);
		}
		return null;
	}
    /**
     * @return the PublicationByPublicationTitle
     */
	@Override
	public Publication getPublicationByPublicationTitle(String publicationTitle,
			int publicationId) {
		List<Publication> publicationList = getSessionFactory().getCurrentSession().createQuery("from Publication where publicationTitle=? and isDeleted=0 and id!="+publicationId).setParameter(0, publicationTitle).list();
		if (publicationList.size() > 0 ) {
			return publicationList.get(0);
		}
		return null;
	}
    /**
     * @return the PublicationByTypeSeachCriteria
     */
	@Override
	public List<Publication> getPublicationByTypeSeachCriteria(
			int configurationType,String searchValue) {
		/*String 	SQL = "From Publication as m where m.isDeleted=0 and  m.publicationType="+configurationType;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();*/
		String appendStr = "";
		String statusAppendStr = "";
		if(StringUtils.isNotBlank(searchValue)){
			appendStr  = appendStr+" m.publicationTitle LIKE '%"+searchValue+"%'";
			appendStr  = "AND ( "+appendStr+" )";
		}
		String 	SQL = "From Publication as m where m.isDeleted=0 and m.publicationType="+configurationType+""+appendStr;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
   /**
    * @return the RegionByCountryId
    */
	@Override
	public List<Region> getRegionByCountryId(long countryId) {
		String 	SQL = "From Region as m where m.isDeleted=0 and  m.country.countryId="+countryId;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
    /**
     * @return the RegionCountBySeachCriteria
     */
	@Override
	public String getRegionCountBySeachCriteria(long countryId) {
		List regionList = getSessionFactory().getCurrentSession().createQuery("select count(*) from Region as m where m.isDeleted=0 and  m.country.countryId="+countryId).list();
		if (regionList.size() > 0 ) {
			return String.valueOf(regionList.get(0));
		}
		return null;
	}
    /**
     * @return the UserListById
     */
	@Override
	public List<User> getUserListById(int id) {
		String SQL = "from User as m WHERE isDeleted = 0 AND isActive = 1 and id="+id;
		List list = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		return list;
	}
    /**
     * @return the UserByEmail
     */
	@Override
	public User getUserByEmail(int userId, String email) {
		List<User> userList = getSessionFactory().getCurrentSession().createQuery("from User where email=? and isDeleted=0  and id!="+userId).setParameter(0, email).list();
		if (userList.size() > 0 ) {
			return userList.get(0);
		}
		return null;

	}
    //add SubscriberPublication 
	@Override
	public Integer addSubscriberPublication(SubscriberPublication subscriberPublication) {
		return (Integer)getSessionFactory().getCurrentSession().save(subscriberPublication);
	}
    /**
     * @return the findPublicationBySubscriber
     */
	@Override
	public List<SubscriberPublication> findPublicationBySubscriber(int id) {
		String SQL = "from SubscriberPublication as m where m.isDeleted =0 and   m.subscriber.id="+id;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
    // update SubscriberPublication
	@Override
	public void updateSubscriberPublication(
			SubscriberPublication subscriberPublication) {
		getSessionFactory().getCurrentSession().update(subscriberPublication);

	}
    /**
     * @return the  findBySubscriberPublicationId
     */
	@Override
	public SubscriberPublication findBySubscriberPublicationId(int id) {
		List<SubscriberPublication> subscriberPublicationList = getSessionFactory().getCurrentSession().createQuery("from SubscriberPublication where id=? ").setParameter(0, id).list();
		if (subscriberPublicationList.size() > 0 ) {
			return subscriberPublicationList.get(0);
		}
		return null;
	}
    /**
     * @return the CalendarViewReportList
     */
	@Override
	public List getCalendarViewReportList(int id,int reportCurrentMonthFirst, int reportCurrentYearFirst, int reportCurrentMonthSecond, int reportCurrentYearSecond,int reportCurrentMonthThird, int reportCurrentYearThird,int reportCurrentMonthFour, int reportCurrentYearFour) {
		//String SQL="select c.DC_COMPANY_NAME,count((case when (month(pa.DD_issue_date)="+reportCurrentMonthFirst+" && year(pa.DD_issue_date)="+reportCurrentYearFirst+") then pa.DD_issue_date else NULL end)),count((case when (month(pa.DD_issue_date)="+reportCurrentMonthSecond+" && year(pa.DD_issue_date)="+reportCurrentYearSecond+") then pa.DD_issue_date else NULL end)), count((case when (month(pa.DD_issue_date)="+reportCurrentMonthThird+" && year(pa.DD_issue_date)="+reportCurrentYearThird+") then pa.DD_issue_date else NULL end)),count((case when (month(pa.DD_issue_date)="+reportCurrentMonthFour+" && year(pa.DD_issue_date)="+reportCurrentYearFour+") then pa.DD_issue_date else NULL end)),d.DN_ID as de_id,c.DN_ID as company_id from tbl_subscriber_publication sp Left outer join tbl_parent_image pa on sp.Dc_publication_title=pa.Dc_publication_title Left Outer Join tbl_de_data d on pa.DN_ID=d.DN_Parent_Image_ID Left Outer Join tbl_de_company c on d.DN_DECOMPANY_ID =c.DN_ID where sp.DC_subscriber="+id+" and pa.DD_issue_date >= sp.DD_From_date and pa.DD_issue_date <= sp.dd_to_date and d.DB_ISAPPROVED=1 group by c.DC_COMPANY_NAME";
		//System.out.println("SQL"+SQL);
	/*	String SQL="select c.DC_COMPANY_NAME,count(distinct(case when (month(pa.DD_ISSUE_DATE)="+reportCurrentMonthFirst+" && year(pa.DD_ISSUE_DATE)="+reportCurrentYearFirst+") then d.DN_ID else NULL end)),"+
		"count(distinct(case when (month(pa.DD_ISSUE_DATE)="+reportCurrentMonthSecond+" && year(pa.DD_ISSUE_DATE)="+reportCurrentYearSecond+") then d.DN_ID else NULL end)),"+ 
		"count(distinct(case when (month(pa.DD_ISSUE_DATE)="+reportCurrentMonthThird+" && year(pa.DD_ISSUE_DATE)="+reportCurrentYearThird+") then d.DN_ID else NULL end)),"+
		"count(distinct(case when (month(pa.DD_ISSUE_DATE)="+reportCurrentMonthFour+" && year(pa.DD_ISSUE_DATE)="+reportCurrentYearFour+") then d.DN_ID else NULL end)), d.DN_ID as de_id,c.DN_ID as company_id"+ 
		"from tbl_subscriber_publication sp LEFT OUTER JOIN tbl_parent_image pa on sp.DC_PUBLICATION_TITLE = pa.DC_PUBLICATION_TITLE LEFT OUTER JOIN tbl_de_data d on pa.DN_ID=d.DN_PARENT_IMAGE_ID LEFT OUTER JOIN tbl_de_company c on d.DN_DECOMPANY_ID =c.DN_ID where sp.DC_SUBSCRIBER="+id+" and pa.DD_ISSUE_DATE >= sp.DD_FROM_DATE and pa.DD_ISSUE_DATE <= sp.DD_TO_DATE and d.DB_ISAPPROVED=1 group by c.DC_COMPANY_NAME";
		*/
		String SQL="select c.DC_COMPANY_NAME,count(distinct(case when (month(pa.DD_ISSUE_DATE)="+reportCurrentMonthFirst+"&& year(pa.DD_ISSUE_DATE)="+reportCurrentYearFirst+") then d.DN_ID else NULL end)) as month1,count(distinct(case when (month(pa.DD_ISSUE_DATE)="+reportCurrentMonthSecond+" && year(pa.DD_ISSUE_DATE)="+reportCurrentYearSecond+") then d.DN_ID else NULL end)) as month2,count(distinct(case when (month(pa.DD_ISSUE_DATE)="+reportCurrentMonthThird+" && year(pa.DD_ISSUE_DATE)="+reportCurrentYearThird+") then d.DN_ID else NULL end))as month3,count(distinct(case when (month(pa.DD_ISSUE_DATE)="+reportCurrentMonthFour+" && year(pa.DD_ISSUE_DATE)="+reportCurrentYearFour+") then d.DN_ID else NULL end))as month4, d.DN_ID as de_id,c.DN_ID as company_id from tbl_subscriber_publication sp LEFT OUTER JOIN tbl_parent_image pa on sp.DC_PUBLICATION_TITLE = pa.DC_PUBLICATION_TITLE LEFT OUTER JOIN tbl_de_data d on pa.DN_ID=d.DN_PARENT_IMAGE_ID LEFT OUTER JOIN tbl_de_company c on d.DN_DECOMPANY_ID =c.DN_ID where sp.DC_SUBSCRIBER="+id+" and pa.DD_ISSUE_DATE >= sp.DD_FROM_DATE and pa.DD_ISSUE_DATE <= sp.DD_TO_DATE and d.DB_ISAPPROVED=1 group by c.DC_COMPANY_NAME";
		return getSessionFactory().getCurrentSession().createSQLQuery(SQL).list();
	} 

    /**
     * @return the CalendarViewReportDetails
     */
	@Override
	public List getCalendarViewReportDetails(int id, int reportMonth, int reportYear, long deCompanyId) {
		//String SQL="select d.DN_ID from tbl_subscriber_publication sp Left outer join tbl_parent_image pa on sp.Dc_publication_title=pa.Dc_publication_title Left Outer Join tbl_de_data d on pa.DN_ID=d.DN_Parent_Image_ID Left Outer Join tbl_de_company c on d.DN_DECOMPANY_ID =c.DN_ID where sp.DC_subscriber="+id+" and pa.DD_issue_date >= sp.DD_From_date and pa.DD_issue_date <= sp.dd_to_date and d.DB_ISAPPROVED=1";
		//String SQL ="SELECT * FROM tbl_de_data as dat LEFT OUTER JOIN tbl_de_company as c ON (dat.DN_DECOMPANY_ID =c.DN_ID) LEFT OUTER JOIN tbl_parent_image as pa ON (pa.DN_ID=dat.DN_PARENT_IMAGE_ID) WHERE dat.DN_DECOMPANY_ID="+deCompanyId+" AND MONTH(pa.DD_issue_date)="+reportMonth+" && YEAR(pa.DD_issue_date)="+reportYear+" AND dat.DB_ISAPPROVED=1";
		String SQL="SELECT * FROM tbl_de_data AS dat LEFT OUTER JOIN tbl_de_company AS c ON (dat.DN_DECOMPANY_ID =c.DN_ID) " +
				"LEFT OUTER JOIN tbl_parent_image AS pa ON (pa.DN_ID=dat.DN_PARENT_IMAGE_ID) " +
				"LEFT OUTER JOIN tbl_subscriber_publication AS  sp ON (sp.Dc_publication_title=pa.Dc_publication_title) " +
				"WHERE sp.DC_subscriber="+id+" AND dat.DN_DECOMPANY_ID="+deCompanyId+" AND MONTH(pa.DD_issue_date)="+reportMonth+" && YEAR(pa.DD_issue_date)="+reportYear+" AND dat.DB_ISAPPROVED=1 group by dat.DN_ID";
		//System.out.println("SQL"+SQL);
		return getSessionFactory().getCurrentSession().createSQLQuery(SQL).addEntity(DataEntry.class).list();
	}
    /**
     * @return the Country
     */
	@Override
	public List<Country> getCountry() {
		List<Country> list = getSessionFactory().getCurrentSession().createQuery("from Country").list();
		return list;
	}
    /**
     * @return the States
     */
	@Override
	public List<States> getStates() {
		List<States> list = getSessionFactory().getCurrentSession().createQuery("from States").list();
		return list;
	}
    /**
     * @return the SubscriberUserListBySubscriberId
     */
	@Override
	public List<SubscriberUser> getSubscriberUserListBySubscriberId(int id) {
		String 	SQL = "From SubscriberUser as m where m.isDeleted=0 and m.subscriber="+id;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
    /**
     * @return the SubscriberUserByEmail
     */
	@Override
	public SubscriberUser getSubscriberUserByEmail(String email,
			int subscriberUserId) {
		List<SubscriberUser> subscriberUserList = getSessionFactory().getCurrentSession().createQuery("from SubscriberUser where email=? and isDeleted=0  and id!="+subscriberUserId).setParameter(0, email).list();
		if (subscriberUserList.size() > 0 ) {
			return subscriberUserList.get(0);
		}
		return null;
	}
    /**
     * @return the findAllAdvertiserType
     */
	@Override
	public List<Publication> findAllAdvertiserType() {
		String 	SQL = "From Publication as m where m.isDeleted=0 and m.publicationType="+4;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
    /**
     * @return the findAllInstitutionType
     */
	@Override
	public List<Publication> findAllInstitutionType() {
		String 	SQL = "From Publication as m where m.isDeleted=0 and m.publicationType="+5;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
     /**
      * @return the findAllSearchAdvertiserType
      */
	@Override
	public List<Publication> findAllSearchAdvertiserType() {
		String 	SQL = "From Publication as m where m.isDeleted=0 and m.publicationType="+6;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
    /**
     * @return the CompanyNameListBySubscriber
     */
	@Override
	public List getCompanyNameListBySubscriber(int id) {
		String SQL="SELECT * FROM tbl_de_data AS dat LEFT OUTER JOIN tbl_de_company AS c ON (dat.DN_DECOMPANY_ID =c.DN_ID) LEFT OUTER JOIN tbl_parent_image AS pa ON (pa.DN_ID=dat.DN_PARENT_IMAGE_ID) LEFT OUTER JOIN tbl_subscriber_publication AS  sp ON (sp.Dc_publication_title=pa.Dc_publication_title) WHERE pa.DD_ISSUE_DATE <= sp.DD_TO_DATE and pa.DD_ISSUE_DATE >=sp.DD_FROM_DATE and sp.DC_subscriber="+id+" GROUP BY c.DC_COMPANY_NAME";
		return getSessionFactory().getCurrentSession().createSQLQuery(SQL).addEntity(DataEntry.class).list();
	}
    /**
     * @return the PublicationListBySubscribe
     */
	@Override
	public List getPublicationListBySubscriber(int id) {
		String SQL="SELECT * FROM tbl_parent_image AS pa LEFT OUTER JOIN tbl_subscriber_publication AS  sp ON (sp.Dc_publication_title=pa.Dc_publication_title) WHERE pa.DD_ISSUE_DATE <= sp.DD_TO_DATE and pa.DD_ISSUE_DATE >=sp.DD_FROM_DATE and sp.DC_subscriber="+id+" GROUP BY sp.Dc_publication_title";
		return getSessionFactory().getCurrentSession().createSQLQuery(SQL).addEntity(ParentImage.class).list();
	}
    /**
     * @return the searchViewReportList
     */
	@Override
	public List searchViewReportList(int id,String fromDateValue,String toDateValue,
			List<String> companyName, List<String> publicationSection,
			List<String> publicationPage, List<String> adSize,
			List<String> companyType, List<String> adType, List<String> state,
			List<String> country, List<String> publicationTitle) {
		String appandQuery = "";

		//date
		/*if(fromMonthRange!=null && !"".equals(fromMonthRange) && fromYearRange != null && !"".equals(fromYearRange) && toMonthRange!=null && !"".equals(toMonthRange) && toYearRange != null && !"".equals(toYearRange)   ){
			String appandQueryDate  = "(month(pa.DD_issue_date)>="+fromMonthRange+" AND year(pa.DD_issue_date)>="+fromYearRange+") AND (month(pa.DD_issue_date)<="+toMonthRange+" AND year(pa.DD_issue_date)<="+toYearRange+")";
			appandQuery  = " AND ( "+appandQueryDate+" )";
			//System.out.println("appandQueryDate:"+appandQuery);
		}*/
		if(fromDateValue != null && !"".equals(fromDateValue) && toDateValue != null && !"".equals(toDateValue)){
			String appandQueryDate  = "pa.DD_issue_date >= '"+fromDateValue+"' AND pa.DD_issue_date <= '"+toDateValue+"'";
			appandQuery  = " AND ( "+appandQueryDate+" )";
		}
		//company
		if(companyName!=null &&  companyName.size() >0){
			String appandQueryCompanyName="";
			for(int i=0;i<companyName.size();i++){
				if(i == 0 ){
					appandQueryCompanyName  = appandQueryCompanyName + "dat.DN_DECOMPANY_ID ="+companyName.get(i)+"";
				}else{
					appandQueryCompanyName  = appandQueryCompanyName +" " + "  OR dat.DN_DECOMPANY_ID ="+companyName.get(i)+"";
				}
			}
			appandQuery  = appandQuery +" AND ( "+appandQueryCompanyName+" )";
			//System.out.println("appandQuery companyName :"+appandQuery);
		}
		//Publication Section
		if(publicationSection!=null && publicationSection.size() >0){
			String appandQueryPublicationSection="";
			for(int i=0;i<publicationSection.size();i++){
				if(i == 0 ){
					appandQueryPublicationSection  = appandQueryPublicationSection+" "+" pa.DC_SECTION ="+publicationSection.get(i)+"";
				}else{
					appandQueryPublicationSection  = appandQueryPublicationSection+" "+"  OR  pa.DC_SECTION ="+publicationSection.get(i)+"";
				}
			}
			appandQuery  = appandQuery + " AND ( "+appandQueryPublicationSection+" )";
			//System.out.println("appandQuery publicationSection:"+appandQuery);
		}
		//Publication page
		if(publicationPage!=null && publicationPage.size() >0){
			String appandQueryPublicationPage="";
			for(int i=0;i<publicationPage.size();i++){
				if(i == 0 ){
					appandQueryPublicationPage  = appandQueryPublicationPage+" "+" pa.DC_PAGE ="+publicationPage.get(i)+"";
				}else{
					appandQueryPublicationPage  = appandQueryPublicationPage+" "+"  OR  pa.DC_PAGE ="+publicationPage.get(i)+"";
				}
			}
			appandQuery  =  appandQuery + " AND ( "+appandQueryPublicationPage+" )";
			//System.out.println("appandQuery publicationPage:"+appandQuery);
		}

		//adSize
		if(adSize!=null && adSize.size() >0){
			String appandQueryAdSize="";
			for(int i=0;i<adSize.size();i++){
				if( i == 0 ){
					appandQueryAdSize  = appandQueryAdSize+" "+" dat.DC_AD_SIZE ="+adSize.get(i)+"";
				}else{
					appandQueryAdSize  = appandQueryAdSize+" "+" OR dat.DC_AD_SIZE ="+adSize.get(i)+"";
				}
			}
			appandQuery  =  appandQuery + " AND ( "+appandQueryAdSize+" )";
			//System.out.println("appandQuery adSize:"+appandQuery);
		}

		//companyType
		if(companyType!=null && companyType.size() >0){
			String appandQueryCompanyType ="";
			for(int i=0;i<companyType.size();i++){
				if(i == 0){
					appandQueryCompanyType  = appandQueryCompanyType+" "+"dat.DC_ADVERTISER_TYPE ="+companyType.get(i)+"";

				}else{
					appandQueryCompanyType  = appandQueryCompanyType+" "+"  OR  dat.DC_ADVERTISER_TYPE ="+companyType.get(i)+"";
				}
			}
			appandQuery  =  appandQuery + " AND ( "+appandQueryCompanyType+" )";
			//System.out.println("appandQuery companyType:"+appandQuery);
		}
		//adType
		if(adType!=null && adType.size() >0){
			String appandQueryAdType ="";
			for(int i=0;i<adType.size();i++){
				if(i == 0 ){
					appandQueryAdType  = appandQueryAdType +" "+" dat.DC_AD_TYPE LIKE '"+adType.get(i)+"'";
				}else{
					appandQueryAdType  = appandQueryAdType +" "+"  OR  dat.DC_AD_TYPE LIKE '"+adType.get(i)+"'";
				}
			}
			appandQuery  =  appandQuery + " AND ( "+appandQueryAdType+" )";
			//System.out.println("appandQuery companyType:"+appandQuery);
		}
		// state
		if(state!=null && state.size() >0){
			String appandQueryState ="";
			for(int i=0;i<state.size();i++){
				if(i ==0){
					appandQueryState  = appandQueryState+" "+"c.DC_STATE LIKE '"+state.get(i)+"'";
				}else{
					appandQueryState  = appandQueryState+" "+" OR c.DC_STATE LIKE '"+state.get(i)+"'";
				}
			}
			appandQuery  =  appandQuery + " AND ( "+appandQueryState+" )";
			//System.out.println("appandQuery state:"+appandQuery);
		}
		// country
		if(country!=null && country.size() >0){
			String appandQueryCountry ="";
			for(int i=0;i<country.size();i++){
				if(i ==0 ){
					appandQueryCountry  = appandQueryCountry+" "+"c.DC_COUNTRY LIKE '"+country.get(i)+"'";
				}else{
					appandQueryCountry  = appandQueryCountry+" "+"  OR c.DC_COUNTRY LIKE '"+country.get(i)+"'";
				}
			}
			appandQuery  =  appandQuery + " AND ( "+appandQueryCountry+" )";
			//System.out.println("appandQuery country:"+appandQuery);
		}
		//Publication publicationTitle
		if(publicationTitle!=null && publicationTitle.size() >0){
			String appandQueryPublicationTitle ="";
			for(int i=0;i<publicationTitle.size();i++){
				if( i == 0){
					appandQueryPublicationTitle  = appandQueryPublicationTitle +" "+" pa.DC_PUBLICATION_TITLE ="+publicationTitle.get(i)+"";

				}else{
					appandQueryPublicationTitle  = appandQueryPublicationTitle +" "+"  OR pa.DC_PUBLICATION_TITLE ="+publicationTitle.get(i)+"";
				}
			}
			appandQuery  =  appandQuery + " AND ( "+appandQueryPublicationTitle+" )";
			//System.out.println("appandQuery publicationTitle:"+appandQuery);
		}
		String SQL="SELECT * FROM tbl_de_data AS dat LEFT OUTER JOIN tbl_de_company AS c ON (dat.DN_DECOMPANY_ID =c.DN_ID) " +
				"LEFT OUTER JOIN tbl_parent_image AS pa ON (pa.DN_ID=dat.DN_PARENT_IMAGE_ID) " +
				"LEFT OUTER JOIN tbl_subscriber_publication AS  sp ON (sp.Dc_publication_title=pa.Dc_publication_title) " +
				"WHERE sp.DC_subscriber="+id+" AND dat.DB_ISAPPROVED=1 "+appandQuery+"AND pa.DD_ISSUE_DATE <= sp.DD_TO_DATE and pa.DD_ISSUE_DATE >=sp.DD_FROM_DATE group by dat.DN_ID";
		//System.out.println("appandQuery publicationTitle:"+SQL);
		return getSessionFactory().getCurrentSession().createSQLQuery(SQL).addEntity(DataEntry.class).list();
	}
    /**
     * @return the searchViewReportDetails
     */
	@Override
	public List searchViewReportDetails(int id,  String fromDateValue,String toDateValue,
			String searchViewCompanyName, String searchViewPublicationSection,
			String searchViewPublicationPage, String searchViewAdSize,
			String searchViewAdOrientation, /*String searchViewCompanyType,*/ String searchViewAdType,
			String searchViewState, String searchViewCountry,
			String searchViewPublicationTitle,String searchViewJobDensity) {
		String appandQuery = "";
		String appandQueryCompanyName="";
		String appandQueryPublicationSection="";
		String appandQueryPublicationPage="";
		String appandQueryAdSize="";
		String appandQueryCompanyType ="";
		String appandQueryAdType ="";
		String appandQueryState ="";
		String appandQueryCountry ="";
		String appandQueryPublicationTitle ="";
		String appandQueryAdOrientation="";
		String appendQueryJobDensity = "";
		// ( (month(pa.DD_issue_date)>=10 AND year(pa.DD_issue_date)>=2014) AND (month(pa.DD_issue_date)>=10 AND year(pa.DD_issue_date)>=2014) ) AND (   dat.DC_AD_SIZE =0.33  ) AND (   dat.DC_ADVERTISER_TYPE ='Line' ) AND (   dat.DC_AD_TYPE LIKE 'Line' ) AND (   c.DC_STATE LIKE 'Texas' ) AND (   c.DC_COUNTRY LIKE 'United States' ) AND (   pa.DC_PUBLICATION_TITLE ='CELL (CELL)' )
		//date
		/*if(fromMonthRange!=null && !"".equals(fromMonthRange) && fromYearRange != null && !"".equals(fromYearRange) && toMonthRange!=null && !"".equals(toMonthRange) && toYearRange != null && !"".equals(toYearRange)   ){
			String appandQueryDate  = "(month(pa.DD_issue_date)>="+fromMonthRange+" AND year(pa.DD_issue_date)>="+fromYearRange+") AND (month(pa.DD_issue_date)>="+toMonthRange+" AND year(pa.DD_issue_date)>="+toYearRange+")";
			appandQuery  = " AND ( "+appandQueryDate+" )";
			//System.out.println("appandQueryDate:"+appandQuery);
		}*/
		if(fromDateValue != null && !"".equals(fromDateValue) && toDateValue != null && !"".equals(toDateValue)){
			String appandQueryDate  = "pa.DD_issue_date >= '"+fromDateValue+"' AND pa.DD_issue_date <= '"+toDateValue+"'";
			appandQuery  = " AND ( "+appandQueryDate+" )";
		}
		//company
		if(searchViewCompanyName !=null && StringUtils.isNotEmpty(searchViewCompanyName)){
			appandQueryCompanyName  = appandQueryCompanyName +" " + " dat.DN_DECOMPANY_ID = '"+searchViewCompanyName+"'";
			appandQuery  = appandQuery +" AND ( "+appandQueryCompanyName+" )";
		}

		//System.out.println("appandQuery companyName :"+appandQuery);
		//Publication Section
		if(searchViewPublicationSection !=null && StringUtils.isNotEmpty(searchViewPublicationSection)){
			appandQueryPublicationSection  = appandQueryPublicationSection+" "+"pa.DC_SECTION = '"+searchViewPublicationSection+"'";
			appandQuery  = appandQuery + " AND ( "+appandQueryPublicationSection+" )";
		}

		//System.out.println("appandQuery publicationSection:"+appandQuery);
		//Publication page
		if(searchViewPublicationPage !=null && StringUtils.isNotEmpty(searchViewPublicationPage)){

			appandQueryPublicationPage  = appandQueryPublicationPage+" "+" pa.DC_PAGE = '"+searchViewPublicationPage+"'";
			appandQuery  =  appandQuery + " AND ( "+appandQueryPublicationPage+" )";
		}
		//System.out.println("appandQuery publicationPage:"+appandQuery);
		//adSize
		if(searchViewAdSize !=null && StringUtils.isNotEmpty(searchViewAdSize)){
			appandQueryAdSize  = appandQueryAdSize+" "+" dat.DC_AD_SIZE = '"+searchViewAdSize+"'";
			appandQuery  =  appandQuery + " AND ( "+appandQueryAdSize+" )";
		}

		//System.out.println("appandQuery adSize:"+appandQuery);

		//AdOrientation
		if(searchViewAdOrientation !=null && StringUtils.isNotEmpty(searchViewAdOrientation)){
			appandQueryAdOrientation  = appandQueryAdOrientation+" "+" dat.DC_AD_ORIENTATION ='"+searchViewAdOrientation+"'";
			appandQuery  =  appandQuery + " AND ( "+appandQueryAdOrientation+" )";
		}

		if(searchViewJobDensity != null && StringUtils.isNotEmpty(searchViewJobDensity)){
			appendQueryJobDensity = appendQueryJobDensity+" "+" dat.DC_JOB_DENSITY ='"+searchViewJobDensity+"'";
			appandQuery	 =	appandQuery + " AND ( "+appendQueryJobDensity+" )";
		}
		/*// companyType
		if(searchViewCompanyType !=null && StringUtils.isNotEmpty(searchViewCompanyType)){
			appandQueryCompanyType  = appandQueryCompanyType+" "+" dat.DC_ADVERTISER_TYPE ="+searchViewCompanyType+"";
			appandQuery  =  appandQuery + " AND ( "+appandQueryCompanyType+" )";
		}*/
		//System.out.println("appandQuery companyType:"+appandQuery);
		//Publication adType
		if(searchViewAdType !=null && StringUtils.isNotEmpty(searchViewAdType)){
			appandQueryAdType  = appandQueryAdType +" "+" dat.DC_AD_TYPE LIKE '"+searchViewAdType+"'";
			appandQuery  =  appandQuery + " AND ( "+appandQueryAdType+" )";
		}

		//System.out.println("appandQuery companyType:"+appandQuery);
		//Publication state
		if(searchViewState !=null && StringUtils.isNotEmpty(searchViewState)){
			appandQueryState  = appandQueryState+" "+" c.DC_STATE LIKE '"+searchViewState+"'";
			appandQuery  =  appandQuery + " AND ( "+appandQueryState+" )";
		}

		//System.out.println("appandQuery state:"+appandQuery);
		//Publication country
		if(searchViewCountry !=null && StringUtils.isNotEmpty(searchViewCountry)){

			appandQueryCountry  = appandQueryCountry+" "+" c.DC_COUNTRY LIKE '"+searchViewCountry+"'";
			appandQuery  =  appandQuery + " AND ( "+appandQueryCountry+" )";
		}

		//System.out.println("appandQuery country:"+appandQuery);
		//Publication publicationTitle
		if(searchViewPublicationTitle !=null && StringUtils.isNotEmpty(searchViewPublicationTitle)){
			appandQueryPublicationTitle  = appandQueryPublicationTitle +" "+" pa.DC_PUBLICATION_TITLE ="+searchViewPublicationTitle+"";
		}
		appandQuery  =  appandQuery + " AND ( "+appandQueryPublicationTitle+" )";
		//System.out.println("appandQuery publicationTitle:"+appandQuery);
		String SQL="SELECT * FROM tbl_de_data AS dat LEFT OUTER JOIN tbl_de_company AS c ON (dat.DN_DECOMPANY_ID = c.DN_ID) " +
				"LEFT OUTER JOIN tbl_parent_image AS pa ON (pa.DN_ID=dat.DN_PARENT_IMAGE_ID) " +
				"LEFT OUTER JOIN tbl_subscriber_publication AS  sp ON (sp.Dc_publication_title=pa.Dc_publication_title) " +
				"WHERE sp.DC_subscriber='"+id+"' AND dat.DB_ISAPPROVED=1 "+appandQuery;
		System.out.println("appandQuery publicationTitle:"+SQL);
		return getSessionFactory().getCurrentSession().createSQLQuery(SQL).addEntity(DataEntry.class).list();
	}
    /**
     * @return the AdvertiserViewReportList
     */
	@Override
	public List getAdvertiserViewReportList(int id,
			int reportCurrentMonthFirst, int reportCurrentYearFirst,
			int reportCurrentMonthSecond, int reportCurrentYearSecond,
			int reportCurrentMonthThird, int reportCurrentYearThird,
			int reportCurrentMonthFour, int reportCurrentYearFour) {
		
		//String SQL="select pu.DC_PUBLICATION_TITLE,count((case when (month(pa.DD_issue_date)="+reportCurrentMonthFirst+"&& year(pa.DD_issue_date)="+reportCurrentYearFirst+") then pa.DD_issue_date else NULL end)),count((case when (month(pa.DD_issue_date)="+reportCurrentMonthSecond+" && year(pa.DD_issue_date)="+reportCurrentYearSecond+") then pa.DD_issue_date else NULL end)), count((case when (month(pa.DD_issue_date)="+reportCurrentMonthThird+" && year(pa.DD_issue_date)="+reportCurrentYearThird+") then pa.DD_issue_date else NULL end)),count((case when (month(pa.DD_issue_date)="+reportCurrentMonthFour+" && year(pa.DD_issue_date)="+reportCurrentYearFour+") then pa.DD_issue_date else NULL end)),d.DN_ID as de_id,pu.DN_ID as publisher_id from tbl_subscriber_publication sp Left outer join tbl_parent_image pa on sp.Dc_publication_title = pa.DC_PUBLICATION_TITLE Left Outer Join tbl_de_data d on pa.DN_ID=d.DN_Parent_Image_ID Left Outer Join tbl_publication pu on pa.DC_PUBLICATION_TITLE = pu.DN_ID where sp.DC_subscriber="+id+" and pa.DD_issue_date >= sp.DD_From_date and pa.DD_issue_date <= sp.dd_to_date and d.DB_ISAPPROVED=1 group by pa.DC_PUBLICATION_TITLE";
		String SQL="select pu.DC_PUBLICATION_TITLE,count(distinct(case when (month(pa.DD_ISSUE_DATE)="+reportCurrentMonthFirst+" && year(pa.DD_ISSUE_DATE)="+reportCurrentYearFirst+") then d.DN_ID  else NULL end)) as month1,count(distinct(case when (month(pa.DD_issue_date)="+reportCurrentMonthSecond+"&& year(pa.DD_ISSUE_DATE)="+reportCurrentYearSecond+") then d.DN_ID  else NULL end)) as month2,count(distinct(case when (month(pa.DD_issue_date)="+reportCurrentMonthThird+" && year(pa.DD_ISSUE_DATE)="+reportCurrentYearThird+") then d.DN_ID  else NULL end)) as month3,count(distinct(case when (month(pa.DD_issue_date)="+reportCurrentMonthFour+" && year(pa.DD_ISSUE_DATE)="+reportCurrentYearFour+") then d.DN_ID  else NULL end))as month4,d.DN_ID as de_id,pu.DN_ID as publisher_id from tbl_subscriber_publication sp JOIN tbl_parent_image pa on sp.DC_PUBLICATION_TITLE = pa.DC_PUBLICATION_TITLE JOIN tbl_de_data d on pa.DN_ID=d.DN_PARENT_IMAGE_ID JOIN tbl_publication pu on pa.DC_PUBLICATION_TITLE = pu.DN_ID where sp.DC_SUBSCRIBER="+id+" and pa.DD_issue_date >= sp.DD_From_date and pa.DD_ISSUE_DATE <= sp.DD_TO_DATE and d.DB_ISAPPROVED=1 group by pa.DC_PUBLICATION_TITLE";

		//System.out.println("SQL"+SQL);
		return getSessionFactory().getCurrentSession().createSQLQuery(SQL).list();
	}
    /**
     * @return the JournalViewReportList
     */
	@Override
	public List getJournalViewReportList(int id) {
		String appentQuery=""; 
		String SQL="select pu.DN_ID,pu.DC_PUBLICATION_TITLE,pu.DB_DELETED from tbl_subscriber_publication sp JOIN tbl_publication pu ON pu.DN_ID=sp.DC_PUBLICATION_TITLE where sp.DB_DELETED=0 AND sp.DC_SUBSCRIBER="+id;
		List temp= getSessionFactory().getCurrentSession().createSQLQuery(SQL).list();
		for (Iterator iterator = temp.iterator(); iterator.hasNext();) {
			Object[] obj=(Object[]) iterator.next();			
			appentQuery=appentQuery+",count((case when pa.dc_publication_title='"+obj[0]+"' then pa.dc_publication_title else NULL end))";
			
		}
		String sql="select c.DN_ID,c.DC_COMPANY_NAME as Advertiser"+appentQuery+" from tbl_subscriber_publication sp join tbl_parent_image pa on sp.Dc_publication_title=pa.Dc_publication_title Join tbl_de_data d on pa.DN_ID=d.DN_Parent_Image_ID Join tbl_de_company c on d.DN_DECOMPANY_ID =c.DN_ID where d.DB_ISAPPROVED=1 and  pa.DD_ISSUE_DATE <= sp.DD_TO_DATE and pa.DD_ISSUE_DATE >=sp.DD_FROM_DATE and sp.DC_subscriber='"+id+"' group by d.DN_DECOMPANY_ID";
		// System.out.println(sql);
		 return getSessionFactory().getCurrentSession().createSQLQuery(sql).list(); 
	}
    /**
     * @return the AdvertiserViewReportDetails
     */
	@Override
	public List getAdvertiserViewReportDetails(int id, int reportMonth,
			int reportYear, long deCompanyId) {
		String SQL="SELECT * FROM tbl_de_data AS dat LEFT OUTER JOIN tbl_de_company AS c ON (dat.DN_DECOMPANY_ID =c.DN_ID) "+
				"LEFT OUTER JOIN tbl_parent_image AS pa ON (pa.DN_ID=dat.DN_PARENT_IMAGE_ID) LEFT OUTER JOIN tbl_subscriber_publication AS  sp ON (sp.Dc_publication_title=pa.Dc_publication_title)"+
				"LEFT OUTER JOIN tbl_publication AS pu ON pa.DC_PUBLICATION_TITLE = pu.DN_ID WHERE sp.DC_subscriber="+id+" " +
				"AND pu.DN_ID="+deCompanyId+" AND MONTH(pa.DD_issue_date)="+reportMonth+" && YEAR(pa.DD_issue_date)="+reportYear+" AND dat.DB_ISAPPROVED=1 group by dat.DN_ID";
		return getSessionFactory().getCurrentSession().createSQLQuery(SQL).addEntity(DataEntry.class).list();
	}
    /**
     * @return the SubscriberJournalList
     */
	@Override
	public List getSubscriberJournalList(int id) {
		String SQL="select pu.DN_ID,pu.DC_PUBLICATION_TITLE from tbl_subscriber_publication sp JOIN tbl_publication pu ON pu.DN_ID=sp.DC_PUBLICATION_TITLE where sp.DB_DELETED=0 AND sp.DC_SUBSCRIBER="+id;
		List temp=getSessionFactory().getCurrentSession().createSQLQuery(SQL).list();
		return temp; 
	}
    /**
     * @return the JournalViewReportDetails
     */
	@Override
	public List getJournalViewReportDetails(int id, long deCompanyId,
			long deJournalId) {
		String SQL="SELECT * FROM tbl_de_data AS dat JOIN tbl_de_company AS c ON (dat.DN_DECOMPANY_ID =c.DN_ID) JOIN tbl_parent_image AS pa ON (pa.DN_ID=dat.DN_PARENT_IMAGE_ID) JOIN tbl_subscriber_publication AS  sp ON (sp.Dc_publication_title=pa.Dc_publication_title) JOIN tbl_publication AS pu ON pa.DC_PUBLICATION_TITLE = pu.DN_ID WHERE sp.DC_subscriber="+id+" AND pu.DN_ID="+deJournalId+" AND c.DN_ID="+deCompanyId+" AND dat.DB_ISAPPROVED=1 group by dat.DN_ID";
		return getSessionFactory().getCurrentSession().createSQLQuery(SQL).addEntity(DataEntry.class).list();
		
	}
    /**
     * @return the PublicationBySearchValueAdvertisertype
     */
	@Override
	public Publication getPublicationBySearchValueAdvertisertype(
			String publicationId) {
		String SQL="from Publication where isDeleted=0 and id ="+publicationId;		
		List<Publication> publicationList = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		if (publicationList.size() > 0 ) {
			return publicationList.get(0);
		}
		return null;
	}
    /**
     * @return the PublicationByPublicationTitle
     */
	@Override
	public Publication getPublicationByPublicationTitle(String publicationTitle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeCompany> getAllCompanyData() {
		String SQL = "select c.companyName,c.companyURL,c.city,c.pincode,c.state,c.country,c.address,c.address1,c.department from DeCompany  as c  ORDER BY c.companyName ASC";
		List list = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		return list;
	}

	@Override
	public List getCountriesForTerritoryBySubscriberId(long subscriberId) {
		String SQL = "from Territory  as t where t.subscriberId = "+subscriberId;
		List<Territory> territories = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		StringBuffer sb = new StringBuffer();
		for(Territory territory : territories) {
			for(Country country: territory.getCountries()) {
				sb.append(country.getCountryId()+",");
			}
		}
		String ids;
		if(!sb.toString().isEmpty()) {
			ids = sb.toString().substring(0,sb.toString().length()-1);  
			SQL = "from Country as c where c.countryId NOT IN ("+ids+")";
		}
		else
			SQL = "from Country as c";
		
		List<Country> countries = getSessionFactory().getCurrentSession().createQuery(SQL)/*.setParameter("ids", ids)*/.list();
		return countries;
	}

	@Override
	public long saveSubscriberTerritory(String countryIds, long subscriberId,
			String name) {
		String SQL = "from Country as c where c.countryId IN ("+countryIds+")";
		List<Country> countries = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		Territory territory = new Territory();
		territory.setCountries(countries);
		territory.setSubscriberId(subscriberId);
		territory.setTerritoryName(name);
		return (Long)getSessionFactory().getCurrentSession().save(territory);
	}

	@Override
	public List<Territory> getSubscriberTerritory(long subscriberId) {
		String SQL = "from Territory  as t where t.subscriberId = "+subscriberId;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}

	@Override
	public void deleteSubscriberTerritory(long territoryId) {
		String SQL = "from Territory  as t where t.id = "+territoryId;
		List<Territory> territories = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		if(!territories.isEmpty()) {
			getSessionFactory().getCurrentSession().delete(territories.get(0));
		}
	}

	@Override
	public void deleteSubscriberTerritoryCountry(long territoryId,
			long countryId) {
		String SQL = "delete from tbl_territory_tbl_country where Territory_T_ID = "+territoryId + " and countries_DN_ID = "+countryId;
		getSessionFactory().getCurrentSession().createSQLQuery(SQL).executeUpdate();
	}

	@Override
	public void addSubscriberTerritoryCountry(long territoryId,
			List<String> countryIds) {
		StringBuffer sb = new StringBuffer("insert into tbl_territory_tbl_country values ");
		for(String countryId:countryIds) {
			sb.append("(");
			sb.append(territoryId);
			sb.append(",");
			sb.append(countryId);
			sb.append("),");
		}
		String SQL = sb.toString().substring(0,sb.toString().length()-1);
		System.out.println("SQL:"+SQL);
		getSessionFactory().getCurrentSession().createSQLQuery(SQL).executeUpdate();
	}	

}
