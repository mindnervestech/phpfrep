package com.obs.brs.messages;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
/**
 * 
 */

/**
 * @author Sathish
 *
 */
public class MessagesService implements IMessagesService{
	
	/* (non-Javadoc)
	 * @see com.obs.fms.messages.IMessagesController#messageInformation(java.lang.String, java.lang.String)
	 */
	@Override
	public void messageInformation(String msgDetail, String msgSummary) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,msgSummary,msgDetail));
	}
	
	/* (non-Javadoc)
	 * @see com.obs.fms.messages.IMessagesController#messageWarning(java.lang.String, java.lang.String)
	 */
	@Override
	public void messageWarning(String msgDetail, String msgSummary) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,msgSummary,msgDetail));
	}

	/* (non-Javadoc)
	 * @see com.obs.fms.messages.IMessagesController#messageError(java.lang.String, java.lang.String)
	 */
	@Override
	public void messageError(String msgDetail, String msgSummary) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,msgSummary,msgDetail));
	}
	
	/* (non-Javadoc)
	 * @see com.obs.fms.messages.IMessagesController#messageError(java.lang.String, java.lang.String)
	 */
	@Override
	public void messageFatal(String msgDetail, String msgSummary) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,msgSummary,msgDetail));
	}
	
}
