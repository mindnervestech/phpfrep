package com.obs.brs.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jeevanantham
 *
 */
@Entity
@Table(name="tbl_country")
public class Country implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="DN_ID" , nullable = false )
	private long countryId;

	@Column(name="DC_COUNTRY_DESCRIPTION")
	private String countryName;
	
	@Column(name="DC_COUNTRY_CODE")
	private String countryCode;
	
	/**
	 * 
	 * @return the countryId
	 */
	public long getCountryId() {
		return countryId;
	}
    /**
     * 
     * @param countryId
     *         the countryId to set
     */
	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}
    /**
     * 
     * @return the countryCode
     */
	public String getCountryCode() {
		return countryCode;
	}
    /**
     * 
     * @param countryCode
     *          the countryCode to set
     */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
    /**
     * 
     * @return the countryName
     */
	public String getCountryName() {
		return countryName;
	}
    /**
     * 
     * @param countryName
     *        the countryName to set
     */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
}
