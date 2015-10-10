package com.mnt.report.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class TitleMapHelper {

	public static String allDC_DEO(JdbcTemplate jt,Long subscriberId) {
		List<JSONObject> list = jt.query("select U.DN_ID AS value, U.DC_FIRSTNAME AS name  from tbl_user U WHERE U.DN_USER_TYPE = 3 order by U.DC_FIRSTNAME", new RowMapper<JSONObject>(){

			public JSONObject mapRow(ResultSet rs, int arg1)
					throws SQLException {
				JSONObject nameValue = new JSONObject();
				nameValue.put("name", rs.getObject("name").toString());
				nameValue.put("value",  rs.getObject("value").toString());
				nameValue.put("checked",  false);
				return nameValue;
			}
		});
		return JSONArray.toJSONString(list);
	}
	
	public static String allDN_DECOMPANY_ID(JdbcTemplate jt,Long subscriberId) {
    	List<JSONObject> list = jt.query("select P.DN_ID AS value, P.DC_COMPANY_NAME AS name  from tbl_de_company P order by P.DC_COMPANY_NAME", new RowMapper<JSONObject>(){

			public JSONObject mapRow(ResultSet rs, int arg1)
					throws SQLException {
				JSONObject nameValue = new JSONObject();
				nameValue.put("name", rs.getObject("name").toString());
				nameValue.put("value",  rs.getObject("value").toString());
				return nameValue;
			}
		});
		return JSONArray.toJSONString(list);
	}
	
	public static String allDC_ADVERTISER_TYPE(JdbcTemplate jt,Long subscriberId) {
    	List<JSONObject> list = jt.query("SELECT P.DN_ID AS value, P.DC_PUBLICATION_TITLE AS name FROM tbl_publication P where P.DC_PUBLICATION_TYPE = 5", new RowMapper<JSONObject>(){

			public JSONObject mapRow(ResultSet rs, int arg1)
					throws SQLException {
				JSONObject nameValue = new JSONObject();
				nameValue.put("name", rs.getObject("name").toString());
				nameValue.put("value",  rs.getObject("value").toString());
				return nameValue;
			}
		});
		return JSONArray.toJSONString(list);
	}
	
	public static String allDC_PUBLICATION_TITLE(JdbcTemplate jt,Long subscriberId) {
    	List<JSONObject> list = jt.query("SELECT P.DN_ID AS value, P.DC_PUBLICATION_TITLE AS name FROM tbl_publication P where P.DC_PUBLICATION_TYPE = 2", new RowMapper<JSONObject>(){

			public JSONObject mapRow(ResultSet rs, int arg1)
					throws SQLException {
				JSONObject nameValue = new JSONObject();
				nameValue.put("name", rs.getObject("name").toString());
				nameValue.put("value",  rs.getObject("value").toString());
				return nameValue;
			}
		});
		return JSONArray.toJSONString(list);
	}
    
	public static String allDC_AD_CATEGORY(JdbcTemplate jt,Long subscriberId) {
    	List<JSONObject> list = jt.query("SELECT P.DN_ID AS value, P.DC_PUBLICATION_TITLE AS name FROM tbl_publication P where P.DC_PUBLICATION_TYPE = 4", new RowMapper<JSONObject>(){

			public JSONObject mapRow(ResultSet rs, int arg1)
					throws SQLException {
				JSONObject nameValue = new JSONObject();
				nameValue.put("name", rs.getObject("name").toString());
				nameValue.put("value",  rs.getObject("value").toString());
				return nameValue;
			}
		});
		return JSONArray.toJSONString(list);
	}
	
	public static String allDC_SEARCH_ADVERTISER_TYPE(JdbcTemplate jt,Long subscriberId) {
		List<JSONObject> list = jt.query("SELECT P.DN_ID AS value, P.DC_PUBLICATION_TITLE AS name FROM tbl_publication P where P.DC_PUBLICATION_TYPE = 6", new RowMapper<JSONObject>(){

			public JSONObject mapRow(ResultSet rs, int arg1)
					throws SQLException {
				JSONObject nameValue = new JSONObject();
				nameValue.put("name", rs.getObject("name").toString());
				nameValue.put("value",  rs.getObject("value").toString());
				return nameValue;
			}
		});
		return JSONArray.toJSONString(list);
	}
	
	public static String allDC_SUBSCRIBER_TERRITORY(JdbcTemplate jt,Long subscriberId) {
		List<JSONObject> list = jt.query("SELECT T.T_ID AS value, T.T_NAME AS name FROM tbl_territory T where T.T_SUBSCRIBER_ID = "+subscriberId, new RowMapper<JSONObject>(){

			public JSONObject mapRow(ResultSet rs, int arg1)
					throws SQLException {
				JSONObject nameValue = new JSONObject();
				nameValue.put("name", rs.getObject("name").toString());
				nameValue.put("value",  rs.getObject("value").toString());
				return nameValue;
			}
		});
		return JSONArray.toJSONString(list);
	}
}
