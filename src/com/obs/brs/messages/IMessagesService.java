/**
 * 
 */
package com.obs.brs.messages;

/**
 * @author Sathish
 *
 */
public interface IMessagesService {
	
	/**
	 *  Message for Showing type information 
	 * @param msgDetail
	 * @param msgSummary
	 */
	public void messageInformation(String msgDetail, String msgSummary);
	
	/**
	 * Message for showing type Warning 
	 * @param msgDetail
	 * @param msgSummary
	 */
	public void messageWarning(String msgDetail, String msgSummary);

	/**
	 * Message for showing type Error 
	 * @param msgDetail
	 * @param msgSummary
	 */
	public void messageError(String msgDetail, String msgSummary);
	
	/**
	 * Message for showing type Fatal Error 
	 * @param msgDetail
	 * @param msgSummary
	 */
	public void messageFatal(String msgDetail, String msgSummary);

}
