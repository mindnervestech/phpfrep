/**
 *author Gurupandi 
 */
package com.obs.brs.utils;

import org.apache.commons.lang.StringUtils;

/**
 * @author stephen
 *
 */
public class StringUtility {

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
	
	public static boolean contains(String str1, String str2) {
		if(str1 == null ) str1 = "";
		if(str2 == null ) str2 = "";
		return StringUtils.contains(str1, str2);
	}
	
	public static boolean isNumeric(String value) {
		if(value != null) {
			value = value.trim();
			if(StringUtils.isNotEmpty(value))
				return StringUtils.isNumeric(value);
		}
		return false;
	}
}
