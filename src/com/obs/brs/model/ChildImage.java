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
import javax.persistence.Transient;

/**
 * 
 * @author Jeevanantham
 *
 */
/**
 * @author Administrator
 *
 */
@Entity
@Table(name="tbl_child_image")
public class ChildImage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String imageName;
	private ParentImage parentImage;
	private User created_by;
	private User deleted_by;
	private Date createdOn;
	private Date deletedOn;
	private String imageHeight;
	private String imageWidth;
	@Transient
	private String isCompleted;
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
	 * @return the name
	 */
	@Column(name="DC_IMAGENAME", nullable = true)
	public String getImageName() {
		return imageName;
	}
	/**
	 * @param url the url to set
	 */	
	public void setImageName(String imageName) {
		this.imageName = imageName;
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
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param created_On the created_On to set
	 */
	public void setCreatedOn(Date created_On) {
		this.createdOn = created_On;
	}

	/**
	 * @return the deleted_On
	 */
	@Column(name="DD_DELETED_ON", nullable = true)
	public Date getDeletedOn() {
		return deletedOn;
	}

	/**
	 * @param deleted_On the deleted_On to set
	 */
	public void setDeletedOn(Date deleted_On) {
		this.deletedOn = deleted_On;
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
	 * 
	 * @return the imageHeight
	 */
	@Column(name="DC_HEIGHT", nullable = true)
	public String getImageHeight() {
		return imageHeight;
	}
	/**
	 * 
	 * @param imageHeight
	 *         the imageHeight to set
	 */
	public void setImageHeight(String imageHeight) {
		this.imageHeight = imageHeight;
	}
	/**
	 * 
	 * @return the imageWidth
	 */
	@Column(name="DC_WIDTH", nullable = true)
	public String getImageWidth() {
		return imageWidth;
	}
	/**
	 * 
	 * @param imageWidth
	 *        the imageWidth to set
	 */
	public void setImageWidth(String imageWidth) {
		this.imageWidth = imageWidth;
	}
	public String getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}
	
	
}
