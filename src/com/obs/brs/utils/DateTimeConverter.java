/**
 * 
 */
package com.obs.brs.utils;

import java.util.TimeZone;

import org.apache.log4j.Logger;

/**
 * @author stephen
 *
 */
	public class DateTimeConverter  
		extends    javax.faces.convert.DateTimeConverter { 
		private final static Logger LOG = Logger.getLogger(DateTimeConverter.class);
		public static final String CONVERTER_ID = "com.overseas.utils.DateTimeConverter";   
		public DateTimeConverter()  { 
			setTimeZone(TimeZone.getDefault());    
			setPattern("MM/dd/yyyy");  
		}  
	}
