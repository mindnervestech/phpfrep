package com.obs.brs.dao;

import java.util.Collection;
import java.util.List;

import com.obs.brs.model.DataEntry;
import com.obs.brs.model.DeCompany;
import com.obs.brs.model.DeJob;
import com.obs.brs.model.ParentImage;

public interface IDeServiceDAO {
	/**
	 * Add DataEntry
	 * 
	 * @param  DataEntry dataEntry
	 */
	public Long addDataEntry(DataEntry dataEntry);

	/**
	 * Update DataEntry
	 * 
	 * @param  DataEntry dataEntry
	 */
	public void updateDataEntry(DataEntry dataEntry);

	/**
	 * Delete DataEntry
	 * 
	 * @param  DataEntry dataEntry
	 */
	public void deleteDataEntry(DataEntry dataEntry);

	/**
	 * Get DataEntry
	 * 
	 * @param  int DataEntry Id
	 */
	public DataEntry getDataEntryById(long id);

	/**
	 * Get DataEntry List
	 * 
	 */
	public List<DataEntry> getDataEntry();
	/**
	 * 
	 * @return
	 */
	public List<DataEntry> getDeBySeachCriteria(long userId);
	/**
	 * 
	 * @param searchValue
	 * @return
	 */
	public List<DataEntry> getDeBySeachCriteria(long userId,
			String searchValue);

	/**
	 * Add DeCompany
	 * 
	 * @param  DeCompany deCompany
	 */
	public Long addDeCompany(DeCompany deCompany);

	/**
	 * Update DeCompany
	 * 
	 * @param  DeCompany deCompany
	 */
	public void updateDeCompany(DeCompany deCompany);

	/**
	 * Delete DeCompany
	 * 
	 * @param  DeCompany DeCompany
	 */
	public void deleteDeCompany(DeCompany deCompany);

	/**
	 * Get DeCompany
	 * 
	 * @param  int DeCompany Id
	 */
	public DeCompany getDeCompanyById(long id);

	/**
	 * Get DeCompany List
	 * 
	 */
	public List<DeCompany> getDeCompany();
	/**
	 * 
	 * @return
	 */
	public List<DeCompany> getDeCompanyBySeachCriteria(long userId);
	/**
	 * 
	 * @param searchValue
	 * @return
	 */
	public List<DeCompany> getDeCompanyBySeachCriteria(long userId,
			String searchValue);

	/**
	 * Add DeJob
	 * 
	 * @param  DeJob deJob
	 */
	public Long addDeJob(DeJob deJob);

	/**
	 * Update DeJob
	 * 
	 * @param  DeJob DeJob
	 */
	public void updateDeJob(DeJob deJob);

	/**
	 * Delete DeJob
	 * 
	 * @param  DeJob deJob
	 */
	public void deleteDeJob(DeJob deJob);

	/**
	 * Get DeJob
	 * 
	 * @param  int DeJob Id
	 */
	public DeJob getDeJobById(long id);

	/**
	 * Get DeJob List
	 * 
	 */
	public List<DeJob> getDeJob();
	/**
	 * 
	 * @return
	 */
	public List<DeJob> getDeJobBySeachCriteria();
	/**
	 * 
	 * @param searchValue
	 * @return
	 */
	public List<DeJob> getDeJobBySeachCriteria(String searchValue);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public DeJob getDeJobByParentImageId(long id);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public DataEntry getDataEntryByParentImageId(long id);

	/**
	 * 
	 * @param deJobId
	 * @return
	 */
	public DeCompany getDeCompanyByParentImageId(long deJobId);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public DataEntry getDataEntryByChildImageId(long id);
	/**
	 * 
	 * @param parId
	 * @return
	 */
	public List<DataEntry> getImagesByParent(long parId);
	/**
	 * 
	 * @param baseId
	 * @return
	 */
	public DataEntry getNextDataEntry(long baseId, long jobId);
	/**
	 * 
	 * @param baseId
	 * @param jobid
	 * @return
	 */
	public DataEntry getPrevDataEntry(long baseId, long jobid);
	/**
	 * 
	 * @param baseId
	 * @param jobid
	 * @return
	 */
	public DataEntry getCurrentEntry(long baseId, long jobid);
	/**
	 * 
	 * @param deDataId
	 * @return
	 */
	public DataEntry getCompanyByDataEntryId(long deDataId);
	/**
	 * 
	 * @param searchValue
	 * @return
	 */
	public DeCompany getDeCompanySeachByCompanyName(String searchValue);
	/**
	 * 
	 * @param id
	 * @param companyName
	 * @return
	 */
	public DeCompany getDeCompanyNameByCompanyNameWithId(long id,
			String companyName);
	/**
	 * 
	 * @param companyName
	 * @return
	 */
	public DeCompany getDeCompanyNameByCompanyName(String companyName);
	/**
	 * 
	 * @param publicationStatus
	 * @return
	 */
	public List<DeJob> getDeJobByTypeSeachCriteria(
			int publicationStatus);
	/**
	 * 
	 * @return
	 */
	public List<DataEntry> getDeJobByQC();
	/**
	 * 
	 * @param publicationStatus
	 * @return
	 */
	public List<DataEntry> geQcJobBySeachCriteria(int status,String searchValue,String publicationId,String issueDatePubSearch,String CreatedBy);
	
	public List<DataEntry> geQcJobBySeach(String publicationId, String issueDatePubSearch,String createdBy);
	/**
	 * 
	 * @param parId
	 * @return
	 */
	public List<DataEntry> getImagesByJobid(long jobid);

	List<DataEntry> getIssueDateByPublication(String publicationId);

	List<DataEntry> geAllQcJob();

	String getStatusOfChildImageCompletion(long jobid);
	
	public List getAllDeo();

}
