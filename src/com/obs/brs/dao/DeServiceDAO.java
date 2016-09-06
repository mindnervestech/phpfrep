package com.obs.brs.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.obs.brs.controller.ScoreData;
import com.obs.brs.model.DataEntry;
import com.obs.brs.model.DeCompany;
import com.obs.brs.model.DeJob;
import com.obs.brs.model.OcrTextMatchResult;
import com.obs.brs.model.ParentImage;

/**
 * 
 * @author Jeevanatham
 *
 */
public class DeServiceDAO implements IDeServiceDAO {

	private SessionFactory sessionFactory;
	
    private JdbcTemplate jt;
    
	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	/**
	 * Get Hibernate Session Factory
	 * 
	 * @return SessionFactory - Hibernate Session Factory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Set Hibernate Session Factory
	 * 
	 * @param SessionFactory - Hibernate Session Factory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/* (non-Javadoc)
	 * @see ccom.obs.brs.dao.DeServiceDAO#addDataEntry(com.obs.brs.model.DataEntry)
	 */
	@Override
	public Long addDataEntry(DataEntry dataEntry) {
		return (Long)getSessionFactory().getCurrentSession().save(dataEntry);
	}
	
	/* (non-Javadoc)
	 * @see ccom.obs.brs.dao.DeServiceDAO#updateDataEntry(com.obs.brs.model.DataEntry)
	 */
	@Override
	public void updateDataEntry(DataEntry dataEntry) {
		getSessionFactory().getCurrentSession().update(dataEntry);
	}

	/* (non-Javadoc)
	 * @see ccom.obs.brs.dao.DeServiceDAO#deleteDataEntry(com.obs.brs.model.DataEntry)
	 */
	@Override
	public void deleteDataEntry(DataEntry dataEntry) {
		getSessionFactory().getCurrentSession().delete(dataEntry);
	}

	/* (non-Javadoc)
	 * @see ccom.obs.brs.dao.DeServiceDAO#getDataEntryById(com.obs.brs.model.DataEntry)
	 */
	@Override
	public DataEntry getDataEntryById(long id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from DataEntry where id=?")
				.setParameter(0,id).list();
		return list!=null?(DataEntry)list.get(0):null;	
	}

	/* (non-Javadoc)
	 *  @see ccom.obs.brs.dao.DeServiceDAO#getDataEntry(com.obs.brs.model.DataEntry)
	 */
	@Override
	public List<DataEntry> getDataEntry() {
		List<DataEntry> list = getSessionFactory().getCurrentSession().createQuery("from DataEntry").list();
		return list;
	}
	/**
	 * 
	 */
	@Override
	public List<DataEntry> getDeBySeachCriteria(long userId) {
		String 	SQL = "From DataEntry as m where m.isDeleted=0 and m.created_by.id='"+userId+"'";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}

	/**
	 * @Override
	 */
	public List<DataEntry> getDeBySeachCriteria(long userId,String searchValue) {
		String appendStr = "";
		String statusAppendStr = "";
		if(StringUtils.isNotBlank(searchValue)){
			appendStr  = appendStr+" m.companyName LIKE '%"+searchValue+"%'";
			appendStr  = "AND ( "+appendStr+" )";
		}
		String 	SQL = "From DataEntry as m where m.isDeleted=0 and m.created_by.id='"+userId+"'"+appendStr;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}

	@Override
	public Long addDeCompany(DeCompany deCompany) {
		return (Long)getSessionFactory().getCurrentSession().save(deCompany);
	}

	@Override
	public void updateDeCompany(DeCompany deCompany) {
		getSessionFactory().getCurrentSession().update(deCompany);

	}

	@Override
	public void deleteDeCompany(DeCompany deCompany) {
		getSessionFactory().getCurrentSession().delete(deCompany);

	}

	@Override
	public DeCompany getDeCompanyById(long id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from DeCompany where id=?")
				.setParameter(0,id).list();
		return list!=null?(DeCompany)list.get(0):null;	
	}

	@Override
	public List<DeCompany> getDeCompany() {
		List<DeCompany> list = getSessionFactory().getCurrentSession().createQuery("from DeCompany where isDeleted=0").list();
		return list;
	}

	@Override
	public List<DeCompany> getDeCompanyBySeachCriteria(long userId) {
		String 	SQL = "From DeCompany as m where m.isDeleted=0 and m.created_by.id='"+userId+"'";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}

