package com.mnt.report.service;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONObject;
import org.springframework.jdbc.core.RowMapper;

public class Report1 {
	private static final String Y = "Publication";
	private static final String X = "Dates";
	public static String Y_VALUE = "";
	
	public static JSONObject  main(List<RowColValue> rowColValues) {
		
		//Collections.sort(rowColValues);
		Set<String> xAxis = new TreeSet<String>(new Comparator<String>() {

			public int compare(String o1, String o2) {
				Date d1;
				try {
					d1 = sdf.parse("01-"+o1);
					Date d2 = sdf.parse("01-"+o2);
					return d1.compareTo(d2);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
		Set<String> yAxis = new HashSet<String>();
		Map<String, RowColValue> valueHolder = new HashMap<String, RowColValue>();
		List<Column> column  = new ArrayList<Column>();
		column.add(new Column(Y,Y_VALUE)); //(0,0)
		
		Map<String,Float> xSum = new HashMap<String, Float>();
		Map<String,Float> ySum = new HashMap<String, Float>();
		
		float total = 0.0f;
		for(RowColValue _rcv : rowColValues) {
			xAxis.add(_rcv.x);
			yAxis.add(_rcv.y);
			Float sumP = ySum.get(_rcv.y);
			if(sumP == null) {
				ySum.put(_rcv.y,_rcv.summ);
			} else {
				ySum.put(_rcv.y,_rcv.summ + sumP);
			}
			
			Float sumA = xSum.get(_rcv.x);
			if(sumA == null) {
				xSum.put(_rcv.x,_rcv.summ);
			} else {
				xSum.put(_rcv.x,_rcv.summ + sumA);
			}
			total = total+_rcv.summ;
			
			valueHolder.put( _rcv.x + _rcv.y  , _rcv);
		}
		
		for(String _p : xAxis) {
			column.add(new Column(_p,_p));
		}
		column.add(new Column("Total","Total")); //(0,xAxis.length)
		
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		Map<String,Object> sumRowh  = new HashMap<String,Object>();
		Map<String,Object> sumRowf  = new HashMap<String,Object>();
		sumRowh.put(Y, new String[]{" Total","TODO"});//(1,0)
		sumRowh.put("Total", new String[]{" "+ df.format(total),"TODO"});//()
		//sumRowf.put(Y, new String[]{"~Total","TODO"});//(1,0)
		//sumRowf.put("Total", new String[]{"~","TODO"});//()
		for(String xStr : xAxis) {
			Float sum = xSum.get(xStr);
			if(sum != null) {
				sumRowh.put(xStr, new String[]{" " + df.format(sum),"TODO"});
				//sumRowf.put(xStr, new String[]{"~" + sum,"TODO"});
			}
		}
		rows.add(sumRowh); // Adding row for sum of Publication (1,1...xAxis.length)
		//rows.add(sumRowf);		
		for(String yStr : yAxis) {
			Map<String,Object> row  = new HashMap<String,Object>();
			row.put(Y, new String[]{yStr,"TODO"});
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
			row.put("Total", new String[]{" "+df.format(ySum.get(yStr)),"TODO"});//(xAxis.length, 1...yStr.length)
			rows.add(row);
		}
		
		JSONObject response = new JSONObject();
		response.put("columns", column);
		response.put("data", rows);
		return response;
		
		
	}
	
	public static class RowColValue implements RowMapper<RowColValue>{
		
		String ids;
		String x;
		String y;
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
		public String getDate() {
			return x;
		}
		public void setDate(String date) {
			this.x = date;
		}
		public String getPublication() {
			return y;
		}
		public void setPublication(String publication) {
			y = publication;
		}
		public Float getSumm() {
			return summ;
		}
		public void setSumm(Float summ) {
			this.summ = summ;
		}
		
		public RowColValue mapRow(ResultSet rs, int rowNum) throws SQLException {
			RowColValue colValue = new RowColValue();
			colValue.setDate(rs.getString(X));
			colValue.setPublication(rs.getString(Y));
			
			colValue.setSumm(Float.valueOf(df.format(rs.getFloat(adUnit))));
			colValue.setIds(rs.getString("ids"));
			return colValue;
		}
		
	}
	static DecimalFormat df = new DecimalFormat("#.##");
	static {
	df.setRoundingMode(RoundingMode.CEILING);
	}
	static SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMM-yyyy");
	
	public static class Column {
		public String data;
		public String name;
		public String width;
		public String link="report1";
		public Column(String data, String name) {
			super();
			this.data = data;
			this.name = name;
			if(name.equalsIgnoreCase(Y)) {
				width = "50px";
			}
		}
		
	}
}
