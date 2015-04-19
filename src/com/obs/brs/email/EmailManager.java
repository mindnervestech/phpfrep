package com.obs.brs.email;

import org.apache.log4j.Logger;

import com.obs.brs.utils.CommonProperties;


public class EmailManager {

	//Logger Instantiated for Log Management
	private static final Logger log					= Logger.getLogger(EmailManager.class );

	protected static EmailConfiguration emailConfig = new EmailConfiguration();

	static Thread emailThread;
    /**
     * 
     * @param emailAddress
     * @param firstname
     * @param resetlink
     * @return
     */
	public static boolean forgotPwd(String emailAddress,String firstname, String resetlink) {
		final String emailAddressFinal = emailAddress;
		final String firstnameFinal = firstname;
		final String resetlinkFinal = resetlink;
		emailThread = new Thread(){
			@Override
			public void run() {
				try {
					EmailConfiguration emailConfig = new EmailConfiguration();
					EmailSender sender = new EmailSender();
					String URL 	= CommonProperties.getBaseURL() + CommonProperties.getContextPath();
					String logoURL 	= null;//CommonProperties.getBaseURL() +"/"+CommonProperties.getlogoURL();
					String message = emailConfig.getForgotPasswordEmailMessage(emailAddressFinal,firstnameFinal,resetlinkFinal,URL,logoURL);
					sender.sendFromCustomerService(emailAddressFinal, emailConfig.getForgotPasswordSubject(), message);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Error While sendding email at forgotPwd"+e);
				}
				finally{
					emailThread.interrupt();
					emailThread = null;
				}
			}
		};
		emailThread.start();
		return true;
	}
    /**
     * 
     * @param emailAddress
     * @param firstname
     * @param lastName
     * @return
     */
	public static boolean forgotUsername(String emailAddress, String firstname,String lastName) {
		final String emailAddressFinal = emailAddress;
		final String firstnameFinal = firstname;
		final String lastNameFinal = lastName;
		
		emailThread = new Thread(){
			@Override
			public void run() {
				try {
					EmailConfiguration emailConfig = new EmailConfiguration();
					EmailSender sender = new EmailSender();
					String URL 	= CommonProperties.getBaseURL() + CommonProperties.getContextPath();
 					String message = emailConfig.getForgotUsernameEmailMessage(firstnameFinal, lastNameFinal, URL );
					sender.sendFromCustomerService(emailAddressFinal, emailConfig.getForgotUsernameSubject(), message);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Error While sendding email at forgot Username : "+e);
				}
				finally{
					emailThread.interrupt();
					emailThread = null;
				}
			}
		};
		emailThread.start();
		return true;
	}
	/**
	 * 
	 * @param orgName
	 * @param directorName
	 * @param toEmailAddress
	 * @param ccEmailAddress
	 * @return
	 */
	public static boolean mailToDirectorOfOrganization(String orgName, String directorName, String toEmailAddress, String ccEmailAddress) {
		final String toEmailAddressFinal = toEmailAddress;
		final String ccEmailAddressFinal = ccEmailAddress;
		final String directorNameFinal = directorName;
		final String orgNameFinal = orgName;
		
		emailThread = new Thread(){
			@Override
			public void run() {
				try {
					EmailConfiguration emailConfig = new EmailConfiguration();
					EmailSender sender = new EmailSender();
					String URL 	= CommonProperties.getBaseURL() + CommonProperties.getContextPath();
 					String message = emailConfig.getOrganizationEmailForDirector(orgNameFinal, directorNameFinal, URL);
					sender.sendMailWithCCOfPerson(toEmailAddressFinal, ccEmailAddressFinal,  emailConfig.getOrganizationCreatedSubject(), message);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Error While sendding email at forgot Username : "+e);
				}
				finally{
					emailThread.interrupt();
					emailThread = null;
				}
			}
		};
		emailThread.start();
		return true;
	}
	/**
	 * 
	 * @param emailAddress
	 * @param firstname
	 * @param userName
	 * @param pwd
	 * @param user_role
	 * @param admin_email
	 * @return
	 */
	public static boolean newUser(String emailAddress, String firstname, String userName, String pwd, String user_role,String admin_email ) {
		final String emailAddressFinal = emailAddress;
		final String firstnameFinal = firstname;
		final String userNameFinal = userName;
		final String pwdFinal = pwd;
		final String user_roleFinal = user_role;
		//final String orgFinal = org;
		final String adminEmail = admin_email;
		emailThread = new Thread(){
			@Override
			public void run() {
				try {
					EmailConfiguration emailConfig = new EmailConfiguration();
					EmailSender sender = new EmailSender();
					String URL 	= CommonProperties.getBaseURL() + CommonProperties.getContextPath();
					String logoURL 	= null;//CommonProperties.getBaseURL() +"/"+CommonProperties.getlogoURL();
					String message = emailConfig.getNewUserMessage(firstnameFinal,userNameFinal,pwdFinal,user_roleFinal,URL);
					sender.sendMailWithCCOfPerson(emailAddressFinal, adminEmail, emailConfig.getNewUserSubject(), message);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Error While sendding email at forgotPwd"+e);
				}
				finally{
					emailThread.interrupt();
					emailThread = null;
				}
			}
		};
		emailThread.start();
		return true;
	}