	@Override
	public List<DeCompany> getDeCompanyBySeachCriteria(String searchValue) {
		String 	SQL = "From DeCompany as m where m.isDeleted=0 and m.companyName='"+searchValue+"'";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}

	@Override
	public List<DeCompany> getDeCompanyBySeachCriteria(long userId,
			String searchValue) {
		String appendStr = "";
		String statusAppendStr = "";
		if(StringUtils.isNotBlank(searchValue)){
			appendStr  = appendStr+" m.companyName LIKE '%"+searchValue+"%'";
			appendStr  = "AND ( "+appendStr+" )";
		}
		String 	SQL = "From DeCompany as m where m.isDeleted=0 and m.created_by.id='"+userId+"'"+appendStr;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}

	@Override
	public Long addDeJob(DeJob deJob) {
		return (Long)getSessionFactory().getCurrentSession().save(deJob);
	}

	@Override
	public void updateDeJob(DeJob deJob) {
		getSessionFactory().getCurrentSession().update(deJob);

	}

	@Override
	public void deleteDeJob(DeJob deJob) {
		getSessionFactory().getCurrentSession().delete(deJob);

	}

	@Override
	public DeJob getDeJobById(long id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from DeJob where id=?")
				.setParameter(0,id).list();
		return list!=null?(DeJob)list.get(0):null;	
	}

	@Override
	public List<DeJob> getDeJob() {
		List<DeJob> list = getSessionFactory().getCurrentSession().createQuery("from DeJob").list();
		return list;
	}

