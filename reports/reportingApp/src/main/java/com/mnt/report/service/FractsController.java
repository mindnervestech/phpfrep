package com.mnt.report.service;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
@Controller
@RequestMapping(value="/fracts")
public class FractsController {
	
	@Autowired
    private JdbcTemplate jt;
	
	@Autowired
	NamedParameterJdbcTemplate namedJdbcTemplate;
	@Value("${imagePath}")
	String imgPath;
	
	@Value("${liveDataOfDays}")
	Integer liveDataOfDays;
	
	@Value("${notDuplicateDataOfDays}")
	Integer notDuplicateDataOfDays;
	
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	@RequestMapping(value="/get-relevance-list" , method = RequestMethod.GET)
	public @ResponseBody List getRelevanceList() {
		String query = "select cropped.DN_ID as 'croppedId',"+
				" cropped.DC_WIDTH as cropWidth, "+
				" cropped.DC_LENGTH as cropLenght,"+
				" live.DC_WIDTH as liveWidth,"+
				" live.DC_LENGTH as liveLength,"+
				" live.DN_ID as 'liveId' ,"+
				" cropped.DN_CHILD_IMAGE_ID as 'croppedChildImageId',"+
				" live.DN_CHILD_IMAGE_ID as 'liveChildImageId' "+
				" from tbl_ocr_text_match_result ocr, tbl_de_data live, tbl_de_data cropped "+ 
				" where "+
				" ocr.DC_IS_DUPLICATE = '0' and " +
				"ocr.STATUS_CHANGED_DATE IS NULL and "+
				"ocr.DC_CROPPED_JOBID = cropped.DN_ID and "+ 
				"ocr.DC_LIVE_JOBID = live.DN_ID and "+ 
				"cropped.DN_DECOMPANY_ID IS NULL";
		List<Map<String,Object>> results =  jt.queryForList(query);
		Map<Long,List<CroppedVM>> resMap = new HashMap<Long, List<CroppedVM>>();
		List<CroppedVM> croppedVMs = new ArrayList<CroppedVM>();
		for(Map<String, Object> map : results) {
			Long croppedId = Long.valueOf(map.get("croppedId").toString());
			if(resMap.get(croppedId) != null) {
				CroppedVM liveVM = new CroppedVM();
				if(map.get("liveId")!=null){
					liveVM.id = Long.valueOf(map.get("liveId").toString());
				}
			//	liveVM.id = Long.valueOf(map.get("liveId").toString());
				if(map.get("liveChildImageId")!=null){
					liveVM.childImageId = Long.valueOf(map.get("liveChildImageId").toString());
					if(map.get("liveWidth")!=null){
						liveVM.liveWidth=map.get("liveWidth").toString();
					}
					if(map.get("liveLength")!=null){
						liveVM.liveLength=map.get("liveLength").toString();
					}
					
					try {
						
						 Map<String,Object> recordMap=jt.queryForMap("select c.DC_IMAGENAME,c.DN_PARENT_IMAGE_ID from tbl_child_image c where c.DN_ID ="+Long.valueOf(map.get("liveChildImageId").toString()));
							
							if(recordMap!=null &&!recordMap.isEmpty()) {
								
								if(recordMap.get("DN_PARENT_IMAGE_ID")!=null){
									liveVM.parentImageId=Long.valueOf(recordMap.get("DN_PARENT_IMAGE_ID").toString());
								}
								if(recordMap.get("DC_IMAGENAME")!=null){
									liveVM.childImageName=recordMap.get("DC_IMAGENAME").toString();
								//	String fileName=recordMap.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
									liveVM.childImageThumb=recordMap.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
									
								}
								
							}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
				}
			//	liveVM.childImageId = Long.valueOf(map.get("liveChildImageId").toString());
				if( resMap.get(croppedId).size() < 2 ) {  
					resMap.get(croppedId).add(liveVM); 
				}
			} else {
				CroppedVM croppedVM = new CroppedVM();
				croppedVM.id = croppedId;
				
				if(map.get("croppedChildImageId")!=null){
					croppedVM.childImageId = Long.valueOf(map.get("croppedChildImageId").toString());
					
					if(map.get("cropWidth")!=null){
						croppedVM.cropWidth=map.get("cropWidth").toString();
					}
					if(map.get("cropLenght")!=null){
						croppedVM.cropLength=map.get("cropLenght").toString();
					}
					
					try {
						
						 Map<String,Object> recordMapOne=jt.queryForMap("select c.DC_IMAGENAME,c.DN_PARENT_IMAGE_ID from tbl_child_image c where c.DN_ID ="+Long.valueOf(map.get("croppedChildImageId").toString()));
							
							if(recordMapOne!=null &&!recordMapOne.isEmpty()) {
								
								if(recordMapOne.get("DN_PARENT_IMAGE_ID")!=null){
									croppedVM.parentImageId=Long.valueOf(recordMapOne.get("DN_PARENT_IMAGE_ID").toString());
								}
								if(recordMapOne.get("DC_IMAGENAME")!=null){
									croppedVM.childImageName=recordMapOne.get("DC_IMAGENAME").toString();
									
								//	String fileNameOne=recordMap.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
									croppedVM.childImageThumb=recordMapOne.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
									
								}
								
							}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					
				}
			//	croppedVM.childImageId = Long.valueOf(map.get("croppedChildImageId").toString());
				
				List<CroppedVM> relevanceList = new ArrayList<CroppedVM>();
				CroppedVM liveVM = new CroppedVM();
				if(map.get("liveId")!=null){
					liveVM.id = Long.valueOf(map.get("liveId").toString());
				}
			//	liveVM.id = Long.valueOf(map.get("liveId").toString());
				if(map.get("liveChildImageId")!=null){
					liveVM.childImageId = Long.valueOf(map.get("liveChildImageId").toString());
					if(map.get("liveWidth")!=null){
						liveVM.liveWidth=map.get("liveWidth").toString();
					}
					if(map.get("liveLength")!=null){
						liveVM.liveLength=map.get("liveLength").toString();
					}
					
					try {

						Map<String,Object> recordMapTwo=jt.queryForMap("select c.DC_IMAGENAME,c.DN_PARENT_IMAGE_ID from tbl_child_image c where c.DN_ID ="+Long.valueOf(map.get("liveChildImageId").toString()));

						if(recordMapTwo!=null &&!recordMapTwo.isEmpty()) {

							if(recordMapTwo.get("DN_PARENT_IMAGE_ID")!=null){
								liveVM.parentImageId=Long.valueOf(recordMapTwo.get("DN_PARENT_IMAGE_ID").toString());
							}
							if(recordMapTwo.get("DC_IMAGENAME")!=null){
								liveVM.childImageName=recordMapTwo.get("DC_IMAGENAME").toString();
								
							//	String fileNameTwo=recordMapTwo.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
								liveVM.childImageThumb=recordMapTwo.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
								
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					
				}
			//	liveVM.childImageId = Long.valueOf(map.get("liveChildImageId").toString());
				relevanceList.add(liveVM);
				resMap.put(croppedId, relevanceList);
				croppedVM.revelanceList = relevanceList;
				croppedVMs.add(croppedVM);
			}
		}
		
		return croppedVMs;
	}
	
	@RequestMapping(value="/get-relevance-not-duplicated-list" , method = RequestMethod.GET)
	public @ResponseBody List getRelevanceNotDuplicatedList() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1 * notDuplicateDataOfDays);
		String query = "select cropped.DN_ID as 'croppedId',"+
				"live.DN_ID as 'liveId' ,"+
				"cropped.DN_CHILD_IMAGE_ID as 'croppedChildImageId',"+
				"live.DN_CHILD_IMAGE_ID as 'liveChildImageId' "+
				"from tbl_ocr_text_match_result ocr, tbl_de_data live, tbl_de_data cropped "+ 
				"where "+
				"ocr.STATUS_CHANGED_DATE > '"+df.format(c.getTime())+"' and "+
				"ocr.DC_IS_DUPLICATE = '1' and " +
				"ocr.DC_CROPPED_JOBID = cropped.DN_ID and "+ 
				"ocr.DC_LIVE_JOBID = live.DN_ID order by ocr.STATUS_CHANGED_DATE desc";
		List<Map<String,Object>> results =  jt.queryForList(query);
		Map<Long,List<CroppedVM>> resMap = new HashMap<Long, List<CroppedVM>>();
		List<CroppedVM> croppedVMs = new ArrayList<CroppedVM>();
		for(Map<String, Object> map : results) {
			Long croppedId = Long.valueOf(map.get("croppedId").toString());
			if(resMap.get(croppedId) != null) {
				CroppedVM liveVM = new CroppedVM();
				liveVM.id = Long.valueOf(map.get("liveId").toString());
				liveVM.childImageId = Long.valueOf(map.get("liveChildImageId").toString());
				
				try {

					Map<String,Object> recordMapOne=jt.queryForMap("select c.DC_IMAGENAME,c.DN_PARENT_IMAGE_ID from tbl_child_image c where c.DN_ID ="+Long.valueOf(map.get("liveChildImageId").toString()));
					if(recordMapOne!=null &&!recordMapOne.isEmpty()) {
						if(recordMapOne.get("DN_PARENT_IMAGE_ID")!=null){
							liveVM.parentImageId=Long.valueOf(recordMapOne.get("DN_PARENT_IMAGE_ID").toString());
						}
						if(recordMapOne.get("DC_IMAGENAME")!=null){
							liveVM.childImageName=recordMapOne.get("DC_IMAGENAME").toString();
							liveVM.childImageThumb=recordMapOne.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
							
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				if( resMap.get(croppedId).size() < 2 ) {  
					resMap.get(croppedId).add(liveVM); 
				}
			} else {
				CroppedVM croppedVM = new CroppedVM();
				croppedVM.id = croppedId;
				croppedVM.childImageId = Long.valueOf(map.get("croppedChildImageId").toString());
				
				
				try {

					Map<String,Object> recordMap=jt.queryForMap("select c.DC_IMAGENAME,c.DN_PARENT_IMAGE_ID from tbl_child_image c where c.DN_ID ="+Long.valueOf(map.get("croppedChildImageId").toString()));
					if(recordMap!=null &&!recordMap.isEmpty()) {
						if(recordMap.get("DN_PARENT_IMAGE_ID")!=null){
							croppedVM.parentImageId=Long.valueOf(recordMap.get("DN_PARENT_IMAGE_ID").toString());
						}
						if(recordMap.get("DC_IMAGENAME")!=null){
							croppedVM.childImageName=recordMap.get("DC_IMAGENAME").toString();
							croppedVM.childImageThumb=recordMap.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
							
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
				
				List<CroppedVM> relevanceList = new ArrayList<CroppedVM>();
				CroppedVM liveVM = new CroppedVM();
				liveVM.id = Long.valueOf(map.get("liveId").toString());
				liveVM.childImageId = Long.valueOf(map.get("liveChildImageId").toString());
				
				
				try {

					Map<String,Object> recordMapOne=jt.queryForMap("select c.DC_IMAGENAME,c.DN_PARENT_IMAGE_ID from tbl_child_image c where c.DN_ID ="+Long.valueOf(map.get("liveChildImageId").toString()));
					if(recordMapOne!=null &&!recordMapOne.isEmpty()) {
						if(recordMapOne.get("DN_PARENT_IMAGE_ID")!=null){
							liveVM.parentImageId=Long.valueOf(recordMapOne.get("DN_PARENT_IMAGE_ID").toString());
						}
						if(recordMapOne.get("DC_IMAGENAME")!=null){
							liveVM.childImageName=recordMapOne.get("DC_IMAGENAME").toString();
							liveVM.childImageThumb=recordMapOne.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
							
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
				relevanceList.add(liveVM);
				resMap.put(croppedId, relevanceList);
				croppedVM.revelanceList = relevanceList;
				croppedVMs.add(croppedVM);
			}
		}
		
		return croppedVMs;
	}
	
	@RequestMapping(value="/get-all-live-relevance-list" , method = RequestMethod.GET)
	public @ResponseBody List getAllLiveRelevanceList() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1 * liveDataOfDays);
		String query = "select cropped.DN_ID as 'croppedId',"+
				"live.DN_ID as 'liveId' ,"+
				"cropped.DN_CHILD_IMAGE_ID as 'croppedChildImageId',"+
				"live.DN_CHILD_IMAGE_ID as 'liveChildImageId' "+
				"from tbl_ocr_text_match_result ocr, tbl_de_data live, tbl_de_data cropped "+ 
				"where "+
				"ocr.STATUS_CHANGED_DATE > '"+df.format(c.getTime())+"' and "+
				"ocr.DC_IS_DUPLICATE = '0' and " +
				"ocr.DC_CROPPED_JOBID = cropped.DN_ID and "+ 
				"ocr.DC_LIVE_JOBID = live.DN_ID order by ocr.STATUS_CHANGED_DATE desc";
		List<Map<String,Object>> results =  jt.queryForList(query);
		Map<Long,List<CroppedVM>> resMap = new HashMap<Long, List<CroppedVM>>();
		List<CroppedVM> croppedVMs = new ArrayList<CroppedVM>();
		for(Map<String, Object> map : results) {
			Long croppedId = Long.valueOf(map.get("croppedId").toString());
			if(resMap.get(croppedId) != null) {
				CroppedVM liveVM = new CroppedVM();
				liveVM.id = Long.valueOf(map.get("liveId").toString());
				liveVM.childImageId = Long.valueOf(map.get("liveChildImageId").toString());
				
				
				try {

					Map<String,Object> recordMap=jt.queryForMap("select c.DC_IMAGENAME,c.DN_PARENT_IMAGE_ID from tbl_child_image c where c.DN_ID ="+Long.valueOf(map.get("liveChildImageId").toString()));
					if(recordMap!=null &&!recordMap.isEmpty()) {
						if(recordMap.get("DN_PARENT_IMAGE_ID")!=null){
							liveVM.parentImageId=Long.valueOf(recordMap.get("DN_PARENT_IMAGE_ID").toString());
						}
						if(recordMap.get("DC_IMAGENAME")!=null){
							liveVM.childImageName=recordMap.get("DC_IMAGENAME").toString();
							liveVM.childImageThumb=recordMap.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
							
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if( resMap.get(croppedId).size() < 2 ) {  
					resMap.get(croppedId).add(liveVM); 
				}
			} else {
				CroppedVM croppedVM = new CroppedVM();
				croppedVM.id = croppedId;
				croppedVM.childImageId = Long.valueOf(map.get("croppedChildImageId").toString());
				
				

				try {

					Map<String,Object> recordMap=jt.queryForMap("select c.DC_IMAGENAME,c.DN_PARENT_IMAGE_ID from tbl_child_image c where c.DN_ID ="+Long.valueOf(map.get("croppedChildImageId").toString()));
					if(recordMap!=null &&!recordMap.isEmpty()) {
						if(recordMap.get("DN_PARENT_IMAGE_ID")!=null){
							croppedVM.parentImageId=Long.valueOf(recordMap.get("DN_PARENT_IMAGE_ID").toString());
						}
						if(recordMap.get("DC_IMAGENAME")!=null){
							croppedVM.childImageName=recordMap.get("DC_IMAGENAME").toString();
							croppedVM.childImageThumb=recordMap.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
							
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
				List<CroppedVM> relevanceList = new ArrayList<CroppedVM>();
				CroppedVM liveVM = new CroppedVM();
				liveVM.id = Long.valueOf(map.get("liveId").toString());
				liveVM.childImageId = Long.valueOf(map.get("liveChildImageId").toString());
				

				try {

					Map<String,Object> recordMapLive=jt.queryForMap("select c.DC_IMAGENAME,c.DN_PARENT_IMAGE_ID from tbl_child_image c where c.DN_ID ="+Long.valueOf(map.get("liveChildImageId").toString()));
					if(recordMapLive!=null &&!recordMapLive.isEmpty()) {
						if(recordMapLive.get("DN_PARENT_IMAGE_ID")!=null){
							liveVM.parentImageId=Long.valueOf(recordMapLive.get("DN_PARENT_IMAGE_ID").toString());
						}
						if(recordMapLive.get("DC_IMAGENAME")!=null){
							liveVM.childImageName=recordMapLive.get("DC_IMAGENAME").toString();
							liveVM.childImageThumb=recordMapLive.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
							
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
				
				
				relevanceList.add(liveVM);
				resMap.put(croppedId, relevanceList);
				croppedVM.revelanceList = relevanceList;
				croppedVMs.add(croppedVM);
			}
		}
		
		return croppedVMs;
	}
	
	@RequestMapping(value = "/mark-live", method = RequestMethod.POST)
	@ResponseBody public void markLive(
			@RequestBody MarkLiveVM liveVM) {
		String query = "update tbl_de_data d join tbl_de_data live "+
				"set d.DC_AD_CATEGORY = live.DC_AD_CATEGORY,"+
				"d.DC_ADD_COLUMN = live.DC_ADD_COLUMN,"+
				"d.DC_AD_HEADLINE = live.DC_AD_HEADLINE,"+
				"d.DC_AD_ORIENTATION = live.DC_AD_ORIENTATION,"+
				"d.DC_AD_SIZE = live.DC_AD_SIZE,"+
				"d.DC_ADVERTISER_TYPE = live.DC_ADVERTISER_TYPE,"+
				"d.DC_CONTACT_INFO = live.DC_CONTACT_INFO,"+
				"d.DC_CURRENCY = live.DC_CURRENCY,"+
				"d.DC_END_CURRENCY_RANGE = live.DC_END_CURRENCY_RANGE,"+
				"d.DC_END_CURRENCY_RANGE = 0,"+
				"d.DC_JOB_DENSITY = live.DC_JOB_DENSITY,"+
				"d.DC_PAGE_URL = live.DC_PAGE_URL,"+
				"d.DC_OCR_TEXT = live.DC_OCR_TEXT,"+
				"d.DC_START_CURRENCY_RANGE = live.DC_START_CURRENCY_RANGE,"+
				"d.DN_DECOMPANY_ID = live.DN_DECOMPANY_ID,"+
				"d.DC_SEARCH_ADVERTISER_TYPE = live.DC_SEARCH_ADVERTISER_TYPE "+
				"where d.DN_ID = "+liveVM.getCroppedId()+
				" and live.DN_ID = "+liveVM.getLiveId();
		System.out.println(query);
		jt.update(query);
		query = "update tbl_ocr_text_match_result set STATUS_CHANGED_DATE = CURDATE() where DC_CROPPED_JOBID = "+liveVM.getCroppedId()+" and DC_LIVE_JOBID = "+liveVM.getLiveId();
		jt.update(query);
		query = "select count(*) from tbl_de_data where DN_DECOMPANY_ID IS NULL and DE_JOB_ID = (select DE_JOB_ID from tbl_de_data where DN_ID = "+liveVM.getCroppedId()+")";
		int notProcessed = jt.queryForInt(query);
		if(notProcessed == 0) {
			System.out.println("in lopp");
			query = "update tbl_parent_image set DN_STATUS = 2 where DN_ID = (select DN_PARENT_IMAGE_ID from tbl_de_data where DN_ID = "+liveVM.getCroppedId()+")";
			jt.update(query);
			query = "update tbl_de_job set DN_STATUS = 1 where DN_ID = (select DE_JOB_ID from tbl_de_data where DN_ID = "+liveVM.getCroppedId()+") ";
			jt.update(query);
			//query = "update tbl_ocr_text_match_result set STATUS_CHANGED_DATE = CURDATE() where ";
		}
	}
	
	@RequestMapping(value = "/mark-all-live", method = RequestMethod.POST)
	@ResponseBody public void markAllLive(
			@RequestBody List<MarkLiveVM> liveVMs) {
		
	    
		for(MarkLiveVM liveVM : liveVMs) {
			
			System.out.println("crop id is "+liveVM.getCroppedId());
			System.out.println("live id is"+liveVM.getLiveId());
			
			String query = "update tbl_de_data d join tbl_de_data live "+
					"set d.DC_AD_CATEGORY = live.DC_AD_CATEGORY,"+
					"d.DC_ADD_COLUMN = live.DC_ADD_COLUMN,"+
					"d.DC_AD_HEADLINE = live.DC_AD_HEADLINE,"+
					"d.DC_AD_ORIENTATION = live.DC_AD_ORIENTATION,"+
					"d.DC_AD_SIZE = live.DC_AD_SIZE,"+
					"d.DC_ADVERTISER_TYPE = live.DC_ADVERTISER_TYPE,"+
					"d.DC_CONTACT_INFO = live.DC_CONTACT_INFO,"+
					"d.DC_CURRENCY = live.DC_CURRENCY,"+
					"d.DC_END_CURRENCY_RANGE = live.DC_END_CURRENCY_RANGE,"+
					"d.DC_END_CURRENCY_RANGE = 0,"+
					"d.DC_JOB_DENSITY = live.DC_JOB_DENSITY,"+
					"d.DC_PAGE_URL = live.DC_PAGE_URL,"+
					"d.DC_OCR_TEXT = live.DC_OCR_TEXT,"+
					"d.DC_START_CURRENCY_RANGE = live.DC_START_CURRENCY_RANGE,"+
					"d.DN_DECOMPANY_ID = live.DN_DECOMPANY_ID,"+
					"d.DC_SEARCH_ADVERTISER_TYPE = live.DC_SEARCH_ADVERTISER_TYPE "+
					"where d.DN_ID = "+liveVM.getCroppedId()+
					" and live.DN_ID = "+liveVM.getLiveId();
			System.out.println(query);
			jt.update(query);
			query = "update tbl_ocr_text_match_result set STATUS_CHANGED_DATE = CURDATE() where DC_CROPPED_JOBID = "+liveVM.getCroppedId()+" and DC_LIVE_JOBID = "+liveVM.getLiveId();
			jt.update(query);
			query = "select count(*) from tbl_de_data where DN_DECOMPANY_ID IS NULL and DE_JOB_ID = (select DE_JOB_ID from tbl_de_data where DN_ID = "+liveVM.getCroppedId()+")";
			int notProcessed = jt.queryForInt(query);
			
			System.out.println("count is "+notProcessed);
			if(notProcessed == 0) {
				System.out.println("in loop");
				query = "update tbl_parent_image set DN_STATUS = 2 where DN_ID = (select DN_PARENT_IMAGE_ID from tbl_de_data where DN_ID = "+liveVM.getCroppedId()+")";
				jt.update(query);
				query = "update tbl_de_job set DN_STATUS = 1  where DN_ID = (select DE_JOB_ID from tbl_de_data where DN_ID = "+liveVM.getCroppedId()+")";
				jt.update(query);
			}
		}
	}
	
	@RequestMapping(value = "/mark-not-duplicate", method = RequestMethod.POST)
	@ResponseBody public void markNotDuplicate(@RequestBody MarkLiveVM liveVM) {
		String query = "update tbl_ocr_text_match_result set DC_IS_DUPLICATE='1', STATUS_CHANGED_DATE = CURDATE() where DC_CROPPED_JOBID = "+liveVM.getCroppedId() + " and DC_LIVE_JOBID = "+liveVM.getLiveId();
		jt.update(query);
	}
	
	@RequestMapping(value = "/mark-all-not-duplicate", method = RequestMethod.POST)
	@ResponseBody public void markAllNotDuplicate(@RequestBody List<MarkLiveVM> liveVMs) {
		for(MarkLiveVM liveVM : liveVMs) {
			String query = "update tbl_ocr_text_match_result set DC_IS_DUPLICATE='1', STATUS_CHANGED_DATE = CURDATE() where DC_CROPPED_JOBID = "+liveVM.getCroppedId()+" and DC_LIVE_JOBID = "+liveVM.getLiveId();
			jt.update(query);
		}
	}
	
	@RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
	@ResponseBody public List<userVM> getAllUsers() {
		
		List<userVM> users = jt.query(
		        "select * from tbl_user where DN_USER_TYPE <> 1 and DB_DELETED = 0",
		        new RowMapper<userVM>() {
		            public userVM mapRow(ResultSet rs, int rowNum) throws SQLException {
		            	userVM user = new userVM();
		                user.email=rs.getString("DC_EMAIL");
		                user.firstName=rs.getString("DC_FIRSTNAME");
		                user.lastName=rs.getString("DC_LASTNAME");
		                user.status=rs.getString("DB_ACTIVE");
		                user.roleId= Integer.parseInt(rs.getString("DN_USER_TYPE"));
		                user.userId=Long.parseLong(rs.getString("DN_ID"));
		                user.address = rs.getString("DC_ADDRESS_LINE_1");
		                user.city = rs.getString("DC_CITY");
		                user.country = rs.getString("DC_COUNTRY");
		                user.phone = rs.getString("DC_PHONE");
		                user.state = rs.getString("DC_STATE");
		                user.zipCode = rs.getString("DC_ZIPCODE");
		                user.roleName = jt.queryForObject(
		                        "select DC_TYPE_NAME from tbl_user_type where DN_TYPEID = ?",
		                        new Object[]{user.roleId}, String.class);
		                
		                return user;
		            }
		        });
		return users;
	}
	
	@RequestMapping(value = "/getAllRoles", method = RequestMethod.GET)
	@ResponseBody public List<userRoleVM> getAllRoles() {
		
		List<userRoleVM> roles = jt.query(
		        "select * from tbl_user_type where DN_TYPEID <> 1",
		        new RowMapper<userRoleVM>() {
		            public userRoleVM mapRow(ResultSet rs, int rowNum) throws SQLException {
		            	userRoleVM role = new userRoleVM();
		                role.roleName=rs.getString("DC_TYPE_NAME");
		                role.roleId=Long.parseLong(rs.getString("DN_TYPEID"));
		                return role;
		            }
		        });
		return roles;
	}
	
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	@ResponseBody public String addUser(@RequestBody userAddVM list){
		int countOfActorsNamedJoe = jt.queryForObject(
		        "select count(*) from tbl_user where DC_EMAIL = ?", Integer.class, list.email);
		System.out.println("Count : "+countOfActorsNamedJoe);
		
		if(countOfActorsNamedJoe != 0)
		{
			return "Email Already Exist..";
		}else{
			Date date = new Date();
			jt.update(
			        "insert into tbl_user (DC_EMAIL, DC_PASSWORD, DN_USER_TYPE, DC_FIRSTNAME, DC_LASTNAME, DC_PHONE, DN_CREATED_BY, DD_CREATED_ON, DN_DELETED_BY, DC_ADDRESS_LINE_1, DC_CITY, DC_COUNTRY, DC_STATE, DC_ZIPCODE, DB_ACTIVE, DC_FORGOTPWD_ENCP, DN_GENDER, DC_USER_IMAGE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
			        list.email, list.password, Integer.parseInt(list.roleId), list.firstName, list.lastName, list.phone, Long.parseLong(list.createdBy), date, 0, list.address, list.city, list.country, list.state, list.zipCode, 0, "NULL", Integer.parseInt(list.gender), "NULL");
			return "";
		}
	}
	
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	@ResponseBody public void updateUser(@RequestBody userAddVM list){
		jt.update(
		        "update tbl_user set DC_EMAIL = ?, DC_PASSWORD = ? , DN_USER_TYPE = ?, DC_FIRSTNAME = ?, DC_LASTNAME = ?, DC_PHONE = ?, DC_ADDRESS_LINE_1 = ?, DC_CITY = ?, DC_COUNTRY = ?, DC_STATE = ?, DC_ZIPCODE = ?, DN_GENDER = ? where DN_ID = ?",
		        list.email, list.password, Integer.parseInt(list.roleId), list.firstName, list.lastName, list.phone, list.address, list.city, list.country, list.state,list.zipCode, Integer.parseInt(list.gender), list.userId);
	}
	
	@RequestMapping(value = "/addUserGroup", method = RequestMethod.POST)
	@ResponseBody public void addUserGroup(@RequestBody userAddGroupVM list){
		jt.update(
		        "insert into tbl_user_type (DC_TYPE_NAME) values (?)",
		        list.userType);
	}
	
	@RequestMapping(value = "/activeUser", method = RequestMethod.POST)
	@ResponseBody public List<String> activeUser(@RequestBody List<Long> list){
		 List<String> status = new ArrayList<String>();
		for(Long id : list) {
			userAddVM userData = jt.queryForObject(
			        "select * from tbl_user where DN_ID = ?", new Object[]{id},
			        new RowMapper<userAddVM>() {
			            public userAddVM mapRow(ResultSet rs, int rowNum) throws SQLException {
			            	userAddVM role = new userAddVM();
			                role.firstName=rs.getString("DC_FIRSTNAME");
			                role.active=rs.getInt("DB_ACTIVE");
			                return role;
			            }
			        });
			
				if(userData.active == 1)
				{
					status.add(userData.firstName+" "+"has been already Activated.");
					
				}else{
					jt.update(
						    "update tbl_user set DB_ACTIVE = ? where DN_ID = ?", 1, id);
					status.add(userData.firstName+" "+"has been Activated Successfully.");
				}
			
		}
		return status;
		
	}
	
	@RequestMapping(value = "/deactiveUser", method = RequestMethod.POST)
	@ResponseBody public List<String> deactiveUser(@RequestBody List<Long> list){
		 List<String> status = new ArrayList<String>();
		for(Long id : list) {
			userAddVM userData = jt.queryForObject(
			        "select * from tbl_user where DN_ID = ?", new Object[]{id},
			        new RowMapper<userAddVM>() {
			            public userAddVM mapRow(ResultSet rs, int rowNum) throws SQLException {
			            	userAddVM role = new userAddVM();
			                role.firstName=rs.getString("DC_FIRSTNAME");
			                role.active=rs.getInt("DB_ACTIVE");
			                return role;
			            }
			        });
			
				if(userData.active == 0)
				{
					status.add(userData.firstName+" "+"has been already Deactivated.");
					
				}else{
					jt.update(
						    "update tbl_user set DB_ACTIVE = ? where DN_ID = ?", 0, id);
					status.add(userData.firstName+" "+"has been Deactivated Successfully.");
				}
			
		}
		return status;
		
	}
	
	
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	@ResponseBody public void deleteUser(@RequestBody List<Long> list, @RequestParam ("loginUserId") String loginUserId){
		for(Long id : list) {
			jt.update(
			        "update tbl_user set DB_DELETED = ?,DN_DELETED_BY = ? where DN_ID = ?",
			        1,Long.parseLong(loginUserId),id);
			
		}
		System.out.println("Add User List"+list);
	}
	
	
	@RequestMapping(value = "/getUserDetailsById", method = RequestMethod.GET)
	@ResponseBody public userVM getUserDetailsById(@RequestParam("userId") Long id){
		userVM userData = jt.queryForObject(
		        "select * from tbl_user where DN_ID = ?", new Object[]{id},
		        new RowMapper<userVM>() {
		            public userVM mapRow(ResultSet rs, int rowNum) throws SQLException {
		            	userVM user = new userVM();
		            	user.email=rs.getString("DC_EMAIL");
		                user.firstName=rs.getString("DC_FIRSTNAME");
		                user.lastName=rs.getString("DC_LASTNAME");
		                user.status=rs.getString("DB_ACTIVE");
		                user.roleId= Integer.parseInt(rs.getString("DN_USER_TYPE"));
		                user.userId=Long.parseLong(rs.getString("DN_ID"));
		                user.address = rs.getString("DC_ADDRESS_LINE_1");
		                user.city = rs.getString("DC_CITY");
		                user.country = rs.getString("DC_COUNTRY");
		                user.phone = rs.getString("DC_PHONE");
		                user.state = rs.getString("DC_STATE");
		                user.zipCode = rs.getString("DC_ZIPCODE");
		                user.gender = rs.getString("DN_GENDER");
		                user.password = rs.getString("DC_PASSWORD");
		                user.roleName = jt.queryForObject(
		                        "select DC_TYPE_NAME from tbl_user_type where DN_TYPEID = ?",
		                        new Object[]{user.roleId}, String.class);
		                
		                return user;
		            }
		        });
		return userData;
	}
	
	
	@RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
	@ResponseBody public void updateGroup(@RequestBody userAddGroupVM list){
		jt.update(
		        "update tbl_user_type set DC_TYPE_NAME = ? where DN_TYPEID = ?",
		         list.userType, list.id);
	}
	
	public static class CroppedVM {
		public Long id;
		public Long childImageId;
		public Long parentImageId;
		public String childImageName;
		public String childImageThumb;
		public String cropWidth;
		public String cropLength;
		public String liveWidth;
		public String liveLength;
		
		public List<CroppedVM> revelanceList;
	}
	
	public static class userVM {
		public Long userId;
		public String email;
		public int roleId;
		public String roleName;
		public String status;
		public String firstName;
		public String lastName;
		public String phone;
		public String address;
		public String city;
		public String state;
		public String country;
		public String zipCode;
		public String password;
		public String gender;
	}

	public static class userRoleVM {
		public Long roleId;
		public String roleName;
	}
	
	public static class userAddVM {
		
		public Long userId;
		public String email;
		public String password;
		public String roleId;
		public String firstName;
		public String lastName;
		public String phone;
		public int deleteId;
		public String address;
		public String dob;
		public String city;
		public String country;
		public String state;
		public String zipCode;
		public int active;
		public String gender;
		public String userImage;
		public String createdBy;
		public String createdOn;
		public String deletedBy;
		public String deletedOn;
		public String forgotPassEn;
		
	}
	
	public static class userAddGroupVM{
		
		public Long id;
		public String userType;
	}
	
}
