package com.obs.brs.managed.bean;

import java.awt.GraphicsEnvironment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;

import net.sf.jasperreports.engine.JRException;

import com.obs.brs.messages.IMessagesService;
import com.obs.brs.model.DataEntry;
import com.obs.brs.model.ParentImage;
import com.obs.brs.model.Publication;
import com.obs.brs.model.ReportFields;
import com.obs.brs.model.SubscriberUser;
import com.obs.brs.service.IDeService;
import com.obs.brs.service.ISettingsService;
import com.obs.brs.service.IUserService;
import com.obs.brs.session.manager.FacesUtils;
import com.obs.brs.session.manager.SessionManager;
import com.obs.brs.utils.CommonProperties;
import com.obs.brs.utils.CommonUtils;
import com.obs.brs.utils.ReportManager;
import com.obs.brs.utils.StringUtility;

/**
 * @author Jeevanantham
 *
 */
@ManagedBean(name="reportsBean")
@ViewScoped
public class ReportsManagedBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CommonUtils commonUtils 		= CommonUtils.getInstance();
	SessionManager sessionManager 	= new SessionManager();

	//Spring User Service is injected...
	@ManagedProperty(value="#{UserService}")
	IUserService userService;

	@ManagedProperty(value="#{SettingsService}")
	ISettingsService settingsService;

	@ManagedProperty(value ="#{MessageService}")
	IMessagesService messageService;

	@ManagedProperty(value ="#{DeService}")
	IDeService deService;
	private static final String MANAGE_REPORTS="managereports";
	private static final String  CALENDARVIEW = "calendar_view_report.jrxml";
	private static final String  LISTJASPERVIEW = "jasperSubscriberListView";
	private static final String  CALENTARJASPERVIEW = "jasperSubscriberCalendarView";
	private static final String  ADVERTISERJASPERVIEW = "jasperSubscriberAdvertiserView";
	private static final String  JOURNALJASPERVIEW = "jasperSubscriberJournalView";
	public final static String JRM_BEAN_COLLECTION = "Jasper_Bean_Collection";
	public final static String JRM_DB_CONNECTION = "Jasper_con_SQL";
	public final static String JRM_HSQL_SESSION = "Jasper_HSQL_Session";
	public final static String JRM_QUERY = "Jasper_Query";
	public final static String JRM_JRXML_PATH = "Jasper_JRXML_Path";
	public final static String JRM_IS_BEAN_COLLECTION = "Is_Bean_Collection";
	public final static String JRM_OUT_HTML = "Jasper_OutHTML";
	public final static String JRM_OUT_PDF = "Jasper_OutPDF";
	public final static String JRM_OUT_XLS = "Jasper_OutXLS";
	public final static String JRM_OUT_CSV = "Jasper_OutCSV";
	public final static String JRM_OUT_HTML_FILE = "Jasper_OutHTMLFile";
	public final static String JRM_OUT_PDF_FILE = "Jasper_OutPDFFile";
	public final static String JRM_OUT_CSV_FILE = "Jasper_OutCSVFile";
	public final static String JRM_OUT_XLS_FILE = "Jasper_OutXLSFile";
	public final static String JRM_HTML_START_PAGE_INDEX = "Jasper_HTML_Start_Page_Index";
	public final static String JRM_HTML_END_PAGE_INDEX = "Jasper_HTML_End_Page_Index";
	public final static String JRM_LOGO_PATH = "PARAM_LOGOPATH";
	public final static String JRM_HEADER_VALUE = "Jasper_Header";
	public final static String JRM_IS_HEADER = "Is_Jasper_Header";
	public static String SEARCHREPORTVIEW="searchReportView";
	public static String ADVERTISERREPORTVIEW="advertiserReportView";
	public static String JOURNALREPORTVIEW="journalReportView";
	private int reportCurrentMonth;
	private int reportCurrentYear;
	private String reportFirstMonthYear= "";
	private String reportSecondMonthYear="";
	private String reportThirdMonthYear="";
	private String reportFourMonthYear="";
	private int reportCurrentMonthFirst;
	private int reportCurrentYearFirst;
	private int reportCurrentMonthSecond;
	private int reportCurrentYearSecond;
	private int reportCurrentMonthThird;
	private int reportCurrentYearThird;
	private int reportCurrentMonthFour;
	private int reportCurrentYearFour;
	private String fromMonthRange;
	private String fromYearRange;
	private String toMonthRange;
	private String toYearRange;
	private List<String> companyName;
	private List<String> publicationSection;
	private List<String> publicationPage;
	private List<String> adSize;
	private List<String> adOrientation;
	private List<String> companyType;
	private List<String> adType;
	private List<String> state;
	private List<String> country;
	private List<String> publicationTitle;
	private List<String> jobDensity;
	private String calendatViewHtml;
	private String calendatReportPath;
	private boolean					lbPDF=true;
	private boolean					lbPrint=false;
	private boolean					lbCSV=false;
	private Boolean checkVal=false;
	private Boolean checkPreVal=false;
	private Boolean checkAdmin=false;
	private boolean displayImage;	
	private int changeRowsPerPage=15;


	List calendarViewReportDetails = new ArrayList();
	List targetList = new ArrayList ();
	List<DataEntry> searchViewReportList = new ArrayList();
	/**
	 * 
	 * @return the targetList
	 */
	public List getTargetList() {
		return targetList;
	}
	/**
	 * 
	 * @param targetList
	 *         the targetList to set
	 */
	public void setTargetList(List targetList) {
		this.targetList = targetList;
	}
	/**
	 * 
	 * @return the calendarViewReportDetails
	 */
	public List getCalendarViewReportDetails() {
		return calendarViewReportDetails;
	}
	/**
	 * 
	 * @param calendarViewReportDetails
	 *         the calendarViewReportDetails to set
	 */
	public void setCalendarViewReportDetails(List calendarViewReportDetails) {
		this.calendarViewReportDetails = calendarViewReportDetails;
	}
    /**
     * 
     * @return the checkVal
     */
	public Boolean getCheckVal() {
		return checkVal;
	}
	/**
	 * 
	 * @param checkVal
	 *        the checkVal to set
	 */
	public void setCheckVal(Boolean checkVal) {
		this.checkVal = checkVal;
	}
	/**
	 * 
	 * @return the checkPreVal
	 */
	public Boolean getCheckPreVal() {
		return checkPreVal;
	}
	/**
	 * 
	 * @param checkPreVal
	 *           the checkPreVal to set
	 */
	public void setCheckPreVal(Boolean checkPreVal) {
		this.checkPreVal = checkPreVal;
	}
	/**
	 * 
	 * @return the lbPDF
	 */
	public boolean isLbPDF() {
		return lbPDF;
	}
	/**
	 * 
	 * @param lbPDF
	 *       the lbPDF to set
	 */
	public void setLbPDF(boolean lbPDF) {
		this.lbPDF = lbPDF;
	}
	/**
	 * 
	 * @return the lbPrint
	 */
	public boolean isLbPrint() {
		return lbPrint;
	}
	/**
	 * 
	 * @param lbPrint
	 *        the lbPrint to set
	 */
	public void setLbPrint(boolean lbPrint) {
		this.lbPrint = lbPrint;
	}
	/**
	 * 
	 * @return the lbCSV
	 */
	public boolean isLbCSV() {
		return lbCSV;
	}
	/**
	 * 
	 * @param lbCSV
	 *        the lbCSV to set
	 */
	public void setLbCSV(boolean lbCSV) {
		this.lbCSV = lbCSV;
	}
	/**
	 * 
	 * @return the calendatReportPath
	 */
	public String getCalendatReportPath() {
		System.out.print("\n\n\t calendatReportPath : "+calendatReportPath);
		return calendatReportPath;
	}
	/**
	 * 
	 * @param calendatReportPath
	 *           the calendatReportPath to set
	 */
	public void setCalendatReportPath(String calendatReportPath) {
		this.calendatReportPath = calendatReportPath;
	}
	/**
	 * 
	 * @return the calendatViewHtml
	 */
	public String getCalendatViewHtml() {
		return calendatViewHtml;
	}
	/**
	 * 
	 * @param calendatViewHtml
	 *          the calendatViewHtml to set
	 */
	public void setCalendatViewHtml(String calendatViewHtml) {
		this.calendatViewHtml = calendatViewHtml;
	}
    /**
     * 
     * @return the reportCurrentMonthFirst
     */
	public int getReportCurrentMonthFirst() {
		return reportCurrentMonthFirst;
	}
	/**
	 * 
	 * @param reportCurrentMonthFirst
	 *               the reportCurrentMonthFirst to set
	 */
	public void setReportCurrentMonthFirst(int reportCurrentMonthFirst) {
		this.reportCurrentMonthFirst = reportCurrentMonthFirst;
	}
	/**
	 * 
	 * @return the reportCurrentYearFirst
	 */
	public int getReportCurrentYearFirst() {
		return reportCurrentYearFirst;
	}
	/**
	 * 
	 * @param reportCurrentYearFirst
	 *            the reportCurrentYearFirst to set
	 */
	public void setReportCurrentYearFirst(int reportCurrentYearFirst) {
		this.reportCurrentYearFirst = reportCurrentYearFirst;
	}
	/**
	 * 
	 * @return the reportCurrentMonthSecond
	 */
	public int getReportCurrentMonthSecond() {
		return reportCurrentMonthSecond;
	}
	/**
	 * 
	 * @param reportCurrentMonthSecond
	 *             the reportCurrentMonthSecond to set
	 */
	public void setReportCurrentMonthSecond(int reportCurrentMonthSecond) {
		this.reportCurrentMonthSecond = reportCurrentMonthSecond;
	}
	/**
	 * 
	 * @return the reportCurrentYearSecond
	 */
	public int getReportCurrentYearSecond() {
		return reportCurrentYearSecond;
	}
	/**
	 * 
	 * @param reportCurrentYearSecond
	 *             the reportCurrentYearSecond to set
	 */
	public void setReportCurrentYearSecond(int reportCurrentYearSecond) {
		this.reportCurrentYearSecond = reportCurrentYearSecond;
	}
	/**
	 * 
	 * @return the reportCurrentMonthThird
	 */
	public int getReportCurrentMonthThird() {
		return reportCurrentMonthThird;
	}
	/**
	 * 
	 * @param reportCurrentMonthThird
	 *            the reportCurrentMonthThird to set
	 */
	public void setReportCurrentMonthThird(int reportCurrentMonthThird) {
		this.reportCurrentMonthThird = reportCurrentMonthThird;
	}
	/**
	 * 
	 * @return the  reportCurrentYearThird
	 */
	public int getReportCurrentYearThird() {
		return reportCurrentYearThird;
	}
	/**
	 * 
	 * @param reportCurrentYearThird
	 *            the reportCurrentYearThird to set
	 */
	public void setReportCurrentYearThird(int reportCurrentYearThird) {
		this.reportCurrentYearThird = reportCurrentYearThird;
	}
	/**
	 * 
	 * @return the reportCurrentMonthFour
	 */
	public int getReportCurrentMonthFour() {
		return reportCurrentMonthFour;
	}
	/**
	 * 
	 * @param reportCurrentMonthFour
	 *           the reportCurrentMonthFour to set
	 */
	public void setReportCurrentMonthFour(int reportCurrentMonthFour) {
		this.reportCurrentMonthFour = reportCurrentMonthFour;
	}
	/**
	 * 
	 * @return the reportCurrentYearFour
	 */
	public int getReportCurrentYearFour() {
		return reportCurrentYearFour;
	}
	/**
	 * 
	 * @param reportCurrentYearFour
	 *           the reportCurrentYearFour to set
	 */
	public void setReportCurrentYearFour(int reportCurrentYearFour) {
		this.reportCurrentYearFour = reportCurrentYearFour;
	}
	/**
	 * 
	 * @return the reportFirstMonthYear
	 */
	public String getReportFirstMonthYear() {
		return reportFirstMonthYear;
	}
	/**
	 * 
	 * @param reportFirstMonthYear
	 *         the reportFirstMonthYear to set
	 */
	public void setReportFirstMonthYear(String reportFirstMonthYear) {
		this.reportFirstMonthYear = reportFirstMonthYear;
	}
	/**
	 * 
	 * @return the reportSecondMonthYear
	 */
	public String getReportSecondMonthYear() {
		return reportSecondMonthYear;
	}
	/**
	 * 
	 * @param reportSecondMonthYear
	 *          the reportSecondMonthYear to set
	 */
	public void setReportSecondMonthYear(String reportSecondMonthYear) {
		this.reportSecondMonthYear = reportSecondMonthYear;
	}
	/**
	 * 
	 * @return the reportThirdMonthYear
	 */
	public String getReportThirdMonthYear() {
		return reportThirdMonthYear;
	}
	/**
	 * 
	 * @param reportThirdMonthYear
	 *           the reportThirdMonthYear to set
	 */
	public void setReportThirdMonthYear(String reportThirdMonthYear) {
		this.reportThirdMonthYear = reportThirdMonthYear;
	}
	/**
	 * 
	 * @return the reportThirdMonthYear 
	 */
	public String getReportFourMonthYear() {
		return reportFourMonthYear;
	}
	/**
	 * 
	 * @param reportFourMonthYear
	 *           the reportThirdMonthYear to set
	 */
	public void setReportFourMonthYear(String reportFourMonthYear) {
		this.reportFourMonthYear = reportFourMonthYear;
	}
	/**
	 * 
	 * @return the reportCurrentMonth
	 */
	public int getReportCurrentMonth() {
		return reportCurrentMonth;
	}
	/**
	 * 
	 * @param reportCurrentMonth
	 *          the reportCurrentMonth to set
	 */
	public void setReportCurrentMonth(int reportCurrentMonth) {
		this.reportCurrentMonth = reportCurrentMonth;
	}
	/**
	 * 
	 * @return the reportCurrentYear
	 */
	public int getReportCurrentYear() {
		return reportCurrentYear;
	}
	/**
	 * 
	 * @param reportCurrentYear
	 *           the reportCurrentYear to set
	 */
	public void setReportCurrentYear(int reportCurrentYear) {
		this.reportCurrentYear = reportCurrentYear;
	}
	/**
	 * 
	 * @return the searchViewReportList
	 */
	public List<DataEntry> getSearchViewReportList() {
		return searchViewReportList;
	}
    /**
     * 
     * @param searchViewReportList
     *            the searchViewReportList to set
     */
	public void setSearchViewReportList(List<DataEntry> searchViewReportList) {
		this.searchViewReportList = searchViewReportList;
	}
    /**
     * 
     * @return the companyName
     */
	public List<String> getCompanyName() {
		if(companyName == null){
			companyName = new ArrayList<String>();
			companyName.add("all");
		}
		return companyName;
	}
    /**
     * 
     * @param companyName
     *           the companyName to set
     */
	public void setCompanyName(List<String> companyName) {
		this.companyName = companyName;
	}
    /**
     * @return the publicationSection
     */
	public List<String> getPublicationSection() {
		if(publicationSection == null){
			publicationSection = new ArrayList<String>();
			publicationSection.add("all");
		}
		return publicationSection;
	}
    /**
     * 
     * @param publicationSection
     *            the publicationSection to set
     */
	public void setPublicationSection(List<String> publicationSection) {
		this.publicationSection = publicationSection;
	}
    /**
     * 
     * @return the publicationPage
     */
	public List<String> getPublicationPage() {
		if(publicationPage == null){
			publicationPage = new ArrayList<String>();
			publicationPage.add("all");
		}
		return publicationPage;
	}
    /**
     * 
     * @param publicationPage
     *          the publicationPage to set
     */
	public void setPublicationPage(List<String> publicationPage) {
		this.publicationPage = publicationPage;
	}
    /**
     * 
     * @return the adSize
     */
	public List<String> getAdSize() {
		if(adSize == null){
			adSize = new ArrayList<String>();
			adSize.add("all");
		}
		return adSize;
	}
    /**
     * 
     * @param adSize
     *       the adSize to set
     */
	public void setAdSize(List<String> adSize) {
		this.adSize = adSize;
	}
    /**
     * 
     * @return the adOrientation
     */
	public List<String> getAdOrientation() {
		if(adOrientation == null){
			adOrientation = new ArrayList<String>();
			adOrientation.add("all");
		}
		return adOrientation;
	}
    /**
     * 
     * @param adOrientation
     *        the adOrientation to set
     */
	public void setAdOrientation(List<String> adOrientation) {
		this.adOrientation = adOrientation;
	}
    /**
     * 
     * @return the companyType
     */
	public List<String> getCompanyType() {
		if(companyType == null){
			companyType = new ArrayList<String>();
			companyType.add("all");
		}
		return companyType;
	}
    /**
     * 
     * @param companyType
     *          the companyType to set
     */
	public void setCompanyType(List<String> companyType) {
		this.companyType = companyType;
	}
    /**
     * 
     * @return the adType
     */
	public List<String> getAdType() {
		if(adType == null){
			adType = new ArrayList<String>();
			adType.add("all");
		}
		return adType;
	}
    /**
     * 
     * @param adType
     *        the adType to set
     */
	public void setAdType(List<String> adType) {
		this.adType = adType;
	}
    /**
     * 
     * @return the state
     */
	public List<String> getState() {
		if(state == null){
			state = new ArrayList<String>();
			state.add("all");
		}
		return state;
	}
    /**
     * 
     * @param state
     *       the state to set
     */
	public void setState(List<String> state) {
		this.state = state;
	}
    /**
     * 
     * @return the country
     */
	public List<String> getCountry() {
		if(country == null){
			country = new ArrayList<String>();
			country.add("all");
		}
		return country;
	} 
    /**
     * 
     * @param country
     *          the country to set
     */
	public void setCountry(List<String> country) {
		this.country = country;
	}
    /**
     * 
     * @return the publicationTitle
     */
	public List<String> getPublicationTitle() {
		if(publicationTitle == null){
			publicationTitle = new ArrayList<String>();
			publicationTitle.add("all");
		}
		return publicationTitle;
	}
    /**
     * 
     * @param publicationTitle
     *           the publicationTitle to set
     */
	public void setPublicationTitle(List<String> publicationTitle) {
		this.publicationTitle = publicationTitle;
	}
	/**
	 * 
	 * @return the jobDensity
	 */
	public List<String> getJobDensity() {
		if(jobDensity == null){
			jobDensity = new ArrayList<String>();
			jobDensity.add("all");
		}
		return jobDensity;
	}
	/**
	 * 
	 * @param jobDensity
	 * 			the jobDensity to set
	 */
	public void setJobDensity(List<String> jobDensity) {
		this.jobDensity = jobDensity;
	}
    /**
     * 
     * @return the fromMonthRange
     */
	public String getFromMonthRange() {
		if(fromMonthRange == null){
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, -2);
			fromMonthRange = String.valueOf(c.get(Calendar.MONTH));
		}
		return fromMonthRange;
	}
    /**
     * 
     * @param fromMonthRange
     *           the fromMonthRange to set
     */
	public void setFromMonthRange(String fromMonthRange) {
		this.fromMonthRange = fromMonthRange;
	}
    /**
     *  
     * @return the fromYearRange
     */
	public String getFromYearRange() {
		if(fromYearRange == null){
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, -2);
			fromYearRange = String.valueOf(c.get(Calendar.YEAR));
		}
		return fromYearRange;
	}
    /**
     * 
     * @param fromYearRange
     *          the fromYearRange to set
     */
	public void setFromYearRange(String fromYearRange) {
		this.fromYearRange = fromYearRange;
	}
    /**
     * 
     * @return the toMonthRange
     */
	public String getToMonthRange() {
		if(toMonthRange == null){
			Calendar c = Calendar.getInstance();
			toMonthRange = String.valueOf(c.get(Calendar.MONTH));
		}
		return toMonthRange;
	}
    /**
     * 
     * @param toMonthRange
     *         the toMonthRange to set
     */
	public void setToMonthRange(String toMonthRange) {
		this.toMonthRange = toMonthRange;
	}
    /**
     * 
     * @return the toYearRange
     */
	public String getToYearRange() {
		if(toYearRange == null){
			Calendar c = Calendar.getInstance();
			toYearRange = String.valueOf(c.get(Calendar.YEAR));
		}
		return toYearRange;
	}
    /**
     * 
     * @param toYearRange
     *        the toYearRange to set
     */
	public void setToYearRange(String toYearRange) {
		this.toYearRange = toYearRange;
	}
    /**
     * 
     * @return the userService
     */
	public IUserService getUserService() {
		return userService;
	}
    /**
     * 
     * @param userService
     *         the userService to set
     */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
    /**
     * 
     * @return the settingsService
     */
	public ISettingsService getSettingsService() {
		return settingsService;
	}
    /**
     * 
     * @param settingsService
     *          the settingsService to set
     */
	public void setSettingsService(ISettingsService settingsService) {
		this.settingsService = settingsService;
	}
    /**
     * 
     * @return the messageService
     */
	public IMessagesService getMessageService() {
		return messageService;
	} 
    /**
     * 
     * @param messageService
     *         the messageService to set
     */
	public void setMessageService(IMessagesService messageService) {
		this.messageService = messageService;
	}
    /**
     * 
     * @return the deService
     */
	public IDeService getDeService() {
		return deService;
	}
    /**
     * 
     * @param deService
     *         the deService to set
     */
	public void setDeService(IDeService deService) {
		this.deService = deService;
	}
	/**
	 * get the changeRowsPerPage
	 * @return
	 */
	public int getChangeRowsPerPage() {
		return changeRowsPerPage;
	}
	/**
	 * set changeRowsPerPage
	 * @return
	 */
	public void setChangeRowsPerPage(int changeRowsPerPage) {
		this.changeRowsPerPage = changeRowsPerPage;
	}
	/**
	 * DisplayImage on list view
	 * @return
	 */

	public boolean getDisplayImage() {
		return displayImage;
	}
	/**
	 * 
	 * @param displayImage
	 *        the displayImage to set
	 */
	public void setDisplayImage(boolean displayImage) {
		this.displayImage = displayImage;
	}

	/**
	 * Get country List
	 * 
	 * @return List - country List
	 */
	public List<SelectItem> getAllSubscriberCountry() {
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		List countryList = new ArrayList();
		List<SelectItem> selectionCountryList = new ArrayList<SelectItem>();
		List<SelectItem> selectionCountryListDummy = new ArrayList<SelectItem>();
		try {
			countryList = getUserService().getCompanyNameListBySubscriber(subscriberUser.getSubscriber().getId());
			for(int i=0;i<countryList.size();i++)
			{
				DataEntry dataEntry = (DataEntry)countryList.get(i);
				SelectItem sItem = new SelectItem();
				if(dataEntry.getDeCompany() != null){
					sItem.setValue(dataEntry.getDeCompany().getCountry());
					sItem.setLabel(dataEntry.getDeCompany().getCountry());
					selectionCountryListDummy.add(sItem);
				}
			}
			for (Iterator iterator = selectionCountryListDummy.iterator(); iterator
					.hasNext();) {
				SelectItem selectItem1 = (SelectItem) iterator.next();
				boolean flag=removeDummy(selectionCountryList,selectItem1);
				if(flag){
					selectionCountryList.add(selectItem1);
				}
			}
			return selectionCountryList;

		} catch (Exception e) {
			System.out.println("reportsBean::getAllSubscriberCountry"+e.getMessage());
			e.printStackTrace();
		}
		return null;		

	}

	/**
	 * Get stateList
	 * 
	 * @return List - stateList
	 */
	public List<SelectItem> getAllSubscriberState() {
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		List stateList = new ArrayList();
		List<SelectItem> selectionStateList = new ArrayList<SelectItem>();
		List<SelectItem> selectionStateListDummy = new ArrayList<SelectItem>();
		try {
			stateList = getUserService().getCompanyNameListBySubscriber(subscriberUser.getSubscriber().getId());
			for(int i=0;i<stateList.size();i++)
			{
				DataEntry dataEntry = (DataEntry)stateList.get(i);
				SelectItem sItem = new SelectItem();
				if(dataEntry.getDeCompany() != null){
					sItem.setValue(dataEntry.getDeCompany().getState());
					sItem.setLabel(dataEntry.getDeCompany().getState());
					selectionStateListDummy.add(sItem);
				}
			}
			for (Iterator iterator = selectionStateListDummy.iterator(); iterator
					.hasNext();) {
				SelectItem selectItem1 = (SelectItem) iterator.next();
				boolean flag=removeDummy(selectionStateList,selectItem1);
				if(flag){
					selectionStateList.add(selectItem1);
				}
			}
			return selectionStateList;

		} catch (Exception e) {
			System.out.println("reportsBean::getAllSubscriberCompanyName"+e.getMessage());
			e.printStackTrace();
		}
		return null;		

	}

	/**
	 * Get companyNameList
	 * 
	 * @return List - companyNameList
	 */
	public List<SelectItem> getAllSubscriberCompanyName() {
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		List companyNameList = new ArrayList();
		List<SelectItem> selectionCompanyNameList = new ArrayList<SelectItem>();
		List<SelectItem> selectionCompanyNameListDummy = new ArrayList<SelectItem>();
		try {
			companyNameList = getUserService().getCompanyNameListBySubscriber(subscriberUser.getSubscriber().getId());
			for(int i=0;i<companyNameList.size();i++)
			{
				DataEntry dataEntry = (DataEntry)companyNameList.get(i);
				if(dataEntry.getDeCompany() != null){
					SelectItem sItem = new SelectItem();
					sItem.setValue(dataEntry.getDeCompany().getId());
					sItem.setLabel(dataEntry.getDeCompany().getCompanyName());
					selectionCompanyNameListDummy.add(sItem);
				}
			}
			for (Iterator iterator = selectionCompanyNameListDummy.iterator(); iterator
					.hasNext();) {
				SelectItem selectItem1 = (SelectItem) iterator.next();
				boolean flag=removeDummy(selectionCompanyNameList,selectItem1);
				if(flag){
					selectionCompanyNameList.add(selectItem1);
				}
			}
			return selectionCompanyNameList;

		} catch (Exception e) {
			System.out.println("reportsBean::getAllSubscriberCompanyName"+e.getMessage());
			e.printStackTrace();
		}
		return null;		

	}
	/** list the section details
	 * 
	 * @return
	 */
	public List<SelectItem> getNoOfInstitutionType()
	{
		List<Publication> institutionTypeList = new ArrayList<Publication>();
		List<SelectItem> sectionList = new ArrayList<SelectItem>();
		List<SelectItem> sectionListDummy = new ArrayList<SelectItem>();
		try {
			institutionTypeList = userService.findAllInstitutionType();
			for(int i=0;i<institutionTypeList.size();i++)
			{
				Publication publication = institutionTypeList.get(i);
				SelectItem sItem = new SelectItem();
				sItem.setValue(publication.getId());
				sItem.setLabel(publication.getPublicationTitle());
				sectionListDummy.add(sItem);
			}
			for (Iterator iterator = sectionListDummy.iterator(); iterator
					.hasNext();) {
				SelectItem selectItem1 = (SelectItem) iterator.next();
				boolean flag=removeDummy(sectionList,selectItem1);
				if(flag){
					sectionList.add(selectItem1);
				}
			}
			return sectionList;

		} catch (Exception e) {
			System.out.println("reportsBean::getNoOfInstitutionType"+e.getMessage());
			e.printStackTrace();
		}
		return null;		
	}

	/**
	 * Get page list
	 * 
	 * @return List - pageList
	 */
	public List<SelectItem> getAllSubscriberByPage() {
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		List pageList = new ArrayList();
		List<SelectItem> selectionPageList = new ArrayList<SelectItem>();
		List<SelectItem> selectionPageListDummy = new ArrayList<SelectItem>();
		try {
			pageList = getUserService().getPublicationListBySubscriber(subscriberUser.getSubscriber().getId());
			for(int i=0;i<pageList.size();i++)
			{
				ParentImage parentImage = (ParentImage)pageList.get(i);
				SelectItem sItem = new SelectItem();
				if(parentImage.getPublicationTitle() != null){
					sItem.setValue(parentImage.getPage());
					sItem.setLabel(parentImage.getPage());
					selectionPageListDummy.add(sItem);
				}
			}
			for (Iterator iterator = selectionPageListDummy.iterator(); iterator
					.hasNext();) {
				SelectItem selectItem1 = (SelectItem) iterator.next();
				boolean flag=removeDummy(selectionPageList,selectItem1);
				if(flag){
					selectionPageList.add(selectItem1);
				}
			}
			return selectionPageList;

		} catch (Exception e) {
			System.out.println("reportsBean::getAllSubscriberByPage"+e.getMessage());
			e.printStackTrace();
		}
		return null;		

	}

	/**
	 * Get page list
	 * 
	 * @return List - pageList
	 */
	public List<SelectItem> getAllSubscriberByPublicationTitle() {
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		List pageList = new ArrayList();
		List<SelectItem> selectionPageList = new ArrayList<SelectItem>();
		List<SelectItem> selectionPageListDummy = new ArrayList<SelectItem>();
		try {
			pageList = getUserService().getPublicationListBySubscriber(subscriberUser.getSubscriber().getId());
			for(int i=0;i<pageList.size();i++)
			{
				ParentImage parentImage = (ParentImage)pageList.get(i);
				SelectItem sItem = new SelectItem();
				if(parentImage.getPublicationTitle() != null){
					sItem.setValue(parentImage.getPublicationTitle().getId());
					sItem.setLabel(parentImage.getPublicationTitle().getPublicationTitle());
					selectionPageListDummy.add(sItem);
				}
			}
			for (Iterator iterator = selectionPageListDummy.iterator(); iterator
					.hasNext();) {
				SelectItem selectItem1 = (SelectItem) iterator.next();
				boolean flag=removeDummy(selectionPageList,selectItem1);
				if(flag){
					selectionPageList.add(selectItem1);
				}
			}
			return selectionPageList;

		} catch (Exception e) {
			System.out.println("reportsBean::getSubscriberByPublicationTitle"+e.getMessage());
			e.printStackTrace();
		}
		return null;		

	}

	/**
	 * Get page list
	 * 
	 * @return List - pageList
	 */
	public List<SelectItem> getAllSubscriberBySection() {
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		List pageList = new ArrayList();
		List<SelectItem> selectionPageList = new ArrayList<SelectItem>();
		List<SelectItem> selectionPageListDummy = new ArrayList<SelectItem>();
		try {
			pageList = getUserService().getPublicationListBySubscriber(subscriberUser.getSubscriber().getId());
			for(int i=0;i<pageList.size();i++)
			{
				ParentImage parentImage = (ParentImage)pageList.get(i);
				SelectItem sItem = new SelectItem();
				if(parentImage.getSection() != null){
					sItem.setValue(parentImage.getSection().getId());
					sItem.setLabel(parentImage.getSection().getPublicationTitle());
					selectionPageListDummy.add(sItem);
				}
			}
			for (Iterator iterator = selectionPageListDummy.iterator(); iterator
					.hasNext();) {
				SelectItem selectItem1 = (SelectItem) iterator.next();
				boolean flag=removeDummy(selectionPageList,selectItem1);
				if(flag){
					selectionPageList.add(selectItem1);
				}
			}
			return selectionPageList;

		} catch (Exception e) {
			System.out.println("reportsBean::getSubscriberBySection"+e.getMessage());
			e.printStackTrace();
		}
		return null;		

	}
	/**
	 * 
	 * @throws JRException
	 * @throws IOException
	 * @throws ParseException 
	 */
	public String generateSearchViewReportList() throws JRException, IOException, ParseException
	{
		searchViewReportList = new ArrayList();
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		//searchViewReportList = new ArrayList();

		List<String> companyName 			= new ArrayList(this.companyName);
		List<String> publicationSection 	= new ArrayList(this.publicationSection);
		List<String> publicationPage 		= new ArrayList(this.publicationPage);
		List<String> adSize 				= new ArrayList(this.adSize);
		List<String> adOrientation 			= new ArrayList(this.adOrientation);
		List<String> companyType 			= new ArrayList(this.companyType);
		List<String> adType 				= new ArrayList(this.adType);
		List<String> state 					= new ArrayList(this.state);
		List<String> country 				= new ArrayList(this.country);
		List<String> publicationTitle 		= new ArrayList(this.publicationTitle);
		List<String> jobDensity 			= new ArrayList(this.jobDensity);
		
		/*
		System.out.println("fromMonthRange:"+this.fromMonthRange);
		//System.out.println("adFormat:"+this.adFormat);
		System.out.println("fromYearRange:"+this.fromYearRange);
		System.out.println("toMonthRange:"+this.toMonthRange);
		System.out.println("toYearRange:"+this.toYearRange);
		System.out.println("companyName:"+this.companyName);
		System.out.println("publicationSection:"+this.publicationSection);
		System.out.println("publicationPage:"+this.publicationPage);
		System.out.println("adSize:"+this.adSize);
		System.out.println("companyType:"+this.companyType);
		System.out.println("adType:"+this.adType);
		System.out.println("adOrientation:"+this.adOrientation);
		System.out.println("state:"+this.state);
		System.out.println("country:"+this.country);
		System.out.println("publicationTitle:"+this.publicationTitle);
		*/
		
		if(subscriberUser != null && this.fromYearRange!=null && this.toYearRange!=null && this.fromMonthRange!=null && this.toMonthRange!=null ){
			String DATE_FORMAT = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat (DATE_FORMAT);	
			String fromDateString=this.fromYearRange+"-"+this.fromMonthRange+"-01";
			String toDateString=this.toYearRange+"-"+this.toMonthRange+"-31";
			Date fromDate=dateFormat.parse(fromDateString);
			Date toDate=dateFormat.parse(toDateString);	
			
			removeAllFilterFromSearchCriteria(companyName,publicationSection,publicationPage,adSize,
					adOrientation,companyType,adType,state,country,publicationTitle,jobDensity);
			
			if(fromDate.before(toDate)){
				searchViewReportList = getUserService().searchViewReportList(subscriberUser.getSubscriber().getId(),dateFormat.format(fromDate),dateFormat.format(toDate),companyName,publicationSection,publicationPage,adSize,companyType,adType,state,country,publicationTitle);
				if(searchViewReportList!=null && !searchViewReportList.isEmpty()){
					Collections.reverse(searchViewReportList);
					createReportFields(searchViewReportList);
				} else {
					this.messageService.messageInformation(null, "No Record Founded in Your Search Criteria");
				}
			} else {
				this.messageService.messageError(null, "Please Check your Date Range");
				
			}
		} 
		return null;	
	}

	public void removeAllFilterFromSearchCriteria(List<String> companyName,List<String> publicationSection,List<String> publicationPage,List<String> adSize,
			List<String> adOrientation,List<String> companyType,List<String> adType,List<String> state,List<String> country,List<String> publicationTitle,List<String> jobDensity
			){
		companyName.remove("all");
		publicationSection.remove("all");
		publicationPage.remove("all");
		adSize.remove("all");
		adOrientation.remove("all");
		companyType.remove("all");
		adType.remove("all");
		state.remove("all");
		country.remove("all");
		publicationTitle.remove("all");
		jobDensity.remove("all");
		
		/*
		System.out.println("=============================================");
		System.out.println("After Removal of local Data !!!");
		System.out.println("=============================================");

		System.out.println("companyName:"+companyName);
		System.out.println("publicationSection:"+publicationSection);
		System.out.println("publicationPage:"+publicationPage);
		System.out.println("adSize:"+adSize);
		System.out.println("companyType:"+companyType);
		System.out.println("adType:"+adType);
		System.out.println("adOrientation:"+adOrientation);
		System.out.println("state:"+state);
		System.out.println("country:"+country);
		System.out.println("publicationTitle:"+publicationTitle);
		System.out.println("=============================================");
		*/
	}
	
	/**
	 * Get region List
	 * 
	 * @return List - region List
	 */
	public List getCalendarViewReports() {
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		List calendarViewReportList = new ArrayList();
		if(subscriberUser != null){
			calendarViewReportList.addAll(getUserService().getCalendarViewReportList(subscriberUser.getSubscriber().getId(),this.reportCurrentMonthFirst, this.reportCurrentYearFirst, this.reportCurrentMonthSecond, this.reportCurrentYearSecond,this.reportCurrentMonthThird, this.reportCurrentYearThird,this.reportCurrentMonthFour, this.reportCurrentYearFour));
		}
		Collections.reverse(calendarViewReportList);
		if(this.checkPreVal == false){
			Collections.reverse(calendarViewReportList);
		}
		return calendarViewReportList;

	}

	/**
	 * Page load event for selected company user in admin page
	 * @param event
	 */
	public void loadCurrentMonthAndYear(ComponentSystemEvent event)
	{
		try {
			if (!FacesContext.getCurrentInstance().isPostback()) 
			{
				if(this.reportCurrentMonth ==0 && this.reportCurrentYear ==0 ){
					Calendar cal = Calendar.getInstance();
					this.reportCurrentMonth = cal.get(Calendar.MONTH)+1;
					this.reportCurrentYear = cal.get(Calendar.YEAR);
				}
				reportNextMonthYear(this.reportCurrentMonth,this.reportCurrentYear);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	/**
	 * Change the user password who currently logged in application
	 * @return
	 */
	public String reportNextMonthYear(int latestmonth,int latestyear){
		try {
			this.reportFirstMonthYear="";
			this.reportThirdMonthYear="";
			this.reportSecondMonthYear="";
			this.reportFourMonthYear="";
			FacesUtils facesUtils = new FacesUtils();
			String preValue = facesUtils.getRequestParameterMap("preValue");
			int preValueInt = Integer.valueOf(preValue!=null?preValue:"0");
			if(preValueInt ==1){
				checkPreVal = false;
			}
			else{
				checkPreVal =true;
			}
			reportCurrentMonthFirst = latestmonth;
			reportCurrentYearFirst = latestyear;
			if( reportCurrentMonthFirst > 12){
				reportCurrentMonthFirst = reportCurrentMonthFirst % 12;
				reportCurrentYearFirst = latestyear+1;
			}
			this.reportFirstMonthYear = reportFirstMonthYear +(reportCurrentMonthFirst)+"/"+reportCurrentYearFirst;
			reportCurrentYearSecond = reportCurrentYearFirst;
			reportCurrentMonthSecond = reportCurrentMonthFirst+1;
			if(reportCurrentMonthSecond > 12){
				reportCurrentMonthSecond = reportCurrentMonthSecond % 12;
				reportCurrentYearSecond = latestyear+1;
			}
			this.reportSecondMonthYear = reportSecondMonthYear+(reportCurrentMonthSecond)+"/"+reportCurrentYearSecond;
			reportCurrentYearThird = reportCurrentYearSecond;
			reportCurrentMonthThird = reportCurrentMonthSecond+1;
			if(reportCurrentMonthThird > 12){
				reportCurrentMonthThird = reportCurrentMonthThird % 12;
				reportCurrentYearThird = latestyear+1;
			}
			this.reportThirdMonthYear = reportThirdMonthYear+(reportCurrentMonthThird)+"/"+reportCurrentYearThird;
			reportCurrentYearFour = reportCurrentYearThird;
			reportCurrentMonthFour = reportCurrentMonthThird+1;
			if(reportCurrentMonthFour > 12){
				reportCurrentMonthFour = reportCurrentMonthFour % 12;
				reportCurrentYearFour = latestyear+1;
			}
			this.reportFourMonthYear = reportFourMonthYear+(reportCurrentMonthFour)+"/"+reportCurrentYearFour;
			this.reportCurrentMonth= reportCurrentMonthFour;
			this.reportCurrentYear = reportCurrentYearFour;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * Change the user password who currently logged in application
	 * @return
	 */
	/*	public String reportPreMonthYear(int latestmonth,int latestyear){
		try {
			this.reportFirstMonthYear="";
			this.reportThirdMonthYear="";
			this.reportSecondMonthYear="";
			this.reportFourMonthYear="";
			reportCurrentMonthFour = latestmonth;
			reportCurrentYearFour = latestyear;
			if( reportCurrentMonthFour  == 0){
				reportCurrentMonthFour = reportCurrentMonthFour + 12;
				reportCurrentYearFour = latestyear-1;
			}
			this.reportFourMonthYear  = reportFourMonthYear +(reportCurrentMonthFour)+"/"+reportCurrentYearFour;
			reportCurrentYearThird = reportCurrentYearFour;
			reportCurrentMonthThird = reportCurrentMonthThird-1;
			if(reportCurrentMonthThird == 0){
				reportCurrentMonthThird = reportCurrentMonthThird + 12;
				reportCurrentYearThird = latestyear-1;
			}
			this.reportThirdMonthYear  = reportThirdMonthYear+(reportCurrentMonthThird)+"/"+reportCurrentYearThird;
			reportCurrentYearSecond = reportCurrentYearThird;
			reportCurrentMonthSecond = reportCurrentMonthThird-1;
			if(reportCurrentMonthSecond  == 0){
				reportCurrentMonthSecond = reportCurrentMonthSecond + 12;
				reportCurrentYearSecond = latestyear-1;
			}
			this.reportSecondMonthYear = reportSecondMonthYear+(reportCurrentMonthSecond)+"/"+reportCurrentYearSecond;
			reportCurrentYearFirst = reportCurrentYearSecond;
			reportCurrentMonthFirst = reportCurrentMonthSecond-1;
			if(reportCurrentMonthFirst  == 0){
				reportCurrentMonthFirst = reportCurrentMonthFirst + 12;
				reportCurrentYearFirst = latestyear-1;
			}
			this.reportFirstMonthYear = reportFirstMonthYear+(reportCurrentMonthFirst)+"/"+reportCurrentYearFirst;
			this.reportCurrentMonth= reportCurrentMonthFour;
			this.reportCurrentYear = reportCurrentYearFour;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	public String reportPreMonthYear(int latestmonth,int latestyear){
		try {
			this.reportFirstMonthYear="";
			this.reportThirdMonthYear="";
			this.reportSecondMonthYear="";
			this.reportFourMonthYear="";
			reportCurrentMonthFour = latestmonth;
			reportCurrentYearFour = latestyear;
			if( reportCurrentMonthFour  <= 0){
				reportCurrentMonthFour = reportCurrentMonthFour + 12;
				reportCurrentYearFour = latestyear-1;
			}
			this.reportFourMonthYear  = reportFourMonthYear +(reportCurrentMonthFour)+"/"+reportCurrentYearFour;
			reportCurrentYearThird = reportCurrentYearFour;
			reportCurrentMonthThird = reportCurrentMonthFour-1;
			if(reportCurrentMonthThird <= 0){
				reportCurrentMonthThird = reportCurrentMonthThird + 12;
				reportCurrentYearThird = latestyear-1;
			}
			this.reportThirdMonthYear  = reportThirdMonthYear+(reportCurrentMonthThird)+"/"+reportCurrentYearThird;
			reportCurrentYearSecond = reportCurrentYearThird;
			reportCurrentMonthSecond = reportCurrentMonthThird-1;
			if(reportCurrentMonthSecond  <= 0){
				reportCurrentMonthSecond = reportCurrentMonthSecond + 12;
				reportCurrentYearSecond = latestyear-1;
			}
			this.reportSecondMonthYear = reportSecondMonthYear+(reportCurrentMonthSecond)+"/"+reportCurrentYearSecond;
			reportCurrentYearFirst = reportCurrentYearSecond;
			reportCurrentMonthFirst = reportCurrentMonthSecond-1;
			if(reportCurrentMonthFirst  <= 0){
				reportCurrentMonthFirst = reportCurrentMonthFirst + 12;
				reportCurrentYearFirst = latestyear-1;
			}
			this.reportFirstMonthYear = reportFirstMonthYear+(reportCurrentMonthFirst)+"/"+reportCurrentYearFirst;
			this.reportCurrentMonth= reportCurrentMonthFour;
			this.reportCurrentYear = reportCurrentYearFour;
			System.out.print("\n this.reportCurrentMonth\\this.reportCurrentYear"+this.reportCurrentMonth+"\\"+this.reportCurrentYear);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void createCalendarViewReport() throws JRException, IOException
	{

		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		List calendarViewReportList = new ArrayList();
		if(subscriberUser != null){
			calendarViewReportList = getUserService().getCalendarViewReportList(subscriberUser.getSubscriber().getId(),this.reportCurrentMonthFirst, this.reportCurrentYearFirst, this.reportCurrentMonthSecond, this.reportCurrentYearSecond,this.reportCurrentMonthThird, this.reportCurrentYearThird,this.reportCurrentMonthFour, this.reportCurrentYearFour);
		}
		Collections.reverse(calendarViewReportList);
	}
	public String createCalendarViewReportDetails() throws JRException, IOException
	{
		FacesUtils facesUtils = new FacesUtils();
		String pageName= facesUtils.getRequestParameterMap("page_name");
		calendarViewReportDetails = new ArrayList();
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		HttpServletRequest request = null;
		File fileHTML = null;
		File filePDF = null;
		File fileCSV = null;
		// load calendar details
		if(pageName.equals("calendar") && pageName != null){
			setCalendarDetails("calendar");
		} else if(pageName.equals("journal") && pageName != null){ 
			setJournalDetails();
		} else {
			setCalendarDetails("advertiser");
		}
		/*int reportMonth =0;
		int reportYear=0;
		long deCompanyId =0;
		FacesUtils facesUtils = new FacesUtils();
		String reportMonthAndYear = facesUtils.getRequestParameterMap("reportMonthAndYear");
		if(reportMonthAndYear != null){
			//System.out.println("monthAndYear:"+monthAndYear);
			String[] split = reportMonthAndYear.split("/");
			reportMonth =Integer.valueOf(split[0]);
			reportYear = Integer.valueOf(split[1]);
			//System.out.println("monthAndYear:"+split[0]);
			//System.out.println("monthAndYear:"+split[1]);
		}
		String companyId = facesUtils.getRequestParameterMap("companyId");
		System.out.println("companyName:"+companyId);
		if(companyId != null && !companyId.isEmpty() ){
			deCompanyId =Long.valueOf(companyId);
		}
		if(subscriberUser != null){
			calendarViewReportDetails = getUserService().getCalendarViewReportDetails(subscriberUser.getSubscriber().getId(),reportMonth, reportYear,deCompanyId);
		}
		Collections.reverse(calendarViewReportDetails); 
		createReportFields(calendarViewReportDetails);*/
		String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String jrXMLPath = CommonProperties.getBasePath()+CommonProperties.getContextPath()+"/pages/jasperReports/"+ CALENDARVIEW;//calendar_report.jasper 
		String sContextFullpath = CommonProperties.getBasePath()+CommonProperties.getImageContextPath()+CommonProperties.getReportPath()+subscriberUser.getSubscriber().getId();
		String outPutFile = sContextFullpath +"/"+ "ReportOn"+today;
		fileHTML = new File (sContextFullpath);
		if(!fileHTML.exists()){
			fileHTML.mkdirs();
		}else{
			File [] files = fileHTML.listFiles();
			if(files.length >0){
				for (int i = 0; i <files.length; i++){
					files[i].delete();
				}
			}
		}
		fileHTML = new File ( outPutFile + ".html" );
		filePDF = new File ( outPutFile + ".pdf" );
		filePDF.createNewFile ();
		fileHTML.createNewFile ();
		Map paramMap = new HashMap ();
		boolean rprtFlag = false;
		paramMap = new HashMap ();
		paramMap.put ( JRM_OUT_HTML, "true" );
		paramMap.put ( JRM_OUT_PDF, "" + this.lbPDF);
		paramMap.put ( JRM_JRXML_PATH, jrXMLPath );
		paramMap.put ( JRM_IS_BEAN_COLLECTION, "true" );
		paramMap.put ( JRM_BEAN_COLLECTION, targetList );
		paramMap.put ( JRM_OUT_HTML_FILE, fileHTML );
		paramMap.put ( JRM_OUT_PDF_FILE, filePDF);
		String jScript = ReportManager.getHTMLJavaScript ( this.lbPrint, this.lbPDF, filePDF.getName (),false,"",false,"" );
		paramMap.put ( JRM_IS_HEADER, "true" );
		paramMap.put ( JRM_HEADER_VALUE, jScript );
		System.out.println(Arrays.asList(GraphicsEnvironment
				.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
		rprtFlag = ReportManager.callReportGenerator ( paramMap );
		if ( rprtFlag )
		{
			String contents;
			InputStream is = new FileInputStream(new File(CommonProperties.getBasePath()+CommonProperties.getImageContextPath()+CommonProperties.getReportPath()+subscriberUser.getSubscriber().getId()+"/"+fileHTML.getName())); 
			try {
				contents = IOUtils.toString(is, "UTF-8");
			} finally {
				IOUtils.closeQuietly(is);
			}
			contents = contents.replaceAll("&nbsp;", "");
			try {
				BufferedWriter output = new BufferedWriter(new FileWriter(fileHTML));
				output.write(contents);
				output.close();
			} catch ( IOException e ) {
				e.printStackTrace();
			}
			this.calendatViewHtml = fileHTML.getName();
			//System.out.println("calendatViewHtml:"+calendatViewHtml);
			this.calendatReportPath = CommonProperties.getBaseURL()+CommonProperties.getImageContextPath()+CommonProperties.getReportPath()+subscriberUser.getSubscriber().getId()+"/"+this.calendatViewHtml;
			//System.out.println("calendatReportPath:"+calendatReportPath);
		}
		if(pageName.equals("calendar") && pageName != null)
			return CALENTARJASPERVIEW;
		else if(pageName.equals("journal") && pageName != null)
			return JOURNALJASPERVIEW;
		else 
			return ADVERTISERJASPERVIEW;
	}


	private void createReportFields(List calendarViewReportDetails) {
		targetList = new ArrayList ();
		for(int i=0;i<calendarViewReportDetails.size();i++ ){
			DataEntry dataEntry = (DataEntry)calendarViewReportDetails.get(i);
			if(dataEntry != null){
				ReportFields reportFields = new ReportFields();
				reportFields.setJobId(String.valueOf(dataEntry.getId()));
				reportFields.setPublicationId(dataEntry.getParentImage().getPublicationTitle().getId());
				reportFields.setPublication(dataEntry.getParentImage().getPublicationTitle().getPublicationTitle());
				reportFields.setSectionId(dataEntry.getParentImage().getSection().getId());
				reportFields.setSection(dataEntry.getParentImage().getSection().getPublicationTitle());
				reportFields.setPage(dataEntry.getParentImage().getPage());
				String dateString = new SimpleDateFormat("MM-dd-yyyy").format(dataEntry.getParentImage().getIssueDate());
				reportFields.setIssueDate(dateString);
				reportFields.setCompanyId(dataEntry.getDeCompany().getId());
				reportFields.setCompanyName(dataEntry.getDeCompany().getCompanyName());
				reportFields.setCity(dataEntry.getDeCompany().getCity());
				reportFields.setState(dataEntry.getDeCompany().getState());
				reportFields.setCountry(dataEntry.getDeCompany().getCountry());
				reportFields.setAdType(dataEntry.getAdType());
				reportFields.setAdOrientation(dataEntry.getAdOrientation());
				reportFields.setJobDensity(dataEntry.getJobDensity());
				reportFields.setAdSize(dataEntry.getAdSize());
				String parentImage= CommonProperties.getBaseURL()+CommonProperties.getImageContextPath()+CommonProperties.getParentImagePath()+dataEntry.getParentImage().getId()+"/"+dataEntry.getParentImage().getImageName();
				//System.out.println("parentImagePath:"+parentImage);
				reportFields.setParentImageName(parentImage);
				//reportFields.setParentImageName(dataEntry.getParentImage().getImageName());
				if(dataEntry.getChildImage()!=null){
					String childImage = CommonProperties.getBaseURL()+CommonProperties.getImageContextPath()+CommonProperties.getChildImagePath()+dataEntry.getChildImage().getParentImage().getId()+"/"+dataEntry.getChildImage().getId()+"/"+dataEntry.getChildImage().getImageName();
					reportFields.setThumbnailImageName(childImage);
				}
				//System.out.println("childImagePath:"+childImage);
				//reportFields.setChildImageName(dataEntry.getChildImage().getImageName());
				reportFields.setCompanyType(dataEntry.getAdType());
				reportFields.setOcrText(dataEntry.getOcrText());
				reportFields.setPlacementType("");
				reportFields.setMarketOrsectorType("");
				reportFields.setJobTitle("");
				targetList.add(reportFields);
			}
		}

	}
	/**
	 * 
	 * @return the MANAGE_REPORTS
	 */
	public String calendarViewReport(){

		return MANAGE_REPORTS;

	}
	/**
	 * 
	 * @return the SEARCHREPORTVIEW
	 */
	public String searchViewReport(){

		return SEARCHREPORTVIEW;

	}
	/**
	 * 
	 * @return the ADVERTISERREPORTVIEW
	 */
	public String advertiserViewReport(){

		return ADVERTISERREPORTVIEW;

	}
	/**
	 * 
	 * @return the JOURNALREPORTVIEW
	 */
	public String journalViewReport(){

		return JOURNALREPORTVIEW;

	}
	public String createSearchViewReportDetails() throws JRException, IOException
	{
		searchViewReportList = new ArrayList();
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		HttpServletRequest request = null;
		File fileHTML = null;
		File filePDF = null;
		File fileCSV = null;

		String fromDateString=null;
		String toDateString=null;

		int reportMonth =0;
		int reportYear=0;
		long deCompanyId =0;
		FacesUtils facesUtils = new FacesUtils();
		String searchViewCompanyName = facesUtils.getRequestParameterMap("companyName");
		String searchViewPublicationSection = facesUtils.getRequestParameterMap("publicationSection");
		String searchViewPublicationPage = facesUtils.getRequestParameterMap("publicationPage");
		String searchViewAdSize = facesUtils.getRequestParameterMap("adSize");
		//String searchViewCompanyType = facesUtils.getRequestParameterMap("companyType");
		String searchViewAdOrientation = facesUtils.getRequestParameterMap("adOrientation");
		String searchViewAdType = facesUtils.getRequestParameterMap("adType");
		String searchViewState = facesUtils.getRequestParameterMap("state");
		String searchViewCountry = facesUtils.getRequestParameterMap("country");
		String searchViewPublicationTitle = facesUtils.getRequestParameterMap("publicationTitle");
		String searchViewJobDensity = facesUtils.getRequestParameterMap("jobDensity");

		if(subscriberUser != null && searchViewCompanyName != null  
				&& searchViewPublicationSection != null
				&& searchViewPublicationPage != null
				&& searchViewAdSize != null
				&& searchViewAdOrientation != null
				&& searchViewAdType != null
				&& searchViewState != null 
				&& searchViewCountry != null
				&& searchViewPublicationTitle != null
				&& searchViewJobDensity != null){

			if(this.fromYearRange!=null && this.toYearRange!=null && this.fromMonthRange!=null && this.toMonthRange!=null ){				
				String DATE_FORMAT = "yyyy-MM-dd";
				DateFormat dateFormat = new SimpleDateFormat (DATE_FORMAT);	
				fromDateString=this.fromYearRange+"-"+this.fromMonthRange+"-01";
				toDateString=this.toYearRange+"-"+this.toMonthRange+"-31";
			}
			searchViewReportList = getUserService().searchViewReportDetails(subscriberUser.getSubscriber().getId(),fromDateString,toDateString,searchViewCompanyName, searchViewPublicationSection,searchViewPublicationPage, searchViewAdSize, searchViewAdOrientation, /*searchViewCompanyType,*/searchViewAdType,searchViewState,searchViewCountry,searchViewPublicationTitle,searchViewJobDensity);
		}
		Collections.reverse(searchViewReportList);
		createReportFields(searchViewReportList);
		String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String jrXMLPath = CommonProperties.getBasePath()+CommonProperties.getContextPath()+"/pages/jasperReports/"+ CALENDARVIEW;//calendar_report.jasper 
		String sContextFullpath = CommonProperties.getBasePath()+CommonProperties.getImageContextPath()+CommonProperties.getReportPath()+subscriberUser.getSubscriber().getId();
		String outPutFile = sContextFullpath +"/"+ "ReportOn"+today;
		fileHTML = new File (sContextFullpath);
		if(!fileHTML.exists()){
			fileHTML.mkdirs();
		}else{
			File [] files = fileHTML.listFiles();
			if(files.length >0){
				for (int i = 0; i <files.length; i++){
					files[i].delete();
				}
			}
		}
		fileHTML = new File ( outPutFile + ".html" );
		filePDF = new File ( outPutFile + ".pdf" );
		filePDF.createNewFile ();
		fileHTML.createNewFile ();
		Map paramMap = new HashMap ();
		boolean rprtFlag = false;
		paramMap = new HashMap ();
		paramMap.put ( JRM_OUT_HTML, "true" );
		paramMap.put ( JRM_OUT_PDF, "" + this.lbPDF);
		paramMap.put ( JRM_JRXML_PATH, jrXMLPath );
		paramMap.put ( JRM_IS_BEAN_COLLECTION, "true" );
		paramMap.put ( JRM_BEAN_COLLECTION, targetList );
		paramMap.put ( JRM_OUT_HTML_FILE, fileHTML );
		paramMap.put ( JRM_OUT_PDF_FILE, filePDF);
		String jScript = ReportManager.getHTMLJavaScript ( this.lbPrint, this.lbPDF, filePDF.getName (),false,"",false,"" );
		paramMap.put ( JRM_IS_HEADER, "true" );
		paramMap.put ( JRM_HEADER_VALUE, jScript );
		System.out.println(Arrays.asList(GraphicsEnvironment
				.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
		rprtFlag = ReportManager.callReportGenerator ( paramMap );
		if ( rprtFlag )
		{
			String contents;
			InputStream is = new FileInputStream(new File(CommonProperties.getBasePath()+CommonProperties.getImageContextPath()+CommonProperties.getReportPath()+subscriberUser.getSubscriber().getId()+"/"+fileHTML.getName())); 
			try {
				contents = IOUtils.toString(is, "UTF-8");
			} finally {
				IOUtils.closeQuietly(is);
			}
			contents = contents.replaceAll("&nbsp;", "");
			try {
				BufferedWriter output = new BufferedWriter(new FileWriter(fileHTML));
				output.write(contents);
				output.close();
			} catch ( IOException e ) {
				e.printStackTrace();
			}
			this.calendatViewHtml = fileHTML.getName();
			System.out.println("calendatViewHtml:"+calendatViewHtml);
			this.calendatReportPath = CommonProperties.getBaseURL()+CommonProperties.getImageContextPath()+CommonProperties.getReportPath()+subscriberUser.getSubscriber().getId()+"/"+this.calendatViewHtml;
			System.out.println("calendatReportPath:"+calendatReportPath);
		}
		return LISTJASPERVIEW;
	}
	/**
	 * Get region List
	 * 
	 * @return List - region List
	 */
	public List getAdvertiserViewReports() {
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		List advertiserViewReportList = new ArrayList();
		if(subscriberUser != null){
			advertiserViewReportList.addAll(getUserService().getAdvertiserViewReportList(subscriberUser.getSubscriber().getId(),this.reportCurrentMonthFirst, this.reportCurrentYearFirst, this.reportCurrentMonthSecond, this.reportCurrentYearSecond,this.reportCurrentMonthThird, this.reportCurrentYearThird,this.reportCurrentMonthFour, this.reportCurrentYearFour));
		}
		Collections.reverse(advertiserViewReportList);
		if(this.checkPreVal == false){
			Collections.reverse(advertiserViewReportList);
		}
		return advertiserViewReportList;

	}
	/**
	 * Get region List
	 * 
	 * @return List - region List
	 */
	public List getJournalViewReports() {
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		List calendarViewReportList = new ArrayList();
		if(subscriberUser != null){
			calendarViewReportList.addAll(getUserService().getJournalViewReportList(subscriberUser.getSubscriber().getId()));
		}
		Collections.reverse(calendarViewReportList);
		/*if(this.checkPreVal == false){
			Collections.reverse(calendarViewReportList);
		}*/
		return calendarViewReportList;

	}
	/**
	 * Set 
	 */
	public String loadCalendarViewReportDetails()
	{
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		if(subscriberUser!=null){
			FacesUtils facesUtils = new FacesUtils();
			String reportMonthAndYear = facesUtils.getRequestParameterMap("reportMonthAndYear");
			String companyId = facesUtils.getRequestParameterMap("companyId");
			String pageName= facesUtils.getRequestParameterMap("page_name");
			if(reportMonthAndYear!=null && companyId!=null && pageName!=null){
				sessionManager.setSessionAttributeInSession(SessionManager.REPORTCURRENTMONTHYEAR, reportMonthAndYear);
				sessionManager.setSessionAttributeInSession(SessionManager.REPORTCURRENTITEMID, companyId);

				if(pageName.equals("calendar")){
					return "/pages/reports/subscriber_calendar_viewlist.xhtml?faces-redirect=true";
				} else {
					return "/pages/reports/subscriber_advertiser_viewlist.xhtml?faces-redirect=true";
				}

			}
		}
		return null;
	}
	/**
	 * loadCalenderInfo
	 */
	public void loadCalenderInfo(ComponentSystemEvent event)
	{
		try {
			if (!FacesContext.getCurrentInstance().isPostback()) 
			{
				setCalendarDetails("calendar");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * loadCalenderInfo
	 */
	public void loadAdvertiserInfo(ComponentSystemEvent event)
	{
		try {
			if (!FacesContext.getCurrentInstance().isPostback()) 
			{
				setCalendarDetails("advertiser");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	public void setCalendarDetails(String pageName){
		calendarViewReportDetails = new ArrayList();
		int reportMonth =0;
		int reportYear=0;
		long deCompanyId =0;
		//get month and year
		Object monthyearObj = sessionManager.getSessionAttribute(SessionManager.REPORTCURRENTMONTHYEAR);
		String reportMonthAndYear = monthyearObj!=null?(String)monthyearObj:null;
		if(reportMonthAndYear != null){
			String[] split = reportMonthAndYear.split("/");
			reportMonth =Integer.valueOf(split[0]);
			reportYear = Integer.valueOf(split[1]);
		}

		// get selected company id
		Object itemObj = sessionManager.getSessionAttribute(SessionManager.REPORTCURRENTITEMID);
		String companyId = itemObj!=null?(String)itemObj:null;
		if(companyId != null && !companyId.isEmpty() ){
			deCompanyId =Long.valueOf(companyId);
		}

		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		if(subscriberUser!=null){
			if(pageName.equals("calendar")){
				calendarViewReportDetails = getUserService().getCalendarViewReportDetails(subscriberUser.getSubscriber().getId(),reportMonth, reportYear,deCompanyId);
			} else {
				calendarViewReportDetails = getUserService().getAdvertiserViewReportDetails(subscriberUser.getSubscriber().getId(),reportMonth, reportYear,deCompanyId);
			}
			Collections.reverse(calendarViewReportDetails); 
			createReportFields(calendarViewReportDetails);
		}
	}
	/**
	 * displaying datatable row per page
	 * default value is 10
	 * @return
	 */
	public int getRowsPerPage(){
		try{
			String rowsPerPage = (String)sessionManager.getSessionAttribute(SessionManager.ROWSPERPAGE);
			if(!StringUtils.isEmpty(rowsPerPage)){
				return Integer.valueOf(rowsPerPage);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return changeRowsPerPage; //default value
	}

	/**
	 * Get region List
	 * 
	 * @return List - region List
	 */
	public List getJournalViewList() {
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		List jouralOfSubscriberList = new  ArrayList();
		List<String[]> jouralOfSubscriber = new  ArrayList<String[]>();
		if(subscriberUser != null){
			jouralOfSubscriberList.addAll(getUserService().getSubscriberJournalList(subscriberUser.getSubscriber().getId()));
		}
		int i=0;
		for (Iterator iterator = jouralOfSubscriberList.iterator(); iterator.hasNext();) {
			Object[] obj=(Object[]) iterator.next();
			String journal[] = new String[3];
			journal[0]=String.valueOf(obj[0]);
			journal[1]=(String) obj[1];
			journal[2]=String.valueOf(++i);
			jouralOfSubscriber.add(journal);
		}
		return jouralOfSubscriber;

	}
	/**
	 * Set 
	 */
	public String loadJournalViewReportDetails()
	{
		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		if(subscriberUser!=null){
			FacesUtils facesUtils = new FacesUtils();
			String journalId = facesUtils.getRequestParameterMap("journalId");
			String companyId = facesUtils.getRequestParameterMap("companyId");
			if(journalId!=null && companyId!=null){
				sessionManager.setSessionAttributeInSession(SessionManager.REPORTCURRENTJOURNALID, journalId);
				sessionManager.setSessionAttributeInSession(SessionManager.REPORTCURRENTITEMID, companyId);
				return "/pages/reports/subscriber_journal_viewlist.xhtml?faces-redirect=true";
			}
		}
		return null;
	}
	/**
	 * loadCalenderInfo
	 */
	public void loadJournalInfo(ComponentSystemEvent event)
	{
		try {
			if (!FacesContext.getCurrentInstance().isPostback()) 
			{
				setJournalDetails();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setJournalDetails(){
		calendarViewReportDetails = new ArrayList();
		long deCompanyId =0;
		long deJournalId =0;
		// get selected company id
		Object itemObj = sessionManager.getSessionAttribute(SessionManager.REPORTCURRENTITEMID);
		String companyId = itemObj!=null?(String)itemObj:null;
		if(companyId != null && !companyId.isEmpty() ){
			deCompanyId =Long.valueOf(companyId);
		}
		// get selected company id
		Object itemObj1 = sessionManager.getSessionAttribute(SessionManager.REPORTCURRENTJOURNALID);
		String journalId = itemObj1!=null?(String)itemObj1:null;
		if(journalId != null && !journalId.isEmpty() ){
			deJournalId =Long.valueOf(journalId);
		}

		SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
		if(subscriberUser!=null){
			calendarViewReportDetails = getUserService().getJournalViewReportDetails(subscriberUser.getSubscriber().getId(),deCompanyId,deJournalId);
		}
		Collections.reverse(calendarViewReportDetails); 
		createReportFields(calendarViewReportDetails);

	}
	/**
	 * used to download a pdf file
	 */
	public String downloadPDF(){
		String extension = null;
		try {
			SubscriberUser subscriberUser = (SubscriberUser) sessionManager.getSessionAttribute(SessionManager.LOGINSUBSCRIBERUSER);
			if(subscriberUser!=null){
				String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
				String filename="ReportOn"+today+".pdf";
				FacesUtils facesUtils = new FacesUtils();
				HttpServletResponse response = facesUtils.getResponse();
				String type = "PDF";
				response.setContentType(type);
				response.setHeader("Content-disposition", "attachment; filename=" + filename);
				OutputStream out = response.getOutputStream();
				FileInputStream in = new FileInputStream(CommonProperties.getBasePath()+CommonProperties.getImageContextPath()+CommonProperties.getReportPath()+subscriberUser.getSubscriber().getId()+"/"+filename);
				byte[] buffer = new byte[4096];
				int length;
				while ((length = in.read(buffer)) > 0){
					out.write(buffer, 0, length);
				}
				in.close();
				out.flush();
				facesUtils.getContext().responseComplete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * set scrollerAction pagination
	 * @return null
	 */
	public void scrollerAction(ActionEvent event)
	{
		ScrollerActionEvent scrollerEvent = (ScrollerActionEvent) event;
		FacesContext.getCurrentInstance().getExternalContext().log(
				"scrollerAction: facet: "
						+ scrollerEvent.getScrollerfacet()
						+ ", pageindex: "
						+ scrollerEvent.getPageIndex());
	}
	/**
	 * 
	 * @param event
	 */
	public void localeChanged(ValueChangeEvent event)	
	{ 
		String rowsPerPage = event.getNewValue().toString();
		sessionManager.setSessionAttributeInSession(SessionManager.ROWSPERPAGE, rowsPerPage);
	}
	/**
	 * remove all repeated item in list
	 * @param selectionList
	 * @param selectItem1
	 * @return
	 */
	private boolean removeDummy(List<SelectItem> selectionList,SelectItem selectItem1) {
		if(selectionList.isEmpty()){
			return true;	
		} else {			
			for (SelectItem selectItem : selectionList) {
				if(selectItem.getLabel().equals(selectItem1.getLabel())){
					return false;
				}
			}
		}
		return true;
	}
}
