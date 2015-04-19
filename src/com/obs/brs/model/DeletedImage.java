package com.obs.brs.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author Rameshbabu
 *
 */
@Entity
@Table(name="tbl_deleted_image")
public class DeletedImage implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long imageId;
	private String imageName;
	private long deleted_by;
	private Date deleted_On;
	private Boolean isChild;
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="DN_ID" , nullable = false )
	/**
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * 
	 * @param id
	 *      the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * 
	 * @return the imageId
	 */
	@Column(name="DN_IMAGEID" , nullable = true )
	public long getImageId() {
		return imageId;
	}
	/**
	 * 
	 * @param imageId
	 *        the imageId to set
	 */
	public void setImageId(long imageId) {
		this.imageId = imageId;
	}
	/**
	 * 
	 * @return the imageName
	 */
	@Column(name="DC_IMAGENAME", nullable = true)
	public String getImageName() {
		return imageName;
	}
	/**
	 * 
	 * @param imageName
	 *       the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	/**
	 * 
	 * @return the deleted_by
	 */
	@Column(name="DN_DELETED_BY", nullable = true)
	public long getDeleted_by() {
		return deleted_by;
	}
	/**
	 * 
	 * @param deleted_by
	 *         the deleted_by to set
	 */
	public void setDeleted_by(long deleted_by) {
		this.deleted_by = deleted_by;
	}
	/**
	 * 
	 * @return the deleted_On
	 */
	@Column(name="DD_DELETED_ON", nullable = true)
	public Date getDeleted_On() {
		return deleted_On;
	}
	/**
	 * 
	 * @param deleted_On
	 *       the deleted_On to set
	 */
	public void setDeleted_On(Date deleted_On) {
		this.deleted_On = deleted_On;
	}
	/**
	 * 
	 * @return the isChild
	 */
	@Column(name="DB_ISCHILD", nullable = true)
	public Boolean getIsChild() {
		return isChild;
	}
	/**
	 * 
	 * @param isChild
	 *         the isChild to set
	 */
	public void setIsChild(Boolean isChild) {
		this.isChild = isChild;
	}
	
	
	

}
