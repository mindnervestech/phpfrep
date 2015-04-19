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
@Table(name="tbl_subscriber_user")
public class SubscriberUser implements Serializable{

	/**
	 * Default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Subscriber subscriber;
	private Boolean isActive;
	private Boolean isDeleted;
	private int created_by;
	private int deleted_by;
	private Date created_On;
	private Date deleted_On;
	private short gender;
	private Date dateofBirth;
	private UserType userType;
	private String address;
	private String city;
	private String state;
	private String country;
	private String pincode;
	private String phoneNo;
	/**
	 * Get Subscriber User Id
	 * 
	 * @return int - Company User Id
	 */
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="DN_ID" , nullable = false )
	public int getId() {
		return id;
	}

	/**
	 * Set Subscriber User Id
	 * 
	 * @param int - Subscriber User Id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the firstName
	 */
	@Column(name="DC_FIRSTNAME", nullable = true)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	@Column(name="DC_LASTNAME", nullable = true)
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	@Column(name="DC_EMAIL", nullable = true)
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get Password
	 * 
	 * @return String - Password
	 */
	@Column(name="DC_PASSWORD", nullable = true)
	public String getPassword() {
		return password;
	}

	/**
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the subscriber
	 */
	@OneToOne ( targetEntity = Subscriber.class, fetch = FetchType.EAGER )
	@JoinColumn (name = "DN_SUBSCRIBER_ID")
	public Subscriber getSubscriber() {
		return subscriber;
	}
	/**
	 * @param subscriber the subscriber to set
	 */
	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
	/**
	 * @return the isActive
	 */
	@Column(name="DB_ACTIVE", nullable = true)
	public Boolean getIsActive() {
		return isActive;
	}	
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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
	//@OneToOne ( targetEntity = User.class, fetch = FetchType.EAGER )
	@Column (name="DN_CREATED_BY", nullable = true)
	public int getCreated_by() {
		return created_by;
	}

	/**
	 * @param created_by the created_by to set
	 */
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	/**
	 * @return the deleted_by
	 */
	@Column(name="DN_DELETED_BY", nullable = true)
	public int getDeleted_by() {
		return deleted_by;
	}

	/**
	 * @param deleted_by the deleted_by to set
	 */
	public void setDeleted_by(int deleted_by) {
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
	 * @return the gender
	 */
	@Column(name="DN_GENDER", nullable = true)
	public short getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(short gender) {
		this.gender = gender;
	}

	/**
	 * @return the dateofBirth
	 */
	@Column(name="DD_DATE_OF_BIRTH", nullable = true)
	public Date getDateofBirth() {
		return dateofBirth;
	}
	/**
	 * @param dateofBirth the dateofBirth to set
	 */
	public void setDateofBirth(Date dateofBirth) {
		this.dateofBirth = dateofBirth;
	}

	/**
	 * @return the userType
	 */
	@OneToOne ( targetEntity = UserType.class, fetch = FetchType.EAGER )
	@JoinColumn (name = "DN_USER_TYPE")
	public UserType getUserType() {
		return userType;
	}
	/**
	 * @param userType the userRole to set
	 */
	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	/**
	 * @return the address1
	 */
	@Column(name="DC_ADDRESS_LINE_1", nullable = true)
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
	@Column(name="DC_CITY", nullable = true)
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
	@Column (name = "DC_STATE")
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
	@Column(name="DC_COUNTRY", nullable = true)
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
	@Column(name="DC_ZIPCODE", nullable = true)
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
	 * @return the phoneNo 
	 * 
	 */
	@Column(name="DC_PHONE", nullable = true)
	public String getPhoneNo() {
		return phoneNo;
	}

	/**
	 * @param phoneNo the phoneNo to set
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	
	

}
