package com.obs.brs.dao;

import java.util.List;

import org.hibernate.SessionFactory;

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

}
