package com.obs.brs.email;


import javax.mail.PasswordAuthentication;


public class SMTPAuthenticator extends javax.mail.Authenticator {
	private String fUser;
	private String fPassword;

	public SMTPAuthenticator(String user, String password) {
		fUser = user;
		fPassword = password;
	}
    // get PasswordAuthentication
	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(fUser, fPassword);
	}
} 
