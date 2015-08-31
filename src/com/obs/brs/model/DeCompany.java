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

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 
 * @author Jeevanantham
 *
 */
@Entity
@Table(name="tbl_de_company")
public class DeCompany implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String companyName;
	private String address;
	private String address1;
	private String city;
	private String state;
	private String country;
	private String department;
	@Override
	public String toString() {
		return "DeCompany [companyName=" + companyName + ", companyURL="
				+ companyURL + "]";
	}

	private String companyURL;
	private String pincode;
	private Boolean isDeleted;
	private User created_by;
	private User deleted_by;
	private Date created_On;
	private Date deleted_On;
	/**
	 * Get companyName Id
	 * 
	 * @return int - companyName Id
	 */
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="DN_ID" , nullable = false )
	public long getId() {
		return id;
	}

	/**
	 * Set companyName Id
	 * 
	 * @param int - companyName Id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the companyName
	 */
	@JsonIgnore
	@Column(name="DC_COMPANY_NAME", nullable = true)
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
	 * @return the address1
	 */
	
	/**
	 * @return the city
	 */
	@Column(name="DC_CITY", nullable = true)
	public String getCity() {
		return city;
	}
	
	@Column(name="DC_ADDRESS_LINE_2", nullable = true)
	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
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
	 * @return the department
	 */
	@Column(name="DC_DEPARTMENT", nullable = true)
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	
	/**
	 * @return the companyURL
	 */
	@Column(name="DC_COMPANYURL", nullable = true)
	public String getCompanyURL() {
		return companyURL;
	}

	/**
	 * @param companyURL the companyURL to set
	 */
	public void setCompanyURL(String companyURL) {
		this.companyURL = companyURL;
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

}
