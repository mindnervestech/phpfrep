package com.obs.brs.utils;


	import java.net.Authenticator;
	import java.net.PasswordAuthentication;

	public class HTTPAuthenticator 
	   extends Authenticator
	{
	   private String username,
	                  password;
	                     
	   public HTTPAuthenticator(String username,String password)
	   {
	      this.username = username == null ? "" : username;
	      this.password = password == null ? "" : password;
	   }
	   
	   @Override
	protected PasswordAuthentication getPasswordAuthentication()
	   {
	      return new PasswordAuthentication(
	             username,password.toCharArray());
	   }
	}


