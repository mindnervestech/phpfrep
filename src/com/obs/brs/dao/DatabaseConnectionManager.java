/**
 * 
 */
package com.obs.brs.dao;

import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * @author Sathish
 *
 */
public class DatabaseConnectionManager {
	
	private static final ResourceBundle bundle = ResourceBundle.getBundle("com.obs.brs.properties.jdbc");


	private static DatabaseConnectionManager databaseConnectionManager = null;

	public static DatabaseConnectionManager getInstance() {
		if (databaseConnectionManager == null) {
			databaseConnectionManager = new DatabaseConnectionManager();
		}
		return databaseConnectionManager;
	}
	
	/* (non-Javadoc)
	 * @see com.obs.fms.dao.IDatabaseConnectionManager#createDataSource(java.lang.String)
	 */
	public DataSource createDataSource(String databseName) {
		// Create Datasource dynamically
			String url = bundle.getString("jdbc.url")+databseName;
			DriverManagerDataSource dataSource = new DriverManagerDataSource(url,
					bundle.getString("jdbc.username"), bundle.getString("jdbc.password"));
			dataSource.setDriverClassName(bundle.getString("jdbc.driverClassName"));
			return dataSource;
	}
	
	 
}
