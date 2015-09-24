package com.mnt.report.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.ws.RequestWrapper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mnt.report.service.Report3.RowColValue;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

@Controller
public class ReportMDService {
	
	@Autowired
    private JdbcTemplate jt;
	
	@Autowired
	NamedParameterJdbcTemplate namedJdbcTemplate;

	private static Client client;
	
	static {
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "fracts").build();
		TransportClient transportClient = new TransportClient(settings);
		transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
		client  = (Client)transportClient;
	}
	
	@RequestMapping(value="/searchDataEntry",method=RequestMethod.GET)
	@ResponseBody
	public List<Long> searchDataEntry(@RequestParam("query") String query) {
		System.out.println("query:"+query);
		List<Long> ids = new ArrayList<Long>();
		SearchResponse response = client.prepareSearch("fracts").setTypes("DataEntry").setQuery(QueryBuilders.queryString(query)
				.defaultField("ocrText")).execute().actionGet();
		 for (SearchHit hit : response.getHits().getHits()) {
		        Long id = Long.parseLong(hit.getId());
		        ids.add(id);
			 System.out.println("id:"+id);//Handle the hit...
		    }

		return ids;
	}
	
	@RequestMapping(value="/addUnProcessedOcr",method=RequestMethod.GET)
	@ResponseBody 
	public String addUnProcessedOcr() {
		try {
		
			List<DataEntryVM> unProcessedList =  jt.query("Select DN_ID as 'id',DC_OCR_TEXT as 'ocrText' from tbl_de_data where DB_IS_PROCESSED_OCR = 0", new RowMapper<DataEntryVM>(){
				
				public DataEntryVM mapRow(ResultSet arg0, int arg1)
						throws SQLException {
					try {
						DataEntryVM dataEntryVM = new DataEntryVM();
						dataEntryVM.id = (arg0.getLong("id"));
						dataEntryVM.ocrText = (arg0.getString("ocrText"));
						return dataEntryVM;
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
					
				}
				
			});
			KeyHolder keyHolder=new GeneratedKeyHolder();
			for(final DataEntryVM vm :unProcessedList) {
				IndexResponse response = client.prepareIndex("fracts", "DataEntry",vm.id+"")
						.setSource(XContentFactory.jsonBuilder()
							    .startObject()
						        .field("ocrText", vm.ocrText)
						    .endObject())
						.execute()
						.actionGet();
				try {
					jt.update(new PreparedStatementCreator(){
						public PreparedStatement createPreparedStatement(    Connection connection) throws SQLException {
							PreparedStatement ps=connection.prepareStatement("update tbl_de_data set DB_IS_PROCESSED_OCR = 1 where DN_ID=?",new String[]{"id"});
							int index=1;
							ps.setLong(index++,vm.id);
							return ps;
						}
					}
					,keyHolder);
				} catch (Exception e) {
					e.printStackTrace();
					return "error";
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "success";
	}
	
	@RequestMapping(value="/updateDEOcr",method=RequestMethod.GET)
	@ResponseBody
	public String updateDEOcr(@RequestParam("id")Long id) {
		try {
			
			Map<String,Object> mdResult = jt.queryForMap("Select DC_OCR_TEXT as 'ocrText' from tbl_de_data where DN_ID =" + id);
			IndexResponse response = client.prepareIndex("fracts", "DataEntry",id+"")
					.setSource(XContentFactory.jsonBuilder()
						.startObject()
						.field("ocrText", mdResult.get("ocrText"))
						.endObject())
					.execute()
					.actionGet();
		} catch(Exception e) {
			return "error";
		}
		return "success";
	}
	
	@RequestMapping(value="/deleteDEOcr",method=RequestMethod.GET)
	@ResponseBody
	public String deleteDEOcr(@RequestParam("id") Long id) {
		try {
			client.prepareDelete("fracts", "DataEntry", id+"")
	        .execute()
	        .actionGet();
		} catch(Exception e) {
			return "error";
		}
		return "success";
	}
	
	@RequestMapping(value="/reports/drildownreport",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject drildownreport(@RequestBody String report) {
		JSONObject resp = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject)parser.parse(report);
			System.out.println("id:"+((JSONObject)jsonObject.get("searchCriteria")).get("id"));
			final Map<String,Object> mdResult = jt.queryForMap("Select columns,query,hiddenpivotcol from reportmd where id =" + ((JSONObject)jsonObject.get("searchCriteria")).get("id"));
			String columns = mdResult.get("columns").toString();
			JSONArray colArr = ((JSONArray)parser.parse(columns));
			JSONObject filterObj = (JSONObject)jsonObject.get("filters");
			String drilFilter = "";
			for(String filterKey:((Set<String>)filterObj.keySet())) {
				for(Object column :colArr) {
					JSONObject colObj = (JSONObject)column;
					if(colObj.get("data").equals(filterKey)) {
						if(drilFilter.isEmpty()) 
							drilFilter += colObj.get("column")+"='"+filterObj.get(filterKey)+"' ";
						else 
							drilFilter += "and "+ colObj.get("column")+"='"+filterObj.get(filterKey)+"' ";
					}
				}
			}
			System.out.println("drilFilter:"+drilFilter);
			String query = mdResult.get("query").toString();	
			String[] namedParameters =  query.split(":");
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			// This is imp to set all named params to null 
			for(String param : namedParameters) {
				for(int i = 0 ; i < param.length() ; i++) {
					if(param.charAt(i) == ' ' || param.charAt(i) == ')' || param.charAt(i) == '\n' || param.charAt(i) == '\t'|| param.charAt(i) == '\r') {
						parameters.put(param.substring(0, i), null);
						break;
					}
				}
			}
			
			for(Object key : ((JSONObject)jsonObject.get("searchCriteria")).keySet()){
				Object value = ((JSONObject)jsonObject.get("searchCriteria")).get(key);
				if (value instanceof String) {
					if(!value.equals(""))
						parameters.put(key.toString(), value);
				}
				
				if (value instanceof JSONArray) {
					JSONArray jsonArray1 = (JSONArray) value;
				    int len = jsonArray1.size();
				    if(len > 0) {
				      List<String> inValues = new ArrayList<String>();
				      for (int i=0;i<len;i++){ 
				    	inValues.add(jsonArray1.get(i).toString());
				      }
				      parameters.put(key.toString()+"in", inValues);
				      parameters.put(key.toString(), "");
				    }
				}
			}
			if(query.indexOf("and")!=-1 || query.indexOf("AND")!=-1) {
				query +=" and "+drilFilter;
			} else {
				query += drilFilter;
			}
			System.out.println("final query:"+query);
			List<Map<String, Object>> rs = namedJdbcTemplate.queryForList(query,parameters);
			resp.put("data" , rs);
			 
			JSONArray columns1 = ((JSONArray)new JSONParser().parse(mdResult.get("columns").toString()));
			resp.put("columns" , columns1);
			
			JSONArray hiddenpivotcol = ((JSONArray)new JSONParser().parse(mdResult.get("hiddenpivotcol").toString()));
			resp.put("hiddenpivotcol",hiddenpivotcol);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	@RequestMapping(value="/report/saveTemplate",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject saveTemplate(@RequestBody final ReportTemplateVM reportTemplateVM) {
		JSONObject resp = new JSONObject();
		try {
			final Map<String,Object> mdResult = jt.queryForMap("Select query,columns,hiddenpivotcol,jsonForm,jsonSchema from reportmd where id =" + reportTemplateVM.parentId);//.query("Select query from reportmd where id = ?",new Object[]{id},);		
			KeyHolder keyHolder=new GeneratedKeyHolder();
			jt.update(new PreparedStatementCreator(){
		    public PreparedStatement createPreparedStatement(    Connection connection) throws SQLException {
		      PreparedStatement ps=connection.prepareStatement("Insert into reportmd(columns,description,jsonForm,jsonSchema,name,query,hiddenpivotcol,pivotConfig,searchCriteria) values(?,?,?,?,?,?,?,?,?)",new String[]{"id"});
		      int index=1;
		      ps.setString(index++,mdResult.get("columns").toString());
		      if(reportTemplateVM.data==null)
		    	  ps.setString(index++,"Saved table template");
		      else
		    	  ps.setString(index++,"Saved pivot template");
		      ps.setString(index++,mdResult.get("jsonForm").toString());
		      ps.setString(index++,mdResult.get("jsonSchema").toString());
		      ps.setString(index++,reportTemplateVM.templateName);
		      ps.setString(index++,mdResult.get("query").toString());
		      ps.setString(index++,mdResult.get("hiddenpivotcol").toString());
		      if(reportTemplateVM.data==null)
		    	  ps.setNull(index++, java.sql.Types.NULL);
		      else
		    	  ps.setString(index++,reportTemplateVM.data);
		      ps.setString(index++,reportTemplateVM.searchCriteria);
		      return ps;
		    }
		  }
		,keyHolder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	
    @RequestMapping(value="/report/run",method=RequestMethod.GET)
    @ResponseBody
    public JSONObject runReports(@RequestParam String filter) {
    	JSONObject resp = new JSONObject();
		
    	try {
			JSONObject jsonObject = (JSONObject)new JSONParser().parse(filter);
			Long id = Long.parseLong(jsonObject.get("id").toString());
			Map<String,Object> mdResult = jt.queryForMap("Select query,columns,hiddenpivotcol,isJava from reportmd where id =" + id);//.query("Select query from reportmd where id = ?",new Object[]{id},);
			String query = mdResult.get("query").toString();	
			String[] namedParameters =  query.split(":");
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			// This is imp to set all named params to null 
			for(String param : namedParameters) {
				for(int i = 0 ; i < param.length() ; i++) {
					if(param.charAt(i) == ' ' || param.charAt(i) == ')' || param.charAt(i) == '\n' || param.charAt(i) == '\t'|| param.charAt(i) == '\r') {
						parameters.put(param.substring(0, i), null);
						break;
					}
				}
			}
			
			for(Object key : jsonObject.keySet()){
				Object value = jsonObject.get(key);
				if (value instanceof String) {
					if(!value.equals(""))
						parameters.put(key.toString(), value);
				}
				if (value instanceof Long || value instanceof Integer || value instanceof Double || value instanceof Float) {
					parameters.put(key.toString(), value);
				}
				
				if (value instanceof JSONArray) {
					JSONArray jsonArray = (JSONArray) value;
				    int len = jsonArray.size();
				    if(len > 0) {
				      List<String> inValues = new ArrayList<String>();
				      for (int i=0;i<len;i++){ 
				    	inValues.add(jsonArray.get(i).toString());
				      }
				      parameters.put(key.toString()+"in", inValues);
				      parameters.put(key.toString(), "");
				    }
				}
			}
				Object java = mdResult.get("isJava");
				if(java != null) {
					String adUnit = jsonObject.get("DC_AD_UNIT").toString();
					if(java.toString().equalsIgnoreCase("report3")) {
						List<Report3.RowColValue> rs = namedJdbcTemplate.query(mdResult.get("query").toString(),
								parameters,new Report3.RowColValue(adUnit));
						return Report3.main(rs);
					}
					if(java.toString().equalsIgnoreCase("report1")) {
						List<Report1.RowColValue> rs = namedJdbcTemplate.query(mdResult.get("query").toString(),
								parameters,new Report1.RowColValue(adUnit));
						return Report1.main(rs);
					}
				} else {
					boolean flag = true;
					if(jsonObject.get("DC_OCR_TEXT")!=null && !jsonObject.get("DC_OCR_TEXT").toString().isEmpty()) {
						List<Long> ids = searchDataEntry(jsonObject.get("DC_OCR_TEXT").toString());
						if(ids.size()>0) {
							System.out.println("size:"+ids.size());
							parameters.put("DE_IDSin", ids);
							parameters.put("DE_IDS", "");
						} else {
							flag = false;
							resp.put("data" , new ArrayList<Map<String,Object>>());
						}
					} else {
						parameters.put("DE_IDS", null);							
						
					}
					if(flag) {
						List<Map<String, Object>> rs = namedJdbcTemplate.queryForList(mdResult.get("query").toString(),parameters);
						resp.put("data" , rs);
					}
				
					JSONArray columns = ((JSONArray)new JSONParser().parse(mdResult.get("columns").toString()));
					resp.put("columns" , columns);
				
					JSONArray hiddenpivotcol = ((JSONArray)new JSONParser().parse(mdResult.get("hiddenpivotcol").toString()));
					resp.put("hiddenpivotcol",hiddenpivotcol);
			}
			if(id == 19l) {
				Map<String,Object> companyMap= jt.queryForMap("Select DC_COMPANY_NAME as 'companyName',DC_ADDRESS_LINE_1 as 'addressLine1',DC_COUNTRY as 'country' from tbl_de_company where DN_ID =" + Long.parseLong(jsonObject.get("DN_DECOMPANY_ID").toString().replace("[","").replace("]","")));
				resp.put("company",companyMap);
			}
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return resp;
    }
    
    @RequestMapping(value="/report/getImage/{id}",method=RequestMethod.GET)
    @Transactional
    @ResponseBody FileSystemResource getImage(@PathVariable("id")Long id ) {
    	Map<String,Object> mdResult = jt.queryForMap("Select DC_IMAGE from tbl_de_data where DN_ID =" + id);
    	String filePath = mdResult.get("DC_IMAGE").toString();
    	return new FileSystemResource(new File(filePath));
    }
    
    @RequestMapping(value="/deleteReport",method=RequestMethod.GET)
    @ResponseBody
    @Transactional
    public List<ReportMDVM> deleteReport(@RequestParam("id") final Long id) {
    	try {
			KeyHolder keyHolder=new GeneratedKeyHolder();
			jt.update(new PreparedStatementCreator(){
		    public PreparedStatement createPreparedStatement(    Connection connection) throws SQLException {
		      PreparedStatement ps=connection.prepareStatement("delete from reportmd where id=?",new String[]{"id"});
		      int index=1;
		      ps.setLong(index++,id);
		      return ps;
		    }
		  }
		,keyHolder);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return getReports(-1l);
    }
    
	@RequestMapping(value="/reports/md",method=RequestMethod.GET)
	@ResponseBody
	@Transactional
	public List<ReportMDVM> getReports(@RequestParam("subscriberId") final Long subscriberId) {
		//return sessionFactory.getCurrentSession().createQuery("FROM ReportMD1").list();
		
		return jt.query("Select * from reportmd where access = 1", new RowMapper<ReportMDVM>(){

			public ReportMDVM mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				try {
				ReportMDVM reportMD = new ReportMDVM();
				reportMD.id = (arg0.getLong("id"));
				reportMD.jsonForm = ((JSONArray)new JSONParser().parse(buildTitleMap(arg0.getString("jsonForm"),subscriberId)));
				reportMD.jsonSchema = ((JSONObject)new JSONParser().parse(arg0.getString("jsonSchema")));
				reportMD.name = (arg0.getString("name"));
				reportMD.description = (arg0.getString("description"));
				//System.out.println("jkds:"+arg0.getString("pivotConfig"));
				if(arg0.getString("pivotConfig")!=null) 
					reportMD.pivotConfig = ((JSONObject)new JSONParser().parse(arg0.getString("pivotConfig")));
				if(arg0.getString("searchCriteria")!=null) 
					reportMD.searchCriteria = arg0.getString("searchCriteria");
				reportMD.isJava = arg0.getString("isJava");
				return reportMD;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				
			}
			
		});
		
	}
	
	private String  buildTitleMap(String jsonStr,Long subscriberId) {
		String titleMapFn[] =  jsonStr.split("titleMapFn_");
		for(int i = 1 ; i < titleMapFn.length ;i++) {
			String functionName = titleMapFn[i].split("\"")[0].trim();
			try {
				String o = (String)TitleMapHelper.class.getMethod(functionName,JdbcTemplate.class,Long.class).invoke(null,jt,subscriberId);
				jsonStr = jsonStr.replace("\"titleMapFn_"+functionName+"\"", o);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return jsonStr;
	}
	
	@RequestMapping(value="/reports/md/{reportid}",method=RequestMethod.GET)
	@ResponseBody
	public Object execute(Long id) {
		return null;
	}
	
	@RequestMapping(value="/getParentImage",method=RequestMethod.GET)
	@ResponseBody
	public String getParentImage(@RequestParam("parentImageId") Long id) {
		Map<String,Object> mdResult = jt.queryForMap("select DC_IMAGENAME from tbl_parent_image where DN_ID ="+id);
		String result = "";
		if(mdResult!=null &&!mdResult.isEmpty()) {
			result = id+"/"+mdResult.get("DC_IMAGENAME").toString();
		}
		return result;
	}
	
	public static class  ReportMDVM {
		public JSONArray jsonForm;
		public JSONObject jsonSchema;
		public JSONObject pivotConfig;
		public String searchCriteria;
		public String name;
		public String description;
		public String isJava;
		public Long id;
	}
	
	public static class ReportTemplateVM {
		public Long parentId;
		public String templateName;
		public String data;
		public String searchCriteria;
	}
	
	public static class DataEntryVM {
		public Long id;
		public String ocrText;
	}
	/*
	select B.DD_ISSUE_DATE from tbl_de_data A,tbl_parent_image B  WHERE 
	(A.DC_AD_SIZE IN (:DC_AD_SIZEin) OR :DC_AD_SIZE IS NULL) AND
	(A.DC_AD_TYPE IN  (:DC_AD_TYPEin) OR :DC_AD_TYPE IS NULL) AND 
	(A.DC_AD_ORIENTATION IN  (:DC_AD_ORIENTATIONin) OR :DC_AD_ORIENTATION IS NULL) AND
	(A.DC_JOB_DENSITY IN  (:DC_JOB_DENSITYin) OR :DC_JOB_DENSITY IS NULL) AND
	(A.DC_AD_CATEGORY IN  (:DC_AD_CATEGORYin) OR :DC_AD_CATEGORY IS NULL) AND
	(A.DC_SEARCH_ADVERTISER_TYPE IN  (:DC_SEARCH_ADVERTISER_TYPEin) OR :DC_SEARCH_ADVERTISER_TYPE IS NULL) AND
	(A.DC_ADVERTISER_TYPE IN  (:DC_ADVERTISER_TYPEin) OR :DC_ADVERTISER_TYPE IS NULL) AND
	(A.DN_DECOMPANY_ID  IN  (:DN_DECOMPANY_IDin) OR :DN_DECOMPANY_ID IS NULL) AND 
	(B.DN_ID = A.DN_PARENT_IMAGE_ID ) AND
	(B.DC_PUBLICATION_TITLE IN (:DC_PUBLICATION_TITLEin) OR :DC_PUBLICATION_TITLE IS NULL)
	
	
	
SELECT 
 A.DN_ID,PIM.DC_PUBLICATION_TITLE,PIM.DN_ID,P2.DC_PUBLICATION_TITLE
FROM tbl_de_data A LEFT JOIN 
(SELECT P.DN_ID, P.DC_PUBLICATION_TITLE FROM tbl_publication P where 
P.DC_PUBLICATION_TYPE = 4) P4 ON A.DC_AD_CATEGORY = P4.DN_ID LEFT JOIN
(SELECT P.DN_ID, P.DC_PUBLICATION_TITLE FROM tbl_publication P where 
P.DC_PUBLICATION_TYPE = 5) P5 ON A.DC_ADVERTISER_TYPE = P5.DN_ID LEFT JOIN 
(SELECT P.DN_ID, P.DC_PUBLICATION_TITLE FROM tbl_publication P where 
P.DC_PUBLICATION_TYPE = 6) P6 ON A.DC_SEARCH_ADVERTISER_TYPE = P6.DN_ID LEFT JOIN
tbl_parent_image PIM ON PIM.DN_ID = A.DN_PARENT_IMAGE_ID
 LEFT JOIN 
(SELECT P.DN_ID, P.DC_PUBLICATION_TITLE FROM tbl_publication P where 
P.DC_PUBLICATION_TYPE = 2) P2 ON P2.DN_ID = PIM.DC_PUBLICATION_TITLE
	*
	*
	*/

}
