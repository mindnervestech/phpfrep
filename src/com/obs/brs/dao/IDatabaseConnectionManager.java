/**
 * 
 */
package com.obs.brs.dao;

import javax.sql.DataSource;

/**
 * @author Sathish
 *
 */
public interface IDatabaseConnectionManager {
	
	/**
	 *  Create the database connection dynamically based on the given database name.
	 * @param databseName
	 * @return
	 */
	public DataSource createDataSource(String databseName);
	
}
