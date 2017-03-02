package com.obs.brs.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import com.obs.brs.service.IChildImageService;
import com.obs.brs.service.IDeService;
import com.obs.brs.service.IParentImageService;


/**
 * 
 * @author Jeevanantham
 *
 */
@Entity
@Table(name="tab_role")
public class TabRole implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	
	private long tabId;
	
	private long roleId;

	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(name="tab_id" , nullable = false )
	public long getTabId() {
		return tabId;
	}
	public void setTabId(long tabId) {
		this.tabId = tabId;
	}
	
	@Column(name="role_id" , nullable = false )
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
}
