package com.obs.brs.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;

import com.obs.brs.model.DeCompany;
import com.obs.brs.model.DeJob;
import com.obs.brs.model.DataEntry;

/**
 * 
 * @author Jeevanatham
 *
 */
public class DeServiceDAO implements IDeServiceDAO {

	private SessionFactory sessionFactory;
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
		String 	SQL = "From DeJob as m where m.isDeleted=0";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}

	@Override
	public List<DeJob> getDeJobBySeachCriteria(String searchValue) {
		String appendStr = "";
		String statusAppendStr = "";
		if(StringUtils.isNotBlank(searchValue)){
			appendStr  = appendStr+" m.parentImage.imageName LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.parentImage.publicationTitle.publicationTitle LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.parentImage.section.publicationTitle LIKE '%"+searchValue+"%'";
			appendStr  = appendStr+" OR m.parentImage.issueDate = '"+searchValue+"'";
			appendStr  = "AND ( "+appendStr+" )";
		}
		/*if(StringUtils.isNotBlank(issueDateString)){
			appendStr  = appendStr+" m.parentImage.issueDate = '"+issueDateString+"'";
			appendStr  = " AND ( "+appendStr+" )";
		}*/
		String 	SQL = "From DeJob as m where m.isDeleted=0"+appendStr;
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
		List<DeCompany> deCompanyList = getSessionFactory().getCurrentSession().createQuery("from DeCompany where companyName=? and isDeleted=0 ").setParameter(0, companyName).list();
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
	 public List<DataEntry> geQcJobBySeachCriteria(int status,String searchValue,String publicationId,String issueDatePubSearch) {
	  String appendStr = "";
	  String appendDate = "";
	  String appendQuote = "";
	  String appendStatus = "";
	  if(StringUtils.isNotBlank(searchValue)){
	   appendStr  = appendStr+"m.parentImage.imageName LIKE '%"+searchValue+"%'";
	   appendStr  = appendStr+" OR m.parentImage.publicationTitle.publicationTitle LIKE '%"+searchValue+"%'";
	   appendStr  = appendStr+" OR m.parentImage.section.publicationTitle LIKE '%"+searchValue+"%'";
	   appendStr  = appendStr+" OR m.created_by.firstName = '"+searchValue+"'";
	   appendStr  = " AND ( "+appendStr+" )";
	   
	  } 
	  
	  if(status > 0){
		  appendStatus = " AND m.isApproved ="+" "+status;
	  }
	
	  if(!issueDatePubSearch.equals("") && !issueDatePubSearch.equals(null)){
		  appendDate = "AND m.parentImage.issueDate = '";
		  appendQuote = "'";
	  }
	  String  SQL = "From DataEntry as m " +""+" where m.deCompany != null and m.isDeleted=0 AND m.parentImage.publicationTitle = "+publicationId+appendStatus+" "+appendDate+issueDatePubSearch+appendQuote+""+appendStr;
	  //System.out.println("SQL"+SQL);
	  return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	 } 

	 @Override
	 public List<DataEntry> geQcJobBySeach(String publicationId, String issueDatePubSearch) {
	  String appendDate = "";
	  String appendQuote = "";
	  if(!issueDatePubSearch.equals("") && !issueDatePubSearch.equals(null)){
		  appendDate = "AND m.parentImage.issueDate = '";
		  appendQuote = "'";
	  }
	  String  SQL = "From DataEntry as m " +""+" where m.deCompany != null and m.isDeleted=0 AND m.parentImage.publicationTitle = "+publicationId+" "+appendDate+issueDatePubSearch+appendQuote;
	  return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	 }
	 
	 @Override
	 public List<DataEntry> geAllQcJob() {
	  String  SQL = "From DataEntry as m " +""+" where m.deCompany != null and m.isDeleted=0";
	  //System.out.println("SQL"+SQL);
	  return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	 }
	 
	@Override
	public List<DataEntry> getImagesByJobid(long jobid) {
		String 	SQL = "From DataEntry as m where m.isDeleted=0 and m.deJobid='"+jobid+"' ORDER BY m.id ASC";
		return getSessionFactory().getCurrentSession().createQuery(SQL).list();
	}


}
