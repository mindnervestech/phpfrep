/**
 * 
 */
package com.obs.brs.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.obs.brs.dao.ISettingsDAO;
import com.obs.brs.model.Settings;

/**
 * @author Sathish
 *
 */
@Transactional(readOnly = true)
public class SettingsService implements ISettingsService{
    
	ISettingsDAO settingsDAO;
	/*
	 * get settingsDAO
	 */
	public ISettingsDAO getSettingsDAO() {
		return settingsDAO;
	}

	/*
	 * set settingsDAO
	 */
	public void setSettingsDAO(ISettingsDAO settingsDAO) {
		this.settingsDAO = settingsDAO;
	}

	/* (non-Javadoc)
	 * @see com.obs.fms.service.ISettingsService#addSettings(com.obs.fms.model.Settings)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addSettings(Settings settings) {
		 getSettingsDAO().addSettings(settings);
	}

	/* (non-Javadoc)
	 * @see com.obs.fms.service.ISettingsService#updateSettings(com.obs.fms.model.Settings)
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateSettings(Settings settings) {
		getSettingsDAO().updateSettings(settings);
	}

	/* (non-Javadoc)
	 * @see com.obs.fms.service.ISettingsService#deleteSettings(com.obs.fms.model.Settings)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteSettings(Settings settings) {
		getSettingsDAO().deleteSettings(settings);
	}

	/* (non-Javadoc)
	 * @see com.obs.fms.service.ISettingsService#getSettingsById(long)
	 */
	@Override
	@Transactional(readOnly = false)
	public Settings getSettingsById(long id) {
		return getSettingsDAO().getSettingsById(id);
	}

	/* (non-Javadoc)
	 * @see com.obs.fms.service.ISettingsService#getSettings()
	 */
	@Override
	@Transactional(readOnly = false)
	public List<Settings> getSettings() {
		return getSettingsDAO().getSettings();
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.ISettingsService#roleHasPermission(java.lang.String, long)
     */
	@Override
	public Boolean roleHasPermission(String menuValue, long id) {
		return getSettingsDAO().roleHasPermission(menuValue, id);
	}
}
