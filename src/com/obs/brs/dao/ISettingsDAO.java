/**
 * 
 */
package com.obs.brs.dao;

import java.util.List;

import com.obs.brs.model.Settings;

/**
 * @author Sathish
 *
 */
public interface ISettingsDAO {
	
	/**
	 * Add Settings
	 * 
	 * @param  Settings settings
	 */
	public void addSettings(Settings settings);
	
	/**
	 * Update Settings
	 * 
	 * @param  Settings settings
	 */
	public void updateSettings(Settings settings);
	
	/**
	 * Delete Settings
	 * 
	 * @param  Settings settings
	 */
	public void deleteSettings(Settings settings);
	
	/**
	 * Get Settings
	 * 
	 * @param  int Settings Id
	 */
	public Settings getSettingsById(long id);
	
	/**
	 * Get Settings List
	 * 
	 */
	public List<Settings> getSettings();
	
	/**
	 * 
	 * @param menuId
	 * @param id
	 * @return
	 */
	public Boolean roleHasPermission(String menuValue, long id);


}