	/**
	 * email send to variant user
	 * @param emailAddress
	 * @param firstname
	 * @param userName
	 * @param pwd
	 * @param user_role
	 * @param org
	 * @param admin_email
	 * @return
	 */
	public static boolean newVariantUser(String emailAddress, String firstname, String userName, String pwd, String user_role, String org,String admin_email ) {
		final String emailAddressFinal = emailAddress;
		final String firstnameFinal = firstname;
		final String userNameFinal = userName;
		final String pwdFinal = pwd;
		final String user_roleFinal = user_role;
		final String orgFinal = org;
		final String adminEmail = admin_email;
		emailThread = new Thread(){
			@Override
			public void run() {
				try {
					EmailConfiguration emailConfig = new EmailConfiguration();
					EmailSender sender = new EmailSender();
					String URL 	= CommonProperties.getBaseURL() + CommonProperties.getContextPath();
					String logoURL 	= null;//CommonProperties.getBaseURL() +"/"+CommonProperties.getlogoURL();
					String message = emailConfig.getNewUserVariantMessage(firstnameFinal,userNameFinal,pwdFinal,user_roleFinal,orgFinal,URL);
					sender.sendMailWithCCOfPerson(emailAddressFinal, adminEmail, emailConfig.getNewUserSubject(), message);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Error While sendding email at forgotPwd"+e);
				}
				finally{
					emailThread.interrupt();
					emailThread = null;
				}
			}
		};
		emailThread.start();
		return true;
	}

	/**
	 * sending email to the receiver address With Dynamic Subject. 
	 * @param email
	 * @param emailContent
	 * @return
	 */
	public static boolean sendEmail(String email, String emailContent, String senderName,String Subject) {
		final String emailAddressFinal = email;
		final String emailContentFinal = emailContent;
		final String senderNameFinal = senderName;
		final String senderSubjectFinal = Subject;
		emailThread = new Thread(){
			@Override
			public void run() {
				try {
					EmailSender sender = new EmailSender();
					String URL 	= CommonProperties.getBaseURL()+ CommonProperties.getContextPath();
					String logoURL 	= null;//CommonProperties.getBaseURL() +"/"+CommonProperties.getlogoURL();
					String message = emailConfig.getGeneralEmailMessage(emailContentFinal,senderNameFinal,URL,logoURL);
					sender.sendFromCustomerService(emailAddressFinal, senderSubjectFinal, message);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Error While sendding email at NotApproved"+e);
				}
				finally{
					emailThread.interrupt();
				}
			}
		};
		emailThread.start();
		return true;
	}
	/**
	 * 
	 * @param emailAddress
	 * @param receiverName
	 * @param attachmentName
	 * @param attachmentUrl
	 * @return
	 */
	public static boolean sendPdfReport(String emailAddress,String receiverName, String attachmentName, String attachmentUrl) {
		try {
			EmailSender sender = new EmailSender();
			String URL 	= CommonProperties.getBaseURL() + CommonProperties.getContextPath();
			String logoURL 	= null;//CommonProperties.getBaseURL() + CommonProperties.getContextPath()+"/"+CommonProperties.getlogoURL();
			String message = emailConfig.getPdfReportMessage(receiverName,attachmentName,URL,logoURL);
			sender.sendWithAttachement(emailAddress, emailConfig.getPdfReportSubject(), message, attachmentUrl, attachmentName );
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error While sendding email at sendPdfForOTA"+e);
			return false;
		}
	}

    /**
     * 
     * @param emailAddress
     * @param firstname
     * @param userName
     * @param user_role
     * @param admin_email
     * @return
     */
	public static boolean userByRole(String emailAddress, String firstname, String userName, String user_role,String admin_email ) {
		final String emailAddressFinal = emailAddress;
		final String firstnameFinal = firstname;
		final String userNameFinal = userName;
		final String user_roleFinal = user_role;
		//final String orgFinal = org;
		final String adminEmail = admin_email;
		emailThread = new Thread(){
			@Override
			public void run() {
				try {
					EmailConfiguration emailConfig = new EmailConfiguration();
					EmailSender sender = new EmailSender();
					String URL 	= CommonProperties.getBaseURL() + CommonProperties.getContextPath();
					String logoURL 	= null;//CommonProperties.getBaseURL() +"/"+CommonProperties.getlogoURL();
					String message = emailConfig.getUserRoleMessage(firstnameFinal,userNameFinal,user_roleFinal,URL);
					sender.sendMailWithCCOfPerson(emailAddressFinal, adminEmail, emailConfig.getUserRoleSubject(), message);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Error While sendding email at forgotPwd"+e);
				}
				finally{
					emailThread.interrupt();
					emailThread = null;
				}
			}
		};
		emailThread.start();
		return true;
	}

}

