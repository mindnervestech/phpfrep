/**
 * 
 */
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
 * Settings Entity
 * 
 * @author Jeevanatham
 *
 */
@Entity
@Table(name="tbl_setting")
public class Settings implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private UserType userType_id;
	private Boolean user;
	private Boolean subscriber;
	private Boolean configurations;
	private Boolean job;
	private Boolean imageUpload;
	private Boolean parentImage;
	private Boolean qcjob;
	private Boolean manage_company;

	private int created_by;
	private int deleted_by;
	private Date created_On;
	private Date deleted_On;
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="DN_ID" , nullable = false )
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	 
	/**
	 * @return the userType_id
	 */
	@OneToOne ( targetEntity = UserType.class, fetch = FetchType.EAGER )
	@JoinColumn (name = "DN_USERTYPE_ID")
	public UserType getUserType_id() {
		return userType_id;
	}
	/**
	 * @param userType_id the userType_id to set
	 */
	public void setUserType_id(UserType userType_id) {
		this.userType_id = userType_id;
	}
		/**
	 * @return the user
	 */
	@Column(name="DB_USER", nullable = true)
	public Boolean getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(Boolean user) {
		this.user = user;
	}
	/**
	 * @return the Subscriber
	 */
	@Column(name="DB_SUBSCRIBER", nullable = true)
	public Boolean getSubscriber() {
		return subscriber;
	}
	/**
	 * @param Subscriber the Subscriber to set
	 */
	public void setSubscriber(Boolean subscriber) {
		this.subscriber = subscriber;
	}
	/**
	 * @return the configurations
	 */
	@Column(name="DB_CONFIGURATION", nullable = true)
	public Boolean getConfigurations() {
		return configurations;
	}
	/**
	 * @param configurations the configurations to set
	 */
	public void setConfigurations(Boolean configurations) {
		this.configurations = configurations;
	}
	/**
	 * @return the active
	 */
	@Column(name="DB_JOB", nullable = true)
	public Boolean getJob() {
		return job;
	}
	/**
	 * @param job the job to set
	 */
	public void setJob(Boolean job) {
		this.job = job;
	}
	/**
	 * @return the imageUpload
	 */
	@Column(name="DB_IMAGE_UPLOAD", nullable = true)
	public Boolean getImageUpload() {
		return imageUpload;
	}
	/**
	 * @param imageUpload the imageUpload to set
	 */
	public void setImageUpload(Boolean imageUpload) {
		this.imageUpload = imageUpload;
	}
	/**
	 * @return the parentImage
	 */
	@Column(name="DB_PARENT_IMAGE", nullable = true)
	public Boolean getParentImage() {
		return parentImage;
	}
	/**
	 * @param parentImage the parentImage to set
	 */
	public void setParentImage(Boolean parentImage) {
		this.parentImage = parentImage;
	}
	/**
	 * @return the publish  
	 */
	@Column(name="DB_QCJOB", nullable = true)
	public Boolean getQcjob() {
		return qcjob;
	} 
	/**
	 * @param publish the publish to set
	 */
	public void setQcjob(Boolean qcjob) {
		this.qcjob = qcjob;
	}
	/**
	 * @return the created_by
	 */
	@Column(name="DN_CREATED_BY", nullable = true)
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
	
	
	@Column(name="DD_MANAGE_COMPANY", nullable = true)
	public Boolean getManage_company() {
		return manage_company;
	}
	
	public void setManage_company(Boolean manage_company) {
		this.manage_company = manage_company;
	}

	
}
