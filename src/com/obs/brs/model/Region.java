package com.obs.brs.model;

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
@Table(name="tbl_region")
public class Region {

	private int id;
	private String region;
	private Country country;
	private Boolean isDeleted;
	private SubscriberUser created_by;
	private SubscriberUser deleted_by;
	private Date created_On;
	private Date deleted_On;
	private Subscriber subscriber;
	
	@Transient
	   private long countOfRegions;
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
	@Column(name="DC_REGION", nullable = true)
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the country
	 */
	@OneToOne ( targetEntity = Country.class, fetch = FetchType.EAGER )
	@JoinColumn(name="DC_COUNTRY", nullable = true)
	public Country getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(Country country) {
		this.country = country;
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
	@OneToOne ( targetEntity = SubscriberUser.class, fetch = FetchType.EAGER )
	@JoinColumn(name="DN_CREATED_BY", nullable = true)
	public SubscriberUser getCreated_by() {
		return created_by;
	}

	/**
	 * @param created_by the created_by to set
	 */
	public void setCreated_by(SubscriberUser created_by) {
		this.created_by = created_by;
	}
	/**
	 * @return the deleted_by
	 */
	@OneToOne ( targetEntity = SubscriberUser.class, fetch = FetchType.EAGER )
	@JoinColumn(name="DN_DELETED_BY", nullable = true)
	public SubscriberUser getDeleted_by() {
		return deleted_by;
	}

	/**
	 * @param deleted_by the deleted_by to set
	 */
	public void setDeleted_by(SubscriberUser deleted_by) {
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
	 * @return the deleted_by
	 */
	@OneToOne ( targetEntity = Subscriber.class, fetch = FetchType.EAGER )
	@JoinColumn(name="DN_SUBSCRIBER", nullable = true)
	public Subscriber getSubscriber() {
		return subscriber;
	}

	/**
	 * @param deleted_by the deleted_by to set
	 */
	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
	/**
	 * 
	 * @return the countOfRegions
	 */
	@Transient
	public long getCountOfRegions() {
		return countOfRegions;
	}
    /**
     * 
     * @param countOfRegions
     *           the countOfRegions to set
     */
	public void setCountOfRegions(long countOfRegions) {
		this.countOfRegions = countOfRegions;
	}
	
	

}
