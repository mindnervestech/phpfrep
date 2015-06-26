package com.obs.brs.servlet;
import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.obs.brs.model.User;
import com.obs.brs.session.manager.SessionManager;
import com.obs.brs.utils.CommonProperties;

public class UploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isMultipart;
	private String filePath;
	//private int maxFileSize = 5 * 1024 * 1024;
	//private int maxMemSize = 5 * 1024 * 1024;
	private File file ;
	private Boolean flag;
	public void init( ){
		// Get the file location where it would be stored.
		//flag=true;
//		filePath = CommonProperties.getBasePath()+CommonProperties.getImageContextPath()+CommonProperties.getParentImageTempPath();
		//makeFile(CommonProperties.getBasePath()+CommonProperties.getImageContextPath()+CommonProperties.getParentImageTempPath());
		
	}
	public void doPost(HttpServletRequest request, 
			HttpServletResponse response)
					throws ServletException, java.io.IOException {
		// Check that we have a file upload request
		isMultipart = ServletFileUpload.isMultipartContent(request);
		response.setContentType("text/html");
		java.io.PrintWriter out = response.getWriter( );
		if( !isMultipart ){
			System.out.println("form type mismatch");
			return;
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		//factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File(CommonProperties.getBasePath()+CommonProperties.getImageContextPath()+"/temp"));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		//upload.setSizeMax( maxFileSize );
		try{ 
			String userId=request.getParameter("userId");
			filePath = CommonProperties.getBasePath()+CommonProperties.getImageContextPath()+CommonProperties.getParentImageTempPath();
			filePath=filePath+"/"+userId;
			File file = new File(filePath);
			if(!file.exists()){				
				file.mkdirs();
			}
		/*	if(file.isDirectory()){
				File [] files = file.listFiles();
			if(files.length >0){
				for (int i = 0; i <files.length; i++){
					files[i].delete();
				}
			}
			}*/
			System.out.println("Current User Id:"+userId);
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(request);
			// Process the uploaded file items
			Iterator i = fileItems.iterator();
			System.out.println("Image Servlet upload");  
			while ( i.hasNext () ) 
			{
				FileItem fi = (FileItem)i.next();
				if ( !fi.isFormField () )	
				{
					String  extension="";
					// Get the uploaded file parameters
					String fieldName = fi.getFieldName();
					String fileName = fi.getName();
					String contentType = fi.getContentType();
					boolean isInMemory = fi.isInMemory();
					long sizeInBytes = fi.getSize();
					String[] split = fileName.split("\\.");
					extension = split[split.length - 1];
					String fileString = split[split.length - 2];
					fileString=fileString.replaceAll(" ", "_");
					fileName=fileString+"."+extension;
					// Write the file
					if( fileName.lastIndexOf("\\") >= 0 ){
						file = new File( filePath + "/"+
								fileName.substring( fileName.lastIndexOf("\\"))) ;
					}else{
						file = new File( filePath +  "/"+
								fileName.substring(fileName.lastIndexOf("\\")+1)) ;
					}
					fi.write( file ) ;
					System.out.println("Uploaded Filename: " + fileName + "<br>");
				}
				}
			out.println("</body>");
			out.println("</html>");
		}catch(Exception ex) {
			System.out.println(ex);
		}
	}
	public void doGet(HttpServletRequest request, 
			HttpServletResponse response)
					throws ServletException, java.io.IOException {

		throw new ServletException("GET method used with " +
				getClass( ).getName( )+": POST method required.");
	} 
	/**
	 * make file directory to upload photos
	 * @param path
	 */
	public void makeFile(String path)
	{
		try 
		{
			File file = new File(path);
			if(!file.exists()){
				file.mkdirs();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}