package com.obs.brs.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="tbl_states")
public class States implements Serializable{

	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="DN_ID" , nullable = false )
	private long stateId;

	@Column(name="DC_NAME")
	private String stateName;
	
	@Column(name="DC_CODE")
	private String stateCode;

	/**
	 * 
	 * @return the stateId
	 */
	public long getStateId() {
		return stateId;
	} 

	/**
	 * 
	 * @param stateId
	 *        the stateId to set
	 */
	public void setStateId(long stateId) {
		this.stateId = stateId;
	}
    /**
     * 
     * @return the stateName
     */
	public String getStateName() {
		return stateName;
	}
    /**
     * 
     * @param stateName
     *           the stateName to set
     */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
    /**
     * 
     * @return the stateCode
     */
	public String getStateCode() {
		return stateCode;
	}
    /**
     * 
     * @param stateCode
     *          the stateCode to set
     */
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	
	
}
