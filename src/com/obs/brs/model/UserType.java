/**
 * 
 */
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
@Table(name="tbl_user_type")
public class UserType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String userType;
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="DN_TYPEID" , nullable = false )
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
	 * @return the roleName
	 */
	@Column(name="DC_TYPE_NAME")
	public String getUserType() {
		return userType;
	}
	/**
	 * @param roleName the roleName to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
}
