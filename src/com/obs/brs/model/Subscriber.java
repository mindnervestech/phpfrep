package com.obs.brs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


import org.codehaus.jackson.annotate.JsonIgnore;


/**
 * 
 * @author Jeevanantham
 *
 */
@Entity
@Table(name="tbl_subscriber")
public class Subscriber implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String companyName;
	private String companyLocation;
	private String notes;
	private Boolean isActive;
	private Boolean isDeleted;
	private User created_by;
	private int deleted_by;
	private Date created_On;
	private Date deleted_On;
	
	@Transient
   private List<SubscriberUser> users = new ArrayList<SubscriberUser>();

	/**
	 * Get Subscriber Id
	 * 
	 * @return int - Subscriber Id
	 */
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="DN_ID" , nullable = false )
	public int getId() {
		return id;
	}
	
	/**
	 * Set User Id
	 * 
	 * @param int - User Id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the companyName
	 */
	@Column(name="DC_COMPANYNAME", nullable = true)
	
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
	 * @return the companyLocation
	 */
	@Column(name="DC_COMPANYLOCATION", nullable = true)
	public String getCompanyLocation() {
		return companyLocation;
	}
	/**
	 * @param companyWebsite the companyWebsite to set
	 */
	public void setCompanyLocation(String companyLocation) {
		this.companyLocation = companyLocation;
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
	 * @return the address1
	 */
	@Column(name="DC_NOTES", nullable = true)
	public String getNotes() {
		return notes;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
    /**
     * 
     * @return the Users
     */
	@Transient
	public List<SubscriberUser> getUsers() {
		return users;
	}
     /**
      * 
      * @param users
      *        the users to set
      */
	public void setUsers(List<SubscriberUser> users) {
		this.users = users;
	}
	
	

}
