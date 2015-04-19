package com.obs.brs.email;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import com.obs.brs.utils.CommonProperties;
import com.obs.brs.utils.IoUtils;

public class EmailConfiguration {
	private static ResourceBundle config = getResourceBundle();

	private static final String BUNDLENAME = "com.obs.brs.properties.emailConfig";
    
	/**
	 * 
	 * @return the bundle
	 */
	private static ResourceBundle getResourceBundle() {
		ResourceBundle bundle = null;
		bundle = ResourceBundle.getBundle(BUNDLENAME);
		return bundle;
	}
    /**
     * 
     * @param key
     * @return
     */
	private static String getString(String key) {
		return config.getString(key);
	}
    /**
     * 
     * @return the isAuth
     */
	public boolean isAuth() {
		String isAuthStr = getString("isAuth");

		if("YES".equalsIgnoreCase(isAuthStr)) 
			return true;

		return false;
	}
    /**
     * 
     * @return the smtpHost
     */
	public String smtpHost() {
		return getString("smtpHost");
	}
    /**
     * 
     * @return the smtpPort
     */
	public int smtpPort() {
		String strPort = getString("smtpPort");
		return Integer.valueOf(strPort).intValue();
	}
    /**
     * 
     * @return the smtpUser
     */
	public String smtpUser() {
		return getString("smtpUser");
	}
    /**
     * 
     * @return the smtpPassword
     */
	public String smtpPassword() {
		return getString("smtpPassword");
	}

    /**
     * 
     * @return the UserServiceEmailAddress
     */
	public String getUserServiceEmailAddress() {
		return getString("userServiceEmailAddress");
	}

