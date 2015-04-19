package com.obs.brs.utils;

public enum UserTypeEnum {

	 //ADMIN("Admin"),ORG_DIRETOR("ROLE_DIRECTOR"),ORG_MANAGER("ROLE_MANAGER"), ORG_VIEWER("ROLE_VIEWER"),VARIANT("ROLE_VARIANT");
	 ADMIN("Admin"),DATA_ENTRY("Data Entry"),QUALITY_CHECK("Quality Check"), COMPANY_USER("Company user");
	private String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	private UserTypeEnum(String value) {
		this.value = value;
	}
	 
	
}
