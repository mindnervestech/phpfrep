package com.mnt.report.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springframework.jdbc.core.RowMapper;

public class Report3 {
	public static JSONObject  main(List<RowColValue> rowColValues) {
		
		
		Set<String> xAxis = new HashSet<String>();
		Set<String> yAxis = new HashSet<String>();
		Map<String, RowColValue> valueHolder = new HashMap<String, RowColValue>();
		List<Column> column  = new ArrayList<Column>();
		column.add(new Column("Advertiser","Advertiser")); //(0,0)
		
		Map<String,Float> advertiserSum = new HashMap<String, Float>();
		Map<String,Float> publicationSum = new HashMap<String, Float>();
		
		float total = 0.0f;
		for(RowColValue _rcv : rowColValues) {
			xAxis.add(_rcv.Publication);
			yAxis.add(_rcv.Advertiser);
			Float sumP = publicationSum.get(_rcv.Publication);
			if(sumP == null) {
				publicationSum.put(_rcv.Publication,_rcv.summ);
			} else {
				publicationSum.put(_rcv.Publication,_rcv.summ + sumP);
			}
			
			Float sumA = advertiserSum.get(_rcv.Advertiser);
			if(sumA == null) {
				advertiserSum.put(_rcv.Advertiser,_rcv.summ);
			} else {
				advertiserSum.put(_rcv.Advertiser,_rcv.summ + sumA);
			}
			total = total+_rcv.summ;
			
			valueHolder.put(_rcv.Publication + _rcv.Advertiser , _rcv);
		}
		
		for(String _p : xAxis) {
			column.add(new Column(_p,_p));
		}
		column.add(new Column("Total","Total")); //(0,xAxis.length)
		
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		Map<String,Object> sumRowh  = new HashMap<String,Object>();
		Map<String,Object> sumRowf  = new HashMap<String,Object>();
		sumRowh.put("Advertiser", new String[]{" Total","TODO"});//(1,0)
		sumRowh.put("Total", new String[]{" "+total,"TODO"});//()
		sumRowf.put("Advertiser", new String[]{"~Total","TODO"});//(1,0)
		sumRowf.put("Total", new String[]{"~","TODO"});//()
		for(String xStr : xAxis) {
			Float sum = publicationSum.get(xStr);
			if(sum != null) {
				sumRowh.put(xStr, new String[]{" " + sum,"TODO"});
				sumRowf.put(xStr, new String[]{"~" + sum,"TODO"});
			}
		}
		rows.add(sumRowh); // Adding row for sum of Publication (1,1...xAxis.length)
		//rows.add(sumRowf);		
		for(String yStr : yAxis) {
			Map<String,Object> row  = new HashMap<String,Object>();
			row.put("Advertiser", new String[]{yStr,"TODO"});
			for(String xStr : xAxis) {
				RowColValue value = valueHolder.get(xStr + yStr);
				if(value == null) {
					row.put(xStr, new String[]{"","TODO"});
				} else {
					;
					row.put(xStr, new String[]{" "+value.summ,value.ids});
					//row.put(xStr+"ids", value.ids);
				}
				
			}
			row.put("Total", new String[]{" "+advertiserSum.get(yStr),"TODO"});//(xAxis.length, 1...yStr.length)
			rows.add(row);
		}
		
		JSONObject response = new JSONObject();
		response.put("columns", column);
		response.put("data", rows);
		return response;
		
		
	}
	
	public static class RowColValue implements RowMapper<RowColValue>{
		String ids;
		String Advertiser;
		String Publication;
		Float summ;
		String adUnit;
		public RowColValue() {
			
		}
		public RowColValue(String adUnit) {
			this.adUnit = adUnit;
		}
		public String getIds() {
			return ids;
		}
		public void setIds(String ids) {
			this.ids = ids;
		}
		public String getAdvertiser() {
			return Advertiser;
		}
		public void setAdvertiser(String advertiser) {
			Advertiser = advertiser;
		}
		public String getPublication() {
			return Publication;
		}
		public void setPublication(String publication) {
			Publication = publication;
		}
		public Float getSumm() {
			return summ;
		}
		public void setSumm(Float summ) {
			this.summ = summ;
		}
		
		public RowColValue mapRow(ResultSet rs, int rowNum) throws SQLException {
			RowColValue colValue = new RowColValue();
			colValue.setAdvertiser(rs.getString("Advertiser"));
			colValue.setPublication(rs.getString("Publication"));
			colValue.setSumm(rs.getFloat(adUnit));
			colValue.setIds(rs.getString("ids"));
			return colValue;
		}
	}
	
	public static class Column {
		public String data;
		public String name;
		public String width;
		public String link="report3";
		public Column(String data, String name) {
			super();
			this.data = data;
			this.name = name;
			if(name.equalsIgnoreCase("advertiser")) {
				width = "50px";
			}
		}
		
	}
}
