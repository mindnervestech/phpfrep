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
 * @author Jeevanantham
 *
 */
@Entity
@Table(name="tbl_publication")
public class Publication implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String publicationTitle;
	private int publicationType;
	private Boolean isDeleted;
	private Date created_On;
	private Date deleted_On;
	private User created_by;
	private int deleted_by;

	/**
	 * Get region Id
	 * 
	 * @return int - region Id
	 */
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="DN_ID" , nullable = false )
	public int getId() {
		return id;
	}

	/**
	 * Set region Id
	 * 
	 * @param int - region Id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the region
	 */
	@JsonIgnore
	@Column(name="DC_PUBLICATION_TITLE", nullable = true)
	public String getPublicationTitle() {
		return publicationTitle;
	}

	/**
	 * @param region the region to set
	 */
	public void setPublicationTitle(String publicationTitle) {
		this.publicationTitle = publicationTitle;
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
	 * 
	 * @return
	 */
	@JsonIgnore
	@Column(name="DC_PUBLICATION_TYPE", nullable = true)
	public int getPublicationType() {
		return publicationType;
	}
	/**
	 * 
	 * @param publicationType
	 */
	public void setPublicationType(int publicationType) {
		this.publicationType = publicationType;
	}

}
