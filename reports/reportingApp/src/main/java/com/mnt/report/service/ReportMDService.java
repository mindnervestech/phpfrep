package com.mnt.report.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
import org.springframework.web.multipart.MultipartFile;
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

import net.coobird.thumbnailator.Thumbnails;

@Controller
public class ReportMDService {
	
	@Autowired
    private JdbcTemplate jt;
	
	@Autowired
	NamedParameterJdbcTemplate namedJdbcTemplate;
	@Value("${imagePath}")
	String imgPath;
	
	@Value("${fullImagePath}")
	String fullImagePath;
	
	private static Map<String,String> titleMap = new HashMap<String,String>(6);
	private static Map<String,String> sectionMap = new HashMap<String,String>(4);
	static {
		titleMap.put("natus", "1084");
		titleMap.put("natuk", "1085");
		titleMap.put("natgbl", "1086");
		titleMap.put("newus", "520");
		titleMap.put("newuk", "521");
		titleMap.put("cell", "522");
		titleMap.put("scius", "518");
		sectionMap.put("SR", "973");
		sectionMap.put("ST", "1032");
		sectionMap.put("CL", "644");
		sectionMap.put("OT", "1033");
	}
	private static String[] monthsStr = {"January","Febuary","March","April","May","June","July","August","September","October","November","December"};

	private static String localpath = "/usr/local/apache-tomcat-7.0.34/webapps"+File.separator+"files"+File.separator+"fracts_files"+File.separator+"images"+File.separator;
	
	/*
	 *  978  mvn install
  979  cd /usr/local/apache-tomcat-7.0.34/webapps/
  980  rm -rf webapp
  981  cp -r /home/phpfrep/reports/webapp/target/webapp /usr/local/apache-tomcat-7.0.34/webapps/webapp
  982  /usr/local/apache-tomcat-7.0.34/bin/shutdown.sh
  983  /usr/local/apache-tomcat-7.0.34/bin/startup.sh

	 * */
	private static Client client;
	
