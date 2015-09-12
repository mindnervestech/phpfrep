package com.obs.brs.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="tbl_territory")
public class Territory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String territoryName;
	private long subscriberId;
	
	private List<Country> countries;

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="T_ID" , nullable = false )
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name="T_NAME" , nullable = false )
	public String getTerritoryName() {
		return territoryName;
	}

	public void setTerritoryName(String territoryName) {
		this.territoryName = territoryName;
	}

	@Column(name="T_SUBSCRIBER_ID" , nullable = false )
	public long getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(long subscriberId) {
		this.subscriberId = subscriberId;
	}
	
	@ManyToMany(fetch = FetchType.EAGER )
	@ElementCollection(targetClass=Country.class)
	@Column(name = "COUNTRY_ID")
	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}
}
