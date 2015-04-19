package com.obs.brs.utils;

import java.util.ResourceBundle;

public class CommonProperties {

	private static ResourceBundle commonBundle = getResourceBundle();

	// Bundle Names  
	public static final String COMMON = "com.obs.brs.properties.Application";

	private static ResourceBundle getResourceBundle() {
		return getResourceBundle(COMMON);
	}

	private static ResourceBundle getResourceBundle(String bundleName) {
		ResourceBundle bundle = null;
		bundle = ResourceBundle.getBundle(bundleName);
		return bundle;
	}

	/** Get Common Properties **/
	private static String getString(String key) {
		return commonBundle.getString(key);
	}

	public static String getBaseURL() {
		return getString("baseURL");
	}

	public static String getBasePath() {
		return getString("basePath");
	}
	
	public static String getContextPath() {
		return getString("contextPath");
	}
	
	public static String getImageContextPath() {
		return getString("imageContextPath");
	}
	
	public static String getTempPath() {
		return getString("tempPath");
	}
	
	public static String getProfilePhotoPath(){
		return getString("profilePhotoPath");
	}
	
	public static String getParentImagePath(){
		return getString("parentImagePath");
	}
	
	public static String getChildImagePath() {
		return getString("childImagePath");
	}
	public static String getParentImageTempPath() {
		return getString("parentImageTempPath");
	}
	
	public static String getReportPath() {
		return getString("reportPath");
	}
	
	public static String getSourcePath(){
		return getString("sourcePath");
	}
	
}

