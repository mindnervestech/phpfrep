package com.obs.brs.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.obs.brs.model.DeJob;
import com.obs.brs.model.OcrTextMatchResult;
import com.obs.brs.model.ParentImage;

/**
 * 
 * @author Jeevanantham
 *
 */
public class ParentImageDAO implements IParentImageDAO{
	
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
	 * @see ccom.obs.brs.dao.ParentImageDAO#addParentImage(com.obs.brs.model.ParentImage)
	 */
	@Override
	public Long addParentImage(ParentImage parentImage) {
		return (Long) getSessionFactory().getCurrentSession().save(parentImage);
	}

	/* (non-Javadoc)
	 * @see ccom.obs.brs.dao.ParentImageDAO#addParentImage(com.obs.brs.model.ParentImage)
	 */
	@Override
	public void updateParentImage(ParentImage parentImage) {
		getSessionFactory().getCurrentSession().update(parentImage);
	}

	/* (non-Javadoc)
	  * @see ccom.obs.brs.dao.ParentImageDAO#addParentImage(com.obs.brs.model.ParentImage)
	 */
	@Override
	public void deleteParentImage(ParentImage parentImage) {
		getSessionFactory().getCurrentSession().delete(parentImage);
	}

	/* (non-Javadoc)
	  * @see ccom.obs.brs.dao.ParentImageDAO#addParentImage(com.obs.brs.model.ParentImage)
	 */
	@Override
	public ParentImage getParentImageById(long id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from ParentImage where id=?")
		        .setParameter(0,id).list();
		return list!=null?(list.size()!=0?(ParentImage)list.get(0):null):null;	
	}

	/* (non-Javadoc)
	  * @see ccom.obs.brs.dao.ParentImageDAO#getParentImage(com.obs.brs.model.ParentImage)
	 */
	@Override
	public List<ParentImage> getParentImage() {
		List<ParentImage> list = getSessionFactory().getCurrentSession().createQuery("from ParentImage where status = '0'").list();
		return list;
	}
	
	@Override
	public List<ParentImage> getParentImageByFilter(String val) {
		String sqlQuery = "from ParentImage";
		int filter = Integer.parseInt(val);
		if(filter == 1){
			sqlQuery = sqlQuery + " where status = '1'";
		} else if(filter == 2){
			sqlQuery = sqlQuery + " where status = '0'";
		}else{
			sqlQuery = sqlQuery + " where status != '2'";
		}
		
		List<ParentImage> list = getSessionFactory().getCurrentSession().createQuery(sqlQuery).list();
		return list;
	}

	@Override
	public ParentImage getParentImageByName(String name) {
		List<ParentImage> list = getSessionFactory().getCurrentSession().createQuery("from ParentImage where imageName=?").setParameter(0, name).list();
		if(list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public void updateParentImageStatusById(Map<Long, Boolean> selectedIds) {
		// TODO Auto-generated method stub
		System.out.println("selectedIds.size  in move : "+selectedIds.size());
		for (Map.Entry<Long, Boolean> entry : selectedIds.entrySet()) {
			         System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			        if(entry.getValue() == true){
			        	 
			        	String sqlQuery = "from ParentImage";
			        	//int filter = Integer.parseInt(entry.getKey().toString());
			        	//Long l = new Long(Integer.parseInt(entry.getKey().toString()));
			        	sqlQuery = sqlQuery + " where id ="+entry.getKey().toString();
				        
					    List<ParentImage> parentImage = getSessionFactory().getCurrentSession().createQuery(sqlQuery).list();
				        System.out.println(parentImage.size());
	     			        
				        for(ParentImage p : parentImage ){
	     			        	p.setStatus(2);
	     			        	getSessionFactory().getCurrentSession().update(p);
				        	    String sqlQuerydejob = "from DeJob";
							    long  id =  p.getId();
							    sqlQuerydejob = sqlQuerydejob + " where  parentImage.id ="+id;
								List<DeJob>  deJobs = getSessionFactory().getCurrentSession().createQuery(sqlQuerydejob).list();
						         
								 for(DeJob d :deJobs){
									 d.setStatus(0);
									 getSessionFactory().getCurrentSession().update(d);
								 }
	     			        }	
			        }
			         
		}
		
	}
	
	
	public void updateParentImageStatusLiveById(Map<Long, Boolean> selectedIds) {
		// TODO Auto-generated method stub
		System.out.println("selectedIds.size  in move : "+selectedIds.size());
		for (Map.Entry<Long, Boolean> entry : selectedIds.entrySet()) {
			         System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			        if(entry.getValue() == true){
			        	 
			        	String sqlQuery = "from ParentImage";
			        	//int filter = Integer.parseInt(entry.getKey().toString());
			        	//Long l = new Long(Integer.parseInt(entry.getKey().toString()));
			        	sqlQuery = sqlQuery + " where id ="+entry.getKey().toString();
				        
					    List<ParentImage> parentImage = getSessionFactory().getCurrentSession().createQuery(sqlQuery).list();
				        System.out.println(parentImage.size());
	     			        
				        for(ParentImage p : parentImage ){
	     			        	p.setStatus(2);
	     			        	getSessionFactory().getCurrentSession().update(p);
				        	    String sqlQuerydejob = "from DeJob";
							    long  id =  p.getId();
							    sqlQuerydejob = sqlQuerydejob + " where  parentImage.id ="+id;
								List<DeJob>  deJobs = getSessionFactory().getCurrentSession().createQuery(sqlQuerydejob).list();
						         
								 for(DeJob d :deJobs){
									 d.setStatus(2);
									 getSessionFactory().getCurrentSession().update(d);
								 }
	     			        }	
			        }
			         
				}
		}
	
	public void updateOcrTextIDDupDetails(String id) {
			// TODO Auto-generated method stub
			//System.out.println("not dup id  : "+id);
			String sqlQuery = "from OcrTextMatchResult ";
        	long idss = Long.parseLong(id);
        	sqlQuery = sqlQuery + " where croppedData ="+idss;
		    List<OcrTextMatchResult> ocrTextMatchResults = getSessionFactory().getCurrentSession().createQuery(sqlQuery).list();
		   // System.out.println("size dup id list: "+ocrTextMatchResults.size());
		    for(OcrTextMatchResult o : ocrTextMatchResults){
		    	o.setDuplicate(true);
		    	getSessionFactory().getCurrentSession().update(o);
		    }
		}
	}
