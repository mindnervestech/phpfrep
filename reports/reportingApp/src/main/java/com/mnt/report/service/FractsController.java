package com.mnt.report.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
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
	
	public static class CroppedVM {
		public Long id;
		public Long childImageId;
		public Long parentImageId;
		public String childImageName;
		public String childImageThumb;
		public List<CroppedVM> revelanceList;
	}
}
