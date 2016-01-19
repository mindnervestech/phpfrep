package com.obs.brs.session.manager;


import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionManager implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static SessionManager sessionManager;
	
	public static final String LOGINUSER		= "login_user";
	public static final String BRSMENU 			= "brs.menu";
	public static final String ROWSPERPAGE 		= "rowsPerPage";
	public static final String EDITUSER 		= "edit_user";
	public static final String CROPIMAGE 		= "crop_image";
	public static final String LOGINSUBSCRIBERUSER		= "login_subscriber_user";
	public static final String EDITSUBSCRIBERUSER 		= "edit_subscriber_user";
	public static final String EDITSUBSCRIBERUSERBYADMIN 		= "edit_subscriber_user_byadmin";
	public static final String EDITDATAENTRYANDCOMPANY =  "editdatandcompany";
	public static final String REPORTCURRENTMONTHYEAR = "report_current_month_and_year";
	public static final String REPORTCURRENTITEMID = "report_current_item_id";
	public static final String REPORTCURRENTJOURNALID = "report_current_journal_id";
	public static final String IMAGEPERPAGE 		= "imagePerPage";
	public static final String IMAGEPERPAGEOCR 		= "imagePerPageOcr";
	public static final String CHILDIMAGEPERPAGE 		= "childImagePerPage";
	public static final String PUBLICATIONID 		= "publicationId";
	public static final String JOBSTATUS 			= "jobStatusGot";
	public static final String ISSUEDATE 			= "issueDate";
	public static String CHILDIMAGEID 			= "child_image_id";
	public static final String CREATEDBYDEO = "createdByDeo";
	public static final String IMAGEOFFSET = "image_offset";
	public static final String IMAGEOFFSETOCR = "image_offset_ocr";
	public static final String TERRITORYSUCCESS = "territorysuccess";
	public static SessionManager getMyInstatnce(){
		if(sessionManager == null){
			return sessionManager = new SessionManager();
		}
		return sessionManager;
	}
	
	private HttpSession getSession() {
		return getSession(getFacesRequest());
	}

	private HttpServletRequest getFacesRequest() {
		return new FacesUtils().getRequest();
	}

	private HttpSession getSession(HttpServletRequest request) {
		return request.getSession(true);
	}

	public void setUserInSession(String key,Object user) {
		setUserInSession(key,user,getFacesRequest());
	}

	private void setUserInSession(String key, Object user, HttpServletRequest request) {
		getSession(request).setAttribute(key, user);
	}

	public void setSessionAttributeInSession(String attribute, Object value) {
		setSessionAttributeInSession(attribute,value,getFacesRequest());
	}
	
	public void removeSessionAttributeInSession(String attribute) {
		removeSessionAttributeInSession(attribute,getFacesRequest());
	}

	private void setSessionAttributeInSession(String attribute, Object value, HttpServletRequest request) {
		getSession(request).setAttribute(attribute, value);
	}
	
	private void removeSessionAttributeInSession(String attribute,HttpServletRequest request) {
		getSession(request).removeAttribute(attribute);
	}

	public Object getSessionAttribute(String attribute) {
		return getSessionAttribute(attribute, getFacesRequest());
	}

	private Object getSessionAttribute(String attribute, HttpServletRequest request) {
		return getSession(request).getAttribute(attribute);
	}

	public void removeSession() {
		getSession(getFacesRequest()).invalidate();
	}
}
