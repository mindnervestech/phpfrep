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
@Table(name="tbl_de_job")
public class DeJob implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private Boolean isDeleted;
	private User created_by;
	private User deleted_by;
	private Date created_On;
	private Date deleted_On;
	private Boolean isActive;
	private ParentImage parentImage;
	/**
	 * Get de_data Id
	 * 
	 * @return int - de_data Id
	 */
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="DN_ID" , nullable = false )
	public long getId() {
		return id;
	}

	/**
	 * Set User Id
	 * 
	 * @param int - de_data Id
	 */
	public void setId(long id) {
		this.id = id;
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
	/**
	 * @return the parentImage
	 */
	@OneToOne ( targetEntity = ParentImage.class, fetch = FetchType.EAGER )
	@JoinColumn (name = "DN_PARENT_IMAGE_ID")
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

}
