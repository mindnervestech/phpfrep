package com.obs.brs.utils;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;


import org.apache.log4j.Logger;

public class CommonUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Logger Instantiated for Log Management
	private static final Logger log					= Logger.getLogger(CommonUtils.class);


	private static CommonUtils myInstance = null;
	public static CommonUtils getInstance()
	{
		if(myInstance == null)
			myInstance = new CommonUtils();
		return myInstance;
	}

	//generate random pwd
	//pwd contains first 4 digit of input string with 4 random numbers 
	public String generatePwd(String username)
	{
		String unameSub = username.substring(0, username.length()>4?3:username.length());
		return unameSub+String.valueOf ((int)(Math.random ()*10000));
	}
	
	//encrypting MD5 hash method
	public String generateEncryptedPwd(String pwd){
		try {
			MessageDigest md5;
			md5 = MessageDigest.getInstance ( "MD5" );
			md5.update ( pwd.getBytes () );
			BigInteger hash = new BigInteger ( 1, md5.digest () );
			return hash.toString ( 16 ) ;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			log.error("Error While generateEncryptedPwd:"+e);
			return null;
		}

	}

	//round to 2 decimals
	public static String roundTwoDecimals(double d) {
		//to round of the double value
		long l = Math.round(d);
		d = Long.valueOf(l).doubleValue();
		//end to round
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		
		return twoDForm.format(d);
	}
	
	public static boolean isNotEmpty(String value) {
		if(value != null) value = value.trim();
		return StringUtils.isNotEmpty(value);
	}
	
	public static String fixedValue(String value){
		StringBuffer fixedValue = new StringBuffer((int) (value.length()* 1.1));
 	for (int i=0; i < value.length(); i++) {
 		char c = value.charAt(i);
 		if (c == '\'')
 		fixedValue.append("''");
 		else
 		fixedValue.append(c);
 	}
 	return fixedValue.toString();
	}
	
	public static String getQuestionType(int type){
		String restult="";
		switch (type) {
		case 0:
			restult = "Descriptive Type";
			break;
		case 1:
			restult = "Multiple Choice Single Answer( MCSA )";
			break;
		case 2:
			restult = "Rating Type";
			break;
			
		default:
			break;
		}
		return restult;
	}
	
	public static String getUserRoleType(int type){
		String restult="";
		switch (type) {
		case 1:
			restult = "DIRECTOR";
			break;
		case 2:
			restult = "MANAGER";
			break;
		case 3:
			restult = "VIEWER";
			break;
		case 4:
			restult = "VARIANT";
			break;	
		default:
			break;
		}
		return restult;
	}
	
	public static String getAccessLinkByNumeric(int type){
		String restult="";
		switch (type) {
		case 1:
			restult = "Added New";
			break;
		case 2:
			restult = "Edited";
			break;
		case 3:
			restult = "Deleted";
			break;
		case 4:
			restult = "Activated";
			break;
		case 5:
			restult = "Deactivated";
			break;
		case 6:
			restult = "Viewed";
			break;
		case 7:
			restult = "Viewed Report";
			break;
		case 8:
			restult = "Viewed Analytics";
			break;
		case 9:
			restult = "Generated PDF";
			break;
		default:
			break;
		}
		return restult;
	}
	
}
