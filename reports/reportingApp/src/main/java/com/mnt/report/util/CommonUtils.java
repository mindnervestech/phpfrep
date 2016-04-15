package com.mnt.report.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;



@Component
public class CommonUtils {
	
	public static long getMaxId(JdbcTemplate jt, String tblName){
		long id = jt.queryForLong("select max(DN_ID) from "+tblName);
		return id;
	
	}
}