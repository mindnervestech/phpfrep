package com.obs.brs.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.obs.brs.model.DeletedImage;
import com.obs.brs.model.ParentImage;

public class DeletedImageDAO implements IDeletedImageDAO{
	
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
    /*
     * (non-Javadoc)
     * @see com.obs.brs.dao.IDeletedImageDAO#addDeletedImage(com.obs.brs.model.DeletedImage)
     */
	@Override
	public Long addDeletedImage(DeletedImage deletedImage) {
		// TODO Auto-generated method stub
		return (Long) getSessionFactory().getCurrentSession().save(deletedImage);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.dao.IDeletedImageDAO#updateDeletedImage(com.obs.brs.model.DeletedImage)
     */
	@Override
	public void updateDeletedImage(DeletedImage deletedImage) {
		// TODO Auto-generated method stub
		getSessionFactory().getCurrentSession().update(deletedImage);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.dao.IDeletedImageDAO#deleteDeletedImage(com.obs.brs.model.DeletedImage)
     */
	@Override
	public void deleteDeletedImage(DeletedImage deletedImage) {
		// TODO Auto-generated method stub
		getSessionFactory().getCurrentSession().delete(deletedImage);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.dao.IDeletedImageDAO#getDeletedImageById(long)
     */
	@Override
	public DeletedImage getDeletedImageById(long id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from DeletedImage where id=?")
		        .setParameter(0,id).list();
		return list!=null?(DeletedImage)list.get(0):null;
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.dao.IDeletedImageDAO#getDeletedImage()
     */
	@Override
	public List<DeletedImage> getDeletedImage() {
		List<DeletedImage> list = getSessionFactory().getCurrentSession().createQuery("from DeletedImage").list();
		return list;
	}

}
