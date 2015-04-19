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
@Table(name="tbl_subscriber_publication")
public class SubscriberPublication implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private Publication publicationTitle;
	private Date fromDate;
	private Date toDate;
	private Subscriber subscriber;
	private Boolean isDeleted;
	private int created_by;
	private int deleted_by;
	private Date created_On;
	private Date deleted_On;
	
	
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
	 * @return the publicationTitle
	 */
	@OneToOne ( targetEntity = Publication.class, fetch = FetchType.EAGER )
	@JoinColumn(name="DC_PUBLICATION_TITLE", nullable = true)
	public Publication getPublicationTitle() {
		return publicationTitle;
	}
	/**
	 * @param publicationTitle the publicationTitle to set
	 */
	public void setPublicationTitle(Publication publicationTitle) {
		this.publicationTitle = publicationTitle;
	}
    /**
     * 
     * @return the FromDate
     */
	@Column(name="DD_FROM_DATE", nullable = true)
	public Date getFromDate() {
		return fromDate;
	}
    /**
     * 
     * @param fromDate
     *          the fromDate to set
     */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	/**
	 * 
	 * @return ToDate
	 */
	@Column(name="DD_TO_DATE", nullable = true)
	public Date getToDate() {
		return toDate;
	}
    /**
     * 
     * @param toDate
     *          the toDate to set
     */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	/**
	 * @return the subscriberUser
	 */
	@OneToOne ( targetEntity = Subscriber.class, fetch = FetchType.EAGER )
	@JoinColumn(name="DC_SUBSCRIBER", nullable = true)
	public Subscriber getSubscriber() {
		return subscriber;
	}
	/**
	 * @param subscriberUser the subscriberUser to set
	 */
	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
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
}