	static {
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "Fracts").build();
		client  = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
		
	}
	
	@RequestMapping(value="/searchDataEntry",method=RequestMethod.GET)
	@ResponseBody
	public List<Long> searchDataEntry(@RequestParam("query") String query) {
		System.out.println("query:"+query);
		List<Long> ids = new ArrayList<Long>();
		SearchResponse response = client.prepareSearch("fracts").setTypes("DataEntry").setQuery(QueryBuilders.queryString(query)
				.defaultField("ocrText")).setSize(1000).execute().actionGet();
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
		
		System.out.println("in /addUnProcessedOcr api");
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
			
			System.out.println("size of addUnProcessedOcr is"+unProcessedList.size());
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
				
				System.out.println("record updated");
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
	
    @RequestMapping(value="/report/run",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject runReports(@RequestBody String filter) {
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
					if(key.toString().equals("DN_F_ID") && !value.toString().isEmpty()) {
						String[] ids = value.toString().split(",");
						List<String> inValues = new ArrayList<String>();
						for (String ida:ids) {
							try {
								inValues.add((Long.parseLong(ida))+"");
							} catch(Exception e) {}
						}
						parameters.put(key.toString()+"in", inValues);
					    parameters.put(key.toString(), "");
					} else if(!value.equals(""))
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
						/*JSONArray columns = ((JSONArray)new JSONParser().parse(mdResult.get("columns").toString()));
						System.out.println(columns.size());
						for(int i=0; i < columns.size(); i++) {
							JSONObject object = (JSONObject)columns.get(i);
							System.out.println(i+" : "+object.get("data"));
							if(((String)object.get("data")).equalsIgnoreCase("Publication")) {
								Report1.Y_VALUE = object.get("name").toString();
								break;
							}
						}*/
						//System.out.println("after loop");
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
			if(id == 19l && jsonObject.get("DN_DECOMPANY_ID") != null) {
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
    
    @RequestMapping(value="/getAllPublication",method=RequestMethod.GET)
    public @ResponseBody List<SelectItem> getAllPublication() {
    	List<Map<String,Object>> mdResult = jt.queryForList("Select DN_ID,DC_PUBLICATION_TITLE from tbl_publication where DC_PUBLICATION_TYPE =" + 2);
    	List<SelectItem> selectItems = new ArrayList<ReportMDService.SelectItem>();
    
    	for(Map<String,Object> m:  mdResult){
    		SelectItem selectItem = new SelectItem();
			selectItem.name = m.get("DC_PUBLICATION_TITLE").toString();
			selectItem.value = (m.get("DN_ID").toString());
			selectItems.add(selectItem);
    	}
        return selectItems;
    }
    
    @RequestMapping(value="/getAllDeJobs",method=RequestMethod.GET)
    public @ResponseBody void  getAllDeJobs() {
    	List<Map<String,Object>> dejobs = jt.queryForList("Select * from tbl_de_job");
    	System.out.println("Dejobs: "+dejobs.size());
    	final Map<Long ,Long> map = new HashMap<Long, Long>();
    	
    	MapSqlParameterSource parameters = new MapSqlParameterSource();
    	List<Long> idss = new ArrayList<Long>();
    	String idParent = new String();
    	
    	for(Map<String,Object> m:  dejobs){
			idss.add(Long.parseLong(m.get("DN_PARENT_IMAGE_ID").toString()));
			map.put(Long.parseLong(m.get("DN_PARENT_IMAGE_ID").toString()), Long.parseLong(m.get("DN_CREATED_BY").toString()));
			idParent += m.get("DN_PARENT_IMAGE_ID").toString()+",";
    	}
    	
    	if(!idParent.isEmpty()) {
    		idParent = idParent.substring(0,idParent.length()-1);
    	}
    	
    	parameters.addValue("ids", idss);
    	
    	System.out.println("Idsss: "+idss.size());      
    	System.out.println("parameters: "+parameters);
    	
    	List<Map<String,Object>> parentJobsNotDejob = jt.queryForList("Select * from tbl_parent_image where DN_ID not in ("+idParent+")");
    	System.out.println("parentJobsNotDejob: "+parentJobsNotDejob.size());

    	for(final Map<String,Object> m :  parentJobsNotDejob){
    		map.put(Long.parseLong(m.get("DN_ID").toString()), Long.parseLong(m.get("DN_CREATED_BY").toString()));
    	}
    	
    	for(final Map<String,Object> m :  parentJobsNotDejob){
    		
    		System.out.println("DN ID toString(); "+m.get("DN_ID").toString());
    		KeyHolder keyHolder=new GeneratedKeyHolder();
			jt.update(new PreparedStatementCreator(){
		    public PreparedStatement createPreparedStatement( Connection connection) throws SQLException {
		      PreparedStatement ps=connection.prepareStatement("Insert into tbl_de_job"
		      		+ "(DD_CREATED_ON,DN_CREATED_BY,"
		      		+ "DD_DELETED_ON,DN_DELETED_BY,"
		      		+ "DB_DELETED,DN_PARENT_IMAGE_ID,"
		      		+ "DB_ACTIVE,DN_STATUS,"
		      		+ "isCompleted,render) values(?,?,?,?,?,?,?,?,?,?)",new String[]{"id"});
		      int index=1;
		     //DD_CREATED_ON
		      ps.setDate(index++,new java.sql.Date(new Date().getTime()));
		      //DD_CREATED BY
		      ps.setLong(index++,Long.parseLong(m.get("DN_CREATED_BY").toString()));
		     //DD_DELETED_ON
		      ps.setNull(index++,java.sql.Types.NULL);
		      //DD_DELETED_BY
		      ps.setNull(index++, java.sql.Types.NULL);//Null(index++, (Long)null );
		      //DB_DELETED
		      ps.setInt(index++, 0);
		      //DN_PARENT_IMAGE_ID
		      String pid = m.get("DN_ID").toString();
		      ps.setLong(index++, Long.parseLong(pid));
		      //DB_ACTIVE
		      ps.setInt(index++, 1);
		      //DN_STATUS
		      ps.setInt(index++, 1);
		      //isCompleted
		      ps.setNull(index++, java.sql.Types.NULL);
		      //render
		      ps.setInt(index++, 0);
		      return ps;
		    }
		  }
		,keyHolder);
    		
    	}
    	
    	
    }
    
    
    @RequestMapping(value="/getAllPublicationData",method=RequestMethod.POST)
    public @ResponseBody Map getAllpublicationData(@RequestBody String filter) {
    	JSONObject jsonObject = null;
    	Map<String,Object> map = new HashMap();
    	Map<String, Object> parameters = new HashMap<String, Object>(); 
    	try {
    		Calendar cal = Calendar.getInstance();
			jsonObject = (JSONObject)new JSONParser().parse(filter);
			List<Map<String,Object>> mdResult = getParentImageData(filter);
	    	Set<String> years =  new HashSet<String>();
	    	for(Map<String,Object> m:  mdResult){
	    		cal.setTime((Date)m.get("ISSUE_DATE"));
				String year = cal.get(Calendar.YEAR)+"";
				years.add(year);
	    	}
	    	map.put("years", years);
	    	map.put("reports", mdResult);
    	} catch(Exception e) {}
        return map;
    }
    
    @RequestMapping(value="/get-year-publication",method=RequestMethod.POST)
    public @ResponseBody Map getYearPublication(@RequestBody String filter) {
    	JSONObject jsonObject = null;
    	Map<String,Object> map = new HashMap();
    	Map<String, Object> parameters = new HashMap<String, Object>(); 
    	try {
    		Calendar cal = Calendar.getInstance();
			jsonObject = (JSONObject)new JSONParser().parse(filter);
			List<Map<String,Object>> mdResult = getParentImageData(filter);
	    	Set<SelectItem> months =  new HashSet<SelectItem>();
	    	Set<String> buffer = new HashSet<String>();
	    	for(Map<String,Object> m:  mdResult) {
	    		SelectItem item = new SelectItem();
	    		cal.setTime((Date)m.get("ISSUE_DATE"));
	    		if ( buffer.add((cal.get(Calendar.MONTH)+1)+"") ) {
	    			item.name = monthsStr[cal.get(Calendar.MONTH)];
	    			item.value = (cal.get(Calendar.MONTH)+1)+"";
					months.add(item);
	    		}
	    	}
	    	map.put("months", months);
	    	map.put("reports", mdResult);
    	} catch(Exception e) {}
        return map;
    }
    
    @RequestMapping(value="/get-month-publication",method=RequestMethod.POST)
    public @ResponseBody Map getMonthPublication(@RequestBody String filter) {
    	JSONObject jsonObject = null;
    	Map<String,Object> map = new HashMap();
    	Map<String, Object> parameters = new HashMap<String, Object>(); 
    	try {
    		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		Calendar cal = Calendar.getInstance();
			jsonObject = (JSONObject)new JSONParser().parse(filter);
			List<Map<String,Object>> mdResult = getParentImageData(filter);
	    	Set<String> days =  new HashSet<String>();
	    	for(Map<String,Object> m:  mdResult){
	    		try {
	    			days.add(df.format((Date)m.get("ISSUE_DATE")));
	    		} catch(Exception e) {}
	    	}
	    	map.put("days", days);
	    	map.put("reports", mdResult);
    	} catch(Exception e) {}
        return map;
    }
     
    @RequestMapping(value="/get-date-publication",method=RequestMethod.POST)
    public @ResponseBody Map getDatePublication(@RequestBody String filter) {
    	JSONObject jsonObject = null;
    	Map<String,Object> map = new HashMap();
    	Map<String, Object> parameters = new HashMap<String, Object>(); 
    	try {
    		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		Calendar cal = Calendar.getInstance();
			jsonObject = (JSONObject)new JSONParser().parse(filter);
			List<Map<String,Object>> mdResult = getParentImageData(filter);
	    	Set<String> days =  new HashSet<String>();
	    	for(Map<String,Object> m:  mdResult){
	    		try {
	    			days.add(df.format((Date)m.get("ISSUE_DATE")));
	    		} catch(Exception e) {}
	    	}
	    	map.put("reports", mdResult);
    	} catch(Exception e) {}
        return map;
    }
    
    private List getParentImageData(String filter) {
    	List<Map<String, Object>> rs = null;
    	try {
			JSONObject jsonObject = (JSONObject)new JSONParser().parse(filter);
			Long id = Long.parseLong(jsonObject.get("id").toString());
			Map<String,Object> mdResult = jt.queryForMap("Select query from reportmd where id =" + id);
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
					if(!((String) value).isEmpty()) {
						System.out.println("key :"+key.toString());
						System.out.println("value :"+value.toString());
						parameters.put(key.toString(), value);
					}
				}
				if (value instanceof Long || value instanceof Integer || value instanceof Double || value instanceof Float) {
					System.out.println("key :"+key.toString());
					System.out.println("value :"+value.toString());
					parameters.put(key.toString(), value);
				}
				if (value instanceof JSONArray) {
					JSONArray jsonArray = (JSONArray) value;
				    int len = jsonArray.size();
				    if(len > 0) {
				      List<String> inValues = new ArrayList<String>();
				      System.out.println("key :"+key.toString());
				      for (int i=0;i<len;i++){ 
				    	  System.out.println("value :"+jsonArray.get(i).toString());
				    	  inValues.add(jsonArray.get(i).toString());
				    	  System.out.println("pub title :"+key.toString().equals("DC_PUBLICATION_TITLE"));
				    	  System.out.println("value :"+jsonArray.get(i).toString().equals("1086"));
				    	  if(key.toString().equals("DC_PUBLICATION_TITLE") && jsonArray.get(i).toString().equals("1086")) {
				    		  if(!inValues.contains("1084")) {
				    			  inValues.add("1084");
				    		  }
				    		  if(!inValues.contains("1085")) {
				    			  inValues.add("1085");
				    		  }
				    	  }
				      }
				      parameters.put(key.toString()+"in", inValues);
				      parameters.put(key.toString(), "");
				    }
				}
			}
			rs = namedJdbcTemplate.queryForList(mdResult.get("query").toString(),parameters);
    	}catch (Exception e) {}
    	return rs;
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
		System.out.println("subscriberId:"+subscriberId);
		String ids = "";
		if(subscriberId==-1)
			ids = "(1,3)";
		else 
			ids = "(2,3)";
		List<ReportMDVM> results = jt.query("Select * from reportmd where access in "+ids, new RowMapper<ReportMDVM>(){

			public ReportMDVM mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				try {
				ReportMDVM reportMD = new ReportMDVM();
			    reportMD.order = arg0.getInt("order"); 
				reportMD.id = (arg0.getLong("id"));
				reportMD.jsonForm = ((JSONArray)new JSONParser().parse(buildTitleMap(arg0.getString("jsonForm"),subscriberId)));
				reportMD.jsonSchema = ((JSONObject)new JSONParser().parse(arg0.getString("jsonSchema")));
				reportMD.name = (arg0.getString("name"));
				reportMD.description = (arg0.getString("description"));
				reportMD.isPlugin = (arg0.getInt("isPlugin"));
				
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

		Collections.sort(results, new Comparator() {

			public int compare(Object o1, Object o2) {
				ReportMDVM rm1 = (ReportMDVM)o1;
				ReportMDVM rm2 = (ReportMDVM)o2;
				return rm1.order.compareTo(rm2.order);
			}
			
		});
		return results; 
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
	
	@RequestMapping(value="/getParentImageThumb",method=RequestMethod.GET) 
	@ResponseBody
	public FileSystemResource getParentImageThumb(@RequestParam("id") Long id) {
		Map<String,Object> mdResult = jt.queryForMap("select DC_IMAGENAME from tbl_parent_image where DN_ID ="+id);
		String result = "";
		if(mdResult!=null &&!mdResult.isEmpty()) {
			result = localpath+File.separator+"parent"+File.separator+id+File.separator+(mdResult.get("DC_IMAGENAME").toString().split("\\.")[0])+"_thumb.jpg";
			File f = new File(result);
			if(!f.exists()) {
				try {	
					Thumbnails.of(new File(localpath+File.separator+"parent"+File.separator+id+File.separator+mdResult.get("DC_IMAGENAME").toString()))
		        	.size(200, 300).keepAspectRatio(true)
		        	.outputFormat("jpg")
		        	.toFile(f);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			return new FileSystemResource(f);
		}
		return null;
	}
	
	@RequestMapping(value="/getChildImageThumb",method=RequestMethod.GET) 
	@ResponseBody
	public FileSystemResource getChildImageThumb(@RequestParam("id") Long id,@RequestParam("parentId") Long parentId) {
		Map<String,Object> mdResult = jt.queryForMap("select DC_IMAGENAME from tbl_child_image where DN_ID ="+id);
		String result = "";
		if(mdResult!=null &&!mdResult.isEmpty()) {
			result = localpath+File.separator+"child"+File.separator+parentId+File.separator+id+File.separator+mdResult.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
			File f = new File(result);
			if(!f.exists()) {
				try {	
					Thumbnails.of(new File(localpath+File.separator+"child"+File.separator+parentId+File.separator+id+File.separator+mdResult.get("DC_IMAGENAME").toString()))
		        	.size(200, 300).keepAspectRatio(true)
		        	.outputFormat("jpg")
		        	.toFile(f);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			return new FileSystemResource(f);
		}
		return null;
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
	
	@RequestMapping(value="/get-parent-image",method=RequestMethod.GET)
	@ResponseBody
	public FileSystemResource getParentImageImg(@RequestParam("id") Long id) {
		Map<String,Object> mdResult = jt.queryForMap("select DC_IMAGENAME from tbl_parent_image where DN_ID ="+id);
		String result = "";
		if(mdResult!=null &&!mdResult.isEmpty()) {
			result = imgPath+"parent"+File.separator+id+File.separator+mdResult.get("DC_IMAGENAME").toString();
		}
		return new FileSystemResource(result);
	}
	
	@RequestMapping(value="/get-all-parent-image",method=RequestMethod.GET)
	@ResponseBody
	public FileSystemResource getAllParentImageImg(@RequestParam("id") Long id) {
		
		System.out.println("in get-all-parent-image....api");
		Map<String,Object> mdResult = jt.queryForMap("select DC_IMAGENAME from tbl_parent_image where DN_ID ="+id);
		String result = "";
		if(mdResult!=null &&!mdResult.isEmpty()) {
			
			result = fullImagePath+"parent"+File.separator+id+File.separator+mdResult.get("DC_IMAGENAME").toString();
			
		}
		return new FileSystemResource(result);
	}
	
	@RequestMapping(value="/get-all-child-image",method=RequestMethod.GET)
	@ResponseBody
	public FileSystemResource getAllChildImageImg(@RequestParam("cid") Long cid,@RequestParam("pid") Long pid, @RequestParam("iname") String iname ) {
		
		String result=fullImagePath+"child"+File.separator+pid+File.separator+cid+File.separator+iname;
		
		return new FileSystemResource(result);
		
	}
	
	@RequestMapping(value="/get-child-image",method=RequestMethod.GET)
	@ResponseBody
	public FileSystemResource getChildImageImg(@RequestParam("id") Long id) {
		Map<String,Object> mdResult = jt.queryForMap("select DC_IMAGENAME,DN_PARENT_IMAGE_ID from tbl_child_image where DN_ID ="+id);
		String result = "";
		if(mdResult!=null &&!mdResult.isEmpty()) {
			result = imgPath+"child"+File.separator+mdResult.get("DN_PARENT_IMAGE_ID").toString()+File.separator+id+File.separator+mdResult.get("DC_IMAGENAME").toString();
		}
		return new FileSystemResource(result);
	}
	
	@RequestMapping(value="/getChildImage",method=RequestMethod.GET)
	@ResponseBody
	public String getChildImage(@RequestParam("childImageId") Long id) {
		Map<String,Object> mdResult = jt.queryForMap("select DC_IMAGENAME from tbl_child_image where DN_ID ="+id);
		String result = "";
		if(mdResult!=null &&!mdResult.isEmpty()) {
			result = mdResult.get("DC_IMAGENAME").toString();
		}
		return result;
	}
	
	@RequestMapping(value="/updateReportRecord",method=RequestMethod.GET)
	@ResponseBody 
	public String updateReportRecord(@RequestParam Map<String,String> map) {
		for(String key :map.keySet()) {
			System.out.println(key+" : "+ map.get(key));
		}
		return "";
	}
	
	@RequestMapping(value="/getDeData",method=RequestMethod.GET)
	@ResponseBody
	public DEVM getDeData(@RequestParam("id") Long id) {
		Map<String,Object> mdResult = jt.queryForMap("select DC_OCR_TEXT,DC_AD_HEADLINE,DC_AD_TYPE,DC_JOB_DENSITY,DC_AD_SIZE,DC_AD_ORIENTATION,"+
					"DC_ADD_COLUMN,DC_ADVERTISER_TYPE as institutionType,DC_SEARCH_ADVERTISER_TYPE as advertiserType,DC_AD_CATEGORY,DC_PAGE_URL,"+
					"DC_LENGTH,DC_WIDTH,DC_CURRENCY,DC_START_CURRENCY_RANGE,DC_END_CURRENCY_RANGE,DN_DECOMPANY_ID,DC_CONTACT_INFO from tbl_de_data where DN_ID ="+id);
		if(mdResult!=null &&!mdResult.isEmpty()) {
			DEVM devm = new DEVM();
			devm.id = id;
			devm.adCategory = mdResult.get("DC_AD_CATEGORY").toString();
			devm.adColumn = mdResult.get("DC_ADD_COLUMN").toString();
			devm.adOrientation = mdResult.get("DC_AD_ORIENTATION").toString();
			devm.adSize = mdResult.get("DC_AD_SIZE").toString();
			devm.adType = mdResult.get("DC_AD_TYPE").toString();
			devm.advertiserType = mdResult.get("advertiserType").toString();
			devm.company = mdResult.get("DN_DECOMPANY_ID").toString();
			devm.contactInfo = mdResult.get("DC_CONTACT_INFO").toString();
			devm.currency = mdResult.get("DC_CURRENCY").toString();
			devm.endCurrency = mdResult.get("DC_END_CURRENCY_RANGE").toString();
			devm.headline = mdResult.get("DC_AD_HEADLINE").toString();
			devm.institutionType = mdResult.get("institutionType").toString();
			devm.jobDensity = mdResult.get("DC_JOB_DENSITY").toString();
			devm.landingPageURL = mdResult.get("DC_PAGE_URL").toString();
			devm.length = mdResult.get("DC_LENGTH").toString();
			devm.ocrText = mdResult.get("DC_OCR_TEXT").toString();
			devm.startCurrency = mdResult.get("DC_START_CURRENCY_RANGE").toString();
			devm.width = mdResult.get("DC_WIDTH").toString();
			return devm;
		}
		return null;
	}
	
	@RequestMapping(value="/getAdvertiserType",method=RequestMethod.GET)
	@ResponseBody
	public List<SelectItem> getAdvertiserType(){
		return jt.query("select DN_ID as value,DC_PUBLICATION_TITLE as name from tbl_publication as m where m.DB_DELETED=0 and m.DC_PUBLICATION_TYPE="+6, new RowMapper<SelectItem>(){

			public SelectItem mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				try {
					SelectItem selectItem = new SelectItem();
					selectItem.name = arg0.getString("name");
					selectItem.value = arg0.getString("value");
					return selectItem;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});
	}
	
	@RequestMapping(value="/getInstituteType",method=RequestMethod.GET)
	@ResponseBody
	public List<SelectItem> getInstituteType(){
		return jt.query("select DN_ID as value,DC_PUBLICATION_TITLE as name from tbl_publication as m where m.DB_DELETED=0 and m.DC_PUBLICATION_TYPE="+5, new RowMapper<SelectItem>(){

			public SelectItem mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				try {
					SelectItem selectItem = new SelectItem();
					selectItem.name = arg0.getString("name");
					selectItem.value = arg0.getString("value");
					return selectItem;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});
	}
	
	@RequestMapping(value="/getAdCategory",method=RequestMethod.GET)
	@ResponseBody
	public List<SelectItem> getAdCategory(){
		return jt.query("select DN_ID as value,DC_PUBLICATION_TITLE as name from tbl_publication as m where m.DB_DELETED=0 and m.DC_PUBLICATION_TYPE="+4, new RowMapper<SelectItem>(){

			public SelectItem mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				try {
					SelectItem selectItem = new SelectItem();
					selectItem.name = arg0.getString("name");
					selectItem.value = arg0.getString("value");
					return selectItem;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});
	}
	
	@RequestMapping(value="/getAllCompany",method=RequestMethod.GET)
	@ResponseBody
	public List<SelectItem> getAllCompany(){
		return jt.query("select DN_ID as value,DC_COMPANY_NAME as name from tbl_de_company as m where m.DB_DELETED=0", new RowMapper<SelectItem>(){

			public SelectItem mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				try {
					SelectItem selectItem = new SelectItem();
					selectItem.name = arg0.getString("name");
					selectItem.value = arg0.getString("value");
					return selectItem;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});
	}
	
	@RequestMapping(value="/getOcrTextArea",method=RequestMethod.GET)
	@ResponseBody 
	public String getOcrTextArea(@RequestParam("deDataId") Long id) {
		Map<String,Object> mdResult = jt.queryForMap("select DC_OCR_TEXT from tbl_de_data where DN_ID ="+id);
		String result = "";
		if(mdResult!=null &&!mdResult.isEmpty()) {
			result = mdResult.get("DC_OCR_TEXT").toString();
		}
		return result;
	}
	
	@RequestMapping(value="/updateDe",method=RequestMethod.POST)
	@ResponseBody
	public void updateDe(@RequestBody final DEVM devm) {
		try {
			jt.update(new PreparedStatementCreator(){
		    public PreparedStatement createPreparedStatement(    Connection connection) throws SQLException {
		      PreparedStatement ps=connection.prepareStatement("Update tbl_de_data SET DC_AD_HEADLINE = ?,DC_ADVERTISER_TYPE = ?,DC_AD_ORIENTATION = ?,DC_AD_SIZE = ?,"+
		    		  "DC_CURRENCY = ?,DC_OCR_TEXT = ?,DC_AD_TYPE = ?,DC_START_CURRENCY_RANGE = ?,DC_END_CURRENCY_RANGE = ?,DN_DECOMPANY_ID = ?,DC_AD_CATEGORY = ?,DC_JOB_DENSITY = ?,"+
		    		  "DC_LENGTH = ?,DC_SEARCH_ADVERTISER_TYPE = ?,DC_WIDTH = ?,DC_ADD_COLUMN = ?,DC_PAGE_URL = ?,DC_CONTACT_INFO = ? where DN_ID = ?");
		      int index=1;
		      ps.setString(index++,devm.headline);
		      ps.setString(index++,devm.institutionType);
		      ps.setString(index++,devm.adOrientation);
		      ps.setString(index++,devm.adSize);
		      ps.setString(index++,devm.currency);
		      ps.setString(index++,devm.ocrText);
		      ps.setString(index++,devm.adType);
		      ps.setString(index++,devm.startCurrency);
		      ps.setString(index++,devm.endCurrency);
		      ps.setString(index++,devm.company);
		      ps.setString(index++,devm.adCategory);
		      ps.setString(index++,devm.jobDensity);
		      ps.setString(index++,devm.length);
		      ps.setString(index++,devm.advertiserType);
		      ps.setString(index++,devm.width);
		      ps.setString(index++,devm.adColumn);
		      ps.setString(index++,devm.landingPageURL);
		      ps.setString(index++,devm.contactInfo);
		      ps.setLong(index++,devm.id);
		      return ps;
		    }
		  });
			updateDEOcr(devm.id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value="/process-all-parent-images",method=RequestMethod.GET) 
	@ResponseBody public void processAllParentImages() {
		List<Map<String,Object>> list =  jt.query("select DN_ID as id, DC_IMAGENAME as name from tbl_parent_image", new RowMapper<Map<String,Object>>(){

			public Map<String,Object> mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				try {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("name", arg0.getString("name"));
					map.put("id", arg0.getString("id"));
					System.out.println("id :"+((String)map.get("id")));
					System.out.println("name :"+((String)map.get("name")));
					return map;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});
		System.out.println("list:"+list.size());
		for(Map<String,Object> map  : list) {
			
			String section = null;
			Integer day = null;
			Integer month = null;
			Integer year = null;
			String page = null;
			String filename = (String)map.get("name");
			String publication = null;
			String dateField = null;
			Date date = null;
			System.out.println(filename);
			if(filename != null && !filename.isEmpty()) {
				try {
					String[] arr = filename.split("_");
					if(arr.length==3) {
						publication = titleMap.get(arr[0]);
						String[] arr1  = arr[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
						try {
							if(arr1.length==2) {
								day = Integer.parseInt(arr1[0].substring(4, 6));
								month = Integer.parseInt(arr1[0].substring(2, 4));
								section = sectionMap.get(arr1[1].toUpperCase());
								year = 2000+Integer.parseInt(arr1[0].substring(0, 2));
							} else {
								try {
									Integer.parseInt(arr1[0]);
									day = Integer.parseInt(arr1[0].substring(4, 6));
									month = Integer.parseInt(arr1[0].substring(2, 4));
									year = 2000+Integer.parseInt(arr1[0].substring(0, 2));
								} catch(Exception e) {
									e.printStackTrace();
									section = sectionMap.get(arr1[1].toUpperCase());
								}
							}
							System.out.println(day);
							System.out.println(month);
							System.out.println(section);
							System.out.println(year);
							page = arr[2].split("\\.")[0];
							if(day != null && month != null && year != null ){
								dateField=year+"-"+month+"-"+day;
								date = new SimpleDateFormat("yyyy-MM-dd").parse(dateField);
							}
							final Date d = date;
							final String pg = page;
							final String pbl = publication;
							final String sec = section;
							final Long id = Long.parseLong((String)map.get("id"));
							System.out.println(pg);
							System.out.println(d);
							System.out.println(sec);
							System.out.println(pbl);
							System.out.println(id);
							try {
								jt.update(new PreparedStatementCreator(){
									public PreparedStatement createPreparedStatement(    Connection connection) throws SQLException {
										PreparedStatement ps=connection.prepareStatement("Update tbl_parent_image SET DD_ISSUE_DATE = ?,DC_PAGE = ?,DC_PUBLICATION_TITLE = ?,DC_SECTION = ?,"+
												"DC_SECTION_OTHER = ?,DC_SECTION_SPECIAL_REGIONAL = ?,DC_SECTION_SPECIAL_TOPIC = ? where DN_ID = ?");
										int index=1;
										if(d==null)
											ps.setNull(index++, java.sql.Types.NULL);
										else
											ps.setDate(index++, new java.sql.Date(d.getTime()));
										if(pg==null)
											ps.setNull(index++, java.sql.Types.NULL);
										else
											ps.setString(index++, pg);
										if(pbl==null)
											ps.setNull(index++, java.sql.Types.NULL);
										else
											ps.setString(index++, pbl);
										if(sec==null)
											ps.setNull(index++, java.sql.Types.NULL);
										else
											ps.setString(index++, sec);
										ps.setString(index++, "");
										ps.setString(index++, "");
										ps.setString(index++, "");
										ps.setLong(index++, id);
										return ps;
									}
								});
							} catch (Exception e) {
								e.printStackTrace();
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static class SelectItem {
		public String name;
		public String value;
	}
	
	public static class DEVM {
		public Long id;
		public String ocrText;
		public String headline;
		public String adType;
		public String jobDensity;
		public String adSize;
		public String adOrientation;
		public String adColumn;
		public String advertiserType;
		public String institutionType;
		public String adCategory;
		public String landingPageURL;
		public String length;
		public String width;
		public String currency;
		public String startCurrency;
		public String endCurrency;
		public String company;
		public String contactInfo;
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
		public Integer order;
		public Integer isPlugin;
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
