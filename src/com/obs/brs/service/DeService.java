package com.obs.brs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.obs.brs.controller.ScoreData;
import com.obs.brs.dao.IDeServiceDAO;
import com.obs.brs.model.DataEntry;
import com.obs.brs.model.DeCompany;
import com.obs.brs.model.DeJob;
import com.obs.brs.model.OcrTextMatchResult;
import com.obs.brs.model.ParentImage;

/**
 * 
 * @author Jeevanantham
 *
 */
@Transactional(readOnly = true)
public class DeService implements IDeService{

	IDeServiceDAO deServiceDAO;
    /*
     * @return the deServiceDAO
     */
	public IDeServiceDAO getDeServiceDAO() {
		return deServiceDAO;
	}
    /*
     * @param deServiceDAO the deServiceDAO to set
     */
	public void setDeServiceDAO(IDeServiceDAO deServiceDAO) {
		this.deServiceDAO = deServiceDAO;
	}

	/* (non-Javadoc)
	 * @see com.obs.brs.service.IDeService#addDataEntry(com.obs.brs.model.DataEntry)
	 */
	@Override
	@Transactional(readOnly = false)
	public Long addDataEntry(DataEntry dataEntry) {
		return (Long)getDeServiceDAO().addDataEntry(dataEntry);

	}
	/* (non-Javadoc)
	 * @see com.obs.brs.service.IDeService#updateDataEntry(com.obs.brs.model.DataEntry)
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateDataEntry(DataEntry dataEntry) {
		getDeServiceDAO().updateDataEntry(dataEntry);

	}
	/* (non-Javadoc)
	 * @see com.obs.brs.service.IDeService#deleteDataEntry(com.obs.brs.model.DataEntry)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteDataEntry(DataEntry dataEntry) {
		getDeServiceDAO().deleteDataEntry(dataEntry);

	}
	/* (non-Javadoc)
	 * @see com.obs.brs.service.IDeService#getDataEntryById(com.obs.brs.model.DataEntry)
	 */
	@Override
	public DataEntry getDataEntryById(long id) {
		return getDeServiceDAO().getDataEntryById(id);
	}
	/* (non-Javadoc)
	 * @see com.obs.brs.service.IDeService#getDataEntry(com.obs.brs.model.DataEntry)
	 */
	@Override
	public List<DataEntry> getDataEntry() {
		return getDeServiceDAO().getDataEntry();
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IDeService#getDeBySeachCriteria(long)
	 */
	@Override
	public List<DataEntry> getDeBySeachCriteria(long userId) {
		return getDeServiceDAO().getDeBySeachCriteria(userId);
	}
	/*
	 * (non-Javadoc)
	 * @see com.obs.brs.service.IDeService#getDeBySeachCriteria(long, java.lang.String)
	 */
	@Override
	public List<DataEntry> getDeBySeachCriteria(long userId,String searchValue) {
		return getDeServiceDAO().getDeBySeachCriteria(userId,searchValue);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#addDeCompany(com.obs.brs.model.DeCompany)
     */
	@Override
	@Transactional(readOnly = false)
	public Long addDeCompany(DeCompany deCompany) {
		return (Long)getDeServiceDAO().addDeCompany(deCompany);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#updateDeCompany(com.obs.brs.model.DeCompany)
     */
	@Override
	@Transactional(readOnly = false)
	public void updateDeCompany(DeCompany deCompany) {
		getDeServiceDAO().updateDeCompany(deCompany);

	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#deleteDeCompany(com.obs.brs.model.DeCompany)
     */
	@Override
	@Transactional(readOnly = false)
	public void deleteDeCompany(DeCompany deCompany) {
		getDeServiceDAO().deleteDeCompany(deCompany);

	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeCompanyById(long)
     */
	@Override
	public DeCompany getDeCompanyById(long id) {
		return getDeServiceDAO().getDeCompanyById(id);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeCompany()
     */
	@Override
	public List<DeCompany> getDeCompany() {
		return getDeServiceDAO().getDeCompany();
	}
	
	
	public List<DeCompany> getDeCompanyBySeachCriteria(String searchValue) {
		return getDeServiceDAO().getDeCompanyBySeachCriteria(searchValue);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeCompanyBySeachCriteria(long)
     */
	@Override
	public List<DeCompany> getDeCompanyBySeachCriteria(long userId) {
		return getDeServiceDAO().getDeCompanyBySeachCriteria(userId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeCompanyBySeachCriteria(long, java.lang.String)
     */
	@Override
	public List<DeCompany> getDeCompanyBySeachCriteria(long userId,
			String searchValue) {
		return getDeServiceDAO().getDeCompanyBySeachCriteria(userId,searchValue);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#addDeJob(com.obs.brs.model.DeJob)
     */
	@Override
	@Transactional(readOnly = false)
	public Long addDeJob(DeJob deJob) {
		return (Long)getDeServiceDAO().addDeJob(deJob);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#updateDeJob(com.obs.brs.model.DeJob)
     */
	@Override
	@Transactional(readOnly = false)
	public void updateDeJob(DeJob deJob) {
		getDeServiceDAO().updateDeJob(deJob);

	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#deleteDeJob(com.obs.brs.model.DeJob)
     */
	@Override
	@Transactional(readOnly = false)
	public void deleteDeJob(DeJob deJob) {
		getDeServiceDAO().deleteDeJob(deJob);

	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeJobById(long)
     */
	@Override
	public DeJob getDeJobById(long id) {
		return getDeServiceDAO().getDeJobById(id);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeJob()
     */
	@Override
	public List<DeJob> getDeJob() {
		return getDeServiceDAO().getDeJob();
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeJobBySeachCriteria()
     */
	@Override
	public List<DeJob> getDeJobBySeachCriteria() {
		return getDeServiceDAO().getDeJobBySeachCriteria();
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeJobBySeachCriteria(java.lang.String)
     */
	@Override
	public List<DeJob> getDeJobBySeachCriteria(String searchValue) {
		return getDeServiceDAO().getDeJobBySeachCriteria(searchValue);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeJobByParentImageId(long)
     */
	@Override
	public DeJob getDeJobByParentImageId(long id) {
		return getDeServiceDAO().getDeJobByParentImageId(id);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDataEntryByParentImageId(long)
     */
	@Override
	public DataEntry getDataEntryByParentImageId(long id) {
		return getDeServiceDAO().getDataEntryByParentImageId(id);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeCompanyByParentImageId(long)
     */
	@Override
	public DeCompany getDeCompanyByParentImageId(long deJobId) {
		return getDeServiceDAO().getDeCompanyByParentImageId(deJobId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDataEntryByChildImageId(long)
     */
	@Override
	public DataEntry getDataEntryByChildImageId(long id) {
		// TODO Auto-generated method stub
		return getDeServiceDAO().getDataEntryByChildImageId(id);
	}
     /*
      * (non-Javadoc)
      * @see com.obs.brs.service.IDeService#getImagesByParent(long)
      */
	@Override
	public List<DataEntry> getImagesByParent(long parId) {
		// TODO Auto-generated method stub
		return getDeServiceDAO().getImagesByParent(parId);	
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getNextDataEntry(long, long)
     */
	@Override
	public DataEntry getNextDataEntry(long baseId,long jobId) {
		return getDeServiceDAO().getNextDataEntry(baseId,jobId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getPrevDataEntry(long, long)
     */
	@Override
	public DataEntry getPrevDataEntry(long baseId, long jobid) {
		// TODO Auto-generated method stub
		return getDeServiceDAO().getPrevDataEntry(baseId,jobid);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getCompanyByDataEntryId(long)
     */
	@Override
	public DataEntry getCompanyByDataEntryId(long deDataId) {
		return getDeServiceDAO().getCompanyByDataEntryId(deDataId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeCompanySeachByCompanyName(java.lang.String)
     */
	@Override
	public DeCompany getDeCompanySeachByCompanyName(String searchValue) {
		return getDeServiceDAO().getDeCompanySeachByCompanyName(searchValue);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeCompanyNameByCompanyNameWithId(long, java.lang.String)
     */
	@Override
	public DeCompany getDeCompanySeachByCompanyNameId(long searchValue) {
		return getDeServiceDAO().getDeCompanySeachByCompanyNameSaveId(searchValue);
	}
	
	
	
	
	
	
	
	
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeCompanyNameByCompanyNameWithId(long, java.lang.String)
     */
	@Override
	public DeCompany getDeCompanyNameByCompanyNameWithId(long id,
			String companyName) {
		return getDeServiceDAO().getDeCompanyNameByCompanyNameWithId(id,companyName);
 	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeCompanyNameByCompanyName(java.lang.String)
     */
	@Override
	public DeCompany getDeCompanyNameByCompanyName(String companyName) {
		return getDeServiceDAO().getDeCompanyNameByCompanyName(companyName);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeJobByTypeSeachCriteria(int)
     */
	@Override
	public List<DeJob> getDeJobByTypeSeachCriteria(int publicationStatus) {
		return getDeServiceDAO().getDeJobByTypeSeachCriteria(publicationStatus);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getDeJobByQC()
     */
	@Override
	public List<DataEntry> getDeJobByQC() {
		return getDeServiceDAO().getDeJobByQC();
	}
	
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#geQcJobBySeachCriteria(int, java.lang.String, java.lang.String)
     */
	@Override
	public List<DataEntry> geQcJobBySeachCriteria(int status,String searchValue,String publicationId,String issueDatePubSearch,String createdBy) {
		// TODO Auto-generated method stub
		return getDeServiceDAO().geQcJobBySeachCriteria(status,searchValue,publicationId,issueDatePubSearch,createdBy);
	}
	  /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#geQcJobBySeach(java.lang.String)
     */
	@Override
	public List<DataEntry> geQcJobBySeach(String publicationId, String issueDatePubSearch,String createdBy) {
		// TODO Auto-generated method stub
		return getDeServiceDAO().geQcJobBySeach(publicationId, issueDatePubSearch,createdBy);
	}
	  /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#geQcJobBySeach(java.lang.String)
     */
	@Override
	public List<DataEntry> geAllQcJob() {
		// TODO Auto-generated method stub
		return getDeServiceDAO().geAllQcJob();
	}
	  /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getIssueDateByPublication(java.lang.String)
     */
	@Override
	public List<DataEntry> getIssueDateByPublication(String publicationId) {
		// TODO Auto-generated method stub
		return getDeServiceDAO().getIssueDateByPublication(publicationId);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeService#getImagesByJobid(long)
     */
	@Override
	public List<DataEntry> getImagesByJobid(long jobid) {
		// TODO Auto-generated method stub
		return getDeServiceDAO().getImagesByJobid(jobid);
	}
	
	@Override
	public String getStatusOfChildImageCompletion(long jobid) {
		// TODO Auto-generated method stub
		return getDeServiceDAO().getStatusOfChildImageCompletion(jobid);
	}
	@Override
	public List getAllDeo() {
		return getDeServiceDAO().getAllDeo();
	}
	@Override
	public DataEntry getCurrentEntry(long baseId, long jobid) {
		return getDeServiceDAO().getCurrentEntry(baseId, jobid);
	}
	@Override
	public List<DeCompany> getDeCompanyBySeachCriteriaId(long searchValue) {
		// TODO Auto-generated method stub
		return getDeServiceDAO().getDeCompanySeachByCompanyNameId(searchValue);
	}
	@Override
	public Boolean isRender(int id) {
		return getDeServiceDAO().isRender(id);
	}
	@Override
	@Transactional(readOnly=false)
	public void sendJobToQC(int id) {
		getDeServiceDAO().sendJobToQC(id);
	}
	@Override
	@Transactional(readOnly=false)
	public void updateParentImage(ParentImage parentImage) {
		getDeServiceDAO().updateParentImage(parentImage);
	}
	@Override
	public List<DataEntry> getDataEntryByChildImageIds(Set<Long> ids) {
		return getDeServiceDAO().getDataEntryByChildImageIds(ids);
	}
	
	@Transactional
    public  List<DataEntry> getLiveDeData() {
		return getDeServiceDAO().getLiveDeData();
    }
	@Override
	public List<DataEntry> getCropedImagesJobs() {
		// TODO Auto-generated method stub
		return getDeServiceDAO().getCropedImagesJobs();
	}

	@Transactional
	public void saveOcrTextResult(OcrTextMatchResult ocr) {
		// TODO Auto-generated method stub
	     getDeServiceDAO().saveOcrTextResult(ocr);	
	}
	@Transactional
	public List<ScoreData> getDeoByReleavance() {
		// TODO Auto-generated method stub
	    return  getDeServiceDAO().getDeoByReleavance();	
	}
	
	

}