	@Override
	public List<DeJob> getDeJobBySeachCriteria() {
		String 	SQL = "From DeJob as m where m.isDeleted=0 and m.status = 0 and m.parentImage.status = 2";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
	
	@Override
	public List<DeJob> getDeJobBySeachCriteriaGallery() {
		String 	SQL = "From DeJob as m where m.isDeleted=0 and m.status = 0 and m.parentImage.status = 2";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
	
	@Override
	public List<DeJob> getDeJobBySeachCriteria(String searchValue) {
		String appendStr = "";
		String statusAppendStr = "";
		if(StringUtils.isNotBlank(searchValue)){
			
			String regex = "[0-9]+"; 
			if(searchValue.matches(regex)){
				System.out.println(searchValue);
				appendStr  = appendStr+" m.id LIKE '%"+Long.parseLong(searchValue.trim())+"%'";
			}else{
				appendStr  = appendStr+" m.parentImage.imageName LIKE '%"+searchValue+"%'";
				appendStr  = appendStr+" OR m.parentImage.publicationTitle.publicationTitle LIKE '%"+searchValue+"%'";
				appendStr  = appendStr+" OR m.parentImage.section.publicationTitle LIKE '%"+searchValue+"%'";
				appendStr  = appendStr+" OR m.parentImage.issueDate = '"+searchValue+"'";
			}
			appendStr  = "AND ( "+appendStr+" )";
		}
		/*if(StringUtils.isNotBlank(issueDateString)){
			appendStr  = appendStr+" m.parentImage.issueDate = '"+issueDateString+"'";
			appendStr  = " AND ( "+appendStr+" )";
		}*/
		String 	SQL = "From DeJob as m where m.isDeleted=0 and m.status = 0 and m.parentImage.status = 2"+appendStr;
		//System.out.println("SQL"+SQL);
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}

	@Override
	public DeJob getDeJobByParentImageId(long id) {
		List <DeJob> deJobList = getSessionFactory().getCurrentSession().createQuery("From DeJob as m where m.isDeleted=0 and  m.parentImage.id="+id).list();
		if (deJobList.size() > 0 ) {
			return deJobList.get(0);
		}
		return null;
	}

	@Override
	public DataEntry getDataEntryByParentImageId(long id) {
		List <DataEntry> dataEntryList = getSessionFactory().getCurrentSession().createQuery("From DataEntry as m where m.isDeleted=0 and  m.parentImage.id='"+id+"' ORDER BY m.id ASC").list();
		if (dataEntryList.size() > 0 ) {
			return dataEntryList.get(0);
		}
		return null;
	}

	@Override
	public DeCompany getDeCompanyByParentImageId(long deJobId) {
		List <DeCompany> deCompanyList = getSessionFactory().getCurrentSession().createQuery("From DeCompany as m where m.isDeleted=0 and  m.parentImage.id="+deJobId).list();
		if (deCompanyList.size() > 0 ) {
			return deCompanyList.get(0);
		}
		return null;
	}

	@Override
	public DataEntry getDataEntryByChildImageId(long id) {
		List <DataEntry> deChildList = getSessionFactory().getCurrentSession().createQuery("From DataEntry as m where m.isDeleted=0 and  m.childImage.id="+id).list();
		if (deChildList.size() > 0 ) {
			return deChildList.get(0);
		}
		return null;
	}

	@Override
	public List<DataEntry> getImagesByParent(long parId) {
		String 	SQL = "From DataEntry as m where m.isDeleted=0 and m.parentImage.id='"+parId+"'";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}

	@Override
	public DataEntry getNextDataEntry(long baseId,long jobid) {
		List <DataEntry> dataEntryList = getSessionFactory().getCurrentSession().createQuery("From DataEntry as m where m.isDeleted=0 and m.id >'"+baseId+"' and m.deJobid='"+jobid+"'"+"ORDER BY m.id ASC").list();
		if (dataEntryList!=null && dataEntryList.size() > 0 ) {
			return dataEntryList.get(0);
		}
		return null;
	}

	@Override
	public DataEntry getPrevDataEntry(long baseId, long jobid) {
		List <DataEntry> dataEntryList = getSessionFactory().getCurrentSession().createQuery("From DataEntry as m where m.isDeleted=0 and m.id < '"+baseId+"' and m.deJobid='"+jobid+"'"+" ORDER BY m.id DESC").list();
		if (dataEntryList.size() > 0 ) {
			return dataEntryList.get(0);
		}
		return null;
	}

	@Override
	public DataEntry getCompanyByDataEntryId(long deDataId) {
		List <DataEntry> dataEntryList = getSessionFactory().getCurrentSession().createQuery("From DataEntry as m where m.isDeleted=0 and  m.id  ='"+deDataId+"' and m.deCompany.id != null").list();
		if (dataEntryList.size() > 0 ) {
			return dataEntryList.get(0);
		}
		return null;
	}

	@Override
	public DeCompany getDeCompanySeachByCompanyName(String searchValue) {
		List <DeCompany> deCompanyList = getSessionFactory().getCurrentSession().createQuery("From DeCompany as m where m.isDeleted=0 and  m.companyName='"+searchValue+"'").list();
		if (deCompanyList.size() > 0 ) {
			return deCompanyList.get(0);
		}
		return null;
	}

	@Override
	public DeCompany getDeCompanySeachByCompanyNameSaveId(long searchValue) {
		List <DeCompany> deCompanyList = getSessionFactory().getCurrentSession().createQuery("From DeCompany as m where m.isDeleted=0 and  m.id='"+searchValue+"'").list();
		if (deCompanyList.size() > 0 ) {
			return deCompanyList.get(0);
		}
		return null;
	}

	
	
	@Override
	public DeCompany getDeCompanyNameByCompanyNameWithId(long id,
			String companyName) {
		List<DeCompany> deCompanyList = getSessionFactory().getCurrentSession().createQuery("from DeCompany where companyName=? and isDeleted=0  and id!="+id).setParameter(0, companyName).list();
		if (deCompanyList.size() > 0 ) {
			return deCompanyList.get(0);
		}
		return null;
		
	}

	@Override
	public DeCompany getDeCompanyNameByCompanyName(String companyName) {
		List<DeCompany> deCompanyList = getSessionFactory().getCurrentSession().createQuery("from DeCompany where lower(companyName)=? and isDeleted=0 ").setParameter(0, companyName.toLowerCase()).list();
		if (deCompanyList.size() > 0 ) {
			return deCompanyList.get(0);
		}
		return null;
	}

	@Override
	public List<DeJob> getDeJobByTypeSeachCriteria(int publicationStatus) {
		String 	SQL = "From DeJob as m where m.isDeleted=0 and  m.parentImage.publicationTitle.publicationType="+publicationStatus;
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}

	@Override
	 public List<DataEntry> getDeJobByQC() {
	  /*String  SQL = "From DataEntry as m where m.deCompany != null and m.isDeleted=0 and m.isApproved=0";*/
		String SQL = "select pub.dc_publication_title, " +
				"COUNT((CASE WHEN(da.db_isapproved=1) THEN da.dn_id ELSE NULL END )) AS approved0, " +
				"COUNT((CASE WHEN(da.db_isapproved=2) THEN da.dn_id ELSE NULL END )) AS approved1, " +
				"COUNT((CASE WHEN(da.db_isapproved=3) THEN da.dn_id ELSE NULL END )) AS approved2,  pub.dn_id  FROM tbl_de_data da LEFT OUTER JOIN tbl_parent_image pa ON da.dn_parent_image_id=pa.dn_id LEFT OUTER JOIN tbl_publication pub  ON pa.dc_publication_title=pub.dn_id WHERE da.db_deleted = 0 GROUP BY pa.dc_publication_title ";
	  return getSessionFactory().getCurrentSession().createSQLQuery(SQL).list();
	 }
	
	@Override
	 public List<DataEntry> getIssueDateByPublication(String publicationId) {
	  /*String  SQL = "From DataEntry as m where m.deCompany != null and m.isDeleted=0 and m.isApproved=0";*/
		String SQL = "select pi.DD_ISSUE_DATE from tbl_parent_image pi join tbl_de_data da on pi.DN_ID = da.DN_PARENT_IMAGE_ID where da.DB_DELETED = 0 AND pi.DC_PUBLICATION_TITLE = "+publicationId;
	  return getSessionFactory().getCurrentSession().createSQLQuery(SQL).list();
	 }

	 @Override
	 public List<DataEntry> geQcJobBySeachCriteria(int status,String searchValue,String publicationId,String issueDatePubSearch,String createdBy) {
	  String appendStr = "";
	  String appendDate = "";
	  String appendQuote = "";
	  String appendStatus = "";
	  String appendPublicationID = "";
	  String appendCreateBy = "";
	  if(StringUtils.isNotBlank(searchValue)){
	   appendStr  = appendStr+"m.parentImage.imageName LIKE '%"+searchValue+"%'";
	   appendStr  = appendStr+" OR m.parentImage.publicationTitle.publicationTitle LIKE '%"+searchValue+"%'";
	   appendStr  = appendStr+" OR m.parentImage.section.publicationTitle LIKE '%"+searchValue+"%'";
	   appendStr  = appendStr+" OR m.created_by.firstName = '"+searchValue+"'";
	   appendStr  = " AND ( "+appendStr+" )";
	   
	  } 
	  
	  if(publicationId != null && publicationId != "" && !publicationId.equals("") ){
		  appendPublicationID = " AND m.parentImage.publicationTitle = "+publicationId;
		  appendQuote = "'";
	  }
	  
	  if(status > 0){
		  appendStatus = " AND m.isApproved ="+" "+status;
		  appendQuote = "";
	  }
	
	  if(!issueDatePubSearch.equals("") && !issueDatePubSearch.equals(null)){
		  appendDate = " AND m.parentImage.issueDate = '";
		  appendQuote = "'";
	  }
	  
	  if(createdBy!=null && !createdBy.isEmpty()) {
		  appendCreateBy = " AND m.created_by.id = "+createdBy;
	  }
	  
	  String  SQL = "From DataEntry as m " +""+" where m.deCompany != null and m.isDeleted=0 "+appendCreateBy+appendPublicationID+appendStatus+appendDate+issueDatePubSearch+appendQuote+""+appendStr;
	  System.out.println("SQL:"+SQL);
	  return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	 } 

	 @Override
	 public List<DataEntry> geQcJobBySeach(String publicationId, String issueDatePubSearch,String createdBy) {
	  String appendDate = "";
	  String appendQuote = "";
	  String appendPublicationId = "";
	  String appendCreatedBy = "";
	  if(!issueDatePubSearch.equals("") && !issueDatePubSearch.equals(null)){
		  appendDate = "AND m.parentImage.issueDate = '";
		  appendQuote = "'";
	  }
	  if(publicationId != null && publicationId != "" && !publicationId.equals("") ){
		  appendPublicationId = "AND m.parentImage.publicationTitle = "+publicationId;
	  }
	  if(createdBy != null && !createdBy.isEmpty()) {
		  appendCreatedBy = " AND m.created_by.id ="+createdBy+" ";
	  }
	  String  SQL = "From DataEntry as m where m.deCompany != null and m.isDeleted=0 "+appendCreatedBy+appendPublicationId+appendDate+issueDatePubSearch+appendQuote;
	  return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	 }
	 
	 @Override
	 public List<DataEntry> geAllQcJob() {
	  String  SQL = "From DataEntry as m where m.deCompany != null and m.isDeleted=0";
	  //System.out.println("SQL"+SQL);
	  return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	 }
	 
	@Override
	public List<DataEntry> getImagesByJobid(long jobid) {
		String 	SQL = "From DataEntry as m where m.isDeleted=0 and m.deJobid='"+jobid+"' ORDER BY m.id ASC";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}
	
	@Override
	public String getStatusOfChildImageCompletion(long jobid) {
		String 	SQL = "From DataEntry as m where m.isDeleted=0 and m.isDeleted=0 and m.deJobid='"+jobid+"'";
		List<DataEntry> dataEntries = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		long compleated = 0;
		for(DataEntry data : dataEntries){
			if(data.getDeCompany() != null){
				compleated++;
			}
		}
		String status =  compleated+"/"+dataEntries.size()+"";
		return status;
	}

	@Override
	public List getAllDeo() {
		String SQL = "select DN_ID, DC_FIRSTNAME  FROM tbl_user da where DN_USER_TYPE=3 ";
		return getSessionFactory().getCurrentSession().createSQLQuery(SQL).list();
	}

	@Override
	public DataEntry getCurrentEntry(long baseId, long jobid) {
		List <DataEntry> dataEntryList = getSessionFactory().getCurrentSession().createQuery("From DataEntry as m where m.isDeleted=0 and m.id ='"+baseId+"' and m.deJobid='"+jobid+"'"+"ORDER BY m.id ASC").list();
		if (dataEntryList!=null && dataEntryList.size() > 0 ) {
			return dataEntryList.get(0);
		}
		return null;
	}

	@Override
	public List<DeCompany> getDeCompanySeachByCompanyNameId(long searchValue) {
		// TODO Auto-generated method stub
		
		String 	SQL = "From DeCompany as m where m.isDeleted=0 and m.id='"+searchValue+"'";
		
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}

	@Override
	public List<DeCompany> getDeCompanyBySeachCriteriaId(long searchValue) {
		// TODO Auto-generated method stub
	
		String 	SQL = "From DeCompany as m where m.isDeleted=0 and m.id='"+searchValue+"'";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}

	@Override
	public Boolean isRender(int id) {
		String 	SQL = "From DataEntry as m where m.isDeleted=0 and m.isDeleted=0 and m.deJobid='"+id+"'";
		List<DataEntry> dataEntries = getSessionFactory().getCurrentSession().createQuery(SQL).list();
		if(dataEntries.size()==0)
			return false;
		long compleated = 0;
		for(DataEntry data : dataEntries){
			if(data.getDeCompany() != null){
				compleated++;
			}
		}
		
		return compleated == dataEntries.size();
	}

	@Override
	public void sendJobToQC(int id) {
		getSessionFactory().getCurrentSession().createSQLQuery("Update tbl_de_job set DN_STATUS=1 Where DN_ID="+id).executeUpdate();
	}

	@Override
	public void updateParentImage(ParentImage parentImage) {
		getSessionFactory().getCurrentSession().update(parentImage);
	}

	@Override
	public List<DataEntry> getDataEntryByChildImageIds(Set<Long> ids) {
		List <DataEntry> deChildList = getSessionFactory().getCurrentSession().createQuery("From DataEntry as m where m.isDeleted=0 and  m.childImage.id in (:ids)").setParameterList("ids", ids).list();
		return deChildList;
	}
	
	@Override
	public List<DataEntry> getLiveDeData() {
		HashSet<Integer> ids = new HashSet<>();
		ids.add(Integer.parseInt("1"));
		ids.add(Integer.parseInt("2"));
		List <DeJob> deChildList = getSessionFactory().getCurrentSession().createQuery("From DeJob where status in (:ids)").setParameterList("ids", ids).list();
		HashSet <Long> idss = new HashSet<>();
	
		for(DeJob d : deChildList){
			idss.add(d.getId());
		}
		List <DataEntry> deChildLiveList = null;
		if (idss.isEmpty()) {
			deChildLiveList = getSessionFactory().getCurrentSession().createQuery("From DataEntry").list();
		} else {
			deChildLiveList = getSessionFactory().getCurrentSession().createQuery("From DataEntry where deJobid.id in (:idss)").setParameterList("idss", idss).list();
		}
		return deChildLiveList;
	}

	@Override
	public List<DataEntry> getCropedImagesJobs() {
		HashSet<Integer> ids = new HashSet<>();
		ids.add(Integer.parseInt("0"));
		/*ids.add(Integer.parseInt("2"));*/
		List <DeJob> deChildList = getSessionFactory().getCurrentSession().createQuery("From DeJob as m where m.status in (:ids)").setParameterList("ids", ids).list();
		HashSet <Long> idss = new HashSet<>();
	
		for(DeJob d : deChildList){
			idss.add(d.getId());
		}
		
		List <DataEntry> deChildLiveList = getSessionFactory().getCurrentSession().createQuery("From DataEntry as m where m.deJobid.id in (:idss)").setParameterList("idss", idss).list();
		return deChildLiveList;
	}
	
	@Override
	@Transactional
	public void saveOcrTextResult(OcrTextMatchResult ocr) {
		boolean isExists = false;
		
		List <OcrTextMatchResult> ocrTextMatchResultList = getSessionFactory().getCurrentSession().createQuery("From OcrTextMatchResult as m ").list();
		
		for(OcrTextMatchResult o : ocrTextMatchResultList){
			if(o.getCroppedData() == ocr.getCroppedData() && o.getLiveData() == ocr.getLiveData() ){
				isExists = true;
			}	
		}
		
		if(isExists == false){
		  getSessionFactory().getCurrentSession().save(ocr);	
		}
	}
	
	

	@Override
	public List<Map<String, Object>> getDedupeStatusLists() {
		
		String query = "select cropped.id as croppedId,"+
				" cropped.width as cropWidth, "+
				" cropped.length as cropLenght,"+
				" live.width as liveWidth,"+
				" live.length as liveLength,"+
				" live.id as liveId ,"+
				" cropped.childImage as croppedChildImageId,"+
				" live.childImage as liveChildImageId "+
				" from OcrTextMatchResult ocr, DataEntry live, DataEntry cropped "+ 
				" where "+
				" ocr.isDuplicate = '0' and " +
				"ocr.statusChangedDate IS NULL and "+
				"ocr.croppedData = cropped.id and "+ 
				"ocr.liveData = live.id and "+ 
				"cropped.deCompany IS NULL";
		
		List<Map<String,Object>> results = getSessionFactory().getCurrentSession().createQuery(query).list();

		/*String query = "select cropped.DN_ID as 'croppedId',"+
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
		*/
		
	//	List<Map<String,Object>> results =  jt.queryForList(query);
		return results;
		
	}
	
	@Override
	public List<ScoreData> getDeoByReleavance() {
		List<ScoreData> scoreDatas = new ArrayList<ScoreData>();
		
		List <OcrTextMatchResult> ocrTextMatchResultList = getSessionFactory().getCurrentSession().createQuery("From OcrTextMatchResult as m where m.statusChangedDate IS NULL ").list();
		HashSet<Long> idss = new HashSet<>();
		for(OcrTextMatchResult o :ocrTextMatchResultList){
			if(o.isDuplicate() == false){
				idss.add(o.getCroppedData());	
			}
		}
		
		//System.out.println("Data In file : "+idss);
	
		for(Long o1 : idss){
			HashSet<Long>  id = new HashSet<>();
		    id.add(o1);
		    //
		    List<DataEntry> dataEntries = getSessionFactory().getCurrentSession().createQuery("From DataEntry as m where m.id in (:id) and m.deCompany IS NULL").setParameterList("id", id).list();
		  
		    if(dataEntries.size() > 0) {
				String 	SQL = "From OcrTextMatchResult as m where m.croppedData ='"+o1+"'";
				List <OcrTextMatchResult>  ocrTextMatchResultSubList=  getSessionFactory().getCurrentSession().createQuery(SQL).list();
			
				HashSet<Long>  sublistIds = new HashSet<>();
		   
				for(OcrTextMatchResult subList : ocrTextMatchResultSubList){
		    		sublistIds.add(subList.getLiveData());
		    	}
				
				List<DataEntry> dataSubEntries = new ArrayList<>();
		
				if(sublistIds.size() != 0){
					dataSubEntries = getSessionFactory().getCurrentSession().createQuery("From DataEntry as m where m.id in (:sublistIds)").setParameterList("sublistIds", sublistIds).list();
		    	}
				
				List<DataEntry> dataE = new ArrayList<>();
				if(dataSubEntries.size() >=2){
					
					dataE.add(dataSubEntries.get(0));
					dataE.add(dataSubEntries.get(1));
				}else{
					dataE.add(dataSubEntries.get(0));
				}
				ScoreData results = new ScoreData();
		    	results.dEntry = dataEntries.get(0);
		    	results.dataEntry = dataE;
		    	scoreDatas.add(results);
		    
			}
		 }
		//System.out.println("size:"+scoreDatas.size());
		return scoreDatas;
	}
	
}