	//------------------------------------------------------------------
    /**
     * 
     * @return the ForgotPasswordSubject
     */
	public String getForgotPasswordSubject() {
		return getString("forgotPassword.subject");
	}
	/**
	 * 
	 * @return the ForgotUsernameSubject
	 */
	public String getForgotUsernameSubject() {
		return getString("forgotUsername.subject");
	}
	/**
	 * 
	 * @return the  NewUserSubject 
	 */
	public String getNewUserSubject() {
		return getString("newUser.subject");
	}
	/**
	 * 
	 * @return the UserRoleSubject
	 */
	public String getUserRoleSubject() {
		return getString("userRole.subject");
	}
    /**
     * 
     * @return the OrganizationCreatedSubject
     */
	public String getOrganizationCreatedSubject() {
		return getString("organizationCreated.subject");
	}
	/**
	 * 
	 * @return the PdfReportSubject
	 */
	public String getPdfReportSubject() {
		return getString("sendPdfReport.subject");
	}
	/**
	 * 
	 * @param emailAddress
	 * @param firstname
	 * @param resetlink
	 * @param URL
	 * @param logoUrl
	 * @return
	 * @throws Exception
	 */
	public String getForgotPasswordEmailMessage(String emailAddress,String firstname,String resetlink,String URL,String logoUrl) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("${username}", emailAddress);
		map.put("${firstname}", firstname);
		map.put("${resetlink}", resetlink);
		map.put("${url}", URL);
		map.put("${logoUrl}", logoUrl);
		String messageFile = config.getString("forgotPassword.fileName");
		return readFile(messageFile, map);
	}
    /**
     * 
     * @param firstname
     * @param email
     * @param url
     * @return
     * @throws Exception
     */
	public String getForgotUsernameEmailMessage(String firstname,String email,String url) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("${username}", email);
		map.put("${firstname}", firstname);
 		map.put("${url}", url);
		String messageFile = config.getString("forgotUsername.fileName");
		return readFile(messageFile, map);
	}
	/**
	 * 
	 * @param orgName
	 * @param directorName
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String getOrganizationEmailForDirector(String orgName, String directorName, String url) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("${organization_name}", orgName);
		map.put("${directorname}", directorName);
 		map.put("${url}", url);
		String messageFile = config.getString("organization_director.fileName");
		return readFile(messageFile, map);
	}
	/**
	 * 
	 * @param firstname
	 * @param userName
	 * @param pwd
	 * @param user_role
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String getNewUserMessage(String firstname, String userName, String pwd,String user_role, String url) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("${username}", userName);
		map.put("${pwd}", pwd);
		map.put("${firstname}", firstname);
 		map.put("${url}", url);
 		map.put("${user_type}", user_role);
 		//map.put("${organization_name}", org);
		String messageFile = config.getString("newUser.fileName");
		return readFile(messageFile, map);
	}
	/**
	 * 
	 * @param firstname
	 * @param userName
	 * @param pwd
	 * @param user_role
	 * @param org
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String getNewUserVariantMessage(String firstname, String userName, String pwd,String user_role, String org, String url) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("${username}", userName);
		map.put("${pwd}", pwd);
		map.put("${firstname}", firstname);
 		map.put("${url}", url);
 		map.put("${user_type}", user_role);
 		map.put("${organization_name}", org);
		String messageFile = config.getString("newVariantUser.fileName");
		return readFile(messageFile, map);
	}
	/**
	 * 
	 * @param emailContent
	 * @param senderName
	 * @param URL
	 * @param logoUrl
	 * @return
	 * @throws Exception
	 */
	public String getGeneralEmailMessage(String emailContent,String senderName, String URL, String logoUrl) throws Exception {

		HashMap<String,String> map = new HashMap<String,String>();
		map.put("${message}", emailContent);
		map.put("${sender}", senderName);
		map.put("${url}", URL);
		map.put("${logoUrl}", logoUrl);
		String messageFile = config.getString("generalEmail.fileName");
		return readFile(messageFile, map);
	}
	/**
	 * 
	 * @param receiverName
	 * @param reportName
	 * @param URL
	 * @param logoUrl
	 * @return
	 * @throws Exception
	 */
	public String getPdfReportMessage(String receiverName ,String reportName,String URL,String logoUrl) throws Exception {
		HashMap map = new HashMap();
		map.put("${receiverName}", receiverName);
		map.put("${reportName}", reportName);
		map.put("${url}", URL);
		map.put("${logoUrl}", logoUrl);
		String messageFile = config.getString("sendPdfReport.fileName");
		return readFile(messageFile, map);
	}
	/**
	 * 
	 * @param firstname
	 * @param userName
	 * @param user_role
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String getUserRoleMessage(String firstname, String userName,String user_role, String url) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("${username}", userName);
		map.put("${firstname}", firstname);
 		map.put("${url}", url);
 		map.put("${user_type}", user_role);
 		//map.put("${organization_name}", org);
		String messageFile = config.getString("userRole.fileName");
		return readFile(messageFile, map);
	}
	
	//------------------------------------------------------------------
    /**
     * 
     * @param messageFile
     * @param token
     * @param tokenValue
     * @return
     * @throws Exception
     */
	private String readFile(String messageFile, String token, String tokenValue) throws Exception {
		HashMap<String,String> tokens = new HashMap<String,String>();
		tokens.put(token, tokenValue);
		return readFile(messageFile, tokens);
	}
    /**
     * 
     * @param messageFile
     * @param tokens
     * @return
     * @throws Exception
     */
	private String readFile(String messageFile, Map tokens) throws Exception {
		String filePath = CommonProperties.getBasePath()+CommonProperties.getContextPath()+messageFile;
		try {
			String message = new IoUtils().read(filePath);
			return replaceTokens(message, tokens);
		} catch (IOException e) {
			System.out.println( "error message -"+ e.getMessage());
			throw new Exception("Serious Error. Error reading " + filePath, e);
		}
	}
    /**
     * 
     * @param message
     * @param tokens
     * @return
     */
	private String replaceTokens(String message, Map tokens) {
		for (Iterator iterator = tokens.keySet().iterator(); iterator.hasNext();) {
			String token = (String) iterator.next();
			message = StringUtils.replace(message, token, (String)tokens.get(token));
		}
		return message;
	}

}