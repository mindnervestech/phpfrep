package com.obs.brs.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.obs.brs.model.ChildImage;

/**
 * 
 * @author Jeevanantham
 *
 */
public class ChildImageDAO implements IChildImageDAO{

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
	 * @see ccom.obs.brs.dao.ChildImageDAO#addChildImage(com.obs.brs.model.ChildImage)
	 */
	@Override
	public Long addChildImage(ChildImage childImage) {
		return (Long)getSessionFactory().getCurrentSession().save(childImage);
	}

	/* (non-Javadoc)
	 * @see ccom.obs.brs.dao.ChildImageDAO#updateChildImage(com.obs.brs.model.ChildImage)
	 */
	@Override
	public void updateChildImage(ChildImage childImage) {
		getSessionFactory().getCurrentSession().update(childImage);
	}

	/* (non-Javadoc)
	 * @see ccom.obs.brs.dao.ChildImageDAO#deleteChildImage(com.obs.brs.model.ChildImage)
	 */
	@Override
	public void deleteChildImage(ChildImage childImage) {
		getSessionFactory().getCurrentSession().delete(childImage);
	}

	/* (non-Javadoc)
	 * @see ccom.obs.brs.dao.ChildImageDAO#getChildImageById(com.obs.brs.model.ChildImage)
	 */
	@Override
	public ChildImage getChildImageById(long id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from ChildImage where id=?")
		        .setParameter(0,id).list();
		return list!=null?(ChildImage)list.get(0):null;	
	}

	/* (non-Javadoc)
	 * @see ccom.obs.brs.dao.ChildImageDAO#getChildImage(com.obs.brs.model.ChildImage)
	 */
	@Override
	public List<ChildImage> getChildImage() {
		List<ChildImage> list = getSessionFactory().getCurrentSession().createQuery("from ChildImage").list();
		return list;
	}
	
	/* (non-Javadoc)
	 * @see ccom.obs.brs.dao.ChildImageDAO#getChildImage(com.obs.brs.model.ChildImage)
	 */
	@Override
	public List<ChildImage> getChildImagesByParent(long parentId) {
		List<ChildImage> list = getSessionFactory().getCurrentSession().createQuery("from ChildImage as c where c.parentImage.id="+parentId).list();
		return list;
	}
	

}
