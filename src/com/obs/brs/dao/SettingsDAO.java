/**
 * 
 */
package com.obs.brs.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.obs.brs.model.Settings;

/**
 * @author Sathish
 *
 */
public class SettingsDAO implements ISettingsDAO{

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
	 * @see com.obs.fms.dao.ISettingsDAO#addSettings(com.obs.fms.model.Settings)
	 */
	@Override
	public void addSettings(Settings settings) {
		getSessionFactory().getCurrentSession().save(settings);
	}

	/* (non-Javadoc)
	 * @see com.obs.fms.dao.ISettingsDAO#updateSettings(com.obs.fms.model.Settings)
	 */
	@Override
	public void updateSettings(Settings settings) {
		getSessionFactory().getCurrentSession().update(settings);
	}

	/* (non-Javadoc)
	 * @see com.obs.fms.dao.ISettingsDAO#deleteSettings(com.obs.fms.model.Settings)
	 */
	@Override
	public void deleteSettings(Settings settings) {
		getSessionFactory().getCurrentSession().delete(settings);
	}

	/* (non-Javadoc)
	 * @see com.obs.fms.dao.ISettingsDAO#getSettingsById(long)
	 */
	@Override
	public Settings getSettingsById(long id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from Settings where id=?")
		        .setParameter(0,id).list();
		return list!=null?(Settings)list.get(0):null;	
	}

	/* (non-Javadoc)
	 * @see com.obs.fms.dao.ISettingsDAO#getSettings()
	 */
	@Override
	public List<Settings> getSettings() {
		List<Settings> list = getSessionFactory().getCurrentSession().createQuery("from Settings").list();
		return list;
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.dao.ISettingsDAO#roleHasPermission(java.lang.String, long)
     */
	@Override
	public Boolean roleHasPermission(String menuValue, long id) {
		String sql = "from Settings where "+menuValue+" = true and userType_id.id="+id;
		List<Settings> list = getSessionFactory().getCurrentSession().createQuery(sql).list();
		if (list.size() > 0) {
			return true;
		}
		return false;
	}
}
